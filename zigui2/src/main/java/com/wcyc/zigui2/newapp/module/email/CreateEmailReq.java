package com.wcyc.zigui2.newapp.module.email;

import java.util.List;
import java.util.Map;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class CreateEmailReq extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3506657770134539698L;
	private String childId;
	private String userID;
	private String userName;
	private Map<String,List<String>> subordinateGroupMap ; //教师选择器       有下级
	private Map<String,List<String>> ccSubordinateGroupMap ;// CC教师选择器   有下级
	private List<Teacher> teacherID;//选择老师List
	private List<TeacherDept> teacherDeptID;//选择老师部门List
	private List<Student> studentID;//学生List
	private List<StudentClass> studentClassID;//学生班级List
	private List<CCTeacher> CCTeacherID;//抄送老师 list
	private List<CCTeacherDept> CCTeacherDeptID;//抄送老师部门 list
	public class Teacher{
		private int tid;
		private String employeeNo;
		private String tname;
		public int getTid() {
			return tid;
		}
		public void setTid(int tid) {
			this.tid = tid;
		}
		public String getTname() {
			return tname;
		}
		public void setTname(String tname) {
			this.tname = tname;
		}
		public String getEmployeeNo() {
			return employeeNo;
		}
		public void setEmployeeNo(String employeeNo) {
			this.employeeNo = employeeNo;
		}
	}
	public class TeacherDept{



		private int tdid;
		private String employeeNo;
		private String tdname;
		public int getTdid() {
			return tdid;
		}
		public void setTdid(int tdid) {
			this.tdid = tdid;
		}
		public String getEmployeeNo() {
			return employeeNo;
		}
		public void setEmployeeNo(String employeeNo) {
			this.employeeNo = employeeNo;
		}
		public String getTdname() {
			return tdname;
		}
		public void setTdname(String tdname) {
			this.tdname = tdname;
		}
		public Map<String, List<String>> getSubordinateGroupMap() {
			return subordinateGroupMap;
		}


	}
	public class Student{
		private int sid;
		private String sname;
		private String sbname;//学生班级姓名
		public int getSid() {
			return sid;
		}
		public void setSid(int sid) {
			this.sid = sid;
		}
		public String getSbname() {
			return sbname;
		}
		public void setSbname(String sbname) {
			this.sbname = sbname;
		}
		public String getSname() {
			return sname;
		}
		public void setSname(String sname) {
			this.sname = sname;
		}
	}
	public class StudentClass{
		private int scid;
		private String scname;//名字
		private String snname;//班级所在的年级
		public int getScid() {
			return scid;
		}
		public void setScid(int scid) {
			this.scid = scid;
		}
		public String getScname() {
			return scname;
		}
		public void setScname(String scname) {
			this.scname = scname;
		}
		public String getSnname() {
			return snname;
		}
		public void setSnname(String snname) {
			this.snname = snname;
		}
	}
	public class CCTeacher{



		private int cid;
		private String cname;
		private String employeeNo;
		public int getCid() {
			return cid;
		}
		public void setCid(int cid) {
			this.cid = cid;
		}
		public String getCname() {
			return cname;
		}
		public void setCname(String cname) {
			this.cname = cname;
		}
		public String getEmployeeNo() {
			return employeeNo;
		}
		public void setEmployeeNo(String employeeNo) {
			this.employeeNo = employeeNo;
		}
		public Map<String, List<String>> getCcSubordinateGroupMap() {
			return ccSubordinateGroupMap;
		}


	}
	public class CCTeacherDept{
		private int ctid;
		private String ctname;
		public int getCtid() {
			return ctid;
		}
		public void setCtid(int ctid) {
			this.ctid = ctid;
		}
		public String getCtname() {
			return ctname;
		}
		public void setCtname(String ctname) {
			this.ctname = ctname;
		}
	}
	
	private String title;
	private String content;
	private List<String> fileAppend;//附件list
	private String isSendSms;
	private String emailStatus;//是否是草稿 （存草稿 0  不存就发1）
	private String schoolId;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public List<Teacher> getTeacherID() {
		return teacherID;
	}
	public void setTeacherID(List<Teacher> teacherID) {
		this.teacherID = teacherID;
	}
	public List<TeacherDept> getTeacherDeptID() {
		return teacherDeptID;
	}
	public void setTeacherDeptID(List<TeacherDept> teacherDeptID) {
		this.teacherDeptID = teacherDeptID;
	}
	public List<Student> getStudentID() {
		return studentID;
	}
	public void setStudentID(List<Student> studentID) {
		this.studentID = studentID;
	}
	public List<StudentClass> getStudentClassID() {
		return studentClassID;
	}
	public void setStudentClassID(List<StudentClass> studentClassID) {
		this.studentClassID = studentClassID;
	}
	public List<CCTeacher> getCCTeacherID() {
		return CCTeacherID;
	}
	public void setCCTeacherID(List<CCTeacher> cCTeacherID) {
		CCTeacherID = cCTeacherID;
	}
	public List<CCTeacherDept> getCCTeacherDeptID() {
		return CCTeacherDeptID;
	}
	public void setCCTeacherDeptID(List<CCTeacherDept> cCTeacherDeptID) {
		CCTeacherDeptID = cCTeacherDeptID;
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
	public List<String> getFileAppend() {
		return fileAppend;
	}
	public void setFileAppend(List<String> fileAppend) {
		this.fileAppend = fileAppend;
	}
	public String getIsSendSms() {
		return isSendSms;
	}
	public void setIsSendSms(String isSendSms) {
		this.isSendSms = isSendSms;
	}
	public String getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	public Map<String, List<String>> getSubordinateGroupMap() {
		return subordinateGroupMap;
	}

	public void setSubordinateGroupMap(Map<String, List<String>> subordinateGroupMap) {
		this.subordinateGroupMap = subordinateGroupMap;
	}

	public Map<String, List<String>> getCcSubordinateGroupMap() {
		return ccSubordinateGroupMap;
	}

	public void setCcSubordinateGroupMap(Map<String, List<String>> ccSubordinateGroupMap) {
		this.ccSubordinateGroupMap = ccSubordinateGroupMap;
	}
	
}