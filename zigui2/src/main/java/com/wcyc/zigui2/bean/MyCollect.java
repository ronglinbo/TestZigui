package com.wcyc.zigui2.bean;
/**
 * 	入参
参数名	参数类型（长度）	描述
userID	String		用户ID
childID	String		孩子ID
curPage	String	当前页
pageCountNum	String	每页记录数


	出参
参数名	参数类型	描述
Code	Integer	返回代码
200 成功 201 请求失败
enrollListNum	String	消息总数
enrollList	List	列表
coursewareID	String	课件资源Id
coursewareName	String	课件名称
coursewareIconURL	String	图标URL
coursewareTime	String	开课时间
coursewareMaster	String	主讲人
 */
/**
 * @author xfliu
 * @version 1.05
 */
public class MyCollect extends BaseBean{
	private static final long serialVersionUID = 3074125587382579981L;
	private String coursewareID;
    private String coursewareName;
    private String coursewareIconURL;
    private String coursewareTime;
    private int countdownTime;
    private String coursewareMaster;
    private String state;
    private String orderID;
    private String orderTime;
    private int needPeopleNum;
    private int curPeopleNum;
    private String content;
    private String studentName;
	public String getCoursewareID() {
		return coursewareID;
	}
	public void setCoursewareID(String coursewareID) {
		this.coursewareID = coursewareID;
	}
	public String getCoursewareName() {
		return coursewareName;
	}
	public void setCoursewareName(String coursewareName) {
		this.coursewareName = coursewareName;
	}
	public String getCoursewareIconURL() {
		return coursewareIconURL;
	}
	public void setCoursewareIconURL(String coursewareIconURL) {
		this.coursewareIconURL = coursewareIconURL;
	}
	public String getCoursewareTime() {
		return coursewareTime;
	}
	public void setCoursewareTime(String coursewareTime) {
		this.coursewareTime = coursewareTime;
	}
	public int getCountdownTime() {
		return countdownTime;
	}
	public void setCountdownTime(int countdownTime) {
		this.countdownTime = countdownTime;
	}
	public String getCoursewareMaster() {
		return coursewareMaster;
	}
	public void setCoursewareMaster(String coursewareMaster) {
		this.coursewareMaster = coursewareMaster;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public int getNeedPeopleNum() {
		return needPeopleNum;
	}
	public void setNeedPeopleNum(int needPeopleNum) {
		this.needPeopleNum = needPeopleNum;
	}
	public int getCurPeopleNum() {
		return curPeopleNum;
	}
	public void setCurPeopleNum(int curPeopleNum) {
		this.curPeopleNum = curPeopleNum;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
    
    
}
