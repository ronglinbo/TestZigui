package com.wcyc.zigui2.newapp.module.notice;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PublishNotifyReq implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -1106049961883302068L;


	//新增天元同步字段
	private  String syncFlag; //0 同步 1不同步 2同步失败

	private String userId;
	private String schoolId;

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	private String noticeType;

	private List<String> classIdList;

	private List<String> teacherGroupIdList;

	private List<String> departMentIdList;

	private List<String> gradeIdList;

	private List<String> commonGroupList;


	public Map<String, List<String>> getSubordinateGroupMap() {
		return subordinateGroupMap;
	}

	public void setSubordinateGroupMap(Map<String, List<String>> subordinateGroupMap) {
		this.subordinateGroupMap = subordinateGroupMap;
	}

	private Map<String,List<String>> subordinateGroupMap;

	private Map<Long,String> userIdTypeMap;
//	private UserIdTypeMap userIdTypeMap;
//	public class UserIdTypeMap{
//		private long userId;
//		private String type;
//		public long getUserId() {
//			return userId;
//		}
//		public void setUserId(long userId) {
//			this.userId = userId;
//		}
//		public String getType() {
//			return type;
//		}
//		public void setType(String type) {
//			this.type = type;
//		}
//	}

	private List<String> attachmentIdList;


	private String msgText;
	private String msgTitle;
	private String picNum;
	private String receiveReport;
	private String isSmsRemind;
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

//	public UserIdTypeMap getUserIdTypeMap() {
//		return userIdTypeMap;
//	}
//	public void setUserIdTypeMap(UserIdTypeMap userIdTypeMap) {
//		this.userIdTypeMap = userIdTypeMap;
//	}

	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public String getPicNum() {
		return picNum;
	}
	public void setPicNum(String picNum) {
		this.picNum = picNum;
	}
	public String getReceiveReport() {
		return receiveReport;
	}
	public void setReceiveReport(String receiveReport) {
		this.receiveReport = receiveReport;
	}
	public String getIsSmsRemind() {
		return isSmsRemind;
	}
	public void setIsSmsRemind(String isSmsRemind) {
		this.isSmsRemind = isSmsRemind;
	}
	public List<String> getClassIdList() {
		return classIdList;
	}
	public void setClassIdList(List<String> classIdList) {
		this.classIdList = classIdList;
	}
	public List<String> getTeacherGroupIdList() {
		return teacherGroupIdList;
	}
	public void setTeacherGroupIdList(List<String> teacherGroupIdList) {
		this.teacherGroupIdList = teacherGroupIdList;
	}
	public List<String> getDepartMentIdList() {
		return departMentIdList;
	}
	public void setDepartMentIdList(List<String> departMentIdList) {
		this.departMentIdList = departMentIdList;
	}
	public List<String> getGradeIdList() {
		return gradeIdList;
	}
	public void setGradeIdList(List<String> gradeIdList) {
		this.gradeIdList = gradeIdList;
	}
	public List<String> getCommonGroupList() {
		return commonGroupList;
	}
	public void setCommonGroupList(List<String> commonGroupList) {
		this.commonGroupList = commonGroupList;
	}
	public List<String> getAttachmentIdList() {
		return attachmentIdList;
	}
	public void setAttachmentIdList(List<String> attachmentIdList) {
		this.attachmentIdList = attachmentIdList;
	}
	public Map<Long,String> getUserIdTypeMap() {
		return userIdTypeMap;
	}
	public void setUserIdTypeMap(Map<Long,String> userIdTypeMap) {
		this.userIdTypeMap = userIdTypeMap;
	}
	public String getSyncFlag() {
		return syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}


}