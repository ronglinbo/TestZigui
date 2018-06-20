package com.wcyc.zigui2.listener;
/**
 * 
 */
public interface HttpRequestAsyncTaskListener{
	void onRequstComplete(String result);  //请求完成
	void onRequstCancelled();  //请求被打断
}
