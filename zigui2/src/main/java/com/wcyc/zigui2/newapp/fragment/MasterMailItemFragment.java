package com.wcyc.zigui2.newapp.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wcyc.zigui2.R;

import com.wcyc.zigui2.chat.SlideListView;
import com.wcyc.zigui2.newapp.module.mailbox.MailInfo;
import com.wcyc.zigui2.newapp.module.mailbox.MasterMailDetailActivity;
import com.wcyc.zigui2.newapp.module.mailbox.MasterMailListAdapter;
import com.wcyc.zigui2.newapp.module.mailbox.NonSchoolMasterMailDetailActivity;
import com.wcyc.zigui2.newapp.module.mailbox.SchoolMasterMailActivity;
import com.wcyc.zigui2.newapp.module.mailbox.SchoolMasterMailDetailActivity;
import com.wcyc.zigui2.newapp.bean.EmailBean;


import com.wcyc.zigui2.newapp.widget.RefreshListView1.OnRefreshListener;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.utils.DataUtil;
//信箱管理员需要拆成两个
public class MasterMailItemFragment extends Fragment{
	protected View view;
	protected RefreshListView1 listView;
//	protected SlideListView listView;
	protected List<EmailBean> list1;
	protected List<MailInfo> list;
	protected MasterMailListAdapter listAdapter;
	protected TextView tvNoMessage;
	protected GetAdapterDataListener listener;
	protected int type;
	protected static int pos;//当前选中的email位置
	protected boolean isReply;
	protected boolean isAdmin;//是否是校长信箱管理员
	private int curPage = 1,curPage1 = 1,totalPageNum;
	
	public interface GetAdapterDataListener{
		List<MailInfo> GetAdapterData(int Type);
		EmailBean GetMail();
		EmailBean getReplyMail();
	}
	public MasterMailItemFragment(){

	}
//	public MasterMailItemFragment(int type){
//		this.type = type;
//	}
//
//	public MasterMailItemFragment(int type,boolean isAdmin){
//		this.type = type;
//		this.isAdmin = isAdmin;
//	}
		
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle){
		view = inflater.inflate(R.layout.master_mail_fragment, null);	
		initView();
		initData();
		initEvent();
		return view;
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		listener = (GetAdapterDataListener) activity;
		list = listener.GetAdapterData(type);
		if(type == SchoolMasterMailActivity.ALL
		||type == SchoolMasterMailActivity.UNREPLY){
			totalPageNum = listener.GetMail().getTotalPageNum();
			curPage = listener.GetMail().getPageNum();
		}else if(type == SchoolMasterMailActivity.REPLYED){
			EmailBean email = listener.getReplyMail();
			if(email != null){
				totalPageNum = email.getTotalPageNum();
				curPage = email.getPageNum();
			}
		}
	}
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null){
			type = bundle.getInt("type");
			isAdmin = bundle.getBoolean("isAdmin");
		}
	}
	public void onResume(){
		super.onResume();
		System.out.println(this+"onResume");
		listener = (GetAdapterDataListener) getActivity();
		list = listener.GetAdapterData(type);
	}
	
	public static Fragment newInstance(int type) {
		// TODO Auto-generated method stub
		Fragment fragment = new MasterMailItemFragment();
		Bundle args = new Bundle();
		args.putInt("type",type);
		args.putInt("index", type);
		fragment.setArguments(args);
		//fragment.setIndex(index);
		return fragment;
	}
	
	public static Fragment newInstance(int type,boolean isAdmin) {
		// TODO Auto-generated method stub
		Fragment fragment = new MasterMailItemFragment();
		Bundle args = new Bundle();
		args.putInt("type",type);
		args.putBoolean("isAdmin",isAdmin);
		args.putInt("index", type);
		fragment.setArguments(args);
		//fragment.setIndex(index);
		return fragment;
	}
	
	private void initView(){
		listView = (RefreshListView1) view.findViewById(R.id.email_list1);
//		listView = (SlideListView) view.findViewById(R.id.email_list);
		listView.setVisibility(View.VISIBLE);
		
		tvNoMessage = (TextView) view.findViewById(R.id.tv_no_message);
	}
	
	private void initData(){
		
		if(listView != null){
			listAdapter = new MasterMailListAdapter(getActivity(),list,isAdmin,type);
			listView.setAdapter(listAdapter);
		}
		//如果没有消息那么显示没有消息的界面
		if(tvNoMessage != null){
			if(listAdapter.isEmpty()){
				tvNoMessage.setVisibility(View.VISIBLE);
			}else{
				tvNoMessage.setVisibility(View.GONE);
			}
		}
	}
	
	private void initEvent(){
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.out.println("email item onItemClick:"+arg2+"arg3:"+arg3);
				//toadd query attachemnt
				Intent intent = null;
				if(isAdmin){
					intent = new Intent(getActivity(),SchoolMasterMailDetailActivity.class);
				}else{
					intent = new Intent(getActivity(),NonSchoolMasterMailDetailActivity.class);
				}
				pos = (int) arg3;
				Bundle bundle = new Bundle();
				bundle.putInt("type", type);
				MailInfo detail = list.get(pos);
				bundle.putSerializable("data", detail);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
			
		});
		
		listView.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onDownPullRefresh() {
				// TODO Auto-generated method stub
				System.out.println("onDownPullRefresh curPage:"+curPage);
				if(curPage > 1){
					--curPage;
					getMail();
				}else{
					DataUtil.getToast("已经是首页了");
				}
				listView.hideHeaderView();
			}

			@Override
			public void onLoadingMore() {
				// TODO Auto-generated method stub
				System.out.println("onLoadingMore curPage:"+curPage+" totalPageNum："+totalPageNum);
				if(curPage < totalPageNum){
					++curPage;
					getMail();
				}else{
					DataUtil.getToast("没有更多数据了");
				}
				listView.hideFooterView();
			}
			
		});
	}
	
	public MasterMailListAdapter getAdapter(){
		return listAdapter;
	}
	
	public void getMail(){
		int action;
		if(type == SchoolMasterMailActivity.ALL
			||type == SchoolMasterMailActivity.UNREPLY){
			action = SchoolMasterMailActivity.GET_UNREPLY_MAIL;
			((SchoolMasterMailActivity) getActivity()).getEmailList(isAdmin,curPage,"0",action);
		}else if(type == SchoolMasterMailActivity.REPLYED){
			action = SchoolMasterMailActivity.GET_REPLYED_MAIL;
			((SchoolMasterMailActivity) getActivity()).getEmailList(isAdmin,curPage,"1",action);
		}
		
	}
}