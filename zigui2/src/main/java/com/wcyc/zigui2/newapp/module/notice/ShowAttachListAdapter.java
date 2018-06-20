package com.wcyc.zigui2.newapp.module.notice;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;
import com.wcyc.zigui2.newapp.module.email.NewMailInfo;
import com.wcyc.zigui2.newapp.widget.AttachmentActionOption;
import com.wcyc.zigui2.utils.ImageUtils;
//通知详情中显示的附件列表
public class ShowAttachListAdapter extends BaseAdapter{
	private List<Attachment> list;
	private Context mContext;
	private String function;
	
	public ShowAttachListAdapter(Context mContext,List<Attachment> list){
		this.mContext = mContext;
		this.list = list;
	}
	public ShowAttachListAdapter(Context mContext,List<Attachment> list,String function){
		this.mContext = mContext;
		this.list = list;
		this.function = function;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list != null)
			return list.size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, final ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(arg1 == null){
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.show_attachment_list_item, null);
		}
		TextView attach1 = (TextView) arg1.findViewById(R.id.attach1);
		final TextView attach2 = (TextView) arg1.findViewById(R.id.attach2);
		attach1.setText(list.get(arg0).getAttachementName());
//		attach1.setAutoLinkMask(Linkify.ALL);
		int color = mContext.getResources().getColor(R.color.font_darkblue);
		attach1.setTextColor(color);
		if("summary".equals(function) || "dailyRecord".equals(function)){

		}else{
			attach1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
			attach1.getPaint().setAntiAlias(true);//抗锯齿
			attach1.getPaint().setColor(color);
		}
		attach1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				handleOnClick(list.get(arg0),view);
			}
			
		});
		return arg1;
	}
	
	public void handleOnClick(Attachment attach,View view){
		if(ImageUtils.isImage(attach.getAttachementName())){
			ImageUtils.showImage(mContext,attach.getAttachementUrl());
		}else{
			AttachmentActionOption option = new AttachmentActionOption(mContext,attach);
			option.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	}
	
}