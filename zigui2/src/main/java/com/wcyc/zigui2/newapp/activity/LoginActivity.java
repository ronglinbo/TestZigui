
/**
 * 文件名：com.wcyc.zigui.home.LoginActivity.java
 * <p>
 * 版本信息：
 * 日期：2014年9月28日 下午2:26:42
 * Copyright 惟楚有材 Corporation 2014
 * 版权所有
 */

package com.wcyc.zigui2.newapp.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dh.DHMoniterService;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;
import com.wcyc.zigui2.R;
//import com.wcyc.zigui2.bean.MemberBean;

import com.wcyc.zigui2.newapp.bean.LoginBean;
import com.wcyc.zigui2.newapp.bean.LoginVerifyBean;
import com.wcyc.zigui2.newapp.home.ServiceAgreementActivity;
import com.wcyc.zigui2.newapp.service.ChatLoginService;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.core.HomeWebviewActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.NewVersionCheckModel;
import com.wcyc.zigui2.newapp.bean.UserType;

import com.wcyc.zigui2.updatesystem.NewUpdateVer;
import com.wcyc.zigui2.utils.ApiManager;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.RequestHeader;
import com.wcyc.zigui2.utils.TextFilter;
import com.wcyc.zigui2.newapp.service.PushSmsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fitScreen.ScreenInfo;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 用户登录主界面.
 * =
 */
public class LoginActivity extends BaseActivity
        implements OnClickListener, HttpRequestAsyncTaskListener {

    static String TAG = LoginActivity.class.getName();
    private TextView loginactivity_tv_forgetpassword;
    private Button loginactivity_btn_login;
    private EditText loginactivity_name_et;
    private EditText loginactivity_password_et;
    private LinearLayout service_agreement;
    private HttpRequestAsyncTaskListener httpRequestAsyncTaskListener = new HttpRequestAsyncTaskListener() {
        @Override
        public void onRequstComplete(String result) {
            parseQIDong(result);
        }

        @Override
        public void onRequstCancelled() {

        }
    };
    private NewMemberBean member;
    private MemberDetailBean detail;
    private final int ACTION_LOGIN = 0;
    private final int ACTION_VERIFY = 1;
    private final int ACTION_UPDATE = 2;
    private final int ACTION_GET_LOGIN_INFO = 3;
    private final int ACTION_GET_QIDONG_PAGE = 4;
    private final int VERIFY_RESULT = 100;
    private android.app.AlertDialog.Builder conflictBuilder;
    private boolean isConflictDialogShow;
    private ImageView nameDeleteImage, passwordDeleteImage;

    private String phoneNum; //保存登录的电话号码
    private String phonePwd; //保存登录的密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CCApplication.app.finishAllActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getStatusBarHeight();
        initView();
        fitLType();
        member = CCApplication.getInstance().getMemberInfo();
        System.out.println("member" + member);
        initEvents();
        //消息推送
//    	Intent intent = new Intent(this, PushSmsService.class);
//        // 启动服务
//        startService(intent);

        if (member != null) {

            String isNeedModifyPsd = member.getIsNeedModifyPwd();

            if ("1".equals(isNeedModifyPsd)) {
                //修改密码

                //和教育做了特殊处理
//                changePassword(true);

                afterLogin();

            } else {
//				if(hasLoginRight(member)) {
//					List<UserType> list = member.getUserTypeList();
//					List<UserType> users = filterItem(list);
//					member.setUserTypeList(users);
                System.err.println("LoginActivity afterLoginToHome");
                afterLogin();
//				}else{
//					DataUtil.getToast(R.string.no_login_right);
//				}
            }
        }
    }

    /**
     * 更新版本.
     *
     * @param deviceID   硬件ID
     * @param mobileType 系统版本：“android”
     * @param version    版本:1.0
     */
    private void checkVersionUpdate(String deviceID, String mobileType, String version) {
        JSONObject json = new JSONObject();
        try {
            json.put("deviceId", deviceID);
            json.put("versionType", mobileType);
            json.put("versionNumber", version);
            System.out.println("获取版本更新json:" + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpRequestAsyncTask(json, this, this).execute(Constants.VERSION_CHECK);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        System.out.println("statusBarHeight:" + result);
        return result;
    }

    private void fitLType() {
        float screenTypeSize = ScreenInfo.GetScreenSizeType(this);
        Log.d(TAG, "screen is size " + screenTypeSize);
        if (screenTypeSize > 900) {
            View view_login = findViewById(R.id.loginactivity_title);
            view_login.setPadding(view_login.getPaddingLeft(), view_login.getPaddingTop() + 200,
                    view_login.getPaddingRight(), view_login.getPaddingBottom());
        } else if (screenTypeSize > 700) {
            View view_login = findViewById(R.id.loginactivity_title);
            view_login.setPadding(view_login.getPaddingLeft(), view_login.getPaddingTop() + 100,
                    view_login.getPaddingRight(), view_login.getPaddingBottom());
        }
    }

    /**
     * 初始化组件.
     */
    private void initView() {
        service_agreement = (LinearLayout) findViewById(R.id.service_agreement);
        loginactivity_btn_login = (Button) findViewById(R.id.loginactivity_btn_login);
        loginactivity_tv_forgetpassword = (TextView) findViewById(R.id.loginactivity_tv_forgetpassword);
        loginactivity_name_et = (EditText) findViewById(R.id.loginactivity_name_et);
        loginactivity_password_et = (EditText) findViewById(R.id.loginactivity_password_et);
        nameDeleteImage = (ImageView) findViewById(R.id.loginactivity_name_et_delete);
        passwordDeleteImage = (ImageView) findViewById(R.id.loginactivity_password_et_delete);

        TextFilter filter2 = new TextFilter();
        loginactivity_name_et.addTextChangedListener(filter2);
        filter2.setEditeTextClearListener(loginactivity_name_et, nameDeleteImage);

        TextFilter filter = new TextFilter();
        loginactivity_password_et.addTextChangedListener(filter);
        filter.setEditeTextClearListener(loginactivity_password_et, passwordDeleteImage);

        //如果保存了电话号码，设置到输入框中
        loginactivity_name_et.setText(getPhoneNum());
        loginButtonEnable(false);
//		loginactivity_password_et.setText(getPhonePwd());
        initState();

        //隐藏忘记密码
        loginactivity_tv_forgetpassword.setVisibility(View.GONE);
    }

    private void initState() {
        boolean enter = false;
        loginactivity_name_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.length() > 0) {
                    String pass = loginactivity_password_et.getText().toString();

                    if (pass.length() > 0) {
                        loginButtonEnable(true);
                    } else {
                        loginButtonEnable(false);
                    }
                } else {
                    loginButtonEnable(false);
                }
            }

        });

        loginactivity_password_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.length() > 0) {
                    String name = loginactivity_name_et.getText().toString();

                    if (name.length() > 0) {
                        loginButtonEnable(true);
                    } else {
                        loginButtonEnable(false);
                    }
                } else {
                    loginButtonEnable(false);
                }
            }

        });

    }

    private void loginButtonEnable(boolean enable) {
        if (enable) {
            loginactivity_btn_login.setClickable(true);
            loginactivity_btn_login.setBackgroundResource(R.drawable.shape_only_login_btn_selector);
        } else {
            loginactivity_btn_login.setClickable(false);
            loginactivity_btn_login.setBackgroundResource(R.drawable.shape_only_login_btn);
        }
    }

    /**
     * 保存用户名和密码.
     */
    public void savaPhoneNum() {
        SharedPreferences sp = getSharedPreferences("little_data", 1);
        Editor editor = sp.edit();
        editor.putString("phoneNum", phoneNum);
        editor.putString("phonePwd", phonePwd);
        editor.commit();
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
    }


    /**
     * 设置监听效果.
     */
    private void initEvents() {
        loginactivity_btn_login.setOnClickListener(this);
        loginactivity_tv_forgetpassword.setOnClickListener(this);
        service_agreement.setOnClickListener(this);
    }

    /**
     * 登录.
     *
     * @param userName 用户名
     * @param pwd      密码
     */
    private void login(String userName, String pwd) {
        if (DataUtil.isNull(userName)) {
            DataUtil.getToast("请输入手机号码");
            loginactivity_name_et.requestFocus();
            return;
        }
        if (DataUtil.isNull(pwd)) {
            DataUtil.getToast("请输入密码");
            loginactivity_password_et.requestFocus();
            return;
        }

        String version = getCurVersion();

        String pwdMd5 = DataUtil.encodeMD5(pwd);
        JSONObject json = new JSONObject();
        try {
            json.put("userName", userName);
            json.put("password", pwd);
            //debug版本不需要校验
//			if(DataUtil.isAPKDebugMode(this)) {
//				json.put("isNeedVerify", "0");
//			}
            json.put("isNeedVerify", "0");
            System.out.println("login json:" + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!isLoading()) {
            //设置电话号码
            phoneNum = userName;
            phonePwd = pwd;
            //只要是重新登录的，都清除一次数据
            CCApplication.app.clearData();
            action = ACTION_LOGIN;
            queryPost(Constants.LOGIN_URL, json);
        }
    }


    @Override
    public void onClick(View v) {
        String phone = "";
        switch (v.getId()) {
            case R.id.loginactivity_btn_login:
                userName = loginactivity_name_et.getText().toString().trim();
                pwd = loginactivity_password_et.getText().toString();
                login(userName, pwd);
//			getLogin(userName,pwd);
                break;
            case R.id.loginactivity_tv_forgetpassword:
                changePassword(false);
                break;
            case R.id.service_agreement:
                newActivity(ServiceAgreementActivity.class, null);
                break;
            default:
                break;
        }
    }

    private void changePassword(boolean isFirstLogin) {
        String phone = userName = loginactivity_name_et.getText().toString().trim();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        bundle.putString("title", "修改密码");
        bundle.putBoolean("isFirstLogin", isFirstLogin);
        newActivity(ForgetPasswordActivity.class, bundle);
    }

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case ACTION_LOGIN:
                parseLoginData(data);
                break;
            case ACTION_UPDATE:
                parseUpdateData(data);
                break;
            case ACTION_GET_LOGIN_INFO:
                parseLoginInfoData(data);
                break;
            default:
                parseLoginData(data);
                break;
        }
    }

    private void parseQIDong(String data) {
        System.out.print(data);
        try {

            JSONObject jsonObject = new JSONObject(data);
            jsonObject = jsonObject.getJSONObject("imageUrlList");
            String url = Constants.URL + "/" + jsonObject.get(getDpi());
            Picasso.with(this).load(url);

            CCApplication.dbsp.putString("QidongUrl", url);
        } catch (JSONException e) {
            //没有的话 默认 子贵校园
            //没有的话 默认 子贵校园
            CCApplication.dbsp.putString("QidongUrl", "zigui");
        }
    }

    private void parseLoginData(String data) {
        System.out.println("login:" + data);
        try {
            member = JsonUtils.fromJson(data, NewMemberBean.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (member.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
            handleError(member.getServerResult().getResultCode());

        } else {
//			if(hasLoginRight(member)) {
//				List<UserType> list = member.getUserTypeList();
//				List<UserType> users = filterItem(list);
//				member.setUserTypeList(users);
            handleResponse();
//			}else {
//				DataUtil.getToast(R.string.no_login_right);
//			}
        }
    }

    /**
     * 处理系统更新返回数据.
     *
     * @param data 系统更新返回的数据
     */
    private void parseUpdateData(String data) {
        Log.d(TAG, "获取版本更新数据: " + data);
        NewVersionCheckModel ret;
        ret = JsonUtils.fromJson(data, NewVersionCheckModel.class);

        if (ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
            DataUtil.getToast(ret.getServerResult().getResultMessage());
        } else { //有新版本
            if ("1".equals(ret.getIsNeedUpdate())) {
                if (!DataUtil.isNullorEmpty(ret.getUpdateAddress())) {
                    NewUpdateVer uv = new NewUpdateVer(ret.getUpdateExplain(), ret.getUpdateAddress(), this);
                    //强制更新-
                    if (ret.getIsUpdate() == 1) {
                        uv.mustUp = true;
                    }
                    uv.checkVer();
                } else {
                    DataUtil.getToast("无效的apk下载地址");
                }
            } else {//无新版本
                NewMemberBean member = CCApplication.app.getMemberInfo();
                member.setUpdateFlag("0");
                CCApplication.app.setMember(member);
            }
        }
    }

    private void parseLoginInfoData(String data) {
        System.err.println("解析登录信息:" + data);
        MemberDetailBean loginDetail = JsonUtils.fromJson(data, MemberDetailBean.class);
        loginDetail = DataUtil.sortUserList(loginDetail);
        CCApplication.app.setMemberDetail(loginDetail);
        Log.i("chat", "userName:" + loginDetail.getHxUsername() + " psw:" + loginDetail.getHxPassword());
//		CCApplication.getInstance().setUserName(loginDetail.getHxUsername());
//		CCApplication.getInstance().setPassword(loginDetail.getHxPassword());
        LoginEmob();
//		LoginEmob(loginDetail.getHxUsername(),loginDetail.getHxPassword());
        //  afterLogin();
    }

    //班级排序，班主任的班级排前面
    public void bubbleSort(MemberDetailBean loginDetail) {

    }

    /**
     * 登录后进行后续操作
     */
    private void afterLogin() {
        if (CCApplication.getInstance().isCurUserParent()) {
            if (detail != null) {
                //存userid和childid
                String userId = CCApplication.getInstance().getPresentUser().getUserId();

                if (!commitUser(userId, detail.getChildList().get(0).getChildID(), "0")) {
                    DataUtil.getToast("本地化用户信息失败，请检查存储空间是否已满！ ");
                }
            }
        }
        String userId = CCApplication.getInstance().getPresentUser().getUserId();
        CrashReport.setUserId(userId);

        goHome();
    }


    //获取 本机 dpi 类型
    public String getDpi() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int densityDpi = dm.densityDpi;
        if (densityDpi == 320) {
            return "xhdpi";
        }
        if (densityDpi == 240) {
            return "hdpi";
        }
        if (densityDpi == 160) {
            return "mdpi";
        }
        if (densityDpi == 120) {
            return "ldpi";
        }
        return "xhdpi";

    }

    /**
     * 启动页 获取
     */
    private void getLanucherPage() {
        try {
            UserType userType = CCApplication.app.getPresentUser();
            String schoolId = userType.getSchoolId();

            JSONObject json = new JSONObject();
            try {
                json.put("schoolId", schoolId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new HttpRequestAsyncTask(json, httpRequestAsyncTaskListener, this).execute(Constants.GET_IMAGE_URL);

        } catch (Exception e) {

        }

    }

    /**
     * 跳入首页
     */
    private void goHome() {
        Bundle bundle = new Bundle();
        String ver = getCurVersion();

        if (Integer.parseInt(ver) < Constants.NEW_VERSION) {
            newActivity(HomeWebviewActivity.class, bundle);
        } else {
            newActivity(HomeActivity.class, bundle);
        }
        LoginActivity.this.finish();
    }

    /**
     * 登录环信.
     */
    public void LoginEmob() {
        System.out.println("LoginActivity登录环信!!");
        Intent intent = new Intent(this, ChatLoginService.class);
        // 启动服务  
        startService(intent);
    }

//	private void LoginEmob(final String userName, final String password){
//		EMChatManager.getInstance().login(userName, password, new EMCallBack() {
//			@Override
//			public void onSuccess() {
//				// 登录陆成功，保存用户名密码
//				System.out.println("loginEmob success!!!");
//				CCApplication.getInstance().setUserName(userName);
//				CCApplication.getInstance().setPassword(password);
//				try {
//					EMGroupManager.getInstance().getAllGroups();
//					EMChatManager.getInstance().getAllConversations();
//				}catch (Exception e){
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onProgress(int progress, String status) {
//			}
//
//			@Override
//			public void onError(int code, String message) {
//				//如果聊天登录失败了，那么清除数据
//				System.out.println("loginEmob failed:"+code+":"+message);
//				CCApplication.getInstance().setPassword(null);
//				new Thread(runnable).start();
//			}
//		});
//	}

//	private Runnable runnable = new Runnable() {
//		@Override
//		public void run() {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			if(detail != null){
//				Log.e("chat","getHxUsername:"+detail.getHxUsername()+" getHxPassword:"+detail.getHxPassword());
//				LoginEmob(detail.getHxUsername(), detail.getHxPassword());
//			}
//		}
//	};

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        try {

            if (conflictBuilder == null)
                conflictBuilder = new android.app.AlertDialog.Builder(LoginActivity.this);
//				conflictBuilder.setTitle("下线通知");
            conflictBuilder.setMessage(R.string.connect_conflict);
            System.out.println("环信下线");
            conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    conflictBuilder = null;
                }
            });
            conflictBuilder.setCancelable(false);
            conflictBuilder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新Intent.
     *
     * @param intent Intent类型
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
                System.out.println("环信onNewIntent:" + intent);
                showConflictDialog();
            }
        } catch (Exception e) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow) {
                System.out.println("环信onResume:showConflictDialog");
                showConflictDialog();
            }
        } catch (Exception e) {

        }
    }

    protected void getLoginInfo() {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.app.getPresentUser();
        if (user == null) return;
        try {
            String userType = user.getUserType();
            json.put("userId", user.getUserId());
            json.put("userType", userType);
            json.put("schoolId", user.getSchoolId());
            if (Constants.PARENT_STR_TYPE.equals(userType)) {
                json.put("studentId", user.getChildId());
            }
            System.out.println("login getLoginInfo:" + json);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        action = ACTION_GET_LOGIN_INFO;
        queryPost(Constants.LOGIN_INFO_URL, json);
    }

    @Override
    public void onRequstComplete(String result) {
        // TODO Auto-generated method stub
        parseUpdateData(result);
    }

    @Override
    public void onRequstCancelled() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.print("requestCode:" + requestCode + " resultCode:" + resultCode);
        switch (requestCode) {
            case 1:
                if (RESULT_OK == resultCode) {
                    afterLogin();
                } else {
                    finish();
                }
                break;
            case VERIFY_RESULT:
                if (RESULT_OK == resultCode) {
                    login(userName, pwd);
                } else {

                }
                break;
        }
    }

    public void loginDHMoniter() {
        System.out.println("loginActivity loginDHMoniter");
        Intent intent = new Intent();
        intent.setClass(this, DHMoniterService.class);
        startService(intent);
    }

    //是否有登陆子贵校园的权限
    private boolean hasLoginRight(NewMemberBean member) {
        List<UserType> userList;
        if (member != null) {
            userList = member.getUserTypeList();
            for (UserType user : userList) {
                if (!Constants.ZIGUI_SCHOOL_ID.equals(user.getSchoolId())) {
                    return true;
                }
            }
        }
        return false;
    }

    //不显示子贵学苑的身份
    private List<UserType> filterItem(List<UserType> users) {
        if (users != null) {
            List<UserType> list = new ArrayList<UserType>();
            for (UserType user : users) {
                if (!Constants.ZIGUI_SCHOOL_ID.equals(user.getSchoolId())) {
                    list.add(user);
                }
            }
            return list;
        }
        return users;
    }

    private String initLoginData(String userName, String psw) {
        LoginBean login = new LoginBean(userName, psw);
        Gson gson = new Gson();
        phoneNum = userName;
        phonePwd = psw;
        CCApplication.app.clearData();
        return gson.toJson(login);
    }

    private void getLogin(final String userName, String psw) {
        String json = initLoginData(userName, psw);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL + Constants.LOGIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(CCApplication.getInstance().initClient())
                .build();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        ApiManager apiManager = retrofit.create(ApiManager.class);
        Call<NewMemberBean> call = apiManager.login(body);
        call.enqueue(new Callback<NewMemberBean>() {
            @Override
            public void onResponse(Call<NewMemberBean> call, Response<NewMemberBean> response) {
                member = response.body();
                if (member.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    handleResponse();
                } else {
                    handleError(member.getServerResult().getResultCode());
                }
            }

            @Override
            public void onFailure(Call<NewMemberBean> call, Throwable t) {
                System.out.println(t);
                DataUtil.getToast(t.getMessage());
            }
        });
    }

    private void handleResponse() {

        //去掉修改密码  去掉换机验证
        if ("1".equals(member.getIsNeedModifyPwd())) {
            changePassword(true);
        } else if ("1".equals(member.getIsVerification())) {
            Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
            intent.putExtra("mobile", userName);
            startActivityForResult(intent, VERIFY_RESULT);
        } else {
            //存储登录的电话号码
            CCApplication.app.setMember(member);
            //错误日志 用户id
            String userId = CCApplication.getInstance().getPresentUser().getUserId();
            CrashReport.setUserId(userId);
            //存储手机本地信息
            savaPhoneNum();
            //获取登录详情信息
            getLoginInfo();
            //登陆环信
            LoginEmob();
            loginDHMoniter();
            //提前预备 启动页
            getLanucherPage();
            afterLogin();
        }
    }

    private void handleError(int code) {
        switch (code) {
            case 458:
                loginactivity_password_et.requestFocus();
                break;
            case 402:
            case 102003:
                loginactivity_name_et.requestFocus();
                break;
            case 102001:
                loginactivity_name_et.requestFocus();
                DataUtil.getToast("输入账号错误");
                return;
            case 102002:
                DataUtil.getToast("输入密码错误");
                return;
            case Constants.ACCOUNT_DISABLE_CODE:
                loginactivity_name_et.requestFocus();
                DataUtil.getToast("账户被禁用，请与管理员联系");
                return;
            default:
                break;
        }
        DataUtil.getToast(member.getServerResult().getResultMessage());
    }
}
