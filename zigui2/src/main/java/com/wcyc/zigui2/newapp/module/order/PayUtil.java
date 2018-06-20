package com.wcyc.zigui2.newapp.module.order;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.CommitOrderPayReq;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge2.NewCreateOrderResult;
import com.wcyc.zigui2.newapp.module.charge2.NewGetPaymentInfoBean;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargeProductActivity;
import com.wcyc.zigui2.newapp.module.charge2.PayFailActivity;
import com.wcyc.zigui2.newapp.module.charge2.PayResult;
import com.wcyc.zigui2.newapp.module.charge2.PaymentInfo;
import com.wcyc.zigui2.newapp.module.charge2.SysProductListInfo;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 章豪 on 2017/7/5.
 */

public class PayUtil implements HttpRequestAsyncTaskListener {
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
     * 支付宝支付结果通知消息标志
     */
    private final int RQF_PAY = 0;
    private IWXAPI api;
    private UserType user;
    private int productId = 0;
    private String selectProductCode;

    /**
     * 	"00" - 生产环境
     "01" - 测试环境，该环境中不发生真实交易
     */
   String serverMode = "00";
//	String serverMode = "01";

    private  static final int  CREATE_ORDER =2;
    private static final int GET_PAYMENT_INFO = 1;
    private static  int action;
    private static Order order;
    private static String payType;
    private static BaseActivity myorderactivity;
    private static NewRechargeProductActivity newRechargeProductActivity;
    public void commitOrder(Order order1, String  payType1, BaseActivity context1) {
        //1.获取订单支付参数
             order=order1;
             payType=payType1;
        myorderactivity=context1;

        getPaymentInfo(order.getId()+"");

    }
   private  static PayUtil payUtil;
    public static PayUtil getInstance(){
        if(payUtil==null){
            payUtil=new PayUtil();
        }
        return payUtil;

    }


    private SysProductListInfo.Schoolallproductlist schoolallproductlist;
    public void createOrder(String payType1, UserType userType, SysProductListInfo.Schoolallproductlist schoolallproductlist1, BaseActivity context) {
        myorderactivity=context;
        schoolallproductlist=schoolallproductlist1;
        payType=payType1;
        try {
            JSONObject json = new JSONObject();
           //json.put("productId", productId);
            json.put("productCode", schoolallproductlist.getProductCode());//大于200008，以上版本添加这个
            json.put("orderModel", schoolallproductlist.getOrderModel());
            json.put("payType", payType);
            json.put("studentId",userType.getChildId());
            json.put("parentId", userType.getUserId());
            json.put("schoolId", userType.getSchoolId());
            json.put("productNames", schoolallproductlist.getProductName());
            json.put("deviceType","ANDROID");
            System.out.println("create Order:"+json);
//			startDate = DataUtil.getPayDate(this);
            new HttpRequestAsyncTask(json,this, CCApplication.applicationContext).execute(Constants.CREATE_SYS_ORDER);
            action = CREATE_ORDER;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void   getPaymentInfo(String orderNo) {
        JSONObject json = new JSONObject();
        List<String> productList = new ArrayList<String>();
//        for (int i = 0; i < packageRoductList.size(); i++) {
//            productList.add(packageRoductList.get(i).getProductName());
//        }
        if(schoolallproductlist!=null){
            productList.add(schoolallproductlist.getProductName());
        }
        if(order!=null){
            productList.add(order.getProductName());
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
        new HttpRequestAsyncTask(json, this, CCApplication.applicationContext).execute(Constants.GET_PAYMENT_INFO);
    }

    CommitOrderPayReq req;
    private void parsePayMentInfo(String data) {
        System.out.println("获取支付参数结果:" + data);
        PaymentInfo payInfo = JsonUtils.fromJson(data, PaymentInfo.class);
        if (payInfo.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
            DataUtil.getToast(payInfo.getServerResult().getResultMessage());
        } else {
            goToPay(payInfo.getPaymentInfo());
            req = new CommitOrderPayReq();
            UserType user = CCApplication.getInstance().getPresentUser();
            long actual=0;
            long origin=0;
            if(schoolallproductlist!=null){
            actual = schoolallproductlist.getActualAmount(); //实际价格
           origin = schoolallproductlist.getAmount();//原价
           }
            if(order!=null){
              actual = order.getOrderAmount(); //实际价格
               origin = order.getFullAmount();//原价
            }

            req.setUserId(user.getUserId());
            req.setOrderId(payInfo.getOrderId());
            req.setStudentId(user.getChildId());
            req.setCouponAmount(origin - actual);
            req.setCouponType("0");
            req.setFullAmount(origin);
            req.setOriginalPayAmount(actual);
            req.setPayNo( payInfo.getOrderNo()); //j訂單編號
            req.setPayAmount(actual);
            req.setPayType("1"); //购买方式
            LocalUtil.req = req;//给微信支付使用
        }
    }
    private void goToPay(String arg){
//		String arg = ret.getSysOrder().getPaymentInfo();
        if(arg == null) return;
        System.out.println("pay arg:"+arg);
        if(MOBILEALIPAYSECURE.equals(payType)){
            goAliApp(arg);
        }else if(MOBILEWXPAYSECURE.equals(payType)){
            goWxApp(arg);
        }else if(MOBILCXETLPAYSECURE.equals(payType)){
            System.out.println("==MOBILCXETLPAYSECURE=="+MOBILCXETLPAYSECURE);
            System.out.println("==payType=="+payType);
            goTlApp(arg);
        }else if(MOBILXYETLPAYSECURE.equals(payType)){
            goTlXYApp(arg);
        }
    }
    /**
     * 处理支付的handler
     */
    public Handler payHandler = new Handler(){
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
     * @param para 参数
     */
    private void goAliApp(final String para){

//		para.contains("out_trade_no");

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {


                PayTask alipay = new PayTask(myorderactivity);
                String result = alipay.pay(para,true);

                Message msg = new Message();
                msg.what = RQF_PAY;
                msg.obj = result;
                payHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**支付宝支付结果处理方法
     * @param data 支付宝返回的支付结果数据
     */
    private void dealAliPay(String data) {
        //如果返回支付结果是空字符，那么不处理
        System.out.println("支付宝返回的数据："+data);
        PayResult payResult = new PayResult(data);

        /**
         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
         * docType=1) 建议商户依赖异步通知
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息

        String resultStatus = payResult.getResultStatus();
        resultStatus="{"+resultStatus+"}";
        Map<String,String> map = new HashMap<String,String>();
        map.put("resultStatus",resultStatus);

        if(map.get("resultStatus")==null)return;
        if(map.get("resultStatus").equals("{9000}")){
            //代表支付宝支付成功
            //后台告诉自己服务器支付结果
            new PayUtil.PayResultAsyncTask(MOBILEALIPAYSECURE, "1",req).execute();
            //跳转到我的界面
//			CCApplication.app.finishAllActivity();
            myorderactivity.setResult(RESULT_OK);
            if(myorderactivity instanceof MyOrderActivity){
                MyOrderActivity myOrderActivity= (MyOrderActivity) myorderactivity;
                myOrderActivity.radioGroup.check(R.id.have_pay);
                myOrderActivity.status=1;
            }else{
                String call = myorderactivity.getIntent().getStringExtra("call");

                if (call != null) {
                    myorderactivity.finish();
                    Iterator<Activity> activityIterator = CCApplication.activityList.iterator();
                    while (activityIterator.hasNext()) {
                        Activity activity = activityIterator.next();
                        if (activity instanceof NewRechargeProductActivity) {
                            activity.setResult(RESULT_OK);
                            activity.finish();
                        }
                    }


                } else {
                    Intent intent=new Intent(myorderactivity,MyOrderActivity.class);
                    intent.putExtra("status",1);
                    myorderactivity. startActivity(intent);
                    myorderactivity.finish();
                }


            }

//			newActivity(MyInformationActivity.class, null);
        }else if (map.get("resultStatus").equals("{4000}")){
            //代表支付宝支付失败
            //后台告诉自己服务器支付结果
            new PayUtil.PayResultAsyncTask(MOBILEALIPAYSECURE, "0",req).execute();
            //跳转到支付失败界面
            myorderactivity.newActivity(PayFailActivity.class, null);
        }else if (map.get("resultStatus").equals("{8000}")){
            //代表正在处理中

        }else if (map.get("resultStatus").equals("{6001}")){
            //代表用户中途取消

        }else if (map.get("resultStatus").equals("{6002}")){
            //代表网络连接出错
            DataUtil.getToast(map.get("memo").replace("{", "").replace("}", ""));
        }else if (map.get("resultStatus").equals("{4001}")){
            //代表参数错误
            DataUtil.getToast(map.get("memo").replace("{", "").replace("}", ""));
        }else if(map.get("resultStatus").equals("{7001}")){
            DataUtil.getToast(map.get("memo").replace("{", "").replace("}", ""));
        }
    }
    /**
     * 跳转到微信支付应用
     * @param payParam 参数
     */
    private void goWxApp(final String  payParam){
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
                    System.out.println("appid:"+appid);
                    String noncestr = json.getString("noncestr");
                    String packagevalue = json.getString("package");
                    String partnerid = json.getString("partnerid");
                    String prepayid = json.getString("prepayid");
                    String sign1 = json.getString("sign");
                    String timestamp = json.getString("timestamp");
                    Log.i("wxpay","payParam:"+payParam);
                    if(api == null){
                        api = WXAPIFactory.createWXAPI(myorderactivity, appid);
                        // 将该app注册到微信
                        api.registerApp(appid);
                    }

                    PayReq request = new PayReq();
                    //微信分配的公众账号ID
                    request.appId = appid;
                    //信支付分配的商户号
                    request.partnerId = partnerid;
                    //微信返回的支付交易会话ID
                    request.prepayId= prepayid;
                    //暂填写固定值Sign=WXPay
                    request.packageValue = packagevalue;
                    //随机字符串，不长于32位
                    request.nonceStr= noncestr;
                    //时间戳
                    request.timeStamp= timestamp;
                    //签名
                    request.sign= sign1;
                    //调用api接口发送数据到微信
                    boolean isSendSucces = api.sendReq(request);
                    if(isSendSucces){
                        System.out.println("发送成功");
                    }else{
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
     * @param para 参数
     */
    private void goTlApp(final String para){
    }
    /**
     * 跳转到通联信用卡支付
     * @param para 参数
     */
    private void goTlXYApp(final String para){
    }



    /**
     *
     *通知服务器的异步线程类
     */
    static class PayResultAsyncTask extends AsyncTask<String, String, String> {
        String orderNo , payType , state;
        CommitOrderPayReq req;
        /**
         * @param payType 支付方式  ALIPAY：支付宝；
         * @param state 支付状态 0：失败   1：成功
         */
        public PayResultAsyncTask(String payType,String state,CommitOrderPayReq req) {
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





    @Override
    public void onRequstComplete(String result) {
        switch (action) {
            case GET_PAYMENT_INFO:
                //包裝支付信息并且跳轉
                parsePayMentInfo(result);
                break;
            case CREATE_ORDER:
                //包裝订单信息并且跳轉
                parseOrderInfo(result);
                break;
        }

    }

    private void parseOrderInfo(String data) {
        System.out.println("创建订单结果:"+data);
        NewCreateOrderResult ret = JsonUtils.fromJson(data, NewCreateOrderResult.class);

        if(ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
            DataUtil.getToast(ret.getServerResult().getResultMessage());
        }else{
            if(ret.getSysOrder() != null){
                getPaymentInfo(ret.getSysOrder().getId()+"");
            }
        }



    }

    @Override
    public void onRequstCancelled() {

    }
}
