package com.wcyc.zigui2.newapp.bean;

import java.util.List;



public class NewPublishBean  {
	
	private String classId;
	private String gradeId;
	private String userId;
	private String content;
	private String picNum;
	private List<String> attachmentIdList;
	private String userType;
	private String childId;
	private String relation;
	private String vedioPic;
	
		
	
	
	@Override
	public String toString() {
		return "NewPublishBean [classId=" + classId + ", gradeId=" + gradeId
				+ ", userId=" + userId + ", content=" + content + ", picNum="
				+ picNum + ", attachmentIdList=" + attachmentIdList
				+ ", userType=" + userType + ", childId=" + childId
				+ ", relation=" + relation + "]";
	}

	public String getVedioPic() {
		return vedioPic;
	}
	public void setVedioPic(String vedioPic) {
		this.vedioPic = vedioPic;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getPicNum() {
		return picNum;
	}
	public void setPicNum(String picNum) {
		this.picNum = picNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getAttachmentIdList() {
		return attachmentIdList;
	}
	public void setAttachmentIdList(List<String> attachmentIdList) {
		this.attachmentIdList = attachmentIdList;
	}
	
	
	
}
