package com.shichuang.mobileworkingticket.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.adapter.ReportDetailsAdapter;
import com.shichuang.mobileworkingticket.adapter.WorkingTicketListAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.Empty;
import com.shichuang.mobileworkingticket.entify.MessageList;
import com.shichuang.mobileworkingticket.entify.SparePartsByParts;
import com.shichuang.mobileworkingticket.widget.DateSelectView;
import com.shichuang.mobileworkingticket.widget.SelectionConditionsTabLayout;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.open.widget.RxEmptyLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 报表详情
 * Created by Administrator on 2018/3/27.
 */

public class ReportDetailsActivity extends BaseActivity {
    //private SelectionConditionsTabLayout mSelectionConditionsTabLayout;
    private EditText mEtSearchPartDrawingNo;
    private TextView mTvDate;
    private View headerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ReportDetailsAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;

    private TextView mTvWorkOrderNo;
    private TextView mTvProductDrawingNo;
    private TextView mTvComponentNo;
    private TextView mTvTotalWorkOrderCount;
    private TextView mTvCompleteCount;
    private TextView mTvTotalWorkingHours;

    private int pageSize = 10;
    private int pageIndex = 1;

    public String startDate;
    public String endDate;
    private String partsDrawingNo = "";

    private String workOrderNo;
    private String componentNo;
    private String productDrawingNo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_report_details;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        workOrderNo = getIntent().getStringExtra("workOrderNo");
        componentNo = getIntent().getStringExtra("componentNo");
        productDrawingNo = getIntent().getStringExtra("productDrawingNo");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");

        mEtSearchPartDrawingNo = view.findViewById(R.id.et_search_part_drawing_no);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        headerView = LayoutInflater.from(mContext).inflate(R.layout.layout_report_details_header, (ViewGroup) mRecyclerView.getParent(), false);
        mTvWorkOrderNo = headerView.findViewById(R.id.tv_work_order_no);
        mTvProductDrawingNo = headerView.findViewById(R.id.tv_product_drawing_no);
        mTvComponentNo = headerView.findViewById(R.id.tv_component_no);
        mTvTotalWorkOrderCount = headerView.findViewById(R.id.tv_total_work_order_count);
        mTvCompleteCount = headerView.findViewById(R.id.tv_complete_count);
        mTvTotalWorkingHours = headerView.findViewById(R.id.tv_total_working_hours);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ReportDetailsAdapter();
        mAdapter.addHeaderView(headerView);
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
        // 优先使用上个页面的时间
        if (!TextUtils.isEmpty(startDate)) {
            mTvDate.setText(startDate + "~" + endDate);
        } else {
            initTodayDate();
        }
        //initWorkshopTab();

        mTvWorkOrderNo.setText("工作令号：" + workOrderNo);
        mTvProductDrawingNo.setText("产品图号：" + productDrawingNo);
        mTvComponentNo.setText("部件号：" + componentNo);
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

//    private void initWorkshopTab() {
//        mSelectionConditionsTabLayout = (SelectionConditionsTabLayout) findViewById(R.id.selection_conditions_tab_layout);
//        mSelectionConditionsTabLayout.setWidth((int) (RxScreenTool.getDisplayMetrics(mContext).widthPixels * ((float) 0.7 / 1.7)));
//        final List<SelectionConditionsTabLayout.Tab> mTabsList = new ArrayList<>();
//        mTabsList.add(mSelectionConditionsTabLayout.newTab().setText("阶段"));
//        mSelectionConditionsTabLayout.addTabs(mTabsList);
//    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_date_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelectView pop = new DateSelectView(mContext);
                pop.setOnDateSetListener(new DateSelectView.OnDateSetListener() {
                    @Override
                    public void onDateSet(String startDate, String endDate) {
                        ReportDetailsActivity.this.startDate = startDate;
                        ReportDetailsActivity.this.endDate = endDate;
                        mTvDate.setText(startDate + "~" + endDate);
                        refresh();
                    }
                });
                pop.show(findViewById(R.id.ll_date_select));
            }
        });
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

        mEtSearchPartDrawingNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String content = mEtSearchPartDrawingNo.getText().toString().trim();
                    if (!TextUtils.isEmpty(content)) {
                        partsDrawingNo = content;
                        refresh();
                    } else {
                        showToast("请输入零件图号");
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
        refresh();
    }

    private void refresh() {
        pageIndex = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        getMessageListData();
    }

    private void loadMore() {
        Log.d("test", "加载更多");
        getMessageListData();
    }

    private void getMessageListData() {
        OkGo.<AMBaseDto<SparePartsByParts>>get(Constants.getSparePartsByPartsUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("component", componentNo)
                .params("product_drawing_no", productDrawingNo)
                .params("start_time", startDate)
                .params("end_time", endDate)
                .params("workshop_info_id", "")
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .params("parts_drawing_no", partsDrawingNo)
                .params("work_order_no", workOrderNo)
                .execute(new NewsCallback<AMBaseDto<SparePartsByParts>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<SparePartsByParts>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<SparePartsByParts>> response) {
                        if (response.body().code == 0 && response.body().data != null &&
                                response.body().data.getDataRows() != null && response.body().data.getStatisticsRow() != null) {
                            SparePartsByParts.StatisticsModel statisticsModel = response.body().data.getStatisticsRow();
                            SparePartsByParts.DataRows table = response.body().data.getDataRows();

                            mTvTotalWorkOrderCount.setText(table.getRecordCount() + "");
                            mTvCompleteCount.setText(statisticsModel.getSumCompletedNumber() + "");
                            mTvTotalWorkingHours.setText(statisticsModel.getWorkingHours() + "");

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
                                        //Log.d("test", "有更多数据");
                                    } else {
                                        mAdapter.loadMoreEnd(false);
                                        //Log.d("test", "没有更多数据");
                                        //mAdapter.setEnableLoadMore(false);
                                    }
                                }
                            } else {
                                mEmptyLayout.hide();
                                //mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<SparePartsByParts>> response) {
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

    private void setData(List<SparePartsByParts.DataRows.Rows> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }
}
