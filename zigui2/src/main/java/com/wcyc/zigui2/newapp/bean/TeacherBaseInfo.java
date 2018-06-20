package com.wcyc.zigui2.newapp.bean;


/**
 * 工资条老师基本信息
 * @author 郑国栋
 * 2016-7-13
 * @version 2.0
 */
public class TeacherBaseInfo {
	private String employeeNo;
	private String id;
	private String name;
	
	
	
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public String toString() {
		return "TeacherBaseInfo [employeeNo=" + employeeNo + ", id=" + id
				+ ", name=" + name + "]";
	}
	
	
	
}
