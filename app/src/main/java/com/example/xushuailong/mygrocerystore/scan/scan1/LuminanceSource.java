package com.example.xushuailong.mygrocerystore.scan.scan1;


public abstract class LuminanceSource {
	private int width;
	private int height;
	protected byte[] buffer = null;
	private int bufferSize = 0;
	
	protected LuminanceSource() {
	}

	protected LuminanceSource(int width, int height) {
		init(width, height);
	}
	
	public void init(int width, int height) {
		this.width = width;
		this.height = height;
		
		int newSize = width * height * 3;
		if (bufferSize < newSize) {
			if (buffer != null) 
				buffer = null;
			bufferSize = newSize;
		}
		
		if (buffer == null) {
			buffer = new byte[newSize];
		}
	}
	
//	public abstract byte[] getRow(int y, byte[] row);

//	public abstract byte[] getMatrix();
	public abstract byte[] getRGBMatrix(int format, boolean hsvFilter);
	
	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public boolean isCropSupported() {
		return false;
	}

//	public LuminanceSource crop(int left, int top, int width, int height) {
//		throw new RuntimeException(
//				"This luminance source does not support cropping.");
//	}

	public boolean isRotateSupported() {
		return false;
	}

	public LuminanceSource rotateCounterClockwise() {
		throw new RuntimeException(
				"This luminance source does not support rotation.");
	}

}
