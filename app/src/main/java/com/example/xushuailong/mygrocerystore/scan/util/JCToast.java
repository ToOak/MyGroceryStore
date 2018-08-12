package com.example.xushuailong.mygrocerystore.scan.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xushuailong.mygrocerystore.R;


/**
 * @version V9.0.0
 * @author: guanghui_wan
 * @date: 2017/1/4
 */

public class JCToast {

    private Context context;
    private static JCToast toast;
    private TextView tvToast;
    private Toast mToast;


    private JCToast(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        tvToast = (TextView) view.findViewById(R.id.tv_toast);
        mToast = new Toast(context);
        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_SHORT);
    }

    public static JCToast getInstance(Context context) {
        if (toast == null) {
            synchronized (JCToast.class) {
                if (toast == null)
                    toast = new JCToast(context);
            }
        }
        return toast;
    }

    public void toastShort(String msg) {
        mToast.setDuration(Toast.LENGTH_SHORT);
        toastMsg(msg);
    }

    public void toastLong(String msg) {
        mToast.setDuration(Toast.LENGTH_LONG);
        toastMsg(msg);
    }

    private void toastMsg(String msg) {
        tvToast.setText(msg);
        mToast.show();
    }
}
