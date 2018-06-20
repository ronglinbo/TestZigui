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
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllGradeClass;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.ClassStudentList;
import com.wcyc.zigui2.newapp.bean.GradeClass;
import com.wcyc.zigui2.newapp.bean.GradeMap;
import com.wcyc.zigui2.newapp.bean.GradeleaderBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.newapp.fragment.AllMessageFragment;
import com.wcyc.zigui2.newapp.fragment.ContactFragment;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpUtils;
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

public class ChooseStudentActivity extends BaseActivity {
	
	private TextView cancel,enter;
	private Button chooseClass,chooseStudent;

	private ClassStudentList classStudentList,showAllClasses;

	private AllGradeClass allGradeClass,showGradeClass;
	private List<NewClasses> AllClasses;
	private ChooseClassFragment  chooseClassFragment;
	private ChooseStudentFragment chooseStudentFragment;
	private List<ClassMap> choosedClassList;//已选择学生班级
	private List<Student> choosedStudentList;//已选择学生

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parseIntent();
		setContentView(R.layout.activity_choose_student);
		initView();
		initEvent();
		AllClasses = CCApplication.getInstance().getSchoolAllClassList();
		allGradeClass = CCApplication.getInstance().getAllGradeClass();
		showGradeClass = getGradeClass();
		showAllClasses = getClassStudent();
		placeView(0);
	}

	private AllGradeClass getGradeClass(){
		List<GradeleaderBean> list = getGradeList();
		if(list == null) return allGradeClass;
		List<GradeMap> gradeList = allGradeClass.getGradeMapList();

		List<GradeMap> ret = new ArrayList<GradeMap>();
		//获取担任班主任的班级信息
		List<NewClasses> classAdmin = DataUtil.getClassAdmin();

		if(classAdmin != null){
			for(NewClasses cls:classAdmin){
				int gradeId = Integer.parseInt(cls.getGradeId());
				int clsId = Integer.parseInt(cls.getClassID());
				if(!isClassIdExistGrade(ret,gradeId,clsId)) {
					GradeMap gradeInfo = new GradeMap();
					gradeInfo.setName(cls.getGradeName());
					gradeInfo.setId(gradeId);
					List<ClassMap> classMapList = new ArrayList<ClassMap>();
					ClassMap classInfo = new ClassMap();
					classInfo.setId(clsId);
					classInfo.setName(cls.getClassName());
					classMapList.add(classInfo);
					gradeInfo.setClassMapList(classMapList);
					ret.add(gradeInfo);
				}
			}
		}
		//
		for(GradeleaderBean item:list){
			String gradeId = item.getGradeId();
			for(GradeMap gradeMap:gradeList){
				if(gradeId.equals(String.valueOf(gradeMap.getId()))){
					ret.add(gradeMap);
				}
			}
		}

		AllGradeClass gradeClass = new AllGradeClass();
		gradeClass.setGradeMapList(ret);
		return gradeClass;
	}

	private boolean isClassIdExistGrade(List<GradeMap> list,int gradeId,int classId){
		for(GradeMap item :list){
			if(gradeId == item.getId()){
				List<ClassMap> classList = item.getClassMapList();
				for(ClassMap classMap :classList){
					if(classId == classMap.getId()){
						return true;
					}
				}
			}
		}
		return false;
	}

	private ClassStudentList getClassStudent(){
		classStudentList = new ClassStudentList();
		List<ClassStudent> list = new ArrayList<ClassStudent>();
		if(AllClasses != null) {
			for (NewClasses item : AllClasses) {
				String id = item.getClassID();
				ClassStudent classItem = new ClassStudent();
				classItem.setId(Integer.parseInt(id));
				classItem.setName(item.getClassName());
				classItem.setGradeId(item.getGradeId());
				classItem.setGradeName(item.getGradeName());
				List<Student> studentlist = item.getContactList();
				classItem.setStudentList(studentlist);
				list.add(classItem);
			}
		}
		classStudentList.setClassStudent(list);
		List<GradeleaderBean> gradeList = getGradeList();
		if(gradeList == null) return classStudentList;
		ClassStudentList ret = new ClassStudentList();
		List<ClassStudent> newList = new ArrayList<ClassStudent>();

			//获取担任班主任的班级信息
		List<NewClasses> classAdmin = DataUtil.getClassAdmin();
		if(classAdmin != null) {
			for (NewClasses cls : classAdmin) {
				List<Student> sList = cls.getContactList(); //是空的
				if(AllClasses != null) {
					for (NewClasses item : AllClasses) {
						if(item.getClassID().equals(cls.getClassID())){
							sList=item.getContactList();
						}
					}
				}
				ClassStudent classStudent = new ClassStudent();
				classStudent.setStudentList(sList);
				classStudent.setGradeId(cls.getGradeId());
				classStudent.setGradeName(cls.getGradeName());
				String classId = cls.getClassID();
				int clsId = Integer.parseInt(classId);
				if(!isClassIdExist(newList,clsId)) {
					classStudent.setId(clsId);
					classStudent.setName(cls.getClassName());
					newList.add(classStudent);
				}
			}
		}

		for(GradeleaderBean item:gradeList){
			String gradeId = item.getGradeId();
			for(ClassStudent classStudent:list){
				if(gradeId.equals(classStudent.getGradeId())){
					newList.add(classStudent);
				}
			}
		}

		ret.setClassStudent(newList);

		return ret;
	}

	private boolean isClassIdExist(List<ClassStudent> list,int classId){
		for(ClassStudent item:list){
			if(classId == item.getId()){
				return true;
			}
		}
		return false;
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
				chooseClassFragment = (ChooseClassFragment)
				(fragment = ChooseClassFragment.newInstance(index,showGradeClass,choosedClassList));
				chooseClass.setSelected(true);
				chooseStudent.setSelected(false);
				break;
			case 1:
				chooseStudentFragment = (ChooseStudentFragment) 
				(fragment = ChooseStudentFragment.newInstance(index,showAllClasses,choosedStudentList));
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
	}


	//获取担任年级组长的年级
	private List<GradeleaderBean> getGradeList(){
		MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
		boolean isParent = CCApplication.getInstance().isCurUserParent();
		if(isParent == true){
			return null;
		}
		if(detail != null){
			List<MemberDetailBean.Role> list = detail.getRoleList();
			List<GradeleaderBean> gradeList = detail.getGradeInfoList();
			if(list != null){
				for(MemberDetailBean.Role item:list){
					if(Constants.GRADE_LEADER.equals(item.getRoleCode())){
						return gradeList;
					}
				}
			}
		}
		return null;
	}
}
