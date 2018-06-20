package com.wcyc.zigui2.newapp.adapter;

import java.util.List;
import java.util.Map;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PointBean;
import com.wcyc.zigui2.newapp.bean.NewPointBean;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * @author yytan
 * @version
 * @since 2016-4-5
 */
public class LoveNameAdapter extends BaseAdapter{
	private Context mContext;
	private List<NewPointBean> data_list;
	public LoveNameAdapter(Context mContext,List<NewPointBean> data_list){
		super();
		this.mContext = mContext;
		this.data_list = data_list;
	}

	@Override
	public int getCount() {
		return data_list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		if(convertView == null){
			viewholder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.new_love_name,parent,false);
			viewholder.loveName = (TextView) convertView.findViewById(R.id.loveName);
			
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder) convertView.getTag();
		}
        
		viewholder.loveName.setText(data_list.get(position).getCommentUserName());
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView loveName;
	}
}