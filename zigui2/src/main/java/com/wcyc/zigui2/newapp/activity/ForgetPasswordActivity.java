/**   
 * 文件名：com.wcyc.zigui.home.ForgetPasswordActicity.java   
 *   
 * 版本信息：   
 * 日期：2014年9月28日 下午2:55:38  
 * Copyright 惟楚有材 Corporation 2014    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.newapp.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.activity.NewPasswordActivity;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.module.notice.NewNoticeBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;

//2016年3月21日 
/**
 * 忘记密码Activity
 * 
 * @author xiehua
 * @version 1.01
 */

public class ForgetPasswordActivity extends BaseActivity implements OnClickListener{


	private EditText fogetpassworld_phoneNumber, fogetpassworld_et_verify;
	private Button fogetpassworld_btn_send , fogetpassworld_btn_next;
	private ImageView fogetpassworld_delete_phoneNumber_icon;
	
	public static final String PHONE = "phone";
	public static final String CODE = "captcha";
	private Button titleButton;
	private String phone,captcha,title;
	private boolean isFirstLogin = false;
	private ImageView fogetpassworld_et_verify_delete_icon;
	private static final int GET_CODE = 0;
	private static final int VALIDE_CODE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fogetpassword);
		parseIntent();
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		Log.i("TAG","SHDH");
		//手机号

		fogetpassworld_phoneNumber = (EditText) findViewById(R.id.fogetpassworld_phoneNumber);
		fogetpassworld_phoneNumber.setText(phone);
		
		fogetpassworld_delete_phoneNumber_icon = (ImageView) findViewById(R.id.fogetpassworld_delete_phoneNumber_icon);
		TextFilter textFilter2 = new TextFilter(fogetpassworld_phoneNumber);
		fogetpassworld_phoneNumber.addTextChangedListener(textFilter2);//设置不能输入空格
		textFilter2.setEditeTextClearListener(fogetpassworld_phoneNumber, fogetpassworld_delete_phoneNumber_icon);
		
		//验证码
		fogetpassworld_et_verify = (EditText) findViewById(R.id.fogetpassworld_et_verify);
		fogetpassworld_et_verify_delete_icon = (ImageView) findViewById(R.id.fogetpassworld_et_verify_delete_icon);
		TextFilter textFilter3 = new TextFilter(fogetpassworld_et_verify);
		fogetpassworld_et_verify.addTextChangedListener(textFilter3);//设置不能输入空格
		textFilter3.setEditeTextClearListener(fogetpassworld_et_verify, fogetpassworld_et_verify_delete_icon);
			
		fogetpassworld_btn_send = (Button) findViewById(R.id.fogetpassworld_btn_send);
		fogetpassworld_btn_send.setOnClickListener(this);
		fogetpassworld_btn_next = (Button) findViewById(R.id.fogetpassworld_btn_next);
		fogetpassworld_btn_next.setOnClickListener(this);
		if(DataUtil.isNullorEmpty(phone)){
			getCodeButtonState(false);
		}else{
			getCodeButtonState(true);
			fogetpassworld_et_verify.requestFocus();
		}
		nextButtonState(false);
		
		titleButton = (Button) findViewById(R.id.title_btn);
		titleButton.setOnClickListener(this);
		titleButton.setText(R.string.fogerpasswordtext);
		TextView titleText2 = (TextView) findViewById(R.id.title_text_2);
		Button title_imgbtn = (Button) findViewById(R.id.title_btn);
		ImageView image = (ImageView) findViewById(R.id.title_arrow_iv);
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		title_imgbtn.setVisibility(View.GONE);
//		image.setVisibility(View.GONE);
		titleText2.setVisibility(View.VISIBLE);
		if(isFirstLogin == false){
			titleText2.setText(R.string.forgetpasswd);
		}else{
			titleText2.setText(title);
			fogetpassworld_phoneNumber.setEnabled(false);
		}
		inputState();
	}
	
	private void parseIntent(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		phone = bundle.getString("phone");
		title = bundle.getString("title");
		isFirstLogin = bundle.getBoolean("isFirstLogin");
	}
	
	private void inputState(){
		fogetpassworld_phoneNumber.addTextChangedListener(new TextWatcher(){

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
				if(arg0.length() > 0){
					getCodeButtonState(true);
				}else{
					getCodeButtonState(false);
					nextButtonState(false);
				}
			}
			
		});
		fogetpassworld_et_verify.addTextChangedListener(new TextWatcher(){

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
				if(arg0.length() > 0){
					nextButtonState(true);
				}else{
					nextButtonState(false);
				}
			}
			
		});
	}

	@Override
	protected void getMessage(String data) {
		
		NewBaseBean bb = JsonUtils.fromJson(data, NewBaseBean.class);
				
		switch (action) {
		case GET_CODE:
			if(bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
				DataUtil.getToast("验证码已发送至您手机");
				runtime();
			}else{
				int code = bb.getServerResult().getResultCode();
				switch(code){
				case 402:
					fogetpassworld_phoneNumber.requestFocus();
					break;
				case Constants.ACCOUNT_DISABLE_CODE:
					fogetpassworld_phoneNumber.requestFocus();
					DataUtil.getToast("账户被禁用，请与管理员联系");
					return;
				case 102003:
					DataUtil.getToast("您输入的手机号不存在！");
					return;
				}
				DataUtil.getToast(bb.getServerResult().getResultMessage());
			}
			break;
		case VALIDE_CODE:
			System.out.println("valid code:"+data);
			if(bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
				JSONObject json;
				try {
					json = new JSONObject(data);
					String result = (String) json.get("result");
					if("1".equals(result)){
						Bundle bundle = new Bundle();
						bundle.putString(PHONE, phone);
						bundle.putString(CODE, captcha);
						newActivity(NewPasswordActivity.class,bundle);
						ForgetPasswordActivity.this.finish();
					}else{
						DataUtil.getToast("输入验证码错误");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				DataUtil.getToast(bb.getServerResult().getResultMessage());
			}
			
			break;
		default:
			break;
		}
	}

	private void runtime() {
		time = 60;
		getCodeButtonState(false);
		handler.removeCallbacks(runnable_time);
		handler.postAtTime(runnable_time, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fogetpassworld_btn_send:
			sendVerify();
			break;
		case R.id.fogetpassworld_delete_phoneNumber_icon:
			fogetpassworld_phoneNumber.getEditableText().clear();
			nextButtonState(false);
			break;
		case R.id.title_btn:
			ForgetPasswordActivity.this.finish();
			break;
		case R.id.fogetpassworld_btn_next:
			Bundle bundle = new Bundle();
			phone = fogetpassworld_phoneNumber.getText().toString();
			bundle.putString(PHONE, phone);
			captcha = fogetpassworld_et_verify.getText().toString();
			bundle.putString(CODE, captcha);
			validCode();
			//newActivity(NewPasswordActivity.class,bundle);
			//ForgetPasswordActivity.this.finish();
			break;
		default:
			break;
		}
	}
	private void getCodeButtonState(boolean enable){
		if(enable){
			fogetpassworld_btn_send.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
		}else{
			fogetpassworld_btn_send.setBackgroundResource(R.drawable.shape_only_login_btn);
		}
		fogetpassworld_btn_send.setClickable(enable);
	}
	private void nextButtonState(boolean enable){
		if(enable){
			fogetpassworld_btn_next.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
		}else {
			fogetpassworld_btn_next.setBackgroundResource(R.drawable.shape_only_login_btn);
		}
		fogetpassworld_btn_next.setClickable(enable);
	}
	/**
	 * 获取验证码
	 */
	private void sendVerify() {
		if(!isLoading()){
			phone = fogetpassworld_phoneNumber.getText().toString();
			if(DataUtil.isNull(phone)){
				DataUtil.getToast("请输入手机号码");
				fogetpassworld_phoneNumber.requestFocus();
				return;
			}
			if(!DataUtil.checkPhone(phone)){
				DataUtil.getToast("请输入正确的手机号码");
				return;
			}
			JSONObject json = new JSONObject();
			try {
				json.put("userPhone", phone);
				json.put("type", "1");
				json.put("clientSys","1");//1:子贵校园，2：子贵学苑
			} catch (JSONException e) {
				e.printStackTrace();
			}
			action = GET_CODE;
			queryPost(Constants.GETCODE_URL, json);
		}
	}
	
	//校验验证码
	private void validCode(){
		JSONObject json = new JSONObject();
		try{
			json.put("mobile", phone);
			json.put("buzzType","1");//找回密码
			json.put("checkCode", captcha);
			action = VALIDE_CODE;
			queryPost(Constants.VALID_CODE,json);
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	//验证码倒计时功能
	private int time;
    Runnable runnable_time = new Runnable() {
        @Override
        public void run() {
        	setTimeView();
        	time -= 1;
        	if(time > 0){
        		handler.postDelayed(this, 1000);
        	}else{
        		fogetpassworld_btn_send.setText("重新获取");
        		fogetpassworld_btn_send.setClickable(true);
        		fogetpassworld_btn_send.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
        	}
	    }
	};
	    
	    
	private ImageView repassDelete;
    private void setTimeView(){
    	fogetpassworld_btn_send.setText("重新获取"+time + "s");
    }
	   
}
