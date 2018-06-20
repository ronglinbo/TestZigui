package com.wcyc.zigui2.bean;

import java.util.List;


public class NewClassDynamicsBean  {
	
	private int curPage;
	private int pageSize;
	private String isNeedCLA;
	private List classIdList;
	private String type;//添加type和userId
	private String userId;
	
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getIsNeedCLA() {
		return isNeedCLA;
	}
	public void setIsNeedCLA(String isNeedCLA) {
		this.isNeedCLA = isNeedCLA;
	}
	public List getClassIdList() {
		return classIdList;
	}
	public void setClassIdList(List classIdList) {
		this.classIdList = classIdList;
	}
	
	//添加type和userId
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "NewClassDynamicsBean [curPage=" + curPage + ", pageSize="
				+ pageSize + ", isNeedCLA=" + isNeedCLA + ", classIdList="
				+ classIdList + ", type=" + type + ", userId=" + userId + "]";
	}
	
	
	
}
