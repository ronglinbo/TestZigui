package com.wcyc.zigui2.newapp.module.othernumber;

import java.io.Serializable;
import java.util.List;

/**
 * 副号用户基本信息
 * 
 * @author 郑国栋
 * 2016-9-7
 * @version 2.06
 */
public class NewOtherUserBean implements Serializable{

	private int id;//
	private int relationTypeCode;//副号与小孩的关系码
	private String relationType;//副号与小孩当前的关系名称
	private String mobile;//副号的手机号
//	private int viceNumber;//当前账号的副号个数
	private String name;
	private int ismain;

	public NewOtherUserBean() {
		super();
	}

	public int getRelationTypeCode() {
		return relationTypeCode;
	}

	public void setRelationTypeCode(int relationTypeCode) {
		this.relationTypeCode = relationTypeCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIsmain() {
		return ismain;
	}

	public void setIsmain(int ismain) {
		this.ismain = ismain;
	}

	@Override
	public String toString() {
		return "NewOtherUserBean{" +
				"id=" + id +
				", relationTypeCode=" + relationTypeCode +
				", relationType='" + relationType + '\'' +
				", mobile='" + mobile + '\'' +
				", name='" + name + '\'' +
				", ismain=" + ismain +
				'}';
	}
}
