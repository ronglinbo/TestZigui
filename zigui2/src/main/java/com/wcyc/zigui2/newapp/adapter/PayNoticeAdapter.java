package com.wcyc.zigui2.newapp.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.PayNoticeActivity;
import com.wcyc.zigui2.newapp.activity.SystemMessageActivity;
import com.wcyc.zigui2.newapp.activity.SystemMessageDetailActivity;
import com.wcyc.zigui2.newapp.bean.PayNoticeListBean;
import com.wcyc.zigui2.newapp.bean.SystemMessageListBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

import java.util.List;

/**
 * 缴费提醒
 */
public class PayNoticeAdapter extends BaseAdapter {


    private static final int SYSTEM_MESSAGE_DETAILS_CODE = 100;

    private List<PayNoticeListBean.MsgListBean> messageInfoList;
    private PayNoticeActivity context;

    public PayNoticeAdapter(List<PayNoticeListBean.MsgListBean> messageInfoList, PayNoticeActivity context) {
        this.messageInfoList = messageInfoList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return messageInfoList == null ? 0 : messageInfoList.size();
    }

    @Override
    public PayNoticeListBean.MsgListBean getItem(int position) {
        return messageInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        PayNoticeAdapter.ViewHolder viewHolder = null;
        final PayNoticeListBean.MsgListBean messageBean = messageInfoList.get(position);

        if (convertView == null) {
            viewHolder = new PayNoticeAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_system_info, parent, false);

            viewHolder.tv_out_time = (TextView) convertView
                    .findViewById(R.id.tv_out_time);//
            viewHolder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            viewHolder.tv_type = (TextView) convertView
                    .findViewById(R.id.tv_type);
            viewHolder.rl_detail = (RelativeLayout) convertView
                    .findViewById(R.id.rl_detail);//
            viewHolder.iv_state = (ImageView) convertView
                    .findViewById(R.id.iv_state);
            viewHolder.iv_unRead = (ImageView) convertView
                    .findViewById(R.id.iv_unRead);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PayNoticeAdapter.ViewHolder) convertView.getTag();
        }

        //不需要秒
        String operateTime = messageBean.getOperateTime();
        if (!DataUtil.isNullorEmpty(operateTime)) {
            operateTime = operateTime.substring(0, operateTime.lastIndexOf(":"));
            viewHolder.tv_time.setText(operateTime);
        }


        viewHolder.tv_type.setText(messageBean.getTypeName());

        //获取到标题
        viewHolder.tv_title.setText(messageBean.getTitle());

        //回执状态Gone
        viewHolder.iv_state.setVisibility(View.GONE);

        //过期状态Gone
        int status = messageBean.getStatus();
        if (status == 3) {
            viewHolder.tv_out_time.setVisibility(View.VISIBLE);
            viewHolder.tv_out_time.setText("已失效");
        } else {
            viewHolder.tv_out_time.setVisibility(View.GONE);
        }

        //是否已读
        int readStatus = messageBean.getReadStatus();
        if (readStatus == 0) {
            viewHolder.iv_unRead.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_unRead.setVisibility(View.GONE);
        }

        //拼接地址
        final String detailURL = new StringBuilder(Constants.URL)
                .append(Constants.PAY_NOTICE_DETAIL_URL)
                .append("recordId=")
                .append(messageBean.getId()).toString();


        // 跳转到相应的详情
        viewHolder.rl_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("url", detailURL);
                bundle.putInt("position", position);
                intent.setClass(context, SystemMessageDetailActivity.class);
                intent.putExtras(bundle);
                context.startActivityForResult(intent, SYSTEM_MESSAGE_DETAILS_CODE);
                System.out.println("跳转到具体详情页面地址: " + detailURL);
            }
        });
        return convertView;
    }


    public void addItem(List<PayNoticeListBean.MsgListBean> more_data) {
        messageInfoList.addAll(more_data);
    }

    public void RefreshData(List<PayNoticeListBean.MsgListBean> list) {
        this.messageInfoList = list;
    }


    private class ViewHolder {

        private ImageView iv_unRead;

        //发布时间
        private TextView tv_time;

        //致家长书
        private TextView tv_type;

        //标题
        private TextView tv_title;

        //是否回执
        private ImageView iv_state;

        //查看详情
        private RelativeLayout rl_detail;

        //是否过期
        private TextView tv_out_time;
    }

}
