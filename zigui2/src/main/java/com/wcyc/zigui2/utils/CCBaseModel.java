/*
* 文 件 名:CCBaseModel.java
* 创 建 人： 姜韵雯
* 日    期： 2014-09-23 
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.utils;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

//import com.wcyc.zigui2.core.BaseActivity.RequestHeader;

//2014-9-23
/**
 * http通信base解析模型
 * 
 */
public class CCBaseModel extends CCModel {

	private static final long serialVersionUID = 3893675823352951728L;
	static String TAG = CCBaseModel.class.getSimpleName();
	final static String cacheFile = Constants.CACHE_PATH + "cache/base";
    private Callbacks mCallbacks;
    
    private static final String URL_BASE = SERVER_URL;
    
    /**
     * 0 get查询 1 post查询
     */
    private int action;
    private final int QUERYGET = 0;
    private final int QUERYPOST = 1;
    /**
     * post方式时传入的数据
     */
    private JSONObject jsonObject;
    private static RequestHeader header;
	private String url;
	private Context mContext;
    
	public CCBaseModel(Activity paramContext, Callbacks callbacks) {
	    super(paramContext);
	    this.mCallbacks = callbacks;
	    mContext = paramContext;
	}
	public void registeCallback(Callbacks callbacks){
		this.mCallbacks = callbacks;
	}
	/**
	 * 加载缓存
	 */
	@SuppressWarnings("unchecked")
    public void LoadingCache(){
	    Object obj = ObjCache.get(cacheFile);
        if(obj != null){
        	mCallbacks.bindItem(obj);
        }
	}
	
	private String getPostData(String url , JSONObject jsonObject) {
//	    Log.d(TAG, "getData{线程ID="+Thread.currentThread().getId()+"}");
	    String json = null;
        try {
        	json = HttpHelper.httpPostJson(url, jsonObject);
            ObjCache.create(json, cacheFile);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
	}
	
	private String getPostData(String url ,RequestHeader header,JSONObject jsonObject) {
	    String json = null;
        try {
        	//修改为HttpUrlConnection
        	json = HttpUtils.useHttpUrlConnectionPost(mContext, url, jsonObject);
//        	json = HttpHelper.httpPostJson(url, header,jsonObject);
            ObjCache.create(json, cacheFile);
        } 
//          catch (ClientProtocolException e) {q
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e){
//            e.printStackTrace();
//        }
          catch (Exception e){
            e.printStackTrace();
        }
        return json;
	}
	
	private String getGetData(String url) {
//	    Log.d(TAG, "getData{线程ID="+Thread.currentThread().getId()+"}");
	    String json = null;
        try {
            json = HttpHelper.httpGet(url);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
	}
    @Override
    public void process() {
        Log.d(TAG, "process{线程ID="+Thread.currentThread().getId()+"}");
        mCallbacks.startBinding();
        // 网络是否可用
        boolean isConnected = DataUtil.isNetworkAvailable(mContext);
        if (!isConnected) {
        	mCallbacks.bindCode(-1);
        	mCallbacks.finishBindingItems();
        	return;
        }
        switch (action) {
		case QUERYGET:
//	        if (!isConnected) {
//	        	LoadingCache();
//	            return;
//	        }else{
//	        }
			mCallbacks.bindItem(getGetData(url));
			break;
		case QUERYPOST:
			mCallbacks.bindItem(getPostData(url, header,jsonObject));
			break;
		default:
			break;
		}
        mCallbacks.finishBindingItems();
    }


    public void queryGet(String url){
    	action = QUERYGET;
        this.url = url;
        startLoader();
    }

    public void queryPost(String url , JSONObject json){
    	action = QUERYPOST;
        this.url = new StringBuilder(URL_BASE).append(url).toString();
        jsonObject = json;
        startLoader();
    }
    
    public void queryPost(String url , RequestHeader header,JSONObject json){
    	action = QUERYPOST;
        this.url = new StringBuilder(URL_BASE).append(url).toString();
        jsonObject = json;
        CCBaseModel.header = header;
        startLoader();
    }
    
}
