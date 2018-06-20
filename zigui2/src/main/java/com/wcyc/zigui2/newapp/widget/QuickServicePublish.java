/*
 * 文 件 名:QuickServicePublish.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-1
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.widget;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.CCApplication;


import com.wcyc.zigui2.newapp.adapter.MenuAdapter;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.home.NewAttendanceActivity;
import com.wcyc.zigui2.newapp.home.NewCommentActivity;
import com.wcyc.zigui2.newapp.home.NewHomeworkActivity;
import com.wcyc.zigui2.newapp.module.classdynamics.NewPublishDynamicActivity;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordActivity;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordRightControll;
import com.wcyc.zigui2.newapp.module.dailyrecord.PublishDailyRecordActivity;
import com.wcyc.zigui2.newapp.module.email.ComposeEmailActivity;
import com.wcyc.zigui2.newapp.module.email.ComposeEmailByParentActivity;
import com.wcyc.zigui2.newapp.module.email.MenuConfigBean;
import com.wcyc.zigui2.newapp.module.leave.NewMyLeaveAskActivity;
import com.wcyc.zigui2.newapp.module.mailbox.ComposeEmailToMasterActivity;
import com.wcyc.zigui2.newapp.module.notice.NoticeRightControll;
import com.wcyc.zigui2.newapp.module.notice.PublishNotifyActivity;
import com.wcyc.zigui2.newapp.module.summary.PublishSummaryActivity;
import com.wcyc.zigui2.newapp.module.summary.SummaryRightControll;
import com.wcyc.zigui2.utils.Constants;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

public class QuickServicePublish extends PopupWindow{
	private Button cancel;
	private View view;
	private GridView gridview;
	Map<Integer, MenuItem> map = new HashMap<Integer, MenuItem>();
	MenuItem email = new MenuItem("14","邮件",MenuItem.EMAIL_NUMBER,R.drawable.sheet_icon_youjian,ComposeEmailActivity.class);
	MenuItem notice = new MenuItem("02","通知",MenuItem.NOTICE_NUMBER,R.drawable.sheet_icon_tongzhi,PublishNotifyActivity.class);
	MenuItem homework = new MenuItem("08","作业",MenuItem.HOMEWORK_NUMBER,R.drawable.sheet_icon_zuoye,NewHomeworkActivity.class);
	MenuItem dailyRecord = new MenuItem("19","日志",MenuItem.DAILY_RECORD_NUMBER,R.drawable.sheet_icon_rizhi,PublishDailyRecordActivity.class);
	MenuItem summary = new MenuItem("20","总结",MenuItem.SUMMARY_NUMBER,R.drawable.sheet_icon_zongjie,PublishSummaryActivity.class);
	MenuItem attend = new MenuItem("11","考勤",MenuItem.ATTENDANCE_NUMBER,R.drawable.sheetl_icon_kaoqin,NewAttendanceActivity.class);
	MenuItem comment = new MenuItem("07","点评",MenuItem.COMMENT_NUMBER,R.drawable.sheet_iocn_dianping,NewCommentActivity.class);
	MenuItem dyn = new MenuItem("10","班级动态",MenuItem.DYNAMICS_NUMBER,R.drawable.sheet_iocn_banjiquan,NewPublishDynamicActivity.class);

	MenuItem emailParent = new MenuItem("14","邮件",MenuItem.EMAILPARENT_NUMBER,R.drawable.sheet_icon_youjian,ComposeEmailByParentActivity.class);
	MenuItem schoolmasterParent = new MenuItem("18","给校长写信",MenuItem.SCHOOLMASTERPARENT_NUMBER,R.drawable.sheetl_icon_xzxx, ComposeEmailToMasterActivity.class);
	MenuItem leaveParent = new MenuItem("","请假申请",MenuItem.LEAVEPARENT_NUMBER,R.drawable.sheetl_icon_qingjiatiao, NewMyLeaveAskActivity.class);

	List<MenuItem> list = new ArrayList<MenuItem>();
	List<MenuItem> parentItem = new ArrayList<MenuItem>();

	public QuickServicePublish(Activity context){
		super(context);
		initMenuItem();
		UserType user = CCApplication.getInstance().getPresentUser();
		init(context);
		if(Constants.PARENT_STR_TYPE.equals(user.getUserType())){
			initParentMenu(context);
		}else{
			initMenu(context);
		}
		initEvent();
	}

	private void initMenu(Activity context){
		if(Constants.hasEmailFunc) {
			list.add(email);
		}
		if(NoticeRightControll.hasPublishNoticeRight()){
			list.add(notice);
		}
		list.add(homework);
		list.add(attend);
		list.add(comment);
		if(DailyRecordRightControll.hasPublishDailyRecordRight()){
			list.add(dailyRecord);
		}if(SummaryRightControll.hasPublishSummaryRight()){
			list.add(summary);
		}
		list.add(dyn);

		MenuAdapter adapter;
		adapter = new MenuAdapter(context,list,true);
		gridview.setAdapter(adapter);
	}
	private void initMenuItem(){
		map.put(MenuItem.LEAVE_NUMBER,leaveParent);
		map.put(MenuItem.SCHOOLMAIL_NUMBER,schoolmasterParent);
	}
	private void initParentMenu(Activity context) {
		MenuConfigBean config = CCApplication.getInstance().getMenuConfig();
		List<MenuConfigBean.MenuConfig> list = config.getPersonalConfigList();
		if(list != null) {
			for (MenuConfigBean.MenuConfig item : list) {
				if (item != null && item.getStatus() == MenuItem.VALID) {
					MenuItem menuItem = map.get(item.getFunctionNumber());
					if (menuItem != null) {
						if (MenuItem.FREE.equals(item.getType())) {
							menuItem.setFree(true);
							parentItem.add(menuItem);
						} else if (MenuItem.CHARGE.equals(item.getType())) {
							menuItem.setFree(false);
							parentItem.add(menuItem);
						}
					}
				}
			}
		}
		//家长端去掉发班级动态功能
//		parentItem.add(dyn);
		MenuAdapter adapter = new MenuAdapter(context, parentItem, true);
		gridview.setAdapter(adapter);
	}

	private void init(Activity context){
		LayoutInflater inflater = context.getLayoutInflater();
		view = inflater.inflate(R.layout.quick_service_publish, null);
		cancel = (Button) view.findViewById(R.id.cancel);
		gridview = (GridView) view.findViewById(R.id.quick_service);
		this.setContentView(view);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setOutsideTouchable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
	}

	private void initEvent(){
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}

		});

		gridview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				System.out.println("publish dismiss");
				dismiss();
			}

		});
	}
}