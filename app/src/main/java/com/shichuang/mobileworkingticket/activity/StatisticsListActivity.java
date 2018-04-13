package com.shichuang.mobileworkingticket.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.adapter.StatisticsListAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.PartsByProductList;
import com.shichuang.mobileworkingticket.entify.ProcessList;
import com.shichuang.mobileworkingticket.widget.DateSelectView;
import com.shichuang.mobileworkingticket.widget.SelectionConditionsTabLayout;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.open.widget.RxEmptyLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 统计列表
 * Created by Administrator on 2018/3/27.
 */

public class StatisticsListActivity extends BaseActivity {
    private SelectionConditionsTabLayout mSelectionConditionsTabLayout;
    private TextView mTvDate;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private StatisticsListAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;

    private int pageSize = 10;
    private int pageIndex = 1;
    private String productDrawingNo;

    public String startDate;
    public String endDate;


    @Override
    public int getLayoutId() {
        return R.layout.activity_statistics_list;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        productDrawingNo = getIntent().getStringExtra("productDrawingNo");
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new StatisticsListAdapter();
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

        initTodayDate();
        initWorkshopTab();
    }

    private void initTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String monthStr = "";
        if ((month + 1) < 10) {
            monthStr = "0" + (month + 1);
        } else {
            monthStr = month + "";
        }
        String dayStr = "";
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = day + "";
        }
        String dateStr = year + "-" + monthStr + "-" + dayStr;
        startDate = dateStr;
        endDate = dateStr;
        mTvDate.setText(startDate + "~" + endDate);
    }

    private void initWorkshopTab() {
        mSelectionConditionsTabLayout = (SelectionConditionsTabLayout) findViewById(R.id.selection_conditions_tab_layout);
        mSelectionConditionsTabLayout.setWidth((int) (RxScreenTool.getDisplayMetrics(mContext).widthPixels * ((float) 0.7 / 1.7)));
        final List<SelectionConditionsTabLayout.Tab> mTabsList = new ArrayList<>();
        mTabsList.add(mSelectionConditionsTabLayout.newTab().setText("阶段"));
        mSelectionConditionsTabLayout.addTabs(mTabsList);
    }


    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("workOrderNo", mAdapter.getData().get(position).getWorkOrderNo());
                bundle.putString("componentNo", mAdapter.getData().get(position).getComponent());
                bundle.putString("productDrawingNo", mAdapter.getData().get(position).getProductDrawingNo());
                RxActivityTool.skipActivity(mContext, ReportDetailsActivity.class, bundle);
            }
        });

        mSelectionConditionsTabLayout.addOnTabItemSelectedListener(new SelectionConditionsTabLayout.OnTabItemSelectedListener() {
            @Override
            public void onTabItemSelected(int tabType, int id) {

            }
        });
        findViewById(R.id.ll_date_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelectView pop = new DateSelectView(mContext);
                pop.setOnDateSetListener(new DateSelectView.OnDateSetListener() {
                    @Override
                    public void onDateSet(String startDate, String endDate) {
                        StatisticsListActivity.this.startDate = startDate;
                        StatisticsListActivity.this.endDate = endDate;
                        mTvDate.setText(startDate + "~" + endDate);
                        refresh();
                    }
                });
                pop.show(findViewById(R.id.ll_date_select));
            }
        });

    }

    @Override
    public void initData() {
        getProcessListData();
        refresh();
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
                            List<SelectionConditionsTabLayout.Tab.TabList> mProcessList = new ArrayList<>();
                            for (ProcessList.ProcessInfoRows model : mProcessInfoList) {
                                mProcessList.add(new SelectionConditionsTabLayout.Tab.TabList(model.getId(), model.getProcessName()));
                            }
                            if (mSelectionConditionsTabLayout != null && mSelectionConditionsTabLayout.getTabView(0) != null && mSelectionConditionsTabLayout.getTabView(0).getTab() != null) {
                                mSelectionConditionsTabLayout.getTabView(0).getTab().setTabList(mProcessList);
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
        getReportProcessData();
    }

    private void loadMore() {
        getReportProcessData();
    }

    private void getReportProcessData() {
        OkGo.<AMBaseDto<PartsByProductList>>get(Constants.getPartsByProductUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("start_time", startDate)
                .params("end_time", endDate)
                .params("product_drawing_no", productDrawingNo)
                .params("workshop_info_id", "")
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .execute(new NewsCallback<AMBaseDto<PartsByProductList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<PartsByProductList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<PartsByProductList>> response) {
                        if (response.body().code == 0) {
                            PartsByProductList table = response.body().data;
                            setData(table.getRows());
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
                    public void onError(Response<AMBaseDto<PartsByProductList>> response) {
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

    private void setData(List<PartsByProductList.PartsByProductModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }
}
