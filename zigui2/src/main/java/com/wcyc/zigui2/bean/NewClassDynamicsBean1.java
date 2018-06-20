
package com.wcyc.zigui2.bean;

import java.io.Serializable;
import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewPointBean;



//2016-3-25
/**
 * 班级动态实体类  new
 * @author yytan
 * @version 
 */
public class NewClassDynamicsBean1 {
	
	/**
	 * 图片上传地址的list
	 */
	private List<String> attachmentInfoList;//
	private List<ClassAttachmentBean> attachmentInfoList_new;//
	/**
	 * 附件类型
	 */
	private String attchmentType;//1图片   2视频音频  3文件文档
	/**
	 * 班级id
	 */
	private int classId;
	/**
	 * 评论list
	 */
	private List<NewPointBean> commentList;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 点赞数
	 */
	private int goodCommentNo;
	/**
	 * 年级ID
	 */
	private int gradeId;
	/**
	 * 班级动态的ID
	 */
	private int id;
	/**
	 * 点赞的list和评论的一样
	 */
	private List<NewPointBean> loveList;
	/**
	 * 评论数
	 */
	private int pictureNo;
	/**
	 * 发布时间
	 */
	private String publishTime;
	/**
	 * 发布人头像
	 */
	private String publisherImgUrl;
	/**
	 * 发布人名字
	 */
	private String publisherName;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 发布人id
	 */

	/**
	 * 发布班级
	 */
	private String className;

	private int publishUserId;
	private String subjectView;//发布人在这个班级所教科目
	private String publishChildId;//小孩id 如果是家长
	private String publishChildName;//小孩名字  如果是家长
	private String publishUserType;//
	private String relation;//与小孩关系  如果是家长
	private String pcitureAddress;//


	public String getPcitureAddress() {
		return pcitureAddress;
	}
	public void setPcitureAddress(String pcitureAddress) {
		this.pcitureAddress = pcitureAddress;
	}
	public String getAttchmentType() {
		return attchmentType;
	}
	public void setAttchmentType(String attchmentType) {
		this.attchmentType = attchmentType;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getPublishUserType() {
		return publishUserType;
	}
	public void setPublishUserType(String publishUserType) {
		this.publishUserType = publishUserType;
	}
	public String getPublishChildId() {
		return publishChildId;
	}
	public void setPublishChildId(String publishChildId) {
		this.publishChildId = publishChildId;
	}
	public String getPublishChildName() {
		return publishChildName;
	}
	public void setPublishChildName(String publishChildName) {
		this.publishChildName = publishChildName;
	}
	public String getSubjectView() {
		return subjectView;
	}
	public void setSubjectView(String subjectView) {
		this.subjectView = subjectView;
	}
	public NewClassDynamicsBean1() {
		super();
	}
	public List<String> getAttachmentInfoList() {
		return attachmentInfoList;
	}
	public void setAttachmentInfoList(List<String> attachmentInfoList) {
		this.attachmentInfoList = attachmentInfoList;
	}
	public List<ClassAttachmentBean> getAttachmentInfoList_new() {
		return attachmentInfoList_new;
	}
	public void setAttachmentInfoList_new(List<ClassAttachmentBean> attachmentInfoList_new) {
		this.attachmentInfoList_new = attachmentInfoList_new;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public List<NewPointBean> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<NewPointBean> commentList) {
		this.commentList = commentList;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getGoodCommentNo() {
		return goodCommentNo;
	}
	public void setGoodCommentNo(int goodCommentNo) {
		this.goodCommentNo = goodCommentNo;
	}
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<NewPointBean> getLoveList() {
		return loveList;
	}
	public void setLoveList(List<NewPointBean> loveList) {
		this.loveList = loveList;
	}
	public int getPictureNo() {
		return pictureNo;
	}
	public void setPictureNo(int pictureNo) {
		this.pictureNo = pictureNo;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getPublisherImgUrl() {
		return publisherImgUrl;
	}
	public void setPublisherImgUrl(String publisherImgUrl) {
		this.publisherImgUrl = publisherImgUrl;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public int getPublishUserId() {
		return publishUserId;
	}
	public void setPublishUserId(int publishUserId) {
		this.publishUserId = publishUserId;
	}
	@Override
	public String toString() {
		return "NewClassDynamicsBean1 [attachmentInfoList="
				+ attachmentInfoList + ", classId=" + classId
				+ ", commentList=" + commentList + ", content=" + content
				+ ", goodCommentNo=" + goodCommentNo + ", gradeId=" + gradeId
				+ ", id=" + id + ", loveList=" + loveList + ", pictureNo="
				+ pictureNo + ", publishTime=" + publishTime
				+ ", publisherImgUrl=" + publisherImgUrl + ", publisherName="
				+ publisherName + ", publishUserId=" + publishUserId
				+ ", subjectView=" + subjectView + ", publishChildId="
				+ publishChildId + ", publishChildName=" + publishChildName
				+ ", publishUserType=" + publishUserType + ", relation="
				+ relation + "]";
	}

	public class ClassAttachmentBean implements Serializable{
		private String attachementName;
		private String attachementUrl;

		public ClassAttachmentBean() {
			super();
		}
		public String getAttachementName() {
			return attachementName;
		}
		public void setAttachementName(String attachementName) {
			this.attachementName = attachementName;
		}
		public String getAttachementUrl() {
			return attachementUrl;
		}
		public void setAttachementUrl(String attachementUrl) {
			this.attachementUrl = attachementUrl;
		}
	}

}
