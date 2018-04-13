package com.shichuang.mobileworkingticket.activity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

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
import com.shichuang.mobileworkingticket.entify.HistoryWorkInfo;
import com.shichuang.mobileworkingticket.entify.WorkingTicketList;
import com.shichuang.mobileworkingticket.widget.RxTitleBar;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.widget.RxEmptyLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 历史工票
 * Created by Administrator on 2018/2/28.
 */

public class HistoryWorkOrderActivity extends BaseActivity {
    //private View headerView;
    private TextView mTvTotalWorkOrderCount;
    private TextView mTvCompleteCount;
    private TextView mTvTotalWorkingHours;
    private TextView mTvTimeInterval;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private WorkingTicketListAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;


    private int pageSize = 10;
    private int pageIndex = 1;
    private String startTime = "", endTime = "";
    private Calendar currentCal;

    @Override
    public int getLayoutId() {
        return R.layout.activity_history_work_order;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mTvTimeInterval = view.findViewById(R.id.tv_time_interval);
        mTvTotalWorkOrderCount = view.findViewById(R.id.tv_total_work_order_count);
        mTvCompleteCount = view.findViewById(R.id.tv_complete_count);
        mTvTotalWorkingHours = view.findViewById(R.id.tv_total_working_hours);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        //headerView = LayoutInflater.from(mContext).inflate(R.layout.layout_history_work_order_header, (ViewGroup) mRecyclerView.getParent(), false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new WorkingTicketListAdapter();
        //mAdapter.addHeaderView(headerView);
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);

        mEmptyLayout = view.findViewById(R.id.empty_layout);
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                if (status != RxEmptyLayout.NETWORK_LOADING) {
                    refresh();
                }
            }
        });
        initTimeInterval();
    }

    /**
     * 初始化时间区间
     */
    private void initTimeInterval() {
        // 当前时间
        currentCal = Calendar.getInstance();
        endTime = formatDate(currentCal.get(Calendar.YEAR), currentCal.get(Calendar.MONTH), currentCal.get(Calendar.DAY_OF_MONTH));
        // 前一个月时间
        Calendar oneMonthBefore = Calendar.getInstance();
        oneMonthBefore.setTime(new Date());
        oneMonthBefore.add(Calendar.MONTH, -1);
        startTime = formatDate(oneMonthBefore.get(Calendar.YEAR), oneMonthBefore.get(Calendar.MONTH), oneMonthBefore.get(Calendar.DAY_OF_MONTH));

        mTvTimeInterval.setText(startTime + "~" + endTime);
    }

    @Override
    public void initEvent() {
        ((RxTitleBar) findViewById(R.id.title_bar)).setTitleBarClickListener(new RxTitleBar.TitleBarClickListener() {
            @Override
            public void onRightClick() {
                RxActivityTool.skipActivity(mContext, WorkingTicketSearchActivity.class);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("ticketId", mAdapter.getData().get(position).getId());
                RxActivityTool.skipActivity(mContext, PickingTheAuditActivity.class, bundle);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        findViewById(R.id.ll_select_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStartTime();
            }
        });
    }

    @Override
    public void initData() {
        getHistoricalWorkInfoData();
        refresh();
    }

    private void getHistoricalWorkInfoData() {

        OkGo.<AMBaseDto<HistoryWorkInfo>>get(Constants.historicalWorkInfoUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("ticket_state", 2)  // 工票状态 0=待分配成员，1=生产作业，2=已完成
                .params("start_time", startTime)
                .params("end_time", endTime)
                .execute(new NewsCallback<AMBaseDto<HistoryWorkInfo>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<HistoryWorkInfo>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<HistoryWorkInfo>> response) {
                        if (response.body().code == 0 && response.body().data != null && response.body().data.getRow() != null) {
                            HistoryWorkInfo.HistoryWorkInfoModel historyWorkInfo = response.body().data.getRow();
                            mTvCompleteCount.setText(historyWorkInfo.getSumCompleteCount());
                            mTvTotalWorkingHours.setText(historyWorkInfo.getSumWorkingHours());
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<HistoryWorkInfo>> response) {
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
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .params("start_time", startTime)
                .params("end_time", endTime)
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
                            mTvTotalWorkOrderCount.setText(table.getRecordCount() + "");
                            // 判断是否有更多数据
                            if (table.getRecordCount() > 0) {
                                mEmptyLayout.hide();
                                if (mAdapter.getData().size() < table.getRecordCount()) {
                                    pageIndex++;
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

    private void selectStartTime() {
        DatePickerDialog mDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startTime = formatDate(year, month, dayOfMonth);
                mTvTimeInterval.setText(startTime + "~" + endTime);
                selectEndTime();
            }
        }, currentCal.get(Calendar.YEAR), currentCal.get(Calendar.MONTH), currentCal.get(Calendar.DAY_OF_MONTH));
        mDialog.setTitle("选择开始时间");
        if (Build.VERSION.SDK_INT >= 11) {
            mDialog.getDatePicker().setMaxDate(currentCal.getTimeInMillis());
        }
        mDialog.show();
    }

    private void selectEndTime() {
        DatePickerDialog mDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endTime = formatDate(year, month, dayOfMonth);
                mTvTimeInterval.setText(startTime + "~" + endTime);
                getHistoricalWorkInfoData();
                refresh();
            }
        }, currentCal.get(Calendar.YEAR), currentCal.get(Calendar.MONTH), currentCal.get(Calendar.DAY_OF_MONTH));
        //mDialog.setTitle("选择结束时间");
        if (Build.VERSION.SDK_INT >= 11) {
            mDialog.getDatePicker().setMaxDate(currentCal.getTimeInMillis());
        }
        mDialog.show();
    }

    private String formatDate(int year, int month, int day) {
        String yearStr = year + "";

        String monthStr = "";
        if ((month + 1) < 10) {
            monthStr = "0" + (month + 1);
        } else {
            monthStr = (month + 1) + "";
        }

        String dayStr = "";
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = day + "";
        }

        return yearStr + "-" + monthStr + "-" + dayStr;
    }
}
