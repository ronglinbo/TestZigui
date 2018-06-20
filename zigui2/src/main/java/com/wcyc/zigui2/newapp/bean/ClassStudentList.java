package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

public class ClassStudentList extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4202852838211211253L;
	private List<ClassStudent> classStudent;
	public List<ClassStudent> getClassStudent() {
		return classStudent;
	}
	public void setClassStudent(List<ClassStudent> classStudent) {
		this.classStudent = classStudent;
	}
	public void addClassStudent(List<ClassStudent> classStudent){
		if(this.classStudent != null){
			this.classStudent.addAll(classStudent);
		}else{
			this.classStudent = classStudent;
		}
	}
	
}