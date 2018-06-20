package com.wcyc.zigui2.newapp.adapter;
/*
* 文 件 名:NewPaymentRecordsAdapter.javas

*/
import java.util.ArrayList;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.OrderInfoResult;
import com.wcyc.zigui2.bean.OrderResult;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge.NewCreateOrderResult;
import com.wcyc.zigui2.newapp.module.charge.NewPaymentRecordActivity;
import com.wcyc.zigui2.newapp.module.charge.NewOrderDetailList.OrderDetail;
import com.wcyc.zigui2.newapp.module.charge.NewOrderInfoList.OrderInfo;
import com.wcyc.zigui2.utils.DataUtil;
/**
 */
public class NewPaymentRecordsAdapter extends BaseAdapter {
	
	private ArrayList<OrderDetail> detailList;
	private ArrayList<OrderInfo> list;
	private LayoutInflater inflater;
	private NewPaymentRecordActivity activity;
	//支付状态(0:生成订单，未支付  1：支付成功  2 支付失败 3 失效)
	private final int SUCCESS = 1;
	private final int FAIL = 2;
	private final int INVAILD = 3;
	private final int UNFINISHED = 0;
	
	public NewPaymentRecordsAdapter(NewPaymentRecordActivity activity,
			ArrayList<OrderDetail> detailList,ArrayList<OrderInfo> list){
		this.activity = activity;
		inflater = LayoutInflater.from(activity);
		this.list = list;
		this.detailList = detailList;
	}

	@Override
	public int getCount() {
		if(detailList != null)
			return detailList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(detailList != null)
			return detailList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
//		if(convertView == null){
			convertView = inflater.inflate(R.layout.new_payment_record_item, parent , false);
			holder = new ViewHolder();
			holder.tv_repayment = (TextView) convertView.findViewById(R.id.tv_repayment);
			holder.tv_payment_money = (TextView) convertView.findViewById(R.id.tv_payment_money);
//			holder.tv_renew_fee_duration = (TextView) convertView.findViewById(R.id.tv_renew_fee_duration);
			holder.tv_record_orders_status = (TextView) convertView.findViewById(R.id.tv_record_orders_status);
			holder.tv_renew_fee = (TextView) convertView.findViewById(R.id.tv_renew_fee);
			holder.tv_record_ordertime = (TextView) convertView.findViewById(R.id.tv_record_ordertime);
			holder.tv_record_ordernum = (TextView) convertView.findViewById(R.id.tv_record_ordernum);
			holder.payment_start_stop_tv = (TextView) convertView.findViewById(R.id.payment_start_stop_tv);
//			convertView.setTag(holder);
//		}else{
//			holder = (ViewHolder) convertView.getTag();
//		}
		initDataToView(holder , position);
		return convertView;
	}

	private void initDataToView(ViewHolder holder, int position) {
		final OrderDetail mOrderDetail = detailList.get(position);
		final OrderInfo mRenewOrder = list.get(position);
		if(mRenewOrder.getStatus() == SUCCESS){
			holder.tv_repayment.setTextColor(activity.getResources().getColorStateList(R.color.font_lightgray));
			holder.tv_repayment.setText("已支付");
			holder.tv_repayment.setVisibility(View.VISIBLE);
			holder.tv_repayment.setEnabled(false);
		}else if(mRenewOrder.getStatus() == INVAILD ){
			holder.tv_repayment.setTextColor(activity.getResources().getColorStateList(R.color.font_lightgray));
			holder.tv_repayment.setText("已失效");
			holder.tv_repayment.setVisibility(View.VISIBLE);
			holder.tv_repayment.setEnabled(false);
		}else if(mRenewOrder.getStatus() == FAIL){
			holder.tv_repayment.setTextColor(activity.getResources().getColorStateList(R.color.font_lightgray));
			holder.tv_repayment.setText("支付失败");
			holder.tv_repayment.setEnabled(false);
			holder.tv_repayment.setVisibility(View.VISIBLE);
		}else if(mRenewOrder.getStatus() == UNFINISHED){
			holder.tv_repayment.setTextColor(activity.getResources().getColorStateList(R.color.font_blue));
			holder.tv_repayment.setText(R.string.title_pay_fail);
			holder.tv_repayment.setVisibility(View.VISIBLE);
		}
		long amount = mOrderDetail.getAmount();
		String actual = DataUtil.convertF2Y(amount);
		holder.tv_payment_money.setText("¥"+actual);
//		holder.tv_renew_fee_duration.setText(mRenewOrder.getRechargeTime()+"个月");
		holder.tv_renew_fee.setText(mOrderDetail.getProductName());
		holder.tv_record_ordertime.setText(mOrderDetail.getCreateDate());
		holder.tv_record_ordernum.setText(mRenewOrder.getOrderNo()+"");
		String endDate = mOrderDetail.getEndDate();
		if(DataUtil.isNullorEmpty(endDate)){
			UserType user = CCApplication.app.getPresentUser();
			String childId = user.getChildId();
			endDate = DataUtil.getGraduateDate(childId);
		}
		String startDate = DataUtil.getDateTime(mOrderDetail.getStartDate(), 1);
		endDate = DataUtil.getDateTime(endDate, 1);
		holder.payment_start_stop_tv.setText(startDate+"至"+endDate);
	}

	class ViewHolder{
		TextView tv_repayment;
		TextView tv_payment_money;
		TextView tv_renew_fee_duration;
		TextView tv_record_orders_status;
		TextView tv_renew_fee;
		TextView tv_record_ordertime;
		TextView tv_record_ordernum;
		TextView payment_start_stop_tv;
	}
}
