package com.wcyc.zigui2.newapp.module.charge2;


import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.loopj.android.http.AsyncHttpClient;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.module.order.Order;
import com.wcyc.zigui2.newapp.module.order.PayUtil;

public class NewPayPop extends PopupWindow{
	private View view;
	private Button cancel;
	private ImageView new_weixin , new_zhifubao , new_chuxuka , new_xinyongka;
	public static AsyncHttpClient client = new AsyncHttpClient();
	private  String payType;
	public   static  String call;
	private int type=0;
    private Order order;
	private String order_no;
	private String  url;//回调url
	private BaseActivity context;
    private   SysProductListInfo.Schoolallproductlist schoolallproductlist;
	public NewPayPop(final BaseActivity context, final int type, Order order){
	          this(context,type);
		      this.order=order;

	}
	public NewPayPop(final BaseActivity context, final int type, String order_no,String url){
		this(context,type);
		this.order_no=order_no;
		this.url=url;

	}
	public NewPayPop(final BaseActivity context, final int type, SysProductListInfo.Schoolallproductlist schoolallproductlist){
		this(context,type);
		this.schoolallproductlist=schoolallproductlist;

	}

	private  void getOrderByType(){
		if(type == 1){
			CreateOrder();
		}else if(type == 2){
			ReCommitOrder();
		}else if(type == 3){
			CreateOrder();
		}else if(type == 4){
			ReCommitOrder1();
		}else if(type == 5){

			PayUtil.getInstance().createOrder(payType, CCApplication.getInstance().getPresentUser(),schoolallproductlist,context);
		}

	}
	//根据type是提交新订单，还是原来的订单
	public NewPayPop(final BaseActivity context, final int type){
		super(context);
		this.type=type;
		this.context=context;
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
				getOrderByType();
				dismiss();			
			}
		});
		
		new_zhifubao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payType = "ALIPAY";
				getOrderByType();
				dismiss();
			}});
		
		new_chuxuka.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				payType = "TLIANCXPAY";
				payType = "CXKPAY";
				getOrderByType();
				dismiss();
			}});
		
		new_xinyongka.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				payType = "TLIANXYPAY";
				payType = "XYKPAY";
				getOrderByType();
				dismiss();
			}});
	}
	
	private void CreateOrder(){
//		System.out.println("==type==="+type);
		if(type==1){
		//	NewPackageSelectActivity.getInstance().createOrder();
		}else if(type==3){
			NewRechargePriceActivity.getInstance().createOrder();
		}
	}

	private void ReCommitOrder1(){
		PayUtil payUtil=PayUtil.getInstance();

		payUtil.commitOrder(order,payType,  context);
	}
	private void ReCommitOrder(){
		NewPaymentRecordActivity.getInstance().recommitOrder();
	}
	
	public String getPayType(){
		return payType;
	}
}