package com.wcyc.zigui2.newapp.asynctask;
/**
 * @author xiehua
 * @version 1.0
 */
public interface AsyncTaskListener{
	void onCancelled();  //请求被打断
	void onComplete(String result);//请求完成
}
