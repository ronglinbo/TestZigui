package com.wcyc.zigui2.newapp.module.email;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chat.SlideListView;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.EmailBean;
import com.wcyc.zigui2.newapp.bean.UserType;

import com.wcyc.zigui2.newapp.module.email.EmailActivity;
import com.wcyc.zigui2.newapp.module.email.EmailDetailActivity;
import com.wcyc.zigui2.newapp.module.mailbox.MailInfo;
import com.wcyc.zigui2.newapp.module.notice.NewNoticeBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.newapp.widget.RefreshListView.OnRefreshListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

public class InboxFragment extends EmailItemFragment{
	private View view;
//	private SlideListView listView;
	private RefreshListView listView;
	private EmailListAdapter listAdapter;
	private TextView tvNoMessage;
	private ImageView unread;
	private int curPage = 1;
	
//	public InboxFragment(int Type) {
//		super(Type);
//		// TODO Auto-generated constructor stub
//	}
	public InboxFragment() {
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
		args.putInt("type",EmailActivity.INBOX);
		args.putInt("index", index);
		fragment.setArguments(args);
		//fragment.setIndex(index);
		return fragment;
	}
	
	private void initView(){
		listView = (RefreshListView) view.findViewById(R.id.email_list);
		tvNoMessage = (TextView) view.findViewById(R.id.tv_no_message);
		unread = (ImageView) view.findViewById(R.id.unreadLabel);
	}
	
	private void initData(){
		listAdapter = new EmailListAdapter(getActivity(),list,EmailActivity.INBOX);
		listView.setAdapter(listAdapter);
	}
	
	private void initEvent(){
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.err.println("onItemClick!!!");
				Intent intent = new Intent(getActivity(),EmailDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("type", EmailActivity.INBOX);
				NewMailInfo detail = list.get(arg2);
				bundle.putSerializable("data", detail);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			
		});
		
		listView.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onDownPullRefresh() {//下拉刷新
				// TODO Auto-generated method stub
				System.out.println("onDownPullRefresh curPage:"+curPage);
				if(curPage > 1){
					curPage--;
					list = email.getMailInfoList();	
					DataUtil.getToast("已经是首页了");
				}else{
					curPage = 1;
					getEmailList(curPage);
				}
				listView.hideHeaderView();
			}

			@Override
			public void onLoadingMore() {//上拉加载更多
				// TODO Auto-generated method stub
				System.out.println("onLoadingMore curPage:"+curPage);
				
				if(curPage < email.getTotalPageNum()&& curPage >= email.getPageNum()){
					getEmailList(++curPage);
				}else{
					NewEmailBean email = CCApplication.getInstance().getEmail("inbox");
					list = email.getMailInfoList();
				}
				if(curPage >= email.getTotalPageNum()){
					DataUtil.getToast("没有更多数据了");
				}
				listView.hideFooterView();
			}
			
		});
	}
	
	private void doDelete(int position){
		EmailBean tobeDelete = (EmailBean) listAdapter.getItem(position);
		list.remove(position);	
		listAdapter.notifyDataSetChanged();
		System.out.println("position:"+position+" list size:"+list.size());
		//如果没有消息那么显示没有消息的界面
		if(list.isEmpty()){
			tvNoMessage.setVisibility(View.VISIBLE);
		}else{
			tvNoMessage.setVisibility(View.GONE);
		}
		// 更新消息未读数
	}
	public void getEmailList(int curPage){
		JSONObject json = new JSONObject();
		UserType user = CCApplication.getInstance().getPresentUser(); 

		try {
			String schoolId = user.getSchoolId();
			json.put("userID", user.getUserId());
			json.put("schoolId", schoolId);
			json.put("status", EmailActivity.INBOX);
			json.put("curPage", curPage);
			json.put("pageSize", Constants.defaultPageSize);
			new HttpRequestAsyncTask(json, this,getActivity())
			.execute(Constants.EMAIL_LIST);
			System.out.println("getEmailList:"+json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}