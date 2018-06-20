package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;

public class ValidCodeBean implements Serializable{
	private String resultMessage;
	private ServerResult serverResult;


	public ServerResult getServerResult() {
		return serverResult;
	}


	public void setServerResult(ServerResult serverResult) {
		this.serverResult = serverResult;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
}
