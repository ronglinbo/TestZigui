package com.wcyc.zigui2.newapp.bean;

import java.util.List;

public class MoniterListInfo extends NewBaseBean {

	public InfoIpcSchoolInfo getInfoIpcSchoolInfo() {
		return infoIpcSchoolInfo;
	}

	public void setInfoIpcSchoolInfo(InfoIpcSchoolInfo infoIpcSchoolInfo) {
		this.infoIpcSchoolInfo = infoIpcSchoolInfo;
	}

	public List<UserGroupList> getUserGroupList() {
		return userGroupList;
	}

	public void setUserGroupList(List<UserGroupList> userGroupList) {
		this.userGroupList = userGroupList;
	}

	/**
	 *
	 */
	public class InfoIpcSchoolInfo {
		private String hostIp;
		private long hostPort;
		private String user;
		private String pass;

		public String getHostIp() {
			return hostIp;
		}

		public void setHostIp(String hostIp) {
			this.hostIp = hostIp;
		}

		public long getHostPort() {
			return hostPort;
		}

		public void setHostPort(long hostPort) {
			this.hostPort = hostPort;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getPass() {
			return pass;
		}

		public void setPass(String pass) {
			this.pass = pass;
		}
	}
	public class UserGroupList {
		private String id;
		private String user_group_name;
		private String type;
		private List<IPCList> IPCList;
		private List<AreaList> areaList;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUser_group_name() {
			return user_group_name;
		}

		public void setUser_group_name(String user_group_name) {
			this.user_group_name = user_group_name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<MoniterListInfo.IPCList> getIPCList() {
			return IPCList;
		}

		public void setIPCList(List<MoniterListInfo.IPCList> IPCList) {
			this.IPCList = IPCList;
		}

		public List<AreaList> getAreaList() {
			return areaList;
		}

		public void setAreaList(List<AreaList> areaList) {
			this.areaList = areaList;
		}
	}
	public class IPCList {
		private String name;
		private String type;
		private String ipcId;
		private int status;
		private String ipcDesc;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getIpcId() {
			return ipcId;
		}

		public void setIpcId(String ipcId) {
			this.ipcId = ipcId;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getIpcDesc() {
			return ipcDesc;
		}

		public void setIpcDesc(String ipcDesc) {
			this.ipcDesc = ipcDesc;
		}
	}
	public class AreaList{
		private long id;
		private String groupName;
		private String type;
		private List<IPCList> areaIPCList;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<IPCList> getAreaIPCList() {
			return areaIPCList;
		}

		public void setAreaIPCList(List<IPCList> areaIPCList) {
			this.areaIPCList = areaIPCList;
		}
	}

	private InfoIpcSchoolInfo infoIpcSchoolInfo;
	private List<UserGroupList> userGroupList;
}