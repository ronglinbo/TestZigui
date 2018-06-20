package com.wcyc.zigui2.newapp.bean;

import java.util.List;

public class ChargeProduct extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7030599186806543845L;
	
	private List<Product> productList;
	public class Product extends NewBaseBean{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8113779702997354424L;
		private long id;
		private long actualAmount;
		private long amount;
		private float discount;
		private String productCode;
		private String productName;
		private String productType;
		private long returnFee;
		private long returnFeeProportion;
		private String validityDateType;
		private String validityDateValue;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public long getActualAmount() {
			return actualAmount;
		}
		public void setActualAmount(long actualAmount) {
			this.actualAmount = actualAmount;
		}
		public long getAmount() {
			return amount;
		}
		public void setAmount(long amount) {
			this.amount = amount;
		}
		public float getDiscount() {
			return discount;
		}
		public void setDiscount(long discount) {
			this.discount = discount;
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
		public String getProductType() {
			return productType;
		}
		public void setProductType(String productType) {
			this.productType = productType;
		}
		public long getReturnFee() {
			return returnFee;
		}
		public void setReturnFee(long returnFee) {
			this.returnFee = returnFee;
		}
		public long getReturnFeeProportion() {
			return returnFeeProportion;
		}
		public void setReturnFeeProportion(long returnFeeProportion) {
			this.returnFeeProportion = returnFeeProportion;
		}
		public String getValidityDateType() {
			return validityDateType;
		}
		public void setValidityDateType(String validityDateType) {
			this.validityDateType = validityDateType;
		}
		public String getValidityDateValue() {
			return validityDateValue;
		}
		public void setValidityDateValue(String validityDateValue) {
			this.validityDateValue = validityDateValue;
		}
	}
	public List<Product> getProductList() {
		return productList;
	}
	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
}