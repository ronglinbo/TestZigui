package com.wcyc.zigui2.newapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.newapp.module.educationinfor.EducationDetailsActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

/**
 * 系统消息详情页面
 */

public class SystemMessageDetailActivity extends BaseActivity {

    private static final int CLEAR_UNREAD = 200;

    private WebView webView_message_list;
    private String url = null;
    private JsInterface jsInterface;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message_detail);


        url = getIntent().getStringExtra("url");
        if (url == null) {
            url = "file:///android_asset/www/wrong.html";
        }
        position = getIntent().getIntExtra("position", 0);

        initView();
        initEvents();
    }

    /**
     * 设置点击事件监听器
     */
    private void initEvents() {

        WebSettings webSettings = webView_message_list.getSettings();
//        webSettings.setAppCacheEnabled(true);    //设置启动缓存
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);    //设置缓存模式

        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        webSettings.setJavaScriptEnabled(true);    //设置WebView属性，能够执行JavaScript脚本
        webView_message_list.setWebViewClient(new WebViewClient() {        //web 视图
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;

            }
        });
        webSettings.setAllowFileAccess(true);//可以读取文件缓存(manifest生效)
        webSettings.setAppCachePath(Constants.CACHE_PATH);

        jsInterface = new JsInterface();
        webView_message_list.addJavascriptInterface(jsInterface, "android");
        webView_message_list.loadUrl(url);    // 加载需要显示的网页
    }


    /**
     * 初始化控件
     */
    private void initView() {
        webView_message_list = (WebView) findViewById(R.id.webView_message_list);
    }

    @Override
    protected void getMessage(String data) {

    }

    @Override
    public void onBackPressed() {
        clearUnReadIcon();
        super.onBackPressed();
    }


    public class JsInterface {

        @JavascriptInterface
        public void back() {
            clearUnReadIcon();
        }

        @JavascriptInterface
        public void readBack() {
            showReceipt();
        }
    }

    private void showReceipt() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        SystemMessageDetailActivity.this.finish();
    }

    private void clearUnReadIcon() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        setResult(CLEAR_UNREAD, intent);
        SystemMessageDetailActivity.this.finish();
    }
}
