package com.example.xushuailong.mygrocerystore.scan.util;


/**
 * Created by anxiaofei on 16/10/25.
 */
public class JCConstant {

    private JCConstant() {

    }

    /**
     * App版本号，统一配置
     */
    public static final String APP_VERSION = "1.1.5.alpha";

    public static final String HOST_WEBVIEW_ALPHA = "https://api-alpha.wochacha.cn/dcm/webview/H5/";
    public static final String HOST_WEBVIEW_BETA = "https://openapi-beta.wochacha.cn/dcm/webview/H5/";
    public static final String HOST_WEBVIEW_RELEASE = "https://openapi.wochacha.com/dcm/webview/H5/";
    /**
     * 发布时切换到正式服务器
     */
    public static final String HOST_WEBVIEW = HOST_WEBVIEW_ALPHA;

    public static String SHOP_CART_ID = "";

    /**
     * 自动对焦
     */
    public static final boolean AUTO_FOCUS = false;

    public static String BusinessId = "";
}
