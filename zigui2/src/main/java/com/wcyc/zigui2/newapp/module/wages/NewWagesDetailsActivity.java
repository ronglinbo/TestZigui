package com.wcyc.zigui2.newapp.module.wages;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.ModelRemindList.ModelRemind;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.MyListView;

/**
 * 工资详情
 * 
 * @author 郑国栋 2016-6-27
 * @version 2.1
 */
public class NewWagesDetailsActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout title_back;
	private TextView new_content;
	private TextView new_wages_timeandname;
	private TextView new_wages_publish_nameandtime;
	private ArrayList<NewWagesDetailsBean> newWagesDetailsBeanList;// 工资详情list
	private MyListView new_wages_details_lv;
	private NewWagesDetailsAdapter newWagesDetailsAdapter;
	private String userid;
	private String userType;
	private String wageId;
	private Boolean isRead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_wages_details);

		initView();
		initDatas();
		initEvents();

	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键

		new_wages_timeandname = (TextView) findViewById(R.id.new_wages_timeandname);
		new_wages_publish_nameandtime = (TextView) findViewById(R.id.new_wages_publish_nameandtime);

		new_wages_details_lv = (MyListView) findViewById(R.id.new_wages_details_lv);

	}

	// 初始化数据
	private void initDatas() {
		new_content.setText("工资详情");
		newWagesDetailsBeanList = new ArrayList<NewWagesDetailsBean>();

		String position = getIntent().getStringExtra("position");
		wageId = getIntent().getStringExtra("wageId");
		isRead = getIntent().getBooleanExtra("isRead", true);
		userid = CCApplication.getInstance().getPresentUser().getUserId();
		userType = CCApplication.getInstance().getPresentUser().getUserType();
		String newWagesTimeAndNameStr = getIntent().getStringExtra(
				"newWagesTimeAndNameStr");
		String newWagesPublishNameAndTimeStr = getIntent().getStringExtra(
				"newWagesPublishNameAndTimeStr");
		newWagesPublishNameAndTimeStr=newWagesPublishNameAndTimeStr.substring(0,newWagesPublishNameAndTimeStr.lastIndexOf(":"));

		new_wages_timeandname.setText(newWagesTimeAndNameStr);
		new_wages_publish_nameandtime.setText(newWagesPublishNameAndTimeStr);

		JSONObject json = new JSONObject();
		try {
			json.put("wageId", wageId);
			json.put("teacherId", userid);
			json.put("curPage", "1");
			json.put("pageSize", "10");

			if (!DataUtil.isNetworkAvailable(NewWagesDetailsActivity.this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}

			if (!isLoading()) {
				System.out.println("工资详情入参=====" + json);
				queryPost(Constants.GET_WAGE_RECORD_DETAIL, json);

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
			NewWagesDetailsActivity.this.finish();
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
				String wageRecordsDetailsStr = json
						.getString("wageRecordsDetails");
				if (!DataUtil.isNullorEmpty(wageRecordsDetailsStr)) {
					JSONArray json2 = new JSONArray(wageRecordsDetailsStr);
					for (int i = 0; i < json2.length(); i++) {
						NewWagesDetailsBean newWagesDetailsBean = JsonUtils
								.fromJson(json2.get(i).toString(),
										NewWagesDetailsBean.class);
						newWagesDetailsBeanList.add(newWagesDetailsBean);
					}
				}
				// 设置adapter
				newWagesDetailsAdapter = new NewWagesDetailsAdapter(this,
						newWagesDetailsBeanList);
				new_wages_details_lv.setAdapter(newWagesDetailsAdapter);
				
				if(!isRead){
					List<ModelRemind> remind = CCApplication.getInstance()
							.getModelRemindList().getMessageList();
					String newWagesNumb = "";
					if (remind != null) {
						for (int i = 0; i < remind.size(); i++) {
							String remindType = remind.get(i).getType();
							if ("16".equals(remindType)) {
								newWagesNumb = remind.get(i).getCount();
							}
						}
					}
					System.out.println("==newWagesNumb==" + newWagesNumb);
					if (!DataUtil.isNullorEmpty(newWagesNumb)) {
						JSONObject jsonC = new JSONObject();
						jsonC.put("userId", userid);
						jsonC.put("userType", userType);
						jsonC.put("dataId", wageId);
						if ("3".equals(userType)) {
							String studentId = CCApplication.getInstance()
									.getPresentUser().getChildId();
							jsonC.put("studentId", studentId);
						}
						
						jsonC.put("modelType", "16");
						
						String urlC = new StringBuilder(Constants.SERVER_URL)
						.append(Constants.DEL_MODEL_REMIND_ZGD).toString();
						String resultC = HttpHelper.httpPostJson(this, urlC, jsonC);
						System.out.println("==resultC==="+resultC);
						// json对象 里面有属性ServerResult 请求结果
						NewBaseBean bb = JsonUtils.fromJson(resultC,
								NewBaseBean.class);
						if (bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
							System.out.println("===工资模块最新动态-1已删除===");
							Intent broadcast = new Intent(
									NewWagesActivity.INTENT_REFESH_WAGES_DATA);
							sendBroadcast(broadcast);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
