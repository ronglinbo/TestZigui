package com.wcyc.zigui2.newapp.module.charge2;

import java.io.Serializable;
import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class NewOrderDetail extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6192035874040228929L;
	
	private OrderPayInfo orderPayInfo;
    class OrderPayInfo{
        private long id;
        private long orderId;
        private String payNo;
        private long payAmount;
        private long fullAmount;
        private long couponAmount;
        private String couponType;
        private String payType;
        private String paymentPlatformType;
        private String returnUnigueValue;
        private String notifyId;
        private String platformPayDate;
        private String channel;
        private long userId;
        private long status;
        private String currency;
        private String payDate;
        private String constrantDate;
        private String originalPayNo;
        private long originalPayAmount;
        private String refundReason;
        private String failReason;
        private long createUserId;
        private String createUserName;
        private String createDate;
        private long updateUserId;
        private String updateUserName;
        private String updateTime;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getPayNo() {
			return payNo;
		}
		public void setPayNo(String payNo) {
			this.payNo = payNo;
		}
		public long getPayAmount() {
			return payAmount;
		}
		public void setPayAmount(long payAmount) {
			this.payAmount = payAmount;
		}
		public long getFullAmount() {
			return fullAmount;
		}
		public void setFullAmount(long fullAmount) {
			this.fullAmount = fullAmount;
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
		public String getPayType() {
			return payType;
		}
		public void setPayType(String payType) {
			this.payType = payType;
		}
		public String getPaymentPlatformType() {
			return paymentPlatformType;
		}
		public void setPaymentPlatformType(String paymentPlatformType) {
			this.paymentPlatformType = paymentPlatformType;
		}
		public String getReturnUnigueValue() {
			return returnUnigueValue;
		}
		public void setReturnUnigueValue(String returnUnigueValue) {
			this.returnUnigueValue = returnUnigueValue;
		}
		public String getNotifyId() {
			return notifyId;
		}
		public void setNotifyId(String notifyId) {
			this.notifyId = notifyId;
		}
		public String getPlatformPayDate() {
			return platformPayDate;
		}
		public void setPlatformPayDate(String platformPayDate) {
			this.platformPayDate = platformPayDate;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		public long getStatus() {
			return status;
		}
		public void setStatus(long status) {
			this.status = status;
		}
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public String getPayDate() {
			return payDate;
		}
		public void setPayDate(String payDate) {
			this.payDate = payDate;
		}
		public String getConstrantDate() {
			return constrantDate;
		}
		public void setConstrantDate(String constrantDate) {
			this.constrantDate = constrantDate;
		}
		public String getOriginalPayNo() {
			return originalPayNo;
		}
		public void setOriginalPayNo(String originalPayNo) {
			this.originalPayNo = originalPayNo;
		}
		public long getOriginalPayAmount() {
			return originalPayAmount;
		}
		public void setOriginalPayAmount(long originalPayAmount) {
			this.originalPayAmount = originalPayAmount;
		}
		public String getRefundReason() {
			return refundReason;
		}
		public void setRefundReason(String refundReason) {
			this.refundReason = refundReason;
		}
		public String getFailReason() {
			return failReason;
		}
		public void setFailReason(String failReason) {
			this.failReason = failReason;
		}
		public long getCreateUserId() {
			return createUserId;
		}
		public void setCreateUserId(long createUserId) {
			this.createUserId = createUserId;
		}
		public String getCreateUserName() {
			return createUserName;
		}
		public void setCreateUserName(String createUserName) {
			this.createUserName = createUserName;
		}
		public String getCreateDate() {
			return createDate;
		}
		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}
		public long getUpdateUserId() {
			return updateUserId;
		}
		public void setUpdateUserId(long updateUserId) {
			this.updateUserId = updateUserId;
		}
		public String getUpdateUserName() {
			return updateUserName;
		}
		public void setUpdateUserName(String updateUserName) {
			this.updateUserName = updateUserName;
		}
		public String getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}
		public long getOrderId() {
			return orderId;
		}
		public void setOrderId(long orderId) {
			this.orderId = orderId;
		}
	}
	public OrderPayInfo getOrderPayInfo() {
		return orderPayInfo;
	}
	public void setOrderPayInfo(OrderPayInfo orderPayInfo) {
		this.orderPayInfo = orderPayInfo;
	}
}