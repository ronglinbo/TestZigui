/**   
 * 文件名：com.example.zigui.bean.Stu.java   
 *   
 * 版本信息：   
 * 日期：2015年12月9日 下午3:02:51  
 * Copyright 惟楚有材 Corporation 2015    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.newapp.bean;



//2016-3-30 09:10:51
/**
 * 班级动态评论实体类  点赞的实体类也被改成了这个
 * @author yytan
 * @version 
 */
public class NewPointBean {
	/**
	 * 评论时间
	 */
	private String commentTime;
	/**
	 * 评论类型
	 */
	private boolean commentType;
	/**
	 * 评论人ID
	 */
	private int commentUserId;
	/**
	 * 评论人名字
	 */
	private String commentUserName;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * 评论ID
	 */
	private int id;
	/**
	 * 班级动态ID
	 */
	private int interactionId;
	/**
	 * 被评论人ID
	 */
	private int toCommentUserId;
	/**
	 * 被评论人名字
	 */
	private String toCommentUserName;
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public boolean isCommentType() {
		return commentType;
	}
	public void setCommentType(boolean commentType) {
		this.commentType = commentType;
	}
	public int getCommentUserId() {
		return commentUserId;
	}
	public void setCommentUserId(int commentUserId) {
		this.commentUserId = commentUserId;
	}
	public String getCommentUserName() {
		return commentUserName;
	}
	public void setCommentUserName(String commentUserName) {
		this.commentUserName = commentUserName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInteractionId() {
		return interactionId;
	}
	public void setInteractionId(int interactionId) {
		this.interactionId = interactionId;
	}
	public int getToCommentUserId() {
		return toCommentUserId;
	}
	public void setToCommentUserId(int toCommentUserId) {
		this.toCommentUserId = toCommentUserId;
	}
	public String getToCommentUserName() {
		return toCommentUserName;
	}
	public void setToCommentUserName(String toCommentUserName) {
		this.toCommentUserName = toCommentUserName;
	}
	public NewPointBean(String commentTime, boolean commentType,
			int commentUserId, String commentUserName, String content, int id,
			int interactionId, int toCommentUserId, String toCommentUserName) {
		super();
		this.commentTime = commentTime;
		this.commentType = commentType;
		this.commentUserId = commentUserId;
		this.commentUserName = commentUserName;
		this.content = content;
		this.id = id;
		this.interactionId = interactionId;
		this.toCommentUserId = toCommentUserId;
		this.toCommentUserName = toCommentUserName;
	}
	public NewPointBean() {
		super();
	}
	@Override
	public String toString() {
		return "NewPointBean [commentTime=" + commentTime + ", commentType="
				+ commentType + ", commentUserId=" + commentUserId
				+ ", commentUserName=" + commentUserName + ", content="
				+ content + ", id=" + id + ", interactionId=" + interactionId
				+ ", toCommentUserId=" + toCommentUserId
				+ ", toCommentUserName=" + toCommentUserName + "]";
	}
	
	
	
}
