package com.shichuang.mobileworkingticket.common;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.mobileworkingticket.activity.SettingActivity;
import com.shichuang.mobileworkingticket.entify.AMBaseDto;
import com.shichuang.mobileworkingticket.entify.Version;
import com.shichuang.mobileworkingticket.service.AppUpdateService;
import com.shichuang.open.tool.RxAppTool;
import com.shichuang.open.tool.RxToastTool;

/**
 * App更新工具类
 *
 * @author Administrator
 */
public class AppUpdateHelper {
    public static final int PERMISSON_REQUESTCODE = 0x16;
    private static final String FILE_NAME = "mobileworkingticket";
    private static AppUpdateHelper mInstance;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String url;


    public AppUpdateHelper() {
    }

    public static AppUpdateHelper getInstance() {
        if (mInstance == null) {
            mInstance = new AppUpdateHelper();
        }
        return mInstance;
    }

    public void update(Context context) {
        this.mContext = context;
        checkUpdate();
    }

    /**
     * 检测更新
     */
    private void checkUpdate() {
        // TODO Auto-generated method stub
        String updateUrl = Constants.appUpdateUrl;
        OkGo.<AMBaseDto<Version>>get(updateUrl)
                //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<Version>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Version>, ? extends Request> request) {
                        super.onStart(request);
                        if (mContext instanceof SettingActivity) {
                            ((SettingActivity) mContext).showLoading();
                        }
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<Version>> response) {
                        if (response.body().code == 0) {
                            // 对比版本号
                            Version version = response.body().data;
                            if (version == null)
                                return;
                            if (!("").equals(version.getAppversion())) { // 防止版本号为空
                                int systemVersion = Integer.parseInt(RxAppTool.getAppVersionName(mContext).replace(".", ""));
                                int serviceVersion = Integer.parseInt(version.getAppversion().replace(".", ""));
                                String description = version.getAppdescription();
                                Log.d("test", "systemVersion：" + systemVersion + "  serviceVersion:" + serviceVersion);
                                if (serviceVersion > systemVersion) {
                                    url = version.getAppurl();
                                    startUpdate(description);
                                } else {
                                    if (mContext instanceof SettingActivity) {
                                        RxToastTool.showShort("已是最新版");
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Version>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mContext instanceof SettingActivity) {
                            ((SettingActivity) mContext).dismissLoading();
                        }
                    }
                });
    }

    private void startUpdate(String description) {
//        UpdateDialog mDialog = new UpdateDialog(mContext);
//        mDialog.setMessage(description);
//        mDialog.setOnUpdateClickListener(new UpdateDialog.OnUpdateClickListener() {
//            @Override
//            public void onClick() {
//                // 检查是否开启写入权限
//                if (ContextCompat.checkSelfPermission(mContext, needPermissions[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(
//                        (Activity) mContext, needPermis sions[0])) {
//                    ActivityCompat.requestPermissions((Activity) mContext, needPermissions, PERMISSON_REQUESTCODE);
//                } else {
//                    startService();
//                }
//            }
//        });
//        mDialog.show();
        AlertDialog mDialog = new AlertDialog.Builder(mContext)
                //.setView(R.layout.dialog_app_update)
                .setTitle("检测到新版本，需更新")
                .setMessage(description)
                //.setNegativeButton("取消", null)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 检查是否开启写入权限
                        if (ContextCompat.checkSelfPermission(mContext, needPermissions[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(
                                (Activity) mContext, needPermissions[0])) {
                            ActivityCompat.requestPermissions((Activity) mContext, needPermissions, PERMISSON_REQUESTCODE);
                        } else {
                            startService();
                        }
                    }
                }).create();
        mDialog.show();
    }

    /**
     * 获得权限后继续下载
     */
    public void startDownOnGetPermission() {
        if (!TextUtils.isEmpty(url)) {
            startService();
        }
    }

    private void startService() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("新版本下载中...");
        //mProgressDialog.setMessage("");
        mProgressDialog.setMax(100);
        mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "后台下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mProgressDialog.show();

        // 启动Service 开始下载
        AppUpdateService.startUpdate(mContext, url, FILE_NAME, new AppUpdateService.OnProgressListener() {
            @Override
            public void onProgress(int progress) {
                if (mProgressDialog != null) {
                    mProgressDialog.setProgress(progress);
                }
            }

            @Override
            public void onSuccess(boolean isSuccess) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                // 失败提示
                if (isSuccess) {
                    RxToastTool.showShort("下载成功");
                } else {
                    RxToastTool.showShort("下载失败");
                }
            }
        });
    }

}
