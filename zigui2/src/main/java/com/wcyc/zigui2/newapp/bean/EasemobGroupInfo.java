package com.wcyc.zigui2.newapp.bean;

import com.easemob.chat.EMGroup;

import java.io.Serializable;
import java.util.List;

public class EasemobGroupInfo implements Serializable {
	private List<EMGroup> userGroupList;

	public List<EMGroup> getUserGroupList() {
		return userGroupList;
	}

	public void setUserGroupList(List<EMGroup> userGroupList) {
		this.userGroupList = userGroupList;
	}
}