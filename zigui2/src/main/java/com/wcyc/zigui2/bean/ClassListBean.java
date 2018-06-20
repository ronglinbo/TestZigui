package com.wcyc.zigui2.bean;

import java.util.List;

/*
code	Integer	返回代码
（200 成功 201 失败）
classNum	String	班级数
classList	List	班级list
  classID	String	班级ID
  isAdviser	String	是否班主任1是，0不是

  className	String	班级名称
		

 * */

public class ClassListBean extends BaseBean {
	private static final long serialVersionUID = -9074125441382579981L;
	
	
	
	private int classNum;
	private List<Classes> classList;
	
	 
	
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
