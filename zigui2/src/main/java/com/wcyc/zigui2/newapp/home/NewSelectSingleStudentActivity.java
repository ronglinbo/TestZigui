package com.wcyc.zigui2.newapp.home;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.contactselect.LetterListView;
import com.wcyc.zigui2.contactselect.LetterListView.OnTouchingLetterChangedListener;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.listener.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.activity.SearchContactActivity;
import com.wcyc.zigui2.newapp.adapter.NewSelectStudentAdapter;
import com.wcyc.zigui2.newapp.adapter.NewSelectStudentSingleAdapter;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.ClassStudentList;
import com.wcyc.zigui2.newapp.bean.GradeClass;
import com.wcyc.zigui2.newapp.bean.GradeMap;
import com.wcyc.zigui2.newapp.bean.GradeleaderBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean.Role;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewUsedHelpBean;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.GoHtml5Function;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

/**
 * 选择学生界面 列表 选单个
 *
 * @author 郑国栋 2016-4-18
 * @version 2.0
 */
public class NewSelectSingleStudentActivity extends BaseActivity implements
		OnClickListener {

	String TAG="SelectSingleStudentTAG";

	private TextView new_content;
	private TextView title2_off;
	private TextView title2_ok;
	private ExpandableListView student_list_view_elv;
	private ArrayList<Student> studentList_aa;
	private ArrayList<Student> studentList_bb;
	private NewSelectStudentSingleAdapter myNewSelectStudentSingleAdapter;
	private LetterListView letterListView;
	private List<NewClasses> cList;// 任教班级
	private List<NewClasses> cList0930;// 任教班级
	private List<NewClasses> cList_aa;
	private List<NewClasses> classList;//
	private String type;
	private Button searchButton;
	private SearchView search;
	private boolean schooladmin=false;
	private boolean schoolleader=false;
	private boolean educationadmin=false;
	private boolean gradeleader=false;
	private boolean fileadmin=false;


	private ClassStudentList classStudentListSingle;
	private List<ClassStudent> classStudentList_aa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_select_single_student);
		initView();
		long startLong=System.currentTimeMillis();
		Log.i(TAG,"开始加载数据");
		initDatas();
		long endLong=System.currentTimeMillis();

		Log.i(TAG,"加载数据完成用时："+(endLong-startLong));
		initEvents();
	}

	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);
		title2_off = (TextView) findViewById(R.id.title2_off);
		title2_ok = (TextView) findViewById(R.id.title2_ok);

		student_list_view_elv = (ExpandableListView) findViewById(R.id.contact_list_view_elv);
		student_list_view_elv.setVisibility(View.VISIBLE);
		ListView contact_list_view = (ListView)findViewById(R.id.contact_list_view);
		contact_list_view.setVisibility(View.GONE);

		letterListView = (LetterListView) findViewById(R.id.letter_list_view);

		searchButton = (Button) findViewById(R.id.searchButton);//搜索
	}

	private boolean isClassIdExist(List<NewClasses> classlist,String classId){
		for (int i = 0; i < classlist.size(); i++) {
			if(classlist.get(i).getClassID().equals(classId)){
				return true;
			}
		}

		return false;
	}


	@SuppressWarnings({ "unchecked", "rawtypes", "null" })
	private void initDatas() {
		Log.i(TAG,"处理数据1111");
		new_content.setText("选择单个学生");
		type = getIntent().getExtras().getString("type");

		cList=new ArrayList<NewClasses>();
		studentList_bb=new ArrayList<Student>();
		cList = CCApplication.app.getMemberDetail().getClassList();// 任教班级
		if(cList!=null){
			cList0930=new ArrayList<NewClasses>();
			cList0930.addAll(cList);
		}
		Log.i(TAG,"处理数据2222");
		List<Role> roleListA=CCApplication.getInstance().getMemberDetail().getRoleList();
		for (int i = 0; i < roleListA.size(); i++) {
			String roleCode=roleListA.get(i).getRoleCode();
			System.out.println(i+"=选=roleCode===="+roleCode);
			if("schooladmin".equals(roleCode)){
				schooladmin=true;
			}

			if("score".equals(type)||"attendance".equals(type)
                    ||"schoolAttendance".equals(type)
					||"carAttendance".equals(type)
					||"dormAttendance".equals(type)){
				if("schoolleader".equals(roleCode)){
					schoolleader=true;
				}

				if("educationadmin".equals(roleCode)){
					educationadmin=true;
				}

				//人工考勤
				if("attendance".equals(type)){
					if("fileadmin".equals(roleCode)){
						fileadmin=true;
					}
				}

				//进出校考勤
				if("schoolAttendance".equals(type)){
                    if("fileadmin".equals(roleCode)){
                        fileadmin=true;
                    }
                }

                //校车考勤
				if("carAttendance".equals(type)){
					if("fileadmin".equals(roleCode)){
						fileadmin=true;
					}
				}

				//宿舍考勤
				if("dormAttendance".equals(type)){
					if("fileadmin".equals(roleCode)){
						fileadmin=true;
					}
				}

				if("gradeleader".equals(roleCode)){
					gradeleader=true;
				}

			}

		}
		Log.i(TAG,"处理数据3333");

		if(schooladmin||schoolleader||educationadmin||gradeleader||fileadmin){
			List<NewClasses> schoolAllClassList=CCApplication.getInstance().getSchoolAllClassList();
			if(schoolAllClassList!=null){
				cList=schoolAllClassList;

				if(gradeleader&&!(schooladmin||schoolleader||educationadmin||fileadmin)){
					List<GradeleaderBean> gradeInfoList = CCApplication.getInstance().getMemberDetail().getGradeInfoList();
					List<NewClasses> cListA=new ArrayList<NewClasses>();
					cListA.addAll(cList);

					if("attendance".equals(type)||"score".equals(type)
							||"schoolAttendance".equals(type)
							||"carAttendance".equals(type)
							||"dormAttendance".equals(type)){
						cList.clear();
						for(int i=0;i<gradeInfoList.size();i++){
							String gradeIdA=gradeInfoList.get(i).getGradeId();
							System.out.println("=选=gradeIdA===="+gradeIdA);
							System.out.println("=选=cListA===="+cListA);
							for (int j=0;j<cListA.size();j++){
								if(gradeIdA.equals(cListA.get(j).getGradeId())){
									cList.add(cListA.get(j));
								}
							}
						}
					}
				}

				classList= new ArrayList<NewClasses>();
				classList.addAll(cList);
				for (int i = 0; i < cList.size(); i++) {
					List<Student> student_i=cList.get(i).getContactList();
					if(student_i!=null&&student_i.size()>0){
						for (int j = 0; j < student_i.size(); j++) {
							studentList_bb.add(student_i.get(j));
						}
					}
				}
			}
//			else{
//				System.out.println("=选=2====");
//				try {
//					JSONObject json = new JSONObject();
//					json.put("isNeedAllGrade", "1");
//					String url = new StringBuilder(Constants.SERVER_URL).append(
//						"/getGradeClassView").toString();//
//					String result = HttpHelper.httpPostJson(this, url, json);
//					GradeClass gradeClass = JsonUtils.fromJson(result, GradeClass.class);
//					List<GradeMap> gradeList = gradeClass.getGradeMapList();
//
//					if(gradeList!=null&&gradeList.size()>0){
//						if(cList==null||cList.size()==0){
//							cList=new ArrayList<NewClasses>();
//						}
//						if(gradeleader&&!(schooladmin||schoolleader||educationadmin||fileadmin)){
//							List<GradeleaderBean> gradeInfoList =CCApplication.getInstance().getMemberDetail().getGradeInfoList();
//							if(gradeInfoList!=null){
//								gradeList=null;
//								gradeList=new ArrayList<GradeMap>();
//								for (int i = 0; i < gradeInfoList.size(); i++) {
//									int gradeId=0;
//									GradeMap gradeMap= new GradeMap();
//									if(!DataUtil.isNullorEmpty(gradeInfoList.get(i).getGradeId())){
//										gradeId=Integer.parseInt(gradeInfoList.get(i).getGradeId());
//									}
//									gradeMap.setId(gradeId);
//									gradeMap.setName(gradeInfoList.get(i).getGradeName());
//									gradeList.add(gradeMap);
//								}
//							}
//						}
//						long cur1=System.currentTimeMillis();
//						System.out.println("==学=开始==="+System.currentTimeMillis());
//						for (int i = 0; i < gradeList.size(); i++) {
//							JSONObject jsonB = new JSONObject();
//							jsonB.put("gradeId", gradeList.get(i).getId());
//							jsonB.put("isNeedAllGrade", "0");
//							String urlB = new StringBuilder(Constants.SERVER_URL).append(
//								"/getGradeClassView").toString();//
//							String resultB = HttpHelper.httpPostJson(this, urlB, jsonB);
//							GradeClass gradeClassB = JsonUtils.fromJson(resultB, GradeClass.class);
//							List<ClassMap> classList=gradeClassB.getClassMapList();
//							for (int j = 0; j < classList.size(); j++) {
//								NewClasses newClasses=new NewClasses();
//								newClasses.setClassID(classList.get(j).getId()+"");
//								newClasses.setClassName(classList.get(j).getName());
//								newClasses.setGradeId(gradeList.get(i).getId()+"");
//								newClasses.setGradeName(gradeList.get(i).getName());
//								newClasses.setIsAdviser("1");
//
//								cList.add(newClasses);
//							}
//						}
//						System.out.println("=选==学-结束==="+System.currentTimeMillis());
//						System.out.println("=选==学-时间==="+(System.currentTimeMillis()-cur1));
//					}
//					CCApplication.getInstance().setSchoolAllClassList(cList);
//				} catch (Exception e) {
//				}
//			}
		}


		Log.i(TAG,"处理数据4444");

		String usertype_a = CCApplication.getInstance().getPresentUser()
				.getUserType();
		if (!usertype_a.equals("2")) {
			System.out.println("是否有班级=======没有");

			DataUtil.getToast("您无任教班级！");
			NewSelectSingleStudentActivity.this.finish();
			return;
		} else if (cList == null||cList.size()<1) {
			System.out.println("是否有班级=======没有C");
			DataUtil.getToast("您无任教班级！");
			NewSelectSingleStudentActivity.this.finish();
		} else {

			cList_aa=CCApplication.app.getMemberDetail().getClassList();

			if(schooladmin||schoolleader||educationadmin||fileadmin||gradeleader){
				if(cList_aa==null){
					cList_aa=new ArrayList<NewClasses>();
				}
				cList_aa.addAll(cList);
			}

			classStudentListSingle =new ClassStudentList();
			classStudentList_aa = new ArrayList<ClassStudent>();


			if (cList_aa != null) {//去重复
				classList = new ArrayList<NewClasses>();

				for(NewClasses classes:cList_aa){
					if(!isClassIdExist(classList,classes.getClassID()))//是否已存在list中
						classList.add(classes);
				}
				Log.i(TAG,"studentList_bb.size():"+studentList_bb.size());
				if(studentList_bb.size()==0){
					long cur3=System.currentTimeMillis();
					System.out.println("=选==全 开始==="+System.currentTimeMillis());
					for (int i = 0; i < classList.size(); i++) {

						try {

							String classId = classList.get(i).getClassID();
							// JSON对象
							JSONObject json = new JSONObject();
							json.put("classId", classId);
							//请求地址
							String url = new StringBuilder(Constants.SERVER_URL).append(Constants.GET_CLASS_STUDENT_LIST).toString();
							String result = HttpHelper.httpPostJson(this,url, json);
							JSONObject json3 = new JSONObject(result);

							//活动学生list
							JSONArray ja = json3.getJSONArray("studentList");
							Gson gson1 = new Gson();
							Type t = new TypeToken<List<Student>>() {}.getType();
							//将gson格式转为json格式的字符串
							studentList_aa = gson1.fromJson(ja.toString(), t);

//							classList.get(i).setContactList(studentList_aa);
							studentList_bb.addAll(studentList_aa);//所有学生添加到这个总查询的班级里

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					System.out.println("=选==全结束==="+System.currentTimeMillis());
					System.out.println("=选==全时间==="+(System.currentTimeMillis()-cur3));
//					CCApplication.getInstance().setSchoolAllClassList(classList);
				}

				ClassStudent studentList=new ClassStudent();// 学生列表
				studentList.setStudentList(studentList_bb);//将班级所有学生添加到班级
				classStudentList_aa.add(studentList);//将班级添加到班级列表；
				classStudentListSingle.setClassStudent(classStudentList_aa);//设置班级列表


			}
			Log.i(TAG,"处理数据5555");
			myNewSelectStudentSingleAdapter = new NewSelectStudentSingleAdapter(
					this, studentList_aa,title2_ok,this,cList);
			student_list_view_elv.setAdapter(myNewSelectStudentSingleAdapter);

		}

		Log.i(TAG,"处理数据6666");

	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initEvents() {
		title2_off.setOnClickListener(this);
		title2_ok.setOnClickListener(this);
		title2_ok.setVisibility(View.GONE);
		title2_ok.setClickable(false);
		title2_ok.setTextColor(getResources().getColor(
				R.color.font_lightgray));


		final BaseActivity activity=this;
		student_list_view_elv.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				String studentId=classList.get(groupPosition).getContactList().get(childPosition).getId()+"";
				HashMap<String,String> para = new HashMap<String,String>();
				para.put("X-mobile-Type", "android");

				if("comment".equals(type)){
					GoHtml5Function.goCommentById(para,studentId);

                    //人工考勤
				}else if("attendance".equals(type)){
					GoHtml5Function.goAttendenceChildById(para,studentId);

                    //进出校考勤
				}else if("schoolAttendance".equals(type)){
                    GoHtml5Function.goSchoolAttendenceChildById(para,studentId);

					//校车考勤
                } else if("carAttendance".equals(type)){
					GoHtml5Function.goSchoolBusAttendenceChildById(para,studentId);

					//宿舍考勤
                }else if("dormAttendance".equals(type)) {
					GoHtml5Function.goDormAttendenceChildById(para, studentId);


				}else if("score".equals(type)){
					GoHtml5Function.goScoreChildById(para,studentId);

				}

//				NewSelectSingleStudentActivity.this.finish();//不关闭此界面

				return true;
			}});




		search = (SearchView) findViewById(R.id.search);
		if(search != null){
			search.setQueryHint("姓名/姓名首字母/手机号");
			search.setIconifiedByDefault(false);
			search.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putString("type", type);
					bundle.putSerializable("classStudentListSingle", classStudentListSingle);//classStudentList
					newActivity(SearchContactActivity.class, bundle);
				}
			});
		}
		searchButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("type", type);
				bundle.putSerializable("classStudentListSingle", classStudentListSingle);
				newActivity(SearchContactActivity.class, bundle);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (myNewSelectStudentSingleAdapter == null) {
			return;
		}
		switch (v.getId()) {
		case R.id.title2_off:
//			GoHtml5Function.goToHtmlApp(this,"考勤");
			NewSelectSingleStudentActivity.this.finish();// 取消键
			break;
		case R.id.title2_ok:// 选择学生确定键
			// httpBusiInerface();

			// 返回需要的数据
			ArrayList<String> student_id_List_checked = myNewSelectStudentSingleAdapter
					.getStudent_id_List_checked();
			ArrayList<String> student_name_List_checked = myNewSelectStudentSingleAdapter
					.getStudent_name_List_checked();

			Bundle bundle = new Bundle();
			// 封装数据
			bundle.putSerializable("student_id_List_checked",
					student_id_List_checked);
			bundle.putSerializable("student_name_List_checked",
					student_name_List_checked);

			// 封装bundle
			Intent intent = new Intent();
			intent.setClass(NewSelectSingleStudentActivity.this,
					NewAttendanceActivity.class);
			intent.putExtras(bundle);

			// 返回
			NewSelectSingleStudentActivity.this.setResult(RESULT_OK, intent);// 返回上一个界面，并带参数

			NewSelectSingleStudentActivity.this.finish();
			break;

		}

	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(cList0930!=null){
			CCApplication.app.getMemberDetail().setClassList(cList0930);
		}
	}

	@Override
	protected void getMessage(String data) {

	}

}
