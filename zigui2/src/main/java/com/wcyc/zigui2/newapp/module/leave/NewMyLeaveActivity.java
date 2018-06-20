package com.wcyc.zigui2.newapp.module.leave;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.module.wages.NewWagesAdapter;
import com.wcyc.zigui2.newapp.module.wages.NewWagesBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.newapp.widget.RefreshListView1.OnRefreshListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

/**
 * 我的请假
 * 
 * @author 郑国栋 2016-7-15
 * @version 2.0
 */
public class NewMyLeaveActivity extends BaseActivity implements OnClickListener {

	private LinearLayout title_back;
	private TextView new_content;
	private RefreshListView1 new_my_leave_lv;
	private ArrayList<NewMyLeaveBean> newMyLeaveBeanList;
	private NewMyLeaveAdapter newMyLeaveAdapter;
	private ImageView title_imgbtn_add;
	private String userid;
	private int pages;// 总页数
	private ImageView no_data_iv;
	private int k = 2;
	private String childId;
	public static final String INTENT_REFRESH_DATA_LEAVE = "com.wcyc.zigui.action.INTENT_REFRESH_DATA_LEAVE";//刷新的广播

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_my_leave);

		initView();
		initDatas();
		initEvents();

		IntentFilter mrefeshDataFilter = new IntentFilter(INTENT_REFRESH_DATA_LEAVE);
		registerReceiver(refeshDataReceiver, mrefeshDataFilter);
	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
		title_back.setVisibility(View.VISIBLE);
		title_imgbtn_add = (ImageView) findViewById(R.id.title_imgbtn_add);
		title_imgbtn_add.setVisibility(View.VISIBLE);

		new_my_leave_lv = (RefreshListView1) findViewById(R.id.new_my_leave_lv);
		no_data_iv = (ImageView) findViewById(R.id.no_data_iv);// 无数据

	}

	// 初始化数据
	private void initDatas() {
		new_content.setText("我的请假");//
		newMyLeaveBeanList = new ArrayList<NewMyLeaveBean>();
		k = 2;

		userid = CCApplication.getInstance().getPresentUser().getUserId();
		childId = CCApplication.getInstance().getPresentUser().getChildId();
		try {
			JSONObject json = new JSONObject();
			json.put("schoolId", schoolId);
			json.put("userId", childId);
			json.put("curPage", "1");
			json.put("pageSize", "10");

			if (!DataUtil.isNetworkAvailable(NewMyLeaveActivity.this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}

			if (!isLoading()) {
				System.out.println("我的请假入参=====" + json);
				queryPost(Constants.LEAVE_LIST, json);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 设置点击效果监听器
	private void initEvents() {
		title_back.setOnClickListener(this);
		title_imgbtn_add.setOnClickListener(this);

		new_my_leave_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int arg2_i = arg2 - 1;
				Bundle bundle = new Bundle();
				bundle.putString("position", arg2_i + "");
				NewMyLeaveBean newMyLeaveBean_i=newMyLeaveBeanList.get(arg2_i);
				bundle.putSerializable("newMyLeaveBean_i", newMyLeaveBean_i);
				newActivity(NewMyLeaveDetailsActivity.class, bundle);
			}

		});

		new_my_leave_lv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		});

		new_my_leave_lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onDownPullRefresh() {// 下拉刷新
				k = 2;
				initDatas();
				new_my_leave_lv.hideHeaderView();// 收起下拉刷新
			}

			@Override
			public void onLoadingMore() {// 上拉加载更多
				if (k <= pages) {// 后一页页， 总页数////// 如果后面还有数据// 则加载一页
					loadData();
				} else if (k > pages) {
					DataUtil.getToast("没有更多数据了");
				}
				new_my_leave_lv.hideFooterView();
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
//			json.put("userId", userid);//childId
			json.put("userId", childId);//childId
			json.put("curPage", k);
			json.put("pageSize", "10");

			System.out.println("第" + k + "页" + "我的请假入参=====" + json);

			String url = new StringBuilder(Constants.SERVER_URL).append(
					Constants.LEAVE_LIST).toString();// BASE_URL//SERVER_URL
			String result = HttpHelper.httpPostJson(this, url, json);
//			System.out.println("第" + k + "页" + "我的请假出参=====" + result);

			ArrayList<NewMyLeaveBean> newMyLeaveBeanList_more = new ArrayList<NewMyLeaveBean>();
			// result);
			JSONObject json3 = new JSONObject(result);
			if(json3.has("leaveList")){
				String leaveListStr = json3.getString("leaveList");
				JSONArray json2 = new JSONArray(leaveListStr);
				for (int i = 0; i < json2.length(); i++) {
					NewMyLeaveBean newMyLeaveBean = JsonUtils.fromJson(json2.get(i)
							.toString(), NewMyLeaveBean.class);
					newMyLeaveBeanList_more.add(newMyLeaveBean);
				}
				newMyLeaveAdapter.addItem(newMyLeaveBeanList_more);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			NewMyLeaveActivity.this.finish();
			break;
		case R.id.title_imgbtn_add:
			newActivity(NewMyLeaveAskActivity.class, null);
			break;
		default:
			break;

		}
	}

	@Override
	protected void getMessage(String data) {
		System.out.println("我的请假出参data=====" + data);
		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if (ret.getServerResult().getResultCode() != 200) {// 请求失败
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		} else {

			try {
				JSONObject json = new JSONObject(data);
				String pagesStr = json.getString("totalPageNum");
				pages = Integer.parseInt(pagesStr);
				System.out.println("我的请假出参pages=====" + pages);

				if (pages < 1) {
					new_my_leave_lv.setVisibility(View.GONE);
					no_data_iv.setVisibility(View.VISIBLE);
				} else {
					new_my_leave_lv.setVisibility(View.VISIBLE);
					no_data_iv.setVisibility(View.GONE);

					String leaveListStr = json.getString("leaveList");
					JSONArray json2 = new JSONArray(leaveListStr);
					for (int i = 0; i < json2.length(); i++) {
						NewMyLeaveBean newMyLeaveBean = JsonUtils.fromJson(
								json2.get(i).toString(), NewMyLeaveBean.class);
						newMyLeaveBeanList.add(newMyLeaveBean);
					}

					newMyLeaveAdapter = new NewMyLeaveAdapter(this,
							newMyLeaveBeanList);
					new_my_leave_lv.setAdapter(newMyLeaveAdapter);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(refeshDataReceiver);
	}
	
	private BroadcastReceiver refeshDataReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			initDatas();
		}
	};

}
