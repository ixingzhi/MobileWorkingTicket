package com.shichuang.mobileworkingticket.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.Version;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;

/**
 * Created by Administrator on 2018/4/17.
 */

public class ChangePasswordActivity extends BaseActivity {
    private EditText mEtOldPassword;
    private EditText mEtNewPassword;
    private EditText mEtAgainNewPassword;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEtOldPassword = (EditText) findViewById(R.id.et_old_password);
        mEtNewPassword = (EditText) findViewById(R.id.et_new_password);
        mEtAgainNewPassword = (EditText) findViewById(R.id.et_again_new_password);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
    }

    @Override
    public void initData() {

    }

    private void checkInfo() {
        String oldPassword = mEtOldPassword.getText().toString().trim();
        String newPassword = mEtNewPassword.getText().toString().trim();
        String againNewPassword = mEtAgainNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword)) {
            showToast("请输入旧密码");
        } else if (TextUtils.isEmpty(newPassword)) {
            showToast("请输入新密码");
        } else if (TextUtils.isEmpty(againNewPassword)) {
            showToast("请再次输入新密码");
        } else if (!newPassword.equals(againNewPassword)) {
            showToast("两次新密码输入不一致");
        } else {
            changePassword(newPassword, againNewPassword);
        }
    }

    private void changePassword(String newPassword, String againNewPassword) {
        OkGo.<AMBaseDto<Version>>post("")
                //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
                .tag(mContext)
                .params("", newPassword)
                .params("", againNewPassword)
                .execute(new NewsCallback<AMBaseDto<Version>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Version>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<Version>> response) {
                        dismissLoading();
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            RxActivityTool.finish(mContext);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Version>> response) {
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
