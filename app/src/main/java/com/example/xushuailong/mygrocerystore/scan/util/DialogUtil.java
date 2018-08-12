package com.example.xushuailong.mygrocerystore.scan.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by anxiaofei on 16/10/24.
 */

public class DialogUtil {

    /**
     * 显示默认对话框
     *
     * @param context          除了系统window,务必使用Activity的上下文
     * @param title            对话框标题
     * @param message          对话框内容
     * @param positiveText     eg:确定
     * @param negativeText     eg:取消
     * @param positiveListener 确定listener
     * @param negativeListener 取消listener
     */
    public static void showNormalDialog(Context context,
                                        String title,
                                        String message,
                                        String positiveText,
                                        String negativeText,
                                        DialogInterface.OnClickListener positiveListener,
                                        DialogInterface.OnClickListener negativeListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, positiveListener)
                .setNegativeButton(negativeText, negativeListener)
                .show();
    }

    /**
     * 得到进度条对话框
     *
     * @param context
     * @return
     */
    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage("正在请求数据...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
        return mProgressDialog;
    }


    /**
     * 显示对话框
     *
     * @param progressDialog
     */
    public static void show(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    /**
     * 关闭对话框
     *
     * @param progressDialog
     */
    public static void close(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
