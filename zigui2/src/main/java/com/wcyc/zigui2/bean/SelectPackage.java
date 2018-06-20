/*
 * 文 件 名:OrderResult.java
 * 创 建 人： 姜韵雯
 * 日    期： 2014-12-13
 * 版 本 号： 1.10
 */
package com.wcyc.zigui2.bean;

import java.util.List;


/**
 * 选择套餐 实体类
 * 
 * @author 谭园园
 * @version 1.15
 * @since 1.15
 */
public class SelectPackage{
//	private static final long serialVersionUID = 950621904348287265L;
	/*
	 * validityDateType String 月/年  productDescription String 套餐说明  amount Float 付款金额
	 *  productName String 套餐项目
	 */

	private String amount;
	private String memo;
	private String productDescription;
	private String productName;
	private String validityDateType;
	public SelectPackage(String amount, String memo, String productDescription,
			String productName, String validityDateType) {
		super();
		this.amount = amount;
		this.memo = memo;
		this.productDescription = productDescription;
		this.productName = productName;
		this.validityDateType = validityDateType;
	}
	public SelectPackage() {
		super();
	}
	@Override
	public String toString() {
		return "Entity [amount=" + amount + ", memo=" + memo
				+ ", productDescription=" + productDescription
				+ ", productName=" + productName + ", validityDateType="
				+ validityDateType + "]";
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getValidityDateType() {
		return validityDateType;
	}
	public void setValidityDateType(String validityDateType) {
		this.validityDateType = validityDateType;
	}
	
}
