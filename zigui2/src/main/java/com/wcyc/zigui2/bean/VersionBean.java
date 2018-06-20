package com.wcyc.zigui2.bean;


/*
 *  userID	String		用户ID
deviceID	String	硬件ID
mobileType	String	系统版本：“android”
version	float	版本:1.0
		
		
		
		


	出参
参数名	参数类型	描述
code	Integer	返回代码
200 成功 201 请求失败
downURL	String	完成的下载路径
versionNum	String	版本号
versionDesc	String	版本描述
		


 * 
 * 
 * */

public class VersionBean extends BaseBean {
	private static final long serialVersionUID = -9074178418332579881L;
	
	private String downURL;
	private String versionNum;
	
	private String versionDesc;

	
	
	public String getVersionDesc() {
		return versionDesc;
	}
	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}
	 
	
	
	public String getDownURL() {
		return downURL;
	}
	
	
	public void setDownURL(String downURL) {
		this.downURL = downURL;
	}
	
	
	public String getVersionNum() {
		return versionNum;
	}
	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}
	 
 
	
 

	
}
