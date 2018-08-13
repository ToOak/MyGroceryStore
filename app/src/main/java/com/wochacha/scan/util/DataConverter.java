package com.wochacha.scan.util;


public class DataConverter {


	public static String convertMD5(byte[] byteMD5) {
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16 进制需要
										// 32 个字符
		int k = 0; // 表示转换结果中对应的字符位置
		for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节 转换成 16 进制字符的转换
			byte byte0 = byteMD5[i]; // 取第 i 个字节
			str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, // >>>
														// 为逻辑右移，将符号位一起右移
			str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
		}
		return new String(str); // 换后的结果转换为字符串
	}

	/**
	 * 
	 * @param source
	 * @return 32 characters which represent a 128 bits long integer
	 */
	public static String getMD5(byte[] source) {
		String s = "";

		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			s = convertMD5(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}




	




}
