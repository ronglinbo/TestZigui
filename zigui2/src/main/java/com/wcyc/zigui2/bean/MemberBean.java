package com.wcyc.zigui2.bean;

import java.util.List;


public class MemberBean extends BaseBean {
	private static final long serialVersionUID = -9077125918332595881L;
	
	private int userType;
	private String userID;
	private String userName;
	private String teacherClassID;
	private int childNum;
	private List<Child> childList;
	private List<Classes> classList;
	private List<Classes> contactClassList;
	private List<Rights> rights;

	private String updateFlag;
	
	private String hxUseruame;
	private String hxPassword;
	private String sessionid;
	private String userIconURL;
	
	private String course;
	
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	
	public String getUserIconURL() {
		return userIconURL;
	}
	public void setUserIconURL(String userIconURL) {
		this.userIconURL = userIconURL;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getHxUseruame() {
		return hxUseruame;
	}
	public void setHxUseruame(String hxUseruame) {
		this.hxUseruame = hxUseruame;
	}
	public String getHxPassword() {
		return hxPassword;
	}
	public void setHxPassword(String hxPassword) {
		this.hxPassword = hxPassword;
	}
	public String getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}
	
	
	public String getTeacherClassID() {
		return teacherClassID;
	}
	public void setTeacherClassID(String teacherClassID) {
		this.teacherClassID = teacherClassID;
	}
	
	
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getChildNum() {
		return childNum;
	}
	public void setChildNum(int childNum) {
		this.childNum = childNum;
	}
	public List<Child> getChildList() {
		return childList;
	}
	public void setChildList(List<Child> childList) {
		this.childList = childList;
	}
	public List<Classes> getClassList() {
		return classList;
	}
	public void setClassList(List<Classes> classList) {
		this.classList = classList;
	}
	
	
	public List<Rights> getRights() {
		return rights;
	}
	public void setRights(List<Rights> rights) {
		this.rights = rights;
	}
	public List<Classes> getContactClassList() {
		return contactClassList;
	}
	public void setContactClassList(List<Classes> contactClassList) {
		this.contactClassList = contactClassList;
	}
	
}
