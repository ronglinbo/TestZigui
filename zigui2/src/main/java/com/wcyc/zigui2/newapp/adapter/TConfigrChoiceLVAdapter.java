/**   
 * 文件名：com.wcyc.zigui.adapter.TConfigrChoiceLVAdapter.java   
 *   
 * 版本信息：   
 * 日期：2014年10月15日 下午3:33:08  
 * Copyright 2014-2015 惟楚有有才网络股份有限公司
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.newapp.adapter;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.Classes;

//2014年10月15日 下午3:33:08
/**
 * 老师端 发布消息选择班级适配器.
 * @author 王登辉
 * @version 1.01
 */

public class TConfigrChoiceLVAdapter extends BaseAdapter {

	private List<Classes> classes;
	private List<Classes> beChoiceClas ;
	private Context context;
	private Classes c;
	// 用来控制CheckBox的选中状况
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();

	@Override
	public int getCount() {
		return null == classes ? 0 : classes.size();
	}

	/**
	 * 创建一个新的实例 TConfigrChoiceLVAdapter.
	 * 
	 * @param classes
	 * @param context
	 */

	public TConfigrChoiceLVAdapter(List<Classes> classes, Context context,List<Classes> beChoiceClas) {
		super();
		this.classes = classes;
		this.context = context;
		this.beChoiceClas =beChoiceClas;
		initDate();
	}
	/**
	 * 初始化isSelected的数据
	 */
	private void initDate() {
		int s = (null == beChoiceClas? 0:beChoiceClas.size());
		int size = (null == classes ? 0 : classes.size());
		for (int i = 0; i < size; i++) {
			getIsSelected().put(i, false);
		}
		if(0 != classes.size()&&0 !=s)
		{
			for (int i = 0; i < size; i++) {
				String claID = classes.get(i).getClassID();
				for (int j = 0; j < s; j++) {
					String beChoiceClaID = beChoiceClas.get(j).getClassID();
					if(claID.equals(beChoiceClaID))
					{
							getIsSelected().put(i, true);
					}
				}
			}
		}
		
	}

	@Override
	public Object getItem(int position) {
		return null == classes ? 0 : classes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return null == classes ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHorld horld;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.teacher_choiceclass_item, null);
			horld = new ViewHorld();
			horld.className = (TextView) convertView.findViewById(R.id.item_tv);
			horld.icon = (ImageView) convertView.findViewById(R.id.item_icon);
			horld.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
			convertView.setTag(horld);
		} else {
			horld = (ViewHorld) convertView.getTag();
		}
		c = classes.get(position);
		horld.cb.setChecked(getIsSelected().get(position));
		horld.className.setText(c.getGradeName()+c.getClassName());
		if ("1".equals(c.getIsAdviser())) {
			horld.icon.setVisibility(View.VISIBLE);
		}else
		{
			horld.icon.setVisibility(View.GONE);
		}
		return convertView;
	}

	public class ViewHorld {
		public ImageView icon;// 班主任头标
		public TextView className;// 班级名称
		public CheckBox cb;

	}
	public HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

}
