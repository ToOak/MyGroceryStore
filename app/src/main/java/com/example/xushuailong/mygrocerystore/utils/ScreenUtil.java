package com.example.xushuailong.mygrocerystore.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {

    private static int width = 0;
    private static int height = 0;
    private static float density = 0;
    private static DisplayMetrics dm = new DisplayMetrics();

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (width == 0) {
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            width = dm.widthPixels;
        }
        return width;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (height == 0) {
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            height = dm.heightPixels;
        }
        return height;
    }

    public static float getScreenDensity(Context context) {
        if (density == 0) {
            try {
                DisplayMetrics dm = new DisplayMetrics();
                WindowManager manager = (WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE);
                manager.getDefaultDisplay().getMetrics(dm);
                density = dm.density;
            } catch (Exception ex) {
                ex.printStackTrace();
                density = 1.0f;
            }
        }
        return density;
    }

    public int getWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = getDisplayMetrics(context).density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int getScreenStateHeight(Context context) {
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight1 = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }


    /**
     * 获取屏幕size
     *
     * @return
     */

    public static String getScreenSize(Context context) {
        DisplayMetrics dm2 = getDisplayMetrics(context);
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return dm2.widthPixels + "x" + dm2.heightPixels;
        } else {
            return dm2.heightPixels + "x" + dm2.widthPixels;
        }
    }


}
