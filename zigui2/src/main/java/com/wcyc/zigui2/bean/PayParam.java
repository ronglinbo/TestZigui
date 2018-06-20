/*
* 文 件 名:PayParam.java
* 创 建 人： 姜韵雯
* 日    期： 2014-12-13
* 版 本 号： 1.10
*/
package com.wcyc.zigui2.bean;

/**
 * 支付参数实体类
 * 
 * @author 姜韵雯
 * @version 1.10
 * @since 1.10
 */
public class PayParam extends BaseBean {
	private static final long serialVersionUID = -452663899866195430L;
	private String payParam;

	public String getPayParam() {
		return payParam;
	}

	public void setPayParam(String payParam) {
		this.payParam = payParam;
	}
}
