package com.wcyc.zigui2.newapp.module.summary;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PublishSummaryReq implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5775342258823376481L;
	/**
	 * 
	 */
	
	private String userId;
	private String departId;
	private String departName;
	private String schoolId;
	private String allTeacher;//“1”全体教职工
	private List<String> classIdList;
	
	private List<String> teacherGroupIdList;

	private  Map<String,List<String>> subordinateGroupMap;
	
	private List<String> departMentIdList;
	
	private List<String> gradeIdList;
	
	private List<String> commonGroupList;
	
	private List<String> teacherList;
	
	private Map<Long,String> userIdTypeMap;

	private List<String> attachmentIdList;
	private String summaryText;
	private String summaryTitle;
	private String picNum;
	private String summaryType;
	private String summaryTypeCode;//总结类型编号
	
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
	public String getPicNum() {
		return picNum;
	}
	public void setPicNum(String picNum) {
		this.picNum = picNum;
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
	
	public String getDepartId() {
		return departId;
	}
	public void setDepartId(String departId) {
		this.departId = departId;
	}
	public String getSummaryType() {
		return summaryType;
	}
	public void setSummaryType(String summaryType) {
		this.summaryType = summaryType;
	}
	public String getSummaryText() {
		return summaryText;
	}
	public void setSummaryText(String summaryText) {
		this.summaryText = summaryText;
	}
	public String getSummaryTitle() {
		return summaryTitle;
	}
	public void setSummaryTitle(String summaryTitle) {
		this.summaryTitle = summaryTitle;
	}
	public String getSummaryTypeCode() {
		return summaryTypeCode;
	}
	public void setSummaryTypeCode(String summaryTypeCode) {
		this.summaryTypeCode = summaryTypeCode;
	}
	public String getAllTeacher() {
		return allTeacher;
	}
	public void setAllTeacher(String allTeacher) {
		this.allTeacher = allTeacher;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public List<String> getTeacherList() {
		return teacherList;
	}
	public void setTeacherList(List<String> teacherList) {
		this.teacherList = teacherList;
	}
	public Map<String, List<String>> getSubordinateGroupMap() {
		return subordinateGroupMap;
	}

	public void setSubordinateGroupMap(Map<String, List<String>> subordinateGroupMap) {
		this.subordinateGroupMap = subordinateGroupMap;
	}
}