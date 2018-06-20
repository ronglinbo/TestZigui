package com.wcyc.zigui2.newapp.module.charge2;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class PaymentInfo extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2234201521679454215L;
	private String orderId;
	private String orderNo;
	private String paymentInfo;

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
}