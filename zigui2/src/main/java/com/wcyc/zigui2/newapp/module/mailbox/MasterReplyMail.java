package com.wcyc.zigui2.newapp.module.mailbox;

import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class MasterReplyMail extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4765778046363482919L;
	private List<ReplyMailBody> anwerList;
	public class ReplyMailBody{
		private String answerId;
		private String answerName;
		private String title;
		private String content;
		private String additionNo;
		private String answerTime;
		public String getAnswerId() {
			return answerId;
		}
		public void setAnswerId(String answerId) {
			this.answerId = answerId;
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
		public String getAdditionNo() {
			return additionNo;
		}
		public void setAdditionNo(String additionNo) {
			this.additionNo = additionNo;
		}
		public String getAnswerTime() {
			return answerTime;
		}
		public void setAnswerTime(String answerTime) {
			this.answerTime = answerTime;
		}
		public String getAnswerName() {
			return answerName;
		}
		public void setAnswerName(String answerName) {
			this.answerName = answerName;
		}
	}
	public List<ReplyMailBody> getAnwerList() {
		return anwerList;
	}
	public void setAnwerList(List<ReplyMailBody> anwerList) {
		this.anwerList = anwerList;
	}
}