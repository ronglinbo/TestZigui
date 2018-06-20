/*
 * 文 件 名:ChooseTeacherByParentActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-08-11
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.chooseContact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.AllDeptList;
import com.wcyc.zigui2.newapp.bean.ClassList;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.AllDeptList.CommonGroup;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.ContactGroupMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.DepMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.GradeMap;
import com.wcyc.zigui2.newapp.bean.AllTeacherList.TeacherMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.newapp.fragment.AllMessageFragment;
import com.wcyc.zigui2.newapp.fragment.ContactFragment;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") 
public class ChooseTeacherByParentActivity extends BaseActivity {
	private TextView cancel,enter;
	private Button chooseTeacher;

	private List<TeacherMap> list = new ArrayList<TeacherMap>();
	private static final int GET_ALL_TEACHER = 1;
	private Map<Integer,Boolean> isChecked = new HashMap<Integer,Boolean>();
	private ListAdapter adapter;
	private ListView listView;
	private AllTeacherList allTeacher = new AllTeacherList();
	private List<TeacherMap> chooseTeacherList;
	private List<TeacherMap> choosedTeacher;//回选的list

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.new_select_teacher);
		initData();
		initView();
		initEvent();
		parseIntent();
	}
	
	private void initData(){
		//当前孩子的班级Id
		String classId = DataUtil.getChildClassId();
		//从所有联系人中获取本班的教师
		AllContactListBean allContact = CCApplication.getInstance().getAllContactList();
		List <ClassList> list = allContact.getClassList();
		for(ClassList item :list){
			if(classId.equals(item.getClassID())){
				List<ContactsList> contacts = item.getContactsList();
				getClassTeacher(contacts);
				break;
			}
		}
		
	}
	
	private void getClassTeacher(List<ContactsList> contacts){
		for(ContactsList item:contacts){
			TeacherMap teacher = allTeacher.new TeacherMap();
			try{
				int id = Integer.parseInt(item.getUserId());
				teacher.setId(id);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(Constants.TEACHER_STR_TYPE.equals(item.getUserIdentity())){
				teacher.setName(item.getNickName());
				teacher.setMobile(item.getCellphone());
				teacher.setPicAddress(item.getUserIconURL());
				teacher.setTitle(item.getText());
				list.add(teacher);
			}
		}
	}
	
	private void initView(){
		chooseTeacher = (Button) findViewById(R.id.choose_teacher);
		TextView title = (TextView) findViewById(R.id.new_content);
		title.setText("选择老师");
		cancel = (TextView) findViewById(R.id.title2_off);
		enter = (TextView) findViewById(R.id.title2_ok);
		listView = (ListView) findViewById(R.id.contact_list_view);
	}
	
	private void parseIntent(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			choosedTeacher = (List<TeacherMap>) bundle.getSerializable("teacher");
			listToMap();
		}
		adapter = new ListAdapter();
		listView.setAdapter(adapter);
	}
	
	private void listToMap(){
		if(choosedTeacher != null){
			for(TeacherMap teacher:choosedTeacher){
				isChecked.put(teacher.getId(), true);
			}
		}
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
				returnTeacher();
			}
			
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final TeacherMap teacher = list.get(position);
			final int teacherId = teacher.getId();
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.choose);
			if(isChecked != null){
				boolean checked = false;
				if(isChecked.containsKey(teacherId)){
					checked = isChecked.get(teacherId);
				}
				checkBox.setChecked(!checked);
				if(checked == false){
					isChecked.put(teacherId,!checked);
				}else{
					isChecked.remove(teacherId);
				}
			}
			if(adapter != null)
				adapter.notifyDataSetChanged();
			}
		});
	}

	private List<TeacherMap> getChoosedTeacher(){
		List<TeacherMap> choosedlist = new ArrayList<TeacherMap>();
		for(TeacherMap item:list){
			int id = item.getId();
			if(isChecked.containsKey(id)) {
				choosedlist.add(item);
			}
		}
		return choosedlist;
	}

	//返回结果给调用它的界面
	public void returnTeacher(){
		List<TeacherMap> list = getChoosedTeacher();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("teacher", (Serializable)list);
		intent.putExtras(bundle);
		setResult(RESULT_OK,intent);
		finish();
	}
	

	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
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
			if(arg1 == null){
				viewholder = new ViewHolder();
				arg1 = LayoutInflater.from(ChooseTeacherByParentActivity.this)
						.inflate(R.layout.list_dept_item, null);
				viewholder.choose = (CheckBox)arg1.findViewById(R.id.choose);
				viewholder.view = arg1.findViewById(R.id.dept_info);
				viewholder.name = (TextView) arg1.findViewById(R.id.name);
				viewholder.title = (TextView) arg1.findViewById(R.id.title);
				arg1.setTag(viewholder);
			}else{
				viewholder = (ViewHolder) arg1.getTag();
			}
			final TeacherMap teacher = list.get(arg0);
			final int id = teacher.getId();
			if(!DataUtil.isNullorEmpty(teacher.getName())){
				viewholder.name.setText(teacher.getName());
				viewholder.title.setText(teacher.getTitle());
			}
			viewholder.choose.setTag(teacher);

			if(isChecked != null && isChecked.containsKey(id)){
				viewholder.choose.setChecked(isChecked.get(id));
			}else{
				viewholder.choose.setChecked(false);
			}

			return arg1;
		}
		
	}
	
	private static class ViewHolder{
		CheckBox  choose;
		TextView name;
		TextView title;
		View view;
	}
}
