package com.wcyc.zigui2.newapp.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.newapp.adapter.TConfAddImageGvAdapter;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.ImagePagerActivity;

import com.wcyc.zigui2.newapp.home.TConfigrChoiceClassActivity;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.GradeClass;
import com.wcyc.zigui2.newapp.bean.GradeMap;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewCourseList;
import com.wcyc.zigui2.newapp.bean.NewPublishHomeworkBean;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.home.NewAttendanceActivity.Kq_object_bean;
import com.wcyc.zigui2.newapp.home.NewAttendanceActivity.Kq_object_retrun_bean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;
import com.wcyc.zigui2.widget.CustomDialog;
import com.wcyc.zigui2.widget.ExpandGridView;
import com.wcyc.zigui2.widget.SpinnerButton;

/**
 * 新版app作业发布界面
 * 
 * @author 郑国栋 2016-4-15
 * @version 2.0
 */
public class NewHomeworkActivity extends BaseActivity implements
		OnClickListener, ImageUploadAsyncTaskListener {

	private TextView title2_off, new_content, title2_ok;
	private String classId;// 班级ID
	private EditText ed_homework;
	private List<String> couse_name_list;
	private List<String> couse_id_list;
	String isadviser;
	private String courseId;
	private SpinnerButton spinnerButton;
	private List<NewClasses> cList;
	private List<NewClasses> cList_aa;
	private ListView spinnerListView;// 班级listview
	private int class_i = -1;
	private SpinnerButton spinnerButton_course;

	private ListView spinnerListView_course;// 选择课程
	private int class_i_course = -1;
	private ImageView ed_homework_delete_icon;

	// 图片选择
	private ExpandGridView homework_addiv_gv;
	private TConfAddImageGvAdapter gvAdapter;
	private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
	private ArrayList<String> transmitImagePaths = new ArrayList<String>();// 传递给服务器的集合
	private int nowImageNumber = 0;
	private static final int MAX_IMAGE = 6;
	private static final int REQUEST_CODE = 100;
	List<PictureURL> datas = new ArrayList<PictureURL>();// 发布消息时候，去相册选取图片后
	private boolean is_compress;
	public static final int REASON_CANCEL = 2;
	public static final String INTENT_CANCEL_UPLOAD_PICTURE = "com.wcyc.zigui.action.UPLOAD_PICTURE_CANCEL";
	private static String mMsgID;
	private List<String> attachementList = new ArrayList<String>();

	private static final int KQ_BUSIINERFACE = 1; // 获取考勤基本信息 请求后 getmassege标记为1
	private static final int PUB_HOMEWORK = 2; // 发布作业 请求后 getmassege标记为2
	private final String http_url_AttenBasicInfo = "/getAttenBasicInfo";// 获取考勤基本信息
	List<Kq_object_bean> courseList_aa = new ArrayList<NewAttendanceActivity.Kq_object_bean>();
	private boolean schooladmin=false;
	private CustomDialog dialog;
	private String null_str="";

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		setContentView(R.layout.new_teacher_homework_release);

		initView();
		initDatas();
		initEvents();

		inputState();

	}

	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);
			if (TConfAddImageGvAdapter.DELETE_IMAGES == msg.what) {
				int p = (Integer) msg.obj;
				imagePaths.remove(p);
				transmitImagePaths.remove(p);
				nowImageNumber = imagePaths.size();
				gvAdapter.notifyDataSetChanged();
			}
			
			switch (msg.what) {
			case CustomDialog.DIALOG_CANCEL:// 取消退出
				dialog.dismiss();
				break;
			case CustomDialog.DIALOG_SURE:// 确认退出
				NewHomeworkActivity.this.finish();//
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};

	// 初始化控件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);
		title2_off = (TextView) findViewById(R.id.title2_off);
		title2_ok = (TextView) findViewById(R.id.title2_ok);

		ed_homework = (EditText) findViewById(R.id.ed_homework);
		ed_homework_delete_icon = (ImageView) findViewById(R.id.ed_homework_delete_icon);
		TextFilter textFilter1 = new TextFilter(ed_homework);
//		ed_homework.addTextChangedListener(textFilter1);// 设置不能输入空格
		textFilter1.setEditeTextClearListener(ed_homework,
				ed_homework_delete_icon);

		spinnerButton = (SpinnerButton) findViewById(R.id.spinner_butt_class);
		spinnerButton_course = (SpinnerButton) findViewById(R.id.spinner_butt_course);

		homework_addiv_gv = (ExpandGridView) findViewById(R.id.homework_addiv_gv);
		gvAdapter = new TConfAddImageGvAdapter(this, imagePaths,
				getImageLoader(), handler);
		homework_addiv_gv.setAdapter(gvAdapter);

	}

	private boolean isClassIdExist(List<NewClasses> classlist, String classId) {
		for (int i = 0; i < classlist.size(); i++) {
			if (classlist.get(i).getClassID().equals(classId)) {
				return true;
			}
		}

		return false;
	}

	// 初始化数据
	private void initDatas() {
		new_content.setText("发布作业");
		// 课程名称列表 课程id列表
		couse_name_list = new ArrayList<String>();
		couse_id_list = new ArrayList<String>();

		// 判断用户类型
		String userType = CCApplication.getInstance().getPresentUser()
				.getUserType();
		
		if (!userType.equals("2")) {
			
			DataUtil.getToast("您无任教班级，不能发布作业");
			NewHomeworkActivity.this.finish();
		}else{
			
			// 活动班级列表 字段有班级id 和班级名称
			cList=new ArrayList<NewClasses>();
			cList = CCApplication.app.getMemberDetail().getClassList();// 任教班级
			
			for (int i = 0; i < CCApplication.getInstance().getMemberDetail().getRoleList().size(); i++) {
				String roleCode=CCApplication.getInstance().getMemberDetail().getRoleList().get(i).getRoleCode();
				if("schooladmin".equals(roleCode)){
					schooladmin=true;
				}
			}
			
			if(schooladmin){
				List<NewClasses> schoolAllClassList=CCApplication.getInstance().getSchoolAllClassList();
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
			
			
			
//			List<Role> roleList=CCApplication.app.getMemberDetail();
			int i=CCApplication.getInstance().getPresentUserIndex();
			CCApplication.app.getMemberDetail().getChildList();
			
			if (cList == null||cList.size()<1) {
				DataUtil.getToast("您无任教班级，不能发布作业！");
				NewHomeworkActivity.this.finish();

			}
			
			if (cList != null) {
				cList_aa = new ArrayList<NewClasses>();
				for (NewClasses classes : cList) {
					if (!isClassIdExist(cList_aa, classes.getClassID()))// 是否已存在list中
						cList_aa.add(classes);
				}
				
				for (int j = 0; j < cList.size(); j++) {//即是班主任     又是老师   如果上一步移除的班级是班主任  则将cList_aa里的这个班的老师设置回为班主任  
					for (int j2 = 0; j2 < cList_aa.size(); j2++) {
						if(cList_aa.get(j2).getClassID().equals(cList.get(j).getClassID())){
							if("1".equals(cList.get(j).getIsAdviser())){
								cList_aa.get(j2).setIsAdviser("1");
							}
						}
					}
				}

				try {
					// 获取科目基本信息
					String schoolId = CCApplication.getInstance().getPresentUser()
							.getSchoolId();
					String gradeId = cList_aa.get(0).getGradeId();
					JSONObject json = new JSONObject();
					json.put("schoolId", schoolId);
					json.put("gradeId", gradeId);
					// 请求地址
					String url = new StringBuilder(Constants.SERVER_URL).append(
							http_url_AttenBasicInfo).toString();
					String result = HttpHelper.httpPostJson(this, url, json);
					Kq_object_retrun_bean bean = JsonUtils.fromJson(result,
							Kq_object_retrun_bean.class);
					System.out.println("==bean.getCourseList()==="+bean.getCourseList()+"====");
					if(bean.getCourseList()!=null){
						courseList_aa = bean.getCourseList();
					}else{
						DataUtil.getToast("请后台先准备数据，作业科目");
					}

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			
		}

		null_str="";
		for (int j = 0; j < 10; j++) {
			null_str+="          ";
		}
	}

	private void initEvents() {
		title2_off.setOnClickListener(this);
		title2_ok.setOnClickListener(this);
		title2_ok.setClickable(false);
		title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));

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
					DataUtil.getToast(getResources().getString(
							R.string.no_network));
					return;
				}
				// 显示年级班级
				spinnerButton.setText(null_str);
				final int arg2Final=arg2;
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);//休眠1000毫秒
						} catch (Exception e) {
							e.printStackTrace();
						}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								spinnerButton.setText(cList_aa.get(arg2Final).getGradeName()
										+ cList_aa.get(arg2Final).getClassName());
							}
						});
					}
				}).start();



				class_i = arg2;// 标记选择的是哪个班

				couse_id_list.clear();
				couse_name_list.clear();

				String schoolId = CCApplication.getInstance().getPresentUser()
						.getSchoolId();
				String gradeId = cList_aa.get(arg2).getGradeId();
				httpBusiInerface_kq_base(schoolId, gradeId);// 选择班级后 获取考勤基本信息

				String isAdviser = cList_aa.get(arg2).getIsAdviser();

				choose_couser(class_i, isAdviser);

				String courese=spinnerButton_course.getText().toString();
				String ed_homework_str=ed_homework.getText().toString();
//				System.out.println("==="+courese+"==="+ed_homework_str+"===");
				if(ed_homework_str.length()>0&&courese.length()>0){
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_darkblue));
				}else {
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_lightgray));
				}
			}

		});

		homework_addiv_gv.setOnItemClickListener(new OnItemClickListener() {
			private PictureURL pictureURL;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (0 == imagePaths.size()) {// 第一次过去选择相片
					Intent intentAdd = new Intent(NewHomeworkActivity.this,
							SelectImageActivity.class);
					intentAdd.putExtra("limit", MAX_IMAGE);
					intentAdd.putStringArrayListExtra("addPic", imagePaths);
					startActivityForResult(intentAdd, REQUEST_CODE);
				} else {
					datas.clear();
					for (int i = 0; i < imagePaths.size(); i++) {
						pictureURL = new PictureURL();
						pictureURL.setPictureURL("file://" + imagePaths.get(i));
						datas.add(pictureURL);
					}
				}
				if (0 != imagePaths.size() && position == imagePaths.size()) {// 非第一次选取相片
					int limitImage = MAX_IMAGE;
					if (limitImage <= MAX_IMAGE) {
						Intent intentAdd = new Intent(NewHomeworkActivity.this,
								SelectImageActivity.class);
						intentAdd.putExtra("limit", limitImage);
						intentAdd.putStringArrayListExtra("addPic", imagePaths);
						intentAdd.putExtra("is_compress", is_compress);
						startActivityForResult(intentAdd, REQUEST_CODE);
					} else {
						DataUtil.getToast("最多不能超过" + MAX_IMAGE + "张图片");
					}
				}
				if (position != imagePaths.size()) {
					// 查看照片
					Intent intent = new Intent(NewHomeworkActivity.this,
							ImagePagerActivity.class);
					intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
							(Serializable) datas);
					intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,
							position);
					startActivity(intent);
				}
			}
		});

		if (cList_aa != null) {
			spinnerButton.setText(cList_aa.get(0).getGradeName()
					+ cList_aa.get(0).getClassName());

			choose_couser(0, cList_aa.get(0).getIsAdviser());

			if (couse_name_list != null && couse_name_list.size() > 0) {
				spinnerButton_course.setText(couse_name_list.get(0));
			}
		}
	}

	// 设置选择班级 adapter 下拉列表
	class MySpinnerAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public MySpinnerAdapter() {
			super();
			inflater = LayoutInflater.from(NewHomeworkActivity.this);
		}

		@Override
		public int getCount() {

			if (cList != null) {

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

	// 选择课程
	public void choose_couser(int class_i, String isAdviser) {

		if (cList != null) {
			if ("1".equals(isAdviser)) {
				for (int i = 0; i < courseList_aa.size(); i++) {
					String course_id = courseList_aa.get(i).getCode();// 课程id
					String course_name = courseList_aa.get(i).getName();// 课程name

					if (course_name != null && course_id != null) {
						couse_name_list.add(course_name);
						couse_id_list.add(course_id);
					}

				}

			} else if ("0".equals(isAdviser)) {
				// 活动班级列表 字段有班级id 和班级名称
				// List<NewClasses> cList_a =
				// CCApplication.app.getMemberDetail().getClassList();// 任教班级
				String class_id_selected = cList_aa.get(class_i).getClassID();
				for (int i = 0; i < cList.size(); i++) {
					String class_id_i = cList.get(i).getClassID();
					if (class_id_selected.equals(class_id_i)) {

						String course_name = cList.get(i).getCouseName();// 课程name
						String course_id = cList.get(i).getCouseId();// 课程id

						if (course_name != null && course_id != null) {
							couse_name_list.add(course_name);
							couse_id_list.add(course_id);
						}

					}
				}
			}

			if ( couse_name_list != null&&couse_name_list.size() > 0 ) {

				spinnerButton_course.setText(null_str);//清空之前的科目显示
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);//休眠1000毫秒
						} catch (Exception e) {
							e.printStackTrace();
						}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								spinnerButton_course.setText(couse_name_list.get(0));//重新设置科目
							}
						});
					}
				}).start();
				
			} else {
				spinnerButton_course.setText("");
			}

			// 课程下拉列表
			spinnerButton_course.showAble(true);
			spinnerButton_course.setResIdAndViewCreatedListener(
					R.layout.spinner_layout,
					new SpinnerButton.ViewCreatedListener() {

						@Override
						public void onViewCreated(View v) {
							spinnerListView_course = (ListView) v
									.findViewById(R.id.spinner_lv);
						}
					});

			final MySpinnerAdapter_course spinnerAdapter_course = new MySpinnerAdapter_course();
			spinnerListView_course.setAdapter(spinnerAdapter_course);// 添加打气筒
			spinnerListView_course
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							spinnerButton_course.dismiss();
							if (!DataUtil.isNetworkAvailable(getBaseContext())) {
								DataUtil.getToast(getResources().getString(
										R.string.no_network));
								return;
							}
							
							String null_str="";
							for (int j = 0; j < 10; j++) {
								null_str+="          ";
							}
							
							spinnerButton_course.setText(null_str);//清空之前的科目显示
							
							// 显示课程
							class_i_course = arg2;// 标记选择的是哪个课程

							
							new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										Thread.sleep(600);//休眠600毫秒
									} catch (Exception e) {
										e.printStackTrace();
									}
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											spinnerButton_course.setText(couse_name_list.get(class_i_course));//重新设置科目
										}
									});
								}
							}).start();
						}

					});
		}

	}

	// 设置选择课程 adapter 课程下拉列表
	class MySpinnerAdapter_course extends BaseAdapter {

		private LayoutInflater inflater;

		public MySpinnerAdapter_course() {
			super();
			inflater = LayoutInflater.from(NewHomeworkActivity.this);
		}

		@Override
		public int getCount() {
			if (couse_name_list != null) {
				return couse_name_list.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {

			return couse_name_list.get(position);

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
						parent, false);//打气筒设置布局   选择班级的   一个班级 布局 找到要显示 课程 的控件
				viewholder.course_name_show = (TextView) convertView.findViewById(R.id.class_name);//找到要显示  年级班级 的控件
				// 设置标签
				convertView.setTag(viewholder);
			}else{
				// 获得标签 如果已经实例化则用历史记录
				viewholder = (ViewHolder) convertView.getTag();
			}
			
			viewholder.course_name_show.setText(couse_name_list.get(position));// 设置显示课程
			return convertView;
		}
		
		private class ViewHolder {
			TextView course_name_show;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title2_off:
			String ed_homeworkStr=ed_homework.getText().toString();
			if(!DataUtil.isNullorEmpty(ed_homeworkStr)){
				exitOrNot();
				return;
			}
			NewHomeworkActivity.this.finish();// 取消键
			break;
		case R.id.title2_ok:// 发布作业确定键

			if (!DataUtil.isNetworkAvailable(getBaseContext())) {
				DataUtil.getToast(getResources().getString(R.string.no_network));// 当前网络不可用，请检查您的网络设置
				break;
			}

			if (DataUtil.isFastDoubleClick()) {// 多次点击判断 时间间隔两秒
				DataUtil.getToast("正在上传，请不要多次重复提交...");
				break;
			}

			// 获得作业编辑器的内容
			String content = ed_homework.getText().toString();
			if (class_i == -1) {
				class_i = 0;

			}
			if (class_i_course == -1) {
				class_i_course = 0;
			}
			if (content.length() == 0) {
				DataUtil.getToast("请输入作业内容");
			} else {

				// 上传图片 如果成功执行... 如果失败执行....
				ImageUploadAsyncTask upload = new ImageUploadAsyncTask(this,
						"1", imagePaths, Constants.UPLOAD_URL, this);
				upload.execute();
			}
			break;

		}
	}
	
	private void exitOrNot(){
		dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.customdialog, handler);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		dialog.setTitle("退出此次编辑?");
		dialog.setContent("");
		
//		handler.
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			String ed_homeworkStr=ed_homework.getText().toString();
			if(!DataUtil.isNullorEmpty(ed_homeworkStr)){
				exitOrNot();
				return true;
			}	
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void getMessage(String data) {

		switch (action) {
		case KQ_BUSIINERFACE:// 1 表示 获取考勤基础信息

			try {
				// 用实体类接收 考勤基础信息接口 返回的数据
				Kq_object_retrun_bean bean = JsonUtils.fromJson(data,
						Kq_object_retrun_bean.class);
				courseList_aa = bean.getCourseList();

			} catch (Exception e) {

			}
			break;
		case PUB_HOMEWORK:// 发布作业
			// 请求结果
			NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);

			if (ret.getServerResult().getResultCode() != 200) {// 失败
				DataUtil.getToast(ret.getServerResult().getResultMessage());
			} else {// 成功

				DataUtil.getToast("发布作业成功!");
				
				// 通知BaseWebView  activity刷新数据
				Intent broadcast = new Intent(
						BaseWebviewActivity.INTENT_REFESH_DATA);
				sendBroadcast(broadcast);

				NewHomeworkActivity.this.finish();
			}

			break;
		}

	}

	// 请求 考勤基本信息
	private void httpBusiInerface_kq_base(String schoolId, String gradeId) {
		try {

			JSONObject json = new JSONObject();
			json.put("schoolId", schoolId);
			json.put("gradeId", gradeId);

			// 请求地址    方法二   用同步
			String url = new StringBuilder(Constants.SERVER_URL).append(
					http_url_AttenBasicInfo).toString();
			String result = HttpHelper.httpPostJson(this, url, json);
			Kq_object_retrun_bean bean = JsonUtils.fromJson(result,
					Kq_object_retrun_bean.class);

			if(courseList_aa!=null){
				courseList_aa.clear();
			}
			
			if(bean.getCourseList()!=null){
				courseList_aa = bean.getCourseList();
			}else{
				DataUtil.getToast("请后台先准备数据，作业科目");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void httpBusiInerface() {
		// 获得作业编辑器的内容
		String content = ed_homework.getText().toString();
		if (class_i == -1) {
			DataUtil.getToast("请选择班级");
		} else if (class_i_course == -1) {
			DataUtil.getToast("请选择课程");
		} else if (content.length() == 0) {
			DataUtil.getToast("请输入作业内容");
		} else {
			classId = cList_aa.get(class_i).getClassID();
			courseId = couse_id_list.get(class_i_course);

			// attachementList picNum
			String picNum = "0";
			if (attachementList.size() > 0) {
				picNum = attachementList.size() + "";

			}

			// 对象设置数据
			NewCourseList courseList_Object = new NewCourseList();
			courseList_Object.setCourseId(courseId);
			courseList_Object.setContent(content);
			courseList_Object.setPicNum(picNum);
			if (attachementList.size() > 0) {
				courseList_Object.setAttachmentIdList(attachementList);
			}

			// 封装对象
			List<NewCourseList> courseList = new ArrayList<NewCourseList>();
			courseList.add(courseList_Object);

			// 作业
			NewPublishHomeworkBean homework = new NewPublishHomeworkBean();
			homework.setClassId(classId);
			homework.setIsAllClass("1");
			homework.setCourseName(couse_name_list.get(class_i_course));
			homework.setCourseList(courseList);

//			if (attachementList.size() > 0) {
//				homework.setAttachmentIdList(attachementList);
//			}

			// json有引用类型时， 引用类型转Gson--在Gson转字符串--在字符串转json
			// 才能得到json格式的字符串
			Gson gson = new Gson();
			String string = gson.toJson(homework);
			JSONObject json;
			try {
				json = new JSONObject(string);
				System.out.println("发布作业 json====" + json);
				queryPost(Constants.PUBLISH_HOMEWORK, json);
				action = PUB_HOMEWORK;

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

	}

	

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (TConfigrChoiceClassActivity.BACK_RESULT == resultCode) {
		}
		if (RESULT_OK == resultCode) {
			ArrayList<String> images = data.getExtras().getStringArrayList(
					"pic_paths");// 每次选择相片返回的数据
			if (null != images) {
				String title = "file://";
				transmitImagePaths.clear();
				imagePaths.clear();
				for (String str : images) {
					transmitImagePaths.add(title + str);
				}
				imagePaths.addAll(images);
			}
			if (null != imagePaths) {
				nowImageNumber = imagePaths.size();
			}
			gvAdapter.setData(transmitImagePaths);
			gvAdapter.notifyDataSetChanged();
			is_compress = data.getBooleanExtra("is_compress", true);
		}
	}

	@Override
	public void onImageUploadCancelled() {
		DataUtil.getToast("发送失败，请稍后再试");
	}

	@Override
	public void onImageUploadComplete(String result) {

		// DataUtil.getToast("发布作业图片成功");
		UploadFileResult ret = JsonUtils.fromJson(result,
				UploadFileResult.class);

		try {

			Set<String> set = ret.getSuccFiles().keySet();
			for (String string : set) {
				attachementList.add(string);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		httpBusiInerface();

	}
	
	private void inputState() {
		
		ed_homework.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String courese=spinnerButton_course.getText().toString().trim();

				if (s.length() > 0&&!DataUtil.isNullorEmpty(courese)) {
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_darkblue));
				} else {
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_lightgray));
				}

				if (s.length() > 499) {
					DataUtil.getToast("作业内容不能超过500个字");
					String temp=ed_homework.getText().toString();
					temp=temp.substring(0,499);
					ed_homework.setText(temp);
//					ed_homework.requestFocus();
				}
			}

		});

	}

}
