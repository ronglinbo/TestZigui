package com.wcyc.zigui2.newapp.module.news;
/**
 * 校园新闻 bean
 * 
 * @author 郑国栋
 * 2016-6-28
 * @version 2.1
 */
public class NewSchoolNewsBean {

	private String id;
	private String title;
	private String content;
	private String publishTime;
	private int browseNo;
	private String goodCommentNo;//点赞数
	private String commentCounts;//评论数
	private String pictureUrls;//
	private String isRead;//为1已读，为0未读

	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getPictureUrls() {
		return pictureUrls;
	}
	public void setPictureUrls(String pictureUrls) {
		this.pictureUrls = pictureUrls;
	}
	public String getCommentCounts() {
		return commentCounts;
	}
	public void setCommentCounts(String commentCounts) {
		this.commentCounts = commentCounts;
	}
	public String getGoodCommentNo() {
		return goodCommentNo;
	}
	public void setGoodCommentNo(String goodCommentNo) {
		this.goodCommentNo = goodCommentNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public int getBrowseNo() {
		return browseNo;
	}
	public void setBrowseNo(int browseNo) {
		this.browseNo = browseNo;
	}
}
