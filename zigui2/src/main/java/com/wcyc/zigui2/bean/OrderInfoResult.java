/*
* 文 件 名:RenewOrder.java
* 创 建 人： xfliu
* 日    期： 2014-12-12
* 版 本 号： 1.05
*/
package com.wcyc.zigui2.bean;

import java.util.List;

/**
 * @author xfliu
 * @version 1.05
 */

public class OrderInfoResult extends BaseBean{
	private static final long serialVersionUID = 6258581384538563721L;
	
//	private int orderList;
//	private String orderTime;
//	private String orderNumber;
//	private String money;
//	private String state;
//	private String orderID;
//	private String rechargeTime;
//	private String description;
//	private String productList;
//	private String name;
//	private Float price;
//	private String unit;
//	private String productID;
	/*
    orderTime	String	订单时间
    orderNo	String	订单编号
    money	String	总金额
    state	String	状态 0:未支付  1：支付成功
    orderID	String	订单ID
    rechargeTime	String	缴费时长
    description	String	描述
    products	String	续费项目
    */
	private String orderTime;
    private Float payMoney;
    private String state;
    private String orderID;
    private String rechargeTime;
    private String orderNo;
    private String description;
    private String products;
    private String startDate;
    private String endDate;
    private List<ProductResult> productList;
    private String card;
    
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public List<ProductResult> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductResult> productList) {
		this.productList = productList;
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
	public String isSuccessed(){
		if("1".equals(state)){
			return "1";
		}else if("2".equals(state)){
			return "2";
		}else {
			return "3";
		}
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public Float getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Float payMoney) {
		this.payMoney = payMoney;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getRechargeTime() {
		return rechargeTime;
	}
	public void setRechargeTime(String rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProducts() {
		return products;
	}
	public void setProducts(String products) {
		this.products = products;
	}
}
