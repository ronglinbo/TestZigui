package com.wcyc.zigui2.newapp.videoutils;

import android.graphics.Bitmap;

import com.wcyc.zigui2.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件工具类
 * 
 * @ClassName: VideoFileUtil
 * @Description:
 * @creationtime: 2016-9-22 上午9:31:21
 */
public class VideoFileUtil {

	/**
	 * 判断文件是否是视频文件
	 * 
	 * @param file
	 * @return
	 * @creationtime: 2016-9-22 上午9:33:19
	 */
	public static boolean isVideoFile(File file) {
		if (file != null && file.exists() && file.isFile()
				&& file.getName().indexOf(AppConstant.VIDEO_FILE_PREFIX) >= 0
				&& file.getName().indexOf(AppConstant.VIDEO_FILE_SUFFIX) > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 判断文件是否是视频文件
	 * 
	 * @param filePath
	 * @return
	 * @creationtime: 2016-9-22 上午9:35:11
	 */
	public static boolean isVideoFile(String filePath) {
		return isVideoFile(new File(filePath));
	}

	/**
	 * 创建视频文件对象
	 * 
	 * @return
	 * @creationtime: 2016-9-22 上午9:42:36
	 */
	public static File createVideoFile() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String date = formatter.format(new Date());
		File videoFile = new File(AppConstant.DIR_VIDEO
				 + date+ AppConstant.VIDEO_FILE_SUFFIX);
		if (!videoFile.getParentFile().exists()) {
			videoFile.getParentFile().mkdirs();
		}

		return videoFile;
	}

	/**
	 * 创建照片文件对象
	 * 
	 * @return
	 * @creationtime: 2016-9-22 上午9:43:31
	 */
	public static File createPhotoFile() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String date = formatter.format(new Date());

		File photoFile = new File(AppConstant.DIR_PHOTO
				+ AppConstant.PHOTO_FILE_PREFIX + date + "_"
				+ System.currentTimeMillis() + AppConstant.PHOTO_FILE_SUFFIX);
		if (!photoFile.getParentFile().exists()) {
			photoFile.getParentFile().mkdirs();
		}

		return photoFile;
	}

	public static String savaVideoThumbmail(String filePath,Bitmap bitm){
		String fileStart=filePath.substring(0,filePath.lastIndexOf("."));
		String fileEnd=filePath.substring(filePath.lastIndexOf("."));
		String imagePathAndName=fileStart+".jpeg";
		if (bitm != null) {
			File file = new File(imagePathAndName);
			if(!file.exists()){
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);
					bitm.compress(Bitmap.CompressFormat.JPEG, 100, out);
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("===已有缩略图==");
			}
			return imagePathAndName;
		}
		return null;
	}
}
