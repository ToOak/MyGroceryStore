package com.wochacha.scan;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.xushuailong.mygrocerystore.scan.util.Constant;
import com.example.xushuailong.mygrocerystore.scan.util.WccConfigure;


public class WccBarcode {
    public static boolean isLibOk = true;
    private static volatile WccBarcode instance;
    // 用于标识只扫描彩虹码，提高识别效率
    public static boolean rainbowOnly = false;

    private Context context;
    private static boolean libInited = false;
    private static int initResult = 1;
    private int rotateMode;
    private boolean hasHan = false;

    private volatile int flashFlag = -1;//算法返回0（不需要闪光灯）或1（需要打开闪光灯）

    public WccBarcode(Context con) {
        this.rotateMode = 1;
        context = con;
        if (libInited == false) {
            libInited = true;
            try {
                initResult = wccInit();
            } catch (Exception e) {
            } catch (UnsatisfiedLinkError e) {
            }
        }
    }

    public static WccBarcode getInstance(Context con) {
        if (instance == null) {
            synchronized (WccBarcode.class) {
                if (instance == null)
                    instance = new WccBarcode(con);
            }
        }
        return instance;
    }

    public static WccBarcode getInstance() {
        return instance;
    }

    public static void free() {
        if (instance != null)
            instance.wccRelease();
        instance = null;
        isLibOk = true;
        libInited = false;
        initResult = 1;
    }

    public int getInitResult() {
        return initResult;
    }

    public void setRoteMode(int rotate) {
        this.rotateMode = rotate;
    }

    public WccResult decode(byte[] rgb, byte[] yuv, int width, int height, int mode, boolean colorOn) {

        Log.e("lalala", "decode: " + width + "\t" + height);
        try {
            hasHan = WccConfigure.isHxcodeOpen(context);

            WccResult result;
            if (mode == Constant.ScanMode.BARCODE) {
//				if (colorOn)
//					result = wccColorInput(data, rotateMode, width, height, 0);
//				else
//					result = wccGrayInput(data, rotateMode, width, height);
                /**
                 * 先用彩虹码算法解，如解不出再用普通算法，在普通算法中过滤EAN-13
                 */
                result = wccColorInput(rgb, rotateMode, width, height, 0);


                if (result.flag != 2) {
                    result = wccGrayInput(yuv, rotateMode, width, height);
                    if (rainbowOnly) {
                        if (result.flag == 2) {
                            if (result.type == Constant.BarcodeType.HZBAR_EAN13) {
                                return null;
                            }
                        }
                    }
                }
            } else if (mode == Constant.ScanMode.BLURBARCODE) {
//				if (colorOn) 
//					result = wccColorInput(data, rotateMode, width, height, 1);
//				else
//					result = wccGrayBlurInput(data, rotateMode, width, height);
                //模糊识别去掉彩虹码算法，内部也是清晰识别方式
                //result = wccColorInput(rgb, rotateMode, width, height, 1);

                //if (result.flag != 2) {
                result = wccGrayBlurInput(yuv, rotateMode, width, height);
                //}
                if (rainbowOnly) {
                    if (result.flag == 2) {
                        if (result.type == Constant.BarcodeType.HZBAR_EAN13) {
                            return null;
                        }
                    }
                }
            } else if (mode == Constant.ScanMode.QRCODE) {
//				if (colorOn) {
//					return null;
//				}
                result = wcc2Dinput(rgb, width, height);
                if (result.flag != 2 && hasHan) {
                    result = wccHanXin(rgb, width, height);
                    if (result.flag == 2)
                        result.type = Constant.BarcodeType.HZBAR_HANXIN;
                }
            } else {
                result = wccColorInput(rgb, rotateMode, width, height, 0);
                // 去除彩虹码模糊识别
//				if (result.flag != 2)
//					result = (WccResult)wccColorInput(rgb, rotateMode, width, height, 1);
                // 彩虹码算法的图片识别效果一般，如果彩虹码算法认为是黑白码，则用普通算法去识别
                if (result.flag != 2 || (result.flag == 2 && "0".equals(new String(result.colorcode))))
                    result = wccGrayInput(yuv, rotateMode, width, height);
                if (result.flag != 2)
                    result = wccGrayBlurInput(yuv, rotateMode, width, height);
                if (result.flag != 2)
                    result = wcc2Dinput(rgb, width, height);
                if (result.flag != 2 && hasHan) {
                    result = wccHanXin(rgb, width, height);
                    if (result.flag == 2)
                        result.type = Constant.BarcodeType.HZBAR_HANXIN;
                }
            }

            if (result.flag == 2) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (UnsatisfiedLinkError e) {
        }
        return null;
    }

    /**
     * 光线明暗判断
     *
     * @param rgb
     * @param width
     * @param height
     * @return
     */
    public int checkout(byte[] rgb, int width, int height) {
        try {
            /**
             * 最后一个参数为经验值
             */

            return wccCheckout(rgb, width, height, 25);
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
    }

    public native int deliverColorCode(byte[] colorCodeList, int len);

    public native int wccCheckout(byte[] data, int dataWidth, int dataHeight, int threshold);

    public native int wccInit() throws Exception;

    public native int wccRelease();

    public native WccResult wcc2Dinput(byte[] data, int dataWidth, int dataHeight);

    public native WccResult wccHanXin(byte[] data, int dataWidth, int dataHeight);

    public native WccResult wccGrayInput(byte[] data, int rotate, int dataWidth, int dataHeight);

    public native WccResult wccGrayBlurInput(byte[] data, int rotate, int dataWidth, int dataHeight);

    public native WccResult wccColorInput(byte[] data, int rotate, int dataWidth, int dataHeight, int mode);

    public native byte[] enReq(byte[] input, int len);

    public native byte[] deRes(byte[] input, int len);


    public native String conv(byte[] b, int index, int n);

    static {
        try {
            System.loadLibrary("gcbarcode_k");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            isLibOk = false;
        } finally {
            Log.e("xsl", "isLibOk: " + isLibOk);
        }
    }

//	public String[] getRainBowList() {
//		return WccConfigure.getRainbowCodeListArray(context);
//	}
//
//	public boolean hasRemindOpenFlash() {
//		return WccConfigure.isFlashHasOnRemind(context);
//	}

    public int getFlashFlag() {
        return flashFlag;
    }

    public void setFlashFlag(int flashFlag) {
        this.flashFlag = flashFlag;
    }

}