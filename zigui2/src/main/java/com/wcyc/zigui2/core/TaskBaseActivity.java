/*
* 文 件 名:TaskBaseActivity.java
* 创 建 人： 姜韵雯
* 日    期： 2014-10-24
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;
/**
 * 基类activity，在创建的时候会加入app的list，finish的时候从app移除。
 * Baseactivity也继承于他
 * 日    期： 2014-10-24
 * 版 本 号： 1.00
 * 
 * @author 姜韵雯
 * @version 1.00
 * 
 */
public class TaskBaseActivity extends FragmentActivity {

	private final  String className = this.getClass().getName();
	
	private SharedPreferences spf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CCApplication.app.addActivity(this);
	}
	@Override
	public void finish() {
		super.finish();
		CCApplication.app.removeActivity(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		//友盟统计开始
		MobclickAgent.onPageStart(className.substring(className.lastIndexOf(".") + 1));
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		//界面隐藏，调用友盟的接口
		MobclickAgent.onPageEnd(className.substring(className.lastIndexOf(".") + 1));
		MobclickAgent.onPause(this);
	}

	/**
	 * 跳转到新的activity
	 * @param cls
	 */
	public void newActivity(Class <?> cls , Bundle bundle){
		Intent intent=new Intent(getApplicationContext(),cls);
		if(bundle != null)intent.putExtras(bundle);
		startActivity(intent);
	}
	
	public void newActivityForResult(Class <?> cls , Bundle bundle,int code){
		Intent intent=new Intent(getApplicationContext(),cls);
		if(bundle != null)intent.putExtras(bundle);
		startActivityForResult(intent,code);
	}
	//by lxf
	/**
	 * 获取用户id.
	 * @return 用户id
	 */
	public String getUserID() {
		String userID = null;
		if (spf == null) {
			spf = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
			// 用户id
		}
		userID = spf.getString("userID", "");
		return userID;
	}
	
	//by lxf
	/**
	 * 获取孩子id.
	 * @return 孩子id
	 */
	public String getChildID() {
		String childID = null;
		if (spf == null) {
			spf = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
		}
		childID = spf.getString("childID", "");
		return childID;
	}
	
	//by lxf
	/**
	 * 获取用户id
	 * @return 老师班级id
	 */
	public String getTeacherClassID() {
		String teacherClassID = null;
		if (spf == null) {
			spf = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
			// 用户id
		}
		teacherClassID = spf.getString("teacherClassID", "");
		return teacherClassID;
	}
}
