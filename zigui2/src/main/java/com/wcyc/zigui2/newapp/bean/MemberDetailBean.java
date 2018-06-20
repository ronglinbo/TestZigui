package com.wcyc.zigui2.newapp.bean;

import java.util.List;

public class MemberDetailBean extends NewBaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -753474713853414156L;
	private String userId;
	private String hxUseruame;
	private String hxPassword;
	private List<NewChild> childList;
	private List<NewClasses> classList;
	private List<Role> roleList;
	private List<GradeleaderBean> gradeInfoList;
	
	
	public List<GradeleaderBean> getGradeInfoList() {
		return gradeInfoList;
	}
	public void setGradeInfoList(List<GradeleaderBean> gradeInfoList) {
		this.gradeInfoList = gradeInfoList;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHxUsername() {
		return hxUseruame;
	}
	public void setHxUsername(String hxUsername) {
		this.hxUseruame = hxUsername;
	}
	public String getHxPassword() {
		return hxPassword;
	}
	public void setHxPassword(String hxPassword) {
		this.hxPassword = hxPassword;
	}
	public List<NewChild> getChildList() {
		return childList;
	}
	public void setChildList(List<NewChild> childList) {
		this.childList = childList;
	}
	public List<NewClasses> getClassList() {
		return classList;
	}
	public void setClassList(List<NewClasses> classList) {
		this.classList = classList;
	}
	
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public class Role{
		private String roleCode;
		private String roleName;
		public String getRoleCode() {
			return roleCode;
		}
		public void setRoleCode(String roleCode) {
			this.roleCode = roleCode;
		}
		public String getRoleName() {
			return roleName;
		}
		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
	}
}