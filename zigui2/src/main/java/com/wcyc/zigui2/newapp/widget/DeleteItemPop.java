package com.wcyc.zigui2.newapp.widget;

import com.wcyc.zigui2.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class DeleteItemPop extends PopupWindow{
	private View view;
	private Context mContext;
	private OnLongClick onClick;
	public TextView delete;

	public DeleteItemPop(Context context,OnLongClick onClick){
		super(context);
		mContext = context;
		this.onClick = onClick;
		initView();
		initEvent();
	}

	public interface OnLongClick{
		void deleteItem();
	}

	private void initView(){
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		view = inflater.inflate(R.layout.delete_item_pop, null);

		this.setContentView(view);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable();
		this.setBackgroundDrawable(dw);
	}

	private void initEvent(){
		delete = (TextView) view.findViewById(R.id.delete_item);
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//delete
				onClick.deleteItem();
				dismiss();
			}

		});
	}
}