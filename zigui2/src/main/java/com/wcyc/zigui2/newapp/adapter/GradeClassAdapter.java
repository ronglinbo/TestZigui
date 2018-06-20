package com.wcyc.zigui2.newapp.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.wcyc.zigui2.R;


import com.wcyc.zigui2.newapp.bean.AllGradeClass;
import com.wcyc.zigui2.newapp.bean.ClassList;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.GradeMap;
import com.wcyc.zigui2.widget.RoundImageView;


public class GradeClassAdapter extends BaseExpandableListAdapter{
	private List<GradeMap> gradeList;
//	private List<List<ClassMap>> classList;
	private AllGradeClass allGradeClass;
	private Context mcontext;
	public GradeClassAdapter(Context mContext,AllGradeClass allGradeClass) {
		// TODO Auto-generated constructor stub
		this.allGradeClass = allGradeClass;
		gradeList = allGradeClass.getGradeMapList();
		mcontext = mContext;
	}

	@Override
	public Object getChild(int groupPos, int childPos) {
		// TODO Auto-generated method stub
		return allGradeClass.getGradeMapList().get(groupPos).getClassMapList().get(childPos);
//		return classList.get(groupPos).get(childPos);
	}

	@Override
	public long getChildId(int groupPos, int childPos) {
		// TODO Auto-generated method stub
		return childPos;
	}

	@Override
	public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		ClassMap child = allGradeClass.getGradeMapList().get(groupPos).getClassMapList().get(childPos);
		//ClassMap list = classList.get(groupPos).get(childPos);
		String string = child.getName();
		CheckBox check = null;
		
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.list_dept_item, null);
			viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.choose);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.checkbox.setText(string);
		
		viewHolder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1 == true){
					
				}else{
					
				}
			}
			
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPos) {
		// TODO Auto-generated method stub
		List<ClassMap> list = allGradeClass.getGradeMapList().get(groupPos).getClassMapList();
		if(list != null)
			return list.size();
		else return 0;
		//return classList.get(groupPos).size();
	}

	@Override
	public Object getGroup(int groupPos) {
		// TODO Auto-generated method stub
		return gradeList.get(groupPos);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		if(gradeList != null)
			return gradeList.size();
		return 0;
	}

	@Override
	public long getGroupId(int groupPos) {
		// TODO Auto-generated method stub
		return groupPos;
	}

	@Override
	public View getGroupView(int groupPos, boolean isExpanded, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		String string = gradeList.get(groupPos).getName();
		convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.list_grade_item, null);
		holder.checkbox = (CheckBox) convertView.findViewById(R.id.choose);
		holder.checkbox.setText(string);
		return getView(string);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private View getView(String s){
		AbsListView.LayoutParams lp = 
				new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,80);
		TextView text = new TextView(mcontext);
		text.setLayoutParams(lp);
		text.setGravity(Gravity.CLIP_VERTICAL | Gravity.LEFT);
		text.setPadding(80, 20, 0, 0);
		text.setText(s);
		return text;
	}
	
	private static class ViewHolder {
		CheckBox checkbox;
		TextView num;
	}
	
}
	
	
