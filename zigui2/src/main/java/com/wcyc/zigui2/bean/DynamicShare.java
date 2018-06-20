package com.wcyc.zigui2.bean;

import java.util.List;
/**
 * 
 * 动态内容基类
 * @author xiehua
 */
public class DynamicShare{
	private String ID;
	private String picPath;
	private String content;
	private boolean praise; 
	private List<String> comments;
	private String onecomment;
	private String time;
	
	public DynamicShare(String ID,String URL,String content,boolean praise,List<String> comments,String time){
		this.ID = ID;
		this.picPath = URL;
		this.content = content;
		this.praise = praise;
		this.comments = comments;
		this.time = time;
	}
	
	public DynamicShare(String ID,String URL,String content,boolean praise,String comments,String time){
		this.ID = ID;
		this.picPath = URL;
		this.content = content;
		this.praise = praise;
		this.onecomment = comments;
		this.time = time;
	}
	
	public String GetURL(){
		return picPath;
	}
	
	public String GetContent(){
		return content;
	}
	
	public String GetID(){
		return ID;
	}
	
	public boolean GetPraise(){
		return praise;
	}
	
	public List<String> GetComments(){
		return comments;
	}
	
	public String GetTime(){
		return time;
	}
}