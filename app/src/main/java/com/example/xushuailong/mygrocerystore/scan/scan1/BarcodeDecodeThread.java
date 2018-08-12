package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.graphics.Rect;
import android.util.Log;

import com.example.xushuailong.mygrocerystore.scan.util.Constant;
import com.example.xushuailong.mygrocerystore.scan.util.Constant.*;
import com.example.xushuailong.mygrocerystore.scan.util.HardWare;
import com.example.xushuailong.mygrocerystore.scan.util.MessageConstant;
import com.example.xushuailong.mygrocerystore.scan.util.WccConfigure;
import com.wochacha.scan.WccBarcode;
import com.wochacha.scan.WccResult;

public class BarcodeDecodeThread extends DecodeThread {
    public WccBarcode scanner = null; // gc_lib
    private int format;
    private Rect activeRect;
    private int num = 0;
    private int scanType = Constant.ScanType.ALL;
    private int scanMode;
    private BaseLuminanceSource source;
    private Rect rect1;
    private Rect rect2;
    private Rect rect3;
    private boolean colorOn;

    public BarcodeDecodeThread(WccScanApplication app, DataThread dataThread, int type, int mode, int threadType) {
        super(app, dataThread);
        scanType = type;
        scanMode = mode;
        this.threadType = threadType;
        if (mode == ScanMode.BARCODE) {
            TAG = "BarcodeDecodeThread";
        } else if (mode == ScanMode.BLURBARCODE) {
            TAG = "BlurBarcodeDecodeThread";
        } else if (mode == ScanMode.QRCODE) {
            TAG = "QRcodeDecodeThread";
        } else if (mode == ScanMode.LIGHTWATCHER) {
            TAG = "LightWatcherThread";
        } else {
            TAG = "ImageDecodeThread";
        }

        scanner = app.getScanner();
        num = 0;

        CameraManager manager = app.getCamera();
        format = manager.getPreviewFormat();
        activeRect = manager.getCameraRectFromScreenRect();
    }

    private boolean decodeByGClib(Object data, int width, int height, int mode, boolean hsv) {

        Log.e("xsl", "decodeByGClib: " + ((byte[]) data).length + "\t" + width + "\t" + height);
        try {
            WccResult rawResult_gc = null;
            Rect rect;
            byte[] yuv = null;
            byte[] rgb = null;
            int w = width;
            int h = height;

            colorOn = WccConfigure.getColorMode(mContext);

            if (isImageScan) {
                if (mode != ScanMode.LIGHTWATCHER) {
                    rgb = (byte[]) data;// 图片识别实际上是rgb数据
                    if (hsv)
                        BaseLuminanceSource.processData(rgb, w, h);
                    scanner.setRoteMode(0);

                    /**
                     * 从RGB获取y分量，灰度算法识别使用
                     */
                    yuv = BaseLuminanceSource.getYData(rgb, width, height);
                }
            } else {
                yuv = (byte[]) data;
                if (rect1 == null)
                    rect1 = new Rect((w - activeRect.bottom) & 0xfffffffe, activeRect.left & 0xfffffffe, (w - activeRect.top) & 0xfffffffe, activeRect.right & 0xfffffffe);
                rect = rect1;

                int sample = 1;
                if (mode == ScanMode.BARCODE || mode == ScanMode.BLURBARCODE) {
                    if (rect2 == null) {
                        int rw = (rect.width() - 60) / 2;
                        if (rw < 0) rw = 0;
                        rect2 = new Rect((rect.left + rw) & 0xfffffffe, rect.top & 0xfffffffe, (rect.right - rw) & 0xfffffffe, rect.bottom & 0xfffffffe);
                    }
                    rect = rect2;
                } else {
                    if (rect3 == null) {
                        int rh = (rect.height() - rect.width()) / 2;
                        if (rh < 0) rh = 0;
                        rect3 = new Rect(rect.left & 0xfffffffe, (rect.top + rh) & 0xfffffffe, rect.right & 0xfffffffe, (rect.bottom - rh) & 0xfffffffe);
                    }
                    rect = rect3;
                    sample = rect.width() / 200;
                }

                scanner.setRoteMode(1);
                if (source == null)
                    source = mContext.getCamera().buildLuminanceSource(yuv, w, h, rect);
                else
                    source.setData(yuv, w, h, rect);


                if (mode == ScanMode.BARCODE || mode == ScanMode.BLURBARCODE) {
                    rgb = source.getRGBMatrix(format, false);
                    yuv = source.getMatrix();
                } else {
                    rgb = source.subSampleRGB(source.getRGBMatrix(format, hsv), source.getWidth(), source.getHeight(), sample);
                }


                if (sample > 1) {
                    w = (source.getWidth() / sample) & 0xfffffffe;
                    h = (source.getHeight() / sample) & 0xfffffffe;
                } else {
                    w = source.getWidth();
                    h = source.getHeight();
                }
                Log.e("lalala", "sample: " + w + "\t" + h + "\t" + sample);
            }

            //上面预处理后得到的是YUV格式的Y分量图像数据

            if (mode == ScanMode.LIGHTWATCHER) {
                int lightJudgment = -1;//光线亮度判断，接口返回1-需要开闪光灯；0-不需要
                lightJudgment = scanner.checkout(rgb, w, h);
                scanner.setFlashFlag(lightJudgment);
//				if (lightJudgment == 1) {
//					HardWare.sendMessage(parent.getHandler(), BarcodeDecodeMsg.FlashOnRemind);
//				}
            } else {
                rawResult_gc = scanner.decode(rgb, yuv, w, h, mode, colorOn);
            }

            // 弹对话框
            if (mode == ScanMode.BARCODE || mode == ScanMode.BLURBARCODE) {
                if (rawResult_gc != null) {
                    Log.e("AN", "光线亮度返回值：" + scanner.getFlashFlag());
                    Log.e("AN", "barcode:" + new String(rawResult_gc.result));
                    Log.e("AN", "colorcode:" + new String(rawResult_gc.colorcode));
                    //Log.e("AN", "是否在列表:"+ isInList(new String(rawResult_gc.result)));
                    //Log.e("AN", "hasremind:" + scanner.hasRemindOpenFlash());
                    String colorCode = new String(rawResult_gc.colorcode);

                    //if (("single".equals(colorCode) || "2color".equals(colorCode) || "3color".equals(colorCode) || "imprvqlty".equals(colorCode))
                    // colorCode = 0,表示算法认为是黑白码，colorCode = 1，表示算法认为是彩虹码，但没识别出来
                    if (("0".equals(colorCode) || "1".equals(colorCode))
                            //&& isInList(new String(rawResult_gc.result))
                            //&& !scanner.hasRemindOpenFlash()
                            && scanner.getFlashFlag() == 1) {
                        HardWare.sendMessage(parent.getHandler(), MessageConstant.BarcodeDecodeMsg.FlashOnRemind);
                        return false;
                    }

                    /**
                     * V8.7
                     * 如果Ean-13识别出来了，返回2color,3color,imprvqlty,darkTooBright,再抓一帧数据重试，
                     * 设定尝试次数，超过次数按失败处理，走黑白码逻辑（暂不加次数控制，测试效果还可以）
                     *
                     */
//					if ("darkTooBright".equals(colorCode)
//							|| "2color".equals(colorCode)
//							|| "3color".equals(colorCode)
//							|| "imprvqlty".equals(colorCode)) {
//						return false;
//					}

                    if ("1".equals(colorCode)) {
                        return false;
                    }

                    /**
                     * 保存预览图片，供算法测试使用
                     * 暂保留代码
                     * 测试时需要手动在wochachacache目录新建一个rainbow文件夹，不然无法保存
                     */
//					if (rawResult_gc != null && rawResult_gc.result != null && rawResult_gc.result.length > 0) {
//						if ("single".equals(colorCode) || "2color".equals(colorCode) || "3color".equals(colorCode)) {
//							makeBarcodeBitmap(rawResult_gc, renderRGBBitmap(rgb, w, h, true), colorCode + "_" + w + "_" + h + "_" + System.currentTimeMillis());
//						}
//					}
                }
            }

            if (rawResult_gc != null && rawResult_gc.result != null && rawResult_gc.result.length > 0) {
                if ((ScanType.EXP == scanType || ScanType.RETURNGOODSEXPRESS == scanType) && rawResult_gc.type != BarcodeType.HZBAR_CODE39
                        && rawResult_gc.type != BarcodeType.HZBAR_CODE128 && rawResult_gc.type != BarcodeType.HZBAR_CODE93)
                    return false;

                if (mode == ScanMode.QRCODE || rawResult_gc.type == BarcodeType.HZBAR_QRCODE || rawResult_gc.type == BarcodeType.HZBAR_HANXIN || rawResult_gc.type == BarcodeType.HZBAR_CODE_DM)
                    sendDecodeResult(rawResult_gc, Constant.ScanResult.DecodeFromGcUNI, renderRGBBitmap(rgb, w, h, true));
                else
                    sendDecodeResult(rawResult_gc, Constant.ScanResult.DecodeFromGcUNI, null);

                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean decodeBy(Object data, int width, int height) {
        try {
            boolean result;
            if (isImageScan) {
                //对于图片识别，一维码和二维码识别在一个线程里做，无所谓性能，同时可以避免多线程处理图像数据导致互相干扰的问题
                result = decodeByGClib(data, width, height, ScanMode.ALLCODE, false);
                if (result == false) {
                    result = decodeByGClib(data, width, height, ScanMode.ALLCODE, true);
                }
            } else {
                num++;
                boolean hsv;
                if (num % 6 == 0)
                    hsv = true;
                else
                    hsv = false;
                result = decodeByGClib(data, width, height, scanMode, hsv);
            }

            return result;
        } catch (OutOfMemoryError oom) {
            oom.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//	public boolean isInList(String barcode) {
//		String[] rainbowList = scanner.getRainBowList();
//		if (rainbowList != null) {
//			return Arrays.asList(rainbowList).contains(barcode);
//		}
//		return false;
//	}


}
