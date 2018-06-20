package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

import com.easemob.util.HanziToPinyin;

public class ClassStudent extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4202852838211211253L;
	private List<Student> studentList;
	private String name;//add for classname
	private String gradeName;
	private int id;//班级id
	private String gradeId;//年级Id
	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}


	public static class Student implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -4911982922704347539L;
		private int id;
		private String name;
		private String imgUrl;
		private String gradeClass;
		
		private String header;//
		private String sortLetter;//
		
		
		public void setHeaderStr(){
			if(name!=null){
				String header_str = HanziToPinyin.getInstance().get(name.substring(0, 1)).get(0)
						.target.substring(0, 1).toUpperCase();
				header	= header_str;
				char header_a = header.toLowerCase().charAt(0);
				if (header_a < 'a' || header_a > 'z') {
					header="#";
				}
			}
		}
		
		
		
		
		public String getHeader() {
			return header;
		}




		public void setHeader(String header) {
			this.header = header;
		}


		public String getGradeClass() {
			return gradeClass;
		}

		public void setGradeClass(String gradeClass) {
			this.gradeClass = gradeClass;
		}

		public String getSortLetter() {
			return sortLetter;
		}
		public void setSortLetter(String sortLetter) {
			this.sortLetter = sortLetter;
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
		public String getImgUrl() {
			return imgUrl;
		}
		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}