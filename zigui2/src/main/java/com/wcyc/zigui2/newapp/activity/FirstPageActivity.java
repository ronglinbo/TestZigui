package com.wcyc.zigui2.newapp.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.dh.DHMoniterService;


import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.HeEducationUserBean;
import com.wcyc.zigui2.newapp.bean.LoginBean;
import com.wcyc.zigui2.newapp.service.ChatLoginService;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.NewVersionCheckModel;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.updatesystem.NewUpdateVer;
import com.wcyc.zigui2.utils.ApiManager;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.RequestHeader;
import com.wcyc.zigui2.utils.SPUtils;
import com.wcyc.zigui2.utils.SystemUtils;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstPageActivity extends BaseActivity implements HttpRequestAsyncTaskListener {

    private static final int GET_LOGIN_INFO = 0x001;
    private ImageView imageView;
    private String appToken; //和教育传递AppToken

    private HttpRequestAsyncTaskListener httpRequestAsyncTaskListener = new HttpRequestAsyncTaskListener() {
        @Override
        public void onRequstComplete(String result) {
            parseQIDong(result);
        }

        @Override
        public void onRequstCancelled() {

        }
    };

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isFirst()) {
                goSplashActivity();
            } else {
                goLoginActivity();
            }
        }
    };
    private HeEducationUserBean heEducationUserBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_pager);

        IntentFilter filter = new IntentFilter(UPDATE_VERSION_SUCCESS);
        registerReceiver(receiver, filter);


        if (checkSign()) return;

        initView();

        if (checkRootAndSimulator()) {
            return;
        }

        getAppToken();
    }

    /**
     * 校验签名问题.防止二次打包
     */
    private boolean checkSign() {
        boolean signCheck = CCApplication.getInstance().isSignCheck();
        if (!signCheck) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("请前往官方渠道下载正版app");
            builder.setPositiveButton("立即安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Uri apkDownUrl = Uri.parse(Constants.MARKET_APK_DOWNLOAD);
                    FirstPageActivity.this.startActivity(new Intent("android.intent.action.VIEW", apkDownUrl));
                    finish();
                }
            });
            builder.setNegativeButton("取消", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return false;
    }


    private boolean checkRootAndSimulator() {
        boolean isRoot = SystemUtils.isRoot();
        boolean isSimulator = SystemUtils.checkSimulator(FirstPageActivity.this);
        boolean isFirst = (boolean) SPUtils.get(CCApplication.applicationContext, "CHECK_ROOT", "ROOT", false);
        if (isRoot && !isFirst) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("检测到手机已Root,存在风险隐患，是否继续运行？");
            builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SPUtils.put(CCApplication.applicationContext, "CHECK_ROOT", "ROOT", true);
                    getAppToken();

                }
            });
            builder.setNegativeButton("离开", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CCApplication.getInstance().finishAllActivity();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return false;
    }


    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    private void getAppToken() {

        CCApplication.getInstance().setHeAutoLogin(false);
        Intent intent = getIntent();
        try {
            if (intent != null) {
                appToken = intent.getStringExtra("appToken");
                if (appToken != null) {
                    CCApplication.getInstance().setAppToken(appToken);
                }
            }
        } catch (Exception e) {
            System.out.println("没有获取到 appToken");
        }

        appToken = CCApplication.getInstance().getAppToken();
        if (!DataUtil.isNullorEmpty(appToken)) {
            new GetHeInfoAsyncTask().execute();
        } else {
            initLoginDetail();
        }
    }


    private void parseHeUserData(String jsonStr) {
        System.out.println(jsonStr);
        heEducationUserBean = JsonUtils.fromJson(jsonStr, HeEducationUserBean.class);
        //判断缴费状态
        int queryResult = heEducationUserBean.getFreeQueryResult();
        if (1 != queryResult) {
            DataUtil.getToast("尚未开通全课通服务,请联系客服开通");
            CCApplication.app.clearData();
            CCApplication.getInstance().stopReceiver();
            CCApplication.getInstance().clearNotification();
            CCApplication.app.finishAllActivity();

            //跳转到登录页面
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

//            finish();
        } else {
            CCApplication.getInstance().setHeEducationDetail(jsonStr);
            registerHeUser();
        }
    }

    private void registerHeUser() {
        new RegisterAsyncTask().execute();
    }


    private class GetHeInfoAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            String url = Constants.URL + Constants.GET_HE_EDU_USER_INFO;
            JSONObject json = new JSONObject();
            try {
                json.put("app_token", appToken);
                result = HttpHelper.httpPostJson(FirstPageActivity.this, url, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!DataUtil.isNullorEmpty(result)) {
                parseHeUserData(result);
            } else {
                DataUtil.getToast("获取和教育用户数据失败,请联系客服");
                CCApplication.app.clearData();
                CCApplication.getInstance().stopReceiver();
                CCApplication.getInstance().clearNotification();
                CCApplication.app.finishAllActivity();
                FirstPageActivity.this.finish();
            }
        }
    }


    /**
     * 和教育注册用户的Task
     */
    private class RegisterAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String url = Constants.URL + Constants.REGIST_QKT_INFO;
            JSONObject json = new JSONObject();
            try {
                json.put("app_token", appToken);
                result = HttpHelper.httpPostJson(FirstPageActivity.this, url, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                JSONObject jsonRoot = null;
                try {
                    jsonRoot = new JSONObject(result);
                    String hePhone = jsonRoot.getString("phone");
                    String hePwd = jsonRoot.getString("pwd");
                    if (!DataUtil.isNullorEmpty(hePhone) && !DataUtil.isNullorEmpty(hePwd)) {
                        CCApplication.getInstance().savePhoneNum(hePhone);
                        CCApplication.getInstance().savePhonePwd(hePwd);
                        CCApplication.getInstance().setHeAutoLogin(true);
                    }

                    DataUtil.getToast("欢迎回来:" + heEducationUserBean.getResult().getUserInfo().getNickName());

                    initLoginDetail();

                } catch (JSONException e) {
                    e.printStackTrace();

                    DataUtil.getToast("注册用户失败,请联系客服");
                    CCApplication.app.clearData();
                    CCApplication.getInstance().stopReceiver();
                    CCApplication.getInstance().clearNotification();
                    CCApplication.app.finishAllActivity();
                    FirstPageActivity.this.finish();
                }
            }
        }

    }


    private void initLoginDetail() {

        checkVersionUpdate(getDeviceID(), "android", getCurVersion());

        NewMemberBean member = CCApplication.app.getMemberInfo();
        if (member != null) {
            //启动页面
            String qidong = CCApplication.dbsp.getString("QidongUrl");
            if (CCApplication.dbsp.getString("QidongUrl").equals("")) {
                getLanucherPage();
            } else {
                if (CCApplication.dbsp.getString("QidongUrl").equals("zigui")) {
                    imageView.setImageResource(R.drawable.splash);
                } else {
                    try {
                        Picasso.with(this).load(CCApplication.dbsp.getString("QidongUrl")).into(imageView);
                    } catch (Exception e) {

                    }

                }
            }

            String strupdateFlag = member.getUpdateFlag();
            String isNeedModifyPsd = member.getIsNeedModifyPwd();
            if (strupdateFlag != null) {
                int updateFlag = Integer.valueOf(strupdateFlag);
                if ("1".equals(isNeedModifyPsd)) {
                    //修改密码
                } else {
                    //登陆自己服务器刷新数据
                    Log.i("update", "登陆自己服务器刷新数据");
                    Login();
                }
            }
        } else {

            //解决和教育 第一次过来实现自动登录功能.
            String phonePwd = getPhonePwd();
            String phoneNum = getPhoneNum();
            boolean autoLogin = CCApplication.getInstance().getHeAutoLogin();

            if (autoLogin && !DataUtil.isNullorEmpty(phonePwd) && !DataUtil.isNullorEmpty(phoneNum)) {
                Login();
            }
            imageView.setImageResource(R.drawable.splash);
        }
    }

    private void parseQIDong(String data) {
        System.out.print(data);
        try {

            JSONObject jsonObject = new JSONObject(data);
            jsonObject = jsonObject.getJSONObject("imageUrlList");
            String url = Constants.URL + "/" + jsonObject.get(getDpi());
            Picasso.with(this).load(url).into(imageView);
            CCApplication.dbsp.putString("QidongUrl", url);
        } catch (JSONException e) {
            //没有的话 默认 子贵校园
            CCApplication.dbsp.putString("QidongUrl", "zigui");
        }
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

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case GET_LOGIN_INFO:
                parseGetInfo(data);
                break;
        }
    }

    private void parseGetInfo(final String result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CCApplication.app.setMemberDetail(result);
                MemberDetailBean detail = CCApplication.app.getMemberDetail();
                detail = DataUtil.sortUserList(detail);
                if (detail != null) {
                    CCApplication.getInstance().setUserName(detail.getHxUsername());
                    CCApplication.getInstance().setPassword(detail.getHxPassword());
                }
                //登陆环信
                LoginEmob();
                loginDHMoniter();
            }
        }).start();
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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
     * 更新版本.
     *
     * @param deviceID   硬件ID
     * @param mobileType pe 系统版本：“android”
     * @param version    版本:1.0
     */
    private void checkVersionUpdate(String deviceID, String mobileType, String version) {
        JSONObject json = new JSONObject();
        try {
            json.put("deviceId", deviceID);
            json.put("versionType", mobileType);
            json.put("versionNumber", version);
            json.put("productName", 3); // 0 子贵校园 1 子贵学苑 2 子贵课堂  3全课通
            System.out.println("FirstPageActivity获取版本更新json:" + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpRequestAsyncTask(json, this, this, false).execute(Constants.VERSION_CHECK);
    }

    /**
     * 是否是第一次进入应用
     *
     * @return 如果是第一次进入应用，返回true，否则返回 false
     */
    public boolean isFirst() {
        SharedPreferences sp = getSharedPreferences("little_data", 1);

        return sp.getBoolean("isFirstInApp", true);
    }

    /**
     * 跳入splash界面
     */
    private void goSplashActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 1000);

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                newActivity(LoginActivity.class, null);
                FirstPageActivity.this.finish();
            }

            if (msg.what == 2) {
                newActivity(NewSplashActivity.class, null);
                FirstPageActivity.this.finish();
            }

        }
    };

    /**
     * 跳入登录界面.
     */
    public void goLoginActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 1000);

    }

    /**
     * 登陆自己服务器，刷新用户数据
     */
    public void Login() {
        new LoginAsyncTask().execute(getPhoneNum(), getPhonePwd(), "");
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
     * 处理系统更新返回数据.
     *
     * @param data 系统更新返回的数据
     */
    private void parseUpdateData(String data) {
        Log.d("update", "获取版本更新数据: " + data);
        NewVersionCheckModel ret;
        ret = JsonUtils.fromJson(data, NewVersionCheckModel.class);
        if (ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
            DataUtil.getToast(ret.getServerResult().getResultMessage());
            if (isFirst()) {
                goSplashActivity();
            } else {
                goLoginActivity();
            }
        } else { //有新版本
            if ("1".equals(ret.getIsNeedUpdate())) {
                if (!DataUtil.isNullorEmpty(ret.getUpdateAddress())) {
                    NewUpdateVer uv = new NewUpdateVer(ret, this);
                    //强制更新
                    if (ret.getIsUpdate() == 1) {
                        uv.mustUp = true;
                    }
                    uv.checkVer();
                } else {
                    DataUtil.getToast("无效的apk下载地址");
                }
            } else {//无新版本
                NewMemberBean member = CCApplication.app.getMemberInfo();
                if (member != null) {
                    member.setUpdateFlag("0");
                    CCApplication.app.setMember(member);
                }
                if (isFirst()) {
                    goSplashActivity();
                } else {
                    goLoginActivity();
                }
            }
        }
    }

    @Override
    public void onRequstComplete(String result) {

        parseUpdateData(result);


    }

    @Override
    public void onRequstCancelled() {
        System.out.println("onRequstCancelled");
        if (isFirst()) {
            goSplashActivity();
        } else {
            goLoginActivity();
        }
    }

    /**
     * 读取登陆信息的异步线程类
     */
    class LoginAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String version = getCurVersion();
            String pwdMd5 = DataUtil.encodeMD5(params[1]);
            JSONObject json = new JSONObject();
            try {
                json.put("userName", params[0]);
                json.put("password", /*pwdMd5*/params[1]);
                //去掉换机验证
                json.put("isNeedVerify", "0");
                String url = new StringBuilder(Constants.SERVER_URL).append(Constants.LOGIN_URL)
                        .toString();
                RequestHeader header = new RequestHeader(FirstPageActivity.this);
                result = HttpHelper.httpPostJson(url, header, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                System.err.println("LoginAsyncTask onPostExecute:" + result);
                member = JsonUtils.fromJson(result, NewMemberBean.class);
                if (member.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    handleRespone();
                } else {
                    handleError();
                }
            }
        }

    }

    /**
     * 登录环信.
     */
    public void LoginEmob() {
        System.out.println("firstPageActivity登录环信");

        Intent intent = new Intent(CCApplication.applicationContext, ChatLoginService.class);
        // 启动服务
        startService(intent);
    }

    protected void getLoginInfo() {

        //修改为异步方式:
        NewMemberBean member = CCApplication.getInstance().getMemberInfo();
        if (member != null) {
            JSONObject json = new JSONObject();
            UserType user = CCApplication.app.getPresentUser();
            try {
                String userType = user.getUserType();
                json.put("userId", user.getUserId());
                json.put("userType", userType);
                json.put("schoolId", user.getSchoolId());
                if (Constants.PARENT_STR_TYPE.equals(userType)) {
                    json.put("studentId", user.getChildId());
                }
                System.out.println("Firstpage getLoginInfo:" + json);
//                String url = new StringBuilder(Constants.SERVER_URL).append(Constants.LOGIN_INFO_URL).toString();

                action = GET_LOGIN_INFO;
                queryPost(Constants.LOGIN_INFO_URL, json);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void loginDHMoniter() {
        System.out.println("FirstPageActivity loginDHMoniter");
        Intent intent = new Intent();
        intent.setClass(CCApplication.applicationContext, DHMoniterService.class);
        startService(intent);
    }

    private String initLoginData(String userName, String psw) {
        LoginBean login = new LoginBean(userName, psw);
        Gson gson = new Gson();
//		CCApplication.app.clearData();
        return gson.toJson(login);
    }

    private void handleError() {
        DataUtil.getToast(member.getServerResult().getResultMessage());
        new Thread(new Runnable() {
            @Override
            public void run() {

                CCApplication.app.clearData();
                CCApplication.app.finishAllActivity();

//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
            }
        }).start();
    }

    private void handleRespone() {
        CCApplication.app.setMember(member);
        CCApplication.getInstance().setPresentUser(null);

        //校验是否需要换机验证
        //全课通 去掉换机验证
//        if ("1".equals(member.getIsVerification())) {
//            CCApplication.app.singleLogout();
//            ChooseTeacherActivity.teacherSelectInfo = null;// 初始化部门信息
//            CCApplication.app.finishAllActivity();
//            Intent intent = new Intent(FirstPageActivity.this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("conflict", true);
//            startActivity(intent);
//            finish();
//        } else {
//            getLoginInfo();
//        }

        getLoginInfo();
    }

    private void getLogin(final String userName, String psw) {
        String json = initLoginData(userName, psw);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL + "/")
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
                    handleRespone();
                } else {
                    handleError();
                }
            }

            @Override
            public void onFailure(Call<NewMemberBean> call, Throwable t) {
                System.out.println(t);
                DataUtil.getToast(t.getMessage());
            }
        });
    }
}
