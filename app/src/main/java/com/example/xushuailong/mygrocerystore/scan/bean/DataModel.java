package com.example.xushuailong.mygrocerystore.scan.bean;

import android.content.Context;


/**
 * Created by yanchunfei on 10/28/16.
 */

public class DataModel {

    private static final String NOT_LOGGED_IN = "4001";

    /**
     * 接口请求状态,具体含义参考wiki
     */
    private String isokType;
    /**
     * 接口请求提示消息
     */
    private String message;
    /**
     * 状态码
     */
    private String rescode;

    public String getIsokType() {
        return isokType;
    }

    public void setIsokType(String isokType) {
        this.isokType = isokType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRescode() {
        return rescode;
    }

    public void setRescode(String rescode) {
        this.rescode = rescode;
    }

    public String getMsg(Context mContext){
//        if (ValidatorUtil.isEffective(message)) {
//            switch (rescode) {
//                case NOT_LOGGED_IN:
//                    LoginUtil.startLoginActivity(mContext);
//                    break;
//                default:
//                    break;
//            }
//            return message;
//        }else {
            return "数据请求失败！";
//        }
    }
}
