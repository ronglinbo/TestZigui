package com.wcyc.zigui2.newapp.bean;

/**
 * 
 * 老师个人信息 bean
 * 
 * @author gdzheng
 * @time 2016-04-11
 * 
 *	code	Integer	返回代码
 *	200 成功 201 请求失败
 *	userId     		String  老师id
 *	userPicFileId	String	老师头像id
 *	email			String	老师邮箱
 *  newPassword		String  老师新密码
 * 	password		String  老师密码
 * 	mobileNum		String  老师手机号码
 * 
 */
/**
 * @author Administrator
 *
 */
public class NewTeacherInfoBean {
//	private static final long serialVersionUID = -9077225915482595881L;
	private String userId;
	private String userPicFileId;
	private String email;
	private String newPassword;
	private String password;
	private String mobileNum;
	public NewTeacherInfoBean(String userId, String userPicFileId,
			String email, String newPassword, String password, String mobileNum) {
		super();
		this.userId = userId;
		this.userPicFileId = userPicFileId;
		this.email = email;
		this.newPassword = newPassword;
		this.password = password;
		this.mobileNum = mobileNum;
	}
	public NewTeacherInfoBean() {
		super();
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPicFileId() {
		return userPicFileId;
	}
	public void setUserPicFileId(String userPicFileId) {
		this.userPicFileId = userPicFileId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	@Override
	public String toString() {
		return "NewTeacherInfoBean [userId=" + userId + ", userPicFileId="
				+ userPicFileId + ", email=" + email + ", newPassword="
				+ newPassword + ", password=" + password + ", mobileNum="
				+ mobileNum + "]";
	}
	
	
	
	

}
