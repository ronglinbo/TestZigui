package com.wcyc.zigui2.bean;

import java.io.Serializable;

import com.wcyc.zigui2.newapp.bean.ServerResult;

public class BaseBean implements Serializable {
	private static final long serialVersionUID = -4433883125158971260L;
	
	private int resultCode;
	private String errorInfo;
	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		resultCode = resultCode;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		errorInfo = errorInfo;
	}
}
