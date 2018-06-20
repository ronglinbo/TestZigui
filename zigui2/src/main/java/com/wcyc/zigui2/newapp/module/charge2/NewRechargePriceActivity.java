/*
 * 文 件 名:PackageSelectActivity.java
 * 版 本 号： 1.05
 */
package com.wcyc.zigui2.newapp.module.charge2;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.bean.CommitOrderPayReq;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.order.MyOrderActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 选择套餐价格
 *
 * @author 郑国栋 2017-01-06
 * @version 2.0.9
 */
public class NewRechargePriceActivity extends BaseActivity implements
        OnClickListener {
    private LinearLayout title_back;// 返回键布局
    private TextView new_content;// 标题
    private TextView title_right_tv;// 右标题
    private MyListView vip_services_mlv;
    private List<SysProductListInfo.Schoolallproductlist> schoolallproductlist;
    private Button recharge_center_bt;
    private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    private SysProductListInfo sysProductListInfo;
    private GridView product_gv;
    private static final int CREATE_ORDER = 1;
    private static final int GET_PRODUCT_INFO = 2;
    private static final int GET_PAYMENT_INFO = 3;
    private ProductInfo productInfo;
    private HashMap<String, Boolean> isTimeSelected = new HashMap<String, Boolean>();
    private ProductInfo.ProductTime item;
    private NewPayPop payPop;
    // 定义选择套餐界面的对象
    private static NewRechargePriceActivity instance = null;
    private String payType;
    private String function;//web调用
    private CommitOrderPayReq req;
    private NewCreateOrderResult ret;
    //支付方式 1：购买 2：部分退款 3：全额退款
    private static final String BUY = "1";
    private static final String PART_RETURN = "2";
    private static final String ALL_RETURN = "3";
    /**
     * 支付支付方式
     * 1 支付宝，2 微信，3 通联储蓄卡，4 通联信用卡
     */
    public static final String MOBILEALIPAYSECURE = "ALIPAY";
    public static final String MOBILEWXPAYSECURE = "WEIXINPAY";
    //	public static final String MOBILCXETLPAYSECURE = "TLIANCXPAY";
//	public static final String MOBILXYETLPAYSECURE = "TLIANXYPAY";
    public static final String MOBILCXETLPAYSECURE = "CXKPAY";
    public static final String MOBILXYETLPAYSECURE = "XYKPAY";
    /**
     * "00" - 生产环境
     * "01" - 测试环境，该环境中不发生真实交易
     */
    String serverMode = "00";
    //String serverMode = "01";

    /**
     * 支付宝支付结果通知消息标志
     */
    private final int RQF_PAY = 0;
    private IWXAPI api;
    private UserType user;
    private int productId = 0;
    private String selectProductCode;
    private String selectProductName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.new_recharge_price_activity);
        initView();
        initDatas();
        initEvents();
        parseIntent();
    }

    // 实例化组件
    private void initView() {
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);// 右标题
        vip_services_mlv = (MyListView) findViewById(R.id.vip_services_mlv);
        recharge_center_bt = (Button) findViewById(R.id.recharge_center_bt);
        buttonEnable(recharge_center_bt, false);
        product_gv = (GridView) findViewById(R.id.product_gv);
    }

    // 初始化数据
    private void initDatas() {
        title_back.setVisibility(View.VISIBLE);
        new_content.setText("充值中心");
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setText("充值记录");
        title_right_tv.setTextColor(getResources().getColor(R.color.blue));
        user = CCApplication.getInstance().getPresentUser();
        isSelected = new HashMap<Integer, Boolean>();
        isSelected = (HashMap<Integer, Boolean>) getIntent().getExtras().getSerializable("isSelected");
        sysProductListInfo = new SysProductListInfo();
        sysProductListInfo = (SysProductListInfo) getIntent().getExtras().getSerializable("sysProductListInfo");
        schoolallproductlist = new ArrayList<SysProductListInfo.Schoolallproductlist>();
        List<SysProductListInfo.Schoolallproductlist> packageRoductListA = sysProductListInfo.getSchoolAllProductList();
        String productCode = "";
        for (int i = 0; i < packageRoductListA.size(); i++) {
            int id = packageRoductListA.get(i).getServiceId();
            if (isSelected.get(id) != null) {
                if (isSelected.get(id)) {
                    schoolallproductlist.add(packageRoductListA.get(i));
                    if (DataUtil.isNullorEmpty(productCode)) {
                        productCode = packageRoductListA.get(i).getProductCode() + "";
                    } else {
                        productCode += "," + packageRoductListA.get(i).getProductCode();
                    }
                }
            }

        }
        NewProductGridViewAdapter newProductGridViewAdapter = new NewProductGridViewAdapter(this, schoolallproductlist);
        product_gv.setAdapter(newProductGridViewAdapter);

        if (!DataUtil.isNullorEmpty(productCode)) {
            getProductInfo(productCode);
        }

    }

    private int orderModel = -1;

    // 设置点击效果监听器
    private void initEvents() {
        title_back.setOnClickListener(this);
        title_right_tv.setOnClickListener(this);
        recharge_center_bt.setOnClickListener(this);

        vip_services_mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                RadioButton check = (RadioButton) arg1.findViewById(R.id.check);
                for (String key : isTimeSelected.keySet()) {
                    isTimeSelected.put(key, false);
                }
                isTimeSelected.put(String.valueOf(arg2), true);
                check.setChecked(true);
                ((ProductInfoAdapter) arg0.getAdapter()).setSelected(isTimeSelected);
                ((BaseAdapter) arg0.getAdapter()).notifyDataSetChanged();

                if (productInfo != null) {
                    List<ProductInfo.ProductTime> info = productInfo.getFreeProductList();
                    if (info != null) {
                        item = info.get(arg2);
                        productId = item.getId();
                        selectProductCode = item.getProductCode();
                        selectProductName = item.getProductName();

                    }
                } else {
                    List<ProductInfo.ProductTime> info = productInfo.getFreeProductList();
                    if (info != null) {
                        item = info.get(arg2);
                        productId = item.getId();
                        selectProductCode = item.getProductCode();
                        selectProductName = item.getProductName();
                    }
                }
                buttonEnable(recharge_center_bt, true);
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_right_tv://跳转充值记录
                newActivity(MyOrderActivity.class, null);
                break;
            case R.id.recharge_center_bt://去支付
                payPop = new NewPayPop(NewRechargePriceActivity.this, 3);
                payPop.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            default:
                break;

        }
    }

    private void getProductInfo(String productCode) {
        JSONObject json = new JSONObject();
        if (user != null) {
            try {
                json.put("classId", user.getClassId());
                json.put("productCode", productCode);
                json.put("schoolId", user.getSchoolId());
                System.out.println("==套餐价格入参==" + json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            queryPost(Constants.GET_PRODUCT_TIME, json);
            action = GET_PRODUCT_INFO;
        }
    }

    private void buttonEnable(Button button, boolean enable) {
        if (enable) {
            button.setClickable(true);
            button.setEnabled(true);
            button.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
        } else {
            button.setClickable(false);
            button.setEnabled(false);
            button.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
        }
    }

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case GET_PRODUCT_INFO://
                System.out.println("==套餐价格出参===" + data);
                productInfo = JsonUtils.fromJson(data, ProductInfo.class);
                if (productInfo.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
                    DataUtil.getToast(productInfo.getServerResult().getResultMessage());
                } else {
                    List<ProductInfo.ProductTime> info = productInfo.getFreeProductList();
                    vip_services_mlv.setAdapter(new ProductInfoAdapter(this, info));
                    vip_services_mlv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                }
                break;
            case GET_PAYMENT_INFO:
                parsePayMentInfo(data);
                break;
            case CREATE_ORDER:
                System.out.println("创建订单结果:" + data);
                ret = JsonUtils.fromJson(data, NewCreateOrderResult.class);

                if (ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
                    DataUtil.getToast(ret.getServerResult().getResultMessage());
                } else {
                    if (ret.getSysOrder() != null) {
                        getPaymentInfo(ret.getSysOrder().getId() + "");
                    }
                }
                break;

        }
    }

    /**
     * 获取类的实例.
     *
     * @return 类的实例
     */
    public static NewRechargePriceActivity getInstance() {
        return instance;
    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                function = bundle.getString("call");
                if ("html5".equals(function)) {

                }
            }
        }
    }


    private void getPaymentInfo(String orderNo) {
        JSONObject json = new JSONObject();
        List<String> productList = new ArrayList<String>();
        for (int i = 0; i < schoolallproductlist.size(); i++) {
            productList.add(schoolallproductlist.get(i).getProductName());
        }

        try {
//			json.put("orderId", orderNo);
//			json.put("paymentPlatformType",payType);

            //2017-01-12增加了productList入参 用对象类型 用下面的方法
            NewGetPaymentInfoBean ngpib = new NewGetPaymentInfoBean();
            ngpib.setOrderId(orderNo);
            ngpib.setPaymentPlatformType(payType);
            ngpib.setProductList(productList);
            Gson gson = new Gson();
            String string = gson.toJson(ngpib);
            json = new JSONObject(string);
            System.out.println("请求支付参数:" + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        action = GET_PAYMENT_INFO;
        queryPost(Constants.GET_PAYMENT_INFO, json);
    }


    private void parsePayMentInfo(String data) {
        System.out.println("获取支付参数结果:" + data);
        PaymentInfo payInfo = JsonUtils.fromJson(data, PaymentInfo.class);
        if (payInfo.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
            DataUtil.getToast(payInfo.getServerResult().getResultMessage());
        } else {
            goToPay(payInfo.getPaymentInfo());
            req = new CommitOrderPayReq();
            UserType user = CCApplication.getInstance().getPresentUser();
            long actual = item.getActualAmount();
            long origin = item.getAmount();
            req.setUserId(user.getUserId());
            req.setOrderId(payInfo.getOrderId());
            req.setStudentId(user.getChildId());
            req.setCouponAmount(origin - actual);
            req.setCouponType("0");
            req.setFullAmount(origin);
            req.setOriginalPayAmount(actual);
            req.setPayNo(ret.getSysOrder().getOrderNo());
            req.setPayAmount(actual);
            req.setPayType(BUY);
            LocalUtil.req = req;//给微信支付使用
        }
    }


    /**
     * 选择了套餐后生成订单
     */
    public void createOrder() {
        payType = payPop.getPayType();
        try {
            JSONObject json = new JSONObject();
            //json.put("productId", productId);
            json.put("orderModel", 2 + ""); //写死 自由订购产品
            json.put("productCode", selectProductCode);//大于200008，以上版本添加这个
            json.put("productNames", schoolallproductlist.get(0).getProductName());
            json.put("payType", payType);
            json.put("studentId", user.getChildId());
            json.put("parentId", user.getUserId());
            json.put("schoolId", user.getSchoolId());
            json.put("deviceType", "ANDROID");
            System.out.println("create Order:" + json);
//			startDate = DataUtil.getPayDate(this);
            queryPost(Constants.CREATE_SYS_ORDER, json);
            action = CREATE_ORDER;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void goToPay(String arg) {
//		String arg = ret.getSysOrder().getPaymentInfo();
        if (arg == null) return;
        System.out.println("pay arg:" + arg);
        if (MOBILEALIPAYSECURE.equals(payType)) {
            goAliApp(arg);
        } else if (MOBILEWXPAYSECURE.equals(payType)) {
            goWxApp(arg);
        } else if (MOBILCXETLPAYSECURE.equals(payType)) {
            System.out.println("==MOBILCXETLPAYSECURE==" + MOBILCXETLPAYSECURE);
            System.out.println("==payType==" + payType);
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

        //para.contains("out_trade_no");

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                //	String payInfo="partner=\"2088621142261630\"&seller_id=\"jliu@ziguiw.com\"&out_trade_no=\"10020171100228\"&subject=\"%E5%AD%90%E8%B4%B5%E8%AF%BE%E5%A0%82%EF%BC%88%E5%B0%8F%E5%AD%A6%EF%BC%89\"&body=\"%E5%AD%90%E8%B4%B5%E8%AF%BE%E5%A0%82%EF%BC%88%E5%B0%8F%E5%AD%A6%EF%BC%89\"&total_fee=\"0.01\"&notify_url=\"http%3A%2F%2Fcs.ziguiw.cn%3A8028%2Fzgwps%2FalipayPlatform%2Fapp.do\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&show_url=\"m.alipay.com\"&sign=\"hA0Dk4IQssrk%2FI9NJVJR7Kv2AARoLd0VM1qgK%2FC5Ym%2BAgAm%2FniD48PQdz1Tcg75SbvcF6zaQyrDQwYlb3pd5t4OSAWw%2F92CwKax6GYaAdan3H9ePZRtrCnHnC1pnqpr2hfBBjY10WncliRoGZyVT9uB8gXDkXzIwa%2BJ2keq4zXUiVfWffGtNHWU2hFLZiWil1Oh4yd8UpnlwxIYvqFTzynE5%2BTFPSWFTrL67J9CsmAWVn5prpAMfitvVYkw6gqgUgF0DUwK94X1uQoeAdcFPntGu%2Bq9v%2BA%2B4WNVvB%2Bjz2eU2gMV%2BBf9f4o5n0Bdw9ScUDnkhg9djr0e3rz%2FZ61iIeg%3D%3D\"&sign_type=\"RSA\"";

                PayTask alipay = new PayTask(NewRechargePriceActivity.this);
                String result = alipay.pay(para, true);
                Message msg = new Message();
                msg.what = RQF_PAY;
                msg.obj = result;
                payHandler.sendMessage(msg);
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
        System.out.println("支付宝返回的数据：" + data);
        PayResult payResult = new PayResult(data);

        /**
         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
         * docType=1) 建议商户依赖异步通知
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息

        String resultStatus = payResult.getResultStatus();
        resultStatus = "{" + resultStatus + "}";
        Map<String, String> map = new HashMap<String, String>();
        map.put("resultStatus", resultStatus);

//		// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//		if(DataUtil.isNull(data))return;
//		String[] data2 = data.split(";");
//		Map<String,String> map = new HashMap<String,String>();
//		for (String s : data2) {
//			if(s.indexOf("=") < 0)continue;
//			map.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
//		}
        if (map.get("resultStatus") == null) return;
        if (map.get("resultStatus").equals("{9000}")) {
            //代表支付宝支付成功
            //后台告诉自己服务器支付结果
            new PayResultAsyncTask(MOBILEALIPAYSECURE, "1", req).execute();
            //跳转到我的界面
//			CCApplication.app.finishAllActivity();
            //  setResult(RESULT_OK);
            String call = getIntent().getStringExtra("call");
            Activity newrechargeProductActivity=null;
            Activity newrechargePriceActivity=null;
            for (int i = 0; i <CCApplication.activityList.size() ; i++) {
                if(CCApplication.activityList.get(i) instanceof NewRechargeProductActivity){
                    newrechargeProductActivity=CCApplication.activityList.get(i);
                }
                if(CCApplication.activityList.get(i) instanceof NewRechargePriceActivity){
                    newrechargePriceActivity=CCApplication.activityList.get(i);
                }
                ;
            }
            this.finish();
            if(newrechargePriceActivity!=null){
                newrechargePriceActivity.finish();
            }
            if(newrechargeProductActivity!=null){
                newrechargeProductActivity.setResult(RESULT_OK);
                newrechargeProductActivity.finish();
            }
            if (call != null) {
                finish();

            } else {
                //原来逻辑
                Intent intent = new Intent(this, MyOrderActivity.class);
                intent.putExtra("status", 1);
                startActivity(intent);
                finish();
            }

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
//					String appid = "wx2e813871dfe69697";//"wxef06aadd90fab5f1";//com.wcyc.zigui2生成的appid
                    System.out.println("appid:" + appid);
                    String noncestr = json.getString("noncestr");
                    String packagevalue = json.getString("package");
                    String partnerid = json.getString("partnerid");
                    String prepayid = json.getString("prepayid");
                    String sign1 = json.getString("sign");
                    String timestamp = json.getString("timestamp");
                    Log.i("wxpay", "payParam:" + payParam);
                    if (api == null) {
                        api = WXAPIFactory.createWXAPI(NewRechargePriceActivity.this, appid);
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
    }

    /**
     * 跳转到通联信用卡支付
     *
     * @param para 参数
     */
    private void goTlXYApp(final String para) {
    }

    /**
     * 通联支付回调的返回码
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //	super.onActivityResult(requestCode, resultCode, data);
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
//
//				String platformPayDate = DataUtil.GetBeijingTime();
//				System.out.println("platformPayDate:"+platformPayDate);
//				req.setPlatformPayDate(platformPayDate);
//				String string = gson.toJson(req);
//				json = new JSONObject(string);
//				System.out.println("提交订单json:"+json);
//				String url = new StringBuilder(Constants.SERVER_URL).append(Constants.COMMIT_ORDER_PAY).toString();
//				result = HttpHelper.httpPostJson(NewPackageSelectActivity.this,url, json);
//				System.out.println("提交订单 result:"+result);
////				NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
////				if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
////					expired.setProductExpired("1");
////					CCApplication.getInstance().setServiceExpiredInfo(expired);
////				}
//
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
