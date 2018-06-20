/**
 * 文 件 名:BaseActivity.java
 * 创 建 人： 姜韵雯
 * 日    期： 2014-09-23
 * 版 本 号： 1.01
 */
package com.wcyc.zigui2.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

//import com.bugtags.library.Bugtags;
import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.message.PushAgent;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.bean.BaseBean;
import com.wcyc.zigui2.bean.ClassDynamicsBean;
import com.wcyc.zigui2.chat.MainActivity;

import com.wcyc.zigui2.newapp.adapter.AttachmentListAdapter;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargePriceActivity;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargeProductActivity;
import com.wcyc.zigui2.newapp.module.charge2.PayFailActivity;
import com.wcyc.zigui2.newapp.module.order.MyOrderActivity;
import com.wcyc.zigui2.newapp.module.order.PayUtil;
import com.wcyc.zigui2.newapp.widget.AttachmentActionOption;
import com.wcyc.zigui2.utils.CCBaseModel;
import com.wcyc.zigui2.utils.Callbacks;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.utils.RequestHeader;

import static anet.channel.util.Utils.context;
import static com.taobao.accs.ACCSManager.mContext;

/**
 * Activity基类.
 *
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public abstract class BaseActivity extends TaskBaseActivity {
    private ImageLoader mImageLoader;
    protected DisplayImageOptions mOptions;

    //存储本地数据
    private SharedPreferences sp;
    private ProgressDialog pd;
    protected boolean isLoading = false;
    protected final int RETURN_DATA = 0;
    protected final int RETURN_CODE = 1;
    protected CCBaseModel model;
    protected Map<String, Object> mBitmapMap = new HashMap<String, Object>();
    protected List<ClassDynamicsBean> list;
    protected UserType user;
    protected NewMemberBean member;
    /**
     * 当一个界面有很多接口的时候，用这个字段来判断我当前是调用的哪个接口
     */
    protected int action;
    private String deviceID;
    protected String userName, pwd, schoolId;

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RETURN_DATA:
                    String data = (String) msg.obj;
                    getMessage(data);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 得到消息.
     *
     * @param data http通信后返回的json格式的字符串
     */
    protected abstract void getMessage(String data);

    private Button[] mTabs;
    // 未读消息textview
    private TextView unreadLabel;
    public final static int VIEW_FILE = 0;
    public final static int UPDATE_VERSION = 1;
    protected int touchSlop, xDown, yDown, xMove, yMove;
    protected boolean isSliding;
    protected View mCurrentView;
    protected int mCurrentViewPos;
    public static final String UPDATE_VERSION_SUCCESS = "com.wcyc.zigui2.action.update_version_success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Image loader init
        PushAgent.getInstance(this).onAppStart();
        touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        setImageLoader(ImageLoader.getInstance());
        mOptions = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .showImageForEmptyUri(R.drawable.default_image)  // empty URI时显示的图片
                .showImageOnFail(R.drawable.default_image)       // 不是图片文件 显示图片
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .resetViewBeforeLoading(false)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        model = new CCBaseModel(this, callbacks);
        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        if (deviceId == null) {
            // android pad
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        setDeviceID(deviceId);
        CCApplication.getInstance().setDeviceId(deviceId);
        member = CCApplication.getInstance().getMemberInfo();
        CCApplication.getInstance().getPassword();
        user = CCApplication.getInstance().getPresentUser();
        if (CCApplication.app.getPresentUser() != null) {
            userName = CCApplication.app.getPresentUser().getUserId();
            schoolId = CCApplication.app.getPresentUser().getSchoolId();
        } else {
            userName = CCApplication.app.getUserName();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //onresume时，取消notification显示
        EMChatManager.getInstance().activityResumed();
//		Bugtags.onResume(this);
    }

    protected void onPause() {
        super.onPause();
//		Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
//		Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    protected Callbacks callbacks = new Callbacks<Object>() {
        @Override
        public void startBinding() {
            isLoading = true;
        }

        @Override
        public void bindItems(List<Object> list) {
        }

        @Override
        public void bindItems(List<Object> list, int index) {
        }

        @Override
        public void bindItem(Object item) {
            if (item != null) {
                Message msg = new Message();
                msg.what = RETURN_DATA;
                msg.obj = item;
                handler.sendMessage(msg);
            } else {
                DataUtil.getToast("连接服务器失败");
            }
        }

        @Override
        public void finishBindingItems() {
            isLoading = false;
        }

        @Override
        public void bindCode(int code) {
            if (code == -1) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
            }
        }
    };

    /**
     * @return 是否还处于上一个http操作中
     */
    public boolean isLoading() {
        if (isLoading) {
            DataUtil.getToast("正在处理中，请稍候");
        }
        return isLoading;
    }

    /**
     * 用户ID和会话ID写入本地
     *
     * @param userID         用户ID
     * @param childID        孩子ID
     * @param teacherClassID 教师班级ID
     * @return 是否提交
     * @author ph
     */
    public boolean commitUser(String userID, String childID, String teacherClassID) {
        if (sp == null) {
            sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
            // 用户id
        }
        Editor ed = sp.edit();
        ed.putString("userID", userID);
        ed.putString("childID", childID);
        ed.putString("teacherClassID", teacherClassID);
        return ed.commit();
    }

    //by ph

    /**
     * 设置写入本地.
     *
     * @param ifMsg   消息
     * @param ifVoice 声音
     * @param ifShake 抖动
     * @return 是否提交
     */
    public boolean commitSetting(String ifMsg, String ifVoice, String ifShake) {
        if (sp == null) {
            sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
            // 用户id
        }
        Editor ed = sp.edit();
        ed.putString("ifMsg", ifMsg);
        ed.putString("ifVoice", ifVoice);
        ed.putString("ifShake", ifShake);
        return ed.commit();
    }

    public boolean commitUserNameAndPhone(boolean isTeacher, String name, String phone) {
        if (sp == null) {
            sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
        }
        Editor ed = sp.edit();
        if (isTeacher) {
            ed.putString("teachername", name);
            ed.putString("teacherphone", phone);
        } else {
            ed.putString("patriarchname", name);
            ed.putString("patriarchphone", phone);
        }
        return ed.commit();
    }

    public String getUserName(boolean isTeacher) {
        String name = "";
        if (sp == null) {
            sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
        }
        if (isTeacher) {
            name = sp.getString("teachername", "");
        } else {
            name = sp.getString("patriarchname", "");
        }
        return name;
    }

    public String getUserPhone(boolean isTeacher) {
        String phone = "";
        if (sp == null) {
            sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
        }
        if (isTeacher) {
            phone = sp.getString("teacherphone", "");
        } else {
            phone = sp.getString("patriarchphone", "");
        }
        return phone;
    }

    //by ph

    /**
     * 获取ifMsg.
     *
     * @return 是否接受新消息
     */
    public String getIfMsg() {
        String ifMsg = null;
        if (sp == null) {
            sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
        }
        ifMsg = sp.getString("ifMsg", "");
        return ifMsg;
    }

    //by ph

    /***
     * 获取ifVoice
     * @return 是否提示音
     */
    public String getIfVoice() {
        String ifVoice = null;
        if (sp == null) {
            sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
        }
        ifVoice = sp.getString("ifVoice", "");
        return ifVoice;
    }

    //by ph

    /***
     * 获取ifShake
     * @return 是否震动
     */
    public String getIfShake() {
        String ifShake = null;
        if (sp == null) {
            sp = getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
        }
        ifShake = sp.getString("ifShake", "");
        return ifShake;
    }

    //by ph

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

    //by ph

    /**
     * 获取系统本地版本号
     *
     * @return 系统本地版本号
     */
    public String getCurVersionName() {
        Properties properties = new Properties();

        try {
            InputStream stream = this.getAssets().open("ver.cfg");
            properties.load(stream);
        } catch (FileNotFoundException e) {
            return "1.0";
        } catch (IOException e) {
            return "1.0";
        } catch (Exception e) {
            return "1.0";
        }

        String version = String.valueOf(properties.get("VersionName").toString());

        return version;
    }

    public void showProgessBar(){
        if(pd==null){
            pd = new ProgressDialog(this);
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(true);
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //cancel(true);
                    dismissPd();

                }
            });
            pd.show();
            pd.setContentView(R.layout.progress_bar);
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.getWindow().setBackgroundDrawableResource(R.color.transparent);
        }else {
            if(!pd.isShowing()){
                pd.show();
            }
        }


    }

    public void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    protected void initView(BaseActivity activity, int name) {
        TextView titleText2 = (TextView) findViewById(R.id.title_text_2);
        Button title_imgbtn = (Button) findViewById(R.id.title_btn);
        ImageView image = (ImageView) findViewById(R.id.title_arrow_iv);
        title_imgbtn.setVisibility(View.GONE);
        titleText2.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        titleText2.setText(name);
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
    }

    public String getDisplayResolution() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        String resolution = dm.heightPixels + "*" + dm.widthPixels;
        return resolution;
    }

    public void queryPost(String url, JSONObject json) {
        RequestHeader header = new RequestHeader(this);
        if (model != null)
            model.queryPost(url, header, json);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
          dismissPd();

    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void setImageLoader(ImageLoader mImageLoader) {
        this.mImageLoader = mImageLoader;
    }

    public DisplayImageOptions getImageLoaderOptions() {
        return mOptions;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCode:" + requestCode + " resultCode:" + resultCode + " data:" + data);
        switch (requestCode) {
            case VIEW_FILE:
                AttachmentActionOption.delFile();
                break;
            case UPDATE_VERSION:
                if (resultCode == RESULT_CANCELED) {
                    CCApplication.app.finishAllActivity();
                } else {
                    Intent intent = new Intent(UPDATE_VERSION_SUCCESS);
                    sendBroadcast(intent);
                }
                break;

        }
    }

}



