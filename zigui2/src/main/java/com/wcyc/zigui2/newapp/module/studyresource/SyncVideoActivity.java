package com.wcyc.zigui2.newapp.module.studyresource;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.BaseExpandableListAdapter;

import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;

import com.wcyc.zigui2.utils.GoHtml5Function;


//在线课程主界面
public class SyncVideoActivity extends BaseActivity{
	private ExpandableListView listView;
	private ImageView back;
	private List<StudyResource> list;
	private Context mContext;
	private ResAdapter adapter;
	
	//语文资源
	private String[][] chinese = 
				{{"小学语文同步课程 一年级 20集","http://haojiazhang123.com/share/course/class.html?id=20"},
				{"小学语文同步课程 二年级 20集","http://haojiazhang123.com/share/course/class.html?id=25"},
				{"小学语文同步课程 三年级 21集","http://haojiazhang123.com/share/course/class.html?id=23"},
				{"小学语文同步课程 四年级 21集","http://haojiazhang123.com/share/course/class.html?id=24"},
				{"小学语文同步课程 五年级 1集","http://haojiazhang123.com/share/course/class.html?id=26"}};
	
	private String[][] maths = 
			{{"小学数学同步课程 一年级 16集","http://haojiazhang123.com/share/course/class.html?id=5"},
			{"小学数学同步课程 二年级 16集","http://haojiazhang123.com/share/course/class.html?id=6"},
			{"小学数学同步课程 三年级 16集","http://haojiazhang123.com/share/course/class.html?id=7"},
			{"小学数学同步课程 四年级 16集","http://haojiazhang123.com/share/course/class.html?id=8"},
			{"小学数学同步课程 五年级 16集","http://haojiazhang123.com/share/course/class.html?id=4"},
			{"小学数学同步课程 六年级15集","http://haojiazhang123.com/share/course/class.html?id=9"}};
	
		
	private String[][] english = 
			{{"小学英语同步课程 三年级 10集","http://haojiazhang123.com/share/course/class.html?id=28"},
			{"小学英语同步课程 四年级 5集","http://haojiazhang123.com/share/course/class.html?id=27"},
			{"小学英语同步课程 五年级 10集","http://haojiazhang123.com/share/course/class.html?id=21"}};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_list);
		initView();
		initEvent();
		initData();
	}
	
	private void initView(){
		TextView titleText2 = (TextView) findViewById(R.id.title_text_2);
		titleText2.setVisibility(View.VISIBLE);
		titleText2.setText("同步视频");

		back = (ImageView) findViewById(R.id.title_arrow_iv);
		listView = (ExpandableListView) findViewById(R.id.res_list);
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
		list = new ArrayList<StudyResource>();
		StudyResource resource = new StudyResource();
		resource.setCourse("语文");
		List<Resource> studyResource = new ArrayList<Resource>();
		for(int i = 0 ; i < chinese.length ; i ++){
			Resource item = new Resource(chinese[i][0],
					chinese[i][1]);
			studyResource.add(item);
		}
		resource.setStudyResource(studyResource);
		list.add(resource);
		
		resource = new StudyResource();
		resource.setCourse("数学");
		studyResource = new ArrayList<Resource>();
		for(int i = 0 ; i < maths.length ; i ++){
			Resource item = new Resource(maths[i][0],
					maths[i][1]);
			studyResource.add(item);
		}
		resource.setStudyResource(studyResource);
		list.add(resource);
		
		resource = new StudyResource();
		resource.setCourse("英语");
		studyResource = new ArrayList<Resource>();
		for(int i = 0 ; i < english.length ; i ++){
			Resource item = new Resource(english[i][0],
					english[i][1]);
			studyResource.add(item);
		}
		resource.setStudyResource(studyResource);
		list.add(resource);
		adapter = new ResAdapter();
		listView.setAdapter(adapter);
	}
	
	public class ResAdapter extends BaseExpandableListAdapter{

		private class ViewHolder {
			TextView name;
		}

		@Override
		public Object getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return ((StudyResource) getGroup(arg0)).getStudyResource().get(arg1);
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(final int arg0, final int arg1, boolean arg2, View arg3,
				ViewGroup arg4) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(arg3 == null){
				arg3 = getLayoutInflater().inflate(R.layout.list_resource_item, arg4, false);
			}
			holder = new ViewHolder();
			List<Resource> studyResource = list.get(arg0).getStudyResource();
			holder.name = (TextView) arg3.findViewById(R.id.name);
			holder.name.setText(studyResource.get(arg1).getName());
			holder.name.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					goWeb(list.get(arg0).getStudyResource().get(arg1).getUrl());
				}
				
			});
			return arg3;
		}

		@Override
		public int getChildrenCount(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0).getStudyResource().size();
		}

		@Override
		public Object getGroup(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View arg2,
				ViewGroup arg3) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(arg2 == null){
				arg2 = getLayoutInflater().inflate(R.layout.list_resource_item, arg3, false);
				holder = new ViewHolder();
				holder.name = (TextView) arg2.findViewById(R.id.name);
				holder.name.setText(list.get(arg0).getCourse());
				arg2.setTag(holder);
			}else{
				holder = (ViewHolder) arg2.getTag();
			}
			return arg2;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
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