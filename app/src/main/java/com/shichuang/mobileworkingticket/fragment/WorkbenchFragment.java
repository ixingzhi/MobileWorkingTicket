package com.shichuang.mobileworkingticket.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.activity.HistoryWorkOrderActivity;
import com.shichuang.mobileworkingticket.activity.PickingTheAuditActivity;
import com.shichuang.mobileworkingticket.adapter.WorkingTicketListAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.TodayStatistics;
import com.shichuang.mobileworkingticket.entify.WorkingTicketList;
import com.shichuang.mobileworkingticket.widget.WaterWaveView;
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxActivityTool;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class WorkbenchFragment extends BaseFragment {
    private LinearLayout mLlStatusSelect;
    private TextView mTvWorkingTicketStatus;
    private View headerView;
    private WaterWaveView mWaterWave;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private WorkingTicketListAdapter mAdapter;
    private TextView mTvUnfinishedCount;  // 未完成
    private TextView mTvScrapCount;  // 报废
    private TextView mTvWorkingHoursToday;  // 今日工时
    //private TabLayout mTabLayout;

    private int pageSize = 10;
    private int pageIndex = 1;
    private int completionState = 1;   // 完成情况 1=处理中 2=未处理 3=已完成


    @Override
    public int getLayoutId() {
        return R.layout.fragment_workbench;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mLlStatusSelect = view.findViewById(R.id.ll_status_select);
        mTvWorkingTicketStatus = view.findViewById(R.id.tv_working_ticket_status);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        headerView = LayoutInflater.from(mContext).inflate(R.layout.layout_workbench_header, (ViewGroup) mRecyclerView.getParent(), false);
        mWaterWave = headerView.findViewById(R.id.water_wave);
        mTvUnfinishedCount = headerView.findViewById(R.id.tv_unfinished_count);
        mTvScrapCount = headerView.findViewById(R.id.tv_scrap_count);
        mTvWorkingHoursToday = headerView.findViewById(R.id.tv_working_hours_today);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new WorkingTicketListAdapter();
        mAdapter.addHeaderView(headerView);
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);

//        mTabLayout = view.findViewById(R.id.tab_layout);
//        mTabLayout.addTab(mTabLayout.newTab().setText("处理中"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("未处理"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("已完成"));

        //mScrollableLayout.getHelper().setCurrentScrollableContainer((ScrollableHelper.ScrollableContainer) mRecyclerView);
    }

    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTodayStatisticsData();
                refresh();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
//        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                switch (tab.getPosition()) {
//                    case 0:
//                        completionState = 1;
//                        break;
//                    case 1:
//                        completionState = 2;
//                        break;
//                    case 2:
//                        completionState = 3;
//                        break;
//                }
//                refresh();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("ticketId", mAdapter.getData().get(position).getId());
                RxActivityTool.skipActivity(mContext, PickingTheAuditActivity.class, bundle);
            }
        });
        mLlStatusSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, mLlStatusSelect);
                popupMenu.getMenuInflater().inflate(R.menu.popup_completion_state, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.state_processing) {
                            completionState = 1;
                        } else if (item.getItemId() == R.id.state_untreated) {
                            completionState = 2;
                        } else if (item.getItemId() == R.id.state_Completed) {
                            completionState = 3;
                        }
                        mTvWorkingTicketStatus.setText(item.getTitle());
                        refresh();
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public void initData() {
        getTodayStatisticsData();
        refresh();
    }

    private void getTodayStatisticsData() {
        OkGo.<AMBaseDto<TodayStatistics>>post(Constants.todayStatisticsUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .execute(new NewsCallback<AMBaseDto<TodayStatistics>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<TodayStatistics>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<TodayStatistics>> response) {
                        // 第一个今日已完成 第二个未完成 第三个报废 第四个今日工时
                        if (response.body().code == 0 && response.body().data != null && response.body().data.getStatistics() != null) {
                            List<TodayStatistics.TodayStatisticsModel> list = response.body().data.getStatistics();
                            for (int i = 0; i < list.size(); i++) {
                                if (i == 0) {
                                    int progress = Integer.valueOf(list.get(i).getCount());
                                    mWaterWave.setMax(progress * 5);
                                    mWaterWave.setProgressSync(progress);
                                } else if (i == 1) {
                                    mTvUnfinishedCount.setText(list.get(i).getCount());
                                } else if (i == 2) {
                                    mTvScrapCount.setText(list.get(i).getCount());
                                } else if (i == 3) {
                                    mTvWorkingHoursToday.setText(list.get(i).getCount());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<TodayStatistics>> response) {
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
                .params("process_state", -1)
                .params("priority", -1)
                .params("process_id", -1)
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .params("completion_state", completionState)   // 完成情况 1=处理中 2=未处理 3=已完成
                .params("type", 4)  // 1=工票追溯 2=按工序工序分析 3=不良品 ，4=今日工票/工作台
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
                                //mEmptyLayout.hide();
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
                                //mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<WorkingTicketList>> response) {
                        super.onError(response);
                        //mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                        showToast("数据异常：" + response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(List<WorkingTicketList.WorkingTicketListModel> data) {
//        List<WorkingTicketList.WorkingTicketListModel> list = new ArrayList<>();
//        list.add(new WorkingTicketList.WorkingTicketListModel());
//        list.add(new WorkingTicketList.WorkingTicketListModel());
//        list.add(new WorkingTicketList.WorkingTicketListModel());
//        list.add(new WorkingTicketList.WorkingTicketListModel());
//        list.add(new WorkingTicketList.WorkingTicketListModel());
//        list.add(new WorkingTicketList.WorkingTicketListModel());
//        list.add(new WorkingTicketList.WorkingTicketListModel());
//        list.add(new WorkingTicketList.WorkingTicketListModel());
//        list.add(new WorkingTicketList.WorkingTicketListModel());
//        list.add(new WorkingTicketList.WorkingTicketListModel());

//        mAdapter.replaceData(list);

        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }
}
