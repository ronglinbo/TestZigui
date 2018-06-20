package com.wcyc.zigui2.newapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.ADFilterTool;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.core.NoAdWebViewClient;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.consume.NewRechargeRecordActivity1;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.taobao.accs.ACCSManager.mContext;

/**
 * 缴费详情页面
 */

public class PaymentDetailActivity extends BaseActivity {

    private WebView webView_message_list;
    private String url = null;
    private JsInterface jsInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_h5_activity);


        url = getIntent().getStringExtra("url");
        if (url == null) {
            url = "file:///android_asset/www/wrong.html";
        }

        initView();
        initEvents();
    }

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
        webView_message_list.setWebViewClient(new H5PayWebViewClient(this, url));


        UserType user = CCApplication.getInstance().getPresentUser();
        HashMap<String, String> para = new HashMap<>();
        String imgAuthId = Constants.AUTHID + "@" + getDeviceID()
                + "@" + CCApplication.app.getMemberInfo().getAccId();
        para.put("X-School-Id", user.getSchoolId());
        para.put("X-mobile-Type", "android");
        para.put("imgAuthId", imgAuthId);
//        String url = Constants.URL + "/app_slideshow/homeSlideshow.do";
        webView_message_list.loadUrl(url, para);

//        webView_message_list.loadUrl(url);    // 加载需要显示的网页


    }



    /**
     * 设置Web视图.
     */
    private void setWebView() {
//		contentWebView.setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = webView_message_list.getSettings();
        // webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);//设置可以使用localStorage
        if (DataUtil.isNetworkAvailable(PaymentDetailActivity.this)) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//默认使用缓存
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//默认使用
            // 缓存
        }
        webSettings.setAppCacheMaxSize(8 * 1024 * 1024);//缓存最多可以有8M
        webSettings.setAllowFileAccess(true);//可以读取文件缓存(manifest生效)
        webSettings.setAppCacheEnabled(true);//应用可以有缓存
        webSettings.setAppCachePath(Constants.CACHE_PATH);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//		webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        //以下影响视频播放
//		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webSettings.setSavePassword(false);
        webSettings.setDefaultTextEncodingName("gb2312");
        jsInterface = new JsInterface();
        webView_message_list.addJavascriptInterface(jsInterface, "android");
        webView_message_list.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                // TODO Auto-generated method stub
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        });
        webView_message_list.setWebViewClient(new NoAdWebViewClient(this, url));
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


    public class JsInterface {


        //html5界面返回
        @JavascriptInterface
        public void back() {
            System.out.println("back");
            PaymentDetailActivity.this.finish();
        }

        //支付成功,返回原来页面 刷新数据
        @JavascriptInterface
        public void backToPaymentList() {
            goBack();
        }


        //支付成功  跳转到充值记录页面
        @JavascriptInterface
        public void backToOnlineRecordList(int status) {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("status", status);
            intent.setClass(PaymentDetailActivity.this, NewRechargeRecordActivity1.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        /**
         * 查看大图.<P>
         * 点击图片，滑动查看大图。
         *
         * @param imageUrl          本组图片路径，以逗号分隔开，http://10.0.7.60:8080/zgw/upload/picture/803b34bf-986c-4ae4-bdf5-da2252419184.jpg
         * @param onClickImageIndex 点击图片的位置，从0开始计数
         */
        @JavascriptInterface
        public void jumpSlideActicity(String imageUrl, String onClickImageIndex) {

            TelephonyManager telephonyManager =
                    (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String deviceId = telephonyManager.getDeviceId();
            if (deviceId == null) {
                // android pad
                deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            }

            String accId = CCApplication.app.getMemberInfo().getAccId();

            PictureURL pictureURL = null;
            int length = imageUrl.split(",").length;
            String[] paths = new String[length];//如果传过来的imageUrl有多个地址 则下一步分割，  如果只有一个地址，则分割就是本事
            paths = imageUrl.split(",");
            List<PictureURL> datas = new ArrayList<PictureURL>();
            for (int i = 0; i < paths.length; i++) {
//				String imageUrlStr=paths[i].substring(27);//获得单个图片地址
                String imageUrlStr = paths[i].substring(paths[i].lastIndexOf("/"));//获得单个图片地址


                //方法一
//				String url_a = DataUtil.getDownloadURL(getApplicationContext(), imageUrlStr);

                //方法二


                String url_a = "";
                if (paths[i].indexOf("eyijiao") >= 0) {
                    url_a = Constants.IMAGE_URL + imageUrlStr;

                } else {
                    imageUrlStr = imageUrlStr.replace("/downloadApi", "");
                    url_a = Constants.DOWNLOAD_URL + imageUrlStr// 测试环境的一个配置不对--则设置图片像素+"&sf=720*1280"
                            + Constants.AUTHID + "@" + deviceId
                            + "@" + accId;//设置正确的图片下载地址
                }
                pictureURL = new PictureURL();
                pictureURL.setPictureURL(url_a);
                Log.d("wcyc", "url:" + url_a);
                datas.add(pictureURL);
            }

            Intent intent = new Intent(PaymentDetailActivity.this,
                    ImagePagerActivity.class);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
                    (Serializable) datas);
            int index = Integer.parseInt(onClickImageIndex);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,
                    index);
            startActivity(intent);
        }
    }

    /**
     * 支付成功的回调
     */
    private void goBack() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        PaymentDetailActivity.this.finish();
    }

    private class H5PayWebViewClient extends WebViewClient {

        private String homeurl;
        private PaymentDetailActivity paymentH5Activity;

        public H5PayWebViewClient(PaymentDetailActivity context, String homeurl) {
            this.paymentH5Activity = context;
            this.homeurl = homeurl;
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            // ------  对alipays:相关的scheme处理 -------
            if (url.startsWith("alipays:") || url.startsWith("alipay")) {

                //显示标题栏
                showTitle();
                try {
                    paymentH5Activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                } catch (Exception e) {
//                    showDialog();
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
            new AlertDialog.Builder(paymentH5Activity)
                    .setMessage("未检测到支付宝客户端，请安装后重试。")
                    .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri alipayUrl = Uri.parse("https://d.alipay.com");
                            paymentH5Activity.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                        }
                    }).setNegativeButton("取消", null).show();
        }

        public void showTitle() {
            RelativeLayout rl_h5pay_title = (RelativeLayout) paymentH5Activity.findViewById(R.id.rl_h5pay_title);
            rl_h5pay_title.setVisibility(View.VISIBLE);

            LinearLayout title_back = (LinearLayout) paymentH5Activity.findViewById(R.id.title_back);
            title_back.setVisibility(View.VISIBLE);

            TextView new_content = (TextView) paymentH5Activity.findViewById(R.id.new_content);
            new_content.setText("在线缴费");
            title_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("WebView", "onPageStarted");
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            Log.d("WebView", "onPageFinished ");
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
}
