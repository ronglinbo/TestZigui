package com.wcyc.zigui2.newapp.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMMessage;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chat.PreferenceUtils;
import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.bean.NewPushMsg;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import static android.os.Build.VERSION.SDK;
import static android.os.Build.VERSION.SDK_INT;

public class CMDMessageBroadcastReceiver extends BroadcastReceiver {
    private Context mContext;
    public CMDMessageBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        mContext = context;
        System.out.println("CMDMessageBroadcastReceiver:"+intent);
        boolean isPush = PreferenceUtils.getInstance(context).getSettingMsgNotification();
        if(isPush) {
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            //获取消息body
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String action = cmdMsgBody.action;//获取自定义action
            parseMessage(action);
        }
        // 注销广播
//        abortBroadcast();
    }

    private void parseMessage(String data){
        if (data == null)
            return;

        System.out.println("ret is: " + data);
        NewPushMsg msg = JsonUtils.fromJson(data, NewPushMsg.class);
        if(msg == null) return;

        Intent broadcast = new Intent(
                HomeActivity.INTENT_NEW_MESSAGE);
        mContext.sendBroadcast(broadcast);
        boolean isTop = DataUtil.isTopActivity(mContext);
        if(isTop == true){
            return;
        }

        String messageType = msg.getMessageType();
        msg.setMessageTypeName(getMsgTypeName(messageType));
        //合并消息（22，24，25）为业务办理
        if (messageType.equals("22")
                || messageType.equals("24")
                || messageType.equals("25")) {
            msg.setMessageTypeName("业务办理");
        } else if (messageType.equals("23")) {
            msg.setMessageTypeName("请假条");
        }
        Intent intent = new Intent();
        intent.setClass(mContext, HomeActivity.class);
        //跳到首页
        intent.putExtra("firstPage", true);
        notification(msg, intent);
    }

    private void notification(NewPushMsg msg, Intent intent) {
        // 获取系统的通知管理器
        String type = msg.getMessageTypeName();
        String content = msg.getContent();

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                mContext);
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
                    mContext, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
        }
        String id = msg.getId();
        mNotificationManager.notify(Integer.parseInt(id), builder.build());

    }

    private String getMsgTypeName(String type){
        if(type == null) return null;
        for(String[] item: Constants.MsgType){
            if(type.equals(item[0])){
                return item[1];
            }
        }
        return "";
    }
}
