/*
* 文 件 名:ImageBean.java
* 创 建 人： 姜韵雯
* 日    期： 2014-10-6
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.imageselect;

//2014-10-6
/**
 * 图片选择器的文件夹类.
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public class ImageBean{
	/**
	 * 文件夹的第一张图片路径
	 */
	private String topImagePath;
	/**
	 * 文件夹名
	 */
	private String folderName; 
	/**
	 * 文件夹中的图片数
	 */
	private int imageCounts;
	/**
	 * 父路径
	 */
	private String  fa_filepath;
	
	public String getTopImagePath() {
		return topImagePath;
	}
	
	public void setTopImagePath(String topImagePath) {
		this.topImagePath = topImagePath;
	}
	
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	public int getImageCounts() {
		return imageCounts;
	}
	
	public void setImageCounts(int imageCounts) {
		this.imageCounts = imageCounts;
	}
	
	public String getFa_filepath() {
		return fa_filepath;
	}
	
	public void setFa_filepath(String fa_filepath) {
		this.fa_filepath = fa_filepath;
	}
	
}
