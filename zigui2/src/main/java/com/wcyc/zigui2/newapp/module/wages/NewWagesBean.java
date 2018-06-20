package com.wcyc.zigui2.newapp.module.wages;

import java.util.List;

import com.wcyc.zigui2.newapp.bean.TeacherBaseInfo;

/**
 * 工资条 bean
 * 
 * @author 郑国栋
 * 2016-6-27
 * @version 2.0
 */
public class NewWagesBean {

	private String id;//工资id
	private Boolean isRead;
	private String wageRecordName;//工资发放名称
	private String publishId;//发布者id
	private String publishTime;//工资发布时间
	private TeacherBaseInfo teacherBaseInfo;
	
//	private String wagesTime;//工资年月
//	private String publishPersonName;//发布人名字
	private String month;//工资月份
	private String operatorId;//发布者id
	private String operatorTime;//工资年月
	private String year;//工资年
	private String pages;//总页数

	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	


	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	public String getWageRecordName() {
		return wageRecordName;
	}
	public void setWageRecordName(String wageRecordName) {
		this.wageRecordName = wageRecordName;
	}
	public String getPublishId() {
		return publishId;
	}
	public void setPublishId(String publishId) {
		this.publishId = publishId;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public TeacherBaseInfo getTeacherBaseInfo() {
		return teacherBaseInfo;
	}
	public void setTeacherBaseInfo(TeacherBaseInfo teacherBaseInfo) {
		this.teacherBaseInfo = teacherBaseInfo;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(String operatorTime) {
		this.operatorTime = operatorTime;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	
	
	@Override
	public String toString() {
		return "NewWagesBean [id=" + id + ", isRead=" + isRead
				+ ", wageRecordName=" + wageRecordName + ", publishId="
				+ publishId + ", publishTime=" + publishTime
				+ ", teacherBaseInfo=" + teacherBaseInfo + ", month=" + month
				+ ", operatorId=" + operatorId + ", operatorTime="
				+ operatorTime + ", year=" + year + ", pages=" + pages + "]";
	}

	
	
	
	
	
	
	
	
	
	
}
