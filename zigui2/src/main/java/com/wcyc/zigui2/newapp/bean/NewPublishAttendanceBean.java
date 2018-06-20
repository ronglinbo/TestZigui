package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 发布考勤封装业务类
 * 
 * @author 郑国栋
 * 2016-4-20
 * @version 2.0
 */
public class NewPublishAttendanceBean implements Serializable{


	private static final long serialVersionUID = 1243372876499118777L;
	
	
	private String classId;//班级ID
	private ArrayList<String> studentId;//学生id  List
	private String courseNo;//第几节课   节次numb   1早自习 2上午课 3下午课 4晚自习
	private String courseNoName;// 节次名称
	private String courseName;// 课程名
	private String courseCode;// 课程代码
	private String type;// 考勤类型    1迟到 2缺席 3请假 4早退 5其他
	private String typeName;// 考勤类型名称
	private String content;// 考勤文字说明
	
	
	/*
	 * 无参构造器
	 */
	public NewPublishAttendanceBean() {
		super();
	}
	
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public ArrayList<String> getStudentId() {
		return studentId;
	}
	public void setStudentId(ArrayList<String> studentId) {
		this.studentId = studentId;
	}
	public String getCourseNo() {
		return courseNo;
	}
	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}
	public String getCourseNoName() {
		return courseNoName;
	}
	public void setCourseNoName(String courseNoName) {
		this.courseNoName = courseNoName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	@Override
	public String toString() {
		return "NewPublishAttendanceBean [classId=" + classId + ", studentId="
				+ studentId + ", courseNo=" + courseNo + ", courseNoName="
				+ courseNoName + ", courseName=" + courseName + ", courseCode="
				+ courseCode + ", type=" + type + ", typeName=" + typeName
				+ ", content=" + content + "]";
	}
	
	
	

}
