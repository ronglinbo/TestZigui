package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

public class AllDeptList extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2288207977357176469L;

	/** 行政机构list */
	private List<DepMap> depMapList;
	/** 我的分组list */
	private List<ContactGroupMap> contactGroupMapList;
	/** 教学机构list */
	private List<GradeMap> gradeMapList;
	/** 常用分组list */
	private List<CommonGroup> commonList;
	public class DepMap implements Serializable {
		/** 部门id */
		private int id;
		/** 部门名称 */
		private String departmentName;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getDepartmentName() {
			return departmentName;
		}
		public void setDepartmentName(String departmentName) {
			this.departmentName = departmentName;
		}
	}
	public class ContactGroupMap implements Serializable{
		/** 群组id */
		private int id;
		/** 群组名称 */
		private String name;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	public class GradeMap implements Serializable{
		/** 年级id */
		private int id;
		/** 年级名称 */
		private String name;
		/** 年级代码 */
		private String gradeCode;
		/**  */
		private String schoolStage;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getGradeCode() {
			return gradeCode;
		}
		public void setGradeCode(String gradeCode) {
			this.gradeCode = gradeCode;
		}
		public String getSchoolStage() {
			return schoolStage;
		}
		public void setSchoolStage(String schoolStage) {
			this.schoolStage = schoolStage;
		}
	}
	public class CommonGroup implements Serializable{
		private int id;
		private String name;
		private String code;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
	}
	public List<DepMap> getDepMapList() {
		return depMapList;
	}
	public void setDepMapList(List<DepMap> depMapList) {
		this.depMapList = depMapList;
	}
	public List<ContactGroupMap> getContactGroupMapList() {
		return contactGroupMapList;
	}
	public void setContactGroupMapList(List<ContactGroupMap> contactGroupMapList) {
		this.contactGroupMapList = contactGroupMapList;
	}
	public List<GradeMap> getGradeMapList() {
		return gradeMapList;
	}
	public void setGradeMapList(List<GradeMap> gradeMapList) {
		this.gradeMapList = gradeMapList;
	}
	public List<CommonGroup> getCommonList() {
		return commonList;
	}
	public void setCommonList(List<CommonGroup> commonList) {
		this.commonList = commonList;
	}
}