package com.shichuang.mobileworkingticket.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.adapter.AddressBookAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.PinYinHelper;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.AddressBook;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 通讯录
 * Created by Administrator on 2018/2/28.
 */

public class AddressBookActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AddressBookAdapter mAdapter;

    private View mHeaderView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_book;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new AddressBookAdapter(null);
        mAdapter.setPreLoadNumber(2);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.layout_address_book_header, (ViewGroup) findViewById(android.R.id.content), false);
        mAdapter.addHeaderView(mHeaderView);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAddressBookData();
            }
        });
        mHeaderView.findViewById(R.id.ll_search_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, SearchContactsActivity.class);
            }
        });
        mHeaderView.findViewById(R.id.ll_department).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, OrganizationalStructureActivity.class);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getData().get(position).getItemType() == AddressBook.AddressBookModel.USER) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", mAdapter.getData().get(position).getId());
                    RxActivityTool.skipActivity(mContext, PersonalDetailsActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public void initData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getAddressBookData();
    }

    private void getAddressBookData() {
        OkGo.<AMBaseDto<AddressBook>>get(Constants.getDirectoriesUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .execute(new NewsCallback<AMBaseDto<AddressBook>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<AddressBook>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<AddressBook>> response) {
                        if (response.body().code == 0 && response.body().data != null && response.body().data.getUserRows() != null) {
                            List<AddressBook.AddressBookModel> mList = response.body().data.getUserRows();
                            setData(mList);
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<AddressBook>> response) {
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

    /**
     * 先显示本部门，然后在显示其他人
     *
     * @param data
     */
    private void setData(List<AddressBook.AddressBookModel> data) {
        // 检索出联系人的首字母，并排序
        if (data != null && data.size() > 0) {
            disposeData(data);
        } else {
            return;
        }
        List<AddressBook.AddressBookModel> mAllList = new ArrayList<>();
        List<AddressBook.AddressBookModel> mThisDepartmentList = new ArrayList<>();
        List<AddressBook.AddressBookModel> mOtherDepartmentList = new ArrayList<>();
        for (AddressBook.AddressBookModel model : data) {
            model.setItemType(AddressBook.AddressBookModel.USER);
            if (model.getIsEqualDept() == 1) {  // 是否本部门 1=本部门 2=不是本部门
                mThisDepartmentList.add(model);
            } else {
                mOtherDepartmentList.add(model);
            }
        }

        if (mThisDepartmentList.size() > 0) {
            AddressBook.AddressBookModel model = new AddressBook.AddressBookModel();
            model.setItemType(AddressBook.AddressBookModel.LETTER);
            model.setIndex("本部门");
            mAllList.add(model);
            mAllList.addAll(mThisDepartmentList);
        }
        if (mOtherDepartmentList.size() > 0) {
            for (int i = 0; i < mOtherDepartmentList.size(); i++) {
                if (i >= 1) {
                    if (!mOtherDepartmentList.get(i).getIndex().equals(mOtherDepartmentList.get(i - 1).getIndex())) {
                        AddressBook.AddressBookModel model = new AddressBook.AddressBookModel();
                        model.setItemType(AddressBook.AddressBookModel.LETTER);
                        model.setIndex(mOtherDepartmentList.get(i).getIndex());
                        mAllList.add(model);
                    }
                    mAllList.add(mOtherDepartmentList.get(i));
                } else {
                    AddressBook.AddressBookModel model = new AddressBook.AddressBookModel();
                    model.setItemType(AddressBook.AddressBookModel.LETTER);
                    model.setIndex(mOtherDepartmentList.get(i).getIndex());
                    mAllList.add(model);
                    mAllList.add(mOtherDepartmentList.get(i));
                }
            }
        }
        mAdapter.replaceData(mAllList);
    }

    private void disposeData(List<AddressBook.AddressBookModel> data) {
        for (int i = 0; i < data.size(); i++) {
            AddressBook.AddressBookModel user = data.get(i);
            String pinYin = PinYinHelper.getFirstSpell(data.get(i).getNickName());
            if (pinYin != null && pinYin.length() > 0) {
                String index = pinYin.substring(0, 1);
                Pattern pattern = Pattern.compile("^[A-Za-z]+$");
                if (pattern.matcher(index).matches()) {
                    char c = index.charAt(0);
                    if (c >= 'a' && c <= 'z') {
                        index = index.toUpperCase();
                    }
                    user.setIndex(index);
                } else {
                    user.setIndex("#");
                }
            } else {
                user.setIndex("#");
            }
        }
        // 根据用户首字母排序
        Collections.sort(data, new Comparator<AddressBook.AddressBookModel>() {

            @Override
            public int compare(AddressBook.AddressBookModel o1, AddressBook.AddressBookModel o2) {
                return o1.getIndex().compareTo(o2.getIndex());
            }
        });
    }
}
