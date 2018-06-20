package com.wcyc.zigui2.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
/**
 * 加载大图片工具通用类：解决android加载大图片时报OOM异常
 * @author xfliu
 * @version 1.11
 * @since 1.11
 */
public class BitmapTool {

	public static final int UNCONSTRAINED = -1;
	// 获得设置信息
	public static Options getOptions(String path) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		return options;
	}

	// 获得图像
	private static Bitmap getBitmapByPath(String path, Options options,
			int screenWidth, int screenHeight) throws FileNotFoundException {
		if (options != null) {
			Rect r = getScreenRegion(screenWidth, screenHeight);
			// 取得图片的宽和高
			int w = r.width();
			int h = r.height();
			// 计算缩放比例
			int inSimpleSize = computeSampleSize(options, -1, w * h);
			// 设置缩放比例
			options.inSampleSize = inSimpleSize;
			options.inJustDecodeBounds = false;
		}
		Bitmap bitmap = null;
		try {
			// 加载压缩后的图片
			bitmap = BitmapFactory.decodeFile(path, options);
		}catch(OutOfMemoryError e){
			e.printStackTrace();
		}
		return bitmap;
	}

	private static Rect getScreenRegion(int width, int height) {
		return new Rect(0, 0, width, height);
	}

	// 获取需要进行缩放的比例，即options.inSampleSize
	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		// 获得图片的宽和高
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
				.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
				.min(Math.floor(w / minSideLength),
						Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == UNCONSTRAINED)
				&& (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	// 返回加载后的大图片
	public static Bitmap getBitmap(Context context ,String path) throws FileNotFoundException {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		return BitmapTool.getBitmapByPath(path, BitmapTool.getOptions(path),
				dm.widthPixels, dm.heightPixels);
	}

	//保存图片
	public static File saveImage(Context context,Bitmap bm,String name) throws IOException {
		String subForder = Environment.getExternalStorageDirectory()+"/ZIGUI_TEMP/";
		File foder = new File(subForder);
		if (!foder.exists()) {
			foder.mkdirs();
		}

		File file = new File(subForder, name);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		bos.flush();
		bos.close();

		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		context.sendBroadcast(intent);

		return file;
	}

}
