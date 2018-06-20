package com.dh;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Login_Info_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.dpsdk_retval_e;
import com.dh.groupTree.GroupListActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.MoniterListInfo;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DHMoniterService extends Service implements HttpRequestAsyncTaskListener {
    static IDpsdkCore dpsdkcore = new IDpsdkCore();

    Resources res;

    //标记是否第一次登入
    private String isfirstLogin;
    protected ProgressDialog mProgressDialog;
    private int mDPSDKHandler;
    private int mloginHandle;
    private CCApplication mAPP = CCApplication.get();
    private MoniterListInfo info;

    public DHMoniterService() {

    }

    public void onCreate() {
        super.onCreate();
        System.out.println("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isfirstLogin = getSharedPreferences("LOGININFO", 0).getString("ISFIRSTLOGIN", "");
        if (isfirstLogin.equals("false")) {
            setEditTextContent();
        }
//        if(SDK_INT > 22) {
//            showLoadingProgress(R.string.login);
//        }
        getMoniterInfo();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return null;
    }


    class LoginTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... arg0) {//在此处处理UI会导致异常
            int nRet = -1;
            if (mloginHandle != 0) {
                IDpsdkCore.DPSDK_Logout(mloginHandle, 30000);
                mloginHandle = 0;
            }
            if (info != null) {
                MoniterListInfo.InfoIpcSchoolInfo schoolInfo = info.getInfoIpcSchoolInfo();
                if (schoolInfo != null) {
                    Login_Info_t loginInfo = new Login_Info_t();
                    Integer error = Integer.valueOf(0);
                    loginInfo.szIp = schoolInfo.getHostIp().getBytes();
                    loginInfo.nPort = (int) schoolInfo.getHostPort();
                    loginInfo.szUsername = schoolInfo.getUser().getBytes();
                    loginInfo.szPassword = schoolInfo.getPass().getBytes();
                    loginInfo.nProtocol = 2;
                    saveLoginInfo(schoolInfo);
                    System.out.println("ip:" + loginInfo.szIp + " port:" + loginInfo.nPort + " userName:"
                            + loginInfo.szUsername + " password:" + loginInfo.szPassword);
                    nRet = IDpsdkCore.DPSDK_Login(mAPP.getDpsdkCreatHandle(), loginInfo, 30000);
                }
            }
            return nRet;
        }

        @Override
        protected void onPostExecute(Integer result) {

            super.onPostExecute(result);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (result == 0) {

                //登录成功，开启GetGPSXMLTask线程
                new DHMoniterService.GetGPSXMLTask().execute();

                Log.d("DpsdkLogin success:", result + "");
                IDpsdkCore.DPSDK_SetCompressType(mAPP.getDpsdkCreatHandle(), 1);
                mAPP.setLoginHandler(1);
                mloginHandle = 1;
//                jumpToItemListActivity();
            } else {
                Log.d("DpsdkLogin failed:", result + "");
                Toast.makeText(getApplicationContext(), "监控服务器登录失败", Toast.LENGTH_SHORT).show();
                mAPP.setLoginHandler(0);
                mloginHandle = 0;
                //jumpToContentListActivity();
            }
        }
    }

    /**
     * 取出 sharedpreference的登录信息并显示
     */
    private void setEditTextContent() {
        SharedPreferences sp = getSharedPreferences("LOGININFO", 0);
        String content = sp.getString("INFO", "");
        String[] loginInfo = content.split(",");
        if (loginInfo != null) {
        }
    }

    private void saveLoginInfo(MoniterListInfo.InfoIpcSchoolInfo info) {
        SharedPreferences sp = getSharedPreferences("LOGININFO", 0);
        SharedPreferences.Editor ed = sp.edit();
        StringBuilder sb = new StringBuilder();
        sb.append(info.getHostIp()).append(",").append(info.getHostPort()).append(",")
                .append(info.getPass()).append(",").append(info.getUser());
        ed.putString("INFO", sb.toString());
        ed.putString("ISFIRSTLOGIN", "false");
        ed.commit();
    }

    protected void showLoadingProgress(int resId) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
        } else {
            mProgressDialog = ProgressDialog.show(this, null, getString(resId));
            mProgressDialog.setCancelable(false);
        }
    }

    public void jumpToItemListActivity() {
        Intent intent = new Intent();
        intent.setClass(this, GroupListActivity.class);
        //intent.setClass(this, ItemListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.getApplicationContext().startActivity(intent);
    }

    public void Logout() {
        System.out.println("DHMoniterService Logout");
        if (mAPP.getLoginHandler() == 0) {
            System.out.println("DHMoniterService Logout getLoginHandler = 0");
            return;
        }
        int nRet = IDpsdkCore.DPSDK_Logout(mAPP.getDpsdkCreatHandle(), 30000);

        if (0 == nRet) {
            mloginHandle = 0;
            mAPP.setLoginHandler(0);
        }
    }

    @Override
    public void onDestroy() {
        Logout();
        IDpsdkCore.DPSDK_Destroy(mAPP.getDpsdkCreatHandle());
        super.onDestroy();
    }

    //读取GPSXMl 模块
    class GetGPSXMLTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int nRet = GetGPSXML();
            return nRet;
        }

        @Override
        protected void onPostExecute(Integer result) {
//            Toast.makeText(DHMoniterService.this, "GetGPSXML nRet"+result, Toast.LENGTH_SHORT).show();
            System.out.println("GetGPSXML nRet" + result);
            super.onPostExecute(result);
        }

    }

    public int GetGPSXML() {
        int res = -1;
        Return_Value_Info_t nGpsXMLLen = new Return_Value_Info_t();
        int nRet = IDpsdkCore.DPSDK_AskForLastGpsStatusXMLStrCount(mAPP.getDpsdkCreatHandle(), nGpsXMLLen, 10 * 1000);
        if (nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS && nGpsXMLLen.nReturnValue > 1) {
            byte[] LastGpsIStatus = new byte[nGpsXMLLen.nReturnValue - 1];
            nRet = IDpsdkCore.DPSDK_AskForLastGpsStatusXMLStr(mAPP.getDpsdkCreatHandle(), LastGpsIStatus, nGpsXMLLen.nReturnValue);

            if (nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS) {

                //System.out.printf("获取GPS XML成功，nRet = %d， LastGpsIStatus = [%s]", nRet, new String(LastGpsIStatus));
                Log.d("GetGPSXML", String.format("获取GPS XML成功，nRet = %d， LastGpsIStatus = [%s]", nRet, new String(LastGpsIStatus)));
                try {
                    File file = new File(CCApplication.LAST_GPS_PATH); // 路径  sdcard/LastGPS.xml
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(LastGpsIStatus);
                    out.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else {
                //System.out.printf("获取GPS XML失败，nRet = %d", nRet);
                Log.d("GetGPSXML", String.format("获取GPS XML失败，nRet = %d", nRet));
            }
        } else if (nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS && nGpsXMLLen.nReturnValue == 0) {
            //System.out.printf("获取GPS XML  XMLlength = 0");
            Log.d("GetGPSXML", "获取GPS XML  XMLlength = 0");
        } else {
            //System.out.printf("获取GPS XML失败，nRet = %d", nRet);
            Log.d("GetGPSXML", String.format("获取GPS XML失败，nRet = %d", nRet));
        }
        //System.out.println();
        res = nRet;
        return res;
    }

    //获取监控服务器和摄像头列表
    private void getMoniterInfo() {


        JSONObject json = new JSONObject();
        String url = new StringBuilder(Constants.SERVER_URL)
                .append(Constants.GET_MONITER_LIST).toString();
        String result;
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
//            try {
//                result = HttpHelper.httpPostJson(this,url,json);
//                info = JsonUtils.fromJson(result, MoniterListInfo.class);
//                if(info != null && info.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
//                    if(info.getInfoIpcSchoolInfo() != null) {
//                        new LoginTask().execute();
//                        CCApplication.getInstance().setMoniterInfo(info);
//                    }
//                }else{
////                    DataUtil.getToast("获取监控信息错误");
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new HttpRequestAsyncTask(json, this, this, false).execute(Constants.GET_MONITER_LIST);
    }


    @Override
    public void onRequstComplete(String result) {

        info = JsonUtils.fromJson(result, MoniterListInfo.class);
        if (info != null && info.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
            if (info.getInfoIpcSchoolInfo() != null) {
                new LoginTask().execute();
                CCApplication.getInstance().setMoniterInfo(info);
            }
        } else {
//                    DataUtil.getToast("获取监控信息错误");
        }

    }

    @Override
    public void onRequstCancelled() {

    }
}
