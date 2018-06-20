package com.wcyc.zigui2.newapp.module.leave;

import java.text.SimpleDateFormat;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.DateTimePickDialogUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.SlipButton;
import com.wcyc.zigui2.utils.SlipButton.OnChangedListener;
import com.wcyc.zigui2.widget.CustomDialog;

/**
 * 家长端 请假申请
 * 
 * @author 郑国栋 2016-7-15
 * @version 2.0
 */
public class NewMyLeaveAskActivity extends BaseActivity implements
		OnClickListener {

	private TextView new_content, title2_ok, title2_off;
	private TextView leaveStartTime_tv, leaveEndTime_tv, myleave_ask_name_tv;
	private EditText leaveDays_et, leaveHours_et,myleave_reason_et;
	private TextView shijia,  bingjia,sangjia, tanqinjia;
	private String initStartDateTime;
	private String initEndDateTime;
	private SlipButton leave_choose_btn;
	private String isSms = "0";//0不通知，1通知
	private String userid;
	private String childId;
	private int leaveType=0;
	private String leaveStartTimeStr;
	private String leaveEndTimeStr;
	private String leaveDaysStr;
	private String leaveHoursStr;
	private String myleave_reasonStr;
	private CustomDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_my_leave_ask);

		initView();
		initDatas();
		initEvents();

		inputState();
	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title2_off = (TextView) findViewById(R.id.title2_off);
		title2_ok = (TextView) findViewById(R.id.title2_ok);

		myleave_ask_name_tv = (TextView) findViewById(R.id.myleave_ask_name_tv);
		leaveStartTime_tv = (TextView) findViewById(R.id.leaveStartTime_tv);
		leaveEndTime_tv = (TextView) findViewById(R.id.leaveEndTime_tv);
		leaveDays_et = (EditText) findViewById(R.id.leaveDays_et);
		leaveHours_et = (EditText) findViewById(R.id.leaveHours_et);
		
		myleave_reason_et = (EditText) findViewById(R.id.myleave_reason_et);

		shijia = (TextView) findViewById(R.id.shijia);//
		bingjia = (TextView) findViewById(R.id.bingjia);//
		sangjia = (TextView) findViewById(R.id.sangjia);//
		tanqinjia = (TextView) findViewById(R.id.tanqinjia);//


		leave_choose_btn = (SlipButton) findViewById(R.id.leave_choose_btn);// 短信开关
	}

	// 初始化数据
	@SuppressLint("SimpleDateFormat")
	private void initDatas() {
		new_content.setText("请假申请");//

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String timeStr = sdf.format(System.currentTimeMillis());
//		System.out.println("==timeStr=="+timeStr);

//		initStartDateTime = timeStr;
//		initEndDateTime = timeStr;
//		leaveStartTime_tv.setText(initStartDateTime);
//		leaveEndTime_tv.setText(initEndDateTime);
		leaveStartTime_tv.setText("");
		leaveEndTime_tv.setText("");

		userid = CCApplication.getInstance().getPresentUser().getUserId();
		childId=CCApplication.getInstance().getPresentUser().getChildId();
		String childName = CCApplication.getInstance().getPresentUser()
				.getChildName();
		myleave_ask_name_tv.setText(childName);

	}

	// 设置点击事件监听器
	private void initEvents() {
		title2_off.setOnClickListener(this);
		title2_ok.setOnClickListener(this);
		title2_ok.setClickable(false);
		title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));

		shijia.setOnClickListener(this);
		bingjia.setOnClickListener(this);
		sangjia.setOnClickListener(this);
		tanqinjia.setOnClickListener(this);
		

		leaveStartTime_tv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						NewMyLeaveAskActivity.this, "");
				dateTimePicKDialog.dateTimePicKDialog(leaveStartTime_tv);
				initStartDateTime = leaveStartTime_tv.getText().toString()
						.trim();
			}
		});

		leaveEndTime_tv.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				initStartDateTime = leaveStartTime_tv.getText().toString()
						.trim();
				
				if(DataUtil.isNullorEmpty(initStartDateTime)){
					DataUtil.getToast("请选择请假开始时间");
				}else{			
					DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
							NewMyLeaveAskActivity.this, initStartDateTime);
					dateTimePicKDialog.dateTimePicKDialog(leaveEndTime_tv);
					initEndDateTime = leaveEndTime_tv.getText().toString().trim();
				}
			}
		});

		leave_choose_btn.setCheck(false);// 设置开关为开启状态//0不通知，1通知
		leave_choose_btn.SetOnChangedListener(new OnChangedListener() {
			@Override
			public void OnChanged(boolean CheckState) {
				if (false == CheckState) {
					isSms = "0";
				} else if (true == CheckState) {
					isSms = "1";
				}
			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.title2_off:
			getAllTvOrEtVale();
			if(isHaveOneNotNull()){
				exitOrNot();
				return;
			}
			NewMyLeaveAskActivity.this.finish();//
			break;
		case R.id.title2_ok:// 确定键
			if("0".equals(leaveType)){
				DataUtil.getToast("请选择请假类型");
				return;
			}
			
			if (!DataUtil.isNetworkAvailable(NewMyLeaveAskActivity.this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}
			if (!isLoading()) {
				httpMyLeaveAsk();
			}
			break;
		case R.id.shijia://
			chooseLeaveType(shijia, true);
			leaveType=1;
			chooseLeaveType(bingjia, false);
			chooseLeaveType(sangjia, false);
			chooseLeaveType(tanqinjia, false);
			getAllTvOrEtVale();
			isOkAllowClick();

			break;
		case R.id.bingjia://
			chooseLeaveType(shijia, false);
			chooseLeaveType(bingjia, true);
			leaveType=2;
			chooseLeaveType(sangjia, false);
			chooseLeaveType(tanqinjia, false);
			getAllTvOrEtVale();
			isOkAllowClick();

			break;
		case R.id.sangjia://
			chooseLeaveType(shijia, false);
			chooseLeaveType(bingjia, false);
			chooseLeaveType(sangjia, true);
			leaveType=3;
			chooseLeaveType(tanqinjia, false);
			getAllTvOrEtVale();
			isOkAllowClick();

			break;
		case R.id.tanqinjia://
			chooseLeaveType(shijia, false);
			chooseLeaveType(bingjia, false);
			chooseLeaveType(sangjia, false);
			chooseLeaveType(tanqinjia, true);
			leaveType=4;
			getAllTvOrEtVale();
			isOkAllowClick();
			break;			
		default:
			break;
		}
	}

	public void chooseLeaveType(TextView view, Boolean isChoose) {
		if (isChoose) {
			view.setBackgroundResource(R.drawable.btn2_leixing_selected);
			view.setTextColor(getResources().getColor(R.color.font_white));
		} else {
			view.setBackgroundResource(R.drawable.btn2_leixing_normal);
			view.setTextColor(getResources().getColor(R.color.font_black));
		}
	}

	public void httpMyLeaveAsk() {
		
		try {
			JSONObject json2 = new JSONObject();
			json2.put("schoolId", schoolId);
//			json2.put("applyUserId", userid);//childId
			json2.put("applyUserId", childId);//childId
			json2.put("leaveType", leaveType+"");
			json2.put("leaveStartTime", leaveStartTime_tv.getText().toString().trim());
			json2.put("leaveEndTime", leaveEndTime_tv.getText().toString().trim());
			json2.put("leaveHours", leaveHours_et.getText().toString().trim());
			json2.put("leaveDays", leaveDays_et.getText().toString().trim());
			json2.put("reason", myleave_reason_et.getText().toString());
			json2.put("isSms", isSms);

			System.out.println("请假申请入参=====" + json2);
			queryPost(Constants.LEAVE_ADD_INFO, json2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void getMessage(String data) {
		System.out.println("1====="+data);
		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if (ret.getServerResult().getResultCode() != 200) {// 请求失败
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		} else {
			DataUtil.getToast("发送成功！");
			Intent broadcast = new Intent(NewMyLeaveActivity.INTENT_REFRESH_DATA_LEAVE);
			sendBroadcast(broadcast);
			NewMyLeaveAskActivity.this.finish();
		}
	}

	
	
	private void exitOrNot(){
		dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.customdialog, handler);
		dialog.setCanceledOnTouchOutside(false);
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
	 * 控制CustomDialog按钮事件.
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
				NewMyLeaveAskActivity.this.finish();//
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	
	
	//所有需要的  textview和editText获得值
	private void getAllTvOrEtVale(){
		leaveStartTimeStr = leaveStartTime_tv.getText().toString().trim();
		leaveEndTimeStr = leaveEndTime_tv.getText().toString().trim();
		leaveDaysStr = leaveDays_et.getText().toString().trim();
		leaveHoursStr = leaveHours_et.getText().toString().trim();
		myleave_reasonStr = myleave_reason_et.getText().toString();
	}
	
	//所有需要的  textview或editText 是否都有值
	private boolean isAllHaveLength(){
		return leaveStartTimeStr.length() > 0 && leaveEndTimeStr.length() > 0
				&& leaveDaysStr.length() > 0 && leaveHoursStr.length() > 0
				&& myleave_reasonStr.length() > 0 && leaveType > 0;
	}
	
	//所有需要的  textview或editText 是否有一个不为空
	private boolean isHaveOneNotNull(){
		return leaveDaysStr.length() > 0 || leaveHoursStr.length() > 0
				|| myleave_reasonStr.length() > 0;
	}
	
	//确定键是否可点击
	private void isOkAllowClick(){
		if (isAllHaveLength()) {
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
		leaveStartTime_tv.addTextChangedListener(new TextWatcher() {
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
				
				if(isAllHaveLength()){
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
				}else{
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
				}	
			}
		});
		
		leaveEndTime_tv.addTextChangedListener(new TextWatcher() {
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
				
				if(isAllHaveLength()){
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
				}else{
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
				}	
			}
		});
		
		leaveDays_et.addTextChangedListener(new TextWatcher() {
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
				
				if(isAllHaveLength()){
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
				}else{
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
				}
				
				
			}
		});
		
		leaveHours_et.addTextChangedListener(new TextWatcher() {
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
				if(isAllHaveLength()){
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
				}else{
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
				}	
			}
		});
		
		myleave_reason_et.addTextChangedListener(new TextWatcher() {
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
				
				if(isAllHaveLength()){
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
				}else{
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
				}
				
				if(s.length()>199){
					DataUtil.getToast("请假事由不能超过200个字");
				}
			}
		});
	}
}
