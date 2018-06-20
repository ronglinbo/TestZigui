package com.wcyc.zigui2.newapp.module.charge;

import java.math.BigDecimal;
import java.util.List;

import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge.ChargeProduct.Product;
import com.wcyc.zigui2.utils.DataUtil;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderPackageAdapter extends BaseAdapter{
	private List<Product> list;
	private Context mContext;
	
	public OrderPackageAdapter(Context mContext,List<Product> list){
		this.list = list;
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list != null){
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(list != null){
			return list.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		Product info = list.get(arg0);
		String startDate = null;
		String ret[];
		if(arg1 == null){
			
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.package_list_item, null);
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.name.setText(info.getProductName());
			holder.price = (TextView) arg1.findViewById(R.id.price);
			ret = DataUtil.computeStartDate(mContext);
			startDate = ret[0];
			long months = DataUtil.computeMonths(info,startDate);
			long amount = info.getActualAmount()*months;
			long originAmount = info.getAmount()*months;
			
			String actual = DataUtil.convertF2Y(amount);
			holder.price.setText("¥"+actual);
			holder.date = (TextView) arg1.findViewById(R.id.date);
		
			String origin = DataUtil.convertF2Y(originAmount);
			holder.originPrice = (TextView) arg1.findViewById(R.id.origin_price);
			holder.originPrice.setText("原价"+origin+"元");
			//设置中划线并加清晰
			holder.originPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);  
			setDate(info,holder,startDate);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		
		return arg1;
	}
	
	
	private void setDate(Product productItem,ViewHolder holder,String startDate){
		String endDate = "",dateValue,dateType;
		String[] startDateTime,endDateTime;
		UserType user = CCApplication.getInstance().getPresentUser();
		dateValue = productItem.getValidityDateValue();
		dateType = productItem.getValidityDateType();
		
		if(!DataUtil.isNullorEmpty(dateValue) && "null".equals(dateValue) == false){
			endDate = DataUtil.getLastDate(startDate, dateValue, dateType);//需要判断闰月
		}else if(DataUtil.isNullorEmpty(dateValue)){
			String childId = user.getChildId();
			endDate = DataUtil.getGraduateDate(childId);
		}
		startDateTime = startDate.split(" ");
		endDateTime = endDate.split(" ");
		holder.date.setText(startDateTime[0]+"至"+endDateTime[0]);
		System.out.println("endDate:"+endDate);
	}
	private static class ViewHolder {
		TextView name,price,date,originPrice;
	}
}