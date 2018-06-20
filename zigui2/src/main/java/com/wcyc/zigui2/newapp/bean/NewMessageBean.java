package com.wcyc.zigui2.newapp.bean;

import java.util.List;

public class NewMessageBean extends NewBaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3232737220498749319L;
	/**
	 * 
	 */
	private String messageContent;
	private String messageTime;
	private String messageId;
	private String messageType;
	private String messageTypeName;
	private String count;
	private HXUser hxUser;
	public class HXUser{
		private String userName;//环信用户名
		private String nickName;//环信昵称
		private String iconUrl;//头像
		private String cellPhone;//手机号码
		private String title;//职位
		private int chatType;//单聊还是群聊
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public String getIconUrl() {
			return iconUrl;
		}
		public void setIconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
		}
		public String getCellPhone() {
			return cellPhone;
		}
		public void setCellPhone(String cellPhone) {
			this.cellPhone = cellPhone;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public int getChatType() {
			return chatType;
		}
		public void setChatType(int chatType) {
			this.chatType = chatType;
		}
	}
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
	//设置业务办理类型
	public void setBussinessMessgeType(String messageType){
		this.messageType += messageType;
		this.messageType += ",";
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getMessageTypeName() {
		return messageTypeName;
	}
	public void setMessageTypeName(String messageTypeName) {
		this.messageTypeName = messageTypeName;
	}

	public HXUser getHxUser() {
		return hxUser;
	}
	public void setHxUser(HXUser hxUser) {
		this.hxUser = hxUser;
	}
	
}