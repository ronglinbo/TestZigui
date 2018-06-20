/*
* 文 件 名:PayFailActivity.java
* 创 建 人： 姜韵雯
* 日    期： 2014-12-12
* 版 本 号： 1.05
*/
package com.wcyc.zigui2.newapp.module.charge2;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.ChildMember;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.utils.DataUtil;

/**
 * 支付失败
 * 
 * @author 姜韵雯
 * @version 1.05
 * @since 1.05
 */
public class PayFailActivity extends BaseActivity implements OnClickListener{
	
	private ChildMember child;
	private Button backButton , bt_repay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_fail);
		initView();
		initDatas();
	}
	private void initDatas() {
		//得到小孩的数据
//		Bundle bundle = getIntent().getExtras();
//		if(bundle != null){
//			child = (ChildMember) bundle.getSerializable("child");
//		}
//		
//		if(child != null){
//			
//		}else{
//			DataUtil.getToast("没有小孩信息");
//			finish();
//		}
		
	}

	private void initView() {
		backButton = (Button) findViewById(R.id.title_btn);
		backButton.setOnClickListener(this);
		backButton.setText(R.string.title_pay_fail);
		
		bt_repay = (Button) findViewById(R.id.bt_repay);
		bt_repay.setOnClickListener(this);
	}

	@Override
	protected void getMessage(String data) {
		
	}

	@Override
	public void onClick(View v) {
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("child", child);
		switch (v.getId()) {
			case R.id.title_btn:
				finish();
				break;
			case R.id.bt_repay:
				finish();
				break;
		}
	}

}
