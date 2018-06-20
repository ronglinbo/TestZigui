/**   
 * 文件名：com.example.zigui.bean.Stu.java   
 *   
 * 版本信息：   
 * 日期：2014年9月23日 下午3:02:51  
 * Copyright 惟楚有材 Corporation 2014    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.bean;

import java.io.Serializable;

/**
 * 此类描述的是:权限列表
 * 
 rights	list	权限列表
  funcKey	String	功能关键字


 */

public class Rights implements Serializable {
	private static final long serialVersionUID = 6236587884538563724L;
	private String  funcKey;
	 
	
	 
	
	public String getFuncKey() {
		return funcKey;
	}
	 
	
	public void setFuncKey(String funcKey) {
		this.funcKey = funcKey;
	}
	 
	 


	
	
	
}
