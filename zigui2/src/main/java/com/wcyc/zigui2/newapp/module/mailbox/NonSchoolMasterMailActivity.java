package com.wcyc.zigui2.newapp.module.mailbox;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;



import com.wcyc.zigui2.chooseContact.ChooseDeptFragment;
import com.wcyc.zigui2.chooseContact.ChooseTeacherFragment;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.adapter.AttachmentListAdapter;
import com.wcyc.zigui2.newapp.bean.EmailBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean.Role;
import com.wcyc.zigui2.newapp.module.mailbox.MasterMailItemFragment.GetAdapterDataListener;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.newapp.widget.RefreshListView.OnRefreshListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.JsonUtils;

import android.app.Activity;
import android.content.Intent;
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
//非校长信箱管理员主界面
public class NonSchoolMasterMailActivity extends BaseActivity 
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
//	private boolean isAdmin = false;//校长信箱管理员
	private static final String ADMIN = "headermailadmin";//校长信箱管理员权限代码
	private int curPage = 1;
	public static final int UNREPLY = 0,REPLYED = 1,ALL = 3;
	private Fragment fragment;
	private static final int COMPOSE_MAIL = 1;
	private int action;
	public static final int GET_UNREPLY_MAIL = 1;
	public static final int GET_REPLYED_MAIL = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.school_master_activity_email);
		initView();
		initEvent();
		getFirstPage();
	}

	private void getFirstPage(){
		getEmailList(1,GET_UNREPLY_MAIL);
		action = GET_UNREPLY_MAIL;
	}

	protected void onResume(){
		super.onResume();

	}

	
	private void ParseIntent(Intent intent){
		Bundle bundle = intent.getExtras();
		System.out.println("SchoolMasterEmailActivity onNewIntent:"+bundle);
		if(bundle != null){
			int type = bundle.getInt("addMail");
			EmailBean data = (EmailBean) bundle.getSerializable("addItem");
			System.out.println("SchoolMasterEmailActivity onResume,type:"+type+" data:"+data);
			switch(type){
			case UNREPLY:
				unreplyList.remove(data);

				break;	
			case REPLYED:
				replyedList.remove(data);
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
		allList = list;
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
		
		add.setVisibility(View.VISIBLE);
	}
	
	private void initEvent(){
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NonSchoolMasterMailActivity.this,ComposeEmailToMasterActivity.class);
				intent.putExtra("isAdmin", false);
				startActivityForResult(intent,COMPOSE_MAIL);
//				startActivity(intent);
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
			}
			
		});
		replyed.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				placeView(REPLYED);
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
				boolean ret = data.getBooleanExtra("composeMail", true);
				if(ret == true){//发送校长信箱成功
					getEmailList(1,GET_UNREPLY_MAIL);
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
//		if(fragment == null){
			fragment = MasterMailItemFragment.newInstance(index,false);
			SetButtonState(index);
//		}
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
		switch(action){
		case GET_UNREPLY_MAIL:
			mail = JsonUtils.fromJson(data, EmailBean.class);
			parseMail();
			placeView(ALL);
			break;
		}
		if(fragment != null){
			MasterMailListAdapter adapter = ((MasterMailItemFragment)fragment).getAdapter();
			if(adapter != null){
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	
	@Override
	public List<MailInfo> GetAdapterData(int Type) {
		// TODO Auto-generated method stub
		System.out.println("GetAdapterData:"+Type);
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
		return mail;
	}
	
	public void getEmailList(int page,int action){
		JSONObject json = new JSONObject();

		if(member != null){
			try {
				json.put("userId", member.getUserId());
				json.put("schoolId", user.getSchoolId());
				json.put("userType",user.getUserType());
				json.put("curPage", page);
				json.put("pageSize", Constants.defaultPageSize);
				System.out.println("getEmailList:"+json);
				
				queryPost(Constants.GET_SEND_MAIL_URL,json);
				
				this.action = action;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public EmailBean getReplyMail() {
		// TODO Auto-generated method stub
		return null;
	}
}
