package com.wcyc.zigui2.newapp.module.duty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wcyc.zigui2.R;

import com.wcyc.zigui2.chat.ContactDetail;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.ClassList;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.RoleBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 值班查询adapter
 * 
 * @author 郑国栋 2016-6-23
 * @version 2.1
 */
public class NewDutyInquiryAdapter extends BaseAdapter {

	private Context myContext;// 上下文
	private ArrayList<NewDutyInquiryBean> dutyInquiryList;// 展示list
	private String dutyUserId;
	private String userid;
	private String schoolId;
	private List<NewDutyDiaryBean> newDutyDiaryBeanList;

	public NewDutyInquiryAdapter(Context myContext,
			ArrayList<NewDutyInquiryBean> dutyInquiryList,String schoolId) {
		super();
		this.myContext = myContext;
		this.dutyInquiryList = dutyInquiryList;
		this.schoolId = schoolId;
		
		newDutyDiaryBeanList=new ArrayList<NewDutyDiaryBean>();
		getDutyDiaryList(dutyInquiryList);
	}
	
	public void getDutyDiaryList(ArrayList<NewDutyInquiryBean> dutyInquiryList){
		if(dutyInquiryList!=null){
			for (int i = 0; i < dutyInquiryList.size(); i++) {
				try {
					NewDutyDiaryBean newDutyDiaryBean = new NewDutyDiaryBean();
					JSONObject json = new JSONObject();
					json.put("schoolId", schoolId);
					json.put("inputDutyID", dutyInquiryList.get(i).getId());

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

		if (dutyInquiryList != null) {

			return dutyInquiryList.size();// 长度
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
					R.layout.new_duty_inquiry_item, parent, false);

			// 获得布局中的控件
			viewholder.new_duty_time = (TextView) convertView
					.findViewById(R.id.new_duty_time);
			viewholder.new_duty_diary = (TextView) convertView
					.findViewById(R.id.new_duty_diary);
			viewholder.new_duty_man_name = (TextView) convertView
					.findViewById(R.id.new_duty_man_name);
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
		String timeStr = dutyInquiryList.get(position).getOperatorTime();

		timeStr = timeStr.substring(0, timeStr.lastIndexOf(" "));
		String week=DataUtil.getStrToWeek(timeStr);
		final String timeRegister=timeStr;
		viewholder.new_duty_time.setText(timeStr+" "+week);
		timeStr=timeStr.replaceAll("-", "");
		int timeInt=Integer.parseInt(timeStr);

		String nowTime = getNowTime();
		nowTime = nowTime.substring(0, nowTime.lastIndexOf(" "));
		nowTime=nowTime.replaceAll("-", "");
		int nowTimeInt=Integer.parseInt(nowTime);

		if ("2" .equals(dutyInquiryList.get(position).getIdentity())){
			//值班人员
			viewholder.new_duty_man_name.setText(dutyInquiryList.get(position)
					.getUserName() + "（行政干部）");
		}else {
			viewholder.new_duty_man_name.setText(dutyInquiryList.get(position)
					.getUserName());
		}


		dutyUserId = dutyInquiryList.get(position).getDutyUserId();
		userid = CCApplication.getInstance().getPresentUser().getUserId();
		
		if (timeInt==nowTimeInt) {// nowTime.equals(timeStr)//position == 0
			viewholder.new_duty_iv.setVisibility(View.VISIBLE);
			
			if(userid.equals(dutyUserId)){
				viewholder.new_duty_diary.setVisibility(View.VISIBLE);
				viewholder.new_duty_diary.setText("值班登记");
			}else{
				viewholder.new_duty_diary.setVisibility(View.GONE);
			}
			
			viewholder.new_duty_diary.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					 Bundle bundle = new Bundle();
					 bundle.putString("position", position_final + "");
					 bundle.putString("inputDutyID", dutyInquiryList.get(position_final).getId());
					 bundle.putString("TeaUserName", dutyInquiryList.get(position_final).getUserName());
					 bundle.putString("TeaMobile", dutyInquiryList.get(position_final).getMobile());
					 bundle.putString("timeRegister", timeRegister);
					 ((BaseActivity) myContext).newActivity(
							 NewDutyRegisterActivity.class, bundle);
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
					bundle.putString("inputDutyID", dutyInquiryList.get(position_final).getId());
					bundle.putString("TeaUserName", dutyInquiryList.get(position_final).getUserName());
					bundle.putString("TeaMobile", dutyInquiryList.get(position_final).getMobile());
					
					((BaseActivity) myContext).newActivity(
							NewDutyDiaryActivity.class, bundle);
					// DataUtil.getToast("去"+dutyInquiryList.get(position_final).getDutyDiary()+"界面");
				}
			});
		}else if(timeInt>nowTimeInt){
			viewholder.new_duty_iv.setVisibility(View.GONE);
			viewholder.new_duty_diary.setVisibility(View.GONE);
		}
		if(dutyUserId.equals(userid)){
			viewholder.new_duty_man_name.setClickable(false);
			viewholder.new_duty_man_name.setTextColor(myContext.getResources().getColor(R.color.font_black));
		}else{
			viewholder.new_duty_man_name.setClickable(true);
			viewholder.new_duty_man_name.setTextColor(myContext.getResources().getColor(R.color.blue));
			
			viewholder.new_duty_man_name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					try {
						
						if (!DataUtil.isNetworkAvailable(myContext)) {
							DataUtil.getToast(myContext.getResources().getString(R.string.no_network));
							return;
						}
						
						JSONObject json2 = new JSONObject();
						json2.put("dutyUserId", dutyInquiryList.get(position_final).getDutyUserId());
						System.out.println("==去环信个人信息入参==="+json2);
						String url = new StringBuilder(Constants.SERVER_URL).append(
								Constants.GET_TEACHER_STAFF_INFO).toString();
						if (!((BaseActivity) myContext).isLoading()) {
							String result = HttpHelper.httpPostJson(myContext,url, json2);
							System.out.println("==去环信个人信息出参==="+result);
							NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
//							if (ret.getServerResult().getResultCode() != 200) {// 请求失败
//								DataUtil.getToast(ret.getServerResult().getResultMessage());
//							} else {
								JSONObject json3 = new JSONObject(result);
								if(json3.has("userMobile")){
									String cellPhone = json3.getString("userMobile");
									bundle.putString("cellPhone", cellPhone);
								}
								if(json3.has("userName")){
									String userNick = json3.getString("userName");
									bundle.putString("userNick", userNick);
								}
								if(json3.has("huanxinAccountId")){
									String huanxinAccountId = json3.getString("huanxinAccountId");
									bundle.putString("userName", huanxinAccountId);
								}
								if(json3.has("userHeadUrl")){
									String avatarUrl = json3.getString("userHeadUrl");
									bundle.putString("avatarUrl", avatarUrl);
								}
								if(json3.has("userTitleRoles")){
									String userTitleRoles = json3.getString("userTitleRoles");
									String userTitle="";
									JSONArray json4=new JSONArray(userTitleRoles);
									if(json4.length()>0){
										for (int i = 0; i < json4.length(); i++) {
											RoleBean roleBean=JsonUtils.fromJson(json4.get(i).toString(),RoleBean.class);
											userTitle+=roleBean.getRoleName()+",";
										}
									}
									bundle.putString("userTitle", userTitle);
								}
								if(json3.has("employeeNo")){
									String employeeNo=json3.getString("employeeNo");
									bundle.putString("employeeNo", employeeNo);
								}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(DataUtil.isNullorEmpty(bundle.getString("userName"))){
						DataUtil.getToast("账号信息有误，请联系管理员");
					}
					if(DataUtil.isNullorEmpty(bundle.getString("cellPhone"))){
						bundle.putString("cellPhone", dutyInquiryList.get(position_final).getMobile());
					}
					if(DataUtil.isNullorEmpty(bundle.getString("userNick"))){
						bundle.putString("userNick", dutyInquiryList.get(position_final).getUserName());
					}
					((BaseActivity) myContext).newActivity(ContactDetail.class,bundle);
				}
			});
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
		TextView new_duty_time, new_duty_diary, new_duty_man_name;
		ImageView new_duty_iv;
	}

	// 添加数据
	public void addItem(ArrayList<NewDutyInquiryBean> i) {
		getDutyDiaryList(i);
		dutyInquiryList.addAll(i);
	}
}
