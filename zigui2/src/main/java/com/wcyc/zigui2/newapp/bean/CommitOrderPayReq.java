package com.wcyc.zigui2.newapp.bean;
//提交订单支付
public class CommitOrderPayReq extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3238910561365221791L;
	private String orderId;
	private String userId;
	private String studentId;
	private long couponAmount;
	private String couponType;
	private long fullAmount;
	private long failReason;
	private String notifyId;
	private long originalPayAmount;
	private String originalPayNo;
	private long payAmount;
	private String payDate;
	private String paymentPlatformType;
	private String payNo;
	private String payType;
	private String platformPayDate;
	private String refundReason;
	private String returnUnigueValue;
	private String payStatus;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public long getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(long couponAmount) {
		this.couponAmount = couponAmount;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public long getFullAmount() {
		return fullAmount;
	}
	public void setFullAmount(long fullAmount) {
		this.fullAmount = fullAmount;
	}
	public long getFailReason() {
		return failReason;
	}
	public void setFailReason(long failReason) {
		this.failReason = failReason;
	}
	public String getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public long getOriginalPayAmount() {
		return originalPayAmount;
	}
	public void setOriginalPayAmount(long originalPayAmount) {
		this.originalPayAmount = originalPayAmount;
	}
	public String getOriginalPayNo() {
		return originalPayNo;
	}
	public void setOriginalPayNo(String originalPayNo) {
		this.originalPayNo = originalPayNo;
	}
	public long getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(long payAmount) {
		this.payAmount = payAmount;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPaymentPlatformType() {
		return paymentPlatformType;
	}
	public void setPaymentPlatformType(String paymentPlatformType) {
		this.paymentPlatformType = paymentPlatformType;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPlatformPayDate() {
		return platformPayDate;
	}
	public void setPlatformPayDate(String platformPayDate) {
		this.platformPayDate = platformPayDate;
	}
	public String getRefundReason() {
		return refundReason;
	}
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}
	public String getReturnUnigueValue() {
		return returnUnigueValue;
	}
	public void setReturnUnigueValue(String returnUnigueValue) {
		this.returnUnigueValue = returnUnigueValue;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
}