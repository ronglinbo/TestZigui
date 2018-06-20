/*
 * 文 件 名:HomeActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-1
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.bugtags.library.Bugtags;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
//import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.id;
import com.wcyc.zigui2.R.layout;
import com.wcyc.zigui2.bean.MyInfoBean;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.asynctask.JoinGroupAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.JoinGroupAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.ClassList;
import com.wcyc.zigui2.newapp.bean.GetAllContactsReq;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.ModelRemindList;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.bean.NewMessageListBean;
import com.wcyc.zigui2.newapp.bean.NewVersionCheckModel;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.ModelRemindList.ModelRemind;
import com.wcyc.zigui2.newapp.fragment.AllMessageAdapter;
import com.wcyc.zigui2.newapp.fragment.AllMessageFragment;
import com.wcyc.zigui2.newapp.fragment.ContactFragment;
import com.wcyc.zigui2.newapp.fragment.HomeFragment;
import com.wcyc.zigui2.newapp.fragment.NewFindFragment;
import com.wcyc.zigui2.newapp.fragment.NewMyFragment;
import com.wcyc.zigui2.newapp.fragment.NewServiceFragment;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct;
import com.wcyc.zigui2.newapp.module.charge2.ProductAdapter;
import com.wcyc.zigui2.newapp.module.charge2.ProductInfoAdapter;

import com.wcyc.zigui2.newapp.module.email.MenuConfigBean;
import com.wcyc.zigui2.newapp.receiver.CMDMessageBroadcastReceiver;
import com.wcyc.zigui2.newapp.widget.ChooseRolesList;
import com.wcyc.zigui2.newapp.widget.QuickServicePublish;
import com.wcyc.zigui2.updatesystem.NewUpdateVer;
import com.wcyc.zigui2.utils.ApiManager;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.utils.SPConstants;
import com.wcyc.zigui2.utils.SPUtils;
import com.wcyc.zigui2.widget.CustomDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.IntentFilter.SYSTEM_HIGH_PRIORITY;


public class HomeActivity extends BaseActivity
        implements HttpRequestAsyncTaskListener, JoinGroupAsyncTaskListener {
    private static final int DELETE = 10;
    private NewMessageBroadcastReceiver receiver;
    private CMDMessageBroadcastReceiver cmdReceiver;
    private Fragment homeFragment, serviceFragment;
    private FragmentTabHost mTabHost;
    private Button messageBtn, serviceBtn, friendBtn, settingBtn;
    private View messagePaper, servicePaper, friendPaper, settingPaper;
    //	private RadioGroup m_radioGroup;
    private View bottom;
    private TextView unreadLabel;//消息页中的信息
    private ImageView unreadLabelService, unreadLabelFriend;//服务页中的新消息

    private String tabs[] = {"message", "services", "friends", "setting"};
    Class cls[] = {HomeFragment.class, NewServiceFragment.class,
            NewFindFragment.class, NewMyFragment.class};

    public static final int GET_MESSAGE_ACTION = 1;
    public static final int GET_MESSAGE_LIST_ACTION = 2;
    public static final int GET_CONTACT_LIST_ACTION = 3;
    public static final int CHECK_SERVICE_EXPIRED = 4;
    public static final int GET_CHARGE_INFO = 5;
    private QuickServicePublish publishPop;
    private ChooseRolesList pop;
    private Bitmap icon;
    private NewMessageListBean message;
    private int newMessageCount = 0, newFriendMsgCount = 0;
    private CustomDialog dialog;
    private AllMessageFragment messfragment;
    private ChargeProduct product;
    public static final String INTENT_HIDE_RED_POINT = "com.wcyc.zigui2.action.INTENT_HIDE_RED_POINT";//广播 刷新小红点
    public static final String INTENT_NEW_MESSAGE = "com.wcyc.zigui2.action.NEW_MESSAGE";//新通知消息
    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";//网络变化
    private JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//		getProductList();
//		PushManager.getInstance().initialize(this.getApplicationContext());
        init();

        //注册网络变化广播
        registerDateTransReceiver();

        // 注册接收消息广播
        receiver = new NewMessageBroadcastReceiver();
        if (EMChatManager.getInstance() != null) {
            IntentFilter intentFilter = null;
            try {
                intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
                // 设置广播的优先级别大于HomeActivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
                intentFilter.setPriority(5);
                registerReceiver(receiver, intentFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        // 注册一个接收推送消息的BroadcastReceiver
//		System.err.println("registerCmdReceiver!!!!!!");
//		cmdReceiver = new CMDMessageBroadcastReceiver();
//		IntentFilter cmdmsgFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
//		cmdmsgFilter.setPriority(SYSTEM_HIGH_PRIORITY);
//		registerReceiver(cmdReceiver,cmdmsgFilter);

        IntentFilter mrefeshDataFilter = new IntentFilter(INTENT_HIDE_RED_POINT);
        registerReceiver(hideRedPointReceiver, mrefeshDataFilter);

        IntentFilter newMessageFilter = new IntentFilter(INTENT_NEW_MESSAGE);
        registerReceiver(newMessageReceiver, newMessageFilter);

        IntentFilter filter = new IntentFilter(UPDATE_VERSION_SUCCESS);
        registerReceiver(updateReceiver, filter);
    }


    protected void onResume() {
        super.onResume();
        getMenuConfig();
        checkServiceExpired();
        getModelRemind();
        Intent intent = getIntent();
        Boolean firstPage = intent.getBooleanExtra("firstPage", false);
        System.out.println("firstPage:" + firstPage);
        if (firstPage) {
            messageBtn.setSelected(true);
//			AllContactListBean allContactList = CCApplication.getInstance().getAllContactList();
//			new JoinGroupAsyncTask(this,allContactList,this).execute();
        } else {
            serviceFragment = getSupportFragmentManager().findFragmentByTag(tabs[1]);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // 注销广播
        try {
            if (receiver != null) {
                unregisterReceiver(receiver);
                receiver = null;
            }
//			unregisterReceiver(cmdReceiver);
//			cmdReceiver = null;
            if (hideRedPointReceiver != null) {
                unregisterReceiver(hideRedPointReceiver);
                hideRedPointReceiver = null;
            }
            if (newMessageReceiver != null) {
                unregisterReceiver(newMessageReceiver);
                newMessageReceiver = null;
            }
            if (updateReceiver != null) {
                unregisterReceiver(updateReceiver);
                updateReceiver = null;
            }

            if (NetChangeReceiver != null) {
                unregisterReceiver(NetChangeReceiver);
                NetChangeReceiver = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadLabelService = (ImageView) findViewById(id.unread_msg_service);
        unreadLabelFriend = (ImageView) findViewById(id.unread_msg_discover);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setVisibility(View.GONE);
        for (int i = 0; i < tabs.length; i++) {
            mTabHost.addTab(mTabHost.newTabSpec(tabs[i]).setIndicator(tabs[i]), cls[i], null);
        }
        bottom = findViewById(R.id.bottom);
        messageBtn = (Button) findViewById(R.id.firstpager);
//        messageBtn.setSelected(true);
//        messageBtn.setTextColor(getResources().getColor(R.color.font_darkblue));
        serviceBtn = (Button) findViewById(R.id.service);
        friendBtn = (Button) findViewById(R.id.friend);
        settingBtn = (Button) findViewById(R.id.setting);
        messagePaper = findViewById(R.id.message_paper);
        servicePaper = findViewById(R.id.service_paper);
        friendPaper = findViewById(R.id.friend_paper);
        settingPaper = findViewById(R.id.setting_pager);
        initEvent();
        mTabHost.setCurrentTabByTag(tabs[0]);
        modifyButtonState(messageBtn);
        if (getSupportFragmentManager() != null) {
            homeFragment = getSupportFragmentManager().findFragmentByTag(tabs[0]);
            serviceFragment = getSupportFragmentManager().findFragmentByTag(tabs[1]);
        }
        NewMemberBean member = CCApplication.getInstance().getMemberInfo();
        if (member != null) {
            String file = member.getUserIconURL();
            String url = DataUtil.getIconURL(file);
            ImageLoader mloader = getImageLoader();
            mloader.loadImage(url, listener);
        }

    }

    private void initEvent() {
        messageBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mTabHost.setCurrentTabByTag(tabs[0]);
                modifyButtonState(messageBtn);
            }

        });
        serviceBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				getModelRemind();
                mTabHost.setCurrentTabByTag(tabs[1]);
                modifyButtonState(serviceBtn);
            }

        });
        friendBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mTabHost.setCurrentTabByTag(tabs[2]);
                modifyButtonState(friendBtn);
            }

        });
        settingBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mTabHost.setCurrentTabByTag(tabs[3]);
                modifyButtonState(settingBtn);
            }

        });

        messagePaper.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mTabHost.setCurrentTabByTag(tabs[0]);
                modifyButtonState(messageBtn);
            }
        });

        servicePaper.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mTabHost.setCurrentTabByTag(tabs[1]);
                modifyButtonState(serviceBtn);
            }
        });

        friendPaper.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mTabHost.setCurrentTabByTag(tabs[2]);
                modifyButtonState(friendBtn);
            }
        });

        settingPaper.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mTabHost.setCurrentTabByTag(tabs[3]);
                modifyButtonState(settingBtn);
            }
        });

        Button publish = (Button) findViewById(R.id.publish);
        publish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                publishPop = new QuickServicePublish(HomeActivity.this);
                publishPop.showAtLocation(bottom, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }

        });
    }

    private void modifyButtonState(Button button) {
        messageBtn.setSelected(false);
        messageBtn.setTextColor(getResources().getColor(R.color.font_darkgray));
        serviceBtn.setSelected(false);
        serviceBtn.setTextColor(getResources().getColor(R.color.font_darkgray));
        friendBtn.setSelected(false);
        friendBtn.setTextColor(getResources().getColor(R.color.font_darkgray));
        settingBtn.setSelected(false);
        settingBtn.setTextColor(getResources().getColor(R.color.font_darkgray));
        button.setSelected(true);
        button.setTextColor(getResources().getColor(R.color.font_darkblue));
    }

    private ImageLoadingListener listener = new ImageLoadingListener() {

        @Override
        public void onLoadingCancelled(String arg0, View arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
            // TODO Auto-generated method stub
            setIcon(arg2);
//			LocalUtil.mBitMap = arg2;
        }

        @Override
        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLoadingStarted(String arg0, View arg1) {
            // TODO Auto-generated method stub

        }

    };

    private void checkServiceExpired() {
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null && Constants.PARENT_STR_TYPE.equals(user.getUserType())) {
            JSONObject json = new JSONObject();
            try {
                json.put("userId", user.getUserId());
                json.put("studentId", user.getChildId());
                System.out.println("HomeActivity checkServiceExpired:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//			queryPost(Constants.CHECK_SERVICE_EXPIRE,json);
            new HttpRequestAsyncTask(json, this, this).execute(Constants.CHECK_SERVICE_EXPIRE);
            action = CHECK_SERVICE_EXPIRED;
        }
    }

    @Override
    protected void getMessage(String data) {

        switch (action) {
            case GET_MESSAGE_ACTION:
                System.out.println("getMessage data:" + data);
                message = JsonUtils.fromJson(data, NewMessageListBean.class);
                if (message.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    CCApplication.getInstance().setMessageList(message);
                    if (homeFragment == null) {
                        homeFragment = getSupportFragmentManager().findFragmentByTag(tabs[0]);
                        if (homeFragment != null) {
                            Fragment fragment = homeFragment.getChildFragmentManager().findFragmentByTag(0 + "");
                            messfragment = (AllMessageFragment) fragment;
                            if (messfragment != null) {
                                AllMessageAdapter adapter = messfragment.getAdapter();
                                if (adapter != null) {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                    updateUnreadLabel();
                }
                break;
            case GET_MESSAGE_LIST_ACTION:
                break;
            case GET_CONTACT_LIST_ACTION:
                System.out.println("getAllContactList data:" + data);
                AllContactListBean contact = JsonUtils.fromJson(data, AllContactListBean.class);
                if (contact.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    CCApplication.getInstance().setAllContactList(contact);
                    if (homeFragment != null) {
                        ContactFragment fragment = (ContactFragment) ((HomeFragment) homeFragment).getContactFragment();
                        if (fragment != null)
                            fragment.getAdapter().notifyDataSetChanged();
                    }
                }
                break;

            case CHECK_SERVICE_EXPIRED:
                System.out.println("HomeActivity checkServiceExpired ret:" + data);
                ServiceExpiredBean service = JsonUtils.fromJson(data, ServiceExpiredBean.class);
                if (service.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    CCApplication.getInstance().setServiceExpiredInfo(service);
                }
                break;
            case GET_CHARGE_INFO:
                System.out.println("获取套餐列表结果:" + data);
                product = JsonUtils.fromJson(data, ChargeProduct.class);
                if (product.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
                    DataUtil.getToast(product.getServerResult().getResultMessage());
                } else {
                    CCApplication.getInstance().setProductInfo(product);
                }
                break;
            case DELETE:
                System.out.println("删除:" + data);

                break;

        }

    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            if (count > 99) {
                unreadLabel.setText("99+");
            } else {
                unreadLabel.setText(String.valueOf(count));
            }
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }

        //应用小红点
        int serviceCount = newMessageCount + newFriendMsgCount + getUnreadStatus(Constants.SCHOOL_NEWS);
        int systemCount = getUnreadStatus(Constants.SYSTEM_MESSAGE);
        int payCount = getUnreadStatus(Constants.PAY_NOTICE);

        if (serviceCount - systemCount - payCount > 0) {
            unreadLabelService.setVisibility(View.VISIBLE);
        } else {
            unreadLabelService.setVisibility(View.INVISIBLE);
        }



        if (getUnreadStatus(Constants.EDU_INFO) > 0) {
            unreadLabelFriend.setVisibility(View.VISIBLE);
        } else {
            unreadLabelFriend.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    private int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        NewMessageListBean AllMessageList = CCApplication.getInstance().getMessageList();
        if (AllMessageList != null) {
            List<NewMessageBean> messageList = AllMessageList.getMessageList();
            if (messageList != null) {
                for (NewMessageBean message : messageList) {

                    //把环信消息未读数添加进去
                    if ("chat".equals(message.getMessageType())) {
                        String count = message.getCount();
                        int num = 0;
                        if (count != null) {
                            num = Integer.parseInt(count);
                        }
                        unreadMsgCountTotal += num;
                    }

                    //把百川消息未读数添加进去
                    if("aLiChat".equals(message.getMessageType())){
                        String count = message.getCount();
                        int num = 0;
                        if (count != null) {
                            num = Integer.parseInt(count);
                        }
                        unreadMsgCountTotal += num;
                    }
                }
            }
        }
        newMessageCount = DataUtil.getAllModelRemind();
        unreadMsgCountTotal += newMessageCount;
        newFriendMsgCount = getUnreadStatus(Constants.CLASSDYN);//班级动态类型
        return unreadMsgCountTotal;
    }


    //标记未读消息
    private int getUnreadStatus(String type) {
        ModelRemindList remind = CCApplication.getInstance().getModelRemindList();
        if (remind != null) {
            List<ModelRemind> list = remind.getMessageList();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (type.equals(list.get(i).getType())) {
                        String count = list.get(i).getCount();
                        return Integer.parseInt(count);
                    }
                }
            }
        }
        return 0;
    }

    private void setShortcutBadger() {
        int unreadNum = getUnreadMsgCountTotal();
        if (unreadNum > 99) unreadNum = 99;
        try {
            ShortcutBadger.setBadge(this, unreadNum);
        } catch (ShortcutBadgeException e) {
            e.printStackTrace();
        }
    }

    public void setAction(int action) {
        this.action = action;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (publishPop != null) {
                    publishPop.dismiss();
                    publishPop = null;
                    return true;
                }
                if (getSupportFragmentManager() != null) {
                    homeFragment = getSupportFragmentManager().findFragmentByTag(tabs[0]);
                    if (homeFragment != null) {
                        pop = ((HomeFragment) homeFragment).getPopWindowHandle();
                        if (pop != null) {
                            pop.dismiss();
                            ((HomeFragment) homeFragment).setPopWindowHandle(null);
                            return true;
                        }
                    }
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }


    public void getModelRemind() {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            try {
                json.put("userId", user.getUserId());
                json.put("userType", user.getUserType());
                if (CCApplication.getInstance().isCurUserParent()) {
                    json.put("studentId", user.getChildId());
                }
                json.put("schoolId", user.getSchoolId());
                System.out.println("主界面获取服务未读消息数:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(json, this, this).execute(Constants.GET_MODEL_REMIND);
        }
    }

    private void getAllContact() {

        GetAllContactsReq req = new GetAllContactsReq();
        UserType user = CCApplication.getInstance().getPresentUser();
        try {
            JSONObject json;
            List<String> classIdList = new ArrayList<String>();
            if (user.getUserType().equals(Constants.TEACHER_STR_TYPE)) {
                MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
                if (detail != null) {
                    List<NewClasses> list = detail.getClassList();
                    if (list != null) {
                        for (NewClasses classes : list) {
                            if (!DataUtil.isClassIdExist(classIdList, classes.getClassID()))//是否已存在list中
                                classIdList.add(classes.getClassID());
                        }
                    }
                }
            } else if (user.getUserType().equals(Constants.PARENT_STR_TYPE)) {
                classIdList.add(user.getClassId());
            }
            req.setSchoolId(user.getSchoolId());
            req.setUserId(user.getUserId());
            req.setUserType(user.getUserType());
            if (classIdList.size() > 0) {
                req.setClassIdList(classIdList);
            } else {
                classIdList.add("-1");
                req.setClassIdList(classIdList);
            }
            Gson gson = new Gson();
            String string = gson.toJson(req);
            json = new JSONObject(string);
            System.out.println("HomeActivity getAllContact json:" + json);
            new HttpRequestAsyncTask(json, this, this)
                    .execute(Constants.GET_CONTACT_URL);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onRequstComplete(String result) {
        // TODO Auto-generated method stub
        if (result.contains("classList")) {
            AllContactListBean allContactList = JsonUtils.fromJson(result, AllContactListBean.class);
            if (allContactList.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                CCApplication.getInstance().setAllContactList(allContactList);
//				loadchat();
//				joinAllGroup(allContactList);
//				new JoinGroupAsyncTask(this,allContactList,this).execute();
            }
        } else if (result.contains("modelRemindList")) {
            ModelRemindList remind = JsonUtils.fromJson(result, ModelRemindList.class);
            if (remind.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                CCApplication.getInstance().setModelRemindList(remind);
                serviceFragment = getSupportFragmentManager().findFragmentByTag(tabs[1]);
                if (serviceFragment != null) {
                    ((NewServiceFragment) serviceFragment).Refesh();
                }
                setShortcutBadger();
                homeFragment = getSupportFragmentManager().findFragmentByTag(tabs[0]);
                if (homeFragment != null) {
                    Fragment fragment = homeFragment.getChildFragmentManager().findFragmentByTag(0 + "");
                    messfragment = (AllMessageFragment) fragment;
                    if (messfragment != null) {
                        AllMessageAdapter adapter = messfragment.getAdapter();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        } else if (result.contains("serviceList")) {
            System.out.println("HomeActivity checkServiceExpired ret:" + result);
            ServiceExpiredBean service = JsonUtils.fromJson(result, ServiceExpiredBean.class);
            if (service.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                CCApplication.getInstance().setServiceExpiredInfo(service);
            }
        } else {
            System.out.println("HomeActivity checkServiceExpired ret:" + result);
            ServiceExpiredBean service = JsonUtils.fromJson(result, ServiceExpiredBean.class);
            if (service.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                CCApplication.getInstance().setServiceExpiredInfo(service);
            }
        }
        updateUnreadLabel();
    }

    @Override
    public void onRequstCancelled() {
        // TODO Auto-generated method stub

    }

    //加入所有群组
    private void joinAllGroup(final AllContactListBean allContactList) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                List<ClassList> list = allContactList.getClassList();
                for (ClassList item : list) {
                    String groupId = item.getGroupId();
                    System.out.println("joinAllGroup groupId:" + groupId);
                    if (groupId != null) {
                        try {
                            EMGroupManager.getInstance().joinGroup(groupId);
                        } catch (EaseMobException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }

        }).run();

    }

    /**
     * 消息广播接收者类.
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {

        /**
         * 处理接收事件.
         *
         * @param context 正文
         * @param intent  响应意图
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DataUtil.isMain() == false) return;//副号不接受聊天消息
            String username = intent.getStringExtra("from");
            String msgid = intent.getStringExtra("msgid");
            System.out.println("newChatMessage:" + userName + " homeFragment:" + homeFragment);
            getHxMessage(msgid);

            if (homeFragment == null) {
                homeFragment = getSupportFragmentManager().findFragmentByTag(tabs[0]);
            }
            System.out.println("homeFragment:" + homeFragment);
            if (homeFragment != null) {
                Fragment fragment = homeFragment.getChildFragmentManager().findFragmentByTag(0 + "");
                messfragment = (AllMessageFragment) fragment;
                if (messfragment != null) {
//					messfragment.loadRecentChat();
//					messfragment.loadGroupChat(username);
                    messfragment.refresh();
                    AllMessageAdapter adapter = messfragment.getAdapter();
                    System.out.println("adapter:" + adapter);
                    if (adapter != null)
                        adapter.notifyDataSetChanged();

                    updateUnreadLabel();
                }
            }
        }
    }

    private void getHxMessage(String msgid) {
        String username;// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
        EMMessage message = EMChatManager.getInstance().getMessage(msgid);
        // 如果是群聊消息，获取到group id
        if (message.getChatType() == ChatType.GroupChat) {
            username = message.getTo();
            System.out.println("userName:" + userName);
        }
    }

    private BroadcastReceiver hideRedPointReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            unreadLabelFriend.setVisibility(View.INVISIBLE);//隐藏发现的小红点
        }
    };

    private BroadcastReceiver newMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (homeFragment == null) {
                homeFragment = getSupportFragmentManager().findFragmentByTag(tabs[0]);
            }
            System.out.println("homeFragment:" + homeFragment);
            if (homeFragment != null) {
                Fragment fragment = homeFragment.getChildFragmentManager().findFragmentByTag(0 + "");
                messfragment = (AllMessageFragment) fragment;

                if (messfragment != null) {
                    messfragment.getNewMessage();
                }
            }
            getModelRemind();
            updateUnreadLabel();
        }

    };

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) return;
            NewVersionCheckModel ret = (NewVersionCheckModel) bundle.getSerializable("updateVersion");
            if ("1".equals(ret.getIsNeedUpdate())) {
                if (!DataUtil.isNullorEmpty(ret.getUpdateAddress())) {
                    NewUpdateVer uv = new NewUpdateVer(ret, HomeActivity.this);
                    //强制更新
                    if (ret.getIsUpdate() == 1) {
                        uv.mustUp = true;
                        uv.checkVer();
                    }
                } else {
                    DataUtil.getToast("无效的apk下载地址");
                }
            }
        }
    };


    @Override
    public void onJoinGroupCancelled() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onJoinGroupComplete(String result) {
        // TODO Auto-generated method stub

    }

    public void getMenuConfig() {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            try {
                json.put("schoolId", user.getSchoolId());
                json.put("userType", user.getUserType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(CCApplication.get().initClient())
                .build();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json.toString());
        ApiManager apiManager = retrofit.create(ApiManager.class);
        Call call = apiManager.getPersonalApplyList(body);
        call.enqueue(new Callback<MenuConfigBean>() {
            @Override
            public void onResponse(Call<MenuConfigBean> call, Response<MenuConfigBean> response) {
                MenuConfigBean config = response.body();
                Log.i("临时TAG","获取功能列表");
                if(CCApplication.getInstance().isCurUserParent()){
                    setMenuItemName(config);
                }else{
                    MenuItem.initMenuItemName();
                }

                if (config != null && config.getServerResult() != null) {
                    if (config.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                        handleResponse(config);
                    } else {
                        DataUtil.getToast(config.getServerResult().getResultMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<MenuConfigBean> call, Throwable t) {

            }
        });

    }

    public void setMenuItemName(MenuConfigBean menuItemName){
        if(null!=menuItemName){
            List<MenuConfigBean.MenuConfig> menuConfigs=menuItemName.getPersonalConfigList();
            if(null!=menuConfigs){
                for (int i=0;i<menuConfigs.size();i++){
                    SPUtils.put(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(menuConfigs.get(i).getFunctionNumber()),menuConfigs.get(i).getFunctionName());
                }
                MenuItem.getMenuItemName();
            }
        }
    }

    private BroadcastReceiver NetChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action1 = intent.getAction();

            if (TextUtils.equals(action1, CONNECTIVITY_CHANGE_ACTION)) {//网络变化的时候会发送通知
                System.out.println("网络变化了");
                if (DataUtil.isNetworkAvailable(HomeActivity.this)) {
                    //网络恢复
                    //       DataUtil.getToastShort("网络恢复");
                    SharedPreferences sharedPreferences = CCApplication.applicationContext.getSharedPreferences("delete", Context.MODE_PRIVATE);
                    String json = sharedPreferences.getString(Constants.DELETE_PARENT_MESSAGE, "0");
                    String json1 = sharedPreferences.getString(Constants.DELETE_STUDENTBOARD_MESSAGE, "0");
                    if (!json.equals("0")) {
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonOb = jsonArray.getJSONObject(i);
                                jsonObject = jsonOb;
                                String url = new StringBuilder(Constants.SERVER_URL).append(Constants.DELETE_PARENT_MESSAGE).toString();
                                // /删除数据
                                String result = HttpHelper.httpPostJson(HomeActivity.this, url, jsonObject);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("200")) {

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        sharedPreferences = CCApplication.applicationContext.getSharedPreferences("delete", Context.MODE_PRIVATE);
                        sharedPreferences.edit().remove(Constants.DELETE_STUDENTBOARD_MESSAGE);
                        sharedPreferences.edit().commit();
                    }
                    if (!json1.equals("0")) {
                        try {
                            JSONArray jsonArray = new JSONArray(json1);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonOb = jsonArray.getJSONObject(i);
                                jsonObject = jsonOb;
                                String url = new StringBuilder(Constants.SERVER_URL).append(Constants.DELETE_STUDENTBOARD_MESSAGE).toString();
                                // /删除数据
                                String result = HttpHelper.httpPostJson(HomeActivity.this, url, jsonObject);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("200")) {

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        sharedPreferences = CCApplication.applicationContext.getSharedPreferences("delete", Context.MODE_PRIVATE);
                        sharedPreferences.edit().remove(Constants.DELETE_STUDENTBOARD_MESSAGE);
                        sharedPreferences.edit().commit();
                    }

                } else {
                    //网络失效
                    //    DataUtil.getToastShort("网络断掉");
                }

                return;
            }
        }
    };

    private void registerDateTransReceiver() {
        Log.i("网络变化", "register receiver " + CONNECTIVITY_CHANGE_ACTION);
        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_CHANGE_ACTION);
        filter.setPriority(1000);
        registerReceiver(NetChangeReceiver, filter);
    }


    private void handleResponse(MenuConfigBean config) {
        CCApplication.getInstance().setMenuConfig(config);
    }
}
