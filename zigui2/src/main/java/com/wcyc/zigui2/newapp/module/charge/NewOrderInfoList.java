package com.wcyc.zigui2.newapp.module.charge;

import java.io.Serializable;
import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class NewOrderInfoList extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6192035874040228929L;
	private List<OrderInfo> orderList;
	public class OrderInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2884909117837644088L;
		private long id;
		private String channel;
		private long couponAmount;
		private String couponType;
		private String createTime;
		private String currency;
		private long fullAmount;
		private long orderAmount;
		private String orderDate;
		private String orderNo;
		private String orderType;
		private long payId;
		private int status;
		private long studentId;
		private long userId;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
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
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public long getFullAmount() {
			return fullAmount;
		}
		public void setFullAmount(long fullAmount) {
			this.fullAmount = fullAmount;
		}
		public long getOrderAmount() {
			return orderAmount;
		}
		public void setOrderAmount(long orderAmount) {
			this.orderAmount = orderAmount;
		}
		public String getOrderDate() {
			return orderDate;
		}
		public void setOrderDate(String orderDate) {
			this.orderDate = orderDate;
		}
		public String getOrderNo() {
			return orderNo;
		}
		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}
		public String getOrderType() {
			return orderType;
		}
		public void setOrderType(String orderType) {
			this.orderType = orderType;
		}
		public long getPayId() {
			return payId;
		}
		public void setPayId(long payId) {
			this.payId = payId;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
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
	}
	public List<OrderInfo> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<OrderInfo> orderList) {
		this.orderList = orderList;
	}
}