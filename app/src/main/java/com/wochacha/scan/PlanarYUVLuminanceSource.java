package com.wochacha.scan;

import android.graphics.ImageFormat;
import android.graphics.Rect;

/**
 * Created by IntelliJ IDEA. User: wbmark Date: Feb 28, 2010 Time: 8:32:25 PM To
 * change this template use File | Settings | File Templates.
 */
public final class PlanarYUVLuminanceSource extends BaseLuminanceSource {
	public PlanarYUVLuminanceSource(byte[] yuvData, int dataWidth, int dataHeight, Rect rect) {
		super(yuvData, dataWidth, dataHeight, rect);
	}

//	public PlanarYUVLuminanceSource(Bitmap bitmap) throws IOException {
//		super(bitmap.getWidth(), bitmap.getHeight());
//		this.dataWidth = bitmap.getWidth();
//		this.dataHeight = bitmap.getHeight();
//		this.yuvData = getPlanarYUVData(bitmap);
//		this.left = 0;
//		this.top = 0;
//
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
//			int width = getWidth();
//			int height = getHeight();
//			int[] pixels = new int[width * height];
//			byte[] yuv = yuvData;
//			int inputOffset = top * dataWidth + left;
//			int grey;
//			int outputOffset;
//			for (int i = 0; i < height; i++) {
//				outputOffset = i * width;
//				for (int j = 0; j < width; j++) {
//					grey = yuv[inputOffset + j] & 0xff;
//					pixels[outputOffset + j] = (0xff000000) | (grey * 0x00010101);
//				}
//				inputOffset += dataWidth;
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

//	@Override
//	public Bitmap renderFullColorBitmap(boolean halfSize) {
//		return null;
//	}

//	@Override
//	public byte[] getRow(int i, byte[] row) {
//		if (i < 0 || i >= getHeight()) {
//			throw new IllegalArgumentException(
//					"Requested row is outside the image :" + i);
//		}
//		int width = getWidth();
//		if (row == null || row.length < width) {
//			row = new byte[width];
//		}
//		int offset = (i + top) * dataWidth + left;
//		System.arraycopy(yuvData, offset, row, 0, width);
//		return row;
//	}

	private byte[] convertNV21toRGB888(boolean hsvFilter) {
		final int w = getWidth();
		final int h = getHeight();
		final int right = left + w;
		final int size = dataWidth * dataHeight;
		final int rsize = w * h * 3;
		final int start = top * dataWidth;
		final int rw = w * 3;
		
		int x, u, v, y1, y2, y3, y4, r;
        byte[] result;
        int deadline = rsize - rw;
        result = buffer;
        
        x = 0;
        r = 0;
        for (int i=start, k=start/2; i<size; i+=2, k+=2) {
        	if (x >= left && x < right) {
	            y1 = yuvData[i] & 0xff;
	            y2 = yuvData[i + 1] & 0xff;
	            y3 = yuvData[dataWidth + i] & 0xff;
	            y4 = yuvData[dataWidth + i + 1] & 0xff;
	
	            v = yuvData[size + k] & 0xff;
	            u = yuvData[size + k + 1] & 0xff;
	            u = u - 128;
	            v = v - 128;
	
	            getRGB(y1, u, v, result, r, hsvFilter);
            	getRGB(y2, u, v, result, r + 3, hsvFilter);
	            getRGB(y3, u, v, result, r + rw, hsvFilter);
	            getRGB(y4, u, v, result, r + rw + 3, hsvFilter);
	            r += 6;
        	}

            x += 2;
            if (i != 0 && (i + 2) % dataWidth == 0) {
                i += dataWidth;
                x = 0;
            	r += rw;
                if (r >= deadline) break;
            }
        }
        
        return result;
    }
	
	private byte[] convertNV16toRGB888(boolean hsvFilter) {
		final int w = getWidth();
		final int h = getHeight();
		final int right = left + w;
		final int size = dataWidth * dataHeight;
		final int rsize = w * h * 3;
		final int start = top * dataWidth;
		
		int x, u, v, y1, y2, r;
        byte[] result;
        int deadline = rsize;
        result = buffer;
        
        x = 0;
        r = 0;
        for (int i=start, k=start; i<size; i+=2, k+=2) {
        	if (x >= left && x < right) {
	            y1 = yuvData[i] & 0xff;
	            y2 = yuvData[i + 1] & 0xff;
	
	            u = yuvData[size + k] & 0xff;
	            v = yuvData[size + k + 1] & 0xff;
	            u = u - 128;
	            v = v - 128;
	
            	getRGB(y1, u, v, result, r, hsvFilter);
	            getRGB(y2, u, v, result, r + 3, hsvFilter);
	            r += 6;
        	}

            x += 2;
            if (i != 0 && (i + 2) % dataWidth == 0) {
                x = 0;
               	if (r >= deadline) break;
            }
        }
        
        return result;
    }
	
	private byte[] convertYV12toRGB888(boolean uv, boolean hsvFilter) {
		final int w = getWidth();
		final int h = getHeight();
		final int right = left + w;
		final int size = dataWidth * dataHeight;
		final int usize = size + (dataWidth / 2) * (dataHeight / 2);
		final int rsize = w * h * 3;
		final int start = top * dataWidth;
		final int rw = w * 3;
		
		int x, u, v, y1, y2, y3, y4, r;
        byte[] result;
        int deadline = rsize - rw;
        result = buffer;
        
        int uoffset, voffset;
        if (uv) {
        	uoffset = size;
        	voffset = usize;
        }
        else {
        	uoffset = usize;
        	voffset = size;
        }

        x = 0;
        r = 0;
        for (int i=start, k=start/4; i<size; i+=2, k+=1) {
        	if (x >= left && x < right) {
	            y1 = yuvData[i] & 0xff;
	            y2 = yuvData[i + 1] & 0xff;
	            y3 = yuvData[dataWidth + i] & 0xff;
	            y4 = yuvData[dataWidth + i + 1] & 0xff;
	
	            v = yuvData[voffset + k] & 0xff;
	            u = yuvData[uoffset + k] & 0xff;
	            u = u - 128;
	            v = v - 128;
	
	            getRGB(y1, u, v, result, r, hsvFilter);
            	getRGB(y2, u, v, result, r + 3, hsvFilter);
	            getRGB(y3, u, v, result, r + rw, hsvFilter);
	            getRGB(y4, u, v, result, r + rw + 3, hsvFilter);
	            r += 6;
        	}

            x += 2;
            if (i != 0 && (i + 2) % dataWidth == 0) {
                i += dataWidth;
                x = 0;
            	r += rw;
                if (r >= deadline) break;
            }
        }
        
        return result;
	}
	
	@Override
	public byte[] getRGBMatrix(int format, boolean hsvFilter) {
		if (format == ImageFormat.NV21) {
			return convertNV21toRGB888(hsvFilter);
		}
		else if (format == ImageFormat.NV16) {
			return convertNV16toRGB888(hsvFilter);
		}
		else if (format == ImageFormat.YV12) {
			return convertYV12toRGB888(false, hsvFilter);
		}
		else if (format == 18) {
			return convertYV12toRGB888(true, hsvFilter);
		}
		else
			return null;
	}
	
	@Override
	public byte[] getMatrix() {
		byte[] yuv = yuvData;
		byte[] matrix;
		int w = getWidth();
		int h = getHeight();
		
		if (w == dataWidth && h == dataHeight) {
			return yuv;
		}
		
		int area = w * h;
		matrix = new byte[area];
		int inputOffset = top * dataWidth + left;
		if (w == dataWidth) {
			System.arraycopy(yuv, inputOffset, matrix, 0, area);
			return matrix;
		}
		int outputOffset = 0;
		for (int i = 0; i < h; i++) {
			System.arraycopy(yuv, inputOffset, matrix, outputOffset, w);
			inputOffset += dataWidth;
			outputOffset += w;
		}
		
		return matrix;
	}

	@Override
	public boolean isCropSupported() {
		return true;
	}

//	@Override
//	public LuminanceSource crop(int left, int top, int width, int height) throws IllegalArgumentException{
//		return new PlanarYUVLuminanceSource(yuvData, dataWidth, dataHeight, left, top, width, height);
//	}

//	/**
//	 * 
//	 * @param bitmap
//	 * @return Y only
//	 */
//	private static byte[] getPlanarYUVData(Bitmap bitmap) {
//		if (bitmap.isRecycled()) return null;
//		
//		int w = bitmap.getWidth();
//		int h = bitmap.getHeight();
//		byte[] Y = new byte[w * h];
//
//		for (int j = 0; j < h; j++)
//			for (int i = 0; i < w; i++) {
//				int color = bitmap.getPixel(i, j);
//				int R = (color >> 16) & 0xFF;
//				int G = (color >> 8) & 0xFF;
//				int B = color & 0xFF;
//				int y = (int) (0.30 * R + 0.59 * G + 0.11 * B);
//				y = y > 255 ? 255 : y < 0 ? 0 : y;
//				Y[i + j * w] = (byte)y;
//			}
//		return Y;
//	}
}


