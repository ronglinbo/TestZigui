package com.wcyc.zigui2.newapp.module.classdynamics;

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

/**
 * 发布班级动态 选择图片还是视频弹出框
 * 郑国栋 2016-02-15
 */
public class SaveClassPop extends PopupWindow{
	private View view;
	private Context mContext;
	private SelectPicOrVideo selectPicOrVideo;

	public SaveClassPop(Context context, SelectPicOrVideo selectPicOrVideo){
		super(context);
		mContext = context;
		this.selectPicOrVideo = selectPicOrVideo;
		initView();
		initEvent();
	}
	
	public interface SelectPicOrVideo{
		void selectVideo();
		void selectLocalVideo();
		void selectPic();
	}
	
	private void initView(){
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		view = inflater.inflate(R.layout.save_class_pop, null);

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
		TextView select_video = (TextView) view.findViewById(R.id.select_video);
		TextView select_local_video = (TextView) view.findViewById(R.id.select_local_video);
		TextView select_pic = (TextView) view.findViewById(R.id.select_pic);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);
		select_video.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				selectPicOrVideo.selectVideo();
				dismiss();
			}

		});
		select_local_video.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				selectPicOrVideo.selectLocalVideo();
				dismiss();
			}

		});
		select_pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				selectPicOrVideo.selectPic();
				dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}
}