package com.shichuang.mobileworkingticket.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.shichuang.mobileworkingticket.R;
import com.shichuang.mobileworkingticket.common.TokenCache;
import com.shichuang.open.tool.RxActivityTool;

public class LogoutDialogActivity extends Activity {
    public static boolean isFront = false;  // 是否正在前台显示

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 去除这个Activity的标题栏
        RxActivityTool.addActivity(this);
        setContentView(R.layout.activity_logout_dialog);
        setFinishOnTouchOutside(false);  // 点击屏幕不消失

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxActivityTool.finishAllActivity();
                RxActivityTool.skipActivity(LogoutDialogActivity.this, LoginActivity.class);
            }
        });
        //退出登录
        TokenCache.clear(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        isFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isFront = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
