/**   
 * 文件名：com.example.zigui.bean.Stu.java   
 *   
 * 版本信息：   
 * 日期：2015年12月9日 下午3:02:51  
 * Copyright 惟楚有材 Corporation 2015    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.bean;



//2015-15-09 15:13:51
/**
 * 班级动态评论实体类
 * @author yytan
 * @version 
 */
public class PointBean {

	/**
	 * 评论人的名字
	 */
	private String authorname;
	/**
	 * 评论条目ID
	 */
	private String comId;
	/**
	 * 评论人的ID
	 */
	private String authorId;
	/**
	 * 评论内容
	 */
	private String commentContent;
	/**
	 * 被评论人的名字
	 */
	private String pointName;
	
	public String getAuthorname() {
		return authorname;
	}
	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getComId() {
		return comId;
	}
	public void setComId(String comId) {
		this.comId = comId;
	}
	public String getPointName() {
		return pointName;
	}
	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String toString() {
		return "PointBean [authorname="+ authorname+" commentContent=" + commentContent+"]";
	}
}
