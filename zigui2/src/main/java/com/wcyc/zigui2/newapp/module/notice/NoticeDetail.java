package com.wcyc.zigui2.newapp.module.notice;

import java.io.Serializable;
import java.util.List;

import com.wcyc.zigui2.newapp.bean.AttachmentBean;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;

public class NoticeDetail implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -1608386879858085623L;
	private String creatorId;
	private String creatorName;

	private String noticeTitle;
	private String noticetime;
	private String noticeContent;
	private List<String> attachementList;
	private int noticeId;
	private int noticeBrowseNum;
	private String isRead;
	private String isMysend;//是否是自己发送的（"1"自己发送的）
	private List<Attachment> attachDetail;
	private String noticeTypeStr; //通知类型

	public String getNoticeTypeStr() {
		return noticeTypeStr;
	}

	public void setNoticeTypeStr(String noticeTypeStr) {
		this.noticeTypeStr = noticeTypeStr;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	public String getNoticetime() {
		return noticetime;
	}
	public void setNoticetime(String noticetime) {
		this.noticetime = noticetime;
	}
	public String getNoticeContent() {
		return noticeContent;
	}
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	//	public List<Attachment> getAttachementList() {
//		return attachementList;
//	}
//	public void setAttachementList(List<Attachment> attachementList) {
//		this.attachementList = attachementList;
//	}
	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
	public int getNoticeBrowseNum() {
		return noticeBrowseNum;
	}
	public void setNoticeBrowseNum(int noticeBrowseNum) {
		this.noticeBrowseNum = noticeBrowseNum;
	}
	public String isRead() {
		return isRead;
	}
	public void setRead(String isRead) {
		this.isRead = isRead;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
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

	public String getIsMysend() {
		return isMysend;
	}

	public void setIsMysend(String isMysend) {
		this.isMysend = isMysend;
	}
}
