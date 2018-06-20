package com.wcyc.zigui2.newapp.module.duty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 我的值班Adapter
 * 
 * @author 郑国栋 2016-6-23
 * @version 2.1
 */
public class NewMyDutyAdapter extends BaseAdapter {

	private Context myContext;// 上下文
	private BaseActivity activity;
	private String schoolId;
	private ArrayList<NewMyDutyBean> myDutyList;// 展示list
	private List<NewDutyDiaryBean> newDutyDiaryBeanList;

	public NewMyDutyAdapter(Context myContext,
			ArrayList<NewMyDutyBean> myDutyList,String schoolId) {
		super();
		this.myContext = myContext;
		this.myDutyList = myDutyList;
		this.schoolId=schoolId;
		
		newDutyDiaryBeanList=new ArrayList<NewDutyDiaryBean>();
		getDutyDiaryList(myDutyList);
	}

	public NewMyDutyAdapter(BaseActivity activity, Context myContext,
			ArrayList<NewMyDutyBean> myDutyList,String schoolId) {
		super();
		this.activity = activity;
		this.myContext = myContext;
		this.myDutyList = myDutyList;
		this.schoolId=schoolId;
		
		newDutyDiaryBeanList=new ArrayList<NewDutyDiaryBean>();
		getDutyDiaryList(myDutyList);
	}
	
	public void getDutyDiaryList(ArrayList<NewMyDutyBean> myDutyList){
		if(myDutyList!=null){
			for (int i = 0; i < myDutyList.size(); i++) {
				try {
					NewDutyDiaryBean newDutyDiaryBean = new NewDutyDiaryBean();
					JSONObject json = new JSONObject();
					json.put("schoolId", schoolId);
					json.put("inputDutyID", myDutyList.get(i).getId());

					if (!DataUtil.isNetworkAvailable(myContext)) {
						DataUtil.getToast(myContext.getResources().getString(R.string.no_network));
					}else{
//						System.out.println("值班日志入参=====" + json);
						String url = new StringBuilder(Constants.SERVER_URL).append(
								Constants.GET_DUTY_LOG).toString();//
						String result = HttpHelper.httpPostJson(myContext, url, json);
//						System.out.println("值班日志出参=====" + result);
						JSONObject jsonB = new JSONObject(result);
						String dutyLog = jsonB.getString("dutyLog");
						newDutyDiaryBean = JsonUtils.fromJson(dutyLog,
								NewDutyDiaryBean.class);
					}
					newDutyDiaryBeanList.add(newDutyDiaryBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public int getCount() {

		if (myDutyList != null) {

			return myDutyList.size();// 长度
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return position;// 当前位置ID
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		if (convertView == null) {
			// 实例化控件
			viewholder = new ViewHolder();
			// 配置单个item的布局
			convertView = LayoutInflater.from(myContext).inflate(
					R.layout.new_my_duty_item, parent, false);

			// 获得布局中的控件
			viewholder.new_duty_time = (TextView) convertView
					.findViewById(R.id.new_duty_time);
			viewholder.new_duty_diary = (TextView) convertView
					.findViewById(R.id.new_duty_diary);
			viewholder.new_duty_iv = (ImageView) convertView
					.findViewById(R.id.new_duty_iv);

			// 设置标签
			convertView.setTag(viewholder);

		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}
		final int position_final = position;

		
		// 显示内容
		String timeStr = myDutyList.get(position).getOperatorTime();
		timeStr = timeStr.substring(0, timeStr.lastIndexOf(" "));
		final String timeRegister=timeStr;
		String week=DataUtil.getStrToWeek(timeStr);
		viewholder.new_duty_time.setText(timeStr + " " + week );
		timeStr=timeStr.replaceAll("-", "");
		int timeInt=Integer.parseInt(timeStr);
		
		String nowTime = getNowTime();
		nowTime = nowTime.substring(0, nowTime.lastIndexOf(" "));
		nowTime=nowTime.replaceAll("-", "");
		int nowTimeInt=Integer.parseInt(nowTime);

		if (timeInt==nowTimeInt) {
			viewholder.new_duty_iv.setVisibility(View.VISIBLE);
			viewholder.new_duty_diary.setVisibility(View.VISIBLE);
			viewholder.new_duty_diary.setText("值班登记");
			viewholder.new_duty_diary.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putString("position", position_final + "");
					bundle.putString("inputDutyID", myDutyList.get(position_final).getId());
					bundle.putString("TeaUserName", myDutyList.get(position_final).getUserName());
					bundle.putString("TeaMobile", myDutyList.get(position_final).getMobile());
					bundle.putString("timeRegister", timeRegister);
					((BaseActivity) myContext).newActivity(NewDutyRegisterActivity.class, bundle);
				}
			});
		} else if(timeInt<nowTimeInt){
			viewholder.new_duty_iv.setVisibility(View.GONE);
			
			NewDutyDiaryBean newDutyDiaryBean = new NewDutyDiaryBean();
			newDutyDiaryBean=newDutyDiaryBeanList.get(position);
			
			if(!DataUtil.isNullorEmpty(newDutyDiaryBean.getEarlyStudyStu())
					||!DataUtil.isNullorEmpty(newDutyDiaryBean.getEarlyStudyTea())
					||!DataUtil.isNullorEmpty(newDutyDiaryBean.getClassStudent())
					||!DataUtil.isNullorEmpty(newDutyDiaryBean.getClassTeacher())
					||!DataUtil.isNullorEmpty(newDutyDiaryBean.getNightStudyStu())
					||!DataUtil.isNullorEmpty(newDutyDiaryBean.getNightStudyTea())
					||!DataUtil.isNullorEmpty(newDutyDiaryBean.getSchoolStory())){
				
				viewholder.new_duty_diary.setVisibility(View.VISIBLE);
			}else{
				viewholder.new_duty_diary.setVisibility(View.GONE);
			}
			viewholder.new_duty_diary.setText("值班日志");
			viewholder.new_duty_diary.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!DataUtil.isNetworkAvailable(myContext)) {
						DataUtil.getToast(myContext.getResources().getString(R.string.no_network));
						return;
					}
					
					Bundle bundle = new Bundle();
					bundle.putString("position", position_final + "");
					bundle.putString("inputDutyID", myDutyList.get(position_final).getId());
					bundle.putString("TeaUserName", myDutyList.get(position_final).getUserName());
					bundle.putString("TeaMobile", myDutyList.get(position_final).getMobile());
					
					((BaseActivity) myContext).newActivity(
							NewDutyDiaryActivity.class, bundle);

				}
			});
		}else if(timeInt>nowTimeInt){
			viewholder.new_duty_iv.setVisibility(View.GONE);
			viewholder.new_duty_diary.setVisibility(View.GONE);
		}

		return convertView;
	}
	
	
	/**
	 * 获得系统的最新时间
	 * 
	 */
	@SuppressLint("SimpleDateFormat")
	private String getNowTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(System.currentTimeMillis());
	}
	

	private class ViewHolder {
		//
		TextView new_duty_time, new_duty_diary;
		ImageView new_duty_iv;

	}
	
	// 添加数据
	public void addItem(ArrayList<NewMyDutyBean> i){
		getDutyDiaryList(i);
		myDutyList.addAll(i);
	}
}
