package com.wcyc.zigui2.contactselect;

import java.util.List;

import com.wcyc.zigui2.bean.BaseBean;

/*
 * 
 * code	Integer	返回代码
200 成功 201 请求失败
childNum	String	班级学生个数
childList	List	小孩List
childID	String	小孩ID
ChildName	String	小孩姓名
childIconURL	String	小孩头像Url
childClassName  	String	班级名称
    sortLetter	String	小孩名字的排序字母（拼音首字母）

 * 
 * 
 * */

public class ClassChildBean extends BaseBean {
	private static final long serialVersionUID = -9074125587382579981L;
	
	private int childNum;
	
	
	private List<ClassChild> childList;
	
	
	
	public int getChildNum() {
		return childNum;
	}
	public void setChildNum(int childNum) {
		this.childNum = childNum;
	}
	
	
	
	public List<ClassChild> getClassChildList() {
		return childList;
	}
	
	
	public void setClassChildList(List<ClassChild> childList) {
		this.childList = childList;
	}
	
}
