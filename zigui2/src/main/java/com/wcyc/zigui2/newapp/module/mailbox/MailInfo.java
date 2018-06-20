package com.wcyc.zigui2.newapp.module.mailbox;

import com.wcyc.zigui2.bean.BaseBean;
//校长信箱实体类
public class MailInfo extends BaseBean{
	private String mailId;
	private String mailTile;
	private String mailContent;
	private String type;
	private String typeName;
	private String publishTime;
	private String isReply;
	private String isRead;
	private String mailSender;
	private String mailSenderName;
	private String additionNum;
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getMailTile() {
		return mailTile;
	}
	public void setMailTile(String mailTile) {
		this.mailTile = mailTile;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getIsReply() {
		return isReply;
	}
	public void setIsReply(String isReply) {
		this.isReply = isReply;
	}
	public String getMailSender() {
		return mailSender;
	}
	public void setMailSender(String mailSender) {
		this.mailSender = mailSender;
	}
	public String getMailSenderName() {
		return mailSenderName;
	}
	public void setMailSenderName(String mailSenderName) {
		this.mailSenderName = mailSenderName;
	}
	public String getAdditionNum() {
		return additionNum;
	}
	public void setAdditionNum(String additionNum) {
		this.additionNum = additionNum;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
}
