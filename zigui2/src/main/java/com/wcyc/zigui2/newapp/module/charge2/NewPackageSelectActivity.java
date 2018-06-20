///*
// * 文 件 名:PackageSelectActivity.java
// * 版 本 号： 1.05
// */
//package com.wcyc.zigui2.newapp.module.charge2;
//
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.TextView;
//
//import com.alipay.android.app.sdk.AliPay;
//import com.allinpay.appayassistex.APPayAssistEx;
//import com.tencent.mm.sdk.modelpay.PayReq;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.wcyc.zigui2.R;
//import com.wcyc.zigui2.core.BaseActivity;
//import com.wcyc.zigui2.core.CCApplication;
//
//import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
//import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
//import com.wcyc.zigui2.newapp.bean.CommitOrderPayReq;
//import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
//import com.wcyc.zigui2.newapp.bean.UserType;
//import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.PackageRoduct;
//import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.SchoolProducts;
//import com.wcyc.zigui2.utils.Constants;
//import com.wcyc.zigui2.utils.DataUtil;
//import com.wcyc.zigui2.utils.JsonUtils;
//import com.wcyc.zigui2.utils.LocalUtil;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 选择套餐
// *
//
// */
//public class NewPackageSelectActivity extends BaseActivity implements
//		OnClickListener, HttpRequestAsyncTaskListener {
//
//	private Button goPay;
//	private NewPayPop payPop;
//	private ListView listView,infoListView;
//
//	// 定义选择套餐界面的对象
//	private static NewPackageSelectActivity instance = null;
//	private LinearLayout title_back;
//	private TextView new_content;
//	private TextView title_right_tv;
//
//	private int pos;//选择套餐项
//	private SysProductListInfo sysProductListInfo;
//	private List<SysProductListInfo.Schoolallproductlist> productList;
//	private PackageRoduct productItem;
//	private SchoolProducts item;
//	private String payType,startDate,endDate;//（yyyy-MM-dd HH:mm:ss）;
//	private NewCreateOrderResult ret;
//	private CommitOrderPayReq req;
//	private UpdateOrderReq updateOrder;
//	private ServiceExpiredBean expired;
//	private ProductInfo productInfo;
////	private String paymentPlatformType;
//	private static final int GET_CHARGE_INFO = 0;
//	private static final int CREATE_ORDER = 1;
//	private static final int GET_PRODUCT_INFO = 2;
//	private static final int GET_PAYMENT_INFO = 3;
//	//支付方式 1：购买 2：部分退款 3：全额退款
//	private static final String BUY = "1";
//	private static final String PART_RETURN = "2";
//	private static final String ALL_RETURN = "3";
//
//	/**
//	 * 支付支付方式
//	 * 1 支付宝，2 微信，3 通联储蓄卡，4 通联信用卡
//	 */
//	public static final String MOBILEALIPAYSECURE = "ALIPAY";
//	public static final String MOBILEWXPAYSECURE = "WEIXINPAY";
//	public static final String MOBILCXETLPAYSECURE = "TLIANCXPAY";
//	public static final String MOBILXYETLPAYSECURE = "TLIANXYPAY";
//	/**
//	 * 	"00" - 生产环境
//		"01" - 测试环境，该环境中不发生真实交易
//	 */
//	String serverMode = "00";
//	/**
//	 * 支付宝支付结果通知消息标志
//	 */
//	private final int RQF_PAY = 0;
//	private IWXAPI api;
//	private UserType user;
//	private int productId = 0;
//	private String function;//web调用
//	private HashMap<String,Boolean> isPackageSelected = new HashMap<String,Boolean>();
//	private HashMap<String,Boolean> isTimeSelected = new HashMap<String,Boolean>();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		instance = this;
//		setContentView(R.layout.new_package_select_list);
//		parseIntent();
//		getProductList();
//		initView();
//		initEvents();
//	}
//
//	private void parseIntent() {
//		Intent intent = getIntent();
//		if (intent != null) {
//			Bundle bundle = intent.getExtras();
//			if (bundle != null){
//				function = bundle.getString("call");
//				if("html5".equals(function)){
//
//				}
//			}
//		}
//	}
//	/**
//	 * 获取类的实例.
//	 *
//	 * @return 类的实例
//	 */
//	public static NewPackageSelectActivity getInstance() {
//		return instance;
//	}
//
//	@Override
//	public void finish() {
//		// TODO Auto-generated method stub
//		super.finish();
//		instance = null;
//	}
//
//	private void initEvents() {
////		backButton.setOnClickListener(this);
//		goPay.setOnClickListener(this);
//		title_back.setVisibility(View.VISIBLE);
//		title_back.setOnClickListener(this);
//		title_right_tv.setVisibility(View.VISIBLE);
//		title_right_tv.setOnClickListener(this);
//		listView.setOnItemClickListener(new OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				RadioButton check = (RadioButton) arg1.findViewById(R.id.check);
//				for(String key:isPackageSelected.keySet()){
//					isPackageSelected.put(key,false);
//				}
//				isPackageSelected.put(String.valueOf(arg2), true);
////				boolean checked = getCheckStatus(isPackageSelected,arg2);
//				check.setChecked(true);
//				((ProductAdapter) arg0.getAdapter()).setSelected(isPackageSelected);
//				((BaseAdapter) arg0.getAdapter()).notifyDataSetChanged();
//
//				SysProductListInfo.Schoolallproductlist item = productList.get(arg2);
//				if(item != null){
//					getProductInfo(item.getProductCode());
//				}
//				buttonEnable(goPay,false);
//			}
//		});
//
//		infoListView.setOnItemClickListener(new OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				RadioButton check = (RadioButton) arg1.findViewById(R.id.check);
//				for(String key:isTimeSelected.keySet()){
//					isTimeSelected.put(key,false);
//				}
//				isTimeSelected.put(String.valueOf(arg2), true);
////				boolean checked = getCheckStatus(isTimeSelected,arg2);
//				check.setChecked(true);
//				((ProductInfoAdapter) arg0.getAdapter()).setSelected(isTimeSelected);
//				((BaseAdapter) arg0.getAdapter()).notifyDataSetChanged();
//
//				if(productInfo != null){
//					List<SchoolProducts> info = productInfo.getSchoolProductsList();
//					if(info != null){
//						item = info.get(arg2);
//						productId = item.getId();
//					}
//				}else{
//					List<SchoolProducts> info = product.getSchoolProducts();
//					if(info != null){
//						item = info.get(arg2);
//						productId = item.getId();
//					}
//				}
//				buttonEnable(goPay,true);
//			}
//
//		});
//	}
//
//	private void initView() {
//		goPay = (Button) findViewById(R.id.bt_select_package);
//		buttonEnable(goPay,false);
//		listView = (ListView) findViewById(R.id.package_list);
//		infoListView = (ListView) findViewById(R.id.due_list);
//		title_back = (LinearLayout) findViewById(R.id.title_back);
//		new_content = (TextView) findViewById(R.id.new_content);
//		new_content.setText("会员充值");
//		title_right_tv = (TextView) findViewById(R.id.title_right_tv);
//		title_right_tv.setText("充值记录");
//		title_right_tv.setTextColor(getResources().getColor(R.color.blue));
//	}
//
//	private void buttonEnable(Button button,boolean enable){
//		if(enable){
//			button.setClickable(true);
//			button.setEnabled(true);
//			button.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
//		}else{
//			button.setClickable(false);
//			button.setEnabled(false);
//			button.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
//		}
//	}
//
//	private void getProductList(){
//		user = CCApplication.getInstance().getPresentUser();
//		JSONObject json = new JSONObject();
//		try {
//			if(user != null){
//				json.put("parentId", user.getUserId());
//				json.put("userType",user.getUserType());
//				json.put("schoolId",user.getSchoolId());
//				json.put("studentId", user.getChildId());
//				System.out.println("请求套餐列表 json:"+json);
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		queryPost(Constants.GET_VIP_SERVICE_INFO,json);
//		action = GET_CHARGE_INFO;
//	}
//
//	private void getProductInfo(int productCode){
//		JSONObject json = new JSONObject();
//		if(user != null){
//			try {
//				json.put("classId",user.getClassId());
//				json.put("productCode",productCode);
//				json.put("schoolId",user.getSchoolId());
//				System.out.println("请求某一套餐对应的详情:"+json);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			queryPost(Constants.GET_PRODUCT_TIME,json);
//			action = GET_PRODUCT_INFO;
//		}
//	}
//
//	private void getPaymentInfo(String orderNo){
//		JSONObject json = new JSONObject();
//		try {
//			json.put("orderId", orderNo);
//			json.put("paymentPlatformType",payType);
//			System.out.println("请求支付参数:"+json);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		action = GET_PAYMENT_INFO;
////		queryPost(Constants.GET_PAYMENT_INFO, json);
//		new HttpRequestAsyncTask(json,this,this).execute(Constants.GET_PAYMENT_INFO);
//	}
//
//	@Override
//	protected void getMessage(String data) {
//		switch(action){
//		case GET_CHARGE_INFO:
//			System.out.println("获取套餐列表结果:"+data);
//            sysProductListInfo = JsonUtils.fromJson(data, SysProductListInfo.class);
//			if(sysProductListInfo.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
//				DataUtil.getToast(sysProductListInfo.getServerResult().getResultMessage());
//			}else{
//				productList = sysProductListInfo.getSchoolAllProductList();
//				if(productList.size() > 0) {
//					findViewById(R.id.rl_package).setVisibility(View.VISIBLE);
//					listView.setAdapter(new ProductAdapter(this, productList));
//					//默认显示第一个
//					isPackageSelected.put(String.valueOf(0), true);
//					((ProductAdapter) listView.getAdapter()).setSelected(isPackageSelected);
//
//					List<SchoolProducts> info = product.getSchoolProducts();
//					infoListView.setAdapter(new ProductInfoAdapter(this, info));
//					infoListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//				}else{
//					DataUtil.getToast("无套餐列表信息");
//				}
//			}
//			break;
//		case CREATE_ORDER:
//			System.out.println("创建订单结果:"+data);
//			ret = JsonUtils.fromJson(data, NewCreateOrderResult.class);
//
//			if(ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
//				DataUtil.getToast(ret.getServerResult().getResultMessage());
//			}else{
//				if(ret.getSysOrder() != null){
//					getPaymentInfo(ret.getSysOrder().getId()+"");
//				}
//			}
//			break;
//		case GET_PRODUCT_INFO:
//			System.out.println("获取某一套餐对应的详情结果:"+data);
//			productInfo = JsonUtils.fromJson(data, ProductInfo.class);
//			if(productInfo.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
//				DataUtil.getToast(productInfo.getServerResult().getResultMessage());
//			}else{
//				List<SchoolProducts> info = productInfo.getSchoolProductsList();
//				infoListView.setAdapter(new ProductInfoAdapter(this,info));
//				infoListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//			}
//			break;
//		case GET_PAYMENT_INFO:
//			parsePayMentInfo(data);
//			break;
//		}
//
//	}
//
//	private void parsePayMentInfo(String data){
//		System.out.println("获取支付参数结果:"+data);
//		PaymentInfo payInfo = JsonUtils.fromJson(data, PaymentInfo.class);
//		if(payInfo.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
//			DataUtil.getToast(payInfo.getServerResult().getResultMessage());
//		}else{
//			goToPay(payInfo.getPaymentInfo());
//			req = new CommitOrderPayReq();
//			UserType user = CCApplication.getInstance().getPresentUser();
//			//			String childId = CCApplication.app.getPresentUser().getChildId();
//			long actual = item.getActualAmount();
//			long origin = item.getAmount();
//			req.setUserId(user.getUserId());
//			req.setOrderId(payInfo.getOrderId());
//			req.setStudentId(user.getChildId());
//			req.setCouponAmount(origin-actual);
//			req.setCouponType("0");
//			req.setFullAmount(origin);
//			req.setOriginalPayAmount(actual);
//			//			req.setOriginalPayNo(ret.get);
//			req.setPayNo(ret.getSysOrder().getOrderNo());
//			req.setPayAmount(actual);
////			req.setPlatformPayDate(startDate);
////			req.setPayDate(startDate);
//			req.setPayType(BUY);
//			LocalUtil.req = req;//给微信支付使用
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//
//		switch (v.getId()) {
//		case R.id.title_right_tv:
//			//跳转充值记录
//			newActivity(NewPaymentRecordActivity.class, null);
//			break;
//		case R.id.title_back:
//			finish();
//			break;
//		case R.id.bt_select_package:
//			payPop = new NewPayPop(NewPackageSelectActivity.this,1);
//			payPop.showAtLocation(v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//			break;
//		}
//
//	}
//
//	/**
//	 * 选择了套餐后生成订单
//	 */
//	public void createOrder() {
//		payType = payPop.getPayType();
//		try {
//			JSONObject json = new JSONObject();
//			json.put("productId", productId);
//			json.put("payType", payType);
//			json.put("studentId",user.getChildId());
//			json.put("parentId", user.getUserId());
//			json.put("schoolId", user.getSchoolId());
//			json.put("deviceType","ANDROID");
//			System.out.println("create Order:"+json);
////			startDate = DataUtil.getPayDate(this);
//			queryPost(Constants.CREATE_SYS_ORDER,json);
//			action = CREATE_ORDER;
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private void goToPay(String arg){
////		String arg = ret.getSysOrder().getPaymentInfo();
//		if(arg == null) return;
//		System.out.println("pay arg:"+arg);
//		if(MOBILEALIPAYSECURE.equals(payType)){
//			goAliApp(arg);
//		}else if(MOBILEWXPAYSECURE.equals(payType)){
//			goWxApp(arg);
//		}else if(MOBILCXETLPAYSECURE.equals(payType)){
//			goTlApp(arg);
//		}else if(MOBILXYETLPAYSECURE.equals(payType)){
//			goTlXYApp(arg);
//		}
//	}
//	/**
//     * 处理支付的handler
//     */
//    public Handler payHandler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case RQF_PAY:
//				dealAliPay((String) msg.obj);
//				break;
//			default:
//				break;
//			}
//		}
//    };
//	/**
//	 * 跳转到支付宝应用
//	 * @param para 参数
//	 */
//	private void goAliApp(final String para){
//
////		para.contains("out_trade_no");
//
//		Runnable payRunnable = new Runnable() {
//
//			@Override
//			public void run() {
//				AliPay alipay = new AliPay(NewPackageSelectActivity.this, payHandler);
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
//	}
//
//	/**支付宝支付结果处理方法
//	 * @param data 支付宝返回的支付结果数据
//	 */
//	private void dealAliPay(String data) {
//		//如果返回支付结果是空字符，那么不处理
//		System.out.println("支付宝返回的数据："+data);
//		if(DataUtil.isNull(data))return;
//		String[] data2 = data.split(";");
//		Map<String,String> map = new HashMap<String,String>();
//		for (String s : data2) {
//			if(s.indexOf("=") < 0)continue;
//			map.put(s.substring(0, s.indexOf("=")), s.substring(s.indexOf("=") + 1));
//		}
//		if(map.get("resultStatus")==null)return;
//		if(map.get("resultStatus").equals("{9000}")){
//			//代表支付宝支付成功
//			//后台告诉自己服务器支付结果
//			new PayResultAsyncTask(MOBILEALIPAYSECURE, "1",req).execute();
//			//跳转到我的界面
////			CCApplication.app.finishAllActivity();
//			setResult(RESULT_OK);
//			finish();
////			newActivity(MyInformationActivity.class, null);
//		}else if (map.get("resultStatus").equals("{4000}")){
//			//代表支付宝支付失败
//			//后台告诉自己服务器支付结果
//			new PayResultAsyncTask(MOBILEALIPAYSECURE, "0",req).execute();
//			//跳转到支付失败界面
//			newActivity(PayFailActivity.class, null);
//		}else if (map.get("resultStatus").equals("{8000}")){
//			//代表正在处理中
//
//		}else if (map.get("resultStatus").equals("{6001}")){
//			//代表用户中途取消
//
//		}else if (map.get("resultStatus").equals("{6002}")){
//			//代表网络连接出错
//			DataUtil.getToast(map.get("memo").replace("{", "").replace("}", ""));
//		}else if (map.get("resultStatus").equals("{4001}")){
//			//代表参数错误
//			DataUtil.getToast(map.get("memo").replace("{", "").replace("}", ""));
//		}else if(map.get("resultStatus").equals("{7001}")){
//			DataUtil.getToast(map.get("memo").replace("{", "").replace("}", ""));
//		}
//	}
//	/**
//	 * 跳转到微信支付应用
//	 * @param payParam 参数
//	 */
//	private void goWxApp(final String  payParam){
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// 传回的字符串
////				{\"appid\":\"wxef06aadd90fab5f1\"," +
////						"\"noncestr\":\"4bdcc4628e054740ae3092f49a24d82e\"," +
////						"\"package\":\"Sign=WXPay\",\"partnerid\":\"1230710001\"," +
////						"\"prepayid\":\"1201000000150519947090d34c02b4c5\"," +
////						"\"sign\":\"63619f9945f10021b578dcffca57d1dc2da52c5e\"," +
////						""timestamp\":\"1432014786\"}
//
//				JSONObject json;
//				try {
//					json = new JSONObject(payParam);
//					String appid = json.getString("appid");
////					String appid = "wx2e813871dfe69697";//"wxef06aadd90fab5f1";//com.wcyc.zigui2生成的appid
//					System.out.println("appid:"+appid);
//					String noncestr = json.getString("noncestr");
//					String packagevalue = json.getString("package");
//					String partnerid = json.getString("partnerid");
//					String prepayid = json.getString("prepayid");
//					String sign1 = json.getString("sign");
//					String timestamp = json.getString("timestamp");
//					Log.i("wxpay","payParam:"+payParam);
//					if(api == null){
//						api = WXAPIFactory.createWXAPI(NewPackageSelectActivity.this, appid);
//						// 将该app注册到微信
//						api.registerApp(appid);
//					}
//
//					PayReq request = new PayReq();
//					//微信分配的公众账号ID
//					request.appId = appid;
//					//信支付分配的商户号
//					request.partnerId = partnerid;
//					//微信返回的支付交易会话ID
//					request.prepayId= prepayid;
//					//暂填写固定值Sign=WXPay
//					request.packageValue = packagevalue;
//					//随机字符串，不长于32位
//					request.nonceStr= noncestr;
//					//时间戳
//					request.timeStamp= timestamp;
//					//签名
//					request.sign= sign1;
//					//调用api接口发送数据到微信
//					boolean isSendSucces = api.sendReq(request);
//					if(isSendSucces){
//						System.out.println("发送成功");
//					}else{
//						DataUtil.getToast("请安装最新版微信");
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		})
//		.start();
//	}
//
//	/**
//	 * 跳转到通联储蓄卡支付
//	 * @param para 参数
//	 */
//	private void goTlApp(final String para){
//		APPayAssistEx.startPay(this, para.toString(), serverMode);
//	}
//	/**
//	 * 跳转到通联信用卡支付
//	 * @param para 参数
//	 */
//	private void goTlXYApp(final String para){
//		APPayAssistEx.startPay(this, para.toString(), serverMode);
//	}
//
//	/**
//	 * 通联支付回调的返回码
//	 */
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (APPayAssistEx.REQUESTCODE == requestCode) {
//			if (null != data) {
//				String payRes = null;
//				String payAmount = null;
//				String payTime = null;
//				try {
//					JSONObject resultJson = new JSONObject(data.getExtras().getString("result"));
//					payRes = resultJson.getString(APPayAssistEx.KEY_PAY_RES);
//					payAmount = resultJson.getString("payAmount");
//					payTime = resultJson.getString("payTime");
//					System.out.println("payTime:"+payTime);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				if (null != payRes && payRes.equals(APPayAssistEx.RES_SUCCESS)) {
//					DataUtil.getToast("支付成功");
//					setResult(RESULT_OK);
//					finish();
////					CCApplication.app.finishAllActivity();
////					newActivity(MyInformationActivity.class, null);
//					new PayResultAsyncTask(MOBILCXETLPAYSECURE, "1",req).execute();//写死的
//				}else {
//					DataUtil.getToast("支付失败");
//					newActivity(PayFailActivity.class, null);
//					setResult(RESULT_CANCELED);
//					finish();
//				}
//			}
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//
//	@Override
//	public void onRequstComplete(String result) {
//		parsePayMentInfo(result);
//	}
//
//	@Override
//	public void onRequstCancelled() {
//
//	}
//
//	/**
//	 *
//	 *通知服务器的异步线程类
//	 */
//	class PayResultAsyncTask extends AsyncTask<String, String, String> {
//		String orderNo , payType , state;
//		CommitOrderPayReq req;
//		/**
//		 * @param payType 支付方式  ALIPAY：支付宝；
//		 * @param state 支付状态 0：失败   1：成功
//		 */
//		public PayResultAsyncTask(String payType,String state,CommitOrderPayReq req) {
//			super();
//			this.payType = payType;
//			this.state = state;
//			this.req = req;
//		}
//		@Override
//		protected String doInBackground(String... params) {
//			String result = null;
////			JSONObject json ;
////			try{
////				Gson gson = new Gson();
////
////				req.setPaymentPlatformType(payType);
////				req.setPayStatus(state);
////
////				String platformPayDate = DataUtil.GetBeijingTime();
////				System.out.println("platformPayDate:"+platformPayDate);
////				req.setPlatformPayDate(platformPayDate);
////				String string = gson.toJson(req);
////				json = new JSONObject(string);
////				System.out.println("提交订单json:"+json);
////				String url = new StringBuilder(Constants.SERVER_URL).append(Constants.COMMIT_ORDER_PAY).toString();
////				result = HttpHelper.httpPostJson(NewPackageSelectActivity.this,url, json);
////				System.out.println("提交订单 result:"+result);
//////				NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
//////				if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
//////					expired.setProductExpired("1");
//////					CCApplication.getInstance().setServiceExpiredInfo(expired);
//////				}
////
////			}catch(Exception e){
////				e.printStackTrace();
////			}
//			return result;
//		}
//		@Override
//		protected void onPostExecute(String result) {
//		}
//	}
//}
