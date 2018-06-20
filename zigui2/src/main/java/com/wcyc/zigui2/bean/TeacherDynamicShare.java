package com.wcyc.zigui2.bean;

import java.util.List;
/**
 * 
 * 老师侧动态内容类
 * @author xiehua
 */
public class TeacherDynamicShare extends DynamicShare{

	private String classId;
	
	public TeacherDynamicShare(String ID, String URL, String content,
			boolean praise, List<String> comments,String classId,String time) {
		super(ID, URL, content, praise, comments,time);
		this.classId = classId;
	}
	
	public TeacherDynamicShare(String ID, String URL, String content,
			boolean praise, String comments,String classId,String time) {
		super(ID, URL, content, praise, comments,time);
		this.classId = classId;
	}
	
	public String getClassId(){
		return classId;
	}
}