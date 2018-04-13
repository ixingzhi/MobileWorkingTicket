package com.shichuang.mobileworkingticket.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.shichuang.mobileworkingticket.entify.Token;
import com.shichuang.mobileworkingticket.entify.User;
import com.shichuang.open.tool.RxConvertTool;


/**
 * Created by xiedd on 2017/9/28.
 */

public class UserCache {

    private static final String PREFS_NAME = "com.shichuang.mobileworkingticker.usercache";
    private static final String USER_KEY = "user_info_v1";

    public static void update(Context ctx, User.UserModel user) {
        if (user != null) {
            String userStr = RxConvertTool.toJson(user);
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(USER_KEY, userStr);
            editor.commit();
        }
    }

    public static void clear(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(USER_KEY);
        editor.commit();
    }

    public static User.UserModel user(Context ctx) {
        User.UserModel user = new User.UserModel();
        if (isUserLogin(ctx)) {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String userStr = settings.getString(USER_KEY, null);
            user = RxConvertTool.fromJson(userStr, User.UserModel.class);
        }
        return user;
    }

    public static boolean isUserLogin(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(USER_KEY, null) != null;
    }

}
