package com.wcyc.zigui2.newapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;

import org.json.JSONObject;

public class testnetActivity extends BaseActivity  implements HttpRequestAsyncTaskListener{

    @Override
    protected void getMessage(String data) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testnet);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderId", 67);
            jsonObject.put("paymentPlatformType", "ALIPAY");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new HttpRequestAsyncTask(jsonObject,this,this).execute("http://10.0.7.104:8088/zgkt/clientApi/getPaymentInfo.do");
    }

    @Override
    public void onRequstComplete(String result) {
        System.out.print("result:"+result);
    }

    @Override
    public void onRequstCancelled() {
        System.out.print("失败");
    }
}
