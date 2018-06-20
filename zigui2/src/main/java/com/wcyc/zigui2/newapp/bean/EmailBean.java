/*
 * 文 件 名:EmailBean.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-07
 * 版 本 号： 1.00
 */

package com.wcyc.zigui2.newapp.bean;

import java.util.List;

import com.wcyc.zigui2.bean.BaseBean;
import com.wcyc.zigui2.newapp.module.mailbox.MailInfo;
//校长信箱实体类
public class EmailBean extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6047227902610427723L;
	private int pageNum;
	private int totalPageNum;
	private int pageSize;
	private List<MailInfo> mailInfoList;
	
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

	public List<MailInfo> getMailInfoList() {
		return mailInfoList;
	}

	public void setMailInfoList(List<MailInfo> mailInfoList) {
		this.mailInfoList = mailInfoList;
	}
}