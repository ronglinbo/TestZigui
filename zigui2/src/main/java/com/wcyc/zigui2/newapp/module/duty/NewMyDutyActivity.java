package com.wcyc.zigui2.newapp.module.duty;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.newapp.widget.RefreshListView.OnRefreshListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

/**
 * 我的值班
 * 
 * @author 郑国栋 2016-6-30
 * @version 2.0
 */
public class NewMyDutyActivity extends BaseActivity implements OnClickListener,HttpRequestAsyncTaskListener {

	private LinearLayout title_back;
	private TextView new_content;
	private RefreshListView new_my_duty_lv;
	private ArrayList<NewMyDutyBean> newMyDutyList;// 展示list
	private NewMyDutyAdapter newMyDutyAdapter;
	private int k = 2;
	private int pages;// 总页数
	private String userid;
	private ImageView no_data_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_my_duty);

		initView();
		initDatas();
		initEvents();

	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键

		new_my_duty_lv = (RefreshListView) findViewById(R.id.new_my_duty_lv);
		no_data_iv = (ImageView) findViewById(R.id.no_data_iv);
	}

	// 初始化数据
	private void initDatas() {
		new_content.setText("我的值班");
		newMyDutyList = new ArrayList<NewMyDutyBean>();

		userid = CCApplication.getInstance().getPresentUser().getUserId();
		JSONObject json = new JSONObject();
		try {
			json.put("schoolId", schoolId);
			json.put("teacherId", userid);
			json.put("curPage", "1");
			json.put("pageSize", "10");

			if (!DataUtil.isNetworkAvailable(this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}

			if (!isLoading()) {

				System.out.println("我的值班入参=====" + json);
				queryPost(Constants.MA_DATY_PLAN, json);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 设置点击事件监听器
	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);

		new_my_duty_lv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		});
		new_my_duty_lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onDownPullRefresh() {// 下拉刷新
				// requestClassList();
				k = 2;
				initDatas();
				new_my_duty_lv.hideHeaderView();// 收起下拉刷新
			}

			@Override
			public void onLoadingMore() {// 上拉加载更多
				if (k <= pages) {// 后一页页， 总页数////如果后面还有数据// 则加载一页
					loadData();
				} else if (k > pages) {
					DataUtil.getToast("没有更多数据了");
				}
				new_my_duty_lv.hideFooterView();
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
//private ProgressDialog pd;
//	public  void dismissPd(){
//		if(pd != null && pd.isShowing()) {
//			try {
//				pd.dismiss();
//			}catch (Exception e){
//				e.printStackTrace();
//			}
//		}
//	}
	// 加载更多
	public void loadDataByIndex(int index) {
		JSONObject json = new JSONObject();
		try {
			json.put("schoolId", schoolId);
			json.put("teacherId", userid);
			json.put("curPage", k);
			json.put("pageSize", "10");

			System.out.println("第" + k + "页" + "我的值班入参=====" + json);

			String url = new StringBuilder(Constants.SERVER_URL).append(
					Constants.MA_DATY_PLAN).toString();// BASE_URL//SERVER_URL
//进度条

			new HttpRequestAsyncTask(json,this,this).execute(url);
//这里开始  错误
			//String result = HttpHelper.httpPostJson(this, url, json);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			NewMyDutyActivity.this.finish();
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
				String pagesStr = json.getString("pages");
				pages = Integer.parseInt(pagesStr);
				System.out.println("我的值班pages=====" + pages);

				if (pages < 1) {
					new_my_duty_lv.setVisibility(View.GONE);
					no_data_iv.setVisibility(View.VISIBLE);
				} else {
					new_my_duty_lv.setVisibility(View.VISIBLE);
					no_data_iv.setVisibility(View.GONE);
					String dutyRecordsStr = json.getString("dutyRecords");
					JSONArray json2 = new JSONArray(dutyRecordsStr);
					for (int i = 0; i < json2.length(); i++) {
						NewMyDutyBean newMyDutyBean = JsonUtils.fromJson(json2
								.get(i).toString(), NewMyDutyBean.class);
						newMyDutyList.add(newMyDutyBean);
					}

					newMyDutyAdapter = new NewMyDutyAdapter(this, newMyDutyList,schoolId);
					new_my_duty_lv.setAdapter(newMyDutyAdapter);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onRequstComplete(String result) {


		ArrayList<NewMyDutyBean> newMyDutyList_more = new ArrayList<NewMyDutyBean>();

		JSONObject json3 = null;
		try {
			json3 = new JSONObject(result);
			String dutyRecordsStr = json3.getString("dutyRecords");
			JSONArray json2 = new JSONArray(dutyRecordsStr);
			for (int i = 0; i < json2.length(); i++) {
				NewMyDutyBean newMyDutyBean = JsonUtils.fromJson(json2.get(i)
						.toString(), NewMyDutyBean.class);
				newMyDutyList_more.add(newMyDutyBean);
			}
			newMyDutyAdapter.addItem(newMyDutyList_more);
			newMyDutyAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onRequstCancelled() {

	}
}
