package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.xushuailong.mygrocerystore.scan.util.Constant;
import com.example.xushuailong.mygrocerystore.scan.util.Constant.*;
import com.example.xushuailong.mygrocerystore.scan.util.Validator;
import com.wochacha.scan.WccResult;

import java.io.UnsupportedEncodingException;



public class DecodeResultProcessor {
    final static String TAG = "DecodeResultProcessor";
    DecodeThread.DecodeResult result;
    Handler invoker;
    Activity hostAcitivty;
    int libType = Constant.ScanResult.DecodeFromGcUNI;
    int scanType = Constant.ScanType.ALL;
    //UrlParamsInfo paramsInfo;
    boolean isFinish = false;
    static boolean isJumpto = true;
    String barcode = "";
	String format = "";
	String colorcode = "";
	boolean isImageScan = false;
    
    public void SetisJumpto(boolean isjumpto){
    	isJumpto  = isjumpto;
    }
    public static boolean GetisJumpto(){
    	return isJumpto;
    }
    public DecodeResultProcessor(Activity hostAct, int libType, DecodeThread.DecodeResult result, int scanType, boolean isfinish) {
    	this.libType = libType;
    	this.scanType = scanType;
    	this.result = result;
    	hostAcitivty = hostAct;
    	isFinish = isfinish;    	
    	isImageScan = true;
    }
    public DecodeResultProcessor(Activity hostAct, int libType, DecodeThread.DecodeResult result, int scanType, boolean isfinish, boolean isjumpto) {
    	this.libType = libType;
    	this.scanType = scanType;
    	this.result = result;
    	hostAcitivty = hostAct;
    	isFinish = isfinish;
    	isJumpto = isjumpto;
    	
    }
    
    public void setInvoker(Handler invoker) {
    	this.invoker = invoker;
    }
    
    public DecodeResultProcessor(Activity hostAct) {
    	hostAcitivty = hostAct;
    }
    
    /**
     * if need callback, please set invoker before calling
     * @return the code or empty
     */
    public String process() {
    	if (result == null || hostAcitivty == null) return "";
    	String bmpName = result.bmpName;
    	switch(libType) {
    	case Constant.ScanResult.DecodeFromGcUNI:
    		return handleDecodeExternally(hostAcitivty, Constant.ScanResult.DecodeFromGcUNI, (WccResult) result.obj, null, bmpName);
    		 
    	case Constant.ScanResult.DecodeFromHcode:
    		return handleDecodeExternally(hostAcitivty, Constant.ScanResult.DecodeFromHcode, null, (Wcc2dResult) result.obj, bmpName);
    	 
//    	case ScanResult.DecodeFromMatch:
//    		return handleMatch((ccmatch_result_t) result.obj);
    		
    	default:
    		return "";
    	}
    }
    
    /**
     * 
     * @param result
     * @param format
     * @param bmpName
     * @param inputType
     * @return the result or empty
     */
    public String process(String colorcode, String result, String format, String bmpName, int inputType) {
	    if(result == null || format == null || hostAcitivty == null) return "";
	    
	    //ExtFuncConfigInfo info =  DataProvider.getInstance(hostAcitivty).getExtFuncConfig();
	    
	    result = result.trim();
	    Intent intent = null;

	    if ((Constant.ScanType.CONTINUOUS_ONE == scanType || ScanType.CONTINUOUS_SCHEDULE == scanType
	    		|| ScanType.PRICETREND == scanType)
	      && !format.equals(ScanResult.BFORMAT_EAN13) && !format.equals(ScanResult.BFORMAT_EAN13_alias)
          && !format.equals(ScanResult.BFORMAT_EAN8) && !format.equals(ScanResult.BFORMAT_EAN8_alias))
	    	return "";
	    
	    if ((ScanType.EXP == scanType || ScanType.RETURNGOODSEXPRESS == scanType) 
	  	        && !format.equals(ScanResult.BFORMAT_CODE39) && !format.equals(ScanResult.BFORMAT_CODE39_alias)
	            && !format.equals(ScanResult.BFORMAT_CODE128) && !format.equals(ScanResult.BFORMAT_CODE128_alias)
	            && !format.equals(ScanResult.BFORMAT_EXPRESS) && !format.equals(ScanResult.BFORMAT_CODE93))
	    	return "";
	  	    	
	    
	    if (format.equals(ScanResult.BFORMAT_QR) 
	    		|| format.equals(ScanResult.BFORMAT_DM) || format.equals(ScanResult.BFORMAT_HANXIN)) { // Eagle  20120814
//	    	if (format.equals(ScanResult.BFORMAT_QR)) {
//	    		String params = UrlParamsParser.getPkidAndTimestamp(result);// 返回值只有两种:"URLERROR"和所取的参数字符串
//	    		if (paramsInfo == null) {
//	    			paramsInfo = UrlParamsInfo.getInstance();
//	    			paramsInfo.setEpAndEt(params);
//	    		}
//	        	if (WccConstant.DEBUG){
//	        		Log.e(TAG, "提取参数结果：" + paramsInfo.getEpAndEt());
//	        	}
//	        	if ("URLERROR".equals(params)) {
//	        		intent = DecodeResultProcessor.precessQRAndDM(hostAcitivty, result, format, info, bmpName);// 如果没拿到解析结果，按普通二维码处理
//	        	} else {
//	        		intent = precessOther(result, format, info, bmpName);
//	        	}
//	    	} else {
//	    		intent = DecodeResultProcessor.precessQRAndDM(hostAcitivty, result, format, info, bmpName);
//	    	}
	        //intent = DecodeResultProcessor.precessQRAndDM(hostAcitivty, result, format, info);
	    } else if (format.equals(ScanResult.BFORMAT_CODE39) 
	    		|| format.equals(ScanResult.BFORMAT_CODE128) 
	    		|| format.equals(ScanResult.BFORMAT_CODE39_alias)
	            || format.equals(ScanResult.BFORMAT_CODE128_alias) 
	            || format.equals(ScanResult.BFORMAT_CODE93)
	            || format.equals(ScanResult.BFORMAT_EXPRESS)) {
	        //intent = precess39And128(hostAcitivty, result, format, info, bmpName, inputType);
	    } else if (format.equals(ScanResult.BFORMAT_HCODE)) {// HCode
//	        intent = new Intent(hostAcitivty, WccWebViewActivity.class);
//	        String url = String.format(WccConstant.HCODE_QUERY_URL, result, HardWare.getDeviceId(hostAcitivty));
//	        intent.putExtra("webview_url", url);

	    } else {
	    	if (Validator.isEffective(colorcode)
	    			&& !"single".equals(colorcode)
	    			&& !"2color".equals(colorcode)
	    			&& !"3color".equals(colorcode)
	    			&& !"imprvqlty".equals(colorcode)
	    			&& !"darkTooBright".equals(colorcode)
					&& !"0".equals(colorcode)
					&& !"1".equals(colorcode)) {	//add v8.7 如果是彩虹码,跳转彩虹码结果页
	    		//intent = new Intent(hostAcitivty, RainbowResultActivity.class);
	    	} else {
	    		//intent = precessOther(result, format, info, bmpName);
	    	}
	        
	    }
	
	    if (intent != null) {
	        intent.putExtra(ScanResult.kScanResult, result);
	        intent.putExtra(ScanResult.kRainbowResult, colorcode);
	        intent.putExtra(ScanResult.kResultType, format);
	        if (isImageScan)
	        	intent.putExtra(Constant.KeyAction, Constant.RequireAction.Image);
	        else {
	        	if (inputType == Constant.InputType.NOINPUT)
		        	intent.putExtra(Constant.KeyAction, Constant.RequireAction.BarScan);
		        else
		        	intent.putExtra(Constant.KeyAction, Constant.RequireAction.Input);
	        }
	      
	        if (isJumpto ) {
	           hostAcitivty.startActivity(intent); 
	        }
	        if (isFinish) {	        	
	           hostAcitivty.finish();
	        }
	    }
	    return result;
    }
    
 

//	private Intent precessOther(String result, String format, ExtFuncConfigInfo info, String bmpName) {
//	    Intent intent = null;
//
//	    switch(scanType){
//	    	case ScanType.CONTINUOUS_ONE:
//	    		if(WccConstant.DEBUG)
//		    		Log.e(TAG, "process ScanType.CONTINUOUS_ONE");
//
//		    	if(DataBaseHelper.getInstance(hostAcitivty).inShoppingCartByBarcode(result))
//		    		return null;
//
//		    	return null;
//	    	case ScanType.CONTINUOUS_SCHEDULE:
//	    		MapArgs mapArgs = new MapArgs();
//				mapArgs.put("MapKey", ""+hashCode());
//				mapArgs.put("Callback", invoker);
//				mapArgs.put("DataType", DataType.ArticleScan);
//				mapArgs.put("Barcode", result);
//				HardWare.sendMessage(WccScanApplication.getHandler(), MessageConstant.RequireData, mapArgs);
//
//				return null;
//	    	case ScanType.PRICETREND:
//	    		return null;
//	    	default:
//	    		break;
//    	}
//
////	    if (QcStartupActivity.getQcMode(hostAcitivty)) {
////	    	intent = new Intent(hostAcitivty, QcResultActivity.class);
////	    }
////	    else {
////	    	intent = new Intent(hostAcitivty, CommodityCompareResultActivity.class);
////	        intent.putExtra(DataThread.BARCODE_BITMAP_PATH, bmpName);
////	    }
//
// 	    return intent;
//	}

	private boolean isDrugAdminCode(String code) {
		if (code == null) return false;
		if (code.length() == 20 && code.charAt(0) == '8')
			return true;
		return false;
	}
	
//	private Intent precess39And128(Activity act, String result, String format, ExtFuncConfigInfo info, String bmpName, int inputType) {
//		 if (WccConstant.DEBUG)
//			 Log.e(TAG, "precess39And128, result=" + result + ", format=" + format+ ", scanType=" + scanType );
//		Intent intent = null;
//
//	    boolean ret = false;
//	    if (info != null && (Constant.ScanType.EXP != scanType && Constant.ScanType.RETURNGOODSEXPRESS != scanType && inputType != Constant.InputType.EXPRESS) && !isDrugAdminCode(result))
//	        ret = info.isAntifakeCode(result.substring(0, 3));
//
//	    if (ret == true) {
//	        //intent = new Intent(act, AntifakeInquireActivity.class);
//	    }
//	    else {
//	    	 if (isDrugAdminCode(result)) {
////                 intent = new Intent(act, DrugAdminCodeActivity.class);
////                 intent.putExtra(DataThread.BARCODE_BITMAP_PATH, bmpName);
////                 intent.putExtra(BarcodeDecodeMsg.ScanType, 0);
//             }
//	    	 else if (ScanType.RETURNGOODSEXPRESS == scanType){
//// 				HardWare.sendMessage(ReturnGoodsActivity.getHandler(), MessageConstant.NotifyDataSetChanged, result);
//// 				act.finish();
//	    	 } else {
////              	intent = new Intent(act, ExpressInquiryActivity.class);
////            	ExpressInfo express = new ExpressInfo(ExpressCompanyInfo.parserJson(WccConfigure.getExpressCompany(act)));
////            	express.setExpressId(result);
////            	intent.putExtra("title", "快递查询");
////                intent.putExtra("express", express);
////                intent.putExtra("scanType", scanType);
//	    	 }
// 	    }
//
//	    return intent;
//	}
//
//	private static Intent precessQRAndDM(Activity act, String result, String format, ExtFuncConfigInfo info, String bmpName) {
//	    Intent intent = null;
//
//	    boolean isAntifake = false;
//
//	    if (info != null) {
//	        // newfunction接口antilist字段，包含二维码防伪前缀词列表
//	        isAntifake = info.isAntifakeCode(result);
//	        if (!isAntifake) { // Eagle 20122830 for 夏商
//	            isAntifake = ExtFuncConfigInfo.isAntifakeCodeSpec(format, 1, result);
//	            if (isAntifake)
//	                result = "xs:" + result.substring(0, 28);
//	        }
//	    }
//
////	    if ("987654321".equals(result) || "123456789".equals(result) || "999999999".equals(result))// Eagle 20120814
////	        isAntifake = true;// Demo for jila
//
//	    if (isAntifake == true) {
//	        // 在antilist中找到匹配，客户端展示服务器返回的json数据（通过newdaydayup/antifake接口获取）
//	        //intent = new Intent(act, AntifakeInquireActivity.class);
//
//	    } else {
//	        if (info != null) {
//	            // newfunction接口antijump字段，包含需要自动跳转到某个网址的二维码前缀列表
//	            isAntifake = info.shouldLoadAntiJump(result);
//	            if (isAntifake == true) {
//	                // 在antijump中找到匹配，客户端直接跳转到服务器返回的网址（通过newdaydayup/antijump接口获取）
//	            	 intent = new Intent(act, AntifakeInquireActivity.class);
//	            	 intent.putExtra("jump", true);
//	            }
//
//	            // newfunction接口autojump字段，包含自动跳转网址列表
//	            ContentBaseInfo content = ContentBaseInfo.Parser(result);
//	            List<String> urls = content.getWebsitesContent();
//	            if (urls != null) {
//	                int len = urls.size();
//	                for (int i = 0; i < len; i++) {
//	                    String url = urls.get(i);
//	                    isAntifake = info.shouldAutoJump(url);
//	                    if (isAntifake) {
//	                        // 二维码中包含需要自动跳转的网址，则起浏览器处理之
//	                        HardWare.startWebView(url, act);
//	                        act.finish();
//	                        return null;
//	                    }
//	                }
//	            }
//	        }
//	        if(intent == null){
//        		DataBaseHelper.getInstance(act).putEnigmaHists(result,bmpName);
//	        	intent = new Intent(act, QRContentResultView.class);
//	        	intent.putExtra("imagefilepath", bmpName);
//	        }
//	    }
//
//	    return intent;
//	}

	public static String convertByteToString(byte[] data) {
	    String result = null;
	    try {
	        String encoding = BarcodeScanActivity.getDataEncoding(data, 0, data.length);
	        result = new String(data, 0, data.length, encoding);
	    } catch (UnsupportedEncodingException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return result;
	}
 
	 
	String handleDecodeExternally(Activity context, int whichLib, WccResult g_rawResult, Wcc2dResult g_2dResult, String bmpName) {
		if (whichLib == ScanResult.DecodeFromGcUNI) { // gc_lib
	        barcode = convertByteToString(g_rawResult.result);
	        format = getBarcodeFormat(g_rawResult.type);
	        colorcode = convertByteToString(g_rawResult.colorcode);
	        Log.e(TAG, "-----------colorcode:" + colorcode);
//	        if (colorcode.equals("000000000000000000000000000000") || colorcode.equals("111111111111111111111111111111"))
//	        	colorcode = "";
	    }
		else if (whichLib == ScanResult.DecodeFromHcode) {// Eagle 20120814
	        barcode = g_2dResult.getResult();
	        format = g_2dResult.getFormat();// Eagle 20120720
	    }
	
	    //某些情况下，UPC-12码会被识别成EAN-13码，这时，需要在前面补0
	    //UPC-12码在前面补0，转换成EAN-13
	    if (ScanResult.BFORMAT_EAN13.equals(format) || ScanResult.BFORMAT_EAN13_alias.equals(format) 
	    		|| ScanResult.BFORMAT_UPC12.equals(format)) {
	    	int diff = 13 - barcode.length();
	    	if (diff > 0) {
	    		for (int i=0; i<diff; i++) {
	    			barcode = "0" + barcode;
	    		}
	    		format = ScanResult.BFORMAT_EAN13;
	    	}
	    }
	    return process(colorcode, barcode, format, bmpName, Constant.InputType.NOINPUT);
	    
	}
 	
	/**
	 * 
	 * @param barcodeType
	 * @return format or null;
	 */
	static String getBarcodeFormat(int barcodeType) {
		String format = null;
		  switch (barcodeType) {
	        case BarcodeType.HZBAR_EAN8:
	            format = ScanResult.BFORMAT_EAN8;
	            break;
	        case BarcodeType.HZBAR_EAN13:
	            format = ScanResult.BFORMAT_EAN13;
	            break;
	        case BarcodeType.HZBAR_ISBN13:
	            format = ScanResult.BFORMAT_EAN13;	//ISBN-13是图书编码，早期是10位，后来升级成13位，与EAN-13完全相同
	            break;
	        case BarcodeType.HZBAR_UPCA:
	            format = ScanResult.BFORMAT_UPC12;
	            break;
	        case BarcodeType.HZBAR_UPCE:
	            format = ScanResult.BFORMAT_UPC8;
	            break;
	        case BarcodeType.HZBAR_QRCODE:
	            format = ScanResult.BFORMAT_QR;
	            break;
	        case BarcodeType.HZBAR_CODE_WEPC:
	            format = ScanResult.BFORMAT_WEPC;
	            break;
	        case BarcodeType.HZBAR_CODE39:
	            format = ScanResult.BFORMAT_CODE39;
	            break;
	        case BarcodeType.HZBAR_CODE128:
	            format = ScanResult.BFORMAT_CODE128;
	            break;
	        case BarcodeType.HZBAR_CODE_DM:
	            format = ScanResult.BFORMAT_DM;
	            break;
	        case BarcodeType.HZBAR_CODE93:
	        	format = ScanResult.BFORMAT_CODE93;
	            break;
	        case BarcodeType.HZBAR_HANXIN:
	        	format = ScanResult.BFORMAT_HANXIN;
	        	break;
	        }
		 return format; 
	}
	
}
