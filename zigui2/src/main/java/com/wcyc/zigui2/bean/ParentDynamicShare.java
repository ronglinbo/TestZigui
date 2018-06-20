package com.wcyc.zigui2.bean;

import java.util.List;
/**
 * 
 * 家长侧动态内容类
 * @author xiehua
 */
public class ParentDynamicShare extends DynamicShare{

	private String childId;
	
	public ParentDynamicShare(String ID, String URL, String content,
			boolean praise, List<String> comments,String childId,String time) {
		super(ID, URL, content, praise, comments, time);
		this.childId = childId;
	}
	public ParentDynamicShare(String ID, String URL, String content,
			boolean praise, String comments,String childId,String time) {
		super(ID, URL, content, praise, comments, time);
		this.childId = childId;
	}
	public String getChildId(){
		return childId;
	}
}