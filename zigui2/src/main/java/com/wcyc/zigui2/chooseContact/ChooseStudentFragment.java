/*
 * 文 件 名:ChooseTeacherFragment.java
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
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.activity.SearchContactActivity;
import com.wcyc.zigui2.newapp.bean.AllGradeClass;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.ClassStudentList;
import com.wcyc.zigui2.newapp.bean.GradeMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
//选择学生fragment
public class ChooseStudentFragment extends Fragment{
	private ClassStudentList classStudentList;
	private View view;
	private ExpandableListView expandList;
	private List<Student> chooseStudentList;//选择的学生
	private ClassStudent classStudent;
	private List<Student> studentList;
	private Map<Integer,Boolean> isChecked = new HashMap<Integer,Boolean>();
	private Button searchButton;
	private Context mContext;
	public static final int SEARCH_STUDENT = 100;
	private ClassStudentAdapter adapter;
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle){
		view = inflater.inflate(R.layout.new_student_list, null);
		mContext = getActivity();
		ListToMap();
		initView();
		initData();
		return view;
	}
	public ClassStudentAdapter getAdapter(){
		return adapter;
	}
	public ChooseStudentFragment(){

	}

//	public ChooseStudentFragment(ClassStudentList classStudentList,List<Student> choosedStudent){
//		this.classStudentList = classStudentList;
//		this.chooseStudentList = choosedStudent;
//	}
	
	private void ListToMap(){
		if(chooseStudentList != null){
			for(Student student:chooseStudentList){
				isChecked.put(student.getId(), true);
			}
		}
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null){
			classStudentList = (ClassStudentList)bundle.getSerializable("list");
			chooseStudentList = (List<Student>)bundle.getSerializable("choosed");
		}
	}
	public static Fragment newInstance(int index,ClassStudentList classStudentList,List<Student> choosedStudent) {
		// TODO Auto-generated method stub
		Fragment fragment = new ChooseStudentFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		args.putSerializable("list",classStudentList);
		args.putSerializable("choosed",(Serializable) choosedStudent);
		fragment.setArguments(args);
		//fragment.setIndex(index);
		return fragment;
	}
	
	private void initData(){
		
		expandList = (ExpandableListView) view.findViewById(android.R.id.list);
		if(expandList != null){
			adapter = new ClassStudentAdapter(getActivity(),classStudentList);
			expandList.setAdapter(adapter);
		}
	}
	private void initView(){
		
		searchButton = (Button) view.findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putSerializable("classStudentList", classStudentList);
				bundle.putString("type","");//暫時去掉手機号提示

				Intent intent = new Intent();
				intent.setClass(mContext, SearchContactActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, SEARCH_STUDENT);
			}
			
		});
	}
	//接口需要修改
	public class ClassStudentAdapter extends BaseExpandableListAdapter{

		private ClassStudentList classStudentList;
//		Map<Integer,Boolean> isChecked = new HashMap<Integer,Boolean>();
		private Context mcontext;
		public ClassStudentAdapter(Context mContext,ClassStudentList classStudentList) {
			// TODO Auto-generated constructor stub
			this.classStudentList = classStudentList;
//			chooseStudentList = new ArrayList<Student>();
			mcontext = mContext;
		}

		@Override
		public Object getChild(int groupPos, int childPos) {
			// TODO Auto-generated method stub
			return classStudentList.getClassStudent().get(groupPos).getStudentList().get(childPos);
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

			final Student student = classStudentList.getClassStudent().get(groupPos).getStudentList().get(childPos);
			
			String string = student.getName();
			final int id = student.getId();
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
//							chooseStudentList.add(student);
							isChecked.put(id,!checked);
						}else{
//							chooseStudentList.remove(student);
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
			List<Student> list = classStudentList.getClassStudent().get(groupPos).getStudentList();
			if(list != null)
				return list.size();
			else return 0;
			//return classList.get(groupPos).size();
		}

		@Override
		public Object getGroup(int groupPos) {
			// TODO Auto-generated method stub
			return classStudentList.getClassStudent().get(groupPos);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			if(classStudentList.getClassStudent() != null)
				return classStudentList.getClassStudent().size();
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
			int choose = 0;
			int size = 0;
			final ClassStudent temp = classStudentList.getClassStudent().get(groupPos);
			if(temp != null){
				List<Student> list = temp.getStudentList();
				if(list != null) size = list.size();
				choose = getCheckedSize(isChecked,list);
			}
			String string = temp.getGradeName() + temp.getName();
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = ((Activity) mcontext).getLayoutInflater().inflate(R.layout.list_grade_item, null);
				holder.checkbox = (CheckBox) convertView.findViewById(R.id.choose);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.num = (TextView) convertView.findViewById(R.id.num);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.num.setText(choose+"/"+size);
			holder.name.setText(string);
//			holder.checkbox.setText(string);

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
	
	private int getCheckedSize(Map<Integer,Boolean> isChecked,List<Student> list){
		int i = 0;
		if(list != null){
			for(Student student:list){
				if(isChecked.containsKey(student.getId())){
					i++;
				}
			}
		}
		return i;
	}
	
	public List<Student> getChooseStudentList(){
		if(chooseStudentList != null){
			chooseStudentList.clear();
		}else{
			chooseStudentList = new ArrayList<Student>();
		}
		List<ClassStudent> classStudent = classStudentList.getClassStudent();
		if(classStudent != null){
			for(ClassStudent temp:classStudent){
				List<Student> list = temp.getStudentList();
				if(list != null){
					for(Student stu:list){
						int id = stu.getId();
						if(isChecked.containsKey(id)){
							chooseStudentList.add(stu);
						}
					}
				}
			}
		}
		return chooseStudentList;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); 
        if(SEARCH_STUDENT == requestCode && resultCode == -1){
        	int id = data.getIntExtra("studentId", 0);
        	System.out.println("id:"+id);
        	isChecked.put(id,true);
        	adapter.notifyDataSetChanged();
        }
    }
}