package com.shichuang.mobileworkingticket.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.adapter.PickingTheAuditDrawingAdapter;
import com.shichuang.mobileworkingticket.adapter.PickingTheAuditProjectTeamAdapter;
import com.shichuang.mobileworkingticket.adapter.PickingTheAuditWorkflowAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.Empty;
import com.shichuang.mobileworkingticket.entify.WorkingTicketDetails;
import com.shichuang.mobileworkingticket.event.MessageEvent;
import com.shichuang.mobileworkingticket.fragment.PhotoViewFragment;
import com.shichuang.mobileworkingticket.widget.RxTitleBar;
import com.shichuang.mobileworkingticket.widget.WorkingTicketAddNotesDialog;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxTimeTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.open.widget.RxEmptyLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 派工调配
 * Created by Administrator on 2018/2/28.
 */

public class WorkingTicketDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final int ASSIGN_TEAM_MEMBERS = 0x11;   // 分配人员
    private static final int START_WORK = 4; // 开始工作
    private static final int END_WORK = 5; // 结束工作
    private final int REQUEST_CODE_ASK_RUNTIME_PERMISSIONS = 124;

    private RxTitleBar mTitleBar;
    private TextView mTvWorkingTicketNo;
    private TextView mTvProcessName;
    private TextView mTvProductName;
    private TextView mTvWorkOrderNo;
    private TextView mTvComponent;
    private TextView mTvPartDrawingNo;
    private TextView mTvPartName;
    private TextView mTvBrandNo;
    private TextView mTvOverallDimensions;
    private TextView mTvSpecifications;
    private TextView mTvEachNumber;
    private TextView mTvPlanNumber;
    private TextView mTvCurrentNumber;
    private TextView mTvDistributionOfWorkshop;
    private TextView mTvPublisher;
    private TextView mTvTotalWorkingHours;  // 总工时
    private TextView mTvPrepareWorkingHours;  // 准备工时
    private TextView mTvSinglePieceWorkingHours;  // 单件工时
    private TextView mTvLastProcessName;  // 最后工序名
    private TextView mTvAllocateTime;
    private TextView mTvLeaderRemarks; // 组长备注信息
    private TextView mTvGroupRemarks; // 组员备注信息
    private TextView mTvWorkTicketRemarks; // 施工备注信息
    private TextView mTvPriority;
    private RxEmptyLayout mEmptyLayout;
    private Button mBtnWorkflow, mBtnProjectTeam;
    private RecyclerView mRecyclerViewDrawing;  // 图纸
    private PickingTheAuditDrawingAdapter mAdapterDrawing;
    private RecyclerView mRecyclerViewWorkflow;  // 工作流
    private PickingTheAuditWorkflowAdapter mAdapterWorkflow;
    private RecyclerView mRecyclerViewProjectTeam;  // 项目组
    private PickingTheAuditProjectTeamAdapter mAdapterProjectTeam;
    // 状态
    private LinearLayout mLlAuditStatus;
    private EditText mEtQualifiedCount;  // 合格数量
    private EditText mEtScrapCount;  // 报废数量
    //private EditText mEtContentDescription;  // 内容描述
    private LinearLayout mLlCauseAnalysis;
    private TextView mTvCauseAnalysis;
    private Button mBtnSubmitAudit;  // 提交审核
    private Button mBtnAssignTeamMembersStatus;
    private Button mBtnStartStatus;
    private LinearLayout mLlMaterialManagerStatus;  // 缺料，发料
    private Button mBtnLackOfMaterial, mBtnSendOfMaterial;

    private int ticketId;
    private int processState;
    private int workTicketStateId;
    // 当前操作状态
    private int operationDisplay;
    private WorkingTicketDetails data;
    private int causeAnalysisId = -1; // 审核没通过原因id
    // 查看大图片
    private PhotoViewFragment mPhotoViewFragment;
    private boolean isPhotoViewFragmentShow;
    // 添加备注Dialog
    private WorkingTicketAddNotesDialog mAddNotesDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_working_ticket_details;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        ticketId = getIntent().getIntExtra("ticketId", 0);
        processState = getIntent().getIntExtra("processState", 0);
        workTicketStateId = getIntent().getIntExtra("workTicketStateId", 0);
        mTitleBar = view.findViewById(R.id.title_bar);
        mTvWorkingTicketNo = view.findViewById(R.id.tv_working_ticket_no);
        mTvProcessName = view.findViewById(R.id.tv_process_name);
        mTvProductName = view.findViewById(R.id.tv_product_name);
        mTvWorkOrderNo = view.findViewById(R.id.tv_work_order_no);
        mTvComponent = view.findViewById(R.id.tv_component);
        mTvPartDrawingNo = view.findViewById(R.id.tv_part_drawing_no);
        mTvPartName = view.findViewById(R.id.tv_part_name);
        mTvBrandNo = view.findViewById(R.id.tv_brand_no);
        mTvOverallDimensions = view.findViewById(R.id.tv_overall_dimensions);
        mTvSpecifications = view.findViewById(R.id.tv_specifications);
        mTvEachNumber = view.findViewById(R.id.tv_each_number);
        mTvPlanNumber = view.findViewById(R.id.tv_plan_number);
        mTvCurrentNumber = view.findViewById(R.id.tv_current_number);
        mTvDistributionOfWorkshop = view.findViewById(R.id.tv_distribution_of_workshop);
        mTvPublisher = view.findViewById(R.id.tv_publisher);
        mTvTotalWorkingHours = view.findViewById(R.id.tv_total_working_hours);
        mTvPrepareWorkingHours = view.findViewById(R.id.tv_prepare_working_hours);
        mTvSinglePieceWorkingHours = view.findViewById(R.id.tv_single_piece_working_hours);
        mTvLastProcessName = view.findViewById(R.id.tv_last_process_name);
        mTvAllocateTime = view.findViewById(R.id.tv_allocate_time);
        mTvLeaderRemarks = view.findViewById(R.id.tv_leader_remarks);
        mTvGroupRemarks = view.findViewById(R.id.tv_group_remarks);
        mTvWorkTicketRemarks = view.findViewById(R.id.tv_work_ticket_remark);
        mTvPriority = view.findViewById(R.id.tv_priority);
        mBtnWorkflow = view.findViewById(R.id.btn_workflow);
        mBtnProjectTeam = view.findViewById(R.id.btn_project_team);
        mLlAuditStatus = view.findViewById(R.id.ll_audit_status);
        mEtQualifiedCount = view.findViewById(R.id.et_qualified_count);
        mEtScrapCount = view.findViewById(R.id.et_scrap_count);
        //mEtContentDescription = view.findViewById(R.id.et_content_description);
        mLlCauseAnalysis = view.findViewById(R.id.ll_cause_analysis);
        mTvCauseAnalysis = view.findViewById(R.id.tv_cause_analysis);
        mBtnSubmitAudit = view.findViewById(R.id.btn_submit_audit);
        mBtnAssignTeamMembersStatus = view.findViewById(R.id.btn_assign_team_members_status);
        mBtnStartStatus = view.findViewById(R.id.btn_start_status);
        mLlMaterialManagerStatus = view.findViewById(R.id.ll_material_manager_status);
        mBtnLackOfMaterial = view.findViewById(R.id.btn_lack_of_material);
        mBtnSendOfMaterial = view.findViewById(R.id.btn_send_of_material);

        setBarTitle();
        initDrawingRecycler();
        initWorkflowRecycler();
        initProjectTeamRecycler();

        mEmptyLayout = view.findViewById(R.id.empty_layout);
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                if (status != RxEmptyLayout.NETWORK_LOADING) {
                    refresh();
                }
            }
        });
        mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
    }

    private void setBarTitle() {
        switch (workTicketStateId) {
            case WorkingTicketListActivity.PACKING_CONFIRMATION:
                mTitleBar.setTitle("领料确认");
                break;
            case WorkingTicketListActivity.ASSIGN_TEAM_MEMBERS:
                mTitleBar.setTitle("分配组员");
                break;
            case WorkingTicketListActivity.PRODUCTION_OPERATION:
                mTitleBar.setTitle("生产作业");
                break;
            case WorkingTicketListActivity.QUALITY_INSPECTION:
                mTitleBar.setTitle("质量检查");
                break;
            default:
                mTitleBar.setTitle("工票详情");
                break;
        }

    }

    private void initDrawingRecycler() {
        mRecyclerViewDrawing = (RecyclerView) findViewById(R.id.recycler_view_drawing);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLayoutManager.setScrollEnabled(false);
        mRecyclerViewDrawing.setLayoutManager(mLayoutManager);
        mRecyclerViewDrawing.setNestedScrollingEnabled(false);
        mAdapterDrawing = new PickingTheAuditDrawingAdapter();
        mRecyclerViewDrawing.setAdapter(mAdapterDrawing);
    }


    private void initWorkflowRecycler() {
        mRecyclerViewWorkflow = (RecyclerView) findViewById(R.id.recycler_view_workflow);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(false);
        mRecyclerViewWorkflow.setLayoutManager(mLayoutManager);
        mRecyclerViewWorkflow.setNestedScrollingEnabled(false);
        mAdapterWorkflow = new PickingTheAuditWorkflowAdapter();
        mRecyclerViewWorkflow.setAdapter(mAdapterWorkflow);
    }


    private void initProjectTeamRecycler() {
        mRecyclerViewProjectTeam = (RecyclerView) findViewById(R.id.recycler_view_project_team);
        CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(mContext);
        mLayoutManager.setScrollEnabled(false);
        mRecyclerViewProjectTeam.setLayoutManager(mLayoutManager);
        mRecyclerViewProjectTeam.setNestedScrollingEnabled(false);
        mAdapterProjectTeam = new PickingTheAuditProjectTeamAdapter();
        mRecyclerViewProjectTeam.setAdapter(mAdapterProjectTeam);
    }


    @Override
    public void initEvent() {
        // 标题栏右侧
        findViewById(R.id.ll_right).setOnClickListener(this);
        findViewById(R.id.ll_leader_remarks).setOnClickListener(this);
        findViewById(R.id.ll_group_remarks).setOnClickListener(this);
        findViewById(R.id.ll_work_ticket_remark).setOnClickListener(this);
        mBtnWorkflow.setOnClickListener(this);
        mBtnProjectTeam.setOnClickListener(this);
        mBtnAssignTeamMembersStatus.setOnClickListener(this);
        mBtnStartStatus.setOnClickListener(this);
        mBtnLackOfMaterial.setOnClickListener(this);
        mBtnSendOfMaterial.setOnClickListener(this);
        mBtnSubmitAudit.setOnClickListener(this);
        mLlCauseAnalysis.setOnClickListener(this);

        // 查看大图纸
        mAdapterDrawing.setOnItemClickListener(new PickingTheAuditDrawingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String url) {
                showPhotoViewFragmentView(url);
            }
        });
        // 项目组
        mAdapterProjectTeam.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_make_phone_call) {
                    String phoneNum = mAdapterProjectTeam.getData().get(position).getPhoneNum();
                    if (!TextUtils.isEmpty(phoneNum)) {
                        call(phoneNum);
                    } else {
                        showToast("联系电话为空");
                    }
                }
            }
        });
        // 监听报废数量（如果大于0，显示原因布局）
        mEtScrapCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String countStr = s.toString().trim();
                if (!TextUtils.isEmpty(countStr)) {
                    int count = Integer.valueOf(countStr);
                    if (count > 0) {
                        mLlCauseAnalysis.setVisibility(View.VISIBLE);
                    } else {
                        mLlCauseAnalysis.setVisibility(View.GONE);
                    }
                } else {
                    mLlCauseAnalysis.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 显示大图
     */

    private void showPhotoViewFragmentView(String url) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mPhotoViewFragment == null) {
            mPhotoViewFragment = PhotoViewFragment.newInstance(url);
            ft.replace(android.R.id.content, mPhotoViewFragment);
        } else {
            mPhotoViewFragment.loadPhoto(url);
            ft.show(mPhotoViewFragment);
        }
        ft.commitAllowingStateLoss();
        isPhotoViewFragmentShow = true;
    }

    /**
     * 影藏大图显示
     */
    private void hidePhotoViewFragmentView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mPhotoViewFragment);
        ft.commitAllowingStateLoss();
        isPhotoViewFragmentShow = false;
    }

    private void call(final String phoneNum) {
        new AlertDialog.Builder(mContext)
                .setMessage("拨打 " + phoneNum + " ？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(WorkingTicketDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(WorkingTicketDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                                    REQUEST_CODE_ASK_RUNTIME_PERMISSIONS);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum.toString().trim()));
                            startActivity(intent);
                        }
                    }
                }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_ASK_RUNTIME_PERMISSIONS == requestCode) {
            // 如果获得了基本存储权限，则允许执行
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //call();
                return;
            }

            String msg = "需要拨打电话权限，否则将无法正常使用移动工票";
            AlertDialog dialog = new AlertDialog.Builder(this).setMessage(msg).setPositiveButton("去设置", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_right:
                if (mAddNotesDialog == null) {
                    mAddNotesDialog = new WorkingTicketAddNotesDialog(mContext);
                    mAddNotesDialog.setOnCompleteClickListener(new WorkingTicketAddNotesDialog.OnCompleteClickListener() {
                        @Override
                        public void complete(String remarks) {
                            addNotes(remarks);
                        }
                    });
                }
                mAddNotesDialog.show();
                break;
            case R.id.ll_leader_remarks:
                String leaderRemarks = data.getTicketRow().getLeaderRemarks();
                if (TextUtils.isEmpty(leaderRemarks)) {
                    showToast("暂无组长备注信息");
                } else {
                    showRemarksDialog(leaderRemarks);
                }
                break;
            case R.id.ll_group_remarks:
                String groupRemarks = data.getTicketRow().getGroupRemarks();
                if (TextUtils.isEmpty(groupRemarks)) {
                    showToast("暂无组员备注信息");
                } else {
                    showRemarksDialog(groupRemarks);
                }
                break;
            case R.id.ll_work_ticket_remark:
                String workTicketRemark = data.getTicketRow().getWorkTicketRemark();
                if (TextUtils.isEmpty(workTicketRemark)) {
                    showToast("暂无施工备注信息");
                } else {
                    showRemarksDialog(workTicketRemark);
                }
                break;
            case R.id.btn_workflow: // 工作流Tab
                mBtnWorkflow.setTextColor(getResources().getColor(R.color.colorPrimary));
                mBtnProjectTeam.setTextColor(getResources().getColor(R.color.black));
                mRecyclerViewWorkflow.setVisibility(View.VISIBLE);
                mRecyclerViewProjectTeam.setVisibility(View.GONE);
                break;
            case R.id.btn_project_team:  // 项目组Tab
                mBtnWorkflow.setTextColor(getResources().getColor(R.color.black));
                mBtnProjectTeam.setTextColor(getResources().getColor(R.color.colorPrimary));
                mRecyclerViewWorkflow.setVisibility(View.GONE);
                mRecyclerViewProjectTeam.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_assign_team_members_status:  // 分配工作人员
                Bundle bundle = new Bundle();
                bundle.putString("ticketId", ticketId + "");
                bundle.putInt("nowProcessId", data.getTicketRow().getNowProcessId());
                RxActivityTool.skipActivityForResult((Activity) mContext, AssignTeamMembersActivity.class, bundle, ASSIGN_TEAM_MEMBERS);
                break;
            case R.id.btn_start_status: // 开始
                String message = "";
                if (operationDisplay == START_WORK) {
                    message = "开始工作？";
                } else if (operationDisplay == END_WORK) {
                    message = "结束工作？";
                } else {
                    return;
                }
                new AlertDialog.Builder(mContext).setMessage(message)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (operationDisplay == START_WORK) {
                                    startOrEndWork(START_WORK);
                                } else if (operationDisplay == END_WORK) {
                                    startOrEndWork(END_WORK);
                                }
                            }
                        }).show();
                break;
            case R.id.btn_lack_of_material: // 缺料
                materialOperation(2);
                break;
            case R.id.btn_send_of_material: // 发料
                materialOperation(1);
                break;
            case R.id.btn_submit_audit: // 提交审核
                checkSubmitAudit();
                break;
            case R.id.ll_cause_analysis:  // 选择报错原因
                selectCauseAnalysis();
                break;
            default:
                break;
        }
    }


    @Override
    public void initData() {
        getPickingTheAuditData();
    }

    private void getPickingTheAuditData() {
        OkGo.<AMBaseDto<WorkingTicketDetails>>get(Constants.ticketDetailsUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("ticket_id", ticketId)
                .params("process_state", processState)
                .execute(new NewsCallback<AMBaseDto<WorkingTicketDetails>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<WorkingTicketDetails>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<WorkingTicketDetails>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            mEmptyLayout.hide();
                            data = response.body().data;
                            setData();
                        } else {
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<WorkingTicketDetails>> response) {
                        super.onError(response);
                        showToast("服务器返回数据异常，请稍后再试");
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void setData() {
        WorkingTicketDetails.TicketRowModel ticketData = data.getTicketRow();
        mTvWorkingTicketNo.setText("工票ID：" + ticketData.getWorkTicketNum());
        if (!TextUtils.isEmpty(ticketData.getProcessName())) {
            mTvProcessName.setVisibility(View.VISIBLE);
            mTvProcessName.setText("当前工序：" + ticketData.getProcessName());
        } else {
            mTvProcessName.setVisibility(View.GONE);
        }
        mTvProductName.setText("产品图号：" + ticketData.getProductDrawingNo());
        mTvWorkOrderNo.setText("工作令号：" + ticketData.getWorkOrderNo());
        mTvComponent.setText("部件图号：" + ticketData.getComponent());
        mTvPartDrawingNo.setText("零件图号：" + ticketData.getPartsDrawingNo());
        mTvPartName.setText("零件名称：" + ticketData.getPartsName());
        mTvBrandNo.setText("牌号：" + ticketData.getBrandNo());
        mTvOverallDimensions.setText("落料尺寸：" + ticketData.getOverallDimensions());
        mTvSpecifications.setText("规格：" + ticketData.getSpecifications());
        mTvEachNumber.setText("每台数：" + ticketData.getEachNumber());
        mTvPlanNumber.setText("计划数：" + ticketData.getPlanNumber());
        mTvCurrentNumber.setText("当前数：" + ticketData.getCompleteCount());
        mTvDistributionOfWorkshop.setText("分配车间：" + ticketData.getWorkshopName());
        mTvPublisher.setText("发布人：" + ticketData.getReleaseUsername());
        mTvTotalWorkingHours.setText("总工时：" + ticketData.getWorkingHours());
        mTvPrepareWorkingHours.setText("准备工时：" + ticketData.getPreparationHours());
        mTvSinglePieceWorkingHours.setText("单件工时：" + ticketData.getSingleHours());
        mTvLastProcessName.setText("发运工序：" + ticketData.getLastProcess());
        String leaderRemarks = ticketData.getLeaderRemarks();
        mTvLeaderRemarks.setText(leaderRemarks.replace("&","   "));
        String groupRemarks = ticketData.getGroupRemarks();
        mTvGroupRemarks.setText(groupRemarks.replace("&","   "));
        String workTicketRemark = ticketData.getWorkTicketRemark();
        mTvWorkTicketRemarks.setText(workTicketRemark.replace("&","   "));
        mTvAllocateTime.setText("分配时间：" + RxTimeTool.stringFormat(ticketData.getAddTime()));
        // 优先级
        if (ticketData.getPriority() == 1) {
            mTvPriority.setText("优先级：紧急");
        } else if (ticketData.getPriority() == 2) {
            mTvPriority.setText("优先级：普通");
        }
        // 绑定图纸信息
        String drawingUrls = ticketData.getReferenceDrawings();
        if (drawingUrls != null && drawingUrls.length() > 0) {
            String[] urls = drawingUrls.split(",");
            mAdapterDrawing.clear();
            if (urls.length > 0) {
                mAdapterDrawing.addData(Arrays.asList(urls));
            }
        } else {
            mAdapterDrawing.clear();
        }
        // 初始化Tab选择
        mBtnWorkflow.setTextColor(getResources().getColor(R.color.colorPrimary));
        mBtnProjectTeam.setTextColor(getResources().getColor(R.color.black));
        mRecyclerViewWorkflow.setVisibility(View.VISIBLE);
        mRecyclerViewProjectTeam.setVisibility(View.GONE);
        // 绑定工作流
        if (data.getWorkFlowModels() != null) {
            mAdapterWorkflow.replaceData(data.getWorkFlowModels());
        } else {
            mAdapterWorkflow.setNewData(null);
        }
        // 绑定项目组
        if (data.getMemberRos() != null) {
            mAdapterProjectTeam.replaceData(data.getMemberRos());
        } else {
            mAdapterProjectTeam.setNewData(null);
        }
        // 判断当前状态  操作显示 2=发料 3=分配 4=生产作业未开始 5=生产作业进行中 6=质量检查
        operationDisplay = ticketData.getOperationDisplay();
        switch (ticketData.getOperationDisplay()) {
            case 2:
                mLlAuditStatus.setVisibility(View.GONE);
                mBtnAssignTeamMembersStatus.setVisibility(View.GONE);
                mBtnStartStatus.setVisibility(View.GONE);
                mLlMaterialManagerStatus.setVisibility(View.VISIBLE);
                break;
            case 3:
                mLlAuditStatus.setVisibility(View.GONE);
                mBtnAssignTeamMembersStatus.setVisibility(View.VISIBLE);
                mBtnStartStatus.setVisibility(View.GONE);
                mLlMaterialManagerStatus.setVisibility(View.GONE);
                break;
            case 4:
                mLlAuditStatus.setVisibility(View.GONE);
                mBtnAssignTeamMembersStatus.setVisibility(View.GONE);
                mBtnStartStatus.setVisibility(View.VISIBLE);
                mBtnStartStatus.setText("开始");
                mLlMaterialManagerStatus.setVisibility(View.GONE);
                break;
            case 5:
                mLlAuditStatus.setVisibility(View.GONE);
                mBtnAssignTeamMembersStatus.setVisibility(View.GONE);
                mBtnStartStatus.setVisibility(View.VISIBLE);
                mBtnStartStatus.setText("结束");
                mLlMaterialManagerStatus.setVisibility(View.GONE);
                break;
            case 6:
                mLlAuditStatus.setVisibility(View.VISIBLE);
                mBtnAssignTeamMembersStatus.setVisibility(View.GONE);
                mBtnStartStatus.setVisibility(View.GONE);
                mLlMaterialManagerStatus.setVisibility(View.GONE);
                break;
            default:
                mLlAuditStatus.setVisibility(View.GONE);
                mBtnAssignTeamMembersStatus.setVisibility(View.GONE);
                mBtnStartStatus.setVisibility(View.GONE);
                mLlMaterialManagerStatus.setVisibility(View.GONE);
                break;
        }

    }

    private void selectCauseAnalysis() {
        if (data != null && data.getCauseAnalysisModels() != null) {
            List<String> list = new ArrayList<>();
            for (WorkingTicketDetails.CauseAnalysisModel model : data.getCauseAnalysisModels()) {
                list.add(model.getCauseContent());
            }
            String[] items = list.toArray(new String[list.size()]);
            new AlertDialog.Builder(mContext).setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    causeAnalysisId = data.getCauseAnalysisModels().get(which).getId();
                    mTvCauseAnalysis.setText(data.getCauseAnalysisModels().get(which).getCauseContent());
                }
            }).show();
        }
    }

    /**
     * 缺料，领料 操作
     */
    private void materialOperation(int operationType) {
        OkGo.<AMBaseDto<Empty>>post(Constants.receiveMaterialUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("ticket_id", ticketId)
                .params("operation_val", operationType)  // 1=发料 2=缺料
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            refresh();
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    /**
     * 开始或结束工作
     */

    private void startOrEndWork(int operationType) {
        String url = "";
        if (operationType == START_WORK) {
            url = Constants.startWorkUrl;
        } else {
            url = Constants.endWorkUrl;
        }
        OkGo.<AMBaseDto<Empty>>post(url)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("ticket_id", ticketId)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            refresh();
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    /**
     * 提交审核检查
     */
    private void checkSubmitAudit() {
        String qualifiedCount = mEtQualifiedCount.getText().toString().trim();
        String scrapCount = mEtScrapCount.getText().toString().trim();
        if (TextUtils.isEmpty(qualifiedCount)) {
            showToast("请输入合格数");
            return;
        }
        if (!TextUtils.isEmpty(scrapCount) && Integer.valueOf(scrapCount) > 0) {
            if (causeAnalysisId == -1) {
                showToast("请选择废数原因");
                return;
            }
        }

        OkGo.<AMBaseDto<Empty>>post(Constants.qualityTestingUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("ticket_id", ticketId)
                .params("qualified_count", qualifiedCount)  //
                .params("scrap_count", scrapCount)  //
                .params("cause_analysis_id", causeAnalysisId)
                .params("complete_count", data.getTicketRow().getCompleteCount())  // 当前数
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            refresh();
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    /**
     * 刷新当前页面数据（刷新上个页面列表数据）
     */
    private void refresh() {
        getPickingTheAuditData();
        EventBus.getDefault().post(new MessageEvent("refreshWorkingTicketList"));
    }

    /**
     * 显示提示Dialog
     *
     * @param remarks 用&分割
     */
    private void showRemarksDialog(String remarks) {
        String[] items = remarks.split("&");
        new AlertDialog.Builder(mContext).setItems(items, null).show();
    }


    /**
     * 添加备注
     *
     * @param remarks
     */
    private void addNotes(String remarks) {
        OkGo.<AMBaseDto<Empty>>get(Constants.createRemarksUrl)
                .tag(mContext)
                .params("ticket_id", ticketId)
                .params("remarks", remarks)
                .params("token", TokenCache.getToken(mContext))
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            if (mAddNotesDialog != null) {
                                mAddNotesDialog.dismiss();
                                getPickingTheAuditData();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ASSIGN_TEAM_MEMBERS && resultCode == RESULT_OK) {
            refresh();
        }
    }

    @Override
    public void onBackPressed() {
        if (mPhotoViewFragment != null && isPhotoViewFragmentShow) {
            hidePhotoViewFragmentView();
            return;
        }
        super.onBackPressed();
    }
}
