package com.shichuang.mobileworkingticket.activity;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.adapter.DepartmentCategoryItemViewBinder;
import com.shichuang.mobileworkingticket.adapter.DepartmentDepItemViewBinder;
import com.shichuang.mobileworkingticket.adapter.DepartmentMembersItemViewBinder;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.Category;
import com.shichuang.mobileworkingticket.entify.DepartmentList;
import com.shichuang.mobileworkingticket.widget.RxTitleBar;
import com.shichuang.open.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Administrator on 2018/3/19.
 */

public class OrganizationalStructureActivity extends BaseActivity {
    private RxTitleBar mTitleBar;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @VisibleForTesting
    private MultiTypeAdapter mAdapter;
    @VisibleForTesting
    private List<Object> items = new ArrayList<>();

    private String title;
    private int parentId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_organizational_structure;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        title = getIntent().getStringExtra("title");
        parentId = getIntent().getIntExtra("parentId", 0);
        mTitleBar = view.findViewById(R.id.title_bar);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Category.class, new DepartmentCategoryItemViewBinder());   //分类
        mAdapter.register(DepartmentList.DepartmentUser.class, new DepartmentMembersItemViewBinder());  // 部门人
        mAdapter.register(DepartmentList.Department.class, new DepartmentDepItemViewBinder());  // 部门
        mRecyclerView.setAdapter(mAdapter);

        mTitleBar.setTitle(title != null && !"".equals(title) ? title : "组织架构");
    }

    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrganizationalStructureData();
            }
        });

    }

    @Override
    public void initData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getOrganizationalStructureData();
    }

    private void getOrganizationalStructureData() {
        OkGo.<AMBaseDto<DepartmentList>>get(Constants.getDeptListUrl)
                .tag(mContext)
                .params("parent_id", parentId)
                .params("token", TokenCache.getToken(mContext))
                .execute(new NewsCallback<AMBaseDto<DepartmentList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<DepartmentList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<DepartmentList>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            setData(response.body().data);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<DepartmentList>> response) {
                        super.onError(response);
                        showToast("网络错误");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(DepartmentList data) {
        items.clear();
        // 部门人
        List<DepartmentList.DepartmentUser> mDepartmentUserList = data.userRows;
        if (mDepartmentUserList != null && mDepartmentUserList.size() > 0) {
            items.add(new Category("人员"));
            for (int i = 0; i < mDepartmentUserList.size(); i++) {
                items.add(mDepartmentUserList.get(i));
            }
        }
        // 部门
        List<DepartmentList.Department> mDepartmentList = data.deptRows;
        if (mDepartmentList != null && mDepartmentList.size() > 0) {
            items.add(new Category("部门"));
            for (int i = 0; i < mDepartmentList.size(); i++) {
                items.add(mDepartmentList.get(i));
            }
        }
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }
}
