package com.wcyc.zigui2.newapp.bean;

import java.util.List;

import com.wcyc.zigui2.bean.Child;
import com.wcyc.zigui2.bean.Classes;
import com.wcyc.zigui2.bean.Rights;

//账号信息
public class NewMemberBean extends NewBaseBean{
	private static final long serialVersionUID = -9077125918332595881L;
	
	private String isFirstLogin;
	private String updateFlag;
	private String accId;
	private String userName;
	private List<UserType> userTypeList;
	private String email;
	private String mobile;
	private String userIconURL;
	private String nickName;
	private String idCard;
	private String isNeedModifyPwd;//是否需要修改密码（1：是，0：否）
	private String isVerification;
	private String teacherName;//设置老师名字

	public String getIsFirstLogin() {
		return isFirstLogin;
	}
	public void setIsFirstLogin(String isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	public String getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<UserType> getUserTypeList() {
		return userTypeList;
	}
	public void setUserTypeList(List<UserType> userTypeList) {
		this.userTypeList = userTypeList;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUserIconURL() {
		return userIconURL;
	}
	public void setUserIconURL(String userIconURL) {
		this.userIconURL = userIconURL;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getIsNeedModifyPwd() {
		return isNeedModifyPwd;
	}
	public void setIsNeedModifyPwd(String isNeedModifyPwd) {
		this.isNeedModifyPwd = isNeedModifyPwd;
	}

	public String getIsVerification() {
		return isVerification;
	}

	public void setIsVerification(String isVerification) {
		this.isVerification = isVerification;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
}
