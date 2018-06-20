package com.wcyc.zigui2.bean;

import java.util.List;

public class UserPojo extends BaseBean {
	private static final long serialVersionUID = 6734654608145413994L;
	private List<ClassContacts> classList;
	public List<ClassContacts> getClassList() {
		return classList;
	}
	public void setClassList(List<ClassContacts> classList) {
		this.classList = classList;
	}
	
}
