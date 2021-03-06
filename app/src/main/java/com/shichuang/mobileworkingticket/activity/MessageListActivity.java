package com.shichuang.mobileworkingticket.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.adapter.MessageListAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.MessageList;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.widget.RxEmptyLayout;

import java.util.List;

/**
 * 消息详情
 * Created by Administrator on 2018/2/28.
 */

public class MessageListActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MessageListAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;

    private int messageTypeId;    // 1 系统消息，2 通知公告， 3 工作任务
    private int pageSize = 10;
    private int pageIndex = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_list;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        messageTypeId = getIntent().getIntExtra("messageTypeId", 1);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MessageListAdapter();
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
                refresh();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getItem(position).getIsRead() <= 0) {
                    // 如果是发运员，直接进入消息
                    if (TokenCache.getTypeWork(mContext) == 7) {
                        skipWorkingTicketDetails(mAdapter.getItem(position));
                    } else {
                        readMessage(mAdapter.getItem(position), position, true);
                    }
                    //
                } else {
//                    rotateAnim(mAdapter.getViewByPosition(position, R.id.iv_open_status), 180f);
//                    boolean openStatus = !mAdapter.getData().get(position).isOpenMessageContent();
//                    mAdapter.getData().get(position).setOpenMessageContent(openStatus);
//                    mAdapter.notifyItemChanged(position);
                    // 已读，直接进入消息详情
                    skipWorkingTicketDetails(mAdapter.getItem(position));
                }
            }
        });
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                if (messageTypeId == 3 && TokenCache.getTypeWork(mContext) == 7) {  // （工作任务）只针对发运员
                    String[] items = new String[]{"已操作"};
                    new AlertDialog.Builder(mContext).setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            readMessage(mAdapter.getItem(position), position, false);
                        }
                    }).show();
                }


                return false;
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

    private void refresh() {
        pageIndex = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        getMessageListData();
    }

    private void loadMore() {
        getMessageListData();
    }

    private void getMessageListData() {
        OkGo.<AMBaseDto<MessageList>>get(Constants.getMessageListUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("message_type_id", messageTypeId)
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .execute(new NewsCallback<AMBaseDto<MessageList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<MessageList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<MessageList>> response) {
                        if (response.body().code == 0) {
                            MessageList table = response.body().data;
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
                    public void onError(Response<AMBaseDto<MessageList>> response) {
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

    private void setData(List<MessageList.MessageListModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }


    private void readMessage(final MessageList.MessageListModel data, final int position, final boolean isEnterDetails) {
        OkGo.<AMBaseDto<MessageList>>get(Constants.messageDetailsUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("message_id", data.getId())
                .execute(new NewsCallback<AMBaseDto<MessageList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<MessageList>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<MessageList>> response) {
                        dismissLoading();
                        if (response.body().code == 0) {
                            showToast("更新消息状态成功");
                            if (mAdapter != null) {
                                rotateAnim(mAdapter.getViewByPosition(position, R.id.iv_open_status), 180f);
                                mAdapter.getData().get(position).setIsRead(1);
                                boolean openStatus = !mAdapter.getData().get(position).isOpenMessageContent();
                                mAdapter.getData().get(position).setOpenMessageContent(openStatus);
                                mAdapter.notifyItemChanged(position);
                                if (isEnterDetails) {
                                    skipWorkingTicketDetails(data);
                                }
                            }
                        } else {
                            showToast("更新消息状态失败");
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<MessageList>> response) {
                        super.onError(response);
                        dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void skipWorkingTicketDetails(MessageList.MessageListModel data) {
        Bundle bundle = new Bundle();
        bundle.putInt("ticketId", data.getTicketId());
        bundle.putInt("processState", data.getProcessState());
        RxActivityTool.skipActivity(mContext, WorkingTicketDetailsActivity.class, bundle);
    }

    private void rotateAnim(View view, float rotate) {
        Animation anim = new RotateAnimation(0f, rotate, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(400); // 设置动画时间
        //anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        view.startAnimation(anim);
    }

}
