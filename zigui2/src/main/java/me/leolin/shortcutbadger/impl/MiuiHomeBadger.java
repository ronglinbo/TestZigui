/*
* 文 件 名:MiuiHomeBadger.java
* 创 建 人： 姜韵雯
* 日    期： 2015-03-09
* 版 本 号： 1.05
*/
package me.leolin.shortcutbadger.impl;

import java.lang.reflect.Field;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * 小米手机通知未读消息数
 * 
 * @author 姜韵雯
 * @version 1.14
 * @since 1.14
 */
public class MiuiHomeBadger extends ShortcutBadger {

    private static final String INTENT_ACTION = "android.intent.action.APPLICATION_MESSAGE_UPDATE";
    private static final String UPDATE_APPLICATION_MESSAGE_TEXT = "update_application_message_text";
    private static final String INTENT_EXTRA_COMPONENTNAME = "android.intent.extra.update_application_component_name";

    public MiuiHomeBadger(Context context) {
        super(context);
    }

    @Override
    protected void executeBadge(int badgeCount) {
//        Intent intent = new Intent(INTENT_ACTION);
//        intent.putExtra(UPDATE_APPLICATION_MESSAGE_TEXT, String.valueOf(badgeCount));
////        intent.putExtra(INTENT_EXTRA_COMPONENTNAME, getContextPackageName());
//        包名+activity类名
//        intent.putExtra(INTENT_EXTRA_COMPONENTNAME, getEntryActivityName());
////        intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, getEntryActivityName());
//        mContext.sendBroadcast(intent);
        sendToXiaoMi(mContext,badgeCount);
//        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = null;
//        boolean isMiUIV6 = true;
//        try {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
//            builder.setContentTitle("您有"+badgeCount+"未读消息");
//            builder.setTicker("您有"+badgeCount+"未读消息");
//            builder.setAutoCancel(true);
////            builder.setSmallIcon(R.drawable.common_icon_lamp_light_red);
//            builder.setDefaults(Notification.DEFAULT_LIGHTS);
//            notification = builder.build();
//            System.out.println("badgeCount:"+badgeCount);
//            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
//            Object miuiNotification = miuiNotificationClass.newInstance();
//            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
//            field.setAccessible(true);
//            field.set(miuiNotification, String.valueOf(badgeCount == 0 ? "" : badgeCount) );// 设置信息数
//            field = notification.getClass().getField("extraNotification");
//            field.setAccessible(true);
//            field.set(notification, miuiNotification);
////        Toast.makeText(this, "Xiaomi=>isSendOk=>1", Toast.LENGTH_LONG).show();
//        }catch (Exception e) {
//            e.printStackTrace();
//            //miui 6之前的版本
//            isMiUIV6 = false;
//            Intent intent = new Intent(INTENT_ACTION);
//            intent.putExtra(UPDATE_APPLICATION_MESSAGE_TEXT, String.valueOf(badgeCount));
////            intent.putExtra(INTENT_EXTRA_COMPONENTNAME, getContextPackageName());
//            //包名+activity类名
//            intent.putExtra(INTENT_EXTRA_COMPONENTNAME, getEntryActivityName());
////            intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, getEntryActivityName());
//            mContext.sendBroadcast(intent);
//        }
//        finally
//        {
//          if(notification!=null && isMiUIV6 )
//           {
//               //miui6以上版本需要使用通知发送
//            nm.notify(101010, notification);
//           }
//        }
    }

    /**
     * 向小米手机发送未读消息数广播
     * @param count
     */
    private void sendToXiaoMi(Context context, int count) {
        try {
            System.out.println("sendToXiaoMi");
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, String.valueOf(count == 0 ? "" : count));  // 设置信息数-->这种发送必须是miui 6才行
        } catch (Exception e) {
            e.printStackTrace();
            // miui 6之前的版本
            Intent localIntent = new Intent(
                    "android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra(
                    "android.intent.extra.update_application_component_name",
                    context.getPackageName() + "/" + getLauncherClassName(context));
            localIntent.putExtra(
                    "android.intent.extra.update_application_message_text", String.valueOf(count == 0 ? "" : count));
            context.sendBroadcast(localIntent);
        }
    }
    private String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }

}
