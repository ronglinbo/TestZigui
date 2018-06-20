/**   
 * 文件名：com.wcyc.zigui.home.TConfigrChoiceClassActivity.java   
 *   
 * 版本信息：   
 * 日期：2014年10月15日 下午1:45:42  
 * Copyright 惟楚有材 Corporation 2014    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.newapp.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.adapter.TConfigrChoiceLVAdapter;
import com.wcyc.zigui2.newapp.adapter.TConfigrChoiceLVAdapter.ViewHorld;
import com.wcyc.zigui2.bean.Classes;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.utils.DataUtil;
//2014年10月15日 下午1:45:42
/**
 * 通知页面模块的老师选择班级activity
 * 
 * @author 王登辉
 * @version 1.01
 */
public class TConfigrChoiceClassActivity extends BaseActivity implements
		OnClickListener {
	// 班级集合
	private List<Classes> classes;
	public static final int BACK_RESULT = 10;
	private Button titleButton;
	private Button choiceButton;
	private ListView listView;
	private List<Classes>  choiceBackList =null;//选中  返回给发布页面的班级集合
	private TConfigrChoiceLVAdapter adapter;
	private boolean isChoice;
	private List<Classes> beChoiceClas;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_choiceclass);
		classes = (List<Classes>) getIntent().getSerializableExtra("classList");
		beChoiceClas = (List<Classes>) getIntent().getSerializableExtra("beChoiceClas");
		initView();
		initDatas();
		initEvents();

	}

	/**
	 * 实例化控件
	 */

	private void initView() {
		titleButton = (Button) findViewById(R.id.title_btn);
		choiceButton = (Button) findViewById(R.id.teacher_choice_btn);
		listView = (ListView) findViewById(R.id.teacher_choice_class_lv);
	}

	/**
	 * 实例化数据
	 */

	private void initDatas() {
		titleButton.setText("选择接收班级");
	}

	/**
	 * 设置效果监听
	 */

	private void initEvents() {
		adapter = new TConfigrChoiceLVAdapter(classes, this,beChoiceClas);
		listView.setAdapter(adapter);
		titleButton.setOnClickListener(this);
		choiceButton.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			ViewHorld horld = (ViewHorld) view.getTag();
			horld.cb.toggle();
		    adapter.getIsSelected().put(position, horld.cb.isChecked());
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_btn:
			TConfigrChoiceClassActivity.this.finish();
			break;

		case R.id.teacher_choice_btn://确定按钮
			choiceBackList = new ArrayList<Classes>();
			if (DataUtil.isFastDoubleClick()) {
				break;
			}
			for (int i = 0; i < classes.size(); i++) {
				isChoice = adapter.getIsSelected().get(i);
				if(isChoice)
				{
					choiceBackList.add(classes.get(i));
				}
			}
//			if(0 == choiceBackList.size())
//			{
//				TConfigrChoiceClassActivity.this.finish();
//			}
//			else
//			{
				Intent data = new Intent();
				data.putExtra("beChoiceClas", (Serializable)choiceBackList);
				setResult(BACK_RESULT, data);
				TConfigrChoiceClassActivity.this.finish();
//			}
			
			break;
		default:
			break;
		}

	}

	@Override
	protected void getMessage(String data) {

	}

}
