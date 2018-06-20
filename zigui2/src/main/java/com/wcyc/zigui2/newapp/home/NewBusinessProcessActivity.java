package com.wcyc.zigui2.newapp.home;

import java.net.HttpURLConnection;
import java.util.List;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.GradeleaderBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.HttpHelper;

/**
 * 业务办理 
 * 
 * @author 郑国栋
 * 2016-6-22
 * @version 2.1
 */
public class NewBusinessProcessActivity extends BaseActivity implements OnClickListener{
	
	private LinearLayout title_back;
	private TextView new_content;
	private RelativeLayout my_leave_rl;
	private RelativeLayout my_repair_rl;
	private RelativeLayout my_document_print_rl;
	private RelativeLayout my_school_news_rl;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//		getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
//		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_SHOW_FULLSCREEN);
		setContentView(R.layout.new_bisiness_process);
		
		initView();
		initDatas();
		initEvents();
		
		
//		System.out.println("======="+hasSoftKeys());
//		List<GradeleaderBean> aa=CCApplication.getInstance().getMemberDetail().getGradeInfoList();
//		System.out.println("==年级组长===="+aa);
		
		
	}
	
	//实例化组件
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);//标题
		title_back = (LinearLayout) findViewById(R.id.title_back);//返回键
		
		my_leave_rl = (RelativeLayout) findViewById(R.id.my_leave_rl);
		my_repair_rl = (RelativeLayout) findViewById(R.id.my_repair_rl);
		my_document_print_rl = (RelativeLayout) findViewById(R.id.my_document_print_rl);
		my_school_news_rl = (RelativeLayout) findViewById(R.id.my_school_news_rl);
		
//		my_leave_rl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}
	
	//初始化数据
	private void initDatas() {
		new_content.setText("我的所有模块");//业务办理	
		
		
//		System.out.println("===1====");
//		try {
//			JSONObject json = new JSONObject();
//			json.put("schoolId", schoolId);
//			String url = new StringBuilder(Constants.SERVER_URL).append(
//					"/getSchoolClassesList").toString();// getSchoolClassesList
//			String result = HttpHelper.httpPostJson(this, url, json);
//			System.out.println("===result===="+result);
//		} catch (Exception e) {
//		}
		
//		HttpURLConnection.
		
	}
	
	//设置点击事件监听器
	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);
		
		my_leave_rl.setOnClickListener(this);
		my_repair_rl.setOnClickListener(this);
		my_document_print_rl.setOnClickListener(this);
		my_school_news_rl.setOnClickListener(this);
	}
//	newActivity(NewZhanshiActivity.class, null);//展示
//	newActivity(NewConsumeActivity.class, null);//消费
//	newActivity(NewWagesActivity.class, null);//工资条
//	newActivity(NewDutyInquiryActivity.class, null);//值班查询
//	newActivity(NewMyDutyActivity.class, null);//我的值班
	@Override
	public void onClick(View v) {
//		switch (v.getId()){
//		case R.id.title_back:
////			NewZhanshiActivity.this.finish();
//			break;
//		case R.id.my_leave_rl:
////			Bundle bundle = new Bundle();
////			bundle.putString("userId", userName);
////			newActivity(MySetActivity.class, bundle);
//			newActivity(NewConsumeActivity.class, null);//消费
//			break;
//		case R.id.my_repair_rl:
////			newActivity(MySetActivity.class, null);
//			newActivity(NewWagesActivity.class, null);//工资条
//			break;
//		case R.id.my_document_print_rl:
////			newActivity(MySetActivity.class, null);
//			newActivity(NewDutyInquiryActivity.class, null);//值班查询
//			break;
//		case R.id.my_school_news_rl:
////			newActivity(MySetActivity.class, null);
//			newActivity(NewSchoolNewsActivity.class, null);//校园新闻
//			break;
//		default:
//			break;
//		
//		}
	}

	@Override
	protected void getMessage(String data) {		
	}
	
	
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressWarnings("unused")
	private boolean hasSoftKeys(){
		WindowManager windowManager=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display d = windowManager.getDefaultDisplay();

		DisplayMetrics realDisplayMetrics = new DisplayMetrics();
		d.getRealMetrics(realDisplayMetrics);


		int realHeight = realDisplayMetrics.heightPixels;
		int realWidth = realDisplayMetrics.widthPixels;
		System.out.println(realHeight+"===="+realWidth);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		d.getMetrics(displayMetrics);


		int displayHeight = displayMetrics.heightPixels;
		int displayWidth = displayMetrics.widthPixels;
		System.out.println(displayHeight+"===="+displayWidth);
		
//		realDisplayMetrics.;

		return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
		}
}
