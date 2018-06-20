package com.wcyc.zigui2.newapp.bean;

import java.util.HashMap;
import java.util.List;

public class UploadFileResult extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4648206869218662728L;
	private List<String> failFiles;
	private String failReason;
	private HashMap<String,String> succFiles;
	public List<String> getFailFiles() {
		return failFiles;
	}
	public void setFailFiles(List<String> failFiles) {
		this.failFiles = failFiles;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public HashMap<String,String> getSuccFiles() {
		return succFiles;
	}
	public void setSuccFiles(HashMap<String,String> succFiles) {
		this.succFiles = succFiles;
	}
}