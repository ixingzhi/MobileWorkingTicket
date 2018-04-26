package com.shichuang.mobileworkingticket.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.shichuang.mobileworkingticket.entify.WorkingTicketList;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public class WorkingTicketSearchActivity extends BaseActivity {
    private TextView mTvEmptyResult;
    private EditText mEtSearchWorkOrderNo;
    private EditText mEtSearchContent;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private WorkingTicketListAdapter mAdapter;

    private int pageSize = 10;
    private int pageIndex = 1;
    private String searchWorkOrderNoContent = "";
    private String searchContent = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_working_ticket_search;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEtSearchWorkOrderNo = view.findViewById(R.id.et_search_work_order_no);
        mEtSearchContent = view.findViewById(R.id.et_search_content);
        mTvEmptyResult = view.findViewById(R.id.tv_empty_result);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new WorkingTicketListAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
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
                RxActivityTool.skipActivity(mContext, PickingTheAuditActivity.class, bundle);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        // 工作令号搜索
        mEtSearchWorkOrderNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String context = s.toString().trim();
                if (!TextUtils.isEmpty(context)) {
                    searchWorkOrderNoContent = context;
                    refresh();
                } else {
                    searchWorkOrderNoContent = "";
                    refresh();
//                    searchWorkOrderNoContent = "";
//                    mTvEmptyResult.setVisibility(View.VISIBLE);
//                    mAdapter.setNewData(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // 内容搜索
        mEtSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String context = s.toString().trim();
                if (!TextUtils.isEmpty(context)) {
                    searchContent = context;
                    refresh();
                } else {
                    searchContent = "";
                    refresh();
//                    searchContent = "";
//                    mTvEmptyResult.setVisibility(View.VISIBLE);
//                    mAdapter.setNewData(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
//        mEtSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {   // 按下完成按钮，这里和上面imeOptions对应
//                    String context = v.getText().toString().trim();
//                    if (!TextUtils.isEmpty(context)) {
//                        searchContent = context;
//                        refresh();
//                    }else {
//                        searchContent = "";
//                        mTvEmptyResult.setVisibility(View.VISIBLE);
//                        mAdapter.setNewData(null);
//                    }
//                }
//                return false;  //返回true，保留软键盘。false，隐藏软键盘
//            }
//        });
    }

    @Override
    public void initData() {

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
                .params("work_order_no", searchWorkOrderNoContent)
                .params("queryStr", searchContent)
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
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
        mTvEmptyResult.setVisibility(data.size() > 0 ? View.GONE : View.VISIBLE);
    }
}
