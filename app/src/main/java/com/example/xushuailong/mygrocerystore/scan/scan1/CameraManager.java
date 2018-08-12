package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;


import com.example.xushuailong.mygrocerystore.scan.util.HardWare;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant.*;
import com.example.xushuailong.mygrocerystore.scan.util.Validator;
import com.example.xushuailong.mygrocerystore.scan.util.WccConfigure;
import com.example.xushuailong.mygrocerystore.utils.ScreenUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;


@SuppressWarnings("deprecation")
public class CameraManager {
    final static String TAG = "CameraManager";

    private Handler previewHandler;
    private Point screenResolution;
    private Point cameraResolution;
    private int previewMessage;
    private Handler autoFocusHandler;
    private int autoFocusMessage;
    private String previewFormatString;
    private int previewFormat;
    private boolean initialized;
    private boolean previewing;
    private Camera camera;
    private Rect rectCameraRotate;
    private Rect rectPartCameraRotate;
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private static boolean isAF = false;
    private Context app;
    private boolean hasTimer;
    private static volatile CameraManager instance;
    private byte[] buffer;
    private static boolean isPartCamera;
    private static int viewHeight;
    private static int scanViewHeight;
    public static boolean isReleased = true;

    private boolean checkData(byte[] data) {
        for (int i = 0; i < 160; i++) {
            if (data[i] != 0)
                return true;
        }
        return false;
    }

    private final Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.e("xsl", "data length: " + data.length);
//            try {
            if (previewHandler != null && data != null) {
                if (data.length < 25600) {
                    previewHandler = null;
                    return;
                }

                Message message = previewHandler.obtainMessage(
                        previewMessage, cameraResolution.x,
                        cameraResolution.y, data);
                message.sendToTarget();
                previewHandler = null;

                if (hasTimer && checkData(data)) {
                    HardWare.sendMessage(ScanFragment.captureHandler, MessageConstant.BarcodeDecodeMsg.StopTimer);
                    hasTimer = false;
                }
            }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    };

    private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
//            try {
//                Log.e("oak", "onAutoFocus");
//                if (autoFocusHandler != null) {
//                    Message message = autoFocusHandler.obtainMessage(
//                            autoFocusMessage, success);
//                    autoFocusHandler.sendMessageDelayed(message, 500L);
//                    autoFocusHandler = null;
//                }
//            } catch (Exception e) {
//            }
        }
    };

    public static CameraManager getInstance(Application wccApp) {
        if (instance == null) {
            synchronized (CameraManager.class) {
                if (instance == null)
                    instance = new CameraManager(wccApp);
            }
        }
        return instance;
    }

    public static CameraManager getInstance(Application wccApp, boolean isPartCamer) {
        isPartCamera = isPartCamer;
        return getInstance(wccApp);
    }

    public static CameraManager getInstance(Application wccApp, boolean isPartCamer, int vHeight, int scanVHeight) {
        isPartCamera = isPartCamer;
        viewHeight = vHeight;
        scanViewHeight = scanVHeight;
        return getInstance(wccApp);
    }

    public static void free() {
        if (instance != null)
            instance.buffer = null;
        instance = null;
    }

    private CameraManager(Context context) {
        app = context;
        camera = null;
        initialized = false;
        previewing = false;
        getScreenResolution();
        rectCameraRotate();
//        rectActivityRotate();
        rectPartCameraRotate();
    }

    public Camera getCamera() {
        return camera;
    }

    public void openDriver() throws IOException {
        if (camera == null) {
            try {
                camera = getCamera(WccConfigure.getCameraSelect(app));
                if (camera == null)
                    camera = Camera.open();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (camera == null) {
                throw new NullPointerException("camera is null");
            }

            try {
//                if (!HardWare.needRotateActivity()) {
                /*	if (Build.MODEL.equals("U558"))
                        rotate(270);
					else*/
                rotate(90);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (!initialized) {
            initialized = true;
            getScreenResolution();
        }
        setCameraParameters();
    }

    public void closeDriver() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        initialized = false;
        previewing = false;
    }

    public boolean startPreview(SurfaceHolder holder) {
        if (camera != null && !previewing) {
            try {
                //三星的某些手机，需要进行此类操作，才能启动预览
                try {
                    Class<?> cls = Class.forName("android.app.admin.DevicePolicyManager");
                    @SuppressLint("WrongConstant")
                    Object obj = app.getSystemService("device_policy");
                    ComponentName adminame = new ComponentName(app, CameraManager.class);
                    boolean setcamera = true;
                    Method mymethod = cls.getMethod("setAllowCamera", ComponentName.class, boolean.class);
                    Object args[] = new Object[2];
                    args[0] = adminame;
                    args[1] = setcamera;
                    mymethod.invoke(obj, args);
                } catch (Exception e) {
//					e.printStackTrace();
                }

                camera.setPreviewDisplay(holder);
                camera.startPreview();
                previewing = true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    // gc_lib
    public boolean isPreview() {
        if (camera != null && previewing)
            return true;
        else
            return false;
    }

    // end

    public void stopPreview() {
        if (camera != null && previewing) {
            camera.stopPreview();
            turnOnOffFlash(false);
            previewHandler = null;
            previewMessage = 0;
            autoFocusHandler = null;
            previewing = false;
        }
    }

    public void turnOnOffFlash(boolean onOff) {
        try {
            String flashMode = WccConfigure.getCamFlashMode(app);
            if (flashMode.equals("5")) {
                Class<?> sm = Class.forName("android.os.ServiceManager");
                Object hwBinder = sm.getMethod("getService", String.class).invoke(null, "hardware");
                Class<?> hwsstub = Class.forName("android.os.IHardwareService$Stub");
                Method asInterface = hwsstub.getMethod("asInterface", IBinder.class);
                Object svc = asInterface.invoke(null, (IBinder) hwBinder);
                Class<? extends Object> proxy = svc.getClass();
                Method setFlashlightEnabled = proxy.getMethod("setFlashlightEnabled", boolean.class);
                setFlashlightEnabled.invoke(svc, onOff);
            } else if (flashMode.equals("6")) {
                // This is a hack to turn the flash off on the Samsung Galaxy
                // and the Behold II
                // as advised by Samsung, neither of which respected the
                // official parameter.
                Parameters parameters = camera.getParameters();
                if (onOff == true) {
                    parameters.set("flash-value", 1);
                } else {
                    parameters.set("flash-value", 2);
                }
                camera.setParameters(parameters);
            } else {
                Parameters parameters = camera.getParameters();
                if (onOff) {
                    if (flashMode.equals("1"))
                        parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
                    else if (flashMode.equals("2"))
                        parameters.setFlashMode(Parameters.FLASH_MODE_ON);
                    else if (flashMode.equals("3"))
                        parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);
                    else if (flashMode.equals("4"))
                        parameters.setFlashMode(Parameters.FLASH_MODE_RED_EYE);
                    else
                        parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                } else
                    parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
        }
    }

    public void requestPreviewFrame(Handler handler, int message, boolean needTimer) {
        if (camera != null && previewing) {
            previewHandler = handler;
            previewMessage = message;

            if (buffer == null) {
                int size = cameraResolution.x * cameraResolution.y * 2;
                buffer = new byte[size];
            }

            camera.addCallbackBuffer(buffer);
            camera.setPreviewCallbackWithBuffer(previewCallback);
            if (needTimer) {
                HardWare.sendMessage(ScanFragment.captureHandler, BarcodeDecodeMsg.StartTimer);
                hasTimer = true;
            }
        }
    }

    // 不要调用本函数，有的手机多次扫描后会崩溃
    public void cancelAutoFocus(Camera c) {
        try {
            c.cancelAutoFocus();
        } catch (Exception e) {
        }
    }

    public void requestAutoFocus(Handler handler, int message)
            throws IOException {
        if (camera != null && previewing) {
            autoFocusHandler = handler;// zxing_lib
            autoFocusMessage = message;// zxing_lib
            // 1:auto detect 2:autofocus 3:no autofocus
            //Todo
            Log.e("oak", "app: " + WccConfigure.getCamAutoFocus(app) + "\t" + isAF);
            if (!"3".equals(WccConfigure.getCamAutoFocus(app))) {
                if ("2".equals(WccConfigure.getCamAutoFocus(app)) || isAF) {
                    try {
                        if (Build.MODEL.equals("OMAP_SS"))
                            cancelAutoFocus(camera);
                        camera.autoFocus(autoFocusCallback);

                        if (autoFocusHandler != null) {
                            Message message1 = autoFocusHandler.obtainMessage(
                                    autoFocusMessage, true);
                            autoFocusHandler.sendMessageDelayed(message1, 500L);
                            autoFocusHandler = null;
                        }

                    } catch (Exception e) {
                        // app.getDataProvider().setIsAutoFocus(false);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * return rect or null
     */
    public Rect getCameraRectFromScreenRect() {
        Rect screenRect = getFramingRect();

        double xScale = ((double) cameraResolution.x) / ((double) screenResolution.x);
        double yScale = ((double) cameraResolution.y) / ((double) screenResolution.y);

        return new Rect(((int) (screenRect.left * yScale) / 2) * 2,
                ((int) (screenRect.top * xScale) / 2) * 2,
                ((int) (screenRect.right * yScale) / 2) * 2,
                ((int) (screenRect.bottom * xScale) / 2) * 2);
    }

    private void rectCameraRotate() {
        if (rectCameraRotate == null) {


//            int leftOffset, topOffset;
//            int width, height;
//
//            width = screenResolution.y; //540
//            height = (int) (width * 0.65f); //351
//
//            topOffset = (screenResolution.x - height) / 2;//304
//            if (topOffset < 0) { //need reset ScreenScale, update init datas
//                int x = screenResolution.x;
//                int y = screenResolution.y;
//                screenResolution.x = y;
//                screenResolution.y = x;
//                width = screenResolution.y;
//                height = (int) (width * 0.65f);
//                topOffset = (screenResolution.x - height) / 2;
//            }
//            leftOffset = (screenResolution.y - width) / 2;
//
//            rectCameraRotate = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);


            int w = ScreenUtil.getScreenWidth(app);
            int h = ScreenUtil.getScreenHeight(app);
            Rect frameRect = new Rect();
            frameRect.left = (int) (w * 0.15);
            frameRect.top = (int) ((h - (w * 0.7)) / 2);
            frameRect.right = w - frameRect.left;
            frameRect.bottom = h - frameRect.top;
            rectCameraRotate = frameRect;
            Log.e("afei", "screen: " + ScreenUtil.getScreenHeight(app));
            Log.e("afei", "rectCameraRotate: " + rectCameraRotate.width() + "\t" + rectCameraRotate.height());
            Log.e("afei", "rectCameraRotate: " + rectCameraRotate.top + "\t" + rectCameraRotate.bottom);

            SharedPreferences sharepre = PreferenceManager.getDefaultSharedPreferences(app);
            Editor editor = sharepre.edit();
            editor.putInt("camera_rotate_left", rectCameraRotate.left);
            editor.putInt("camera_rotate_top", rectCameraRotate.top);
            editor.putInt("camera_rotate_right", rectCameraRotate.right);
            editor.putInt("camera_rotate_bottom", rectCameraRotate.bottom);
            editor.commit();

        }
    }

    private void rectPartCameraRotate() {
        if (rectPartCameraRotate == null && isPartCamera) {
            int height = ScreenUtil.dip2px(app, viewHeight);
            int cameraHeight = ScreenUtil.dip2px(app, scanViewHeight);
            int screenWidth = screenResolution.y;
            int screenHeight = screenResolution.x;
            int cameraLeft;
            int cameraTop;
            int cameraRight;
            int cameraBottom;

            cameraLeft = 0;
            cameraTop = screenHeight - ((height - cameraHeight) / 2 + cameraHeight);
            cameraRight = screenWidth;
            cameraBottom = screenHeight - (height - cameraHeight) / 2;
            rectPartCameraRotate = new Rect(cameraLeft, cameraTop, cameraRight, cameraBottom);

        }
    }


    public Rect getFramingRect() {
        if (isPartCamera) {
            rectPartCameraRotate();
            return rectPartCameraRotate;
        } else {
            rectCameraRotate();
            return rectCameraRotate;
        }
    }

    public int getPreviewFormat() {
        return previewFormat;
    }

    // end
    public BaseLuminanceSource buildLuminanceSource(byte[] data, int width, int height, Rect rect) {
        switch (previewFormat) {
            case ImageFormat.NV21:
            case ImageFormat.NV16:
            case ImageFormat.YV12:
                return new PlanarYUVLuminanceSource(data, width, height, rect);
            case ImageFormat.YUY2:
                return new InterleavedYUV422LuminanceSource(data, width, height, rect);
            default:
                if (previewFormatString.equals("yuv422i-yuyv")) {
                    return new InterleavedYUV422LuminanceSource(data, width, height, rect);
                }
                return new PlanarYUVLuminanceSource(data, width, height, rect);
        }
    }

    // end
    private void setCameraParameters() {
        try {
            Parameters parameters = camera.getParameters();
            isAF = isAutoFocus(parameters);

            List<Integer> list = parameters.getSupportedPreviewFormats();
            for (Integer i : list) {
                Log.e(TAG, "---------------getSupportedPreviewFormats:" + i);
            }

//			parameters.setPreviewFormat(ImageFormat.YV12);

            previewFormat = parameters.getPreviewFormat();

            previewFormatString = parameters.get("preview-format");
            String previewSizeValueString = parameters.get("preview-size-values");

            if (previewSizeValueString != null) {
                cameraResolution = findBestPreviewSizeValue(
                        previewSizeValueString, screenResolution);
            }
            if (cameraResolution == null) {
                Camera.Size defaultSize = parameters.getPreviewSize();
                cameraResolution = new Point(defaultSize.width,
                        defaultSize.height);
                // cameraResolution = new Point(screenResolution.x >> 3 << 3,
                // screenResolution.y >> 3 << 3);
            }
            parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);

//			if (WccConstant.dist.equals("f_yinkete_2012")) {
//				Class<? extends Parameters> cl = parameters.getClass();
//				try {
//					Method m = cl.getMethod("setCameraSensor", (Class[]) null);
//					if (m != null) {
//						m.invoke(parameters, (int) 2);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}

            // 某些手机会因此而导致setParameters失败，不要执行！！！！！
            // // This is the standard setting to turn the flash off that all
            // devices should honor.
            // parameters.set("flash-mode", "off");

//            setZoom(parameters);

            List<String> focusModes = parameters.getSupportedFocusModes();

            String rotate = WccConfigure.getCameraRotate(app);
            if ("1".equals(rotate)) {
                rotate(90);
            } else if ("2".equals(rotate)) {
                rotate(180);
            } else if ("3".equals(rotate)) {
                rotate(270);
            }
            camera.setParameters(parameters);
            // MB525，android 4.0，设置的预览分辨率 ＝＝ 实际的预览分辨率 !＝ 读取的预览分辨率
            if (!Build.MODEL.equals("MB525")) {
                parameters = camera.getParameters();
                cameraResolution.x = parameters.getPreviewSize().width;
                cameraResolution.y = parameters.getPreviewSize().height;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // zxing 判断合适预览尺寸
    private static Point findBestPreviewSizeValue(CharSequence previewSizeValueString, Point screenResolution) {
        int bestX = 0;
        int bestY = 0;
        int diff = Integer.MAX_VALUE;
        for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {
            previewSize = previewSize.trim();
            int dimPosition = previewSize.indexOf('x');
            if (dimPosition < 0) {
                continue;
            }

            int newX;
            int newY;
            try {
                newX = Integer.parseInt(previewSize.substring(0, dimPosition));
                newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
            } catch (NumberFormatException nfe) {
                continue;
            }

            int newDiff;
//			if (screenResolution.x + screenResolution.y >= 1600 + 960)
//				newDiff = Math.abs(newX - (screenResolution.x * 2 / 3)) + Math.abs(newY - (screenResolution.y * 2 / 3));
//			else
            newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
            if (newDiff == 0) {
                bestX = newX;
                bestY = newY;
                break;
            } else if (newDiff < diff) {
                bestX = newX;
                bestY = newY;
                diff = newDiff;
            }
        }

        if (bestX > 0 && bestY > 0) {
            return new Point(bestX, bestY);
        }
        return null;
    }

    private void getScreenResolution() {
        if (screenResolution == null) {
            // 旋转后的屏幕大小
            screenResolution = new Point(HardWare.getScreenHeight(app), HardWare.getScreenWidth(app));
        }
    }

    public boolean isAutoFocus(Parameters parameters) {
        boolean result = true;
        try {
            String isFocus = parameters.getFocusMode();
            if (Validator.isEffective(isFocus)) {
                if (isFocus.equals(Parameters.FOCUS_MODE_EDOF) || isFocus.equals(Parameters.FOCUS_MODE_FIXED)
                        || isFocus.equals(Parameters.FOCUS_MODE_INFINITY)) {
                    result = false;
                } else {
                    result = true;
                }
            } else {
                result = WccConfigure.getIsAutoFocus(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    // 0: CAMERA_FACING_BACK
    // 1: CAMERA_FACING_FRONT
    public Camera getCamera(String CameraChoose) {
        try {
            if (Build.VERSION.SDK_INT >= 9) {
                int choice = Integer.parseInt(CameraChoose);
                int CameraNums = Camera.getNumberOfCameras();
                for (int i = 0; i < CameraNums; i++) {
                    CameraInfo obj = new CameraInfo();
                    Camera.getCameraInfo(i, obj);
                    int pos = obj.facing;
                    if (pos == choice) {
                        camera = Camera.open(i);
                        return camera;
                    }
                }
            } else
                return null;
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
        return null;
    }

    public void rotate(int degree) {
        try {
            if (Build.VERSION.SDK_INT >= 8) {
                camera.setDisplayOrientation(degree);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
