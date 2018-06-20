package com.wcyc.zigui2.newapp.module.mailbox;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;



import com.wcyc.zigui2.chooseContact.ChooseDeptFragment;
import com.wcyc.zigui2.chooseContact.ChooseTeacherFragment;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.bean.EmailBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean.Role;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordBean;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordBean.DailyRecordDetail;
import com.wcyc.zigui2.newapp.module.mailbox.MasterMailItemFragment.GetAdapterDataListener;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.newapp.widget.RefreshListView.OnRefreshListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
//校长信箱主界面
public class SchoolMasterMailActivity extends BaseActivity 
	implements GetAdapterDataListener{
	private RefreshListView listView;
	private ImageButton add;
	private Button unreply,replyed;
	private ImageView back;
	private View backBtn;
	private List<MailInfo> unreplyList;
	private List<MailInfo> replyedList;
	private List<MailInfo> allList;
	private EmailBean mail,replyedMail;
	private MemberDetailBean member;
	private boolean isAdmin = false;//校长信箱管理员
	private static final String ADMIN = "headermailadmin";//校长信箱管理员权限代码
	private int curPage = 1;
	public static final int UNREPLY = 0,REPLYED = 1,ALL = 3;
	private Fragment fragment;
	private static final int COMPOSE_MAIL = 1;
	private int action;
	public static final int GET_UNREPLY_MAIL = 1;
	public static final int GET_REPLYED_MAIL = 2;
	private int button = UNREPLY;
	private boolean ret;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.school_master_activity_email);
		isAdmin = isAdmin();
		initView();
		initEvent();
		getFirstPage();
	}

	protected void getFirstPage(){
		Intent intent = getIntent();
		ParseIntent(intent);
		getEmailList(isAdmin,1,"0",GET_UNREPLY_MAIL);
		action = GET_UNREPLY_MAIL;
	}

	protected void onResume(){
		super.onResume();

	}
	
	private boolean isAdmin(){
		UserType user = CCApplication.getInstance().getPresentUser();
		member = CCApplication.getInstance().getMemberDetail();
		if(user != null){
			//家长不是管理员
			if((Constants.PARENT_STR_TYPE).equals(user.getUserType())){
				return false;
			}
		}
		if(member != null){
			List<Role> roleList = member.getRoleList();
			if(roleList != null){
				for(Role role:roleList){
					if(ADMIN.equals(role.getRoleCode())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void ParseIntent(Intent intent){
		Bundle bundle = intent.getExtras();
		System.out.println("SchoolMasterEmailActivity onNewIntent:"+bundle);
		if(bundle != null){
			int type = bundle.getInt("type");
			MailInfo data = (MailInfo) bundle.getSerializable("data");
			System.out.println("SchoolMasterEmailActivity onResume,type:"+type+" data:"+data);
			switch(type){
			case UNREPLY:
				unreplyList.remove(data);
				break;	
			case REPLYED:
				replyedList.add(data);
				break;
			}
			
			placeView(type);
		}
	}
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		ParseIntent(intent);
	}
	
	private void parseMail(){
		System.out.println("parseAllMail");
		List<MailInfo> list = mail.getMailInfoList();
		if(isAdmin){
			unreplyList = list;
		}else{
			allList = list;
		}
	}
	
	private void parseReplyedMail(){
		System.out.println("parseReplyedMail");
		replyedList = replyedMail.getMailInfoList();		
	}
	
	public void initView(){
		TextView titleText2 = (TextView) findViewById(R.id.title_text_2);
		titleText2.setVisibility(View.VISIBLE);
		titleText2.setText(R.string.title_activity_mail);
		Button title_imgbtn = (Button) findViewById(R.id.title_btn);
		title_imgbtn.setVisibility(View.GONE);
		back = (ImageView) findViewById(R.id.title_arrow_iv);
		backBtn = findViewById(R.id.title_back);
		add = (ImageButton)findViewById(R.id.title_imgbtn_add);
		
		unreply = (Button) findViewById(R.id.unreply);
		replyed = (Button) findViewById(R.id.replyed);
		
		if(isAdmin){
			unreply.setVisibility(View.VISIBLE);
			replyed.setVisibility(View.VISIBLE);
//			placeView(0);
		}else{
			add.setVisibility(View.VISIBLE);
		}		
	}
	
	private void initEvent(){
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SchoolMasterMailActivity.this,ComposeEmailToMasterActivity.class);
				intent.putExtra("isAdmin", isAdmin);
				startActivityForResult(intent,COMPOSE_MAIL);
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
		unreply.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				placeView(UNREPLY);
				button = UNREPLY;
			}
			
		});
		replyed.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				placeView(REPLYED);
				button = REPLYED;
			}
			
		});
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		System.out.println("requestCode:"+requestCode+" resultCode:"+resultCode+" data:"+data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
			case COMPOSE_MAIL:
				ret = data.getBooleanExtra("composeMail", true);
				if(ret == true){//发送校长信箱成功
					getEmailList(isAdmin,1,"0",GET_UNREPLY_MAIL);
				}
				break;
			}
		}
	}
		
	public void placeView(int index){
		fragment = getSupportFragmentManager().findFragmentByTag(index+"");
//		listView = (RefreshListView) fragment.getView().findViewById(R.id.email_list1);
//		listViewRefresh();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(fragment == null){
			fragment = MasterMailItemFragment.newInstance(index,isAdmin);
			SetButtonState(index);
		}
		ft.replace(R.id.maincontent, fragment, index+"");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		//ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}

	private void SetButtonState(int Type){
		unreply.setSelected(false);
		replyed.setSelected(false);

		switch(Type){
		case UNREPLY:
			unreply.setSelected(true);
			break;
		case REPLYED:
			replyed.setSelected(true);
			break;
		}
	}
	
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		System.out.println("parseEmailList:"+data);
		List<MailInfo> list = null;
		switch(action){
		case GET_UNREPLY_MAIL:
			EmailBean newMail = JsonUtils.fromJson(data, EmailBean.class);
			mail = addMasterMail(mail,newMail,false);
			parseMail();
			list = mail.getMailInfoList();
			if(!isAdmin){//非管理员所有mail都在一起
				placeView(ALL);
			}else{
				placeView(UNREPLY);
				getEmailList(isAdmin,1,"1",GET_REPLYED_MAIL);
			}
			break;
		case GET_REPLYED_MAIL:
			EmailBean newReplyedMail = JsonUtils.fromJson(data, EmailBean.class);
			replyedMail = addMasterMail(replyedMail,newReplyedMail,true);
			parseReplyedMail();
			if(button == REPLYED){
				placeView(REPLYED);
				list = replyedMail.getMailInfoList();
			}
			break;
		}
		
		if(fragment != null){
			//更新adapter数据
			MasterMailListAdapter adapter = ((MasterMailItemFragment)fragment).getAdapter();
			if(adapter != null){
				if(list != null) {
					adapter.setItem(list);
				}
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	
	@Override
	public List<MailInfo> GetAdapterData(int Type) {
		// TODO Auto-generated method stub
		System.out.println("SchoolMaster GetAdapterData:"+Type);
		switch(Type){
		case UNREPLY:
			return unreplyList;
		case REPLYED:
			return replyedList;
		case ALL:
			return allList;
		}
		return null;
	}
	
	@Override
	public EmailBean GetMail(){
		mail = getEmail(false);
		return mail;
	}
	
	@Override
	public EmailBean getReplyMail() {
		// TODO Auto-generated method stub
		replyedMail = getEmail(true);
		return replyedMail;
	}
	public void getEmailList(boolean isAdmin,int page,String isReply,int action){
		JSONObject json = new JSONObject();

		if(member != null){
			try {
				json.put("schoolId", user.getSchoolId());
				json.put("curPage", page);
				json.put("pageSize", Constants.defaultPageSize);

				if(user.getUserType().equals("3")){
					String studentId = user.getChildId();
					json.put("userId", studentId);
					json.put("userType","1");
				}else{
					json.put("userId", member.getUserId());
					json.put("userType",user.getUserType());
				}

				System.out.println("getEmailList:"+json);
				if(isAdmin){
					json.put("isReplyType", isReply);
					queryPost(Constants.ADMIN_URL,json);
				}else{
					queryPost(Constants.GET_SEND_MAIL_URL,json);
				}
				this.action = action;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private EmailBean addMasterMail(EmailBean orginEmail,EmailBean mail,boolean isRelied){
		if(mail == null) return null;
		if(orginEmail != null && orginEmail.getMailInfoList() != null){
			int pageNum = mail.getPageNum();
			int totalPage = mail.getTotalPageNum();
			int originPageNum = orginEmail.getPageNum();
			int originTotalPage = orginEmail.getTotalPageNum();
			System.out.println("pageNum:"+pageNum+" totalPage:"+totalPage+
					" originPageNum:"+originPageNum+"originTotalPage:"+originTotalPage);
			if(pageNum > originPageNum){//加载更多，插入列表后面
				List<MailInfo> allList = 
						modifyEmail(mail.getMailInfoList(),orginEmail.getMailInfoList());
				if(allList == null) return null;
				orginEmail.getMailInfoList().addAll(allList);
				orginEmail.setPageNum(pageNum);
				orginEmail.setPageSize(totalPage);
			}else{
				List<MailInfo> allList = new ArrayList<MailInfo>();
				List<MailInfo> list = mail.getMailInfoList();
				List<MailInfo> oldList = orginEmail.getMailInfoList(); 
				if(oldList != null){ 
					allList = modifyEmail(list,oldList);
					if(allList != null){
						allList.addAll(oldList);
						sort(allList);
					}
					mail.setMailInfoList(allList);
				}
				
				orginEmail = mail;	
			}
		}else{
			orginEmail = mail;
		}
		Gson gson = new Gson();
		String data = gson.toJson(orginEmail);
		if(isRelied){
			CCApplication.dbsp.putString("masterMail", data);
		}else{
			CCApplication.dbsp.putString("unpliedMasterMail", data);
		}
		return orginEmail;
	}

	private void sort(List<MailInfo> list){
		Collections.sort(list, new Comparator<MailInfo>() {
			@Override
			public int compare(MailInfo lhs, MailInfo rhs) {
				long time1 = 0;
				long time2 = 0;
				try {
					time1 = DataUtil.getLongDate(lhs.getPublishTime());
					time2 = DataUtil.getLongDate(lhs.getPublishTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (time1 == time2) {
					return 0;
				} else if (time1 < time2) {
					return 1;
				} else {
					return -1;
				}
			}
		});
	}
	private List<MailInfo> modifyEmail(List<MailInfo> list,List<MailInfo> oldList){
		boolean isExist = false;
		if(list == null) return null;
		List<MailInfo> allList = new ArrayList<MailInfo>();
		for(MailInfo item:list){
			if(oldList != null){
				for(MailInfo oldItem:oldList){
					if(item.getMailId().equals(oldItem.getMailId())){
						isExist = true;
						break;
					}
				}
			}
			if(isExist == false){
				allList.add(item);
			}else{
				isExist = false;
				continue;
			}
		}
		return allList;
	}
	
	private EmailBean getEmail(boolean isRelied){
		if(mail == null){
			String data;
			if(isRelied){
				data = CCApplication.dbsp.getString("masterMail");
			}else{
				data = CCApplication.dbsp.getString("unpliedMasterMail");
			}
			if(!DataUtil.isNull(data)){
				mail = JsonUtils.fromJson(data, EmailBean.class);
			}
		}
		return mail;
	}
}
