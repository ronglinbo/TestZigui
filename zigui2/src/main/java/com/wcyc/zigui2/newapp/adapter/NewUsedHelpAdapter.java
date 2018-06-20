package com.wcyc.zigui2.newapp.adapter;

import java.util.ArrayList;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.NewUsedHelpBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 使用帮助 适配器
 * @author 郑国栋
 * 2016-4-14
 * @version 2.0
 */
public class NewUsedHelpAdapter extends BaseAdapter {
	
	private Context myContext;//上下文
	private ArrayList<NewUsedHelpBean> contentList;// 使用帮助列表
	
	

	public NewUsedHelpAdapter(ArrayList<NewUsedHelpBean> contentList) {
		super();
		this.contentList = contentList;
	}
	
	

	public NewUsedHelpAdapter(Context myContext,
			ArrayList<NewUsedHelpBean> contentList) {
		super();
		this.myContext = myContext;
		this.contentList = contentList;
	}



	public NewUsedHelpAdapter() {
		super();
	}



	@Override
	public int getCount() {
		
		return contentList.size();//长度

	}

	@Override
	public Object getItem(int position) {

		return null;//当前位置的对象
	}

	@Override
	public long getItemId(int position) {
		
		return position;//当前位置ID
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder=null;
		if(convertView==null){
			// 实例化控件
			viewholder=new ViewHolder();
			//配置单个item的布局
			convertView=LayoutInflater.from(myContext).inflate(R.layout.new_used_help_item, parent,false);
			
			//获得布局中的控件
			viewholder.question=(TextView) convertView.findViewById(R.id.new_used_help_q);
			viewholder.answer=(TextView) convertView.findViewById(R.id.new_used_help_a);
			
			//设置标签
			convertView.setTag(viewholder);
		}else{
			// 获得标签    如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		//显示内容
		viewholder.question.setText(contentList.get(position).getQuestion());
		viewholder.answer.setText(contentList.get(position).getAnswer());
		
		return convertView;
	}
	
	private class ViewHolder{
		// 
		TextView question , answer;
		
	}

}
