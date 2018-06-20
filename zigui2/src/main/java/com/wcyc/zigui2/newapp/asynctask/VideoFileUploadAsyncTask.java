package com.wcyc.zigui2.newapp.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.Gravity;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.utils.BitmapCompression;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.List;

import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;
import putFileby.CustomMultipartEntity;
import putFileby.CustomMultipartEntity.ProgressListener;

/**
 * @author xiehua
 * @version 1.0
 */
public class VideoFileUploadAsyncTask extends AsyncTask<String, Integer, String> {
	private Context context;
    private String filePath;
    private List<String> fileList;
    private long totalSize;
    private String requestUrl;
        private String Type;//tpye=1图片   =2视频和音频   =3文件文档  =6视频可获取缩略图
    private ProgressDialog pd;
    private ImageUploadAsyncTaskListener mImageUploadAsyncTaskListener;
    private HttpClient httpClient;
    private boolean isSingle = false;

    public VideoFileUploadAsyncTask(Context context, String Type, String filePath, String requestUrl,
                                    ImageUploadAsyncTaskListener mImageUploadAsyncTaskListener) {

    	this.mImageUploadAsyncTaskListener = mImageUploadAsyncTaskListener;
        this.context = context;
        this.filePath = filePath;
        this.requestUrl = requestUrl;
        this.Type = Type;
        isSingle = true;
    }

    public VideoFileUploadAsyncTask(Context context, String Type, List<String> fileList, String requestUrl,
                                    ImageUploadAsyncTaskListener mImageUploadAsyncTaskListener) {
    	
    	this.mImageUploadAsyncTaskListener = mImageUploadAsyncTaskListener;
        this.context = context;
        this.fileList = fileList;
        this.requestUrl = requestUrl;
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
        File filewenjian = null;
        try {
            CustomMultipartEntity multipartContent = new CustomMultipartEntity(
                    new ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });
            
            multipartContent.addPart("dirType",new StringBody(Type));

            if (!isSingle) {
                if (fileList != null) {
                    for (int i = 0; i < fileList.size(); i++) {
                        String file = fileList.get(i);
                        filewenjian = compressFile(file);
                        multipartContent.addPart("picture", new FileBody(
                                filewenjian));
                        String filename = file.substring(
                                file.lastIndexOf("/") + 1, file.length());
                        multipartContent.addPart("fileName", new StringBody(
                                filename, Charset.forName("UTF-8")));
                    }
                }
            } else {
                filewenjian = compressFile(filePath);
                multipartContent.addPart("picture", new FileBody(filewenjian));
                String filename = filePath.substring(
                        filePath.lastIndexOf("/") + 1, filePath.length());
                multipartContent.addPart("fileName", new StringBody(filename,
                        Charset.forName("UTF-8")));
            }
            
            totalSize = multipartContent.getContentLength();
            httpPost.setEntity(multipartContent);
            HttpResponse response = httpClient.execute(httpPost, httpContext);
            serverResponse = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponse;
    }

    private File compressFile(String filePath){
        File file = new File(filePath);

        try {
            //文件大小小于150k不压缩  视频文件
//            if(DataUtil.getFileSize(file) < 20*1024*1024) {
                return file;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Bitmap bitm = BitmapCompression.getimageTwo(filePath);
//        if (bitm != null) {
//            long time = System.currentTimeMillis();
//            file = new File(Constants.CACHE_PATH + String.valueOf(time) + ".jpeg");
//            System.out.println("fileName:"+file.getName());
//            FileOutputStream out = null;
//            try {
//                out = new FileOutputStream(file);
//                bitm.compress(Bitmap.CompressFormat.JPEG, 80, out);
//                out.flush();
//                out.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return file;
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