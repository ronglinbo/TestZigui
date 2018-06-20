package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;

public class GradeleaderBean implements Serializable{

	private String id;
	private String gradeId;
	private String gradeName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	
	
	
	@Override
	public String toString() {
		return "GradeleaderBean [id=" + id + ", gradeId=" + gradeId
				+ ", gradeName=" + gradeName + "]";
	}
	
	
}
