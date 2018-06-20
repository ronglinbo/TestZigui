package com.wcyc.zigui2.newapp.module.charge2;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class NewCreateOrderResult extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1480984003027091232L;
	/**
	 * 
	 */
//	     "channel": "ANDROID",
//				 "couponAmount": 0,
//				 "createTime": "2017-07-12 09:13:41",
//				 "createUserId": 10238610,
//				 "createUserName": "",
//				 "currency": "CNY",
//				 "fullAmount": 54000,
//				 "id": 115334,
//				 "orderAmount": 48000,
//				 "orderDate": "2017-07-12 09:13:41",
//				 "orderModel": "1",
//				 "orderNo": "10020171115334",
//				 "orderType": "1",
//				 "payId": 114840,
//				 "payType": "1",
//				 "status": 1,
//				 "studentId": 163058,
//				 "userId": 10238610
	public SysOrder sysOrder;
	public class SysOrder{
		private long id;
		private long payId;
		private String orderNo;
		private String payType;
		private int status;
		private String orderModel;
		private long orderAmount;
		private long fullAmount;
		private long couponAmount;
		
		private String currency;
		private String orderType;
		private String channel;
		private String orderDate;
		private String originalOrderNo;
		private long originalOrderAmount;
		private long studentId;
		private long userId;
		private long createUserId;
		private String createUserName;
		private String createTime;
		private String updateUserId;
		private String updateUserName;
		private String paymentInfo;

		public String getPayType() {
			return payType;
		}

		public void setPayType(String payType) {
			this.payType = payType;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getOrderModel() {
			return orderModel;
		}

		public void setOrderModel(String orderModel) {
			this.orderModel = orderModel;
		}

		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public long getPayId() {
			return payId;
		}
		public void setPayId(long payId) {
			this.payId = payId;
		}
		public String getOrderNo() {
			return orderNo;
		}
		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}
		public long getOrderAmount() {
			return orderAmount;
		}
		public void setOrderAmount(long orderAmount) {
			this.orderAmount = orderAmount;
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
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public String getOrderType() {
			return orderType;
		}
		public void setOrderType(String orderType) {
			this.orderType = orderType;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public String getOrderDate() {
			return orderDate;
		}
		public void setOrderDate(String orderDate) {
			this.orderDate = orderDate;
		}
		public String getOriginalOrderNo() {
			return originalOrderNo;
		}
		public void setOriginalOrderNo(String originalOrderNo) {
			this.originalOrderNo = originalOrderNo;
		}
		public long getOriginalOrderAmount() {
			return originalOrderAmount;
		}
		public void setOriginalOrderAmount(long originalOrderAmount) {
			this.originalOrderAmount = originalOrderAmount;
		}
		public long getStudentId() {
			return studentId;
		}
		public void setStudentId(long studentId) {
			this.studentId = studentId;
		}
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
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
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getUpdateUserId() {
			return updateUserId;
		}
		public void setUpdateUserId(String updateUserId) {
			this.updateUserId = updateUserId;
		}
		public String getUpdateUserName() {
			return updateUserName;
		}
		public void setUpdateUserName(String updateUserName) {
			this.updateUserName = updateUserName;
		}
		public String getPaymentInfo() {
			return paymentInfo;
		}
		public void setPaymentInfo(String paymentInfo) {
			this.paymentInfo = paymentInfo;
		}
	}
	public SysOrder getSysOrder() {
		return sysOrder;
	}
	public void setSysOrder(SysOrder sysOrder) {
		this.sysOrder = sysOrder;
	}
}