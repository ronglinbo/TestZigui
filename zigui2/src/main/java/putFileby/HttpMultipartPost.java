package putFileby;



import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.wcyc.zigui2.listener.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.utils.BitmapCompression;
import com.wcyc.zigui2.utils.BitmapTool;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.UploadUtil;
 

import putFileby.CustomMultipartEntity.ProgressListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;
 
public class HttpMultipartPost extends AsyncTask<String, Integer, String> {
 
    private Context context;
    private String filePath;
    private ArrayList<String> fileList;
    //private ProgressDialog pd;
    private long totalSize;
    private String requestUrl;
    private String ID;
    private String Type;
    private String order;
    private ImageUploadAsyncTaskListener mImageUploadAsyncTaskListener;   
    private BitmapCompression bitmapCompression;
    private boolean isCompress;
 
    public HttpMultipartPost(Context context,String Type,String ID, String filePath, String requestUrl) {
        this.context = context;
        this.filePath = filePath;
        this.requestUrl = requestUrl;
        this.ID = ID;
        this.Type = Type;
    }
    
    public HttpMultipartPost(Context context,String Type,String ID,
    		String filePath, String requestUrl,String order,
    		boolean isCompress) {
        this.context = context;
        this.filePath = filePath;
        this.requestUrl = requestUrl;
        this.ID = ID;
        this.Type = Type;
        this.order = order;
        this.isCompress = isCompress;

    }
    
    public HttpMultipartPost(Context context,String Type,String ID,
    		String filePath, String requestUrl,String order,
    		boolean isCompress,ImageUploadAsyncTaskListener mImageUploadAsyncTaskListener) {
        this.context = context;
        this.filePath = filePath;
        this.requestUrl = requestUrl;
        this.ID = ID;
        this.Type = Type;
        this.order = order;
        this.isCompress = isCompress;
        this.mImageUploadAsyncTaskListener = mImageUploadAsyncTaskListener;
    }
 
    public HttpMultipartPost(Context context,String Type,String ID,ArrayList<String> fileList, 
    		String requestUrl, boolean isCompress,ImageUploadAsyncTaskListener mImageUploadAsyncTaskListener){
    	this.context = context;
    	this.fileList = fileList;
    	this.ID = ID;
    	this.Type = Type;
    	this.requestUrl = requestUrl;
    	this.isCompress = isCompress;
        this.mImageUploadAsyncTaskListener = mImageUploadAsyncTaskListener;
    }
    @Override
    protected void onPreExecute() {
        //pd = new ProgressDialog(context);
        //pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //pd.setMessage("Uploading Picture...");
        //pd.setCancelable(false);
        //pd.show();
    }
 
    @Override
    protected String doInBackground(String... params) {
        String serverResponse = null;
        File file = null;
		try {
			file = handleFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
          
        if(file == null){
        	file = new File(filePath);
        }

        serverResponse = sendFile(file);
//        serverResponse = UploadUtil.uploadFile(file, Constants.IMG_SERVER_URL);
//        for(int i = 0; i < fileList.size(); i++){
//        	String filename = fileList.get(i);
//        	File file = handleFile(filename,i);
//        	if(file == null){
//            	file = new File(filename);
//            }
//        	serverResponse = sendFile(file,i);
//        }
        return serverResponse;
    }
    
    private File handleFile(String filename,int index){
    	File file = null;   
    	if(isCompress){
//	        Bitmap bitm = bitmapCompression.getimage(filename);
    		Bitmap bitm = null;
	        try {
				bitm = BitmapTool.getBitmap(context, filename);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        System.out.println("compress index:"+index);
			if(bitm != null){
				file = new File(Environment.getExternalStorageDirectory().getPath()+"/namecard"+index+".png");
				
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);
					bitm.compress(Bitmap.CompressFormat.PNG, 100, out); 
					out.flush(); 
				    out.close(); 
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
        }else{
        	file = new File(filePath);
        }
    	return file;
    }
    
    private File handleFile() throws FileNotFoundException{
    	File file = null,tempFile = null;   
    	if(isCompress){
	        int i = Integer.parseInt(order);
//	        Bitmap bitm = bitmapCompression.getimage(filePath);
	        Bitmap bitm = DataUtil.fitSizeImg(filePath);
	        tempFile = new File(filePath);
	        
			if(bitm != null){
				file = new File(Environment.getExternalStorageDirectory().getPath()+"/namecard"+i+".jpeg");
				
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);
					bitm.compress(Bitmap.CompressFormat.JPEG, 100, out); 
					out.flush(); 
				    out.close(); 
				} catch (Exception e) {
					e.printStackTrace();
				} 
				System.out.println("before:"+tempFile.length()/1024+"kb,size:"+ 
						bitm.getByteCount()/1024+"kb,after:"+file.length()/1024+"kb");
			}
        }else{
        	file = new File(filePath);
        }
    	return file;
    }
    
    private String sendFile(File file,int index){
    	String serverResponse = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(requestUrl);
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
            multipartContent.addPart("order",new StringBody(""+index));
            
            multipartContent.addPart("picture", new FileBody(file));
            totalSize = multipartContent.getContentLength();

            httpPost.setEntity(multipartContent);
        	System.out.println("httpPost:"+httpPost);
            HttpResponse response = httpClient.execute(httpPost, httpContext);
            
            serverResponse = EntityUtils.toString(response.getEntity());
            System.out.println("serverResponse "+serverResponse);
            //serverResponse = HttpHelper.uploadFile(filePath, requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return serverResponse;
    }

    private String sendFile(File file){
    	
    	String serverResponse = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(requestUrl);
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
            if(order != null){
            	multipartContent.addPart("order",new StringBody(order));
            }

            multipartContent.addPart("picture", new FileBody(file));
            totalSize = multipartContent.getContentLength();

            httpPost.setEntity(multipartContent);
            System.out.println("httpPost： "+httpPost);
            HttpResponse response = httpClient.execute(httpPost, httpContext);
            
            serverResponse = EntityUtils.toString(response.getEntity());
            System.out.println("serverResponse "+serverResponse);
            //serverResponse = HttpHelper.uploadFile(filePath, requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return serverResponse;
    }
 
    @Override
    protected void onProgressUpdate(Integer... progress) {
        //pd.setProgress((int) (progress[0]));
    }
 
    @Override
    protected void onPostExecute(String results) {
    	
        System.out.println("result: " + results);
        if(results == null){
        	System.out.println("cancel: " + results);
        	cancel(true);
        	mImageUploadAsyncTaskListener.onImageUploadCancelled();
        }
        if(results != null&&mImageUploadAsyncTaskListener != null){
    		mImageUploadAsyncTaskListener.onImageUploadComplete(results,ID);
    	}        
        //pd.dismiss();
    }
 
    
    
    @Override
    protected void onCancelled() {
        System.out.println("cancled");
    }
 
}