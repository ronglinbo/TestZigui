package com.wcyc.zigui2.newapp.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.bean.ChildMember;
import com.wcyc.zigui2.bean.OrderResult;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.listener.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.fragment.AllMessageFragment;
import com.wcyc.zigui2.newapp.fragment.ContactFragment;
import com.wcyc.zigui2.newapp.fragment.NewPayCardFragment;
import com.wcyc.zigui2.newapp.fragment.NewWangFragment;
import com.wcyc.zigui2.newapp.module.charge.NewPaymentRecordActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.RoundImageView;

/**
 *新版app 会员中心activity
 * 
 * @author yytan
 * @version 2.0
 * @since 2.0
 */
public class NewMemberCenterActivity extends BaseActivity implements OnClickListener,HttpRequestAsyncTaskListener{
	
	private RelativeLayout rl_payment_record , rl_package_manage , rl_invite_attention;
	private Button backButton , bt_renewal_fee , bt_pay_card;
	private ChildMember child;
	private RoundImageView riv_child_icon;
	private TextView tv_child_name , tv_member_time;
	private TextView new_wang , new_card , new_content , title_imgbtn_add;
	private LinearLayout title_back;
	
	// 定义选择套餐界面的对象
	private static NewMemberCenterActivity instance = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.new_member_recharge);
		initView();
		initDatas();
		initEvents();
	}
	
	/**
	 * 获取类的实例.
	 * 
	 * @return 类的实例
	 */
	public static NewMemberCenterActivity getInstance() {
		return instance;
	}
	@Override
	public void finish() {
		super.finish();
		instance = null;
	}

	private void initEvents() {
		if(new_wang != null){
			new_wang.setOnClickListener(this);
		}
		if(new_card != null){
			new_card.setOnClickListener(this);
		}
		title_back.setOnClickListener(this);
		title_imgbtn_add.setOnClickListener(this);
	}

	private void initDatas() {
		//得到小孩的数据
		child = (ChildMember) getIntent().getExtras().get("child");
		title_back.setVisibility(View.VISIBLE);
		new_content.setText("会员充值");
	}

	private void initView() {
		new_wang = (TextView) findViewById(R.id.new_wang);
		new_card = (TextView) findViewById(R.id.new_card);
		title_back = (LinearLayout) findViewById(R.id.title_back);
		new_content = (TextView) findViewById(R.id.new_content);
		title_imgbtn_add =(TextView)findViewById(R.id.title_imgbtn_add);
	}

	@Override
	protected void getMessage(String data) {

		OrderResult orderInfo = JsonUtils.fromJson(data, OrderResult.class);
		if (orderInfo.getResultCode() != 200) {
			DataUtil.getToast(orderInfo.getErrorInfo());
		} else {
			// 跳转到支付确认界面
			Bundle bundle = new Bundle();
			bundle.putSerializable("child", child);
			bundle.putSerializable("orderInfo", orderInfo);
		}
	
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("child", child);
		switch (v.getId()) {
			case R.id.title_back:
				finish();
				break;
			case R.id.title_imgbtn_add://充值记录界面
				newActivity(NewPaymentRecordActivity.class, bundle);
				break;
			case R.id.new_wang:
//				goNewWang();
				placeView(0);
				new_wang.setTextColor(0xff007aff);
				new_card.setTextColor(0xff212121);
				break;
			case R.id.new_card:
//				goNewCard();
				placeView(1);
				new_wang.setTextColor(0xff212121);
				new_card.setTextColor(0xff007aff);
				break;
		}
	}
	
	/**
	 * 跳转卡充值界面
	 */
	private void payCard() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("child", child);
//		newActivity(PayCardActivity.class, bundle);
	}
	
	/**
	 * 选择了套餐后生成订单
	 */
	private void createOrder() {
			getOrderInfo(CCApplication.app.getPresentUser().getUserId(),
					child.getChildID(), "1");
	}

	/**
	 * 得到订单信息
	 * 
	 * @param userID
	 *            用户id
	 * @param childID
	 *            缴费的小孩id
	 * @param products
	 *            缴费的套餐类型 1：基础套餐； 2：家庭套餐
	 */
	private void getOrderInfo(String userID, String childID, String products) {
		JSONObject json = new JSONObject();
		try {
			json.put("userID", userID);
			json.put("childID", childID);
			json.put("products", products);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (!isLoading()) {
			model.queryPost(Constants.RECHARGE_SERVICE_NEW_ORDER, json);
		}
	}
	
	
	//网络请求 是否续费
	@Override
	protected void onResume() {
		super.onResume();
		httpRequest();
	}
	private void httpRequest() {
		JSONObject json = new JSONObject();
		try {
			json.put("studentId", child.getChildID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new HttpRequestAsyncTask(json, this, this).execute(Constants.SET_MEAL_RENEW_SERVICE_BUY_SET_MEAL);
	}
	//网络请求成功
	@Override
	public void onRequstComplete(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			//1是已续费不用再交钱了 2是没钱了 要交钱
			String money = obj.getString("money");
			if ("1".equals(money)) {
				bt_renewal_fee.setBackgroundResource(R.drawable.bg_red_press1);
				bt_renewal_fee.setClickable(false);
				bt_renewal_fee.setText("已缴费");
				
				bt_pay_card.setBackgroundResource(R.drawable.bg_red_press1);
				bt_pay_card.setClickable(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onRequstCancelled() {
		DataUtil.getToast("没有调到接口");
	}

	public void placeView(int index){
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(index+"");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(fragment == null){
			switch(index){
			case 0:
				fragment = NewWangFragment.newInstance(index);
				Bundle bundle = new Bundle();
				
				fragment.setArguments(bundle);
				break;
			case 1:
				fragment = NewPayCardFragment.newInstance(index);
				break;
			default:
				break;
			}
		}
		ft.replace(R.id.wang_or_card, fragment, index+"");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//		ft.addToBackStack(null);
		ft.commitAllowingStateLoss();
	}
	
	private void init(int index){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.wang_or_card,AllMessageFragment.newInstance(index),index+"");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}
}
