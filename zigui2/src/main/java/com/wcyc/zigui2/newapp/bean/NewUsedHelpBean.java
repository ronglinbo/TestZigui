package com.wcyc.zigui2.newapp.bean;

import java.util.ArrayList;
/**
 * 使用帮助 业务逻辑
 * 
 * @author 郑国栋
 * 2016-4-14
 * @version 2.0
 */

public class NewUsedHelpBean {
	private String question;
	private String answer;
	
	public NewUsedHelpBean(String question, String answer) {
		super();
		this.question = question;
		this.answer = answer;
	}
	
	public NewUsedHelpBean() {
		super();
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		return "NewUsedHelpBean [question=" + question + ", answer=" + answer
				+ "]";
	}
	
	
	
	
}
