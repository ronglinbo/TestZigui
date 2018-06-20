package com.wcyc.zigui2.updatesystem;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewVersionCheckModel;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.widget.CustomDialog;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 软件版本更新
 *
 *
 * */

public class NewUpdateVer {
	private static final String TAG = "DOWNLOADAPK";
	private String desc;
	public ProgressDialog pBar;
	private CustomDialog dialog;
	public boolean mustUp = false;
	private boolean is360Url = false;//是否从360应用市场下载
	private String currentFilePath = "";
	private String fileEx = "";
	//private String fileNa="";
	private String strURL = "";
	//private String VersionUri ="";
	private Context mContext;
	private final String fileVer = "ver.cfg";

	public NewUpdateVer(String desc,String urlapk, final Context context){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String ver = "?ver=" + df.format(new Date());//主要是避开手机的缓存
		System.out.println(ver);
		strURL = "/" + urlapk + ver;
		System.out.println(strURL);
		this.desc = desc;
		mContext = context;
	}

	public NewUpdateVer(NewVersionCheckModel ret, final Context context){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String ver = "?ver=" + df.format(new Date());//主要是避开手机的缓存
		System.out.println(ver);
		String url = ret.getUpdateAddress();
		if(url.contains("http")||url.contains("360")){
			strURL = ret.getUpdateAddress();
			is360Url =  true;
		}else {
			strURL = "/" + ret.getUpdateAddress() + ver;
		}
		System.out.println(strURL);
		String newVersion = String.format("发现新版本%s\r\n",ret.getVersionName());
		this.desc = newVersion + ret.getUpdateExplain();
		mContext = context;
	}

	public void checkVer() {
		// 解析Version网页，获取版本号
		getVersionxml();
	}

	public boolean ifSDCard(){
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}else
			return false;
	}

	/**
	 * 控制CustomDialog按钮效果.
	 */
	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);

			switch (msg.what) {
				case CustomDialog.DIALOG_CANCEL:// 取消
					if(mustUp){
						CCApplication.app.finishAllActivity();
					}
					dialog.dismiss();
					break;
				case CustomDialog.DIALOG_SURE://立即更新
					forceUpdate();
					dialog.dismiss();
					break;
				default:
					break;
			}
		};
	};

	private void forceUpdate(){
		if(ifSDCard()){
			//有卡 下载到本地安装
			pBar = new ProgressDialog(mContext);
			pBar.setTitle("正在下载");
			pBar.setMessage("请稍候...");
			pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pBar.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			if(!is360Url){
				fileEx = strURL.substring(strURL.lastIndexOf(".") + 1, strURL.length());
				fileEx = fileEx.substring(0, fileEx.lastIndexOf("?"));
				strURL = DataUtil.getDownloadURLWithNoId(fileEx);
			}
			getFile(strURL);
		}else{
			//无卡用web方式
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(strURL);
			intent.setData(content_url);
			mContext.startActivity(intent);
		}
	}

	private void compareVer() {
		//当有最新版本的时候
		try {
			dialog = new CustomDialog(mContext, R.style.mystyle,
					R.layout.update_ver_dialog, handler);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			dialog.setContent(String.format("%s", desc));

			Button negative = (Button) dialog.findViewById(R.id.cancel_btn);
			Button update = (Button) dialog.findViewById(R.id.confirm_btn);
			update.getPaint().setFakeBoldText(true);
			if (mustUp) {
				dialog.setTitle("强制更新提示");
				negative.setText("退出");
				dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							CCApplication.app.finishAllActivity();
						}
						return false;
					}
				});
			} else {
				dialog.setTitle("更新提示");
				if (negative != null) {
					negative.setText("下次再说");
					negative.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(BaseActivity.UPDATE_VERSION_SUCCESS);
							mContext.sendBroadcast(intent);
							dialog.dismiss();
						}
					});
				}
				dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							Intent intent = new Intent(BaseActivity.UPDATE_VERSION_SUCCESS);
							mContext.sendBroadcast(intent);
						}
						return false;
					}
				});
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void getFile(final String strPath){
		try {
			pBar.show();
		}catch (Exception e){
			e.printStackTrace();
		}
		try{
			if (strPath.equals(currentFilePath) ){
				getDataSource(strPath);
//				downLoadApk(strPath);
			}
			currentFilePath = strPath;
			Runnable r = new Runnable(){
				@Override
				public void run() {
					try{
						System.out.println("下载远程apk:"+strPath);
						getDataSource(strPath);
//						downLoadApk(strPath);
					} catch (Exception e){
						Log.e(TAG, e.getMessage(), e);
					}
				}
			};
			new Thread(r).start();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	private void downLoadApk(String url){
		String subForder = Environment
				.getExternalStorageDirectory() + "/";
		String fileName = "ziguinew.apk";
		try {
			HttpHelper.downFile(url, subForder, fileName);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/*取得远程文件*/
	private void getDataSource(String strPath) throws Exception {
		System.out.println("开始下载远程apk:"+strPath);

		if (!URLUtil.isNetworkUrl(strPath)) {
			Log.d("Tag","error 文件路径错误");
			System.out.println("文件路径错误:"+strPath);
		} else {
			/*取得URL*/
			URL myURL = new URL(strPath);
			/*建立联机*/
			HttpURLConnection conn = (HttpURLConnection)myURL.openConnection();
			conn.connect();
			int length = 0;
			if(conn.getResponseCode() == HttpStatus.SC_OK) {
				length = conn.getContentLength();
			}
			/*InputStream 下载文件*/
			InputStream is = conn.getInputStream();

			if (is == null || length <= 0) {
				Log.d("tag","error没有读取到文件内容");
				System.out.println("没有读取到文件内容:"+strPath);
				throw new RuntimeException("没有读取到文件内容");
			}

			String savePath;
			String saveFileName;

			if(ifSDCard()){
				savePath = Environment
						.getExternalStorageDirectory() + "/";
				saveFileName = savePath + "ziguinew.apk";
				File myTempFile = new File(saveFileName);

				/*将文件写入临时盘*/
				FileOutputStream fos = new FileOutputStream(myTempFile);
				int bytesum = 0;
				byte buf[] = new byte[2048];
				do{
					int numread = is.read(buf);
					bytesum += numread;
					if (numread <= 0) {
						break;
					}
					fos.write(buf, 0, numread);
				}while (true);

				/*打开文件进行安装*/
				openFile(myTempFile);

				try {
					is.close();
				} catch (Exception ex){
					Log.d("Tag","error");
					System.out.println("保存文件错误:"+ex.getMessage());
					Log.e(TAG, "保存文件错误 error: " + ex.getMessage(), ex);
				}
			}else{
				//无卡
			}
		}
	}

	/* 在手机上打开文件 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void openFilebyName(String f){
		System.out.println("准备打开文件" );
		pBar.cancel();
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		/* 调用getMIMEType()来取得MimeType */

		Uri uri = Uri.fromFile(new File(f));
		/* 设定intent的file与MimeType */
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		//intent.setDataAndType(uri, "")
		((BaseActivity)mContext).startActivityForResult(intent,1,null);
	}


	/* 在手机上打开文件 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void openFile(File f){
		System.out.println("准备打开文件" );
		pBar.cancel();
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		/* 调用getMIMEType()来取得MimeType */
		String type = getMIMEType(f);
		System.out.println("文件类型 ： " + type + f.length());
		/* 设定intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(f),type);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		mContext.startActivity(intent);
		((BaseActivity)mContext).startActivityForResult(intent,1);
	}


	/* 判断文件MimeType的method */
	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* 取得扩展名 */
		String end = fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();

		/* 按扩展名的类型决定MimeType */
		if(end.equals("m4a")
				|| end.equals("mp3")
				|| end.equals("mid")
				|| end.equals("xmf")
				|| end.equals("ogg")
				|| end.equals("wav")){
			type = "audio";
		}
		else if(end.equals("3gp") || end.equals("mp4")){
			type = "video";
		} else if(end.equals("jpg")
				|| end.equals("gif")
				|| end.equals("png")
				|| end.equals("jpeg")
				|| end.equals("bmp")){
			type = "image";
		} else if(end.equals("apk")){
		/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else{
			type = "*";
		}
		/*如果无法直接打开，就跳出软件清单给使用者选择 */
		if(!end.equals("apk")){
			type += "/*";
		}
		return type;
	}

	private void getVersionxml(){
		try {
			compareVer();
		}catch (Exception e){
			e.printStackTrace();
		}
//		GetVer gv = new GetVer();
//		gv.execute();
	}




	//==========================================================================
// GetVer
//==========================================================================
	class GetVer extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... urlVer) {
			return "";
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			compareVer();
		}
	}
}