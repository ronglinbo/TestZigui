package com.wcyc.zigui2.chooseContact;

import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class AllStudentsByClass extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3407984370946421954L;
	private List<StudentClass> studentClassList;
	class StudentClass{
		private long classId;
		private long gradeId;
		private String gradeName; 
		private String className;
		private List<Student> studentList;
		class Student{  
			private long studentId;
			private String studentName;  
			private String imgUrl;
			private String phone;
			public long getStudentId() {
				return studentId;
			}
			public void setStudentId(long studentId) {
				this.studentId = studentId;
			}
			public String getStudentName() {
				return studentName;
			}
			public void setStudentName(String studentName) {
				this.studentName = studentName;
			}
			public String getImgUrl() {
				return imgUrl;
			}
			public void setImgUrl(String imgUrl) {
				this.imgUrl = imgUrl;
			}
			public String getPhone() {
				return phone;
			}
			public void setPhone(String phone) {
				this.phone = phone;
			}
		}
		public long getClassId() {
			return classId;
		}
		public void setClassId(long classId) {
			this.classId = classId;
		}
		public long getGradeId() {
			return gradeId;
		}
		public void setGradeId(long gradeId) {
			this.gradeId = gradeId;
		}
		public String getGradeName() {
			return gradeName;
		}
		public void setGradeName(String gradeName) {
			this.gradeName = gradeName;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public List<Student> getStudentList() {
			return studentList;
		}
		public void setStudentList(List<Student> studentList) {
			this.studentList = studentList;
		}
	}
	public List<StudentClass> getStudentClassList() {
		return studentClassList;
	}
	public void setStudentClassList(List<StudentClass> studentClassList) {
		this.studentClassList = studentClassList;
	}
}