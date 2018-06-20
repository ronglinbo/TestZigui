/*
 * 文 件 名:ChooseClassFragment.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-08
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.chooseContact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wcyc.zigui2.R;

import com.wcyc.zigui2.newapp.bean.AllGradeClass;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.GradeMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
//发送通知添加学生选择班级fragment
public class ChooseClassFragment extends Fragment{
	
	private AllGradeClass allGradeClass;
	private GradeClassAdapter adapter;
	private ExpandableListView expandList;
	private View view;
	private CheckBox allChoose;
	private List<GradeMap> chooseList;//回选的list
	private List<ClassMap> chooseClassList;
	private Map<Integer,Boolean> isChecked = new HashMap<Integer,Boolean>();

	public ChooseClassFragment(){
		// Required empty public constructor
	}

//	public ChooseClassFragment(AllGradeClass allGradeClass,List<ClassMap> choosedClass) {
//		// TODO Auto-generated constructor stub
//		this.allGradeClass = allGradeClass;
//		chooseClassList = choosedClass;
//		chooseList = new ArrayList<GradeMap>();
//	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null){
			allGradeClass = (AllGradeClass)bundle.getSerializable("allGrade");
			chooseClassList = (List<ClassMap>)bundle.getSerializable("list");
		}
	}

	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle){
		view = inflater.inflate(R.layout.new_classlist, null);	
		ListToMap();
		initView();
		initChooseAll();
		return view;
	}
	
	public void ListToMap(){
		if(chooseClassList != null){
			for(ClassMap classMap:chooseClassList){
				isChecked.put(classMap.getId(),true);
			}
		}
	}
	
	private void initChooseAll(){
		int count = 0;
		List<GradeMap> list = allGradeClass.getGradeMapList();
		if(list == null) return;
		for(GradeMap gradeMap:list){
			if(gradeMap != null){
				List<ClassMap> classList = gradeMap.getClassMapList();
				if(classList != null)
					count += classList.size();
			}
		}

		if(chooseClassList!= null && chooseClassList.size() == count){
			allChoose.setChecked(true);
		}
	}
	public void chooseAll(boolean checked){
		List<GradeMap> list = allGradeClass.getGradeMapList();
		for(GradeMap gradeMap:list){
			if(gradeMap != null){
				List<ClassMap> classList = gradeMap.getClassMapList();
				if(classList != null){
					for(ClassMap classMap:classList){
						if(checked)
							isChecked.put(classMap.getId(),checked);
						else{
							isChecked.remove(classMap.getId());
						}
					}
				}
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	public static Fragment newInstance(int index,AllGradeClass allGradeClass,List<ClassMap> choosedClass) {
		// TODO Auto-generated method stub
		Fragment fragment = new ChooseClassFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		args.putSerializable("allGrade",allGradeClass);
		args.putSerializable("list", (Serializable) choosedClass);
		fragment.setArguments(args);
		//fragment.setIndex(index);
		return fragment;
	}
	
	private void initView(){
		allChoose = (CheckBox) view.findViewById(R.id.allChoose);
		allChoose.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				chooseAll(arg1);
			}
			
		});
		expandList = (ExpandableListView) view.findViewById(android.R.id.list);
		if(expandList != null){
			adapter = new GradeClassAdapter(getActivity(),allGradeClass);
			expandList.setAdapter(adapter);
		}
	}
		
	public class GradeClassAdapter extends BaseExpandableListAdapter{
		private List<GradeMap> gradeList;
//		private List<List<ClassMap>> classList;
		private AllGradeClass allGradeClass;
		private Context mcontext;
		public GradeClassAdapter(Context mContext,AllGradeClass allGradeClass) {
			// TODO Auto-generated constructor stub
			this.allGradeClass = allGradeClass;
			if(allGradeClass != null){
				gradeList = allGradeClass.getGradeMapList();
			}
			mcontext = mContext;
		}

		@Override
		public Object getChild(int groupPos, int childPos) {
			// TODO Auto-generated method stub
			return allGradeClass.getGradeMapList().get(groupPos).getClassMapList().get(childPos);
//			return classList.get(groupPos).get(childPos);
		}

		@Override
		public long getChildId(int groupPos, int childPos) {
			// TODO Auto-generated method stub
			return childPos;
		}

		@Override
		public View getChildView(int groupPos, final int childPos, boolean isLastChild, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder viewHolder;
			final GradeMap grade = allGradeClass.getGradeMapList().get(groupPos);
			final ClassMap child = grade.getClassMapList().get(childPos);
			//ClassMap list = classList.get(groupPos).get(childPos);
			String string = child.getName();
			final int id = child.getId();
			CheckBox check = null;
			
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.list_dept_item, null);
				viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.choose);
				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				viewHolder.view = convertView.findViewById(R.id.dept_info);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.name.setText(string);
			
			if(isChecked != null && isChecked.containsKey(id)){
				viewHolder.checkbox.setChecked(isChecked.get(id));
			}else{
				viewHolder.checkbox.setChecked(false);
			}

			viewHolder.view.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(isChecked != null){
						boolean checked = false;
						if(isChecked.containsKey(id)){
							checked = isChecked.get(id);
						}

						if(checked == false){
							//chooseClassList.add(child);
							isChecked.put(id,!checked);
						}else{
							//chooseClassList.remove(child);
							isChecked.remove(id);
						}
						viewHolder.checkbox.setChecked(!checked);
						notifyDataSetChanged();
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
			ViewHolder holder;
			int size = 0;
			String string = gradeList.get(groupPos).getName();
			List<ClassMap> list = gradeList.get(groupPos).getClassMapList();
			if(list != null){
				size = list.size();
			}
			int choose = getCheckedSize(isChecked,list);
			if(convertView == null){
				holder = new ViewHolder();
				
				convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.list_grade_item, null);
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.choose);
				holder.num = (TextView) convertView.findViewById(R.id.num);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(holder);
//				holder.checkbox.setText(string);	
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.num.setText(choose+"/"+size);
			holder.name.setText(string);

			return convertView;
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
		
		private class ViewHolder {
			CheckBox checkbox;
			TextView num;
			TextView name;
			View view;
		}
		
	}
	
	private int getCheckedSize(Map<Integer,Boolean> isChecked,List<ClassMap> list){
		int i = 0;
		if(list != null){
			for(ClassMap classItem:list){
				if(isChecked.containsKey(classItem.getId())){
					i++;
				}
			}
		}
		return i;
	}
	
	public List<ClassMap> getChooseList(){
		List<GradeMap> gradeList = allGradeClass.getGradeMapList();
		int i = 0;
		if(chooseClassList != null)
			chooseClassList.clear();
		else{
			chooseClassList = new ArrayList<ClassMap>();
		}
		for(GradeMap map:gradeList){
			List<ClassMap> classlist = map.getClassMapList();
			for(ClassMap classMap:classlist){
				int id = classMap.getId();
				if(isChecked.containsKey(id)){
					chooseClassList.add(classMap);
				}
			}
		}
		return chooseClassList;
	}
}