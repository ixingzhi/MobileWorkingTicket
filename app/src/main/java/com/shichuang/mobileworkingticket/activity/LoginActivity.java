package com.shichuang.mobileworkingticket.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.MainActivity;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.JpushUtils;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.Token;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxDataTool;
import com.shichuang.open.tool.RxStatusBarTool;

/**
 * Created by Administrator on 2018/3/19.
 */

public class LoginActivity extends BaseActivity {
    private EditText mEtUserName;
    private EditText mEtPassword;
    private Button mBtnLogin;

    private boolean needFinish;

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBar(this, true);
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEtUserName = view.findViewById(R.id.et_user_name);
        mEtPassword = view.findViewById(R.id.et_password);
        mBtnLogin = view.findViewById(R.id.btn_login);
    }

    @Override
    public void initEvent() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
        findViewById(R.id.tv_forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, ForgotPasswordActivity.class);
            }
        });
    }

    @Override
    public void initData() {

    }

    private void checkInfo() {
        String userName = mEtUserName.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();

//        RxActivityTool.skipActivity(mContext, MainActivity.class);
//        needFinish = true;

        if (TextUtils.isEmpty(userName)) {
            showToast("请输入手机号");
        } else if (!RxDataTool.isMobileNO(userName)) {
            showToast("请输入正确的手机号");
        } else if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
        } else {
            login(userName, password);
        }
    }

    private void login(String userName, String password) {

        OkGo.<AMBaseDto<Token>>post(Constants.loginUrl)
                .tag(mContext)
                .params("login_name", userName)
                .params("password", password)
                .execute(new NewsCallback<AMBaseDto<Token>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Token>, ? extends Request> request) {
                        super.onStart(request);
                        mBtnLogin.setEnabled(false);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Token>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            TokenCache.update(mContext, response.body().data);
                            JpushUtils.setJpushAlias(mContext, response.body().data.getUserId() + "");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    RxActivityTool.skipActivity(mContext, MainActivity.class);
                                    needFinish = true;
                                }
                            }, 300);
                        } else {
                            mBtnLogin.setEnabled(true);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Token>> response) {
                        super.onError(response);
                        showToast("网络错误");
                        mBtnLogin.setEnabled(true);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (needFinish) {
            finish();
        }
    }


}
