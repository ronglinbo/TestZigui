package com.wcyc.zigui2.aliChat;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMNotification;
import com.wcyc.zigui2.R;


/**
 * 自定义阿里百川通知栏消息
 *
 */
public class NotificationInitSampleHelper extends IMNotification {
    public NotificationInitSampleHelper(Pointcut pointcut) {
        super(pointcut);
    }

    //显示名称

    @Override
    public String getAppName() {
        return "云智全课通";
    }

    /**
     * 显示推送图标问题
     * @return
     */
    @Override
    public int getNotificationIconResID() {
        return R.drawable.ic_quanketong_small;
    }
}
