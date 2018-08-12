package com.example.xushuailong.mygrocerystore.scan.scan1;


import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.xushuailong.mygrocerystore.scan.util.Constant;
import com.example.xushuailong.mygrocerystore.scan.util.Constant.*;
import com.example.xushuailong.mygrocerystore.scan.util.HardWare;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant.*;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant;
import com.example.xushuailong.mygrocerystore.scan.util.WccConfigure;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import com.example.xushuailong.mygrocerystore.R;

public class ScanFragment extends Fragment implements SurfaceHolder.Callback {
    private final String TAG = "ScanFragment";
    private ViewfinderView viewfinderView;
    private CameraPreview surfaceView;
    private boolean flashOnOff = false;
    private DataThread decodeThread;
    private State state;
    private int scanType;
    public static CaptureActivityHandler captureHandler;
    private WccScanApplication app;
    private boolean needPreview = true;
    private boolean isPaused = false;
    private boolean success = false;
    private CameraManager cameraManager;

    public static final int RUNNING = 0;
    public static final int STOPPING = 1;
    public static final int STOPPED = 2;
    public static int status = STOPPED;

    private static enum State {
        PREVIEW, SUCCESS, DONE
    }

    private Timer dataTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WccConfigure.setFlashHasOnRemind(getActivity(), false);
        if (getArguments() != null) {
            scanType = getArguments().getInt("scanType");
        }

        Activity act = getActivity();
        HardWare.getScreenHeight(act);
        HardWare.getScreenWidth(act);
        app = (WccScanApplication) act.getApplication();
        cameraManager = CameraManager.getInstance(app,false);
        Window window = act.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        success = false;

        needPreview = true;

        status = RUNNING;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (container == null) return null;

        View root;


        root = inflater.inflate(R.layout.scanfragment, container, false);
        findViews(root);

        return root;
    }

    private void findViews(View view) {
        surfaceView = (CameraPreview) view.findViewById(R.id.preview_view);
        viewfinderView = (ViewfinderView) view.findViewById(R.id.viewfinder_view);
        viewfinderView.setVisibility(View.GONE);

        captureHandler = null;

        initSurfaceView();
    }

    private void stopPreview() {
        if (flashOnOff) {
            app.getCamera().turnOnOffFlash(false);
            flashOnOff = !flashOnOff;
        }
        cameraManager.isReleased = true;
        cameraManager.getCamera().setPreviewCallback(null);
        app.getCamera().stopPreview();
        close();
        stopThread();
    }

    private void close() {
        app.getCamera().closeDriver();
    }

    private class CaptureActivityHandler extends Handler {
        public CaptureActivityHandler() {
            state = State.SUCCESS;
            decodeThread = new DataThread(app, scanType, false);
            decodeThread.start();
            decodeThread.setInvokerHandler(this);
        }

        public void handleMessage(Message message) {
            try {
                super.handleMessage(message);
                switch (message.what) {
                    case MessageConstant.SleepDown:
                    case MessageConstant.BarcodeDecodeMsg.CloseCamera:
                        stopPreview();
                        break;
                    case BarcodeDecodeMsg.ColorOn:
                        viewfinderView.setColorOnOff(true);
                        break;
                    case BarcodeDecodeMsg.ColorOff:
                        viewfinderView.setColorOnOff(false);
                        break;
                    case BarcodeDecodeMsg.FlashOff:
                        flashOnOff = true;
                        app.getCamera().turnOnOffFlash(false);
                        flashOnOff = !flashOnOff;
                        break;
                    case BarcodeDecodeMsg.FlashOn:
                        flashOnOff = false;
                        app.getCamera().turnOnOffFlash(true);
                        flashOnOff = !flashOnOff;
                        break;
                    case BarcodeDecodeMsg.FlashOnRemind:

                        break;
                    case BarcodeDecodeMsg.AUTOFOCUS:
                        if (state == State.PREVIEW) {
                            Log.e("oak","BarcodeDecodeMsg.AUTOFOCUS");
                            try {
                                app.getCamera().requestAutoFocus(this, BarcodeDecodeMsg.AUTOFOCUS);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case BarcodeDecodeMsg.DecodeSuccess:
                        if (success == true && Constant.ScanType.CONTINUOUS_ONE != scanType && ScanType.CONTINUOUS_SCHEDULE != scanType)
                            break;
                        success = true;
                        state = State.SUCCESS;

                        HardWare.sendMessage(BarcodeScanActivity.getMainHandler(), BarcodeDecodeMsg.DecodeSuccessScan, message.arg1, message.arg2, message.obj);
                        break;
                    case BarcodeDecodeMsg.StartTimer:
                        dataTimer = new Timer();
                        dataTimer.schedule(new DataTimerTask(), 4000);
                        break;
                    case BarcodeDecodeMsg.StopTimer:
                        if (dataTimer != null) {
                            dataTimer.cancel();
                            dataTimer = null;
                        }
                        break;
                    case BarcodeDecodeMsg.CameraNoData:
                        final String title = (String) message.obj;
                        showDialog(title);
                        break;
                    case BarcodeDecodeMsg.RestartPreviewAndDecode:
                        restartPreviewAndDecode();
                        break;
                    case BarcodeDecodeMsg.DecodeFail:
                        state = State.PREVIEW;
                        app.getCamera().requestPreviewFrame(decodeThread.getHandler(), BarcodeDecodeMsg.DECODE, false);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void quitSynchronously() {
            state = State.DONE;
            //  if(IsLandScape.GetIsLandScape()){
            removeMessages(BarcodeDecodeMsg.DecodeSuccess);
            removeMessages(BarcodeDecodeMsg.DecodeFail);
            //  }
            HardWare.sendMessage(decodeThread.getHandler(), BarcodeDecodeMsg.QUIT_DECODE);
            try {
                decodeThread.join();
            } catch (InterruptedException e) {
            }
            decodeThread = null;
        }
    }

    private void stopThread() {
        if (captureHandler != null) {
            captureHandler.quitSynchronously();
            captureHandler = null;
        }
    }

    @SuppressWarnings("deprecation")
    private void initSurfaceView() {

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        //HardWare.sendMessage(ExpressInquiryFragment.getHandler(),  MessageConstant.CLOSE_DIALOG);
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        viewfinderView.setVisibility(View.VISIBLE);
    }

    @Override
    public void surfaceCreated(final SurfaceHolder surfaceHolder) {

        if (isPaused) {
            return;
        }

        initCamera(surfaceHolder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        status = STOPPING;

        stopDrawViewFinder();
        stopPreview();
        if (dataTimer != null) {
            dataTimer.cancel();
            dataTimer = null;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
        success = false;

    }

    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
    }

    private void openCamera() {
        cameraManager.isReleased = false;
        if (needPreview == false)
            return;
        try {
            if (HardWare.needRotateActivity())
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            app.getCamera().openDriver();
        } catch (Exception e) {
            e.printStackTrace();
            showDialog("无法打开相机？");
            needPreview = false;
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (needPreview == false)
            return;

        openCamera();

        if (captureHandler == null) {
            captureHandler = new CaptureActivityHandler();
        }else {
        }


        boolean ret = app.getCamera().startPreview(surfaceHolder);
        if (ret == false)
            HardWare.sendMessage(captureHandler, BarcodeDecodeMsg.CameraNoData, "无法启动预览？");
        else
            restartPreviewAndDecode();

        surfaceView.setCamera(cameraManager.getCamera());
    }

    private void showDialog(String title) {
//		Intent intent = new Intent(getActivity(), PopMessageView.class);
//    	intent.putExtra("Title", title);
//    	intent.putExtra("Message", "<br>请检查手机中应用权限控制，允许我查查使用相机。点击<font color=\"#ff0000\">权限设置</font>, 了解手机权限设置方法<br>");
//		intent.putExtra("PopType", "2");
//    	startActivity(intent);
        HardWare.ToastShort(getActivity(), title);
    }

    private class DataTimerTask extends TimerTask {
        @Override
        public void run() {
            HardWare.sendMessage(captureHandler, BarcodeDecodeMsg.CameraNoData, "无法获取图像数据？");
        }
    }

    private void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void stopDrawViewFinder() {
        viewfinderView.stopDraw();
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS && decodeThread != null && captureHandler != null) {
            state = State.PREVIEW;
            try {
                Log.e("oak","restart preview and decode");
                if (decodeThread.getHandler() == null)
                    Thread.sleep(100);
                app.getCamera().requestPreviewFrame(decodeThread.getHandler(), BarcodeDecodeMsg.DECODE, true);
                HardWare.sendMessageDelayed(captureHandler, BarcodeDecodeMsg.AUTOFOCUS, 10);
            } catch (Exception e) {
                Log.e("oak","print stack trace!");
                e.printStackTrace();
            }
            drawViewfinder();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        HardWare.getInstance(getActivity().getApplicationContext()).UnRegisterHandler(this.hashCode());
    }

    @Override
    public void onDetach() {
        super.onDetach();

        status = STOPPED;
    }
}