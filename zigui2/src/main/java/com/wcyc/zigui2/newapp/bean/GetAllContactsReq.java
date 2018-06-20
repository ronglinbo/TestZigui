package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

public class GetAllContactsReq implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 109163855027901083L;

	private String userId;
	
	private List<String> classIdList;
	
	private String userType;
	
	private String schoolId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getClassIdList() {
		return classIdList;
	}

	public void setClassIdList(List<String> classIdList) {
		this.classIdList = classIdList;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}	
}