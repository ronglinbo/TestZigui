package com.wcyc.zigui2.newapp.module.consume;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wcyc.zigui2.R;

import java.util.ArrayList;

/**
 * Created by xiehua on 2017/5/4.
 */

public class NewRechargeRecordAdapter extends BaseAdapter {

    private Context myContext;// 上下文
    private ArrayList<NewRechargeRecordBean> cardConsumeList;

    public NewRechargeRecordAdapter(Context myContext,
                                    ArrayList<NewRechargeRecordBean> cardConsumeList) {
        super();
        this.myContext = myContext;
        this.cardConsumeList = cardConsumeList;
    }

    @Override
    public int getCount() {
        if (cardConsumeList != null) {
            return cardConsumeList.size();
        }
        return 0;
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
        if (convertView == null) {
            //实例化控件
            viewholder = new ViewHolder();
            //配置单个item的布局
            convertView = LayoutInflater.from(myContext).inflate(
                    R.layout.new_recharge_record_item, parent, false);

            //获取布局中的控件
            viewholder.offline_recharge = (TextView) convertView.findViewById(R.id.offline_recharge);
            viewholder.recharge_money = (TextView)convertView.findViewById(R.id.recharge_money);
            viewholder.recharge_time = (TextView)convertView.findViewById(R.id.recharge_time);
            viewholder.recharge_balance = (TextView)convertView.findViewById(R.id.recharge_balance);

            // 设置标签
            convertView.setTag(viewholder);

        }else {
            // 获得标签 如果已经实例化则用历史记录
            viewholder = (NewRechargeRecordAdapter.ViewHolder) convertView.getTag();
        }

        //显示内容
        viewholder.recharge_money.setText("+￥" + cardConsumeList.get(position).getAddmoney());      //充值金额
        String time = cardConsumeList.get(position).getUpdatetime();
        String Updatetime = time.substring(0,time.length()-3);
        viewholder.recharge_time.setText(Updatetime);            //充值时间
        viewholder.recharge_balance.setText("充值后余额: ￥" + cardConsumeList.get(position).getCardRemaining()); //充值余额

        String consumeType = cardConsumeList.get(position).getConsumeType();
        if ("4".equals(consumeType)) {
            viewholder.offline_recharge.setText("线下充值");
        }else {
            viewholder.offline_recharge.setText("线上充值");
        }

        return convertView;
    }

    private class ViewHolder {
        /** 充值类型 */
        TextView offline_recharge;
        /** 充值金额 */
        TextView recharge_money;
        /** 充值时间 */
        TextView recharge_time;
        /** 充值余额 */
        TextView recharge_balance;
    }

    // 添加数据
    public void addItem(ArrayList<NewRechargeRecordBean> i) {
        cardConsumeList.addAll(i);
    }
}
