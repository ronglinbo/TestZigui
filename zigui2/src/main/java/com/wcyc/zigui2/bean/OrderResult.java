/*
 * 文 件 名:OrderResult.java
 * 创 建 人： 姜韵雯
 * 日    期： 2014-12-13
 * 版 本 号： 1.10
 */
package com.wcyc.zigui2.bean;

import java.util.List;


/**
 * 续费（选择套餐确认）实体类
 * 
 * @author 姜韵雯
 * @version 1.10
 * @since 1.10
 */
public class OrderResult extends BaseBean {
	private static final long serialVersionUID = 950621904348287265L;
	/*
	 * orderNumber String 订单编号 rechargeTime String 缴费时长 payMoney Float 付款金额
	 * description String 描述 productList String 续费项目
	 */
	private List<ProductResult> productList;
	private String orderNo;
	private String rechargeTime;
	private Float payMoney;
	private String description;
	private String card;

	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public void initByOrderInfoResult(OrderInfoResult mRenewOrder){
		orderNo = mRenewOrder.getOrderNo();
		rechargeTime = mRenewOrder.getRechargeTime();
		payMoney = mRenewOrder.getPayMoney();
		description = mRenewOrder.getDescription();
		productList = mRenewOrder.getProductList();
		card = mRenewOrder.getCard();
	}
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(String rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public Float getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Float payMoney) {
		this.payMoney = payMoney;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ProductResult> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductResult> productList) {
		this.productList = productList;
	}

}
