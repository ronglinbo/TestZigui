package com.wcyc.zigui2.bean;

import java.util.List;

/*
 * 
 * 家长我的报名
	
	功能描述

	入参
参数名	参数类型（长度）	描述
userID	String		用户ID
childID	String		孩子ID
curPage	String	当前页
pageCountNum	String	每页记录数


	出参
参数名	参数类型	描述
enrollListNum	String	消息总数
enrollList	List	列表
  coursewareID	String	课件资源Id
  coursewareName	String	课件名称
  coursewareIconURL	String	图标URL
coursewareTime	String	开课时间
  coursewareMaster	String	主讲人
  state	String	状态（报名中 1 已下架 2 已结束3）
orderID	String	订单号
orderTime	String	订单时间

		

 * 
 * 
 * */

public class MyEnrollBean extends BaseBean {
	private static final long serialVersionUID = -9074125587382579981L;
	
	private int enrollListNum;
	
	private List<MyEnroll> enrollList;
	
	public int getEnrollListNum() {
		return enrollListNum;
	}
	public void setEnrollListNum(int enrollListNum) {
		this.enrollListNum = enrollListNum;
	}
	public List<MyEnroll> getEnrollList() {
		return enrollList;
	}
	public void setEnrollList(List<MyEnroll> enrollList) {
		this.enrollList = enrollList;
	}
}
