package com.wcyc.zigui2.newapp.module.charge;

import java.io.Serializable;
import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class NewOrderDetailList extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6192035874040228929L;
	private List<OrderDetail> orderDetail;
	public class OrderDetail implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2884909117837644088L;
		private long id;
		private long orderId;
		private String channel;
		private long amount;
		private long couponAmount;
		private String couponType;
		private String createDate;
		private String currency;
		private long fullAmount;
		private long goodsNo;
		private String endDate;
		private String startDate;
		private String productCode;
		private String productName;
		private int status;
		private long studentId;
		
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
		public long getOrderId() {
			return orderId;
		}
		public void setOrderId(long orderId) {
			this.orderId = orderId;
		}
		public long getAmount() {
			return amount;
		}
		public void setAmount(long amount) {
			this.amount = amount;
		}
		public String getCreateDate() {
			return createDate;
		}
		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}
		public long getGoodsNo() {
			return goodsNo;
		}
		public void setGoodsNo(long goodsNo) {
			this.goodsNo = goodsNo;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
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
		
	}
	public List<OrderDetail> getOrderDetail() {
		return orderDetail;
	}
	public void setOrderDetail(List<OrderDetail> orderDetail) {
		this.orderDetail = orderDetail;
	}
	
}