package com.wcyc.zigui2.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wcyc.zigui2.R;


/**
 * Created by 章豪 on 2017/6/19.
 */

public class MySpinner extends TextView {
    private Context mContext;
    private BaseAdapter adapter;
    private ListView popContentView;
    private AdapterView.OnItemClickListener onItemClickListener;
    private BaloonView mDropView;

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    private   LinearLayout container;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        container = (LinearLayout) inflater.inflate(R.layout.spinner_content,null);
        popContentView = (ListView) container.findViewById(R.id.spinner_content);

//        mDropView = new PopupWindow(container, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        mDropView.setBackgroundDrawable(new BitmapDrawable());
//        mDropView.setFocusable(true);
//        mDropView.setOutsideTouchable(true);
//        mDropView.setOutsideTouchable(true);
//        mDropView.setTouchable(true);
        mDropView=new BaloonView(mContext,container);
        container.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismissPop();
            }
        });
        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mDropView.isShowing()){
                    dismissPop();
                }else{
                    showPop();
                }
            }
        });
        mDropView.update();
    }
    public  void setSelection(int  i){

    };
    public void setHint(String hint){
        this.setText(hint);
    }

    public void setAdapter(BaseAdapter adapter){
        if(adapter != null){
            this.adapter = adapter;
            popContentView.setAdapter(this.adapter);
        }

    }

    public void setOnItemSelectedListener(AdapterView.OnItemClickListener listener){
        if(listener != null){
            this.onItemClickListener = listener;
            popContentView.setOnItemClickListener(listener);
        }

    }

    public void dismissPop(){
        if(mDropView.isShowing()){
            mDropView.dismiss();
        }
    }

    public void showPop(){

//        int width = mDropView.getWidth() ;
//        int height = mDropView.getHeight() ;
//        mDropView.showAtLocation( container , Gravity.LEFT | Gravity.TOP , container.getWidth()/2 - width/2, container.getHeight()/2 - height/2);

        mDropView.showUnderView(this);
    }

}