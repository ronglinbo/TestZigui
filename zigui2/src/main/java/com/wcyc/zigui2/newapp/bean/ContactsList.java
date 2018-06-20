
package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.Map;


public class ContactsList implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1029816876823448832L;
	private String userId;//用户Id
	private String userName;//环信用户名
	private String cellphone;
	private String nickName;
	private String hxNickName;//环信昵称
	private String userIconURL;
	private String course;
	private String userIdentity;
	private String text;//职能标签
	private String header;//首字母
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserIconURL() {
		return userIconURL;
	}
	public void setUserIconURL(String userIconURL) {
		this.userIconURL = userIconURL;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getUserIdentity() {
		return userIdentity;
	}
	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHxNickName() {
		return hxNickName;
	}
	public void setHxNickName(String hxNickName) {
		this.hxNickName = hxNickName;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
