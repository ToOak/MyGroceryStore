package com.wochacha.scan;


import java.io.UnsupportedEncodingException;



public class DecodeResultProcessor {




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
 	

	
}
