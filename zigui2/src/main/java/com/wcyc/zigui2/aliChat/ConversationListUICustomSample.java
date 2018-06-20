package com.wcyc.zigui2.aliChat;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.wcyc.zigui2.R;


/**
 * 自定义会话列表
 */
public class ConversationListUICustomSample extends IMConversationListUI {
    public ConversationListUICustomSample(Pointcut pointcut) {
        super(pointcut);
    }

    @Override
    public boolean needHideTitleView(Fragment fragment) {
        return false;
    }


    //自定义
    @Override
    public View getCustomConversationTitleView(Fragment fragment, YWConversation conversation, View convertView, TextView defaultView) {
        if (conversation.getConversationType() == YWConversationType.Custom) {
            if (convertView == null) {
                convertView = new TextView(fragment.getActivity());
            }
            TextView textView = (TextView) convertView;
            textView.setText("系统消息");
            return convertView;
        }
        return super.getCustomConversationTitleView(fragment, conversation, convertView, defaultView);
    }

    /**
     * 标题栏自定义
     * @param fragment
     * @param context
     * @param inflater
     * @return
     */
    @Override
    public View getCustomConversationListTitle(final Fragment fragment, Context context, LayoutInflater inflater) {

        View view = inflater.inflate(R.layout.new_title, null, false);
        TextView title = (TextView) view.findViewById(R.id.new_content);
        title.setText("最近会话");
        LinearLayout title_back = (LinearLayout) view.findViewById(R.id.title_back);

        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭最近会话列表
               fragment.getActivity().finish();
            }
        });

        return view;
    }




}
