package com.shichuang.mobileworkingticket.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.adapter.MyFragmentPagerAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.Workshop;
import com.shichuang.mobileworkingticket.fragment.ReportProcessFragment;
import com.shichuang.mobileworkingticket.fragment.ReportProductFragment;
import com.shichuang.mobileworkingticket.fragment.ReportRejectsFragment;
import com.shichuang.mobileworkingticket.widget.DateSelectView;
import com.shichuang.mobileworkingticket.widget.SelectionConditionsTabLayout;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxScreenTool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 报表列表
 * Created by Administrator on 2018/2/28.
 */

public class ReportListActivity extends BaseActivity implements View.OnClickListener {
    private SelectionConditionsTabLayout mSelectionConditionsTabLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView mTvDate;

    private ReportProductFragment mReportProductFragment;
    private ReportProcessFragment mReportProcessFragment;
    private ReportRejectsFragment mReportRejectsFragment;

    public String startDate;
    public String endDate;
    public int workshopId = -1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_report_list;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mTvDate = (TextView) findViewById(R.id.tv_date);
        initTodayDate();
        initWorkshopTab();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        initViewPager();
        mTabLayout.removeAllTabs();
        mTabLayout.addTab(mTabLayout.newTab().setText("产品"));
        mTabLayout.addTab(mTabLayout.newTab().setText("工序"));
        mTabLayout.addTab(mTabLayout.newTab().setText("不良品"));
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
        startDate = year + "-" + monthStr + "-" + "01";
        endDate = year + "-" + monthStr + "-" + dayStr;
        mTvDate.setText(startDate + "~" + endDate);
    }

    private void initWorkshopTab() {
        mSelectionConditionsTabLayout = (SelectionConditionsTabLayout) findViewById(R.id.selection_conditions_tab_layout);
        mSelectionConditionsTabLayout.setWidth((int) (RxScreenTool.getDisplayMetrics(mContext).widthPixels * ((float) 0.7 / 1.7)));
        final List<SelectionConditionsTabLayout.Tab> mTabsList = new ArrayList<>();
        mTabsList.add(mSelectionConditionsTabLayout.newTab().setText("车间"));
        mSelectionConditionsTabLayout.addTabs(mTabsList);
    }

    private void initViewPager() {
        mReportProductFragment = ReportProductFragment.newInstance();
        mReportProcessFragment = ReportProcessFragment.newInstance();
        mReportRejectsFragment = ReportRejectsFragment.newInstance();
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(mReportProductFragment);
        mFragmentList.add(mReportProcessFragment);
        mFragmentList.add(mReportRejectsFragment);

        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_left).setOnClickListener(this);
        findViewById(R.id.ll_date_select).setOnClickListener(this);
        //findViewById(R.id.ll_workshop_select).setOnClickListener(this);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mSelectionConditionsTabLayout.addOnTabItemSelectedListener(new SelectionConditionsTabLayout.OnTabItemSelectedListener() {
            @Override
            public void onTabItemSelected(int tabType, int id) {
                workshopId = id;
                refreshData();
            }
        });
    }

    @Override
    public void initData() {
        getWorkshopData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                RxActivityTool.finish(mContext);
                break;
            case R.id.ll_date_select:
                DateSelectView pop = new DateSelectView(mContext);
                pop.setOnDateSetListener(new DateSelectView.OnDateSetListener() {
                    @Override
                    public void onDateSet(String startDate, String endDate) {
                        ReportListActivity.this.startDate = startDate;
                        ReportListActivity.this.endDate = endDate;
                        mTvDate.setText(startDate + "~" + endDate);
                        refreshData();
                    }
                });
                pop.show(findViewById(R.id.ll_date_select));
                break;
//            case R.id.ll_workshop_select:
//
//                break;

            default:
                break;
        }
    }

    private void refreshData() {
        if (mReportProductFragment != null) {
            mReportProductFragment.refresh();
        }
        if (mReportProcessFragment != null) {
            mReportProcessFragment.refresh();
        }
        if (mReportRejectsFragment != null) {
            mReportRejectsFragment.refresh();
        }
    }

    /**
     * 获取车间信息
     */
    private void getWorkshopData() {
        OkGo.<AMBaseDto<Workshop>>get(Constants.getWorkshopInfoListUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .execute(new NewsCallback<AMBaseDto<Workshop>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Workshop>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Workshop>> response) {
                        if (response.body().code == 0 && response.body().data != null && response.body().data.getWorkshopInfoModels() != null) {
                            List<Workshop.WorkshopModel> mProcessInfoList = response.body().data.getWorkshopInfoModels();
                            List<SelectionConditionsTabLayout.Tab.TabList> mProcessList = new ArrayList<>();
                            for (Workshop.WorkshopModel model : mProcessInfoList) {
                                mProcessList.add(new SelectionConditionsTabLayout.Tab.TabList(model.getId(), model.getName()));
                            }
                            if (mSelectionConditionsTabLayout != null && mSelectionConditionsTabLayout.getTabView(0) != null && mSelectionConditionsTabLayout.getTabView(0).getTab() != null) {
                                mSelectionConditionsTabLayout.getTabView(0).getTab().setTabList(mProcessList);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Workshop>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

}
