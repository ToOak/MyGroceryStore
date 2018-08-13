package com.wochacha.scan;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.wochacha.scan.util.Constant;
import com.wochacha.scan.util.Constant.*;
import com.wochacha.scan.util.HardWare;
import com.wochacha.scan.util.MessageConstant;
import com.wochacha.scan.util.MessageConstant.*;

@SuppressLint("HandlerLeak")
public class DataThread extends Thread {
    private Handler handler;
    private Handler Invokerhandler;
    private final WccScanApplication mContext;

    private BarcodeDecodeThread barcodeThread;
    private BarcodeDecodeThread barcodeBlurThread;
    private BarcodeDecodeThread barcode2DThread;
    private BarcodeDecodeThread barcodeAllThread;
    private BarcodeDecodeThread lightWatcherThread;

    private int scanType = Constant.ScanType.ALL;
    private int threadNum = 0;


    private boolean isImageScan = false;

    public DataThread(WccScanApplication app, int scanType, boolean imageScan) {
        mContext = app;
        this.scanType = scanType;
        isImageScan = imageScan;
        barcodeThread = null;
        barcodeBlurThread = null;
        barcode2DThread = null;
        barcodeAllThread = null;
        lightWatcherThread = null;
    }

    public void setInvokerHandler(Handler invoker) {
        Invokerhandler = invoker;
    }

    public void run() {
        try {
            switch (scanType) {
                case ScanType.ALL:
                case ScanType.FROMEXPOSURE:
                case ScanType.FROMQRCODE:
                    if (!isImageScan)
                        barcode2DThread = new BarcodeDecodeThread(mContext, DataThread.this, scanType, ScanMode.QRCODE, DecodeThread.QRCODE_THREAD);
                case ScanType.PRICETREND:
                case ScanType.EXP:
                case ScanType.RETURNGOODSEXPRESS:
                case ScanType.CONTINUOUS_ONE:
                case ScanType.CONTINUOUS_SCHEDULE:
                    if (!isImageScan) {
                        barcodeThread = new BarcodeDecodeThread(mContext, DataThread.this, scanType, ScanMode.BARCODE, DecodeThread.BARCODE_THREAD);
                        barcodeBlurThread = new BarcodeDecodeThread(mContext, DataThread.this, scanType, ScanMode.BLURBARCODE, DecodeThread.BLURBARCODE_THREAD);
                        // 不用新写一个线程，直接使用解码线程，也要对数据进行处理，也要传给算法，实质类似解码
                        lightWatcherThread = new BarcodeDecodeThread(mContext, DataThread.this, scanType, ScanMode.LIGHTWATCHER, DecodeThread.LIGHT_THREAD);
                    }
                    if (isImageScan)
                        barcodeAllThread = new BarcodeDecodeThread(mContext, DataThread.this, scanType, ScanMode.ALLCODE, DecodeThread.IMAGE_THREAD);
                    break;
            }

            threadNum = 0;
            if (WccBarcode.isLibOk) {
                if (barcodeThread != null) {
                    barcodeThread.start();
                    threadNum++;
                }
                if (barcodeBlurThread != null) {
                    barcodeBlurThread.start();
                    threadNum++;
                }
                if (barcode2DThread != null) {
                    barcode2DThread.start();
                    threadNum++;
                }
                if (barcodeAllThread != null) {
                    barcodeAllThread.start();
                    threadNum++;
                }
                if (lightWatcherThread != null) {
                    lightWatcherThread.start();
                }
            }

            Looper.prepare();
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        switch (msg.what) {
                            case MessageConstant.BarcodeDecodeMsg.DECODE:    //从CameraManager发送过来的消息，表明有一帧图像数据准备好了
                                Log.e("xsl", "bbbbb");
                                int width = msg.arg1;
                                int height = msg.arg2;
                                byte[] img = (byte[]) msg.obj;
                                removeDecodeMessages(BarcodeDecodeMsg.DECODE);
                                if (barcodeThread != null) {
                                    HardWare.sendMessage(barcodeThread.getHandler(), BarcodeDecodeMsg.DECODE, width, height, img);
                                }
                                if (barcodeBlurThread != null) {
                                    HardWare.sendMessage(barcodeBlurThread.getHandler(), BarcodeDecodeMsg.DECODE, width, height, img);
                                }
                                if (barcode2DThread != null) {
                                    HardWare.sendMessage(barcode2DThread.getHandler(), BarcodeDecodeMsg.DECODE, width, height, img);
                                }
                                if (lightWatcherThread != null) {
                                    HardWare.sendMessage(lightWatcherThread.getHandler(), BarcodeDecodeMsg.DECODE, width, height, img);
                                }
                                img = null;
                                break;
                            case BarcodeDecodeMsg.DecodeFail:    //从DecodeThread发送过来的消息，表明该线程解码一帧图像失败，需要新的数据
                                if (msg.arg1 == DecodeThread.BARCODE_THREAD) {
                                    HardWare.sendMessage(Invokerhandler, BarcodeDecodeMsg.DecodeFail);
                                }
                                break;
                            case BarcodeDecodeMsg.DecodeSuccess:
                                HardWare.sendMessage(Invokerhandler, BarcodeDecodeMsg.DecodeSuccess, msg.arg1, msg.arg2, msg.obj);
                                break;
                            case BarcodeDecodeMsg.QUIT_DECODE:
                                end();
                                handler = null;
                                Looper.myLooper().quit();
                                break;
                            case BarcodeDecodeMsg.FlashOnRemind:
                                HardWare.sendMessage(Invokerhandler, BarcodeDecodeMsg.FlashOnRemind);
                                break;
                        }
                        super.handleMessage(msg);
                    } catch (Exception e) {
                    }
                }
            };

            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeDecodeMessages(int msg) {
        try {
            handler.removeMessages(msg);
            if (barcodeThread != null && barcodeThread.getHandler() != null)
                barcodeThread.getHandler().removeMessages(msg);
            if (barcodeBlurThread != null && barcodeBlurThread.getHandler() != null)
                barcodeBlurThread.getHandler().removeMessages(msg);
            if (barcode2DThread != null && barcode2DThread.getHandler() != null)
                barcode2DThread.getHandler().removeMessages(msg);
            if (barcodeAllThread != null && barcodeAllThread.getHandler() != null)
                barcodeAllThread.getHandler().removeMessages(msg);
            if (lightWatcherThread != null && lightWatcherThread.getHandler() != null)
                lightWatcherThread.getHandler().removeMessages(msg);
        } catch (Exception e) {
        }
    }

    private void end() {
        try {
            removeDecodeMessages(BarcodeDecodeMsg.DECODE);
            if (barcodeThread != null)
                HardWare.sendMessage(barcodeThread.getHandler(), BarcodeDecodeMsg.QUIT_DECODE);
            if (barcodeBlurThread != null)
                HardWare.sendMessage(barcodeBlurThread.getHandler(), BarcodeDecodeMsg.QUIT_DECODE);
            if (barcode2DThread != null)
                HardWare.sendMessage(barcode2DThread.getHandler(), BarcodeDecodeMsg.QUIT_DECODE);
            if (barcodeAllThread != null)
                HardWare.sendMessage(barcodeAllThread.getHandler(), BarcodeDecodeMsg.QUIT_DECODE);
            if (lightWatcherThread != null)
                HardWare.sendMessage(lightWatcherThread.getHandler(), BarcodeDecodeMsg.QUIT_DECODE);
        } catch (Exception e) {
        }
    }

    public Handler getHandler() {
        return handler;
    }

}
