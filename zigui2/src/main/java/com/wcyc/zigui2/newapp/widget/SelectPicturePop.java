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
import android.widget.TextView;

import com.wcyc.zigui2.R;


/**
 * 拍照或者从相册当中选择
 */
public class SelectPicturePop extends PopupWindow {
    private View view;
    private Context mContext;
    private SelectPictureInterface select;

    public SelectPicturePop(Context context, SelectPictureInterface select) {
        super(context);
        mContext = context;
        this.select = select;
        initView();
        initEvent();
    }

    public interface SelectPictureInterface {
        void takePicture();

        void selectPicture();
    }

    private void initView() {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.select_picture_pop, null);

        this.setContentView(view);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
    }

    private void initEvent() {
        Button bt_take_photo = (Button) view.findViewById(R.id.bt_take_photo);
        Button bt_pick_photo = (Button) view.findViewById(R.id.bt_pick_photo);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);


        bt_take_photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                select.takePicture();
                dismiss();
            }

        });
        bt_pick_photo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                select.selectPicture();
                dismiss();
            }

        });


        bt_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
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
                int y = (int) event.getY();
                //当点击pw高度之上的地方 pw会消失
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < top) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}