package com.wcyc.zigui2.listener;
/**
 * @author xfliu
 * @version 1.13
 */
public interface ImageUploadAsyncTaskListener{
	void onImageUploadComplete(String result,String ID);  //请求完成
	void onImageUploadCancelled();  //请求被打断
	void onImageUploadComplete(String result);//请求完成
}
