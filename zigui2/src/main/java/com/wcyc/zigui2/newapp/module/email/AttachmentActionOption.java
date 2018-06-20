/*
 * 文 件 名:AttachmentActionOption.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-31
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.module.email;

import java.io.File;
import java.io.IOException;



import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.adapter.MenuAdapter;
import com.wcyc.zigui2.newapp.module.email.NewMailInfo.Attachment;

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
		String file = "";
		String fileId = attach.getFileSystemId();
		file = "?fileId="+fileId;

		url = Constants.DOWNLOAD_URL + file
					+ Constants.AUTHID + "@" + CCApplication.app.getDeviceId()
					+ "@" + CCApplication.app.getMemberInfo().getAccId();
		
//		url = DataUtil.getDownloadURL(file);
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
				String subForder = Environment.getExternalStorageDirectory() + "/Zigui_cache/";
				String fileName = attach.getFileName();
				try {
					savedFile = HttpHelper.downFile(url, subForder, fileName);
					System.out.println("savedFile:" + savedFile);
					if (savedFile != null) {
						if (view) {
							openFile(savedFile);
						} else {
							DataUtil.getToast("文件已保存" + subForder + fileName);
						}
						System.out.println("openFile:" + savedFile);
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
						String name = attach.getFileName();
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
	    String type = getMIMEType(file); 

	    intent.setDataAndType(Uri.fromFile(file), type); 
	    try{
//	    	((BaseActivity) context).startActivityForResult(intent, BaseActivity.VIEW_FILE);   
	    	context.startActivity(intent);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }	     
	} 
	
	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * @param file
	 */ 
	private String getMIMEType(File file) { 
	     
	    String type="*/*"; 
	    String fName = file.getName(); 
	    int dotIndex = fName.lastIndexOf("."); 
	    if(dotIndex < 0){ 
	        return type; 
	    } 
	    /* 获取文件的后缀名*/ 
	    String end = fName.substring(dotIndex,fName.length()).toLowerCase(); 
	    if(end == "") return type; 
	    //在MIME和文件类型的匹配表中找到对应的MIME类型。 
	    for(int i=0;i< MIME_MapTable.length;i++){ 
	        if(end.equals(MIME_MapTable[i][0])) 
	            type = MIME_MapTable[i][1]; 
	    }        
	    return type; 
	} 
	 
	 
	 //MIME_MapTable是所有文件的后缀名所对应的MIME类型的一个String数组：
	 
	private final String[][] MIME_MapTable={ 
	    //{后缀名，MIME类型} 
		{".3gp",    "video/3gpp"}, 
		{".apk",    "application/vnd.android.package-archive"}, 
		{".asf",    "video/x-ms-asf"}, 
		{".avi",    "video/x-msvideo"}, 
		{".bin",    "application/octet-stream"}, 
		{".bmp",    "image/bmp"}, 
		{".c",  "text/plain"}, 
		{".class",  "application/octet-stream"}, 
		{".conf",   "text/plain"}, 
		{".cpp",    "text/plain"}, 
		{".doc",    "application/msword"}, 
		{".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, 
		{".xls",    "application/vnd.ms-excel"},  
		{".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, 
		{".exe",    "application/octet-stream"}, 
		{".gif",    "image/gif"}, 
		{".gtar",   "application/x-gtar"}, 
		{".gz", "application/x-gzip"}, 
		{".h",  "text/plain"}, 
		{".htm",    "text/html"}, 
		{".html",   "text/html"}, 
		{".jar",    "application/java-archive"}, 
		{".java",   "text/plain"}, 
		{".jpeg",   "image/jpeg"}, 
		{".jpg",    "image/jpeg"}, 
		{".js", "application/x-javascript"}, 
		{".log",    "text/plain"}, 
		{".m3u",    "audio/x-mpegurl"}, 
		{".m4a",    "audio/mp4a-latm"}, 
		{".m4b",    "audio/mp4a-latm"}, 
		{".m4p",    "audio/mp4a-latm"}, 
		{".m4u",    "video/vnd.mpegurl"}, 
		{".m4v",    "video/x-m4v"},  
		{".mov",    "video/quicktime"}, 
		{".mp2",    "audio/x-mpeg"}, 
		{".mp3",    "audio/x-mpeg"}, 
		{".mp4",    "video/mp4"}, 
		{".mpc",    "application/vnd.mpohun.certificate"},        
		{".mpe",    "video/mpeg"},   
		{".mpeg",   "video/mpeg"},   
		{".mpg",    "video/mpeg"},   
		{".mpg4",   "video/mp4"},    
		{".mpga",   "audio/mpeg"}, 
		{".msg",    "application/vnd.ms-outlook"}, 
		{".ogg",    "audio/ogg"}, 
		{".pdf",    "application/pdf"}, 
		{".png",    "image/png"}, 
		{".pps",    "application/vnd.ms-powerpoint"}, 
		{".ppt",    "application/vnd.ms-powerpoint"}, 
		{".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, 
		{".prop",   "text/plain"}, 
		{".rc", "text/plain"}, 
		{".rmvb",   "audio/x-pn-realaudio"}, 
		{".rtf",    "application/rtf"}, 
		{".sh", "text/plain"}, 
		{".tar",    "application/x-tar"},    
		{".tgz",    "application/x-compressed"},  
		{".txt",    "text/plain"}, 
		{".wav",    "audio/x-wav"}, 
		{".wma",    "audio/x-ms-wma"}, 
		{".wmv",    "audio/x-ms-wmv"}, 
		{".wps",    "application/vnd.ms-works"}, 
		{".xml",    "text/plain"}, 
		{".z",  "application/x-compress"}, 
		{".zip",    "application/x-zip-compressed"}, 
		{"",        "*/*"}   
	}; 
}