package com.wochacha.scan.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;

public class WccConfigure {


    public static String getCameraRotate(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getString(Constant.KEY_CAMROTATE_PREF, "0");
    }


    public static String getCameraSelect(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getString(Constant.KEY_CAMSELECT_PREF, "0");
    }

    public static boolean getIsAutoFocus(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getBoolean(Constant.KeyIsAutofocus, false);
    }

    public static boolean getIsPlayBeep(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getBoolean(Constant.KEY_INFO_SOUND, true);
    }


    //默认识别成功后不震动
    public static boolean getIsVibrate(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getBoolean(Constant.KEY_VIBRATE, false);
    }


    /**
     * "1" default<br>
     * "2" AutoFocus<br>
     * "3" FixedFocus<br>
     *
     * @param context
     */
    public static String getCamAutoFocus(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getString(Constant.KEY_AUTOFOCUS_PREF, "1");
    }

    public static void setCameraFocusType(Context context, String type) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putString(Constant.KEY_AUTOFOCUS_PREF, type);
        editor.commit();
    }

    public static String getCamFlashMode(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (Build.MANUFACTURER.equals("motorola")
                || Build.MANUFACTURER.equals("Motorola")) {
            return sharepre.getString(Constant.KEY_FLASH_PREF, "5");
        } else if (Build.MODEL.equals("MT620")) {
            return sharepre.getString(Constant.KEY_FLASH_PREF, "2");
        } else {
            return sharepre.getString(Constant.KEY_FLASH_PREF, "1");
        }
    }


    public static void setFlashHasOnRemind(Context context, boolean hasRemind) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putBoolean("isFlashHasOnRemind", hasRemind);
        editor.commit();
    }

    // V8.0.1是否打开汉信码
    public static boolean isHxcodeOpen(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getBoolean("HxCodeStatus", false);
    }


    //去掉扫描页面闪光灯和设置里面闪光灯选择
    public static boolean checkSpecialModelNoFlash() {
        if (Build.MODEL.equals("Lenovo S560")
                || Build.MODEL.equals("Lenovo S686"))
            return true;
        return false;
    }

}