package com.wcyc.zigui2.newapp.module.mailbox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.adapter.AttachmentListAdapter;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllDeptList;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.AllTeacherList.TeacherMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
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
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ComposeEmailToMasterActivity extends BaseActivity 
		implements ImageUploadAsyncTaskListener{
	private TextView cancel,enter;
	private Button addAttach,suggest,question,good,praise,other;

	private EditText replyContent,replyTitle;
	private ListView listView;
	//private SpinnerAdapter adapter;
//	private ArrayAdapter<String> adapter;
	private String[] list = {"投诉建议","问题咨询","好人好事","表彰嘉奖","其他"};
	private static final int PICK_PICTURE = 100;
	private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
	private int select = SUGGEST;
	private UploadFileResult ret;
	private boolean isChecked;
	private CustomDialog dialog;
	private static final int SUGGEST = 1;
	private static final int QUESTION = 2;
	private static final int GOOD = 3;
	private static final int PRAISE = 4;
	private static final int OTHER = 9;
	private static int num = 0;
	private List<String> attachment = new ArrayList<String>();
	private AttachmentListAdapter attachAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_email_to_master);
		initView();
		initEvent();
	}
	
	private void initView(){
		TextView title = (TextView) findViewById(R.id.new_content);
		title.setText("给校长写信");
		cancel = (TextView) findViewById(R.id.title2_off);
		enter = (TextView) findViewById(R.id.title2_ok);
		enter.setEnabled(false);
		enter.setTextColor(Color.GRAY);
		addAttach = (Button)findViewById(R.id.add_attach);
		replyContent = (EditText) findViewById(R.id.content);
		replyTitle = (EditText)findViewById(R.id.subject);
		listView = (ListView) findViewById(R.id.attachList);
		suggest = (Button) findViewById(R.id.suggest);
		question = (Button) findViewById(R.id.question);
		good = (Button) findViewById(R.id.good);
		praise = (Button) findViewById(R.id.praise);
		other = (Button) findViewById(R.id.other);
	}
		
	private void initEvent(){
		validInput();
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(hasInput()){
					exitOrNot();
				}else{
					cancel();
					finish();
				}
			}
			
		});
		
		enter.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(hasSubjectInput() == false){//邮件主题未输入
					sendOrNot();
				}else{
					sendMail();
				}
			}
			
		});
		
		addAttach.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ComposeEmailToMasterActivity.this,SelectImageActivity.class);
				intent.putExtra("limit", 8);
				intent.putStringArrayListExtra("addPic", imagePaths);
				intent.putExtra("attachmentLimit", "attachmentLimit");
				
				startActivityForResult(intent,PICK_PICTURE);
			}
			
		});
		
		
		suggest.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				select = SUGGEST;
				changeButtonBackGround(suggest);
			}
			
		});
		
		question.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				select = QUESTION;
				changeButtonBackGround(question);
			}
			
		});
		
		good.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				select = GOOD;
				changeButtonBackGround(good);
			}
			
		});
		
		praise.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				select = PRAISE;
				changeButtonBackGround(praise);
			}
			
		});
		
		other.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				select = OTHER;
				changeButtonBackGround(other);
			}
			
		});

	}
	private void cancel(){
		Intent intent = new Intent();
		setResult(RESULT_OK,intent);
	}
	
	private void changeButtonBackGround(Button button){
		int color = getResources().getColor(R.color.font_gray);
		suggest.setBackgroundResource(R.drawable.btn2_leixing_normal);
		suggest.setTextColor(color);
		question.setBackgroundResource(R.drawable.btn2_leixing_normal);
		question.setTextColor(color);
		good.setBackgroundResource(R.drawable.btn2_leixing_normal);
		good.setTextColor(color);
		praise.setBackgroundResource(R.drawable.btn2_leixing_normal);
		praise.setTextColor(color);
		other.setBackgroundResource(R.drawable.btn2_leixing_normal);
		other.setTextColor(color);
		button.setTextColor(getResources().getColor(R.color.white));
		button.setBackgroundResource(R.drawable.btn2_leixing_selected);
	}
	
	protected void validInput(){
		replyTitle.addTextChangedListener(new TextWatcher(){

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
				if(arg0.length() >= 200){
					DataUtil.getToast("邮件主题不能超过200个字");
				}
			}
			
		});
		
		//邮件内容不能超过5000个字
		replyContent.addTextChangedListener(new TextWatcher(){

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
				if(arg0.length() >= 5000){
					DataUtil.getToast("邮件内容不能超过5000个字");
				}else if(arg0.length() > 0){
					enableSend(true);
				}else{
					if(!hasContent()) {
						enableSend(false);
					}
				}
			}
			
		});
	}

	private void enableSend(boolean enabled){
		enter.setEnabled(enabled);
		if(enabled){
			enter.setTextColor(getResources().getColor(R.color.blue));
		}else {
			enter.setTextColor(Color.GRAY);
		}
	}

	protected void sendMail(){
		if(imagePaths.isEmpty()){
			addMail();
		}else{
			uploadFile();
		}
	}
	
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		System.out.println("addmail:"+data);
		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if(ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE){
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		}else{
			DataUtil.getToast("发布成功!");
			Intent intent = new Intent();
			intent.putExtra("composeMail", true);
			setResult(RESULT_OK,intent);
			finish();
		}
	}
	
	private boolean hasSubjectInput(){//邮件主题是否输入
		String title = replyTitle.getText().toString();
		return title.length() != 0;
	}
	
	private boolean hasInput(){//是否有输入
		String title = replyTitle.getText().toString();
		String content = replyContent.getText().toString();
		return title.length() > 0 || content.length() > 0
				|| (listView.getAdapter() != null && !listView.getAdapter().isEmpty());
	}

	private boolean hasContent(){
		String content = replyContent.getText().toString();
		return content.length() > 0
				|| (listView.getAdapter() != null && !listView.getAdapter().isEmpty());
	}

	private void sendOrNot(){
		dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.customdialog, handler);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.setTitle("您的邮件未填写主题，确定要发送吗?");
		dialog.setContent("");
	}
	
	private void exitOrNot(){
		dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.customdialog, exithandler);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.setTitle("退出此次编辑?");
		dialog.setContent("");
	}
	
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(hasInput()){
				exitOrNot();
				return true;
			}else{
				cancel();
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
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
				sendMail();
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	
	Handler exithandler = new Handler(){
		public void dispatchMessage(android.os.Message msg){
			super.dispatchMessage(msg);
			switch(msg.what){
			case CustomDialog.DIALOG_CANCEL:
				dialog.dismiss();
				break;
			case CustomDialog.DIALOG_SURE:
				cancel();
				finish();
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	
	protected void uploadFile(){
		for(int i = 0 ; i < imagePaths.size(); i++){
			ImageUploadAsyncTask upload = new ImageUploadAsyncTask(
					this,Constants.PIC_TYPE,imagePaths.get(i),Constants.UPLOAD_URL,this);
			upload.execute();
		}
	}
	
	protected void addMail(){
		ComposeMasterMailReq req = new ComposeMasterMailReq();
		UserType user = CCApplication.app.getPresentUser();

		if(user != null){
			if("3".equals(user.getUserType())){
				req.setUserId(user.getChildId());
				req.setUserType("1");
			}else{
				req.setUserId(user.getUserId());
				req.setUserType(user.getUserType());
			}
		}

		if(CCApplication.getInstance().isCurUserParent()){
			req.setUserName(user.getChildName()+user.getRelationTypeName());
		}else {
			req.setUserName(CCApplication.app.getMemberInfo().getUserName());
		}
		req.setSchoolId(user.getSchoolId());

		req.setAttachmentIdList(attachment);
		req.setAttachmentNum(attachment.size()+"");
		req.setIsSms("0");//写死
		req.setMailContent(replyContent.getText().toString());
		req.setMailTitle(replyTitle.getText().toString());
		req.setMailType(select+"");
		req.setTypeName(setTypeName(select));
		Gson gson = new Gson();
		String string = gson.toJson(req);
		System.out.println(this + string);
		try {
			JSONObject json = new JSONObject(string);
			queryPost(Constants.ADD_MAIL_URL,json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String setTypeName(int type){
		switch(type){
		case SUGGEST:
			return list[0];
		case QUESTION:
			return list[1];
		case GOOD:
			return list[2];
		case PRAISE:
			return list[3];
		case OTHER:
			return list[4];
		}
		return "";
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
					if(imagePaths.size() > 0){
						enableSend(true);
					}
				}
				data.getBooleanExtra("is_compress", true);
				attachAdapter = new AttachmentListAdapter(this,imagePaths);
				listView.setAdapter(attachAdapter);
				listView.setVisibility(View.VISIBLE);
				attachAdapter.registerDataSetObserver(new DataSetObserver() {
					@Override
					public void onChanged() {
						super.onChanged();
						if(attachAdapter.getCount() == 0){
							if(!hasContent()){
								enableSend(false);
							}
						}
					}
				});
				break;
			}
		}
	}

	@Override
	public void onImageUploadCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onImageUploadComplete(String result) {
		// TODO Auto-generated method stub
		System.out.println("result:"+result);
		num++;
		ret = JsonUtils.fromJson(result, UploadFileResult.class);
		if(ret != null){
			if(ret.getSuccFiles() != null){
				Set<String> set = ret.getSuccFiles().keySet();
				for(String string:set){
					attachment.add(string); 
				}
			}
		}
		if(num == imagePaths.size()){
			addMail();
			num = 0;
		}
	}
}
