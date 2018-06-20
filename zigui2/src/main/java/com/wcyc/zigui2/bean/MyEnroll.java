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

import java.io.Serializable;

/**
 * 此类描述的是:家长我的消息列表
 * 
 *
 *enrollList	List	列表
  coursewareID	String	课件资源Id
  coursewareName	String	课件名称
  coursewareIconURL	String	图标URL
coursewareTime	String	开课时间
  coursewareMaster	String	主讲人
  state	String	状态（报名中 1 已下架 2 已结束3）
orderID	String	订单号
orderTime	String	订单时间

 */

public class MyEnroll implements Serializable {
	private static final long serialVersionUID = 6236583454538563724L;
	
	private String coursewareID;
	private String coursewareName;
	private String coursewareIconURL;
	private String coursewareTime;

	private String coursewareMaster;
	private String state;
	
	private String orderID;
	private String orderTime;
	private String studentName;

	private String childID;
	private String childName;
	
	public String getChildID() {
		return childID;
	}
	public void setChildID(String childID) {
		this.childID = childID;
	}
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	/**   
	 * studentName   
	 *   
	 * @return  the studentName   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getStudentName() {
		return studentName;
	}
	/**   
	 * @param studentName the studentName to set   
	 */
	
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
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
