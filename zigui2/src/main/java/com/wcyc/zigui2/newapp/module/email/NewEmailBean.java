/*
 * 文 件 名:EmailBean.java
 * 创 建 人： xiehua
 * 日    期： 2016-07-04
 * 版 本 号： 1.00
 */

package com.wcyc.zigui2.newapp.module.email;

import java.util.List;

import com.wcyc.zigui2.bean.BaseBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;

//邮件实体类
public class NewEmailBean extends NewBaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6047227902610427723L;
	private int pageNum;
	private int totalPageNum;
	private int total;//当前类型邮件的数目
	private int pageSize;
	private String code;
	private List<NewMailInfo> emailList;
//	private String type;
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

	public List<NewMailInfo> getMailInfoList() {
		return emailList;
	}

	public void setMailInfoList(List<NewMailInfo> mailInfoList) {
		this.emailList = mailInfoList;
	}

//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}