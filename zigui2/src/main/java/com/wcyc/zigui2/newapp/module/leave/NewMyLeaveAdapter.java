package com.wcyc.zigui2.newapp.module.leave;

import java.util.ArrayList;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.module.wages.NewWagesBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @author 郑国栋
 * 2016-7-15
 * @version 2.0
 */
public class NewMyLeaveAdapter extends BaseAdapter{

	
	private Context myContext;// 上下文
	private ArrayList<NewMyLeaveBean> newMyLeaveBeanList;
	
	public NewMyLeaveAdapter(Context myContext, ArrayList<NewMyLeaveBean> newMyLeaveBeanList){
		super();
		this.myContext = myContext;
		this.newMyLeaveBeanList = newMyLeaveBeanList;
	}
	
	@Override
	public int getCount() {
		if (newMyLeaveBeanList != null) {
			return newMyLeaveBeanList.size();// 长度
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
					R.layout.new_my_leave_item, parent, false);

			// 获得布局中的控件
			viewholder.myLeaveTitle_tv = (TextView) convertView
					.findViewById(R.id.myLeaveTitle_tv);// 
			viewholder.status_tv = (TextView) convertView
					.findViewById(R.id.status_tv);// 
			viewholder.startTime_tv = (TextView) convertView
					.findViewById(R.id.startTime_tv);// 
			viewholder.endTime_tv = (TextView) convertView
					.findViewById(R.id.endTime_tv);// 
			viewholder.myLeaveContent_tv = (TextView) convertView
					.findViewById(R.id.myLeaveContent_tv);// 

			// 设置标签
			convertView.setTag(viewholder);

		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		//状态  
		String statusStr=newMyLeaveBeanList.get(position).getStatus();
		String status = "";//0无效  1审批中  2同意  3不同意
		if("0".equals(statusStr)){
			status="无效";
			viewholder.status_tv.setTextColor(myContext.getResources().getColor(R.color.font_gray));
		}else if("1".equals(statusStr)){
			status="审批中";
			viewholder.status_tv.setTextColor(myContext.getResources().getColor(R.color.font_gray));
		}else if("2".equals(statusStr)){
			status="同意";
			viewholder.status_tv.setTextColor(myContext.getResources().getColor(R.color.font_gray));
		}else if("3".equals(statusStr)){
			status="不同意";
			viewholder.status_tv.setTextColor(myContext.getResources().getColor(R.color.leave_red));
		}
		
		String leaveTypeStr=newMyLeaveBeanList.get(position).getLeaveType();
		String leaveType="";
		if("1".equals(leaveTypeStr)){
			leaveType="事假";
			viewholder.myLeaveTitle_tv.setBackgroundResource(R.drawable.leave_shijia_bg);
		}else if("2".equals(leaveTypeStr)){
			leaveType="病假";
			viewholder.myLeaveTitle_tv.setBackgroundResource(R.drawable.leave_bingjia_bg);
		}else if("3".equals(leaveTypeStr)){
			leaveType="丧假";
			viewholder.myLeaveTitle_tv.setBackgroundResource(R.drawable.leave_sangjia_bg);
		}else if("4".equals(leaveTypeStr)){
			leaveType="探亲假";
			viewholder.myLeaveTitle_tv.setBackgroundResource(R.drawable.leave_tanqinjia_bg);
		}else {
			leaveType="其他";
			viewholder.myLeaveTitle_tv.setBackgroundResource(R.drawable.leave_qita_bg);
		}
		
		String leaveStartTimeStr =newMyLeaveBeanList.get(position).getLeaveStartTime();
		String leaveStartTime=leaveStartTimeStr.substring(0, leaveStartTimeStr.lastIndexOf(":"));
		String leaveEndTimeStr =newMyLeaveBeanList.get(position).getLeaveEndTime();
		String leaveEndTime=leaveEndTimeStr.substring(0, leaveStartTimeStr.lastIndexOf(":"));
		// 显示内容
		viewholder.myLeaveTitle_tv.setText(leaveType);
		viewholder.status_tv.setText(status);
		viewholder.startTime_tv.setText(leaveStartTime+"至");
		viewholder.endTime_tv.setText(leaveEndTime);
		viewholder.myLeaveContent_tv.setText(newMyLeaveBeanList.get(position).getReason());
		

		return convertView;
	}
	
	
	private class ViewHolder {
		//
		TextView myLeaveTitle_tv, status_tv,startTime_tv,endTime_tv,myLeaveContent_tv;

	}

	// 添加数据
	public void addItem(ArrayList<NewMyLeaveBean> i) {
		newMyLeaveBeanList.addAll(i);
	}

}
