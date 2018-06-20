package com.wcyc.zigui2.newapp.module.duty;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

/**
 * 值班日志
 * 
 * @author 郑国栋 2016-6-30
 * @version 2.0
 */
public class NewDutyDiaryActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout title_back;
	private TextView new_content;
	private TextView duty_man_name;
	private TextView duty_man_phone;
	private TextView morning_study_sdudent_tv;
	private TextView morning_study_teacher_tv;
	private TextView inclass_study_sdudent_tv;
	private TextView inclass_study_teacher_tv;
	private TextView evening_study_sdudent_tv;
	private TextView evening_study_teacher_tv;
	private TextView school_event_record_tv;
	private String inputDutyID;
	private String teaUserName;
	private String teaMobile;
	private String userid;
	
	private LinearLayout morning_ll;
	private LinearLayout morning_stu_ll;
	private LinearLayout morning_tea_ll;
	private LinearLayout inclass_ll;
	private LinearLayout inclass_stu_ll;
	private LinearLayout inclass_tea_ll;
	private LinearLayout evening_ll;
	private LinearLayout evening_stu_ll;
	private LinearLayout evening_tea_ll;
	private LinearLayout school_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_duty_diary);

		initView();
		initDatas();
		initEvents();

	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键

		duty_man_name = (TextView) findViewById(R.id.duty_man_name);
		duty_man_phone = (TextView) findViewById(R.id.duty_man_phone);

		morning_study_sdudent_tv = (TextView) findViewById(R.id.morning_study_sdudent_tv);
		morning_study_teacher_tv = (TextView) findViewById(R.id.morning_study_teacher_tv);
		inclass_study_sdudent_tv = (TextView) findViewById(R.id.inclass_study_sdudent_tv);
		inclass_study_teacher_tv = (TextView) findViewById(R.id.inclass_study_teacher_tv);
		evening_study_sdudent_tv = (TextView) findViewById(R.id.evening_study_sdudent_tv);
		evening_study_teacher_tv = (TextView) findViewById(R.id.evening_study_teacher_tv);
		school_event_record_tv = (TextView) findViewById(R.id.school_event_record_tv);
		
		morning_ll = (LinearLayout) findViewById(R.id.morning_ll);//
		morning_stu_ll = (LinearLayout) findViewById(R.id.morning_stu_ll);//
		morning_tea_ll = (LinearLayout) findViewById(R.id.morning_tea_ll);//
		
		inclass_ll = (LinearLayout) findViewById(R.id.inclass_ll);//
		inclass_stu_ll = (LinearLayout) findViewById(R.id.inclass_stu_ll);//
		inclass_tea_ll = (LinearLayout) findViewById(R.id.inclass_tea_ll);//
		
		evening_ll = (LinearLayout) findViewById(R.id.evening_ll);//
		evening_stu_ll = (LinearLayout) findViewById(R.id.evening_stu_ll);//
		evening_tea_ll = (LinearLayout) findViewById(R.id.evening_tea_ll);//
		
		school_ll = (LinearLayout) findViewById(R.id.school_ll);//
	}

	// 初始化数据
	private void initDatas() {
		new_content.setText("值班日志");

		String position = getIntent().getStringExtra("position");
		inputDutyID = getIntent().getStringExtra("inputDutyID");
		teaUserName = getIntent().getStringExtra("TeaUserName");
		teaMobile = getIntent().getStringExtra("TeaMobile");
		
		
		duty_man_name.setText(teaUserName);
		duty_man_phone.setText(teaMobile);

		userid = CCApplication.getInstance().getPresentUser().getUserId();

		JSONObject json = new JSONObject();
		try {
			json.put("schoolId", schoolId);
			json.put("inputDutyID", inputDutyID);

			if (!DataUtil.isNetworkAvailable(this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}

			if (!isLoading()) {
				System.out.println("值班日志入参=====" + json);
				queryPost(Constants.GET_DUTY_LOG, json);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	// 设置点击效果监听器
	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			NewDutyDiaryActivity.this.finish();
			break;
		default:
			break;

		}
	}

	@Override
	protected void getMessage(String data) {
		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if (ret.getServerResult().getResultCode() != 200) {// 请求失败
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		} else {
			try {
				JSONObject json = new JSONObject(data);
				String dutyLog = json.getString("dutyLog");
				NewDutyDiaryBean newDutyDiaryBean = JsonUtils.fromJson(dutyLog,
						NewDutyDiaryBean.class);
				// 早自习
				if (DataUtil
						.isNullorEmpty(newDutyDiaryBean.getEarlyStudyStu())&&DataUtil
						.isNullorEmpty(newDutyDiaryBean.getEarlyStudyTea())) {
					morning_ll.setVisibility(View.GONE);
				}
				
				// 早自习学生
				if (!DataUtil
						.isNullorEmpty(newDutyDiaryBean.getEarlyStudyStu())) {
					morning_study_sdudent_tv.setText(newDutyDiaryBean
							.getEarlyStudyStu());
				} else {
					morning_stu_ll.setVisibility(View.GONE);
				}
				// 早自习老师
				if (!DataUtil
						.isNullorEmpty(newDutyDiaryBean.getEarlyStudyTea())) {
					morning_study_teacher_tv.setText(newDutyDiaryBean
							.getEarlyStudyTea());
				} else {
					morning_tea_ll.setVisibility(View.GONE);
				}
				
				// 上课期间
				if (DataUtil.isNullorEmpty(newDutyDiaryBean.getClassStudent())
						&&DataUtil.isNullorEmpty(newDutyDiaryBean.getClassTeacher())) {
					inclass_ll.setVisibility(View.GONE);
				}
				// 上课期间学生
				if (!DataUtil.isNullorEmpty(newDutyDiaryBean.getClassStudent())) {
					inclass_study_sdudent_tv.setText(newDutyDiaryBean
							.getClassStudent());
				} else {
					inclass_stu_ll.setVisibility(View.GONE);
				}
				// 上课期间老师
				if (!DataUtil.isNullorEmpty(newDutyDiaryBean.getClassTeacher())) {
					inclass_study_teacher_tv.setText(newDutyDiaryBean
							.getClassTeacher());
				} else {
					inclass_tea_ll.setVisibility(View.GONE);
				}
				
				// 晚自习
				if (DataUtil
						.isNullorEmpty(newDutyDiaryBean.getNightStudyStu())&&DataUtil
						.isNullorEmpty(newDutyDiaryBean.getNightStudyTea())) {
					evening_ll.setVisibility(View.GONE);
				}
				// 晚自习学生
				if (!DataUtil
						.isNullorEmpty(newDutyDiaryBean.getNightStudyStu())) {
					evening_study_sdudent_tv.setText(newDutyDiaryBean
							.getNightStudyStu());
				} else {
					evening_stu_ll.setVisibility(View.GONE);
				}
				// 晚自习老师
				if (!DataUtil
						.isNullorEmpty(newDutyDiaryBean.getNightStudyTea())) {
					evening_study_teacher_tv.setText(newDutyDiaryBean
							.getNightStudyTea());
				} else {
					evening_tea_ll.setVisibility(View.GONE);
				}
				// 学校大事记载
				if (!DataUtil.isNullorEmpty(newDutyDiaryBean.getSchoolStory())) {
					school_event_record_tv.setText(newDutyDiaryBean
							.getSchoolStory());
				} else {
					school_ll.setVisibility(View.GONE);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
