/*
* 文 件 名:PayParam.java
* 创 建 人： 姜韵雯
* 日    期： 2014-12-13
* 版 本 号： 1.10
*/
package com.wcyc.zigui2.bean;

/**
 * 微信预订单应答实体类
 * 
 * @author 谭园园
 * @version 1.15
 * @since 1.15
 */
public class WxPayParam extends BaseBean {
	
	private String appid;
	//随机字符串
	private String noncestr;
	//固定值
	private String packagevalue;
	//微信支付分配的商户号
	private String partnerid;
	//时间戳
	private String timestamp;
	//签名
	private String sign;
	//微信返回的支付交易会话ID
	private String prepayid;
	
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getPackagevalue() {
		return packagevalue;
	}
	public void setPackagevalue(String packagevalue) {
		this.packagevalue = packagevalue;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "WxPayParam [appid=" + appid + ", noncestr=" + noncestr
				+ ", packagevalue=" + packagevalue + ", partnerid=" + partnerid
				+ ", timestamp=" + timestamp + ", sign=" + sign + ", prepayid="
				+ prepayid + "]";
	}
	public WxPayParam(String appid, String noncestr, String packagevalue,
			String partnerid, String timestamp, String sign, String prepayid) {
		super();
		this.appid = appid;
		this.noncestr = noncestr;
		this.packagevalue = packagevalue;
		this.partnerid = partnerid;
		this.timestamp = timestamp;
		this.sign = sign;
		this.prepayid = prepayid;
	}
	public WxPayParam() {
		super();
	}

	
}
