/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wcyc.zigui2.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.widget.ImageView;

import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.wcyc.zigui2.bean.Child;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.activity.ImagePagerActivity;

public class ImageUtils {
	
	public static String getImagePath(String remoteUrl)
	{
		String imageName= remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ imageName;
        EMLog.d("msg", "image path:" + path);
        return path;
	}
	
	
	public static String getThumbnailImagePath(String thumbRemoteUrl) {
		String thumbImageName= thumbRemoteUrl.substring(thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
		String path =PathUtil.getInstance().getImagePath()+"/"+ "th"+thumbImageName;
        EMLog.d("msg", "thum image path:" + path);
        return path;
    }
	
	public static int getImageResIdByImageName(Context context,String name){
		ApplicationInfo appInfo = context.getApplicationInfo();
		return context.getResources().getIdentifier(name, "drawable", appInfo.packageName);
	}

    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            myFileUrl = null;
        }
        try {
        	if(myFileUrl != null){
	            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
	            conn.setConnectTimeout(0);
	            conn.setDoInput(true);
	            conn.connect();
	            InputStream is = conn.getInputStream();
	            bitmap = BitmapFactory.decodeStream(is);
	            System.out.println("bitmap:"+bitmap);
	            is.close();
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
     
        return bitmap;
    }
    
    public static Drawable fetchDrawable(Context context,String url) {
        Drawable drawable = null;
        URL Url;   
        try {    
        Url = new URL(url);    
         drawable = Drawable.createFromStream(Url.openStream(), "");   
        } catch (Exception e) {    
         return null;   
        }
        //按比例缩放图片
        Rect bounds = getDefaultImageBounds(context);
        int newwidth = bounds.width();
        int newheight = bounds.height();
        double factor = 1;
        double fx = (double)drawable.getIntrinsicWidth() / (double)newwidth;
        double fy = (double)drawable.getIntrinsicHeight() / (double)newheight;
        factor = fx > fy ? fx : fy;
        if (factor < 1) factor = 1;
        newwidth = (int)(drawable.getIntrinsicWidth() / factor);
        newheight = (int)(drawable.getIntrinsicHeight() / factor);
        drawable.setBounds(0, 0, newwidth, newheight);
        return drawable;
    }
  
    //预定图片宽高比例为 4:3
    @SuppressWarnings("deprecation")
    public static Rect getDefaultImageBounds(Context context) {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = width * 3 / 4;
        Rect bounds = new Rect(0, 0, width, height);
        return bounds;
    }
   
    public static void showImage(Context mContext,String file,ImageView image){	
    	if(!DataUtil.isNullorEmpty(file)){
    		String url = DataUtil.getIconURL(file);
    		System.out.println("icon file:"+url);

          ImageLoader mloader = ((BaseActivity)mContext).getImageLoader();
   	    	mloader.displayImage(url, image);
    	}
    }
  
    public static boolean isImage(String name){
		File file = new File(name);
		String type = DataUtil.getMIMEType(file);
        return type.contains("image");
    }
	
    public static void showImage(Context context,String url){
		PictureURL pictureURL = null;
		List<PictureURL> datas = new ArrayList<PictureURL>();
		
		pictureURL = new PictureURL();
		pictureURL.setPictureURL(DataUtil.getDownloadURL(url));
		datas.add(pictureURL);
		
		Intent intent = new Intent(context,ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
				(Serializable) datas);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
