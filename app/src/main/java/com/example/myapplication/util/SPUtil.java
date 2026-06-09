package com.example.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences 工具类（保存/读取 Token）
 */
public class SPUtil {

    private static final String SP_NAME = "newbee_mall";
    private static final String KEY_TOKEN = "token";

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 保存 Token
     */
    public static void saveToken(Context context, String token) {
        getSP(context).edit().putString(KEY_TOKEN, token).apply();
    }

    /**
     * 读取 Token
     */
    public static String getToken(Context context) {
        return getSP(context).getString(KEY_TOKEN, "");
    }

    /**
     * 清除 Token（退出登录时调用）
     */
    public static void clearToken(Context context) {
        getSP(context).edit().remove(KEY_TOKEN).apply();
    }
}
