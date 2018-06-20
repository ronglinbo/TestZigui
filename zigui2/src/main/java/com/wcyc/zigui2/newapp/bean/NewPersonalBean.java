package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class NewPersonalBean  {
	
	private String publishTime;
	private String content;
	private String classId;
	private ArrayList<String> attachmentInfoList;
	private List<ClassAttachmentBean> attachmentInfoList_new;//
	private String attchmentType;//附件类型 1图片   2视频音频  3文件文档
	private String publisherImgUrl;//发布人头像
	private String pcitureAddress;//

	public String getPcitureAddress() {
		return pcitureAddress;
	}
	public void setPcitureAddress(String pcitureAddress) {
		this.pcitureAddress = pcitureAddress;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<ClassAttachmentBean> getAttachmentInfoList_new() {
		return attachmentInfoList_new;
	}
	public void setAttachmentInfoList_new(ArrayList<ClassAttachmentBean> attachmentInfoList_new) {
		this.attachmentInfoList_new = attachmentInfoList_new;
	}
	public String getAttchmentType() {
		return attchmentType;
	}
	public void setAttchmentType(String attchmentType) {
		this.attchmentType = attchmentType;
	}
	public String getPublisherImgUrl() {
		return publisherImgUrl;
	}
	public void setPublisherImgUrl(String publisherImgUrl) {
		this.publisherImgUrl = publisherImgUrl;
	}


	public class ClassAttachmentBean implements Serializable {
		private String attachementName;
		private String attachementUrl;

		public ClassAttachmentBean() {
			super();
		}
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
}
