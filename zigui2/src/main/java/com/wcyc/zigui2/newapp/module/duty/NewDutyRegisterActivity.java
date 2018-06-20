package com.wcyc.zigui2.newapp.module.duty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.dao.DBSharedPreferences;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;
import com.wcyc.zigui2.widget.CustomDialog;
/**
 * 值班登记
 * 
 * @author 郑国栋
 * 2016-6-30
 * @version 2.0
 */
public class NewDutyRegisterActivity extends BaseActivity implements
		OnClickListener {

	private TextView new_content;
	private TextView title2_off;
	private TextView title2_ok;
	private EditText morning_study_sdudent_et;
	private EditText morning_study_teacher_et;
	private EditText inclass_study_sdudent_et;
	private EditText inclass_study_teacher_et;
	private EditText evening_study_sdudent_et;
	private EditText evening_study_teacher_et;
	private EditText school_event_record_et;
	
	private String morning_study_sdudent_et_str;
	private String morning_study_teacher_et_str;
	private String inclass_study_sdudent_et_str;
	private String inclass_study_teacher_et_str;
	private String evening_study_sdudent_et_str;
	private String evening_study_teacher_et_str;
	private String school_event_record_et_str;
	private String inputDutyID;
	private String teaUserName;
	private String teaMobile;
	private String timeRegister;
	private TextView duty_man_name;
	private TextView duty_man_phone;
	private CustomDialog dialog;
	private String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_duty_register);

		initView();
		initDatas();
		initEvents();
		
		inputState();
	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title2_off = (TextView) findViewById(R.id.title2_off);// 返回键
		title2_ok = (TextView) findViewById(R.id.title2_ok);//确定键
		
		duty_man_name = (TextView) findViewById(R.id.duty_man_name);
		duty_man_phone = (TextView) findViewById(R.id.duty_man_phone);
		
		morning_study_sdudent_et = (EditText)findViewById(R.id.morning_study_sdudent_et);
		morning_study_teacher_et = (EditText)findViewById(R.id.morning_study_teacher_et);
		
		inclass_study_sdudent_et = (EditText)findViewById(R.id.inclass_study_sdudent_et);
		inclass_study_teacher_et = (EditText)findViewById(R.id.inclass_study_teacher_et);
		
		evening_study_sdudent_et = (EditText)findViewById(R.id.evening_study_sdudent_et);
		evening_study_teacher_et = (EditText)findViewById(R.id.evening_study_teacher_et);
		
		school_event_record_et = (EditText)findViewById(R.id.school_event_record_et);
		
		
	}

	// 初始化数据
	private void initDatas() {
		new_content.setText("值班登记");
		userid = CCApplication.getInstance().getPresentUser().getUserId();
		dbsp = new DBSharedPreferences(this);
		
		inputDutyID = getIntent().getStringExtra("inputDutyID");
		teaUserName = getIntent().getStringExtra("TeaUserName");
		teaMobile = getIntent().getStringExtra("TeaMobile");
		timeRegister = getIntent().getStringExtra("timeRegister");
		duty_man_name.setText(teaUserName);
		duty_man_phone.setText(teaMobile);
				
		try {
			NewDutyDiaryBean newDutyDiaryBean = new NewDutyDiaryBean();
			JSONObject json = new JSONObject();
			json.put("schoolId", schoolId);
			json.put("inputDutyID", inputDutyID);
			if (!DataUtil.isNetworkAvailable(this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
			}else{
//				System.out.println("值班日志入参=====" + json);
				String url = new StringBuilder(Constants.SERVER_URL).append(
						Constants.GET_DUTY_LOG).toString();//
				String result = HttpHelper.httpPostJson(this, url, json);
//				System.out.println("值班日志出参=====" + result);
				JSONObject jsonB = new JSONObject(result);
				String dutyLog = jsonB.getString("dutyLog");
				newDutyDiaryBean = JsonUtils.fromJson(dutyLog,
						NewDutyDiaryBean.class);
				morning_study_sdudent_et.setText(newDutyDiaryBean.getEarlyStudyStu());
				morning_study_teacher_et.setText(newDutyDiaryBean.getEarlyStudyTea());
				inclass_study_sdudent_et.setText(newDutyDiaryBean.getClassStudent());
				inclass_study_teacher_et.setText(newDutyDiaryBean.getClassTeacher());
				evening_study_sdudent_et.setText(newDutyDiaryBean.getNightStudyStu());
				evening_study_teacher_et.setText(newDutyDiaryBean.getNightStudyTea());
				school_event_record_et.setText(newDutyDiaryBean.getSchoolStory());	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置点击效果监听器
	private void initEvents() {
		title2_off.setVisibility(View.VISIBLE);
		title2_off.setOnClickListener(this);
		
		title2_ok.setOnClickListener(this);
		title2_ok.setClickable(false);
		title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()){
		case R.id.title2_off:
			
			getAllTvOrEtVale();
			
			if(isHaveOneNotNull()){
				exitOrNot();
			}else{
				NewDutyRegisterActivity.this.finish();
			}
			
			break;
		case R.id.title2_ok:// 值班登记请求确定键
			
			if (!DataUtil.isNetworkAvailable(NewDutyRegisterActivity.this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}

			if (!isLoading()) {
				httpDutyRegister();
			}
			
			
			break;
		default:
			break;
		
		}
	}
	
	// 值班登记请求
	private void httpDutyRegister(){

		getAllTvOrEtVale();
		
		
		try {
			JSONObject json = new JSONObject();
			json.put("schoolId", schoolId);
			json.put("inputDutyID", inputDutyID);
			json.put("earlyStudyStu", morning_study_sdudent_et_str);
			json.put("earlyStudyTea", morning_study_teacher_et_str);
			json.put("classStudent", inclass_study_sdudent_et_str);
			json.put("classTeacher", inclass_study_teacher_et_str);
			json.put("nightStudyStu", evening_study_sdudent_et_str);
			json.put("nightStudyTea", evening_study_teacher_et_str);//
			json.put("schoolStory", school_event_record_et_str);//
			
			System.out.println("值班登记入参====="+json);
			queryPost(Constants.INPUT_DUTY, json);
			
			
			json.put("operatorTime", timeRegister+" ");
			json.put("dutyUserId", userid);
			dbsp.putString("dutyRegister", json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void getMessage(String data) {

		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if (ret.getServerResult().getResultCode() != 200) {// 请求失败
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		} else {
			try {
				DataUtil.getToast("值班登记发布成功！");
				
				
				NewDutyRegisterActivity.this.finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
			getAllTvOrEtVale();
			
			if(isHaveOneNotNull()){
				exitOrNot();
				return true;
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 控制CustomDialog按钮效果.
	 */
	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);
			if (0 != msg.arg1) {
			}
			switch (msg.what) {
			case CustomDialog.DIALOG_CANCEL:// 取消退出
				dialog.dismiss();
				break;
			case CustomDialog.DIALOG_SURE:// 确认退出
//				finish();
				NewDutyRegisterActivity.this.finish();
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	private DBSharedPreferences dbsp;
	
	//所有需要的  textview或editText 是否有一个不为空
	private boolean isHaveOneNotNull(){
		return !DataUtil.isNullorEmpty(morning_study_sdudent_et_str)
				|| !DataUtil.isNullorEmpty(morning_study_teacher_et_str)
				|| !DataUtil.isNullorEmpty(inclass_study_sdudent_et_str)
				|| !DataUtil.isNullorEmpty(inclass_study_teacher_et_str)
				|| !DataUtil.isNullorEmpty(evening_study_sdudent_et_str)
				|| !DataUtil.isNullorEmpty(evening_study_teacher_et_str)
				|| !DataUtil.isNullorEmpty(school_event_record_et_str);
	}
	
	//所有需要的  textview和editText获得值
	private void getAllTvOrEtVale(){
		morning_study_sdudent_et_str = morning_study_sdudent_et.getText().toString().trim();
		morning_study_teacher_et_str = morning_study_teacher_et.getText().toString().trim();
		inclass_study_sdudent_et_str = inclass_study_sdudent_et.getText().toString().trim();
		inclass_study_teacher_et_str = inclass_study_teacher_et.getText().toString().trim();
		evening_study_sdudent_et_str = evening_study_sdudent_et.getText().toString().trim();
		evening_study_teacher_et_str = evening_study_teacher_et.getText().toString().trim();
		school_event_record_et_str = school_event_record_et.getText().toString().trim();
	}
	
	//所有需要的  textview或editText 是否有一个有值
	private boolean isOneHaveLength(){
		return morning_study_sdudent_et_str.length() > 0 || morning_study_teacher_et_str.length() > 0
				|| inclass_study_sdudent_et_str.length() > 0 || inclass_study_teacher_et_str.length() > 0
				|| evening_study_sdudent_et_str.length() > 0 || evening_study_teacher_et_str.length() > 0
				|| school_event_record_et_str.length() > 0;
	}
	
	//确定键是否可点击
	private void isOkAllowClick(){
		if (isOneHaveLength()) {
			title2_ok.setClickable(true);
			title2_ok.setTextColor(getResources().getColor(
					R.color.font_darkblue));
		} else {
			title2_ok.setClickable(false);
			title2_ok.setTextColor(getResources().getColor(
					R.color.font_lightgray));
		}
	}
	
	private void inputState() {
		//早自习学生编辑框监听
		morning_study_sdudent_et.addTextChangedListener(new TextWatcher() {
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
				getAllTvOrEtVale();
				isOkAllowClick();
				if(s.length()>399){
					DataUtil.getToast("情况记录不能超过400字！");
				}
			}
		});
		//早自习老师编辑框监听
		morning_study_teacher_et.addTextChangedListener(new TextWatcher() {
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
				getAllTvOrEtVale();
				isOkAllowClick();
				
				if(s.length()>399){
					DataUtil.getToast("情况记录不能超过400字！");
				}
			}
		});
		//上课期间学生编辑框监听
		inclass_study_sdudent_et.addTextChangedListener(new TextWatcher() {
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
				getAllTvOrEtVale();
				isOkAllowClick();
				
				if(s.length()>399){
					DataUtil.getToast("情况记录不能超过400字！");
				}
			}
		});
		//上课期间老师编辑框监听
		inclass_study_teacher_et.addTextChangedListener(new TextWatcher() {
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
				getAllTvOrEtVale();
				isOkAllowClick();
				
				if(s.length()>399){
					DataUtil.getToast("情况记录不能超过400字！");
				}
			}
		});
		//晚自习学生编辑框监听
		evening_study_sdudent_et.addTextChangedListener(new TextWatcher() {
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
				getAllTvOrEtVale();
				isOkAllowClick();
				
				if(s.length()>399){
					DataUtil.getToast("情况记录不能超过400字！");
				}
			}
		});
		//晚自习老师编辑框监听
		evening_study_teacher_et.addTextChangedListener(new TextWatcher() {
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
				getAllTvOrEtVale();
				isOkAllowClick();
				
				if(s.length()>399){
					DataUtil.getToast("情况记录不能超过400字！");
				}
			}
		});
		//学校效果记载编辑框监听
		school_event_record_et.addTextChangedListener(new TextWatcher() {
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
				getAllTvOrEtVale();
				isOkAllowClick();
				
				if(s.length()>399){
					DataUtil.getToast("情况记录不能超过400字！");
				}
			}
		});
	}
}
