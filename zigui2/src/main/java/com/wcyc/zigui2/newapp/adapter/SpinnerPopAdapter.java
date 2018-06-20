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
 * 点击切换班级 弹出的pop适配器.
 * @author 王登辉
 * @version 1.01
 */
public class SpinnerPopAdapter extends BaseAdapter {

	private List<Classes> classes;
	private Context context;
	private Classes c;
//	private Handler handler;
	protected Classes classBack;

	/**
	 * 记录选中的是哪个班级
	 */
	private int select;
	/**
	 * 创建一个新的实例 SpinnerPopAdapter.
	 * 
	 * @param context 正文
	 */
	public SpinnerPopAdapter( Context context,List<Classes> classes) {
		super();
		this.classes = classes;
		this.context = context;
//		this.handler = handler;
	}
	/**
	 * 设置数据给adapter.
	 * @param classList 班级列表
	 */
	public void setData(List<Classes> classList) {
		this.classes = classList;
	}
	@Override
	public int getCount() {
		return classes == null ? 0 : classes.size();
	}

	@Override
	public Object getItem(int position) {
		return classes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return classes == null ? 0 : position;
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
		c = classes.get(position);
		if("1".equals(c.getIsAdviser()))
		{
			holder.imageView.setVisibility(View.VISIBLE);
		}else
		{
			holder.imageView.setVisibility(View.INVISIBLE);
		}
		holder.className.setText(c.getGradeName()+c.getClassName());
//		holder.className.setTag(c);
//		holder.className.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				classBack = (Classes) v.getTag();
//				Message message = Message.obtain();
//				message.obj = classBack;
//				message.what = 1;
//				handler.sendMessage(message);
//			}
//		});
		if(position == select){
			convertView.setBackgroundResource(R.color.backgroud_lightyellow); 
		}else{
			convertView.setBackgroundResource(R.color.transparent);
		} 
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView className;
	}
	
	
}
