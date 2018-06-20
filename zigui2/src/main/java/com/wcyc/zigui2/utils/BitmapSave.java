package com.wcyc.zigui2.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;


/**
 * 图片保存到本地
 * @author 谭园园
 *
 */

public class BitmapSave {
	static String subForder;
	static final String TAG = "com.wcyc.zigui2";
	//保存图片
	public static String saveFile(Context context,Bitmap bm) throws IOException {
		subForder = Environment.getExternalStorageDirectory()+"/ZIGUI_Photos/";
		File foder = new File(subForder);
		if (!foder.exists()) {
			foder.mkdirs();
		}
		
		File myCaptureFile = new File(subForder, System.currentTimeMillis()+".jpeg");
		if (!myCaptureFile.exists()) {
			myCaptureFile.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		bos.flush();
		bos.close();
	
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(myCaptureFile);
		intent.setData(uri);
		Log.i(TAG,"uri:"+uri);
		context.sendBroadcast(intent);
		
		return myCaptureFile.toString();
	}
	/**
	 * 发送广播，重新挂载SD卡
	 */
	private static void sendBroadCaseRemountSDcard() {
		Intent intent = new Intent();
		// 重新挂载的动作
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		// 要重新挂载的路径
		intent.setData(Uri.fromFile(new File(subForder)));
		//sendBroadcast(intent);
	}
	/*private static void sendBroadcast(Intent intent) {
		// TODO Auto-generated method stub
	}*/
	
	
	public static String copyFile(Context context,String oldPath) throws IOException {	 
		subForder = context.getFilesDir().getAbsolutePath();
		boolean ret;
		String filename = System.currentTimeMillis()+".jpg";
		String NewPath = subForder + filename;
		Log.i(TAG,"NewFile:"+NewPath);
	
		try {   
			int bytesum = 0;	 
			int byteread = 0;   
			File oldfile = new File(oldPath);   
			if (oldfile.exists()) { //文件存在时 	 
				InputStream inStream = new FileInputStream(oldPath);    
				FileOutputStream fs = new FileOutputStream(NewPath);	 
				byte[] buffer = new byte[2048];	
				int length;	
				while ( (byteread = inStream.read(buffer)) != -1) {	
					bytesum += byteread;   	 
					fs.write(buffer, 0, byteread);   
				}   
				inStream.close(); 
				fs.close();
			}   
		}   
		catch (Exception e) {   
			System.out.println("复制单个文件操作出错");	
			e.printStackTrace();	 
		}  
		return NewPath;

	}

}

