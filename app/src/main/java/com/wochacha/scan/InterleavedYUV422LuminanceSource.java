package com.wochacha.scan;

import android.graphics.Rect;


/**
 * Created by IntelliJ IDEA. User: wbmark Date: Feb 28, 2010 Time: 9:13:24 PM To
 * change this template use File | Settings | File Templates.
 */
public final class InterleavedYUV422LuminanceSource extends BaseLuminanceSource {
	public static int num = 0; 

	public InterleavedYUV422LuminanceSource(byte[] yuvData, int dataWidth, int dataHeight, Rect rect) {
		super(yuvData, dataWidth, dataHeight, rect);
	}

//	@Override
//	public byte[] getRow(int y, byte[] row) {
//		if (y < 0 || y > getHeight()) {
//			throw new IllegalArgumentException(
//					"Requested row is outside the image :" + y);
//		}
//		int width = getWidth();
//		if (row == null || row.length < width) {
//			row = new byte[width];
//		}
//		int offset = (y + top) * dataWidth * 2 + (left * 2);
//		byte[] yuv = yuvData;
//		for (int x = 0; x < width; x++) {
//			row[x] = yuv[offset + (x << 1)];
//		}
//		return row;
//	}

	@Override
	public byte[] getRGBMatrix(int format, boolean hsvFilter) {
		final int w = getWidth();
		final int h = getHeight();
		final int right = left + w;
		final int bottom = top + h;
		
		byte[] result;
        result = buffer;
        
		int u, v, y1, y2, y_pos, u_pos;
		int index = 0;
		int hw = top * dataWidth;
		int r = 0;
		int yuy2_num = num;
		int offset = (yuy2_num + 1) % 2;

		for (int i=top; i<bottom; i++) {
			for (int j=left; j<right; j+=2) {
				index = (hw + j) << 1;
				y_pos = index + yuy2_num;
				u_pos = index + offset;
				y1 = yuvData[y_pos] & 0xff;
				y2 = yuvData[y_pos + 2] & 0xff;
				u = yuvData[u_pos] & 0xff;
				v = yuvData[u_pos + 2] & 0xff;
				u = u - 128;
	            v = v - 128;
    		
            	getRGB(y1, u, v, result, r, hsvFilter);
	            getRGB(y2, u, v, result, r + 3, hsvFilter);
	            r += 6;
			}
			hw += dataWidth;
		}
		
//		num++;
//		num = num % 2;
		return result;
	}
	
	@Override
	public byte[] getMatrix() {
		byte[] yuv = yuvData;
		int width = getWidth();
		int height = getHeight();
		int area = width * height;
		byte[] matrix = new byte[area];
		
		int inputOffset = top * dataWidth * 2 + left * 2;
		int outputOffset = 0;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				matrix[outputOffset + x] = yuv[inputOffset + (x << 1) + num];
			}
			inputOffset += (dataWidth * 2);
			outputOffset += width;
		}
//		num++;
//		num = num % 2;
		return matrix;
	}

//	@Override
//	public LuminanceSource crop(int left, int top, int width, int height) {
//		return new InterleavedYUV422LuminanceSource(yuvData, dataWidth,
//				dataHeight, left, top, width, height);
//	}

	@Override
	public int getDataWidth() {
		return dataWidth;
	}

	@Override
	public int getDataHeight() {
		return dataHeight;
	}

//	@Override
//	public Bitmap renderCroppedGreyscaleBitmap() {
//		try {
//			// int newWidth = getWidth();
//			// int newHeight = getHeight();
//			// int[] pixels = new int[(newWidth + 1) * (newHeight + 1)];
//			// byte[] yuv = yuvData;
//			// int inputOffset = top * dataWidth*2+(left * 2);
//			// int grey;
//			// int outputOffset = 0;
//			// for(int i=0;i <newHeight ;i++){
//			// for(int j=0; j<newWidth; j++){
//			// grey = yuv[inputOffset + (j<<1)] & 0xff;
//			// pixels[outputOffset] = (0xff000000) | (grey * 0x00010101);
//			// outputOffset++;
//			// }
//			// inputOffset += dataWidth*8;
//			// }
//			// Bitmap bitmap = Bitmap.createBitmap(newWidth, newHeight,
//			// Bitmap.Config.ARGB_8888);
//			// bitmap.setPixels(pixels, 0, newWidth, 0, 0, newWidth, newHeight);
//			// pixels = null;
//			// return bitmap;
//
//			int width = getWidth();
//			int height = getHeight();
//			int[] pixels = new int[width * height];
//			byte[] yuv = yuvData;
//			int inputOffset = top * dataWidth * 2 + (left * 2);
//			int outputOffset;
//			int grey;
//			for (int y = 0; y < height; y++) {
//				outputOffset = y * width;
//				for (int x = 0; x < width; x++) {
//					grey = yuv[inputOffset + (x << 1)] & 0xff;
//					pixels[outputOffset + x] = (0xff000000) | (grey * 0x00010101);
//				}
//				inputOffset += (dataWidth * 2);
//			}
//			Bitmap bitmap = Bitmap.createBitmap(width, height,
//					Bitmap.Config.ARGB_8888);
//			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//			return bitmap;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	@Override
//	public Bitmap renderFullColorBitmap(boolean halfSize) {
//		return null; // To change body of implemented methods use File |
//						// Settings | File Templates.
//	}
}
