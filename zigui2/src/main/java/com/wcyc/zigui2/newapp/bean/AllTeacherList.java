package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

public class AllTeacherList extends NewBaseBean implements  Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 4312120240830809146L;

	private List<TeacherMap> teacherMapList;

	public class TeacherMap implements Serializable{
		private int id;//用户id
		private String employeeNo;
		private String name;
		private int accId;//账户id
		private String header;//首字母
		private String mobile;
		private String picAddress;///downloadApi?fileId=1291
		private String title;//职能标签
		private String department_name; //教师所在部门

		//特殊字符排序.
		public String getSortLetters() {
			if(sortLetters==null){
				sortLetters = "#";
			}
			return sortLetters;
		}

		public void setSortLetters(String sortLetters) {
			this.sortLetters = (sortLetters.matches("[A-Z]")?sortLetters:"#");
		}

		private String sortLetters;

		public String getDepartment_name() {
			return department_name;
		}

		public void setDepartment_name(String department_name) {
			this.department_name = department_name;
		}

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getEmployeeNo() {
			return employeeNo;
		}
		public void setEmployeeNo(String employeeNo) {
			this.employeeNo = employeeNo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAccId() {
			return accId;
		}
		public void setAccId(int accId) {
			this.accId = accId;
		}
		public String getHeader() {
			return header;
		}
		public void setHeader(String header) {
			this.header = header;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getPicAddress() {
			return picAddress;
		}
		public void setPicAddress(String picAddress) {
			this.picAddress = picAddress;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
	}

	public List<TeacherMap> getTeacherMapList() {
		return teacherMapList;
	}

	public void setTeacherMapList(List<TeacherMap> teacherMapList) {
		this.teacherMapList = teacherMapList;
	}

}