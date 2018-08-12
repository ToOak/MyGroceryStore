package com.example.xushuailong.mygrocerystore.scan.util;

/**
 * prefix 0xff 00 00
 */
public interface MessageConstant {
    int BASE_MSG = 0xff0000;

    int SearchFinished = BASE_MSG + 3;
    /**
     * msg.arg1 = pageNum
     */

    int SHOW_DIALOG = BASE_MSG + 8;
    int CLOSE_DIALOG = BASE_MSG + 9;
    int ACTIVITY_CLOSE = BASE_MSG + 10;
    int SleepDown = BASE_MSG + 15;

    int SET_ZOOM = BASE_MSG + 20;

    public static interface BarcodeDecodeMsg {
        int AUTOFOCUS = BASE_MSG + 200;// auto_focus
        //      int RMB_DECODE_1 = BASE_MSG + 201; // 人民币识别第一步
//      int RMB_DECODE_2 = BASE_MSG + 202; // 人民币识别第二步
//      int RMB_DECODE_3 = BASE_MSG + 203; // 人民币识别第三步
        int DECODE = BASE_MSG + 210;
        int DecodeSuccess = BASE_MSG + 211;
        int DecodeFail = BASE_MSG + 212;
        //      int INRANGE = BASE_MSG + 213;
//      int OUTRANGE = BASE_MSG + 214;
        int StartPreview = BASE_MSG + 215;
        int QUIT_DECODE = BASE_MSG + 216;
        int IMAGE_DECODE = BASE_MSG + 217;
        int ImageDecodeFail = BASE_MSG + 218;
        int PreviewFrame = BASE_MSG + 219;
        int CameraFailure = BASE_MSG + 220;
        int InitPreview = BASE_MSG + 221;
        int PreviewFailure = BASE_MSG + 222;
        int FlashOff = BASE_MSG + 223;
        int FlashOn = BASE_MSG + 224;
        int CloseCamera = BASE_MSG + 225;
        int RestartPreviewAndDecode = BASE_MSG + 226;
        int DecodeSuccessScan = BASE_MSG + 227;

        int ScanResult = BASE_MSG + 230;
        int CameraNoData = BASE_MSG + 231;
        int StartTimer = BASE_MSG + 232;
        int StopTimer = BASE_MSG + 233;

        int ColorOn = BASE_MSG + 240;
        int ColorOff = BASE_MSG + 241;

        int FlashOnRemind = BASE_MSG + 250;


        String ScanType = "scan_type";
    }


}
