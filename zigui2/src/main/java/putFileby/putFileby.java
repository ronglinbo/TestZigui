package putFileby;
 

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;
 


public class putFileby extends Thread {

	private String picURL;
	private String userID;
	public String http_url;
	
	private MultipartEntity entity;
	
	public putFileby(String http_url,String picURL,String userID)
	{
		this.http_url = http_url;
		this.picURL = picURL;
		this.userID = userID;
	}
	
	public void run()
	{
		putFile();
	}
	
	private void putFile() 
	{
		
		
		entity=new MultipartEntity();
		try {
			entity.addPart("userID",new StringBody(userID, Charset.forName("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entity.addPart("picture",new FileBody(new File(picURL)));
		 
		HttpPost request=new HttpPost(http_url);
		request.setEntity(entity);
		HttpClient client=new DefaultHttpClient();
		try {
			 HttpResponse response = client.execute(request);
			 
			 Log.d("putimage", "上传完成！");
			 
			 if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			 {  
	                //InputStream is = response.getEntity().getContent();  
	                
	                String responseString = EntityUtils.toString(  
	                		response.getEntity(), HTTP.UTF_8);
	                
	                Log.d("got response:\n"  , responseString);  
	   
	            }  
			 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
