package com.wcyc.zigui2.newapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.wcyc.zigui2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DynamicsAdapter extends BaseAdapter{

	ArrayList<HashMap<String,Object>> list;
	Context mContext;
	private boolean quickPulish = false;
	
	@Override
	public int getCount() {
		if(list != null){
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ImageView icon;
		TextView text;
		if(arg1 == null){
			if(quickPulish){
				arg1 = LayoutInflater.from(mContext).inflate(R.layout.quick_service_menu_item,arg2,false);
			}else{
				arg1 = LayoutInflater.from(mContext).inflate(R.layout.new_service_menu_item,arg2,false);
			}
			icon = (ImageView) arg1.findViewById(R.id.imageView);
			text = (TextView) arg1.findViewById(R.id.textView);
			HashMap<String,Object> item = list.get(arg0);
			icon.setScaleType(ImageView.ScaleType.CENTER);
			icon.setPadding(2, 2, 2, 2);
			icon.setImageResource((Integer) item.get("ImageView"));
			text.setText((CharSequence) item.get("TextView"));
			arg1.setTag(icon);
		}else{
		}
		return arg1;
	}
	public DynamicsAdapter(Context mContext,ArrayList<HashMap<String,Object>> list){
		this.mContext = mContext;
		this.list = list;
	}
	
	public DynamicsAdapter(Context mContext,ArrayList<HashMap<String,Object>> list,boolean quickPulish){
		this.mContext = mContext;
		this.list = list;
		this.quickPulish = quickPulish;
	}
}