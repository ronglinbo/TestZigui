package com.wcyc.zigui2.asynctask;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.wcyc.zigui2.utils.BitmapCompression;

public class ImageCompressAsyncTask extends AsyncTask<String,Integer,String>{
	BitmapCompression bitmapCompression;
	String url;
	int index;
	
	void ImageCompressAsyncTask(BitmapCompression bitmapCompression,String url,int i){
		this.bitmapCompression = bitmapCompression;
		this.url = url;
		this.index = i;
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		Bitmap bitm = BitmapCompression.getimage(url);
		File f = new File(Environment.getExternalStorageDirectory().getPath()+"/namecard"+index+".jpeg");
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(f);
			bitm.compress(Bitmap.CompressFormat.JPEG, 100, out); 
			out.flush(); 
		    out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	@Override
    protected void onPostExecute(String result) {
    	if(result != null){
    		
    	}
    }
}