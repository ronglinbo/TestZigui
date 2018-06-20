    
    /**   
     * 文件名：com.wcyc.zigui.bean.WorkBean.java   
     *   
     * 版本信息：   
     * 日期：2014年11月4日 上午9:04:48  
     * Copyright 惟楚有材 Corporation 2014    
     * 版权所有   
     *   
     */   
    
package com.wcyc.zigui2.bean;

import java.util.ArrayList;

//2014年11月4日 上午9:04:48    

/**   
 * 基本Bean类. 
 * @author 王登辉
 * @version 1.01 
 */
public class WorkBean extends BaseBean{
	/*
	 * classID String 班级ID
	 * className String 班级名
	 * courseID String 科目ID
	 * courseName String 科目名
	 * content String 内容
	 * state String 状态
	 * picAdress	String	原图片地址
	 * thumbnailPicAdress	String	缩略图地址
		date	String	时间
		courseIcon	String	科目图片路径
		String siteUrl;  //访问路径
	 */
	
	private static final long serialVersionUID = 2598072170527502839L;
	private String classID;
	private String className;
	private String courseID;
	private String courseName;
	private String content;
	private String state;
	private ArrayList<String> picAdress;
	private ArrayList<String> thumbnailPicAdress;
	private String date;
	private String courseIcon;
	private String siteUrl; 
	
	/**
	 * 得到网站URL.
	 * @return 网站URL
	 */
	public String getSiteUrl() {
		return siteUrl;
	}
	
	/**
	 * 设置网站URL.
	 * @param siteUrl 网站URL
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
	
	/**
	 * 得到课程图标
	 * @return 课程图标
	 */
	public String getCourseIcon() {
		return courseIcon;
	}
	
	/**
	 * 设置课程图标.
	 * @param courseIcon 课程图标
	 */
	public void setCourseIcon(String courseIcon) {
		this.courseIcon = courseIcon;
	}
	
	/**
	 * 得到图片地址列表.
	 * @return 图片地址列表
	 */
	public ArrayList<String> getPicAdress() {
		return picAdress;
	}
	
	/**
	 * 设置图片地址列表. 
	 * @param picAdress 图片地址列表 
	 */
	public void setPicAdress(ArrayList<String> picAdress) {
		this.picAdress = picAdress;
	}
	
	/**
	 * 得到图片缩略图列表.
	 * @return 图片缩略图列表
	 */
	public ArrayList<String> getThumbnailPicAdress() {
		return thumbnailPicAdress;
	}
	
	/**
	 * 设置图片缩略图列表.
	 * @param thumbnailPicAdress 图片缩略图列表
	 */
	public void setThumbnailPicAdress(ArrayList<String> thumbnailPicAdress) {
		this.thumbnailPicAdress = thumbnailPicAdress;
	}

	/**
	 * 得到班级ID.
	 * @return 班级ID
	 */
	public String getClassID() {
		return classID;
	}
	
	/**
	 * 设置班级ID. 
	 * @param classID 班级ID
	 */
	public void setClassID(String classID) {
		this.classID = classID;
	}

	/**
	 * 得到班级名称. 
	 * @return 班级名称
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 设置班级名称.
	 * @param className 班级名称
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 得到课程ID. 
	 * @return 课程ID
	 */
	public String getCourseID() {
		return courseID;
	}

	/**
	 * 设置课程ID.
	 * @param courseID 课程ID
	 */
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	/**
	 * 得到课程名称. 
	 * @return 课程名称
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * 设置课程名称. 
	 * @param courseName 课程名称
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * 得到内容.
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容. 
	 * @param content 内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 得到状态.
	 * @return 状态
	 */
	public String getState() {
		return state;
	}

	/**
	 * 设置状态. 
	 * @param state 状态
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * 得到日期. 
	 * @return 日期
	 */
	public String getDate() {
		return date;
	}

	/**
	 * 设置日期. 
	 * @param date 日期
	 */
	public void setDate(String date) {
		this.date = date;
	}
}
