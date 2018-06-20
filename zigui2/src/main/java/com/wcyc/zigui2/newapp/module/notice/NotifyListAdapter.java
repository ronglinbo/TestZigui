package com.wcyc.zigui2.newapp.module.notice;

import java.util.Date;
import java.util.List;

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

import com.wcyc.zigui2.utils.DataUtil;


public class NotifyListAdapter extends BaseAdapter{

	private NewNoticeBean notice;
	private List<NoticeDetail> list;
	private Context mContext;
	private String userId;


	public NotifyListAdapter(Context mContext,NewNoticeBean notice){
		this.mContext = mContext;
		this.notice = notice;
		userId = CCApplication.getInstance().getPresentUser().getUserId();
		if(notice != null)
			this.list = notice.getNoticeList();
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

	public void addItem(List<NoticeDetail> more){
		list.addAll(more);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.notify_list_item, parent,false);
		}
		ViewHolder viewHolder ;
		viewHolder = (ViewHolder) convertView.getTag();
		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.detail = (TextView) convertView.findViewById(R.id.publish_detail);
			viewHolder.unread = (ImageView) convertView.findViewById(R.id.unreadLabel);
			viewHolder.time = (TextView) convertView.findViewById(R.id.publish_time);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if("0".equals(list.get(position).isRead())
//					&&!userId.equals(list.get(position).getCreatorId())
				){//未读
			viewHolder.unread.setVisibility(View.VISIBLE);
		}else{
			viewHolder.unread.setVisibility(View.INVISIBLE);
		}
		viewHolder.title.setText(list.get(position).getNoticeTitle());
		String detail = list.get(position).getNoticeTypeStr();
		//detail += "发布于 ";
		String time = list.get(position).getNoticetime();
		//detail += DataUtil.getShowTime(time);
		viewHolder.detail.setText(detail);	//通知类型
		viewHolder.time.setText(time);		//时间

		return convertView;
	}

	public class ViewHolder{
		ImageView unread;
		TextView title;
		TextView detail;
		TextView time;
		ImageButton next;
	}
}