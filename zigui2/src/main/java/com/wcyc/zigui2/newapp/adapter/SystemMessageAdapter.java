package com.wcyc.zigui2.newapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.SystemMessageActivity;
import com.wcyc.zigui2.newapp.activity.SystemMessageDetailActivity;
import com.wcyc.zigui2.newapp.bean.SystemMessageListBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;


import java.util.List;

/**
 * 系统消息的适配器
 *
 * @author zzc
 * @time 2017/12/20 0020
 */
public class SystemMessageAdapter extends BaseAdapter {


    private static final int SYSTEM_MESSAGE_DETAILS_CODE = 100;

    private List<SystemMessageListBean.MsgListBean> messageInfoList;
    private SystemMessageActivity context;

    public SystemMessageAdapter(List<SystemMessageListBean.MsgListBean> messageInfoList, SystemMessageActivity context) {
        this.messageInfoList = messageInfoList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return messageInfoList == null ? 0 : messageInfoList.size();
    }

    @Override
    public SystemMessageListBean.MsgListBean getItem(int position) {
        return messageInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        SystemMessageAdapter.ViewHolder viewHolder = null;
        final SystemMessageListBean.MsgListBean messageBean = messageInfoList.get(position);

        if (convertView == null) {
            viewHolder = new SystemMessageAdapter.ViewHolder();
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
            viewHolder = (SystemMessageAdapter.ViewHolder) convertView.getTag();
        }

        //不需要秒
        String operateTime = messageBean.getOperateTime();
        if (!DataUtil.isNullorEmpty(operateTime)) {
            operateTime = operateTime.substring(0, operateTime.lastIndexOf(":"));
            viewHolder.tv_time.setText(operateTime);
        }
        viewHolder.tv_type.setText(messageBean.getTypeName());
        viewHolder.tv_title.setText(messageBean.getTitle());

        //是否需要回执
        String receiptType = messageBean.getReceiptType();
        //是否已回执
        int receiptStatus = messageBean.getReceiptStatus();
        //显示回执状态
        if (receiptType.equals("1") && receiptStatus == 1) {
            viewHolder.iv_state.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_state.setVisibility(View.GONE);
        }

        //是否过期
        String status = messageBean.getStatus();
        if(!status.equals("3")){
            viewHolder.tv_out_time.setVisibility(View.GONE);
        }else{
            viewHolder.tv_out_time.setVisibility(View.VISIBLE);
            viewHolder.iv_state.setVisibility(View.GONE);
        }

        //未读状态
        int readStatus = messageBean.getReadStatus();
        if (readStatus == 0) {
            viewHolder.iv_unRead.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_unRead.setVisibility(View.GONE);
        }

        String userId = CCApplication.getInstance().getPresentUser().getUserId();
        String userType = CCApplication.getInstance().getPresentUser().getUserType();

        String studentId ="";
        if (userType.equals("3")) {
            studentId = CCApplication.getInstance().getPresentUser().getChildId();
        }


        //拼接地址
        final String detailURL = new StringBuilder(Constants.URL)
                .append(Constants.SYSTEM_MESSAGE_DETAIL_URL)
                .append("userId=")
                .append(userId)
                .append("&userType=")
                .append(userType)
                .append("&studentId=")
                .append(studentId)
                .append("&messageId=")
                .append(messageBean.getId()).toString();


        // 跳转到相应的详情
        viewHolder.rl_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("url", detailURL);
                bundle.putInt("position",position);
                intent.setClass(context, SystemMessageDetailActivity.class);
                intent.putExtras(bundle);
                context.startActivityForResult(intent, SYSTEM_MESSAGE_DETAILS_CODE);
                System.out.println("跳转到具体详情页面地址:detailURL" + detailURL);
            }
        });
        return convertView;
    }


    public void addItem(List<SystemMessageListBean.MsgListBean> more_data) {
        messageInfoList.addAll(more_data);
    }

    /**
     * 刷新数据
     *
     * @param list
     */
    public void RefreshData(List<SystemMessageListBean.MsgListBean> list) {
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
