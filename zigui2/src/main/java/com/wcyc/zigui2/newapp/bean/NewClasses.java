/**
 * 文件名：newClasses.java
 * <p>
 * 版本信息：
 * 日期：2016年3月17
 * Copyright 惟楚有材 Corporation 2014
 * 版权所有
 */

package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;

/**
 * 此类描述的是:班级列表
 * <p>
 * classList	List	班级list
 * classID	String	班级ID
 * className	String	班级名称
 * isAdviser String  是否是班主任  0代表不是班主任   1代表是班主任
 * gradeName String 所在年级
 * <p>
 * couseId 所教课程id
 * couseName 所教课程名称
 */

public class NewClasses implements Serializable {
    private static final long serialVersionUID = 6236581484538563724L;
    private String isAdviser;
    private String classId;
    private String className;
    private String gradeName;
    private String gradeId;
    private String couseId;
    private String couseName;
    private ArrayList<Student> contactList;


    public ArrayList<Student> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<Student> contactList) {
        this.contactList = contactList;
    }

    //	public String getClassId() {
//		return classId;
//	}
//	public void setClassId(String classId) {
//		this.classId = classId;
//	}
    public String getCouseId() {
        return couseId;
    }

    public void setCouseId(String couseId) {
        this.couseId = couseId;
    }

    public String getCouseName() {
        return couseName;
    }

    public void setCouseName(String couseName) {
        this.couseName = couseName;
    }

    /**
     * gradeName
     *
     * @return the gradeName
     * @since CodingExample Ver(编码范例查看) 1.0
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
     * @return the isAdviser
     * @since CodingExample Ver(编码范例查看) 1.0
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
     * @return the classID
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public String getClassID() {
        return classId;
    }

    /**
     * @param classID the classID to set
     */

    public void setClassID(String classID) {
        this.classId = classID;
    }

    /**
     * className
     *
     * @return the className
     * @since CodingExample Ver(编码范例查看) 1.0
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
     * @return the serialversionuid
     * @since CodingExample Ver(编码范例查看) 1.0
     */

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    @Override
    public String toString() {
        return "NewClasses [isAdviser=" + isAdviser + ", classId=" + classId
                + ", className=" + className + ", gradeName=" + gradeName
                + ", gradeId=" + gradeId + ", couseId=" + couseId
                + ", couseName=" + couseName + "]";
    }


    /**
     * equals用于数据去重
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        NewClasses classA = (NewClasses) obj;
        return this.getClassID().equals(classA.getClassID())
                && this.getClassName().equals(classA.getClassName())
                && this.getGradeId().equals(classA.getGradeId())
                && this.getGradeName().equals(classA.getGradeName());
    }
}
