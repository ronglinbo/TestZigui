package com.wcyc.zigui2.newapp.module.charge2;

import java.io.Serializable;
import java.util.List;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

public class ChargeProduct extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -741297336046908035L;

	public class StudentBaseVipServices implements Serializable{
		private int id;
		private String serviceCode;
		private String studentId;
		private String serviceName;
		private String endDate;
		
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getServiceCode() {
			return serviceCode;
		}
		public void setServiceCode(String serviceCode) {
			this.serviceCode = serviceCode;
		}
		public String getStudentId() {
			return studentId;
		}
		public void setStudentId(String studentId) {
			this.studentId = studentId;
		}
		public String getServiceName() {
			return serviceName;
		}
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		@Override
		public String toString() {
			return "BaseVipServices [id=" + id + ", serviceCode=" + serviceCode
					+ ", studentId=" + studentId + ", serviceName="
					+ serviceName + "]";
		}
		
		
	}
	class PackageRoduct implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5842106863161948965L;
		private int actualAmount;
		private int amount;
		private float discount;
		private int id;
		private String parentProductCode;
		private String productCode;
		private String productName;
		private String state;
		private String validityDateType;
		private String validityDateValue;
		public int getActualAmount() {
			return actualAmount;
		}
		public void setActualAmount(int actualAmount) {
			this.actualAmount = actualAmount;
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
		public float getDiscount() {
			return discount;
		}
		public void setDiscount(float discount) {
			this.discount = discount;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getParentProductCode() {
			return parentProductCode;
		}
		public void setParentProductCode(String parentProductCode) {
			this.parentProductCode = parentProductCode;
		}
		public String getProductCode() {
			return productCode;
		}
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getValidityDateType() {
			return validityDateType;
		}
		public void setValidityDateType(String validityDateType) {
			this.validityDateType = validityDateType;
		}
		public String getValidityDateValue() {
			return validityDateValue;
		}
		public void setValidityDateValue(String validityDateValue) {
			this.validityDateValue = validityDateValue;
		}
	}
	class ParentStudents implements Serializable{
		private String classId;
		private String className;
		private String gradeId;
		private String gradeName;
		private int ismain;
		private String schoolId;
		private String schoolName;
		private int studentId;
		private String studentNumber;
		private String userName;
		public String getClassId() {
			return classId;
		}
		public void setClassId(String classId) {
			this.classId = classId;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
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
		public int getIsmain() {
			return ismain;
		}
		public void setIsmain(int ismain) {
			this.ismain = ismain;
		}
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
		public int getStudentId() {
			return studentId;
		}
		public void setStudentId(int studentId) {
			this.studentId = studentId;
		}
		public String getStudentNumber() {
			return studentNumber;
		}
		public void setStudentNumber(String studentNumber) {
			this.studentNumber = studentNumber;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
	}


	class SchoolProducts implements Serializable{

		private int actualAmount;
		private int amount;
		private float discount;
		private int id;
		private String parentProductCode;
		private String productCode;
		private String productName;
		private int schoolId;
		private int state;
		private String validityDateType;
		private String validityDateValue;
		public int getActualAmount() {
			return actualAmount;
		}
		public void setActualAmount(int actualAmount) {
			this.actualAmount = actualAmount;
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
		public float getDiscount() {
			return discount;
		}
		public void setDiscount(float discount) {
			this.discount = discount;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getParentProductCode() {
			return parentProductCode;
		}
		public void setParentProductCode(String parentProductCode) {
			this.parentProductCode = parentProductCode;
		}
		public String getProductCode() {
			return productCode;
		}
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public int getSchoolId() {
			return schoolId;
		}
		public void setSchoolId(int schoolId) {
			this.schoolId = schoolId;
		}
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		public String getValidityDateType() {
			return validityDateType;
		}
		public void setValidityDateType(String validityDateType) {
			this.validityDateType = validityDateType;
		}
		public String getValidityDateValue() {
			return validityDateValue;
		}
		public void setValidityDateValue(String validityDateValue) {
			this.validityDateValue = validityDateValue;
		}
	}
	private String curr_StudentId;
	private String curr_stuClassId;
	private String curr_stuSchool_id;
	private String curr_stuUsername;
	public String getCurr_StudentId() {
		return curr_StudentId;
	}
	public void setCurr_StudentId(String curr_StudentId) {
		this.curr_StudentId = curr_StudentId;
	}
	public String getCurr_stuClassId() {
		return curr_stuClassId;
	}
	public void setCurr_stuClassId(String curr_stuClassId) {
		this.curr_stuClassId = curr_stuClassId;
	}
	public String getCurr_stuSchool_id() {
		return curr_stuSchool_id;
	}
	public void setCurr_stuSchool_id(String curr_stuSchool_id) {
		this.curr_stuSchool_id = curr_stuSchool_id;
	}
	public String getCurr_stuUsername() {
		return curr_stuUsername;
	}
	public void setCurr_stuUsername(String curr_stuUsername) {
		this.curr_stuUsername = curr_stuUsername;
	}
	public List<StudentBaseVipServices> getStudentBaseVipServices() {
		return studentBaseVipServices;
	}
	public void setStudentBaseVipServices(List<StudentBaseVipServices> studentBaseVipServices) {
		this.studentBaseVipServices = studentBaseVipServices;
	}
	public List<PackageRoduct> getPackageRoductList() {
		return packageRoductList;
	}
	public void setPackageRoductList(List<PackageRoduct> packageRoductList) {
		this.packageRoductList = packageRoductList;
	}
	public List<ParentStudents> getParentStudents() {
		return parentStudents;
	}
	public void setParentStudents(List<ParentStudents> parentStudents) {
		this.parentStudents = parentStudents;
	}
	public List<SchoolProducts> getSchoolProducts() {
		return schoolProducts;
	}
	public void setSchoolProducts(List<SchoolProducts> schoolProducts) {
		this.schoolProducts = schoolProducts;
	}
	private List<StudentBaseVipServices> studentBaseVipServices;
	private List<PackageRoduct> packageRoductList;
	private List<ParentStudents> parentStudents;
	private List<SchoolProducts> schoolProducts;
	@Override
	public String toString() {
		return "ChargeProduct [curr_StudentId=" + curr_StudentId
				+ ", curr_stuClassId=" + curr_stuClassId
				+ ", curr_stuSchool_id=" + curr_stuSchool_id
				+ ", curr_stuUsername=" + curr_stuUsername
				+ ", baseVipServices=" + studentBaseVipServices
				+ ", packageRoductList=" + packageRoductList
				+ ", parentStudents=" + parentStudents + ", schoolProducts="
				+ schoolProducts + "]";
	}
	
	
}