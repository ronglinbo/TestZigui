/**   
 * 文件名：ForgetPasswordActicity.java   
 *   
 *   
 */

package com.wcyc.zigui2.newapp.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.BaseBean;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.ForgetPasswordActivity;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;

/**
 * 忘记密码Activity
 * 
 * @author 
 * @version 1.01
 */
public class NewPasswordActivity extends BaseActivity implements OnClickListener{

	private EditText fogetpassworld_phoneNumber, fogetpassworld_et_verify,fogetpassworld_newpaswworld,fogetpassworld_renewpaswworld;
	private Button fogetpassworld_btn_send , fogetpassworld_btn_sure;
	private ImageView fogetpassworld_delete_phoneNumber_icon;

	private final String GETPSW_URL = "/getPassword";
	private Button titleButton;
	private ImageView newPassDele;
	private View back;
	private String newPassword;
	private static final int ACTION_CHANGE_PSW = 1;
	private static final int ACTION_ACTIVE = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_resetpassword);
		initView();
		
		inputState();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		//新密码
		fogetpassworld_newpaswworld = (EditText) findViewById(R.id.fogetpassworld_newpaswworld);
		newPassDele = (ImageView) findViewById(R.id.updateemail_newpass_delete);
		TextFilter textFilter = new TextFilter(fogetpassworld_newpaswworld);
		fogetpassworld_newpaswworld.addTextChangedListener(textFilter);//设置不能输入空格
		textFilter.setEditeTextClearListener(fogetpassworld_newpaswworld, newPassDele);
		
		
		//再次输入新密码
		fogetpassworld_renewpaswworld = (EditText) findViewById(R.id.fogetpassworld_renewpaswworld);
		repassDelete = (ImageView) findViewById(R.id.updateemail_repass_delete);
		TextFilter textFilter1 = new TextFilter(fogetpassworld_renewpaswworld);
		fogetpassworld_renewpaswworld.addTextChangedListener(textFilter1);//设置不能输入空格
		textFilter1.setEditeTextClearListener(fogetpassworld_renewpaswworld, repassDelete);
		
		
		fogetpassworld_btn_sure = (Button) findViewById(R.id.fogetpassworld_btn_sure);
		fogetpassworld_btn_sure.setOnClickListener(this);
		fogetpassworld_btn_sure.setClickable(false);
		fogetpassworld_btn_sure.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
		
		titleButton = (Button) findViewById(R.id.title_btn);
		titleButton.setOnClickListener(this);
		back = findViewById(R.id.title_back);
		back.setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.title_text_2);
		title.setVisibility(View.VISIBLE);
		title.setText(R.string.reset_passsword);
//		titleButton.setText(R.string.fogerpasswordtext);
	}

	@Override
	protected void getMessage(String data) {
		System.out.println("data:"+data);
		NewBaseBean bb = JsonUtils.fromJson(data, NewBaseBean.class);
		switch (action) {
		case ACTION_CHANGE_PSW:
			if(bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
				DataUtil.getToast("密码重置成功，请用新密码登录");
				CCApplication.app.savePhonePwd(newPassword);
				NewMemberBean member = CCApplication.app.getMemberInfo();
				if(member != null){
					member.setIsNeedModifyPwd("0");
					active();
				}else{
					NewPasswordActivity.this.finish();
				}
			}else{
				if(bb.getServerResult().getResultCode() == 402){
					fogetpassworld_phoneNumber.requestFocus();
				}
				DataUtil.getToast(bb.getServerResult().getResultMessage());
			}
			break;
		case ACTION_ACTIVE:
			if(bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
				NewPasswordActivity.this.finish();
			}else{
				DataUtil.getToast(bb.getServerResult().getResultMessage());
			}
			break;
		default:
			break;
		}
	}

	private void active(){
		JSONObject json = new JSONObject();
		queryPost(Constants.LOGIN_ACTIVE,json);
		System.out.println("active:"+json);
		action = ACTION_ACTIVE;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fogetpassworld_btn_sure:
			sendNewPwd();
			break;
		case R.id.fogetpassworld_delete_phoneNumber_icon:
			fogetpassworld_phoneNumber.getEditableText().clear();
			break;
		case R.id.title_btn:
			case R.id.title_back:
			NewPasswordActivity.this.finish();
			break;
		default:
			break;
		}
	}

	
	/**
	 * 发送新的密码给服务端
	 */
	private void sendNewPwd() {
		if(!isLoading()){
			String phone = getIntent().getExtras().getString(ForgetPasswordActivity.PHONE);
			String captcha = getIntent().getExtras().getString(ForgetPasswordActivity.CODE);
			String pwd1 = fogetpassworld_newpaswworld.getText().toString();
			String pwd2 = fogetpassworld_renewpaswworld.getText().toString();
			
			if(DataUtil.isNull(phone)){
				DataUtil.getToast("请输入手机号码");
			    fogetpassworld_phoneNumber.requestFocus();
				return;
			}
			if(DataUtil.isNull(captcha)){
				DataUtil.getToast("请输入验证码");
				return;
			}
			if(!DataUtil.checkPhone(phone)){
				DataUtil.getToast("请输入正确的手机号码");
				return;
			}
			if(DataUtil.isNull(pwd1)){
				DataUtil.getToast("请重新设置密码");
				fogetpassworld_newpaswworld.setText("");
				fogetpassworld_renewpaswworld.setText("");
				fogetpassworld_newpaswworld.requestFocus();
				return;
			}
			if(!DataUtil.isLength(pwd1, 6, 20)){
				DataUtil.getToast("请输入6-20位数字、字母、符号");
				return;
			}
			if(pwd1.contains(" ")){
				DataUtil.getToast("密码不能有空格");
				return;
			}
			if(!pwd1.equals(pwd2)){
//				fogetpassworld_newpaswworld.setText("");
//				fogetpassworld_renewpaswworld.setText("");
				fogetpassworld_newpaswworld.requestFocus();
				DataUtil.getToast("密码输入不一致");
				return;
			}
			
			JSONObject json = new JSONObject();
			try {
				//pwd1 = DataUtil.encodeMD5(pwd1);//暂时不需要加密
				
				json.put("loginMobile", phone);
				json.put("newPassword", pwd1);
				newPassword = pwd1;
				//json.put("userID", CCApplication.getInstance().getPresentUser().getUserId());
				
				System.out.println("sendNewPwd:"+json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			action = ACTION_CHANGE_PSW;
			queryPost(GETPSW_URL, json);
		}
	}
	

	private ImageView repassDelete;
	
	
	private void inputState() {
		fogetpassworld_newpaswworld.addTextChangedListener(new TextWatcher() {

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
				String fogetpassworld_newpaswworld_str=fogetpassworld_newpaswworld.getText().toString().trim();
				String fogetpassworld_renewpaswworld_str=fogetpassworld_renewpaswworld.getText().toString().trim();
				
				if(fogetpassworld_newpaswworld_str.length()>0&&fogetpassworld_renewpaswworld_str.length()>0){
					fogetpassworld_btn_sure.setClickable(true);
					fogetpassworld_btn_sure.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
				}else{
					fogetpassworld_btn_sure.setClickable(false);
					fogetpassworld_btn_sure.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
				}
			}
			
		});
		
		fogetpassworld_renewpaswworld.addTextChangedListener(new TextWatcher() {

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
				
				String fogetpassworld_newpaswworld_str=fogetpassworld_newpaswworld.getText().toString().trim();
				String fogetpassworld_renewpaswworld_str=fogetpassworld_renewpaswworld.getText().toString().trim();
				
				if(fogetpassworld_newpaswworld_str.length()>0&&fogetpassworld_renewpaswworld_str.length()>0){
					fogetpassworld_btn_sure.setClickable(true);
					fogetpassworld_btn_sure.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
				}else{
					fogetpassworld_btn_sure.setClickable(false);
					fogetpassworld_btn_sure.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
				}
				
			}
			
		});
	}
}
