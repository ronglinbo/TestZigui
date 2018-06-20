package com.wcyc.zigui2.newapp.module.email;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chat.SlideListView;
import com.wcyc.zigui2.newapp.bean.EmailBean;

import com.wcyc.zigui2.newapp.module.email.EmailActivity;
import com.wcyc.zigui2.newapp.module.email.EmailDetailActivity;
import com.wcyc.zigui2.newapp.module.mailbox.MailInfo;

public class OutboxFragment extends EmailItemFragment{
	private View view;
	private SlideListView listView;
//	private static List<EmailBean> list;
	private EmailListAdapter listAdapter;
	private TextView tvNoMessage;
	
//	public OutboxFragment(int Type) {
//		super(Type);
//		// TODO Auto-generated constructor stub
//	}

	public OutboxFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle){
		view = inflater.inflate(R.layout.inbox_fragment, null);	
		initView();
		initData();
		initEvent();
		return view;
	}

	public static Fragment newInstance(int index) {
		// TODO Auto-generated method stub
		Fragment fragment = new EmailItemFragment();
		Bundle args = new Bundle();
		args.putInt("type",EmailActivity.OUTBOX);
		args.putInt("index", index);
		fragment.setArguments(args);
		//fragment.setIndex(index);
		return fragment;
	}
	
	private void initView(){
		listView = (SlideListView) view.findViewById(R.id.email_list);
		tvNoMessage = (TextView)view.findViewById(R.id.tv_no_message);
	}
	
	private void initData(){
		listAdapter = new EmailListAdapter(getActivity(),list,EmailActivity.OUTBOX);
		listView.setAdapter(listAdapter);
	}
	
	
	private void initEvent(){
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.err.println("outbox onItemClick");
				Intent intent = new Intent(getActivity(),EmailDetailActivity.class);
				startActivity(intent);
			}
			
		});
		
		//设置删除按钮监听
		listView.setDelButtonClickListener(new com.wcyc.zigui2.chat.SlideListView.DelButtonClickListener(){
			@Override
			public void clickHappend(final int position){
				
				list.remove(position);
				//listadapter.remove(tobeDelete);
				listAdapter.notifyDataSetChanged();
				for(NewMailInfo bean:list){
					System.out.println("left:"+bean.getTitle());
				}

				//如果没有消息那么显示没有消息的界面
				if(list.isEmpty()){
					tvNoMessage.setVisibility(View.VISIBLE);
				}
				// 更新消息未读数
			}
		});
	}
}