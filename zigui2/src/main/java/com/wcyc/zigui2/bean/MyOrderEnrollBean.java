/**   
 * 文件名：com.example.zigui.bean.Stu.java   
 *   
 * 版本信息：   
 * 日期：2014年9月23日 下午3:02:51  
 * Copyright 惟楚有材 Corporation 2014    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.bean;


/**
 * 	我的消息资源详情
	
	入参
参数名	参数类型（长度）	描述
coursewareID	String	课件资源Id


	出参
参数名	参数类型	描述
code	Integer	返回代码
200 成功 201 请求失败
  coursewareID	String	课件资源Id
  coursewareName	String	课件名称
  coursewareIconURL	String	图标URL
coursewareTime	String	开课时间
  coursewareMaster	String	主讲人
  countdownTime	String	倒计时
  needPeopleNum	String	开课人数
  curPeopleNum	String	当前预定人数
  content	String	简介
  state	String	状态（报名中 1 已下架 2 已结束3）
orderID	String	订单号
orderTime	String	订单时间
 */

public class MyOrderEnrollBean extends BaseBean {
	private static final long serialVersionUID = 6236583454538563724L;
	
	private String coursewareID;
	private String coursewareName;
	private String coursewareIconURL;
	private String coursewareTime;
	private String coursewareMaster;
	
	private String countdownTime;
	private String needPeopleNum;
	private String curPeopleNum;
	private String content;
	
	
	private String state;
	private String orderTime;
	private String orderID;
	
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

	public String getCurPeopleNum() {
		return curPeopleNum;
	}
	public void setCurPeopleNum(String curPeopleNum) {
		this.curPeopleNum = curPeopleNum;
	}
	
	
	
	public String getNeedPeopleNum() {
		return needPeopleNum;
	}
	public void setNeedPeopleNum(String needPeopleNum) {
		this.needPeopleNum = needPeopleNum;
	}
	
	public String getCountdownTime() {
		return countdownTime;
	}
	public void setCountdownTime(String countdownTime) {
		this.countdownTime = countdownTime;
	}
	
	
	public String getCoursewareID() {
		return coursewareID;
	}
	public void setCoursewareID(String coursewareID) {
		this.coursewareID = coursewareID;
	}
	
	
	
	
	public String getCoursewareName() {
		return coursewareName;
	}
	public void setCoursewareName(String coursewareName) {
		this.coursewareName = coursewareName;
	}
	
	
	public String getCoursewareIconURL() {
		return coursewareIconURL;
	}
	public void setCoursewareIconURL(String coursewareIconURL) {
		this.coursewareIconURL = coursewareIconURL;
	}
	
	
	public String getCoursewareTime() {
		return coursewareTime;
	}
	public void setCoursewareTime(String coursewareTime) {
		this.coursewareTime = coursewareTime;
	}
	
	
	public String getCoursewareMaster() {
		return coursewareMaster;
	}
	public void setCoursewareMaster(String coursewareMaster) {
		this.coursewareMaster = coursewareMaster;
	}
	
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
