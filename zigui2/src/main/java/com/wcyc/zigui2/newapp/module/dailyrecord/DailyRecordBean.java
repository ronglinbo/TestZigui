package com.wcyc.zigui2.newapp.module.dailyrecord;

import java.io.Serializable;
import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;

public class DailyRecordBean extends NewBaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -881386055442650480L;
	/**
	 * 
	 */

	private int pageNum;
	private int totalPageNum;
	private int pageSize;
	private List<DailyRecordDetail> dailyList;
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getTotalPageNum() {
		return totalPageNum;
	}
	public void setTotalPageNum(int totalPageNum) {
		this.totalPageNum = totalPageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public List<DailyRecordDetail> getDailyList() {
		return dailyList;
	}
	public void setDailyList(List<DailyRecordDetail> dailyList) {
		this.dailyList = dailyList;
	}

	public class DailyRecordDetail implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1830962361053818223L;
		private String title;
		private String create_time;
		private String content;
		private String departeId;
		private String departName;
		private List<String> attachementList;
		private String attachementUrl;
		private String id;
		private String readNo;

		private String isRead;
		private String createUserId;
		private String creatorName;
		private List<Attachment> attachDetail;
		private String userCode;
		private String userName;
		
		public String getDailyTitle() {
			return title;
		}
		public void setDailyTitle(String dailyTitle) {
			this.title = dailyTitle;
		}
		public String getDailyTime() {
			return create_time;
		}
		public void setDailyTime(String dailyTime) {
			this.create_time = dailyTime;
		}
		public String getDailyContent() {
			return content;
		}
		public void setDailyContent(String dailyContent) {
			this.content = dailyContent;
		}
		
		public String getAttachementUrl() {
			return attachementUrl;
		}
		public void setAttachementUrl(String attachementUrl) {
			this.attachementUrl = attachementUrl;
		}
		public String getDailyId() {
			return id;
		}
		public void setDailyId(String dailyId) {
			this.id = dailyId;
		}
		public String getDailyBrowseNum() {
			return readNo;
		}
		public void setDailyBrowseNum(String dailyBrowseNum) {
			this.readNo = dailyBrowseNum;
		}
		public String getIsRead() {
			return isRead;
		}
		public void setIsRead(String isRead) {
			this.isRead = isRead;
		}
		public String getCreatorId() {
			return createUserId;
		}
		public void setCreatorId(String creatorId) {
			this.createUserId = creatorId;
		}
		public String getCreatorName() {
			return creatorName;
		}
		public void setCreatorName(String creatorName) {
			this.creatorName = creatorName;
		}
		public List<String> getAttachementList() {
			return attachementList;
		}
		public void setAttachementList(List<String> attachementList) {
			this.attachementList = attachementList;
		}
		public List<Attachment> getAttachDetail() {
			return attachDetail;
		}
		public void setAttachDetail(List<Attachment> attachDetail) {
			this.attachDetail = attachDetail;
		}
		public String getDeparted() {
			return departeId;
		}
		public void setDeparted(String departeId) {
			this.departeId = departeId;
		}
		public String getDepartName() {
			return departName;
		}
		public void setDepartName(String departName) {
			this.departName = departName;
		}
		public String getReadNo() {
			return readNo;
		}
		public void setReadNo(String readNo) {
			this.readNo = readNo;
		}
		public String getUserCode() {
			return userCode;
		}
		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
	}
}