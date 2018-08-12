package com.example.xushuailong.mygrocerystore.scan.scan1;

import android.graphics.Bitmap;
import android.graphics.Rect;

public abstract class BaseLuminanceSource extends LuminanceSource {
	protected byte[] yuvData;
	protected byte[] subData;
	protected int dataWidth;
	protected int dataHeight;
	protected int left;
	protected int top;
	
	BaseLuminanceSource(byte[] yuvData, int dataWidth, int dataHeight, Rect rect) {
		setData(yuvData, dataWidth, dataHeight, rect);
	}
	
	public void setData(byte[] yuvData, int dataWidth, int dataHeight, Rect rect) {
		init(rect.width(), rect.height());
		if (rect.left + rect.width() > dataWidth || rect.top + rect.height() > dataHeight) {
			throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
		}
		this.yuvData = yuvData;
		this.dataWidth = dataWidth;
		this.dataHeight = dataHeight;
		this.left = rect.left;
		this.top = rect.top;
	}

	public byte[] subSampleRGB(byte[] rgb, int w, int h, int subSample) {
		if (subSample <= 1) return rgb;
		
		int subW = (w / subSample) & 0xfffffffe;
		int subH = (h / subSample) & 0xfffffffe;
		int newW = subW * subSample;
		int newH = subH * subSample;
		
		int size = subW * subH * 3;
		int index = 0;
		if (subData == null) {
			subData = new byte[size];
		}
		
		for (int i=0; i<newH; i++) {
			if ((i % subSample) != 0) continue;
			
			for (int j=0; j<newW; j++) {
				if ((j % subSample) == 0) {
					int oi = (i * w + j) * 3;
					subData[index++] = rgb[oi++];
					subData[index++] = rgb[oi++];
					subData[index++] = rgb[oi];
				}
			}
		}
		
		return subData;
	}
	
	/**
	 * Requests the width of the underlying platform's bitmap
	 * 
	 * @return
	 */
	public abstract int getDataWidth();

	/**
	 * Requests the height of the underlying platform's bitmap
	 * 
	 * @return
	 */
	public abstract int getDataHeight();
	
	public abstract byte[] getMatrix();

//	/**
//	 * Creates a greyscale Android Bitmap from the YUV data based on the crop
//	 * rectangle
//	 */
//	public abstract Bitmap renderCroppedGreyscaleBitmap();

//	/**
//	 * Creates a color Android Bitmap from the YUV data, ignoring the crop
//	 * rectangle
//	 */
//	public abstract Bitmap renderFullColorBitmap(boolean halfSize);
	
	public static byte[] getRawRGBData(Bitmap bmp) {
		byte[] out = null; 
		try {	
			if (bmp.isRecycled()) return out;
			
			int w = bmp.getWidth();
			int h = bmp.getHeight();
			out = new byte[w * h * 3];
								
			for (int j=0; j<h; j++){
				for (int i=0; i<w; i++) {
					int index = j * w + i;
					int color = bmp.getPixel(i, j);
					int R = (color >> 16) & 0xFF;
					int G = (color >> 8) & 0xFF;
					int B = color & 0xFF;
					out[index * 3] = (byte)R;
		            out[index * 3 + 1] = (byte)G;
		            out[index * 3 + 2] = (byte)B;
				}
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return out;
	}
	
	public static byte[] getYData(byte[] rgb, int w, int h) {
		byte[] out = null; 
		try {	
			out = new byte[w * h];
								
			for (int j=0; j<h; j++){
				for (int i=0; i<w; i++) {
					int index = j * w + i;
					int R = rgb[index * 3] & 0xFF;
					int G = rgb[index * 3 + 1] & 0xFF;
					int B = rgb[index * 3 + 2] & 0xFF;
					int y = (int) (0.30 * R + 0.59 * G + 0.11 * B);
					y = y > 255 ? 255 : y < 0 ? 0 : y;
					out[index] = (byte)y;
				}
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return out;
	}
	
	public static void processData(byte[] rgb, int w, int h) {
		try {	
			for (int j=0; j<h; j++){
				for (int i=0; i<w; i++) {
					int index = j * w + i;
					int R = rgb[index * 3] & 0xFF;
					int G = rgb[index * 3 + 1] & 0xFF;
					int B = rgb[index * 3 + 2] & 0xFF;
					int max = R < G ? G : R;
					max = max < B ? B: max;
					int min = R < G ? R : G;
					min = min < B ? min : B;
					
					if (max >= 128 && (min * 5) < (max * 3)) {
						rgb[index * 3] = (byte)255;
						rgb[index * 3 + 1] = (byte)255;
						rgb[index * 3 + 2] = (byte)255;
					}
				}
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void getRGB(int y, int u, int v, byte[] rgb, int index, boolean hsvFilter) {
    	int r, g, b;

        int rdif = v + ((v * 103) >> 8);
        int invgdif = ((u * 88) >> 8) + ((v * 183) >> 8);
        int bdif = u + ((u * 198) >> 8);
        r = y + rdif;
        g = y - invgdif;
        b = y + bdif;
        r = r > 255 ? 255 : r < 0 ? 0 : r;
        g = g > 255 ? 255 : g < 0 ? 0 : g;
        b = b > 255 ? 255 : b < 0 ? 0 : b;
        
        if (hsvFilter) {
        	if (checkPixel(r, g, b)) {
        		r = 255;
        		g = 255;
        		b = 255;
        	}
        }
        rgb[index] = (byte)r;
        rgb[index + 1] = (byte)g;
        rgb[index + 2] = (byte)b;
    }
	
	private boolean checkPixel(int r, int g, int b) {
		int max = r < g ? g : r;
		max = max < b ? b : max;
		int min = r < g ? r : g;
		min = min < b ? min : b;
		
		if (max >= 128 && (min * 5) < (max * 3)) {
			return true;
		}
		else
			return false;
    }
}
