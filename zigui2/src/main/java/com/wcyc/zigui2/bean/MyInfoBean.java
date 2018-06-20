package com.wcyc.zigui2.bean;

import java.util.List;


/*
 * 家长我的主页
	使用方法

接口地址： 
请求方式：

	功能描述


	入参
参数名	参数类型（长度）	描述
userID	String		用户ID
childID	String		孩子ID


	出参
参数名	参数类型	描述
code	Integer	返回代码
200 成功 201 请求失败
userPhone	String	手机号码（中间4位隐藏，例如135****9191）
userIconURL	String	会员头像url
newMsgNum	Integer	新消息数
allMsgNum	Integer	总消息数
allEnrollNum	Integer	我的报名总数
		

 * 
 * 
 * */

public class MyInfoBean extends BaseBean {
	private static final long serialVersionUID = -9074125918332579881L;
	
	private String userPhone;
	private String userIconURL;
	
	private int newMsgNum;
	
	private int allEnrollNum;
	
	private int allMsgNum;
	
	private int allCollectNum;
	
	private String userName;
	
	private List<ChildMember> childList;



	public List<ChildMember> getChildList() {
		return childList;
	}


	public int getAllCollectNum() {
		return allCollectNum;
	}


	public void setAllCollectNum(int allCollectNum) {
		this.allCollectNum = allCollectNum;
	}


	public void setChildList(List<ChildMember> childList) {
		this.childList = childList;
	}


	/**   
	 * userName   
	 *   
	 * @return  the userName   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getUserName() {
		return userName;
	}


	/**   
	 * @param userName the userName to set   
	 */
	
	public void setUserName(String userName) {
		this.userName = userName;
	}


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
	
	
	public int getNewMsgNum() {
		return newMsgNum;
	}
	
	public void setNewMsgNum(int newMsgNum) {
		this.newMsgNum = newMsgNum;
	}
	
	
	
	public int getAllEnrollNum() {
		return allEnrollNum;
	}
	
	public void setAllEnrollNum(int allEnrollNum) {
		this.allEnrollNum = allEnrollNum;
	}
	
	

	public int getAllMsgNum() {
		return allMsgNum;
	}
	
	public void setAllMsgNum(int allMsgNum) {
		this.allMsgNum = allMsgNum;
	}
	
	

	
}
