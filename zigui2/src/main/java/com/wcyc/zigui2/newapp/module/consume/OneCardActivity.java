package com.wcyc.zigui2.newapp.module.consume;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.PaymentH5Activity;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 一卡通充值页面
 * 只是比卡消费充值页面 少了一个消费记录
 */
public class OneCardActivity extends BaseActivity implements OnClickListener {

    private static final int GET_CARD_STATUS = 0X0001;
    private static final int PAYMENT_H5_DETAIL = 100;
    private LinearLayout title_back;
    private TextView new_content;

    private RelativeLayout new_consume_recoad;
    private TextView card_remaining;
    private RelativeLayout new_recharge_record;
    private RelativeLayout rl_online_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_card);

        initView();
        initDatas();
        initEvents();

    }

    // 实例化组件
    private void initView() {
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
        card_remaining = (TextView) findViewById(R.id.card_remaining);

        new_consume_recoad = (RelativeLayout) findViewById(R.id.new_consume_recoad);
        new_recharge_record = (RelativeLayout) findViewById(R.id.new_recharge_record);

        rl_online_pay = (RelativeLayout) findViewById(R.id.rl_online_pay);
        rl_online_pay.setVisibility(View.VISIBLE);

        new_consume_recoad.setVisibility(View.GONE);
    }

    // 初始化数据
    private void initDatas() {
        new_content.setText("一卡通充值");

        try {
            String userId = CCApplication.getInstance().getPresentUser()
                    .getUserId();
            String userType = CCApplication.getInstance().getPresentUser().getUserType();
            JSONObject json2 = new JSONObject();
            json2.put("schoolId", schoolId);
            if ("2".equals(userType)) {
                json2.put("userId", userId);
            } else if ("3".equals(userType)) {
                String childId = CCApplication.getInstance().getPresentUser().getChildId();
                json2.put("userId", childId);
            }
            json2.put("userType", userType);
            System.out.println("一卡通余额入参===" + json2);
            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.GET_CARD_REMAINING).toString();//
            String result = HttpHelper.httpPostJson(this, url, json2);
            System.out.println("一卡通余额出参===" + result);
            NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
            if (ret.getServerResult().getResultCode() != 200) {// 请求失败
                DataUtil.getToast(ret.getServerResult().getResultMessage());
            } else {
                JSONObject json = new JSONObject(result);
                String cardRemainingStr = json.getString("cardRemaining");
                card_remaining.setText(cardRemainingStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // 设置点击事件监听器
    private void initEvents() {
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);
        new_consume_recoad.setOnClickListener(this);
        new_recharge_record.setOnClickListener(this);
        rl_online_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                OneCardActivity.this.finish();
                break;
            case R.id.new_consume_recoad:    //消费记录
                newActivity(NewConsumeRecordActivity.class, null);
                break;
            case R.id.new_recharge_record:    //充值记录
                newActivity(NewRechargeRecordActivity1.class, null);
                break;
            case R.id.rl_online_pay:
                getCardStatus();
                break;

            default:
                break;

        }
    }

    /**
     * 判断学校是否开通在线支付业务
     */
    private void getCardStatus() {
        try {
            String schoolId = CCApplication.getInstance().getPresentUser().getSchoolId();
            JSONObject json = new JSONObject();
            json.put("schoolId", schoolId);//

            if (!DataUtil.isNetworkAvailable(this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            if (!isLoading()) {
                System.out.println("==获取学校在线充值状态==" + json);
                action = GET_CARD_STATUS;
                queryPost(Constants.GET_CARD_PAY_STATUS, json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case GET_CARD_STATUS:
                parseData(data);
                break;
        }
    }

    private void parseData(String data) {
        System.out.println(data);

        if (DataUtil.isNullorEmpty(data)) {
            return;
        }

        try {
            JSONObject jsonRoot = new JSONObject(data);
            JSONObject serverResult = jsonRoot.getJSONObject("serverResult");
            int resultCode = serverResult.getInt("resultCode");
            if (resultCode == 200) {
                JSONObject jsonData = jsonRoot.getJSONObject("data");
                int status = jsonData.getInt("status");
                if (0 == status) {
                    gotoH5pay();
                } else {
                    showTips(status);
                }
            } else {
                String message = serverResult.getString("resultMessage");
                DataUtil.getToastShort(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到H5支付
     */
    private void gotoH5pay() {

        String userId = CCApplication.getInstance().getPresentUser().getUserId();
        String userType = CCApplication.getInstance().getPresentUser().getUserType();

        String studentId = "";
        if (userType.equals("3")) {
            studentId = CCApplication.getInstance().getPresentUser().getChildId();
        }
        String teacherId = CCApplication.getInstance().getPresentUser().getUserId();

        //拼接地址
        final String detailURL = new StringBuilder(Constants.URL)
                .append(Constants.H5_PAY_URL)
                .append("userId=")
                .append(userId)
                .append("&userType=")
                .append(userType)
                .append("&studentId=")
                .append(studentId)
                .append("&teacherId=")
                .append(teacherId).toString();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("url", detailURL);
        intent.setClass(this, PaymentH5Activity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, PAYMENT_H5_DETAIL);
        System.out.println("支付页面: " + detailURL);
    }

    private void showTips(int status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        if (1 == status) {
            builder.setMessage("该学校还没有开通在线充值服务，请联系学校开通！");
        } else if (2 == status) {
            builder.setMessage("该学校的在线充值服务暂停使用，请稍后再试！");
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }
}
