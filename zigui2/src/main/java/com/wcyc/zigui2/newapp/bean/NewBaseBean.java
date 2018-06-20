package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;

import com.wcyc.zigui2.newapp.bean.ServerResult;

	public class NewBaseBean implements Serializable{
	private static final long serialVersionUID = -4433883125158971260L;
	
	
	private ServerResult serverResult;


	public ServerResult getServerResult() {
		return serverResult;
	}


	public void setServerResult(ServerResult serverResult) {
		this.serverResult = serverResult;
	}
	
}
