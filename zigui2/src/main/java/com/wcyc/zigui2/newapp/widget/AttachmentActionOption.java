/*
 * 文 件 名:AttachmentActionOption.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-31
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.widget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.adapter.MenuAdapter;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;
import com.wcyc.zigui2.utils.AsyncImageLoader;
import com.wcyc.zigui2.utils.BitmapSave;
import com.wcyc.zigui2.utils.BitmapTool;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.ImageUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AttachmentActionOption extends PopupWindow{
	private View view;
	public ListView list;
	private Context context;
	private Attachment attach;
	private TextView otherApp,saveFile,cancel;
	private String url;
	private static File savedFile;
	private ProgressDialog pd;
	private Thread mThread;
	
	public AttachmentActionOption(Context context,Attachment attach){
		super(context);
		this.context = context;
		this.attach = attach;
		initData();
		initView();
		initEvent();
	}
	
	private void initData(){
		String file = attach.getAttachementUrl();
		url = DataUtil.getDownloadURL(file);
	}

	private void initView(){
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		view = inflater.inflate(R.layout.attachment_action_option, null);
		otherApp = (TextView) view.findViewById(R.id.other_app);
		saveFile = (TextView) view.findViewById(R.id.save_file);
		cancel = (TextView) view.findViewById(R.id.cancel);
		this.setContentView(view);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setOutsideTouchable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
	}
	
	private void initEvent(){
		otherApp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				//saveImage(true);
				showProgress();
				mThread = new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						downloadFile(true);
						dismissPd();
					}
					
				});
				mThread.start();
				dismiss();
			}
			
		});
		
		saveFile.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				downloadFile(false);
				dismiss();
			}
			
		});
		
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
			
		});
	}
	
	private void showProgress(){
		pd = new ProgressDialog(context);
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(true);
		pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if(mThread != null)
				mThread.interrupt();
			}
		});
		pd.show();
		pd.setContentView(R.layout.progress_bar);
		pd.getWindow().setGravity(Gravity.CENTER);
		pd.getWindow().setBackgroundDrawableResource(R.color.transparent);
	}
	
	private void dismissPd(){
		if(pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}
	
	public void downloadFile(final boolean view){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String subForder = Environment.getExternalStorageDirectory()+"/Zigui_cache/";
				String fileName = attach.getAttachementName();
				try {
					savedFile = HttpHelper.downFile(url, subForder,fileName);
					System.out.println("savedFile:"+savedFile);
					if(savedFile != null){
						if(view){
							openFile(savedFile);
						}else{
							DataUtil.getToast("文件已保存"+subForder+fileName);
						}
						System.out.println("openFile:"+savedFile);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void delFile(){
		System.out.println("delFile:"+savedFile);
		savedFile.delete();
	}
	
	public void saveImage(final boolean viewWithOther){
		DisplayImageOptions options = ((BaseActivity)context).getImageLoaderOptions();
		((BaseActivity)context).getImageLoader().loadImage(url, options,new ImageLoadingListener(){

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				System.out.println("url："+url+"bitmap:"+arg2);
				if(arg2 != null){
					try {
//						String name= url.substring(url.lastIndexOf("/") + 1, url.length());
						String name = attach.getAttachementName();
						savedFile = BitmapTool.saveImage(context,arg2,name);
						System.out.println("file:"+savedFile);
						if(viewWithOther){
							getImageFileIntent(savedFile);
						}else{
							DataUtil.getToast("文件已保存"+savedFile);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public Intent getImageFileIntent(File file){
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "image/*");
        ((BaseActivity) context).startActivityForResult(intent,100);
        return intent;
    }
	
	private void openFile(File file){ 
	    Intent intent = new Intent(); 
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    intent.setAction(Intent.ACTION_VIEW); 
	    String type = DataUtil.getMIMEType(file); 

	    intent.setDataAndType(Uri.fromFile(file), type); 
	    try{
//	    	((BaseActivity) context).startActivityForResult(intent, BaseActivity.VIEW_FILE);   
	    	context.startActivity(intent);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }	     
	} 
	
	
}