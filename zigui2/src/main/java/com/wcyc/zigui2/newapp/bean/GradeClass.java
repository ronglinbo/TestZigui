package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

public class GradeClass extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5955773695259821256L;
	/**
	 * 
	 */

	

	private List<ClassMap> classMapList;
	private List<GradeMap> gradeMapList;


	public List<GradeMap> getGradeMapList() {
		return gradeMapList;
	}
	public void setGradeMapList(List<GradeMap> gradeMapList) {
		this.gradeMapList = gradeMapList;
	}
	public List<ClassMap> getClassMapList() {
		return classMapList;
	}
	public void setClassMapList(List<ClassMap> classMapList) {
		this.classMapList = classMapList;
	}
}