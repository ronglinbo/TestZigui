/**   
 * 文件名：com.example.zigui.bean.Stu.java   
 *   
 * 版本信息：   
 * 日期：2014年9月23日 下午3:02:51  
 * Copyright 惟楚有材 Corporation 2014    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.contactselect;


//2015-09-16 10:11:51
/**
 * 成绩对象类.
 * @author yytan
 * @version 1.20
 */
public class ScoreChild {

	/**
	 * 学期
	 */
	private String term;

	/**
	 * 考试名称
	 */
	private String ksmc;

	/**
	 * 成绩
	 */
	private String score;

	/**
	 * 平均成绩
	 */
	private String avgScore;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getKsmc() {
		return ksmc;
	}

	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(String avgScore) {
		this.avgScore = avgScore;
	}

	
}
