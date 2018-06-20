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
import com.wcyc.zigui2.core.WebviewActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

/**
 * Created by xiehua on 2017/5/22.
 * 用户服务协议界面
 */

public class ServiceAgreementActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout title_back;
    private TextView new_content;
    private WebView service_agreement_webview;
    public static final String AGREEMENT_ADDRESS = "https://school.ziguiw.com/zgwt/source/serviceAgreement.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_agreement);

        initView();
        initEvents();
    }

    /**
     * 设置点击事件监听器
     */
    private void initEvents() {
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);
        new_content.setText("用户服务协议");
        service_agreement_webview.getSettings().setAppCacheEnabled(true);	//设置启动缓存
        service_agreement_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);	//设置缓存模式
        service_agreement_webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        service_agreement_webview.getSettings().setJavaScriptEnabled(true);	//设置WebView属性，能够执行JavaScript脚本
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
                ServiceAgreementActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
