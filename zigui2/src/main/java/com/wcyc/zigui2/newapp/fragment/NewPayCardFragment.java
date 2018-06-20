package com.wcyc.zigui2.newapp.fragment;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.ChildMember;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;

/**
 * 支付卡输入卡号密码界面 new
 * 
 * @author 谭园园
 * @version 2.0
 * @since 2.0
 */
public class NewPayCardFragment extends Fragment implements OnClickListener{
	
	private Button  commit_paycard;
	private EditText card_number , card_password;
	private ChildMember child;
	private final String pay_card = "/cardService/goPayCard";
	
	private View view;
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.pay_card_activity);
//		initView();
//		initDatas();
//		initEvents();
//	}
	public static Fragment newInstance(int index) { 
		Fragment fragment = new NewPayCardFragment();
		Bundle args = new Bundle();
		args.putInt("index", index);
		fragment.setArguments(args);
		//fragment.setIndex(index);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.new_pay_card_fragment, null);
		card_number = (EditText) view.findViewById(R.id.card_number);
		card_password = (EditText) view.findViewById(R.id.card_password);
		commit_paycard = (Button) view.findViewById(R.id.commit_paycard);
		initDatas();
		initEvents();
		return view;
	}
		
		
	private void initDatas() {
		//得到小孩的数据
//		Bundle bundle = getIntent().getExtras();
//		if(bundle != null){
//			child = (ChildMember) bundle.getSerializable("child");
//	 }
	}
	
	private void initEvents() {
		commit_paycard.setOnClickListener(this);
	}

//	@Override
//	protected void getMessage(String data) {
//		
//	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit_paycard:
			getCardMessage();
			break;
		}
	}
	private void getCardMessage() {
		String result = null;
	
		String number = card_number.getText().toString();
		String password = card_password.getText().toString();
		if ("".equals(number) || "".equals(password)) {
			DataUtil.getToast("卡号或充值密码不能为空");
			return;
		}
		JSONObject json = new JSONObject();
		try {
			json.put("number",number);
			json.put("password",password);
			json.put("child",child.getChildID());
			json.put("userid",CCApplication.app.getPresentUser().getUserId());
			String url = new StringBuilder(Constants.SERVER_URL).append(pay_card).toString();
			result = HttpHelper.httpPostJson(url, json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result != null) {
			try {
				JSONObject json1 = new JSONObject(result);
				//返回"1"表示充值成功  "2"表示卡号不正确 "3"表示密码不正确
				if ("1".equals(json1.getString("resultCode"))) {
					CCApplication.app.finishAllActivity();
//					newActivity(MyInformationActivity.class, null);
					DataUtil.getToast("充值成功");
				}else if ("2".equals(json1.getString("resultCode"))) {
					DataUtil.getToast("卡号不正确");
				}else if("3".equals(json1.getString("resultCode"))){
					DataUtil.getToast("充值密码不正确");
				}else if ("4".equals(json1.getString("resultCode"))) {
					DataUtil.getToast("此卡已失效");
				}else{
					DataUtil.getToast("系统异常");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//		if (!isLoading()) {
//			model.queryPost(pay_card, json);
//		}
	}

}
