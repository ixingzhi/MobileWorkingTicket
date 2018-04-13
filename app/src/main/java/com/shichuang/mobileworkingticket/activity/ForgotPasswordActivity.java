package com.shichuang.mobileworkingticket.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
import com.shichuang.mobileworkingticket.entify.ServicePhone;
import com.shichuang.mobileworkingticket.entify.Token;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxStatusBarTool;

/**
 * Created by Administrator on 2018/3/19.
 */

public class ForgotPasswordActivity extends BaseActivity {
    private final int REQUEST_CODE_ASK_RUNTIME_PERMISSIONS = 124;
    private String servicePhone;
    private TextView mTvServicePhone;

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBar(this, true);
        return R.layout.activity_forgot_password;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mTvServicePhone = (TextView) findViewById(R.id.tv_service_phone);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        getServicePhone();
    }

    private void getServicePhone() {
        OkGo.<AMBaseDto<ServicePhone>>get(Constants.getServicePhoneUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<ServicePhone>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ServicePhone>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ServicePhone>> response) {
                        if (response.body().code == 0) {
                            servicePhone = response.body().data.getForgetPasswordContacts();
                            mTvServicePhone.setText("客服电话：" + servicePhone);
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ServicePhone>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    public void makePhoneCall(View view) {
        if (!TextUtils.isEmpty(servicePhone)) {
            call();
        } else {
            showToast("客服电话为空");
        }
    }

    private void call() {
        if (ContextCompat.checkSelfPermission(ForgotPasswordActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ForgotPasswordActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CODE_ASK_RUNTIME_PERMISSIONS);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + servicePhone.toString().trim()));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_ASK_RUNTIME_PERMISSIONS == requestCode) {
            // 如果获得了基本存储权限，则允许执行
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call();
                return;
            }

            String msg = "需要拨打电话权限，否则将无法正常使用移动工票";
            AlertDialog dialog = new AlertDialog.Builder(this).setMessage(msg).setPositiveButton("去设置", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

}
