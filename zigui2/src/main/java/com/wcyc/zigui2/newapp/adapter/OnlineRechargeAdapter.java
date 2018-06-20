package com.wcyc.zigui2.newapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.OnlineRechargeRecordBean;
import com.wcyc.zigui2.newapp.module.consume.NewRechargeRecordAdapter;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.DateUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

/**
 * @author zzc
 * @time 2018/3/29
 * 线下支付记录适配器
 */
public class OnlineRechargeAdapter extends BaseAdapter {

    private List<OnlineRechargeRecordBean.CardRechargeListBean> rechargeList;
    private Context context;

    public OnlineRechargeAdapter(Context context, List<OnlineRechargeRecordBean.CardRechargeListBean> rechargeList) {
        this.rechargeList = rechargeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return rechargeList == null ? 0 : rechargeList.size();
    }

    @Override
    public OnlineRechargeRecordBean.CardRechargeListBean getItem(int position) {
        return rechargeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OnlineRechargeRecordBean.CardRechargeListBean bean = rechargeList.get(position);
        DecimalFormat df = new DecimalFormat("0.00#");
        OnlineRechargeAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new OnlineRechargeAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_online_recharge_record, parent, false);
            viewHolder.tv_recharge_money = (TextView) convertView.findViewById(R.id.tv_recharge_money);
            viewHolder.tv_recharge_sum = (TextView) convertView.findViewById(R.id.tv_recharge_sum);
            viewHolder.tv_pay_method = (TextView) convertView.findViewById(R.id.tv_pay_method);
            viewHolder.tv_pay_result = (TextView) convertView.findViewById(R.id.tv_pay_result);
            viewHolder.tv_order_no = (TextView) convertView.findViewById(R.id.tv_order_no);
            viewHolder.tv_order_time = (TextView) convertView.findViewById(R.id.tv_order_time);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OnlineRechargeAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.tv_recharge_money.setText("¥ " + df.format(bean.getFullAmount()));
        viewHolder.tv_recharge_sum.setText("¥ " + df.format(bean.getOrderAmount()));


        String operateTime = bean.getUpdateTime();

        String currentWeekOfDay = DateUtils.getInstance().getCurrentWeekOfDay(operateTime);
        System.out.println(currentWeekOfDay);

        if (!DataUtil.isNullorEmpty(operateTime)) {
            //判断不是本周
            if (DataUtil.isNullorEmpty(currentWeekOfDay)) {
                operateTime = operateTime.substring(0, operateTime.lastIndexOf(":"));
                viewHolder.tv_time.setText(operateTime);
            } else {
                operateTime = operateTime.substring(0, operateTime.lastIndexOf(":"));
                String substring = operateTime.substring(10, 16);
                System.out.println(substring);
                viewHolder.tv_time.setText(currentWeekOfDay + " " + substring);
            }
        } else {
            viewHolder.tv_time.setVisibility(View.GONE);
        }


        if ("1".equals(bean.getPayType())) {
            viewHolder.tv_pay_method.setText("支付宝");
        } else {
            viewHolder.tv_pay_method.setText("其他");
        }

        //支付结果(0:生成订单，未支付  1：支付成功  2 支付失败 3 失效)
        int status = bean.getStatus();
        if (0 == status) {
            viewHolder.tv_pay_result.setText("未支付");
        } else if (1 == status) {
            viewHolder.tv_pay_result.setText("成功");
        } else if (2 == status) {
            viewHolder.tv_pay_result.setText("失败");
        } else if (3 == status) {
            viewHolder.tv_pay_result.setText("失效");
        } else {
            viewHolder.tv_pay_result.setText("其他");
        }

        viewHolder.tv_order_no.setText(bean.getOrderSn());
        viewHolder.tv_order_time.setText(bean.getCreateTime());

        return convertView;
    }

    public void addItem(List<OnlineRechargeRecordBean.CardRechargeListBean> cardRechargeList) {
        rechargeList.addAll(cardRechargeList);
    }

    public class ViewHolder {
        TextView tv_recharge_money;
        TextView tv_recharge_sum;
        TextView tv_pay_method;
        TextView tv_pay_result;
        TextView tv_order_no;
        TextView tv_order_time;
        TextView tv_time;
    }
}
