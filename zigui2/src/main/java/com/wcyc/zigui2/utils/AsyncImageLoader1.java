package com.wcyc.zigui2.utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

//2015-12-15
/**
 * 异步从网络获取图片.
 * @author 谢华
 * @version 1.01
 * @since 1.01
 */
public class AsyncImageLoader1{
	private HashMap<String[], SoftReference<ArrayList<Bitmap>>> imageCache 
		= new HashMap<String[],SoftReference<ArrayList<Bitmap>>>();
	private ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
	public AsyncImageLoader1(){

	}
	
	public ArrayList<Bitmap> loadBitmap(final String[] imageUrl,final ImageCallback imageCallback){
		if(imageCache.containsKey(imageUrl)){
			SoftReference <ArrayList<Bitmap>> sr = imageCache.get(imageUrl);
			System.out.println("sr:"+sr);
			bitmapList = sr.get();
			if(bitmapList != null){
				return bitmapList;
			}
		}	
		final Handler handler = new Handler(){
			public void handleMessage(Message message){
				imageCallback.imageLoaded((ArrayList<Bitmap>)message.obj);
			}
		};
		new Thread(){
			public void run(){
				ArrayList<Bitmap> temp = new ArrayList<Bitmap>();
				for(String s:imageUrl){
					Bitmap bm = ImageUtils.getHttpBitmap(Constants.BASE_URL+"/"+s);
					temp.add(bm);
				}
				bitmapList = temp;
				imageCache.put(imageUrl, new SoftReference<ArrayList<Bitmap>> (bitmapList));
				Message msg = handler.obtainMessage(0,bitmapList);
				handler.sendMessage(msg);
			}
		}.start();	
		
		return null;
	}
	
	public interface ImageCallback{
		void imageLoaded(ArrayList<Bitmap> bm);
	}
}