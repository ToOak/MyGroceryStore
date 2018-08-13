package com.wochacha.scan.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ImagesManager {

	public static Bitmap Rotate(Bitmap bitmap, float degrees) {
		if (bitmap == null)
			return null;

		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		try {
			return Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param bmp
	 * @return "" or file path
	 */
	public static String SaveBarcodeBitmap(Bitmap bmp, String name) {
		String filename;
		if (HardWare.isSDCardAvailable() == false || HardWare.isSDCardFull() == true)
			filename = FileManager.getInnerImagesPath() + name;
		else
			filename = FileManager.getExImagesPath() + name;

		if (SaveBitmap(bmp, filename))
			return filename;
		else
			return "";
	}

	public static boolean SaveBitmap(Bitmap bmp, String fileName) {
		File mfile = new File(fileName);
		if (bmp != null) {
			try {
				BufferedOutputStream baos = new BufferedOutputStream(new FileOutputStream(mfile));
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				baos.flush();
				baos.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

}