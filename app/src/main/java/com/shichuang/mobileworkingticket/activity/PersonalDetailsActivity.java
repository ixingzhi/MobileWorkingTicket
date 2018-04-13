package com.shichuang.mobileworkingticket.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.common.Constants;
import com.shichuang.mobileworkingticket.common.NewsCallback;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.PersonalDetails;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.widget.RxEmptyLayout;

/**
 * Created by Administrator on 2018/3/19.
 */

public class PersonalDetailsActivity extends BaseActivity {
    private final int REQUEST_CODE_ASK_RUNTIME_PERMISSIONS = 124;

    private ImageView mIvAvatar;
    private TextView mTvUserName;
    private TextView mTvPositionName;
    private TextView mTvDepartmentName;
    private TextView mTvPositionName02;
    private TextView mTvMobilePhone;
    private RxEmptyLayout mEmptyLayout;

    private int userId;
    private String phoneNum;

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_details;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        userId = getIntent().getIntExtra("userId", 0);

        mIvAvatar = view.findViewById(R.id.iv_avatar);
        mTvUserName = view.findViewById(R.id.tv_user_name);
        mTvPositionName = view.findViewById(R.id.tv_position_name);
        mTvDepartmentName = view.findViewById(R.id.tv_department_name);
        mTvPositionName02 = view.findViewById(R.id.tv_position_name_02);
        mTvMobilePhone = view.findViewById(R.id.tv_mobile_phone);

        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                if (status != RxEmptyLayout.NETWORK_LOADING) {
                    getPersonalDetailsData();
                }
            }
        });
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_call_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });
    }

    @Override
    public void initData() {
        getPersonalDetailsData();
    }

    private void getPersonalDetailsData() {
        OkGo.<AMBaseDto<PersonalDetails>>get(Constants.userDetailsUrl)
                .tag(mContext)
                .params("user_id", userId)
                .params("token", TokenCache.getToken(mContext))
                .execute(new NewsCallback<AMBaseDto<PersonalDetails>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<PersonalDetails>, ? extends Request> request) {
                        super.onStart(request);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<PersonalDetails>> response) {
                        if (response.body().code == 0 && response.body().data != null && response.body().data.getUserRow() != null) {
                            mEmptyLayout.hide();
                            setData(response.body().data.getUserRow());
                        } else {
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<PersonalDetails>> response) {
                        super.onError(response);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void setData(PersonalDetails.PersonalDetailsModel userRow) {
        phoneNum = userRow.getPhoneNum();
        RxGlideTool.loadImageView(mContext, Constants.MAIN_ENGINE_PIC + userRow.getHeadPortrait(), mIvAvatar, R.drawable.ic_avatar_default);
        mTvUserName.setText(userRow.getNickName());
        mTvPositionName.setText(userRow.getPositionName());
        mTvDepartmentName.setText(userRow.getDeptName());
        mTvPositionName02.setText(userRow.getPositionName());
        mTvMobilePhone.setText(userRow.getPhoneNum());
    }

    private void call() {
        if (phoneNum != null && !phoneNum.equals("")) {
            new AlertDialog.Builder(mContext)
                    .setMessage("拨打 " + phoneNum + " ？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ContextCompat.checkSelfPermission(PersonalDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(PersonalDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                                        REQUEST_CODE_ASK_RUNTIME_PERMISSIONS);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum.toString().trim()));
                                startActivity(intent);
                            }
                        }
                    }).create().show();
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
