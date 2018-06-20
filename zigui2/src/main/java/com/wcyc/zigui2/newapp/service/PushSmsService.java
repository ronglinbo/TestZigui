package com.wcyc.zigui2.newapp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogRecord;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.easemob.chat.EMChatManager;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.chat.PreferenceUtils;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.ModelRemindList;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.bean.NewMessageListBean;
import com.wcyc.zigui2.newapp.bean.NewVersionCheckModel;
import com.wcyc.zigui2.newapp.bean.PushMsg;
import com.wcyc.zigui2.newapp.bean.PushMsg.Message;

import com.wcyc.zigui2.newapp.bean.UserType;

import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

/**
 * 
 * 短信推送服务类，在后台长期运行，每个一段时间就向服务器发送一次请求
 * 
 * 
 */
public class PushSmsService extends Service
		implements HttpRequestAsyncTaskListener {
	private ServiceThread serviceThread;
	private NotificationManager manager;
	private Notification notification;
	private PendingIntent pi;

	private boolean flag = true;

	protected boolean isLoading = false;

	private List<Message> msgs;
	protected final int RETURN_DATA = 0;
	protected final int RETURN_CODE = 1;
	private boolean isUpdateMark = false;

	@Override
	public void onCreate() {
		System.out.println("oncreate()");
		CCApplication.getInstance().initReceiver();
		this.serviceThread = new ServiceThread();
		this.serviceThread.start();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent,int flags,int startId){
		flags = START_FLAG_RETRY;
		return  super.onStartCommand(intent,flags,startId);
	}

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg){
			super.handleMessage(msg);
			switch (msg.what){
				case 1:
					break;
			}
		}
	};
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(1);
		}
	};

	@Override
	public void onDestroy() {
		this.flag = false;
		super.onDestroy();
	}

	private void notification(Message msg,Intent intent) {
		// 获取系统的通知管理器
		String type = msg.getMessageTypeName();
		String content = msg.getMessageContent();
		
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				getApplicationContext());
		//当字数太多而notification显示不完全的bug修改
		NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
		inboxStyle.bigText(content);
		builder.setStyle(inboxStyle);
//		builder.setSmallIcon(setIconByType(msg.getMessageType()));
		builder.setSmallIcon(R.drawable.app_icon);
		builder.setDefaults(Notification.DEFAULT_ALL);
		builder.setAutoCancel(true);
		builder.setContentTitle("云智全课通");
		builder.setTicker(type + content);
		builder.setContentInfo(type);
		builder.setSubText("");
		builder.setPriority(10);
		if (intent != null) {
			PendingIntent pendingIntent = PendingIntent.getActivity(
					getApplicationContext(), 0, intent, 0);
			builder.setContentIntent(pendingIntent);
		}
		String id = msg.getMessageId();
		
		mNotificationManager.notify(Integer.parseInt(id), builder.build());
	}
	
	private int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		NewMessageListBean AllMessageList = CCApplication.getInstance().getMessageList();
		if(AllMessageList != null){
			List <NewMessageBean> messageList = AllMessageList.getMessageList();
			if(messageList != null){
				for(NewMessageBean message:messageList){
					if("chat".equals(message.getMessageType())){
						String count = message.getCount();
						int num = 0;
						if(count != null){
							num = Integer.parseInt(count);
						}
						unreadMsgCountTotal += num;
					}
				}
			}
		}
		int newMessageCount = getAllModelRemind();
		unreadMsgCountTotal += newMessageCount;
		return unreadMsgCountTotal;
	}

	private int getAllModelRemind(){
		int count = 0;
		ModelRemindList remind = CCApplication.getInstance().getModelRemindList();
		if(remind != null){
			List <ModelRemindList.ModelRemind> list = remind.getMessageList();
			if(list != null){
				for(ModelRemindList.ModelRemind item :list){
//					for(String type:Constants.func){
					if(!Constants.hasEmailFunc){
						if(Constants.EMAIL.equals(item.getType())){
							continue;
						}
					}
					//去掉班级动态
					if(!Constants.CLASSDYN.equals(item.getType())){
						String temp = item.getCount();
						count += Integer.parseInt(temp);
					}
//					}
				}
			}
		}
		return count;
	}

	private void parseModelRemind(String result){
		ModelRemindList remind = JsonUtils.fromJson(result, ModelRemindList.class);
		if(remind == null) return;
		if(remind.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
			CCApplication.getInstance().setModelRemindList(remind);
		}
		int unreadNum = getUnreadMsgCountTotal();
		if(unreadNum > 99)unreadNum = 99;
		try {
			ShortcutBadger.setBadge(this, unreadNum);
		} catch (ShortcutBadgeException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onRequstComplete(String result) {
		parseModelRemind(result);
	}

	@Override
	public void onRequstCancelled() {

	}

	private class ServiceThread extends Thread {
		@Override
		public void run() {
			while (flag) {
				try {
					synchronized (this) {
//						Context context = CCApplication.getInstance().getApplicationContext();
//						boolean isPush = PreferenceUtils.getInstance(context).getSettingMsgNotification();
//
//						if(isPush) {
//							boolean ret = getPushMessage();
//							if (!ret) continue;
//						}
						getModelRemind();
						getUpdateVer();
					}

					Thread.sleep(Constants.MSG_PUSHTIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean getPushMessage(){
		// 调用接口
		String userID = null;
		UserType user = CCApplication.getInstance().getPresentUser();
		if (user != null) {
			userID = user.getUserId();
		}
		if (userID == null || userID.equals("")) {
			return false;
		}
		String retjson = null;
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userID);
			if (user != null) {
				String type = user.getUserType();
				json.put("userType", type);
				if (type.equals(Constants.PARENT_STR_TYPE)) {
					json.put("childId", user.getChildId());
				}
			}
			json.put("isPushMessage", "1");//1:推送消息，0：非推送消息
			try {
				retjson = HttpHelper.httpPostJson(CCApplication.applicationContext, Constants.MSG_URL,
						json);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		parseMessage(retjson);
		return true;
	}

	private void getModelRemind(){
		JSONObject json = new JSONObject();
		UserType user = CCApplication.getInstance().getPresentUser();
		if(user != null){
			try {
				json.put("userId", user.getUserId());
				json.put("userType",user.getUserType());
				if(CCApplication.getInstance().isCurUserParent()){
					json.put("studentId", user.getChildId());
				}
				json.put("schoolId",user.getSchoolId());
				System.out.println("后台获取服务未读消息数:"+json);
				try {
					String	result = HttpHelper.httpPostJson(CCApplication.applicationContext,
							Constants.BASE_URL+Constants.GET_MODEL_REMIND,
                            json);
					parseModelRemind(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			new HttpRequestAsyncTask(json, this, this).execute(Constants.GET_MODEL_REMIND);
		}
	}

	protected void parseMessage(String data) {
		if (data == null)
			return;

		System.out.println("PushSmsService message: " + data);
		PushMsg ret = JsonUtils.fromJson(data, PushMsg.class);
		if(ret == null) return;
		if (ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
			// 失败
		} else {
			msgs = ret.getMessageList();
			boolean isTop = isTopActivity();
			if(isTop == true){
				if (msgs != null && msgs.size() > 0) {
					Intent broadcast = new Intent(
							HomeActivity.INTENT_NEW_MESSAGE);
					sendBroadcast(broadcast);
				}
				return;
			}

			Message msg;
			if (msgs!=null &&msgs.size() > 0) {
				for(Message item:msgs) {
					msg = item;
					//合并消息（22，24，25）为业务办理
					String messageType = item.getMessageType();
					if (messageType.equals("22")
							|| messageType.equals("24")
							|| messageType.equals("25")) {
						item.setMessageTypeName("业务办理");
					} else if (messageType.equals("23")) {
						item.setMessageTypeName("请假条");
					}
					if (!Constants.hasEmailFunc){
						if(messageType.equals(Constants.EMAIL)){
							continue;
						}
					}
					String msgType = msg.getMessageType();
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), HomeActivity.class);
					//跳到首页
					intent.putExtra("firstPage", true);
					if (msgType.equals("4")) {
						// 强制更新
						NewMemberBean member = CCApplication.app.getMemberInfo();
						member.setUpdateFlag("1");
						CCApplication.app.setMember(member);
						isUpdateMark = true;
					}
					if (msgType.equals("5")) {
						if(!isUpdateMark){   //如果是强制更新一定是先优先更新，而不是查看缴费通知
							// 缴费通知
						}
					}else {
						// 如果消息类型是1234都跳转到登陆界面
						System.out.println("msgType:"+msgType);
					}
					notification(msg, intent);
				}
			}
		}
	}

	private void getUpdateVer(){
		if(!isTopActivity()) return;
		CCApplication app = CCApplication.getInstance();
		checkVersionUpdate(app.getDeviceId(),"android",app.getCurVersion());
	}

	/**
	 * 更新版本.
	 * @param deviceID 硬件ID
	 * @param mobileType 系统版本：“android”
	 * @param version 版本:1.0
	 */
	private void checkVersionUpdate(String deviceID,String mobileType,String version) {
		JSONObject json = new JSONObject();
		try {
			json.put("deviceId", deviceID);
			json.put("versionType", mobileType);
			json.put("versionNumber", version);
			System.out.println("获取版本更新json:"+json);
			String ret = HttpHelper.httpPostJson(CCApplication.applicationContext,
					Constants.BASE_URL+Constants.VERSION_CHECK, json);
			parseUpdateData(ret);
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
//		new HttpRequestAsyncTask(json, this, this).execute(Constants.VERSION_CHECK);
	}

	/**
	 * 处理系统更新返回数据.
	 * @param data 系统更新返回的数据
	 */
	private void parseUpdateData(String data) {
		System.out.println("获取版本更新数据: "+ data);
		NewVersionCheckModel ret;
		ret = JsonUtils.fromJson(data, NewVersionCheckModel.class);
		if(ret != null && ret.getServerResult() != null) {
			if (ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
				DataUtil.getToast(ret.getServerResult().getResultMessage());
			} else { //有新版本
				if ("1".equals(ret.getIsNeedUpdate())) {
					Intent intent = new Intent(BaseActivity.UPDATE_VERSION_SUCCESS);
					Bundle bundle = new Bundle();
					bundle.putSerializable("updateVersion", ret);
					intent.putExtras(bundle);
					sendBroadcast(intent);
				} else {//无新版本
					NewMemberBean member = CCApplication.app.getMemberInfo();
					if (member != null) {
						member.setUpdateFlag("0");
						CCApplication.app.setMember(member);
					}
				}
			}
		}
	}

	private boolean isTopActivity(){  
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<RunningTaskInfo>  tasksInfo = am.getRunningTasks(1);  
        if(tasksInfo.size() > 0){  
            //应用程序位于堆栈的顶层  
            if("com.wcyc.zigui2".equals(tasksInfo.get(0).topActivity.getPackageName())){  
                return true;  
            }  
        }  
        return false;  
    }  
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}