package com.shichuang.mobileworkingticket;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

/**
 * Created by xiedd on 2018/1/19.
 */

public class Setting {
    private static final String KEY_JPUSH_ALIAS = "jpushAlias";  // 保存极光推送别名
    private static final String KEY_ENGINE_ADDRESS = "engineAddress";  // 保存极光推送别名


    public static SharedPreferences getSettingPreferences(Context context) {
        return context.getSharedPreferences(Setting.class.getName(), Context.MODE_PRIVATE);
    }


    public static void updateJpushInfo(Context context, String alias) {
        SharedPreferences sp = getSettingPreferences(context);
        SharedPreferences.Editor editor = sp.edit().putString(KEY_JPUSH_ALIAS, alias);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static boolean hasJpushInfo(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        String alias = sp.getString(KEY_JPUSH_ALIAS, "");
        if (alias == null || "".equals(alias)) {
            return false;
        }
        return true;
    }

    /**
     * 域名保存修改
     */
    public static void updateEngineAddress(Context context, String address) {
        SharedPreferences sp = getSettingPreferences(context);
        SharedPreferences.Editor editor = sp.edit().putString(KEY_ENGINE_ADDRESS, address);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static String getEngineAddress(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        String address = sp.getString(KEY_ENGINE_ADDRESS, "");
        if (address == null || "".equals(address)) {
            return address;
        }
        return address;
    }

}
