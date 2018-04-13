package com.shichuang.mobileworkingticket.activity;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.common.JpushUtils;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.mobileworkingticket.common.UserCache;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.base.WebPageActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxAppTool;
import com.shichuang.open.tool.RxFileTool;

import java.io.File;

/**
 * Created by Administrator on 2018/3/15.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvCacheSize;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        tvCacheSize = (TextView) findViewById(R.id.tv_cache_size);
        ((TextView) findViewById(R.id.tv_version_number)).setText(getVersionNumber());
    }

    @Override
    public void initEvent() {
        findViewById(R.id.rl_wipe_cache).setOnClickListener(this);
        findViewById(R.id.rl_version_number).setOnClickListener(this);
        findViewById(R.id.rl_about_us).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
    }

    @Override
    public void initData() {
        calculateCacheSize();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_wipe_cache:
                showWipeCacheDialog();
                break;
            case R.id.rl_version_number:
                //AppUpdateUtils.getInstance().update(mContext);
                break;
            case R.id.rl_about_us:
                WebPageActivity.newInstance(mContext, "", "");
                break;
            case R.id.btn_logout:
                logout();
                break;
            default:
                break;
        }
    }

    private void showWipeCacheDialog() {
        new AlertDialog.Builder(mContext).setMessage("确定清空缓存吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cleanWipeCache();
                        tvCacheSize.setText("0KB");
                    }
                })
                .create().show();
    }

    private String getVersionNumber() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info.versionName;
    }

    /**
     * 计算缓存的大小
     */
    private void calculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = mContext.getFilesDir();
        File cacheDir = mContext.getCacheDir();

        fileSize += RxFileTool.getDirSize(filesDir);
        fileSize += RxFileTool.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (RxAppTool.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = RxFileTool
                    .getExternalCacheDir(mContext);
            fileSize += RxFileTool.getDirSize(externalCacheDir);
        }
        if (fileSize > 0)
            cacheSize = RxFileTool.formatFileSize(fileSize);
        tvCacheSize.setText(cacheSize);
    }

    /**
     * 清空缓存
     */
    private void cleanWipeCache() {
        RxFileTool.cleanInternalCache(this);
    }

    private void logout() {
        new AlertDialog.Builder(mContext).setMessage("确定退出登录？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TokenCache.clear(mContext);
                        JpushUtils.delJpushAlias(mContext);
                        UserCache.clear(mContext);
                        RxActivityTool.finishAllActivity();
                        RxActivityTool.skipActivity(mContext, LoginActivity.class);
                    }
                }).create().show();
    }
}
