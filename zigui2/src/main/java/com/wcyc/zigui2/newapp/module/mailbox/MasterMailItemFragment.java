package com.wcyc.zigui2.newapp.module.mailbox;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
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

import com.wcyc.zigui2.R;

import com.wcyc.zigui2.chat.SlideListView;
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
	public static final int OPERATION = 1;
	private Bundle bundle;

	public interface GetAdapterDataListener{
		List<MailInfo> GetAdapterData(int Type);
		EmailBean GetMail();
		EmailBean getReplyMail();
	}

	public MasterMailItemFragment(){

	}

	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle){
		view = inflater.inflate(R.layout.master_mail_fragment, null);	
		initView();
		initEvent();
		listener = (GetAdapterDataListener) getActivity();
		list = listener.GetAdapterData(type);
		//System.out.println("====list.size()===="+list.size());
		initData();
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
	
	public void onResume(){
		super.onResume();
		System.out.println(this+"onResume");

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

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		bundle = getArguments();
		if(bundle != null){
			type = bundle.getInt("type");
			isAdmin = bundle.getBoolean("isAdmin");
		}
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
				System.out.println("email item onItemClick:" + arg2 + "arg3:" + arg3);
				//toadd query attachemnt
				Intent intent = null;
				if (isAdmin) {
					intent = new Intent(getActivity(), SchoolMasterMailDetailActivity.class);
				} else {
					intent = new Intent(getActivity(), NonSchoolMasterMailDetailActivity.class);
				}
				type = bundle.getInt("type");
				List<MailInfo> list1 = listener.GetAdapterData(type);
				if(list1 != null) {
					list = list1;
				}
				pos = (int) arg3;
				Bundle bundle = new Bundle();
				bundle.putInt("type", type);
				System.out.println("====list.size()====="+list.size());
				MailInfo detail = list.get(pos);
				bundle.putSerializable("data", detail);
				intent.putExtras(bundle);
				startActivityForResult(intent, OPERATION);
			}
		});
		
		listView.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onDownPullRefresh() {
				// TODO Auto-generated method stub
				System.out.println("onDownPullRefresh curPage:"+curPage);
				if(curPage > 1){
					--curPage;
					getMail(MasterMailItemFragment.this.type);
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
					getMail(MasterMailItemFragment.this.type);
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
	
	public void getMail(int type){
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

	public void onActivityResult(int requestCode,int resultCode,Intent data){
		if(requestCode == OPERATION){
			if(resultCode == Activity.RESULT_OK){
				if(data != null){
					parseIntent(data);
				}
				getMail(SchoolMasterMailActivity.REPLYED);
			}
		}
		super.onActivityResult(requestCode,resultCode,data);
	}

	private void parseIntent(Intent intent){
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			int type = bundle.getInt("type");
			int pos = bundle.getInt("pos");
			MailInfo data = (MailInfo) bundle.getSerializable("data");
			System.out.println("SchoolMasterEmailActivity onResume,type:"+type+" data:"+data);
			List<MailInfo> unReply = listener.GetAdapterData(SchoolMasterMailActivity.UNREPLY);
			unReply.remove(pos);
			if(listAdapter != null){
				listAdapter.notifyDataSetChanged();
			}
//			List<MailInfo> replied = listener.GetAdapterData(SchoolMasterMailActivity.REPLYED);
//			replied.add(data);
//			Collections.sort(replied, new Comparator<MailInfo>() {
//				@Override
//				public int compare(MailInfo lhs, MailInfo rhs) {
//					long time1 = 0;
//					long time2 = 0;
//					try {
//						time1 = DataUtil.getLongDate(lhs.getPublishTime());
//						time2 = DataUtil.getLongDate(lhs.getPublishTime());
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if (time1 == time2) {
//						return 0;
//					} else if (time1 < time2) {
//						return 1;
//					} else {
//						return -1;
//					}
//				}
//			});
		}
	}
}