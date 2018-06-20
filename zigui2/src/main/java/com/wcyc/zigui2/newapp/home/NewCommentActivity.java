package com.wcyc.zigui2.newapp.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.listener.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.GradeClass;
import com.wcyc.zigui2.newapp.bean.GradeMap;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewPublishStuentCommentBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;
import com.wcyc.zigui2.widget.SpinnerButton;

/**
 * 新版app 点评   发布界面
 * 
 * @author 郑国栋 2016-4-18
 * @version 2.0
 */

public class NewCommentActivity extends BaseActivity implements
		OnClickListener, ImageUploadAsyncTaskListener {

//	private final String http_url = "/publishStuentComment";
	private TextView title2_off, new_content, title2_ok;
	private EditText ed_comment;
	private Spinner sp_class;
	private List<NewClasses> classList;
	private ArrayList<String> classList_names;
	private ArrayList<String> couse_name_list;
	private ArrayAdapter<String> class_list_adapter;
	int selected_class_index;
	private String classId;
	private String ed_comment_content;
	private ImageView comment_select_student;
	private ArrayList<String> student_id_List_checked;
	private ArrayList<String> student_name_List_checked;
	private TextView add_student_tv;
	private SpinnerButton spinnerButton;
	private ListView spinnerListView;// 班级listview
	private List<NewClasses> cList;// 任教班级
	private List<NewClasses> cList_aa;
	private int class_i = -1;
	private ImageView ed_comment_delete_icon;
	private ImageView comment_add_xiaohonghua_iv;
	private ImageView comment_add_jiayou_iv;
	private ImageView comment_add_pipin_iv;
	private String picId;
	private HashMap<Integer, Boolean> isSelected=new HashMap<Integer, Boolean>();
	private boolean schooladmin=false;


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		setContentView(R.layout.new_teacher_comment_release);

		initView();
		initDatas();
		initEvents();
		
		inputState();
	}

	private void initView() {
		student_id_List_checked = new ArrayList<String>();
		student_name_List_checked = new ArrayList<String>();
		spinnerButton = (SpinnerButton) findViewById(R.id.spinner_butt_class);
		title2_off = (TextView) findViewById(R.id.title2_off);
		new_content = (TextView) findViewById(R.id.new_content);
		title2_ok = (TextView) findViewById(R.id.title2_ok);
		
		ed_comment = (EditText) findViewById(R.id.ed_comment);
		ed_comment_delete_icon = (ImageView) findViewById(R.id.ed_comment_delete_icon);
		TextFilter textFilter1 = new TextFilter(ed_comment);
//		ed_comment.addTextChangedListener(textFilter1);// 设置不能输入空格
		textFilter1.setEditeTextClearListener(ed_comment,
				ed_comment_delete_icon);
		
		comment_select_student = (ImageView) findViewById(R.id.comment_select_student);
		add_student_tv = (TextView) findViewById(R.id.add_student_tv);
		
		comment_add_xiaohonghua_iv = (ImageView)findViewById(R.id.comment_add_xiaohonghua_iv);
		comment_add_jiayou_iv = (ImageView)findViewById(R.id.comment_add_jiayou_iv);
		comment_add_pipin_iv = (ImageView)findViewById(R.id.comment_add_pipin_iv);
		
	}

	private void initDatas() {
		new_content.setText("发布点评");
		// 判断用户类型
		String userType = CCApplication.getInstance().getPresentUser().getUserType();
		System.out.println("当前用户角色  下标======="+userType);
		 if(!userType.equals("2")){
			 DataUtil.getToast("您不是老师或者您选择的身份不是老师，不能发布点评");
			 NewCommentActivity.this.finish();
		 }

		// 活动班级列表 字段有班级id 和班级名称
		cList=new ArrayList<NewClasses>();
		cList = CCApplication.app.getMemberDetail().getClassList();// 任教班级

		for (int i = 0; i < CCApplication.getInstance().getMemberDetail().getRoleList().size(); i++) {
			String roleCode=CCApplication.getInstance().getMemberDetail().getRoleList().get(i).getRoleCode();
			if("schooladmin".equals(roleCode)){
				schooladmin=true;
			}
		}
		
		if(schooladmin){List<NewClasses> schoolAllClassList=CCApplication.getInstance().getSchoolAllClassList();
			if(schoolAllClassList!=null&&schoolAllClassList.size()>0){
				cList=schoolAllClassList;
			}else{
				try {
					JSONObject json = new JSONObject();
					json.put("isNeedAllGrade", "1");
					String url = new StringBuilder(Constants.SERVER_URL).append(
							"/getGradeClassView").toString();//
					String result = HttpHelper.httpPostJson(this, url, json);
					GradeClass gradeClass = JsonUtils.fromJson(result, GradeClass.class);
					List<GradeMap> gradeList = gradeClass.getGradeMapList();
					if(gradeList!=null&&gradeList.size()>0){
						if(cList==null||cList.size()==0){
							cList=new ArrayList<NewClasses>();
						}
						for (int i = 0; i < gradeList.size(); i++) {
							JSONObject jsonB = new JSONObject();
							jsonB.put("gradeId", gradeList.get(i).getId());
							jsonB.put("isNeedAllGrade", "0");
							String urlB = new StringBuilder(Constants.SERVER_URL).append(
									"/getGradeClassView").toString();//
							String resultB = HttpHelper.httpPostJson(this, urlB, jsonB);
							GradeClass gradeClassB = JsonUtils.fromJson(resultB, GradeClass.class);
							List<ClassMap> classList=gradeClassB.getClassMapList();
							for (int j = 0; j < classList.size(); j++) {
								NewClasses newClasses=new NewClasses();
								newClasses.setClassID(classList.get(j).getId()+"");
								newClasses.setClassName(classList.get(j).getName());
								newClasses.setGradeId(gradeList.get(i).getId()+"");
								newClasses.setGradeName(gradeList.get(i).getName());
								newClasses.setIsAdviser("1");
								
								cList.add(newClasses);
							}
						}
					}
					CCApplication.getInstance().setSchoolAllClassList(cList);
				} catch (Exception e) {
				}
			}
		}

		if(cList==null||cList.size()<1){
			DataUtil.getToast("您无任教班级，不能发布点评！");
			NewCommentActivity.this.finish();
		}

	}

	private void initEvents() {
		title2_off.setOnClickListener(this);
		title2_ok.setOnClickListener(this);
		title2_ok.setClickable(false);
		title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
		
		comment_add_xiaohonghua_iv.setOnClickListener(this);
		comment_add_jiayou_iv.setOnClickListener(this);
		comment_add_pipin_iv.setOnClickListener(this);
		
		comment_select_student.setOnClickListener(this);

		// 班级下拉列表
		spinnerButton.showAble(true);
		spinnerButton.setResIdAndViewCreatedListener(R.layout.spinner_layout,
				new SpinnerButton.ViewCreatedListener() {

					@Override
					public void onViewCreated(View v) {
						spinnerListView = (ListView) v
								.findViewById(R.id.spinner_lv);
					}
				});

		final MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter();
		spinnerListView.setAdapter(spinnerAdapter);// 添加打气筒
		spinnerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				spinnerButton.dismiss();
				if (!DataUtil.isNetworkAvailable(getBaseContext())) {
					DataUtil.getToast(getResources().getString(R.string.no_network));
					return;
				}
				// 显示年级班级
				spinnerButton.setText(cList_aa.get(arg2).getGradeName()
						+ cList_aa.get(arg2).getClassName());
				
				add_student_tv.setText("");
				isSelected.clear();
				
				class_i = arg2;// 标记选择的是哪个班
			}

		});

	}
	
	private boolean isClassIdExist(List<NewClasses> classlist,String classId){
		for (int i = 0; i < classlist.size(); i++) {
			if(classlist.get(i).getClassID().equals(classId)){
				return true;
			}
		}
		
		return false;
	}

	// 设置选择班级 adapter 下拉列表
	class MySpinnerAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public MySpinnerAdapter() {
			super();
			inflater = LayoutInflater.from(NewCommentActivity.this);
		}

		@Override
		public int getCount() {
			
			if (cList != null) {
				cList_aa = new ArrayList<NewClasses>();
				
				for(NewClasses classes:cList){
					if(!isClassIdExist(cList_aa,classes.getClassID()))//是否已存在list中
						cList_aa.add(classes);
				}
				
				return cList_aa.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {

			return cList_aa.get(position);

		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewholder = null;
			if (convertView == null) {
				// 实例化控件
				viewholder = new ViewHolder();
				// 配置单个item的布局
				convertView = inflater.inflate(R.layout.new_class_list_name,
						parent, false);//打气筒设置布局   选择班级的   一个班级 布局
				viewholder.bzr_class_name = (TextView) convertView.findViewById(R.id.class_name);//找到要显示  年级班级 的控件
				// 设置标签
				convertView.setTag(viewholder);
			}else{
				// 获得标签 如果已经实例化则用历史记录
				viewholder = (ViewHolder) convertView.getTag();
			}
			
			viewholder.bzr_class_name.setText(cList_aa.get(position).getGradeName()
					+ cList_aa.get(position).getClassName());// 设置显示年级班级
			return convertView;
		}
		private class ViewHolder {
			TextView bzr_class_name;
		}

	}

	@Override
	public void onImageUploadComplete(String result, String ID) {

	}

	@Override
	public void onImageUploadCancelled() {

	}

	@Override
	public void onImageUploadComplete(String result) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title2_off:
			NewCommentActivity.this.finish();// 取消键
			break;
		case R.id.title2_ok:// 发布点评确定键
			httpBusiInerface();
			break;
		case R.id.comment_add_xiaohonghua_iv://已经确认  1点赞    2加油   3批评  4小红花
			
			if("4".equals(picId)){
				picId=null;
				comment_add_xiaohonghua_iv.setImageResource(R.drawable.icon_xiaohonghua_normal);
			}else {
			
				picId="4";
				comment_add_xiaohonghua_iv.setImageResource(R.drawable.icon_xiaohonghua_pressed);
				comment_add_jiayou_iv.setImageResource(R.drawable.icon_jiayou_normal);
				comment_add_pipin_iv.setImageResource(R.drawable.icon_pipin_normal);
			}
			
			break;
		case R.id.comment_add_jiayou_iv:
			if("2".equals(picId)){
				picId=null;
				comment_add_jiayou_iv.setImageResource(R.drawable.icon_jiayou_normal);
			}else{
				picId="2";
				comment_add_xiaohonghua_iv.setImageResource(R.drawable.icon_xiaohonghua_normal);
				comment_add_jiayou_iv.setImageResource(R.drawable.icon_jiayou_pressed);
				comment_add_pipin_iv.setImageResource(R.drawable.icon_pipin_normal);
			}

			break;
		case R.id.comment_add_pipin_iv:
			if("3".equals(picId)){
				picId=null;
				comment_add_pipin_iv.setImageResource(R.drawable.icon_pipin_normal);
			}else{
				picId="3";
				comment_add_xiaohonghua_iv.setImageResource(R.drawable.icon_xiaohonghua_normal);
				comment_add_jiayou_iv.setImageResource(R.drawable.icon_jiayou_normal);
				comment_add_pipin_iv.setImageResource(R.drawable.icon_pipin_pressed);
			}
			

			
			break;
		case R.id.comment_select_student:
			
			if(class_i==-1){
				DataUtil.getToast("请选择班级");
			}else{
				
				// 去选择学生界面
				// newActivity(NewSelectStudentActivity.class, null);
				String class_id=cList_aa.get(class_i).getClassID();
				System.out.println("class_id===="+class_id);
				
				Intent intent = new Intent();
				intent.putExtra("classId", class_id);
				intent.putExtra("isSelected", isSelected);
				
				intent.setClass(NewCommentActivity.this,
						NewSelectStudentActivity.class);
				startActivityForResult(intent, 2);
			}
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		super.onActivityResult(requestCode, resultCode, intent);

		switch (resultCode) {
		case RESULT_OK:// 从学生界面返回
			if (intent != null) {
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					// 获得从学生界面返回时携带的数据

					
					student_id_List_checked = bundle
							.getStringArrayList("student_id_List_checked");
					student_name_List_checked = bundle
							.getStringArrayList("student_name_List_checked");
					isSelected = (HashMap<Integer, Boolean>) bundle
							.getSerializable("isSelected");


					String student_name = "";
					if (student_id_List_checked.size() != 0) {
						for (String student_id_checkd : student_id_List_checked) {
							System.out.println("返回的被选中学生=id=="
									+ student_id_checkd);

						}
						System.out.println("student_id_List_checked====有数据");
					} else {
						System.out.println("student_id_List_checked======无数据");
					}
					if (student_name_List_checked.size() != 0) {
						for (int i = 0; i < student_name_List_checked.size(); i++) {
							student_name += student_name_List_checked.get(i);
							student_name += ",";
						}
					}
					add_student_tv.setText(student_name);
				}
			}
		}
	}

	@Override
	protected void getMessage(String data) {

		// 请求结果
		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);

		if (ret.getServerResult().getResultCode() != 200) {// 失败
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		} else {// 成功
			DataUtil.getToast("发布点评成功");
//			BaseWebviewActivity.this.finish();	
//			GoHtml5Function.goToHtmlApp(this, "点评");//这句不要了  BaseWebView重写了 重新获得焦点效果  onResume
			// 通知BaseWebView  activity刷新数据
			Intent broadcast = new Intent(
					BaseWebviewActivity.INTENT_REFESH_DATA);
			sendBroadcast(broadcast);
			
			
			NewCommentActivity.this.finish();
			
			
		}

	}

	// 封装点评业务逻辑
	private void httpBusiInerface() {
		ed_comment_content = ed_comment.getText().toString();
		if(class_i==-1){
			DataUtil.getToast("请选择班级");
		}else if (student_id_List_checked.size() == 0) {
			DataUtil.getToast("请选择点评的学生");
		} else if (ed_comment_content.length() == 0) {
			DataUtil.getToast("请输入评论");
		} else if(DataUtil.isNullorEmpty(picId)){
			DataUtil.getToast("请选则图片(小红花、加油、批评)");
		}else {		
			classId = cList_aa.get(class_i).getClassID();
			String userId=CCApplication.getInstance().getPresentUser().getUserId();
			String userName=CCApplication.getInstance().getMemberInfo().getUserName();
			NewPublishStuentCommentBean comment_bean = new NewPublishStuentCommentBean();

			comment_bean.setClassId(classId);
			comment_bean.setStudentIdList(student_id_List_checked);
			
			if(!DataUtil.isNullorEmpty(picId)){
			 comment_bean.setPicId(picId);
			}
			
			comment_bean.setNum(student_id_List_checked.size() + "");
			comment_bean.setContent(ed_comment_content);
			comment_bean.setUserId(userId);
			comment_bean.setUserName(userName);

			Gson gson = new Gson();
			String string = gson.toJson(comment_bean);
			JSONObject json;
			try {
				json = new JSONObject(string);
				System.out.println("发布点评 json====" + json);
				queryPost(Constants.PUBLISH_STUENT_COMMENT, json);
			} catch (Exception e) {

				e.printStackTrace();
			}
		
		}

	}
	
	
	private void inputState() {
		
		
		spinnerButton.addTextChangedListener(new TextWatcher() {

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
				
				String spinnerButton_str=spinnerButton.getText().toString().trim();
				String add_student_tv_str=add_student_tv.getText().toString().trim();
				String ed_comment_str=ed_comment.getText().toString();
				
				if (s.length() > 0&&!DataUtil.isNullorEmpty(add_student_tv_str)&&!DataUtil.isNullorEmpty(ed_comment_str)) {
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
				}else{
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
				}
				
			}
			
		});
		
		
		
		
		
		add_student_tv.addTextChangedListener(new TextWatcher() {

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
				
				String spinnerButton_str=spinnerButton.getText().toString().trim();
				String add_student_tv_str=add_student_tv.getText().toString().trim();
				String ed_comment_str=ed_comment.getText().toString();
				
				if (s.length() > 0&&!DataUtil.isNullorEmpty(spinnerButton_str)&&!DataUtil.isNullorEmpty(ed_comment_str)) {
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
				}else{
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
				}
				
			}
			
		});
		
		
		
		ed_comment.addTextChangedListener(new TextWatcher() {

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
				
				String spinnerButton_str=spinnerButton.getText().toString().trim();
				String add_student_tv_str=add_student_tv.getText().toString().trim();
				
				if (s.length() > 0&&!DataUtil.isNullorEmpty(spinnerButton_str)&&!DataUtil.isNullorEmpty(add_student_tv_str)) {
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
				}else{
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
				}
				
				if(s.length()>499){
					DataUtil.getToast("点评内容不能超过500个字");
					ed_comment.requestFocus();
				}
				
			}
			
		});

	}	

}
