package com.wcyc.zigui2.newapp.module.charge;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class NewCreateOrderResult extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2234201521679454215L;
	private String orderId;
	private String orderNo;
	private String paymentInfo;
//	private PaymentInfo paymentInfo;
//	public class PaymentInfo{
//		private String sign;
//		private String timestamp;
//		private String partnerid;
//		private String prepayid;
//		private String appid;
//		private String nonce_str;
//		public String getSign() {
//			return sign;
//		}
//		public void setSign(String sign) {
//			this.sign = sign;
//		}
//		public String getTimestamp() {
//			return timestamp;
//		}
//		public void setTimestamp(String timestamp) {
//			this.timestamp = timestamp;
//		}
//		public String getPartnerid() {
//			return partnerid;
//		}
//		public void setPartnerid(String partnerid) {
//			this.partnerid = partnerid;
//		}
//		public String getPrepayid() {
//			return prepayid;
//		}
//		public void setPrepayid(String prepayid) {
//			this.prepayid = prepayid;
//		}
//		public String getAppid() {
//			return appid;
//		}
//		public void setAppid(String appid) {
//			this.appid = appid;
//		}
//		public String getNonce_str() {
//			return nonce_str;
//		}
//		public void setNonce_str(String nonce_str) {
//			this.nonce_str = nonce_str;
//		}
//	}
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