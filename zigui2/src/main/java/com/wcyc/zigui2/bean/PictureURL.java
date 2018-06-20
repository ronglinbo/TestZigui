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
 * 此类描述的是:图片列表
 * 
 * by ph
 * 
 * msgPicList List 图片列表 pictureURL string 图片url 今后可以扩，目前只有一个属性
 * thumbnailPictureURL  缩列图路径
 */

public class PictureURL implements Serializable {
	private static final long serialVersionUID = 6258581484538563724L;

	private String pictureURL;
	private String thumbnailPictureURL;

	/**
	 * thumbnailPictureURL
	 * 
	 * @return the thumbnailPictureURL
	 * @since CodingExample Ver(编码范例查看) 1.0
	 */

	public String getThumbnailPictureURL() {
		return thumbnailPictureURL;
	}

	/**
	 * @param thumbnailPictureURL
	 *            the thumbnailPictureURL to set
	 */

	public void setThumbnailPictureURL(String thumbnailPictureURL) {
		this.thumbnailPictureURL = thumbnailPictureURL;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

}
