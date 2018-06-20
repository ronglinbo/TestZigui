package com.wcyc.zigui2.newapp.module.consume;
/**
 * 消费记录bean
 *  
 * @author 郑国栋
 * 2016-6-28
 * @version 2.0
 */
/**
 * @author 郑国栋
 * 2016-7-27
 * @version 2.0
 */
public class NewConsumeRecoadBean {

	private String consumeNumber;//消费金额
	private String updatetime;//消费时间
	private String consumeType;//消费地点  地址
	public String getConsumeNumber() {
		return consumeNumber;
	}
	public void setConsumeNumber(String consumeNumber) {
		this.consumeNumber = consumeNumber;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getConsumeType() {
		return consumeType;
	}
	public void setConsumeType(String consumeType) {
		this.consumeType = consumeType;
	}

	@Override
	public String toString() {
		return "NewConsumeRecoadBean [consumeNumber=" + consumeNumber
				+ ", updatetime=" + updatetime + ", consumeType=" + consumeType
				+ "]";
	}
}
