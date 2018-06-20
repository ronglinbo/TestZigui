/*
* 文 件 名:LoaderTask.java
* 创 建 人： 姜韵雯
* 日    期： 2014-09-23 
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.utils;

import android.util.Log;

import okhttp3.Headers;

//2014-9-23
/**
 * 数据加载线程
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public class LoaderTask implements Runnable {
    static String TAG = LoaderTask.class.getSimpleName();
    private CCModel mModel;
    private boolean mStopped;

    public LoaderTask(CCModel syModel) {
        mModel = syModel;
    }

    @Override
    public void run() {
        Log.d(TAG, "run{线程ID="+Thread.currentThread().getId()+"}");
        if(mModel == null){
            return;
        }
        keep_running: {
            if (mStopped) {
                break keep_running;
            }
            mModel.process();
        }
        DataUtil.clearDialog();
    }
    
    public void stopLocked() {
        synchronized (LoaderTask.this) {
            mStopped = true;

            this.notify();
        }
    }
    
}
