
package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;


public class ContactsMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3323474172403589783L;

	/**
	 * 
	 */
	private String userName;
	private String cellphone;
	private String nickName;
	private String userIconURL;
	private String course;
	private String userIdentity;
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
}
