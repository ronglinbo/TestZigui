package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

public class AttachmentBean extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7168120723901011038L;
	private List<Attachment> attachmentList; 
	
	public class Attachment implements Serializable{
		private String attachementName;
		private String attachementUrl;
		public String getAttachementName() {
			return attachementName;
		}
		public void setAttachementName(String attachementName) {
			this.attachementName = attachementName;
		}
		public String getAttachementUrl() {
			return attachementUrl;
		}
		public void setAttachementUrl(String attachementUrl) {
			this.attachementUrl = attachementUrl;
		}
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}
}