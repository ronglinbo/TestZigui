package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 发布点评封装业务类
 * 
 * @author 郑国栋
 * 2016-4-18
 * @version 2.0
 */
public class NewPublishStuentCommentBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8128668218583845403L;
	
	private String classId;
	private List<String>studentIdList;
	private String picId;
	private String num;
	private String content;
	private String userId;
	private String userName;
	
	
	public NewPublishStuentCommentBean() {
		super();
	}

	

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

	public List<String> getStudentIdList() {
		return studentIdList;
	}

	public void setStudentIdList(List<String> studentIdList) {
		this.studentIdList = studentIdList;
	}

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
	public String toString() {
		return "NewPublishStuentCommentBean [classId=" + classId
				+ ", studentIdList=" + studentIdList + ", picId=" + picId
				+ ", num=" + num + ", content=" + content + ", userId="
				+ userId + ", userName=" + userName + "]";
	}

	
	
	
	
	
	

}
