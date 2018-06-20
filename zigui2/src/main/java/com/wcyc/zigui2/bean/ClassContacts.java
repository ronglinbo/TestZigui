package com.wcyc.zigui2.bean;

import java.io.Serializable;
import java.util.List;

public class ClassContacts implements Serializable {
	
	private static final long serialVersionUID = 2660372045409002136L;

	private List<UserEntity> contactsList;
	private String classID;
	private String className;
	private String groupID;
	private String groupName;
	private String gradeName;
	private String isAdviser;
	private int isMain;
	
	public int getIsMain() {
		return isMain;
	}
	public void setIsMain(int isMain) {
		this.isMain = isMain;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public List<UserEntity> getContactsList() {
		return contactsList;
	}
	public void setContactsList(List<UserEntity> contactsList) {
		this.contactsList = contactsList;
	}
	public String getClassID() {
		return classID;
	}
	public void setClassID(String classID) {
		this.classID = classID;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getIsAdviser() {
		return isAdviser;
	}
	public void setIsAdviser(String isAdviser) {
		this.isAdviser = isAdviser;
	}
	
}
