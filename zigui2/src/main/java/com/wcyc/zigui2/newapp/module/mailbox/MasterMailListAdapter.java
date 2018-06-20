package com.wcyc.zigui2.newapp.module.mailbox;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.newapp.bean.EmailBean;
import com.wcyc.zigui2.utils.DataUtil;

public class MasterMailListAdapter extends BaseAdapter{

	private List<MailInfo> list;
	private Context mContext;
	private boolean isAdmin;
	private int type;
	
	public MasterMailListAdapter(Context mContext,List<MailInfo> list,boolean isAdmin){
		this.list = list;
		this.mContext = mContext;
		this.isAdmin = isAdmin;
	}
	public MasterMailListAdapter(Context mContext,List<MailInfo> list,boolean isAdmin,int type){
		this.list = list;
		this.mContext = mContext;
		this.isAdmin = isAdmin;
		this.type = type;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list != null)
			return list.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setItem(List<MailInfo> list){
		this.list = list;
		/*this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();*/
	}

	public void addItem(List<MailInfo> more){
		//list.clear();
		list.addAll(more);
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.master_mail_list_item, parent,false);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		if (viewHolder == null) {
			
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.detail = (TextView) convertView.findViewById(R.id.publish_detail);
			viewHolder.type = (Button) convertView.findViewById(R.id.type);
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		MailInfo info = list.get(position);
		viewHolder.title.setText(info.getMailTile());
		String detail = info.getPublishTime();
		String type = info.getTypeName();
		viewHolder.type.setText(type);
		viewHolder.detail.setText(DataUtil.getShowTime(detail));
		setTypeColor(viewHolder.type,type);
		TextView replyed = (TextView) convertView.findViewById(R.id.replyed);
		if(!isAdmin &&"1".equals(info.getIsReply())){
			replyed.setVisibility(View.VISIBLE);
		}else{
			replyed.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private void setTypeColor(Button view,String type){
		if("问题咨询".equals(type)){
			view.setBackgroundColor(Color.rgb(96, 149, 206));
			view.setBackgroundResource(R.drawable.btn_type_question);
		}else if("投诉建议".equals(type)){
			view.setBackgroundColor(Color.rgb(79, 182, 118));
			view.setBackgroundResource(R.drawable.btn_type_suggest);
		}else if("表彰嘉奖".equals(type)){
			view.setBackgroundColor(Color.rgb(216, 102, 124));
			view.setBackgroundResource(R.drawable.btn_type_phraise);
		}else if("好人好事".equals(type)){
			view.setBackgroundColor(Color.rgb(202, 164, 62));
			view.setBackgroundResource(R.drawable.btn_type_good);
		}else if("其他".equals(type)){
			view.setBackgroundColor(Color.rgb(245, 92, 157));
			view.setBackgroundResource(R.drawable.btn_type_other);
		}
	}
	
	public class ViewHolder{
		ImageView unread;
		TextView title;
		TextView detail;
		Button type;
		ImageButton next;
	}
}