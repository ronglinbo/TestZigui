package com.wcyc.zigui2.newapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.Role;



public class RoleListAdapter extends BaseAdapter{

	private List<Role> roles;
	private Context context;
	private int selectdPos;
	
	public RoleListAdapter(Context context,List<Role> roles){
		this.context = context;
		this.roles = roles;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(roles != null)
			return roles.size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return roles.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.roles_item, arg2, false);
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.school = (TextView) arg1.findViewById(R.id.school);
			holder.checked = (ImageView) arg1.findViewById(R.id.checked);
			
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}

		if(arg0 == selectdPos){
			holder.checked.setVisibility(View.VISIBLE);
		}else{
			holder.checked.setVisibility(View.GONE);
		}

		holder.name.setText(roles.get(arg0).name);
		holder.school.setText(roles.get(arg0).school);
		return arg1;
	}
	
	public void selectdItem(int pos){
		selectdPos = pos;
	}
	
	private class ViewHolder{
		ImageView checked;
		TextView name;
		TextView school;
	}
		
}
	
	
