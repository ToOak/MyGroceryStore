package com.wochacha.scan.util;

/**
 * prefix 0xff 00 00
 */
public interface MessageConstant {
    int BASE_MSG = 0xff0000;

    /**
     * msg.arg1 = pageNum
     */

    int SleepDown = BASE_MSG + 15;


    interface BarcodeDecodeMsg {
        int AUTOFOCUS = BASE_MSG + 200;// auto_focus
        int DECODE = BASE_MSG + 210;
        int DecodeSuccess = BASE_MSG + 211;
        int DecodeFail = BASE_MSG + 212;

        int QUIT_DECODE = BASE_MSG + 216;

        int FlashOff = BASE_MSG + 223;
        int FlashOn = BASE_MSG + 224;
        int CloseCamera = BASE_MSG + 225;
        int RestartPreviewAndDecode = BASE_MSG + 226;
        int DecodeSuccessScan = BASE_MSG + 227;

        int CameraNoData = BASE_MSG + 231;
        int StartTimer = BASE_MSG + 232;
        int StopTimer = BASE_MSG + 233;


        int FlashOnRemind = BASE_MSG + 250;


    }


}
