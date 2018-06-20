package com.wcyc.zigui2.newapp.module.summary;

import java.util.Date;
import java.util.List;

import org.apache.http.impl.cookie.DateUtils;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.module.notice.NewNoticeBean;
import com.wcyc.zigui2.newapp.module.notice.NoticeDetail;
import com.wcyc.zigui2.newapp.module.summary.SummaryBean.SummaryDetail;
import com.wcyc.zigui2.utils.DataUtil;


public class SummaryListAdapter extends BaseAdapter{

	private SummaryBean summary;
	private List<SummaryDetail> list;
	private Context mContext;
	private String userId;
	
	
	public SummaryListAdapter(Context mContext,SummaryBean summary){
		this.mContext = mContext;
		this.summary = summary;
		userId = CCApplication.getInstance().getPresentUser().getUserId();
		if(summary != null)
			this.list = summary.getSummaryList();
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

	public void addItem(List<SummaryDetail> more){
		list.addAll(more);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.notify_list_item, parent,false);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.detail = (TextView) convertView.findViewById(R.id.publish_detail);
			viewHolder.unread = (ImageView) convertView.findViewById(R.id.unreadLabel);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if("0".equals(list.get(position).getIsRead())
//					&&!userId.equals(list.get(position).getCreatorId())
				){//未读
			viewHolder.unread.setVisibility(View.VISIBLE);
		}else{
			viewHolder.unread.setVisibility(View.INVISIBLE);
		}
		viewHolder.title.setText(list.get(position).getSummaryTitle());
		String detail = list.get(position).getCreatorName();
		detail += "发布于 ";
		String time = list.get(position).getSummaryTime();
		System.out.println(time);
		if(!DataUtil.isNullorEmpty(time)){
			time = DataUtil.formatDate(time);
			detail += time;		
		}
		viewHolder.detail.setText(detail);
		
		return convertView;
	}
	
	public class ViewHolder{
		ImageView unread;
		TextView title;
		TextView detail;
		ImageButton next;
	}
}