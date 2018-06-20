package com.wcyc.zigui2.bean;

import java.util.List;

/*
 * 
 * 家长我的消息
	
	入参
参数名	参数类型（长度）	描述
userID	String		用户ID
childID	String		孩子ID


		出参
参数名	参数类型	描述
code	Integer	返回代码
200 成功 201 请求失败
msgListNum	String	消息总数

msgList	List	消息列表
  msgID	String	消息id
  msgDate	String	消息日期
  msgText	String	消息
  msgType	String	消息类型（系统消息1 其他消息2）
		
		


		

 * 
 * 
 * */

public class ParentsMsgBean extends BaseBean {
	private static final long serialVersionUID = -9074125911382579981L;
	
	private int msgListNum;
	
	
	
	
	private List<ParentsMsg> msgList;
	
	//应用未读消息数
	private int unreadNum;

	
	public int getUnreadNum() {
		return unreadNum;
	}
	public void setUnreadNum(int unreadNum) {
		this.unreadNum = unreadNum;
	}
	public int getMsgListNum() {
		return msgListNum;
	}
	public void setMsgListNum(int msgListNum) {
		this.msgListNum = msgListNum;
	}
	
	public List<ParentsMsg> getMsgList() {
		return msgList;
	}
	
	
	public void setMsgList(List<ParentsMsg> msgList) {
		this.msgList = msgList;
	}
	
}
