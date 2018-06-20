package com.wcyc.zigui2.newapp.asynctask;
/**
 * @author xiehua
 * @version 1.0
 */
public interface ImageUploadAsyncTaskListener{
	void onImageUploadCancelled();  //请求被打断
	void onImageUploadComplete(String result);//请求完成
}
