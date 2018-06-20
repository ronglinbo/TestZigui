/*
 * 文 件 名:ChooseStudentByClassAdminActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-08
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.chooseContact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.AllGradeClass;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.ClassStudentList;
import com.wcyc.zigui2.newapp.bean.GradeClass;
import com.wcyc.zigui2.newapp.bean.GradeMap;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.newapp.fragment.AllMessageFragment;
import com.wcyc.zigui2.newapp.fragment.ContactFragment;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

//班主任选择学生
public class ChooseStudentByClassAdminActivity extends BaseActivity {
	private TextView cancel,enter;
	private Button chooseClass,chooseStudent;
	private GradeClass gradeClass;
	private ClassStudent classStudent;
	private ClassStudentList classStudentList;
	private List<GradeMap> gradeList;
	private List<List<ClassMap>> classList;
	private AllGradeClass allGradeClass;
	private ChooseClassFragment  chooseClassFragment;
	private ChooseStudentFragment chooseStudentFragment;
	private List<ClassMap> choosedClassList;//已选择学生班级
	private List<Student> choosedStudentList;//已选择学生
	private List<NewClasses> adminClass;//担任班主任的班级信息
	List<ClassStudent> list;
	private static final int GET_GRADE_CLASS = 0;
	private static final int GET_CLASS = 1;
	private static final int GET_STUDENT_LIST = 2;
	private int i = 0;
	public static final int SEARCH_STUDENT = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parseIntent();
		setContentView(R.layout.activity_choose_student);
		initView();
		initEvent();
		adminClass = DataUtil.getClassAdmin();
		if(adminClass != null){//班主任只发本班学生
			getStudentList(adminClass);
		}
	}
	
	private void parseIntent(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		choosedClassList = (List<ClassMap>) bundle.getSerializable("choosedStudentClass");
		choosedStudentList = (List<Student>) bundle.getSerializable("choosedStudentList");
	}
	
	private void initView(){
		chooseClass = (Button) findViewById(R.id.choose_class);
		chooseStudent = (Button) findViewById(R.id.choose_student);
		findViewById(R.id.new_content).setVisibility(View.GONE);
		cancel = (TextView) findViewById(R.id.title2_off);
		enter = (TextView) findViewById(R.id.title2_ok);
	}
	
	private void initEvent(){
		chooseClass.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				placeView(0);
			}
			
		});
		chooseStudent.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				placeView(1);
			}
			
		});
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
	
	private void returnStudent(){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		if(chooseStudentFragment != null){
			bundle.putSerializable("studentList", (Serializable) chooseStudentFragment.getChooseStudentList());
		}else{
			bundle.putSerializable("studentList", (Serializable)choosedStudentList);
		}
		if(chooseClassFragment != null){
			bundle.putSerializable("studentClass", (Serializable) chooseClassFragment.getChooseList());
		}else{
			bundle.putSerializable("studentClass", (Serializable) choosedClassList);
		}
		intent.putExtras(bundle);

		setResult(RESULT_OK,intent);
		finish();
	}
	
	public void placeView(int index){
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(index+"");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(fragment == null){
			switch(index){
			case 0:
				if(adminClass != null){
					allGradeClass = new AllGradeClass();
					gradeList = new ArrayList<GradeMap>();
					int pos = 0;
					
					for(NewClasses classes:adminClass){
						GradeMap grade = new GradeMap();//
						ClassMap Class = new ClassMap();
						List<ClassMap> classMapList = new ArrayList<ClassMap>();
						grade.setName(classes.getGradeName());
						Class.setName(classes.getClassName());
						int classId = Integer.parseInt(classes.getClassID());
						int id = Integer.parseInt(classes.getGradeId());
						Class.setId(classId);
						grade.setId(id);
						classMapList.add(Class);
						grade.setClassMapList(classMapList);
						
						//是否已插入
						if((pos = isItemInList(gradeList,id)) < 0){
							//年级未存在
							gradeList.add(grade);
						}else{
							gradeList.get(pos).getClassMapList().add(Class);
						}
					}
					allGradeClass.setGradeMapList(gradeList);
				}
				chooseClassFragment = (ChooseClassFragment) 
				(fragment = ChooseClassFragment.newInstance(index,allGradeClass,choosedClassList));
				chooseClass.setSelected(true);
				chooseStudent.setSelected(false);
				break;
			case 1:
				chooseStudentFragment = (ChooseStudentFragment) 
				(fragment = ChooseStudentFragment.newInstance(index,classStudentList,choosedStudentList));
				chooseClass.setSelected(false);
				chooseStudent.setSelected(true);
				break;
			default:
				break;
			}
		}
		ft.replace(R.id.maincontent, fragment, index+"");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		//ft.addToBackStack(null);
		ft.commit();
	}
	
	private int isItemInList(List<GradeMap> list,int gradeId){
		int i = 0;
		for(GradeMap grade:list){
			if(gradeId == grade.getId()){
				return i;
			}
			i++;
		}
		return -1;
	}

	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		switch(action){
		case GET_STUDENT_LIST:
			parseStudentList(data,i++);
			if(i == adminClass.size()){
				placeView(0);
				i = 0;
			}
			break;
		}
	}

	public void getStudentList(List<NewClasses> classList){
		JSONObject json = new JSONObject();
		classStudentList = new ClassStudentList();
		list = new ArrayList<ClassStudent>();
		try {
			for(NewClasses classes:classList){
				json.put("classId", classes.getClassID());
				queryPost(Constants.GET_STUDENT_LIST,json);
				System.out.println("getStudentList:"+json);
				action = GET_STUDENT_LIST;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseStudentList(String data,int i){
		System.out.println("parseStudentList:"+data);
		classStudent = JsonUtils.fromJson(data, ClassStudent.class);
		
		String name = adminClass.get(i).getClassName();
		String gradeName = adminClass.get(i).getGradeName();
		classStudent.setName(name);
		classStudent.setGradeName(gradeName);
		System.out.println("Name:"+name);
		
		list.add(classStudent);
		if(i == adminClass.size()-1)
		classStudentList.setClassStudent(list);
	}
}
