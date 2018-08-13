package com.wochacha.scan;

import android.app.Application;

/**
 * 在此处加载算法，需要使用扫描功能的Application需要继承自此类
 * <p>
 * 注意：WccConstant.java与DataConverter.java移到com.wochacha.scan.util中，为了算法校验通过，需要修改Native代码中对应的路径
 * <p>
 * Created by anxiaofei on 16/10/23.
 */

public class WccScanApplication extends Application {
    protected boolean initResult = true;

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                initScanner();
            }
        }).start();
    }

    public void initScanner() {
        // 初始化算法
        int ret = getScanner().getInitResult();
        if (ret == 0)
            initResult = false;
        else
            initResult = true;
    }

    public CameraManager getCamera() {
        return CameraManager.getInstance(this);
    }

    public WccBarcode getScanner() {
        return WccBarcode.getInstance(this);
    }
}
