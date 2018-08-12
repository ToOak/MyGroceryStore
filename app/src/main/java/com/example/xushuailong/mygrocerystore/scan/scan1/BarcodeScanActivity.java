package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.xushuailong.mygrocerystore.R;
import com.example.xushuailong.mygrocerystore.scan.util.Constant;
import com.example.xushuailong.mygrocerystore.scan.util.Constant.*;
import com.example.xushuailong.mygrocerystore.scan.util.HardWare;
import com.example.xushuailong.mygrocerystore.scan.util.ImagesManager;
import com.example.xushuailong.mygrocerystore.scan.util.JCConstant;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant.*;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant;
import com.example.xushuailong.mygrocerystore.scan.util.SpUtil;
import com.example.xushuailong.mygrocerystore.scan.util.Validator;
import com.example.xushuailong.mygrocerystore.scan.util.WccConfigure;
import com.wochacha.scan.WccBarcode;
import com.wochacha.scan.WccResult;


/**
 * accept extra: int-ScanType, ScanResult.kActionSrc(such as web ...)
 */

public final class BarcodeScanActivity extends AppCompatActivity {
    private final String SCAN = ScanFragment.class.getName();
    public static final String KEY_FOCUS_TYPE = "key_focus_type";

    private ImageView imgSwitchFlash;
    private ImageView imgCancel;

    private Bitmap bmp_flashOn;
    private Bitmap bmp_flashOff;
    private Bitmap bmp_cancel;
    private static Handler mainhandler;
    private int scanType;
    private int focusType;

    private boolean flashOnOff = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flashOnOff = false;


        mainhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                try {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case MessageConstant.BarcodeDecodeMsg.DecodeSuccessScan:
                            DecodeThread.DecodeResult result = (DecodeThread.DecodeResult) msg.obj;
                            //FilePath = result.bmpName;

                            /**
                             * 扫描结果
                             */
                            WccResult res = (WccResult) result.obj;
                            Log.e("xsl", "res: " + new String(res.result) + "\t" + result.bmpName);
                            int barType = res.type;
                            String barcode = new String(res.result);
                            String colorCode = new String(res.colorcode);

                            if (!Validator.isEffective(barcode)) {
                                HardWare.sendMessage(ScanFragment.captureHandler, MessageConstant.BarcodeDecodeMsg.RestartPreviewAndDecode);
                                break;
                            }
                            HardWare.playBeepSoundAndVibrate(BarcodeScanActivity.this);


                            if (Constant.ScanType.CONTINUOUS_SCHEDULE != scanType) {
                                unselect(SCAN);
                                /**
                                 * 扫描后进入业务逻辑界面
                                 */

                                TextView textView = findViewById(R.id.txt);
                                ImageView imageView = findViewById(R.id.img);
                                textView.setText(new String(res.result));
                                Log.e("xsl", "bitmap: " + result.bitmap.getWidth() + "\t" + result.bitmap.getHeight());
                                imageView.setImageBitmap(result.bitmap);

                            }
                            break;

                        case MessageConstant.BarcodeDecodeMsg.FlashOn:
                            flashOnOff = true;
                            setFlashImage();
                            break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    finish();
                }
            }
        };

        processIntentData();

    }

    private void processIntentData() {

        Intent data = getIntent();
        scanType = data.getIntExtra("ScanType", ScanType.ALL);
        focusType = data.getIntExtra(KEY_FOCUS_TYPE, -1);
        switch (focusType) {
            case Constant.FocusType.AutoFocus:
                WccConfigure.setCameraFocusType(this, "1");
                break;
            case Constant.FocusType.ManualFocus:
                WccConfigure.setCameraFocusType(this, "3");
                break;
            case -1:
                if (JCConstant.AUTO_FOCUS) {
                    WccConfigure.setCameraFocusType(this, "1");
                } else {
                    WccConfigure.setCameraFocusType(this, "3");
                }
                break;
        }

        remove(SCAN);
        initScanView();
        select(SCAN);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processIntentData();
    }

    private void initScanView() {

        setContentView(R.layout.barcodescan_portrait);
        bmp_flashOn = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_flash_on);
        bmp_flashOff = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_flash_off);//此按钮改为了手输图标
        bmp_cancel = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_cancel);

        imgSwitchFlash = findViewById(R.id.img_switchflash);
        imgCancel = findViewById(R.id.img_cancel);

        imgCancel.setImageBitmap(bmp_cancel);


        // 扫描物流码及销售商品都支持手输
        imgSwitchFlash.setImageBitmap(bmp_flashOff);
        imgSwitchFlash.setVisibility(View.VISIBLE);


        setFlashImage();
        imgSwitchFlash.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // 闪光灯功能暂时取消, 在此调用手输功能,控件命名未修改
                if (flashOnOff) {
                    // turn off flash
                    HardWare.sendMessage(ScanFragment.captureHandler, BarcodeDecodeMsg.FlashOff, false);
                } else {
                    // turn on flash
                    HardWare.sendMessage(ScanFragment.captureHandler, BarcodeDecodeMsg.FlashOn, false);
                }
                flashOnOff = !flashOnOff;
                setFlashImage();

            }
        });


        imgCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFlashImage();

    }


    private void setFlashImage() {
        /**
         * 原闪光灯控制逻辑
         */
        Log.e("oak", "on flash: " + WccConfigure.checkSpecialModelNoFlash());
        if (WccConfigure.checkSpecialModelNoFlash()) {
            imgSwitchFlash.setVisibility(View.GONE);
            return;
        }

        if (flashOnOff) {
            imgSwitchFlash.setImageBitmap(bmp_flashOn);
        } else {
            imgSwitchFlash.setImageBitmap(bmp_flashOff);
        }
    }

    private boolean select(String Tag) {

        if (ScanFragment.status != ScanFragment.STOPPED) {
//            HardWare.ToastShort(this, "请稍等片刻再进行扫描！");
            finish();
            ScanFragment.status = ScanFragment.STOPPED;
            return false;
        }
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction v4ft = fm.beginTransaction();
            Fragment fragment = fm.findFragmentByTag(Tag);
            if (fragment == null) {
                fragment = Fragment.instantiate(BarcodeScanActivity.this, Tag);
                Bundle args = new Bundle();
                args.putInt("scanType", scanType);
                fragment.setArguments(args);
                v4ft.replace(R.id.main_fl_layout, fragment, Tag);
                //动画效果
//                v4ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            } else {
                v4ft.attach(fragment);
            }
            v4ft.commitAllowingStateLoss();
        } catch (Exception e) {
                e.printStackTrace();
            return false;
        }
        return true;
    }

    private void unselect(String Tag) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction v4ft = fm.beginTransaction();
            Fragment fragment = fm.findFragmentByTag(Tag);
            if (fragment != null) {
                v4ft.detach(fragment);
            }
            v4ft.commitAllowingStateLoss();
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    private void remove(String Tag) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction v4ft = fm.beginTransaction();
            Fragment fragment = fm.findFragmentByTag(Tag);
            if (fragment != null) {
                v4ft.remove(fragment);
            }
            v4ft.commitAllowingStateLoss();
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    protected void onDestroy() {
        HardWare.releaseMediaPlayer();
        mainhandler = null;

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((((WccScanApplication) getApplication()).getCamera().isPreview())) {
                unselect(SCAN);
                finish();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * UTF-8, GB2312
     *
     * @param data
     * @param index
     * @param length
     * @return
     */
    static String getDataEncoding(byte[] data, int index, int length) {
        if (Validator.isUtf8Data(data, index, length))
            return "UTF-8";
        else
            return "GB2312";
    }


    public static Handler getMainHandler() {
        return mainhandler;
    }

}