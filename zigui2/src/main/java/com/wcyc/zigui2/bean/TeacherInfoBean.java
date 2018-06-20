package com.wcyc.zigui2.bean;




/**
 * @author ph
 * 
 code	Integer	返回代码
200 成功 201 请求失败
userPhone	String	手机号码
userIconURL	String	会员头像url
userMail	String	我的邮箱
userName	String	老师姓名
userJob	String	老师岗位
bankCard String 老师银行卡

 *
 */
public class TeacherInfoBean extends BaseBean {
	private static final long serialVersionUID = -9077225915482595881L;
	
	private String userPhone;
	private String userIconURL;
	private String userMail;
	private String bankCard;

	private String userName;
	private String userJob;
	
	
	
	/**   
	 * bankCard   
	 *   
	 * @return  the bankCard   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getBankCard() {
		return bankCard;
	}
	/**   
	 * @param bankCard the bankCard to set   
	 */
	
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserJob() {
		return userJob;
	}
	public void setUserJob(String userJob) {
		this.userJob = userJob;
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
	
	
	public String getUserMail() {
		return userMail;
	}
	
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	
 
	
}
