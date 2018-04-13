package com.shichuang.mobileworkingticket.activity;

import android.os.Bundle;
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
import com.shichuang.mobileworkingticket.adapter.SearchContactsAdapter;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.AddressBook;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class SearchContactsActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private SearchContactsAdapter mAdapter;
    private TextView mTvEmptyResult;
    private EditText mEtSearchContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_contacts;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEtSearchContent = view.findViewById(R.id.et_search_content);
        mTvEmptyResult = view.findViewById(R.id.tv_empty_result);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SearchContactsAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mEtSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String context = s.toString().trim();
                if (!TextUtils.isEmpty(context)) {
                    getAddressBookData(context);
                } else {
                    mTvEmptyResult.setVisibility(View.VISIBLE);
                    mAdapter.setNewData(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mEtSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {   // 按下完成按钮，这里和上面imeOptions对应
                    String context = v.getText().toString().trim();
                    if (!TextUtils.isEmpty(context)) {
                        getAddressBookData(context);
                    }else {
                        mTvEmptyResult.setVisibility(View.VISIBLE);
                        mAdapter.setNewData(null);
                    }
                }
                return false;  //返回true，保留软键盘。false，隐藏软键盘
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", mAdapter.getData().get(position).getId());
                RxActivityTool.skipActivity(mContext, PersonalDetailsActivity.class, bundle);
            }
        });
    }

    @Override
    public void initData() {

    }

    private void getAddressBookData(String context) {
        OkGo.<AMBaseDto<AddressBook>>get(Constants.getDirectoriesUrl)
                .tag(mContext)
                .params("token", TokenCache.getToken(mContext))
                .params("queryStr", context)
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
                    }
                });
    }

    private void setData(List<AddressBook.AddressBookModel> mList) {
        mAdapter.replaceData(mList);
        mTvEmptyResult.setVisibility(mList.size() > 0 ? View.GONE : View.VISIBLE);
    }
}
