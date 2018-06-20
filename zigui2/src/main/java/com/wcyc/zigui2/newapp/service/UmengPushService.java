package com.wcyc.zigui2.newapp.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.activity.LoginActivity;
import com.wcyc.zigui2.newapp.bean.ModelRemindList;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.bean.NewMessageListBean;
import com.wcyc.zigui2.newapp.bean.NewPushMsg;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargePriceActivity;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargeProductActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * Created by xiehua on 2017/3/20.
 */

public class UmengPushService extends UmengMessageService {
    @Override
    public void onMessage(Context context, Intent intent) {
        try {
            //可以通过MESSAGE_BODY取得消息体
            String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            System.out.println("友盟message=" + message);    //消息体
            System.out.println("custom=" + msg.custom);    //自定义消息的内容
            System.out.println("title=" + msg.title);    //通知标题
            System.out.println("text=" + msg.text);    //通知内容
            // code  to handle message here
            // ...
            parseMessage(context, msg.custom);
            //完全自定义消息的点击统计
            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseMessage(Context context, String data) {
        if (data == null)
            return;
        System.out.println("ret is: " + data);

        NewPushMsg msg = JsonUtils.fromJson(data, NewPushMsg.class);
        if (msg == null) return;
        String messageType = msg.getMessageType();
        Log.i("TAG", "友盟message messageType:" + messageType);
        if (messageType.equals("0")) {
//            logout(context);
            return;
        }else{
            String messageName = getMsgTypeName(messageType);
            if (!DataUtil.isFunctionEnable(messageName))
                return;
            Intent broadcast = new Intent(
                    HomeActivity.INTENT_NEW_MESSAGE);
            context.sendBroadcast(broadcast);

            boolean isTop = DataUtil.isTopActivity(context);
            if (isTop == true) {
                return;
            }
            msg.setMessageTypeName(messageName);
            //合并消息（22，24，25）为业务办理
            if (messageType.equals("22")
                    || messageType.equals("24")
                    || messageType.equals("25")) {
                msg.setMessageTypeName("业务办理");
            } else if (messageType.equals("23")) {
                msg.setMessageTypeName("请假条");
            }

            Intent intent = new Intent();
            intent.setClass(context, HomeActivity.class);
            //跳到首页
            intent.putExtra("firstPage", true);
            notification(context, msg, intent);
            getModelRemind();
        }

//        showNotification(msg);
    }


    /**
     * 强制退出操作
     *
     * @param context
     */
    private void logout(Context context) {

        //避免重复启动 LoginActivity
        for (int i = 0; i < CCApplication.activityList.size(); i++) {
            if (CCApplication.activityList.get(i) instanceof LoginActivity) {
                return;
            }
        }

        CCApplication.app.singleLogout();
        ChooseTeacherActivity.teacherSelectInfo = null;// 初始化部门信息
        CCApplication.app.finishAllActivity();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("conflict", true);
        context.startActivity(intent);
    }


    private void notification(Context context, NewPushMsg msg, Intent intent) {
        // 获取系统的通知管理器
        String type = msg.getMessageTypeName();
        String content = msg.getContent();

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        //当字数太多而notification显示不完全的bug修改
//        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle();
//        inboxStyle.bigText(content);
//        builder.setStyle(inboxStyle);
//		builder.setSmallIcon(setIconByType(msg.getMessageType()));
        builder.setSmallIcon(R.drawable.ic_quanketong_small);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);
        builder.setContentTitle(type);
        builder.setTicker(type + content);
//        builder.setContentInfo(content);
        builder.setContentText(content);

        builder.setPriority(10);
        if (intent != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
        }
        String id = msg.getId();
        mNotificationManager.notify(Integer.parseInt(id), builder.build());
    }

    private void showNotification(NewPushMsg msg) {
        int id = new Random(System.nanoTime()).nextInt();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle(msg.getMessageTypeName())
                .setContentText(msg.getContent())
                .setTicker("")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true);
        Notification notification = mBuilder.getNotification();
//        PendingIntent clickPendingIntent = getClickPendingIntent(this, msg);
//        PendingIntent dismissPendingIntent = getDismissPendingIntent(this, msg);
//        notification.deleteIntent = dismissPendingIntent;
//        notification.contentIntent = clickPendingIntent;
        manager.notify(id, notification);
    }

    private String getMsgTypeName(String type) {
        if (type == null) return null;
        for (String[] item : Constants.MsgType) {
            if (type.equals(item[0])) {
                return item[1];
            }
        }
        return "";
    }

    private void getModelRemind() {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            try {
                json.put("userId", user.getUserId());
                json.put("userType", user.getUserType());
                if (CCApplication.getInstance().isCurUserParent()) {
                    json.put("studentId", user.getChildId());
                }
                json.put("schoolId", user.getSchoolId());
                System.out.println("后台获取服务未读消息数:" + json);
                try {
                    String result = HttpHelper.httpPostJson(CCApplication.applicationContext,
                            Constants.BASE_URL + Constants.GET_MODEL_REMIND,
                            json);
                    parseModelRemind(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    private void parseModelRemind(String result) {
        ModelRemindList remind = JsonUtils.fromJson(result, ModelRemindList.class);
        if (remind == null) return;
        if (remind.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
            CCApplication.getInstance().setModelRemindList(remind);
        }
        int unreadNum = getUnreadMsgCountTotal();
        if (unreadNum > 99) unreadNum = 99;
        try {
            ShortcutBadger.setBadge(this, unreadNum);
        } catch (ShortcutBadgeException e) {
            e.printStackTrace();
        }
    }

    private int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        NewMessageListBean AllMessageList = CCApplication.getInstance().getMessageList();
        if (AllMessageList != null) {
            List<NewMessageBean> messageList = AllMessageList.getMessageList();
            if (messageList != null) {
                for (NewMessageBean message : messageList) {
                    if ("chat".equals(message.getMessageType())) {
                        String count = message.getCount();
                        int num = 0;
                        if (count != null) {
                            num = Integer.parseInt(count);
                        }
                        unreadMsgCountTotal += num;
                    }
                }
            }
        }
        int newMessageCount = DataUtil.getAllModelRemind();
        unreadMsgCountTotal += newMessageCount;
        return unreadMsgCountTotal;
    }
}
