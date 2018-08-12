package com.example.xushuailong.mygrocerystore.scan.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;

public class WccConfigure {

    public static void setColorMode(Context con, boolean onOff) {
        SharedPreferences sharepre = PreferenceManager.getDefaultSharedPreferences(con);
        Editor editor = sharepre.edit();
        editor.putBoolean("SCAN_COLOR_MODE", onOff);
        editor.commit();
    }

    public static boolean getColorMode(Context con) {
        SharedPreferences sharepre = PreferenceManager.getDefaultSharedPreferences(con);
        return sharepre.getBoolean("SCAN_COLOR_MODE", false);
    }

    public static String getCameraRotate(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getString(Constant.KEY_CAMROTATE_PREF, "0");
    }

    public static void setCameraRotateType(Context context, String type) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putString(Constant.KEY_CAMROTATE_PREF, type);
        editor.commit();
    }

    public static String getCameraSelect(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getString(Constant.KEY_CAMSELECT_PREF, "0");
    }

    public static void setCameraSelectType(Context context, String type) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putString(Constant.KEY_CAMSELECT_PREF, type);
        editor.commit();
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

    public static void setSoundEnable(Context context, boolean enable) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putBoolean(Constant.KEY_INFO_SOUND, enable);
        editor.commit();
    }

    //默认识别成功后不震动
    public static boolean getIsVibrate(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getBoolean(Constant.KEY_VIBRATE, false);
    }

    public static void setVibrateEnable(Context context, boolean enable) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putBoolean(Constant.KEY_VIBRATE, enable);
        editor.commit();
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

    public static void setCameraFlashType(Context context, String type) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putString(Constant.KEY_FLASH_PREF, type);
        editor.commit();
    }

    public static void setIsAutoFocus(Context context, boolean isAutofocus) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putBoolean(Constant.KeyIsAutofocus, isAutofocus);
        editor.commit();
    }

    public static boolean isFlashHasOnRemind(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getBoolean("isFlashHasOnRemind", false);
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

    public static void setHxcodeOpen(Context context, boolean isHxcodeOpen) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putBoolean("HxCodeStatus", isHxcodeOpen);
        editor.commit();
    }

    /**
     * 统计需要，每次app启动记录当前时间 add V8.7.5
     *
     * @param context
     * @return
     */
    public static String getLauchTimeStamp(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getString("LauchTimeStamp", "");
    }

    //若以.号或|来分割数组的话，需要转意字符String.split("\\.")与String.split("\\|")
    public static void setLauchTimeStamp(Context context) {
        long currentTimeStamp = System.currentTimeMillis() / 1000L;
        String lastTimeStamp = getLauchTimeStamp(context);
        String timeStamp = "";
        if (Validator.isEffective(lastTimeStamp) && lastTimeStamp.contains(",")) {
            try {
                String[] timeArray = lastTimeStamp.split(",");
                if (timeArray != null && timeArray.length > 2) {
                    timeArray[1] = timeArray[2];
                    timeStamp = timeArray[0] + "," + timeArray[1] + "," + currentTimeStamp;
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else {
            timeStamp = currentTimeStamp + "," + currentTimeStamp + "," + currentTimeStamp;
        }
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putString("LauchTimeStamp", timeStamp);
        editor.commit();
    }

    /**
     * 获取要上传的his参数，即LauchTimeStamp的前两段时间, add V8.8.0
     *
     * @param context
     * @return
     */
    public static String getHisTimeStamp(Context context) {
        String launchTimeStamp = getLauchTimeStamp(context);
        if (Validator.isEffective(launchTimeStamp) && launchTimeStamp.contains(",")) {
            String[] timeArray = launchTimeStamp.split(",");
            if (timeArray != null && timeArray.length > 2) {
                return timeArray[0] + "," + timeArray[1];
            }
        }

        return "";
    }

    /**
     * 某些商城接口是否使用https
     *
     * @param context
     * @return true-使用https,false-不用，默认false
     */
    public static boolean isUseHttps(Context context) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharepre.getBoolean("UseHttps", false);
    }

    public static void setUseHttps(Context context, boolean isUsed) {
        SharedPreferences sharepre = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharepre.edit();
        editor.putBoolean("UseHttps", isUsed);
        editor.commit();
    }

    //去掉扫描页面闪光灯和设置里面闪光灯选择
    public static boolean checkSpecialModelNoFlash() {
        if (Build.MODEL.equals("Lenovo S560")
                || Build.MODEL.equals("Lenovo S686"))
            return true;
        return false;
    }

    //去除设置中前后置摄像头选择,某些机型会崩溃
    public static boolean checkSpecialModeNoCameraSelect() {
        if ("SCH-P709".equals(Build.MODEL))
            return true;
        return false;
    }
}