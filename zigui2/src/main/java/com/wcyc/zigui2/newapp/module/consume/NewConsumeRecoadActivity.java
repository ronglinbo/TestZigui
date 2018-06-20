package com.wcyc.zigui2.newapp.module.consume;

import java.util.ArrayList;
import java.util.List;

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
 * 消费记录界面
 * 
 * @author 郑国栋 2016-6-30
 * @version 2.0
 */

public class NewConsumeRecoadActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout title_back;
	private TextView new_content;
	private RefreshListView1 new_consume_recoad_lv;//com.wcyc.zigui2.newapp.widget.RefreshListView1
	private ArrayList<NewConsumeRecoadBean> consumerecoadList;// 展示list
	private NewConsumeRecoadAdapter newConsumeRecoadAdapter;
	private int k = 2;
	private ImageView no_data_iv;
	private int pages;// 总页数
	private String userId;
	private String userType;
	private String childId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_consume_recoad);

		initView();
		initDatas();
		initEvents();

	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键

		new_consume_recoad_lv = (RefreshListView1) findViewById(R.id.new_consume_recoad_lv);
		no_data_iv = (ImageView) findViewById(R.id.no_data_iv);
	}

	// 初始化数据
	private void initDatas() {
		new_content.setText("消费记录");
		consumerecoadList=new ArrayList<NewConsumeRecoadBean>(); 
		
		try {
			userId = CCApplication.getInstance().getPresentUser()
					.getUserId();
			userType = CCApplication.getInstance().getPresentUser().getUserType();
			
			JSONObject json = new JSONObject();
			json.put("schoolId", schoolId);
			if("2".equals(userType)){
				json.put("userId", userId);				
			}else if("3".equals(userType)){
				childId = CCApplication.getInstance().getPresentUser().getChildId();
				json.put("userId", childId);
			}
			json.put("userType", userType);
			json.put("curPage", "1");
			json.put("pageSize", "10");
			//网络是否可用
			if (!DataUtil.isNetworkAvailable(NewConsumeRecoadActivity.this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}
			//是否在上一个请求中
			if (!isLoading()) {
				System.out.println("消费记录入参=====" + json);
				queryPost(Constants.GET_CONSUME_INFO, json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// 设置点击效果监听器
	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);

		new_consume_recoad_lv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}
		});
		new_consume_recoad_lv.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onDownPullRefresh() {// 下拉刷新
				k = 2;
				initDatas();
				new_consume_recoad_lv.hideHeaderView();// 收起下拉刷新
			}

			@Override
			public void onLoadingMore() {// 上拉加载更多
				if (k <= pages) {// 后一页页， 总页数////// 如果后面还有数据// 则加载一页
					loadData();
				} else if (k > pages) {
					DataUtil.getToast("没有更多数据了");
				}
				new_consume_recoad_lv.hideFooterView();
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
			if("2".equals(userType)){
				json.put("userId", userId);				
			}else if("3".equals(userType)){
				json.put("userId", childId);
			}
			json.put("userType", userType);
			json.put("curPage", k);
			json.put("pageSize", "10");
			
			System.out.println("第" + k + "页" + "消费记录入参=====" + json);
			String url = new StringBuilder(Constants.SERVER_URL).append(
					Constants.GET_CONSUME_INFO).toString();//
			String result = HttpHelper.httpPostJson(this, url, json);
			ArrayList<NewConsumeRecoadBean> consumerecoadList_more = new ArrayList<NewConsumeRecoadBean>();
			JSONObject json3 = new JSONObject(result);
			String cardConsumeListStr = json3.getString("cardConsumeList");//获取列表数据
			JSONArray json2 = new JSONArray(cardConsumeListStr);
			for (int i = 0; i < json2.length(); i++) {
				NewConsumeRecoadBean newConsumeRecoadBean = JsonUtils.fromJson(json2.get(i)
						.toString(), NewConsumeRecoadBean.class);//封装数据
				consumerecoadList_more.add(newConsumeRecoadBean);
			}
			newConsumeRecoadAdapter.addItem(consumerecoadList_more);//添加数据
			newConsumeRecoadAdapter.notifyDataSetChanged();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			NewConsumeRecoadActivity.this.finish();
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
				System.out.println("消费记录出参pages=====" + pages);
				if (pages < 1) {//无数据
					new_consume_recoad_lv.setVisibility(View.GONE);
					no_data_iv.setVisibility(View.VISIBLE);
				} else {//有数据
					new_consume_recoad_lv.setVisibility(View.VISIBLE);
					no_data_iv.setVisibility(View.GONE);
					
					String cardConsumeListStr = json.getString("cardConsumeList");
					JSONArray json2 = new JSONArray(cardConsumeListStr);
					for (int i = 0; i < json2.length(); i++) {
						NewConsumeRecoadBean newConsumeRecoadBean = JsonUtils.fromJson(json2
								.get(i).toString(), NewConsumeRecoadBean.class);
						consumerecoadList.add(newConsumeRecoadBean);
					}
					//设置adapter
					newConsumeRecoadAdapter = new NewConsumeRecoadAdapter(this,
							consumerecoadList);
					new_consume_recoad_lv.setAdapter(newConsumeRecoadAdapter);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
