package com.wcyc.zigui2.newapp.asynctask;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.CommitOrderPayReq;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;

import com.wcyc.zigui2.newapp.module.charge2.UpdateOrderReq;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

/**
 * 
 *通知服务器的异步线程类
 */
public class PayResultAsyncTask extends AsyncTask<String, String, String> {
	String orderNo , payType , state;
	CommitOrderPayReq req;
	Context mContext;
	/**
	 * @param userID 用户唯一ID
	 * @param childID 孩子ID
	 * @param orderNo 订单ID
	 * @param payType 支付方式  ALIPAY：支付宝；
	 * @param state 支付状态 0：失败   1：成功
	 */
	public PayResultAsyncTask(Context mContext,String payType,String state,CommitOrderPayReq req) {
		super();
		this.payType = payType;
		this.state = state;
		this.req = req;
		this.mContext = mContext;
	}
	@Override
	protected String doInBackground(String... params) {
		String result = null;
//		NewBaseBean ret;
//		JSONObject json ;
//		try{
//			Gson gson = new Gson();
//			
//			req.setPaymentPlatformType(payType);
//			req.setPayStatus(state);
////			long time = System.currentTimeMillis();
////			String platformPayDate = DataUtil.getCurrentDate(time);
//			String platformPayDate = DataUtil.GetBeijingTime();
//			req.setPlatformPayDate(platformPayDate);
//			String string = gson.toJson(req);
			
//			json = new JSONObject(string);
//			System.out.println("json:"+json);
//			String url = new StringBuilder(Constants.SERVER_URL).append(Constants.COMMIT_ORDER_PAY).toString();
//			result = HttpHelper.httpPostJson((BaseActivity) mContext,url, json);
//			System.out.println("result:"+result);
//			ret = JsonUtils.fromJson(result, NewBaseBean.class);
//			Log.d("PayResultAsyncTask", "onPayFinish:" + result);
//			if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
//				ServiceExpiredBean expired = CCApplication.getInstance().getServiceExpiredInfo();
//				expired.setProductExpired("1");
//				CCApplication.getInstance().setServiceExpiredInfo(expired);
//			}else{
//				//DataUtil.getToast("支付 失败");
//			}
					
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		return result;
	}
	@Override
	protected void onPostExecute(String result) {
		Log.d("PayResultAsyncTask", "onPostExecute " + result);
	}
}