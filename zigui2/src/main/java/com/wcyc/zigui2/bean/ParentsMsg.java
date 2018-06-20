/**   
 * 文件名：com.example.zigui.bean.Stu.java   
 *   
 * 版本信息：   
 * 日期：2014年9月23日 下午3:02:51  
 * Copyright 惟楚有材 Corporation 2014    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 此类描述的是:家长我的消息列表
 * 
 *
 *Msglist	List	消息列表
 *  
  MsgID	String	消息id
  Msgdate	String	消息日期
  Msg	String	消息
  Msgtype	String	消息类型（系统消息1 其他消息2）
  state String   通知状态  1  新通知， 2  审核通过，3  审核驳回
  String typeID;  详情消息ID
 */

public class ParentsMsg implements Serializable {
	private static final long serialVersionUID = 6236583454538563724L;
	
	private String msgID;
	private String msgTitle;
	
	private String msgDate;
	private String msgText;
	private String msgType;
	private String studentName;
	private String msgState;
	
	private List<PictureURL> msgPicList;
	private String typeID;
	private String read;  //读取状态 0：未读  1：已读
	
	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	/**   
	 * typeID   
	 *   
	 * @return  the typeID   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getTypeID() {
		return typeID;
	}

	/**   
	 * @param typeID the typeID to set   
	 */
	
	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	/**   
	 * msgState   
	 * @return  the msgState   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getMsgState() {
		return msgState;
	}

	/**   
	 * @param msgState the msgState to set   
	 */
	
	public void setMsgState(String msgState) {
		this.msgState = msgState;
	}

	/**   
	 * studentName   
	 *   
	 * @return  the studentName   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getStudentName() {
		return studentName;
	}

	/**   
	 * @param studentName the studentName to set   
	 */
	
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public List<PictureURL> getMsgPicList()
	{
		return msgPicList;
	}
	
	public void setMsgPicList(List<PictureURL> msgPicList)
	{
		this.msgPicList = msgPicList;
	}
	
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	
	public String getMsgID() {
		return msgID;
	}
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}
	
	
	public String getMsgDate() {
		return msgDate;
	}
	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}
	
	
	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	
}
