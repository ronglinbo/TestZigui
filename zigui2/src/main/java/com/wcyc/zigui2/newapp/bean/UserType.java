package com.wcyc.zigui2.newapp.bean;
//多个身份信息
public class UserType{
	private String childId;
	private String childName;
	private String relationType;
	private String relationTypeName;
	private String classId;//小孩所在的班级id
	private String className;
	private String gradeId;
	private String gradeName;//年级名称
	private String parentName;
	private String schoolId;
	private String schoolName;
	private String userId;
	private String userType;//用户身份标识（家长 老师 ）
	private String ismian;//是否是主号
	private String hxUserName;
	private boolean ischecked;
	private String teacherName;
	
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	
	@Override
	public String toString() {
		return "UserType [childId=" + childId + ", classId=" + classId
				+ ", className=" + className + ", parentName=" + parentName
				+ ", schoolId=" + schoolId + ", schoolName=" + schoolName
				+ ", userId=" + userId + ", userType=" + userType + "]";
	}
	
	
	
	

	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	public String getRelationTypeName() {
		return relationTypeName;
	}
	public void setRelationTypeName(String relationTypeName) {
		this.relationTypeName = relationTypeName;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public boolean getIschecked() {
		return ischecked;
	}
	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}
	public String getIsmian() {
		return ismian;
	}
	public void setIsmian(String ismian) {
		this.ismian = ismian;
	}
	public String getHxUserName() {
		return hxUserName;
	}
	public void setHxUserName(String hxUserName) {
		this.hxUserName = hxUserName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
}