/*
 * 文 件 名:ChooseStudentActivity.java
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
//选择全校班级学生
public class ChooseAllStudentActivity extends BaseActivity {
	private TextView cancel,enter;
	private Button chooseClass,chooseStudent;
	private GradeClass gradeClass;
	private ClassStudent classStudent;
	private ClassStudentList classStudentList;

	private AllGradeClass allGradeClass;
	private ChooseClassFragment  chooseClassFragment;
	private ChooseStudentFragment chooseStudentFragment;
	private List<ClassMap> choosedClassList;//已选择学生班级
	private List<Student> choosedStudentList;//已选择学生
	private List<ClassStudent> list;//显示的学生list
	
	private static final int GET_GRADE_CLASS = 0;
	private static final int GET_CLASS = 1;
	private static final int GET_STUDENT_LIST = 2;
	private int i = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parseIntent();
		setContentView(R.layout.activity_choose_student);
		initView();
		initEvent();
		
		getGradeClass("1","");
		allGradeClass = new AllGradeClass();	
	}
	
	private void parseIntent(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			choosedClassList = (List<ClassMap>) bundle.getSerializable("choosedStudentClass");
			choosedStudentList = (List<Student>) bundle.getSerializable("choosedStudentList");
		}
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
				getStudentList(CCApplication.getInstance().getPresentUser().getClassId());
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
		ft.commitAllowingStateLoss();
	}

	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		switch(action){
		case GET_GRADE_CLASS:
			parseGradeClass(data);
			placeView(0);
			break;
		case GET_CLASS:
			parseClass(data);
			break;
		case GET_STUDENT_LIST:
			parseStudentList(data);
			placeView(1);
			break;
		}
	}
	
	public void getGradeClass(String isNeedAllGrade,String gradeId){
		JSONObject json = new JSONObject();
		try {
			json.put("isNeedAllGrade", isNeedAllGrade);
			json.put("gradeId",gradeId);
			queryPost(Constants.GET_GRADE_CLASS,json);
			action = GET_GRADE_CLASS;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseGradeClass(String data){//获取年级信息
		System.out.println("获取年级信息:"+data);
		gradeClass = JsonUtils.fromJson(data, GradeClass.class);
		List<GradeMap> gradeList = gradeClass.getGradeMapList();
		allGradeClass.setGradeMapList(gradeList);
		for(int i = 0 ; i < gradeList.size(); i++){
			try {
				getClass("0",gradeList.get(i).getId()+"");
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void getClass(String isNeedAllGrade,String gradeId){//获取年级下的班级
		JSONObject json = new JSONObject();
		try {
			json.put("isNeedAllGrade", isNeedAllGrade);
			json.put("gradeId",gradeId);
			queryPost(Constants.GET_GRADE_CLASS,json);
			System.out.println("获取年级下的班级:"+gradeId);
			action = GET_CLASS;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseClass(String data){
		System.out.println("解析获取到的班级信息:"+data);
		GradeClass gradeClass = JsonUtils.fromJson(data, GradeClass.class);
		//i需改,需校验
		allGradeClass.getGradeMapList().get(i++).setClassMapList(gradeClass.getClassMapList());
		ClassStudent classInfo = new ClassStudent();
		List<ClassMap> list = gradeClass.getClassMapList();
	}
	
	public void getStudentList(String classId){
		JSONObject json = new JSONObject();
		try {
			json.put("classId",classId);
			//json.put("classIdList", value);//todo
			queryPost(Constants.GET_STUDENT_LIST,json);
			System.out.println("classId:"+json);
			action = GET_STUDENT_LIST;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseStudentList(String data){
		System.out.println("parseStudentList:"+data);
		classStudent = JsonUtils.fromJson(data, ClassStudent.class);
		
		String name = CCApplication.getInstance().getPresentUser().getClassName();
		classStudent.setName(name);
		System.out.println("Name:"+name);
		List<ClassStudent> list = new ArrayList<ClassStudent>();
		list.add(classStudent);
		classStudentList = new ClassStudentList();
		classStudentList.setClassStudent(list);
	}
	
	public void getStudentList(List<NewClasses> classList){
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		classStudentList = new ClassStudentList();
//		list = new ArrayList<ClassStudent>();
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
		
//		String name = adminClass.get(i).getClassName();
//		classStudent.setName(name);
//		System.out.println("Name:"+name);
		
		list.add(classStudent);
//		if(i == adminClass.size()-1)
		classStudentList.setClassStudent(list);
	}
}
