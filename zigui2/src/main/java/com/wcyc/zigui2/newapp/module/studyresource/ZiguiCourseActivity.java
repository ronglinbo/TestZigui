package com.wcyc.zigui2.newapp.module.studyresource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;

import com.easefun.polyvsdk.activity.PolyvMainActivity;
import com.easefun.polyvsdk.activity.PolyvPlayerActivity;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.bean.ZiGuiVideo;
import com.wcyc.zigui2.bean.ZiGuiVideoDao;
import com.wcyc.zigui2.core.ADFilterTool;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.activity.testVideoActivity;
import com.wcyc.zigui2.newapp.module.charge.NewPackageConfirmActivity;
import com.wcyc.zigui2.newapp.module.charge2.NewPayPop;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargeProductActivity;
import com.wcyc.zigui2.newapp.module.leavemessage.ChildMessageDao;
import com.wcyc.zigui2.utils.ApiManager;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.DateUtils;
import com.wcyc.zigui2.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static com.wcyc.zigui2.utils.Constants.ziguiUrl;

/**
 * @author xiehua
 */
public class ZiguiCourseActivity extends BaseActivity {

    String TAG="ZiguiCourseActivityTAG";

    private FrameLayout videoview;
    private Button videolandport;
    private WebView videowebview;
    private Boolean islandport = true;
    private View xCustomView;
    private xWebChromeClient xwebchromeclient;
    private String url;
    private final String ERRORURL = "file:///android_asset/www/wrong.html";
    private JsInterface jsInterface;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    private final int CHARGE = 100;
    private String refreshUrl;//子贵课堂调用充值接口后跳转地址
    private static int clickBackTimes = 0;//在子贵课堂里按返回键的次数
    private ProgressDialog progressDialog;
    private ZiGuiVideoDao ziGuiVideoDao = CCApplication.getDaoinstant().getZiGuiVideoDao();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        clearWebViewCache();
        clearLoginCookies();
        startDate = null;
        endDate = null;
        learningTime = 0;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_video_web);
        videowebview = (WebView) findViewById(R.id.video_webview);
        videowebview.clearFormData();;
        videowebview.clearHistory();
        videowebview.clearCache(true);
        url = getIntent().getStringExtra("url");
        if (url == null) {
            url = "file:///android_asset/www/wrong.html";
        }
        initwidget();
//		initListener();
        jsInterface = new JsInterface();
        videowebview.setWebViewClient(new NoAdWebViewClient(this, url));
        videowebview.addJavascriptInterface(jsInterface, "android");
        videowebview.loadUrl(url);
    }

    @Override
    public void onPause() {
        super.onPause();
        videowebview.onPause();
    }


    @Override
    protected void getMessage(String data) {
        //支付参数
        System.out.println(data);

    }

    /**
     * 重新获得焦点效果
     */
    @Override
    protected void onResume(){
        super.onResume();
        videowebview.onResume();

    }

    private void initListener() {
        // TODO Auto-generated method stub
        videolandport.setOnClickListener(new Listener());
    }

    private void initwidget(){
        // TODO Auto-generated method stub
        videoview = (FrameLayout) findViewById(R.id.video_view);
        videowebview = (WebView) findViewById(R.id.video_webview);
        WebSettings ws = videowebview.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        ws.setUseWideViewPort(true);
//		ws.setLoadWithOverviewMode(true);
        ws.setSavePassword(false);
        ws.setSaveFormData(true);
        ws.setJavaScriptEnabled(true);
        videowebview.removeJavascriptInterface("searchBoxJavaBridge_");
        videowebview.removeJavascriptInterface("accessibility");
        videowebview.removeJavascriptInterface("accessibilityTraversal");

     //   if (DataUtil.isNetworkAvailable(this)) {
         // ws.setCacheMode(WebSettings.LOAD_DEFAULT);//默认
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);// 每次从网络获取
//        } else {
//            ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//默认使用缓存
//        }
        //ws.setAppCacheMaxSize(8 * 1024 * 1024);//缓存最多可以有8M
        ws.setAllowFileAccess(true);//可以读取文件缓存(manifest生效)
       // ws.setAppCacheEnabled(true);//应用可以有缓存
//		ws.setPluginState(WebSettings.PluginState.ON);
//		ws.setGeolocationEnabled(true);
        xwebchromeclient = new xWebChromeClient();
        videowebview.setWebChromeClient(xwebchromeclient);
        videowebview.setWebViewClient(new xWebViewClientent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearLoginCookies();
        videowebview.clearCache(true);
    }

    /**
     * 清除Cookie
     */
    public void clearLoginCookies() {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(CCApplication.getInstance().getApplicationContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieSyncManager.sync(); // forces sync manager to sync now

    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache(){

        //清理Webview缓存数据库
        try {
            boolean is=deleteDatabase("webview.db");
            boolean is1=deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()+"");
        Log.e("delete", "webviewCacheDir path="+webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }

    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        Log.i("delete", "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e("delete", "delete file no exists " + file.getAbsolutePath());
        }
    }

    public static Date startDate = null;
    public static Date endDate = null;
    public static int learningTime = 0;
    private String vid="-1";

    /**
     * 负责和js通信的所有方法.
     */
    public class JsInterface {
        //html5界面返回
        @JavascriptInterface
        public void back() {
            System.out.println("back");
            ZiguiCourseActivity.this.finish();
        }

        @JavascriptInterface
        public  void  commitOrder(String order_no,String url){
            DataUtil.getToastShort("订单:"+order_no+",回调:"+url);
            NewPayPop newPayPop=new NewPayPop(ZiguiCourseActivity.this,6,order_no,url);
            newPayPop.showAtLocation(videowebview, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        @JavascriptInterface
        public void showSource(String html) {
            Log.e("HTML", html);
        }

        /**
         * 当前网络不可用activity.
         */
        @JavascriptInterface
        public void isNetworkAvailable(){
            if (!DataUtil.isNetworkAvailable(ZiguiCourseActivity.this)){
                DataUtil.getToast(getResources().getString(
                        R.string.no_network));
                return;
            }
        }

        @JavascriptInterface
        public void showHubMsg() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgessBar();
                }
            });

        }

        @JavascriptInterface
        public void hideHubMsg() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissPd();
                }
            });

        }



        /**
         * 关闭此activity.
         */

        @JavascriptInterface
        public void finishActivity() {
            ZiguiCourseActivity.this.finish();
        }

        @JavascriptInterface
        public void memberCharge(String packageCode, String url) {
            System.out.println("立即购买充值接口:" + "packageCode:" + packageCode + " url:" + url);
            refreshUrl = url;
            Bundle bundle = new Bundle();
            Intent intent = new Intent(ZiguiCourseActivity.this, NewRechargeProductActivity.class);
            bundle.putString("call", "html5");
            //301 是子贵课堂的小学，201 是初中，101 是高中
            if (packageCode.equals("101")) {
                intent.putExtra("module", "子贵课堂(高中)");
            }
            if (packageCode.equals("201")) {
                intent.putExtra("module", "子贵课堂(初中)");
            }
            if (packageCode.equals("301")) {
                intent.putExtra("module", "子贵课堂(小学)");
            }
            intent.putExtras(bundle);
            startActivityForResult(intent, CHARGE);
        }

        @JavascriptInterface
        public void getVipServiceInfos(String studentId, String url) {
            System.out.println("调用充值接口:" + " studentId:" + studentId + " url:" + url);
            refreshUrl = url;
            Bundle bundle = new Bundle();
            Intent intent = new Intent(ZiguiCourseActivity.this, NewRechargeProductActivity.class);
            bundle.putString("call", "html5");
            intent.putExtra("module", "子贵课堂");
            intent.putExtras(bundle);
            startActivityForResult(intent, CHARGE);
        }

        @JavascriptInterface
        public void setSreeenLandScape(String url) {
            System.out.println("setSreeenLandScape:" + url);
            ZiguiCourseActivity.this.url = url;
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        @JavascriptInterface
        public void setSreeenPortrait(String url) {
            System.out.println("setSreeenPortrait:" + url);
            ZiguiCourseActivity.this.url = url;
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        @JavascriptInterface
        public int getSreenOrientation() {
            int orientation = getRequestedOrientation();
            System.out.println("orientation:" + orientation);
            return orientation;
        }

        //播放
        @JavascriptInterface
        public void playVideo(String vid1, String time1) {
            Log.i(TAG,"点击了播放--");

            vid = vid1;

            List<ZiGuiVideo> list = ziGuiVideoDao.queryBuilder().where(ZiGuiVideoDao.Properties.Vid.eq(vid1)).list();

            if (list.size() > 0) {

                //数据库有值
                time1 = list.get(0).getTime() + "";
            }else{
                time1=Integer.parseInt(time1)*1000+"";
            }

            Bundle bundle = null;
            bundle = new Bundle();
            //   DataUtil.getToastShort("time:" + time1);
            bundle.putString("vid", vid);
            try {

                bundle.putInt("time", Integer.parseInt(time1));
            } catch (Exception e) {

            }

            newActivity(testVideoActivity.class, bundle);
            // newActivity(PolyvPlayerActivity.class, bundle);
        }

        //播放位置
        @JavascriptInterface
        public int getPlayTime() {
            time=time/1000;
            return time;
        }

        //学习时长
        @JavascriptInterface
        public int getLearningRecordsTime() {
            if (endDate == null) {
                learningTime = 0;
            }

            //删除对应本地存储
            List<ZiGuiVideo> list = ziGuiVideoDao.queryBuilder().where(ZiGuiVideoDao.Properties.Vid.eq(vid)).list();
            if (list.size() > 0) {
                ziGuiVideoDao.delete(list.get(0));
            }

            return learningTime;

        }

        @JavascriptInterface
        public void viewWebDoc(String url) {
            System.out.println("viewWebDoc:" + url);
            System.out.println("process viewWebDoc:" + url);
            downloadFile(url);
        }
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileid) {
        try {
            //视频缓存路径
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Constants.CACHE_PATH + "video-cache" + "/" + fileid);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("111", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void downLoad(String url, final String fileid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DLS_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(CCApplication.getInstance().initClient())
                .build();
        ApiManager apiManager = retrofit.create(ApiManager.class);
        Call<ResponseBody> call = apiManager.downloadFileWithDynamicUrlSync(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("111", "server contacted and has file");
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), fileid);
                    //进入视频打开界面
                    DataUtil.clearDialog();
                    File savedFile = new File(Constants.CACHE_PATH + "video-cache" + "/" + fileid);
                    if (savedFile != null) {
                        openFile(savedFile);
                        System.out.println("openFile:" + savedFile);
                    }

                }else{
                    DataUtil.clearDialog();
                    DataUtil.getToastShort("课件已损坏");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DataUtil.clearDialog();
                DataUtil.getToastShort(t.getMessage());
                Log.e("111", "error");
            }
        });
    }

    public void downloadFile(final String url) {
        DataUtil.showDialog(ZiguiCourseActivity.this);
        String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        downLoad(url, fileName);

//		DataUtil.showDialog(ZiguiCourseActivity.this);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//
//					String subForder = Environment.getExternalStorageDirectory()+"/Zigui_cache/";
//
//					String fileName = url.substring(url.lastIndexOf("/")+1,url.length());
//
////					fileName = getShortFileName(fileName);
//					File savedFile = HttpHelper.downFile(url, subForder,fileName);
//
//
//					System.out.println("savedFile:"+savedFile);
//					DataUtil.clearDialog();
//					if(savedFile != null){
//						openFile(savedFile);
//						System.out.println("openFile:"+savedFile);
//					}
// 				} catch (IOException e) {
//					DataUtil.clearDialog();
//					DataUtil.getToast("打开文件出错 "+e.getMessage());
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();

    }

    //截断文件名，倒数第三个‘-’
    private String getShortFileName(String url) {
        int pos = 0;
        int i = 0;
        int[] mark = new int[url.length()];
        while (pos < url.length()) {
            pos = url.indexOf("_", pos);
            mark[i++] = pos;
            if (pos > -1) {
                pos++;
            } else {
                break;
            }
        }
        String result = url.substring(mark[i - 4] + 1, url.length());
        return result;
    }

    private void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = DataUtil.getMIMEType(file);

        intent.setDataAndType(Uri.fromFile(file), type);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHARGE:
                    try {
                        refreshUrl = java.net.URLDecoder.decode(refreshUrl, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String url = new StringBuilder(refreshUrl).append("&paySuccess=1").toString();
                    videowebview.setVisibility(View.INVISIBLE);
                    System.out.println("charge success! " + url);
                    videowebview.loadUrl(url);
                    break;
            }
        } else {
            switch (requestCode) {
                case CHARGE:
//					String url = new StringBuilder(refreshUrl).append("&paySuccess=0").toString();
//					System.out.println("charge failed! "+url);
//					videowebview.loadUrl(url);
                    break;
            }
        }
    }

    class Listener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                hideCustomView();
                return true;
            }

            if (++clickBackTimes < 2) {
                DataUtil.getToast("再按一次退出在线课堂");
                waitFor5s();
                return true;
            }
            addLearnRecord();
            videowebview.loadData("", "text/html; charset=UTF-8", null);
            clickBackTimes = 0;
            this.finish();
        }
        return false;
    }

    private void addLearnRecord() {
        videowebview.loadUrl("javascript:addLearingRecordBack()");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitFor5s() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                    clickBackTimes = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean inCustomView() {
        return (xCustomView != null);
    }

    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
    }

    public class xWebChromeClient extends WebChromeClient {
        private Bitmap xdefaltvideo;
        private View xprogressvideo;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			videowebview.setVisibility(View.GONE);

            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            videoview.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            videoview.setVisibility(View.VISIBLE);
        }

        @Override
        public void onHideCustomView() {
            if (xCustomView == null)
                return;
            // Hide the custom view.
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            // Remove the custom view from its container.
            videoview.removeView(xCustomView);
            xCustomView = null;
            videoview.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            videowebview.setVisibility(View.VISIBLE);
        }

        @Override
        public View getVideoLoadingProgressView() {
            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater.from(ZiguiCourseActivity.this);
                xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            (ZiguiCourseActivity.this).setTitle(title);
        }

        public Bitmap getDefaultVideoPoster() {
            return null;
        }
    }

    public class xWebViewClientent extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("shouldOverrideUrlLoading: " + url);
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            view.stopLoading();
            view.clearView();
            view.loadUrl(ERRORURL);
            DataUtil.clearDialog();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            System.out.println("onPageStarted:" + url);
            compatible(url);
            showProgessBar();
//			videoview.setVisibility(View.VISIBLE);
//			view.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            System.out.println("onPageFinished:" + url);
            dismissPd();
//			compatible(url);
//			videoview.setVisibility(View.GONE);
//			view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.out.println("onConfigurationChanged:" + url);
//		videowebview.loadUrl(url);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            islandport = false;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            islandport = true;
        }
    }

    //兼容魅族手机的播放开始界面黑屏问题
    private void compatible(String url) {
        String model = Build.MANUFACTURER;
        if (model.contains("meizu")
                || model.contains("Meizu")) {
            if (url.contains("videoplay")) {
                System.out.println("sleep 1500");
                sleep(1500);
            }
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class NoAdWebViewClient extends WebViewClient {
        private String homeurl;
        private Context context;
        private ZiguiCourseActivity ziguiCourseActivity;

        public NoAdWebViewClient(Context context, String homeurl) {
            this.context = context;
            this.homeurl = homeurl;
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("WebView", "onPageStarted");
            showProgessBar();
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            Log.d("WebView", "onPageFinished ");

            //   view.loadUrl("javascript:function setTop(){document.querySelector('#EG0phD8f').style.display=\"none\";}setTop();");
            super.onPageFinished(view, url);
            videowebview.setVisibility(View.VISIBLE);
            dismissPd();

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
    public static int time;

}
