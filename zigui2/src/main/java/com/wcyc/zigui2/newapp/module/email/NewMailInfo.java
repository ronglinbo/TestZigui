package com.wcyc.zigui2.newapp.module.email;

import java.io.Serializable;
import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

//邮件实体类
public class NewMailInfo extends NewBaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4144013412663525341L;
	private String id;//邮件的唯一ID
	private String title;//标题
	private String content;//内容
	private String createTime;//时间
	private String updateTime;//更新时间
	private String isRead;//（收件箱）当前用户是否已读（0 否 1 是）
	private String createUserId;//发件人id
	private String createUserName;//发件人姓名
	private String userType;//发件人身份类型（2教师，3学生）
	private int preStatus;
	private int status;
	private String queryRes;
	private List<Recipient> emailRealationReceives;
	private List<Recipient> teacherRealation; 
	private List<Recipient> studentRealation;
	private List<Recipient> copyRealation;
	private List<Attachment> listSAI;

	public List<Recipient> getEmailRealationReceives() {
		return emailRealationReceives;
	}

	public void setEmailRealationReceives(List<Recipient> emailRealationReceives) {
		this.emailRealationReceives = emailRealationReceives;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public class Recipient implements Serializable{
		private String receiveObjectId;
		private String receiveObjectName;
		public String getReceiveObjectId() {
			return receiveObjectId;
		}
		public void setReceiveObjectId(String receiveObjectId) {
			this.receiveObjectId = receiveObjectId;
		}
		public String getReceiveObjectName() {
			return receiveObjectName;
		}
		public void setReceiveObjectName(String receiveObjectName) {
			this.receiveObjectName = receiveObjectName;
		}
	}
	public class Attachment implements Serializable{
		private String fileSystemId;
		private String fileName;
		public String getFileSystemId() {
			return fileSystemId;
		}
		public void setFileSystemId(String fileSystemId) {
			this.fileSystemId = fileSystemId;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public List<Recipient> getTeacherRealation() {
		return teacherRealation;
	}
	public void setTeacherRealation(List<Recipient> teacherRealation) {
		this.teacherRealation = teacherRealation;
	}
	public List<Recipient> getStudentRealation() {
		return studentRealation;
	}
	public void setStudentRealation(List<Recipient> studentRealation) {
		this.studentRealation = studentRealation;
	}
	public List<Recipient> getCopyRealation() {
		return copyRealation;
	}
	public void setCopyRealation(List<Recipient> copyRealation) {
		this.copyRealation = copyRealation;
	}
	public List<Attachment> getListSAI() {
		return listSAI;
	}
	public void setListSAI(List<Attachment> listSAI) {
		this.listSAI = listSAI;
	}
	public int getPreStatus() {
		return preStatus;
	}
	public void setPreStatus(int preStatus) {
		this.preStatus = preStatus;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getQueryRes() {
		return queryRes;
	}
	public void setQueryRes(String queryRes) {
		this.queryRes = queryRes;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
}
