package com.shichuang.mobileworkingticket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.activity.MessageListActivity;
import com.shichuang.mobileworkingticket.common.MessageCountHelper;
import com.shichuang.mobileworkingticket.entify.MessageCount;
import com.shichuang.mobileworkingticket.event.MessageCountEvent;
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxActivityTool;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class MessageFragment extends BaseFragment implements View.OnClickListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvSystemMessageUnreadCount;
    private TextView mTvSystemMessageContent;
    private TextView mTvNotificationUnreadCount;
    private TextView mTvNotificationContent;
    private TextView mTvWorkTaskUnreadCount;
    private TextView mTvWorkTaskContent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mTvSystemMessageUnreadCount = view.findViewById(R.id.tv_system_message_unread_count);
        mTvSystemMessageContent = view.findViewById(R.id.tv_system_message_content);
        mTvNotificationUnreadCount = view.findViewById(R.id.tv_notification_unread_count);
        mTvNotificationContent = view.findViewById(R.id.tv_notification_content);
        mTvWorkTaskUnreadCount = view.findViewById(R.id.tv_work_task_unread_count);
        mTvWorkTaskContent = view.findViewById(R.id.tv_work_task_content);
    }

    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MessageCountHelper.getInstance().update(mContext);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mContentView.findViewById(R.id.rl_system_message).setOnClickListener(this);
        mContentView.findViewById(R.id.rl_notification).setOnClickListener(this);
        mContentView.findViewById(R.id.rl_work_task).setOnClickListener(this);

    }

    @Override
    public void initData() {
        MessageCountHelper.getInstance().update(mContext);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_system_message:
                skipMessageList(1);
                break;
            case R.id.rl_notification:
                skipMessageList(2);
                break;
            case R.id.rl_work_task:
                skipMessageList(3);
                break;
            default:
                break;
        }
    }

    private void skipMessageList(int messageTypeId) {
        Bundle bundle = new Bundle();
        bundle.putInt("messageTypeId", messageTypeId);
        RxActivityTool.skipActivity(mContext, MessageListActivity.class, bundle);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageCountEvent event) {
        if (event != null && event.mList != null) {
            List<MessageCount.MessageCountRows> mList = event.mList;
            for (int i = 0; i < mList.size(); i++) {
                if (i == 0) {
                    setUnreadMessageStatus(mTvSystemMessageUnreadCount, mList.get(i).getCount());
                    setUnreadMessageContent(i, mList.get(i).getCount());
                } else if (i == 1) {
                    setUnreadMessageStatus(mTvNotificationUnreadCount, mList.get(i).getCount());
                    setUnreadMessageContent(i, mList.get(i).getCount());
                } else if (i == 2) {
                    setUnreadMessageStatus(mTvWorkTaskUnreadCount, mList.get(i).getCount());
                    setUnreadMessageContent(i, mList.get(i).getCount());
                }
            }
        }
    }

    public void setUnreadMessageStatus(TextView textView, int unreadCount) {
        if (unreadCount <= 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            if (unreadCount > 99) {
                textView.setText(unreadCount + "+");
            } else {
                textView.setText(unreadCount + "");
            }
        }
    }


    private void setUnreadMessageContent(int position, int unreadCount) {
        switch (position) {
            case 0:
                if (unreadCount > 0) {
                    mTvSystemMessageContent.setTextColor(getResources().getColor(R.color.red));
                    mTvSystemMessageContent.setText("您有" + unreadCount + "条消息未查看");
                } else {
                    mTvSystemMessageContent.setTextColor(getResources().getColor(R.color.textColor2));
                    mTvSystemMessageContent.setText("暂无系统消息");
                }
                break;
            case 1:
                if (unreadCount > 0) {
                    mTvNotificationContent.setTextColor(getResources().getColor(R.color.red));
                    mTvNotificationContent.setText("您有" + unreadCount + "条通知公告未查看");
                } else {
                    mTvNotificationContent.setTextColor(getResources().getColor(R.color.textColor2));
                    mTvNotificationContent.setText("暂无通知公告消息");
                }
                break;
            case 2:
                if (unreadCount > 0) {
                    mTvWorkTaskContent.setTextColor(getResources().getColor(R.color.red));
                    mTvWorkTaskContent.setText("您有" + unreadCount + "条工作任务未查看");
                } else {
                    mTvWorkTaskContent.setTextColor(getResources().getColor(R.color.textColor2));
                    mTvWorkTaskContent.setText("暂无工作任务消息");
                }
                break;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
