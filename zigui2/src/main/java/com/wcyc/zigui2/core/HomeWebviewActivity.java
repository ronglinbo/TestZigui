/*
 * 文 件 名:HomeWebviewActivity.java
 * 创 建 人： 姜韵雯
 * 日    期： 2014-10-23
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.Child;
import com.wcyc.zigui2.bean.ChildMember;
import com.wcyc.zigui2.bean.MemberBean;
import com.wcyc.zigui2.bean.OrderResult;
import com.wcyc.zigui2.chat.MainActivity;
import com.wcyc.zigui2.contactselect.contactMainActivity;
import com.wcyc.zigui2.newapp.activity.LoginActivity;

import com.wcyc.zigui2.newapp.bean.AttachmentBean;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;
import com.wcyc.zigui2.newapp.widget.AttachmentActionOption;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.ProgressWebView;

/**
 * 首页界面的activity 日 期： 2014-10-23 版 本 号： 1.00
 * 
 * @author 姜韵雯
 * @version 1.00
 * 
 */
@SuppressLint("JavascriptInterface")
public class HomeWebviewActivity extends TaskBaseActivity implements
		OnClickListener {

	/**
	 * 支付状态，欠费为false 默认不欠费为true
	 */
	private boolean payStatus = true;
	private ProgressWebView contentWebView = null;
	private String url = null;
	private JsInterface jsInterface;
	private Button[] mTabs;
	// 未读消息textview
	private TextView unreadLabel;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		CCApplication.app.finishAllActivity();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_web);
		initView();
		url = getIntent().getStringExtra("url");
//		url = url + "&version=" + getCurVersion();
		if (url == null) {
			// url =
			// "http://10.0.7.50:8080/zgw/login.do?method=loginsucceed_m&userid=000&childid=001";
			url = "file:///android_asset/www/wrong.html";
		}
		contentWebView = (ProgressWebView) findViewById(R.id.webview);
		setWebView();
		DataUtil.cleanTempFile(true, 12);
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		mTabs = new Button[3];
		mTabs[0] = (Button) findViewById(R.id.first_pager);
		mTabs[1] = (Button) findViewById(R.id.friend_pager);
		mTabs[2] = (Button) findViewById(R.id.my_pager);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
		for (Button bt : mTabs) {
			bt.setOnClickListener(this);
		}
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
		case R.id.first_pager:
			break;
		case R.id.friend_pager:
			goMainActivity();
			break;
		case R.id.my_pager:
			goSetting();
			break;
		}
	}

	private void setWebView() {

		// contentWebView.setInitialScale(25);
		WebSettings webSettings = contentWebView.getSettings();

		// webSettings.setSavePassword(false);
		// webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		// webSettings.setUseWideViewPort(true);
		// webSettings.setLoadWithOverviewMode(true);
		// webSettings.setSupportZoom(false);
		// webSettings.setBuiltInZoomControls(false);
		// webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// webSettings.setDomStorageEnabled(true);//设置可以使用localStorage
		// webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//默认使用缓存
		// webSettings.setAppCacheMaxSize(8*1024*1024);//缓存最多可以有8M
		// webSettings.setAllowFileAccess(true);//可以读取文件缓存(manifest生效)
		// webSettings.setAppCacheEnabled(true);//应用可以有缓存
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setSupportZoom(false);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setSavePassword(false);
		webSettings.setDefaultTextEncodingName("gb2312");
		jsInterface = new JsInterface();
		contentWebView.addJavascriptInterface(jsInterface, "android");
	}

	/**
	 * 负责和js通信的所有方法
	 * 
	 * @author 姜韵雯
	 * 
	 */
	public class JsInterface {

		/**
		 * 跳入另外一个webActivity
		 * 
		 * @param newUrl
		 *            跳转的地址
		 */
		@SuppressLint("JavascriptInterface")
		public void goWebActivity(String newUrl) {
			Intent intent = new Intent(HomeWebviewActivity.this,
					BaseWebviewActivity.class);
			intent.putExtra("url", Constants.WEBVIEW_URL + "/" + newUrl);
			HomeWebviewActivity.this.startActivity(intent);
		}

		/**
		 * 关闭此activity
		 */
		@SuppressLint("JavascriptInterface")
		public void finishActivity() {
			HomeWebviewActivity.this.finish();
		}

		/**
		 * 跳转到原生的activity
		 * 
		 * @param id
		 *            根据id来判断应该跳入我的哪个activity里面
		 */
		@SuppressLint("JavascriptInterface")
		public void goOtherActivity(int id) {

		}

		/**
		 * 老师点评 web跳转到原生界面
		 * 
		 * @param date
		 *            当前点评时间
		 * @param teacherID
		 *            老师ID
		 */
		@SuppressLint("JavascriptInterface")
		public void jumpTeacherComment(String date, String teacherID) {
		}

		/**
		 * html页面跳转到选择小孩界面
		 * 
		 * @param teacherClassID
		 *            void
		 */
		@SuppressLint("JavascriptInterface")
		public void jumpContactMainActivity(String teacherClassID,
				String teacherId) {
			Intent intent = new Intent(HomeWebviewActivity.this,
					contactMainActivity.class);
			intent.putExtra("teacherId", teacherId);
			intent.putExtra("teacherClassID", teacherClassID);
			startActivity(intent);
		}
		/**
		 * 主页调家长成绩页面
		 * 
		 * @param 
		 *        
		 */
		@SuppressLint("JavascriptInterface")
		public void parentsScoreActivity(String childName,
				String studentId,String flag) {
		}

		/**
		 * 跳转到老师作业发布原生界面.
		 * 
		 * @param classID
		 *            班级ID
		 * @param subjectID
		 *            科目ID
		 * @param courseUrl
		 *            课程ID
		 * @param teacherID
		 *            教师ID
		 * @param courseData
		 *            课程数据
		 */
		@SuppressLint("JavascriptInterface")
		public void jumpPutNewWorkActivity(String classID, String subjectID,
				String courseUrl, String teacherID, String courseData) {

		}
		/**
		 * 跳转到老师班级动态原生界面.
		 * 
		 * @param userid
		 *            用户ID
		 */
		@SuppressLint("JavascriptInterface")
		public void jumpTeacherClassDynamicsActivity(String userid) {

		}
		/**
		 * 跳转到家长班级动态原生界面.
		 *
		 *            用户ID
		 */
		@SuppressLint("JavascriptInterface")
		public void jumpStudentClassDynamicsActivity(String uid,String cid,String ismain) {

		}

		/**
		 * 跳转到原生的activity.
		 * <p>
		 * 根据id来判断应该跳入我的哪个activity里面
		 * 
		 * @param childID
		 *            孩子ID
		 */
		@SuppressLint("JavascriptInterface")
		public void ChangeChild(String childID) {
			Log.d("pengh", childID + "");
			SharedPreferences sp;

			sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
				// 用户id

			Editor ed = sp.edit();
			ed.putString("childID", childID);
			ed.commit();
			// 更新地址，避免当切换小孩后再从其它界面跳回来刷新界面的时候还是之前第一个小孩的界面
			url = new StringBuilder(Constants.WEBVIEW_URL)
					.append("/mobileframe.do?method=loginsucceed_m&")
					.append("userid").append("=")
					.append(CCApplication.getInstance().getPresentUser().getUserId())
					.append("&").append("childid").append("=").append(childID)
					.append("&version=")
					.append(getCurVersion())
					.toString();

			return;
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

		/**
		 * 页面出错后点击刷新按钮后的操作
		 */
		@SuppressLint("JavascriptInterface")
		public void refesh() {
			contentWebView.loadUrl(contentWebView.getNewUrl());
		}

		/**
		 * 设置支付情况
		 * 
		 * @param status
		 *            支付情况，true代表已支付 ，false代表已欠费
		 */
		@SuppressLint("JavascriptInterface")
		public void setPaystatus(boolean status) {
			payStatus = status;
		}

		/**
		 * 跳转到套餐选择界面
		 * 
		 * @param childID
		 *            需要缴费的小孩ID
		 */
		@SuppressLint("JavascriptInterface")
		public void goPackageSelect(String childID,String userID) {

		}
		/**
		 * 跳转到支付卡充值界面
		 * 
		 * @param childID
		 *            需要缴费的小孩ID
		 */
		/**
		 * @param childID
		 * @param userID
		 */
		@SuppressLint("JavascriptInterface")
		public void goPayCard(String childID,String userID) {

		}
		
		@SuppressLint("JavascriptInterface")
		public void handleOnClickAttachment(String url,String name){
			System.out.println("handleOnClickAttachment:"+url+" name:"+name);
			Attachment attach = new AttachmentBean(). new Attachment();
			attach.setAttachementName(name);
			attach.setAttachementUrl(url);
			AttachmentActionOption option = new AttachmentActionOption(HomeWebviewActivity.this,attach);
			option.showAtLocation(contentWebView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	}

	/**
	 * 跳转到新的activity
	 * 
	 * @param cls
	 *            新activity的class
	 */
	public void newActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(HomeWebviewActivity.this, cls);
		if (bundle != null)
			intent.putExtras(bundle);
		HomeWebviewActivity.this.startActivity(intent);
	}


	/**
	 * 跳入贵友圈
	 */
	private void goMainActivity() {
		Intent intent = new Intent(HomeWebviewActivity.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		HomeWebviewActivity.this.finish();
	}

	/**
	 * 跳入我的界面
	 */
	public void goSetting() {

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateUnreadLabel();
		contentWebView.loadUrl(url);

		// TODO 禁用账号方法
		disable();
	}

	// 禁用家长和老师的方法
	protected void disable() {
		// 取当前用户名和密码
		SharedPreferences sp = getSharedPreferences("little_data", Context.MODE_PRIVATE);
		String userName = sp.getString("phoneNum", "");
		String password = sp.getString("phonePwd", "");

		// 得到设备ID
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String deviceID = telephonyManager.getDeviceId();

		String login_url = "/loginservice/login";
		String result = null;
		String version = getCurVersion();

		String pwdMd5 = DataUtil.encodeMD5(password);
		JSONObject json = new JSONObject();
		try {
			json.put("userName", userName);
			json.put("password", pwdMd5);
			json.put("deviceID", deviceID);

			json.put("mobileType", "android");
			json.put("version", version);
			String url = new StringBuilder(Constants.SERVER_URL).append(
					login_url).toString();
			result = HttpHelper.httpPostJson(url, json);
			System.out.println("++++" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result != null) {
			MemberBean member = JsonUtils.fromJson(result, MemberBean.class);
			int userType = member.getUserType();
			if (userType == Constants.PARENT_TYPE) {
				// 家长
				List<Child> list = member.getChildList();
				System.out.println("list"+list);
				if (list.size() >= 1) {
					String childid = getChildID();
					boolean flag = false;
					for (Child child : list) {
						
						if (child.getChildID().equals(childid)) {
							// 退出登陆
//							DataUtil.getToast("您的账号被禁用，请联系管理员");
//							CCApplication.app.logout();
//							CCApplication.app.finishAllActivity();
//							newActivity(LoginActivity.class, null);
							flag = true;
						}
					}
					if(!flag){
						DataUtil.getToast("您的账号被禁用，请联系管理员");
						CCApplication.app.logout();
						CCApplication.app.finishAllActivity();
						newActivity(LoginActivity.class, null);
					}
				} else {
					// 退出登陆
					DataUtil.getToast("您的账号被禁用，请联系管理员");
					CCApplication.app.logout();
					CCApplication.app.finishAllActivity();
					newActivity(LoginActivity.class, null);
				}
			} else {
				// 老师
			int code = 	member.getResultCode();
			if (code != Constants.SUCCESS_CODE) {
				//退出登陆
				DataUtil.getToast("您的账号被禁用，请联系管理员");
				CCApplication.app.logout();
				CCApplication.app.finishAllActivity();
				newActivity(LoginActivity.class, null);
			}
			
			}
		}
	}

	/**
	 * 获取系统本地版本号.
	 * 
	 * @return 系统本地版本号
	 */
	public String getCurVersion() {
		Properties properties = new Properties();

		try {
			InputStream stream = this.getAssets().open("ver.cfg");
			properties.load(stream);
		} catch (FileNotFoundException e) {
			return "100";
		} catch (IOException e) {
			return "100";
		} catch (Exception e) {
			return "100";
		}

		String version = String.valueOf(properties.get("Version").toString());
		System.out.println("ver is :" + version);

		return version;
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
		Log.i("HomeWebviewActivity",
				"HomeWebviewActivity  onDestroy");
		DataUtil.hasUnfinishedTask = false;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i("HomeWebviewActivity",
				"HomeWebviewActivity  onSaveInstanceState");
	}
}