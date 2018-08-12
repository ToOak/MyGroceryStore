package com.example.xushuailong.mygrocerystore.scan.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {

	/**
	 * 验证邮箱
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isEmail(String str) {
		String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		return match(regex, str);
	}

	/**
	 * 验证IP地址
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isIP(String str) {
		String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
		String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num
				+ "$";
		return match(regex, str);
	}

	
	/**
	 * 验证网址Url
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsUrl(String str) {

		String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
		String Ip4 = "(" + num + "\\." + num + "\\." + num + "\\." + num + ")";
		String regex = "(?i)http(?-i)([sS])?[:：]//"
				+ "(([0-9A-Za-z]([\\w-]*\\.)+[A-Za-z]+)|" + Ip4+")"
				+ "(?::(\\d{1,5}))?(/[\\w- ./?!%&=#:~|]*)?";
		return match(regex, str);
	}

	/**
	 * 验证网址Url；校验比较宽松的版本，适合包含Http前缀的字符串判断
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsUrl2(String str) {

		String regex = "(?i)http(?-i)([sS])?[:：]//" + "([\\w-]+\\.)+[\\w-]+"
				+ "(?::(\\d{1,5}))?([\\w- ./?!%&=#$_:~|'@;,*+()\\[\\]]*)?";
		return match(regex, str);
	}

	/**
	 * 验证电话号码
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsTelephone(String str) {
		String regex = "^(\\+)?(\\d{2,4}-)?(\\d{2,4}-)?\\d{4,13}(-\\d{2,4})?$";
		return match(regex, str);
	}

	/**
	 * 验证输入密码条件(字符与数据同时出现)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPassword(String str) {
		String regex = "[A-Za-z]+[0-9]";
		return match(regex, str);
	}

	/**
	 * 验证输入密码长度 (6-18位)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPasswLength(String str) {
		String regex = "^\\d{6,18}$";
		return match(regex, str);
	}

	/**
	 * 验证输入邮政编号
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPostalcode(String str) {
		String regex = "^\\d{6}$";
		return match(regex, str);
	}

	/**
	 * 验证输入手机号码
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsHandset(String str) {
		String regex = "^1[3,4,5,7,8]\\d{9}$";
		return match(regex, str);
	}

	/**
	 * 验证输入身份证号
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsIDcard(String str) {
		String regex = "(^\\d{18}$)|(^\\d{15}$)";
		return match(regex, str);
	}

	/**
	 * 验证输入两位小数
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsDecimal(String str) {
		String regex = "^[0-9]+(.[0-9]{2})?$";
		return match(regex, str);
	}

	/**
	 * 验证输入一年的12个月
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsMonth(String str) {
		String regex = "^(0?[[1-9]|1[0-2])$";
		return match(regex, str);
	}

	/**
	 * 验证输入一个月的31天
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsDay(String str) {
		String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
		return match(regex, str);
	}

	/**
	 * 验证日期时间
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合网址格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isDate(String str) {
		// 严格验证时间格式的(匹配[2002-01-31], [1997-04-30],
		// [2004-01-01])不匹配([2002-01-32], [2003-02-29], [04-01-01])
		// String regex =
		// "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((01,3-9])|(1[0-2]))-(29|30)))))$";
		// 没加时间验证的YYYY-MM-DD
		// String regex =
		// "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";
		// 加了时间验证的YYYY-MM-DD 00:00:00
		String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
		return match(regex, str);
	}

	/**
	 * 验证数字输入, if empty return true 
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsNumber(String str) {
		if(str == null) return false;
		String regex = "-?[0-9]*$";
		return match(regex, str);
	}
 

	/**
	 * 验证非零的正整数
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsIntNumber(String str) {
		String regex = "^\\+?[1-9][0-9]*$";
		return match(regex, str);
	}

	/**
	 * 验证大写字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsUpChar(String str) {
		String regex = "^[A-Z]+$";
		return match(regex, str);
	}

	/**
	 * 验证小写字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsLowChar(String str) {
		String regex = "^[a-z]+$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsAllowedLetter(String str) {
		String regex = "^[0-9A-Za-z\u4e00-\u9fa5\\(\\)_ .&-]+$";
		return match(regex, str);
	}

    /**
     * A-Za-z
     * @param str
     * @return
     */
	public static boolean IsLetter(String str) {
		String regex = "^[A-Za-z]+$";
		return match(regex, str);
	}
	
	/**
	 *  0-9A-Za-z 
	 */
	public static boolean IsLetterOrNumber(String str) {
		String regex = "^[0-9A-Za-z]";
		return match(regex, str);
	}
	
	public static boolean IsLettersOrNumbers(String str) {
		if(str == null) return false;
		int strLen = str.length();
		for(int i = 0; i < strLen; i++) {
			String c = str.charAt(i) + "";
		    if(!IsLetterOrNumber(c)) {
		    	return false;
		    }		    
		}
		return true;
	}

	/**
	 * 验证输入汉字
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsChinese(String str) {
		String regex = "^[\u4e00-\u9fa5]+";
		return match(regex, str);
	}
	
   /**
    * if -90~90 return true; if acceptZero=false, 0.0 return false;
    * @param string double string
    * @param acceptZero
    * @return
    */
	public static boolean isLatitude(String string, boolean acceptZero) {
		try{
			double lat = Double.parseDouble(string);
			if(lat > 90 || lat < -90) return false;
			if(lat == 0 && acceptZero == false) return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	   /**
	    * if -180~180 return true; if acceptZero=false, 0.0 return false;
	    * @param string double string
	    * @param acceptZero
	    * @return
	    */
		public static boolean isLongitude(String string, boolean acceptZero) {
			try{
				double lat = Double.parseDouble(string);
				if(lat > 180 || lat < -180) return false;
				if(lat == 0 && acceptZero == false) return false;
				return true;
			} catch (Exception e) {
				return false;
			}
		}

	/**
	 * support ..{..{..{..{..}..}..}..}.. four level
	 * 
	 * @param str
	 * @return
	 */
	public static boolean IsContainJson(String str) {
		String base = "(\\{[^\\{\\}]*\\})";
		String wrap1 = "(\\{([^\\{\\}]*" + base + "*[^\\{\\}]*)+\\})";
		String wrap2 = "(\\{([^\\{\\}]*" + wrap1 + "*[^\\{\\}]*)+\\})";
		String wrap3 = "(\\{([^\\{\\}]*" + wrap2 + "*[^\\{\\}]*)+\\})";

		String regex = "[^\\{\\}]*" + wrap3 + "+[^\\{\\}]*$";

		return match(regex, str);
	}

	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str) {
		if (str == null)
			return false;
		if (regex == null)
			return false;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * check whether be empty/null or not
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isEffective(String string) {
		if ((string == null) || ("".equals(string)) || (" ".equals(string))
				|| ("null".equals(string)) || ("\n".equals(string)))
			return false;
		else
			return true;
	}
	

	/**
	 * check whether the price is empty/null or not or smaller than limitPirce
	 * 
	 * @param str_price
	 * @return
	 */
//	public static boolean isPriceEffective(String str_price, double limitPirce) {
//		if(DataConverter.parseDouble(str_price) > limitPirce) {
//			return true;
//		} else
//			return false;
//	}
	
	/**
	 * check whether the price is empty/null/0.0
	 * 
	 * @param str_price
	 * @return
	 */
//	public static boolean isPriceEffective(String str_price) {
//		return isPriceEffective(str_price, 0.0);
//	}

	public static boolean isUtf8Data(byte[] b, int index, int type) {
	    int lLen = b.length, lCharCount = 0;
	    for (int i = index; i < lLen && lCharCount < type; ++lCharCount) {
	        byte lByte = b[i++];
	        if (lByte >= 0)
	            continue;
	        if (lByte < (byte) 0xc0 || lByte > (byte) 0xfd)
	            return false;
	        int lCount = lByte > (byte) 0xfc ? 5 : lByte > (byte) 0xf8 ? 4 : lByte > (byte) 0xf0 ? 3 : lByte > (byte) 0xe0 ? 2 : 1;
	        if (i + lCount > lLen)
	            return false;
	        for (int j = 0; j < lCount; ++j, ++i)
	            if (b[i] >= (byte) 0xc0)
	                return false;
	    }
	    return true;
	
	}
	
	//-----------------for qrcode-------------------------------------------------//
//	/**
//     *
//     * @param url
//     * @return 判断url是否为普通类型的文件下载地址
//     */
//	public static boolean isFileLinkUrl(String url){
//		if(url==null) return false;
//		url = DataConverter.ExtractFileName(url);
//		if(url==null) return false;
//		if(FileManager.isApk(url) || FileManager.isAudio(url)
//				|| FileManager.isVideo(url) || FileManager.isImage(url)
//				|| FileManager.isDocument(url)||FileManager.isTxt(url) || FileManager.isZip(url))
//			return true;
//		return false;
//	}
//
//	/**
//     *
//     * @param url
//     * @return  判断url是否为apk下载地址
//     */
//    public static boolean isApkLink(String url){
//		if(url==null) return false;
//		url = DataConverter.ExtractFileName(url);
//		if(url==null) return false;
//		if(FileManager.isApk(url))
//			return true;
//		return false;
//	}
//
//    /**
//     *
//     * @param url
//     * @return 判断url是否为除apk以外的文件下载地址
//     */
//	public static boolean isFileLink(String url){
//		if(url==null) return false;
//		url = DataConverter.ExtractFileName(url);
//		if(url==null) return false;
//		if(FileManager.isAudio(url)
//				|| FileManager.isVideo(url) || FileManager.isImage(url)
//				|| FileManager.isDocument(url)||FileManager.isTxt(url) || FileManager.isZip(url))
//			return true;
//		return false;
//	}
	//-------------------------------------------------------------------------------------------------------------//
}