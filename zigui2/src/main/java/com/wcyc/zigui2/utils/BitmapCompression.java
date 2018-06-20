package com.wcyc.zigui2.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 图片压缩工具类
 * @author 谭园园
 *
 */

public class BitmapCompression {

	/**
	 * 根据路径获取图片并压缩
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getimage(String srcPath) {  
	        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
	        newOpts.inJustDecodeBounds = true;  
	        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
	          
	        newOpts.inJustDecodeBounds = false;  
	        int w = newOpts.outWidth;  
	        int h = newOpts.outHeight;  
	        //现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为  
	        float hh = 1280f;//这里设置高度为1280f
	        float ww = 720f;//这里设置宽度为720f  
	        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
	        int be = 1;//be=1表示不缩放
	        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
	            be = (int) (newOpts.outWidth / ww);
	        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
	            be = (int) (newOpts.outHeight / hh);
	        }  
	        if (be <= 0)
	            be = 1;
	        newOpts.inSampleSize = be;//设置缩放比例
	        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
	        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	        if(bitmap!=null){
	        	return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
	        }else{
	        	return null;
	        }
	    }


	/**
	 * 根据路径获取图片并压缩
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getimageTwo(String srcPath) {
		Bitmap mBitmap = null;
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
		float ww = 720f;//这里设置宽度为720f
//	        float hh = 1280f;//这里设置高度为1280f
		float hh = 720f;//这里设置高度为720f 的像素可以了  比例大些
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放

		if (w >= h && h > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = Math.round(newOpts.outHeight / hh);
		} else if (w < h && w > hh) {//如果高度高的话根据宽度固定大小缩放
			be = Math.round(newOpts.outWidth / ww);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		int newWidth = bitmap.getWidth();
		int newHeight = bitmap.getHeight();
		if (newWidth >= newHeight) {
			float width = 720*((float)newWidth/(float) newHeight);
			mBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(width), 720, true);
		} else {
			float height = 720*((float)newHeight/(float)newWidth);
			mBitmap = Bitmap.createScaledBitmap(bitmap, 720, Math.round(height), true);
		}

		if(mBitmap !=null){
//	        	return compressImageTwo(bitmap);//压缩好比例大小后再进行质量压缩
			return mBitmap;
		}else{
			return null;
		}

	}
/**
 * 质量压缩
 * @param image
 * @return
 */
	public static Bitmap compressImage(Bitmap image) {  
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = 4;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, bitmapOptions);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }
	/**
	 * 质量压缩
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage1(Bitmap image) {
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = 4;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

			int options = 100;
		while ( baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, bitmapOptions);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}

}
