package com.wcyc.zigui2.newapp.module.studyresource;

import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class StudyResource extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -241531822607485404L;
	
	private String course;
	private List<Resource> studyResource;
	
	
	public List<Resource> getStudyResource() {
		return studyResource;
	}
	public void setStudyResource(List<Resource> studyResource) {
		this.studyResource = studyResource;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
}