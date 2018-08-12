package com.example.xushuailong.mygrocerystore.scan.util;

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
    String KeyAction = "RequireAction";

    String gchar = "@n0dr#ew!$";


    interface InputType {
        int NOINPUT = -1;
    }

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
        String kScanResult = "scan_result";
        String kResultType = "result_type";
        String kRainbowResult = "rainbow_result";
        int DecodeFromGcUNI = BASE_MSG + 402;
        //		int DecodeFromMatch = BASE_MSG + 403;
        int DecodeFromHcode = BASE_MSG + 404;
        //		int DecodeFromPlate = BASE_MSG + 405;
        String BFORMAT_EAN13 = "EAN-13";
        String BFORMAT_EAN8 = "EAN-8";
        String BFORMAT_UPC12 = "UPC-A";
        String BFORMAT_UPC8 = "UPC-E";
        String BFORMAT_CODE39 = "Code39";
        String BFORMAT_CODE128 = "Code128";
        String BFORMAT_CODE93 = "Code93";
        String BFORMAT_HANXIN = "HANXIN";

        String BFORMAT_EAN13_alias = "EAN_13";
        String BFORMAT_EAN8_alias = "EAN_8";
        String BFORMAT_CODE39_alias = "CODE_39";
        String BFORMAT_CODE128_alias = "CODE_128";

        String BFORMAT_EXPRESS = "MAN_INPUT";
        String BFORMAT_QR = "QR_CODE";
        String BFORMAT_DM = "DM_CODE";
        String BFORMAT_WEPC = "WEPC";
        String BFORMAT_HCODE = "HCODE";

    }

    interface BarcodeType {

        /**
         * EAN-8
         **/
        int HZBAR_EAN8 = 8;

        /**
         * UPC-E
         **/
        int HZBAR_UPCE = 9;

        /**
         * UPC-A
         **/
        int HZBAR_UPCA = 12;

        /**
         * EAN-13
         **/
        int HZBAR_EAN13 = 13;

        /**
         * ISBN-13 (from EAN-13). @since 0.4
         **/
        int HZBAR_ISBN13 = 14;


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
         * WEPC
         **/
        int HZBAR_CODE_WEPC = 129;

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

    interface RequireAction {
        int BarScan = BASE_MSG + 501;
        int Click = BASE_MSG + 502;
        int Input = BASE_MSG + 503;
        int PriceTrend = BASE_MSG + 504;
        int YMDP = BASE_MSG + 505;//一码多品点击 V6.9
        int Image = BASE_MSG + 506;
    }


}