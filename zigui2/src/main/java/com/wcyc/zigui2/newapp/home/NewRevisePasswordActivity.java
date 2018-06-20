package com.wcyc.zigui2.newapp.home;

import org.json.JSONObject;

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

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.LoginActivity;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;
/**
 * 修改密码页面类.
 * 
 * @author gdzheng 
 * @time 2016-04-11
 * @version 2.0
 */
public class NewRevisePasswordActivity extends BaseActivity implements OnClickListener{
	
	private TextView new_content;
	private LinearLayout title_back;
	private EditText revise_password_old;
	private EditText revise_password_new_a;
	private EditText revise_password_new_b;
	private Button revise_password_btn;
	private String password;
	private ImageView revise_password_new_b_delete_icon;
	private ImageView revise_password_new_a_delete_icon;
	private ImageView revise_password_old_delete_icon;
	private String revise_password_new_a_str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_revise_password);		
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
		title_back = (LinearLayout)findViewById(R.id.title_back);//返回键
		revise_password_old = (EditText) findViewById(R.id.revise_password_old);//旧密码
		revise_password_old_delete_icon = (ImageView) findViewById(R.id.revise_password_old_delete_icon);
		TextFilter textFilter1 = new TextFilter(revise_password_old);
		revise_password_old.addTextChangedListener(textFilter1);// 设置不能输入空格
		textFilter1.setEditeTextClearListener(revise_password_old,
				revise_password_old_delete_icon);
		revise_password_new_a = (EditText) findViewById(R.id.revise_password_new_a);//新密码第一次
		revise_password_new_a_delete_icon = (ImageView) findViewById(R.id.revise_password_new_a_delete_icon);
		TextFilter textFilter2 = new TextFilter(revise_password_new_a);
		revise_password_new_a.addTextChangedListener(textFilter2);// 设置不能输入空格
		textFilter2.setEditeTextClearListener(revise_password_new_a,
				revise_password_new_a_delete_icon);
		revise_password_new_b = (EditText) findViewById(R.id.revise_password_new_b);//新密码第二次
		revise_password_new_b_delete_icon = (ImageView) findViewById(R.id.revise_password_new_b_delete_icon);
		TextFilter textFilter3 = new TextFilter(revise_password_new_b);
		revise_password_new_b.addTextChangedListener(textFilter3);// 设置不能输入空格
		textFilter3.setEditeTextClearListener(revise_password_new_b,
				revise_password_new_b_delete_icon);
		revise_password_btn = (Button) findViewById(R.id.revise_password_btn);//修改密码确定按钮
		revise_password_btn.setOnClickListener(this);//修改密码的确定按钮设置监听效果
		revise_password_btn.setClickable(false);
		revise_password_btn.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
	}

	/**
	 * 初始化数据
	 */
	
	private void initDatas() {
		new_content.setText("修改密码");
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
			DataUtil.getToast("密码重置成功，请用新密码登录");
			CCApplication.getInstance().setPassword(revise_password_new_a_str);
			CCApplication.app.savePhonePwd(revise_password_new_a_str);
			CCApplication.app.logout();
			CCApplication.app.finishAllActivity();
			newActivity(LoginActivity.class,null);
		}else{
			//提交失败
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.title_back:
			NewRevisePasswordActivity.this.finish();
			break;
		case R.id.revise_password_btn:
			String revise_password_old_str=revise_password_old.getText().toString();
			revise_password_new_a_str = revise_password_new_a.getText().toString();
			String revise_password_new_b_str=revise_password_new_b.getText().toString();
				//判断旧手机号码输入框是否为空
				if(DataUtil.isNull(revise_password_old_str)){
					DataUtil.getToast("旧密码不能为空");
					revise_password_old.requestFocus();
					return;
				}
				//判断新密码输入框是否为空
				if(DataUtil.isNull(revise_password_new_a_str)){
					DataUtil.getToast("新密码不能为空");
					revise_password_new_a.requestFocus();
					return;
				}
				//检查新密码是否合格
				if(!DataUtil.isLength(revise_password_new_a_str, 6, 20)){
					DataUtil.getToast("新密码格式不对");
					revise_password_new_a.requestFocus();
					return;
				}
				//判断重复新密码输入框是否为空
				if(DataUtil.isNull(revise_password_new_b_str)){
					DataUtil.getToast("重复新密码不能为空");
					revise_password_new_b.requestFocus();
					return;
				}
				//判断重复新密码是否合格
				if(!DataUtil.isLength(revise_password_new_b_str, 6, 20)){
					DataUtil.getToast("重复新密码格式不对");
					revise_password_new_b.requestFocus();
					return;
				}
				//原密码
				password = CCApplication.getInstance().getPhonePwd();				
				if(revise_password_old_str.equals(password)){
					if(revise_password_new_a_str.equals(revise_password_new_b_str)){
						String userid=CCApplication.getInstance().getMemberInfo().getAccId();
						JSONObject json=new JSONObject();
						try {
							json.put("userId", userid);
							json.put("password",password);
							json.put("newPassword",revise_password_new_a_str);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(!isLoading()){
							queryPost(Constants.MODIFY_USER_INFO_ZGD, json);
						}
					}else{
						DataUtil.getToast("两次输入的密码不一致");
						revise_password_new_a.requestFocus();
					}
				}else{
					DataUtil.getToast("输入旧密码错误！");
					revise_password_old.requestFocus();
					return;
				}
			break;
		}
	}

	private void inputState() {
		revise_password_old.addTextChangedListener(new TextWatcher() {

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
				String revise_password_old_str=revise_password_old.getText().toString();
				String revise_password_new_a_str = revise_password_new_a.getText().toString();
				String revise_password_new_b_str=revise_password_new_b.getText().toString();
				if (s.length() > 0&&revise_password_new_a_str.length()>0&&revise_password_new_b_str.length()>0) {
					revise_password_btn.setClickable(true);
					revise_password_btn
							.setBackgroundResource(R.drawable.btn_blue_selector);
				} else {
					revise_password_btn.setClickable(false);
					revise_password_btn
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
				}
				if(s.length()>19){
					DataUtil.getToast("密码最多20位字符");
				}
			}
		});

		revise_password_new_a.addTextChangedListener(new TextWatcher() {

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
				String revise_password_old_str=revise_password_old.getText().toString();
				String revise_password_new_a_str = revise_password_new_a.getText().toString();
				String revise_password_new_b_str=revise_password_new_b.getText().toString();
				if (s.length() > 0&&revise_password_old_str.length()>0&&revise_password_new_b_str.length()>0) {
					revise_password_btn.setClickable(true);
					revise_password_btn
							.setBackgroundResource(R.drawable.btn_blue_selector);
				} else {
					revise_password_btn.setClickable(false);
					revise_password_btn
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
				}
				if(s.length()>19){
					DataUtil.getToast("密码最多20位字符");
				}
				if(revise_password_new_a_str.length()==revise_password_new_b_str.length()){
					if(!revise_password_new_a_str.equals(revise_password_new_b_str)){
						DataUtil.getToast("两次输入的新密码不一致");
					}
				}
			}
		});

		revise_password_new_b.addTextChangedListener(new TextWatcher() {

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
				String revise_password_old_str=revise_password_old.getText().toString();
				String revise_password_new_a_str = revise_password_new_a.getText().toString();
				String revise_password_new_b_str=revise_password_new_b.getText().toString();
				if (s.length() > 0&&revise_password_old_str.length()>0&&revise_password_new_a_str.length()>0) {
					revise_password_btn.setClickable(true);
					revise_password_btn
							.setBackgroundResource(R.drawable.btn_blue_selector);
				} else {
					revise_password_btn.setClickable(false);
					revise_password_btn
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
				}
				if(s.length()>19){
					DataUtil.getToast("密码最多20位字符");
				}
				if(revise_password_new_a_str.length()==revise_password_new_b_str.length()){
					if(!revise_password_new_a_str.equals(revise_password_new_b_str)){
						DataUtil.getToast("两次输入的新密码不一致");
					}
				}
			}
		});
	}
}
