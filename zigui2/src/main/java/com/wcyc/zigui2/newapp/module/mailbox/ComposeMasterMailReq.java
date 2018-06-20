package com.wcyc.zigui2.newapp.module.mailbox;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ComposeMasterMailReq implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3622607762876672769L;
	/**
	 * 
	 */

	private String userId;
	private String userName;
	private String userType;
	private String schoolId;
	private String mailTitle;
	private String mailContent;
	private String mailType;
	private String typeName;
	private String isSms;
	private String attachmentNum;
	private List<String> attachementList;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
		
	public List<String> getAttachmentIdList() {
		return attachementList;
	}
	public void setAttachmentIdList(List<String> attachmentIdList) {
		this.attachementList = attachmentIdList;
	}
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	public String getMailType() {
		return mailType;
	}
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getIsSms() {
		return isSms;
	}
	public void setIsSms(String isSms) {
		this.isSms = isSms;
	}
	public String getAttachmentNum() {
		return attachmentNum;
	}
	public void setAttachmentNum(String attachmentNum) {
		this.attachmentNum = attachmentNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
}