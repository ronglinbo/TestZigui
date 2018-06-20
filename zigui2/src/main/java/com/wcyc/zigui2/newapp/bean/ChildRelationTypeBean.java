package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;

public class ChildRelationTypeBean implements Serializable{

	public String configCode;
	public String configName;

	public String getConfigCode() {
		return configCode;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	@Override
	public String toString() {
		return "ChildRelationTypeBean{" +
				"configCode='" + configCode + '\'' +
				", configName='" + configName + '\'' +
				'}';
	}
}