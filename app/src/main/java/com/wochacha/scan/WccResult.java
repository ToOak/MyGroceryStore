package com.wochacha.scan;

public class WccResult {
	public int flag;
	public byte[] result;
	public byte[] colorcode;
	public int type;
	public int decodefrom;

	public WccResult() {
		flag = 0;
		type = 0;
		decodefrom = 0;
	}
}