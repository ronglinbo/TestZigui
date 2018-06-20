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
 * 此类描述的是:班级列表
 * 
 classList	List	班级list
  classID	String	班级ID
  className	String	班级名称
  isAdviser String  是否是班主任  0代表不是班主任   1代表是班主任
  gradeName String 所在年级

 */

public class Classes implements Serializable {
	private static final long serialVersionUID = 6236581484538563724L;
	private String  isAdviser;
	private String classID;
	private String className;
	private String gradeName;
	
	/**   
	 * gradeName   
	 *   
	 * @return  the gradeName   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getGradeName() {
		return gradeName;
	}
	/**   
	 * @param gradeName the gradeName to set   
	 */
	
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	/**   
	 * isAdviser   
	 *   
	 * @return  the isAdviser   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getIsAdviser() {
		return isAdviser;
	}
	/**   
	 * @param isAdviser the isAdviser to set   
	 */
	
	public void setIsAdviser(String isAdviser) {
		this.isAdviser = isAdviser;
	}
	/**   
	 * classID   
	 *   
	 * @return  the classID   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getClassID() {
		return classID;
	}
	/**   
	 * @param classID the classID to set   
	 */
	
	public void setClassID(String classID) {
		this.classID = classID;
	}
	/**   
	 * className   
	 *   
	 * @return  the className   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public String getClassName() {
		return className;
	}
	/**   
	 * @param className the className to set   
	 */
	
	public void setClassName(String className) {
		this.className = className;
	}
	/**   
	 * serialversionuid   
	 *   
	 * @return  the serialversionuid   
	 * @since   CodingExample Ver(编码范例查看) 1.0   
	 */
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}




	
	
	
}
