package com.wochacha.scan.util;

public interface Constant {

    // --------------------for configure
    String KEY_AUTOFOCUS_PREF = "PrefAutoFocus";
    String KEY_FLASH_PREF = "PrefFlash";
    String KEY_CAMSELECT_PREF = "PrefCameraSelect";
    String KEY_CAMROTATE_PREF = "PrefCameraRotate";
    String KEY_INFO_SOUND = "PrefInfoSound";
    String KEY_VIBRATE = "PrefVibrate";
    String KeyIsAutofocus = "IsAutofocus";

    String KeyDeviceID = "UserDeviceId";
    String KeyNewDeviceID = "NewUserDeviceId";




    interface ScanType {
        int ALL = 0;
        int EXP = 3;
        //		int PLATE = 4;
        int CONTINUOUS_ONE = 10;
        int CONTINUOUS_SCHEDULE = 11;//连续扫描
        int PRICETREND = 13;
        int RETURNGOODSEXPRESS = 14;// 只扫描快递号，不查询

        int FROMQRCODE = 15;
        int FROMEXPOSURE = 16;
    }

    interface FocusType{
        int AutoFocus = 1;//自动对焦
        int ManualFocus = 3;//定焦+触摸对焦
    }

    int BASE_MSG = 0x0a00;

    interface ScanResult {

        int DecodeFromGcUNI = BASE_MSG + 402;

    }

    interface BarcodeType {


        /**
         * EAN-13
         **/
        int HZBAR_EAN13 = 13;



        /**
         * Code 39. @since 0.4
         **/
        int HZBAR_CODE39 = 39;

        /**
         * QR Code. @since 0.10
         **/
        int HZBAR_QRCODE = 64;

        /**
         * Code 93
         **/
        int HZBAR_CODE93 = 93;

        /**
         * Code 128
         **/
        int HZBAR_CODE128 = 128;


        /**
         * DM
         **/
        int HZBAR_CODE_DM = 130;

        /**
         * HANXIN
         **/
        int HZBAR_HANXIN = 131;



    }

    interface ScanMode {
        int BARCODE = 1;
        int QRCODE = 2;
        int ALLCODE = 3;
        int BLURBARCODE = 4;
        int LIGHTWATCHER = 5;//光线判断线程
    }



}