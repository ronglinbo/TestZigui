package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;

public class Role implements Serializable{
	public boolean ischecked;
	public String name;
	public String school;
	
	public boolean isIschecked() {
		return ischecked;
	}
	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	
	
}