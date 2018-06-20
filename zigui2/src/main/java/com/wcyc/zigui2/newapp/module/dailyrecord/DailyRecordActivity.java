package com.wcyc.zigui2.newapp.module.dailyrecord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.EMChatManager;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;

import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.chat.SlideListView;
import com.wcyc.zigui2.chat.SlideListView.DelButtonClickListener;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;


import com.wcyc.zigui2.newapp.bean.AttachmentBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;

import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean.Role;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordBean.DailyRecordDetail;
import com.wcyc.zigui2.newapp.module.notice.NewNoticeBean;
import com.wcyc.zigui2.newapp.module.notice.NoticeDetail;
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
import android.content.IntentFilter;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
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

/**
 * 日志主界面
 */
public class DailyRecordActivity extends BaseActivity 
	implements OnLongClick, HttpRequestAsyncTaskListener {
//	private SlideListView listView;
	private RefreshListView listView;
	private ImageButton add;
	private ImageView back,delBtn;
	private View backBtn;

	private DailyRecordListAdapter listadapter;
	private TextView tvNoMessage;
	
	private static final int ACTION_GET_DAILYRECORD_LIST = 1;
	private static final int ACTION_GET_ATTACHMENT_LIST = 2;
	private static final int ACTION_DEL_DAILYRECORD = 3;
	private static final int ACTION_LOAD_MORE = 4;
	private NewMemberBean member;
	private DailyRecordBean dailyRecord;
	private MemberDetailBean userDetail;
	private List<DailyRecordDetail> list;
	private int pos;
	private String userId,schoolId,noticeId;
	private int curPage = 1;
	private static final String CLASS_AMDIN = "classteacher";//班主任
	private CustomDialog dialog;
	private boolean isAdmin = false;
	private long begin,stop;
	private static final int PUBLISH_DAILYRECORD = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		setContentView(R.layout.activity_dailyrecord);
		initView();
		initEvent();
		getFirstPage();
	}
	
	protected void onResume(){
		super.onResume();
	}
	private void getFirstPage(){
		curPage = 1;
		CCApplication.getInstance().setDailyRecord(null);
		getDailyRecordList(curPage);
	}
	
	private void initView(){
		TextView titleText2 = (TextView) findViewById(R.id.title_text_2);
		titleText2.setVisibility(View.VISIBLE);
		titleText2.setText(R.string.dailyrecord);
		Button title_imgbtn = (Button) findViewById(R.id.title_btn);
		title_imgbtn.setVisibility(View.GONE);
		back = (ImageView) findViewById(R.id.title_arrow_iv);
		backBtn = findViewById(R.id.title_back);
		add = (ImageButton)findViewById(R.id.title_imgbtn_add);
	//	String type = CCApplication.getInstance().getPresentUser().getUserType();
		if(CCApplication.getInstance().isSchoolAdmin()){//学校管理员，校级领导有权限发日志
			isAdmin = true;
			add.setVisibility(View.VISIBLE);
		}
		if(CCApplication.getInstance().isDeptAdmin()){//部门负责人有权限发日志
			add.setVisibility(View.VISIBLE);
		}
		listView = (RefreshListView) findViewById(R.id.notify_list);
		
		tvNoMessage = (TextView) findViewById(R.id.tv_no_message);
	}
	
	private void initEvent(){
		dailyRecord = CCApplication.getInstance().getDailyRecord();
		if(dailyRecord != null){
			list = dailyRecord.getDailyList();
		}
		//发布日志按钮
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DailyRecordActivity.this,
						PublishDailyRecordActivity.class);
				startActivityForResult(intent,PUBLISH_DAILYRECORD);
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
				//bug?
				if(list != null && pos < list.size() && list.get(pos) != null){
					System.out.println("onclick list:"+arg1.getId()+" arg2:"+arg2+" arg3:"+arg3);
					DailyRecordDetail item = list.get(pos);
					item.setIsRead("1");
					listadapter.notifyDataSetChanged();
					String num = item.getDailyBrowseNum();
					if(num != null){
						int add = Integer.parseInt(num)+1;
						item.setDailyBrowseNum(String.valueOf(add));
					}
					CCApplication.getInstance().setDailyRecord(dailyRecord);
					gotoDetail(list.get(pos));
				}
			}
			
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				pos = (int) arg3;

				DeleteItemPop pop = new DeleteItemPop(DailyRecordActivity.this,DailyRecordActivity.this);
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
					list = dailyRecord.getDailyList();
//					getNoticeList(curPage);
					DataUtil.getToast("已经是首页了");
				}else{
					curPage = 1;
					getDailyRecordList(curPage);
				}
				listView.hideHeaderView();
			}

			@Override
			public void onLoadingMore() {//上拉加载更多
				// TODO Auto-generated method stub
				System.out.println("onLoadingMore curPage:"+curPage);
				
				if(curPage < dailyRecord.getTotalPageNum()&& curPage >= dailyRecord.getPageNum()){
					getDailyRecordList(++curPage);
				}else{
					DailyRecordBean dailyRecord = CCApplication.getInstance().getDailyRecord();
					list = dailyRecord.getDailyList();
				}
				if(curPage >= dailyRecord.getTotalPageNum()){
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
	
	protected void getDailyRecordList(int curPage){
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
					action = ACTION_GET_DAILYRECORD_LIST;
				}
				begin = System.currentTimeMillis();
				new HttpRequestAsyncTask(json, this, this).execute(Constants.DAILY_RECORD_LIST);
				System.out.println("getDailyRecordList:"+json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void parseDailyRecordList(String data){
		try{
			stop = System.currentTimeMillis();
			System.out.println("getDailyRecordList:"+data+"time:"+(stop-begin));
			
			dailyRecord = JsonUtils.fromJson(data, DailyRecordBean.class);
		
			CCApplication.getInstance().addDailyRecord(dailyRecord);
			DailyRecordBean allnotice = CCApplication.getInstance().getDailyRecord();
			if(allnotice != null){
				list = allnotice.getDailyList();
			}
			listadapter = new DailyRecordListAdapter(this,allnotice);
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
	
	protected void parseMoreDailyRecordList(String data){
		try{
			stop = System.currentTimeMillis();
			System.out.println("getMoreDailyRecordList:"+data+"time:"+(stop-begin));
			
			dailyRecord = JsonUtils.fromJson(data, DailyRecordBean.class);
			List<DailyRecordDetail> newDailyRecord = 
					CCApplication.getInstance().modifyDailyRecord(dailyRecord.getDailyList(),list);
			//是否是重复数据
			if(newDailyRecord.size() <= 0)return;
			if(listadapter != null){
				listadapter.addItem(dailyRecord.getDailyList());
//				listView.setAdapter(listadapter);
				listadapter.notifyDataSetChanged();
			}
			//如果没有消息那么显示没有消息的界面
			if(listadapter.isEmpty()){
				tvNoMessage.setVisibility(View.VISIBLE);
			}else{
				tvNoMessage.setVisibility(View.GONE);
			}
			CCApplication.getInstance().addDailyRecord(dailyRecord);
			DailyRecordBean allnotice = CCApplication.getInstance().getDailyRecord();
			if(allnotice != null){
				list = allnotice.getDailyList();
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
	
	protected void deleteDailyRecord(String dailyId){
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userId);
			json.put("schoolId", schoolId);
			json.put("dailyId", dailyId);
			queryPost(Constants.DELETE_LOG,json);
			System.out.println("deleteDailyRecord:"+json);
			action = ACTION_DEL_DAILYRECORD;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void gotoDetail(DailyRecordDetail detail){
		Intent intent = new Intent(); 
		Bundle bundle = new Bundle();
		
		bundle.putSerializable("detail", detail);
		intent.putExtras(bundle);

		intent.setClass(DailyRecordActivity.this, DailyRecordDetailActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		switch(action){
		case ACTION_GET_DAILYRECORD_LIST:
			parseDailyRecordList(data);
			break;
		case ACTION_GET_ATTACHMENT_LIST:
			parseAttachment(data);
			break;
		case ACTION_DEL_DAILYRECORD:	
			System.out.println("data:"+data);
			NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
			if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
				list.remove(pos);	
				//listadapter.remove(tobeDelete);
				listadapter.notifyDataSetChanged();
				//如果没有日志那么显示没有日志的界面
				if(list.isEmpty()){
					tvNoMessage.setVisibility(View.VISIBLE);
				}else{
					tvNoMessage.setVisibility(View.GONE);
				}
				// 更新日志未读数
				(DailyRecordActivity.this).updateUnreadLabel();
			}
			break;
		}
	}


	@Override
	public void deleteItem() {
		// TODO Auto-generated method stub
		DailyRecordDetail item = list.get(pos);
		if(DailyRecordRightControll.hasDeleteAllDailyRecordRight()
				|| item != null && userId.equals(item.getCreatorId()+"")){
			deleteOrNot();
		}else{
			DataUtil.getToast("无权限删除！");
		}
	}
	
	public void doDelete(int pos){
		if(list != null && pos < list.size() && list.get(pos) != null){
			DailyRecordDetail item = list.get(pos);
			if(item != null){
				deleteDailyRecord(item.getDailyId()+"");
			}
		}
	}
	private void deleteOrNot(){
		dialog = new CustomDialog(this, R.style.mystyle,
				R.layout.customdialog, handler);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.setTitle("确定删除吗？");
		dialog.setContent("");
	}
	/**
	 * 控制CustomDialog按钮事件.
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
		if(ACTION_LOAD_MORE == action){
			parseMoreDailyRecordList(result);
		}else{
			parseDailyRecordList(result);
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
			if(requestCode == PUBLISH_DAILYRECORD){
				getFirstPage();
			}
		}
	}
}
