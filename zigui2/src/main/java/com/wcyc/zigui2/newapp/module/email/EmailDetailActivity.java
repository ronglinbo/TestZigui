/*
 * 文 件 名:EmailDetailActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-07-05
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.module.email;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.email.NewMailInfo.Attachment;
import com.wcyc.zigui2.newapp.module.email.NewMailInfo.Recipient;

import com.wcyc.zigui2.newapp.widget.CustomWebView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.CustomDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils.TruncateAt;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EmailDetailActivity extends BaseActivity {
	private static final int DELETE_MAIL = 1;
	private static final int RECOVER_MAIL = 2;
	private TextView tvTitle,tvTime,tvSender,tvNotify,
		tvTeacher,tvCCList,tvStudent,tvAttach,tvShowAllTeacher,
		tvShowAllStudent,tvShowAllCCList;
	private WebView Content;
	private LinearLayout view;
	private String time,emailTitle,content,title,sender;
	private SpannableString attach,sCCList,teachers,students;
	private Button reply,replyAll,forward,delete,edit,recover;
	private int type,pos;
	private NewMailInfo data;
	public static final int REPLY = 1;
	public static final int REPLY_ALL = 2;
	public static final int FORWARD = 3;
	public static final int EDIT = 4;
	public static final int SAVE_DRAFT = 5;
	public static final int COMPOSE_NEW = 6;
	public static final String INBOX = "0";
	public static final String OUTBOX = "1";
	public static final String DRAFT = "2";
	public static final String RECYCLE = "3";
	
	public String deleteType;
	private String emailType;//缓存中保存的邮件类型
	private CustomDialog dialog;
	private List<Attachment> attachList;
	private ListView listView;
	private boolean isFirst = true,isStudentFirst = true,isCCFirst = true;
	private boolean isParent = CCApplication.getInstance().isCurUserParent();
	UserType user = CCApplication.getInstance().getPresentUser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_detail);
		initData();
		markRead(data);
		initView();
		initEvent();
	}
	
	private void initView(){
		view = (LinearLayout) findViewById(R.id.title_back);
		view.setVisibility(View.VISIBLE);
		tvTitle = (TextView) findViewById(R.id.title);
		if(DataUtil.isNullorEmpty(emailTitle)){
			tvTitle.setText("(无主题)");
		}else {
			tvTitle.setText(emailTitle);
		}
		tvSender = (TextView) findViewById(R.id.sender);
		tvSender.setText(sender);
		tvTeacher = (TextView) findViewById(R.id.teacher);
//		setText(tvTeacher,teachers);

		tvShowAllTeacher = (TextView) findViewById(R.id.showAllTeacher);
		tvStudent = (TextView) findViewById(R.id.student);
//		setText(tvStudent,students);

		tvShowAllStudent = (TextView) findViewById(R.id.showAllStudent);
		tvCCList = (TextView) findViewById(R.id.cclist);
//		setText(tvCCList,sCCList);
		tvShowAllCCList = (TextView) findViewById(R.id.showAllCCList);

		showAttachList();
		tvTime = (TextView) findViewById(R.id.time);
		tvTime.setText(DataUtil.getShowTime(time));
		Content = (WebView) findViewById(R.id.content);
//		if(content != null && content.length() > 0) {
////			DataUtil.showHtmlSetting(Content, content);
//			new CustomWebView(this,Content,content);
//		}
		reply = (Button)findViewById(R.id.reply);
		replyAll = (Button)findViewById(R.id.reply_all);
		forward = (Button)findViewById(R.id.forward);
		delete = (Button)findViewById(R.id.delete);
		edit = (Button)findViewById(R.id.edit);
		recover = (Button)findViewById(R.id.recover);

		initType();
		
	}
	
	private void setListViewHeight(ShowAttachListAdapter listAdapter){
		int totalHeight = 0;  
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View listItem = listAdapter.getView(i, null, listView);  
			listItem.measure(0, 0); // 计算子项View 的宽高  
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度  
		}  
		
		ViewGroup.LayoutParams params = listView.getLayoutParams();  
		params.height = totalHeight  
		             + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  

		listView.setLayoutParams(params);  
	}
	private void setText(TextView textView,SpannableString string){
		if(string != null && string.length() > 0){
			textView.setText(string);
		}else{
			textView.setVisibility(View.GONE);
		}
	}
	
	private void initType(){
		switch(type){
		case EmailActivity.INBOX:
			title = "收件箱";
			deleteType = INBOX;
			emailType = "inbox";
			break;
		case EmailActivity.OUTBOX:
			title = "发件箱";
			deleteType = OUTBOX;
			emailType = "outbox";
			reply.setVisibility(View.GONE);
			replyAll.setVisibility(View.GONE);
			edit.setVisibility(View.VISIBLE);
			break;
		case EmailActivity.DRAFT:
			title = "草稿箱";
			deleteType = DRAFT;
			emailType = "draft";
			reply.setVisibility(View.GONE);
			replyAll.setVisibility(View.GONE);
			edit.setVisibility(View.VISIBLE);
			break;
		case EmailActivity.RECYCLE:
			title = "回收站";
			deleteType = RECYCLE;
			emailType = "recycle";
			reply.setVisibility(View.GONE);
			replyAll.setVisibility(View.GONE);
			forward.setVisibility(View.GONE);
			recover.setVisibility(View.VISIBLE);
			break;	
		}
		tvNotify = (TextView) findViewById(R.id.new_content);
		tvNotify.setText(title);
	}
	
	private void initEvent(){
		reply.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("originMail", data);
				bundle.putLong("operation", REPLY);
				intent.putExtras(bundle);
				if(isParent){
					intent.setClass(EmailDetailActivity.this, ComposeEmailByParentActivity.class);
				}else {
					intent.setClass(EmailDetailActivity.this, ComposeEmailActivity.class);
				}
				startActivity(intent);
			}
			
		});
		replyAll.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				NewMailInfo temp = data;
				removeSelf(temp);
				bundle.putSerializable("originMail", temp);
				bundle.putLong("operation", REPLY_ALL);
				intent.putExtras(bundle);
				if(isParent){
					intent.setClass(EmailDetailActivity.this, ComposeEmailByParentActivity.class);
				}else {
					intent.setClass(EmailDetailActivity.this, ComposeEmailActivity.class);
				}
				startActivity(intent);
			}
			
		});
		forward.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("originMail", data);
				bundle.putLong("operation", FORWARD);
				intent.putExtras(bundle);
				if(isParent){
					intent.setClass(EmailDetailActivity.this, ComposeEmailByParentActivity.class);
				}else {
					intent.setClass(EmailDetailActivity.this, ComposeEmailActivity.class);
				}

				startActivity(intent);
			}
			
		});
		edit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("originMail", data);
				bundle.putLong("operation", EDIT);
				intent.putExtras(bundle);
				if(isParent){
					intent.setClass(EmailDetailActivity.this, ComposeEmailByParentActivity.class);
				}else {
					intent.setClass(EmailDetailActivity.this, ComposeEmailActivity.class);
				}
				startActivity(intent);
			}
			
		});
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				deleteOrNot();
			}
			
		});
		recover.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RecoverEmail(data);
			}
			
		});
		view.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		tvShowAllTeacher.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(tvShowAllTeacher.getText().equals("全部")){
					tvShowAllTeacher.setText("收起");
					tvTeacher.setMaxLines(Integer.MAX_VALUE);
					tvTeacher.requestLayout();
				}else{
					tvShowAllTeacher.setText("全部");
					tvTeacher.setEllipsize(TruncateAt.END);
					tvTeacher.setMaxLines(2);
				}
			}
			
		});

		tvShowAllStudent.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(tvShowAllStudent.getText().equals("全部")){
					tvShowAllStudent.setText("收起");
					tvStudent.setMaxLines(Integer.MAX_VALUE);
					tvStudent.requestLayout();
				}else{
					tvShowAllStudent.setText("全部");
					tvStudent.setEllipsize(TruncateAt.END);
					tvStudent.setMaxLines(2);
				}
			}
			
		});
		tvShowAllCCList.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(tvShowAllCCList.getText().equals("全部")){
					tvShowAllCCList.setText("收起");
					tvCCList.setMaxLines(Integer.MAX_VALUE);
					tvCCList.requestLayout();
				}else{
					tvShowAllCCList.setText("全部");
					tvCCList.setEllipsize(TruncateAt.END);
					tvCCList.setMaxLines(2);
				}
			}
			
		});
		
	}
	//去掉自己
	private void removeSelf(NewMailInfo data){
		List<Recipient> ccList = data.getCopyRealation();
		List<Recipient> teacherList = data.getTeacherRealation();
		List<Recipient> studentList = data.getStudentRealation();
		if(ccList.size() + teacherList.size() + studentList.size() <= 1)
			return;

		List<Recipient> newCCList = removeItem(ccList, user.getUserId());
		data.setCopyRealation(newCCList);

		List<Recipient> newTeacherList = removeItem(teacherList, user.getUserId());
		data.setTeacherRealation(newTeacherList);

		List<Recipient> newStudentList = removeItem(studentList, user.getChildId());
		data.setStudentRealation(newStudentList);
	}

	private List<Recipient> removeItem(List<Recipient> list,String id){
		if(id == null) return list;
		List<Recipient> ret = new ArrayList<Recipient>();
		for(Recipient item:list){
			if(!id.equals(item.getReceiveObjectId())){
				ret.add(item);
			}
		}
		return ret;
	}

	private void deleteOrNot(){
		dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.customdialog, handler);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		dialog.setTitle("确定删除吗?");
		dialog.setContent("");
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
			case CustomDialog.DIALOG_CANCEL:// 取消
				dialog.dismiss();
				break;
			case CustomDialog.DIALOG_SURE:// 确认
				deleteEmail(data);
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	
	private void initData(){
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			type = bundle.getInt("type");
			pos = bundle.getInt("pos");
			data = (NewMailInfo) bundle.getSerializable("data");
			if(data != null){
				emailTitle = data.getTitle();
				time =   "时    间：" + data.getCreateTime();
				if(isSelf(data.getCreateUserId())){
					sender = "发件人：" + "我";
				}else{
					sender = "发件人：" + data.getCreateUserName();
				}
				content = data.getContent();
			}
		}
	}

	private void parseMailDetail(MailDetailInfo data){
		List<Recipient> teacherList = data.getTeacherRealation();
		this.data.setTeacherRealation(teacherList);

		if(teacherList.size() > 0) {
			String teacherName = getListStringName(teacherList);
			teachers = getSpanString(teacherName);
			setText(tvTeacher, teachers);
		}else{
			findViewById(R.id.rl_teacher).setVisibility(View.GONE);
		}
		ViewTreeObserver vto = tvTeacher.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if(isFirst){
					isFirst = false;
					int lineCount = tvTeacher.getLineCount();
					System.out.println(lineCount);
					if(lineCount > 2){
						tvShowAllTeacher.setVisibility(View.VISIBLE);
						tvTeacher.setEllipsize(TruncateAt.END);
						tvTeacher.setMaxLines(2);
					}
				}
				return true;
			}
		});

		List<Recipient> studentList = data.getStudentRealation();
		this.data.setStudentRealation(studentList);
		if(studentList.size() > 0) {
			String studentName = getListStringName(studentList);
			students = getSpanString(studentName);
			setText(tvStudent, students);
		}else{
			findViewById(R.id.rl_student).setVisibility(View.GONE);
		}
		vto = tvStudent.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if(isStudentFirst){
					isStudentFirst = false;
					int lineCount = tvStudent.getLineCount();
					System.out.println(lineCount);
					if(lineCount > 2){
						tvShowAllStudent.setVisibility(View.VISIBLE);
						tvStudent.setEllipsize(TruncateAt.END);
						tvStudent.setMaxLines(2);
					}
				}
				return true;
			}
		});

		List<Recipient> ccList = data.getCopyRealation();
		this.data.setCopyRealation(ccList);
		if(ccList.size() > 0) {
			String ccName = getListStringName(ccList);
			sCCList = getSpanString(ccName);
			setText(tvCCList, sCCList);
		}else{
			findViewById(R.id.rl_ccteacher).setVisibility(View.GONE);
		}
		vto = tvCCList.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if(isCCFirst){
					isCCFirst = false;
					int lineCount = tvCCList.getLineCount();
					System.out.println(lineCount);
					if(lineCount > 2){
						tvShowAllCCList.setVisibility(View.VISIBLE);
						tvCCList.setEllipsize(TruncateAt.END);
						tvCCList.setMaxLines(2);
					}
				}
				return true;
			}
		});

		attachList = data.getListSAI();
		this.data.setListSAI(attachList);
		String content = data.getContent();
		if(!DataUtil.isNullorEmpty(content)) {
			this.data.setContent(content);
			if(content != null && content.length() > 0) {
//			DataUtil.showHtmlSetting(Content, content);
				new CustomWebView(this,Content,content);
			}
		}
		String attachName = getListAttachName(attachList);
		attach = getSpanString(attachName);
		showAttachList();
	}

	private void showAttachList(){
		View view = findViewById(R.id.attach_ll);
		if(attachList != null && attachList.size() > 0){
			view.setVisibility(View.VISIBLE);
			listView = (ListView) findViewById(R.id.attach_list);
			ShowAttachListAdapter listAdapter = new ShowAttachListAdapter(this,attachList);
			listView.setAdapter(listAdapter);
			setListViewHeight(listAdapter);
		}
	}

	public static SpannableString getSpanString(String name,String prefix){
		if(!DataUtil.isNullorEmpty(name)){
			return new SpannableString(prefix + name);
		}
		return null;
	}

	public static SpannableString getSpanString(String name){
		if(!DataUtil.isNullorEmpty(name)){
			return new SpannableString(name);
		}
		return null;
	}

	public static String getListStringName(List<Recipient> list){
		String name = "";
		UserType user = CCApplication.getInstance().getPresentUser();
		
		if(list == null) return name;
		for(Recipient item:list){
			if(!user.getUserId().equals(item.getReceiveObjectId())){
				name += item.getReceiveObjectName();
				name += ",";
			}else{
				name += "我,";
			}
		}
		return name;
	}

	public static boolean isSelf(String userId){
		UserType user = CCApplication.getInstance().getPresentUser();
		if(user.getUserId().equals(userId)){
			return true;
		}
		return false;
	}

	public static String getListAttachName(List<Attachment> list){
		String name = "";
		if(list != null){
			for(Attachment item:list){
				name += item.getFileName();
				name += ",";
			}
		}
		return name;
	}
	
	private void deleteEmail(NewMailInfo data){
		UserType user = CCApplication.getInstance().getPresentUser();
		String userId = "";
		if(user != null){
			userId = user.getUserId();
		}
		JSONObject json = new JSONObject();
		try {
			json.put("userID", userId);
			json.put("emailID",data.getId());
			json.put("curStatus", deleteType);
			json.put("statusByDel", "");
			json.put("queryRes", data.getQueryRes());
			json.put("userType",user.getUserType());
			System.out.println("删除邮件:"+json);
			queryPost(Constants.DELETE_EMAIL,json);
			action = DELETE_MAIL;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void RecoverEmail(NewMailInfo data){
		UserType user = CCApplication.getInstance().getPresentUser();
		String userId = "";
		if(user != null){
			userId = user.getUserId();
		}
		JSONObject json = new JSONObject();
		try {
			json.put("userID", userId);
			json.put("emailId",data.getId());
			json.put("queryRes", data.getQueryRes());
			System.out.println("RecoverEmail:"+json);
			queryPost(Constants.RESTORE_EMAIL,json);
			action = RECOVER_MAIL;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		System.out.println(data);
		if(action == DELETE_MAIL){
			NewBaseBean ret = JsonUtils.fromJson(data,NewBaseBean.class);
			if (ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
				removeItem(this.data,emailType);
				if(!RECYCLE.equals(deleteType)) {
					DataUtil.getToast("已删除至回收站");
				}else{
					DataUtil.getToast("彻底删除");
				}
				finish();
			} else {
				DataUtil.getToast(ret.getServerResult().getResultMessage());
			}
		}else if(action == RECOVER_MAIL) {
			NewBaseBean ret = JsonUtils.fromJson(data,NewBaseBean.class);
			if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
				removeItem(this.data,emailType);
				finish();
			}else{
				DataUtil.getToast(ret.getServerResult().getResultMessage());
			}
		}else{
			MailDetailInfo ret = JsonUtils.fromJson(data, MailDetailInfo.class);
			if (ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
				parseMailDetail(ret);
			} else {
				DataUtil.getToast(ret.getServerResult().getResultMessage());
			}
		}
	}

	private void removeItem(NewMailInfo remove,String type){
		NewEmailBean email = CCApplication.getInstance().getEmail(type);
		List<NewMailInfo> list = email.getMailInfoList();
		if(list != null && remove != null){
			for(NewMailInfo item:list){
				String id = item.getId();
				if(id != null && id.equals(remove.getId())){
					list.remove(item);
					break;
				}
			}
		}
		CCApplication.getInstance().setEmail(email,type);
	}

	private void markRead(NewMailInfo data){
		JSONObject json = new JSONObject();
		UserType user = CCApplication.getInstance().getPresentUser();
		String userId = "";
		if(user != null){
			userId = user.getUserId();
		}
		try{
			json.put("userID",userId);
			json.put("emailId",data.getId());
			json.put("isRead",data.getIsRead());
			json.put("userType",user.getUserType());
			System.out.println("email markRead:"+json);
			queryPost(Constants.EMAIL_BROWSE,json);
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
