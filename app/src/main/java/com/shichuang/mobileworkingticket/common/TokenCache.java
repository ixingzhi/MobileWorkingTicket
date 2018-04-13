package com.shichuang.mobileworkingticket.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.shichuang.mobileworkingticket.entify.Token;
import com.shichuang.open.tool.RxConvertTool;


/**
 * Created by xiedd on 2017/9/28.
 */

public class TokenCache {

    private static final String PREFS_NAME = "com.shichuang.mobileworkingticker.tokencache";
    private static final String USER_KEY = "token_v1";

    public static void update(Context ctx, Token user) {
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

    public static Token token(Context ctx) {
        Token user = new Token();
        if (isUserLogin(ctx)) {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String userStr = settings.getString(USER_KEY, null);
            user = RxConvertTool.fromJson(userStr, Token.class);
        }
        return user;
    }

    public static boolean isUserLogin(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(USER_KEY, null) != null;
    }

    public static String getToken(Context ctx) {
        String token = "";
        if (token(ctx) != null) {
            token = token(ctx).getToken();
        }
        return token;
    }

}
