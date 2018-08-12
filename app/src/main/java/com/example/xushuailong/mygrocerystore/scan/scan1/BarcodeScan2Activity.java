package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.xushuailong.mygrocerystore.R;
import com.example.xushuailong.mygrocerystore.scan.util.Constant;
import com.example.xushuailong.mygrocerystore.scan.util.Constant.*;
import com.example.xushuailong.mygrocerystore.scan.util.HardWare;
import com.example.xushuailong.mygrocerystore.scan.util.ImagesManager;
import com.example.xushuailong.mygrocerystore.scan.util.JCConstant;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant.*;
import com.example.xushuailong.mygrocerystore.scan.util.SpUtil;
import com.example.xushuailong.mygrocerystore.scan.util.Validator;
import com.example.xushuailong.mygrocerystore.scan.util.WccConfigure;
import com.example.xushuailong.mygrocerystore.scan.util.WccConstant;
import com.wochacha.scan.WccBarcode;
import com.wochacha.scan.WccResult;

import static com.example.xushuailong.mygrocerystore.scan.scan1.BarcodeScanActivity.KEY_FOCUS_TYPE;


/**
 * accept extra: int-ScanType, ScanResult.kActionSrc(such as web ...)
 */

public final class BarcodeScan2Activity extends AppCompatActivity {
    public final static String TAG = "BarcodeScan2Activity";
    private final String SCAN = Scan2Fragment.class.getName();
    private static final String KEY_PART_CAMERA_ZOOM = "key_part_camera_zoom";

    public static final String KEY_SCAN_TYPE = "key_scan_type";
    /**
     * 任务盘点类型
     */
    public static final String VALUE_SCAN_TASK = "value_scan_task";
    /**
     * 入库报损类型
     */
    public static final String VALUE_SCAN_STORAGE = "value_scan_storage";
    /**
     * 直接报损类型
     */
    public static final String VALUE_SCAN_DIRECTLY = "value_scan_directly";
    /**
     * 单SKU盘点
     */
    public static final String VALUE_SKU_CHECK = "value_sku_check";
    public static final String KEY_GOODS_NAME = "key_goods_name";
    public static final String KEY_ID = "key_id";
    public static final String KEY_GOODS_SPEC = "key_goods_spec";
    public static final String KEY_GOODS_BARCODE = "key_goods_barcode";
    public static final String KEY_TARGET = "key_target";
    public static final String KEY_TARGET_COUNT = "key_target_count";
    public static final String KEY_PKID = "key_pkid";


    private WccScanApplication app;
    private ImageView scan_info;

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

    private FragmentManager mFragmengManager;
    private FragmentTransaction mFragmengTs;
    private FrameLayout mfLLossGoods;
    private String id, goodsName, goodsSpec, goodsBarcode, target, targetCount,pkid;

    private String curScanType = "";
    private Toolbar toolbar;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WccBarcode.rainbowOnly = true;

        if (HardWare.needRotateActivity())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        app = (WccScanApplication) getApplication();
        flashOnOff = false;

        screenWidth = HardWare.getScreenWidth(getApplicationContext());
        screenHeight = HardWare.getScreenHeight(getApplicationContext());
        if (savedInstanceState != null) {
            int savedScreenWidth = savedInstanceState.getInt("screenWidth");
            if (screenWidth != savedScreenWidth)
                HardWare.setScreenWidth(savedScreenWidth);
            int savedScreenHeight = savedInstanceState.getInt("screenHeight");
            if (screenHeight != savedScreenHeight)
                HardWare.setScreenHeight(savedScreenHeight);
        }

        mainhandler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                try {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case MessageConstant.BarcodeDecodeMsg.DecodeSuccessScan:
                            if (WccConstant.DEBUG) {
                                Log.e(TAG, "MessageConstant.DecodeSuccessScan:");
                            }
                            DecodeThread.DecodeResult result = (DecodeThread.DecodeResult) msg.obj;
                            //FilePath = result.bmpName;

                            /**
                             * 扫描结果
                             */
                            WccResult res = (WccResult) result.obj;
                            int barType = res.type;
                            String barcode = new String(res.result);
                            String colorCode = new String(res.colorcode);
                            if (barType == 13 && !"0".equals(colorCode)) {
                                if (scanSubmitLossListener != null) {
                                    switch (curScanType) {
                                        case VALUE_SCAN_TASK:
                                        case VALUE_SCAN_STORAGE:
                                        case VALUE_SKU_CHECK:
                                            scanSubmitLossListener.submitLossScanSuccess(barcode, colorCode);
                                            break;
                                        case VALUE_SCAN_DIRECTLY:
                                            scanSubmitLossListener.submitLossScanSuccess(barcode, colorCode);
                                            break;
                                    }
                                }
                            } else {
                                HardWare.ToastShort(BarcodeScan2Activity.this, "请扫描彩虹码！");
                            }

//                            if (WccConstant.DEBUG) {
//                                HardWare.ToastShort(BarcodeScan2Activity.this, "type:" + barType + "\nbarcode:" + barcode + "\nrainbow:" + colorCode);
//                            }

//                            DecodeResultProcessor processor = new DecodeResultProcessor(BarcodeScanActivity.this, msg.arg1, result, scanType, true, true);
//                            processor.setInvoker(mainhandler);
//                            String code = processor.process();
//                            if (WccConstant.DEBUG) {
//                                Log.e(TAG, "processor" + processor + "" + code);
//                            }
                            if (!Validator.isEffective(barcode)) {
                                HardWare.sendMessage(Scan2Fragment.captureHandler2, BarcodeDecodeMsg.RestartPreviewAndDecode);
                                break;
                            }else {
                                HardWare.sendMessageDelayed(Scan2Fragment.captureHandler2, BarcodeDecodeMsg.RestartPreviewAndDecode, 1500);
                            }
                            HardWare.playBeepSoundAndVibrate(BarcodeScan2Activity.this);


//                            if (ScanType.CONTINUOUS_SCHEDULE != scanType) {
//                                unselect(SCAN);
                                /**
                                 * 扫描后进入业务逻辑界面
                                 */
//                                if (WccBarcode.rainbowOnly) {
//                                    if (barType == 13 && Validator.isEffective(colorCode)) {
//                                        if (Validator.isEffective(barcode) && Validator.isEffective(colorCode)) {
//                                            Intent intent = new Intent(BarcodeScan2Activity.this, GoodsDetailActivity.class);
//                                            intent.putExtra("Barcode", barcode);
//                                            intent.putExtra("ColorCode", colorCode);
//                                            startActivity(intent);
//                                        } else {
//                                            HardWare.ToastShort(BarcodeScan2Activity.this, "未能识别出彩虹码，请重新扫描！");
//                                        }
//                                    } else {
//                                        HardWare.ToastShort(BarcodeScan2Activity.this, "请扫描彩虹码！");
//                                    }
//
//                                } else {
//                                    if (barType == 128) {
//                                        if (Validator.isEffective(barcode)) {
//                                            HybridUtil.startHybridActivity(BarcodeScan2Activity.this, JCConstant.HOST_WEBVIEW + "signfor?" + "logistics_code=" + barcode
//                                                    + "&staff_id=" + SpUtil.getString(BarcodeScan2Activity.this, "STAFF_ID", "")
//                                                    + "&store_id=" + SpUtil.getString(BarcodeScan2Activity.this, "STORE_ID", "")
//                                                    + "&udid=" + CommonUtil.getUdid(BarcodeScan2Activity.this)
//                                                    + "&token=" + SpUtil.getToken(BarcodeScan2Activity.this)
//                                                    + "&h5title=" + DataConverterUtil.urlEncode("入库确认"));
//                                        } else {
//                                            HardWare.ToastShort(BarcodeScan2Activity.this, "未能识别出物流码，请重新扫描！");
//                                        }
//                                    } else {
//                                        HardWare.ToastShort(BarcodeScan2Activity.this, "请扫描物流码！");
//                                    }
//
//                                }
//
//                                finish();
//                            } else {
//                                HardWare.sendMessageDelayed(Scan2Fragment.captureHandler2, BarcodeDecodeMsg.RestartPreviewAndDecode, 1500);
//                            }
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
                            if (WccConstant.DEBUG) {
                                Log.e(TAG, "MessageConstant.SearchFinished:");
                            }
                            HardWare.getInstance(app).sendMessage(MessageConstant.CLOSE_DIALOG);

                            break;
                        case MessageConstant.ACTIVITY_CLOSE:
                            if (msg.obj != null && msg.obj instanceof Intent)
                                HardWare.getInstance(app).sendMessage(BarcodeDecodeMsg.ScanResult, msg.obj);
                            finish();
                            break;
                        case BarcodeDecodeMsg.FlashOn:
                            flashOnOff = true;
                            setFlashImage();
                            break;
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

        findViews();

        setLossListFragment();

        pd = new ProgressDialog(BarcodeScan2Activity.this);
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

    /**
     * 根据类型，在扫描框下加载不同的Fragment
     */
    private void setLossListFragment() {
        Fragment fragment = null;
        mFragmengManager = getSupportFragmentManager();
        mFragmengTs = mFragmengManager.beginTransaction();
        switch (curScanType) {
            case VALUE_SCAN_STORAGE:
                fragment = BulkSubmitLossFragment.newInstance(BulkSubmitLossFragment.CHECK_TYPE_STORAGE, id, goodsName, goodsSpec, goodsBarcode,pkid, "", "");
                break;
            case VALUE_SKU_CHECK:
                fragment = BulkSubmitLossFragment.newInstance(BulkSubmitLossFragment.CHECK_TYPE_SKU, id, goodsName, goodsSpec, goodsBarcode,pkid, "", targetCount);
                break;
            case VALUE_SCAN_TASK:
                fragment = BulkSubmitLossFragment.newInstance(BulkSubmitLossFragment.CHECK_TYPE_TASK, id, goodsName, goodsSpec, goodsBarcode,pkid, target, targetCount);
                break;
            case VALUE_SCAN_DIRECTLY:
                fragment = DirectlySubmitLossFragment.newInstance();
                break;
        }
        scanSubmitLossListener = (OnScanCompleteListener) fragment;
        mFragmengTs.replace(R.id.fL_loss_goods, fragment);
        mFragmengTs.commit();

    }

    protected void findViews() {
        mfLLossGoods = (FrameLayout) findViewById(R.id.fL_loss_goods);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        seekBarZoom = (SeekBar) findViewById(R.id.seekBar_zoom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        initTitle();
    }

    private void initTitle() {
        switch (curScanType) {
            case VALUE_SCAN_TASK:
            case VALUE_SCAN_STORAGE:
                tvTitle.setText("盘点盘损");
                break;
            case VALUE_SCAN_DIRECTLY:
                tvTitle.setText("扫码报损");
                break;
        }
    }

    private void processIntentData() {
        Intent data = getIntent();
        scanType = data.getIntExtra("ScanType", Constant.ScanType.ALL);
        id = data.getStringExtra(BarcodeScan2Activity.KEY_ID);
        goodsName = data.getStringExtra(KEY_GOODS_NAME);
        goodsSpec = data.getStringExtra(KEY_GOODS_SPEC);
        goodsBarcode = data.getStringExtra(KEY_GOODS_BARCODE);
        target = data.getStringExtra(KEY_TARGET);
        targetCount = data.getStringExtra(KEY_TARGET_COUNT);
        curScanType = data.getStringExtra(KEY_SCAN_TYPE);
        pkid = data.getStringExtra(KEY_PKID);
        colorOn = false;
        //WccConfigure.setColorMode(context, colorOn);

        focusType = data.getIntExtra(KEY_FOCUS_TYPE,-1);
        switch (focusType){
            case Constant.FocusType.AutoFocus:
                WccConfigure.setCameraFocusType(this,"1");
                break;
            case Constant.FocusType.ManualFocus:
                WccConfigure.setCameraFocusType(this,"3");
                break;
            case -1:
                if (JCConstant.AUTO_FOCUS){
                    WccConfigure.setCameraFocusType(this,"1");
                }else {
                    WccConfigure.setCameraFocusType(this,"3");
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
            setContentView(R.layout.barcodescan_portrait_part);
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

        scan_info = (ImageView) findViewById(R.id.scan_info);
        scan_info.setImageBitmap(bmp_scan_info);

        if (ScanType.CONTINUOUS_SCHEDULE == scanType) {
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
        }

        if (ScanType.RETURNGOODSEXPRESS == scanType) {
            scan_info.setVisibility(View.GONE);
        }

        hasColor = true;
        if (scanType == ScanType.EXP || scanType == ScanType.CONTINUOUS_SCHEDULE
                || scanType == ScanType.FROMEXPOSURE || scanType == ScanType.FROMQRCODE
                || scanType == ScanType.PRICETREND || scanType == ScanType.RETURNGOODSEXPRESS) {
            hasColor = false;
        }

        setFlashImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (WccConstant.DEBUG)
            Log.d(TAG, "barcode onResume start");

        setFlashImage();

    }

    private void setFlashImage() {
        /**
         * 原闪光灯控制逻辑
         */
//        if (WccConfigure.checkSpecialModelNoFlash()) {
//            imgSwitchFlash.setVisibility(View.GONE);
//            return;
//        }
//
//        if (flashOnOff) {
//            imgSwitchFlash.setImageBitmap(bmp_flashOn);
//        } else {
//            imgSwitchFlash.setImageBitmap(bmp_flashOff);
//        }
    }

    private boolean select(String Tag) {
        if (WccConstant.DEBUG) Log.e(TAG, "select(SCAN)");

        if (Scan2Fragment.status != Scan2Fragment.STOPPED) {
            HardWare.ToastShort(this, "请稍等片刻再进行扫描！");
            finish();
            Scan2Fragment.status = Scan2Fragment.STOPPED;
            return false;
        }
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction v4ft = fm.beginTransaction();
            Fragment fragment = fm.findFragmentByTag(Tag);
            if (fragment == null) {
                fragment = Fragment.instantiate(BarcodeScan2Activity.this, Tag);
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

    private OnScanCompleteListener scanSubmitLossListener;

    /**
     * 将扫描数据传给报损Fragment的接口
     */
    public interface OnScanCompleteListener {

        /**
         * 传递条码的接口
         *
         * @param barcode
         * @param rainbowCode
         */
        void submitLossScanSuccess(String barcode, String rainbowCode);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置平滑调节zoom控件
     *
     * @param seekBarZoom
     */
    public void setSeekBar(final SeekBar seekBarZoom, final int currentZoom, final int maxZoom) {
        seekBarZoom.setMax(maxZoom);//最大的zoom
        final int spZoom = SpUtil.getInt(app, KEY_PART_CAMERA_ZOOM, -1);
        if (spZoom != -1){
            seekBarZoom.setProgress(spZoom);
            mainhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CameraManager.getInstance(app).setSmoothZoom(spZoom);
                }
            },300);
        }else {
            seekBarZoom.setProgress(currentZoom);//获取当前camera的真实zoom来显示
        }

        seekBarZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    CameraManager.getInstance(app).setSmoothZoom(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SpUtil.putInt(app,KEY_PART_CAMERA_ZOOM,seekBar.getProgress());
            }
        });
    }
}