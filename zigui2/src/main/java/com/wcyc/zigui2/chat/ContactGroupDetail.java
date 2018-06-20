/*
* 文 件 名:ContactDetail.java
* 创 建 人： 姜韵雯
* 日    期： 2014-10-13 
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.widget.RoundImageView;



/**
 * 群聊详情类.
 *
 * @version 1.01
 * @since 1.01
 */
public class ContactGroupDetail extends BaseActivity implements OnClickListener {


    private LinearLayout title_back;
    private TextView tv_group_id;
    private TextView tv_group_name;
    private RoundImageView avatar;
    private Button bt_groupChat;
    private Button bt_show_member;
    private String groupId;
    private String groupName;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_group_detail);
        initView();
        initData();
    }

    /**
     * 初始化视图.
     */
    private void initView() {
        // TODO Auto-generated method stub
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        tv_group_id = (TextView) findViewById(R.id.tv_group_id);
        title_back = (LinearLayout) findViewById(R.id.title_back);
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);
        avatar = (RoundImageView) findViewById(R.id.avatar);

        bt_groupChat = (Button) findViewById(R.id.bt_groupChat);
        bt_groupChat.setText("发起群聊");
        bt_groupChat.setOnClickListener(this);

        bt_show_member = (Button) findViewById(R.id.bt_show_member);
        bt_show_member.setOnClickListener(this);
        bt_show_member.setText("群成员");
        bt_show_member.setVisibility(View.GONE);

        title = (TextView) findViewById(R.id.new_content);
        tv_group_id.setVisibility(View.GONE);

    }

    /**
     * 初始化数据.
     */
    private void initData() {
        Intent intent = getIntent();

        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");

        if (DataUtil.isNullorEmpty(groupId) || DataUtil.isNullorEmpty(groupName)) {
            DataUtil.getToast("获取群信息失败,请与客服联系");
            finish();
        } else {
            tv_group_id.setText("群号:"+groupId);
            tv_group_name.setText(groupName);
            title.setText(groupName);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.bt_groupChat:
                groupChat();
                break;

            case R.id.bt_show_member:
                DataUtil.getToast("查看群成员");
                break;
            default:
                break;
        }
    }

    private void groupChat() {
        if (!DataUtil.isNullorEmpty(groupId)) {
            try {
                Long tribeID = Long.parseLong(groupId);
                YWIMKit imKit = CCApplication.getInstance().getIMKit();
                Intent intent = imKit.getTribeChattingActivityIntent(tribeID);
                startActivity(intent);
            } catch (Exception e) {
                DataUtil.getToast("发起群聊失败,请与管理员联系");
            }
        } else {
            DataUtil.getToast("发起群聊失败,请与管理员联系");
        }
    }


    @Override
    protected void getMessage(String data) {
        // TODO Auto-generated method stub

    }
}
