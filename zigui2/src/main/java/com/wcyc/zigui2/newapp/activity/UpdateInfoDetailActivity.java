package com.wcyc.zigui2.newapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.module.news.NewSchoolNewsDetailsActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateInfoDetailActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout title_back;
    private TextView new_content;
    private WebView webView;
    private String content;

    @Override
    protected void getMessage(String data) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info_detail);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);

    }

    public String replaceImgSrc(String content){

        Document doc = Jsoup.parse(content);
        Elements pngs = doc.select("img[src]");
        for (Element element : pngs) {
            String imgUrl = element.attr("src");
            //
            imgUrl=imgUrl.trim();
            imgUrl+=Constants.AUTHID + "@" + getDeviceID()
                    + "@" + CCApplication.app.getMemberInfo().getAccId();

                element.attr("src", imgUrl);

        }
        content = doc.toString();
        content=content.replace("&amp;","&");
        System.out.println(content);
        return content;

    }

    private void initData() {
        new_content.setText("版本更新说明");
        content=getIntent().getStringExtra("content");
        if(!DataUtil.isNullorEmpty(content)){
            if (content.length() > 0) {
//			String content=DataUtil.parseHtmlTwoContent(this,contentStr);

                //设置图片大小，文字大小不变，图片超过320px则最大，小于320px则原图显示



                String head="<html><head><style>img{max-width:320px !important;max-height:320px !important;}</style></head><body>";
                content=head+content+"</body></html>";
                content= replaceImgSrc(content);
                webView.getSettings().setDefaultTextEncodingName("UTF -8");// 设置默认为utf-8
                webView.getSettings().setJavaScriptEnabled(true);
                // webView.loadData(data, "text/html",
                // "UTF -8");//API提供的标准用法，无法解决乱码问题
                webView.loadData(content, "text/html; charset=UTF-8", null);//
                // 这种写法可以正确解码
                // webView.loadUrl("file:///assets/www/wrong.html");
                // webView.loadUrl("file:///android_asset/www/wrong.html");
                // webView.loadUrl("http://news.ifeng.com/a/20160705/49299109_0.shtml");
                // webView.loadUrl("http://tl.changyou.com/screen.shtml");
                // webView.loadUrl("http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=11010702000007");
                // webView.loadUrl("http://www.baidu.com");
                // webView.loadUrl("http://www.hnflcp.com/");
                // webView.loadUrl(url, additionalHttpHeaders);
                // 添加js交互接口类，并起别名 imagelistner
                webView.addJavascriptInterface(new JavascriptInterface(this),
                        "imagelistner");
                webView.setWebViewClient(new MyWebViewClient());

                // 下面几句代码主要用来使网页自适应屏幕大小，设置false则按PC端大小显示
			WebSettings webSettings = webView.getSettings();
      		webSettings.setUseWideViewPort(true);
			webSettings.setLoadWithOverviewMode(true);
			// 设置可以支持缩放
			webSettings.setSupportZoom(true);
			// 设置出现缩放工具
			webSettings.setBuiltInZoomControls(true);
			// 缩放功能
			webSettings.setUseWideViewPort(false);
			webSettings.setDomStorageEnabled(true);
			webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
			webSettings.setSaveFormData(true);
			webSettings.setAppCacheMaxSize(1024 * 5);
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int dpi = dm.densityDpi;
                double scale = DataUtil.TableWidthExceed(this,content);
                if(scale > 1) {
                    double times = ((double)dpi)/240;
                    webView.setInitialScale((int) (scale * times * 100));
                }
            } else {
                webView.setVisibility(View.GONE);
            }
        }

    }

    private void initView() {
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
        webView= (WebView) findViewById(R.id.webView);

    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "    objs[i].onclick=function()  " + "    {  "
                + "        window.imagelistner.openImage(this.src);  "
                + "    }  " + "}" + "})()");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                UpdateInfoDetailActivity.this.finish();
                break;
            default:
                break;

        }
    }

    // js通信接口
    public class JavascriptInterface {
        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        //		@SuppressLint("JavascriptInterface")
        @android.webkit.JavascriptInterface
        public void openImage(String img) {

            // Intent intent = new Intent();
            // intent.putExtra("image", img);
            // intent.setClass(context, ShowWebImageActivity.class);
            // context.startActivity(intent);
            String url = img;
//			url=url.substring(0, url.lastIndexOf("&"));
//			url+="&authId=MB@864690025904296@19849";
//			img = "/downloadApi?fileId=4286";// 先用固定的图片地址
			// url = DataUtil.getDownloadURL(context, img);
            url+=Constants.AUTHID + "@" + ((BaseActivity)context).getDeviceID()
                    + "@" + CCApplication.app.getMemberInfo().getAccId();
            System.out.println(url);
            List<PictureURL> datas = new ArrayList<PictureURL>();
            PictureURL pictureURL = new PictureURL();
            pictureURL.setPictureURL(url);
            datas.add(pictureURL);

            Intent intent = new Intent(context, ImagePagerActivity.class);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
                    (Serializable) datas);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 1);
            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("url:" + url);
            Uri uri = Uri.parse(url); // url为你要链接的地址
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
//			aa();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

}
