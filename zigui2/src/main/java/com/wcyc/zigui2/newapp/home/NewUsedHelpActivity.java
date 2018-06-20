package com.wcyc.zigui2.newapp.home;

import java.lang.reflect.Type;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.listener.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.adapter.NewUsedHelpAdapter;
import com.wcyc.zigui2.newapp.bean.NewUsedHelpBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.HttpHelper;

/**
 * 使用帮助
 * 
 * @author 郑国栋
 * 2016-4-14
 * @version 2.0
 */
public class NewUsedHelpActivity extends BaseActivity implements HttpRequestAsyncTaskListener,OnClickListener{
	private LinearLayout title_back;
	private TextView new_content;
	private ListView used_help_list;
	private ArrayList<NewUsedHelpBean> contentList=new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_used_help);
		initView();
		initDatas();
		initEvents();
	}
	
	/**
	 * 初始化控件
	 */

	public void initView(){
		new_content = (TextView) findViewById(R.id.new_content);//标题
		title_back = (LinearLayout) findViewById(R.id.title_back);//返回键
		used_help_list = (ListView) findViewById(R.id.used_help_list);
	}
	
	/**
	 * 初始化数据
	 */
	
	private void initDatas() {
		new_content.setText("使用帮助");
		String result="";
		try {
			// JSON对象
			JSONObject json = new JSONObject();
			//请求地址
			String url = new StringBuilder(Constants.SERVER_URL).append(Constants.USED_HELP).toString();
			result = HttpHelper.httpPostJson(this,url, json);
			JSONObject json3 = new JSONObject(result);
			JSONArray ja = json3.getJSONArray("contentList");
			Gson gson1 = new Gson();
			Type t = new TypeToken<List<NewUsedHelpBean>>() {}.getType();
			contentList = gson1.fromJson(ja.toString(), t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NewUsedHelpAdapter myNewUesdHelpAdapter = new NewUsedHelpAdapter(this, contentList);
		used_help_list.setAdapter(myNewUesdHelpAdapter);
	}
	
	/**
	 * 事件控制
	 */

	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.title_back://返回
			NewUsedHelpActivity.this.finish();
			break;
		}
	}

	@Override
	protected void getMessage(String data) {
	}

	@Override
	public void onRequstComplete(String result) {
	}

	@Override
	public void onRequstCancelled() {
	}
}
