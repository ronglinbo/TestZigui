package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 意见反馈 业务类
 * 
 * @author 郑国栋
 * 2016-5-3
 * @version 2.0
 */

public class NewOpinionBean  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5994332591984213805L;
	
	
	private String userId;
	private String userName;
	private String classId;
	private String feedbackContact;
	private String userType;
	private String questionType;
	private String questionDesc;
	private String questionRate;
	private List attachementList;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getFeedbackContact() {
		return feedbackContact;
	}
	public void setFeedbackContact(String feedbackContact) {
		this.feedbackContact = feedbackContact;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getQuestionDesc() {
		return questionDesc;
	}
	public void setQuestionDesc(String questionDesc) {
		this.questionDesc = questionDesc;
	}
	public String getQuestionRate() {
		return questionRate;
	}
	public void setQuestionRate(String questionRate) {
		this.questionRate = questionRate;
	}
	public List getAttachementList() {
		return attachementList;
	}
	public void setAttachementList(List attachementList) {
		this.attachementList = attachementList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "NewOpinionBean{" +
				"userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", classId='" + classId + '\'' +
				", feedbackContact='" + feedbackContact + '\'' +
				", userType='" + userType + '\'' +
				", questionType='" + questionType + '\'' +
				", questionDesc='" + questionDesc + '\'' +
				", questionRate='" + questionRate + '\'' +
				", attachementList=" + attachementList +
				'}';
	}
}
