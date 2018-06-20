package com.wcyc.zigui2.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.wcyc.zigui2.core.CCApplication;

public class HttpUtils{
	public static final int TIME_OUT = Constants.TIME_OUT;
	
	public static void disableConnectionReuseIfNecessary() {
		// 这是一个2.2版本之前的bug
		if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}
	public static HttpURLConnection getHttpURLConnection(Context mContext,String url){
        HttpURLConnection mHttpURLConnection = null;
        try {
        	disableConnectionReuseIfNecessary();
            URL mUrl= new URL(url);
            mHttpURLConnection = (HttpURLConnection)mUrl.openConnection();
            //设置链接超时时间
            mHttpURLConnection.setConnectTimeout(TIME_OUT);
            //设置读取超时时间
            mHttpURLConnection.setReadTimeout(TIME_OUT);
            //设置请求参数
            mHttpURLConnection.setRequestMethod("POST");
            //添加Header
            mHttpURLConnection.setRequestProperty("Connection","Keep-Alive");
            //接收输入流
            mHttpURLConnection.setDoInput(true);
            //传递参数时需要开启
            mHttpURLConnection.setDoOutput(true);
            
            RequestHeader header = new RequestHeader(mContext);
            mHttpURLConnection.addRequestProperty("X-User-Account-Id",header.getAccId());
            mHttpURLConnection.addRequestProperty("X-User-Id",header.getUserId());
            mHttpURLConnection.addRequestProperty("X-User-Hash", header.getPassword());
            mHttpURLConnection.addRequestProperty("mac",header.getMac());
            mHttpURLConnection.addRequestProperty("X-Device-Id",header.getDeviceId());
            mHttpURLConnection.addRequestProperty("X-School-Id", header.getSchoolId());
            mHttpURLConnection.addRequestProperty("resolution",header.getResolution());
            mHttpURLConnection.addRequestProperty("X-mobile-Type",header.getMobileType());
            mHttpURLConnection.addRequestProperty("version",header.getVersion());
            mHttpURLConnection.addRequestProperty("x-device-name",header.getDeviceType());
            mHttpURLConnection.addRequestProperty("X-os-version",header.getOsVersion());
            mHttpURLConnection.addRequestProperty("X-operator",header.getOperatorName());
            mHttpURLConnection.addRequestProperty("x-device-brand",header.getDeviceBrand());
            mHttpURLConnection.addRequestProperty("X-Device-Token",header.getDeviceToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mHttpURLConnection ;
    }
	
	public static void postParams(OutputStream output,List<NameValuePair>paramsList) 
			throws IOException{
	    StringBuilder mStringBuilder = new StringBuilder();
	    for (NameValuePair pair:paramsList){
	        if(!TextUtils.isEmpty(mStringBuilder)){
	            mStringBuilder.append("&");
	        }
	        mStringBuilder.append(URLEncoder.encode(pair.getName(),"UTF-8"));
	        mStringBuilder.append("=");
	        mStringBuilder.append(URLEncoder.encode(pair.getValue(),"UTF-8"));
	    }
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));
	    writer.write(mStringBuilder.toString());
	    writer.flush();
	    writer.close();
	}
	
	public static void postJson(OutputStream output,JSONObject json)
			throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));
		writer.write(json.toString());
		writer.flush();
		writer.close();
	}
	
	public static String useHttpUrlConnectionPost(Context mContext,String url,JSONObject json) {
        url = new StringBuilder(Constants.SERVER_URL).append(url).toString();
        InputStream mInputStream = null;
        HttpURLConnection mHttpURLConnection = getHttpURLConnection(mContext,url);
        try {
            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            //要传递的参数
            for (Iterator iterator = json.keys(); iterator.hasNext();) {
    			String name = (String) iterator.next();
    			try {
					postParams.add(new BasicNameValuePair(name, json.getString(name)));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
    		}
            postJson(mHttpURLConnection.getOutputStream(),json);
//            postParams(mHttpURLConnection.getOutputStream(), postParams);
            mHttpURLConnection.connect();

            mInputStream = mHttpURLConnection.getInputStream();
            int code = mHttpURLConnection.getResponseCode();

            String response = converStreamToString(mInputStream);
            System.out.println("请求状态码:" + code + "\n请求结果:\n" + response);
            String time=mHttpURLConnection.getHeaderField("X-Timestamp");
            System.out.println("系统时间:"+time);

            Log.d("HTTP","--------------------我是分割线-----------------------------");
            Log.d("HTTP","请求URL:"+url+"\n请求状态码:\n" + code + "\n请求结果:\n" + response);

            //设置系统时间
            CCApplication.getInstance().setServer_date(time);
            mInputStream.close();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
    }
	
	public static String converStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        String respose = sb.toString();
        return respose;
    }
}