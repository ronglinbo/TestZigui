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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;

import com.wcyc.zigui2.newapp.module.studyresource.Resource;
import com.wcyc.zigui2.utils.GoHtml5Function;


		
//年级套餐列表主界面
public class TaoCanListResourceActivity extends BaseActivity{
//	private ListView listView;
	private GridView gridView;
	private ImageView back;
	private List<Resource> list;
	private ResAdapter adapter;
	private String data[][] = 
			{{"初一全科全年套餐","http://www.vko.cn/course/goods/viewDetail?id=34210282836090"},
			{"初二全科全年套餐","http://www.vko.cn/course/goods/viewDetail?id=34210282836084"},
			{"初三全科全年套餐","http://www.vko.cn/course/goods/viewDetail?id=34210282836080"},
			{"高一全科全年套餐","http://www.vko.cn/course/goods/viewDetail?id=34210282836088"},
			//统一写成英文状态下的()括号
			{"高二全科全年套餐(文科)","http://www.vko.cn/course/goods/viewDetail?id=34210282836086"},
			{"高二全科全年套餐(理科)","http://www.vko.cn/course/goods/viewDetail?id=34210282836082"},
			{"高三全科全年套餐(文科)","http://www.vko.cn/course/goods/viewDetail?id=34210282836076"},
			{"高三全科全年套餐(理科)","http://www.vko.cn/course/goods/viewDetail?id=34210282836078"},
			{"备战2017高考 文科一轮复习课程","http://www.vko.cn/course/goods/viewDetail?id=1418"},
			{"备战2017高考 理科一轮复习课程","http://www.vko.cn/course/goods/viewDetail?id=1417"}};
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.list_study_res);
		setContentView(R.layout.list_study_resource);
		initView();
		initEvent();
		initData();
	}
	
	private void initView(){
		TextView titleText2 = (TextView) findViewById(R.id.title_text_2);
		if(titleText2 != null) {
			titleText2.setVisibility(View.VISIBLE);
			titleText2.setText("年级套餐");
		}
		back = (ImageView) findViewById(R.id.title_arrow_iv);
//		listView = (ListView) findViewById(R.id.res_list);
		gridView = (GridView) findViewById(R.id.res_list);
		
	}
	
	private void initEvent(){
		if(back != null) {
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}

			});
		}
	}
	
	private void initData(){
		list = new ArrayList<Resource>();
		for(int i = 0; i < data.length; i++){
			Resource resource = new Resource(data[i][0],data[i][1]);
			list.add(resource);
		}
		adapter = new ResAdapter();
		gridView.setAdapter(adapter);
	}
	
	public class ResAdapter extends BaseAdapter{
		private class ViewHolder {
			TextView name;
			TextView course_type_tv;
			ImageView res_iv;
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
//				arg1 = getLayoutInflater().inflate(R.layout.list_resource_item, arg2, false);
				arg1 = getLayoutInflater().inflate(R.layout.list_res_item, arg2, false);
				
//				arg1.setTag(holder);
			}
			
			holder = new ViewHolder();
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.course_type_tv = (TextView) arg1.findViewById(R.id.course_type_tv);
			holder.res_iv = (ImageView) arg1.findViewById(R.id.res_iv);
			
			if(arg0==1){
				holder.res_iv.setImageResource(R.drawable.icon_chuer);
			}else if(arg0==2){
				holder.res_iv.setImageResource(R.drawable.icon_chusan);
			}else if(arg0==3){
				holder.res_iv.setImageResource(R.drawable.icon_gaoyi);
			}else if(arg0==4){
				holder.res_iv.setImageResource(R.drawable.icon_gaoerwenke);
			}else if(arg0==5){
				holder.res_iv.setImageResource(R.drawable.icon_gaoerlike);
			}else if(arg0==6){
				holder.res_iv.setImageResource(R.drawable.icon_gaosanwenke);
			}else if(arg0==7){
				holder.res_iv.setImageResource(R.drawable.icon_gaosanlike);
			}else if(arg0==8){
				holder.res_iv.setImageResource(R.drawable.icon_gaokaowenke);
			}else if(arg0==9){
				holder.res_iv.setImageResource(R.drawable.icon_gaokaolike);
			}else{
				holder.res_iv.setImageResource(R.drawable.icon_chuyi);
			}

			String name=list.get(arg0).getName();
			if(name.contains("(")){
				holder.course_type_tv.setVisibility(View.VISIBLE);
				String course=name.substring(name.indexOf("("));
				name=name.substring(0,name.indexOf("("));
				holder.name.setText(name);
				holder.course_type_tv.setText(course);
			}else{
				holder.course_type_tv.setVisibility(View.GONE);
				holder.name.setText(name);
			}

			
			
			arg1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					String url = GoHtml5Function.loginWeikeWeb(list.get(arg0).getUrl());
					String title = list.get(arg0).getName();
					goWeb(url,title);
				}
				
			});
			return arg1;
		}
		
	}
	
	//三方web资源
	private void goWeb(String url,String title){
		Bundle bundle = new Bundle();
		bundle.putString("url",url);
		bundle.putString("title",title);
		System.out.println("url:"+url);
		newActivity(BaseWebviewActivity.class, bundle);
	}
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		
	}
}