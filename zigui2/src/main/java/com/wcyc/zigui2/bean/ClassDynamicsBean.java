/**   
 * 文件名：com.example.zigui.bean.Stu.java   
 *   
 * 版本信息：   
 * 日期：2015年12月9日 下午3:02:51  
 * Copyright 惟楚有材 Corporation 2015    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.ImageUtils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;


//2015-15-09 15:13:51
/**
 * 班级动态实体类  
 * @author yytan
 * @version 
 */
public class ClassDynamicsBean {

	/**
	 * 班级动态id
	 */
	private String classesInteractionId;
	/**
	 * 评论list
	 */
	private List<PointBean> commentMapList;

	/**
	 * 缩略图地址
	 */
	private String slt;
	/**
	 * 大图片地址
	 */
	private String pic;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 发布时间  "09月22日 16:43"
	 */
	private String time;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 点赞数
	 */
	private String goodNum;
	/**
	 * "一年级国学班" 班级名字
	 */
	private String className;
	/**
	 * "false" 是否点赞
	 */
	private String goodFlag;
	/**
	 * 评论条数
	 */
	private String pointNum;
	/**
	 * 头像 .jpg
	 */
	private String headPortrait;
	/**
	 * 点赞人的名字
	 */
	private String goodName;
	/**
	 * "true"  是否显示“删除”
	 */
	private String delFlag;
	
	private String[] urls;//small pic

	private String[] pics;//big pic
	
	private ArrayList<Bitmap> sltList;
	
//	private Map<String,Object> mBitmapMap = new HashMap<String ,Object>();
	
	public String getClassesInteractionId() {
		return classesInteractionId;
	}
	public void setClassesInteractionId(String classesInteractionId) {
		this.classesInteractionId = classesInteractionId;
	}
	
	public List<PointBean> getCommentMapList() {
		return commentMapList;
	}
	public void setCommentMapList(List<PointBean> commentMapList) {
		this.commentMapList = commentMapList;
	}
	public String getSlt() {
		return slt;
	}
	public void setSlt(String slt) {
		this.slt = slt;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(String goodNum) {
		this.goodNum = goodNum;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getGoodFlag() {
		return goodFlag;
	}
	public void setGoodFlag(String goodFlag) {
		this.goodFlag = goodFlag;
	}
	public String getPointNum() {
		return pointNum;
	}
	public void setPointNum(String pointNum) {
		this.pointNum = pointNum;
	}
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public String[] GetURLs(){
		if(!TextUtils.isEmpty(slt)){
			urls = slt.split(",");
			//System.out.println("urls: "+urls);
			return urls;
		}
		return null;
	}

	public String[] GetPICs(){
		if(!TextUtils.isEmpty(pic)){
			pics = pic.split(",");
			//System.out.println("urls: "+urls);
			return pics;
		}
		return null;
	}
	
//	public void setBitmapMap(Map<String,Object> mBitmapMap){
//		this.mBitmapMap = mBitmapMap;
//	}
//	
//	public void putBitmapMap(String name,Bitmap bitmap){
//		mBitmapMap.put(name,bitmap);
//	}
//	
//	public Map<String,Object> getBitmapmap(){
//		return mBitmapMap;
//	}
	
	public ArrayList<Bitmap> getBitmapsFromServer(){
		sltList = new ArrayList<Bitmap>();
		if(GetURLs() != null){
			Bitmap bm;
			
			for(int i = 0; i < urls.length; i++){
				bm = ImageUtils.getHttpBitmap(Constants.BASE_URL+"/"+urls[i]);
				System.out.println("bm: "+bm);
				sltList.add(bm);
			}
			return sltList;
		}
		return null;
	}
	
	public ClassDynamicsBean(String classesInteractionId,
			List<PointBean> commentMapList, String slt, String pic,
			String content, String time, String name, String goodNum,
			String className, String goodFlag, String pointNum,
			String headPortrait, String goodName, String delFlag,
			String[] urls, ArrayList<Bitmap> sltList) {
		super();
		this.classesInteractionId = classesInteractionId;
		this.commentMapList = commentMapList;
		this.slt = slt;
		this.pic = pic;
		this.content = content;
		this.time = time;
		this.name = name;
		this.goodNum = goodNum;
		this.className = className;
		this.goodFlag = goodFlag;
		this.pointNum = pointNum;
		this.headPortrait = headPortrait;
		this.goodName = goodName;
		this.delFlag = delFlag;
		this.urls = urls;
		this.sltList = sltList;
	}
	public ClassDynamicsBean() {
		super();
	}
	@Override
	public String toString() {
		return "ClassDynamicsBean [classesInteractionId="
				+ classesInteractionId + ", commentMapList=" + commentMapList
				+ ", slt=" + slt + ", pic=" + pic + ", content=" + content
				+ ", time=" + time + ", name=" + name + ", goodNum=" + goodNum
				+ ", className=" + className + ", goodFlag=" + goodFlag
				+ ", pointNum=" + pointNum + ", headPortrait=" + headPortrait
				+ ", goodName=" + goodName + ", delFlag=" + delFlag + ", urls="
				+ Arrays.toString(urls) + ", sltList=" + sltList + "]";
	}
}
