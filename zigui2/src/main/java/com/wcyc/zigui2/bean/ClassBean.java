
package com.wcyc.zigui2.bean;

import java.io.Serializable;

public class ClassBean implements Serializable {
	
	private static final long serialVersionUID = 3789631766598526280L;
	private String  isAdviser;
	private String classID;
	private String className;
	private String gradeName;
	public String getClassID() {
		return classID;
	}
	public void setClassID(String classID) {
		this.classID = classID;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getIsAdviser() {
		return isAdviser;
	}
	public void setIsAdviser(String isAdviser) {
		this.isAdviser = isAdviser;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	
}
