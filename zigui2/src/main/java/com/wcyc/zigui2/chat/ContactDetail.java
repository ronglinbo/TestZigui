/*
* 文 件 名:ContactDetail.java
* 创 建 人： 姜韵雯
* 日    期： 2014-10-13 
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.chat;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.widget.RoundImageView;

//2014-10-13
/**
 * 联系人详情类.
 *
 * @version 1.01
 * @since 1.01
 */
public class ContactDetail extends BaseActivity implements OnClickListener {

	private TextView name,content;//标题
	private TextView signature;
	private TextView title;
	private RoundImageView avatar;
	private Button bt_goChat , bt_call_phone;
	private String userName;
	private String userNick;
	private String avatarUrl,Url;
	private String cellPhone;
	private String userIdentity;
	private String userTitle;
	private String employeeNo;
	private View view;
	/**
	 * 创建入口.
	 * @param savedInstanceState Bundle类型
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_detail);
		initView();
		initData();
	}
	
	/**
	 * 初始化视图.
	 */
	private void initView() {
		// TODO Auto-generated method stub
		name = (TextView) findViewById(R.id.name);
		content = (TextView)findViewById(R.id.new_content);
		signature = (TextView) findViewById(R.id.signature);
		title = (TextView) findViewById(R.id.title);
		view = findViewById(R.id.title_back);
		view.setVisibility(View.VISIBLE);
		view.setOnClickListener(this);
		avatar = (RoundImageView) findViewById(R.id.avatar);
		bt_goChat = (Button) findViewById(R.id.bt_goChat);
		bt_call_phone = (Button) findViewById(R.id.bt_call_phone);
		bt_call_phone.setOnClickListener(this);
	}
	
	/**
	 * 初始化数据.
	 */
	private void initData() {
		Intent intent = getIntent();
		MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
		String hxSelfUserName = null;
		if(detail != null){
			hxSelfUserName = detail.getHxUsername();
		}
		userName = intent.getStringExtra("userName");
		userNick = intent.getStringExtra("userNick");
		avatarUrl = intent.getStringExtra("avatarUrl");
		userTitle = intent.getStringExtra("userTitle");
		employeeNo = intent.getStringExtra("employeeNo");
		System.out.println("avatarUrl:"+avatarUrl);
		System.out.println("modify avatarUrl:"+avatarUrl);
		cellPhone = intent.getStringExtra("cellPhone");
		name.setText(userNick);
		if(!DataUtil.isNullorEmpty(employeeNo)){
			name.setText(userNick+"["+employeeNo+"]");
		}
		content.setText(userNick);
		String hxUserName = CCApplication.getInstance().getUserName();
		if(LocalUtil.mBitMap != null&& userName != null && userName.equals(hxUserName)){
			avatar.setImageBitmap(LocalUtil.mBitMap);
		}else{
//			方法一
//			Url = DataUtil.getDownloadURL(this, avatarUrl);
//			getImageLoader().displayImage(Url, avatar, mOptions);
			
			//方法二
			ImageUtils.showImage(this, avatarUrl, avatar);//缩略图
		}
		if(DataUtil.isNull(hxSelfUserName) 
				|| DataUtil.isNull(userName) 
				|| !DataUtil.isMain()){//副号不显示聊天选项
			bt_goChat.setVisibility(View.GONE);
		}
		if(DataUtil.isNull(cellPhone)){
			bt_call_phone.setVisibility(View.GONE);
		}
		signature.setText(cellPhone);
		title.setText(userTitle);
	}
	

	/**
	 * 返回.
	 * 
	 * @param view 视图
	 */
	public void back(View view) {
		finish();
	}
	
	/**
	 * 跳入聊天界面.
	 * @param view 视图
	 */
	public void goChat(View view){
		// 进入聊天页面
//		singleChatWithHx();

		YWIMKit imKit = CCApplication.getInstance().getIMKit();
		Intent intent = imKit.getChattingActivityIntent(userName, Constants.BAI_CHUAN_APPKEY);
		startActivity(intent);
	}

	private void singleChatWithHx() {
		Bundle bundle = new Bundle();
		bundle.putString("userId", userName);
		bundle.putString("userNick", userNick);
		bundle.putString("avatar", avatarUrl);
		bundle.putString("cellPhone", cellPhone);
		bundle.putString("userTitle", userTitle);
		newActivity(ChatActivity.class, bundle);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_call_phone:
			call(cellPhone);
			break;
		case R.id.title_back:
			finish();
			break;
		default:
			break;
		}

	}
	/**
	 * 拨打电话
	 * @param number 电话号码
	 */
	public void call(String number){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
		startActivity(intent);
	}

	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		
	}
}
