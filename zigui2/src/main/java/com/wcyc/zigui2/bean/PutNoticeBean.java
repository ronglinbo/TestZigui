package com.wcyc.zigui2.bean;


/*
 * 通知和作业共用bean
 * 
		出参
		参数名	参数类型	描述
		code	Integer	返回代码
		200 成功 201 请求失败
		msgID	String	消息id
 * */

public class PutNoticeBean extends BaseBean {
	private static final long serialVersionUID = -9074125918982579981L;
	
	private String msgID;
	private String workID;
 
	public String getWorkID() {
		return workID;
	}
	
	
	public void setWorkID(String workID) {
		this.workID = workID;
	}
	 
	public String getMsgID() {
		return msgID;
	}
	
	
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}
}
