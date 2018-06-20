package com.wcyc.zigui2.newapp.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.ImagePagerActivity;
import com.wcyc.zigui2.utils.DataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiehua on 2016/9/1.
 */
public class CustomWebView {
    private Context mContext;
    public CustomWebView(Context context,WebView webView,String content){
        mContext = context;
        init(context,webView,content);
    }

    public CustomWebView(Context context,WebView webView,String content,int color){
        mContext = context;
        init(context,webView,content);
        Context mContext = CCApplication.getInstance().getApplicationContext();
        webView.setBackgroundColor(mContext.getResources().getColor(color));
    }

    private void init(Context context,WebView webView,String content){
        mContext = context;
        //设置图片大小，文字大小不变，图片超过320px则最大，小于320px则原图显示
        double scale = DataUtil.TableWidthExceed(context,content);
        content = DataUtil.parseHtmlTable(content);
//        content = DataUtil.parseHtmlContent(context,content);
//        String head = "<head><style>img{max-width:320px !important;max-height:320px !important;}</style></head>";
//        head += "<body width=320px style=\"word-wrap:break-word; font-family:Arial;font-size:30px; \">";
        String newContent = DataUtil.modifyImageUrl(content);
//        newContent = head + newContent;

        WebSettings webSettings = webView.getSettings();
        webSettings.setMinimumLogicalFontSize(16);
        webSettings.setMinimumFontSize(16);
//        webSettings.setDefaultFixedFontSize(16);
//        webSettings.setDefaultFontSize(16);
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        //html页面大小自适应
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webSettings.setDisplayZoomControls(true);
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int dpi = dm.densityDpi;
        if(dpi <= 240) {
            webView.setInitialScale(150);
        }

        System.out.println("dpi:" + dpi + " scale:" + scale);
        if(scale > 1) {
            double times = 1;
            times = ((double) dpi) / 240;
            webView.setInitialScale((int) (scale * times * 100));
        }
//        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        webView.loadData(newContent, "text/html; charset=UTF-8", null);//这种写法可以正确解码\
        webView.addJavascriptInterface(new JavascriptInterface(context), "imagelistner");
        webView.setWebViewClient(new webViewClient());
    }

    private void doubleClick(WebView webView){
        webView.performClick();
        DataUtil.Sleep(100);
        webView.performClick();
//        touch(webView);
//        DataUtil.Sleep(100);
//        touch(webView);
    }

    private void touch(WebView webView){
        int x = 10;
        int y = 10;
        final long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(
                downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        final MotionEvent upEvent = MotionEvent.obtain(
                downTime, SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
        webView.onTouchEvent(downEvent);
        webView.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    // 注入js函数监听

    private void addImageClickListner(WebView webview) {
//        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
//        webview.loadUrl("javascript:(function(){" +
//                "var objs = document.getElementsByTagName(\"img\"); " +
//                "for(var i=0;i<objs.length;i++)  " +
//                "{"
//                + "    objs[i].onclick=function()  " +
//                "    {  "
//                + "        window.imagelistner.openImage(this.src);  " +
//                "    }  " +
//                "}" +
//                "})()");
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webview.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "    objs[i].onclick=function()  " + "    {  "
                + "        window.imagelistner.openImage(this.src);  "
                + "    }  " + "}" + "})()");
    }

    // js通信接口

    public class JavascriptInterface{
        private Context context;
        public JavascriptInterface(Context context) {
            this.context = context;
        }
        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            System.out.println(img);
            //
            Intent intent = new Intent();
            List<PictureURL> urls = new ArrayList<PictureURL>();
            PictureURL item = new PictureURL();
            item.setPictureURL(img);
            urls.add(item);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
                    (Serializable) urls);
//			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,
//					index);
            intent.setClass(context, ImagePagerActivity.class);
            context.startActivity(intent);
            System.out.println(img);
        }
    }

    private class webViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {

            view.getSettings().setJavaScriptEnabled(true);

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner(view);
//            doubleClick(view);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            System.out.println("url:" + url);
            Uri uri = Uri.parse(url); // url为你要链接的地址
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
            return true;
        }
    }}
