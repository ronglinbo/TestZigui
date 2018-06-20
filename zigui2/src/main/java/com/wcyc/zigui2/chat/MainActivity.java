package com.wcyc.zigui2.chat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.util.NetUtils;
import com.umeng.analytics.MobclickAgent;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.core.HomeWebviewActivity;

import com.wcyc.zigui2.fragment.ChatHistoryFragment;
import com.wcyc.zigui2.fragment.ContactlistFragment;
import com.wcyc.zigui2.fragment.SettingsFragment;
import com.wcyc.zigui2.newapp.activity.LoginActivity;

import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.widget.QuickServicePublish;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.HttpHelper;

public class MainActivity extends FragmentActivity implements OnClickListener{

	protected static final String TAG = "MainActivity";
	// 未读消息textview
	private TextView unreadLabel;

	private Button[] mTabs;
	private ContactlistFragment contactListFragment;
	private ChatHistoryFragment chatHistoryFragment;
	private SettingsFragment settingFragment;
	private Fragment[] fragments;
	private int index;
	// 当前fragment的index
	private int currentTabIndex;
	private final int chatTabIndex = 1;
	private NewMessageBroadcastReceiver msgReceiver;
	// 账号在别处登录
	private boolean isConflict = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CCApplication.app.addActivity(this);
		setContentView(R.layout.activity_main);
		initView();
//		userDao = new UserDao(this);

		//显示所有人消息记录的fragment
		chatHistoryFragment = new ChatHistoryFragment();
		contactListFragment = new ContactlistFragment();
		settingFragment = new SettingsFragment();
		fragments = new Fragment[] { contactListFragment, chatHistoryFragment, settingFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chatHistoryFragment)
				.add(R.id.fragment_container, contactListFragment).hide(contactListFragment).show(chatHistoryFragment)
				.commitAllowingStateLoss();

		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
				.getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// 注册一个离线消息的BroadcastReceiver
//		IntentFilter offlineMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
//				.getOfflineMessageBroadcastAction());
//		registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);

		// 注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
	}
	
	/**
	 * 初始化组件
	 */
	private void initView() {
		String ver = getCurVersion();
		if(Integer.parseInt(ver) < Constants.NEW_VERSION ){
			unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
			mTabs = new Button[3];
			mTabs[0] = (Button) findViewById(R.id.first_pager);
			mTabs[1] = (Button) findViewById(R.id.friend_pager);
			mTabs[2] = (Button) findViewById(R.id.my_pager);
			// 把第一个tab设为选中状态
			mTabs[1].setSelected(true);
			currentTabIndex = 1;
			for (Button bt : mTabs) {
				if(bt != null)
					bt.setOnClickListener(this);
			}
		}else{
			mTabs = new Button[5];
			mTabs[0] = (Button) findViewById(R.id.message_pager);
			mTabs[1] = (Button) findViewById(R.id.service_pager);
			mTabs[2] = (Button) findViewById(R.id.quick_pager);
			mTabs[3] = (Button) findViewById(R.id.friend_pager);
			mTabs[4] = (Button) findViewById(R.id.my_pager);
			// 把第一个tab设为选中状态
			mTabs[3].setSelected(true);
			for (Button bt : mTabs) {
				bt.setOnClickListener(this);
			}
			unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		}
	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onClick(View view) {
		String ver = getCurVersion();
		if(Integer.parseInt(ver) < Constants.NEW_VERSION ){
			switch (view.getId()) {
			case R.id.first_pager:
				index = 0;
				goHome();
				break;
			case R.id.friend_pager:
				index = 1;
				break;
			case R.id.my_pager:
				index = 2;
				goSetting();
				break;
			}
		}else{
			switch(view.getId()){
			
			case R.id.quick_pager:
				QuickServicePublish publish = new QuickServicePublish(this);
				View button = this.findViewById(R.id.quick_pager);
				publish.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				//view.setSelected(true);
				//mTabs[3].setSelected(false);
				break;
			case R.id.friend_pager:
				index = 1;
				break;
			case R.id.my_pager:
				goSetting();
				break;
			}
		}
	}
	
	/**
	 * 跳入我的界面
	 */
	public void goSetting(){
//		Intent intent;
//		if(Constants.PARENT_STR_TYPE.equals(CCApplication.app.getPresentUser().getUserType())){
//			intent = new Intent(MainActivity.this,MyInformationActivity.class);
//		}else{
//			intent = new Intent(MainActivity.this,TeacherMyActivity.class);
//		}
//		MainActivity.this.startActivity(intent);
//		MainActivity.this.finish();
	}
	/**
	 * 跳入首页
	 */
	private void goHome() {
		NewMemberBean member = CCApplication.app.getMemberInfo();
		Bundle bundle = new Bundle();
		String url;
		if(Constants.PARENT_STR_TYPE.equals(CCApplication.app.getPresentUser().getUserType())){
		//if(member.getUserType() == Constants.PARENT_TYPE){		
			SharedPreferences sp= getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
			String userID = sp.getString("userID", "");
			String childID = sp.getString("childID", "");
			url = new StringBuilder(Constants.WEBVIEW_URL)
			.append("/mobileframe.do?method=loginsucceed_m&")
			.append("userid").append("=").append(userID)
			.append("&").append("childid").append("=")
			.append(childID)
			.append("&version=")
			.append(getCurVersion()).toString();
			
		}else{
			url = new StringBuilder(Constants.WEBVIEW_URL).append("/mobileframe.do?method=teacher_loginsucceed_m&")
	        		.append("userid").append("=").append(CCApplication.getInstance().getPresentUser().getUserId())
	        		.append("&version=")
	        		.append(getCurVersion())
	        		.toString();
		}
		bundle.putString("url", url);
		
		Intent intent=new Intent(MainActivity.this,HomeWebviewActivity.class);
		intent.putExtras(bundle);
		MainActivity.this.startActivity(intent);
		MainActivity.this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播接收者
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(ackMessageReceiver);
		} catch (Exception e) {
		}

//		try {
//			unregisterReceiver(offlineMessageReceiver);
//		} catch (Exception e) {
//		}

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		System.out.println("updateUnreadLabel");
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			if(count > 99){
				unreadLabel.setText("99+");
			}else{
				unreadLabel.setText(String.valueOf(count));
			}
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (CCApplication.getInstance().getContactList().get(Constants.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = CCApplication.getInstance().getContactList().get(Constants.NEW_FRIENDS_USERNAME)
					.getUnreadMsgCount();
		return unreadAddressCountTotal;
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

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
			
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			// EMMessage message =
			// EMChatManager.getInstance().getMessage(msgId);

			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			if (currentTabIndex == chatTabIndex) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (chatHistoryFragment != null) {
					chatHistoryFragment.refresh();
				}
			}
			// 注销广播，否则在ChatActivity中会收到这个广播
			abortBroadcast();
		}
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance().getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}
			abortBroadcast();
		}
	};

	/**
	 * 离线消息BroadcastReceiver
	 * sdk 登录后，服务器会推送离线消息到client，这个receiver，是通知UI 有哪些人发来了离线消息
	 * UI 可以做相应的操作，比如下载用户信息
	 */
//	private BroadcastReceiver offlineMessageReceiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String[] users = intent.getStringArrayExtra("fromuser");
//			String[] groups = intent.getStringArrayExtra("fromgroup");
//			if (users != null) {
//				for (String user : users) {
//					System.out.println("收到user离线消息：" + user);
//				}
//			}
//			if (groups != null) {
//				for (String group : groups) {
//					System.out.println("收到group离线消息：" + group);
//				}
//			}
//			abortBroadcast();
//		}
//	};
	
//	private UserDao userDao;
	


	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {
			chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				// 显示帐号在其他设备登录dialog
                System.out.println("环信 errorString:"+errorString);
				showConflictDialog();
			} else {
				chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
				if(NetUtils.hasNetwork(MainActivity.this))
					chatHistoryFragment.errorText.setText("连接不到聊天服务器");
				else
					chatHistoryFragment.errorText.setText("当前网络不可用，请检查网络设置");
					
			}
		}

		@Override
		public void onReConnected() {
			chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onReConnecting() {
		}

		@Override
		public void onConnecting(String progress) {
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		//友盟统计开始
		MobclickAgent.onPageStart(this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1));
		MobclickAgent.onResume(this);
		if (!isConflict) {
			updateUnreadLabel();
			EMChatManager.getInstance().activityResumed();
		}
		if(getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow){
			System.out.println("环信:showConflictDialog"+this);
			showConflictDialog();
		}
		isMoney();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//界面隐藏，调用友盟的接口
		MobclickAgent.onPageEnd(this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1));
		MobclickAgent.onPause(this);
	}



	private android.app.AlertDialog.Builder conflictBuilder;
	private boolean isConflictDialogShow;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		CCApplication.app.logout();
		CCApplication.app.finishAllActivity();
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("conflict", true);
		startActivity(intent);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
			System.out.println("环信:showConflictDialog"+intent);
			showConflictDialog();
		}
	}
	@Override
	public void finish() {
		super.finish();
		CCApplication.app.removeActivity(this);
	}
	/**
	 * 获取系统本地版本号.
	 * @return 系统本地版本号
	 */
	public String getCurVersion(){ 
		Properties properties = new Properties(); 
		
		try { 
			InputStream stream = this.getAssets().open("ver.cfg"); 
			properties.load(stream); 
		} catch (FileNotFoundException e){ 
			return "100"; 
		} catch(IOException e) { 
			return "100"; 
		} catch(Exception e){ 
			return "100"; 
		} 
		
		String version = String.valueOf(properties.get("Version").toString()); 
		
		return version; 
	} 
	/**
	 * 判断学生是否该缴费
	 */
	public void isMoney(){
		SharedPreferences sp= getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
		String childID = sp.getString("childID", "");
		if ("0".equals(childID)){
			return;
		}
		String result = null;
		String falg = null;
		JSONObject json = new JSONObject();
		try {
			json.put("studentId", childID);
			String url = new StringBuilder(Constants.SERVER_URL).append(
					"/setMealRenewService/isService").toString();
			result = HttpHelper.httpPostJson(url, json);
			JSONObject obj = new JSONObject(result);
			falg = obj.getString("falg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//1 有钱 ， 0没钱了
		if ("0".equals(falg)) {
//			Bundle bundle1 = new Bundle();
//			String url = new StringBuilder(Constants.WEBVIEW_URL)
//					.append("/mobileframe.do?method=loginsucceed_m&")
//					.append("userid").append("=").append(getUserID())
//					.append("&").append("childid").append("=")
//					.append(getChildID())
//					.append("&version=")
//					.append(getCurVersion())
//					.toString();
//			bundle1.putString("url", url);
//			newActivity(HomeWebviewActivity.class, bundle1);
//			MainActivity.this.finish();
			goHome();
		}
	}
}
