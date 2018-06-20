package com.wcyc.zigui2.newapp.module.charge2;

import java.io.Serializable;
import java.util.List;


import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class NewOrderInfoList extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6192035874040228929L;
	private List<ChargeLogs> parentRechargeLogs;
	class ChargeLogs {
		private int order_amount;
		private String payment_platform_type;
		private int status;
		private String product_name;
		private String create_time;
		private String recharge_time;
		private String order_no;
		private int pageNum;
		private int totalPageNum;
		public int getOrderAmount() {
			return order_amount;
		}
		public void setOrderAmount(int order_amount) {
			this.order_amount = order_amount;
		}
		public String getPaymentPlatformType() {
			return payment_platform_type;
		}
		public void setPaymentPlatformType(String payment_platform_type) {
			this.payment_platform_type = payment_platform_type;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getProductName() {
			return product_name;
		}
		public void setProductName(String product_name) {
			this.product_name = product_name;
		}
		public String getCreateTime() {
			return create_time;
		}
		public void setCreateTime(String create_time) {
			this.create_time = create_time;
		}
		public String getRechargeTime() {
			return recharge_time;
		}
		public void setRechargeTime(String recharge_time) {
			this.recharge_time = recharge_time;
		}
		public String getOrderNo() {
			return order_no;
		}
		public void setOrderNo(String order_no) {
			this.order_no = order_no;
		}
		public int getPageNum() {
			return pageNum;
		}
		public void setPageNum(int pageNum) {
			this.pageNum = pageNum;
		}
		public int getTotalPageNum() {
			return totalPageNum;
		}
		public void setTotalPageNum(int totalPageNum) {
			this.totalPageNum = totalPageNum;
		}
	}
	public List<ChargeLogs> getParentRechargeLogs() {
		return parentRechargeLogs;
	}
	public void setParentRechargeLogs(List<ChargeLogs> parentRechargeLogs) {
		this.parentRechargeLogs = parentRechargeLogs;
	}
	
}