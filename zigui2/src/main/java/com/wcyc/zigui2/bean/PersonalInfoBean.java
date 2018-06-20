package com.wcyc.zigui2.bean;

import java.util.List;



/**
 * @author ph
 * 
 * userPhone	String	手机号码（中间4位隐藏，例如135****9191）
userIconURL	String	会员头像url
userMail	String	我的邮箱
childNum	String	小孩个数
childList	List	小孩List
childID	String	小孩ID
ChildName	String	小孩姓名
schoolID	String	学校ID
SchoolName	String	学校名称
childIconURL	String	小孩头像Url
childClassID	String	小孩班级ID
childClassName  	String	班级名称
gradeID	String	年级Id
gradeName	String	年级名称

 *
 */
public class PersonalInfoBean extends BaseBean {
	private static final long serialVersionUID = -9077125915482595881L;
	
	private String userPhone;
	private String userIconURL;
	private String userMail;

	private int childNum;
	private List<Child> childList;
	
	 
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	
	
	public String getUserIconURL() {
		return userIconURL;
	}
	
	public void setUserIconURL(String userIconURL) {
		this.userIconURL = userIconURL;
	}
	
	
	public String getUserMail() {
		return userMail;
	}
	
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	
	public int getChildNum() {
		return childNum;
	}
	
	public void setChildNum(int childNum) {
		this.childNum = childNum;
	}
	public List<Child> getChildList() {
		return childList;
	}
	public void setChildList(List<Child> childList) {
		this.childList = childList;
	}
	
}
