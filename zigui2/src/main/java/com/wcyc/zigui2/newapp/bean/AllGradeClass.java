package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

public class AllGradeClass extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5955773695259821256L;
	/**
	 * 
	 */

	private List<GradeMap> gradeMapList;


	public List<GradeMap> getGradeMapList() {
		return gradeMapList;
	}
	public void setGradeMapList(List<GradeMap> gradeMapList) {
		this.gradeMapList = gradeMapList;
	}
}