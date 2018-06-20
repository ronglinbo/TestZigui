package com.wcyc.zigui2.newapp.module.charge2;

public class UpdateOrderReq{
	private String tradeNo;
	private String orderNo;
	private String tradeStatus;// 交易状态
	private String gmtPaymentDate;// 交易付款时间
	private String notifyId;// 交易平台通知id
	private String paymentPlatformType;//第三方支付平台类型（ALIPAY：支付宝）
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getGmtPaymentDate() {
		return gmtPaymentDate;
	}
	public void setGmtPaymentDate(String gmtPaymentDate) {
		this.gmtPaymentDate = gmtPaymentDate;
	}
	public String getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public String getPaymentPlatformType() {
		return paymentPlatformType;
	}
	public void setPaymentPlatformType(String paymentPlatformType) {
		this.paymentPlatformType = paymentPlatformType;
	}
}