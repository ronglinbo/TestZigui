/**   
 * 文件名：com.example.zigui.bean.Stu.java   
 *   
 * 版本信息：   
 * 日期：2014年9月23日 下午3:02:51  
 * Copyright 惟楚有材 Corporation 2014    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

//2014-09-23 15:02:51
/**
 * 孩子类.<P>
 * 验证登录成功后，返回小孩的对象类。
 * @author 王登辉
 * @version 1.01
 */

public class Child implements Serializable {
	private static final long serialVersionUID = 6957483454538533724L;
	
	private String childID;
	private String childIconURL;
	private String schoolID;
	private String childClassID;
	private String childName;
	private String schoolName;
	private String gradeName;
	private String childClassName;
//	private Bitmap mBitMap;  
	
	public Bitmap getmBitMap() {
		return null;
	}
	public void setmBitMap(Bitmap mBitMap) {
//		this.mBitMap = mBitMap;
	}
	public String getChildID() {
		return childID;
	}
	public void setChildID(String childID) {
		this.childID = childID;
	}
	public String getChildIconURL() {
		return childIconURL;
	}
	public void setChildIconURL(String childIconURL) {
		this.childIconURL = childIconURL;
	}
	public String getSchoolID() {
		return schoolID;
	}
	public void setSchoolID(String schoolID) {
		this.schoolID = schoolID;
	}
	public String getChildClassID() {
		return childClassID;
	}
	public void setChildClassID(String childClassID) {
		this.childClassID = childClassID;
	}
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getChildClassName() {
		return childClassName;
	}
	public void setChildClassName(String childClassName) {
		this.childClassName = childClassName;
	}
	
	
}
