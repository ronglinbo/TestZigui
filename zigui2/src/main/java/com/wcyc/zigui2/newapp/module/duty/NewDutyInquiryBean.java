package com.wcyc.zigui2.newapp.module.duty;

/**
 * 值班查询bean
 *  
 * @author 郑国栋
 * 2016-6-27
 * @version 2.0
 */
public class NewDutyInquiryBean {

	
	
	private String curWeekNo;//当前周
	private String dutyUserId;//值班人id
	private String id;//值班查询id
	private String mobile;//手机
	private String mode;//值班模式   逐日   还是 逐周   还是逐月
	private String objectVal;//
	
	private String operatorTime;//值班时间
	private String schoolId;//学校id
	private String status;//
	private String teacherCode;//部门id
	private String userName;//值班人名称
	private String weekDay;//
	private String identity;//值班人身份




	public String getCurWeekNo() {
		return curWeekNo;
	}
	public void setCurWeekNo(String curWeekNo) {
		this.curWeekNo = curWeekNo;
	}
	public String getDutyUserId() {
		return dutyUserId;
	}
	public void setDutyUserId(String dutyUserId) {
		this.dutyUserId = dutyUserId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getObjectVal() {
		return objectVal;
	}
	public void setObjectVal(String objectVal) {
		this.objectVal = objectVal;
	}
	public String getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(String operatorTime) {
		this.operatorTime = operatorTime;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTeacherCode() {
		return teacherCode;
	}
	public void setTeacherCode(String teacherCode) {
		this.teacherCode = teacherCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	
	
	
	@Override
	public String toString() {
		return "NewDutyInquiryBean [curWeekNo=" + curWeekNo + ", dutyUserId="
				+ dutyUserId + ", id=" + id + ", mobile=" + mobile + ", mode="
				+ mode + ", objectVal=" + objectVal + ", operatorTime="
				+ operatorTime + ", schoolId=" + schoolId + ", status="
				+ status + ", teacherCode=" + teacherCode + ", userName="
				+ userName + ", weekDay=" + weekDay + ", identity=" + identity +"]";
	}


	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
}
