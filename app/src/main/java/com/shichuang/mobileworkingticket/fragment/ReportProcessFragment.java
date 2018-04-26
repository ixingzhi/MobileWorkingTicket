package com.shichuang.mobileworkingticket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.activity.ReportListActivity;
import com.shichuang.mobileworkingticket.activity.WorkingTicketListActivity;
import com.shichuang.mobileworkingticket.adapter.ReportProcessAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.ReportProcessList;
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.widget.RxEmptyLayout;

import java.util.List;

/**
 * 报表-工序
 * Created by Administrator on 2018/3/16.
 */

public class ReportProcessFragment extends BaseFragment {
    private ReportListActivity mActivity;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ReportProcessAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;

    private int pageSize = 10;
    private int pageIndex = 1;

    public static ReportProcessFragment newInstance() {
        ReportProcessFragment mReportProcessFragment = new ReportProcessFragment();
        return mReportProcessFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (ReportListActivity) context;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_report_product;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ReportProcessAdapter();
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
    }

    @Override
    public void initEvent() {
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
                bundle.putInt("processId", mAdapter.getData().get(position).getProcessId());
                bundle.putString("processName", mAdapter.getData().get(position).getProcessName());
                bundle.putInt("type", 2);  // 标识类型为 工序
                bundle.putString("startDate",mActivity.startDate);
                bundle.putString("endDate",mActivity.endDate);
                RxActivityTool.skipActivity(mContext, WorkingTicketListActivity.class, bundle);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
    }

    @Override
    public void initData() {
        refresh();
    }

    public void refresh() {
        pageIndex = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        getReportProcessData();
    }

    private void loadMore() {
        getReportProcessData();
    }

    private void getReportProcessData() {
        OkGo.<AMBaseDto<ReportProcessList>>get(Constants.getByWorkingProcedureUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("start_time", mActivity.startDate)
                .params("end_time", mActivity.endDate)
                .params("workshop_info_id", mActivity.workshopId)
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .execute(new NewsCallback<AMBaseDto<ReportProcessList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ReportProcessList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ReportProcessList>> response) {
                        if (response.body().code == 0) {
                            ReportProcessList table = response.body().data;
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
                    public void onError(Response<AMBaseDto<ReportProcessList>> response) {
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

    private void setData(List<ReportProcessList.ReportProcessModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }
}
