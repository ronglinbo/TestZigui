package com.wcyc.zigui2.newapp.asynctask;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.HttpUtils;
/**
 * 可通用的异步任务请求
 */
public class HttpRequestAsyncTask extends AsyncTask<String, Integer, String> {
	private JSONObject json;
	private HttpRequestAsyncTaskListener httpRequestAsyncTaskListener;
	private ProgressDialog pd; 
	private Context context;
	boolean isShowProcess = true;

	public HttpRequestAsyncTask(JSONObject json,
			HttpRequestAsyncTaskListener httpRequestAsyncTaskListener
			,Context context){
		this.json = json;
		this.httpRequestAsyncTaskListener = httpRequestAsyncTaskListener;
		this.context = context;
	}

	public HttpRequestAsyncTask(JSONObject json,
								HttpRequestAsyncTaskListener httpRequestAsyncTaskListener
			,Context context,boolean isShowProcess){
		this.json = json;
		this.httpRequestAsyncTaskListener = httpRequestAsyncTaskListener;
		this.context = context;
		this.isShowProcess = isShowProcess;
	}
	@Override
	
	protected void onPreExecute() {
		super.onPreExecute();
		try{
			if(isShowProcess) {
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void onCancelled() {
		super.onCancelled();
		dismissPd();
		httpRequestAsyncTaskListener.onRequstCancelled();
	}
	private void dismissPd(){
		if(pd != null && pd.isShowing()) {
			try {
				pd.dismiss();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if(pd != null) {
			pd.setProgress(values[0]);
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
		String result = null;
		if(isCancelled()){
			return result;
		}
		if(DataUtil.isNetworkAvailable(context)){
			try{
				String urls = new StringBuilder(Constants.SERVER_URL).append(params[0]).toString();
//				result = HttpHelper.httpPostJson((BaseActivity) context,urls,json);
				result = HttpUtils.useHttpUrlConnectionPost(context, params[0], json);
//				System.out.println("result:"+result);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			if(context != null)
				DataUtil.getToast(context.getResources().getString(R.string.no_network));
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		dismissPd();
		if(result != null){
//			Log.i("临时LOG","onPostExecute返回结果："+result);
			httpRequestAsyncTaskListener.onRequstComplete(result);
		}else{
			httpRequestAsyncTaskListener.onRequstCancelled();
		}
	}
}
