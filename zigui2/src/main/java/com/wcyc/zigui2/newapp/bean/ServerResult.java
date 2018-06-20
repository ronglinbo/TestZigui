package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;

public class ServerResult implements Serializable {
//	private static final long serialVersionUID = -4433883125158971260L;
	
	private int resultCode;
	private String resultMessage;
//	private String errorInfo;
	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
//	public String getErrorInfo() {
//		return errorInfo;
//	}
//	public void setErrorInfo(String errorInfo) {
//		this.errorInfo = errorInfo;
//	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
}
