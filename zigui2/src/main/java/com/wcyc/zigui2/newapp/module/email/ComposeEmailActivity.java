package com.wcyc.zigui2.newapp.module.email;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;
import com.wcyc.zigui2.chooseContact.ChooseStudentActivity;
import com.wcyc.zigui2.chooseContact.ChooseStudentByClassAdminActivity;
import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.adapter.AttachmentListAdapter;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllDeptList;
import com.wcyc.zigui2.newapp.bean.AllDeptList.CommonGroup;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.TeacherSelectInfo;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.bean.AllDeptList.ContactGroupMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.DepMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.GradeMap;
import com.wcyc.zigui2.newapp.bean.AllTeacherList.TeacherMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.home.NewAttendanceActivity;
import com.wcyc.zigui2.newapp.home.NewSelectStudentActivity;
import com.wcyc.zigui2.newapp.module.email.CreateEmailReq.CCTeacher;
import com.wcyc.zigui2.newapp.module.email.CreateEmailReq.CCTeacherDept;
import com.wcyc.zigui2.newapp.module.email.CreateEmailReq.Teacher;
import com.wcyc.zigui2.newapp.module.email.CreateEmailReq.TeacherDept;
import com.wcyc.zigui2.newapp.module.email.SaveDraftPop.OnSaveDraft;

import com.wcyc.zigui2.newapp.module.email.NewMailInfo.Attachment;
import com.wcyc.zigui2.newapp.module.email.NewMailInfo.Recipient;
import com.wcyc.zigui2.newapp.module.notice.PublishNotifyActivity;
import com.wcyc.zigui2.newapp.widget.CustomWebView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.SlipButton;
import com.wcyc.zigui2.utils.SlipButton.OnChangedListener;
import com.wcyc.zigui2.widget.CustomDialog;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

//import jp.wasabeef.richeditor.RichEditor;

public class ComposeEmailActivity extends BaseActivity 
	implements ImageUploadAsyncTaskListener,OnSaveDraft{
	

	private TeacherSelectInfo all_departs;
	private TeacherSelectInfo CCall_departs;
	private TextView cancel,enter;
	private ImageButton addST,addTeacher,addCC;
	private Button addAttach;
	private SlipButton messageAlert;
	private TextView tvTeacher,tvStudent,tvCC;
	private EditText subject, content;
//	private RichEditor editContent;
	private WebView originContent;
	private NewMailInfo data;//原来邮件的内容
	private int opertion = -1;//如何操作
	private String RETURN_LINE = "<br>";
	private static final int PICK_PICTURE = 100;
	private static final int CHOOSE_TEACHER = 101;
	private static final int CHOOSE_STUDENT = 102;
	private static final int CHOOSE_CC_TEACHER = 103;
	
	private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
	private AttachmentListAdapter adapter;
	private List<ContactGroupMap> contactList;
	private List<GradeMap> gradeList;
	private List<CommonGroup> commonList;
	private List<DepMap> deptList;
	private List<TeacherMap> chooseTeacherList; //选择老师
	private AllDeptList allList;//选择老师群组
	
	private List<CommonGroup> commonCCList;
	private List<TeacherMap> chooseCCTeacherList; //选择抄送老师
	private List<TeacherMap> chooseCCTeacherList_record; //选择抄送老师 记录
	private List<TeacherMap> chooseTeacherList_record; //选择老师 记录
	private AllDeptList allCCList;//选择抄送老师群组
	
	private List<ClassMap> chooseClassList;//选择学生班级
	private List<Student> chooseStudentList;//选择学生
	private ListView listView;
	private UploadFileResult ret;
	private int i;

	private List<NewClasses> classAdmin;//老师所任教的班级信息
	private List<String> attachment = new ArrayList<String>();
	private boolean isChecked,isStudentChoose,isTeacherChoose,
		isCCChoose,isSubjectWrite;
	private boolean isQuickPublish = false;//是否快捷发布
	private CustomDialog dialog;
	private String DIRECT_SEND = "1";
	private String SAVE_DARFT = "0";
	private boolean CC_selectAll;
	private boolean selectAll;
	private boolean school_leader;
	private boolean departmentcharge;
	private boolean CC_school_leader;
	private boolean CC_departmentcharge;
	private TeacherSelectInfo teacherSelectInfo_record=null;
	private TeacherSelectInfo CC_teacherSelectInfo_record=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_email);

		parseIntent();
		initView();
		fillData();
		initEvent();
	}
	
	private void parseIntent(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			data = (NewMailInfo) bundle.getSerializable("originMail");
			opertion = (int) bundle.getLong("operation");
			isQuickPublish = intent.getBooleanExtra("quickPublish",false);
		}
	}
	
	private void initView(){
		TextView title = (TextView) findViewById(R.id.new_content);
		title.setText("写信");
		cancel = (TextView) findViewById(R.id.title2_off);
		enter = (TextView) findViewById(R.id.title2_ok);
		
		addST = (ImageButton)findViewById(R.id.add_student_bt);
		addTeacher = (ImageButton)findViewById(R.id.add_teacher_bt);
		addCC = (ImageButton)findViewById(R.id.add_cc_bt);
		addAttach = (Button)findViewById(R.id.add_attach);
		messageAlert = (SlipButton)findViewById(R.id.message_alert);
		tvTeacher = (TextView) findViewById(R.id.add_teacher);
		tvStudent = (TextView) findViewById(R.id.add_student);
		tvCC = (TextView) findViewById(R.id.add_cc);
		content = (EditText) findViewById(R.id.content);
//		editContent = (RichEditor)findViewById(R.id.editcontent);
		originContent = (WebView) findViewById(R.id.origin_content);
		subject = (EditText) findViewById(R.id.subject);
		listView = (ListView) findViewById(R.id.attachList);
	}
	
	private boolean validInput(){
		if(subject.getText().length() > 0)
			isSubjectWrite = true;
		return tvTeacher.getText().length() > 0
				|| tvStudent.getText().length() > 0
				|| tvCC.getText().length() > 0;
	}
	//是否有编辑
	private boolean isInput(){
		return tvTeacher.getText().length() > 0
				|| tvStudent.getText().length() > 0
				|| tvCC.getText().length() > 0
				|| content.getText().length() > 0
				|| subject.getText().length() > 0
				|| (listView.getAdapter() != null
				&& listView.getAdapter().getCount() > 0);
	}

	private boolean hasContent(){
		return content.getText().length() > 0
				|| (listView.getAdapter() != null
				&& listView.getAdapter().getCount() > 0);
	}

	private void fillData(){
		switch(opertion){
		case EmailDetailActivity.REPLY:
			fillReplyData();
			fillReplyRecipient();
			break;
		case EmailDetailActivity.REPLY_ALL:
			fillReplyAllData();
			fillChooseContact();
			break;
		case EmailDetailActivity.FORWARD:
			fillForwardData();
			break;
		case EmailDetailActivity.EDIT:
			fillEditData();
			fillChooseContact();
			break;	
		}
		if(validInput() == false){
			enter.setEnabled(false);
			enter.setTextColor(Color.GRAY);
		}else{
			enter.setEnabled(true);
			enter.setTextColor(getResources().getColor(R.color.blue));
		}
		
	}
	
	private void fillReplyRecipient(){
		if(data == null) return;
		//须判断发件人身份来决定放到哪种收件人列表中
		String userType = data.getUserType();
		if(userType != null){
			if(userType.equals(Constants.STUDENT_STR_TYPE)){
				chooseStudentList = new ArrayList<Student>();
				Student student = new ClassStudent(). new Student();
				String id = data.getCreateUserId();
				student.setId(Integer.parseInt(id));
				student.setName(data.getCreateUserName());
				chooseStudentList.add(student);
				tvStudent.setText(data.getCreateUserName());
			}else if(userType.equals(Constants.TEACHER_STR_TYPE)){
				chooseTeacherList = new ArrayList<TeacherMap>();
				chooseTeacherList_record=new ArrayList<TeacherMap>();
				TeacherMap teacher  = new AllTeacherList().new TeacherMap();
				String id = data.getCreateUserId();
				teacher.setId(Integer.parseInt(id));
				teacher.setName(data.getCreateUserName());
				chooseTeacherList.add(teacher);
				chooseTeacherList_record.add(teacher);
				tvTeacher.setText(data.getCreateUserName());
			}
		}
	}
	
	private void fillChooseContact(){
		if(data == null) return;
		List<Recipient> teacherList = data.getTeacherRealation();
		if(teacherList != null){
			chooseTeacherList = new ArrayList<TeacherMap>();
			chooseTeacherList_record=new ArrayList<TeacherMap>();
			for(Recipient item:teacherList){
				TeacherMap teacher  = new AllTeacherList().new TeacherMap();
				String id = item.getReceiveObjectId();
				teacher.setId(Integer.parseInt(id));
				teacher.setName(item.getReceiveObjectName());
				chooseTeacherList.add(teacher);
				chooseTeacherList_record.add(teacher);
			}
		}
		List<Recipient> teacherCCList = data.getCopyRealation();
		if(teacherList != null){
			chooseCCTeacherList = new ArrayList<TeacherMap>();
			chooseCCTeacherList_record=new ArrayList<TeacherMap>();
			for(Recipient item:teacherCCList){
				TeacherMap teacher  = new AllTeacherList().new TeacherMap();
				String id = item.getReceiveObjectId();
				teacher.setId(Integer.parseInt(id));
				teacher.setName(item.getReceiveObjectName());
				chooseCCTeacherList.add(teacher);
				chooseCCTeacherList_record.add(teacher);
			}
		}
		// 教师 加上发件人 id
		TeacherMap teacher  = new AllTeacherList().new TeacherMap();
		teacher.setId(Integer.parseInt(data.getCreateUserId()));
		teacher.setName(data.getCreateUserName());
		chooseTeacherList_record.add(teacher);
		List<Recipient> studentList = data.getStudentRealation();
		if(studentList != null){
			chooseStudentList = new ArrayList<Student>();
			for(Recipient item:studentList){
				Student student = new ClassStudent(). new Student();
				String id = item.getReceiveObjectId();
				student.setId(Integer.parseInt(id));
				student.setName(item.getReceiveObjectName());
				chooseStudentList.add(student);
			}
		}
	}
	
	private void fillReplyData(){
		subject.setText("回复："+data.getTitle());
		fillOriginEmail();
	}
	
	private void fillReplyAllData(){
		fillRecipient(false);
		subject.setText("回复："+data.getTitle());
		fillOriginEmail();
	}
	
	private void fillRecipient(boolean isEdit){
		List<Recipient> teacherList = data.getTeacherRealation();
		String teacherName = EmailDetailActivity.getListStringName(teacherList); 
		if(!DataUtil.isNullorEmpty(teacherName)){
			if(isEdit){
				tvTeacher.setText(teacherName);
			}else{
				tvTeacher.setText(data.getCreateUserName()+","+teacherName);
			}
		}
		List<Recipient> studentList = data.getStudentRealation();
		String studentName = EmailDetailActivity.getListStringName(studentList);
		if(!DataUtil.isNullorEmpty(studentName)){
			tvStudent.setText(studentName);
		}
		List<Recipient> ccList = data.getCopyRealation();
		String ccName = EmailDetailActivity.getListStringName(ccList);
		if(!DataUtil.isNullorEmpty(ccName)){
			tvCC.setText(ccName);
		}
	}
	private void fillForwardData(){
		//须判断发件人身份来决定放到哪种收件人列表中
		subject.setText("转发："+data.getTitle());
		fillOriginEmail();
	}
	
	private void fillEditData(){
		//须判断发件人身份来决定放到哪种收件人列表中
		String str = "";
		subject.setText(data.getTitle());
		if(data.getContent() != null){
			str += Html.fromHtml(data.getContent());
		}
		content.setText(str);
//		editContent.setHtml(data.getContent());
		fillRecipient(true);
	}

	private void fillAttachment(){
		List<Attachment> attachmentList = data.getListSAI();
	}

	private String fillOriginEmail(){
		if(data == null) return "";
		String email = RETURN_LINE+RETURN_LINE
				+"-----------------原始邮件-----------------"+RETURN_LINE;//空行
		String sender = "发件人："+data.getCreateUserName() + RETURN_LINE;
		email += sender;
		List<Recipient> teacherList = data.getTeacherRealation();
		String teacherName = EmailDetailActivity.getListStringName(teacherList); 
		if(!DataUtil.isNullorEmpty(teacherName)){
			teacherName = "教师：" + teacherName + RETURN_LINE;
			email += teacherName;
		}
		List<Recipient> ccList = data.getCopyRealation();
		String ccName = EmailDetailActivity.getListStringName(ccList);
		if(!DataUtil.isNullorEmpty(ccName)){
			ccName = "抄送：" + ccName + RETURN_LINE;
			email += ccName;
		}
		List<Recipient> studentList = data.getStudentRealation();
		String studentName = EmailDetailActivity.getListStringName(studentList);
		if(!DataUtil.isNullorEmpty(studentName)){
			studentName = "学生：" + studentName + RETURN_LINE;
			email += studentName;
		}
		
		List<Attachment> attachList = data.getListSAI();
		String attachName = EmailDetailActivity.getListAttachName(attachList);
		if(!DataUtil.isNullorEmpty(attachName)){
			attachName = "附件：" + attachName + RETURN_LINE;
			email += attachName;
		}
		
		String time = data.getCreateTime();
		if(!DataUtil.isNullorEmpty(time)){
			time = "时间：" + time + RETURN_LINE;
			email += time;
		}
		String subject = data.getTitle();
		if(!DataUtil.isNullorEmpty(subject)){
			subject = "主题：" + subject + RETURN_LINE + RETURN_LINE;
			email += subject;
		}
		if(data.getContent() != null){
			email += data.getContent();
		}
//		content.setText(email);
		if(email != null && email.length() > 0) {
//			DataUtil.showHtmlSetting(originContent, email);
			new CustomWebView(this,originContent,email);
		}
		return email;
	}
	
	private void initEvent(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int count = parent.getAdapter().getCount();
				if(count > 1){

				}
			}
		});
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				saveDraftOrNot(arg0);
			}
			
		});
		
		enter.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isSubjectWrite == false){
					publishOrNot();
				}else{
					if(imagePaths.isEmpty()){
						sendEmail(DIRECT_SEND);
					}else{
						uploadFile();
					}
				}
			}
			
		});
		
		addST.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addStudentByTeacher();
			}
			
		});
		
		addTeacher.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
//				bundle.putSerializable("common", commonList);
				bundle.putInt("type",1);
				bundle.putSerializable("teacherSelectInfo_record", teacherSelectInfo_record);
				bundle.putBoolean("selectAll", selectAll);
				bundle.putBoolean("school_leader", school_leader);
				bundle.putBoolean("departmentcharge", departmentcharge);
				bundle.putSerializable("allDept", allList);
				bundle.putSerializable("teacher", (Serializable) chooseTeacherList_record);
				intent.putExtras(bundle);
				intent.setClass(ComposeEmailActivity.this, ChooseTeacherActivity.class);
				startActivityForResult(intent,CHOOSE_TEACHER);
			}
			
		});
		
		addCC.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
//				bundle.putSerializable("common", commonCCList);
				bundle.putInt("type",2);
				bundle.putSerializable("CC_teacherSelectInfo_record", CC_teacherSelectInfo_record);
				bundle.putBoolean("CC_selectAll", CC_selectAll);
				bundle.putBoolean("CC_school_leader", CC_school_leader);
				bundle.putBoolean("CC_departmentcharge", CC_departmentcharge);
				bundle.putSerializable("CCallDept", allCCList);
				bundle.putSerializable("CCteacher", (Serializable) chooseCCTeacherList_record);
				intent.putExtras(bundle);
				intent.setClass(ComposeEmailActivity.this, ChooseTeacherActivity.class);
				startActivityForResult(intent,CHOOSE_CC_TEACHER);
			}
			
		});
		
		addAttach.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ComposeEmailActivity.this,SelectImageActivity.class);
				intent.putExtra("limit", Constants.MAX_ATTACH_LIST);
				intent.putStringArrayListExtra("addPic", imagePaths);
				intent.putExtra("attachmentLimit", "attachmentLimit");
				
				startActivityForResult(intent,PICK_PICTURE);
			}
			
		});
		
		messageAlert.SetOnChangedListener(new OnChangedListener(){

			@Override
			public void OnChanged(boolean CheckState) {
				// TODO Auto-generated method stub
				isChecked = CheckState;
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
				
				if(s.length() == 0){
					enter.setClickable(false);
					enter.setEnabled(false);
					enter.setTextColor(Color.GRAY);
				}else{
					isSubjectWrite = true;
					if(isStudentChoose == true 
						|| isTeacherChoose == true 
						|| isCCChoose == true
							&&hasContent()){
						enter.setEnabled(true);
						enter.setTextColor(getResources().getColor(R.color.blue));
					}
					if(s.length() >= 240){
						DataUtil.getToast("邮件主题不能超过240个字");
					}
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
					DataUtil.getToast("邮件内容不能超过5000个字");
				}else if(s.length() > 0){
					if(isStudentChoose == true
							|| isTeacherChoose == true
							|| isCCChoose == true
							&&hasContent()) {
						enter.setEnabled(true);
						enter.setTextColor(getResources().getColor(R.color.blue));
					}
				}else{
					if(!hasContent()){
						enter.setEnabled(false);
						enter.setTextColor(Color.GRAY);
					}
				}
			}
			
		});
		tvTeacher.addTextChangedListener(new TextWatcher(){

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
				if(s.length() > 0){
					isTeacherChoose = true;
//					enter.setEnabled(true);
//					enter.setTextColor(getResources().getColor(R.color.blue));
				}else{
					isTeacherChoose = false;
					if(isStudentChoose == false && isCCChoose == false){
						enter.setEnabled(false);
						enter.setTextColor(Color.GRAY);
					}
				}
			}
			
		});
		
		tvCC.addTextChangedListener(new TextWatcher(){

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
				if(s.length() > 0){
					isCCChoose = true;
//					enter.setEnabled(true);
//					enter.setTextColor(getResources().getColor(R.color.blue));
				}else{
					isCCChoose = false;
					if(isStudentChoose == false && isTeacherChoose == false){
						enter.setEnabled(false);
						enter.setTextColor(Color.GRAY);
					}
				}
			}
			
		});
		
		tvStudent.addTextChangedListener(new TextWatcher(){

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
				if(s.length() > 0){
					isStudentChoose = true;
//					enter.setEnabled(true);
//					enter.setTextColor(getResources().getColor(R.color.blue));
				}else{
					isStudentChoose = false;
					if(isCCChoose == false && isTeacherChoose == false){
						enter.setEnabled(false);
						enter.setTextColor(Color.GRAY);
					}
				}
			}
			
		});
	}
	
	private void saveDraftOrNot(View view){
		boolean isEdit = isInput();
		if(isEdit == true){
			SaveDraftPop option = new SaveDraftPop(ComposeEmailActivity.this,ComposeEmailActivity.this);
			option.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		}else{
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	private void addStudentByTeacher(){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("choosedStudentClass", (Serializable) chooseClassList);
		bundle.putSerializable("choosedStudentList", (Serializable) chooseStudentList);
		intent.putExtras(bundle);
//		//把班级信息传过去
		intent.setClass(ComposeEmailActivity.this, ChooseStudentActivity.class);

		startActivityForResult(intent,CHOOSE_STUDENT);
	}
	
	protected void uploadFile(){
		for(String file:imagePaths){
			ImageUploadAsyncTask upload = new ImageUploadAsyncTask(
					this,Constants.PIC_TYPE,file,Constants.UPLOAD_URL,this);
			upload.execute();
		}
	}
	private void sendEmail(String draft){
		try{
			String userId = null,schoolId = null;
			List<Teacher> teacherID = new ArrayList<Teacher>();
			UserType user = CCApplication.getInstance().getPresentUser();
			NewMemberBean member = CCApplication.getInstance().getMemberInfo();
			if(user != null){
				userId = user.getUserId();
				schoolId = user.getSchoolId();
			}
			CreateEmailReq emailReq = new CreateEmailReq();
			emailReq.setUserID(userId);
			if(member != null){
				emailReq.setUserName(member.getUserName());
			}
			String sendContent = content.getText().toString();
			//再次编辑不需要带原来的内容
			if(opertion != EmailDetailActivity.EDIT){
				sendContent += fillOriginEmail();
			}
			emailReq.setContent(sendContent);
			emailReq.setTitle(subject.getText().toString());
			emailReq.setSchoolId(schoolId);
			emailReq.setEmailStatus(draft);//是否是草稿
			emailReq.setIsSendSms(isChecked?"1":"0");
			//再次编辑不需要带原来的附件
			if(data != null && opertion != EmailDetailActivity.EDIT) {
				List<Attachment> orginList = data.getListSAI();
				if (orginList != null && orginList.size() > 0) {
					for (Attachment item : orginList) {
						attachment.add(item.getFileSystemId());
					}
				}
			}
			emailReq.setFileAppend(attachment);
			fillAllReceiptnew(emailReq);
			Gson gson = new Gson();
			String string = gson.toJson(emailReq);
			JSONObject json = new JSONObject(string);
			System.out.println("发送邮件 json:"+json);

			queryPost(Constants.PUBLISH_EMAIL,json);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void fillAllReceiptnew(CreateEmailReq emailReq){
		List<Teacher> teacherID = new ArrayList<Teacher>();
		List<TeacherDept> teacherDeptID = new ArrayList<TeacherDept>();
		List<CCTeacher> teacherCCID = new ArrayList<CCTeacher>();
		List<CCTeacherDept> teacherDeptCCID = new ArrayList<CCTeacherDept>();
		List<CreateEmailReq.Student> studentID = new ArrayList<CreateEmailReq.Student>();
		List<CreateEmailReq.StudentClass> sClassID = new ArrayList<CreateEmailReq.StudentClass>();
		//老师   老师选择
		if(chooseTeacherList != null){
			for(TeacherMap item:chooseTeacherList){
				Teacher teacher = emailReq. new Teacher();
				teacher.setTid(item.getId());
				teacher.setTname(item.getName());
				String SNo = item.getEmployeeNo();
				if(!DataUtil.isNullorEmpty(SNo)){
					teacher.setEmployeeNo(SNo);
				}
				teacherID.add(teacher);
			}
		}
		emailReq.setTeacherID(teacherID);
		//老师群组
        if(selectAll){
			TeacherDept dept = emailReq. new TeacherDept();
			dept.setTdname("teacherstaff");
			teacherDeptID.add(dept);
			emailReq.setTeacherDeptID(teacherDeptID);
		}else{
			if(all_departs != null){//教师选择器

				//行政机构
				List<TeacherSelectInfo.InfoBean.DepartsBean> depList = all_departs.getInfo().getDeparts();
				if(depList != null){
					for(TeacherSelectInfo.InfoBean.DepartsBean item:depList){
						TeacherDept dept = emailReq. new TeacherDept();
						dept.setTdid(item.getId());
						dept.setTdname("depart");
						//	dept.setTdname("depart");
						teacherDeptID.add(dept);
					}
				}

				//常用分组 部门负责人 校级领导
				//部门负责人

				if(departmentcharge){
					TeacherDept dept = emailReq. new TeacherDept();
					dept.setTdname("departmentcharge");
					teacherDeptID.add(dept);
				}
				//校级领导
				if(school_leader){
					TeacherDept dept = emailReq. new TeacherDept();
					dept.setTdname("schoolleader");
					teacherDeptID.add(dept);
				}



			}
			emailReq.setTeacherDeptID(teacherDeptID);
			//有3级页面的  Map集合
			if(subordinateGroupMap!=null){
				emailReq.setSubordinateGroupMap(subordinateGroupMap);
			}
		}




		//CC老师  抄送
		if(chooseCCTeacherList != null){
			for(TeacherMap item:chooseCCTeacherList){
				CCTeacher teacher = emailReq. new CCTeacher();
				teacher.setCid(item.getId());
				teacher.setCname(item.getName());
				String SNo = item.getEmployeeNo();
				if(!DataUtil.isNullorEmpty(SNo)){
					teacher.setEmployeeNo(SNo);
				}
				teacherCCID.add(teacher);
			}
		}
		emailReq.setCCTeacherID(teacherCCID);

		if(CC_selectAll){
			CCTeacherDept dept = emailReq. new CCTeacherDept();
			dept.setCtname("teacherstaff");
			teacherDeptCCID.add(dept);
			emailReq.setCCTeacherDeptID(teacherDeptCCID);
		}else{
			//CC老师群组
			//我的分组
			if(CCall_departs != null){
				//行政机构
				List<TeacherSelectInfo.InfoBean.DepartsBean> depList = CCall_departs.getInfo().getDeparts();
				if(depList != null){
					for(TeacherSelectInfo.InfoBean.DepartsBean item:depList){
						CCTeacherDept dept = emailReq. new CCTeacherDept();
						dept.setCtid(item.getId());

						dept.setCtname("depart");
						//	dept.setTdname("depart");
						teacherDeptCCID.add(dept);
					}
				}

				//常用分组 部门负责人 校级领导
				if(CC_departmentcharge){//部门负责人
					CCTeacherDept dept = emailReq. new CCTeacherDept();
					dept.setCtname("departmentcharge");
					teacherDeptCCID.add(dept);
				}

				if(CC_school_leader){//校级领导
					CCTeacherDept dept = emailReq. new CCTeacherDept();
					dept.setCtname("schoolleader");
					teacherDeptCCID.add(dept);
				}

			}
			emailReq.setCCTeacherDeptID(teacherDeptCCID);
			//有3级页面的  Map集合
			if(CCsubordinateGroupMap!=null){
				emailReq.setCcSubordinateGroupMap(CCsubordinateGroupMap);
			}
		}

		//学生
		if(chooseStudentList != null){
			for(Student item:chooseStudentList){
				CreateEmailReq.Student student = emailReq.new Student();
				student.setSid(item.getId());
				student.setSname(item.getName());
				studentID.add(student);
			}
		}
		emailReq.setStudentID(studentID);
		//学生班级
		if(chooseClassList != null){
			for(ClassMap item:chooseClassList){
				CreateEmailReq.StudentClass studentClass = emailReq.new StudentClass();
				studentClass.setScid(item.getId());
				studentClass.setScname(item.getName());
				sClassID.add(studentClass);
			}
		}
		emailReq.setStudentClassID(sClassID);
	}
	
	private void fillAllReceipt(CreateEmailReq emailReq){
		List<Teacher> teacherID = new ArrayList<Teacher>();
		List<TeacherDept> teacherDeptID = new ArrayList<TeacherDept>();
		List<CCTeacher> teacherCCID = new ArrayList<CCTeacher>();
		List<CCTeacherDept> teacherDeptCCID = new ArrayList<CCTeacherDept>();
		List<CreateEmailReq.Student> studentID = new ArrayList<CreateEmailReq.Student>();
		List<CreateEmailReq.StudentClass> sClassID = new ArrayList<CreateEmailReq.StudentClass>();
		//老师
		if(chooseTeacherList != null){
			for(TeacherMap item:chooseTeacherList){
				Teacher teacher = emailReq. new Teacher();
				teacher.setTid(item.getId());
				teacher.setTname(item.getName());
				String SNo = item.getEmployeeNo();
				if(!DataUtil.isNullorEmpty(SNo)){
					teacher.setEmployeeNo(SNo);
				}
				teacherID.add(teacher);
			}
		}
		emailReq.setTeacherID(teacherID);
		//老师群组
		if(allList != null){
			List<ContactGroupMap> contactGroupList = allList.getContactGroupMapList();
			if(contactGroupList != null){
				for(ContactGroupMap item:contactGroupList){
					TeacherDept dept = emailReq. new TeacherDept();
					dept.setTdid(item.getId());
//					dept.setTdname(item.getName());
					dept.setTdname("contact");
					teacherDeptID.add(dept);
				}
			}
			List<DepMap> depList = allList.getDepMapList();
			if(depList != null){
				for(DepMap item:depList){
					TeacherDept dept = emailReq. new TeacherDept();
					dept.setTdid(item.getId());
//					dept.setTdname(item.getDepartmentName());
					dept.setTdname("depart");
					teacherDeptID.add(dept);
				}
			}
			List<GradeMap> gradeList = allList.getGradeMapList();
			if(gradeList != null){
				for(GradeMap item:gradeList){
					TeacherDept dept = emailReq. new TeacherDept();
					dept.setTdid(item.getId());
//					dept.setTdname(item.getName());
					dept.setTdname("grade");
					teacherDeptID.add(dept);
				}
			}
			List<CommonGroup> commonList = allList.getCommonList();
			if(commonList != null){
				for(CommonGroup item:commonList){
					TeacherDept dept = emailReq. new TeacherDept();
					dept.setTdname(item.getCode());
					teacherDeptID.add(dept);
				}
			}
		}
		emailReq.setTeacherDeptID(teacherDeptID);
		//CC老师
		if(chooseCCTeacherList != null){
			for(TeacherMap item:chooseCCTeacherList){
				CCTeacher teacher = emailReq. new CCTeacher();
				teacher.setCid(item.getId()); 
				teacher.setCname(item.getName());
				String SNo = item.getEmployeeNo();
				if(!DataUtil.isNullorEmpty(SNo)){
					teacher.setEmployeeNo(SNo);
				}
				teacherCCID.add(teacher);
			}
		}
		emailReq.setCCTeacherID(teacherCCID);
		//CC老师群组
		//我的分组
		if(allCCList != null){
			List<ContactGroupMap> contactGroupList = allCCList.getContactGroupMapList();
			if(contactGroupList != null){
				for(ContactGroupMap item:contactGroupList){
					CCTeacherDept dept = emailReq. new CCTeacherDept();
					dept.setCtid(item.getId());
//					dept.setCtname(item.getName());
					dept.setCtname("contact");
					teacherDeptCCID.add(dept);
				}
			}
	    //行政机构
			List<DepMap> depList = allCCList.getDepMapList();
			if(depList != null){
				for(DepMap item:depList){
					CCTeacherDept dept = emailReq. new CCTeacherDept();
					dept.setCtid(item.getId());
//					dept.setCtname(item.getDepartmentName());
					dept.setCtname("depart");
					teacherDeptCCID.add(dept);
				}
			}
		//教学机构
			List<GradeMap> gradeList = allCCList.getGradeMapList();
			if(gradeList != null){
				for(GradeMap item:gradeList){
					CCTeacherDept dept = emailReq. new CCTeacherDept();
					dept.setCtid(item.getId());
//					dept.setCtname(item.getName());
					dept.setCtname("grade");
					teacherDeptCCID.add(dept);
				}
			}
		//常用分组 部门负责人 校级领导
			List<CommonGroup> commonList = allCCList.getCommonList();
			if(commonList != null){
				for(CommonGroup item:commonList){
					CCTeacherDept dept = emailReq. new CCTeacherDept();
					dept.setCtname(item.getCode());
					teacherDeptCCID.add(dept);
				}
			}
		}
		emailReq.setCCTeacherDeptID(teacherDeptCCID);
		//学生
		if(chooseStudentList != null){
			for(Student item:chooseStudentList){
				CreateEmailReq.Student student = emailReq.new Student();
				student.setSid(item.getId());
				student.setSname(item.getName());
				studentID.add(student);
			}
		}
		emailReq.setStudentID(studentID);
		//学生班级
		if(chooseClassList != null){
			for(ClassMap item:chooseClassList){
				CreateEmailReq.StudentClass studentClass = emailReq.new StudentClass();
				studentClass.setScid(item.getId());
				studentClass.setScname(item.getName());
				sClassID.add(studentClass);
			}
		}
		emailReq.setStudentClassID(sClassID);
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
				
				if(imagePaths != null){
					long sizeAllLong = 0; 
					for (int i = 0; i < imagePaths.size(); i++) {
						String file = imagePaths.get(i);
						long size = 0;
						try {
							size = DataUtil.getFileSize(new File(file));
							if(size > 20*Constants.MBYTE){
								DataUtil.getToast("单张图片大小不能超过20M");
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						sizeAllLong += size;
					}
					
					if(sizeAllLong > 200*Constants.MBYTE){//判断拍照后返回来的数据集合 大小  大于200M 则
						imagePaths.remove(imagePaths.size()-1);
						DataUtil.getToast("图片总大小不能超过200M");
					}
					if(imagePaths.size() > 0){
						enter.setEnabled(true);
						enter.setTextColor(getResources().getColor(R.color.blue));
					}
				}
				data.getBooleanExtra("is_compress", true);
				adapter = new AttachmentListAdapter(this,imagePaths);
				listView.setAdapter(adapter);
				adapter.registerDataSetObserver(new DataSetObserver() {
					@Override
					public void onChanged() {
						super.onChanged();
						if(!hasContent()) {
							enter.setEnabled(false);
							enter.setTextColor(Color.GRAY);
						}
					}
				});
				break;
			case CHOOSE_CC_TEACHER:

//				commonCCList = (ArrayList<Object>) bundle.getSerializable("common");
				teacherName="";
				tvCC.setText("");
				allCCList = (AllDeptList) bundle.getSerializable("allDept");
				chooseCCTeacherList = (List<TeacherMap>) bundle.getSerializable("teacher");
				if(chooseCCTeacherList.size()>0){
					try {
						chooseCCTeacherList_record=deepCopy(chooseCCTeacherList);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					chooseCCTeacherList_record=null;
				}

				CCall_departs= (TeacherSelectInfo) bundle.getSerializable("choose_list"); //所有部门集合
				addTeacher(requestCode);
				CC_departmentcharge=ChooseTeacherActivity.departleadr;
				CC_school_leader=ChooseTeacherActivity.schoolleader;
				CC_selectAll=ChooseTeacherActivity.isSelectAll;
				if(CCall_departs!=null){
					try {
						CC_teacherSelectInfo_record=deepCopy(CCall_departs); //拷贝一份 作为 记录
					} catch (Exception e) {
						e.printStackTrace();
					}
					addDeparts(requestCode,CCsubordinateGroupMap,CCall_departs);
				}

				break;
			case CHOOSE_TEACHER:
				//获取教师选择器 值
//				commonList = (ArrayList<Object>) bundle.getSerializable("common");
				teacherName="";
				tvTeacher.setText("");
				allList = (AllDeptList) bundle.getSerializable("allDept");
				all_departs= (TeacherSelectInfo) bundle.getSerializable("choose_list"); //所有部门集合
				chooseTeacherList = (List<TeacherMap>) bundle.getSerializable("teacher");
				if(chooseTeacherList.size()>0){
					try {
						chooseTeacherList_record=deepCopy(chooseTeacherList);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					chooseTeacherList_record=null;
				}
				addTeacher(requestCode);
				departmentcharge=ChooseTeacherActivity.departleadr;
				school_leader=ChooseTeacherActivity.schoolleader;
				selectAll=ChooseTeacherActivity.isSelectAll;

					if(all_departs!=null){
						try {
							teacherSelectInfo_record=deepCopy(all_departs); //拷贝一份 作为 记录
						} catch (Exception e) {
							e.printStackTrace();
						}
					addDeparts(requestCode,subordinateGroupMap,all_departs);
				}
				break;
			case CHOOSE_STUDENT:
				chooseClassList = (List<ClassMap>) bundle.getSerializable("studentClass");
				chooseStudentList = (List<Student>) bundle.getSerializable("studentList");
				addStudent();
				break;
			}
		}
	}
	private String teacherName="";
	protected void addTeacher(int type){

		AllDeptList list = null;
		List<TeacherMap> teacherList = null;
		if(type == CHOOSE_TEACHER){
			list = allList;
			teacherList = chooseTeacherList;
		}else if(type ==  CHOOSE_CC_TEACHER){
			list = allCCList;
			teacherList = chooseCCTeacherList;
		}
		if(list != null){
			deptList = list.getDepMapList();
			contactList = list.getContactGroupMapList();
			gradeList = list.getGradeMapList();
			commonList = list.getCommonList();
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
		if(teacherList != null){
			for(int i = 0; i < teacherList.size(); i++){
				String name = teacherList.get(i).getName();
				teacherName += name;
				teacherName += ",";
			}
		}
		
	/*	if(type == CHOOSE_TEACHER){
			tvTeacher.setText(teacherName);
		}else if(type == CHOOSE_CC_TEACHER){
			tvCC.setText(teacherName);
		}*/
	}

	private Map<String,List<String>> subordinateGroupMap=null;
	private Map<String,List<String>> CCsubordinateGroupMap=null;
	protected void addDeparts(int type,Map<String,List<String>> map,TeacherSelectInfo departs){
		      String departname="";
		      if(ChooseTeacherActivity.isSelectAll){
			   departname+="全体教职工";
			   departname+=",";


		      }
			  if(type==CHOOSE_CC_TEACHER){
				  CCsubordinateGroupMap=new HashMap<String, List<String>>();
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
				  if(type==CHOOSE_CC_TEACHER){

					  CCsubordinateGroupMap.put("sybzr@grade",list);
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
					  list.add(g.getSchoolId()+"");

				  }
				  if(type==CHOOSE_CC_TEACHER){

					  CCsubordinateGroupMap.put("syjyzz@grade",list);
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
				  if(type==CHOOSE_CC_TEACHER){

					  CCsubordinateGroupMap.put("sybkzz@grade",list);
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
				  if(type==CHOOSE_CC_TEACHER){

					  CCsubordinateGroupMap.put("synjzz",list);
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
				  if(type==CHOOSE_CC_TEACHER){

					  CCsubordinateGroupMap.put("classes",list);
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
				  if(type==CHOOSE_CC_TEACHER){

					  CCsubordinateGroupMap.put("prepareLssion",list);
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
				  if(type==CHOOSE_CC_TEACHER){

					  CCsubordinateGroupMap.put("group",list);
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

		if(type == CHOOSE_TEACHER){

			tvTeacher.setText(departname+teacherName);
		}else if(type == CHOOSE_CC_TEACHER){

			tvCC.setText(departname+teacherName);
		}
		if(!TextUtils.isEmpty(content.getText().toString())&&!TextUtils.isEmpty(subject.getText().toString())&&!TextUtils.isEmpty(tvTeacher.getText().toString())){
			 //内容为空
			enter.setEnabled(true);
			enter.setTextColor(getResources().getColor(R.color.blue));
		}else{
			enter.setEnabled(false);
			enter.setTextColor(Color.GRAY);
		}


	}

	
	protected void addStudent(){
		String studentName = "";
		if(chooseClassList != null){
			for(int i = 0; i < chooseClassList.size(); i++){
				String name = chooseClassList.get(i).getName();
				studentName += name;
				studentName += ",";
			}
		}
		if(chooseStudentList != null){
			for(int i = 0; i < chooseStudentList.size(); i++){
				String name = chooseStudentList.get(i).getName();
				studentName += name;
				studentName += ",";
			}
		}
		
		tvStudent.setText(studentName);
	}
	
	private void publishOrNot(){
		dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.customdialog, handler);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		dialog.setTitle("您的邮件未填写主题，确定要发送吗？");
		dialog.setContent("");
	}
	/**
	 * 控制CustomDialog按钮效果.
	 */
	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);

			switch (msg.what) {
			case CustomDialog.DIALOG_CANCEL:// 取消发送
				dialog.dismiss();
				break;
			case CustomDialog.DIALOG_SURE:// 确认发送
				if(imagePaths.isEmpty()){
					sendEmail(DIRECT_SEND);
				}else{
					uploadFile();
				}
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		System.out.println(data);
		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if(ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		}else{
			if(isQuickPublish == false && opertion != -1){
				gotoPage();
				setResult(RESULT_OK);
			}
			finish();
		}
	}
	
	private void gotoPage(){
		Intent intent = new Intent();
		intent.setClass(this,EmailActivity.class);
		Bundle bundle = new Bundle();
		switch(opertion){
		case EmailDetailActivity.REPLY:
		case EmailDetailActivity.REPLY_ALL:
		case EmailDetailActivity.FORWARD:
		case EmailDetailActivity.EDIT:
			bundle.putInt("EmailType",EmailActivity.OUTBOX);
			DataUtil.getToast("发送成功!");
			break;
		case EmailDetailActivity.SAVE_DRAFT:
			bundle.putInt("EmailType",EmailActivity.DRAFT);
			DataUtil.getToast("保存至草稿箱!");
			break;
		case EmailDetailActivity.COMPOSE_NEW:
			bundle.putInt("EmailType",EmailActivity.OUTBOX);
			DataUtil.getToast("发送成功!");
			break;
		default:
			DataUtil.getToast("发送成功!");
			break;
		}
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	@Override
	public void onImageUploadCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onImageUploadComplete(String result) {
		// TODO Auto-generated method stub
		System.out.println("result:"+result+" i:"+i);
		
		ret = JsonUtils.fromJson(result, UploadFileResult.class);

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
			if(opertion == EmailDetailActivity.SAVE_DRAFT){
				sendEmail(SAVE_DARFT);
			}else{
				sendEmail(DIRECT_SEND);
			}
			i = 0;
		}
		
	}

	@Override
	public void saveDraft() {
		// TODO Auto-generated method stub
		if(imagePaths.isEmpty()){
			sendEmail(SAVE_DARFT);
		}else{
			uploadFile();
		}
		opertion = EmailDetailActivity.SAVE_DRAFT;
	}

	@Override
	public void notSaveDraft() {
		// TODO Auto-generated method stub
		setResult(RESULT_CANCELED);
		finish();
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			saveDraftOrNot(listView);
			break;
		}
		return super.onKeyDown(keyCode, event);
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
