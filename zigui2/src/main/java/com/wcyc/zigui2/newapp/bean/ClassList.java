
package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

//联系人信息
public class ClassList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8861097511025503960L;
	private String classId;
	private String className;
	private String groupId;//群组环信Id
	private String groupName;//群组名称
	private List<ContactsList> contactsList;
	public String getClassID() {
		return classId;
	}
	public void setClassID(String classID) {
		this.classId = classID;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<ContactsList> getContactsList() {
		return contactsList;
	}
	public void setContactsList(List<ContactsList> contactsList) {
		this.contactsList = contactsList;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
