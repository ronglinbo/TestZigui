package com.wcyc.zigui2.newapp.module.email;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;
import com.wcyc.zigui2.chooseContact.ChooseStudentActivity;
import com.wcyc.zigui2.chooseContact.ChooseStudentByClassAdminActivity;
import com.wcyc.zigui2.chooseContact.ChooseStudentByParentActivity;
import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.chooseContact.ChooseTeacherByParentActivity;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.adapter.AttachmentListAdapter;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllDeptList;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeEmailByParentActivity extends BaseActivity 
	implements ImageUploadAsyncTaskListener,OnSaveDraft{
	
	private TextView cancel,enter;
	private ImageButton addST,addTeacher,addCC;
	private Button addAttach;
	private SlipButton messageAlert;
	private TextView tvTeacher,tvStudent,tvCC;
	private EditText subject,content;
	private WebView originContent;
	private NewMailInfo data;//原来邮件的内容
	private int opertion = -1;//如何操作
	private String RETURN_LINE = "<br>";
	private static final int PICK_PICTURE = 100;
	private static final int CHOOSE_TEACHER = 101;
	private static final int CHOOSE_STUDENT = 102;
	private static final int CHOOSE_CC_TEACHER = 103;
	
	private static final int CHOOSE_STUDENT_BY_PARENT = 104;
	private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
	private AttachmentListAdapter adapter;
	private List<ContactGroupMap> contactList;
	private List<GradeMap> gradeList;
	private ArrayList<Object> commonList;
	private List<DepMap> deptList;
	private List<TeacherMap> chooseTeacherList; //选择老师
	private AllDeptList allList;//选择老师群组
	
	private ArrayList<Object> commonCCList;
	private List<TeacherMap> chooseCCTeacherList; //选择抄送老师
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
	private boolean isParent = false;
	private HashMap<Integer, Boolean> isSelected= new HashMap<Integer, Boolean>();
	private List<String> studentId,studentName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_email);
		isParent = CCApplication.getInstance().isCurUserParent();
		chooseStudentList = new ArrayList<Student>();
		chooseTeacherList = new ArrayList<TeacherMap>();
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
				|| listView.getAdapter() != null && listView.getAdapter().getCount() > 0;
	}
	
	private void fillData(){
		switch(opertion){
		case EmailDetailActivity.REPLY:
			fillReplyData();
			fillReplyRecipient();
			break;
		case EmailDetailActivity.REPLY_ALL:
			fillReplyAllData();
			fillReplyRecipient();
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
				Student student = new Student();
				String id = data.getCreateUserId();
				student.setId(Integer.parseInt(id));
				student.setName(data.getCreateUserName());
				chooseStudentList.add(student);
				tvStudent.setText(data.getCreateUserName());
			}else if(userType.equals(Constants.TEACHER_STR_TYPE)){
				chooseTeacherList = new ArrayList<TeacherMap>();
				TeacherMap teacher  = new AllTeacherList().new TeacherMap();
				String id = data.getCreateUserId();
				teacher.setId(Integer.parseInt(id));
				teacher.setName(data.getCreateUserName());
				chooseTeacherList.add(teacher);
				tvTeacher.setText(data.getCreateUserName());
			}
		}
	}
	
	private void fillChooseContact(){
		if(data == null) return;
		List<Recipient> teacherList = data.getTeacherRealation();
		if(teacherList != null){
			chooseTeacherList = new ArrayList<TeacherMap>();
			for(Recipient item:teacherList){
				TeacherMap teacher  = new AllTeacherList().new TeacherMap();
				String id = item.getReceiveObjectId();
				teacher.setId(Integer.parseInt(id));
				teacher.setName(item.getReceiveObjectName());
				chooseTeacherList.add(teacher);
			}
		}
		List<Recipient> teacherCCList = data.getCopyRealation();
		if(teacherList != null){
			chooseCCTeacherList = new ArrayList<TeacherMap>();
			for(Recipient item:teacherCCList){
				TeacherMap teacher  = new AllTeacherList().new TeacherMap();
				String id = item.getReceiveObjectId();
				teacher.setId(Integer.parseInt(id));
				teacher.setName(item.getReceiveObjectName());
				chooseCCTeacherList.add(teacher);
			}
		}
		List<Recipient> studentList = data.getStudentRealation();
		if(studentList != null){
			chooseStudentList = new ArrayList<Student>();
			for(Recipient item:studentList){
				Student student = new Student();
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
		fillRecipient(true);
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
				if(isParent){
					String classId = DataUtil.getChildClassId();
					if(classId != null){
						addStudentByParent(classId);
					}
				}else{
					addStudentByTeacher();
				}
			}
			
		});
		
		addTeacher.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
//				bundle.putSerializable("common", commonList);
				bundle.putSerializable("allDept", allList);
				bundle.putSerializable("teacher", (Serializable) chooseTeacherList);
				intent.putExtras(bundle);
				if(isParent){
					intent.setClass(ComposeEmailByParentActivity.this,
							ChooseTeacherByParentActivity.class);
				}else{
					intent.setClass(ComposeEmailByParentActivity.this, 
							ChooseTeacherActivity.class);
				}
				startActivityForResult(intent,CHOOSE_TEACHER);
			}
			
		});
		
		addCC.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("common", commonCCList);
				bundle.putSerializable("allDept", allCCList);
				bundle.putSerializable("teacher", (Serializable) chooseCCTeacherList);
				intent.putExtras(bundle);
				if(isParent){
					intent.setClass(ComposeEmailByParentActivity.this,
							ChooseTeacherByParentActivity.class);
				}else{
					intent.setClass(ComposeEmailByParentActivity.this, 
							ChooseTeacherActivity.class);
				}
				
				startActivityForResult(intent,CHOOSE_CC_TEACHER);
			}
			
		});
		
		addAttach.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ComposeEmailByParentActivity.this,SelectImageActivity.class);
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
						|| isCCChoose == true){
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
					enter.setEnabled(true);
					enter.setTextColor(getResources().getColor(R.color.blue));
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
					enter.setEnabled(true);
					enter.setTextColor(getResources().getColor(R.color.blue));
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
					enter.setEnabled(true);
					enter.setTextColor(getResources().getColor(R.color.blue));
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
	
	private void addStudentByParent(String classId){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		intent.putExtra("classId", classId);
		bundle.putSerializable("choosedStudentList", (Serializable) chooseStudentList);
		intent.putExtras(bundle);
		intent.setClass(this,ChooseStudentByParentActivity.class);
		startActivityForResult(intent, CHOOSE_STUDENT_BY_PARENT);
	}
	
	private void addStudentByTeacher(){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("choosedStudentClass", (Serializable) chooseClassList);
		bundle.putSerializable("choosedStudentList", (Serializable) chooseStudentList);
		intent.putExtras(bundle);
		classAdmin = DataUtil.getTeachClass();
		
		//把班级信息传过去
		if(classAdmin != null){
			intent.setClass(ComposeEmailByParentActivity.this, ChooseStudentByClassAdminActivity.class);
		}else{
			intent.setClass(ComposeEmailByParentActivity.this, ChooseStudentActivity.class);
		}
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
			String userId = null,schoolId = null,childId = null;
			List<Teacher> teacherID = new ArrayList<Teacher>();
			UserType user = CCApplication.getInstance().getPresentUser();
			NewMemberBean member = CCApplication.getInstance().getMemberInfo();
			if(user != null){
				userId = user.getUserId();
				schoolId = user.getSchoolId();
				childId = user.getChildId();
			}
			CreateEmailReq emailReq = new CreateEmailReq();
			emailReq.setUserID(userId);
			emailReq.setChildId(childId);
			if(member != null){
				emailReq.setUserName(user.getChildName()+user.getRelationTypeName());
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
			fillAllReceipt(emailReq);
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
					dept.setTdname(item.getName());
					teacherDeptID.add(dept);
				}
			}
			List<DepMap> depList = allList.getDepMapList();
			if(depList != null){
				for(DepMap item:depList){
					TeacherDept dept = emailReq. new TeacherDept();
					dept.setTdid(item.getId());
					dept.setTdname(item.getDepartmentName());
					teacherDeptID.add(dept);
				}
			}
			List<GradeMap> gradeList = allList.getGradeMapList();
			if(gradeList != null){
				for(GradeMap item:gradeList){
					TeacherDept dept = emailReq. new TeacherDept();
					dept.setTdid(item.getId());
					dept.setTdname(item.getName());
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
		if(allCCList != null){
			List<ContactGroupMap> contactGroupList = allCCList.getContactGroupMapList();
			if(contactGroupList != null){
				for(ContactGroupMap item:contactGroupList){
					CCTeacherDept dept = emailReq. new CCTeacherDept();
					dept.setCtid(item.getId());
					dept.setCtname(item.getName());
					teacherDeptCCID.add(dept);
				}
			}
			List<DepMap> depList = allCCList.getDepMapList();
			if(depList != null){
				for(DepMap item:depList){
					TeacherDept dept = emailReq. new TeacherDept();
					dept.setTdid(item.getId());
					dept.setTdname(item.getDepartmentName());
					teacherDeptID.add(dept);
				}
			}
			List<GradeMap> gradeList = allCCList.getGradeMapList();
			if(gradeList != null){
				for(GradeMap item:gradeList){
					TeacherDept dept = emailReq. new TeacherDept();
					dept.setTdid(item.getId());
					dept.setTdname(item.getName());
					teacherDeptID.add(dept);
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
				
				if(imagePaths!=null){
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
					
				}
				data.getBooleanExtra("is_compress", true);
				adapter = new AttachmentListAdapter(this,imagePaths);
				listView.setAdapter(adapter);
				break;
			case CHOOSE_CC_TEACHER:
				commonCCList = (ArrayList<Object>) bundle.getSerializable("common");
				allCCList = (AllDeptList) bundle.getSerializable("allDept");
				chooseCCTeacherList = (List<TeacherMap>) bundle.getSerializable("teacher");
				addTeacher(requestCode);
				break;
			case CHOOSE_TEACHER:
				commonList = (ArrayList<Object>) bundle.getSerializable("common");
				allList = (AllDeptList) bundle.getSerializable("allDept");
				chooseTeacherList = (List<TeacherMap>) bundle.getSerializable("teacher");
				addTeacher(requestCode);
				break;
			case CHOOSE_STUDENT:
				chooseClassList = (List<ClassMap>) bundle.getSerializable("studentClass");
				chooseStudentList = (List<Student>) bundle.getSerializable("studentList");
				addStudent();
				break;
			case CHOOSE_STUDENT_BY_PARENT:
				chooseStudentList = (List<Student>) bundle.getSerializable("studentList");
				isSelected = (HashMap<Integer, Boolean>) bundle.getSerializable("isSelected");
				addStudent();
				break;
			}
		}
	}
	
	protected void addTeacher(int type){
		String teacherName = "";
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
		List<String> dup = new ArrayList<String>();
		if(teacherList != null){
			for(TeacherMap item:teacherList){
				String name = item.getName();
				dup.add(name);
			}
		}
		List<String> withoutDup = new ArrayList<String>(new HashSet<String>(dup));
		if(withoutDup != null){
			for(String item:withoutDup){
				teacherName += item;
				teacherName += ",";
			}
		}
		if(type == CHOOSE_TEACHER){
			tvTeacher.setText(teacherName);
		}else if(type == CHOOSE_CC_TEACHER){
			tvCC.setText(teacherName);
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
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.setTitle("您的邮件未填写主题，确定要发送吗？");
		dialog.setContent("");
	}
	/**
	 * 控制CustomDialog按钮事件.
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
			if(isQuickPublish == false && opertion != -1) {
				gotoPage();
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
			Toast.makeText(this,"发送成功!",Toast.LENGTH_LONG);
			break;
		case EmailDetailActivity.SAVE_DRAFT:
			bundle.putInt("EmailType",EmailActivity.DRAFT);
			Toast.makeText(this,"保存至草稿箱!",Toast.LENGTH_LONG);
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
	private void saveDraftOrNot(View view){
		boolean isEdit = isInput();
		if(isEdit == true){
			SaveDraftPop option = new SaveDraftPop(this,this);
			option.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		}else{
			finish();
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
}
