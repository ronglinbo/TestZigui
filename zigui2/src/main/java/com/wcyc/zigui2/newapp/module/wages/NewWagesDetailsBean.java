package com.wcyc.zigui2.newapp.module.wages;


/**
 * 工资详情bean
 * 
 * @author 郑国栋
 * 2016-7-14
 * @version 2.0
 */
public class NewWagesDetailsBean {

	private String id;//工资详情id
	private String key;//工资详情 key 名称 如 应发合计   实发合计  奖金  补贴等
 	private String sequence;//工资详情 描述 字段  2  3  4 5  等
	private String value;//工资详情  的值   如  2000     5000
	
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	@Override
	public String toString() {
		return "NewWagesDetailsBean [id=" + id + ", key=" + key + ", sequence="
				+ sequence + ", value=" + value + "]";
	}
	
	
	
	
}
