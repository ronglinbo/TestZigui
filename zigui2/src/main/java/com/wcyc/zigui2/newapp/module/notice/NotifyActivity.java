package com.wcyc.zigui2.newapp.module.notice;

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
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;

import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean.Role;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordActivity;
import com.wcyc.zigui2.newapp.widget.DeleteItemPop;
import com.wcyc.zigui2.newapp.widget.DeleteItemPop.OnLongClick;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.newapp.widget.RefreshListView.OnRefreshListener;
//import com.wcyc.zigui2.newapp.widget.SlideListView.DelButtonClickListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpUtils;
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

//通知主界面
public class NotifyActivity extends BaseActivity
		implements OnLongClick, HttpRequestAsyncTaskListener {

	private RefreshListView listView;
	private ImageButton add;
	private ImageView back,delBtn;
	private View backBtn;
	//	private List<NotifyBean> list;
	private NotifyListAdapter listadapter;
	private TextView tvNoMessage;

	private static final int ACTION_GET_NOTICE_LIST = 1;
	private static final int ACTION_GET_ATTACHMENT_LIST = 2;
	private static final int ACTION_DEL_NOTICE = 3;
	private static final int ACTION_LOAD_MORE = 4;
	private NewMemberBean member;
	private NewNoticeBean notice;
	private MemberDetailBean userDetail;
	private List<NoticeDetail> list;
	private int pos;
	private String userId,schoolId,noticeId;
	private int curPage = 1;
	private CustomDialog dialog;
	private static final int PUBLISH_NOTICE = 100;

	private static final String CLASS_AMDIN = "classteacher";//班主任
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		setContentView(R.layout.activity_notify);
		initView();
		initEvent();
		getFirstPage();
	}

	private void getFirstPage(){
		curPage = 1;
		CCApplication.getInstance().setNotice((NewNoticeBean)null);
		getNoticeList(curPage);
	}

	protected void onResume(){
		super.onResume();
	}

	private void initView(){
		TextView titleText2 = (TextView) findViewById(R.id.title_text_2);
		titleText2.setVisibility(View.VISIBLE);
		titleText2.setText(R.string.notify);
		Button title_imgbtn = (Button) findViewById(R.id.title_btn);
		title_imgbtn.setVisibility(View.GONE);
		back = (ImageView) findViewById(R.id.title_arrow_iv);
		backBtn = findViewById(R.id.title_back);
		add = (ImageButton)findViewById(R.id.title_imgbtn_add);
		String type = CCApplication.getInstance().getPresentUser().getUserType();
		//学校管理员、校级领导、部门负责人、年级组长,班主任老师才有权限发通知
		if(NoticeRightControll.hasPublishNoticeRight()
				||CCApplication.getInstance().isCurUserClassAdmin()){
			add.setVisibility(View.VISIBLE);
		}
		listView = (RefreshListView) findViewById(R.id.notify_list);

		tvNoMessage = (TextView) findViewById(R.id.tv_no_message);
	}

	private void initEvent(){
		notice = CCApplication.getInstance().getNotice();
		if(notice != null){
			list = notice.getNoticeList();
		}
		add.setOnClickListener(new OnClickListener(){       //编辑通知按钮点击事件

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NotifyActivity.this,PublishNotifyActivity.class);
				startActivityForResult(intent,PUBLISH_NOTICE);
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
		listView.setOnItemClickListener(new OnItemClickListener(){      //通知条目点击事件

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				pos = arg2;
				System.out.println("onclick list:"+arg1.getId()+" arg2:"+arg2+" arg3:"+arg3);
//				if(list != null && pos < list.size() && list.get(pos) != null){

//					DataUtil.getAttachmentList(NotifyActivity.this,
//							list.get(pos).getNoticeId()+"",Constants.NOTICE,ACTION_GET_ATTACHMENT_LIST);

				NoticeDetail item = (NoticeDetail)arg0.getAdapter().getItem(pos);
				if(item != null) {
					item.setRead("1");
					int num = item.getNoticeBrowseNum() + 1;
					item.setNoticeBrowseNum(num);
					gotoDetail(item);
				}
//					CCApplication.getInstance().setNotice(notice);
				listadapter.notifyDataSetChanged();
//				}
			}

		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
										   int arg2, long arg3) {
				// TODO Auto-generated method stub
				pos = (int) arg3;

				DeleteItemPop pop = new DeleteItemPop(NotifyActivity.this,NotifyActivity.this);
				pop.delete.setText("撤回");
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
					list = notice.getNoticeList();
//					getNoticeList(curPage);
					DataUtil.getToast("已经是首页了");
				}else{
					curPage = 1;
					getNoticeList(curPage);
				}
				listView.hideHeaderView();
			}

			@Override
			public void onLoadingMore() {//上拉加载更多
				// TODO Auto-generated method stub
				System.out.println("onLoadingMore curPage:"+curPage);
				if(notice != null){
					if(curPage < notice.getTotalPageNum()&& curPage >= notice.getPageNum()){
						getNoticeList(++curPage);
					}else{
						//					list = notice.getNoticeList();
						NewNoticeBean notice = CCApplication.getInstance().getNotice();
						if(notice != null) {
							list = notice.getNoticeList();
						}
					}
					if(curPage >= notice.getTotalPageNum()){
						DataUtil.getToast("没有更多数据了");
					}
				}
				listView.hideFooterView();
			}

		});
	}

	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
				case CustomDialog.DIALOG_CANCEL:// 取消
					dialog.dismiss();
					break;
				case CustomDialog.DIALOG_SURE:// 确认
					doDelete(pos);
					dialog.dismiss();
					break;
				default:
					break;
			}
		}
	};
	protected void updateUnreadLabel() {
		// TODO Auto-generated method stub

	}

	private void initData(){
		member = CCApplication.getInstance().getMemberInfo();
	}

	protected void getNoticeList(int curPage){
		JSONObject json = new JSONObject();
		user = CCApplication.getInstance().getPresentUser();
		userDetail = CCApplication.getInstance().getMemberDetail();
		if(userDetail != null){
			try {
				userId = user.getUserId();
				schoolId = user.getSchoolId();
				json.put("userId", userId);
				json.put("schoolId", schoolId);
				json.put("userType",user.getUserType());
				if(CCApplication.getInstance().isCurUserParent()){
					json.put("childId", user.getChildId());
				}
				String role = getMaxRoleRight(userDetail.getRoleList());
				json.put("role",role);
				json.put("curPage", curPage);
				json.put("pageSize", Constants.defaultPageSize);
				if(curPage > 1){
					action = ACTION_LOAD_MORE;
				}else{
					action = ACTION_GET_NOTICE_LIST;
				}
				System.out.println("getNoticeList:"+json);
				new HttpRequestAsyncTask(json,this,this).execute(Constants.GET_NOTICE_URL);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//获取查看通知最大的角色权限
	private String getMaxRoleRight(List<Role> list){
		String roleCode;
		if(hasRoleRight(list,roleCode = "schooladmin")){
			return roleCode;
		}else if(hasRoleRight(list,roleCode = "schoolleader")){
			return roleCode;
		}else if(hasRoleRight(list,roleCode = "departmentcharge")){
			return roleCode;
		}else if(hasRoleRight(list,roleCode = "gradeleader")){
			return roleCode;
		}else if(hasRoleRight(list,roleCode = "classteacher")){
			return roleCode;
		}
		return roleCode;
	}

	private boolean hasRoleRight(List<Role> list,String roleCode){
		if(list != null){
			for(Role item:list){
				if(roleCode.equals(item.getRoleCode())){
					return true;
				}
			}
		}
		return false;
	}

	protected void parseNoticeList(String data){
		try{
			System.out.println("getNoticeList"+data);
//			NewNoticeBean notice = null;
			notice = JsonUtils.fromJson(data, NewNoticeBean.class);

			CCApplication.getInstance().addNotice(notice);
			NewNoticeBean allnotice = CCApplication.getInstance().getNotice();
			if(allnotice != null){
				list = allnotice.getNoticeList();
			}
			listadapter = new NotifyListAdapter(this,allnotice);
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

	protected void parseMoreNoticeList(String data){
		try{
			System.out.println("getMoreNoticeList"+data);
			notice = JsonUtils.fromJson(data, NewNoticeBean.class);

			List<NoticeDetail> newNotice =
					CCApplication.getInstance().modifyNotice(notice.getNoticeList(),list);
			if(newNotice.size() <= 0) return;
			if(listadapter != null){
				listadapter.addItem(newNotice);
				listadapter.notifyDataSetChanged();
				//如果没有消息那么显示没有消息的界面
				if(listadapter.isEmpty()){
					tvNoMessage.setVisibility(View.VISIBLE);
				}else{
					tvNoMessage.setVisibility(View.GONE);
				}
			}

			CCApplication.getInstance().addNotice(notice);
			NewNoticeBean allnotice = CCApplication.getInstance().getNotice();
			if(allnotice != null){
				list = allnotice.getNoticeList();
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

	protected void deleteNotice(String noticeId){
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userId);
			json.put("schoolId", schoolId);
			json.put("noticeId", noticeId);
			queryPost(Constants.DELETE_NOTICE,json);
			System.out.println("deleteNotice:"+json);
			action = ACTION_DEL_NOTICE;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void gotoDetail(NoticeDetail detail){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();

		bundle.putSerializable("detail", detail);
		intent.putExtras(bundle);
		//intent.putExtra("pos", pos);
		intent.setClass(NotifyActivity.this, NotifyDetailActivity.class);
		startActivity(intent);
	}

	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		switch(action){
			case ACTION_GET_NOTICE_LIST:
				parseNoticeList(data);
				break;
			case ACTION_GET_ATTACHMENT_LIST:
				parseAttachment(data);
				break;
			case ACTION_DEL_NOTICE:
				System.out.println("data:"+data);
				break;
		}
	}
	private boolean hasDeleteRight(){
		if(list != null&& pos < list.size() && list.get(pos) != null) {
			NoticeDetail item = list.get(pos);
			if (NoticeRightControll.hasDeleteAllNoticeRight()
					|| userId.equals(item.getCreatorId())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private void doDelete(int pos){
		if(list != null&& pos < list.size() && list.get(pos) != null){
			deleteNotice(list.get(pos).getNoticeId()+"");
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
			(NotifyActivity.this).updateUnreadLabel();
		}
	}

	@Override
	public void deleteItem() {
		// TODO Auto-generated method stub
		boolean ret = hasDeleteRight();
		if(ret) {
			showDeleteConfirmDialog();
		}else{
			DataUtil.getToast("你没有撤回该通知的权限！");
		}
	}

	public void showDeleteConfirmDialog(){
		dialog = new CustomDialog(NotifyActivity.this,R.style.mystyle,
				R.layout.customdialog, handler);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.setTitle("通知撤回");
		dialog.setContent("通知撤回后其他人无法查看，确定撤回吗?");
	}

	@Override
	public void onRequstComplete(String result) {
		// TODO Auto-generated method stub
		if(action == ACTION_LOAD_MORE){
			parseMoreNoticeList(result);
		}else{
			parseNoticeList(result);
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
			if(requestCode == PUBLISH_NOTICE){
				getFirstPage();
			}
		}
	}

}
