package com.wcyc.zigui2.newapp.bean;

import java.util.List;
//所有联系人信息
public class AllContactListBean extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2048595036573947186L;
	private List <ClassList> classList;
	public List <ClassList> getClassList() {
		return classList;
	}
	public void setClassList(List <ClassList> classList) {
		this.classList = classList;
	}
}