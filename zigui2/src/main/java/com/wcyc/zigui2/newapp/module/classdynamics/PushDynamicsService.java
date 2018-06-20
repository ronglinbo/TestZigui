package com.wcyc.zigui2.newapp.module.classdynamics;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.utils.BitmapCompression;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.UploadUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * 发送 动态信息 给服务器
 * @author xiehua 20151229
 * 
 */

public class PushDynamicsService extends Service{
	private ServiceThread mThread;
	private int timeoutMillis = 60*1000;
    private String Type;
    private String order;
    private String msgID;
    private BitmapCompression bitmapCompression;
    private boolean isCompress;
    private String filePath;
    private ArrayList<String> fileList;
    private final static String UPLOADING_TYPE = "5";
    
	public void onCreate(){
		super.onCreate();
		IntentFilter mfinishFilter = new IntentFilter(NewPublishDynamicActivity.INTENT_FINISH_UPLOAD_PICTURE);
		registerReceiver(finishUploadReceiver,mfinishFilter);
		IntentFilter mcancelFilter = new IntentFilter(NewPublishDynamicActivity.INTENT_CANCEL_UPLOAD_PICTURE);
		registerReceiver(cancelUploadReceiver,mcancelFilter);
//		mThread = new ServiceThread();
//		mThread.start();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("intent:"+intent);
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    if(intent != null){
    		fileList = intent.getStringArrayListExtra("fileList");
    		msgID = intent.getStringExtra("msgID");
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private class ServiceThread extends Thread{
		public void run(){
			//uploadFile(fileList);
			//uploadFiles(fileList);
		}
	}
	
	private void uploadFiles(ArrayList<String> filepath){
		try{
			for(String filename:filepath){
				File file = new File(filename);
				UploadUtil.uploadFile(file, Constants.IMG_SERVER_URL);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void uploadFile(ArrayList<String> filePath){
		try{
			String serverResponse = null;
			File file = null;     
	        HttpClient httpClient = new DefaultHttpClient();
	        HttpContext httpContext = new BasicHttpContext();
	        HttpPost httpPost = new HttpPost(Constants.IMG_SERVER_URL);
	        int i = 0;
	        for(String fileName:filePath){
		        if(isCompress){
			        Bitmap bitm = BitmapCompression.getimage(fileName);
			        System.out.println("index:"+i);
					if(bitm != null){
						file = new File(Environment.getExternalStorageDirectory().getPath()+"/namecard"+i+".jpeg");
						
						FileOutputStream out = null;
						try {
							out = new FileOutputStream(file);
							bitm.compress(Bitmap.CompressFormat.JPEG, 100, out); 
							out.flush(); 
						    out.close(); 
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
		        }else{
		        	file = new File(fileName);
		        }
	
		        if(file != null){
					MultipartEntity mEntity = new MultipartEntity();
					mEntity.addPart("ID", new StringBody(msgID));
					mEntity.addPart("Type",new StringBody(UPLOADING_TYPE));

		            mEntity.addPart("order",new StringBody(""+i));

		            mEntity.addPart("picture", new FileBody(file));
		            httpPost.setEntity(mEntity);
		            HttpResponse response = httpClient.execute(httpPost, httpContext);
		            
		            serverResponse = EntityUtils.toString(response.getEntity());
		            System.out.println("serverResponse "+serverResponse);
		            i++;
		        }
			}
	        
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private BroadcastReceiver finishUploadReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			DataUtil.getToast("班级动态发布成功");
//			Toast.makeText(CCApplication.app, "班级动态发布成功", Toast.LENGTH_LONG)
//			.show();
			System.out.println("hasfinished!!!!");
			DataUtil.hasUnfinishedTask = false;
			DataUtil.isAlert = false;

			boolean is_compress = intent.getBooleanExtra("is_compress",true);
			int size = intent.getIntExtra("upload_nums", 12);
			//System.out.println("upload picture finish receive"+" "+size+" "+is_compress);
			DataUtil.cleanTempFile(is_compress,size);
		}
	};
	
	private BroadcastReceiver cancelUploadReceiver = new BroadcastReceiver(){
		
		public void onReceive(Context context, Intent intent){
		    DataUtil.hasUnfinishedTask = false;
			System.out.println("intent："+intent);
			int reason = intent.getIntExtra("reason", NewPublishDynamicActivity.REASON_BACK);
			String msgID = intent.getStringExtra("msgID");
			//DataUtil.showDialog((Activity) context, "发送图片失败!");
			if(reason == NewPublishDynamicActivity.REASON_CANCEL){
				if(!DataUtil.isAlert){
					if(!DataUtil.isNetworkAvailable(getBaseContext())){
						DataUtil.getToast("无网络");
		            	return;
		            }
					String result = httpBusiInerfaceFinish(msgID,NewPublishDynamicActivity.UPLOADING_TYPE);
					JSONObject json;
					try {
						json = new JSONObject(result);
						System.out.println("FINISH_URL result:"+result);
						int ret =  (Integer) json.get("resultCode");
						if (ret != Constants.SUCCESS_CODE) {
							DataUtil.getToast("发送图片失败!", Toast.LENGTH_LONG);
							DataUtil.isAlert = true;
						} else {
							System.out.println("发送图片成功!");
							DataUtil.getToast("发送图片成功!", Toast.LENGTH_LONG);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				boolean is_compress = intent.getBooleanExtra("is_compress",true);
    			int size = intent.getIntExtra("upload_nums", 12);
    	
    			DataUtil.cleanTempFile(is_compress,size);
			}
			
		}
	};
	
	/*
	 * 业务入口
	 * 
	 * 发布完成
	 * 
	 * ID String 用户ID Type String 反馈内容
	 * 
	 * ? 出参 参数名 参数类型 描述 code Integer 返回代码 （200 成功 201 失败）
	 */

	private String httpBusiInerfaceFinish(String ID, String Type) {
		String result = null;
		JSONObject json = new JSONObject();
		try {
			json.put("ID", ID);
			json.put("Type", Type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String FINISH_URL = "/upload/uploadState";
		try {
			result = HttpHelper.httpPostJson(FINISH_URL, json);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}