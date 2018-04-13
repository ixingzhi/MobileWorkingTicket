package com.shichuang.mobileworkingticket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.widget.NavigationButton;
import com.shichuang.open.base.BaseFragment;

import java.util.List;

/**
 * Created by xiedd on 2017/12/04.
 */

public class NavFragment extends BaseFragment implements View.OnClickListener {
    private NavigationButton mNavHome;
    private NavigationButton mNavWorkbench;
    private NavigationButton mNavMessage;
    private NavigationButton mNavMine;
    private Context mContext;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private NavigationButton mCurrentNavButton;
    private OnTabSelectedListener mOnTabSelectedListener;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mNavHome = view.findViewById(R.id.nav_item_home);
        mNavWorkbench = view.findViewById(R.id.nav_item_workbench);
        mNavMessage = view.findViewById(R.id.nav_item_message);
        mNavMine = view.findViewById(R.id.nav_item_mine);

        mNavHome.init(R.drawable.tab_icon_home,
                R.string.main_tab_name_home,
                HomeFragment.class);
        mNavWorkbench.init(R.drawable.tab_icon_workbench,
                R.string.main_tab_name_workbench,
                WorkbenchFragment.class);
        mNavMessage.init(R.drawable.tab_icon_message,
                R.string.main_tab_name_message,
                MessageFragment.class);
        mNavMine.init(R.drawable.tab_icon_mine,
                R.string.main_tab_name_mine,
                MineFragment.class);
    }

    @Override
    public void initEvent() {
        mNavHome.setOnClickListener(this);
        mNavWorkbench.setOnClickListener(this);
        mNavMessage.setOnClickListener(this);
        mNavMine.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View view) {
        if (view instanceof NavigationButton) {
            NavigationButton nav = (NavigationButton) view;
            doSelect(nav);
        }
    }

    public void setup(Context context, FragmentManager fragmentManager, int contentId, OnTabSelectedListener listener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnTabSelectedListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        doSelect(mNavHome);
    }

    @SuppressWarnings("RestrictedApi")
    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0)
            return;
        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != this && fragment != null) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit)
            transaction.commitNow();
    }

    private void doSelect(NavigationButton newNavButton) {
        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {  // 第二次点击相同,则执行
                onReselect(oldNavButton);
                return;
            }
            oldNavButton.setSelected(false);
        }
        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton);
        onSelected(newNavButton);
        mCurrentNavButton = newNavButton;
    }

    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(mContext,
                        newNavButton.getClx().getName(), null);
                ft.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();
    }

    private void onSelected(NavigationButton newNavButton) {
        OnTabSelectedListener listener = mOnTabSelectedListener;
        if (listener != null) {
            listener.onTabSelected(newNavButton);
        }
    }

    private void onReselect(NavigationButton navigationButton) {
        OnTabSelectedListener listener = mOnTabSelectedListener;
        if (listener != null) {
            listener.onTabReselected(navigationButton);
        }
    }

    public interface OnTabSelectedListener {
        void onTabSelected(NavigationButton navigationButton);

        void onTabReselected(NavigationButton navigationButton);
    }

    public void setCurrentItem(int i) {
        switch (i) {
            case 0:
                doSelect(mNavHome);
                break;
            case 1:
                doSelect(mNavWorkbench);
                break;
            case 2:
                doSelect(mNavMessage);
                break;
            case 3:
                doSelect(mNavMine);
                break;
            default:
                break;
        }
    }

    public void showUnreadMessageCount(int i) {
        if (mNavMessage != null) {
            mNavMessage.showRedDot(i);
        }
    }
}
