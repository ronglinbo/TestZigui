package com.wcyc.zigui2.newapp.module.charge2;

import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.SchoolProducts;

public class ProductInfo extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2963787568279139900L;
	private List<ProductTime> freeProductList;	//学校或班级开通的产品---时长信息

	public List<ProductTime> getFreeProductList() {
		return freeProductList;
	}

	public void setFreeProductList(List<ProductTime> freeProductList) {
		this.freeProductList = freeProductList;
	}

	public class ProductTime {

		private Long actualAmount;        //Long	实际金额
		private Long amount	; //Long	产品原价
		private Long discount;	//BigDecimal	折扣，默认为10
		private int  id;	//integer	在其他接口中使用的---productId
		private String parentProductCode;	//字符串 ”01”,”0102”这样的	父产品代码
		private String productCode;	//字符串 ”01”,”0102”这样的	产品代码
		private String productName;	//字符串	产品名称
		private String productType;	//字符串	产品类别
		private int  schoolId;	//integer	学校id
		private int  state;	//integer	状态 0：无效 1：有效（默认
		private String validityDateType;	//字符串	一份产品有效期（值），与VALIDITY_DATE_TYPE合起来就是有效期
		private String validityDateValue;	//字符串 	产品有效期（单位）：年、月、天、周

		public String getValidityDateValue() {
			return validityDateValue;
		}

		public void setValidityDateValue(String validityDateValue) {
			this.validityDateValue = validityDateValue;
		}

		public Long getActualAmount() {
			return actualAmount;
		}

		public void setActualAmount(Long actualAmount) {
			this.actualAmount = actualAmount;
		}

		public Long getAmount() {
			return amount;
		}

		public void setAmount(Long amount) {
			this.amount = amount;
		}

		public Long getDiscount() {
			return discount;
		}

		public void setDiscount(Long discount) {
			this.discount = discount;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getParentProductCode() {
			return parentProductCode;
		}

		public void setParentProductCode(String parentProductCode) {
			this.parentProductCode = parentProductCode;
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

		public int getSchoolId() {
			return schoolId;
		}

		public void setSchoolId(int schoolId) {
			this.schoolId = schoolId;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

		public String getValidityDateType() {
			return validityDateType;
		}

		public void setValidityDateType(String validityDateType) {
			this.validityDateType = validityDateType;
		}
	}

}