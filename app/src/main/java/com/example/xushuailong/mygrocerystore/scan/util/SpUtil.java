package com.example.xushuailong.mygrocerystore.scan.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by yanchunfei on 10/31/16.
 */

public class SpUtil {
    private static SharedPreferences sp;

    public static void putString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        }
        return sp.getInt(key,defValue);
    }

    /**
     * 获取用户token，作为登录标识
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("UserToken", "");
    }

    public static void setToken(Context context, String token) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UserToken", token);
        editor.commit();
    }

    /**
     * 获取用户名，作为登录账号
     *
     * @param context
     * @return
     */
    public static String getAccount(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("UserAccount", "");
    }

    public static void setAccount(Context context, String account) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UserAccount", account);
        editor.commit();
    }

    /**
     * 获取用户密码，作为登录密码
     *
     * @param context
     * @return
     */
    public static String getPwd(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("UserPwd", "");
    }

    public static void setPwd(Context context, String pwd) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UserPwd", pwd);
        editor.commit();
    }

    /**
     * 获取登录店员的名称
     *
     * @param context
     * @return
     */
    public static String getUserName(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("UserName", "");
    }

    public static void setUserName(Context context, String pwd) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("UserName", pwd);
        editor.commit();
    }

    /**
     * 获取选择柜台的名称
     *
     * @param context
     * @return
     */
    public static String getCounterName(Context context) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sp.getString("CounterName", "");
    }

    public static void setCounterName(Context context, String pwd) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("CounterName", pwd);
        editor.commit();
    }



}
