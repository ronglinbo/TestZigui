package com.wcyc.zigui2.newapp.module.charge2;

import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class NewOrder extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9139124932315284677L;
	/**
	 * 
	 */

	private String userId;
	private String studentId;
	private long couponAmount;
	private String  couponType;
	private long fullAmount;
	private long orderAmount;
	private String paymentPlatformType;
	private List<OrderItem> orderItemList;
	public class OrderItem{
		private long amount;
		private long fullAmount;
		private long couponAmount;
		private String couponType;
		private String productCode;
		private String productName;
		private String startDate;
		private String endDate;
		private String goodsNo;
		public long getAmount() {
			return amount;
		}
		public void setAmount(long amount) {
			this.amount = amount;
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
		public String getProductCode() {
			return productCode;
		}
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getGoodsNo() {
			return goodsNo;
		}
		public void setGoodsNo(String goodsNo) {
			this.goodsNo = goodsNo;
		}
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
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
	public long getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(long orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getPaymentPlatformType() {
		return paymentPlatformType;
	}
	public void setPaymentPlatformType(String paymentPlatformType) {
		this.paymentPlatformType = paymentPlatformType;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
}