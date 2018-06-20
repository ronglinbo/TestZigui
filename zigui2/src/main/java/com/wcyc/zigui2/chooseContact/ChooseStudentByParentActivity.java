/*
 * 文 件 名:ChooseStudentByClassAdminActivity.java
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;

import com.wcyc.zigui2.core.BaseActivity;

import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.ClassStudent;

import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;

import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

//家长发邮件选择本班学生
public class ChooseStudentByParentActivity extends BaseActivity {
	private TextView cancel,enter;
	private ListView listView;
	private ClassStudent classStudent;
	private List<Student> choosedStudentList;//已选择学生
	private List<Student> list;
	private String classId;//孩子所在班的班级信息
	private Map<Integer,Boolean> isChecked = new HashMap<Integer,Boolean>();
	private ListAdapter adapter;
	private static final int GET_STUDENT_LIST = 2;
	private int i = 0;
	public static final int SEARCH_STUDENT = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_select_student);
		parseIntent();
		ListToMap();
		initView();
		initEvent();
		classId = DataUtil.getChildClassId();
		if(classId != null){//只发本班学生
			getStudentList(classId);
		}
	}
	
	private void ListToMap(){
		if(choosedStudentList != null){
			for(Student student:choosedStudentList){
				isChecked.put(student.getId(), true);
			}
		}
	}
	private void parseIntent(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		choosedStudentList = (List<Student>) bundle.getSerializable("choosedStudentList");
//		isChecked = (Map<Integer, Boolean>) bundle.getSerializable("isSelected");
	}
	
	private void initView(){
		findViewById(R.id.ll_choose_cancel).setVisibility(View.GONE);
		cancel = (TextView) findViewById(R.id.title2_off);
		enter = (TextView) findViewById(R.id.title2_ok);
		listView = (ListView) findViewById(R.id.contact_list_view);
		TextView title = (TextView) findViewById(R.id.new_content);
		title.setText("选择学生");
	}
	
	private void initEvent(){
		
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
		enter.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				returnStudent();
			}
			
		});
	}

	private List<Student> getChoosedStudent(){
		List<Student> choosedlist = new ArrayList<Student>();
		for(Student item:list){
			int id = item.getId();
			if(isChecked.containsKey(id)) {
				choosedlist.add(item);
			}
		}
		return choosedlist;
	}

	private void returnStudent(){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();

		bundle.putSerializable("studentList", (Serializable)getChoosedStudent());
		intent.putExtras(bundle);

		setResult(RESULT_OK,intent);
		finish();
	}
	

	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		switch(action){
		case GET_STUDENT_LIST:
			parseStudentList(data);
			break;
		}
	}
		
	public void getStudentList(String classId){
		JSONObject json = new JSONObject();
		try {
			json.put("classId", classId);
			queryPost(Constants.GET_STUDENT_LIST,json);
			System.out.println("getStudentList:"+json);
			action = GET_STUDENT_LIST;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseStudentList(String data){
		System.out.println("parseStudentList:"+data);
		classStudent = JsonUtils.fromJson(data, ClassStudent.class);
		list = classStudent.getStudentList();
		adapter = new ListAdapter();
		listView.setAdapter(adapter);
	}
	
	private class ListAdapter extends BaseAdapter{


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(list != null)
				return list.size();
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final ViewHolder viewholder;
			if (arg1 == null) {
				viewholder = new ViewHolder();
				arg1 = LayoutInflater.from(ChooseStudentByParentActivity.this)
						.inflate(R.layout.list_dept_item, null);
				viewholder.choose = (CheckBox) arg1.findViewById(R.id.choose);
				viewholder.name = (TextView) arg1.findViewById(R.id.name);
				viewholder.view = arg1.findViewById(R.id.dept_info);
				arg1.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) arg1.getTag();
			}
			final Student student = list.get(arg0);
			final int id = student.getId();
			if (!DataUtil.isNullorEmpty(student.getName())) {
				viewholder.name.setText(student.getName());
			}
			viewholder.choose.setTag(student);

			if (isChecked != null && isChecked.containsKey(id)) {
				viewholder.choose.setChecked(isChecked.get(id));
			} else {
				viewholder.choose.setChecked(false);
			}

			viewholder.view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				if (isChecked != null) {
					boolean checked = false;
					if (isChecked.containsKey(id)) {
						checked = isChecked.get(id);
					}
					viewholder.choose.setChecked(!checked);
					if (checked == false) {
//						choosedStudentList.add(student);
						isChecked.put(id, !checked);
					} else {
//						choosedStudentList.remove(student);
						isChecked.remove(id);
					}
				}
				}

			});
			return arg1;
		}
	}
	
	private static class ViewHolder{
		CheckBox  choose;
		View view;
		TextView name;
	}
}
