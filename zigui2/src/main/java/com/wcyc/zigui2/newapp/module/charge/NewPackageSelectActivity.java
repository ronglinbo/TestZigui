/*
 * 文 件 名:PackageSelectActivity.java
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

import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;



import com.google.gson.Gson;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.bean.CommitOrderPayReq;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewChild;

import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge.ChargeProduct.Product;
import com.wcyc.zigui2.newapp.module.charge.NewOrder.OrderItem;
import com.wcyc.zigui2.newapp.widget.NewPayPop;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
//import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;

/**
 * 选择套餐
 * 
 
 */
public class NewPackageSelectActivity extends BaseActivity implements
		OnClickListener{

	private Button bt_select_package;
	private NewPayPop payPop;
	private ListView listView;

	// 定义选择套餐界面的对象
	private static NewPackageSelectActivity instance = null;
	private LinearLayout title_back;
	private TextView new_content;
	private TextView title_right_tv;
	private TextView online_pay_tv;
	private TextView card_pay_tv;
	private LinearLayout online_pay;
	private LinearLayout card_pay;

	private int pos;//选择套餐项
	private ChargeProduct product;
	private List<Product> productList;
	private Product productItem;
	private String payType,startDate,endDate;//（yyyy-MM-dd HH:mm:ss）;
	private NewCreateOrderResult ret;
	private CommitOrderPayReq req;
	private ServiceExpiredBean expired;
//	private String paymentPlatformType;
	private static final int GET_CHARGE_INFO = 0;
	private static final int CREATE_ORDER = 1;
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
	public static final String MOBILCXETLPAYSECURE = "TLIANCXPAY";
	public static final String MOBILXYETLPAYSECURE = "TLIANXYPAY";
	/**
	 * 	"00" - 生产环境
		"01" - 测试环境，该环境中不发生真实交易
	 */
	String serverMode = "00";
	/**
	 * 支付宝支付结果通知消息标志
	 */
	private final int RQF_PAY = 0;
	private IWXAPI api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.new_package_select_list);
		
		JSONObject json = new JSONObject();
		try {
			json.put("classId", CCApplication.getInstance().getPresentUser().getClassId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		queryPost(Constants.GET_CHARGE_INFO_URL,json);
		action = GET_CHARGE_INFO;
		initView();
		initEvents();
	}

	/**
	 * 获取类的实例.
	 * 
	 * @return 类的实例
	 */
	public static NewPackageSelectActivity getInstance() {
		return instance;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		instance = null;
	}

	private void initEvents() {
//		backButton.setOnClickListener(this);
		bt_select_package.setOnClickListener(this);
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);
		title_right_tv.setVisibility(View.VISIBLE);
		title_right_tv.setOnClickListener(this);
		if(online_pay_tv != null){
			online_pay_tv.setOnClickListener(this);
		}
		if(card_pay_tv != null){
			card_pay_tv.setOnClickListener(this);
		}
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {				
				Bundle bundle = new Bundle();
				productItem = productList.get(arg2);
				bundle.putSerializable("productInfo", productItem);
//				newActivity(NewPackageConfirmActivity.class,bundle);
				payPop = new NewPayPop(NewPackageSelectActivity.this,1);
//				payPop.showAsDropDown(arg1);
				payPop.showAtLocation(arg1, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
		
	}


	private void initView() {
//		backButton = (Button) findViewById(R.id.title_btn);
//		backButton.setText(R.string.title_package_select);
		bt_select_package = (Button) findViewById(R.id.bt_select_package);

		listView = (ListView) findViewById(R.id.package_list);
		title_back = (LinearLayout) findViewById(R.id.title_back);
		new_content = (TextView) findViewById(R.id.new_content);
		new_content.setText("会员充值");
		title_right_tv = (TextView) findViewById(R.id.title_right_tv);
		title_right_tv.setText("充值记录");
		title_right_tv.setTextColor(getResources().getColor(R.color.blue));
//		online_pay_tv = (TextView) findViewById(R.id.online_pay_tv);
//		online_pay = (LinearLayout) findViewById(R.id.online_pay);
//		card_pay_tv = (TextView) findViewById(R.id.card_pay_tv);
//		card_pay = (LinearLayout) findViewById(R.id.card_pay);
	}

	@Override
	protected void getMessage(String data) {
		switch(action){
		case GET_CHARGE_INFO:
			System.out.println("GET_CHARGE_INFO:"+data);
			product = JsonUtils.fromJson(data, ChargeProduct.class);
			if(product.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
				DataUtil.getToast(product.getServerResult().getResultMessage());
			}else{
				productList = product.getProductList();
				listView.setAdapter(new OrderPackageAdapter(this,productList));
			}
			break;
		case CREATE_ORDER:
			Log.i("pay","CREATE_ORDER:"+data);
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
				LocalUtil.req = req;//给微信支付使用
			}
			break;	
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
//		case R.id.online_pay_tv:
//			card_pay.setVisibility(View.GONE);
//			online_pay.setVisibility(View.VISIBLE);
//			break;
//		case R.id.card_pay_tv:
//			online_pay.setVisibility(View.GONE);
//			card_pay.setVisibility(View.VISIBLE);
//			break;
		case R.id.title_right_tv:
			//跳转充值记录
			newActivity(NewPaymentRecordActivity.class, null);
			break;
		case R.id.title_back:
			finish();
			break;	
		}
		
	}
	
	/**
	 * 选择了套餐后生成订单
	 */
	public void createOrder() {
		payType = payPop.getPayType();
		NewOrder order = new NewOrder();
		
		long origin;
		long actual;
		long couponAmount;
		String ret[];
		String dateValue,dateType,serveStartDate;
		List<OrderItem> list = new ArrayList<OrderItem>();
		OrderItem item = order. new OrderItem();
				
		item.setCouponType("0");
		item.setProductCode(productItem.getProductCode());
		item.setProductName(productItem.getProductName());
		item.setGoodsNo("1");//先写死
		expired = CCApplication.getInstance().getServiceExpiredInfo();
		ret = DataUtil.computeStartDate(this);
		startDate = ret[0];//计算实际开始收费日期
		serveStartDate = ret[1];//计算服务开始日期
		long months = DataUtil.computeMonths(productItem,startDate);
		actual = productItem.getActualAmount()*months;
		origin = productItem.getAmount()*months;
		couponAmount = origin - actual;
		dateValue = productItem.getValidityDateValue();
		dateType = productItem.getValidityDateType();
		
		item.setStartDate(startDate);
		item.setAmount(actual);
		item.setFullAmount(origin);
		item.setCouponAmount(couponAmount);
		if(!DataUtil.isNullorEmpty(dateValue) && "null".equals(dateValue)==false){
			endDate = DataUtil.getLastDate(startDate, dateValue, dateType);//需要判断闰月
			if(expired != null){
//				expired.setEndDate(endDate);//?订单支付后才能设置
			}
		}
		System.out.println("createOrder endDate:"+endDate);
		if(DataUtil.isNullorEmpty(dateValue)){
			MemberDetailBean member = CCApplication.getInstance().getMemberDetail();
			List<NewChild> childList = member.getChildList();
			String childId = user.getChildId();
			if(childList != null){
				for(NewChild child:childList){//找到当前用户的孩子id
					if(childId.equals(child.getChildID())){
						String endDate = child.getGraduateDate();
						endDate += " 00:00:00";
						item.setEndDate(endDate);//?订单支付后才能设置
						break;
					}
				}
			}
		}else{
			item.setEndDate(endDate);//?订单支付后才能设置
		}
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
			System.out.println("create Order:"+json);
			queryPost(Constants.CREATE_ORDER,json);
			action = CREATE_ORDER;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void goToPay(){
		String arg = ret.getPaymentInfo();
		System.out.println("pay arg:"+arg);
		if(MOBILEALIPAYSECURE.equals(payType)){
			goAliApp(arg);
		}else if(MOBILEWXPAYSECURE.equals(payType)){
			goWxApp(arg);
		}else if(MOBILCXETLPAYSECURE.equals(payType)){
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
		
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
//				AliPay alipay = new AliPay(NewPackageSelectActivity.this, payHandler);
//				String result = alipay.pay(para);
//
//				Message msg = new Message();
//				msg.what = RQF_PAY;
//				msg.obj = result;
//				payHandler.sendMessage(msg);
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
						api = WXAPIFactory.createWXAPI(NewPackageSelectActivity.this, appid);
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
	 * 通联支付回调的返回码
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

				String platformPayDate = DataUtil.GetBeijingTime();
				System.out.println("platformPayDate:"+platformPayDate);
				req.setPlatformPayDate(platformPayDate);
				String string = gson.toJson(req);
				json = new JSONObject(string);
				System.out.println("json:"+json);
				String url = new StringBuilder(Constants.SERVER_URL).append(Constants.COMMIT_ORDER_PAY).toString();
				result = HttpHelper.httpPostJson(NewPackageSelectActivity.this,url, json);
				NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
				if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
//					expired.setProductExpired("1");
//					CCApplication.getInstance().setServiceExpiredInfo(expired);
				}
				System.out.println("COMMIT_ORDER_PAY result:"+result);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
		}
	}
}
