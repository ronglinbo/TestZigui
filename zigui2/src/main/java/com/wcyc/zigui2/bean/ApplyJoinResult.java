package com.wcyc.zigui2.bean;

import java.io.Serializable;

//2014-12-15
/**
 * 邀请关注申请人定义.
 * @author xfliu
 */
public class ApplyJoinResult implements Serializable {

    private String applyID;  //注册申请人ID

    private String cellphone;  //电话号码
    
    private String name;  //名字
    
    private String state;  //状态（ 2：申请中； 3：邀请中； ）

    public String getStateString(){
    	if("2".equals(state)){
    		return "申请中";
    	}else{
    		return "邀请中";
    	}
    }
	public String getApplyID() {
		return applyID;
	}

	public void setApplyID(String applyID) {
		this.applyID = applyID;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	
}

