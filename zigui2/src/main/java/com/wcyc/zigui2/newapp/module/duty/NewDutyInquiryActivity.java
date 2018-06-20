package com.wcyc.zigui2.newapp.module.duty;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
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
import com.wcyc.zigui2.newapp.module.wages.NewWagesAdapter;
import com.wcyc.zigui2.newapp.module.wages.NewWagesBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.newapp.widget.RefreshListView1.OnRefreshListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

/**
 * 值班查询
 * 
 * @author 郑国栋 2016-6-22
 * @version 2.1
 */
public class NewDutyInquiryActivity extends BaseActivity implements
		OnClickListener ,HttpRequestAsyncTaskListener{

	private LinearLayout title_back;
	private TextView new_content;
	private TextView title_right_tv;
	private RefreshListView1 new_duty_inquiry_lv;
	private ArrayList<NewDutyInquiryBean> newDutyInquiryList;
	private NewDutyInquiryAdapter newDutyInquiryAdapter;
	private int k = 2;
	private int pages;// 总页数
	private String userid;
	private ImageView no_data_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_duty_inquiry);

		initView();
		initDatas();
		initEvents();

	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
		title_right_tv = (TextView) findViewById(R.id.title_right_tv);// 右标题

		new_duty_inquiry_lv = (RefreshListView1) findViewById(R.id.new_duty_inquiry_lv);
		no_data_iv = (ImageView) findViewById(R.id.no_data_iv);
	}

	// 初始化数据
	private void initDatas() {
		new_content.setText("值班查询");
		title_right_tv.setText("我的值班");
		title_right_tv.setTextColor(getResources().getColor(
				R.color.font_darkblue));
		// title_right_tv.set

		newDutyInquiryList = new ArrayList<NewDutyInquiryBean>();

		userid = CCApplication.getInstance().getPresentUser().getUserId();

		JSONObject json = new JSONObject();
		try {
			json.put("schoolId", schoolId);
			json.put("teacherId", userid);
			json.put("curPage", "1");
			json.put("pageSize", "10");
			if (!DataUtil.isNetworkAvailable(NewDutyInquiryActivity.this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}
			if (!isLoading()) {
				System.out.println("值班查询入参=====" + json);
				queryPost(Constants.GET_DATY_LISTS, json);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 设置点击事件监听器
	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);

		title_right_tv.setVisibility(View.VISIBLE);
		title_right_tv.setOnClickListener(this);

		new_duty_inquiry_lv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		});
		new_duty_inquiry_lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onDownPullRefresh() {// 下拉刷新
				k = 2;
				initDatas();
				new_duty_inquiry_lv.hideHeaderView();// 收起下拉刷新
			}

			@Override
			public void onLoadingMore() {// 上拉加载更多
				if (k <= pages) {// 后一页页， 总页数////如果后面还有数据// 则加载一页
					loadData();
				} else if (k > pages) {
					DataUtil.getToast("没有更多数据了");
				}
				new_duty_inquiry_lv.hideFooterView();
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

			System.out.println("第" + k + "页" + "值班查询入参=====" + json);

			String url = new StringBuilder(Constants.SERVER_URL).append(
					Constants.GET_DATY_LISTS).toString();// BASE_URL//SERVER_URL
			//String result = HttpHelper.httpPostJson(this, url, json);
			new HttpRequestAsyncTask(json,this,this).execute(url);



		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			 NewDutyInquiryActivity.this.finish();
			break;
		case R.id.title_right_tv:
			newActivity(NewMyDutyActivity.class, null);
			break;
		default:
			break;

		}
	}

	@Override
	protected void getMessage(String data) {
		System.out.println("值班查询出参=====" + data);
		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if (ret.getServerResult().getResultCode() != 200) {// 请求失败
			DataUtil.getToast(ret.getServerResult().getResultMessage());
		} else {
			try {

				JSONObject json = new JSONObject(data);
				String pagesStr = json.getString("pages");
				pages = Integer.parseInt(pagesStr);
				System.out.println("值班查询出参pages=====" + pages);

				if(pages<1){
					new_duty_inquiry_lv.setVisibility(View.GONE);
					no_data_iv.setVisibility(View.VISIBLE);
				}else{
					new_duty_inquiry_lv.setVisibility(View.VISIBLE);
					no_data_iv.setVisibility(View.GONE);
					
					String dutyRecordsStr = json.getString("dutyRecords");
					JSONArray json2 = new JSONArray(dutyRecordsStr);
					for (int i = 0; i < json2.length(); i++) {
						NewDutyInquiryBean newDutyInquiryBean = JsonUtils.fromJson(
								json2.get(i).toString(), NewDutyInquiryBean.class);
						newDutyInquiryList.add(newDutyInquiryBean);
					}
					
					newDutyInquiryAdapter = new NewDutyInquiryAdapter(this,
							newDutyInquiryList,schoolId);
					new_duty_inquiry_lv.setAdapter(newDutyInquiryAdapter);
				}
				

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onRequstComplete(String result) {

		ArrayList<NewDutyInquiryBean> newDutyInquiryList_more = new ArrayList<NewDutyInquiryBean>();

		JSONObject json3 = null;
		try {
			json3 = new JSONObject(result);
			String dutyRecordsStr = json3.getString("dutyRecords");
			JSONArray json2 = new JSONArray(dutyRecordsStr);
			for (int i = 0; i < json2.length(); i++) {
				NewDutyInquiryBean newDutyInquiryBean = JsonUtils.fromJson(
						json2.get(i).toString(), NewDutyInquiryBean.class);
				newDutyInquiryList_more.add(newDutyInquiryBean);
			}
			newDutyInquiryAdapter.addItem(newDutyInquiryList_more);
			newDutyInquiryAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onRequstCancelled() {

	}
}
