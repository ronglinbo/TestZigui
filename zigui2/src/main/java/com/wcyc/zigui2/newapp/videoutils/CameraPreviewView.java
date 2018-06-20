package com.wcyc.zigui2.newapp.videoutils;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * 相机的预览SurfaceView
 * 
 * @ClassName: CameraPreviewView
 * @Description:
 * @creationtime: 2016-9-21 上午9:51:23
 */
public class CameraPreviewView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder surfaceHolder;

	// 默认的XML文件解析方法是调用View的View(Context , AttributeSet)构造函数构造View
	public CameraPreviewView(Context context, AttributeSet attrs) {
		super(context, attrs);

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
	}

	/**
	 * 启动预览
	 *
	 * @creationtime: 2016-9-21 上午11:19:14
	 */
	public void startPreview() {
		Camera mCamera = CameraHelper.getInstance().openCamera();
		if (mCamera != null && surfaceHolder != null) {
			try {
				// 设置相机 录制视频的方向 竖屏
				mCamera.setDisplayOrientation(90);

				mCamera.setPreviewDisplay(surfaceHolder);
				mCamera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭预览
	 * @creationtime: 2016-9-21 上午11:19:27
	 */
	public void stopPreview() {
		CameraHelper.getInstance().releaseCamera();
	}

	/**
	 * 
	 * @return
	 * @creationtime: 2016-9-22 下午3:04:24
	 */
	public Camera getCamera() {
		return CameraHelper.getInstance().getCamera();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		startPreview();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopPreview();
	}

}
