/*
 * 文 件 名:BaseWebviewActivity.java
 * 创 建 人： 姜韵雯
 * 日    期： 2014-09-29
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.core;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.L;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.chat.ContactDetail;
import com.wcyc.zigui2.chat.MainActivity;
import com.wcyc.zigui2.contactselect.OneContactMainActivity;
import com.wcyc.zigui2.contactselect.contactMainActivity;
import com.wcyc.zigui2.newapp.activity.ApplyForMaintainActivity;
import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.activity.ImagePagerActivity;
import com.wcyc.zigui2.newapp.activity.LoginActivity;

import com.wcyc.zigui2.newapp.activity.SearchContactActivity;
import com.wcyc.zigui2.newapp.bean.AttachmentBean;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.home.NewAttendanceActivity;
import com.wcyc.zigui2.newapp.home.NewCommentActivity;
import com.wcyc.zigui2.newapp.home.NewHomeworkActivity;

import com.wcyc.zigui2.newapp.home.NewSelectSingleStudentActivity;
//import com.wcyc.zigui2.newapp.module.charge2.NewPackageSelectActivity;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargeProductActivity;
import com.wcyc.zigui2.newapp.widget.AttachmentActionOption;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.widget.ProgressWebView;

import anetwork.channel.cache.CacheManager;

import static com.taobao.accs.ACCSManager.mContext;

//2014-9-29

/**
 * 所有用web界面实现的界面的activity类.
 *
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
@SuppressLint("JavascriptInterface")
public class BaseWebviewActivity extends TaskBaseActivity {

    String TAG="BaseWebviewTAG";

    private ProgressWebView contentWebView = null;
    private FrameLayout videoView;
    private String url = null;
    private boolean canGoBack;
    private JsInterface jsInterface;
    private View view;
    private TextView text;
    private HashMap<String, String> additionalHttpHeaders = null;
    private final int CHOOSE_STUDENT = 100;
    private final int CHARGE = 101;
    private String type;//区分是哪个Html5模块调用的选择学生原生界面
    private String refreshUrl;//子贵课堂调用充值接口后跳转地址
    public static final String INTENT_REFESH_DATA = HomeActivity.INTENT_NEW_MESSAGE;//刷新的广播


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web);
        url = getIntent().getStringExtra("url");
        Log.i(TAG,"onCreate:url:"+url);
        initView();
        Bundle bundle = getIntent().getExtras();
        additionalHttpHeaders = (HashMap<String, String>) bundle.getSerializable("para");

        if (url == null) {
            url = "file:///android_asset/www/wrong.html";
        }

        contentWebView = (ProgressWebView) findViewById(R.id.webview);
        setWebView();

        IntentFilter mrefeshDataFilter = new IntentFilter(INTENT_REFESH_DATA);
        registerReceiver(refeshDataReceiver, mrefeshDataFilter);//刷新的广播
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 初始化视图.
     */
    private void initView() {
        Intent intent = getIntent();
        String title = getIntent().getStringExtra("title");
        canGoBack = intent.getBooleanExtra("canGoBack", false);
        videoView = (FrameLayout) findViewById(R.id.video_view);
        view = findViewById(R.id.nav);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }

        });
        text = (TextView) findViewById(R.id.title_text_2);
        showNavBar(title);
    }

    private void showNavBar(String title) {
        if (title != null) {
            view.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            text.setText(title);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置Web视图.
     */
    private void setWebView() {
//		contentWebView.setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = contentWebView.getSettings();
        // webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);//设置可以使用localStorage
        if (DataUtil.isNetworkAvailable(BaseWebviewActivity.this)) {
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
        webSettings.setPluginState(PluginState.ON);

        webSettings.setSavePassword(false);
        webSettings.setDefaultTextEncodingName("gb2312");
        jsInterface = new JsInterface();
        contentWebView.addJavascriptInterface(jsInterface, "android");
        contentWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                // TODO Auto-generated method stub
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        });
        Log.i(TAG,"BaseWebView:url:"+url);
        contentWebView.setWebViewClient(new NoAdWebViewClient(this, url));
        loadUrl();
    }

    private void loadUrl() {

        if (additionalHttpHeaders == null) {
            contentWebView.loadUrl(url);
        } else {
            contentWebView.loadUrl(url, additionalHttpHeaders);
        }
    }

    //刷新的广播
    private BroadcastReceiver refeshDataReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String title= intent.getStringExtra("extraAction");
            if(!TextUtils.isEmpty(title)&&title.trim().equals("showRepairList")){
                UserType user = CCApplication.getInstance().getPresentUser();
                url="https://wcyc.ziguiw.com/zgwps/app_myrepair/toList.do?schoolId="+user.getSchoolId()+"&userId="+user.getUserId()+"&mobileType=android#am";
            }
            setWebView();//重新获取数据
        }
    };

    //退出时关闭广播
    protected void onDestroy() {
        super.onDestroy();
//		System.out.println("TeacherClassDynamicsActivity onDestroy");
        unregisterReceiver(refeshDataReceiver);
//		//清空所有Cookie
		CookieSyncManager.createInstance(CCApplication.applicationContext);  //Create a singleton CookieSyncManager within a context
		CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
		cookieManager.removeAllCookie();// Removes all cookies.
		CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
		contentWebView.setWebChromeClient(null);
		contentWebView.setWebViewClient(null);
		contentWebView.getSettings().setJavaScriptEnabled(false);
		contentWebView.clearCache(true);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (canGoBack == true && contentWebView.canGoBack()) {
                contentWebView.goBack();
                return true;
            }
            contentWebView.loadData("", "text/html; charset=UTF-8", null);
            this.finish();
        }
        return false;
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        super.onPause();
        contentWebView.onPause();
    }

    /**
     * 重新获得焦点事件
     */
    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        contentWebView.onResume();
    }

    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        System.out.println("onConfigurationChanged:" + url);
        contentWebView.loadUrl(url);
    }

    /**
     * 负责和js通信的所有方法.
     */
    public class JsInterface {

        //用其他浏览器打开
        @JavascriptInterface
        public void openWithBrowser(String url) {
            if (DataUtil.isNullorEmpty(url)) {
                return;
            }

            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }


        //html5界面返回
        @JavascriptInterface
        public void back() {
            System.out.println("back");
            BaseWebviewActivity.this.finish();
        }

        //发布作业
        @JavascriptInterface
        public void addNewHomework() {
            System.out.println("addNewHomework");
            newActivity(NewHomeworkActivity.class, null);
        }

        //发布点评
        @JavascriptInterface
        public void addNewComment() {
            System.out.println("addNewComment");
            newActivity(NewCommentActivity.class, null);
        }

        //发布考勤
        @JavascriptInterface
        public void addNewAttendance() {
            System.out.println("addNewAttendance");
            newActivity(NewAttendanceActivity.class, null);
        }

        //发布维修申请
        @JavascriptInterface
        public void addNewRepair(){
            System.out.println("addNewRepair");
            newActivity(ApplyForMaintainActivity.class, null);
        }


        //发布考试
        @JavascriptInterface
        public void addNewExamination() {
            System.out.println("addNewExamination");
            //newActivity(NewHomeworkActivity.class,null);
        }

        //选择学生
        @JavascriptInterface
        public void chooseStudent(String type) {
            System.out.println("chooseStudent:" + type);
            BaseWebviewActivity.this.type = type;
            Intent intent = new Intent(BaseWebviewActivity.this, NewSelectSingleStudentActivity.class);
            Bundle bundle = new Bundle();
            String classId = CCApplication.getInstance().getPresentUser().getClassId();
            bundle.putString("classId", classId);
            bundle.putString("type", type);
            intent.putExtras(bundle);

            startActivityForResult(intent, CHOOSE_STUDENT);


//            Intent intent1=new Intent(BaseWebviewActivity.this,SearchContactActivity.class);
//            Bundle bundle1=new Bundle();
//            bundle1.putString("toType","BaseWebview");
//            bundle1.putString("type", type);
//            intent1.putExtras(bundle1);
//
//            startActivityForResult(intent1, CHOOSE_STUDENT);
        }

        //选择学生
        @JavascriptInterface
        public void searchStudent(String type){
            System.out.println("searchStudent:"+type);
            BaseWebviewActivity.this.type = type;
            Intent intent1=new Intent(BaseWebviewActivity.this,SearchContactActivity.class);
            Bundle bundle1=new Bundle();
            bundle1.putString("toType","searchStudent");
            bundle1.putString("type", type);
            intent1.putExtras(bundle1);

            startActivityForResult(intent1, CHOOSE_STUDENT);




        }



        /**
         * 当前网络不可用activity.
         */
        @JavascriptInterface
        public void isNetworkAvailable() {
            if (!DataUtil.isNetworkAvailable(BaseWebviewActivity.this)) {
                DataUtil.getToast(getResources().getString(
                        R.string.no_network));
                return;
            }
        }

        /**
         * 跳入另外一个webActivity.
         *
         * @param newUrl 跳转的地址
         */

        @JavascriptInterface
        public void goWebActivity(String newUrl) {
            Intent intent = new Intent(BaseWebviewActivity.this,
                    BaseWebviewActivity.class);
            intent.putExtra("url", Constants.WEBVIEW_URL + "/" + newUrl);
            BaseWebviewActivity.this.startActivity(intent);
        }

        /**
         * 关闭此activity.
         */

        @JavascriptInterface
        public void finishActivity() {
            BaseWebviewActivity.this.finish();
        }

        /**
         * 直接打电话的方法.
         *
         * @param number 电话号码
         */
        @JavascriptInterface
        public void call(String number) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            startActivity(intent);
        }

        /**
         * 跳转到原生的activity.
         *
         * @param id 根据id来判断应该跳入我的哪个activity里面
         */
        @JavascriptInterface
        public void goOtherActivity(int id) {
            switch (id) {

                case 4://跳转到登陆界面
                    CCApplication.app.logout();
                    CCApplication.app.finishAllActivity();
                    newActivity(LoginActivity.class, null);// 跳转到登录界面
                    break;
                default:
                    break;
            }
        }

        /**
         * HTML5跳转到原生界面选择学生.
         *
         * @param teacherClassID  当前选择班级
         * @param teacherId       老师ID
         * @param comeForWebStuID 已经选择学生ID
         * @param comeForWhere    从哪个功能点跳转过来，1:迟到， 2：早退， 3：留校， 4:点评
         * @param otherTypeStuID  不做操作的学生ID
         */
        @JavascriptInterface
        public void jumpContactMainActivity(String teacherClassID,
                                            String teacherId, String comeForWebStuID, String comeForWhere,
                                            String otherTypeStuID) {
            Intent intent = new Intent(BaseWebviewActivity.this, contactMainActivity.class);
            intent.putExtra("teacherId", teacherId);
            intent.putExtra("comeForWebStuID", comeForWebStuID);
            intent.putExtra("teacherClassID", teacherClassID);
            intent.putExtra("comeForWhere", comeForWhere);
            intent.putExtra("otherTypeStuID", otherTypeStuID);
            startActivity(intent);
        }

        /**
         * 考勤跳转到原生界面选择学生.
         *
         * @param teacherClassID  当前选择班级
         * @param teacherId       老师ID
         * @param comeForWebStuID 已经选择学生ID
         * @param comeForWhere    从哪个功能点跳转过来，1:迟到， 2：早退， 3：留校， 4:点评
         * @param otherTypeStuID  不做操作的学生ID
         * @param classname       班级名称
         */
        @JavascriptInterface
        public void zTjumpContactMainActivity(String teacherClassID,
                                              String teacherId, String comeForWebStuID, String comeForWhere,
                                              String otherTypeStuID, String classname) {
            Intent intent = new Intent(BaseWebviewActivity.this, contactMainActivity.class);
            intent.putExtra("teacherId", teacherId);
            intent.putExtra("comeForWebStuID", comeForWebStuID);
            intent.putExtra("teacherClassID", teacherClassID);
            intent.putExtra("comeForWhere", comeForWhere);
            intent.putExtra("otherTypeStuID", otherTypeStuID);
            intent.putExtra("classname", classname);
            startActivity(intent);
        }

        /**
         * 点评功能跳转到原生界面选择学生.
         *
         * @param teacherClassID     当前选择班级
         * @param teacherId          老师ID
         * @param comeForWebStuID    已经选择学生ID
         * @param comeForWhere       从哪个功能点跳转过来，1:迟到，2：早退， 3：留校，4:点评
         * @param otherTypeStuID     不做操作的学生ID
         * @param speedinessComment1 快速点评   赞
         * @param speedinessComment2 快速点评2
         * @param commentContent     点评内容
         */
        @JavascriptInterface
        public void commentJumpContactActivity(String teacherClassID,
                                               String teacherId, String comeForWebStuID, String comeForWhere,
                                               String otherTypeStuID, String speedinessComment1,
                                               String speedinessComment2, String commentContent) {
            Intent intent = new Intent(BaseWebviewActivity.this, contactMainActivity.class);
            intent.putExtra("speedinessComment1", speedinessComment1);
            intent.putExtra("speedinessComment2", speedinessComment2);
            intent.putExtra("commentContent", commentContent);
            intent.putExtra("teacherId", teacherId);
            intent.putExtra("comeForWebStuID", comeForWebStuID);
            intent.putExtra("teacherClassID", teacherClassID);
            intent.putExtra("comeForWhere", comeForWhere);
            intent.putExtra("otherTypeStuID", otherTypeStuID);
            startActivity(intent);
        }

        /**
         * 点评功能跳转到原生界面_单择学生.
         *
         * @param teacherClassID  当前选择班级
         * @param teacherId       老师ID
         * @param comeForWebStuID 已经选择学生ID
         * @param comeForWhere    从哪个功能点跳转过来，4：点评 ， 3：成绩 ， 1：查询
         * @param classname       班级名称
         */
        @JavascriptInterface
        public void oneCommentJumpContactActivity(String teacherClassID,
                                                  String teacherId, String comeForWebStuID, String comeForWhere, String classname) {
            Intent intent = new Intent(BaseWebviewActivity.this, OneContactMainActivity.class);
            intent.putExtra("teacherId", teacherId);
            intent.putExtra("comeForWebStuID", comeForWebStuID);
            intent.putExtra("teacherClassID", teacherClassID);
            intent.putExtra("comeForWhere", comeForWhere);
            intent.putExtra("classname", classname);
            startActivity(intent);
        }

        /**
         * 主页调家长成绩页面
         *
         * @param
         */
        @JavascriptInterface
        public void parentsScoreActivity(String childName,
                                         String studentId, String flag) {

        }

        /**
         * 网页跳转到修改作业页面.
         *
         * @param teacherId  老师ID
         * @param workID     课程ID
         * @param courseDate 课程事件
         */
        @JavascriptInterface
        public void jumpUpdateHomeWork(String teacherId, String workID, String courseDate) {

        }

        /**
         * 跳转到老师作业发布原生界面.
         *
         * @param classID    班级ID
         * @param subjectID  科目ID
         * @param courseUrl  课程URL
         * @param teacherID  教师ID
         * @param courseDate 课程日期
         * @param courseName 课程名称
         */
        @JavascriptInterface
        public void jumpPutNewWorkActivity(String classID, String subjectID,
                                           String courseUrl, String teacherID, String courseDate,
                                           String courseName) {

        }

        /**
         * 老师点评，web跳转到原生界面。
         *
         * @param date      当前点评时间
         * @param teacherID 老师ID
         */
        @JavascriptInterface
        public void jumpTeacherComment(String date, String teacherID) {

        }

        /**
         * 老师修改时间界面
         *
         * @param schoolTime          当前点评时间
         * @param classid             班级ID
         * @param updSchoolTimeReason 修改理由
         * @param teacherid           老师ID
         */
        @JavascriptInterface
        public void upSchoolTime(String classid, String schoolTime, String updSchoolTimeReason, String teacherid) {

        }

        /**
         * 发布动态，web跳转到原生界面。
         *
         * @param classID 班级ID
         */
        @JavascriptInterface
        public void jumpPublishDynamicActivity(String classID) {

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

            Log.i(TAG,"jumpSlideActicity:imageUrl:"+imageUrl+"------:onClickImageIndex:"+onClickImageIndex);

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

            Intent intent = new Intent(BaseWebviewActivity.this,
                    ImagePagerActivity.class);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
                    (Serializable) datas);
            int index = Integer.parseInt(onClickImageIndex);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,
                    index);
            startActivity(intent);
        }





        /**
         * 跳转到原生的activity.<p>
         * 根据id来判断应该跳入我的哪个activity里面
         *
         * @param childID 孩子ID
         */
        @JavascriptInterface
        public void ChangeChild(String childID) {
            SharedPreferences sp = null;
            if (sp == null) {
                sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
                // 用户id
            }
            Editor ed = sp.edit();
            ed.putString("childID", childID);
            ed.commit();
        }

        /**
         * 本地代码调用js代码.
         */
        @JavascriptInterface
        public void callJs() {
            contentWebView.loadUrl("javascript:javacalljs()");
        }

        /**
         * 本地代码调用js代码.
         *
         * @param data 数据
         */
        @JavascriptInterface
        public void callJs(String data) {
            // 传递参数调用
            contentWebView.loadUrl("javascript:javacalljswithargs(" + "'"
                    + data + "'" + ")");
        }

        /**
         * 页面出错后点击刷新按钮后的操作
         */
        @JavascriptInterface
        public void refesh() {
            System.out.println("===newUrl====" + contentWebView.getNewUrl());
//			contentWebView.loadUrl(contentWebView.getNewUrl());//2.0之前用的方法
            contentWebView.loadUrl(contentWebView.getNewUrl(), contentWebView.getNewAdditionalHttpHeaders());
        }


        /**
         * 页面出错后点击刷新按钮后的操作     2.0版本方法
         */
        @JavascriptInterface
        public void reload() {
            System.out.println("===newUrl====" + contentWebView.getNewUrl());
            contentWebView.loadUrl(contentWebView.getNewUrl());
        }


        /**
         * 跳转到联系人详情
         *
         * @param avatarUrl 头像地址
         * @param userNick  昵称
         * @param userName  环信用户名
         * @param cellPhone 电话号码
         */
        @JavascriptInterface
        public void goContactDetail(String avatarUrl, String userNick, String userName, String cellPhone) {
            Bundle bundle = new Bundle();
            bundle.putString("avatarUrl", Constants.BASE_URL + avatarUrl);
            bundle.putString("userNick", userNick);
            bundle.putString("userName", userName);
            bundle.putString("cellPhone", cellPhone);
            newActivity(ContactDetail.class, bundle);
        }

        /**
         * 打开附件
         * @param url
         * @param name
         */
        @JavascriptInterface
        public void handleOnClickAttachment(String url, String name) {
            System.out.println("handleOnClickAttachment:" + url + " name:" + name);
            Attachment attach = new AttachmentBean().new Attachment();
            attach.setAttachementName(name);
            attach.setAttachementUrl(url);
            AttachmentActionOption option = new AttachmentActionOption(BaseWebviewActivity.this, attach);
            option.showAtLocation(contentWebView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

        @JavascriptInterface
        public void getVipServiceInfos(String studentId, String url) {
            System.out.println("调用充值接口:" + " studentId:" + studentId + " url:" + url);
            refreshUrl = url;
            Bundle bundle = new Bundle();
//			newActivity(NewPackageSelectActivity.class,bundle);
            Intent intent = new Intent(BaseWebviewActivity.this, NewRechargeProductActivity.class);
            bundle.putString("call", "html5");
            intent.putExtras(bundle);
            intent.putExtra("module", "子贵课堂");
            intent.putExtra("moduleNumber", MenuItem.COURSE_NUMBER);
            startActivityForResult(intent, CHARGE);
        }

        @JavascriptInterface
        public void getVipServiceInfos(String module, String studentId, String url) {
            System.out.println("调用充值接口:" + "module:" + module + " studentId:" + studentId + " url:" + url);
            refreshUrl = url;
            Bundle bundle = new Bundle();
//			newActivity(NewPackageSelectActivity.class,bundle);
            Intent intent = new Intent(BaseWebviewActivity.this, NewRechargeProductActivity.class);
            bundle.putString("call", "html5");
            intent.putExtras(bundle);
            intent.putExtra("module", module);
            startActivityForResult(intent, CHARGE);
        }

        @JavascriptInterface
        public void setSreeenLandScape(String url) {
            System.out.println("setSreeenLandScape:" + url);
            BaseWebviewActivity.this.url = url;
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        @JavascriptInterface
        public void setSreeenPortrait(String url) {
            System.out.println("setSreeenPortrait:" + url);
            BaseWebviewActivity.this.url = url;
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

        @JavascriptInterface
        public void viewWebDoc(String url) {
            System.out.println("viewWebDoc:" + url);
//			String prefix = "https://docs.google.com/gview?embedded=true&url=";
//			String webDocUrl = new StringBuilder(prefix).append(url).toString();
//			contentWebView.loadUrl(webDocUrl);
            downloadFile(url);
        }

        @JavascriptInterface
        public void showNavTitle(final String title) {
            contentWebView.post(new Runnable() {
                @Override
                public void run() {
                    showNavBar(title);
                }
            });

        }
    }

    /**
     * 跳转到新的activity.
     *
     * @param cls 新activity的class
     */
    public void newActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(BaseWebviewActivity.this, cls);
        if (bundle != null) intent.putExtras(bundle);
        BaseWebviewActivity.this.startActivity(intent);
    }


    /**
     * 跳入贵友圈.
     */
    private void goMainActivity() {
        Intent intent = new Intent(BaseWebviewActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            switch (requestCode) {
                case CHOOSE_STUDENT:
                    List<String> list = (List<String>) bundle.getSerializable("student_id_List_checked");
                    if (list != null) {
                        goHtmlFunction(type, list.get(0));
                    }
                    break;
                case CHARGE:
                    String url = new StringBuilder(refreshUrl).append("&paySucess=1").toString();
                    System.out.println("charge success! " + url);
                    contentWebView.loadUrl(url);
                    break;
            }
        } else {
            switch (requestCode) {
                case CHARGE:
                    String url = new StringBuilder(refreshUrl).append("&paySucess=0").toString();
                    System.out.println("charge failed! " + url);
                    contentWebView.loadUrl(url);
                    break;
            }
        }
    }

    protected void goHtmlFunction(String type, String studentId) {
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("X-mobile-Type", "android");
        if ("comment".equals(type)) {
            goAttendenceByStudent(para, studentId);

            //人工考勤
        } else if ("attendance".equals(type)) {
            goAttendenceByStudent(para, studentId);

            //进出校考勤
        } else if ("schoolAttendance".equals(type)) {
            goSchoolAttendenceByStudent(para, studentId);
        }

        //校车考勤
        else if ("carAttendance".equals(type)) {
            goSchoolBusAttendenceByStudent(para, studentId);
        }

        //宿舍考勤
        else if ("dormAttendance".equals(type)) {
            goDormAttendenceByStudent(para, studentId);
        } else if ("score".equals(type)) {
            goScoreByStudent(para, studentId);
        }
    }

    //按学生查看考勤
    protected void goAttendenceByStudent(HashMap<String, String> para, String studentId) {
        System.out.println("goAttendenceByStudent:" + studentId);
        goHtml5(Constants.ATTENDANCE_BY_ID, studentId, para);
    }

    //进出校查看学生考勤
    protected void goSchoolAttendenceByStudent(HashMap<String, String> para, String studentId) {
        System.out.println("goAttendenceByStudent:" + studentId);
        goHtml5(Constants.ATTENDANCE_SCHOOL_BY_ID, studentId, para);
    }

    //校车查看学生考勤
    protected void goSchoolBusAttendenceByStudent(HashMap<String, String> para, String studentId) {
        System.out.println("goAttendenceByStudent:" + studentId);
        goHtml5(Constants.ATTENDANCE_SCHOOL_BUS_BY_ID, studentId, para);
    }

    //宿舍查看学生考勤
    protected void goDormAttendenceByStudent(HashMap<String, String> para, String studentId) {
        System.out.println("goAttendenceByStudent:" + studentId);
        goHtml5(Constants.ATTENDANCE_DORM_BY_ID, studentId, para);
    }

    //按学生查看点评
    protected void goCommentByStudent(HashMap<String, String> para, String studentId) {
        System.out.println("goCommentByStudent:" + studentId);
        goHtml5(Constants.COMMENT_BY_ID, studentId, para);
    }

    //按学生查看成绩
    protected void goScoreByStudent(HashMap<String, String> para, String studentId) {
        System.out.println("goScoreByStudent:" + studentId);
        goHtml5(Constants.SCORE_BY_ID, studentId, para);
    }

    public void goHtml5(String appUrl, String studentId, HashMap<String, String> para) {
        Bundle bundle = new Bundle();
        String url = new StringBuilder(Constants.WEBVIEW_URL)
                .append(appUrl).append("?studentId=" + studentId).toString();
        bundle.putString("url", url);
        bundle.putSerializable("para", para);
        System.out.println("url" + url);
        newActivity(BaseWebviewActivity.class, bundle);
    }

    public void downloadFile(final String url) {

        DataUtil.showDialog(BaseWebviewActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String subForder = Environment.getExternalStorageDirectory() + "/Zigui_cache/";
                    String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
                    File savedFile = HttpHelper.downFile(url, subForder, fileName);
                    System.out.println("savedFile:" + savedFile);
                    DataUtil.clearDialog();
                    if (savedFile != null) {
                        openFile(savedFile);
                        System.out.println("openFile:" + savedFile);
                    }
                } catch (IOException e) {
                    DataUtil.clearDialog();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

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
}