package com.wcyc.zigui2.newapp.module.leave;

import java.io.Serializable;

/**
 * 
 * @author 郑国栋
 * 2016-7-15
 * @version 2.0
 */
public class NewMyLeaveBean implements Serializable {

	private String id;//
	
	private String leaveType;//
	private String Status;//
	private String leaveStartTime;//
	private String leaveEndTime;//
	private String reason;//
	
	private String leaveDays;//
	private String leaveHours;//
	
	private String comments;
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLeaveDays() {
		return leaveDays;
	}
	public void setLeaveDays(String leaveDays) {
		this.leaveDays = leaveDays;
	}
	public String getLeaveHours() {
		return leaveHours;
	}
	public void setLeaveHours(String leaveHours) {
		this.leaveHours = leaveHours;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	
	
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getLeaveStartTime() {
		return leaveStartTime;
	}
	public void setLeaveStartTime(String leaveStartTime) {
		this.leaveStartTime = leaveStartTime;
	}
	public String getLeaveEndTime() {
		return leaveEndTime;
	}
	public void setLeaveEndTime(String leaveEndTime) {
		this.leaveEndTime = leaveEndTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	@Override
	public String toString() {
		return "NewMyLeaveBean [id=" + id + ", leaveType=" + leaveType
				+ ", Status=" + Status + ", leaveStartTime=" + leaveStartTime
				+ ", leaveEndTime=" + leaveEndTime + ", reason=" + reason
				+ ", leaveDays=" + leaveDays + ", leaveHours=" + leaveHours
				+ ", comments=" + comments + "]";
	}
	
	
	
	
	
	
	
}
