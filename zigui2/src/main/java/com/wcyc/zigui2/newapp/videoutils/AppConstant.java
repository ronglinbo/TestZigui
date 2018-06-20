package com.wcyc.zigui2.newapp.videoutils;

import android.os.Environment;

/** 
 * @ClassName: AppConstant
 * @Description:
 * @creationtime: 2016-9-20 下午4:14:04
 */
public class AppConstant {

	/** 应用的基目录 */
	public static String DIR_ROOT = Environment.getExternalStorageDirectory() + "/";
	/** 照片目录 */
	public static String DIR_PHOTO = DIR_ROOT + "ZIGUI_Photos/";
	/** 视频目录 */
	public static String DIR_VIDEO = DIR_ROOT + "ZIGUI_Photos/";
	
	/** 视频文件的后缀名 */
	public static String VIDEO_FILE_PREFIX = "VID_";
	/** 视频文件的后缀名 */
	public static String VIDEO_FILE_SUFFIX = ".mp4";
	
	/** 照片文件的后缀名 */
	public static String PHOTO_FILE_PREFIX = "IMG_";
	/** 照片文件的后缀名 */
	public static String PHOTO_FILE_SUFFIX = ".jpg";
	
}
