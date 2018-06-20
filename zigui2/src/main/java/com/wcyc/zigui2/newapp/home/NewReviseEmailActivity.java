package com.wcyc.zigui2.newapp.home;

import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 修改邮箱页面类.
 * 
 * @author gdzheng 
 * @time 2016-04-11
 * @version 2.0
 */
public class NewReviseEmailActivity extends BaseActivity implements
OnClickListener{
	private TextView new_content;
	private LinearLayout title_back;
	private EditText revise_email_et;
	private String revise_email;
	private Button revise_email_btn;
	private ImageView revise_email_et_delete_icon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_revise_email);
		initView();
		initDatas();
		initEvents();
		
		inputState();
	}
	
	/**
	 * 初始化控件
	 */

	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);//标题
		title_back = (LinearLayout) findViewById(R.id.title_back);//返回键
		
		revise_email_et = (EditText) findViewById(R.id.revise_email_et);//邮箱
		revise_email_et_delete_icon = (ImageView) findViewById(R.id.revise_email_et_delete_icon);
		TextFilter textFilter1 = new TextFilter(revise_email_et);
		revise_email_et.addTextChangedListener(textFilter1);// 设置不能输入空格
		textFilter1.setEditeTextClearListener(revise_email_et,
				revise_email_et_delete_icon);
		
		revise_email_btn = (Button) findViewById(R.id.revise_email_btn);
		revise_email_btn.setOnClickListener(this);
		revise_email_btn.setClickable(false);
		revise_email_btn.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
	}

	/**
	 * 初始化数据
	 */
	
	private void initDatas() {
		new_content.setText("修改邮箱地址");
	}
	/**
	 * 效果控制
	 */

	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);
		
	}
	
	
	

	@Override
	protected void getMessage(String data) {
		
		NewBaseBean ret=JsonUtils.fromJson(data, NewBaseBean.class);
		if(ret.getServerResult().getResultCode()==200){
			//提交成功			
			Bundle bundle=new Bundle();
			//封装数据
			bundle.putString("revise_email", revise_email);
			//封装bundle
			Intent intent=new Intent();
			intent.setClass(NewReviseEmailActivity.this, NewTeacherAccountActivity.class);
			intent.putExtras(bundle);
			
			//返回
			NewReviseEmailActivity.this.setResult(RESULT_OK, intent);//返回上一个界面，并带参数
			NewReviseEmailActivity.this.finish();
		}else{
			//提交失败
			DataUtil.getToast(ret.getServerResult().getResultMessage());
			
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.title_back:
			NewReviseEmailActivity.this.finish();
			break;
		case R.id.revise_email_btn:
			//如果邮箱不正确
			if(false==Validate()){
				return;
			}
			String userid=CCApplication.app.getPresentUser().getUserId();
			JSONObject json=new JSONObject();
			try {
				json.put("userId", userid);
				json.put("email",revise_email);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			if(!isLoading()){
				model.queryPost(Constants.MODIFY_USER_INFO_ZGD, json);
			}
			
		}
			
		
	}
	
	/*
	 * 输入参数校验
	 * 
	 * 
	 * 
	 * */
	
	private boolean Validate(){
		//获得输入的邮箱
		
		revise_email = revise_email_et.getText().toString();
		
		if (DataUtil.isNull(revise_email)) {
			DataUtil.getToast("请输入你的邮箱地址");
			return false;
		}
		if(!DataUtil.checkEmail(revise_email)){
			DataUtil.getToast("请输入正确的邮箱");
			return false;
		}
		return true;
	}
	
	
	private void inputState() {
		revise_email_et.addTextChangedListener(new TextWatcher() {

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
				
				if (s.length() > 0) {
					revise_email_btn.setClickable(true);
					revise_email_btn
							.setBackgroundResource(R.drawable.btn_blue_selector);
					
				} else {
					revise_email_btn.setClickable(false);
					revise_email_btn
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
					
				}
				
			}
			
		});
	}
	
	
}
