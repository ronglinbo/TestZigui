package com.wcyc.zigui2.newapp.bean;

import java.util.List;

public class ModelRemindList extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1371642877711315005L;

	private List <ModelRemind> modelRemindList;
	public List <ModelRemind> getMessageList() {
		return modelRemindList;
	}
	public void setMessageList(List <ModelRemind> modelRemindList) {
		this.modelRemindList = modelRemindList;
	}
		
	public class ModelRemind{
		private String type;
		private String count;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getCount() {
			return count;
		}
		public void setCount(String count) {
			this.count = count;
		}
	}
}