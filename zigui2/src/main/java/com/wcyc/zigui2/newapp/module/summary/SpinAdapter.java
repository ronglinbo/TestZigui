package com.wcyc.zigui2.newapp.module.summary;

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

import com.wcyc.zigui2.newapp.module.dailyrecord.GetDeptInfo.DepartInfo;
import com.wcyc.zigui2.newapp.module.dailyrecord.PublishDailyRecordActivity;
import com.wcyc.zigui2.newapp.module.notice.NewNoticeBean;
import com.wcyc.zigui2.newapp.module.notice.NoticeDetail;
import com.wcyc.zigui2.newapp.module.summary.SummaryBean.SummaryDetail;
import com.wcyc.zigui2.utils.DataUtil;


public class SpinAdapter extends BaseAdapter{

	private Context mContext;
	private List<DepartInfo> list;
	
	public SpinAdapter(Context mContext,List<DepartInfo> list){
		this.mContext = mContext;
		this.list = list;
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


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.dept_list_item, parent, false);
			holder=new ViewHolder();

		}else{
			holder = (ViewHolder) convertView.getTag();
		}


		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.name.setText(((DepartInfo) getItem(position)).getDepartmentName());
		convertView.setTag(holder);

		return convertView;

	}
	
	public class ViewHolder{
		TextView name;
	}
}