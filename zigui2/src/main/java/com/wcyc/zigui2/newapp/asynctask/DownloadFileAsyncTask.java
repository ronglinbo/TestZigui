package com.wcyc.zigui2.newapp.asynctask;


import java.util.List;

import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.ClassList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Gravity;
/**
 * @author xiehua
 * @version 1.0
 */
public class DownloadFileAsyncTask extends AsyncTask<String, Integer, String> {
	private Context context;
    private ProgressDialog pd; 
    private AsyncTaskListener mAsyncTaskListener;

 
    public DownloadFileAsyncTask(Context context,
    		AsyncTaskListener mAsyncTaskListener) {
    	this.mAsyncTaskListener = mAsyncTaskListener;
        this.context = context;
    }
  
 
    @Override
    protected void onPreExecute() {
    	pd = new ProgressDialog(context);
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(true);
		pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				cancel(true);
			}
		});
		pd.show();
		pd.setContentView(R.layout.progress_bar);
		pd.getWindow().setGravity(Gravity.CENTER);
		pd.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }
 
    @Override
    protected String doInBackground(String... params) {
         
        
        return "ok";
    }
 
    @Override
    protected void onProgressUpdate(Integer... progress) {
    }
 
    @Override
    protected void onPostExecute(String result) {
    	dismissPd();
    	if(result != null){
    		mAsyncTaskListener.onComplete(result);
    	}
    }
    
    @Override
    protected void onCancelled() {
    	dismissPd();
    	mAsyncTaskListener.onCancelled();
    }
    private void dismissPd(){
		if(pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}
}
