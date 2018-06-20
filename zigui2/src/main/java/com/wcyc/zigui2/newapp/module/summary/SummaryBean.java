package com.wcyc.zigui2.newapp.module.summary;

import java.io.Serializable;
import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;

public class SummaryBean extends NewBaseBean{
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
	private List<SummaryDetail> summaryList;
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
	
	public List<SummaryDetail> getSummaryList() {
		return summaryList;
	}
	public void setSummaryList(List<SummaryDetail> summaryList) {
		this.summaryList = summaryList;
	}

	public class SummaryDetail implements Serializable{
		private String summaryTitle;
		private String summaryTime;
		private String summaryContent;
		private List<String> attachementList;
		private String attachementUrl;
		private String summaryId;
		private String summaryBrowseNum;
		private String isRead;
		private int creatorId;
		private int departId;
		private String departName;
		private String creatorName;
		private List<Attachment> attachDetail;
		private String summaryType;
		private String summarizeTypeCode;

		private int isSms;//暂不需要
//		private int readNo;//浏览次数
		private String userCode;
		private String userName;
		
		public String getAttachementUrl() {
			return attachementUrl;
		}
		public void setAttachementUrl(String attachementUrl) {
			this.attachementUrl = attachementUrl;
		}
		
		public String getIsRead() {
			return isRead;
		}
		public void setIsRead(String isRead) {
			this.isRead = isRead;
		}
		public int getCreatorId() {
			return creatorId;
		}
		public void setCreatorId(int creatorId) {
			this.creatorId = creatorId;
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
		public String getSummaryTitle() {
			return summaryTitle;
		}
		public void setSummaryTitle(String summaryTitle) {
			this.summaryTitle = summaryTitle;
		}
		public String getSummaryTime() {
			return summaryTime;
		}
		public void setSummaryTime(String summaryTime) {
			this.summaryTime = summaryTime;
		}
		public String getSummaryContent() {
			return summaryContent;
		}
		public void setSummaryContent(String summaryContent) {
			this.summaryContent = summaryContent;
		}
		public String getSummaryId() {
			return summaryId;
		}
		public void setSummaryId(String summaryId) {
			this.summaryId = summaryId;
		}
		public String getSummaryBrowseNum() {
			return summaryBrowseNum;
		}
		public void setSummaryBrowseNum(String summaryBrowseNum) {
			this.summaryBrowseNum = summaryBrowseNum;
		}
		public String getSummaryType() {
			return summaryType;
		}
		public void setSummaryType(String summaryType) {
			this.summaryType = summaryType;
		}
		public int getDepartId() {
			return departId;
		}
		public void setDepartId(int departId) {
			this.departId = departId;
		}
		public String getDepartName() {
			return departName;
		}
		public void setDepartName(String departName) {
			this.departName = departName;
		}
//		public int getReadNo() {
//			return readNo;
//		}
//		public void setReadNo(int readNo) {
//			this.readNo = readNo;
//		}
		public String getSummarizeTypeCode() {
			return summarizeTypeCode;
		}
		public void setSummarizeTypeCode(String summarizeTypeCode) {
			this.summarizeTypeCode = summarizeTypeCode;
		}

		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getUserCode() {
			return userCode;
		}
		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}
	}
}