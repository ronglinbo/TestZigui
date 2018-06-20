package com.wcyc.zigui2.bean;

import java.util.List;

/**
 * @author xfliu
 * @since 2014-12-16
 */
public class NoticeBean extends BaseBean {
	private static final long serialVersionUID = -9074125911382579981L;
	
	private int noticeNum;
	private List<ParentsMsg> noticeList;
	private int classNum;
	private List<Classes> classList;
	private String state;
	/**   
	 * state   
	 *   
	 * @return  the state   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getState() {
		return state;
	}
	/**   
	 * @param state the state to set   
	 */
	
	public void setState(String state) {
		this.state = state;
	}
	public int getNoticeNum() {
		return noticeNum;
	}
	public void setNoticeNum(int noticeNum) {
		this.noticeNum = noticeNum;
	}
	
	public List<ParentsMsg> getMsgList() {
		return noticeList;
	}
	
	
	public void setMsgList(List<ParentsMsg> noticeList) {
		this.noticeList = noticeList;
	}
	
	public int getClassNum() {
		return classNum;
	}
	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}
	
	
	public List<Classes> getClassList() {
		return classList;
	}
	
	
	public void setClassList(List<Classes> classList) {
		this.classList = classList;
	}
}
