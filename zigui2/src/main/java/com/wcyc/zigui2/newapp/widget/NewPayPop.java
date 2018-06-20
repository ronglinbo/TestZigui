package com.wcyc.zigui2.newapp.widget;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wcyc.zigui2.R;


import com.wcyc.zigui2.newapp.module.charge2.NewPaymentRecordActivity;
import com.wcyc.zigui2.utils.DataUtil;

public class NewPayPop extends PopupWindow{

	private View view;
	private Button cancel;
	private ImageView new_weixin , new_zhifubao , new_chuxuka , new_xinyongka;
	public static AsyncHttpClient client = new AsyncHttpClient();
	private String payType;
	//根据type是提交新订单，还是原来的订单
	public NewPayPop(final Activity context,final int type){
		super(context);
		
		LayoutInflater inflater = context.getLayoutInflater();
		view = inflater.inflate(R.layout.new_pay_pop, null);
		new_weixin = (ImageView) view.findViewById(R.id.new_weixin);
		
		new_zhifubao = (ImageView) view.findViewById(R.id.new_zhifubao);
		new_chuxuka = (ImageView) view.findViewById(R.id.new_chuxuka);
		new_xinyongka = (ImageView) view.findViewById(R.id.new_xinyongka);
		cancel = (Button) view.findViewById(R.id.cancel);
		
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
			
		});
		
		this.setContentView(view);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setOutsideTouchable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		
		new_weixin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				payType = "WEIXINPAY";
				if(type == 1){
					CreateOrder();
				}else{
					ReCommitOrder();
				}
				dismiss();			
			}
		});
		
		new_zhifubao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payType = "ALIPAY";
				if(type == 1){
					//CreateOrder();
				}else{
					ReCommitOrder();
				}
				dismiss();
			}});
		
		new_chuxuka.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payType = "TLIANCXPAY";
				if(type == 1){
					CreateOrder();
				}else{
					ReCommitOrder();
				}
				dismiss();
			}});
		
		new_xinyongka.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payType = "TLIANXYPAY";
				if(type == 1){
					CreateOrder();
				}else{
					ReCommitOrder();
				}
				dismiss();
			}});
	}
	
	private void CreateOrder(){
	//	NewPackageSelectActivity.getInstance().createOrder();
	}
	
	private void ReCommitOrder(){
		NewPaymentRecordActivity.getInstance().recommitOrder();
	}
	
	public String getPayType(){
		return payType;
	}
}