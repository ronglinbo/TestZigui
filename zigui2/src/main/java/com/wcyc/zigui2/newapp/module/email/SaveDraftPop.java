package com.wcyc.zigui2.newapp.module.email;

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

public class SaveDraftPop extends PopupWindow{
	private View view;
	private Context mContext;
	private OnSaveDraft onSaveDraft;
	
	public SaveDraftPop(Context context,OnSaveDraft onSaveDraft){
		super(context);
		mContext = context;
		this.onSaveDraft = onSaveDraft;
		initView();
		initEvent();
	}
	
	public interface OnSaveDraft{
		void saveDraft();
		void notSaveDraft();
	}
	
	private void initView(){
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		view = inflater.inflate(R.layout.save_draft_option, null);

		this.setContentView(view);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
	}
	
	private void initEvent(){
		TextView save = (TextView) view.findViewById(R.id.save_draft);
		TextView notSave = (TextView) view.findViewById(R.id.not_save_draft);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);
		save.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onSaveDraft.saveDraft();
				dismiss();
			}
			
		});
		notSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onSaveDraft.notSaveDraft();
				dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}
}