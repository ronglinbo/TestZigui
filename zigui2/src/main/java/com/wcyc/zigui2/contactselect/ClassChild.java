/**   
 * 文件名：com.example.zigui.bean.Stu.java   
 *   
 * 版本信息：   
 * 日期：2014年9月23日 下午3:02:51  
 * Copyright 惟楚有材 Corporation 2014    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.contactselect;

import android.os.Parcel;
import android.os.Parcelable;


//2014-09-23 15:02:51
/**
 * 小孩的对象类.
 * @author ph
 * @version 1.01
 */
public class ClassChild implements Parcelable {

	/*
	 *  code Integer 返回代码: 200 成功  201 请求失败 
	 *  childNum String 班级学生个数
	 *  childList List 小孩List
	 */ 
	/**
	 * 小孩ID 
	 */
	private String childID;

	/**
	 * 小孩头像Url
	 */
	private String childIconURL;

	/**
	 * 孩子所在班级ID 
	 */
	private String childClassID;

	/**
	 * 小孩姓名 
	 */
	private String childName;

	/**
	 *  孩子所在班级名称
	 */
	private String childClassName;
	
	/**
	 * 小孩名字的排序字母（拼音首字母） 
	 */
	private String sortLetter;
	/**
	 * 点评条数
	 */
	private String commentNum;

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	/**
	 * 得到 小孩名字的排序字母（拼音首字母）.
	 * @return 小孩名字的排序字母（拼音首字母）
	 */
	public String getSortLetter() {
		return sortLetter;
	}

	/**
	 * 设置小孩名字的排序字母（拼音首字母）.
	 * @param sortLetter 小孩名字的排序字母（拼音首字母）
	 */
	public void setSortLetter(String sortLetter) {
		this.sortLetter = sortLetter;
	}

	/**
	 * 得到孩子ID. 
	 * @return 孩子ID
	 */
	public String getChildID() {
		return childID;
	}

	/**
	 * 设置孩子ID. 
	 * @param childID 孩子ID
	 */
	public void setChildID(String childID) {
		this.childID = childID;
	}

	/**
	 * 得到孩子头像URL.
	 * @return 孩子头像URL 
	 */
	public String getChildIconURL() {
		return childIconURL;
	}

	/**
	 * 设置孩子头像URL. 
	 * @param childIconURL 孩子头像URL
	 */
	public void setChildIconURL(String childIconURL) {
		this.childIconURL = childIconURL;
	}

	/**
	 * 得到孩子所在班级ID.
	 * @return 孩子所在班级ID
	 */
	public String getChildClassID() {
		return childClassID;
	}

	/**
	 * 设置孩子所在班级ID.
	 * @param childClassID 孩子所在班级ID 
	 */
	public void setChildClassID(String childClassID) {
		this.childClassID = childClassID;
	}

	/**
	 * 得到孩子姓名.
	 * @return 孩子姓名  
	 */
	public String getChildName() {
		return childName;
	}

	/**
	 *  设置孩子姓名.
	 *  @param childName 孩子姓名
	 */
	public void setChildName(String childName) {
		this.childName = childName;
	}

	/**
	 * 得到孩子所在班级名称.
	 * @return 孩子所在班级名称 
	 */
	public String getChildClassName() {
		return childClassName;
	}

	/**
	 * 设置孩子所在班级名称.
	 * @param childClassName 孩子所在班级名称 
	 */
	public void setChildClassName(String childClassName) {
		this.childClassName = childClassName;
	}

	public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(childID);
        out.writeString(childIconURL);
        out.writeString(childClassID);
        out.writeString(childName);
        out.writeString(childClassName);
        out.writeString(sortLetter);
        out.writeString(commentNum);
    }

    public static final Parcelable.Creator<ClassChild> CREATOR
            = new Parcelable.Creator<ClassChild>() {
        public ClassChild createFromParcel(Parcel in) {
            return new ClassChild(in);
        }

        public ClassChild[] newArray(int size) {
            return new ClassChild[size];
        }
    };
    
    private ClassChild(Parcel in) {
    	childID = in.readString();
    	childIconURL = in.readString();
    	childClassID = in.readString();
    	childName = in.readString();
    	childClassName = in.readString();
    	sortLetter = in.readString();
    	commentNum = in.readString();
    }



}