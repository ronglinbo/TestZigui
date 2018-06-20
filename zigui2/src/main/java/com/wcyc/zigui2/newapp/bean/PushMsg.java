package com.wcyc.zigui2.newapp.bean;

import java.util.List;

public class PushMsg extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -382891641623606793L;
	private List<Message> messageList;
	public class Message{
		private String count;
		private String messageContent;
		private String messageId;
		private String messageTime;
		private String messageType;
		private String messageTypeName;
		public String getCount() {
			return count;
		}
		public void setCount(String count) {
			this.count = count;
		}
		public String getMessageContent() {
			return messageContent;
		}
		public void setMessageContent(String messageContent) {
			this.messageContent = messageContent;
		}
		public String getMessageId() {
			return messageId;
		}
		public void setMessageId(String messageId) {
			this.messageId = messageId;
		}
		public String getMessageTime() {
			return messageTime;
		}
		public void setMessageTime(String messageTime) {
			this.messageTime = messageTime;
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
	public List<Message> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}
}