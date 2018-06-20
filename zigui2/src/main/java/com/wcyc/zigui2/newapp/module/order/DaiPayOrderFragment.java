package com.wcyc.zigui2.newapp.module.order;

/**
 * Created by 章豪 on 2017/6/29.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;

//import com.wcyc.zigui2.newapp.module.othernumber.NewOtherNumberActivity;


public class DaiPayOrderFragment extends Fragment implements
        OnClickListener, HttpRequestAsyncTaskListener {
    public  interface ChangeDaiPayNumber{
        void setDaiPay(int num);
    }

    public TextView new_content;
    private View layoutView;

    private String type="";

    public static Fragment newInstance(int index) {
        Fragment fragment = new com.wcyc.zigui2.newapp.fragment.NewMyFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_order, null);
        initView();
//		initDatas();//改到了只要获得焦点就执行那里
        initEvents();
        return layoutView;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        TextView textView= (TextView) layoutView.findViewById(R.id.tv_no_message);
        textView.setText(type);
    }


    /**
     * 初始化数据.
     */
    private void initDatas() {

    }

    /**
     * 效果控制.
     */
    private void initEvents() {

    }


    /**
     * 点击视图.
     *
     * @param v 视图
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    /**
     * Resume效果.
     */
    @Override
    public void onResume() {
        super.onResume();
        initDatas();
    }

    /**
     * 获取未读消息数.
     *
     * @return 未读消息数
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        return unreadMsgCountTotal;
    }



    @Override
    public void onRequstComplete(String result) {

    }

    @Override
    public void onRequstCancelled() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}

