package com.wochacha.scan;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.wochacha.scan.util.DataConverter;
import com.wochacha.scan.util.HardWare;
import com.wochacha.scan.util.ImagesManager;
import com.wochacha.scan.util.MessageConstant;

public abstract class DecodeThread extends Thread {
    String TAG = "DecodeThread";
    protected Handler handler;
    protected DataThread parent;
    protected final WccScanApplication mContext;
    protected volatile boolean quit = false;
    protected int threadType = 0;

    public static final int BARCODE_THREAD = 1;
    public static final int BLURBARCODE_THREAD = 2;
    public static final int IMAGE_THREAD = 4;
    public static final int QRCODE_THREAD = 5;
    public static final int HANCODE_THREAD = 6;
    public static final int LIGHT_THREAD = 7;

    public DecodeThread(WccScanApplication app, DataThread dataThread) {
        quit = false;
        mContext = app;
        parent = dataThread;
    }

    @SuppressLint("HandlerLeak")
    public void run() {
        Log.e("xsl", "ddddd");
//		try {
        try {
            if (threadType == BARCODE_THREAD) {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
            } else if (threadType == BLURBARCODE_THREAD) {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);
            } else if (threadType == IMAGE_THREAD) {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);
            } else if (threadType == QRCODE_THREAD) {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT + 1);
            } else if (threadType == HANCODE_THREAD) {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT + 2);
            } else if (threadType == LIGHT_THREAD) {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Looper.prepare();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    switch (msg.what) {
                        case MessageConstant.BarcodeDecodeMsg.DECODE:
                            Log.e("xsl", "aaaaa");
                            try {
                                decode(msg.obj, msg.arg1, msg.arg2);
                            } catch (OutOfMemoryError oom) {
                                removeMessages(MessageConstant.BarcodeDecodeMsg.DECODE);
                                oom.printStackTrace();
                                Toast.makeText(mContext, "警告！当前系统可用内存不足...无法识别！", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case MessageConstant.BarcodeDecodeMsg.QUIT_DECODE:
                            free();
                            handler = null;
                            quit = true;
                            removeMessages(MessageConstant.BarcodeDecodeMsg.DECODE);
                            Looper.myLooper().quit();
                            break;
                    }
                    super.handleMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Looper.loop();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
    }

    public static class DecodeResult {
        public Object obj;
        public String bmpName;
        public Bitmap bitmap;
    }

    protected void sendDecodeResult(WccResult obj, int decoder, Bitmap bmp) {
        if (quit) return;
        Log.e("lalala", "bmp width: " + bmp.getWidth() + "\t" + bmp.getHeight());
        DecodeResult result = new DecodeResult();

        String bmpName = makeBarcodeBitmap(obj, bmp);
//        result.bitmap = bmp;
        result.bitmap = ImagesManager.Rotate(bmp, 90);

        Log.e("xsl", "bmp name: " + bmpName + "\t" + (bmp != null));
        result.obj = obj;
        result.bmpName = bmpName;
        if (!quit) {
            HardWare.sendMessage(parent.getHandler(), MessageConstant.BarcodeDecodeMsg.DecodeSuccess, decoder, 0, result);
        }
    }

    //测试用,保存扫描图片，供算法调试
	/*public String makeBarcodeBitmap(WccResult obj, Bitmap bmp, String name) {
		String bmpName = null;
		if (bmp != null) {
			//String result = DecodeResultProcessor.convertByteToString(obj.result);
			//String md5 = DataConverter.getMD5(result.getBytes());
			
			Bitmap rotated;
		    if (isImageScan)
		    	rotated = ImagesManager.Rotate(bmp, 0);
        	else {
        		rotated = ImagesManager.Rotate(bmp, 90);
        	}
		    bmpName = ImagesManager.SaveBarcodeBitmap(rotated, "rainbow", name + ".jpg");
		    if (rotated != null)
		    	rotated.recycle();
			bmp.recycle();
			bmp = null;
			
			ImagesManager.getInstance().freeBitmap(bmpName + "_ScaleType_" + ScaleType.Normal);
		}
		
		return bmpName;
	}*/

    public String makeBarcodeBitmap(WccResult obj, Bitmap bmp) {
        String bmpName = null;
        if (bmp != null) {
            String result = DecodeResultProcessor.convertByteToString(obj.result);
            String md5 = DataConverter.getMD5(result.getBytes());

            Bitmap rotated;
            rotated = ImagesManager.Rotate(bmp, 90);
            bmpName = ImagesManager.SaveBarcodeBitmap(rotated, md5 + ".jpg");
            //todo recycle
//            if (rotated != null)
//                rotated.recycle();
//            bmp.recycle();
            bmp = null;
            //先不释放图片，先走通流程20160829
            //ImagesManager.getInstance().freeBitmap(bmpName + "_ScaleType_" + ScaleType.Normal);
        }

        return bmpName;
    }

    public Bitmap renderRGBBitmap(byte[] rgb, int w, int h, boolean noScale) {
        try {
            int[] pixels = new int[w * h];
            int out = 0;
            int in = 0;
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    int r = rgb[in] & 0xff;
                    int g = rgb[in + 1] & 0xff;
                    int b = rgb[in + 2] & 0xff;
                    pixels[out] = (0xff000000) | (r << 16) | (g << 8) | b;
                    in += 3;
                    out++;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            pixels = null;

            Bitmap newBmp = null;
            if (noScale)
                newBmp = Bitmap.createScaledBitmap(bitmap, w, h, true);
            else
                newBmp = Bitmap.createScaledBitmap(bitmap, w * 4, h, true);
            bitmap.recycle();
            return newBmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean decode(Object data, int width, int height) {
        if (quit) return false;

        boolean success = decodeBy(data, width, height);
        if (!success) {
            if (!quit) {

                if (threadType != LIGHT_THREAD) {
                    HardWare.sendMessage(parent.getHandler(), MessageConstant.BarcodeDecodeMsg.DecodeFail, threadType, 0);
                }
            }
        }

        data = null;
        return success;
    }

    protected abstract boolean decodeBy(Object data, int width, int height);

    protected void free() {
    }

    public Handler getHandler() {
        return handler;
    }
}
