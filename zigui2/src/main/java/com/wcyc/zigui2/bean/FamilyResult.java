package com.wcyc.zigui2.bean;

import java.io.Serializable;

//2014-12-15
/**
 * 邀请关注家庭成员定义 .
 * @author xfliu
 */
public class FamilyResult implements Serializable {

    private String name;  //人员关系（爷爷奶奶等）
    
    private String cellphone;  //手机号码
    
    private String state;  //状态（0：已删除；、1：已加入 ；2：申请中； 3：邀请中； ）

    private String familyUserID;  // 家庭人员用户ID

    public String getStateString(){
    	if("0".equals(state)){
    		return "已删除";
    	}else if("1".equals(state)){
    		return "删 除";
    	}else if("2".equals(state)){
    		return "申请中";
    	}else{
    		return "等待验证";
    	}
    }
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFamilyUserID() {
		return familyUserID;
	}

	public void setFamilyUserID(String familyUserID) {
		this.familyUserID = familyUserID;
	}
    
}

