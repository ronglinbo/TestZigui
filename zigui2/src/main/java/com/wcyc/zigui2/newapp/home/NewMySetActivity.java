package com.wcyc.zigui2.newapp.home;

import java.io.File;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SyncStatusObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.UpdateSystemBean;
import com.wcyc.zigui2.chat.PreferenceUtils;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.activity.VersionShuomingActivity;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewUpdateSystemBean;
import com.wcyc.zigui2.newapp.bean.NewVersionCheckModel;
import com.wcyc.zigui2.updatesystem.NewUpdateVer;
import com.wcyc.zigui2.updatesystem.UpdateVer;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.SlipButton;
import com.wcyc.zigui2.utils.SlipButton.OnChangedListener;

/**
 * 我的模块 设置页面
 *
 * @author yytan
 * @version 2.0
 */
public class NewMySetActivity extends BaseActivity implements OnClickListener {

    private final static String SWITCH_OPEN = "1";// 开关打开
    private final static String SWITCH_CLOSE = "0";// 开关关闭
    private RelativeLayout help, opinion, about, delete, myset_shuoming;
    private TextView myset_delete_count;
    private String msgSwStr, voiceSwStr, shakeSwStr;
    private NewUpdateSystemBean updateSystemBean;
    private static final String MOBILETYPE = "android";
    private EMChatOptions chatOption;
    private LinearLayout title_back;
    private TextView new_content;
    private RelativeLayout myset_new_msn;
    private boolean isCanUpdate = false;
    private TextView myset_version_tv;
    private ImageView myset_red_iv;
    private NewVersionCheckModel newVersionCheckModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_myset);
        System.out.println("=====getDeviceID()====" + getDeviceID());
        initView();
        initData();
        initEvents();
    }

    /**
     * 实例化控件
     */
    private void initView() {
        myset_shuoming = (RelativeLayout) findViewById(R.id.myset_shuoming); //版本更新说明
        help = (RelativeLayout) findViewById(R.id.myset_use_help);
        opinion = (RelativeLayout) findViewById(R.id.myset_opinion);
        about = (RelativeLayout) findViewById(R.id.myset_about);
        delete = (RelativeLayout) findViewById(R.id.myset_delete);
        myset_delete_count = (TextView) findViewById(R.id.myset_delete_count);
        title_back = (LinearLayout) findViewById(R.id.title_back);
        new_content = (TextView) findViewById(R.id.new_content);
        myset_new_msn = (RelativeLayout) findViewById(R.id.myset_new_msn);
        update = (RelativeLayout) findViewById(R.id.myset_new_update);
        myset_version_tv = (TextView) findViewById(R.id.myset_version_tv);
        myset_red_iv = (ImageView) findViewById(R.id.myset_red_iv);

    }

    /**
     * 此方法描述的是:设置静态数据
     */

    private void initData() {
        new_content.setText("设置");
        title_back.setVisibility(View.VISIBLE);
        myset_delete_count.setText(getFormatSize(getFolderSize(new File(
                Constants.CACHE_PATH))));


        String version = getCurVersion();
        if ("".equals(getDeviceID())) {
            DataUtil.getToast("无法获取版本DEVOCE_ID");
        } else if ("".equals(version)) {
            DataUtil.getToast("无法获取应用版本号");
        } else {
            httpBusiInerfaceForVersionUpDate(getDeviceID(), MOBILETYPE,
                    getCurVersion());
        }
    }

    /**
     * 效果控制
     */

    private void initEvents() {
        delete.setOnClickListener(this);
        help.setOnClickListener(this);
        opinion.setOnClickListener(this);
        about.setOnClickListener(this);
        title_back.setOnClickListener(this);
        myset_new_msn.setOnClickListener(this);
        myset_shuoming.setOnClickListener(this);
        update.setOnClickListener(this);
        update.setClickable(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:// 返回
                NewMySetActivity.this.finish();
                break;
            case R.id.myset_new_msn:// 新消息提醒
                newActivity(NewMsnActivity.class, null);
                NewMySetActivity.this.finish();
                break;
            case R.id.myset_use_help:// 需要修改
                if (!DataUtil.isNetworkAvailable(getBaseContext())) {
                    DataUtil.getToast(getResources().getString(R.string.no_network));// 当前网络不可用，请检查您的网络设置
                    break;
                }
                newActivity(NewUsedHelpActivity.class, null);
                break;
            case R.id.myset_opinion:
                newActivity(NewOpinionActivity.class, null);
                break;
            case R.id.myset_new_update:// 版本更新
                // 有新版本
                if (isCanUpdate) {
                    if (newVersionCheckModel.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                        if (!DataUtil.isNullorEmpty(newVersionCheckModel.getUpdateAddress())) {
                            NewUpdateVer uv = new NewUpdateVer(newVersionCheckModel, this);
                            System.out.println("==newVersionCheckModel==" + newVersionCheckModel);
                            //强制更新
                            if (newVersionCheckModel.getIsUpdate() == 1) {
                                uv.mustUp = true;
                            }
                            uv.checkVer();
                        } else {
                            DataUtil.getToast("无效的apk下载地址");
                        }
                    }
                } else {
                    DataUtil.getToast("当前已经是最新版本");
                }
                break;
            case R.id.myset_about:
                newActivity(NewAboutActivity.class, null);
                break;
            case R.id.myset_delete:// 清除缓存
                showClearCacheDialog();
                break;
            case R.id.myset_shuoming:// 版本更新说明
                newActivity(VersionShuomingActivity.class, null);
                break;
            default:
                break;
        }

    }

    // add jiang
    public ProgressDialog pd; // 进度条对话框
    private boolean cancelClear = false;// 取消清除缓存
    private android.app.AlertDialog.Builder clearCacheBuilder;

    /**
     * 显示确认清除缓存dialog
     */
    private void showClearCacheDialog() {
        try {
            if (clearCacheBuilder == null)
                clearCacheBuilder = new android.app.AlertDialog.Builder(
                        NewMySetActivity.this);
            clearCacheBuilder.setTitle(null);
            clearCacheBuilder.setMessage("确认删除缓存？");
            clearCacheBuilder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            clearCacheBuilder = null;
                            clearCache();
                        }
                    });
            clearCacheBuilder.setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            clearCacheBuilder = null;
                        }
                    });
            clearCacheBuilder.setCancelable(true);
            clearCacheBuilder.create().show();
        } catch (Exception e) {
        }
    }

    /**
     * 弹出进度框
     */
    public void showDialog(Context context, String dialogContent) {
        if (pd == null || !pd.isShowing()) {
            pd = ProgressDialog.show(context, null, dialogContent, true, true);
            pd.setCanceledOnTouchOutside(false);
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancelClear = true;
                    myset_delete_count
                            .setText(getFormatSize(getFolderSize(new File(
                                    Constants.CACHE_PATH))));
                }
            });
            pd.show();
        }
    }

    private static final String APP_CACAHE_DIRNAME = "/webcache";

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            boolean is = deleteDatabase("webview.db");
            boolean is1 = deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);
        Log.e("delete", "appCacheDir path=" + appCacheDir.getAbsolutePath());
        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "");
        Log.e("delete", "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
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

    /**
     * 清除缓存
     */
    public void clearCache() {
        cancelClear = false;
        showDialog(NewMySetActivity.this, "正在清除缓存");
        new Thread(new Runnable() {
            @Override
            public void run() {     //data/data/<包名>/app_webview
                deleteFolderFile(Constants.CACHE_PATH);
                clearWebViewCache();
//				deleteFolderFile(Environment.getExternalStorageDirectory().getAbsolutePath()
//						+ "/Android/data/"+getPackageName()+"/");
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            CCApplication.getDaoinstant().getChildMessageDao().deleteAll();
                            CCApplication.getDaoinstant().getLeaveMessageDao().deleteAll();
                        } catch (Exception e) {

                        }
                        myset_delete_count
                                .setText(getFormatSize(getFolderSize(new File(
                                        Constants.CACHE_PATH))));
                    }
                });
            }
        }).start();

    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public long getFolderSize(java.io.File file) {

        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // return size/1048576;
        return size;
    }

    /**
     * 删除指定目录下文件及目录.
     *
     * @param filePath 删除的文件路径
     */
    public void deleteFolderFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (cancelClear) {
                            return;
                        }
                        deleteFolderFile(files[i].getAbsolutePath());
                    }
                }
                if (!cancelClear) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                        String url = "";
                    } else {// 目录
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * jiang android 系统的单位不是以1024而是1000来进制
     */
    private double memoryUnit = 1000;
    private TextView versionTv;
    private RelativeLayout update;

    /**
     * 格式化单位
     *
     * @param size
     * @return 大小转化后的字符串
     */
    public String getFormatSize(double size) {
        double kiloByte = size / memoryUnit;
        if (kiloByte < 1) {
            return size + "Byte(s)";
        }

        double megaByte = kiloByte / memoryUnit;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / memoryUnit;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / memoryUnit;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    // end jiang

	/*
     * 版本更新 业务入口 userID String 用户ID deviceID String 硬件ID mobileType String
	 * 系统版本：“android” version float 版本:1.0
	 */

    private void httpBusiInerfaceForVersionUpDate(String deviceId,
                                                  String mobileType, String version) {
        JSONObject json = new JSONObject();
        try {
            json.put("deviceId", deviceId);
            json.put("versionType", mobileType);
            json.put("versionNumber", version);
            json.put("productName",3); // 0 子贵校园 1 子贵学苑 2 子贵课堂  3全课通
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isLoading()) {
            System.out.println("==版本更新入参==json===" + json);
            queryPost(Constants.VERSION_SEARCH, json);
        }
    }

    @Override
    protected void getMessage(String data) {

        System.out.println("=版本更新出参===data===" + data);

        newVersionCheckModel = JsonUtils.fromJson(data, NewVersionCheckModel.class);
        if (newVersionCheckModel.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
            DataUtil.getToast(newVersionCheckModel.getServerResult().getResultMessage());
        } else {
            //有新版本
            if ("1".equals(newVersionCheckModel.getIsNeedUpdate())) {
                isCanUpdate = true;
                update.setClickable(true);
                myset_red_iv.setVisibility(View.VISIBLE);
                myset_version_tv.setText(getVersion() + "");

            } else {//无新版本
                update.setClickable(false);
                myset_red_iv.setVisibility(View.GONE);
                myset_version_tv.setText("已是最新版本");
            }
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
