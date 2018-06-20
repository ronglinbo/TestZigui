package com.wcyc.zigui2.newapp.bean;

//关联消息信息
public class CorrelationMessageBean extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1668846133167596658L;
	
	private String messageContent;
	private String messageTime;
	private String messageId;
	private String messageType;
	private String messageTypeName;
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageTypeName() {
		return messageTypeName;
	}
	public void setMessageTypeName(String messageTypeName) {
		this.messageTypeName = messageTypeName;
	}
}