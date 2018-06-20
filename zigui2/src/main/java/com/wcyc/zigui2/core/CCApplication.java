/*
* 文 件 名:CCApplication.java
* 创 建 人： 姜韵雯
* 日    期： 2014-09-29
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

//import com.bugtags.library.Bugtags;
//import com.bugtags.library.BugtagsOptions;
//import com.danikula.videocache.HttpProxyCacheServer;
import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.wxlib.util.SysUtil;
import com.dh.DpsdkCore.Device_Info_Ex_t;
import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.InviteVtCallParam_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.RingInfo_t;
import com.dh.DpsdkCore.dpsdk_constant_value;
import com.dh.DpsdkCore.fDPSDKInviteVtCallParamCallBack;
import com.dh.DpsdkCore.fDPSDKRingInfoCallBack;
import com.dh.DpsdkCore.fDPSDKStatusCallback;
import com.dh.activity.AutoVtActivity;
import com.dh.groupTree.GroupListManager;
//import com.easefun.polyvsdk.PolyvDevMountInfo;
//import com.easefun.polyvsdk.PolyvSDKClient;
//import com.easefun.polyvsdk.PolyvSDKUtil;
import com.easefun.polyvsdk.PolyvDevMountInfo;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.PolyvSDKUtil;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.google.gson.Gson;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.aliChat.ChattingOperationCustomSample;
import com.wcyc.zigui2.aliChat.ChattingUICustomSample;
import com.wcyc.zigui2.aliChat.ConversationListOperationCustomSample;
import com.wcyc.zigui2.aliChat.ConversationListUICustomSample;
import com.wcyc.zigui2.aliChat.NotificationInitSampleHelper;
import com.wcyc.zigui2.bean.DaoMaster;
import com.wcyc.zigui2.bean.DaoSession;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.chat.ChatActivity;
import com.wcyc.zigui2.newapp.adapter.MenuAdapter;
import com.wcyc.zigui2.newapp.bean.EasemobGroupInfo;
import com.wcyc.zigui2.newapp.bean.HeEducationUserBean;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.MoniterListInfo;
import com.wcyc.zigui2.newapp.module.email.MenuConfigBean;

import com.wcyc.zigui2.newapp.service.ChatLoginService;
import com.wcyc.zigui2.chat.PreferenceUtils;
import com.wcyc.zigui2.dao.DBSharedPreferences;
import com.wcyc.zigui2.dao.DbOpenHelper;
import com.wcyc.zigui2.dao.UserDao;
import com.wcyc.zigui2.newapp.activity.LoginActivity;

import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.AllGradeClass;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.ModelRemindList;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.bean.NewMessageListBean;

import com.wcyc.zigui2.newapp.bean.ServerResult;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean.ServiceInfo;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean.Role;

import com.wcyc.zigui2.newapp.service.PushSmsService;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordBean;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordBean.DailyRecordDetail;
import com.wcyc.zigui2.newapp.module.email.NewEmailBean;
import com.wcyc.zigui2.newapp.module.email.NewMailInfo;
import com.wcyc.zigui2.newapp.module.notice.NewNoticeBean;
import com.wcyc.zigui2.newapp.module.notice.NoticeDetail;
import com.wcyc.zigui2.newapp.module.summary.SummaryBean;
import com.wcyc.zigui2.newapp.module.summary.SummaryBean.SummaryDetail;
import com.wcyc.zigui2.newapp.receiver.CMDMessageBroadcastReceiver;
import com.wcyc.zigui2.newapp.service.UmengPushService;
import com.wcyc.zigui2.utils.Constants;
// com.wcyc.zigui2.utils.CrashHandler;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.utils.RequestHeader;
import com.wcyc.zigui2.utils.SignCheck;
import com.wcyc.zigui2.widget.CustomDialog;


import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//2014-9-29

/**
 * 应用Application类
 *
 * @author 姜韵雯
 * @version 1.00
 * @since 1.00
 */
public class CCApplication extends Application {
    private static final String savePath = Constants.CACHE_PATH + "image/";
    public static long memberId = -1;
    public static CCApplication app;
    private NewMemberBean memberInfo;
    private MemberDetailBean detail;
    private NewMessageBean message;
    private NewMessageListBean messageList;
    private ModelRemindList remindList;
    private AllContactListBean AllContactList;
    private ContactsList contact;//本人联系信息
    private ServiceExpiredBean serviceInfo;
    private ChargeProduct chargeProduct;
    private NewNoticeBean notice;
    private DailyRecordBean dailyRecord;
    private SummaryBean summary;
    private NewEmailBean inbox;
    private NewEmailBean outbox;
    private NewEmailBean draft;
    private NewEmailBean recycle;
    private UserType presentUser;
    public static DBSharedPreferences dbsp;
    public static Context applicationContext;
    // login user name
    public final String PREF_USERNAME = "username";
    private String userName = null;
    // login password
    private static final String PREF_PWD = "pwd";
    private String password = null;
    private Map<String, User> contactList;

    private int curUserIndex;
    private String accId;
    private String deviceId;
    private String deviceToken = "";
    private CustomDialog dialog;

    private List<NewClasses> schoolAllClassList;
    //	private List<GradeMap> gradeMapList;
    private AllGradeClass allGradeClass;
    private AllTeacherList teacherList;
    private MoniterListInfo info;
    private EasemobGroupInfo groupInfo;
    private MenuConfigBean menuConfig;
    private ImageLoader imageLoader;
    //系统时间
    private String server_date = "";
    /**
     * 记录所有的activity，在该退出的时候退出
     */
    public static List<Activity> activityList = new ArrayList<Activity>();

    private static String pickPhotoFilename = null;
    private CheckSericeBroadcastReceiver checkService;
    private NewMessageBroadcastReceiver msgReceiver;
    private CMDMessageBroadcastReceiver cmdReceiver;
    private static final String TAG = "CCApplication";
    private static String ROOT = Environment.getExternalStorageDirectory().getPath() + "/ZiguiMoniter";
    private static final String LOG_PATH = ROOT + "/DPSDKlog.txt";
    public static final String LAST_GPS_PATH = ROOT + "/LastGPS.xml";
    private int m_loginHandle = 0;   //标记登录是否成功   1登录成功   0登录失败
    private int m_nLastError = 0;
    private Return_Value_Info_t m_ReValue = new Return_Value_Info_t();
    private boolean isSignCheck;

    public static synchronized CCApplication get() {
        return app;
    }

    @Override
    /**
     * 初始化各种数据.
     * <P>根据配置参数初始化图形加载实例。
     */
    public void onCreate() {
        super.onCreate();

        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(this);
        userStrategy.setAppChannel("正式线");
        CrashReport.initCrashReport(getApplicationContext(), "22415148a8", true, userStrategy); //bugly

        SignCheck signCheck = new SignCheck(this, "AE:CE:E4:67:06:A8:92:4C:76:04:C8:CA:E5:C6:A4:9E:9D:B5:38:F2");
        if (signCheck.check()) {
            isSignCheck = true;
        } else {
            isSignCheck = false;
        }

        //设置友盟
//        MultiDex.install(this); // 解决小米手机 混淆
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
        initPush();
        initDHMoniter();

        //班牌留言缓存路径
        File futureStudioIconFile = new File(Constants.CACHE_PATH + "video-cache");
        if (!futureStudioIconFile.exists()) {
            futureStudioIconFile.mkdir();
        }
//      Vitamio.isInitialized(getApplicationContext());
//		CrashReport.testJavaCrash();
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(this);
//		CrashReport.testJavaCrash();
//		BugtagsOptions options = new BugtagsOptions.Builder().
//				trackingLocation(true).//是否获取位置，默认 true
//				trackingCrashLog(true).//是否收集crash，默认 true
//				trackingConsoleLog(true).//是否收集console log，默认 true
//				trackingUserSteps(true).//是否收集用户操作步骤，默认 true
//				trackingNetworkURLFilter("(.*)").//自定义网络请求跟踪的 url 规则，默认 null
//				build();
//		Bugtags.start("b418932635f37f89829d9d86ddcb3622", this, Bugtags.BTGInvocationEventNone);
        app = this;
        applicationContext = this;
        dbsp = new DBSharedPreferences(this);
        File cacheDir = new File(savePath);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024)) // default
                .discCache(new UnlimitedDiscCache(cacheDir)) // default
                .discCacheSize(50 * 1024 * 1024)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //.writeDebugLogs() // Remove for release app
                .build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        //保利威视本地 SDK 初始化
        initPolyvCilent();
        if (DataUtil.isAPKDebugMode(this)) {
            strictMode();
        }

        boolean is = deleteDatabase("leavemessage.db");
        // 创建数据库对象
        setupDatabase();

        //初始化Mob
        MobSDK.init(this);

        //将用户ID作为Bugly上传的UserId
        NewMemberBean memberInfo = getMemberInfo();
        if (memberInfo != null) {
            if (!DataUtil.isNullorEmpty(memberInfo.getMobile())) {
                CrashReport.setUserId(memberInfo.getMobile());
            }
        }

        initALiChatSDK();
    }

    /**
     * 初始化阿里百川SDK
     */
    public void initALiChatSDK() {
        //必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
        SysUtil.setApplication(this);
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        }
        //第一个参数是Application Context
        //这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
        if (SysUtil.isMainProcess()) {
            //自定义聊天操作
            AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_OPERATION_POINTCUT, ChattingOperationCustomSample.class);
            //自定义聊天界面
            AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT, ChattingUICustomSample.class);
            //自定义推送
            AdviceBinder.bindAdvice(PointCutEnum.NOTIFICATION_POINTCUT, NotificationInitSampleHelper.class);
            //自定义会话列表界面
            AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_POINTCUT, ConversationListUICustomSample.class);
            //自定义会话列表操作
            AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_OPERATION_POINTCUT, ConversationListOperationCustomSample.class);

            YWAPI.init(this, Constants.BAI_CHUAN_APPKEY);
        }

        //去掉电池优化弹窗
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (-1 == sharedPreferences.getInt("IgnoreBatteryOpt", -1)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("IgnoreBatteryOpt", 1);
            editor.apply();
        }
    }


    //解决小米手机 混淆UMeng 适配问题
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public YWIMKit getIMKit() {
        if (mIMKit == null) {
            if (getMemberDetail() != null) {
                if (!DataUtil.isNullorEmpty(getMemberDetail().getHxUsername())) {
                    mIMKit = YWAPI.getIMKitInstance(getMemberDetail().getHxUsername(), Constants.BAI_CHUAN_APPKEY);
                } else {
                    System.out.println("没有百川账号");
                }
            } else {
                System.out.println("没有百川账号");
            }
        }
        return mIMKit;
    }

    public void setIMKit(YWIMKit mIMKit) {
        this.mIMKit = mIMKit;
    }

    private YWIMKit mIMKit;


    private void setupDatabase() {

        //创建数据库DB
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "leavemessage.db", null);
        //获取写数据库
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        //从数据库获取对象
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        //Dao对象管理者
        daoSession = daoMaster.newSession();

    }

    private static DaoSession daoSession;

    public static DaoSession getDaoinstant() {
        return daoSession;
    }


    private void strictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .penaltyFlashScreen()
                .build());

        try {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .setClassInstanceLimit(Class.forName("com.wcyc.zigui2"), 100)
                    .penaltyLog()
                    .build());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                System.out.println("友盟 DeviceToken:" + s);
                CCApplication.getInstance().setDeviceToken(s);
                //友盟注册后再注册环信
//                initEmobNotify();
            }

            @Override
            public void onFailure(String s, String s1) {
                System.out.println("友盟 onFailure:" + s + " :" + s1);
//                initEmobNotify();
            }
        });
        mPushAgent.setDisplayNotificationNumber(10);
        mPushAgent.setPushIntentServiceClass(UmengPushService.class);
    }

    /**
     * 初始化通知.
     * <P>先初始化EMChat实例，并获取到EMChatOptions对象。设置添加好友时，需要验证；设置收到消息是否有新消息通知；
     * 设置收到消息是否有声音提示；设置收到消息是否震动，设置语音消息播放是否设置为扬声器播放。
     * 设置一个connectionlistener监听账户是否重复登录。
     */
    private void initEmobNotify() {
        // 初始化环信SDK
        EMChat.getInstance().init(app);
        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(true);
        // 设置收到消息是否有新消息通知，默认为true
        options.setNotificationEnable(PreferenceUtils.getInstance(applicationContext).getSettingMsgNotification());
        // 设置收到消息是否有声音提示，默认为true
        options.setNoticeBySound(PreferenceUtils.getInstance(applicationContext).getSettingMsgSound());
        // 设置收到消息是否震动 默认为true
        options.setNoticedByVibrate(PreferenceUtils.getInstance(applicationContext).getSettingMsgVibrate());
        // 设置语音消息播放是否设置为扬声器播放 默认为true
        options.setUseSpeaker(PreferenceUtils.getInstance(applicationContext).getSettingMsgSpeaker());


        if (PreferenceUtils.getInstance(applicationContext).getSettingMsgNotification()) {
            options.setShowNotificationInBackgroud(true); // 默认为true
        } else {
            options.setShowNotificationInBackgroud(false);
        }

        //设置一个connectionlistener监听账户重复登录
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());

        //设置notification消息点击时，跳转的intent为自定义的intent
        options.setOnNotificationClickListener(new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                return getChatIntent(message);
            }
        });
        // 取消注释，app在后台，有新消息来时
        options.setNotifyText(new OnMessageNotifyListener() {

            @Override
            public String onNewMessageNotify(EMMessage message) {
                if (nm != null) {
                    nm.cancelAll();
                }
                // 可以根据message的类型提示不同文字
//				String id = message.getFrom();
//				TextMessageBody txtBody = (TextMessageBody) message.getBody();
                return "云智全课通发来了一条新消息";
            }

            @Override
            public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
                return "云智全课通有新的消息";
            }

            @Override
            public String onSetNotificationTitle(EMMessage message) {
                //修改标题
                return "云智全课通";
            }

//            @Override
//            public int onSetSmallIcon(EMMessage emMessage) {
//                return 0;
//            }
        });

        EMChatManager.getInstance().setChatOptions(options);
        // 注册一个接收推送消息的BroadcastReceiver
//		System.err.println("registerCmdReceiver!!!!!!");
//		cmdReceiver = new CMDMessageBroadcastReceiver();
//		IntentFilter cmdmsgFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
//		cmdmsgFilter.setPriority(SYSTEM_HIGH_PRIORITY);
//		registerReceiver(cmdReceiver,cmdmsgFilter);
        // 注册一个接收消息的BroadcastReceiver
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(1);
        registerReceiver(msgReceiver, intentFilter);

        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();
        System.err.println("setAppInited!!!!!!");
    }

    public void initReceiver() {
        System.out.println("initReceiver");
        checkService = new CheckSericeBroadcastReceiver();
        IntentFilter timeFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(checkService, timeFilter);
    }


    /**
     * 单点登录的退出操作
     * 只清除数据,不向服务器写登出日志
     */
    public void singleLogout() {
        try {
            clearData();
            logoutAliChat();
            ChatLoginService.canRun = false;
            stopReceiver();
            clearNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSignCheck() {
        return isSignCheck;
    }

    public void setSignCheck(boolean signCheck) {
        this.isSignCheck = signCheck;
    }


    /**
     * 新消息广播接收者. <P>本类主要是新消息广播接受者。
     *
     * @version 1.00
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        /**
         * 收到消息后提示未读. <P>收到消息后提示未读，实际消息内容需要到chat页面查看。
         * 当收到广播时，message已经在db和内存里，可以通过id获取mesage对象，并显示一条通知，随后主要广播。
         *
         * @param context 消息内容
         * @param intent  跳转intent
         */
        public void onReceive(Context context, Intent intent) {
            //主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
//			DataUtil.getToast("子贵家校发来了一条新消息");
            // 消息id
            String msgId = intent.getStringExtra("msgid");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            showNotification(message, R.drawable.app_icon, "子贵家校发来了一条新消息", "子贵家校", "子贵家校有新的消息");
            // 注销广播
            abortBroadcast();
        }
    }

    //检查推送消息的service是否还在运行
    private class CheckSericeBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            boolean isServiceRunning = false;
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                ActivityManager manager = (ActivityManager)
                        context.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningServiceInfo> list
                        = manager.getRunningServices(Integer.MAX_VALUE);
                for (ActivityManager.RunningServiceInfo serviceInfo : list) {
                    if (serviceInfo.service.getClassName().contains(".PushSmsService")) {
                        isServiceRunning = true;
                        break;
                    }
                }
                if (!isServiceRunning) {
                    Intent intentService = new Intent(context, PushSmsService.class);
                    startService(intentService);
                }
            }
        }
    }

    /**
     * 通知管理器
     */
    NotificationManager nm;

    /**
     * 通知id数
     */
    final int notification_id = 100;

    /**
     * 显示通知. 当新消息来的时候，显示通知。如果通知管理器NotificationManager为空，则创建通知管理器。
     * 创建通知，并设置通知在状态栏里显示的图标、内容、标记。点击通知后，转到响应的Activity。
     *
     * @param message    新消息
     * @param icon       图标id
     * @param tickertext 文本
     * @param title      标题
     * @param content    内容
     */
    @SuppressWarnings("deprecation")
    public void showNotification(EMMessage message, int icon, String tickertext, String title, String content) {
        if (nm == null) {
            nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        nm.cancelAll();

        Notification baseNF = new Notification();
        baseNF.icon = icon;    //设置通知在状态栏显示的图标
        baseNF.tickerText = tickertext; //通知时在状态栏显示的内容
        baseNF.flags |= Notification.FLAG_AUTO_CANCEL; //通知被点击后，自动消失
        PendingIntent pt = PendingIntent.getActivity(this, 0, getChatIntent(message), PendingIntent.FLAG_CANCEL_CURRENT);
        //点击通知后的动作，这里是转回main 这个Acticity
//        baseNF.setLatestEventInfo(this,title,content,pt);
        try {
            nm.notify(notification_id, baseNF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到将要跳转的intent. <p>根据消息message的聊天类型是单聊还是群聊，生成响应的intent并返回。
     *
     * @param message 状态栏里的消息
     * @return intent
     */
    public Intent getChatIntent(EMMessage message) {
        Intent intent = new Intent(applicationContext, HomeActivity.class);
        ChatType chatType = message.getChatType();
        if (message != null) {
            if (chatType == ChatType.Chat) { // 单聊信息
                String userId = message.getFrom();
                intent.putExtra("userId", userId);
                User user = getContactList().get(userId);
                if (user != null) {
                    intent.putExtra("userNick", user.getNick());
                    intent.putExtra("avatar", user.getAvatar());
                }
                intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
            } else { // 群聊信息
                // message.getTo()为群聊id
                intent.putExtra("groupId", message.getTo());
                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
            }
        }

        return intent;
    }


    /**
     * 获取类的实例.
     *
     * @return 类的实例
     */
    public static CCApplication getInstance() {
        return app;
    }

    /**
     * 得到账户信息
     *
     * @return 账户信息
     */
    public NewMemberBean getMemberInfo() {
        if (memberInfo == null) {
            String member = dbsp.getString("member");
            if (!DataUtil.isNull(member)) {
                memberInfo = JsonUtils.fromJson(member, NewMemberBean.class);
            }
        }
        return memberInfo;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceToken(String deviceToken) {
        if (!DataUtil.isNullorEmpty(deviceToken)) {
            dbsp.putString("deviceToken", deviceToken);
        }
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken() {
        if (DataUtil.isNullorEmpty(deviceToken)) {
            deviceToken = dbsp.getString("deviceToken");
        }
        return deviceToken;
    }

    public MemberDetailBean getMemberDetail() {
        if (detail == null) {
            String member = dbsp.getString("memberDetail");
            if (!DataUtil.isNull(member)) {
                detail = JsonUtils.fromJson(member, MemberDetailBean.class);
            }
        }
        return detail;
    }

    /**
     * 设置账户信息. 如果账户对象的json字符串不为空，则设置账户的信息；否则，设置账户的信息为空。
     *
     * @param data 账户对象的json字符串
     */
    public void setMemberInfo(String data) {
        if (data != null) {
            memberInfo = JsonUtils.fromJson(data, NewMemberBean.class);
        } else {
            memberInfo = null;
        }

        dbsp.putString("member", data);
    }

    public void setMemberInfo(NewMemberBean member) {
        if (member != null) {
            memberInfo = member;
        }
    }

    public void setMemberDetail(String data) {
        if (data != null) {
            detail = JsonUtils.fromJson(data, MemberDetailBean.class);
        } else {
            detail = null;
        }

        dbsp.putString("memberDetail", data);
    }

    public void setNotice(NewNoticeBean notice) {
        if (notice != null) {
            Gson gson = new Gson();
            String data = gson.toJson(notice);
            dbsp.putString("notice", data);
        } else {
            dbsp.putString("notice", null);
        }
        this.notice = notice;
    }

    public void setDailyRecord(DailyRecordBean dailyRecord) {
        if (dailyRecord != null) {
            Gson gson = new Gson();
            String data = gson.toJson(dailyRecord);
            dbsp.putString("dailyRecord", data);
        } else {
            dbsp.putString("dailyRecord", null);
        }
        this.dailyRecord = dailyRecord;
    }

    public void setSummary(SummaryBean summary) {
        if (summary != null) {
            Gson gson = new Gson();
            String data = gson.toJson(summary);
            dbsp.putString("summary", data);
        } else {
            dbsp.putString("summary", null);
        }
        this.summary = summary;
    }

    public void setNotice(String data) {
        if (data != null) {
            notice = JsonUtils.fromJson(data, NewNoticeBean.class);
        } else {
            notice = null;
        }
        dbsp.putString("notice", data);
    }

    public void addNotice(NewNoticeBean notice) {
        if (notice == null) return;
        int originPageNum;
        if (this.notice != null
                && (originPageNum = this.notice.getPageNum()) > 0) {
            NewNoticeBean join;
            int pageNum = notice.getPageNum();
            int totalPage = notice.getTotalPageNum();

            int originTotalPage = this.notice.getTotalPageNum();
            System.out.println("pageNum:" + pageNum + " totalPage:" + totalPage +
                    " originPageNum:" + originPageNum + "originTotalPage:" + originTotalPage);
            if (pageNum > originPageNum) {//加载更多，插入列表后面
//				this.notice.getNoticeList().addAll(notice.getNoticeList());
                List<NoticeDetail> allList = modifyNotice(notice.getNoticeList(), this.notice.getNoticeList());
                this.notice.getNoticeList().addAll(allList);
                this.notice.setPageNum(pageNum);
                this.notice.setPageSize(totalPage);
            } else {
//				if(totalPage > originTotalPage){//更新时有新的通知，插入列表前面
//					join = notice;
//					notice.getNoticeList().addAll(this.notice.getNoticeList());
//					notice.setPageSize(totalPage);
//					this.notice = notice;
//				}else{//更新时有新的通知，插入列表前面
                List<NoticeDetail> allList = new ArrayList<NoticeDetail>();
                List<NoticeDetail> list = notice.getNoticeList();
                List<NoticeDetail> oldList = this.notice.getNoticeList();
                if (oldList != null) {
                    allList = modifyNotice(list, oldList);
                    allList.addAll(oldList);
                    notice.setNoticeList(allList);
                }
                this.notice = notice;
//				}
            }
        } else {
            this.notice = notice;
        }
        Gson gson = new Gson();
        String data = gson.toJson(this.notice);
        dbsp.putString("notice", data);
    }

    public List<NoticeDetail> modifyNotice(List<NoticeDetail> list, List<NoticeDetail> oldList) {
        boolean isExist = false;
        List<NoticeDetail> allList = new ArrayList<NoticeDetail>();
        for (NoticeDetail item : list) {
            if (oldList != null) {
                for (NoticeDetail oldItem : oldList) {
                    if (item.getNoticeId() == oldItem.getNoticeId()) {
                        isExist = true;
                        break;
                    }
                }
            }
            if (isExist == false) {
                allList.add(item);
            } else {
                break;
            }
        }
        return allList;
    }


    /**
     * 保存  和获取 该学校 所有年级的所有班级
     */
    public void setSchoolAllClassList(List<NewClasses> schoolAllClassList) {
        if (schoolAllClassList != null) {
            Gson gson = new Gson();
            String data = gson.toJson(schoolAllClassList);
            dbsp.putString("schoolAllClassList", data);
        } else {
            dbsp.putString("schoolAllClassList", null);
        }
        this.schoolAllClassList = schoolAllClassList;
    }

    public List<NewClasses> getSchoolAllClassList() {
        if (schoolAllClassList == null) {
            try {
                String data = dbsp.getString("schoolAllClassList");
                if (!DataUtil.isNull(data)) {
                    schoolAllClassList = new ArrayList<NewClasses>();
                    JSONArray json = new JSONArray(data);
                    for (int i = 0; i < json.length(); i++) {
                        NewClasses newClasses = JsonUtils.fromJson(json.get(i).toString(),
                                NewClasses.class);
                        schoolAllClassList.add(newClasses);
                    }
                }

            } catch (Exception e) {
            }
        }
        return schoolAllClassList;
    }

    //保存所有班級年級
    public void setAllGradeClass(AllGradeClass allGradeClass) {
        if (allGradeClass != null) {
            Gson gson = new Gson();
            String data = gson.toJson(allGradeClass);
            dbsp.putString("allGradeClass", data);
        } else {
            dbsp.putString("allGradeClass", null);
        }
        this.allGradeClass = allGradeClass;
    }

    public AllGradeClass getAllGradeClass() {
        if (allGradeClass != null) {
            return allGradeClass;
        } else {
            String data = dbsp.getString("allGradeClass");
            if (!DataUtil.isNullorEmpty(data)) {
                allGradeClass = JsonUtils.fromJson(data, AllGradeClass.class);
            }
        }
        return allGradeClass;
    }

    public void setAllTeacherList(AllTeacherList teacherList) {
        if (teacherList != null) {
            Gson gson = new Gson();
            String data = gson.toJson(teacherList);
            dbsp.putString("allTeacher", data);
        } else {
            dbsp.putString("allTeacher", null);
        }
        this.teacherList = teacherList;
    }

    public AllTeacherList getAllTeacherList() {
        if (teacherList != null) {
            return teacherList;
        } else {
            String data = dbsp.getString("allTeacher");
            if (!DataUtil.isNullorEmpty(data)) {
                teacherList = JsonUtils.fromJson(data, AllTeacherList.class);
            }
            if (teacherList == null) {
                DataUtil.getToastShort("网络有点慢,请稍等");

            }
        }
        return teacherList;
    }

    public void addDailyRecord(DailyRecordBean dailyRecord) {
        if (dailyRecord == null) return;
        if (this.dailyRecord != null && this.dailyRecord.getDailyList() != null) {
            int pageNum = dailyRecord.getPageNum();
            int totalPage = dailyRecord.getTotalPageNum();
            int originPageNum = this.dailyRecord.getPageNum();
            int originTotalPage = this.dailyRecord.getTotalPageNum();
            System.out.println("pageNum:" + pageNum + " totalPage:" + totalPage +
                    " originPageNum:" + originPageNum + "originTotalPage:" + originTotalPage);
            if (pageNum > originPageNum) {//加载更多，插入列表后面
                List<DailyRecordDetail> allList =
                        modifyDailyRecord(dailyRecord.getDailyList(), this.dailyRecord.getDailyList());
                if (allList == null) return;
                this.dailyRecord.getDailyList().addAll(allList);
                this.dailyRecord.setPageNum(pageNum);
                this.dailyRecord.setPageSize(totalPage);
            } else {
                List<DailyRecordDetail> allList = new ArrayList<DailyRecordDetail>();
                List<DailyRecordDetail> list = dailyRecord.getDailyList();
                List<DailyRecordDetail> oldList = this.dailyRecord.getDailyList();
                if (oldList != null) {
                    allList = modifyDailyRecord(list, oldList);
                    if (allList != null) {
                        allList.addAll(oldList);
                    }
                    dailyRecord.setDailyList(allList);
                }

                this.dailyRecord = dailyRecord;
            }
        } else {
            this.dailyRecord = dailyRecord;
        }
        Gson gson = new Gson();
        String data = gson.toJson(this.dailyRecord);
        dbsp.putString("dailyRecord", data);
    }

    public List<DailyRecordDetail> modifyDailyRecord(List<DailyRecordDetail> list, List<DailyRecordDetail> oldList) {
        boolean isExist = false;
        if (list == null) return null;
        List<DailyRecordDetail> allList = new ArrayList<DailyRecordDetail>();
        for (DailyRecordDetail item : list) {
            if (oldList != null) {
                for (DailyRecordDetail oldItem : oldList) {
                    if (item.getDailyId().equals(oldItem.getDailyId())) {
                        isExist = true;
                        break;
                    }
                }
            }
            if (isExist == false) {
                allList.add(item);
            } else {
                break;
            }
        }
        return allList;
    }

    public void addSummary(SummaryBean summary) {
        if (summary == null) return;
        int originPageNum;
        if (this.summary != null
                && this.summary.getSummaryList() != null) {
            int pageNum = summary.getPageNum();
            int totalPage = summary.getTotalPageNum();
            originPageNum = this.summary.getPageNum();
            int originTotalPage = this.summary.getTotalPageNum();
            System.out.println("pageNum:" + pageNum + " totalPage:" + totalPage +
                    " originPageNum:" + originPageNum + "originTotalPage:" + originTotalPage);
            if (pageNum > originPageNum) {//加载更多，插入列表后面
                List<SummaryDetail> allList =
                        modifySummary(summary.getSummaryList(), this.summary.getSummaryList());
                if (allList == null) return;
                this.summary.getSummaryList().addAll(allList);
                this.summary.setPageNum(pageNum);
                this.summary.setPageSize(totalPage);
            } else {
                List<SummaryDetail> allList = new ArrayList<SummaryDetail>();
                List<SummaryDetail> list = summary.getSummaryList();
                List<SummaryDetail> oldList = this.summary.getSummaryList();
                if (oldList != null) {
                    allList = modifySummary(list, oldList);
                    if (allList != null) {
                        allList.addAll(oldList);
                    }
                    summary.setSummaryList(allList);
                }
                this.summary = summary;
            }
        } else {
            this.summary = summary;
        }
        Gson gson = new Gson();
        String data = gson.toJson(this.summary);
        dbsp.putString("summary", data);
    }

    //修改已存在的项
    public List<SummaryDetail> modifySummary(List<SummaryDetail> list, List<SummaryDetail> oldList) {
        boolean isExist = false;
        if (list == null) return null;
        List<SummaryDetail> allList = new ArrayList<SummaryDetail>();
        for (SummaryDetail item : list) {
            if (oldList != null) {
                for (SummaryDetail oldItem : oldList) {
                    if (item.getSummaryId().equals(oldItem.getSummaryId())) {
                        isExist = true;
//						oldItem = item;
                        break;
                    }
                }
            }
            if (isExist == false) {
                allList.add(item);
            } else {
                break;
            }
        }
        return allList;
    }

    public void addEmail(NewEmailBean email, String type) {
        if (email == null) return;

        NewEmailBean oriEmail = null;
        if (type.equals("inbox")) {
            oriEmail = inbox;
        } else if (type.equals("outbox")) {
            oriEmail = outbox;
        } else if (type.equals("draft")) {
            oriEmail = draft;
        } else if (type.equals("recycle")) {
            oriEmail = recycle;
        }
        if (oriEmail != null) {
            int pageNum = email.getPageNum();
            int totalPage = email.getTotalPageNum();
            int originPageNum = oriEmail.getPageNum();
            int originTotalPage = oriEmail.getTotalPageNum();
            System.out.println("pageNum:" + pageNum + " totalPage:" + totalPage +
                    " originPageNum:" + originPageNum + "originTotalPage:" + originTotalPage);
            if (pageNum > originPageNum) {//加载更多，插入列表后面
                List<NewMailInfo> allList =
                        modifyEmail(email.getMailInfoList(), oriEmail.getMailInfoList());
                if (allList == null) return;
                if (oriEmail.getMailInfoList() != null)
                    oriEmail.getMailInfoList().addAll(allList);
                else {
                    oriEmail.setMailInfoList(allList);
                }
                oriEmail.setPageNum(pageNum);
                oriEmail.setPageSize(totalPage);
            } else {
                List<NewMailInfo> allList = new ArrayList<NewMailInfo>();
                List<NewMailInfo> list = email.getMailInfoList();
                List<NewMailInfo> oldList = oriEmail.getMailInfoList();
                if (oldList != null && list != null) {
                    allList = modifyEmail(list, oldList);
                    allList.addAll(oldList);
                    email.setMailInfoList(allList);
                }
                oriEmail = email;
            }
        } else {
            oriEmail = email;
        }
        if (type.equals("inbox")) {
            inbox = oriEmail;
        } else if (type.equals("outbox")) {
            outbox = oriEmail;
        } else if (type.equals("draft")) {
            draft = oriEmail;
        } else if (type.equals("recycle")) {
            recycle = oriEmail;
        }
        Gson gson = new Gson();
        String data = gson.toJson(oriEmail);
        dbsp.putString(type, data);
    }

    private List<NewMailInfo> modifyEmail(List<NewMailInfo> list, List<NewMailInfo> oldList) {
        boolean isExist = false;
        if (list == null) return null;
        List<NewMailInfo> allList = new ArrayList<NewMailInfo>();
        for (NewMailInfo item : list) {
            if (oldList != null) {
                for (NewMailInfo oldItem : oldList) {
                    if (item.getId().equals(oldItem.getId())) {
                        isExist = true;
//						oldItem = item;
                        break;
                    }
                }
            }
            if (isExist == false) {
                allList.add(item);
            } else {
                break;
            }
        }
        return allList;
    }

    public NewNoticeBean getNotice() {
        if (notice == null) {
            String data = dbsp.getString("notice");
            if (!DataUtil.isNull(data)) {
                notice = JsonUtils.fromJson(data, NewNoticeBean.class);
            }
        }
        return notice;
    }

    public DailyRecordBean getDailyRecord() {
        if (dailyRecord == null) {
            String data = dbsp.getString("dailyRecord");
            if (!DataUtil.isNull(data)) {
                dailyRecord = JsonUtils.fromJson(data, DailyRecordBean.class);
            }
        }
        return dailyRecord;
    }

    public SummaryBean getSummary() {
        if (summary == null) {
            String data = dbsp.getString("summary");
            if (!DataUtil.isNull(data)) {
                summary = JsonUtils.fromJson(data, SummaryBean.class);
            }
        }
        return summary;
    }

    public NewEmailBean getEmail(String type) {
        NewEmailBean email = null;
        if (type.equals("inbox")) {
            email = inbox;
        } else if (type.equals("outbox")) {
            email = outbox;
        } else if (type.equals("draft")) {
            email = draft;
        } else if (type.equals("recycle")) {
            email = recycle;
        }
        if (email == null) {
            String data = dbsp.getString(type);
            if (!DataUtil.isNull(data)) {
                email = JsonUtils.fromJson(data, NewEmailBean.class);
            }
        }
        return email;
    }

    public void setEmail(NewEmailBean email, String type) {
        if (type.equals("inbox")) {
            inbox = email;
        } else if (type.equals("outbox")) {
            outbox = email;
        } else if (type.equals("draft")) {
            draft = email;
        } else if (type.equals("recycle")) {
            recycle = email;
        }

        if (email != null) {
            Gson gson = new Gson();
            String data = gson.toJson(email);
            dbsp.putString(type, data);
        }
    }

    public UserType getPresentUser() {
        if (presentUser == null) {
            if (memberInfo != null) {
                if (memberInfo.getUserTypeList() != null)
                    presentUser = memberInfo.getUserTypeList().get(curUserIndex);
            }
        }
        return presentUser;
    }

    public UserType getUser(int index) {
        if (memberInfo != null) {
            if (memberInfo.getUserTypeList() != null)
                return memberInfo.getUserTypeList().get(index);
        }
        return null;
    }

    public void setPresentUser(UserType presentUser) {
        this.presentUser = presentUser;
    }

    public void setPresentUser(int curUserIndex) {
        this.curUserIndex = curUserIndex;
        if (memberInfo != null) {
            if (memberInfo.getUserTypeList() != null)
                presentUser = memberInfo.getUserTypeList().get(curUserIndex);
        }
    }

    public int getPresentUserIndex() {
        return curUserIndex;
    }

    public boolean isCurUserParent() {
        if (CCApplication.app.getPresentUser() != null)
            return Constants.PARENT_STR_TYPE.equals(CCApplication.app.getPresentUser().getUserType());
        return false;
    }

    //是否为班主任
    public boolean isCurUserClassAdmin() {
        boolean isParent = isCurUserParent();
        if (isParent) {
            return false;
        }
        if (detail != null) {

            List<NewClasses> classes = detail.getClassList();
            if (classes != null) {
                for (NewClasses newClass : classes) {
                    if ("1".equals(newClass.getIsAdviser())) {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    //是否是部门负责人
    public boolean isDeptAdmin() {
        if (detail != null) {
            List<Role> list = detail.getRoleList();
            if (list != null) {
                for (Role item : list) {
                    if (Constants.DEPT_ADMIN.equals(item.getRoleCode())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isSchoolAdmin() {
        if (detail != null) {
            List<Role> roles = detail.getRoleList();
            if (roles != null) {
                for (Role item : roles) {
                    if (item != null) {
                        if (Constants.SCHOOL_ADMIN.equals(item.getRoleCode())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 设置成员.
     *
     * @param member 保存的MemberBean
     */
    public void setMember(NewMemberBean member) {
        if (member != null) {
            sortUserType(member);//老师身份排第一个
            memberInfo = member;
            Gson gson = new Gson();
            String data = gson.toJson(member);
            dbsp.putString("member", data);

        }
    }

    private void sortUserType(NewMemberBean member) {
        List<UserType> list = member.getUserTypeList();
        List<UserType> sortList = new ArrayList<UserType>();
        int index = 0;
        boolean hasTeacher = false;//是否有老师身份
        if (list.size() > 1) {
            for (UserType user : list) {
                if (Constants.TEACHER_STR_TYPE.equals(user.getUserType())) {
                    hasTeacher = true;
                    break;
                }
                index++;
            }
        }
        if (index > 0 && hasTeacher) {
            UserType firstUser = list.get(0);
            UserType teacher = list.get(index);
            teacher.setIschecked(true);

            sortList.add(teacher);
            for (int i = 1; i < index; i++) {
                sortList.add(list.get(i));
            }
            sortList.add(firstUser);
            if (index < list.size()) {
                for (int i = index + 1; i < list.size(); i++) {
                    sortList.add(list.get(i));
                }
            }
            member.setUserTypeList(sortList);
        }
    }

    public void setMemberDetail(MemberDetailBean detail) {
        if (detail != null) {
            this.detail = detail;
            Gson gson = new Gson();
            String data = gson.toJson(detail);
            dbsp.putString("memberDetail", data);
        }
    }

    /**
     * 设置用户头像.
     *
     * @param icon 头像地址
     */
    public void setUserIcon(String icon) {
        memberInfo = getMemberInfo();
        if (memberInfo != null) {
            //memberInfo.setUserIconURL(icon);
            setMember(memberInfo);
        }
    }

    public void setMessage(NewMessageBean message) {
        if (message != null) {
            Gson gson = new Gson();
            this.message = message;
            String data = gson.toJson(message);
            dbsp.putString("message", data);
        }
    }

    public NewMessageBean getMessage() {
        if (message == null) {
            String data = dbsp.getString("message");
            if (!DataUtil.isNull(data)) {
                message = JsonUtils.fromJson(data, NewMessageBean.class);
            }
        }
        return message;
    }

    public void setMessageList(NewMessageListBean messageList) {
        if (messageList != null) {
            Gson gson = new Gson();
            this.messageList = messageList;
            try {
                String data = gson.toJson(messageList);
                dbsp.putString("messageList", data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            dbsp.putString("messageList", null);
        }
        this.messageList = messageList;
    }


    public void addMessageList(NewMessageListBean messageList) {
        if (messageList == null) return;
        if (this.messageList != null) {
            List<NewMessageBean> list = messageList.getMessageList();
            for (NewMessageBean item : list) {
                this.messageList.getMessageList().add(item);
            }
        } else {
            this.messageList = messageList;
        }
        Gson gson = new Gson();
        String data = gson.toJson(this.messageList);
        dbsp.putString("messageList", data);
    }

    public NewMessageListBean getMessageList() {
        if (messageList == null) {
            String data = dbsp.getString("messageList");
            if (!DataUtil.isNull(data)) {
                messageList = JsonUtils.fromJson(data, NewMessageListBean.class);
            }
        }
        return messageList;
    }

    public void setModelRemindList(ModelRemindList remindList) {
        if (remindList != null) {
            Gson gson = new Gson();
            this.remindList = remindList;
            String data = gson.toJson(remindList);
            dbsp.putString("remindList", data);

        }
    }

    //获取模块的消息数
    public ModelRemindList getModelRemindList() {
        if (remindList == null) {
            String data = dbsp.getString("remindList");
            if (!DataUtil.isNull(data)) {
                remindList = JsonUtils.fromJson(data, ModelRemindList.class);
            }
        }
        return remindList;
    }

    public void setAllContactList(AllContactListBean AllContactList) {
        if (AllContactList != null) {
            Gson gson = new Gson();
            String data = gson.toJson(AllContactList);
            dbsp.putString("contactList", data);
        } else {
            dbsp.putString("contactList", null);
        }
        this.AllContactList = AllContactList;
    }

    public AllContactListBean getAllContactList() {
        if (AllContactList == null) {
            String data = dbsp.getString("contactList");
            if (!DataUtil.isNull(data)) {
                AllContactList = JsonUtils.fromJson(data, AllContactListBean.class);
            }
        }
        return AllContactList;
    }

    public void setServiceExpiredInfo(ServiceExpiredBean service) {
        if (service != null) {
            serviceInfo = service;
            Gson gson = new Gson();
            String data = gson.toJson(service);
            dbsp.putString("serviceInfo", data);
        }
    }

    public ServiceExpiredBean getServiceExpiredInfo() {
        if (serviceInfo != null) {
            return serviceInfo;
        }
        String data = dbsp.getString("serviceInfo");
        if (!DataUtil.isNull(data)) {
            serviceInfo = JsonUtils.fromJson(data, ServiceExpiredBean.class);
        }

        return serviceInfo;
    }

    //套餐充值记录
    public void setProductInfo(ChargeProduct product) {
        if (product != null) {
            chargeProduct = product;
            Gson gson = new Gson();
            String data = gson.toJson(product);
            dbsp.putString("chargeProduct", data);
        }
    }

    public ChargeProduct getProductInfo() {
        if (chargeProduct != null) {
            return chargeProduct;
        }
        String data = dbsp.getString("chargeProduct");
        if (!DataUtil.isNull(data)) {
            chargeProduct = JsonUtils.fromJson(data, ChargeProduct.class);
        }
        return chargeProduct;
    }

    public void setMoniterInfo(MoniterListInfo info) {
        if (info != null) {
            this.info = info;
            Gson gson = new Gson();
            String data = gson.toJson(info);
            dbsp.putString("moniterInfo", data);
        }
    }

    public MoniterListInfo getMoniterInfo() {
        if (info != null) {
            return info;
        }
        String data = dbsp.getString("moniterInfo");
        if (!DataUtil.isNull(data)) {
            info = JsonUtils.fromJson(data, MoniterListInfo.class);
        }
        return info;
    }

    public void setMenuConfig(MenuConfigBean menuConfig) {
        if (menuConfig != null) {
            this.menuConfig = menuConfig;
            Gson gson = new Gson();
            String data = gson.toJson(menuConfig);
            dbsp.putString(menuConfig.getClass().getName(), data);
        }
    }

    public MenuConfigBean getMenuConfig() {
        if (menuConfig != null) {
            return menuConfig;
        }
        String data = dbsp.getString(MenuConfigBean.class.getName());
        if (!DataUtil.isNullorEmpty(data)) {
            menuConfig = JsonUtils.fromJson(data, MenuConfigBean.class);
        }
        return menuConfig;
    }

    public ContactsList getCurrentUserContactInfo() {
        return contact;
    }

    public void setCurrentUserContactInfo(ContactsList contact) {
        this.contact = contact;
    }

    /**
     * 获取内存中的好友列表. <p>
     * 如果用户名不为空且好友列表为空，则获取本地好友列表到内存。
     *
     * @return Map&lt;String, User&gt;形式的好友列表
     */
    public Map<String, User> getContactList() {
        if (getUserName() != null && contactList == null) {
            UserDao dao = new UserDao(applicationContext);
            // 获取本地好友user list到内存,方便以后获取好友list
            contactList = dao.getContactList();
        }

        return contactList;
    }

    /**
     * 设置好友列表到内存中.
     *
     * @param contactList 好友列表
     */
    public void setContactList(Map<String, User> contactList) {
        this.contactList = contactList;
    }

    /**
     * 设置陌生人列表到内存中. （暂未实现）
     *
     * @param strangerList 陌生人列表
     */
    public void setStrangerList(Map<String, User> strangerList) {

    }

    /**
     * 获取当前登录环信用户名.
     *
     * @return 当前登录的用户名
     */
    public String getUserName() {
        if (userName == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            userName = preferences.getString(PREF_USERNAME, null);
        }

        return userName;
    }

    /**
     * 获取当前登录环信用户的密码.
     *
     * @return 当前登录用户的密码
     */
    public String getPassword() {
        if (password == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            password = preferences.getString(PREF_PWD, null);
        }

        return password;
    }

    /**
     * 设置环信用户名.
     *
     * @param username 需要设定的用户名
     */
    public void setUserName(String username) {
        if (username != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            SharedPreferences.Editor editor = preferences.edit();
            if (editor.putString(PREF_USERNAME, username).commit()) {
                userName = username;
            }
        }
    }

    /**
     * 设置环信密码.
     *
     * @param pwd 待设定的密码
     */
    public void setPassword(String pwd) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_PWD, pwd).commit()) {
            password = pwd;
        }
    }

    public void setHxGroupList(EasemobGroupInfo groupInfo) {
        if (groupInfo != null) {
            this.groupInfo = groupInfo;
            Gson gson = new Gson();
            String data = gson.toJson(groupInfo);
            dbsp.putString("groupInfo", data);
        }
    }

    public EasemobGroupInfo getHxGroupList() {
        if (groupInfo != null) {
            return groupInfo;
        }
        String data = dbsp.getString("groupInfo");
        if (!DataUtil.isNull(data)) {
            groupInfo = JsonUtils.fromJson(data, EasemobGroupInfo.class);
        }
        return groupInfo;
    }

    /**
     * 得到电话号码.
     *
     * @return 用户号码
     */
    public String getPhoneNum() {
        SharedPreferences sp = getSharedPreferences("little_data", 1);
        return sp.getString("phoneNum", "");
    }

    /**
     * 得到保存的用户密码.
     *
     * @return 用户密码
     */
    public String getPhonePwd() {
        SharedPreferences sp = getSharedPreferences("little_data", 1);
        return sp.getString("phonePwd", "");
//        return "123456";
    }

    /**
     * 保存账号
     */
    public void savePhoneNum(String phoneNum) {
        SharedPreferences sp = getSharedPreferences("little_data", 1);
        Editor editor = sp.edit();
        editor.putString("phoneNum", phoneNum);
        editor.apply();
    }

    /**
     * 保存密码.
     */
    public void savePhonePwd(String phonePwd) {
        SharedPreferences sp = getSharedPreferences("little_data", 1);
        Editor editor = sp.edit();
//		editor.putString("phoneNum", phoneNum);
        editor.putString("phonePwd", phonePwd);
        editor.apply();
    }

    //	设置班级动态最新的更新图片地址
    public void setClassDynUpdate(String url) {
        dbsp.putString("imgUrl", url);
    }

    public String getClassDynUpdate() {
        return dbsp.getString("imgUrl");
    }

    /**
     * 退出登录. 先调用{@link com.easemob.chat.EMChatManager EMChatManager}的
     * {@link com.easemob.chat.EMChatManager#logout() logout()}
     * 退出登录，关闭数据库连接，然后清空密码、联系列表、个人信息等数据。
     */
    public void logout() {
//		 先调用sdk logout，在清理app中自己的数据
        try {

            //退出百川
            logoutAliChat();

            //退出环信
//            logoutHx();

            ChatLoginService.canRun = false;
            logoutOnServer();
            stopReceiver();

            clearNotification();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 清除推送栏信息
     * 避免退出登录之后 状态栏还有历史消息  当点击消息时候 跳转到相应的界面 会报错
     */
    public void clearNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

    public void logoutHx() {
        if (getUserName() != null && getPassword() != null) {
            System.out.println("环信logout!!!");
            EMChatManager.getInstance().logout();
        }
    }

    /**
     * 退出阿里百川
     */
    public void logoutAliChat() {
        if (getUserName() != null && getPassword() != null) {
            YWIMKit imKit = CCApplication.getInstance().getIMKit();
            IYWLoginService loginService = imKit.getLoginService();
            loginService.logout(new IWxCallback() {
                @Override
                public void onSuccess(Object... objects) {
                    System.out.println("百川退出登录成功!");
                    CCApplication.getInstance().setIMKit(null);
                }

                @Override
                public void onError(int i, String s) {
                    System.out.println("百川退出登录失败!");
                }

                @Override
                public void onProgress(int i) {

                }
            });
        }
    }

    public void stopReceiver() {
        if (checkService != null) {
            unregisterReceiver(checkService);
            checkService = null;
        }
        if (msgReceiver != null) {
            unregisterReceiver(msgReceiver);
            msgReceiver = null;
        }
//		if(cmdReceiver != null){
//			System.out.println("unregisterCmdReceiver");
//			unregisterReceiver(cmdReceiver);
//			cmdReceiver = null;
//		}
    }

    /**
     * 清除数据
     */
    public void clearData() {
        System.out.println("clearData!!!");
        DbOpenHelper.getInstance(applicationContext).myclose();
        // reset password to null
        setUserName(null);
        setPassword(null);
        setContactList(null);
        setMemberInfo((String) null);
        setPresentUser(null);
        detail = null;
        message = null;
        messageList = null;
        remindList = null;
        AllContactList = null;
        serviceInfo = null;
        notice = null;
        dailyRecord = null;
        summary = null;
        inbox = null;
        outbox = null;
        draft = null;
        recycle = null;
        schoolAllClassList = null;
        curUserIndex = 0;
        info = null;
        groupInfo = null;
        dbsp.clear();
        //删除自己存储的数据
        SharedPreferences sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        LocalUtil.clearData();
        DataUtil.cleanCacheFile();
        //数据库的信息
        try {
            daoSession.getLeaveMessageDao().deleteAll();
            daoSession.getChildMessageDao().deleteAll();
        } catch (Exception e) {

        }

        //	DataUtil.cleanWebViewCacheFile();
    }

    class MyConnectionListener implements ConnectionListener {
        @Override
        public void onReConnecting() {

        }

        @Override
        public void onReConnected() {

        }

        @Override
        public void onDisConnected(String errorString) {
            if (errorString != null && errorString.contains("conflict")) {
                System.out.println("errorString:" + errorString);
                logout();
                finishAllActivity();
                Intent intent = new Intent(applicationContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("conflict", true);
                startActivity(intent);
            }
        }

        @Override
        public void onConnecting(String progress) {

        }

        @Override
        public void onConnected() {

        }
    }

    /**
     * 增加activity
     *
     * @param activity activity对象
     */
    public void addActivity(Activity activity) {
        if (!isDeling) activityList.add(activity);
    }

    /**
     * 移除activity
     *
     * @param activity activity对象
     */
    public void removeActivity(Activity activity) {
        if (!isDeling) activityList.remove(activity);
    }

    /**
     * 当正在调用finishAllActivity方法的时候不能进行操作
     */
    private boolean isDeling = false;

    /**
     * 关闭所有的activity,如果是要跳转，必须保证在调用该方法后新开一个activity，如果直接调用会关闭掉
     * 所有的页面，包括调用这个方法的页面
     */
    public void finishAllActivity() {
        isDeling = true;
        for (Activity activity : activityList) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityList.clear();
        isDeling = false;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        setContactList(null);
    }

    //该功能是否可以使用
    public boolean CouldFunctionBeUse(String functionName) {
        UserType userType = getPresentUser();
        if (userType != null && Constants.TEACHER_STR_TYPE.endsWith(userType.getUserType())) {
            return true;
        }
        if ("通知".equals(functionName)
                || "请假条".equals(functionName)
                || "请假申请".equals(functionName)
                || "给校长写信".equals(functionName)
                || "邮件".equals(functionName)
                || "课程表".equals(functionName)
                || "校长信箱".equals(functionName)
                || "校历".equals(functionName)
                || "作息时间".equals(functionName)
                || "子贵课堂".equals(functionName)
                || "家长学校".equals(functionName)
                //微课网和小学资源是在模块里面判断是否收费
                || "微课网".equals(functionName)
                || "小学资源".equals(functionName)
                || "校园新闻".equals(functionName)
                || "教育资讯".equals(functionName)) {
            return true;
        }
        return isServiceExpired(functionName, 0);
    }

    //该菜单是否可以使用
    public boolean CouldFunctionBeUseFromConfig(String functionName, int b) {
        UserType userType = getPresentUser();
        if (userType != null && Constants.TEACHER_STR_TYPE.endsWith(userType.getUserType())) {
            return true;
        }
        //微课网和小学资源是在模块里面判断是否收费
        if (MenuItem.WEIKE.equals(functionName)
                || MenuItem.PRIMARYSCHOOL.equals(functionName)
                || MenuItem.COURSE.equals(functionName)) {
            return true;
        }
        boolean isFree = true;
        MenuConfigBean config = CCApplication.getInstance().getMenuConfig();
        if (config.getPersonalConfigList() != null) {
            List<MenuConfigBean.MenuConfig> list = config.getPersonalConfigList();
            //删除 type=4 其他应用的子贵探视
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                MenuConfigBean.MenuConfig menuConfig = (MenuConfigBean.MenuConfig) iterator.next();
                if (menuConfig.getFunctionName().equals("子贵探视")) {
                    if (menuConfig.getType().equals("4")) {
                        iterator.remove();
                    }
                }
            }
            if (list != null) {
                for (MenuConfigBean.MenuConfig item : list) {
                    if (item != null && functionName.equals(item.getFunctionName())) {
                        if ("1".equals(item.getType())) {
                            isFree = true;
                        } else {
                            isFree = false;
                        }
                    }
                }
            }
        }
        if (isFree) {
            return true;
        } else {
            return isServiceExpired(functionName, b);
        }
    }

    public boolean isServiceExpired(String functionName, int b) {
        MenuAdapter.b = 2;
        String kind = getServiceKind(functionName); //子贵探视 返回id 为1
        ServiceExpiredBean serviceinfo = getServiceExpiredInfo();
        if (serviceinfo != null) {
            List<ServiceInfo> infoList = serviceinfo.getServiceList();
            if (infoList != null) {
                for (ServiceInfo item : infoList) {
                    if (kind.equals(item.getServiceId())) {
                        if ("1".equals(item.getServiceExpired())) {//正常
                            MenuAdapter.b = 1;
                            return true;
                        }

                        if ("0".equals(item.getServiceExpired())) {
                            MenuAdapter.b = 0;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isServiceExpired(String functionName) {

        String kind = getServiceKind(functionName);
        ServiceExpiredBean serviceinfo = getServiceExpiredInfo();
        if (serviceinfo != null) {
            List<ServiceInfo> infoList = serviceinfo.getServiceList();
            if (infoList != null) {
                for (ServiceInfo item : infoList) {
                    if (kind.equals(item.getServiceId())) {
                        if ("1".equals(item.getServiceExpired())) {//正常

                            return true;
                        }

                        if ("0".equals(item.getServiceExpired())) {

                        }
                    }
                }
            }
        }
        return false;
    }

    public String getServiceExpired(String functionName) {
        String kind = getServiceKind(functionName);
        ServiceExpiredBean serviceinfo = getServiceExpiredInfo();
        if (serviceinfo != null) {
            List<ServiceInfo> infoList = serviceinfo.getServiceList();
            if (infoList != null) {
                for (ServiceInfo item : infoList) {
                    if (kind.equals(item.getServiceId())) {
                        return item.getServiceExpired();
                    }
                }
            }
        }
        return "";
    }

    //能够删除模块所有未读数__ 点击Item就可以删除相应Item上的个数
    public boolean couldClearRemind(String typeName) {
        String[] func = {"作业", "考勤", "点评", "值班查询", "校长信箱", MenuItem.CONSUME, "班牌留言",
                "人工考勤", "宿舍考勤", "进出校考勤", "校车考勤"};
        if (typeName == null) return false;
        for (String str : func) {
            if (str.equals(typeName) || str.contains(typeName)) {
                return true;
            }
        }
        return false;
    }

    //需要支付才可以查看___支付了之后需要确认
    public String getServiceKind(String functionName) {
        String[] service = {"通知", "邮件", "作业", "考勤", "点评", "成绩",
                "课程表", "请假条", MenuItem.CONSUME, "校长信箱", "校历", "作息时间", "班级动态"
                , MenuItem.PARENTSCHOOL, MenuItem.SCHOOLNEWS, "班牌留言", "人工考勤", "宿舍考勤", "校车考勤", "进出校考勤", "常用网址", "兴趣班选课", "缴费", MenuItem.ONE_CARD};
        String[] resource = {"年级套餐", "同步学习", "同步试题", "推荐课程"};
        //小学资源
        String[] primaryResource = {"同步练习", "专项练习", "在线课程", "口算王",
                "听写助手", "汉语大词典", "汉字笔画"};

        for (String str : service) {
            if (str.equals(functionName) || str.contains(functionName)) {
                return "1";//家校服务
            }
        }
        for (String str : resource) {
            if (str.equals(functionName)) {
                return "2";//子贵资源
            }
        }
        for (String str : primaryResource) {
            if (str.equals(functionName)) {
                return "5";
            }
        }
        if ("子贵课堂".equals(functionName)) {
            return "3";//子贵课堂
        } else if ("子贵探视".equals(functionName)) {
            return "1"; //现在子贵探视也属于 个性服务 serviceId=1
        }
        return "";
    }

    public Integer getIntegerServiceKind2(String functionName) { //新的对应关系
        String[] service = {"通知", "邮件", "作业", "考勤", "点评", "成绩",
                "课程表", "请假条", MenuItem.CONSUME, "校长信箱", "校历", "作息时间", "班级动态"
                , MenuItem.PARENTSCHOOL, MenuItem.SCHOOLNEWS, "班牌留言", "人工考勤", "宿舍考勤", "校车考勤", "进出校考勤", "常用网址", "兴趣班选课", "缴费", "子贵探视", MenuItem.ONE_CARD};
        String[] resource = {"微课网", "年级套餐", "同步学习", "同步试题", "推荐课程"};
        //小学资源
        String[] primaryResource = {"小学宝", "小学资源", "同步练习", "专项练习", "在线课程", "口算王",
                "听写助手", "汉语大词典", "汉字笔画"};

        for (String str : service) {
            if (str.equals(functionName) || str.contains(functionName)) {
                return 1;//个性服务
            }
        }
        for (String str : resource) {
            if (str.equals(functionName)) {
                return 2;//子贵资源,微课网
            }
        }
        for (String str : primaryResource) {
            if (str.equals(functionName)) {
                return 5;//小学资源 小学宝
            }
        }

        //先判断是否从子贵课堂进来
        if (functionName.indexOf("子贵课堂") >= 0) {
            if (functionName.indexOf("高中") >= 0) {
                return 8;//子贵课堂（高中）
            }
            if (functionName.indexOf("初中") >= 0) {
                return 7;//子贵课堂（初中）
            }
            if (functionName.indexOf("小学") >= 0) {
                return 6;//子贵课堂（初中）
            }

        }

        UserType userType = CCApplication.getInstance().getPresentUser();
        if (functionName.indexOf("子贵课堂") >= 0) {
            if (userType.getGradeName().indexOf("高中") >= 0) {
                return 8;//子贵课堂（高中）
            }
            if (userType.getGradeName().indexOf("初中") >= 0) {
                return 7;//子贵课堂（初中）
            }
            if (userType.getGradeName().indexOf("小学") >= 0) {
                return 6;//子贵课堂（初中）
            }

        } else if ("子贵探视".equals(functionName)) {
            return 8;
        }
        return 0;
    }

    public Integer getIntegerServiceKind(String functionName) {
        String[] service = {"通知", "邮件", "作业", "考勤", "点评", "成绩",
                "课程表", "请假条", MenuItem.CONSUME, "校长信箱", "校历", "作息时间", "班级动态"
                , MenuItem.PARENTSCHOOL, MenuItem.SCHOOLNEWS};
        String[] resource = {"微课网", "年级套餐", "同步学习", "同步试题", "推荐课程"};
        //小学资源
        String[] primaryResource = {"小学宝", "小学资源", "同步练习", "专项练习", "在线课程", "口算王",
                "听写助手", "汉语大词典", "汉字笔画"};

        for (String str : service) {
            if (str.equals(functionName) || str.contains(functionName)) {
                return 1;//个性服务
            }
        }
        for (String str : resource) {
            if (str.equals(functionName)) {
                return 2;//子贵资源,微课网
            }
        }
        for (String str : primaryResource) {
            if (str.equals(functionName)) {
                return 16;//小学资源
            }
        }
        if ("子贵课堂".equals(functionName)) {
            return 3;//子贵课堂
        } else if ("子贵探视".equals(functionName)) {
            return 8;
        }
        return 0;
    }

    public void setPickPhotoFilename(String filename) {
        pickPhotoFilename = filename;
        dbsp.putString("photoFile", filename);
    }

    public String getPickPhotoFilename() {
        if (pickPhotoFilename == null) {
            pickPhotoFilename = dbsp.getString("photoFile");
        }
        return pickPhotoFilename;
    }

    /**
     * 获取系统本地版本号.
     *
     * @return 系统本地版本号
     */
    public String getCurVersion() {
        Properties properties = new Properties();

        try {
            InputStream stream = this.getAssets().open("ver.cfg");
            properties.load(stream);
        } catch (FileNotFoundException e) {
            return "100";
        } catch (IOException e) {
            return "100";
        } catch (Exception e) {
            return "100";
        }

        String version = String.valueOf(properties.get("Version").toString());

        return version;
    }

    public void logoutOnServer() {
        JSONObject json = new JSONObject();
        String result = null;
        try {
            String url = new StringBuilder(Constants.SERVER_URL)
                    .append(Constants.LOGIN_OUT).toString();
            result = HttpHelper.httpPostJson(CCApplication.this, url, json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("logoutOnServer:" + result);
        NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
        if (ret != null) {
            ServerResult serverResult = ret.getServerResult();
            if (serverResult != null && serverResult.getResultCode() != Constants.SUCCESS_CODE) {
                DataUtil.getToast("系统退出异常");
            } else if (serverResult != null && serverResult.getResultCode() == Constants.SUCCESS_CODE) {
                clearData();
            }
        }
    }

    public void logoutOnServer(final String logoutSection) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
        JSONObject json = new JSONObject();
        try {
            json.put("logoutSection", logoutSection);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = new StringBuilder(Constants.SERVER_URL).append(Constants.LOGIN_OUT).toString();
        String result = null;
        try {
            result = HttpHelper.httpPostJson(CCApplication.this, url, json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//				String result = HttpUtils.useHttpUrlConnectionPost(CCApplication.this,Constants.LOGIN_OUT,json);
        System.out.println("logoutOnServer:" + result);
        NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
        if (ret != null) {
            ServerResult serverResult = ret.getServerResult();
            if (serverResult != null && serverResult.getResultCode() != Constants.SUCCESS_CODE) {
                DataUtil.getToast("系统退出异常");
            }
        }
//			}
//		}).start();

    }

    public void initDHMoniter() {
        File dir = new File(ROOT);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //Creat DPSDK
        Log.d("initApp:", m_nLastError + "");
        int nType = 1;
        m_nLastError = IDpsdkCore.DPSDK_Create(nType, m_ReValue);
        Log.d("DpsdkCreate:", m_nLastError + "");

        //set logPath
        try {
            m_nLastError = IDpsdkCore.DPSDK_SetLog(m_ReValue.nReturnValue, LOG_PATH.getBytes());
            Log.d("DPSDK_SetLog:", m_nLastError + "");
        } catch (Exception e) {

        }


        int ret = IDpsdkCore.DPSDK_SetDPSDKStatusCallback(m_ReValue.nReturnValue, new fDPSDKStatusCallback() {

            @Override
            public void invoke(int nPDLLHandle, int nStatus) {
                Log.v("fDPSDKStatusCallback", "nStatus = " + nStatus);
            }
        });

        //设置设备拨号监听器
        ret = IDpsdkCore.DPSDK_SetRingCallback(m_ReValue.nReturnValue, new fDPSDKRingInfoCallBack() {

            @Override
            public void invoke(int nPDLLHandle, RingInfo_t param) {
                //获取拨号信息
                Log.e(TAG, "fDPSDKRingInfoCallBack RingInfo_t info"
                        + "      callId : " + param.callId);

                //界面跳转
                Intent intent = new Intent(CCApplication.this, AutoVtActivity.class);
                Bundle bundle = new Bundle();

                bundle.putByteArray("szUserId", param.szUserId);
                bundle.putInt("callId", param.callId);
                bundle.putInt("dlgId", param.dlgId);
                bundle.putInt("tid", param.tid);

                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);      //若在不同的进程需要添加flag,
                startActivity(intent);
            }
        });

        //设置可视对讲呼叫邀请参数回调
        ret = IDpsdkCore.DPSDK_SetVtCallInviteCallback(m_ReValue.nReturnValue, new fDPSDKInviteVtCallParamCallBack() {

            @Override
            public void invoke(int nPDLLHandle, InviteVtCallParam_t param) {

                //通过设备呼叫号码，查找设备id，再查找设备的编码通道
                String strcallnum = new String(param.szUserId).trim();

                List<Device_Info_Ex_t> devlist = GroupListManager.getInstance().getDeviceExList();
                List<Enc_Channel_Info_Ex_t> channellist;
                Device_Info_Ex_t deviceInfoEx;
                byte[] szId = new byte[dpsdk_constant_value.DPSDK_CORE_DEV_ID_LEN];
                Enc_Channel_Info_Ex_t encChannelInfoEx = new Enc_Channel_Info_Ex_t();
                String channelname = "";

                for (int i = 0; i < devlist.size(); i++) {
                    deviceInfoEx = devlist.get(i);
                    String szCallNum = new String(deviceInfoEx.szCallNum).trim();
                    if (deviceInfoEx != null && strcallnum.equals(szCallNum)) { //匹配设备呼叫号码
                        //channellist = GroupListManager.getInstance().getChannelsByDeviceId(deviceInfoEx.szId);  //通过设备id查找编码通道

//						if(channellist != null && channellist.size()>0){
//							encChannelInfoEx = channellist.get(0);   //取编码通道中的第一个
//						}
                        byte[] bt = (new String(deviceInfoEx.szId).trim() + "$1$0$0").getBytes();
                        System.arraycopy(bt, 0, szId, 0, bt.length);

                        channelname = new String(szId).trim();
                        Log.e(TAG, "****************channelid****************" + "           " + channelname);
                    }
                }

                //界面跳转
                Intent intent = new Intent(CCApplication.this, AutoVtActivity.class);
                Bundle bundle = new Bundle();
                bundle.putByteArray("szUserId", param.szUserId);
                bundle.putInt("audioType", param.audioType);
                bundle.putInt("audioBit", param.audioBit);
                bundle.putInt("sampleRate", param.sampleRate);
                bundle.putByteArray("rtpServIP", param.rtpServIP);
                bundle.putInt("rtpAPort", param.rtpAPort);
                bundle.putInt("rtpVPort", param.rtpVPort);
                bundle.putInt("nCallType", param.nCallType);
                bundle.putInt("tid", param.tid);
                bundle.putInt("callId", param.callId);
                bundle.putInt("dlgId", param.dlgId);

//				bundle.putByteArray("channelid", encChannelInfoEx.szId);
//				bundle.putByteArray("channelname", encChannelInfoEx.szName);
                bundle.putByteArray("channelid", szId);
                bundle.putByteArray("channelname", szId);

                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);      //若在不同的进程需要添加flag,
                startActivity(intent);

            }
        });
    }

    public String getServer_date() {
        return server_date;
    }

    public void setServer_date(String server_date) {
        this.server_date = server_date;
    }


    public int getDpsdkHandle() {
        if (m_loginHandle == 1)  //登录成功，返回PDSDK_Creat时返回的 有效句柄
            return m_ReValue.nReturnValue;
        else
            return 0;
    }

    public int getDpsdkCreatHandle() {  //仅用于获取DPSDK_login的句柄
        return m_ReValue.nReturnValue;
    }

    public void setLoginHandler(int loginhandler) {
        this.m_loginHandle = loginhandler;
    }

    public int getLoginHandler() {
        return this.m_loginHandle;
    }

    public OkHttpClient initClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        RequestHeader header = new RequestHeader(getApplicationContext());
                        Request request;
                        try {
                            request = chain.request()
                                    .newBuilder()
                                    .addHeader("x-device-name", header.getDeviceType()).build();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            request = chain.request()
                                    .newBuilder()
                                    .addHeader("x-device-brand", header.getDeviceBrand()).build();
                        } catch (Exception e) {

                        }


                        request = chain.request()
                                .newBuilder().addHeader("X-os-version", header.getOsVersion())
                                .addHeader("X-User-Account-Id", header.getAccId())
                                .addHeader("X-User-Id", header.getUserId())
                                .addHeader("X-User-Hash", header.getPassword())
                                .addHeader("mac", header.getMac())
                                .addHeader("X-Device-Id", header.getDeviceId())
                                .addHeader("X-School-Id", header.getSchoolId())
                                .addHeader("resolution", header.getResolution())
                                .addHeader("X-mobile-Type", header.getMobileType())
                                .addHeader("version", header.getVersion())
                                .addHeader("X-operator", header.getOperatorName())
                                .addHeader("X-Device-Token", header.getDeviceToken())
                                .build();


                        return chain.proceed(request);
                    }
                }).build();
        return client;
    }

    //保利威视
    //加密秘钥和加密向量，在后台->设置->API接口中获取，用于解密SDK加密串
    //值修改请参考https://github.com/easefun/polyv-android-sdk-demo/wiki/10.%E5%85%B3%E4%BA%8E-SDK%E5%8A%A0%E5%AF%86%E4%B8%B2-%E4%B8%8E-%E7%94%A8%E6%88%B7%E9%85%8D%E7%BD%AE%E4%BF%A1%E6%81%AF%E5%8A%A0%E5%AF%86%E4%BC%A0%E8%BE%93
    /**
     * 加密秘钥
     */
    private String aeskey = "VXtlHmwfS2oYm0CZ";

    /**
     * 加密向量
     */
    private String iv = "2u9gDPKdX6GyQJKU";

    public void initPolyvCilent() {
        //网络方式取得SDK加密串，（推荐）
        //网络获取到的SDK加密串可以保存在本地SharedPreference中，下次先从本地获取
//		new LoadConfigTask().execute();
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        //使用SDK加密串来配置
        client.setConfig("5LRvSl9xppuvHXYX4pc/7hwlLqoH67i13aiXKW13nHlnHj//oEUlWJ6bwwf2UgLYTBgK0cMDiYPOUlcIfH5viHq2IkCVCfrIzaG2X4pX1tvhCEubSjjhjgl/QIvnHho1jS6AWZdNWPKlmntJIdUA==", aeskey, iv, getApplicationContext());  //正式
        //    client.setConfig("AJhmwduphe2AoXohVGUfWteFKsbhOq2MF6m+UhvfcIp8cCY2vHYcN3wfbE0EYiFlC4wNn6nMRTxYPyFuxXtNVmxE3ZvERsOJoToX2+M6PvOI59kCs7LE8DX/iF/SLYDf5fe7veAKTwca55ZocIAcDg==", aeskey, iv, getApplicationContext()); //测试
        // client.setConfig("CMWht3MlpVkgpFzrLNAebYi4RdQDY/Nhvk3Kc+qWcck6chwHYKfl9o2aOVBvXVTRZD/14XFzVP7U5un43caq1FXwl0cYmTfimjTmNUYa1sZC1pkHE8gEsRpwpweQtEIiTGVEWrYVNo4/o5jI2/efzA==", aeskey, iv, getApplicationContext());
        //初始化SDK设置
        client.initSetting(getApplicationContext());
        //启动Bugly
        //      client.initCrashReport(getApplicationContext());
        //启动Bugly后，在学员登录时设置学员id
//		client.crashReportSetUserId(userId);
        //获取SD卡信息
        PolyvDevMountInfo.getInstance().init(this, new PolyvDevMountInfo.OnLoadCallback() {

            @Override
            public void callback() {
                //是否有可移除的存储介质（例如 SD 卡）或内部（不可移除）存储可供使用。
                if (!PolyvDevMountInfo.getInstance().isSDCardAvaiable()) {
                    // TODO 没有可用的存储设备,后续不能使用视频缓存功能
                    Log.e(TAG, "没有可用的存储设备,后续不能使用视频缓存功能");
                    return;
                }

                //可移除的存储介质（例如 SD 卡），需要写入特定目录/storage/sdcard1/Android/data/包名/。
                String externalSDCardPath = PolyvDevMountInfo.getInstance().getExternalSDCardPath();
                if (!TextUtils.isEmpty(externalSDCardPath)) {
                    StringBuilder dirPath = new StringBuilder();
                    dirPath.append(externalSDCardPath).append(File.separator).append("Android").append(File.separator).append("data")
                            .append(File.separator).append(getPackageName()).append(File.separator).append("polyvdownload");
                    File saveDir = new File(dirPath.toString());
                    if (!saveDir.exists()) {
                        getExternalFilesDir(null); // 生成包名目录
                        saveDir.mkdirs();//创建下载目录
                    }

                    //设置下载存储目录
                    PolyvSDKClient.getInstance().setDownloadDir(saveDir);
                    return;
                }

                //如果没有可移除的存储介质（例如 SD 卡），那么一定有内部（不可移除）存储介质可用，都不可用的情况在前面判断过了。
                File saveDir = new File(PolyvDevMountInfo.getInstance().getInternalSDCardPath() + File.separator + "polyvdownload");
                if (!saveDir.exists()) {
                    saveDir.mkdirs();//创建下载目录
                }

                //设置下载存储目录
                PolyvSDKClient.getInstance().setDownloadDir(saveDir);
            }
        });
    }

    private class LoadConfigTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String config = PolyvSDKUtil.getUrl2String("http://demo.polyv.net/demo/appkey.php");
            if (TextUtils.isEmpty(config)) {
                try {
                    throw new Exception("没有取到数据");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return config;
        }

        @Override
        protected void onPostExecute(String config) {
            PolyvSDKClient client = PolyvSDKClient.getInstance();
            client.setConfig(config, aeskey, iv);
        }
    }

    //和教育数据存储
    private HeEducationUserBean heEducationUserBean;

    public void setHeEducationDetail(String data) {
        if (data != null) {
            heEducationUserBean = JsonUtils.fromJson(data, HeEducationUserBean.class);
        } else {
            heEducationUserBean = null;
        }
        dbsp.putString("HeEducationUser", data);
    }

    public HeEducationUserBean getHeEducationDetail() {
        if (heEducationUserBean == null) {
            String member = dbsp.getString("HeEducationUser");
            if (!DataUtil.isNull(member)) {
                heEducationUserBean = JsonUtils.fromJson(member, HeEducationUserBean.class);
            }
        }
        return heEducationUserBean;
    }


    private String appToken;

    public void setAppToken(String appToken) {
        dbsp.putString("appToken", appToken);
    }


    public String getAppToken() {
        appToken = dbsp.getString("appToken");
        if (appToken != null) {
            return appToken;
        } else {
            return "";
        }
    }

    public void setHeAutoLogin(boolean flag) {
        dbsp.putBoolean("heLogin", flag);
    }

    public boolean getHeAutoLogin() {
        return dbsp.getBoolean("heLogin");
    }
}
