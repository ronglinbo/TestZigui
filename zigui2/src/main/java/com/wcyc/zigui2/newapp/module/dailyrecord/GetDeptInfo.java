package com.wcyc.zigui2.newapp.module.dailyrecord;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class GetDeptInfo extends NewBaseBean{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1347733537742333388L;
	
	private List<DepartInfo> departList;
	
	public static class DepartInfo{
		private long id;
		private String departmentName;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getDepartmentName() {
			return departmentName;
		}
		public void setDepartmentName(String departmentName) {
			this.departmentName = departmentName;
		}
	}

	public List<DepartInfo> getDepartList() {
		return departList;
	}

	public void setDepartList(List<DepartInfo> departList) {
		this.departList = departList;
	}
}