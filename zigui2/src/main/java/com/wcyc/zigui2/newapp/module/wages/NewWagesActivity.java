package com.wcyc.zigui2.newapp.module.wages;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.newapp.widget.RefreshListView1.OnRefreshListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

/**
 * 工资条
 * 
 * @author 郑国栋 2016-6-27
 * @version 2.1
 */
public class NewWagesActivity extends BaseActivity implements OnClickListener {

	private LinearLayout title_back;// 返回键布局
	private TextView new_content;// 标题
	private RefreshListView1 new_wages_list_lv;// 工资list控件
	private ArrayList<NewWagesBean> newWagesBeanList;// 工资list
	private NewWagesAdapter newWagesAdapter;
	private int k = 2;
	private int pages;// 总页数
	private String userid;
	private ImageView no_data_iv;
	public static final String INTENT_REFESH_WAGES_DATA = "com.wcyc.zigui.action.INTENT_REFESH_WAGES_DATA";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_wages);

		initView();
		initDatas();
		initEvents();

		IntentFilter mrefeshDataFilter = new IntentFilter(INTENT_REFESH_WAGES_DATA);
		registerReceiver(refeshDataReceiver, mrefeshDataFilter);
	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键

		new_wages_list_lv = (RefreshListView1) findViewById(R.id.new_wages_list_lv);
		no_data_iv = (ImageView) findViewById(R.id.no_data_iv);// 无数据
	}

	// 初始化数据
	private void initDatas() {
		new_content.setText("工资条");
		k = 2;
		newWagesBeanList = new ArrayList<NewWagesBean>();
		userid = CCApplication.getInstance().getPresentUser().getUserId();

		try {
			JSONObject json = new JSONObject();
			json.put("schoolId", schoolId);
			json.put("teacherId", userid);
			json.put("curPage", "1");
			json.put("pageSize", "10");
			//网络是否可用
			if (!DataUtil.isNetworkAvailable(NewWagesActivity.this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}
			//是否在上一个请求中
			if (!isLoading()) {
				System.out.println("工资条入参=====" + json);
				queryPost(Constants.GET_WAGE_RECORDS, json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 设置点击效果监听器
	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);
		//条目点击监听
		new_wages_list_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int arg2_i = arg2 - 1;
				Bundle bundle = new Bundle();
				String wageId = newWagesBeanList.get(arg2_i).getId();
				Boolean isRead = newWagesBeanList.get(arg2_i).getIsRead();
				//时间和作者
				String newWagesTimeAndNameStr = newWagesBeanList.get(arg2_i)
						.getYear()
						+ "年"
						+ newWagesBeanList.get(arg2_i).getMonth()
						+ "月"
						+ newWagesBeanList.get(arg2_i).getWageRecordName();
				String newWagesPublishNameAndTimeStr = newWagesBeanList
						.get(arg2_i).getTeacherBaseInfo().getName()
						+ "发布于  " + newWagesBeanList.get(arg2_i).getPublishTime();

				bundle.putString("position", arg2_i + "");
				bundle.putString("wageId", wageId);
				bundle.putBoolean("isRead", isRead);
				bundle.putString("newWagesTimeAndNameStr",
						newWagesTimeAndNameStr);
				bundle.putString("newWagesPublishNameAndTimeStr",
						newWagesPublishNameAndTimeStr);

				newWagesBeanList.get(arg2_i).setIsRead(true);
				newWagesAdapter.notifyDataSetChanged();
				newActivity(NewWagesDetailsActivity.class, bundle);
			}
		});

		new_wages_list_lv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		});
		new_wages_list_lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onDownPullRefresh() {// 下拉刷新
				k = 2;
				initDatas();
				new_wages_list_lv.hideHeaderView();// 收起下拉刷新
			}

			@Override
			public void onLoadingMore() {// 上拉加载更多
				if (k <= pages) {// 后一页页， 总页数////// 如果后面还有数据// 则加载一页
					loadData();
				} else if (k > pages) {
					DataUtil.getToast("没有更多数据了");
				}
				new_wages_list_lv.hideFooterView();
			}

		});
	}

	// 加载更多数据
	public void loadData() {
		if (!DataUtil.isNetworkAvailable(getBaseContext())) {
			DataUtil.getToast("当前网络不可用，请检查您的网络设置");
			return;
		}

		if (!isLoading()) {
			loadDataByIndex(k);
			k++;
		}
	}

	// 加载更多
	public void loadDataByIndex(int index) {

		JSONObject json = new JSONObject();
		try {
			json.put("schoolId", schoolId);
			json.put("teacherId", userid);
			json.put("curPage", k);
			json.put("pageSize", "10");
			System.out.println("第" + k + "页" + "工资条入参=====" + json);
			String url = new StringBuilder(Constants.SERVER_URL).append(
					Constants.GET_WAGE_RECORDS).toString();//
			String result = HttpHelper.httpPostJson(this, url, json);
			ArrayList<NewWagesBean> newWagesBeanList_more = new ArrayList<NewWagesBean>();
			JSONObject json3 = new JSONObject(result);
			String wageRecordsStr = json3.getString("wageRecords");//获取列表数据
			JSONArray json2 = new JSONArray(wageRecordsStr);
			for (int i = 0; i < json2.length(); i++) {
				NewWagesBean newWagesBean = JsonUtils.fromJson(json2.get(i)
						.toString(), NewWagesBean.class);//封装数据
				newWagesBeanList_more.add(newWagesBean);
			}
			newWagesAdapter.addItem(newWagesBeanList_more);//添加数据
			newWagesAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			NewWagesActivity.this.finish();
			break;
		default:
			break;

		}
	}

	@Override
	protected void getMessage(String data) {
		System.out.println("工资条出参data=====" + data);
		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if (ret.getServerResult().getResultCode() != 200) {// 请求失败
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		} else {
			try {
				JSONObject json = new JSONObject(data);
				String pagesStr = json.getString("pages");
				pages = Integer.parseInt(pagesStr);
				System.out.println("工资条出参pages=====" + pages);
				if (pages < 1) {//无数据
					new_wages_list_lv.setVisibility(View.GONE);
					no_data_iv.setVisibility(View.VISIBLE);
				} else {//有数据
					new_wages_list_lv.setVisibility(View.VISIBLE);
					no_data_iv.setVisibility(View.GONE);
					
					String wageRecordsStr = json.getString("wageRecords");
					JSONArray json2 = new JSONArray(wageRecordsStr);
					for (int i = 0; i < json2.length(); i++) {
						NewWagesBean newWagesBean = JsonUtils.fromJson(json2
								.get(i).toString(), NewWagesBean.class);
						newWagesBeanList.add(newWagesBean);
					}
					//设置adapter
					newWagesAdapter = new NewWagesAdapter(this,
							newWagesBeanList);
					new_wages_list_lv.setAdapter(newWagesAdapter);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	
	
	private BroadcastReceiver refeshDataReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
//				initDatas();
			}
		
	};
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(refeshDataReceiver);
	}
}
