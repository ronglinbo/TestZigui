package com.wcyc.zigui2.newapp.bean;
import java.io.Serializable;
import java.util.List;

/**
 * 发布作业对象的封装
 * 
 * @author 郑国栋
 * 2016-4-15
 * @version 2.0
 */
public class NewCourseList implements Serializable{
	private String courseId;//科目id
	private String content;//作业内容
	private String picNum;//图片数量
	private List<String> attachmentIdList;
	
	
	public NewCourseList() {
		super();
	}
	public NewCourseList(String courseId, String content, String picNum) {
		super();
		this.courseId = courseId;
		this.content = content;
		this.picNum = picNum;
	}
	
	
	
	
	public List<String> getAttachmentIdList() {
		return attachmentIdList;
	}
	public void setAttachmentIdList(List<String> attachmentIdList) {
		this.attachmentIdList = attachmentIdList;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPicNum() {
		return picNum;
	}
	public void setPicNum(String picNum) {
		this.picNum = picNum;
	}
	
	@Override
	public String toString() {
		return "NewCourseList [courseId=" + courseId + ", content=" + content
				+ ", picNum=" + picNum + ", attachmentIdList="
				+ attachmentIdList + "]";
	}

}
