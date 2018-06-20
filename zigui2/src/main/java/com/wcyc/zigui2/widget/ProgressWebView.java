/*
* 文 件 名:ProgressWebView.java
* 创 建 人： 姜韵雯
* 日    期： 2014-09-29
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.widget;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wcyc.zigui2.utils.DataUtil;

//2014-9-29
/**
 * 自定义带进度条的webview
 * 
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public class ProgressWebView extends WebView {

//    private ProgressBar progressbar;
    private String newUrl = null;
    private final String ERRORURL = "file:///android_asset/www/wrong.html";
	private Map<String, String> newAdditionalHttpHeaders;
	private Context mContext;
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
		mContext = context;
//        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
//        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 15, 0, 0));
//        addView(progressbar);
        //        setWebViewClient(new WebViewClient(){});
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebClient());
        
        //webview屏蔽长按事件，如果不屏蔽会有在网页上长按相应复印等操作的bug
//        setOnLongClickListener(new OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
    }
	public class WebClient extends WebViewClient {
    	@Override
    	public void onReceivedError(WebView view, int errorCode,
    			String description, String failingUrl) {
    		view.stopLoading();
    	    view.clearView();
    	    view.loadUrl(ERRORURL);
//    	    setProgressbarVisible(false);
    	    DataUtil.clearDialog();
    	}
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		if(!url.equals(ERRORURL)){
    			newUrl = url;
    		}
    		view.loadUrl(url);
    		return true;
    	}
    }
	@Override
	public void loadUrl(String url) {
//		CookieManager cookieManager = CookieManager.getInstance();
        //获取登录时的cookie
//        String oldCookie = "JSESSIONID"+"="+CCApplication.app.getMemberInfo().getSessionid()+";"
//        					+"userID"+"="+CCApplication.app.getMemberInfo().getUserID()+";"
//        					+"type "+"="+"1"+";";
//        cookieManager.setCookie(url, oldCookie);
//        getSettings().setDefaultTextEncodingName("gbk");
		super.loadUrl(url);
		if(url != null && !url.equals(ERRORURL)){
			newUrl = url;
			DataUtil.showDialog((Activity)getContext());
		}
	}
	
	public void loadUrl(String url,Map<String,String> additionalHttpHeaders){
		System.out.println("additionalHttpHeaders:"+additionalHttpHeaders);
	    super.loadUrl(url, additionalHttpHeaders);
		if(url != null && !url.equals(ERRORURL)){
			newUrl = url;
			newAdditionalHttpHeaders=additionalHttpHeaders;
			
			DataUtil.showDialog((Activity)getContext());
		}
	}
	
    public Map<String, String> getNewAdditionalHttpHeaders() {
		return newAdditionalHttpHeaders;
	}

	public void setNewAdditionalHttpHeaders(
			Map<String, String> newAdditionalHttpHeaders) {
		this.newAdditionalHttpHeaders = newAdditionalHttpHeaders;
	}

	public String getNewUrl() {
		return newUrl;
	}
	public void setNewUrl(String newUrl) {
		this.newUrl = newUrl;
	}
	public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 100) {
//                progressbar.setVisibility(GONE);
                DataUtil.clearDialog();
            } else {
//                if (progressbar.getVisibility() == GONE)
//                    progressbar.setVisibility(VISIBLE);
//                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

//		@Override
//		public void onShowCustomView(View view,WebChromeClient.CustomViewCallback callback){
//			view.setVisibility(View.GONE);
////			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			videowebview.setVisibility(View.GONE);
//
//			if (xCustomView != null) {
//				callback.onCustomViewHidden();
//				return;
//			}
//
//			videoview.addView(view);
//			xCustomView = view;
//			xCustomViewCallback = callback;
//			videoview.setVisibility(View.VISIBLE);
//		}
    }
//    public void setProgressbarVisible(boolean visible){
//    	if(progressbar != null){
//    		if(visible){
//    			progressbar.setVisibility(VISIBLE);
//    		}else{
//    			progressbar.setVisibility(GONE);
//    		}
//    	}
//    }
//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
//        lp.x = l;
//        lp.y = t;
//        progressbar.setLayoutParams(lp);
//        super.onScrollChanged(l, t, oldl, oldt);
//    }
}
