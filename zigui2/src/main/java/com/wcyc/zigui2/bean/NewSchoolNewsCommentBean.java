package com.wcyc.zigui2.bean;

import java.io.Serializable;

import com.wcyc.zigui2.newapp.bean.ServerResult;
/**
 * 校园新闻详情 评论bean
 * @author 郑国栋
 * 2016-7-21
 * @version 2.0
 */
public class NewSchoolNewsCommentBean {
	
	private String id;//这条评论的id
	private String content;//内容
	private String comment_user_name;//评论人名字
	private String comment_user_id;//评论人id
	private String comment_type;//类型
	private String point_comment_id;//被评论人id
	private String point_comment_user;//被评论人名字
	private String photo;//
	private String comment_time;//评论时间
	
	
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getComment_user_name() {
		return comment_user_name;
	}
	public void setComment_user_name(String comment_user_name) {
		this.comment_user_name = comment_user_name;
	}
	public String getComment_user_id() {
		return comment_user_id;
	}
	public void setComment_user_id(String comment_user_id) {
		this.comment_user_id = comment_user_id;
	}
	public String getComment_type() {
		return comment_type;
	}
	public void setComment_type(String comment_type) {
		this.comment_type = comment_type;
	}
	public String getPoint_comment_id() {
		return point_comment_id;
	}
	public void setPoint_comment_id(String point_comment_id) {
		this.point_comment_id = point_comment_id;
	}
	public String getPoint_comment_user() {
		return point_comment_user;
	}
	public void setPoint_comment_user(String point_comment_user) {
		this.point_comment_user = point_comment_user;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getComment_time() {
		return comment_time;
	}
	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}
	
	
	
	
	
	@Override
	public String toString() {
		return "NewSchoolNewsCommentBean [id=" + id + ", content=" + content
				+ ", comment_user_name=" + comment_user_name
				+ ", comment_user_id=" + comment_user_id + ", comment_type="
				+ comment_type + ", point_comment_id=" + point_comment_id
				+ ", point_comment_user=" + point_comment_user + ", photo="
				+ photo + ", comment_time=" + comment_time + "]";
	}
	
		
	
}
