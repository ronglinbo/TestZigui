/*
 * 文 件 名:WebviewActivity.java

 */
package com.wcyc.zigui2.newapp.activity;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.core.TaskBaseActivity;
import com.wcyc.zigui2.newapp.home.NewAttendanceActivity;
import com.wcyc.zigui2.newapp.home.NewCommentActivity;
import com.wcyc.zigui2.newapp.home.NewHomeworkActivity;
import com.wcyc.zigui2.newapp.home.NewSelectStudentActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.widget.ProgressWebView;

/**
 * 
 */
@SuppressLint("JavascriptInterface")
public class WebviewActivity extends TaskBaseActivity implements
		OnClickListener {

	/**
	 * 支付状态，欠费为false 默认不欠费为true
	 */
	private boolean payStatus = true;
	private ProgressWebView contentWebView = null;
	private String url = null;
	private JsInterface jsInterface;
	// 未读消息textview
	private TextView unreadLabel;
	private HashMap<String,String> additionalHttpHeaders = null;
	private int CHOOSE_STUDENT = 100;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		CCApplication.app.finishAllActivity();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_web);
		initView();
		url = getIntent().getStringExtra("url");
		if (url == null) {
			url = "file:///android_asset/www/wrong.html";
		}
		Bundle bundle = getIntent().getExtras();
		additionalHttpHeaders = (HashMap<String, String>) bundle.getSerializable("para");
		contentWebView = (ProgressWebView) findViewById(R.id.webview);
		setWebView();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onClick(View view) {
		// 如果欠费就不允许跳转
		if (!payStatus)
			return;
		switch (view.getId()) {
		}
	}

	private void setWebView() {

		WebSettings webSettings = contentWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setSavePassword(false);
		webSettings.setDefaultTextEncodingName("gb2312");
		jsInterface = new JsInterface();
		contentWebView.addJavascriptInterface(jsInterface, "android");
		if(additionalHttpHeaders == null){
			contentWebView.loadUrl(url);
		}else{
			contentWebView.loadUrl(url, additionalHttpHeaders);
		}
	}
	
	
	/**
	 * 负责和js通信的所有方法
	 * 
	 * 
	 */
	public class JsInterface {
		
		@SuppressLint("JavascriptInterface")
		//html5界面返回
		public void back(){
			System.out.println("back");
			WebviewActivity.this.finish();
		}
		
		//发布作业
		public void addNewHomework(){
			System.out.println("addNewHomework");
			newActivity(NewHomeworkActivity.class,null);
		}
		
		//发布点评
		public void addNewComment(){
			System.out.println("addNewComment");
			newActivity(NewCommentActivity.class,null);
		}
				
		//发布考勤
		public void addNewAttendance(){
			System.out.println("addNewAttendance");
			newActivity(NewAttendanceActivity.class,null);
		}
		//发布考试
		public void addNewExamination(){
			System.out.println("addNewExamination");
			//newActivity(NewHomeworkActivity.class,null);
		}
		//选择学生
		public void chooseStudent(){
			System.out.println("chooseStudent");
			Intent intent = new Intent(WebviewActivity.this,NewSelectStudentActivity.class);
			startActivityForResult(intent,CHOOSE_STUDENT);
		}
		/**
		 * 跳入另外一个webActivity
		 * 
		 * @param newUrl
		 *            跳转的地址
		 */
		@SuppressLint("JavascriptInterface")
		public void goWebActivity(String newUrl) {
			Intent intent = new Intent(WebviewActivity.this,
					BaseWebviewActivity.class);
			intent.putExtra("url", Constants.WEBVIEW_URL + "/" + newUrl);
			WebviewActivity.this.startActivity(intent);
		}

		/**
		 * 关闭此activity
		 */
		@SuppressLint("JavascriptInterface")
		public void finishActivity() {
			WebviewActivity.this.finish();
		}

		/**
		 * 本地代码调用js代码 无参
		 */
		@SuppressLint("JavascriptInterface")
		public void callJs() {
			contentWebView.loadUrl("javascript:javacalljs()");
		}

		/**
		 * 本地代码调用js代码
		 * 
		 * @param data
		 *            String 类型的参数
		 */
		@SuppressLint("JavascriptInterface")
		public void callJs(String data) {
			// 传递参数调用
			contentWebView.loadUrl("javascript:javacalljswithargs(" + "'"
					+ data + "'" + ")");
		}


	}

	/**
	 * 跳转到新的activity
	 * 
	 * @param cls
	 *            新activity的class
	 */
	public void newActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(WebviewActivity.this, cls);
		if (bundle != null)
			intent.putExtras(bundle);
		WebviewActivity.this.startActivity(intent);
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateUnreadLabel();
		contentWebView.loadUrl(url);
	}

	
	
	// 当界面被遮盖将webview设为null
	@Override
	protected void onPause() {
		super.onPause();
		contentWebView.loadUrl(null);
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			if (count > 99) {
				unreadLabel.setText("99+");
			} else {
				unreadLabel.setText(String.valueOf(count));
			}
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}