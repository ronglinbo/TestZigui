package com.wcyc.zigui2.newapp.module.wages;

import java.util.ArrayList;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.module.consume.NewConsumeRecoadBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 工资条Adapter
 * 
 * @author 郑国栋 2016-6-23
 * @version 2.1
 */
public class NewWagesAdapter extends BaseAdapter {

	private Context myContext;// 上下文
	private ArrayList<NewWagesBean> wagesList;// 工资条list

	public NewWagesAdapter(Context myContext, ArrayList<NewWagesBean> wagesList) {
		super();
		this.myContext = myContext;
		this.wagesList = wagesList;
	}

	@Override
	public int getCount() {

		if (wagesList != null) {

			return wagesList.size();// 长度
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
					R.layout.new_wages_item, parent, false);

			// 获得布局中的控件
			viewholder.new_wages_timeandname = (TextView) convertView
					.findViewById(R.id.new_wages_timeandname);// 工资发放年月 和 工资名称
			viewholder.new_wages_publish_nameandtime = (TextView) convertView
					.findViewById(R.id.new_wages_publish_nameandtime);// 发布人和发布时间

			// 设置标签
			convertView.setTag(viewholder);

		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}

		// 显示内容
		viewholder.new_wages_timeandname.setText(wagesList.get(position)
				.getYear()+"年"+wagesList.get(position).getMonth()+"月"
				+ wagesList.get(position).getWageRecordName());
		String publishTime=wagesList.get(position).getPublishTime();
		publishTime=publishTime.substring(0,publishTime.lastIndexOf(":"));
		viewholder.new_wages_publish_nameandtime.setText(
				wagesList.get(position).getTeacherBaseInfo().getName()
				+ "发布于  "+ publishTime);
		if(wagesList.get(position).getIsRead()){
			viewholder.new_wages_timeandname.setTextSize(14);
		}else{
			viewholder.new_wages_timeandname.setTextSize(18);
		}

		return convertView;
	}

	private class ViewHolder {
		//
		TextView new_wages_timeandname, new_wages_publish_nameandtime;

	}

	// 添加数据
	public void addItem(ArrayList<NewWagesBean> i) {
		wagesList.addAll(i);
	}
}
