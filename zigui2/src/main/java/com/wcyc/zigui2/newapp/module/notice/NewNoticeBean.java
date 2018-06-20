package com.wcyc.zigui2.newapp.module.notice;

import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class NewNoticeBean extends NewBaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6540857708235963797L;
	private int pageNum;
	private int totalPageNum;
	private int pageSize;
	private List<NoticeDetail> noticeList;
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
	public List<NoticeDetail> getNoticeList() {
		return noticeList;
	}
	public void setNoticeList(List<NoticeDetail> noticeList) {
		this.noticeList = noticeList;
	}
}