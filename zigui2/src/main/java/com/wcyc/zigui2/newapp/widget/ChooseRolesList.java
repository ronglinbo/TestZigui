/*
 * 文 件 名:ChooseRolesList.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-2
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.widget;


import java.util.List;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.color;

import com.wcyc.zigui2.newapp.bean.Role;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.utils.Constants;


import android.app.Activity;
import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ChooseRolesList extends PopupWindow{
	private View view;
	public ListView list;
//	private List<Role> roles;
	private List<UserType> users;
	private String name;
	private Context context;
	private ListAdapter adapter;
	
	
	public ChooseRolesList(Activity context,List<UserType> user,String name){
		super(context);
		this.users = user;
		this.name = name;
		this.context = context;
		LayoutInflater inflater = context.getLayoutInflater();
		view = inflater.inflate(R.layout.choose_roles_list, null);
		list = (ListView) view.findViewById(R.id.roles_list);
		adapter = new ListAdapter();
		list.setAdapter(adapter);
		this.setContentView(view);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		 ColorDrawable dw = new ColorDrawable(0xb0000000);
	  this.setBackgroundDrawable(dw);
	}
	
	public ListAdapter GetAdapter(){
		return adapter;
	}
	
	public class ListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
//			System.out.println("size:"+roles.size());
			if(users != null)
				return users.size();
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return users.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub

			ViewHolder holder;
			View view = null;
			if(arg1 == null){
				holder = new ViewHolder();
				arg1 = LayoutInflater.from(context).inflate(R.layout.roles_item, arg2, false);
				view = arg1.findViewById(R.id.role_item);
				holder.name = (TextView) arg1.findViewById(R.id.name);
				holder.school = (TextView) arg1.findViewById(R.id.school);
				holder.checked = (ImageView) arg1.findViewById(R.id.check);
				
				arg1.setTag(holder);
			}else{
				holder = (ViewHolder) arg1.getTag();
			}
			UserType user = users.get(arg0);
			if(Constants.TEACHER_STR_TYPE.equals(user.getUserType())){
				holder.name.setText(name+"（教职工）");
				holder.school.setText(user.getSchoolName());
			}else{
				holder.name.setText(user.getChildName()+user.getRelationTypeName());
				holder.school.setText(user.getSchoolName()+user.getGradeName()+user.getClassName());
			}
			if(user.getIschecked()){
				holder.checked.setVisibility(View.VISIBLE);
				holder.name.setTextColor(context.getResources().getColor(color.font_darkblue));
				holder.school.setTextColor(context.getResources().getColor(color.font_darkblue));
				if(view != null)
					view.setBackgroundColor(context.getResources().getColor(color.background_color));
			}else{
				holder.name.setTextColor(context.getResources().getColor(color.font_color));
				holder.school.setTextColor(context.getResources().getColor(color.font_color));
				holder.checked.setVisibility(View.INVISIBLE);
			}
			return arg1;
		}
		
	}

	private class ViewHolder{
		ImageView checked;
		TextView name;
		TextView school;
	}
}