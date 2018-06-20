/** 
 * Project Name:JianeyeDrivingRecorder 
 * File Name:CameraHelper.java 
 * Package Name:com.jianeye.car.recorder.view.util 
 * Date:2016-9-21上午9:16:19 
 * Copyright (c) 2016, 广州市戬智眼安防科技有限公司 All Rights Reserved. 
 */ 
package com.wcyc.zigui2.newapp.videoutils;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.view.WindowManager;

/** 
 * 摄像头使用帮助类
 * @ClassName: CameraHelper
 * @Description: 
 * @author: SYF
 * @creationtime: 2016-9-21 上午9:16:19
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CameraHelper {

	public static final int INVALID_CAMERA_ID = -1;
	private static CameraHelper instance;
	private Camera mCamera;
	private Parameters parameters;
	
	private CameraHelper() {
		
	}
	
	public static CameraHelper getInstance() {
		if (instance == null) {
			instance = new CameraHelper();
		}
		
		return instance;
	}
	
	public Camera getCamera() {
		return mCamera;
	}
	
	/**
	 * 打开摄像头
	 *  
	 * @author: SYF
	 * @creationtime: 2016-9-21 上午9:15:21
	 */
	public Camera openCamera() {
		if (mCamera == null) {
			int numberOfCameras = Camera.getNumberOfCameras();
	        CameraInfo cameraInfo = new CameraInfo();
	        for (int i = 0; i < numberOfCameras; i++) {
	        	Camera.getCameraInfo(i, cameraInfo);
	            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
	                mCamera = Camera.open(i);
	                initCamera();
	                break;
	            }
	        }
        }
		
		return mCamera;
	}
	
	/**
	 * 相机参数的初始化设置  
	 *  
	 * @author: SYF
	 * @creationtime: 2016-9-21 下午2:51:52
	 */
	private void initCamera() {

		// 获取屏幕分辨率
//		int width = 1920;
//		int height = 1080;

//		int screen_width=VideoActivity.WIDTH;
//		int screen_height=VideoActivity.HEIGHT;
//
////		if(screen_width<width){
//			width=screen_width;
//			height=screen_height;
////		}
//		System.out.println(width+"=width==c==height="+height);

		parameters=mCamera.getParameters();
		Camera.Size aa=parameters.getPreviewSize();
		int bb=aa.width;
		int cc=aa.height;
		System.out.println(bb+"=CAMERA.Size.bb===1===CAMERA.Size.cc="+cc);

//		if(parameters.isSmoothZoomSupported()){//是否支持对焦
//			System.out.println("===是支持对焦==");
//			int zoomValue=parameters.getZoom();
//			zoomValue +=5;
//			parameters.setZoom(zoomValue);
//		}else{
//			System.out.println("===否支持对焦==");
//		}
//
//		if(parameters.isZoomSupported()){
//			System.out.println("=a==是支持对焦==");
//			int zoomValue=parameters.getZoom();
//			zoomValue +=5;
//			parameters.setZoom(zoomValue);
//		}else{
//			System.out.println("=a==否支持对焦==");
//		}

//    	parameters.setPreviewSize(width, height);
    	parameters.setFlashMode(Parameters.ANTIBANDING_OFF);     
    	parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE); //连续对焦
    	mCamera.setParameters(parameters);  
    	mCamera.cancelAutoFocus();// 如果要实现连续的自动对焦，这一句必须加上  
      } 
	
	/**
	 * 释放摄像头
	 *  
	 * @author: SYF
	 * @creationtime: 2016-9-21 上午9:14:37
	 */
	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

}
