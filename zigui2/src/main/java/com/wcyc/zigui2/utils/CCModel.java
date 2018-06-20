/*
* 文 件 名:CCModel.java
* 创 建 人： 姜韵雯
* 日    期： 2014-09-23 
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.utils;

import java.util.HashMap;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

//2014-9-23
/**
 * 数据加载接口
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public abstract class CCModel extends HashMap<String, Object> implements Constants {

	private static final long serialVersionUID = -8231540585448177946L;

	static String TAG = CCModel.class.getSimpleName();
	
	private final Object mLock = new Object();
	private LoaderTask mLoaderTask;
	
	protected Activity mContext;
	protected Handler mHandler;
	
	private static final HandlerThread sWorkerThread = new HandlerThread("loader");
    static {
        sWorkerThread.start();
    }
    private static final Handler sWorker = new Handler(sWorkerThread.getLooper());
    
	public CCModel(Activity paramContext) {
	    mContext = paramContext;
	    mHandler = new Handler();
	}
	
    public void startLoader() {
    	//只有当外界没有显示进度条的时候这里才调用显示进度条
    	if(DataUtil.pd == null || !DataUtil.pd.isShowing())DataUtil.showDialog(mContext);
        synchronized (mLock) {
            Log.d(TAG, "startLoader{启动线程加载数据...}");
            mLoaderTask = new LoaderTask(this);
            sWorkerThread.setPriority(Thread.NORM_PRIORITY);
            sWorker.post(mLoaderTask);
        }
    }
    
    public void stopLoader() {
        synchronized (mLock) {
            if (mLoaderTask != null) {
                mLoaderTask.stopLocked();
            }
        }
    }
    public abstract void process();
}
