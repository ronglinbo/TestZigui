package com.wcyc.zigui2.newapp.bean;

public class RoleBean {
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
	
	
	@Override
	public String toString() {
		return "RoleBean [roleCode=" + roleCode + ", roleName=" + roleName
				+ "]";
	}
	
	
}
