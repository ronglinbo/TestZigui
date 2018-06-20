package com.wcyc.zigui2.newapp.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dh.DHMoniterService;


import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.newapp.bean.LauncherInfoBean;
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
import com.wcyc.zigui2.utils.DensityUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.RequestHeader;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstPageActivity extends BaseActivity implements HttpRequestAsyncTaskListener {

    private static final int GET_LOGIN_INFO = 0x0001;
    private RelativeLayout rl_content;
    private ImageView iv_school_logo;
    private TextView tv_school_name;
    private ImageView iv_school_name;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        //  newActivity(PolyvPlayerActivity.class,null);

        initView();

        IntentFilter filter = new IntentFilter(UPDATE_VERSION_SUCCESS);
        registerReceiver(receiver, filter);
        NewMemberBean member = CCApplication.app.getMemberInfo();

        //有登录信息
        if (member != null) {

            //保证每次都是最新的启动页.
            getLauncherPage();

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
            //没有登录信息的情况下
            imageView.setImageResource(R.drawable.splash);
        }

        checkVersionUpdate(getDeviceID(), "android", getCurVersion());
    }

    /**
     */
    private void getLauncherPage() {

        try {
            UserType userType = CCApplication.app.getPresentUser();
            String schoolId = userType.getSchoolId();

            JSONObject json = new JSONObject();
            try {
                json.put("schoolId", schoolId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(json, httpRequestAsyncTaskListener, this).execute(Constants.GET_SCHOOL_LAUNCHER_INFO);

        } catch (Exception e) {

        }

    }


    private HttpRequestAsyncTaskListener httpRequestAsyncTaskListener = new HttpRequestAsyncTaskListener() {
        @Override
        public void onRequstComplete(String result) {
            parseLauncherData(result);
        }

        @Override
        public void onRequstCancelled() {

        }
    };

    private void parseLauncherData(String data) {
        System.out.print(data);
        try {
            LauncherInfoBean launcherInfoBean = JsonUtils.fromJson(data, LauncherInfoBean.class);
            if (Constants.SUCCESS_CODE == launcherInfoBean.getServerResult().getResultCode() && launcherInfoBean.getInfoSchoolStart() != null) {
                showIndividuationPicture(launcherInfoBean);
            } else {
                imageView.setImageResource(R.drawable.splash);
                rl_content.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.splash);
            rl_content.setVisibility(View.GONE);
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

    /**
     * 显示个性化启动页
     *
     * @param launcherInfo
     */
    private void showIndividuationPicture(LauncherInfoBean launcherInfo) {

        imageView.setVisibility(View.GONE);
        rl_content.setVisibility(View.VISIBLE);

        String imageLogoUrl = launcherInfo.getInfoSchoolStart().getImageUrl();
        int isTextDefault = launcherInfo.getInfoSchoolStart().getIsTextDefault();
        String textLogoUrl = launcherInfo.getInfoSchoolStart().getTextLogoUrl();
        String schoolName = launcherInfo.getInfoSchoolStart().getSchoolName();

        String imgAuthId = Constants.AUTHID + "@" + getDeviceID()
                + "@" + CCApplication.app.getMemberInfo().getAccId();


        //校徽
        if (!DataUtil.isNullorEmpty(imageLogoUrl)) {
            String logoURL = Constants.URL + "/" + imageLogoUrl + "&" + imgAuthId;
            getImageLoader().displayImage(logoURL, iv_school_logo, getImageLoaderOptions());
            System.out.println("Logo地址:" + logoURL);
        } else {
            iv_school_logo.setBackgroundResource(R.drawable.app_icon);
        }

        //校名
        if (0 == isTextDefault && !DataUtil.isNullorEmpty(textLogoUrl)) {
            iv_school_name.setVisibility(View.VISIBLE);
            tv_school_name.setVisibility(View.INVISIBLE);
            String logoURL = Constants.URL + "/" + textLogoUrl + "&" + imgAuthId;
            getImageLoader().displayImage(logoURL, iv_school_name, getImageLoaderOptions());
            System.out.println("textLogoUrl地址:" + logoURL);
        } else {
            tv_school_name.setVisibility(View.VISIBLE);
            iv_school_name.setVisibility(View.INVISIBLE);
            if (!DataUtil.isNullorEmpty(schoolName)) {
                tv_school_name.setText(schoolName);
            } else {
                tv_school_name.setText("子贵校园");
            }
        }
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);

        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        rl_content.setVisibility(View.GONE);

        iv_school_logo = (ImageView) findViewById(R.id.iv_school_logo);
        tv_school_name = (TextView) findViewById(R.id.tv_school_name);
        iv_school_name = (ImageView) findViewById(R.id.iv_school_name);
        iv_school_name.setAdjustViewBounds(true);

        //获取屏幕宽度
        WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(outMetrics);

        //计算宽高，后台默认图片宽高比 1080:238
        int width = outMetrics.widthPixels - DensityUtil.dp2px(this, 10f) * 2; //乘以2是因为左右两侧的宽度
        int height = (int) (width / 1080f * 238); //280*136

        //设置图片参数
        ViewGroup.LayoutParams layoutParams = iv_school_name.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        iv_school_name.setLayoutParams(layoutParams);
    }


    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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
            //  json.put("productName",0); // 0 子贵校园 1 子贵学苑 2 子贵课堂
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
                handler.sendEmptyMessage(2);
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

        System.out.println("Login登陆自己服务器");
        new LoginAsyncTask().execute(getPhoneNum(), getPhonePwd(), "");
//		getLogin(getPhoneNum(),getPhonePwd());
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
                CCApplication.app.logout();
                CCApplication.app.finishAllActivity();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }).start();
    }

    private void handleRespone() {
        CCApplication.app.setMember(member);
        CCApplication.getInstance().setPresentUser(null);

        //校验是否需要换机验证
        if ("1".equals(member.getIsVerification())) {
            CCApplication.app.singleLogout();
            ChooseTeacherActivity.teacherSelectInfo = null;// 初始化部门信息
            CCApplication.app.finishAllActivity();
            Intent intent = new Intent(FirstPageActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("conflict", true);
            startActivity(intent);
            finish();
        } else {
            //获取到个人信息之后  再去做其他操作
            getLoginInfo();
        }
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
