package com.wcyc.zigui2.newapp.module.email;

import java.util.List;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.newapp.bean.EmailBean;

import com.wcyc.zigui2.newapp.module.email.NewMailInfo.Recipient;
import com.wcyc.zigui2.newapp.module.mailbox.MailInfo;
import com.wcyc.zigui2.utils.DataUtil;


public class EmailListAdapter extends BaseAdapter{

	private List<NewMailInfo> list;
	private Context mContext;
	private int type;
	
	public EmailListAdapter(Context mContext,List<NewMailInfo> list,int type){
		this.list = list;
		this.mContext = mContext;
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

	public void setItem(List<NewMailInfo> newPage){
		list = newPage;
	}

	public void addItem(List<NewMailInfo> newPage){
		list.addAll(newPage);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.email_list_item, parent,false);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		if (viewHolder == null) {
			viewHolder = new ViewHolder();
			viewHolder.receipt = (TextView) convertView.findViewById(R.id.receipt);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
//			viewHolder.detail = (WebView) convertView.findViewById(R.id.publish_detail);
			viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
			viewHolder.unread = (ImageView) convertView.findViewById(R.id.unreadLabel);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		NewMailInfo info = list.get(position);
		String title = info.getTitle();
		if(viewHolder.title != null){
			if(DataUtil.isNullorEmpty(title)){
				viewHolder.title.setText("（无主题）");
			}else{
				viewHolder.title.setText(title);
			}
		}
		String content = info.getContent();
		if(content != null && content.length() > 0){
			CharSequence html = Html.fromHtml(content);
			viewHolder.summary.setText(html);
		}else{
			content = "（无内容摘要）";
			viewHolder.summary.setText(content);
		}
		//回收站按操作时间降序排列，其它的按创建时间降序排列
		if(type == EmailActivity.RECYCLE) {
			String updateTime = info.getUpdateTime();
			if (!DataUtil.isNullorEmpty(updateTime)) {
				if (viewHolder.time != null)
					viewHolder.time.setText(DataUtil.getShowTime(updateTime));
			} else {
				String time = info.getCreateTime();
				if (viewHolder.time != null)
					viewHolder.time.setText(DataUtil.getShowTime(time));
			}
		}else{
			String time = info.getCreateTime();
			if (viewHolder.time != null)
				viewHolder.time.setText(DataUtil.getShowTime(time));
		}
//		System.out.println("type:"+type + " isRead:"+info.getIsRead());
		if(type == EmailActivity.INBOX){
			String receipt = info.getCreateUserName();
			if(receipt != null && receipt.length() > 0){
				viewHolder.receipt.setText(receipt);
			}else{
				viewHolder.receipt.setText("(未填写收件人)");
			}
			if("0".equals(info.getIsRead())){//未读
				viewHolder.unread.setVisibility(View.VISIBLE);
			}else{
				viewHolder.unread.setVisibility(View.INVISIBLE);
			}
		}else if(type == EmailActivity.OUTBOX
				|| type == EmailActivity.DRAFT){
			//不显示cc收件人
			List<Recipient> list = info.getEmailRealationReceives();
			String receiver = EmailDetailActivity.getListStringName(list);
			SpannableString receivers = new SpannableString(receiver);
			if(receivers != null && receivers.length() > 0){
				viewHolder.receipt.setText(receivers);
			}else{
				viewHolder.receipt.setText("(未填写收件人)");
			}
		}else if(type == EmailActivity.RECYCLE){
			String receipt = info.getCreateUserName();
			if(receipt != null && receipt.length() > 0){
				viewHolder.receipt.setText(receipt);
			}else{
				viewHolder.receipt.setText("(未填写收件人)");
			}

		}
		return convertView;
	}
	
	public class ViewHolder{
		ImageView unread;
		TextView receipt;
		TextView time;
		TextView title;
//		WebView detail;
		TextView summary;
		TextView type;
		ImageButton next;
	}
}