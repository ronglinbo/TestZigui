package com.wcyc.zigui2.newapp.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.fragment.AllMessageFragment;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

public class MessageItemPop extends PopupWindow {
    private View view;
    private Context mContext;
    private OnLongClick onClick;
    public TextView tv_delete;
    public TextView tv_read;
    private View view_line;
    private NewMessageBean bean;

    public MessageItemPop(Context context, NewMessageBean bean, OnLongClick onClick) {
        super(context);
        mContext = context;
        this.onClick = onClick;
        this.bean = bean;
        initView();
        initEvent();
    }

    public interface OnLongClick {
        void deleteItem();

        void readItem();
    }

    private void initView() {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.message_item_pop, null);

        this.setContentView(view);
        this.setWidth(LayoutParams.WRAP_CONTENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable();
        this.setBackgroundDrawable(dw);
    }

    private void initEvent() {
        tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        tv_read = (TextView) view.findViewById(R.id.tv_read);
        view_line = view.findViewById(R.id.view_line);
        tv_read = (TextView) view.findViewById(R.id.tv_read);


        String count = bean.getCount();
        boolean showRead = false;
        if (!DataUtil.isNullorEmpty(count) && bean.getHxUser() == null) {
            if (Integer.valueOf(count) > 0) {
                showRead = true;
            }
        }

        //显示设为已读   隐藏删除
        if (showRead) {
            tv_read.setText("设为已读");
            tv_read.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.GONE);
            tv_delete.setVisibility(View.GONE);
        } else {
            //显示删除  隐藏设为已读
            tv_delete.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.GONE);
            tv_read.setVisibility(View.GONE);
        }

        tv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //delete
                onClick.deleteItem();
                dismiss();
            }

        });
        tv_read.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.readItem();
                dismiss();
            }
        });

    }
}