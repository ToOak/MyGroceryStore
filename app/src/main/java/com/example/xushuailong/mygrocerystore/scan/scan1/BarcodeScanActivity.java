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
import com.example.xushuailong.mygrocerystore.scan.util.WccConstant;
import com.example.xushuailong.mygrocerystore.utils.ScreenUtil;
import com.wochacha.scan.WccBarcode;
import com.wochacha.scan.WccResult;


/**
 * accept extra: int-ScanType, ScanResult.kActionSrc(such as web ...)
 */

public final class BarcodeScanActivity extends AppCompatActivity {
    public final static String TAG = "BarcodeScanActivity";
    private final String SCAN = ScanFragment.class.getName();
    private static final String KEY_CAMERA_ZOOM = "key_camera_zoom";
    public static final String KEY_FOCUS_TYPE = "key_focus_type";

    private WccScanApplication app;
    private ImageView imgSwitchFlash;
    private ImageView imgInput;
    private ImageView imgCancel;
    private ImageView imgScanImage;
    private ImageView imgColor;
    private ImageView imgOther;
    private ImageView img_anim;
    private ImageView scan_info;
    private ImageView color_scan_tip;

    private Bitmap bmp_flashOn;
    private Bitmap bmp_flashOff;
    private Bitmap bmp_scan_info;
    private Bitmap bmp_scan_tip;
    private Bitmap bmp_input;
    private Bitmap bmp_cancel;
    private Bitmap bmp_image_scan;
    private Bitmap bmp_color_sel;
    private Bitmap bmp_color_nor;
    private Bitmap bmp_other;
    ProgressDialog pd;
    private static Handler mainhandler;
    private int scanType;
    private int focusType;

    public static String FilePath = "";
    private boolean flashOnOff = false;
    private int screenWidth;
    private int screenHeight;

    private boolean colorOn = false;
    private boolean hasColor = false;

    private SeekBar seekBarZoom;

    private String reqKey;
    private boolean isRequesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        app = (WccScanApplication) getApplication();
        flashOnOff = false;

        screenWidth = ScreenUtil.getScreenWidth(getApplicationContext());
        screenHeight = ScreenUtil.getScreenHeight(getApplicationContext());

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

//                            if (WccConstant.DEBUG) {
//                                HardWare.ToastLong(BarcodeScanActivity.this, "type:" + barType + "\nbarcode:" + barcode + "\nrainbow:" + colorCode);
//                            }

//                            DecodeResultProcessor processor = new DecodeResultProcessor(BarcodeScanActivity.this, msg.arg1, result, scanType, true, true);
//                            processor.setInvoker(mainhandler);
//                            String code = processor.process();
//                            if (WccConstant.DEBUG) {
//                                Log.e(TAG, "processor" + processor + "" + code);
//                            }
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
                                if (!WccBarcode.rainbowOnly) {
                                    if (barType == 128) {
                                        if (Validator.isEffective(barcode)) {
                                            //TODO
//                                            HybridUtil.startHybridActivity(BarcodeScanActivity.this, JCConstant.HOST_WEBVIEW + "signfor?" + "logistics_code=" + barcode
//                                                    + "&staff_id=" + SpUtil.getString(BarcodeScanActivity.this, "STAFF_ID", "")
//                                                    + "&store_id=" + SpUtil.getString(BarcodeScanActivity.this, "STORE_ID", "")
//                                                    + "&udid=" + CommonUtil.getUdid(BarcodeScanActivity.this)
//                                                    + "&token=" + SpUtil.getToken(BarcodeScanActivity.this)
//                                                    + "&h5title=" + DataConverterUtil.urlEncode("入库确认"));
                                        } else {
                                            HardWare.ToastShort(BarcodeScanActivity.this, "未能识别出物流码，请重新扫描！");
                                        }
                                    } else {
                                        HardWare.ToastShort(BarcodeScanActivity.this, "请扫描物流码！");
                                    }

                                }
                                TextView textView = findViewById(R.id.txt);
                                ImageView imageView = findViewById(R.id.img);
                                textView.setText(new String(res.result));
                                Log.e("xsl", "bitmap: " + result.bitmap.getWidth() + "\t" + result.bitmap.getHeight());
                                imageView.setImageBitmap(result.bitmap);

//                                finish();
                            } else {
                                if (WccBarcode.rainbowOnly) {
                                    if (barType == 13 && Validator.isEffective(colorCode) && !"0".equals(colorCode)) {
                                        if (!isRequesting && Validator.isEffective(barcode) && Validator.isEffective(colorCode)) {
                                            isRequesting = true;
                                            requestGoodsInfo(barcode, colorCode);
                                        } else {
                                            HardWare.ToastShort(BarcodeScanActivity.this, "未能识别出彩虹码，请重新扫描！");
                                        }
                                    } else {
                                        HardWare.ToastShort(BarcodeScanActivity.this, "请扫描彩虹码！");
                                    }

                                }
                                HardWare.sendMessageDelayed(ScanFragment.captureHandler, BarcodeDecodeMsg.RestartPreviewAndDecode, 1500);
                            }
                            break;
                        case MessageConstant.SHOW_DIALOG:

                            if (pd != null && DataType.ArticleScan != msg.arg1) {
                                pd.show();
                            }
//						else {
//                        	Intent intent = new Intent(app, PopMessageView.class);
//                        	intent.putExtra("PopType", "0");
//                        	startActivity(intent);
//                        }
                            break;
                        case MessageConstant.CLOSE_DIALOG:
                            if (pd != null && pd.isShowing())
                                pd.dismiss();

                            break;

                        case MessageConstant.SearchFinished:
                            HardWare.getInstance(app).sendMessage(MessageConstant.CLOSE_DIALOG);

                            break;
                        case MessageConstant.ACTIVITY_CLOSE:
                            if (msg.obj != null && msg.obj instanceof Intent)
                                HardWare.getInstance(app).sendMessage(BarcodeDecodeMsg.ScanResult, msg.obj);
                            finish();
                            break;
                        case MessageConstant.BarcodeDecodeMsg.FlashOn:
                            flashOnOff = true;
                            setFlashImage();
                            break;
//                        case ReqType.UserGoodsInfo:
//                            isRequesting = false;
//                            try {
//                                SparseArray<Object> goodsInfoResult = (SparseArray<Object>) msg.obj;
//                                if (goodsInfoResult != null) {
//                                    //TODO
////                                    GoodsInfo goodsInfo = (GoodsInfo) goodsInfoResult.get(1);
////                                    List<TraceDetailInfo> traceInfoList = (List<TraceDetailInfo>) goodsInfoResult.get(2);
////                                    List<PromotionInfo> promotionInfoList = (List<PromotionInfo>) goodsInfoResult.get(3);
////                                    if ("OK".equals(goodsInfo.getIsokType())) {
////                                        switch (goodsInfo.getStatus()) {
////                                            case "SALE":
////                                                Intent intent = new Intent(BarcodeScanActivity.this, GoodsDetailActivity.class);
////                                                intent.putExtra("GoodsInfo", goodsInfo);
////                                                intent.putExtra("TraceInfoList", (Serializable) traceInfoList);
////                                                intent.putExtra("PromotionInfoList", (Serializable) promotionInfoList);
////                                                startActivity(intent);
////                                                finish();
////                                                break;
////                                            case "SOLD":
////                                                HardWare.ToastShort(BarcodeScanActivity.this, "当前商品已售出!");
////                                                return;
////                                            case "LOSS":
////                                            case "DCMSOLD":
////                                                HardWare.ToastShort(BarcodeScanActivity.this, "当前商品已损耗!");
////                                                break;
////                                            case "OFFLINESOLD":
////                                                HardWare.ToastShort(BarcodeScanActivity.this, "当前商品线下已销售!");
////                                                return;
////                                            case "INCART":
////                                                HardWare.ToastShort(BarcodeScanActivity.this, "当前商品已添加购物车!");
////                                                break;
////                                            default:
////                                                HardWare.ToastShort(BarcodeScanActivity.this, "当前商品已不可销售!");
////                                                return;
////                                        }
////                                    } else {
////                                        ToastUtil.toastShort(BarcodeScanActivity.this, goodsInfo.getMsg(BarcodeScanActivity.this));
////                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            break;
                        case MessageConstant.SET_ZOOM:
                            setSeekBar(seekBarZoom, msg.arg1, msg.arg2);
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

        pd = new ProgressDialog(BarcodeScanActivity.this);
        pd.setMessage("请稍候......");

        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
    }

    private void processIntentData() {

        reqKey = hashCode() + "";

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

        colorOn = false;
        //WccConfigure.setColorMode(context, colorOn);

        remove(SCAN);
        initScanView();
        select(SCAN);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (WccConstant.DEBUG)
            Log.e(TAG, "barcode scan onNewIntent");
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processIntentData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putInt("screenWidth", screenWidth);
        outState.putInt("screenHeight", screenHeight);
    }

    private void initScanView() {
        //int resource_scan_info = R.drawable.icon_scan_info;
        //销售商品显示彩虹码标记
        int resource_scan_info;
        if (WccBarcode.rainbowOnly) {
            resource_scan_info = R.drawable.icon_scan_color_info;
        } else {
            resource_scan_info = R.drawable.icon_scan_barcode_info;
        }


        if (colorOn)
            resource_scan_info = R.drawable.icon_scan_color_info;

        switch (scanType) {
            case ScanType.CONTINUOUS_SCHEDULE:
                resource_scan_info = R.drawable.icon_scan_info_to_stockshedule;
                break;
            default:
                break;
        }

        if (HardWare.needRotateActivity()) {
            setContentView(R.layout.barcodescan);
            bmp_flashOn = ImagesManager.Rotate(BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_flash_on), 270);
            bmp_flashOff = ImagesManager.Rotate(BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_flash_off), 270);
            bmp_scan_info = ImagesManager.Rotate(BitmapFactory.decodeResource(getResources(), resource_scan_info), 270);
            bmp_cancel = ImagesManager.Rotate(BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_cancel), 270);
            bmp_input = ImagesManager.Rotate(BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_input), 270);
            bmp_image_scan = ImagesManager.Rotate(BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_picture), 270);
            bmp_color_sel = ImagesManager.Rotate(BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_color_sel), 270);
            bmp_color_nor = ImagesManager.Rotate(BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_color_nor), 270);
            bmp_scan_tip = ImagesManager.Rotate(BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_color_tip), 270);
        } else {
            // 手机版本大于11，在企业直销柜台上都是使用此布局
            setContentView(R.layout.barcodescan_portrait);
            bmp_scan_info = BitmapFactory.decodeResource(getResources(), resource_scan_info);
            bmp_flashOn = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_flash_on);
            bmp_flashOff = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_input2);//此按钮改为了手输图标
            bmp_cancel = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_cancel);
            bmp_input = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_input);
            bmp_image_scan = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_picture);
            bmp_color_sel = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_color_sel);
            bmp_color_nor = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_color_nor);
            bmp_scan_tip = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_color_tip);
        }

        imgSwitchFlash = (ImageView) findViewById(R.id.img_switchflash);
        imgInput = (ImageView) findViewById(R.id.img_input);
        imgCancel = (ImageView) findViewById(R.id.img_cancel);
        imgScanImage = (ImageView) findViewById(R.id.img_scanimg);
        imgColor = (ImageView) findViewById(R.id.img_color);
        imgOther = (ImageView) findViewById(R.id.img_other);
        img_anim = (ImageView) findViewById(R.id.scan_img_anim);
        color_scan_tip = (ImageView) findViewById(R.id.color_scan_tip);
        seekBarZoom = (SeekBar) findViewById(R.id.seekBar_zoom);

        scan_info = (ImageView) findViewById(R.id.scan_info);
        scan_info.setImageBitmap(bmp_scan_info);

        if (ScanType.CONTINUOUS_SCHEDULE == scanType) {
            imgInput.setVisibility(View.GONE);
            imgScanImage.setVisibility(View.GONE);

            imgOther.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    HardWare.sendMessage(ScanFragment.captureHandler, BarcodeDecodeMsg.CloseCamera);
                    finish();
                }
            });

            switch (scanType) {
                case ScanType.CONTINUOUS_SCHEDULE:
                    bmp_other = BitmapFactory.decodeResource(getResources(), R.drawable.icon_scan_stockshedule);
                    break;
                default:
                    break;
            }

            if (HardWare.needRotateActivity()) {
                bmp_other = ImagesManager.Rotate(bmp_other, 270);
            }
            imgOther.setVisibility(View.GONE);
            imgOther.setImageBitmap(bmp_other);
        }

        if (ScanType.RETURNGOODSEXPRESS == scanType) {
            imgInput.setVisibility(View.GONE);
            imgScanImage.setVisibility(View.GONE);
            scan_info.setVisibility(View.GONE);
        }

        imgInput.setImageBitmap(bmp_input);
        imgCancel.setImageBitmap(bmp_cancel);
        imgScanImage.setImageBitmap(bmp_image_scan);

        hasColor = true;
        if (scanType == ScanType.EXP || scanType == ScanType.CONTINUOUS_SCHEDULE
                || scanType == ScanType.FROMEXPOSURE || scanType == ScanType.FROMQRCODE
                || scanType == ScanType.PRICETREND || scanType == ScanType.RETURNGOODSEXPRESS) {
            imgColor.setVisibility(View.GONE);
            hasColor = false;
        }
        /**
         * 所有扫描都隐藏掉彩虹码切换按钮
         * 先以改动最小的方式达到效果，控制逻辑及布局先保留
         */
        if (scanType == ScanType.ALL) {
            imgColor.setVisibility(View.GONE);
        }
        setColorImage();
        if (colorOn) {
            color_scan_tip.setVisibility(View.VISIBLE);
            color_scan_tip.setImageBitmap(bmp_scan_tip);
        } else {
            //color_scan_tip.setVisibility(View.GONE);
            // 销售商品显示彩虹码tip
            if (WccBarcode.rainbowOnly) {
                color_scan_tip.setVisibility(View.VISIBLE);
                color_scan_tip.setImageBitmap(bmp_scan_tip);
            }
        }

        // 扫描物流码及销售商品都支持手输
        imgSwitchFlash.setImageBitmap(bmp_flashOff);
        imgSwitchFlash.setVisibility(View.VISIBLE);

        imgColor.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                colorOn = !colorOn;
                //WccConfigure.setColorMode(context, colorOn);
                if (colorOn)
                    HardWare.sendMessage(ScanFragment.captureHandler, BarcodeDecodeMsg.ColorOn, false);
                else
                    HardWare.sendMessage(ScanFragment.captureHandler, BarcodeDecodeMsg.ColorOff, false);
                setColorImage();
                int resource_scan_info;
                if (colorOn) {
                    color_scan_tip.setVisibility(View.VISIBLE);
                    color_scan_tip.setImageBitmap(bmp_scan_tip);

                    resource_scan_info = R.drawable.icon_scan_color_info;
                } else {
                    color_scan_tip.setVisibility(View.GONE);

                    resource_scan_info = R.drawable.icon_scan_info;
                    if (scanType == ScanType.CONTINUOUS_SCHEDULE)
                        resource_scan_info = R.drawable.icon_scan_info_to_stockshedule;
                }
                if (HardWare.needRotateActivity()) {
                    bmp_scan_info = ImagesManager.Rotate(BitmapFactory.decodeResource(getResources(), resource_scan_info), 270);
                } else {
                    bmp_scan_info = BitmapFactory.decodeResource(getResources(), resource_scan_info);
                }
                scan_info.setImageBitmap(bmp_scan_info);
            }
        });

        setFlashImage();
        imgSwitchFlash.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // 闪光灯功能暂时取消, 在此调用手输功能,控件命名未修改
                if (flashOnOff) {
                    // turn off flash
                    if (WccConstant.DEBUG)
                        Log.d(TAG, "onSwitch mode: off  " + Build.MODEL);
                    HardWare.sendMessage(ScanFragment.captureHandler, BarcodeDecodeMsg.FlashOff, false);
                } else {
                    // turn on flash
                    if (WccConstant.DEBUG)
                        Log.d(TAG, "onSwitch mode: on  " + Build.MANUFACTURER);
                    HardWare.sendMessage(ScanFragment.captureHandler, BarcodeDecodeMsg.FlashOn, false);
                }
                flashOnOff = !flashOnOff;
                setFlashImage();

                /**
                 * 调用手输功能
                 */
//                if (scanType == ScanType.EXP) {
////            		Intent intent = new Intent(BarcodeScanActivity.this, ExpressMainActivity.class);
////	                intent.putExtra("ActTag", ActTag.ExpressMain);
////	                startActivity(intent);
////					finish();
//                } else {
//                    Intent intent = new Intent(BarcodeScanActivity.this, BarcodeInputActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("ScanType", scanType);
//                    intent.putExtra("hasColor", true);
//                    startActivity(intent);
//                    finish();
//                }
                //TODO
//                Intent intent = new Intent(BarcodeScanActivity.this, NewBarcodeInputActivity.class);
//                if (WccBarcode.rainbowOnly) {
//                    intent.putExtra("BarcodeInputType", Constant.BarcodeInputType.RAINBOWCODE);
//                } else {
//                    // 目前非彩虹码之外手输只有物流码，后面需求可能扩展
//                    intent.putExtra("BarcodeInputType", Constant.BarcodeInputType.EXPRESS);
//                }
//
//                startActivity(intent);
//                finish();
            }
        });

        imgInput.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (scanType == ScanType.EXP) {
//            		Intent intent = new Intent(BarcodeScanActivity.this, ExpressMainActivity.class);
//	                intent.putExtra("ActTag", ActTag.ExpressMain);
//	                startActivity(intent);
//					finish();
                } else {
                    Intent intent = new Intent(BarcodeScanActivity.this, BarcodeInputActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("ScanType", scanType);
                    intent.putExtra("hasColor", hasColor);
                    startActivity(intent);
                    finish();
                }
            }
        });

        imgCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        imgScanImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //WccConfigure.setExpressCompany(context, "");
                startImageChooseActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (WccConstant.DEBUG)
            Log.d(TAG, "barcode onResume start");

        setFlashImage();

    }

    private void setColorImage() {
        if (colorOn) {
            imgColor.setImageBitmap(bmp_color_sel);
        } else {
            imgColor.setImageBitmap(bmp_color_nor);
        }
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
        if (WccConstant.DEBUG) Log.e(TAG, "select(SCAN)");

        if (ScanFragment.status != ScanFragment.STOPPED) {
            HardWare.ToastShort(this, "请稍等片刻再进行扫描！");
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
                //v4ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            } else {
                v4ft.attach(fragment);
            }
            v4ft.commitAllowingStateLoss();
        } catch (Exception e) {
            if (WccConstant.DEBUG)
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
            if (WccConstant.DEBUG)
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
            if (WccConstant.DEBUG)
                e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (WccConstant.DEBUG)
            Log.e(TAG, "barcode scan onStop");
    }

    protected void onDestroy() {
        if (WccConstant.DEBUG)
            Log.e(TAG, "barcode scan onDestroy");
        HardWare.releaseMediaPlayer();
        mainhandler = null;

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (WccConstant.DEBUG)
            Log.e(TAG, "onPause");
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

    /**
     * 启动图片识别，企业直销App不添加此功能
     */
    private void startImageChooseActivity() {

    }

    public static Handler getMainHandler() {
        return mainhandler;
    }

    private void requestGoodsInfo(String barcode, String rainbowCode) {
        //TODO
//        MapArgs mapArgs = new MapArgs();
//        mapArgs.put("ReqKey", reqKey);// 必填参数
//        mapArgs.put("ReqType", ReqType.UserGoodsInfo);// 必填参数
//        mapArgs.put("Barcode", barcode);
//        mapArgs.put("RainbowCode", rainbowCode);
//        mapArgs.put("CheckAddCart", true);
//        DataFetcher.getInstance().requestData(mainhandler, mapArgs, false);
    }

    /**
     * 设置平滑调节zoom控件
     *
     * @param seekBarZoom
     */
    public void setSeekBar(final SeekBar seekBarZoom, final int currentZoom, final int maxZoom) {
        seekBarZoom.setMax(maxZoom);//最大的zoom
        final int spZoom = SpUtil.getInt(app, KEY_CAMERA_ZOOM, -1);
        if (spZoom != -1) {
            seekBarZoom.setProgress(spZoom);
            mainhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CameraManager.getInstance(app).setSmoothZoom(spZoom);
                }
            }, 300);

        } else {
            seekBarZoom.setProgress(currentZoom);//获取当前camera的真实zoom来显示
        }
        seekBarZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 测试zoom调节
                CameraManager.getInstance(app).setSmoothZoom(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SpUtil.putInt(app, KEY_CAMERA_ZOOM, seekBar.getProgress());
            }
        });
    }
}