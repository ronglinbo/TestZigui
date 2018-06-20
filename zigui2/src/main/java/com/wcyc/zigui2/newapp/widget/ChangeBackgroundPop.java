package com.wcyc.zigui2.newapp.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wcyc.zigui2.R;


/**
 * 拍照或者从相册当中选择
 */
public class ChangeBackgroundPop extends PopupWindow {
    private View view;
    private Context mContext;
    private ChangeBackgroundInterface change;

    public ChangeBackgroundPop(Context context, ChangeBackgroundInterface change) {
        super(context);
        mContext = context;
        this.change = change;
        initView();
        initEvent();
    }

    public interface ChangeBackgroundInterface {
        void changeBackGround();
    }

    private void initView() {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.pop_change_background, null);

        this.setContentView(view);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.alpha_popupWindow);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
    }

    private void initEvent() {
        Button bt_change = (Button) view.findViewById(R.id.bt_change);


        bt_change.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                change.changeBackGround();
                dismiss();
            }

        });


        /**
         * 点击外部会消失
         */
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                LinearLayout ll_pop_layout = (LinearLayout) view.findViewById(R.id.ll_pop_layout);
                int top = ll_pop_layout.getTop();
                int bottom = ll_pop_layout.getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < top) {
                        dismiss();
                    }

                    if (y > bottom) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}