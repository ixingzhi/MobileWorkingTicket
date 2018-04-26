package com.shichuang.mobileworkingticket.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.activity.HistoryWorkOrderActivity;
import com.shichuang.mobileworkingticket.activity.ReportListActivity;
import com.shichuang.mobileworkingticket.activity.WorkingTicketListActivity;
import com.shichuang.mobileworkingticket.activity.WorkingTicketSearchActivity;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.BannerAd;
import com.shichuang.mobileworkingticket.event.MessageEvent;
import com.shichuang.mobileworkingticket.tool.BannerImageLoader;
import com.shichuang.mobileworkingticket.widget.VerticalSwipeRefreshLayout;
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.base.WebPageActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxToastTool;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class HomeFragment extends BaseFragment {
    private VerticalSwipeRefreshLayout mSwipeRefreshLayout;
    private Banner mBanner;

    private List<BannerAd.AdvPicsModels> mBannerModel;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        initBanner(view);
    }

    private void initBanner(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mBanner = view.findViewById(R.id.banner);
//        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new BannerImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (mBannerModel != null) {
                    WebPageActivity.newInstance(getActivity(), mBannerModel.get(position).getAdvTitle(), mBannerModel.get(position).getLikUrl());
                }
            }
        });
    }

    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBannerData();
            }
        });

        // 领料审核
        skipWorkingTickerActivity(R.id.ll_picking_the_audit, WorkingTicketListActivity.class, WorkingTicketListActivity.PACKING_CONFIRMATION);
        // 派工调配
        skipWorkingTickerActivity(R.id.ll_dispatching_deployment, WorkingTicketListActivity.class, WorkingTicketListActivity.ASSIGN_TEAM_MEMBERS);
        // 生产作业
        skipWorkingTickerActivity(R.id.ll_production_operation, WorkingTicketListActivity.class, WorkingTicketListActivity.PRODUCTION_OPERATION);
        // 质量检查
        skipWorkingTickerActivity(R.id.ll_quality_inspection, WorkingTicketListActivity.class, WorkingTicketListActivity.QUALITY_INSPECTION);
        // 今日工票
        mContentView.findViewById(R.id.rl_today_work_ticket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("1"));
            }
        });
        // 搜索
        skipActivity(R.id.ll_search, WorkingTicketSearchActivity.class);
        // 历史工票
        skipActivity(R.id.rl_history_work_ticket, HistoryWorkOrderActivity.class);
        // 智能报表
        skipActivity(R.id.rl_intelligence_report, ReportListActivity.class);
        // 工票追溯查询
        mContentView.findViewById(R.id.rl_work_ticket_tracing_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("type", 1);   // 区分列表和追溯
//                RxActivityTool.skipActivity(mContext, WorkingTicketListActivity.class, bundle);
                RxActivityTool.skipActivity(mContext, WorkingTicketSearchActivity.class);
            }
        });
    }

    @Override
    public void initData() {
        getBannerData();
    }

    private void skipWorkingTickerActivity(int resId, final Class<?> cls, final int workStatus) {
        mContentView.findViewById(resId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("workTicketStateId", workStatus);
                RxActivityTool.skipActivity(mContext, cls, bundle);
            }
        });
    }

    private void skipActivity(int resId, final Class<?> cls) {
        mContentView.findViewById(resId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, cls);
            }
        });
    }


    private void getBannerData() {
        OkGo.<AMBaseDto<BannerAd>>get(Constants.bannerUrl)
                .tag(mContext)
                .params("show_location", 1)
                .params("token", TokenCache.getToken(mContext))
                .execute(new NewsCallback<AMBaseDto<BannerAd>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<BannerAd>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<BannerAd>> response) {
                        if (response.body().code == 0) {
                            mBannerModel = response.body().data.getAdvPicsModels();
                            if (mBannerModel != null) {
                                List<String> mListBannerPic = new ArrayList<>();
                                List<String> mListBannerTitle = new ArrayList<>();
                                for (BannerAd.AdvPicsModels model : mBannerModel) {
                                    mListBannerPic.add(Constants.MAIN_ENGINE_PIC + model.getCoverPics());
                                    mListBannerTitle.add(model.getAdvTitle());
                                }
                                mBanner.update(mListBannerPic, mListBannerTitle);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<BannerAd>> response) {
                        super.onError(response);
                        showToast("数据异常：" + response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

}
