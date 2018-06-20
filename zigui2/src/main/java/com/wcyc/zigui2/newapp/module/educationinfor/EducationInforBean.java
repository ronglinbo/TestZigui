package com.wcyc.zigui2.newapp.module.educationinfor;

/**
 * 校园新闻 bean
 *
 * @author 郑国栋
 *         2016-6-28
 * @version 2.1
 */
public class EducationInforBean {

    private String id;
    private String title;
    private String content;
    private String publishTime;
    private int browseNo;
    private String goodCommentNo;//点赞数
    private String commentCounts;//评论数
    private String pictureUrls;//
    private String isRead;
    private boolean comment_status;  //是否可以评论

    private String clearContent;//分享内容
    private String cover;//图片地址


    public String getClearContent() {
        return clearContent;
    }

    public void setClearContent(String clearContent) {
        this.clearContent = clearContent;
    }


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

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

    public boolean isComment_status() {
        return comment_status;
    }

    public void setComment_status(boolean comment_status) {
        this.comment_status = comment_status;
    }
}
