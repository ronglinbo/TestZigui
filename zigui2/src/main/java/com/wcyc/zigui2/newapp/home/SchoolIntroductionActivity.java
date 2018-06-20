package com.wcyc.zigui2.newapp.home;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;

/**
 * Created by xiehua on 2017/5/22.
 * 用户服务协议界面
 */

public class SchoolIntroductionActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout title_back;
    private TextView new_content;
    private WebView service_agreement_webview;
    public String AGREEMENT_ADDRESS = "";
    public String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_agreement);
        AGREEMENT_ADDRESS=getIntent().getStringExtra("url");
        title=getIntent().getStringExtra("title");
        initView();
        initEvents();
    }

    /**
     * 设置点击效果监听器
     */
    private void initEvents() {
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);
        new_content.setText(title);
        service_agreement_webview.getSettings().setAppCacheEnabled(true);	//设置启动缓存
        service_agreement_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);	//设置缓存模式
        service_agreement_webview.getSettings().setSavePassword(false);
        service_agreement_webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        service_agreement_webview.getSettings().setJavaScriptEnabled(true);	//设置WebView属性，能够执行JavaScript脚本

        service_agreement_webview.removeJavascriptInterface("searchBoxJavaBridge_");
        service_agreement_webview.removeJavascriptInterface("accessibility");
        service_agreement_webview.removeJavascriptInterface("accessibilityTraversal");

        service_agreement_webview.setWebViewClient(new WebViewClient(){		//web 视图
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;

            }
        });
        service_agreement_webview.loadUrl(AGREEMENT_ADDRESS);	// 加载需要显示的网页
    }


    /** 初始化控件 */
    private void initView() {
        title_back = (LinearLayout) findViewById(R.id.title_back);
        new_content = (TextView) findViewById(R.id.new_content);
        service_agreement_webview = (WebView)findViewById(R.id.service_agreement_webview);
    }

    @Override
    protected void getMessage(String data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                SchoolIntroductionActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
