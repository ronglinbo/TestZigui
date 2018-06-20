package com.wcyc.zigui2.aliChat;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListOperation;
import com.alibaba.mobileim.channel.util.AccountUtils;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWCustomConversationBody;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.utils.DataUtil;

/**
 * @author zzc
 * @time 2018/3/28
 */
public class ConversationListOperationCustomSample extends IMConversationListOperation {
    public ConversationListOperationCustomSample(Pointcut pointcut) {
        super(pointcut);
    }


    /**
     * 定制会话点击效果，该方法可以定制所有的会话类型，包括单聊、群聊、自定义会话
     *
     * @param fragment     会话列表fragment
     * @param conversation 当前点击的会话对象
     * @return true: 使用用户自定义的点击效果  false：使用SDK默认的点击效果
     */
    @Override
    public boolean onItemClick(Fragment fragment, YWConversation conversation) {
//        YWConversationType type = conversation.getConversationType();
//        if (type == YWConversationType.P2P){
//            //TODO 单聊会话点击效果
//            return true;
//        } else if (type == YWConversationType.Tribe){
//            //TODO 群会话点击效果
//            return true;
//        } else if (type == YWConversationType.Custom){
//            //TODO 自定义会话点击效果
//            return true;
//        }


        if (conversation.getConversationType() == YWConversationType.Custom) {
            YWCustomConversationBody body = (YWCustomConversationBody) conversation.getConversationBody();
            String conversationId = body.getIdentity();
            if (conversationId.startsWith("sysTribe")) {
                Intent intent = new Intent(CCApplication.getInstance(), TribeSystemMessageActivity.class);
                fragment.getActivity().startActivity(intent);
                return true;
            }
        }
        return false;
    }

}
