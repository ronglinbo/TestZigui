package com.wcyc.zigui2.newapp.module.mailbox;

import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class MasterReplyMailReq extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1357214158946175670L;
	private String userId;
	private String schoolId;
	private String mailId;
	private String replyContent;
	private String replyTitle;
	private String attchementNum;
	private String isSms;
	private List<String> attachmentList;
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
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getReplyTitle() {
		return replyTitle;
	}
	public void setReplyTitle(String replyTitle) {
		this.replyTitle = replyTitle;
	}
	public String getAttchementNum() {
		return attchementNum;
	}
	public void setAttchementNum(String attchementNum) {
		this.attchementNum = attchementNum;
	}
	public String getIsSms() {
		return isSms;
	}
	public void setIsSms(String isSms) {
		this.isSms = isSms;
	}
	public List<String> getAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<String> attachmentList) {
		this.attachmentList = attachmentList;
	}
}
