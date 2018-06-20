package com.wcyc.zigui2.newapp.videoutils;

public class VideoModelBean {
	private String name;//视频名字
	private String path;//视频路径  通过文件路径可计算文件大小
	private long length;//视频时长
	public VideoModelBean() {
		super();
	}
	public VideoModelBean(String name, String path, long length) {
		super();
		this.name = name;
		this.path = path;
		this.length = length;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	
	
	
}
