package com.wcyc.zigui2.core;

/**
 * Created by 章豪 on 2017/6/26.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wcyc.zigui2.newapp.module.studyresource.ZiguiCourseActivity;

public class NoAdWebViewClient extends WebViewClient {
    private String homeurl;
    private Context context;
    private ZiguiCourseActivity ziguiCourseActivity;

    public NoAdWebViewClient(Context context, String homeurl) {
        this.context = context;
        this.homeurl = homeurl;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {


        // ------  对alipays:相关的scheme处理 -------
        if (url.startsWith("alipays:") || url.startsWith("alipay")) {
            try {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            } catch (Exception e) {
//                showDialog();
            }
            return true;
        }

        // ------- 处理结束 -------
        if (!(url.startsWith("http") || url.startsWith("https"))) {
            return true;
        }

        view.loadUrl(url);
        return true;
    }

    public void showDialog() {
        new AlertDialog.Builder(context)
                .setMessage("未检测到支付宝客户端，请安装后重试。")
                .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri alipayUrl = Uri.parse("https://d.alipay.com");
                        context.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d("WebView", "onPageStarted");

        super.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
        Log.d("WebView", "onPageFinished ");

        //   view.loadUrl("javascript:function setTop(){document.querySelector('#EG0phD8f').style.display=\"none\";}setTop();");
        super.onPageFinished(view, url);

    }


    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        url = url.toLowerCase();
        Log.e("testurl", url);
        if (!url.contains(homeurl)) {
            if (!ADFilterTool.hasAd(CCApplication.applicationContext, url)) {

                return super.shouldInterceptRequest(view, url);
            } else {
                Log.e("屏蔽的url", url);
                return new WebResourceResponse(null, null, null);
            }
        } else {

            return super.shouldInterceptRequest(view, url);
        }


    }
}
