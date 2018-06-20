package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 发布作业业务逻辑  即封装作业信息的类
 * 主要解决json为引用类型时必须用封装方式
 * 
 * @author 郑国栋
 * 2016-4-15
 * @version 2.0
 */
public class NewPublishHomeworkBean implements Serializable{
	

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8158367800456033144L;
	
	private String classId;
	private String isAllClass;
	private String courseName;
	private List<NewCourseList> courseList;
	private List<String> attachmentIdList;
	public NewPublishHomeworkBean() {
		super();
	}
	public NewPublishHomeworkBean(String classId, String isAllClass,
			String courseName, List<NewCourseList> courseList,
			List<String> attachmentIdList) {
		super();
		this.classId = classId;
		this.isAllClass = isAllClass;
		this.courseName = courseName;
		this.courseList = courseList;
		this.attachmentIdList = attachmentIdList;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getIsAllClass() {
		return isAllClass;
	}
	public void setIsAllClass(String isAllClass) {
		this.isAllClass = isAllClass;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public List<NewCourseList> getCourseList() {
		return courseList;
	}
	public void setCourseList(List<NewCourseList> courseList) {
		this.courseList = courseList;
	}
	public List<String> getAttachmentIdList() {
		return attachmentIdList;
	}
	public void setAttachmentIdList(List<String> attachmentIdList) {
		this.attachmentIdList = attachmentIdList;
	}
	@Override
	public String toString() {
		return "NewPublishHomeworkBean [classId=" + classId + ", isAllClass="
				+ isAllClass + ", courseName=" + courseName + ", courseList="
				+ courseList + ", attachmentIdList=" + attachmentIdList + "]";
	}
	
	
	
	
	
	
}
