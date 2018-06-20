package com.wcyc.zigui2.newapp.asynctask;
/**
 * @author xiehua
 * @version 1.0
 */
public interface JoinGroupAsyncTaskListener{
	void onJoinGroupCancelled();  //请求被打断
	void onJoinGroupComplete(String result);//请求完成
}
