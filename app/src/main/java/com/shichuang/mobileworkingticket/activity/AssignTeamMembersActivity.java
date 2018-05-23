package com.shichuang.mobileworkingticket.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.adapter.AssignTeamMembersAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.ChooseTeamMember;
import com.shichuang.mobileworkingticket.entify.Empty;
import com.shichuang.mobileworkingticket.widget.RxTitleBar;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;

import java.util.List;

/**
 * 分配组员
 * Created by Administrator on 2018/3/26.
 */

public class AssignTeamMembersActivity extends BaseActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AssignTeamMembersAdapter mAdapter;

    private String ticketId;
    private int nowProcessId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_assign_team_members;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        ticketId = getIntent().getStringExtra("ticketId");
        nowProcessId = getIntent().getIntExtra("nowProcessId", 0);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new AssignTeamMembersAdapter();
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
        ((RxTitleBar) findViewById(R.id.title_bar)).setTitleBarClickListener(new RxTitleBar.TitleBarClickListener() {
            @Override
            public void onRightClick() {
                if (mAdapter != null) {
                    List<ChooseTeamMember.ChooseTeamMemberModel> mList = mAdapter.getData();
                    StringBuffer sbSelectedMember = new StringBuffer();
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).isSelect()) {
                            sbSelectedMember.append(mList.get(i).getId() + ",");
                        }
                    }
                    if (sbSelectedMember.length() > 0) {
                        // 去掉最后一个逗号
                        sbSelectedMember.deleteCharAt(sbSelectedMember.length() - 1);
                        distributionTeamMember(sbSelectedMember.toString());
                    } else {
                        showToast("请选择分配人员");
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        refresh();
    }

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        getTeamMembersData();
    }

    private void getTeamMembersData() {
        OkGo.<AMBaseDto<ChooseTeamMember>>get(Constants.chooseTeamMemberUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("ticket_ids", ticketId)
                .execute(new NewsCallback<AMBaseDto<ChooseTeamMember>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ChooseTeamMember>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ChooseTeamMember>> response) {
                        if (response.body().code == 0 && response.body().data != null && response.body().data.getMemberRows() != null) {
                            setData(response.body().data.getMemberRows());
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ChooseTeamMember>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(List<ChooseTeamMember.ChooseTeamMemberModel> memberRows) {
        mAdapter.replaceData(memberRows);
    }

    private void distributionTeamMember(String members) {
        OkGo.<AMBaseDto<Empty>>post(Constants.distributionTeamMemberUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("ticket_ids", ticketId)
                .params("user_ids", members)
                .params("now_process_id", nowProcessId)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        dismissLoading();
                        if (response.body().code == 0) {
                            setResult(RESULT_OK);
                            RxActivityTool.finish(mContext);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                        dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
