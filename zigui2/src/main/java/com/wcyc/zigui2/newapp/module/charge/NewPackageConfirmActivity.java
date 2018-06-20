/*
* 文 件 名:RenewalConfirmActivity.java
* 创 建 人： 姜韵雯
* 日    期： 2014-12-12
* 版 本 号： 1.05
*/
package com.wcyc.zigui2.newapp.module.charge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


import com.allinpay.appayassistex.APPayAssistEx;
import com.google.gson.Gson;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import com.wcyc.zigui2.R;

import com.wcyc.zigui2.bean.ChildMember;
import com.wcyc.zigui2.bean.OrderResult;
import com.wcyc.zigui2.bean.ProductResult;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.asynctask.PayResultAsyncTask;
import com.wcyc.zigui2.newapp.bean.CommitOrderPayReq;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge.ChargeProduct.Product;
import com.wcyc.zigui2.newapp.module.charge.NewOrder.OrderItem;
import com.wcyc.zigui2.newapp.widget.NewPayPop;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;

/**
 * 选择套餐确认界面 续费
 * 
 * 
 */
public class NewPackageConfirmActivity extends BaseActivity 
	implements OnClickListener,IWXAPIEventHandler{
	
//	private Product orderInfo;
	private String orderNO;
	private NewCreateOrderResult ret;
	private Button backButton , bt_renew_ok;
	private TextView tv_renew_ordernum , tv_renew_duration , tv_renew_duration_content , tv_pay_sum;

	private IWXAPI api;
	private String payType;
	private NewPayPop payPop;
	private Product productItem;
	private List<Product> productList;
	private ChargeProduct product;
	String startDate,endDate;//（yyyy-MM-dd HH:mm:ss）
	/**
	 * 	"00" - 生产环境
		"01" - 测试环境，该环境中不发生真实交易
	 */
	String serverMode = "00";
	/**
	 * 支付宝支付结果通知消息标志
	 */
	private final int RQF_PAY = 0;
	/**
	 * 支付支付方式
	 * 1 支付宝，2 微信，3 通联储蓄卡，4 通联信用卡
	 */
	public static final String MOBILEALIPAYSECURE = "ALIPAY";
	public static final String MOBILEWXPAYSECURE = "WEIXINPAY";
	public static final String MOBILCXETLPAYSECURE = "TLIANCXPAY";
	public static final String MOBILXYETLPAYSECURE = "TLIANXYPAY";
	private static final int CREATE_ORDER = 1;
	//支付方式 1：购买 2：部分退款 3：全额退款
	private static final String BUY = "1";
	private static final String PART_RETURN = "2";
	private static final String ALL_RETURN = "3";
	
	private static NewPackageConfirmActivity instance = null;
	private int pos;
	private CommitOrderPayReq req;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.new_order_confirm);
		initView();
		initDatas();
		initEvents();
	}
	/**
	 * 获取类的实例.
	 * 
	 * @return 类的实例
	 */
	public static NewPackageConfirmActivity getInstance() {
		return instance;
	}

	/**
	 * 初始化监听时间
	 */
	private void initEvents() {
		backButton.setOnClickListener(this);
		bt_renew_ok.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		//得到订单的数据
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			productItem = (Product) bundle.getSerializable("productInfo");
		}
//		orderNO = ret.getOrderNo();
		if(productItem != null){
			//设置订单数据到view
//			tv_renew_ordernum.setText(orderNO);//order NO
			tv_renew_duration.setText(productItem.getValidityDateValue()+productItem.getValidityDateType());
			tv_renew_duration_content.setText(productItem.getProductName());
			String amount = DataUtil.convertF2Y(productItem.getActualAmount());
			tv_pay_sum.setText("¥" + amount + "元");
		}
		
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		backButton = (Button) findViewById(R.id.title_btn);
		backButton.setText(R.string.title_renew_confirm);
		tv_renew_ordernum = (TextView) findViewById(R.id.tv_renew_ordernum);
		tv_renew_duration = (TextView) findViewById(R.id.tv_renew_duration);
		tv_renew_duration_content = (TextView) findViewById(R.id.tv_renew_duration_content);
		tv_pay_sum = (TextView) findViewById(R.id.tv_pay_sum);
				
		bt_renew_ok = (Button) findViewById(R.id.bt_renew_ok);
	}

	@Override
	protected void getMessage(String data) {
		switch(action){
		case CREATE_ORDER:
			System.out.println("CREATE_ORDER:"+data);
			ret = JsonUtils.fromJson(data, NewCreateOrderResult.class);
			
			if(ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
				DataUtil.getToast(ret.getServerResult().getResultMessage());
			}else{
				goToPay();
				req = new CommitOrderPayReq();
				UserType user = CCApplication.getInstance().getPresentUser();
//				String childId = CCApplication.app.getPresentUser().getChildId();
				long actual = productItem.getActualAmount();
				long origin = productItem.getAmount();
				req.setUserId(user.getUserId());
				req.setOrderId(ret.getOrderId());
				req.setStudentId(user.getChildId());
				req.setCouponAmount(origin-actual);
				req.setCouponType("0");
				req.setFullAmount(origin);
				req.setOriginalPayAmount(actual);
//				req.setOriginalPayNo(ret.get);
				req.setPayNo(ret.getOrderNo());
				req.setPayAmount(actual);
				req.setPayDate(startDate);
				req.setPayType(BUY);
				LocalUtil.req = req;
			}
			break;
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_btn:
				finish();
				break;
			case R.id.bt_renew_ok:
				payPop = new NewPayPop(NewPackageConfirmActivity.this,1);
//				payPop.showAsDropDown(v);
				payPop.showAtLocation(v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
		}
	}

	private void goToPay(){
//		String arg = ret.getPaymentInfo();
//		System.out.println("pay arg:"+arg);
//	if(MOBILEALIPAYSECURE.equals(payType)){
//			goAliApp(arg);
//		}else if(MOBILEWXPAYSECURE.equals(payType)){
//			goWxApp(arg);
//		}else if(MOBILCXETLPAYSECURE.equals(payType)){
//			goTlApp(arg);
//		}else if(MOBILXYETLPAYSECURE.equals(payType)){
//			goTlXYApp(arg);
//		}
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
		
//		Runnable payRunnable = new Runnable() {
//
//			@Override
//			public void run() {
//				AliPay alipay = new AliPay(NewPackageConfirmActivity.this, payHandler);
//				String result = alipay.pay(para);
//
//				Message msg = new Message();
//				msg.what = RQF_PAY;
//				msg.obj = result;
//				payHandler.sendMessage(msg);
//			}
//		};
//		Thread payThread = new Thread(payRunnable);
//		payThread.start();
	}
	
	/**支付宝支付结果处理方法
	 * @param data 支付宝返回的支付结果数据
	 */
	private void dealAliPay(String data) {
		//如果返回支付结果是空字符，那么不处理
		if(DataUtil.isNull(data))return;
		String[] data2 = data.split(";");
		Map<String,String> map = new HashMap<String,String>();
		for (String s : data2) {
			if(s.indexOf("=") < 0)continue;
			map.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
		}
		
		if(map.get("resultStatus")==null)return;
		if(map.get("resultStatus").equals("{9000}")){
			//代表支付宝支付成功
			//后台告诉自己服务器支付结果
			new PayResultAsyncTask(MOBILEALIPAYSECURE, "1",req).execute();
			//跳转到我的界面
//			CCApplication.app.finishAllActivity();
			finish();
//			newActivity(MyInformationActivity.class, null);
		}else if (map.get("resultStatus").equals("{4000}")){
			//代表支付宝支付失败
			//后台告诉自己服务器支付结果
			new PayResultAsyncTask(MOBILEALIPAYSECURE, "0",req).execute();
			//跳转到支付失败界面
			newActivity(PayFailActivity.class, null);
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
					String noncestr = json.getString("noncestr");
					String packagevalue = json.getString("package");
					String partnerid = json.getString("partnerid");
					String prepayid = json.getString("prepayid");
					String sign1 = json.getString("sign");
					String timestamp = json.getString("timestamp");
					if(api == null){
						api = WXAPIFactory.createWXAPI(NewPackageConfirmActivity.this, appid);
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
		APPayAssistEx.startPay(this, para.toString(), serverMode);
	}
	/**
	 * 跳转到通联信用卡支付
	 * @param para 参数
	 */
	private void goTlXYApp(final String para){
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
					System.out.println("payTime:"+payTime);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (null != payRes && payRes.equals(APPayAssistEx.RES_SUCCESS)) {
					DataUtil.getToast("支付成功");
//					finish();
//					CCApplication.app.finishAllActivity();
//					newActivity(MyInformationActivity.class, null);
					new PayResultAsyncTask(MOBILCXETLPAYSECURE, "1",req).execute();//写死的
				}else {
					DataUtil.getToast("支付失败");
					newActivity(PayFailActivity.class, null);
					finish();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 
	 *通知服务器的异步线程类
	 */
	class PayResultAsyncTask extends AsyncTask<String, String, String> {
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
			JSONObject json ;
			try{
				Gson gson = new Gson();
				
				req.setPaymentPlatformType(payType);
				req.setPayStatus(state);
				long time = System.currentTimeMillis();
				String platformPayDate = DataUtil.getCurrentDate(time);
				req.setPlatformPayDate(platformPayDate);
				String string = gson.toJson(req);
				json = new JSONObject(string);
				System.out.println("json:"+json);
				String url = new StringBuilder(Constants.SERVER_URL).append(Constants.COMMIT_ORDER_PAY).toString();
				result = HttpHelper.httpPostJson(NewPackageConfirmActivity.this,url, json);
				System.out.println("result:"+result);
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			Log.d("jiang", "onPostExecute " + result);
		}
	}
	
	/**
	 * 选择了套餐后生成订单
	 */
	public void createOrder() {
		payType = payPop.getPayType();
		NewOrder order = new NewOrder();
		long origin = productItem.getAmount();
		long actual = productItem.getActualAmount();
		long couponAmount = origin - actual;
		
		String dateValue,dateType;
		List<OrderItem> list = new ArrayList<OrderItem>();
		OrderItem item = order. new OrderItem();
		item.setAmount(actual);
		item.setFullAmount(origin);
		item.setCouponAmount(couponAmount);
		item.setCouponType("0");
		item.setProductCode(productItem.getProductCode());
		item.setProductName(productItem.getProductName());
		item.setGoodsNo("1");//先写死
		long time = System.currentTimeMillis();
		startDate = DataUtil.getCurrentDate(time);
		dateValue = productItem.getValidityDateValue();
		dateType = productItem.getValidityDateType();

		item.setStartDate(startDate);
		if(!DataUtil.isNullorEmpty(dateValue) && "null".equals(dateValue)==false){
			endDate = DataUtil.getLastDate(startDate, dateValue, dateType);
		}
		System.out.println("endDate:"+endDate);
		item.setEndDate(endDate);
		list.add(item);
		UserType user = CCApplication.getInstance().getPresentUser();
		order.setUserId(user.getUserId());
		order.setStudentId(user.getChildId());
		order.setPaymentPlatformType(payType);
		order.setOrderItemList(list);
		order.setCouponAmount(couponAmount);
		order.setCouponType("0");
		order.setFullAmount(origin);
		order.setOrderAmount(actual);
		Gson gson = new Gson();
		String string = gson.toJson(order);
		try {
			JSONObject json = new JSONObject(string);
			queryPost(Constants.CREATE_ORDER,json);
			action = CREATE_ORDER;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CommitOrderPayReq getReq(){
		return req;
	}
	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		System.out.println("onWXPayFinish, errCode = " + resp.errCode);
		String str = null;
		int i = resp.errCode;
		if (i == 0) {
			onMyActivity();
			CommitOrderPayReq req = new CommitOrderPayReq();
			new PayResultAsyncTask(MOBILEWXPAYSECURE, "1",req).execute();//写死的
			DataUtil.getToast("支付成功");
		}else if (i == -1) {
			str = "支付失败";
			if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("提示");
				builder.setMessage(str);
				builder.setNeutralButton("确定",new DialogInterface.OnClickListener() {  
	                @Override  
	                public void onClick(DialogInterface dialog,int which) {  
	                	onPayFailActivity();
	                	finish();
	                }  
	            });  
				builder.show();
			}
			onPayFailActivity();
			finish();
//			DataUtil.getToast("支付失败");
		}else if (i == -2) {
			onMyActivity();
			DataUtil.getToast("支付取消");
		}
	}
	
	/**
	 * 支付成功后跳转回 界面
	 */
	public void onMyActivity() {
		//跳转到我的界面
		CCApplication.app.finishAllActivity();
		newActivity(HomeActivity.class , null);
	}
	/**
	 * 支付失败后跳转回 “支付失败” 界面
	 */
	public void onPayFailActivity() {
		//跳转到支付失败界面
		newActivity(PayFailActivity.class, null);
	}
}
