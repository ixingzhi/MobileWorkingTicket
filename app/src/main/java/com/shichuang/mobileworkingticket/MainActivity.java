package com.shichuang.mobileworkingticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.shichuang.mobileworkingticket.activity.MessageListActivity;
import com.shichuang.mobileworkingticket.common.AppUpdateHelper;
import com.shichuang.mobileworkingticket.common.MessageCountHelper;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.MessageCount;
import com.shichuang.mobileworkingticket.event.MessageCountEvent;
import com.shichuang.mobileworkingticket.event.MessageEvent;
import com.shichuang.mobileworkingticket.fragment.NavFragment;
import com.shichuang.mobileworkingticket.push.MyJpushReceiver;
import com.shichuang.mobileworkingticket.widget.NavigationButton;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends BaseActivity implements NavFragment.OnTabSelectedListener {
    private NavFragment mNavBar;
    private FragmentManager mFragmentManager;
    private long mExitTime;
    private int isEnterWorkTask;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        isEnterWorkTask = getIntent().getIntExtra("isEnterWorkTask", 0);
        skipPage();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        isEnterWorkTask = getIntent().getIntExtra("isEnterWorkTask", 0);
        mFragmentManager = getSupportFragmentManager();
        mNavBar = ((NavFragment) mFragmentManager.findFragmentById(R.id.fag_nav));
        mNavBar.setup(this, mFragmentManager, R.id.main_container, this);
        EventBus.getDefault().register(mContext);
        Log.d("test", "token:" + TokenCache.getToken(mContext));
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
        AppUpdateHelper.getInstance().update(mContext);
        skipPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MessageCountHelper.getInstance().update(mContext);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            showToast("再按一次离开");
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onTabSelected(NavigationButton navigationButton) {

    }

    @Override
    public void onTabReselected(NavigationButton navigationButton) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        try {
            int tab = Integer.parseInt(event.message);
            mNavBar.setCurrentItem(tab);
        } catch (RuntimeException e) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageCountEvent event) {
        if (event != null && event.mList != null) {
            List<MessageCount.MessageCountRows> mList = event.mList;
            int unreadCount = 0;
            for (int i = 0; i < mList.size(); i++) {
                unreadCount += mList.get(i).getCount();
            }
            mNavBar.showUnreadMessageCount(unreadCount);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    private void skipPage() {
        if (isEnterWorkTask == MyJpushReceiver.IS_ENTER_WORK_TASK) {
            if (mNavBar != null) {
                mNavBar.setCurrentItem(2);
            }
            Bundle bundle = new Bundle();
            bundle.putInt("messageTypeId", 3);
            RxActivityTool.skipActivity(mContext, MessageListActivity.class, bundle);
        }
    }
}
