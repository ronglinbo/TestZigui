package com.wcyc.zigui2.newapp.module.summary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.adapter.AttachmentListAdapter;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllDeptList;
import com.wcyc.zigui2.newapp.bean.AllDeptList.CommonGroup;
import com.wcyc.zigui2.newapp.bean.AllDeptList.ContactGroupMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.DepMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.GradeMap;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.AllTeacherList.TeacherMap;
import com.wcyc.zigui2.newapp.bean.LastinfoSchool;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.TeacherSelectInfo;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.dailyrecord.GetDeptInfo;
import com.wcyc.zigui2.newapp.module.dailyrecord.GetDeptInfo.DepartInfo;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.CustomDialog;
import com.wcyc.zigui2.widget.MySpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PublishSummaryActivity extends BaseActivity
		implements ImageUploadAsyncTaskListener {
	private TextView cancel,enter;
	private MySpinner spin;
	private ImageButton addTeacher;
	private Button addAttach;
	private Button weekly,month,term,year,more,meeting,other;
	private EditText subject,content;
	private TextView teacher;
	private ListView listView;
	private NewMemberBean member;
	private boolean isChecked,isTeacherChoose,isSubjectWrite,isTypeSelect = false;
	private List<ContactGroupMap> contactList;
	private List<GradeMap> gradeList;
	private List<CommonGroup> commonList;
	private List<DepMap> deptList;
	private List<TeacherMap> chooseTeacherList; //选择老师
	private AllDeptList allList;//选择老师群组

	private List<NewClasses> classAdmin;//担任班主任的班级信息
	//	private String teacherName = "" ,studentName = "";
	private static final int PICK_PICTURE = 100;
	private static final int CHOOSE_TEACHER = 101;
	private static final int CHOOSE_STUDENT = 102;

	private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
	private UploadFileResult ret;
	private CustomDialog dialog;
	private int i = 0;
	private boolean isSingle = true;//是否一张张的上传图片
	private List<String> attachment = new ArrayList<String>();
	private AttachmentListAdapter adapter;
	private int type = WEEKLY;
	private String summaryType;
	private static final int WEEKLY = 1;
	private static final int MONTH = 2;
	private static final int TERM = 3;
	private static final int YEAR = 4;
	private static final int MEETING = 5;
	private static final int OTHER = 9;
	private boolean isShow = false;
	private static final int GET_DEPT_INFO = 1;
	private static final int PUBLISH = 2;
	private static final int LastReceive = 3;
	private List<DepartInfo> departList;
	private BaseAdapter spinAdapter;
	private DepartInfo info;//选择的部门
	private boolean selectAll;
	private boolean school_leader;
	private boolean departmentcharge;
	private boolean spin_is_null=false;
	private TeacherSelectInfo teacherSelectInfo_record=null;
	private List<TeacherMap> chooseTeacherList_record; //选择老师 记录

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_summary);
		initView();
		initEvent();
		member = CCApplication.getInstance().getMemberInfo();
		getDeptInfo();
	}

	private void initView(){
		spin = (MySpinner) findViewById(R.id.choose_dept);
		TextView title = (TextView) findViewById(R.id.new_content);
		title.setText("发布总结");
		cancel = (TextView) findViewById(R.id.title2_off);
		enter = (TextView) findViewById(R.id.title2_ok);
//		enter.setEnabled(false);
		addTeacher = (ImageButton)findViewById(R.id.add_teacher_bt);
		addAttach = (Button)findViewById(R.id.add_attach);

		weekly = (Button) findViewById(R.id.weekly_summary); //周期小结
		month = (Button) findViewById(R.id.month_summay);//月期总结
		term = (Button) findViewById(R.id.term_summary); //学期总结
		year = (Button) findViewById(R.id.year_summary);//学年总结
		more = (Button) findViewById(R.id.more);
		meeting = (Button) findViewById(R.id.meeting);
		other = (Button) findViewById(R.id.other);

		subject = (EditText) findViewById(R.id.subject);
		content = (EditText) findViewById(R.id.content);
		teacher = (TextView) findViewById(R.id.add_teacher);
		//teacher.setText(Constants.ALL_TEACHER);
		isTeacherChoose = true;
		listView = (ListView) findViewById(R.id.attachList);
	}
	//必填项是否已填
	private void inputState(){
		teacher.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
				// TODO Auto-generated method stub
				if(info!=null&&!TextUtils.isEmpty(arg0)&&summaryType!=null){
					//其他2者都满足 在判断自己
					if(TextUtils.isEmpty(arg0)){//为空
						enter.setClickable(false);
						enter.setEnabled(false);
						enter.setTextColor(Color.GRAY);
						//DataUtil.getToast("未选择教师");
					}else{//不为空
						isTeacherChoose=true;
						enter.setClickable(true);
						enter.setEnabled(true);
						enter.setTextColor(getResources().getColor(R.color.blue));
					}
				}else{
					enter.setClickable(false);
					enter.setEnabled(false);
					enter.setTextColor(Color.GRAY);
				}
			}

		});
		subject.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub

				if(info!=null&& !TextUtils.isEmpty(teacher.getText().toString())&&summaryType!=null){
					//其他2个都不是空 才判断
					if (s.length()==0){
						enter.setClickable(false);
						enter.setEnabled(false);
						enter.setTextColor(Color.GRAY);
						//  DataUtil.getToast("主题不能为空");
					} else {
						if (s.length() >= 200) {
							DataUtil.getToast("日志主题不能超过200个字");
							return ;
						}
						isSubjectWrite=true;
						enter.setClickable(true);
						enter.setEnabled(true);
						enter.setTextColor(getResources().getColor(R.color.blue));

					}

				}else{
					enter.setClickable(false);
					enter.setEnabled(false);
					enter.setTextColor(Color.GRAY);
				}
			}

		});
		content.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub
				if(s.length() >= 5000){
					DataUtil.getToast("总结内容不能超过5000个字");
				}
			}

		});

	}

	private void exitOrNot(){
		dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.customdialog, handler);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		dialog.setTitle("退出此次编辑?");
		dialog.setContent("");
	}

	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){

			if(isEdit()){
				exitOrNot();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean isEdit(){
		String title = subject.getText().toString();
		String text = content.getText().toString();
		String teacherChoosed = teacher.getText().toString();

		return !DataUtil.isNullorEmpty(title)||!DataUtil.isNullorEmpty(teacherChoosed)
				||!DataUtil.isNullorEmpty(text);
	}
	/**
	 * 控制CustomDialog按钮效果.
	 */
	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);
			if (0 != msg.arg1) {
//				radiobuttonState = msg.arg1;
			}
			switch (msg.what) {
				case CustomDialog.DIALOG_CANCEL:// 取消退出
					dialog.dismiss();
					break;
				case CustomDialog.DIALOG_SURE:// 确认退出
					setResult(RESULT_CANCELED);
					finish();
					dialog.dismiss();
					break;
				default:
					break;
			}
		}
	};

	private void initEvent(){

		enter.setClickable(false);
		enter.setEnabled(false);
		enter.setTextColor(Color.GRAY);

		inputState();
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(isEdit()){
					exitOrNot();
					return;
				}
				setResult(RESULT_CANCELED);
				finish();
			}

		});

		enter.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String title = subject.getText().toString();

				if(DataUtil.isNullorEmpty(title)){
					DataUtil.getToast("请输入主题");
					return;
				}

				if(!DataUtil.isNetworkAvailable(PublishSummaryActivity.this)){
					DataUtil.getToast(getApplicationContext().getResources().getString(R.string.no_network));
					return;
				}
				if(imagePaths.isEmpty()){
					publish();
				}else{
					uploadFile();
					//finish();
				}
			}

		});

		if(addTeacher != null){
			addTeacher.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
//					bundle.putSerializable("common", commonList);
					bundle.putInt("type",1);
					bundle.putSerializable("teacherSelectInfo_record", teacherSelectInfo_record);
					bundle.putBoolean("selectAll", selectAll);
					bundle.putBoolean("school_leader", school_leader);
					bundle.putBoolean("departmentcharge", departmentcharge);
					bundle.putSerializable("allDept", allList);
					bundle.putSerializable("teacher", (Serializable) chooseTeacherList_record);
					bundle.putSerializable("allDept", allList);
					intent.putExtras(bundle);
					intent.setClass(PublishSummaryActivity.this, ChooseTeacherActivity.class);
					startActivityForResult(intent,CHOOSE_TEACHER);
				}

			});
		}

		addAttach.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(PublishSummaryActivity.this,SelectImageActivity.class);
				intent.putExtra("limit", Constants.MAX_ATTACH_LIST);
				intent.putStringArrayListExtra("addPic", imagePaths);
				intent.putExtra("attachmentLimit", "attachmentLimit");

				startActivityForResult(intent,PICK_PICTURE);
			}

		});

		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				ImageView del = (ImageView)arg1.findViewById(R.id.iv_del);
				del.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						imagePaths.remove(arg2);
						adapter.notifyDataSetChanged();
					}

				});

			}

		});
		weekly.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type = WEEKLY;
				summaryType = "周期小结";
				changeButtonBackGround(weekly);
			}

		});
		month.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type = MONTH;
				summaryType = "月期总结";
				changeButtonBackGround(month);
			}

		});
		term.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type = TERM;
				summaryType = "学期总结";
				changeButtonBackGround(term);
			}

		});
		year.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type = YEAR;
				summaryType = "学年总结";
				changeButtonBackGround(year);
			}

		});
		more.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isShow = !isShow;
				showOther(isShow);
			}

		});
		meeting.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type = MEETING;
				summaryType = "会议汇报";
				changeButtonBackGround(meeting);
			}

		});
		other.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type = OTHER;
				summaryType = "其他";
				changeButtonBackGround(other);
			}

		});
		spin.setOnItemSelectedListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				   spin.dismissPop();
				   spin.setHint(departList.get(position).getDepartmentName());
				   info=departList.get(position);
				//做个判断
				if(!TextUtils.isEmpty(subject.getText().toString())&&!TextUtils.isEmpty(teacher.getText().toString())&&summaryType!=null){
					//其他2者都不为空是
					enter.setClickable(true);
					enter.setEnabled(true);
					enter.setTextColor(getResources().getColor(R.color.blue));
				}else{
					enter.setClickable(false);
					enter.setEnabled(false);
					enter.setTextColor(Color.GRAY);
				}

			}
		});
	}

	private void showOther(boolean show){
		if(show){
			meeting.setVisibility(View.VISIBLE);
			other.setVisibility(View.VISIBLE);
		}else{
			meeting.setVisibility(View.GONE);
			other.setVisibility(View.GONE);
		}
	}

	private void changeButtonBackGround(Button button){
		weekly.setBackgroundResource(R.drawable.btn2_leixing_normal);
		month.setBackgroundResource(R.drawable.btn2_leixing_normal);
		term.setBackgroundResource(R.drawable.btn2_leixing_normal);
		year.setBackgroundResource(R.drawable.btn2_leixing_normal);
		other.setBackgroundResource(R.drawable.btn2_leixing_normal);
		meeting.setBackgroundResource(R.drawable.btn2_leixing_normal);
		button.setBackgroundResource(R.drawable.btn2_leixing_selected);
		isTypeSelect = true;

		//判断所有
		if(info!=null&&!TextUtils.isEmpty(subject.getText().toString())&&!TextUtils.isEmpty(teacher.getText().toString())){
			enter.setClickable(true);
			enter.setEnabled(true);
			enter.setTextColor(getResources().getColor(R.color.blue));
		}else{
			enter.setClickable(false);
			enter.setEnabled(false);
			enter.setTextColor(Color.GRAY);
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bundle bundle = null;
		if(data != null)
			bundle = data.getExtras();
		System.out.println("requestCode:"+requestCode+" resultCode:"+resultCode+" data:"+data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
				case PICK_PICTURE:
					imagePaths = bundle.getStringArrayList("pic_paths");

					if(imagePaths!=null){
						long sizeAllLong=0;
						for (int i = 0; i < imagePaths.size(); i++) {
							String file = imagePaths.get(i);
							long size = 0;
							try {
								size = DataUtil.getFileSize(new File(file));
							} catch (Exception e) {
								e.printStackTrace();
							}
							sizeAllLong += size;
						}

						if(sizeAllLong > 20*1024*1024){//判断拍照后返回来的数据集合 大小  大于20M 则
							imagePaths.remove(imagePaths.size()-1);
							DataUtil.getToast("图片总大小不能超过20M");
						}

					}
					data.getBooleanExtra("is_compress", true);
					adapter = new AttachmentListAdapter(PublishSummaryActivity.this,imagePaths);
					listView.setAdapter(adapter);
					break;
				case CHOOSE_TEACHER:
//				commonList = (ArrayList<Object>) bundle.getSerializable("common");
					teacherName="";
					all_departs= (TeacherSelectInfo) bundle.getSerializable("choose_list"); //所有部门集合
					chooseTeacherList = (List<TeacherMap>) bundle.getSerializable("teacher");

					if(chooseTeacherList!=null){
						if(chooseTeacherList.size()>0){
							try {
								chooseTeacherList_record=deepCopy(chooseTeacherList);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							chooseTeacherList_record=null;
						}
					}

					departmentcharge=ChooseTeacherActivity.departleadr;
					school_leader=ChooseTeacherActivity.schoolleader;
					selectAll=ChooseTeacherActivity.isSelectAll;
					addTeacher();
					if(all_departs!=null){
						try {
							teacherSelectInfo_record=deepCopy(all_departs); //拷贝一份 作为 记录
						} catch (Exception e) {
							e.printStackTrace();
						}
						addDeparts(requestCode,subordinateGroupMap,all_departs);
					}
					allList = (AllDeptList) bundle.getSerializable("allDept");

					//addTeacher();
					break;
			}
		}
	}

	private TeacherSelectInfo all_departs;
	private String teacherName ="";
	protected void addTeacher(){

		if(allList != null){
			deptList = allList.getDepMapList();
			contactList = allList.getContactGroupMapList();
			gradeList = allList.getGradeMapList();
			commonList = allList.getCommonList();
		}

		if(deptList != null){
			for(int i = 0; i < deptList.size(); i++){
				DepMap dept = deptList.get(i);
				String name = dept.getDepartmentName();
				teacherName += name;
				teacherName += ",";
			}
		}
		if(contactList != null){
			for(int i = 0; i < contactList.size(); i++){
				ContactGroupMap contact = contactList.get(i);
				String name = contact.getName();
				teacherName += name;
				teacherName += ",";
			}
		}
		if(gradeList != null){
			for(int i = 0; i < gradeList.size(); i++){
				GradeMap grade = gradeList.get(i);
				String name = grade.getName();
				teacherName += name;
				teacherName += ",";
			}
		}
		if(commonList != null){
			for(CommonGroup item:commonList){
				teacherName += item.getName();
				teacherName += ",";
			}
		}
		if(chooseTeacherList != null){
			for(int i = 0; i < chooseTeacherList.size(); i++){
				String name = chooseTeacherList.get(i).getName();
				teacherName += name;
				teacherName += ",";
			}
		}

		isTeacherChoose = teacherName.length() > 0;
	}

	private void getDeptInfo(){
		JSONObject json = new JSONObject();
		UserType user = CCApplication.getInstance().getPresentUser();
		try {
			json.put("userId", user.getUserId());
			json.put("schoolId",user.getSchoolId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		action = GET_DEPT_INFO;
		queryPost(Constants.GET_DEPART_INFO,json);
	}

	protected void uploadFile(){
		if(isSingle){
			for(String file:imagePaths){
				ImageUploadAsyncTask upload = new ImageUploadAsyncTask(
						this,Constants.PIC_TYPE,file,Constants.UPLOAD_URL,this);
				upload.execute();
			}
		}else{
			ImageUploadAsyncTask upload = new ImageUploadAsyncTask(
					this,Constants.PIC_TYPE,imagePaths,Constants.UPLOAD_URL,this);
			upload.execute();
		}
	}

	Map<String,List<String>> subordinateGroupMap=null;

	protected void addDeparts(int type,Map<String,List<String>> map,TeacherSelectInfo departs){
		String departname="";
		if(ChooseTeacherActivity.isSelectAll){
			departname+="全体教职工";
			departname+=",";


		}

		if(type==CHOOSE_TEACHER){
			subordinateGroupMap=new HashMap<String, List<String>>();
		}

		//常用分组 班主任 教研组长 备课组长 年级组长
		//校级领导
		if(ChooseTeacherActivity.schoolleader){
			departname+="校级领导";
			departname += ",";
		}
		if(ChooseTeacherActivity.departleadr){
			departname+="部门负责人";
			departname += ",";
		}
		//部门负责人
		//班主任
		List<TeacherSelectInfo.InfoBean.GradesTeacherBean> gradesTeacherBeanList=departs.getInfo().getGradesTeacher();
		if(gradesTeacherBeanList.size()>0){
			List<String> list=new ArrayList<String>();
			for (TeacherSelectInfo.InfoBean.GradesTeacherBean g:gradesTeacherBeanList) {
				list.add(g.getId()+"");

			}

			if(type==CHOOSE_TEACHER){
				subordinateGroupMap.put("sybzr@grade",list);
			}
			//数据拼接显示
			if(gradesTeacherBeanList.size()==ChooseTeacherActivity.teacherSelectInfo.getInfo().getGradesTeacher().size()){
				//全选 则拼接班主任
				departname+="班主任";
				departname += ",";
			}else{
				for (TeacherSelectInfo.InfoBean.GradesTeacherBean g:gradesTeacherBeanList) {
					list.add(g.getName());
					departname += g.getName()+"(班主任)";
					departname += ",";
				}
			}


		}


		//教研组长
		List<TeacherSelectInfo.InfoBean.GroupsLeaderBean> groupsLeaderBeanList=departs.getInfo().getGroupsLeader();
		if(groupsLeaderBeanList.size()>0){
			//数据存到map集合上
			List<String> list=new ArrayList<String>();
			for (TeacherSelectInfo.InfoBean.GroupsLeaderBean g:groupsLeaderBeanList) {
				list.add(g.getCode()+"");

			}

			if(type==CHOOSE_TEACHER){
				subordinateGroupMap.put("syjyzz@grade",list);
			}
			//数据拼接显示
			if(groupsLeaderBeanList.size()==ChooseTeacherActivity.teacherSelectInfo.getInfo().getGroupsLeader().size()){
				//全选 则拼接班主任
				departname+="教研组长";
				departname += ",";
			}else {
				for (TeacherSelectInfo.InfoBean.GroupsLeaderBean g:groupsLeaderBeanList) {
					departname += g.getName()+"(教研组长)";
					departname += ",";
				}
			}

		}
		//备课组长
		List<TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean> prepareLessionLeaderBeanList=departs.getInfo().getPrepareLessionLeader();
		if(prepareLessionLeaderBeanList.size()>0){
			//部分选
			List<String> list=new ArrayList<String>();
			for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean g:prepareLessionLeaderBeanList) {
				list.add(g.getId()+"");

			}

			if(type==CHOOSE_TEACHER){
				subordinateGroupMap.put("sybkzz@grade",list);
			}
			if(prepareLessionLeaderBeanList.size()==ChooseTeacherActivity.teacherSelectInfo.getInfo().getPrepareLessionLeader().size()){
				//全选 则拼接班主任
				departname+="备课组长";
				departname += ",";
			}else{
				for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean g:prepareLessionLeaderBeanList) {
					departname+=g.getName()+"(备课组长)";
					departname += ",";
				}
			}

		}
		//年级组长
		List<TeacherSelectInfo.InfoBean.GradeGroupBean> gradeGroupBeanList=departs.getInfo().getGradeGroup();
		if(gradeGroupBeanList.size()>0){
			//部分选
			List<String> list=new ArrayList<String>();
			for (TeacherSelectInfo.InfoBean.GradeGroupBean g:gradeGroupBeanList) {
				list.add(g.getCode()+"");
			}

			if(type==CHOOSE_TEACHER){
				subordinateGroupMap.put("synjzz",list);
			}
			if(gradeGroupBeanList.size()==ChooseTeacherActivity.teacherSelectInfo.getInfo().getGradeGroup().size()){
				//全选 则拼接班主任
				departname+="年级组长";
				departname += ",";
			}else {
				for (TeacherSelectInfo.InfoBean.GradeGroupBean g:gradeGroupBeanList) {
					departname += g.getName()+"(年级组长)";
					departname += ",";
				}
			}
		}



		//行政机构
		List<TeacherSelectInfo.InfoBean.DepartsBean> departsBeanList=departs.getInfo().getDeparts();
		for (TeacherSelectInfo.InfoBean.DepartsBean g:departsBeanList) {
			departname += g.getDepartmentName();
			departname += ",";
		}

		//教学机构 年级/班级 教研组 备课组
		List<TeacherSelectInfo.InfoBean.ClassesBeanX> classesBeanXList=departs.getInfo().getClasses();
		if(classesBeanXList.size()>0){
			//数据加载进 上传的map集合
			List<String> list=new ArrayList<String>();
			for (TeacherSelectInfo.InfoBean.ClassesBeanX g:classesBeanXList) {
				for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean c:g.getClasses()) {
					list.add(c.getId()+"");
				}
			}

			if(type==CHOOSE_TEACHER){
				subordinateGroupMap.put("classes",list);
			}
			int choose_size=0;
			for (TeacherSelectInfo.InfoBean.ClassesBeanX  classbean:classesBeanXList) {
				choose_size++;
				for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean  classbeanx:classbean.getClasses()) {
					choose_size++;
				}

			}
			int size=0;
			for (TeacherSelectInfo.InfoBean.ClassesBeanX  classbean:ChooseTeacherActivity.teacherSelectInfo.getInfo().getClasses()) {
				size++;
				for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean  classbeanx:classbean.getClasses()) {
					size++;
				}

			}

			//负责 拼接数据 显示
			if(choose_size==size){
				departname+="年级/班级";
				departname += ",";
			}else {// 2级页面没有全选    2种情况:1.2级项 部分选择 则拼接所有下级   2级项 全选 则拼接2级项名字
				for (TeacherSelectInfo.InfoBean.ClassesBeanX g:classesBeanXList) {//选择集合
					for (TeacherSelectInfo.InfoBean.ClassesBeanX c: ChooseTeacherActivity.teacherSelectInfo.getInfo().getClasses()) {
						if(g.getName().equals(c.getName())){
							if(g.getClasses().size()==c.getClasses().size()){ //判断是不是全选
								//是的话 则拼接 此name
								departname+=g.getName();
								departname += ",";
							}else {//没全选

								for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean classesBean:g.getClasses()) {
									departname += classesBean.getName();
									departname += ",";
								}

							}
						}
					}

				}



			}
			//全选
		}

		List<TeacherSelectInfo.InfoBean.PrepareLessionBean> prepareLessionBeanList=departs.getInfo().getPrepareLession();
		if(prepareLessionBeanList.size()>0){//部分
			//部分选
			List<String> list=new ArrayList<String>();
			for (TeacherSelectInfo.InfoBean.PrepareLessionBean g:prepareLessionBeanList) {
				for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX c:g.getPrepareLssions()) {
					list.add(c.getId()+"");

				}
			}

			if(type==CHOOSE_TEACHER){
				subordinateGroupMap.put("prepareLssion",list);
			}
			int choose_size=0;
			for (TeacherSelectInfo.InfoBean.PrepareLessionBean  classbean:prepareLessionBeanList) {
				choose_size++;
				for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX  classbeanx:classbean.getPrepareLssions()) {
					choose_size++;
				}

			}
			int size=0;
			for (TeacherSelectInfo.InfoBean.PrepareLessionBean  classbean:ChooseTeacherActivity.teacherSelectInfo.getInfo().getPrepareLession()) {
				size++;
				for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX  classbeanx:classbean.getPrepareLssions()) {
					size++;
				}

			}

			//负责 拼接数据 显示
			if(choose_size==size){
				departname+="备课组";
				departname += ",";
			}else {// 2级页面没有全选    2种情况:1.2级项 部分选择 则拼接所有下级   2级项 全选 则拼接2级项名字
				for (TeacherSelectInfo.InfoBean.PrepareLessionBean g:prepareLessionBeanList) {//选择集合
					for (TeacherSelectInfo.InfoBean.PrepareLessionBean c: ChooseTeacherActivity.teacherSelectInfo.getInfo().getPrepareLession()) {
						if(g.getName().equals(c.getName())){
							if(g.getPrepareLssions().size()==c.getPrepareLssions().size()){ //判断是不是全选
								//是的话 则拼接 此name
								departname+=g.getName()+"(备课组)";
								departname += ",";
							}else {//没全选

								for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX classesBean:g.getPrepareLssions()) {
									departname += classesBean.getName()+"("+g.getName()+")";
									departname += ",";
								}

							}
						}
					}

				}



			}

		}
		List<TeacherSelectInfo.InfoBean.GroupsBean> groupsBeanList1=departs.getInfo().getGroups();
		if(groupsBeanList1.size()>0){//部分
			//部分选
			List<String> list=new ArrayList<String>();
			for (TeacherSelectInfo.InfoBean.GroupsBean g:groupsBeanList1) {
				for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX c:g.getTeacherGroups()) {
					list.add(c.getId()+"");
				}
			}

			if(type==CHOOSE_TEACHER){
				subordinateGroupMap.put("group",list);
			}

			int choose_size=0;
			for (TeacherSelectInfo.InfoBean.GroupsBean  classbean:groupsBeanList1) {
				choose_size++;
				for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX  classbeanx:classbean.getTeacherGroups()) {
					choose_size++;
				}

			}
			int size=0;
			for (TeacherSelectInfo.InfoBean.GroupsBean  classbean:ChooseTeacherActivity.teacherSelectInfo.getInfo().getGroups()) {
				size++;
				for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX  classbeanx:classbean.getTeacherGroups()) {
					size++;
				}

			}

			//负责 拼接数据 显示
			if(choose_size==size){
				departname+="教研组";
				departname += ",";
			}else {// 2级页面没有全选    2种情况:1.2级项 部分选择 则拼接所有下级   2级项 全选 则拼接2级项名字
				for (TeacherSelectInfo.InfoBean.GroupsBean g:groupsBeanList1) {//选择集合
					for (TeacherSelectInfo.InfoBean.GroupsBean c: ChooseTeacherActivity.teacherSelectInfo.getInfo().getGroups()) {
						if(g.getName().equals(c.getName())){
							if(g.getTeacherGroups().size()==c.getTeacherGroups().size()){ //判断是不是全选
								//是的话 则拼接 此name
								departname+=g.getName()+"(教研组)";
								departname += ",";
							}else {//没全选

								for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX classesBean:g.getTeacherGroups()) {
									departname += classesBean.getName()+"("+g.getName()+")";
									departname += ",";
								}

							}
						}
					}

				}



			}
		}


		Log.e("部门选择",departname);

		teacher.setText(departname+teacherName);

	/*	if(type == CHOOSE_TEACHER){

			tvTeacher.setText(departname+teacherName);
		}else if(type == CHOOSE_CC_TEACHER){

			tvCC.setText(departname+teacherName);
		}*/

	}

	protected void publish(){
		UserType user = CCApplication.getInstance().getPresentUser();
		PublishSummaryReq req = new PublishSummaryReq();

		try {
			req.setUserId(user.getUserId());
			req.setSchoolId(user.getSchoolId());
			if(info!=null){
				req.setDepartId(info.getId()+"");
				req.setDepartName(info.getDepartmentName());
			}
			String str = teacher.getText().toString();
			if(selectAll){
				List<String> common = new ArrayList<String>();
				common.add("teacherstaff");
				req.setCommonGroupList(common);

			}else{
				if(chooseTeacherList != null){
					List<String> teacherList = new ArrayList<String>();
//					Map<Long,String> map = new HashMap<Long,String>();
					for(TeacherMap teacher:chooseTeacherList){
//						map.put((long) teacher.getId(), Constants.TEACHER_STR_TYPE);
						teacherList.add(String.valueOf(teacher.getId()));
					}
//					req.setUserIdTypeMap(map);
					req.setTeacherList(teacherList);
				}

				//行政部门
				if(all_departs != null) {
					List<String> deptId = new ArrayList<String>();
					for (TeacherSelectInfo.InfoBean.DepartsBean d:all_departs.getInfo().getDeparts()) {
						deptId.add(d.getId()+"");
					}
					req.setDepartMentIdList(deptId);
				}
				//常用分组

				List<String> commonId = new ArrayList<String>();
				if(departmentcharge){
					commonId.add("departmentcharge");
				}
				if(school_leader){
					commonId.add("schoolleader");
				}
				req.setCommonGroupList(commonId);
				//有3级页面 的机构
				req.setSubordinateGroupMap(subordinateGroupMap);

			}
			if(!isSingle){
				if(ret != null){
					if(ret.getSuccFiles() != null){
						Set<String> set = ret.getSuccFiles().keySet();
						for(String string:set){
							attachment.add(string);
						}
						req.setAttachmentIdList(attachment);
					}
				}
			}else{
				req.setAttachmentIdList(attachment);
			}

			req.setSummaryText(content.getText().toString());
			req.setSummaryTitle(subject.getText().toString());
			req.setSummaryTypeCode(""+type);
			req.setSummaryType(summaryType);
			Gson gson = new Gson();
			String string = gson.toJson(req);
			JSONObject json = new JSONObject(string);
			System.out.println("PUBLISH_SUMMARY json:"+json);
			action = PUBLISH;
			queryPost(Constants.PUBLISH_SUMMARY,json);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//获取上一条记录
	private void getLastReceive() {
		JSONObject json = new JSONObject();
		UserType user = CCApplication.getInstance().getPresentUser();
		try {
			json.put("userId", user.getUserId());
			json.put("schoolId", user.getSchoolId());
			json.put("infoType", "2");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		action = LastReceive;
		showProgessBar();
		queryPost(Constants.GET_LAST_INFO, json);
	}

	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		System.out.println("data:"+data);

		switch(action){
			case PUBLISH:
				NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
				if(ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
					DataUtil.getToast(ret.getServerResult().getResultMessage());
				}else{
					DataUtil.getToast("发布成功!");
					setResult(RESULT_OK);

					finish();
				}
				break;
			case GET_DEPT_INFO:
				GetDeptInfo info1 = JsonUtils.fromJson(data, GetDeptInfo.class);
				if(info1.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
					DataUtil.getToast(info1.getServerResult().getResultMessage());
				}else{
					departList = info1.getDepartList();
					spinAdapter = new SpinAdapter(this,departList);
					spin.setAdapter(spinAdapter);
					spin.setVisibility(View.VISIBLE);
					((BaseAdapter) spinAdapter).notifyDataSetChanged();
				}
				getLastReceive();
				break;
			case LastReceive:
				String a =data;
				LastinfoSchool lastinfoSchool = JsonUtils.fromJson(data, LastinfoSchool.class);
				if (lastinfoSchool.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
					DataUtil.getToast(lastinfoSchool.getServerResult().getResultMessage());
				} else {
					LastinfoSchool.LastinfoSchoolSummarizeBean lastInfo = lastinfoSchool.getLastinfoSchoolSummarize();
					final List<String> stringList = new ArrayList<String>();
					if (lastInfo != null) {
						Date date = null;
						String time = null;
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						StringBuilder sb = new StringBuilder();
						//把list 转化为String 并且用,隔开

						if (lastInfo.getListISS() != null && lastInfo.getListISS().size() > 0) {
                             if(chooseTeacherList==null){
								 chooseTeacherList=new ArrayList<TeacherMap>();
							 }
							chooseTeacherList_record=new ArrayList<TeacherMap>();
							for (int i = 0; i < lastInfo.getListISS().size(); i++) {
								LastinfoSchool.LastinfoSchoolSummarizeBean.ListISSBean listBean = lastInfo.getListISS().get(i);
								AllTeacherList b=new AllTeacherList();
								AllTeacherList.TeacherMap t=b.new TeacherMap();
								t.setId(listBean.getUserId());
								t.setName(listBean.getUserName());
								if (i < lastInfo.getListISS().size() - 1) {
									sb.append(listBean.getUserName() + ",");
								} else {
									sb.append(listBean.getUserName());
								}
								chooseTeacherList_record.add(t);
								chooseTeacherList.add(t);
							}
						}
						//教师
						teacher.setText(sb);
						try {
							Date curDate = new Date(System.currentTimeMillis());//获取当前时间
							String str = simpleDateFormat.format(curDate);
							date = simpleDateFormat.parse(str);
							time = dateToStamp(str);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						//主题
						//subject.setText(lastInfo.getDepartName() + stampToDate(time) + "(" + getWeek(date) + ")" + "工作日志");
						//发布部门
						String departName = lastInfo.getDepartName();
						if (departList != null && departList.size() > 0) {
							for (DepartInfo departInfo : departList) {
								stringList.add(departInfo.getDepartmentName());
							}
						}
						for (int i = 0; i < stringList.size(); i++) {
							if (stringList.get(i).equals(departName)) {
								spin.setHint((stringList.get(i)));
								for (DepartInfo d:departList) {
									if(d.getDepartmentName().equals(departName)){
										info=d;
									}
								}
							}
						}
					}else{
						spin.setHint("");
					}

				}
				///判断
				if(!TextUtils.isEmpty(subject.getText().toString())&&!TextUtils.isEmpty(teacher.getText().toString())&&info!=null&&summaryType!=null){
					//其他2者都不为空是
					enter.setClickable(true);
					enter.setEnabled(true);
					enter.setTextColor(getResources().getColor(R.color.blue));
				}else{
					enter.setClickable(false);
					enter.setEnabled(false);
					enter.setTextColor(Color.GRAY);
				}
				dismissPd();


				break;
		}
	}

	  /*
  * 将时间转换为时间戳
  */

	public static String dateToStamp(String s) throws ParseException {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	/*
    * 将时间戳转换为时间
    */
	public static String stampToDate(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	//根据日期取得星期几
	public static String getWeek(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		String week = sdf.format(date);
		return week;
	}

	@Override
	public void onImageUploadCancelled() {
		// TODO Auto-generated method stub
		System.out.println("cancel!");
	}

	@Override
	public void onImageUploadComplete(String result) {
		// TODO Auto-generated method stub
		System.out.println("result:"+result+" i:"+i);

		ret = JsonUtils.fromJson(result, UploadFileResult.class);
		if(isSingle){
			i++;
			if(ret != null){
				if(ret.getSuccFiles() != null){
					Set<String> set = ret.getSuccFiles().keySet();
					for(String string:set){
						attachment.add(string);
					}
				}
			}
			if(i == imagePaths.size()){
				publish();
				i = 0;
			}
		}else
			publish();
	}
	public static <T> T deepCopy(T src) throws IOException, ClassNotFoundException{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in =new ObjectInputStream(byteIn);
		T dest = (T) in.readObject();
		return dest;
	}
}
