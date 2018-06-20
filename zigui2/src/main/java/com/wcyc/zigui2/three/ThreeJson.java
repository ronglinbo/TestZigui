package com.wcyc.zigui2.three;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ThreeJson implements Serializable {
	
	public Map<String, List<Cityinfo>> kcmap ;
	
	public Map<String, List<Cityinfo>> lbmap ;

	public List<Cityinfo> kqall ;

	public Map<String, List<Cityinfo>> getKcmap() {
		return kcmap;
	}

	public void setKcmap(Map<String, List<Cityinfo>> kcmap) {
		this.kcmap = kcmap;
	}

	public Map<String, List<Cityinfo>> getLbmap() {
		return lbmap;
	}

	public void setLbmap(Map<String, List<Cityinfo>> lbmap) {
		this.lbmap = lbmap;
	}

	public List<Cityinfo> getKqall() {
		return kqall;
	}

	public void setKqall(List<Cityinfo> kqall) {
		this.kqall = kqall;
	}

	public ThreeJson(Map<String, List<Cityinfo>> kcmap,
			Map<String, List<Cityinfo>> lbmap, List<Cityinfo> kqall) {
		super();
		this.kcmap = kcmap;
		this.lbmap = lbmap;
		this.kqall = kqall;
	}

	@Override
	public String toString() {
		return "ThreeJson [kcmap=" + kcmap + ", lbmap=" + lbmap + ", kqall="
				+ kqall + "]";
	}
	
	
}
