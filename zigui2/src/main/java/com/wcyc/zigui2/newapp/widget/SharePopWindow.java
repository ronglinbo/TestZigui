/*
 * 文 件 名:QuickServicePublish.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-1
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.widget;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.ShareModel;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.PhotoBitmapUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * 分享的PW
 */
public class SharePopWindow extends PopupWindow {
    private Button cancel;
    private View view;
    private LinearLayout ll_weChat, ll_friend, ll_qq, ll_zone;
    private Activity context;
    private ShareModel model;

    public SharePopWindow(Activity context, ShareModel model) {
        super(context);
        this.context = context;
        this.model = model;
        init(context);
        initEvent();
    }


    private void init(Activity context) {
        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.pop_window_share, null);
        cancel = (Button) view.findViewById(R.id.cancel);
        ll_weChat = (LinearLayout) view.findViewById(R.id.ll_wechat);
        ll_friend = (LinearLayout) view.findViewById(R.id.ll_friend);
        ll_qq = (LinearLayout) view.findViewById(R.id.ll_qq);
        ll_zone = (LinearLayout) view.findViewById(R.id.ll_zone);


        this.setContentView(view);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
    }

    private void initEvent() {
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();
            }

        });

        ll_weChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare(Wechat.NAME);
                dismiss();
            }
        });

        ll_friend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare(WechatMoments.NAME);
                dismiss();
            }
        });


        ll_qq.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare(QQ.NAME);
                dismiss();
            }
        });


        ll_zone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare(QZone.NAME);
                dismiss();
            }
        });

    }


    private void showShare(String platform) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(model.getTitle());


        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(model.getUrl());


        // text是分享文本，所有平台都需要这个字段
        oks.setText(model.getText());

        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(model.getImageUrl());

//         imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(model.getImageLocal());//确保SDcard下面存在此张图片

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(model.getUrl());


//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("ShareSDK");

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(model.getUrl());


       //监听分享
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                DataUtil.getToast("分享完成");
                //删除本地分享图片缓存
                PhotoBitmapUtils.deleteTempAlum();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
                DataUtil.getToast("分享失败");
                PhotoBitmapUtils.deleteTempAlum();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                DataUtil.getToast("分享取消");
                PhotoBitmapUtils.deleteTempAlum();
            }
        });

        //启动分享
        oks.show(context);
    }

}