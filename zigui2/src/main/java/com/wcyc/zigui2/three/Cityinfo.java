package com.wcyc.zigui2.three;

import java.io.Serializable;

public class Cityinfo implements Serializable {
	
	public Cityinfo(){
		
	}

	public Cityinfo(String id,String name){
		this.id = id;
		this.city_name = name;
	}
	private String id;
	private String city_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	@Override
	public String toString() {
		return "Cityinfo [id=" + id + ", city_name=" + city_name + "]";
	}

}
