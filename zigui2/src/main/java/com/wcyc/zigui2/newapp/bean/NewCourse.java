package com.wcyc.zigui2.newapp.bean;
/**
 * 课程 描述
 * 课程id
 * 课程name
 * 
 * @author 郑国栋
 * 2016-4-15
 * @version 2.0
 */
public class NewCourse {
	private String courseId;
	private String courseName;
	public NewCourse() {
		super();
	}
	public NewCourse(String courseId, String courseName) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	@Override
	public String toString() {
		return "NewCourse [courseId=" + courseId + ", courseName=" + courseName
				+ "]";
	}
	
	
}
