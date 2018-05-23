package com.shichuang.mobileworkingticket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.adapter.WorkingTicketListAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.ProcessList;
import com.shichuang.mobileworkingticket.entify.WorkingTicketList;
import com.shichuang.mobileworkingticket.event.MessageEvent;
import com.shichuang.mobileworkingticket.widget.AssignTeamMembersDialog;
import com.shichuang.mobileworkingticket.widget.RxTitleBar;
import com.shichuang.mobileworkingticket.widget.SelectionConditionsTabLayout;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.widget.RxEmptyLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 工票列表
 * Created by Administrator on 2018/2/28.
 */

public class WorkingTicketListActivity extends BaseActivity {
    private static final int TAB_PRIORITY = 0x11;
    private static final int TAB_PROCESS = 0x12;
    private static final int TAB_STATUS = 0x13;
    // 工票状态
    public static final int PACKING_CONFIRMATION = 2;  // 领料确认
    public static final int ASSIGN_TEAM_MEMBERS = 3;  // 分配组员"
    public static final int PRODUCTION_OPERATION = 4; // 生产作业
    public static final int QUALITY_INSPECTION = 5; // 质量检查
    public static final int COMPLETED = 6; // 已完成（管理员拥有此权限）
    // 分配组员成功
    public static final int ASSIGN_TEAM_MEMBERS_SUCCESS = 0x14;


    private SelectionConditionsTabLayout mSelectionConditionsTabLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private WorkingTicketListAdapter mAdapter;
    // 分配组员
    private Button mBtnAssignTeamMembers;

    private RxEmptyLayout mEmptyLayout;

    private int pageSize = 10;
    private int pageIndex = 1;
    private int workTicketStateId = 0;
    private int priorityId = -1;  //优先级 1=高 2=中 3=低
    private int processId = -1;
    private int causeAnalysisId = -1;
    private int type = -1;
    // 存放工序数据
    private List<SelectionConditionsTabLayout.Tab.TabList> mProcessList;
    // 工序名称（报表-工序）
    private String processName;
    private String startDate = "";
    private String endDate = "";
    // 组员分配，工序筛选
    private int nowProcessId = -1;
    private String brandNo = "";
    private String specifications = "";
    // 标识是否处于正在筛选组员分配
    private boolean isTeamMembersAssigned;

    @Override
    public int getLayoutId() {
        return R.layout.activity_working_ticket_list;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        workTicketStateId = getIntent().getIntExtra("workTicketStateId", 0);
        processId = getIntent().getIntExtra("processId", -1);
        causeAnalysisId = getIntent().getIntExtra("causeAnalysisId", -1);
        type = getIntent().getIntExtra("type", -1);
        processName = getIntent().getStringExtra("processName");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        intTab();
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new WorkingTicketListAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
        mBtnAssignTeamMembers = (Button) findViewById(R.id.btn_assign_team_members_status);

        mEmptyLayout = view.findViewById(R.id.empty_layout);
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                if (status != RxEmptyLayout.NETWORK_LOADING) {
                    refresh();
                }
            }
        });
        EventBus.getDefault().register(this);
        isShowAssignTeamMembers();
    }

    private void intTab() {
        mSelectionConditionsTabLayout = (SelectionConditionsTabLayout) findViewById(R.id.selection_conditions_tab_layout);
        final List<SelectionConditionsTabLayout.Tab> mTabsList = new ArrayList<>();

        List<SelectionConditionsTabLayout.Tab.TabList> mPriorityList = new ArrayList<>();
        mPriorityList.add(new SelectionConditionsTabLayout.Tab.TabList(1, "紧急"));
        mPriorityList.add(new SelectionConditionsTabLayout.Tab.TabList(2, "普通"));
        mTabsList.add(mSelectionConditionsTabLayout.newTab().setText("优先级").setTabType(TAB_PRIORITY).setTabList(mPriorityList));

        mTabsList.add(mSelectionConditionsTabLayout.newTab().setText(TextUtils.isEmpty(processName) ? "阶段" : processName).setTabType(TAB_PROCESS));

        List<SelectionConditionsTabLayout.Tab.TabList> mStatusList = new ArrayList<>();  // 2=领料确认 3=分配组员，4=生产作业,5=质量检查
        mStatusList.add(new SelectionConditionsTabLayout.Tab.TabList(PACKING_CONFIRMATION, "领料确认", workTicketStateId == PACKING_CONFIRMATION));
        mStatusList.add(new SelectionConditionsTabLayout.Tab.TabList(ASSIGN_TEAM_MEMBERS, "分配组员", workTicketStateId == ASSIGN_TEAM_MEMBERS));
        mStatusList.add(new SelectionConditionsTabLayout.Tab.TabList(PRODUCTION_OPERATION, "生产作业", workTicketStateId == PRODUCTION_OPERATION));
        mStatusList.add(new SelectionConditionsTabLayout.Tab.TabList(QUALITY_INSPECTION, "质量检查", workTicketStateId == QUALITY_INSPECTION));
        // 如果是管理员，添加“已完成”
        if (TokenCache.getTypeWork(mContext) == 8) {
            mStatusList.add(new SelectionConditionsTabLayout.Tab.TabList(COMPLETED, "已完成", workTicketStateId == COMPLETED));
        }
        mTabsList.add(mSelectionConditionsTabLayout.newTab().setText("状态").setTabType(TAB_STATUS).setTabList(mStatusList));
        mSelectionConditionsTabLayout.addTabs(mTabsList);
    }

    @Override
    public void initEvent() {
        ((RxTitleBar) findViewById(R.id.title_bar)).setTitleBarClickListener(new RxTitleBar.TitleBarClickListener() {
            @Override
            public void onRightClick() {
                RxActivityTool.skipActivity(mContext, WorkingTicketSearchActivity.class);
            }
        });
        mSelectionConditionsTabLayout.addOnTabItemSelectedListener(new SelectionConditionsTabLayout.OnTabItemSelectedListener() {
            @Override
            public void onTabItemSelected(int tabType, int id) {
                if (tabType == TAB_PRIORITY) {
                    priorityId = id;
                } else if (tabType == TAB_PROCESS) {
                    processId = id;
                } else if (tabType == TAB_STATUS) {
                    workTicketStateId = id;
                    isShowAssignTeamMembers();
                }
                refresh();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("ticketId", mAdapter.getData().get(position).getId());
                bundle.putInt("processState", mAdapter.getData().get(position).getProcessState());
                bundle.putInt("workTicketStateId", workTicketStateId);
                RxActivityTool.skipActivity(mContext, WorkingTicketDetailsActivity.class, bundle);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_is_select) {
                    List<WorkingTicketList.WorkingTicketListModel> mList = mAdapter.getData();
                    WorkingTicketList.WorkingTicketListModel currentWorkingTicket = mList.get(position);
                    WorkingTicketList.WorkingTicketListModel selectedWorkingTicket = null;
                    // 判断选择的是不是相同的工序
                    boolean isFirstSelect = true;
                    for (WorkingTicketList.WorkingTicketListModel workingTicket : mList) {
                        if (workingTicket.isSelect()) {
                            selectedWorkingTicket = workingTicket;
                            isFirstSelect = false;
                        }
                    }
                    if (isFirstSelect) {
                        currentWorkingTicket.setSelect(currentWorkingTicket.isSelect() ? false : true);
                    } else {
                        // 判断当前选择的与上次选择的是否相同(胚料牌号和规格相同即可)
                        if (selectedWorkingTicket != null && currentWorkingTicket.getBrandNo().equals(selectedWorkingTicket.getBrandNo())
                                && currentWorkingTicket.getSpecifications().equals(selectedWorkingTicket.getSpecifications())) {
                            currentWorkingTicket.setSelect(currentWorkingTicket.isSelect() ? false : true);
                        } else {
                            showToast("请选择胚料牌号和规格相同的工票");
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    // 判断是否有选择的，有则设置分配组员按钮确认。
                    boolean flag = false;
                    for (WorkingTicketList.WorkingTicketListModel workingTicket : mAdapter.getData()) {
                        if (workingTicket.isSelect()) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        mBtnAssignTeamMembers.setText("确定");
                    } else {
                        mBtnAssignTeamMembers.setText("分配组员");
                    }
                }
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        // 组员分配/选择完成
        mBtnAssignTeamMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mBtnAssignTeamMembers.getText().toString();
                if ("确定".equals(text)) {
                    // 获得已选择的工票Id
                    String ids = "";
                    for (WorkingTicketList.WorkingTicketListModel workingTicket : mAdapter.getData()) {
                        if (workingTicket.isSelect()) {
                            ids += workingTicket.getId() + ",";
                        }
                    }
                    if (ids.length() > 0) {
                        // 截取最后一个逗号
                        ids = ids.substring(0, ids.length() - 1);
                        Bundle bundle = new Bundle();
                        bundle.putString("ticketId", ids);
                        bundle.putInt("nowProcessId", nowProcessId);
                        RxActivityTool.skipActivityForResult((Activity) mContext, AssignTeamMembersActivity.class, bundle, ASSIGN_TEAM_MEMBERS_SUCCESS);
                    }
                } else {
                    // 显示筛选组员的条件
                    AssignTeamMembersDialog dialog = new AssignTeamMembersDialog(mContext);
                    dialog.setProcessList(mProcessList);
                    dialog.setOnScreeningComplete(new AssignTeamMembersDialog.OnScreeningComplete() {
                        @Override
                        public void complete(String brand, String spec, int id) {
                            brandNo = brand;
                            specifications = spec;
                            nowProcessId = id;
                            // 标识为正在组员分配
                            isTeamMembersAssigned = true;
                            // 影藏条件选择框
                            mSelectionConditionsTabLayout.setVisibility(View.GONE);
                            mAdapter.setTeamMembersAssignedSelectStatus(true);
                            refresh();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void initData() {
        getProcessListData();
        refresh();
    }

    /**
     * 是否显示分配组长的按钮
     */
    private void isShowAssignTeamMembers(){
        // 如果是分配组员，则显示分配组员按钮（只有组长有权限）
        if (TokenCache.getTypeWork(mContext) == 4) {
            mBtnAssignTeamMembers.setVisibility(workTicketStateId == ASSIGN_TEAM_MEMBERS ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 工序列表
     */
    private void getProcessListData() {
        OkGo.<AMBaseDto<ProcessList>>get(Constants.getProcessListUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .execute(new NewsCallback<AMBaseDto<ProcessList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ProcessList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ProcessList>> response) {
                        if (response.body().code == 0 && response.body().data != null && response.body().data.getProcessInfoRows() != null) {
                            List<ProcessList.ProcessInfoRows> mProcessInfoList = response.body().data.getProcessInfoRows();
                            mProcessList = new ArrayList<>();
                            for (ProcessList.ProcessInfoRows model : mProcessInfoList) {
                                // 从报表-工序 页面带过来的工序名，进行匹配
                                if (processName != null && model.getProcessName().equals(processName)) {
                                    mProcessList.add(new SelectionConditionsTabLayout.Tab.TabList(model.getId(), model.getProcessName(), true));
                                } else {
                                    mProcessList.add(new SelectionConditionsTabLayout.Tab.TabList(model.getId(), model.getProcessName()));
                                }
                            }
                            if (mSelectionConditionsTabLayout != null && mSelectionConditionsTabLayout.getTabView(1) != null && mSelectionConditionsTabLayout.getTabView(1).getTab() != null) {
                                mSelectionConditionsTabLayout.getTabView(1).getTab().setTabList(mProcessList);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ProcessList>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void refresh() {
        pageIndex = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        getWorkingTicketListData();
    }

    private void loadMore() {
        getWorkingTicketListData();
    }

    private void getWorkingTicketListData() {
        OkGo.<AMBaseDto<WorkingTicketList>>get(Constants.ticketListUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("process_state", workTicketStateId)
                .params("priority", priorityId)
                .params("process_id", processId)
                .params("cause_analysis_id", causeAnalysisId)
                .params("type", type)
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .params("start_time", startDate)
                .params("end_time", endDate)
                .params("now_process_id", nowProcessId)  // 当前进行中的工序id
                .params("brand_no", brandNo)  // 坯料牌号
                .params("specifications", specifications)  // 规格
                .execute(new NewsCallback<AMBaseDto<WorkingTicketList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<WorkingTicketList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<WorkingTicketList>> response) {
                        if (response.body().code == 0) {
                            WorkingTicketList table = response.body().data;
                            setData(table.getRows());
                            // 判断是否有更多数据
                            if (table.getRecordCount() > 0) {
                                mEmptyLayout.hide();
                                if (mAdapter.getData().size() < table.getRecordCount()) {
                                    pageIndex++;
                                    mAdapter.loadMoreComplete();
                                    mAdapter.setEnableLoadMore(true);
                                } else {
                                    if (table.getRecordCount() < pageSize) {
                                        mAdapter.loadMoreEnd(true);
                                        //showToast("没有更多数据");
                                    } else {
                                        mAdapter.loadMoreEnd(false);
                                        //mAdapter.setEnableLoadMore(false);
                                    }
                                }
                            } else {
                                mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<WorkingTicketList>> response) {
                        super.onError(response);
                        showToast("服务器返回数据异常，请稍后再试");
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(List<WorkingTicketList.WorkingTicketListModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event != null && event.message.equals("refreshWorkingTicketList")) {
            refresh();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 分配组员成功
        if (requestCode == ASSIGN_TEAM_MEMBERS_SUCCESS && resultCode == RESULT_OK) {
            refresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
