/*
* 文 件 名:ChildMember.java

* 创 建 人： 姜韵雯
* 日    期： 2014-12-12
* 版 本 号： 1.05
*/

package com.wcyc.zigui2.bean;

import java.io.Serializable;

/**
 * 家长端我的界面中会员中心小孩列表中的小孩实体类
 * 
 * @author 姜韵雯
 * @version 1.05
 * @since 1.05
 */

public class ChildMember implements Serializable {
	private static final long serialVersionUID = -8587365786945012823L;
	private String childID;
	private String endDate;
	private String childName;
	private String childIconURL;
	private String useid;
	
	public String getUseid() {
		return useid;
	}
	public void setUseid(String useid) {
		this.useid = useid;
	}
	public String getChildIconURL() {
		return childIconURL;
	}
	public void setChildIconURL(String childIconURL) {
		this.childIconURL = childIconURL;
	}
	public String getChildID() {
		return childID;
	}
	public void setChildID(String childID) {
		this.childID = childID;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}

	
}
