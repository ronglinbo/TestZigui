package com.wcyc.zigui2.wxapi;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.asynctask.PayResultAsyncTask;
import com.wcyc.zigui2.newapp.bean.CommitOrderPayReq;
import com.wcyc.zigui2.newapp.module.charge2.NewPayPop;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargePriceActivity;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargeProductActivity;
import com.wcyc.zigui2.newapp.module.charge2.PayFailActivity;
import com.wcyc.zigui2.newapp.module.order.MyOrderActivity;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.LocalUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	public static final String MOBILEWXPAYSECURE = "WEIXINPAY";
    private IWXAPI api;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay);
        if(api == null){
	    	api = WXAPIFactory.createWXAPI(this, "Constants.APP_ID");
//	    	api = WXAPIFactory.createWXAPI(this, "wxef06aadd90fab5f1");
//	    	api.registerApp("wxef06aadd90fab5f1");
	        api.handleIntent(getIntent(), this);
        }
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		System.out.println("onPayFinish, errCode = " + resp.errCode+" msg:"+resp.errStr);
		String str = null;
		int i = resp.errCode;

		if (i == 0) {
			setResult(RESULT_OK);
			//onMyActivity();
			CommitOrderPayReq req = LocalUtil.req;
			new PayResultAsyncTask(this,MOBILEWXPAYSECURE, "1",req).execute();//写死的
			DataUtil.getToast("支付成功");
			onPaySuccusesActivity();
		}else if (i == -1) {
			str = "支付失败";
			setResult(RESULT_CANCELED);
			onPayFailActivity();
			finish();
			DataUtil.getToast("支付失败");
		}else if (i == -2) {
			setResult(RESULT_CANCELED);
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
	/**
	 * 支付成功后跳转回 “我的订单” 界面
	 */
	public void onPaySuccusesActivity() {
		String call = NewPayPop.call;
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


		} else {

			//原来逻辑
			//跳转到我的订单界面  已支付
			Bundle bundle=new Bundle();
			bundle.putInt("status",1);
			newActivity(MyOrderActivity.class, bundle);
		}





	}


	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		
	}
//	/**
//	 * 
//	 *通知服务器的异步线程类
//	 */
//	class PayResultAsyncTask extends AsyncTask<String, String, String> {
//		String userID , childID , orderNo , payType , state;
//		
//		/**
//		 * @param userID 用户唯一ID
//		 * @param childID 孩子ID
//		 * @param orderNo 订单ID
//		 * @param payType 支付方式  ALIPAY：支付宝；
//		 * @param state 支付状态 0：失败   1：成功
//		 */
//		public PayResultAsyncTask(String userID, String childID,
//				String orderNo, String payType, String state) {
//			super();
//			this.userID = userID;
//			this.childID = childID;
//			this.orderNo = orderNo;
//			this.payType = payType;
//			this.state = state;
//		}
//		@Override
//		protected String doInBackground(String... params) {
//			String result = null;
//			JSONObject json = new JSONObject();
//			try{
//				json.put("userID",userID);
//				json.put("childID",childID);
//				json.put("orderNo",orderNo);
//				json.put("payType",payType);
//				json.put("state",state);
//				String url = new StringBuilder(Constants.SERVER_URL).append("/rechargeService/payDone").toString();
//				result = HttpHelper.httpPostJson(url, json);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			return result;
//		}
//		@Override
//		protected void onPostExecute(String result) {
//			Log.d("jiang", "onPostExecute " + result);
//		}
//	}
//	private void dealweixin(String data){
//
//		//如果返回支付结果是空字符，那么不处理
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
//			new PayResultAsyncTask(CCApplication.app.getMemberInfo().getUserID(),
//					child.getChildID(), orderInfo.getOrderNo(), getPayType(), "1").execute();
//			//跳转到我的界面
//			CCApplication.app.finishAllActivity();
//			newActivity(MyInformationActivity.class, null);
//		}else if (map.get("resultStatus").equals("{4000}")){
//			//代表支付宝支付失败
//			//后台告诉自己服务器支付结果
//			new PayResultAsyncTask(CCApplication.app.getMemberInfo().getUserID(),
//					child.getChildID(), orderInfo.getOrderNo(), getPayType(), "0").execute();
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
//		}
////		Log.d("jiang", data);
////		if(!map.get("resultStatus").equals("{9000}")){
////			DataUtil.getToast(map.get("memo").replace("{", "").replace("}", ""));
////		}
//	
//	}
//	
}