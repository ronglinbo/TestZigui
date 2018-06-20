package com.wcyc.zigui2.newapp.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.PaymentDetailActivity;
import com.wcyc.zigui2.newapp.activity.PaymentH5Activity;
import com.wcyc.zigui2.newapp.activity.PaymentListActivity;
import com.wcyc.zigui2.newapp.activity.SystemMessageDetailActivity;
import com.wcyc.zigui2.newapp.bean.PaymentListBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.DateUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 缴费列表适配器
 */
public class PaymentListAdapter extends BaseAdapter {

    private static final int PAYMENT_H5_DETAIL = 100;
    private List<PaymentListBean.ListBean> messageInfoList;
    private PaymentListActivity context;

    public PaymentListAdapter(List<PaymentListBean.ListBean> messageInfoList, PaymentListActivity context) {
        this.messageInfoList = messageInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messageInfoList == null ? 0 : messageInfoList.size();
    }

    @Override
    public PaymentListBean.ListBean getItem(int position) {
        return messageInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        PaymentListAdapter.ViewHolder viewHolder = null;
        final PaymentListBean.ListBean messageBean = messageInfoList.get(position);
        DecimalFormat df = new DecimalFormat("0.00#");
        if (convertView == null) {
            viewHolder = new PaymentListAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_payment_info, parent, false);

            viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            viewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            viewHolder.tv_payMoney = (TextView) convertView.findViewById(R.id.tv_payMoney);
            viewHolder.tv_payType = (TextView) convertView.findViewById(R.id.tv_payType);
            viewHolder.tv_lastTime = (TextView) convertView.findViewById(R.id.tv_lastTime);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_payTime = (TextView) convertView.findViewById(R.id.tv_payTime);
            viewHolder.tv_orderNo = (TextView) convertView.findViewById(R.id.tv_orderNo);
            viewHolder.tv_payModel = (TextView) convertView.findViewById(R.id.tv_payModel);
            viewHolder.iv_state = (ImageView) convertView.findViewById(R.id.iv_state);
            viewHolder.tv_goPay = (TextView) convertView.findViewById(R.id.tv_goPay);

            viewHolder.ll_payMoney = (LinearLayout) convertView.findViewById(R.id.ll_payMoney);
            viewHolder.ll_payType = (LinearLayout) convertView.findViewById(R.id.ll_payType);
            viewHolder.ll_lastTime = (LinearLayout) convertView.findViewById(R.id.ll_lastTime);
            viewHolder.ll_payTime = (LinearLayout) convertView.findViewById(R.id.ll_payTime);
            viewHolder.ll_payModel = (LinearLayout) convertView.findViewById(R.id.ll_payModel);
            viewHolder.ll_orderNo = (LinearLayout) convertView.findViewById(R.id.ll_orderNo);

            viewHolder.rl_detail = (RelativeLayout) convertView.findViewById(R.id.rl_detail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PaymentListAdapter.ViewHolder) convertView.getTag();
        }

        String operateTime = messageBean.getPublishTime();
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

        //支付状态
        int status = messageBean.getStatus();
        //待支付
        if (status == 0) {
            viewHolder.ll_payType.setVisibility(View.VISIBLE);
            viewHolder.ll_lastTime.setVisibility(View.VISIBLE);
            viewHolder.ll_payMoney.setVisibility(View.GONE);
            viewHolder.ll_payTime.setVisibility(View.GONE);
            viewHolder.ll_payModel.setVisibility(View.GONE);
            viewHolder.ll_orderNo.setVisibility(View.GONE);
            viewHolder.iv_state.setVisibility(View.GONE);
            viewHolder.tv_goPay.setVisibility(View.VISIBLE);
            viewHolder.tv_goPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataUtil.getToastShort("去支付");
                }
            });
        }

        //已支付
        if (status == 1) {
            viewHolder.ll_payType.setVisibility(View.VISIBLE);
            viewHolder.ll_lastTime.setVisibility(View.VISIBLE);
            viewHolder.ll_payMoney.setVisibility(View.VISIBLE);
            viewHolder.ll_payTime.setVisibility(View.VISIBLE);
            viewHolder.ll_payModel.setVisibility(View.VISIBLE);
            viewHolder.ll_orderNo.setVisibility(View.VISIBLE);
            viewHolder.tv_goPay.setVisibility(View.GONE);
            viewHolder.iv_state.setVisibility(View.VISIBLE);

            viewHolder.tv_payMoney.setText(df.format(messageBean.getPayAmount()) + "(含手续费)");
            viewHolder.tv_payTime.setText(messageBean.getPayTime());
            viewHolder.tv_orderNo.setText(messageBean.getOrderSn());
            viewHolder.tv_payModel.setText(messageBean.getPayChannel());
            viewHolder.iv_state.setImageResource(R.drawable.icon_yijiaofei);
        }

        //已失效
        if (status == 2) {
            viewHolder.ll_payType.setVisibility(View.VISIBLE);
            viewHolder.ll_lastTime.setVisibility(View.VISIBLE);
            viewHolder.ll_payMoney.setVisibility(View.GONE);
            viewHolder.ll_payTime.setVisibility(View.GONE);
            viewHolder.ll_payModel.setVisibility(View.GONE);
            viewHolder.ll_orderNo.setVisibility(View.GONE);
            viewHolder.tv_goPay.setVisibility(View.GONE);
            viewHolder.iv_state.setVisibility(View.VISIBLE);
            viewHolder.iv_state.setImageResource(R.drawable.icon_yishixiao);
        }

        viewHolder.tv_type.setText(messageBean.getPayName());


        //支付金额

        String format = df.format(messageBean.getAmount());
        viewHolder.tv_money.setText(format);

        viewHolder.tv_payType.setText(messageBean.getPayTypeName());
        viewHolder.tv_lastTime.setText(messageBean.getEndTime());

        String userId = CCApplication.getInstance().getPresentUser().getUserId();
        String userType = CCApplication.getInstance().getPresentUser().getUserType();
        String studentId = "";
        if (userType.equals("3")) {
            studentId = CCApplication.getInstance().getPresentUser().getChildId();
        }
        //拼接地址
        final String goPayDetail = new StringBuilder(Constants.URL)
                .append(Constants.PAYMENT_GO_PAY)
                .append("userId=")
                .append(userId)
                .append("&userType=")
                .append(userType)
                .append("&studentId=")
                .append(studentId)
                .append("&publishPayId=")
                .append(messageBean.getPublishId()).toString();


        final String detailURL = new StringBuilder(Constants.URL)
                .append(Constants.PAYMENT_DETAIL_INFO)
                .append("publishPayId=")
                .append(messageBean.getPublishId()).toString();

        // 去支付
        viewHolder.tv_goPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("url", goPayDetail);
                bundle.putInt("position", position);
                intent.setClass(context, PaymentH5Activity.class);
                intent.putExtras(bundle);
                context.startActivityForResult(intent, PAYMENT_H5_DETAIL);
                System.out.println("支付页面: " + goPayDetail);
            }
        });


        //缴费详情页面
        viewHolder.rl_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("url", detailURL);
                System.out.println("url:" + detailURL);
                context.newActivity(PaymentDetailActivity.class, bundle);
            }
        });


        return convertView;
    }


    public void addItem(List<PaymentListBean.ListBean> more_data) {
        messageInfoList.addAll(more_data);
    }

    /**
     * 刷新数据
     *
     * @param list
     */
    public void RefreshData(List<PaymentListBean.ListBean> list) {
        this.messageInfoList = list;
    }


    private class ViewHolder {

        private TextView tv_type;

        private TextView tv_money;

        private TextView tv_payMoney;

        private TextView tv_payType;

        private TextView tv_lastTime;

        private TextView tv_time;

        private TextView tv_payTime;

        private TextView tv_orderNo;

        private TextView tv_payModel;

        private ImageView iv_state;

        private TextView tv_goPay;

        private LinearLayout ll_payMoney;
        private LinearLayout ll_payType;
        private LinearLayout ll_lastTime;
        private LinearLayout ll_payTime;
        private LinearLayout ll_payModel;
        private LinearLayout ll_orderNo;
        private RelativeLayout rl_detail;
    }

}
