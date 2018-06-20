package com.wcyc.zigui2.newapp.module.summary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.EMChatManager;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;

import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.chat.SlideListView;
import com.wcyc.zigui2.chat.SlideListView.DelButtonClickListener;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AttachmentBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;

import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean.Role;
import com.wcyc.zigui2.newapp.module.notice.NewNoticeBean;
import com.wcyc.zigui2.newapp.module.notice.NoticeDetail;
import com.wcyc.zigui2.newapp.module.notice.NotifyActivity;
import com.wcyc.zigui2.newapp.module.summary.SummaryBean.SummaryDetail;
import com.wcyc.zigui2.newapp.widget.DeleteItemPop;
import com.wcyc.zigui2.newapp.widget.DeleteItemPop.OnLongClick;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.newapp.widget.RefreshListView.OnRefreshListener;
//import com.wcyc.zigui2.newapp.widget.SlideListView.DelButtonClickListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.CustomDialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

//总结主界面
public class SummaryActivity extends BaseActivity 
	implements OnLongClick, HttpRequestAsyncTaskListener {
	private RefreshListView listView;
	private ImageButton add;
	private ImageView back,delBtn;
	private View backBtn;

	private SummaryListAdapter listadapter;
	private TextView tvNoMessage;
	
	private static final int ACTION_GET_SUMMARY_LIST = 1;
	private static final int ACTION_GET_ATTACHMENT_LIST = 2;
	private static final int ACTION_DEL_SUMMARY = 3;
	private static final int ACTION_LOAD_MORE = 4;
	private NewMemberBean member;
	private SummaryBean summary;
	private MemberDetailBean userDetail;
	private List<SummaryDetail> list;
	private int pos;
	private String userId,schoolId,noticeId;
	private int curPage = 1;
	private static final String CLASS_AMDIN = "classteacher";//班主任
	private CustomDialog dialog;
	private boolean isAdmin = false;
	private static final int PUBLISH_SUMMARY = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		setContentView(R.layout.activity_summary);
		initView();
		initEvent();
		getFirstPage();
	}

	private void getFirstPage(){
		curPage = 1;
		CCApplication.getInstance().setSummary(null);
		getSummaryList(curPage);
	}

	protected void onResume(){
		super.onResume();

	}
	
	
	private void initView(){
		TextView titleText2 = (TextView) findViewById(R.id.title_text_2);
		titleText2.setVisibility(View.VISIBLE);
		titleText2.setText(R.string.summary);
		Button title_imgbtn = (Button) findViewById(R.id.title_btn);
		title_imgbtn.setVisibility(View.GONE);
		back = (ImageView) findViewById(R.id.title_arrow_iv);
		backBtn = findViewById(R.id.title_back);
		add = (ImageButton)findViewById(R.id.title_imgbtn_add);
		String type = CCApplication.getInstance().getPresentUser().getUserType();
		if(CCApplication.getInstance().isSchoolAdmin()){//学校管理员，校级领导有权限发总结
			isAdmin = true;
			add.setVisibility(View.VISIBLE);
		}
		if(CCApplication.getInstance().isDeptAdmin()){//部门负责人有权限发总结
			add.setVisibility(View.VISIBLE);
		}
		listView = (RefreshListView) findViewById(R.id.summary_list);
		
		tvNoMessage = (TextView) findViewById(R.id.tv_no_message);
	}
	
	private void initEvent(){
		summary = CCApplication.getInstance().getSummary();
		if(summary != null){
			list = summary.getSummaryList();
		}
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SummaryActivity.this,
						PublishSummaryActivity.class);
				startActivityForResult(intent,PUBLISH_SUMMARY);
			}
			
		});
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		backBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				pos = (int) arg3;
				SummaryDetail item = list.get(pos);
				if(list != null && pos < list.size() && item != null){
					System.out.println("onclick list:"+arg1.getId()+" arg2:"+arg2+" arg3:"+arg3);
//					DataUtil.getAttachmentList(NotifyActivity.this,
//							list.get(pos).getNoticeId()+"",Constants.NOTICE,ACTION_GET_ATTACHMENT_LIST);
					item.setIsRead("1");
					String num = item.getSummaryBrowseNum();
					if(num != null){
						int add = Integer.parseInt(num)+1;
						item.setSummaryBrowseNum(String.valueOf(add));
					}
					
					CCApplication.getInstance().setSummary(summary);
					listadapter.notifyDataSetChanged();
					gotoDetail(item);
				}
				
			}
			
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				pos = (int) arg3;

				DeleteItemPop pop = new DeleteItemPop(SummaryActivity.this,SummaryActivity.this);
				int[] location = {-1,-1};
				arg1.getLocationOnScreen(location);
				pop.showAtLocation(arg1, Gravity.NO_GRAVITY, location[0]+arg1.getWidth()/2, location[1]);  
				
				return true;
			}
			
		});
		listView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		
		
		listView.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onDownPullRefresh() {//下拉刷新
				// TODO Auto-generated method stub
				System.out.println("onDownPullRefresh curPage:"+curPage);
				if(curPage > 1){
					curPage--;
					list = summary.getSummaryList();
//					getNoticeList(curPage);
					DataUtil.getToast("已经是首页了");
				}else{
					curPage = 1;
					getSummaryList(curPage);
				}
				listView.hideHeaderView();
			}

			@Override
			public void onLoadingMore() {//上拉加载更多
				// TODO Auto-generated method stub
				System.out.println("onLoadingMore curPage:"+curPage);
				
				if(curPage < summary.getTotalPageNum()&& curPage >= summary.getPageNum()){
					getSummaryList(++curPage);
				}else{
//					list = notice.getNoticeList();
					SummaryBean summary = CCApplication.getInstance().getSummary();
					list = summary.getSummaryList();
				}
				if(curPage >= summary.getTotalPageNum()){
					DataUtil.getToast("没有更多数据了");
				}
				listView.hideFooterView();
			}
			
		});
	}
	
	protected void updateUnreadLabel() {
		// TODO Auto-generated method stub
		
	}

	private void initData(){
		member = CCApplication.getInstance().getMemberInfo();
	}
	
	protected void getSummaryList(int curPage){
		JSONObject json = new JSONObject();
		user = CCApplication.getInstance().getPresentUser(); 
		if(member != null){
			try {
				userId = user.getUserId();
				schoolId = user.getSchoolId();
				json.put("userId", userId);
				json.put("schoolId", schoolId);
				json.put("userType",user.getUserType());
				if(CCApplication.getInstance().isCurUserParent()){
					json.put("childId", user.getChildId());
				}
				json.put("curPage", curPage);
				json.put("pageSize", Constants.defaultPageSize);
				if(curPage > 1){
					action = ACTION_LOAD_MORE;
				}else{
					action = ACTION_GET_SUMMARY_LIST;
				}
//				queryPost(Constants.SUMMARY_LIST,json);
				new HttpRequestAsyncTask(json, this, this).execute(Constants.SUMMARY_LIST);
				System.out.println("getSummaryList:"+json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void parseSummaryList(String data){
		try{
			System.out.println("getSummaryList"+data);
			
			summary = JsonUtils.fromJson(data, SummaryBean.class);
			
			CCApplication.getInstance().addSummary(summary);
			SummaryBean allsummary = CCApplication.getInstance().getSummary();
			if(allsummary != null){
				list = allsummary.getSummaryList();
			}
			listadapter = new SummaryListAdapter(this,allsummary);
			listView.setAdapter(listadapter);
			listadapter.notifyDataSetChanged();
			//如果没有消息那么显示没有消息的界面
			if(listadapter.isEmpty()){
				tvNoMessage.setVisibility(View.VISIBLE);
			}else{
				tvNoMessage.setVisibility(View.GONE);
			}
		}catch(Exception e){
			e.printStackTrace();   
		}
	}
	
	protected void parseMoreSummaryList(String data){
		try{
			System.out.println("getMoreSummaryList"+data);
			
			summary = JsonUtils.fromJson(data, SummaryBean.class);
			List<SummaryDetail> newSummary = 
					CCApplication.getInstance().modifySummary(summary.getSummaryList(), list);
			if(newSummary.size() <= 0) return;
			if(listadapter != null){
				listadapter.addItem(summary.getSummaryList());
				listadapter.notifyDataSetChanged();
			}
			//如果没有消息那么显示没有消息的界面
			if(listadapter.isEmpty()){
				tvNoMessage.setVisibility(View.VISIBLE);
			}else{
				tvNoMessage.setVisibility(View.GONE);
			}
			CCApplication.getInstance().addSummary(summary);
			SummaryBean allsummary = CCApplication.getInstance().getSummary();
			if(allsummary != null){
				list = allsummary.getSummaryList();
			}
		}catch(Exception e){
			e.printStackTrace();   
		}
	}
	protected void parseAttachment(String data){
		System.out.println("parseAttachment:"+data);
		AttachmentBean attach = JsonUtils.fromJson(data, AttachmentBean.class);
		
		list.get(pos).setAttachDetail(attach.getAttachmentList());
		gotoDetail(list.get(pos));
	}
	
	protected void deleteSummary(String summaryId){
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userId);
			json.put("schoolId", schoolId);
			json.put("summaryId", summaryId);
			queryPost(Constants.DELETE_SUMMARY,json);
			System.out.println("deleteSummary:"+json);
			action = ACTION_DEL_SUMMARY;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void gotoDetail(SummaryDetail detail){
		Intent intent = new Intent(); 
		Bundle bundle = new Bundle();
		
		bundle.putSerializable("detail", detail);
		intent.putExtras(bundle);
		//intent.putExtra("pos", pos);
		intent.setClass(SummaryActivity.this, SummaryDetailActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		switch(action){
		case ACTION_GET_SUMMARY_LIST:
			parseSummaryList(data);
			break;
		case ACTION_GET_ATTACHMENT_LIST:
			parseAttachment(data);
			break;
		case ACTION_DEL_SUMMARY:	
			System.out.println("data:"+data);
			NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
			if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
				list.remove(pos);	
				//listadapter.remove(tobeDelete);
				listadapter.notifyDataSetChanged();
				//如果没有消息那么显示没有消息的界面
				if(list.isEmpty()){
					tvNoMessage.setVisibility(View.VISIBLE);
				}else{
					tvNoMessage.setVisibility(View.GONE);
				}
				
				// 更新消息未读数
				(SummaryActivity.this).updateUnreadLabel();
			}
			break;
		}
	}
	

	@Override
	public void deleteItem() {
		// TODO Auto-generated method stub
		SummaryDetail item = list.get(pos);
		if(SummaryRightControll.hasDeleteAllSummaryRight()
				|| item!= null &&userId.equals(item.getCreatorId()+"")){
			deleteOrNot();
		}else{
			DataUtil.getToast("无权限删除！");
		}
	}
	
	private void doDelete(int pos){
		if(list != null&& pos < list.size() && list.get(pos) != null){
			SummaryDetail item = list.get(pos);
			if(item != null){
				deleteSummary(item.getSummaryId()+"");
			}
		}
	}
	private void deleteOrNot(){
		dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.customdialog, handler);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		dialog.setTitle("确定删除吗？");
		dialog.setContent("");
	}
	/**
	 * 控制CustomDialog按钮效果.
	 */
	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);

			switch (msg.what) {
			case CustomDialog.DIALOG_CANCEL:// 取消删除
				dialog.dismiss();
				break;
			case CustomDialog.DIALOG_SURE:// 确认删除
				doDelete(pos);
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	@Override
	public void onRequstComplete(String result) {
		// TODO Auto-generated method stub
		if(action == ACTION_GET_SUMMARY_LIST){
			parseSummaryList(result);
		}else{
			parseMoreSummaryList(result);
		}
	}

	@Override
	public void onRequstCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		if(resultCode == RESULT_OK){
			if(requestCode == PUBLISH_SUMMARY){
				getFirstPage();
			}
		}
	}
}
