package com.wcyc.zigui2.newapp.module.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;
import com.wcyc.zigui2.chooseContact.ChooseDeptFragment;
import com.wcyc.zigui2.chooseContact.ChooseTeacherFragment;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.EmailBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.email.DraftFragment;
import com.wcyc.zigui2.newapp.module.email.EmailItemFragment;
import com.wcyc.zigui2.newapp.module.email.InboxFragment;
import com.wcyc.zigui2.newapp.module.email.OutboxFragment;
import com.wcyc.zigui2.newapp.module.email.RecycleFragment;
import com.wcyc.zigui2.newapp.module.email.EmailItemFragment.GetAdapterDataListener;
import com.wcyc.zigui2.newapp.module.mailbox.MailInfo;
import com.wcyc.zigui2.newapp.module.notice.NewNoticeBean;
import com.wcyc.zigui2.newapp.module.notice.NotifyListAdapter;
import com.wcyc.zigui2.newapp.widget.ChooseRolesList;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class EmailActivity extends BaseActivity
	implements GetAdapterDataListener, HttpRequestAsyncTaskListener{

	private ImageButton add;
	private ImageView back;
	private View backBtn;
	private List<NewMailInfo> inboxList;
	private List<NewMailInfo> outboxList;
	private List<NewMailInfo> draftList;
	private List<NewMailInfo> recycleList;
	private NewEmailBean email;
	private List<NewMailInfo> list;
	private RefreshListView listView;
	private EmailListAdapter inboxAdapter,outboxAdapter,draftAdapter,recycleAdapter,listAdapter;
	private Fragment fragment;
	private TabHost mTabHost;
	public static final int INBOX = 0,OUTBOX = 1,DRAFT = 2,RECYCLE = 3;
	public static final int DELETE = 0,RECOVER = 1;
	private static final int  ACTION_GET_EMAIL_LIST = 1;
	private static final int COMPOSE_EMAIL = 100;
	private int type ;
	boolean isParent = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);
		initView();
		initEvent();
		initData();
		type = INBOX;
		getFirstPage();
	}

	public void initView(){
		TextView titleText2 = (TextView) findViewById(R.id.title_text_2);
		titleText2.setVisibility(View.VISIBLE);
		titleText2.setText(R.string.title_activity_email);
		Button title_imgbtn = (Button) findViewById(R.id.title_btn);
		title_imgbtn.setVisibility(View.GONE);
		back = (ImageView) findViewById(R.id.title_arrow_iv);
		backBtn = findViewById(R.id.title_back);
		add = (ImageButton)findViewById(R.id.title_imgbtn_add);
		add.setVisibility(View.VISIBLE);

		placeView(INBOX);

		mTabHost = (TabHost) findViewById(R.id.tabHost);
		mTabHost.setup();
		mTabHost.addTab(mTabHost.newTabSpec("inbox")
		             .setContent(R.id.inbox)
		             .setIndicator("收件箱",null));
		mTabHost.addTab(mTabHost.newTabSpec("outbox")
			.setContent(R.id.outbox)
			.setIndicator("发件箱",null));
		mTabHost.addTab(mTabHost.newTabSpec("draft")
				.setContent(R.id.draft)
				.setIndicator("草稿箱",null));
		mTabHost.addTab(mTabHost.newTabSpec("recycle")
				.setContent(R.id.recycle)
				.setIndicator("回收站",null));
		mTabHost.setCurrentTab(0);
		setTabTextStyle(0);

		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				System.out.println(tabId);
				if(tabId.equals("inbox")){
					type = INBOX;
					setTabTextStyle(0);
				}else if(tabId.equals("outbox")){
					type = OUTBOX;
					setTabTextStyle(1);
				}else if(tabId.equals("draft")){
					type = DRAFT;
					setTabTextStyle(2);
				}else if(tabId.equals("recycle")){
					type = RECYCLE;
					setTabTextStyle(3);
				}

				getEmailList(1,type);
				placeView(type);
			}
		});
	}

	private void setTabTextStyle(int index){
		for(int i = 0 ; i < 4; i++) {
			TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			tv.setTextColor(getResources().getColor(R.color.font_color));
			tv.setTextSize(16);
			tv.getPaint().setFakeBoldText(false);
		}
		TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(index).findViewById(android.R.id.title);
		tv.setTextColor(getResources().getColor(R.color.font_darkblue));
	}

	private void getFirstPage(){
		System.out.println("mailActivity onResume");
		CCApplication.getInstance().setEmail(null,"inbox");
		CCApplication.getInstance().setEmail(null,"outbox");
		CCApplication.getInstance().setEmail(null,"draft");
		CCApplication.getInstance().setEmail(null,"recycle");
		type = mTabHost.getCurrentTab();
		getEmailList(1,type);
	}
	protected void onResume(){
		super.onResume();
		EmailListAdapter adapter = getAdapter();
		if(adapter != null) {
			adapter.notifyDataSetChanged();
		}
//		getFirstPage();
	}
	
	private List<NewMailInfo> addItem(List<NewMailInfo> list,NewMailInfo add,int pos){
		int i = 0;
		List<NewMailInfo> ret = new ArrayList<NewMailInfo>();
		if(list != null && add != null){
			for(NewMailInfo item:list){
				i++;
				ret.add(item);
				if(i == pos){
					ret.add(add);
				}
			}
		}
		return ret;
	}
	
	private void removeItem(List<NewMailInfo> list,NewMailInfo remove){
		if(list != null && remove != null){
			for(NewMailInfo item:list){
				String id = item.getId();
				if(id != null && id.equals(remove.getId())){
					list.remove(item);
					break;
				}
			}
		}
	}
	
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		Bundle bundle = intent.getExtras();
		System.out.println("EmailActivity onNewIntent:"+bundle);
		if(bundle != null){
			int type = bundle.getInt("EmailType");
			this.type = type;
			placeView(type);
			mTabHost.setCurrentTab(type);
			setTabTextStyle(type);
			getFirstPage();
		}
	}
	
	private void initData(){
		isParent = CCApplication.getInstance().isCurUserParent();
		System.out.println("initData");
		inboxList = new ArrayList<NewMailInfo>();
		outboxList = new ArrayList<NewMailInfo>();
		draftList = new ArrayList<NewMailInfo>();
		recycleList = new ArrayList<NewMailInfo>();
	}
		
	public void getEmailList(int curPage,int type){
		JSONObject json = new JSONObject();
		UserType user = CCApplication.getInstance().getPresentUser(); 

		try {
			String schoolId = user.getSchoolId();
			if(Constants.PARENT_STR_TYPE.equals(user.getUserType())){
				json.put("childId",user.getChildId());
			}
			json.put("userID", user.getUserId());
			json.put("schoolId", schoolId);
			json.put("status", type);
			json.put("curPage", curPage);
			json.put("pageSize", Constants.defaultPageSize);
			new HttpRequestAsyncTask(json,this,this)
				.execute(Constants.EMAIL_LIST);
			action = type;
			System.out.println("getEmailList:"+json + " action:"+action);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void initEvent(){
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putLong("operation", EmailDetailActivity.COMPOSE_NEW);
				intent.putExtras(bundle);
				if(isParent){
					intent.setClass(EmailActivity.this,ComposeEmailByParentActivity.class);
				}else{
					intent.setClass(EmailActivity.this, ComposeEmailActivity.class);
				}
				startActivityForResult(intent,COMPOSE_EMAIL);
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
	}
	
	public String getType(int type){
		String emailType = "";
		switch(type){
		case EmailActivity.INBOX:
			emailType = "inbox";
			break;
		case EmailActivity.OUTBOX:
			emailType = "outbox";
			break;
		case EmailActivity.DRAFT:
			emailType = "draft";
			break;
		case EmailActivity.RECYCLE:
			emailType = "recycle";
			break;
		}
		return emailType;
	}
	
	public void placeView(int index){
		fragment = getSupportFragmentManager().findFragmentByTag(index+"");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(fragment == null){
			fragment = EmailItemFragment.newInstance(index);
		}
		ft.replace(android.R.id.tabcontent, fragment, index+"");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		//ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}

	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		switch(action){
		case ACTION_GET_EMAIL_LIST:
			email = JsonUtils.fromJson(data, NewEmailBean.class);
			parseEmailList(email);
			break;
		}
	}
	
	private void parseEmailList(NewEmailBean email){
		try{
			String type = getType(this.type);
			CCApplication.getInstance().addEmail(email,type);
//			NewEmailBean allemail = CCApplication.getInstance().getEmail(type);
//			if(allemail != null){
//				list = allemail.getMailInfoList();
//			}
			listAdapter = null;
			switch(action){
			case INBOX:
				inboxList = email.getMailInfoList();
				listAdapter = inboxAdapter = new EmailListAdapter(this,inboxList,action);
				break;
			case OUTBOX:
				outboxList = email.getMailInfoList();
				listAdapter = outboxAdapter = new EmailListAdapter(this,outboxList,action);
				break;
			case DRAFT:
				draftList = email.getMailInfoList();
				listAdapter = draftAdapter = new EmailListAdapter(this,draftList,action);
				break;
			case RECYCLE:
				recycleList = email.getMailInfoList();
				listAdapter = recycleAdapter = new EmailListAdapter(this,recycleList,action);
				break;
			}
//			placeView(action);
			listView = (RefreshListView) ((EmailItemFragment)fragment).getListView();
			((EmailItemFragment) fragment).getListAdapter().setItem(email.getMailInfoList());
			listView.setAdapter(listAdapter);
			listAdapter.notifyDataSetChanged();
			TextView noMessage = ((EmailItemFragment)fragment).getNoMessageView();
			if(listAdapter.isEmpty()){
				noMessage.setVisibility(View.VISIBLE);
			}else{
				noMessage.setVisibility(View.GONE);
			}
		}catch(Exception e){
			e.printStackTrace();   
		}
	}

	private void parseMoreEmailList(NewEmailBean email){
		try{
			String type = getType(this.type);
			listAdapter = null;
			switch(action){
				case INBOX:
					inboxList = email.getMailInfoList();
					inboxAdapter.addItem(inboxList);
					listAdapter = inboxAdapter;
					break;
				case OUTBOX:
					outboxList = email.getMailInfoList();
					outboxAdapter.addItem(outboxList);
					listAdapter = outboxAdapter;
					break;
				case DRAFT:
					draftList = email.getMailInfoList();
					draftAdapter.addItem(draftList);
					listAdapter = draftAdapter;
					break;
				case RECYCLE:
					recycleList = email.getMailInfoList();
					recycleAdapter.addItem(recycleList);
					listAdapter = recycleAdapter;
					break;
			}
//			listView = (RefreshListView) ((EmailItemFragment)fragment).getListView();
//			listView.setAdapter(listAdapter);
			listAdapter.notifyDataSetChanged();
			TextView noMessage = ((EmailItemFragment)fragment).getNoMessageView();
			if(listAdapter.isEmpty()){
				noMessage.setVisibility(View.VISIBLE);
			}else{
				noMessage.setVisibility(View.GONE);
			}
			CCApplication.getInstance().addEmail(email,type);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public List<NewMailInfo> GetAdapterData(int Type) {
		// TODO Auto-generated method stub
		System.out.println("GetAdapterData:"+Type);
		switch(Type){
		case INBOX:
			return inboxList;
		case OUTBOX:
			return outboxList;
		case DRAFT:
			return draftList;
		case RECYCLE:
			return recycleList;
		}
		return null;

	}
	@Override
	public EmailListAdapter getAdapter(){
		return listAdapter;
	}

	public NewEmailBean getEmail(){
		return email;
	}

	@Override
	public void onRequstComplete(String result) {
		// TODO Auto-generated method stub
		email = JsonUtils.fromJson(result, NewEmailBean.class);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(email.getPageNum() > 1){
					parseMoreEmailList(email);
				}else {
					parseEmailList(email);
				}
			}
		});
	}

	@Override
	public void onRequstCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode,resultCode,data);
		if(resultCode == RESULT_OK){
			if(requestCode == COMPOSE_EMAIL){
				getFirstPage();
			}
		}
	}
}
