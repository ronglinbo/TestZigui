package com.wcyc.zigui2.newapp.module.wages;

import java.util.ArrayList;

import com.wcyc.zigui2.R;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 工资条详情Adapter
 * @author 郑国栋
 * 2016-7-14
 * @version 2.0
 */
public class NewWagesDetailsAdapter extends BaseAdapter  {

	private Context myContext;// 上下文
	private ArrayList<NewWagesDetailsBean> wagesDetailsList;// 工资条list
	
	
	public NewWagesDetailsAdapter(Context myContext, ArrayList<NewWagesDetailsBean> wagesDetailsList) {
		super();
		this.myContext = myContext;
		this.wagesDetailsList = wagesDetailsList;
	}
	
	@Override
	public int getCount() {
		if (wagesDetailsList != null) {

			return wagesDetailsList.size();// 长度
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
					R.layout.new_wages_details_item, parent, false);

			// 获得布局中的控件
			viewholder.new_wages_details_name_tv = (TextView) convertView
					.findViewById(R.id.new_wages_details_name_tv);// 工资详情如应发合计名称
			viewholder.new_wages_details_money_tv = (TextView) convertView
					.findViewById(R.id.new_wages_details_money_tv);// 工资详情如应发金额 5000

			// 设置标签
			convertView.setTag(viewholder);

		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		viewholder.new_wages_details_name_tv.setText(wagesDetailsList.get(position).getKey());
		viewholder.new_wages_details_money_tv.setText("¥"+wagesDetailsList.get(position).getValue());
		return convertView;
	}

	
	private class ViewHolder {
		//
		TextView new_wages_details_name_tv, new_wages_details_money_tv;

	}

	// 添加数据
	public void addItem(ArrayList<NewWagesDetailsBean> i) {
		wagesDetailsList.addAll(i);
	}
	
	
}
