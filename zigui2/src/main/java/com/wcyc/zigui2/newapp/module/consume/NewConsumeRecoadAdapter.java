package com.wcyc.zigui2.newapp.module.consume;

import java.util.ArrayList;

import com.wcyc.zigui2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * 
 * @author 郑国栋 2016-6-23
 * @version 2.1
 */
public class NewConsumeRecoadAdapter extends BaseAdapter {

	private Context myContext;// 上下文
	private ArrayList<NewConsumeRecoadBean> consumeRecoadList;// 展示list

	public NewConsumeRecoadAdapter(Context myContext,
			ArrayList<NewConsumeRecoadBean> consumeRecoadList) {
		super();
		this.myContext = myContext;
		this.consumeRecoadList = consumeRecoadList;
	}

	@Override
	public int getCount() {

		if (consumeRecoadList != null) {
			return consumeRecoadList.size();// 长度
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
					R.layout.new_consume_recoad_item, parent, false);

			// 获得布局中的控件
			viewholder.new_consume_money = (TextView) convertView
					.findViewById(R.id.new_consume_money);
			viewholder.new_consume_time = (TextView) convertView
					.findViewById(R.id.new_consume_time);
			viewholder.new_consume_address = (TextView) convertView
					.findViewById(R.id.new_consume_address);

			// 设置标签
			convertView.setTag(viewholder);

		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}

		// 显示内容
		viewholder.new_consume_money.setText("-"+consumeRecoadList.get(position)
				.getConsumeNumber());
		
		viewholder.new_consume_time.setText(consumeRecoadList.get(position)
				.getUpdatetime());
		
		String consumeTpye=consumeRecoadList.get(position).getConsumeType();
		if("1".equals(consumeTpye)){
			viewholder.new_consume_address.setText("用餐消费");
		}else if("2".equals(consumeTpye)){
			viewholder.new_consume_address.setText("商店消费");
		}else if("3".equals(consumeTpye)){
			viewholder.new_consume_address.setText("用水消费");
		}else if("4".equals(consumeTpye)){
			viewholder.new_consume_address.setText("时间段充值总额");
		}

		return convertView;
	}

	private class ViewHolder {
		//
		TextView new_consume_money, new_consume_time, new_consume_address;

	}

	// 添加数据
	public void addItem(ArrayList<NewConsumeRecoadBean> i) {
		consumeRecoadList.addAll(i);
	}
}
