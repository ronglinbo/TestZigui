package com.wcyc.zigui2.aliChat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageOperateion;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactProfileCallback;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.contact.YWContactFactory;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.fundamental.widget.WxAlertDialog;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.fragment.ContactFragment;
import com.wcyc.zigui2.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;


public class ChattingOperationCustomSample extends IMChattingPageOperateion {

    public ChattingOperationCustomSample(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 是否需要隐藏头像
     *
     * @param viewType 自定义view类型
     * @return true: 隐藏头像  false：不隐藏头像
     */
    @Override
    public boolean needHideHead(int viewType) {
        return false;
    }

    /**
     * 是否需要隐藏显示名
     *
     * @param viewType 自定义view类型
     * @return true: 隐藏显示名  false：不隐藏显示名
     */
    @Override
    public boolean needHideName(int viewType) {
        return false;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data, List<YWMessage> messageList) {
        return super.onActivityResult(requestCode, resultCode, data, messageList);
    }

    /**
     * 开发者可以根据用户操作设置该值
     */
    private static boolean mUserInCallMode = false;


    /**
     * 长按消息效果
     *
     * @param fragment
     * @param message
     * @return
     */
    @Override
    public boolean onMessageLongClick(Fragment fragment, final YWMessage message) {
        final Activity context = fragment.getActivity();
        if (message != null) {
            final List<String> linkedList = new ArrayList<String>();
            if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TEXT
                    || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO
                    ) {
                linkedList.add("转发");
            }
            linkedList.add("删除");

            if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TEXT) {
                linkedList.add("复制");
            }

            if ((message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_IMAGE || message
                    .getSubType() == YWMessage.SUB_MSG_TYPE.IM_GIF || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_VIDEO)) {
                String content = message.getMessageBody().getContent();
                if (!TextUtils.isEmpty(content) && content.startsWith("http")) {
                    linkedList.add(0, "转发");
                }
            }
            if ((message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS || message
                    .getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS)) {

                if (message.getMessageBody() != null && message.getMessageBody().getSummary() != null && message.getMessageBody().getSummary().equals("阅后即焚")) {

                } else {
                    linkedList.add(0, "转发");
                }
            }
            if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_AUDIO) {
                String text;
                if (mUserInCallMode) { //当前为听筒模式
                    text = "使用扬声器模式";
                } else { //当前为扬声器模式
                    text = "使用听筒模式";
                }
                linkedList.add(text);
            }

            final String[] strs = new String[linkedList.size()];
            linkedList.toArray(strs);

            final YWConversation conversation = CCApplication.getInstance().getIMKit().getConversationService().getConversationByConversationId(message.getConversationId());

            new WxAlertDialog.Builder(context)
                    .setTitle(getShowName(conversation))
                    .setItems(strs, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            if (which < strs.length) {
                                if ("转发".equals(strs[which])) {
                                    showForwardMessageDialog(context, message);
                                } else if ("删除"
                                        .equals(strs[which])) {
                                    if (conversation != null) {
                                        conversation.getMessageLoader().deleteMessage(message);
                                    } else {
                                        IMNotificationUtils.getInstance().showToast(context, "删除失败，请稍后重试");
                                    }
                                } else if ("复制"
                                        .equals(strs[which])) {
                                    ClipboardManager clip = (ClipboardManager) context
                                            .getSystemService(Context.CLIPBOARD_SERVICE);
//										String content = message.getContent();
                                    String content = message.getMessageBody().getContent();
                                    if (TextUtils.isEmpty(content)) {
                                        return;
                                    }

                                    try {
                                        clip.setText(content);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    } catch (IllegalStateException e) {
                                        e.printStackTrace();
                                    }
                                } else if ("使用扬声器模式"
                                        .equals(strs[which]) || "使用听筒模式"
                                        .equals(strs[which])) {

                                    if (mUserInCallMode) {
                                        mUserInCallMode = false;
                                    } else {
                                        mUserInCallMode = true;
                                    }
                                }
                            }
                        }
                    })
                    .setNegativeButton(
                            context.getResources().getString(
                                    R.string.cancel),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            }).create().show();
        }

        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {
            IMNotificationUtils.getInstance().showToast(context, "你长按了地理位置消息");
            return true;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TEXT
                || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GIF
                || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_IMAGE
                || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_VIDEO
                || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_AUDIO) {
            return true;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS) {
            IMNotificationUtils.getInstance().showToast(context, "你长按了自定义消息");
            return true;
        }
        return false;
    }

    public String getShowName(YWConversation conversation) {
        String conversationName;
        //added by 照虚  2015-3-26,有点无奈
        if (conversation == null) {
            return "";
        }

        if (conversation.getConversationType() == YWConversationType.Tribe) {
            conversationName = ((YWTribeConversationBody) conversation.getConversationBody()).getTribe().getTribeName();
        } else {
            IYWContact contact = ((YWP2PConversationBody) conversation.getConversationBody()).getContact();
            String userId = contact.getUserId();
            String appkey = contact.getAppKey();
            conversationName = userId;
            IYWCrossContactProfileCallback callback = CCApplication.getInstance().getIMKit().getCrossProfileCallback();
            if (callback != null) {
                IYWContact iContact = callback.onFetchContactInfo(userId, appkey);
                if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                    conversationName = iContact.getShowName();
                    return conversationName;
                }
            } else {
                IYWContactProfileCallback profileCallback = CCApplication.getInstance().getIMKit().getProfileCallback();
                if (profileCallback != null) {
                    IYWContact iContact = profileCallback.onFetchContactInfo(userId);
                    if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                        conversationName = iContact.getShowName();
                        return conversationName;
                    }
                }
            }
            IYWContact iContact = CCApplication.getInstance().getIMKit().getIMCore().getContactManager().getWXIMContact(userId);
            if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                conversationName = iContact.getShowName();
            }
        }
        return conversationName;
    }


    /**
     * 转发消息
     *
     * @param context
     * @param msg
     */
    private void showForwardMessageDialog(final Activity context, final YWMessage msg) {
        Intent intent = new Intent();
        intent.putExtra("msg", msg);
        intent.setClass(context, ContactActivity.class);
        context.startActivity(intent);
    }
}
