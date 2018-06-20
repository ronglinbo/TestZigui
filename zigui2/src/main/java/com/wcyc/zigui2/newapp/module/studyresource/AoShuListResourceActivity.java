package com.wcyc.zigui2.newapp.module.studyresource;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.newapp.module.studyresource.Resource;



//奥数列表主界面

public class AoShuListResourceActivity extends BaseActivity{
	private ListView listView;
	private ImageView back;
	private List<Resource> list;
	private ResAdapter adapter;
	private String data[][] = 
			{{"乐乐课堂之一年级奥数 5集","http://haojiazhang123.com/share/course/class.html?id=33"},
			{"乐乐课堂之二年级奥数 5集","http://haojiazhang123.com/share/course/class.html?id=32"},
			{"乐乐课堂之三年级奥数 3集","http://haojiazhang123.com/share/course/class.html?id=31"},
			{"乐乐课堂之四年级奥数 4集","http://haojiazhang123.com/share/course/class.html?id=30"},
			{"乐乐课堂之五年级奥数5集","http://haojiazhang123.com/share/course/class.html?id=29"},
			{"乐乐课堂之六年级奥数5集","http://haojiazhang123.com/share/course/class.html?id=14"}};
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_study_res);
		initView();
		initEvent();
		initData();
	}
	
	private void initView(){
		TextView titleText2 = (TextView) findViewById(R.id.title_text_2);
		titleText2.setVisibility(View.VISIBLE);
		titleText2.setText("奥数");

		back = (ImageView) findViewById(R.id.title_arrow_iv);
		listView = (ListView) findViewById(R.id.res_list);
	}
	
	private void initEvent(){
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}
	
	private void initData(){
		list = new ArrayList<Resource>();
		for(int i = 0; i < data.length; i++){
			Resource resource = new Resource(data[i][0],data[i][1]);
			list.add(resource);
		}
		adapter = new ResAdapter();
		listView.setAdapter(adapter);
	}
	
	public class ResAdapter extends BaseAdapter{
		private class ViewHolder {
			TextView name;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(arg1 == null){
				arg1 = getLayoutInflater().inflate(R.layout.list_resource_item, arg2, false);
			}
			holder = new ViewHolder();
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.name.setText(list.get(arg0).getName());
			holder.name.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					goWeb(list.get(arg0).getUrl());
				}
				
			});
			return arg1;
		}
		
	}
	
	//三方web资源
	private void goWeb(String url){
		Bundle bundle = new Bundle();
		bundle.putString("url",url);
		System.out.println("url:"+url);
		newActivity(BaseWebviewActivity.class, bundle);
	}
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		
	}
	
	
}