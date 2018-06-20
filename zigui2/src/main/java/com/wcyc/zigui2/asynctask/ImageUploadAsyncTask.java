package com.wcyc.zigui2.asynctask;

import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.listener.ImageUploadAsyncTaskListener;

import putFileby.CustomMultipartEntity;
import putFileby.CustomMultipartEntity.ProgressListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.Gravity;
/**
 * @author xfliu
 * @version 1.13
 */
public class ImageUploadAsyncTask extends AsyncTask<String, Integer, String> {
	private Context context;
    private String filePath;
    private long totalSize;
    private String requestUrl;
    private String ID;
    private String Type;
    private ProgressDialog pd; 
    private ImageUploadAsyncTaskListener mImageUploadAsyncTaskListener;
    private HttpClient httpClient;

    public ImageUploadAsyncTask(Context context,String Type,String ID, 
    		String filePath, String requestUrl,ImageUploadAsyncTaskListener mImageUploadAsyncTaskListener) {
    	this.mImageUploadAsyncTaskListener = mImageUploadAsyncTaskListener;
        this.context = context;
        this.filePath = filePath;
        this.requestUrl = requestUrl;
        this.ID = ID;
        this.Type = Type;


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
				httpClient.getConnectionManager().shutdown();
			}
		});
		pd.show();
		pd.setContentView(R.layout.progress_bar);
		pd.getWindow().setGravity(Gravity.CENTER);
		pd.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }
 
    @Override
    protected String doInBackground(String... params) {
        String serverResponse = null;
        if(isCancelled()){
			return serverResponse;
		}
        httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(requestUrl);
        System.out.println("url is " + requestUrl);
        try {
            CustomMultipartEntity multipartContent = new CustomMultipartEntity(
                    new ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });
 
            multipartContent.addPart("ID",new StringBody(ID));
            multipartContent.addPart("Type",new StringBody(Type));
            
            multipartContent.addPart("picture", new FileBody(new File(filePath)));
            totalSize = multipartContent.getContentLength();
            httpPost.setEntity(multipartContent);
            HttpResponse response = httpClient.execute(httpPost, httpContext);
            serverResponse = EntityUtils.toString(response.getEntity());
            System.out.println("ImageUploadAsyncTask "+serverResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }
 
    @Override
    protected void onProgressUpdate(Integer... progress) {
    }
 
    @Override
    protected void onPostExecute(String result) {
    	dismissPd();
    	if(result != null){
    		mImageUploadAsyncTaskListener.onImageUploadComplete(result);
    	}
    }
    
    @Override
    protected void onCancelled() {
    	dismissPd();
    	mImageUploadAsyncTaskListener.onImageUploadCancelled();
    }
    private void dismissPd(){
		if(pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}
}
