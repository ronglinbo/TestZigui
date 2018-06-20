package com.wcyc.zigui2.newapp.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewTeacherInfoBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;

/**
 * 修改手机号
 * 
 * @author gdzheng
 * @time 2016-04-11
 * @version 2.0
 */
public class NewRevisePhoneActivity extends BaseActivity implements
		OnClickListener {

	private TextView new_content;
	private LinearLayout title_back;
	private EditText revise_new_phone;
	private EditText revise_phone_verify;
	private EditText revise_phone_needpassword;
	private Button resive_phone_btn_sendverify;
	private Button update_phone_btn;
	private String user_phone;
	private String revise_new_phone_str;
	private String revise_phone_verify_str;
	private String revise_phone_needpassword_str;
	private String userId;
	private String passwrod;
	private ImageView revise_new_phone_delete_icon;
	private ImageView revise_phone_verify_delete_icon;
	private ImageView revise_phone_needpassword_delete_icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 请求
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_revise_phone);
		initView();
		initDatas();
		initEvents();

		inputState();
	}

	/**
	 * 初始化控件
	 */

	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title_back = (LinearLayout) findViewById(R.id.title_back);// 返回图标

		revise_new_phone = (EditText) findViewById(R.id.revise_new_phone);// 新手机
		revise_new_phone_delete_icon = (ImageView) findViewById(R.id.revise_new_phone_delete_icon);
		TextFilter textFilter1 = new TextFilter(revise_new_phone);
		revise_new_phone.addTextChangedListener(textFilter1);// 设置不能输入空格
		textFilter1.setEditeTextClearListener(revise_new_phone,
				revise_new_phone_delete_icon);

		revise_phone_verify = (EditText) findViewById(R.id.revise_phone_verify);// 验证码
		revise_phone_verify_delete_icon = (ImageView) findViewById(R.id.revise_phone_verify_delete_icon);
		TextFilter textFilter2 = new TextFilter(revise_phone_verify);
		revise_phone_verify.addTextChangedListener(textFilter2);// 设置不能输入空格
		textFilter2.setEditeTextClearListener(revise_phone_verify,
				revise_phone_verify_delete_icon);

		revise_phone_needpassword = (EditText) findViewById(R.id.revise_phone_needpassword);// 密码
		revise_phone_needpassword_delete_icon = (ImageView) findViewById(R.id.revise_phone_needpassword_delete_icon);
		TextFilter textFilter3 = new TextFilter(revise_phone_needpassword);
		revise_phone_needpassword.addTextChangedListener(textFilter3);// 设置不能输入空格
		textFilter3.setEditeTextClearListener(revise_phone_needpassword,
				revise_phone_needpassword_delete_icon);

		resive_phone_btn_sendverify = (Button) findViewById(R.id.resive_phone_btn_sendverify);// 获取验证码
		resive_phone_btn_sendverify.setOnClickListener(this);// 监听
		resive_phone_btn_sendverify.setClickable(false);
		resive_phone_btn_sendverify
				.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);

		update_phone_btn = (Button) findViewById(R.id.update_phone_btn);// 确定按钮
		update_phone_btn.setOnClickListener(this);// 监听
		update_phone_btn.setClickable(false);
		update_phone_btn
				.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
		// update_phone_btn.setBackgroundColor();
	}

	/**
	 * 初始化数据
	 */

	private void initDatas() {
		new_content.setText("修改手机号码");

		try {
			// 获得用户ID 、密码、手机
			userId = CCApplication.getInstance().getPresentUser().getUserId();
			passwrod = CCApplication.getInstance().getPhonePwd();

			user_phone = CCApplication.getInstance().getPhoneNum();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * 效果控制
	 */

	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);

	}

	private void inputState() {
		revise_new_phone.addTextChangedListener(new TextWatcher() {

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
				revise_new_phone_str = revise_new_phone.getText().toString();
				revise_phone_verify_str = revise_phone_verify.getText()
						.toString();
				revise_phone_needpassword_str = revise_phone_needpassword
						.getText().toString();
				if (s.length() > 0) {
					resive_phone_btn_sendverify.setClickable(true);
					resive_phone_btn_sendverify
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);

				} else {
					resive_phone_btn_sendverify.setClickable(false);
					resive_phone_btn_sendverify
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
					update_phone_btn.setClickable(false);
					update_phone_btn
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
				}

				if (s.length() > 0 && revise_phone_verify_str.length() > 0
						&& revise_phone_needpassword_str.length() > 0) {
					update_phone_btn.setClickable(true);
					update_phone_btn
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
				}

			}

		});

		// 验证码编辑框
		revise_phone_verify.addTextChangedListener(new TextWatcher() {

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
				revise_new_phone_str = revise_new_phone.getText().toString();
				revise_phone_verify_str = revise_phone_verify.getText()
						.toString();
				revise_phone_needpassword_str = revise_phone_needpassword
						.getText().toString();

				if (s.length() > 0 && revise_new_phone_str.length() > 0
						&& revise_phone_needpassword_str.length() > 0) {
					update_phone_btn.setClickable(true);
					update_phone_btn
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
				} else {

					update_phone_btn.setClickable(false);
					update_phone_btn
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
				}

			}

		});

		// 密码编辑框
		revise_phone_needpassword.addTextChangedListener(new TextWatcher() {

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
				revise_new_phone_str = revise_new_phone.getText().toString();
				revise_phone_verify_str = revise_phone_verify.getText()
						.toString();
				revise_phone_needpassword_str = revise_phone_needpassword
						.getText().toString();

				if (s.length() > 0 && revise_new_phone_str.length() > 0
						&& revise_phone_verify_str.length() > 0) {
					update_phone_btn.setClickable(true);
					update_phone_btn
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
				} else {

					update_phone_btn.setClickable(false);
					update_phone_btn
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
				}

			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 如果点击返回图标 关闭本界面 回上一个界面
		case R.id.title_back:
			NewRevisePhoneActivity.this.finish();
			break;
		case R.id.resive_phone_btn_sendverify:// 发送验证码
			sendVerify();
			break;
		case R.id.update_phone_btn:// 确定
			revise_new_phone_str = revise_new_phone.getText().toString();
			revise_phone_verify_str = revise_phone_verify.getText().toString();
			revise_phone_needpassword_str = revise_phone_needpassword.getText()
					.toString();

			if (!isLoading()) {

				// 判断输入手机是否为空
				if (DataUtil.isNull(revise_new_phone_str)) {
					DataUtil.getToast("请输入手机号码");
					revise_new_phone.requestFocus();
					return;
				}
				// 判断输入手机是否正确
				if (!DataUtil.checkPhone(revise_new_phone_str)) {
					DataUtil.getToast("请输入正确的手机号码");
					return;
				}
				// 判断输入验证码是否为空
				if (DataUtil.isNull(revise_phone_verify_str)) {
					DataUtil.getToast("请输入验证码");
					revise_phone_verify.requestFocus();
					return;
				}

				// 判断输入密码是否为空
				if (DataUtil.isNull(revise_phone_needpassword_str)) {
					DataUtil.getToast("请输入密码");
					revise_phone_needpassword.requestFocus();
					return;
				}
				// 如果密码匹配成功
				if (revise_phone_needpassword_str.equals(passwrod)) {

					// 查看验证码是否匹配成功
					validCode();
				} else {
					DataUtil.getToast("输入密码错误");
					revise_phone_needpassword.requestFocus();
					return;
				}
			}

			break;
		}

	}

	// 请求结果
	@Override
	protected void getMessage(String data) {
		// json对象 里面有属性ServerResult 请求结果
		NewBaseBean bb = JsonUtils.fromJson(data, NewBaseBean.class);

		if (bb.getServerResult().getResultCode() == 103001) {
			DataUtil.getToast("输入验证码错误");
			revise_phone_verify.requestFocus();
		}

		switch (action) {
		case 1:
			// 结果的结果码如果等于200
			if (bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
				DataUtil.getToast("验证码已发送至"+revise_new_phone_str);
				 runtime();
			} else {
				if (bb.getServerResult().getResultCode() == 102003) {
					DataUtil.getToast("您输入的手机号不存在！");
					return;
				}
				revise_new_phone.requestFocus();
				DataUtil.getToast(bb.getServerResult().getResultMessage());
			}
			break;
		case 2:

			JSONObject json;
			try {

				json = new JSONObject(data);
				String result = (String) json.get("result");
				if ("1".equals(result)) {// 修改手机号码的验证码匹配成功

					// 要修改的老师的个人属性
					NewTeacherInfoBean revise_user_info = new NewTeacherInfoBean();
					revise_user_info.setUserId(userId);
					revise_user_info.setPassword(passwrod);
					revise_user_info.setMobileNum(revise_new_phone_str);

					Gson gson = new Gson();
					String string = gson.toJson(revise_user_info);
					// JSON对象
					JSONObject json2 = null;
					json2 = new JSONObject(string);
					// 请求地址
					String url = new StringBuilder(Constants.SERVER_URL)
							.append(Constants.MODIFY_USER_INFO_ZGD).toString();
					// 请求结果 是json格式的字符串
					String result2 = HttpHelper.httpPostJson(this, url, json2);
					NewBaseBean bb2 = JsonUtils.fromJson(result2,
							NewBaseBean.class);
					if (bb2.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
						DataUtil.getToast("修改手机成功");
						// 新手机号码 revise_new_phone_str
						Bundle bundle = new Bundle();
						// 封装数据
						bundle.putString("revise_new_phone_str",
								revise_new_phone_str);
						// 封装bundle
						Intent intent = new Intent();
						intent.setClass(NewRevisePhoneActivity.this,
								NewTeacherAccountActivity.class);
						intent.putExtras(bundle);

						// 返回
						NewRevisePhoneActivity.this
								.setResult(RESULT_OK, intent);// 返回上一个界面，并带参数

						NewRevisePhoneActivity.this.finish();

					} else {
						// 如果不成功 则显示请求结果中错误信息
						DataUtil.getToast("服务器连接失败");

						return;
					}

				} else if ("0".equals(result)) {
					DataUtil.getToast("输入验证码错误!请重新输入");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		default:
			break;
		}

	}

	/**
	 * 获取验证码
	 */
	private void sendVerify() {
		if (!isLoading()) {
			revise_new_phone_str = revise_new_phone.getText().toString();
			if (DataUtil.isNull(revise_new_phone_str)) {
				DataUtil.getToast("请输入手机号码");
				revise_new_phone.requestFocus();
				return;
			}
			if (!DataUtil.checkPhone(revise_new_phone_str)) {
				DataUtil.getToast("请输入正确的手机号码");
				return;
			}
			JSONObject json = new JSONObject();
			try {
				json.put("userPhone", revise_new_phone_str);
				json.put("type", "2");
				json.put("clientSys","1");//1:子贵校园，2：子贵学苑
			} catch (JSONException e) {
				e.printStackTrace();
			}
			action = 1;
			queryPost(Constants.GETCODE_URL, json);
		}
	}

	// 校验验证码
	private void validCode() {
		JSONObject json = new JSONObject();
		try {
			json.put("mobile", revise_new_phone_str);
			json.put("buzzType", "2");// 修改手机号码
			json.put("checkCode", revise_phone_verify_str);
			action = 2;
			queryPost(Constants.VALID_CODE, json);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 验证码倒计时功能
	private int time;
	Runnable runnable_time = new Runnable() {
		@Override
		public void run() {
			setTimeView();
			time -= 1;
			if (time > 0) {
				handler.postDelayed(this, 1000);
			} else {
				resive_phone_btn_sendverify.setText("重新获取");
				resive_phone_btn_sendverify.setClickable(true);
				resive_phone_btn_sendverify
						.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
			}
		}
	};

	private void runtime() {
		time = 60;
		resive_phone_btn_sendverify.setClickable(false);
		resive_phone_btn_sendverify
				.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
		handler.removeCallbacks(runnable_time);
		handler.postAtTime(runnable_time, 1000);
	}
	
	private ImageView repassDelete;
    private void setTimeView(){
    	resive_phone_btn_sendverify.setText("重新获取"+time + "s");
    }

}
