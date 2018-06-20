package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

public class GradeMap extends NewBaseBean{


	/**
	 * 
	 */
	private static final long serialVersionUID = 855814507270498787L;
	private int id;//年級ID
	private String name;
	private String gradeCode;
	private String schoolStage;
	private List<ClassMap> classMapList;
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
	public List<ClassMap> getClassMapList() {
		return classMapList;
	}
	public void setClassMapList(List<ClassMap> classMapList) {
		this.classMapList = classMapList;
	}
	
}