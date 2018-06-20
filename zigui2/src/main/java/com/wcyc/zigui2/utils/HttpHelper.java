/*
* 文 件 名:HttpHelper.java
* 创 建 人： 姜韵雯
* 日    期： 2014-09-23
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.utils;





import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;


//2014-9-23
/**
 * http通信类.
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public class HttpHelper {
    public static final int TIME_OUT = Constants.TIME_OUT;
    public static String httpGet(String url) throws IOException, URISyntaxException {
        HttpGet httpGet = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        HttpParams parms = client.getParams();
        HttpConnectionParams.setConnectionTimeout(parms, TIME_OUT);//设置网络超时
        HttpConnectionParams.setSoTimeout(parms, TIME_OUT);//设置网络超时

        HttpResponse response = client.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == Constants.SUCCESS_CODE) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, "UTF-8");
            }
        }
        return null;
    }

    /**
     * http post Json数据.
     * @param url 路径
     * @param json 封装的数据
     * @return 服务器返回的数据
     * @throws ClientProtocolException
     * @throws IOException
     * @throws JSONException
     */
    public static String httpPostJson(String url , JSONObject json) throws IOException, JSONException{
        HttpPost request = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Iterator iterator = json.keys(); iterator.hasNext();) {
            String name = (String) iterator.next();
            params.add(new BasicNameValuePair(name, json.getString(name)));
        }

        request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpClient client = new DefaultHttpClient();

        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT); // 超时设置
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);// 连接超时
        HttpResponse response = client.execute(request);

        int code = response.getStatusLine().getStatusCode();
        if (code == Constants.SUCCESS_CODE) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                entity.consumeContent();
                client.getConnectionManager().shutdown();
                return result;
            }
        }
        return null;
    }
    /**
     * http post Json数据.
     * @param url 路径
     * @param json 封装的数据
     * @return 服务器返回的数据
     * @throws ClientProtocolException
     * @throws IOException
     * @throws JSONException
     */
    //方法一   下面有方法二    2016.10.28改为方法二了
//    public static String httpPostJson(Context mcontext,String url , JSONObject json) throws IOException, JSONException{
//        HttpPost request = new HttpPost(url);
////    	List<NameValuePair> params = new ArrayList<NameValuePair>();
////        for (Iterator iterator = json.keys(); iterator.hasNext();) {
////			String name = (String) iterator.next();
////			params.add(new BasicNameValuePair(name, json.getString(name)));
////		}
////
////    	request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//        RequestHeader header = new RequestHeader(mcontext);
//
//        request.setHeader("X-User-Account-Id",header.getAccId());
//        request.setHeader("X-User-Id",header.getUserId());
//        request.setHeader("X-User-Hash", header.getPassword());
//        request.setHeader("mac",header.getMac());
//        request.setHeader("X-Device-Id",header.getDeviceId());
//        request.setHeader("X-School-Id", header.getSchoolId());
//        request.setHeader("resolution",header.getResolution());
//        request.setHeader("X-mobile-Type",header.getMobileType());
//        request.setHeader("version",header.getVersion());
//
////    	System.out.println(request.getFirstHeader("X-User-Account-Id"));
////    	System.out.println(request.getFirstHeader("X-User-Id"));
////    	System.out.println(request.getFirstHeader("X-User-Hash"));
////    	System.out.println(request.getFirstHeader("X-mobile-Type"));
////    	System.out.println(request.getFirstHeader("X-Device-Id"));
////    	System.out.println(request.getFirstHeader("X-School-Id"));
////    	System.out.println(request.getFirstHeader("resolution"));
////    	System.out.println(request.getFirstHeader("version"));
////    	System.out.println(request.getFirstHeader("mac"));
//        //request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//        request.setEntity(new StringEntity(json.toString(),HTTP.UTF_8));
//        HttpClient client = new DefaultHttpClient();
//
//        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT); // 超时设置
//        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);// 连接超时
//        HttpResponse response = client.execute(request);
//
//        int code = response.getStatusLine().getStatusCode();
//        if (code == Constants.SUCCESS_CODE) {
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                String result = EntityUtils.toString(entity, "UTF-8");
//                entity.consumeContent();
//                client.getConnectionManager().shutdown();
//                return result;
//            }
//        }
//        return null;
//    }


    //方法二 开线程，解决同步请求时，运行在主线程问题，这里开了子线程  上面有方法一    2016.10.28改为方法二了
    public static String httpPostJson(Context mcontext,String url , JSONObject json) throws IOException, JSONException{
        HttpPost request = new HttpPost(url);
        RequestHeader header = new RequestHeader(mcontext);

        request.setHeader("X-User-Account-Id",header.getAccId());
        request.setHeader("X-User-Id",header.getUserId());
        request.setHeader("X-User-Hash", header.getPassword());
        request.setHeader("mac",header.getMac());
        request.setHeader("X-Device-Id",header.getDeviceId());
        request.setHeader("X-School-Id", header.getSchoolId());
        request.setHeader("resolution",header.getResolution());
        request.setHeader("X-mobile-Type",header.getMobileType());
        request.setHeader("version",header.getVersion());
        request.setHeader("x-device-name",header.getDeviceType());
        request.setHeader("X-os-version",header.getOsVersion());
        request.setHeader("X-operator",header.getOperatorName());
        request.setHeader("x-device-brand",header.getDeviceBrand());
        request.setHeader("X-Device-Token",header.getDeviceToken());

        request.setEntity(new StringEntity(json.toString(),HTTP.UTF_8));
        HttpClient client = new DefaultHttpClient();

        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT); // 超时设置
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);// 连接超时

        Callable callable=new HttpCallable(client,request);
        ExecutorService es= Executors.newFixedThreadPool(1);
        String result="";
        try {
            Future future = es.submit(callable);
            result=(String)future.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        es.shutdownNow();

        return result;
    }

    //callable 和 runable 类似   用call() 不是用run();  有返回值
    public static class HttpCallable implements Callable {
        private HttpClient client;
        private HttpPost request;

        public HttpCallable(HttpClient client, HttpPost request) {
            this.client = client;
            this.request = request;
        }

        public Object call() throws Exception{
            String result="";
            try {
                HttpResponse response = client.execute(request);
                int code = response.getStatusLine().getStatusCode();
                if (code == Constants.SUCCESS_CODE) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        result = EntityUtils.toString(entity, "UTF-8");
                        entity.consumeContent();
                        client.getConnectionManager().shutdown();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }
    }


    public static String httpPostJson(String url,RequestHeader header,JSONObject json)
            throws IOException, JSONException{
        HttpPost request = new HttpPost(url);

        request.setHeader("X-User-Account-Id",header.getAccId());
        request.setHeader("X-User-Id",header.getUserId());
        request.setHeader("X-User-Hash", header.getPassword());
        request.setHeader("mac",header.getMac());
        request.setHeader("X-Device-Id",header.getDeviceId());
        request.setHeader("X-School-Id", header.getSchoolId());
        request.setHeader("resolution",header.getResolution());
        request.setHeader("X-mobile-Type",header.getMobileType());
        request.setHeader("version",header.getVersion());
        request.setHeader("x-device-name",header.getDeviceType());
        request.setHeader("X-os-version",header.getOsVersion());
        request.setHeader("X-operator",header.getOperatorName());
        request.setHeader("x-device-brand",header.getDeviceBrand());
        request.setHeader("X-Device-Token",header.getDeviceToken());

        //request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        request.setEntity(new StringEntity(json.toString(),HTTP.UTF_8));
        HttpClient client = new DefaultHttpClient();

        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT); // 超时设置
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);// 连接超时
        HttpResponse response = client.execute(request);
        int code = response.getStatusLine().getStatusCode();
        if (code == Constants.SUCCESS_CODE) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                entity.consumeContent();
                client.getConnectionManager().shutdown();
                return result;
            }
        }
        return null;
    }
    //获取服务器时间
    public static String httpPostJsonTime(Context mcontext,String url,JSONObject json)
            throws IOException, JSONException{
        HttpPost request = new HttpPost(url);
        RequestHeader header = new RequestHeader(mcontext);
        request.setHeader("X-User-Account-Id",header.getAccId());
        request.setHeader("X-User-Id",header.getUserId());
        request.setHeader("X-User-Hash", header.getPassword());
        request.setHeader("mac",header.getMac());
        request.setHeader("X-Device-Id",header.getDeviceId());
        request.setHeader("X-School-Id", header.getSchoolId());
        request.setHeader("resolution",header.getResolution());
        request.setHeader("X-mobile-Type",header.getMobileType());
        request.setHeader("version",header.getVersion());
        request.setHeader("x-device-name",header.getDeviceType());
        request.setHeader("X-os-version",header.getOsVersion());
        request.setHeader("X-operator",header.getOperatorName());
        request.setHeader("x-device-brand",header.getDeviceBrand());

        request.setEntity(new StringEntity(json.toString(),HTTP.UTF_8));
        HttpClient client = new DefaultHttpClient();

        client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT); // 超时设置
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);// 连接超时
        HttpResponse response = client.execute(request);
        Header time = response.getFirstHeader("X-Response-Time");

        if(time == null) return null;
        String date = time.getValue();
        System.out.println("serverTime X-Response-Time:"+date);

//    	Header busstime = response.getFirstHeader("X-Bussiness-Time");
//    	date = busstime.getValue();
//    	System.out.println("serverTime X-Bussiness-Time:"+date);
//    	Header timestamp = response.getFirstHeader("X-Timestamp");
//    	date = timestamp.getValue();
//    	System.out.println("serverTime X-Timestamp:"+date);
        int code = response.getStatusLine().getStatusCode();
        if (code == Constants.SUCCESS_CODE) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                entity.consumeContent();
                client.getConnectionManager().shutdown();
                ServiceExpiredBean service = JsonUtils.fromJson(result, ServiceExpiredBean.class);
                if(service.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
                    CCApplication.getInstance().setServiceExpiredInfo(service);
                }
            }
        }
        return date;
    }
    /**
     * 传输数据到服务端.
     * @param data 写入服务端的字节数组
     * @param url2 地址
     * @return 处理服务器的响应结果
     */
    public static String httpPostByte(byte[] data , String url2) {
        String newName ="image.jpg";
        String end ="\r\n";
        String twoHyphens ="--";
        String boundary ="*****";
        try {
            URL url = new URL(url2);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            //设置请求体的类型是文本类型
//            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-data-urlencoded");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            //设置请求体的长度
//            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            DataOutputStream ds =
                    new DataOutputStream(outputStream);
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "+
                    "name=\"file1\";filename=\""+
                    newName +"\""+ end);
            ds.writeBytes(end);
            ds.write(data);
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();

                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 上传文件到服务器.
     * @param uploadFile 上传文件的地址
     * @param url2 服务器的上传文件的接口地址
     * @return 服务器返回的结果
     */
    public static String uploadFile(String uploadFile , String url2) {
        String newName ="image.jpg";
        String end ="\r\n";
        String twoHyphens ="--";
        String boundary ="*****";
        try {
            URL url = new URL(url2);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            //设置请求体的类型是文本类型
//            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-data-urlencoded");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            //设置请求体的长度
//            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            DataOutputStream ds =
                    new DataOutputStream(outputStream);
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "+
                    "name=\"file1\";filename=\""+
                    newName +"\""+ end);
            ds.writeBytes(end);
            /* 取得文件的FileInputStream */
            FileInputStream fStream =new FileInputStream(uploadFile);
            /* 设置每次写入1024bytes */
            int bufferSize =1024;
            byte[] buffer =new byte[bufferSize];
            int length =-1;
            /* 从文件读取数据至缓冲区 */
            while((length = fStream.read(buffer)) !=-1)
            {
              /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();
            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 处理响应结果.
     * @param inputStream 输入流
     * @return 响应结果
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultData = new String(byteArrayOutputStream.toByteArray());

        return resultData;
    }

    /**
     * 文件下载
     * @param url：文件的下载地址
     * @param path：文件保存到本地的地址
     * @throws IOException
     */
    public static File downFile(String url,String path,String fileName) throws IOException{
        //下载函数
        int fileSize;
        //获取文件名
        URL myURL = new URL(url);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        fileSize = conn.getContentLength();//根据响应获取文件大小
        if (fileSize <= 0) throw new RuntimeException("无法获知文件大小 ");
        if (is == null) throw new RuntimeException("stream is null");
        File file1 = new File(path);
        File file2 = new File(path+fileName);
        if(!file1.exists()){
            file1.mkdirs();
        }
        if(!file2.exists()){
            file2.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(path+fileName);
        //把数据存入路径+文件名
        byte buf[] = new byte[1024];
        do{
            //循环读取
            int numread = is.read(buf);
            if (numread == -1){
                break;
            }
            fos.write(buf, 0, numread);

        } while (true);


        try{
            is.close();
        } catch (Exception ex) {
            System.out.println("error: " + ex.getMessage() + ex);
        }
        return file2;
    }

    /**
     * 文件下载
     * @param url：文件的下载地址
     * @throws IOException
     */
    public static String getJsonContent(String url) throws IOException{
        //下载函数
        int fileSize;

        //获取文件名
        URL myURL = new URL(url);
        URLConnection conn = myURL.openConnection();
        conn.connect();
        fileSize = conn.getContentLength();//根据响应获取文件大小
        if (fileSize <= 0) throw new RuntimeException("无法获知文件大小 ");

        InputStream input = conn.getInputStream();
        if (input == null) throw new RuntimeException("stream is null");
        BufferedReader in = new BufferedReader(new InputStreamReader(input,"UTF-8"));
        String line = "";
        String content = "";
        StringBuffer sb = new StringBuffer();
        while ((line = in.readLine()) != null) {
            content += line;
        }
        System.out.println(sb.toString());

        return content;
    }
}
