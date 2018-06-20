package com.wcyc.zigui2.newapp.module.charge2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.allinpay.appayassistex.APPayAssistEx;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.bean.ChildMember;
import com.wcyc.zigui2.bean.OrderInfoResult;
import com.wcyc.zigui2.bean.OrderListResult;
import com.wcyc.zigui2.bean.OrderResult;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.core.TaskBaseActivity;

import com.wcyc.zigui2.listener.HttpRequestAsyncTaskListener;

import com.wcyc.zigui2.newapp.bean.CommitOrderPayReq;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge.NewOrderDetailList;
import com.wcyc.zigui2.newapp.module.charge.NewOrderDetailList.OrderDetail;
import com.wcyc.zigui2.newapp.module.charge2.NewCreateOrderResult;
import com.wcyc.zigui2.newapp.module.charge2.NewOrderDetail.OrderPayInfo;
import com.wcyc.zigui2.newapp.module.charge2.NewOrderInfoList;
import com.wcyc.zigui2.newapp.module.charge2.NewOrderInfoList.ChargeLogs;

import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;

/**
 * 充值记录
 */
public class NewPaymentRecordActivity extends BaseActivity
        implements OnClickListener, HttpRequestAsyncTaskListener {
    private static NewPaymentRecordActivity instance = null;
    private ImageView backButton;
    private ListView lv_payment_records;

    private NewPaymentRecordsAdapter mPaymentRecordsAdapter;
    private NewOrderInfoList mOrderListResult;
    private NewOrderDetail mOrderDetail;
    private LinearLayout payment_record_ll_null;
    private View view;
    private int pos;//选择的项
    private LinearLayout title_back;
    private TextView new_content;
    private CommitOrderPayReq req;
    private PaymentInfo ret;
    private String payType, startDate, endDate;//（yyyy-MM-dd HH:mm:ss）;
    //支付状态(0:生成订单，未支付  1：支付成功  2 支付失败 3 失效)
    private final int SUCCESS = 1;
    private final int FAIL = 2;
    private final int INVAILD = 3;
    private final int UNFINISHED = 0;
    /**
     * 支付支付方式
     * 1 支付宝，2 微信，3 通联储蓄卡，4 通联信用卡
     */
    public static final String MOBILEALIPAYSECURE = "ALIPAY";
    public static final String MOBILEWXPAYSECURE = "WEIXINPAY";
//    public static final String MOBILCXETLPAYSECURE = "TLIANCXPAY";
//    public static final String MOBILXYETLPAYSECURE = "TLIANXYPAY";
    public static final String MOBILCXETLPAYSECURE = "CXKPAY";
    public static final String MOBILXYETLPAYSECURE = "XYKPAY";

    //支付方式 1：购买 2：部分退款 3：全额退款
    private static final String BUY = "1";
    private static final String PART_RETURN = "2";
    private static final String ALL_RETURN = "3";

    /**
     * "00" - 生产环境
     * "01" - 测试环境，该环境中不发生真实交易
     */
    String serverMode = "00";
    private final int RQF_PAY = 0;
    private IWXAPI api;
    /**
     * 获取类的实例.
     *
     * @return 类的实例
     */
    NewPayPop payPop;

    public static NewPaymentRecordActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.new_payment_record);
        initView();
        initDatas();
        initEvents();
        GetOrderList();
    }

    protected void onResume() {
        super.onResume();
        System.out.print("GetOrderList");
    }

    private void GetOrderList() {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.getInstance().getPresentUser();
        try {
            json.put("parentId", user.getUserId());
            json.put("userType", user.getUserType());
            json.put("studentId", user.getChildId());
            System.out.println("===充值记录入参json=="+json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpRequestAsyncTask(json, this, this).execute(Constants.GET_CHARGE_LOGS);
    }

    private void getOrderDetail(String orderId) {
        JSONObject json = new JSONObject();
        try {
            json.put("orderNo", orderId);
            System.out.println("getOrderDetail:" + json);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new HttpRequestAsyncTask(json, this, this).execute(Constants.GET_ORDER_DETAIL_INFO);
    }

    private void initEvents() {
        title_back.setOnClickListener(this);
        lv_payment_records.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ChargeLogs detail = (ChargeLogs) arg0.getAdapter().getItem(arg2);
                if (detail.getStatus() == UNFINISHED) {
                    getOrderDetail(detail.getOrderNo());
                    view = arg1;
                    pos = arg2;
                }
            }
        });
    }

    private void initDatas() {
        new_content.setText("充值记录");
        //得到小孩的数据
        Bundle bundle = getIntent().getExtras();
    }

    private void initView() {
        lv_payment_records = (ListView) findViewById(R.id.lv_payment_records);
        payment_record_ll_null = (LinearLayout) findViewById(R.id.payment_record_ll_null);
        title_back = (LinearLayout) findViewById(R.id.title_back);
        title_back.setVisibility(View.VISIBLE);
        new_content = (TextView) findViewById(R.id.new_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }


    @Override
    public void onRequstComplete(String data) {
        List<ChargeLogs> myOrderResultList;
        if (data.contains("parentRechargeLogs")) {//获取订单列表接口
            mOrderListResult = JsonUtils.fromJson(data, NewOrderInfoList.class);
            if (mOrderListResult.getServerResult().getResultCode() != 200) {
                DataUtil.getToast(mOrderListResult.getServerResult().getResultMessage());
            } else {
                myOrderResultList = mOrderListResult.getParentRechargeLogs();
                if(myOrderResultList.size() > 0) {
                    mPaymentRecordsAdapter = new NewPaymentRecordsAdapter(this, myOrderResultList);
                    lv_payment_records.setAdapter(mPaymentRecordsAdapter);
                }else{
                    payment_record_ll_null.setVisibility(View.VISIBLE);
//                    findViewById(R.id.tv_no_message).setVisibility(View.GONE);
                }
            }
        } else {
            mOrderDetail = JsonUtils.fromJson(data, NewOrderDetail.class);
            if (mOrderDetail.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                OrderPayInfo info = mOrderDetail.getOrderPayInfo();
                if (info != null) {
                    payPop = new NewPayPop(NewPaymentRecordActivity.this, 2);//重新提交订单
                    payPop.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        }
    }

    @Override
    public void onRequstCancelled() {
        finish();
    }

    @Override
    protected void getMessage(String data) {
        // TODO Auto-generated method stub
        ret = JsonUtils.fromJson(data, PaymentInfo.class);
        if (ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
            goToPay(ret.getPaymentInfo());
            req = new CommitOrderPayReq();
            OrderPayInfo info = mOrderDetail.getOrderPayInfo();

            UserType user = CCApplication.getInstance().getPresentUser();
            long actual = info.getPayAmount();
            long origin = info.getFullAmount();
            req.setUserId(user.getUserId());
            req.setOrderId(info.getId() + "");
            req.setStudentId(user.getChildId());
            req.setCouponAmount(info.getCouponAmount());
            req.setCouponType("0");
            req.setFullAmount(origin);
            req.setOriginalPayAmount(actual);
            req.setPayNo(info.getPayNo());
            req.setPayAmount(actual);
            req.setPlatformPayDate(info.getPayDate());
            req.setPayDate(info.getPayDate());
            req.setPayType(BUY);
            LocalUtil.req = req;//给微信支付使用
        } else {
            DataUtil.getToast(ret.getServerResult().getResultMessage());
        }
    }

    //重新支付
    public void recommitOrder() {
        payType = payPop.getPayType();
        OrderPayInfo info = mOrderDetail.getOrderPayInfo();
        if (info != null) {
            getPaymentInfo(info.getOrderId() + "");
        }

    }

    private void getPaymentInfo(String orderId) {
        JSONObject json = new JSONObject();
        try {
            json.put("orderId", orderId);
            json.put("paymentPlatformType", payType);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        queryPost(Constants.GET_PAYMENT_INFO, json);
    }

    private void goToPay(String arg) {
        if (arg == null) return;
        System.out.println("pay arg:" + arg);
        if (MOBILEALIPAYSECURE.equals(payType)) {
            goAliApp(arg);
        } else if (MOBILEWXPAYSECURE.equals(payType)) {
            goWxApp(arg);
        } else if (MOBILCXETLPAYSECURE.equals(payType)) {
            goTlApp(arg);
        } else if (MOBILXYETLPAYSECURE.equals(payType)) {
            goTlXYApp(arg);
        }
    }

    /**
     * 处理支付的handler
     */
    public Handler payHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RQF_PAY:
                    dealAliPay((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 跳转到支付宝应用
     *
     * @param para 参数
     */
    private void goAliApp(final String para) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
//                AliPay alipay = new AliPay(NewPaymentRecordActivity.this, payHandler);
//                String result = alipay.pay(para);
//
//                Message msg = new Message();
//                msg.what = RQF_PAY;
//                msg.obj = result;
//                payHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝支付结果处理方法
     *
     * @param data 支付宝返回的支付结果数据
     */
    private void dealAliPay(String data) {
        //如果返回支付结果是空字符，那么不处理
        if (DataUtil.isNull(data)) return;
        String[] data2 = data.split(";");
        Map<String, String> map = new HashMap<String, String>();
        for (String s : data2) {
            if (s.indexOf("=") < 0) continue;
            map.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
        }

        if (map.get("resultStatus") == null) return;
        if (map.get("resultStatus").equals("{9000}")) {
            //代表支付宝支付成功
            //后台告诉自己服务器支付结果
            new PayResultAsyncTask(MOBILEALIPAYSECURE, "1", req).execute();
            //跳转到我的界面
//			CCApplication.app.finishAllActivity();
            finish();
//			newActivity(MyInformationActivity.class, null);
        } else if (map.get("resultStatus").equals("{4000}")) {
            //代表支付宝支付失败
            //后台告诉自己服务器支付结果
            new PayResultAsyncTask(MOBILEALIPAYSECURE, "0", req).execute();
            //跳转到支付失败界面
            newActivity(PayFailActivity.class, null);
        } else if (map.get("resultStatus").equals("{8000}")) {
            //代表正在处理中

        } else if (map.get("resultStatus").equals("{6001}")) {
            //代表用户中途取消

        } else if (map.get("resultStatus").equals("{6002}")) {
            //代表网络连接出错
            DataUtil.getToast(map.get("memo").replace("{", "").replace("}", ""));
        } else if (map.get("resultStatus").equals("{4001}")) {
            //代表参数错误
            DataUtil.getToast(map.get("memo").replace("{", "").replace("}", ""));
        } else if (map.get("resultStatus").equals("{7001}")) {
            DataUtil.getToast(map.get("memo").replace("{", "").replace("}", ""));
        }
    }

    /**
     * 跳转到微信支付应用
     *
     * @param payParam 参数
     */
    private void goWxApp(final String payParam) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // 传回的字符串
//				{\"appid\":\"wxef06aadd90fab5f1\"," +
//						"\"noncestr\":\"4bdcc4628e054740ae3092f49a24d82e\"," +
//						"\"package\":\"Sign=WXPay\",\"partnerid\":\"1230710001\"," +
//						"\"prepayid\":\"1201000000150519947090d34c02b4c5\"," +
//						"\"sign\":\"63619f9945f10021b578dcffca57d1dc2da52c5e\"," +
//						""timestamp\":\"1432014786\"}

                JSONObject json;
                try {
                    json = new JSONObject(payParam);
                    String appid = json.getString("appid");
                    String noncestr = json.getString("noncestr");
                    String packagevalue = json.getString("package");
                    String partnerid = json.getString("partnerid");
                    String prepayid = json.getString("prepayid");
                    String sign1 = json.getString("sign");
                    String timestamp = json.getString("timestamp");
                    if (api == null) {
                        api = WXAPIFactory.createWXAPI(NewPaymentRecordActivity.this, appid);
                        // 将该app注册到微信
                        api.registerApp(appid);
                    }

                    PayReq request = new PayReq();
                    //微信分配的公众账号ID
                    request.appId = appid;
                    //信支付分配的商户号
                    request.partnerId = partnerid;
                    //微信返回的支付交易会话ID
                    request.prepayId = prepayid;
                    //暂填写固定值Sign=WXPay
                    request.packageValue = packagevalue;
                    //随机字符串，不长于32位
                    request.nonceStr = noncestr;
                    //时间戳
                    request.timeStamp = timestamp;
                    //签名
                    request.sign = sign1;
                    //调用api接口发送数据到微信
                    boolean isSendSucces = api.sendReq(request);
                    if (isSendSucces) {
                        System.out.println("发送成功");
                    } else {
                        DataUtil.getToast("请安装最新版微信");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })
                .start();
    }

    /**
     * 跳转到通联储蓄卡支付
     *
     * @param para 参数
     */
    private void goTlApp(final String para) {
        APPayAssistEx.startPay(this, para.toString(), serverMode);
    }

    /**
     * 跳转到通联信用卡支付
     *
     * @param para 参数
     */
    private void goTlXYApp(final String para) {
        APPayAssistEx.startPay(this, para.toString(), serverMode);
    }

    /**
     * 通联支付回调的返回码
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (APPayAssistEx.REQUESTCODE == requestCode) {
            if (null != data) {
                String payRes = null;
                String payAmount = null;
                String payTime = null;
                try {
                    JSONObject resultJson = new JSONObject(data.getExtras().getString("result"));
                    payRes = resultJson.getString(APPayAssistEx.KEY_PAY_RES);
                    payAmount = resultJson.getString("payAmount");
                    payTime = resultJson.getString("payTime");
                    System.out.println("payTime:" + payTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != payRes && payRes.equals(APPayAssistEx.RES_SUCCESS)) {
                    DataUtil.getToast("支付成功");
                    finish();
//					CCApplication.app.finishAllActivity();
//					newActivity(MyInformationActivity.class, null);
                    new PayResultAsyncTask(MOBILCXETLPAYSECURE, "1", req).execute();//写死的
                } else {
                    DataUtil.getToast("支付失败");
                    newActivity(PayFailActivity.class, null);
                    finish();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 通知服务器的异步线程类
     */
    class PayResultAsyncTask extends AsyncTask<String, String, String> {
        String orderNo, payType, state;
        CommitOrderPayReq req;

        /**
         * @param payType 支付方式  ALIPAY：支付宝；
         * @param state   支付状态 0：失败   1：成功
         */
        public PayResultAsyncTask(String payType, String state, CommitOrderPayReq req) {
            super();
            this.payType = payType;
            this.state = state;
            this.req = req;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
//			JSONObject json ;
//			try{
//				Gson gson = new Gson();
//				
//				req.setPaymentPlatformType(payType);
//				req.setPayStatus(state);
////				long time = System.currentTimeMillis();
////				String platformPayDate = DataUtil.getCurrentDate(time);
//				String platformPayDate = DataUtil.GetBeijingTime();
//				System.out.println("platformPayDate:"+platformPayDate);
//				req.setPlatformPayDate(platformPayDate);
//				String string = gson.toJson(req);
//				json = new JSONObject(string);
//				System.out.println("json:"+json);
//				String url = new StringBuilder(Constants.SERVER_URL).append(Constants.COMMIT_ORDER_PAY).toString();
//				result = HttpHelper.httpPostJson(NewPaymentRecordActivity.this,url, json);
//				NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
//				if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
////					ServiceExpiredBean expired = CCApplication.getInstance().getServiceExpiredInfo();
////					expired.setProductExpired("1");
////					System.out.println("expired.getEndDate()"+expired.getEndDate());
////					CCApplication.getInstance().setServiceExpiredInfo(expired);
//				}
//				System.out.println("result:"+result);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}
