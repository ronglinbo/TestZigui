/**   
 * 文件名：com.wcyc.zigui.adapter.SpinnerPopAdapter.java   
 *   
 * 版本信息：   
 * 日期：2014年10月14日 上午10:55:55  
 * Copyright 2014-2015 惟楚有有才网络股份有限公司    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.newapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.Classes;

//2014年10月14日 上午10:55:55
/**
 * 聊天界面班级切换适配器.
 * @author 王登辉
 * @version 1.01
 */

public class ContactSpinnerPopAdapter extends BaseAdapter {

//	private int userType;//身份判定  2：老师，3：家长
	private Context context;
//	protected Classes classBack;
	private List<Classes> classes;//老师身份
//	private Child childBack;
//	private List<Child> childs;//家长身份
	private Classes clas;
	/**
	 * 记录选中的是哪个班级
	 */
	private int select;


	/**
	 * 创建一个新的实例 SpinnerPopAdapter.
	 * @param context 内容
	 */
	public ContactSpinnerPopAdapter( Context context) {
		super();
		this.context = context;
	}
	
	/**
	 * 设置数据给adapter.
	 * @param classList 班级列表
	 */
	public void setClassBean(List<Classes> classList) {
		this.classes = classList;
	}
	
	
	/*
	 *    设置小孩集合
	 * @param childs  void
	 */
//	public void setChildBean(List<Child> childs,int userType) {
//		this.childs = childs;
//		this.userType = userType;
//	}

	
	@Override
	public int getCount() {
		return classes == null ?0 :classes.size();
	}

	@Override
	public Object getItem(int position) {
		return classes == null ?0 :classes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return classes == null ?0 :position;
	}

	/**
	 * 设置选择的是哪个班级
	 * @param select
	 */
	public void setSelect(int select){
		this.select = select;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.spinner_layout_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.spinner_lv_iv);
			holder.className = (TextView) convertView
					.findViewById(R.id.spinner_lv_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == select){
			convertView.setBackgroundResource(R.color.backgroud_lightyellow);
		}else{
			convertView.setBackground(null);
		}
		setData(holder, position);
		return convertView;
	}

	/**   
	 * 设置数据
	 */

	private void setData(ViewHolder holder , int position) {
		clas = classes.get(position);
		holder.className.setText(clas.getGradeName()+clas.getClassName()  + "通讯录");
		if("1".equals(clas.getIsAdviser()))
		{
			holder.imageView.setVisibility(View.VISIBLE);
		}else
		{
			holder.imageView.setVisibility(View.INVISIBLE);
		}
	}
	
	class ViewHolder {
		ImageView imageView;
		TextView className;
	}
	
	
}
