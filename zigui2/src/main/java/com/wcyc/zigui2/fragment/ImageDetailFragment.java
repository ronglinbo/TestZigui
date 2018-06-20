package com.wcyc.zigui2.fragment;


import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chat.ShowBigImage;
import com.wcyc.zigui2.utils.BitmapSave;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.RepeatOnClick;
import com.wcyc.zigui2.widget.photoview.PhotoViewAttacher;
import com.wcyc.zigui2.widget.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private TextView loading_percentage;
	private ImageView btn_save;
	private Thread mThread;
	static final String TAG = "com.wcyc.zigui2.ImageDetailFragment";
	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
		mImageView = (ImageView) v.findViewById(R.id.big_image);
		loading_percentage = (TextView) v.findViewById(R.id.loading_percentage);
		btn_save = (ImageView) v.findViewById(R.id.btn_save);
		
		mAttacher = new PhotoViewAttacher(mImageView);
//		mAttacher.setZoomable(false);
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				if(null != getActivity()){
					getActivity().finish();
				}
			}
		});
		
		progressBar = (ProgressBar) v.findViewById(R.id.big_loading);
		
		btn_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//add by xiehua for duplicate click 20151102
				btn_save.setEnabled(false);
				/*if (RepeatOnClick.isFastClick()) {
					 return ;
				}*/
//				 mSaveDialog = ProgressDialog.show(getActivity(), "保存图片", "图片正在保存中，请稍等...", true);
//	                new Thread(saveFileRunnable).start();
				DataUtil.showDialog(getActivity(), "图片正在保存中，请稍等...");
				mThread = new Thread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub

						try {
							String subForder = Environment.getExternalStorageDirectory()+"/ZIGUI_Photos/";
							DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
							String time = formatter.format(new Date());
							String fileName = time+".jpeg";
							File file = HttpHelper.downFile(mImageUrl, subForder,fileName);
							Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
							Uri uri = Uri.fromFile(file);
							intent.setData(uri);
							System.out.println("uri:"+uri);
							getActivity().sendBroadcast(intent);
							DataUtil.getToast("图片已成功保存至：" + subForder+fileName,0);
							//add by xiehua for duplicate click 20151102
							DataUtil.clearDialog();
							btn_save.setEnabled(true);
						} catch (Exception e) {
						    e.printStackTrace();
						    DataUtil.clearDialog();
						} 
					}

				});
				mThread.start();

				 
			}
		});
//		mImageView.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				Toast.makeText(getActivity(), mImageUrl, 0).show();
//				return false;
//			}
//		});
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		ImageLoaderConfiguration config =DataUtil.getImageLoaderConfig(getActivity());
		mImageLoader = ImageLoader.getInstance();
//		mImageLoader.init(config);
		options = DataUtil.getImageLoaderOptions(); 
//		mImageLoader.displayImage(mImageUrl, mImageView,options);
		mImageLoader.displayImage(mImageUrl, mImageView,options, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setVisibility(View.VISIBLE);
				LayoutParams params = mImageView.getLayoutParams();
				params.height = LayoutParams.WRAP_CONTENT;
				params.width = LayoutParams.WRAP_CONTENT;
				mImageView.setLayoutParams(params);
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "下载错误";
					break;
				case DECODING_ERROR:
					message = "图片无法显示";
					break;
				case NETWORK_DENIED:
					message = "网络有问题，无法下载";
					break;
				case OUT_OF_MEMORY:
					message = "图片太大无法显示";
					break;
				case UNKNOWN:
					message = "未知的错误";
					break;
				}
				if(getActivity() != null)
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
				if(View.GONE == progressBar.getVisibility())
				{
					LayoutParams params = mImageView.getLayoutParams();
					params.height = LayoutParams.MATCH_PARENT;
					params.width = LayoutParams.MATCH_PARENT;
					mImageView.setLayoutParams(params);
				}
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
				//add by xiehua for bug not to show save button when picture is downloading 
				showSaveBtnOrNot();
				LayoutParams params = mImageView.getLayoutParams();
				params.height = LayoutParams.MATCH_PARENT;
				params.width = LayoutParams.MATCH_PARENT;
				mImageView.setLayoutParams(params);
				mAttacher.update();
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				progressBar.setVisibility(View.GONE);
				loading_percentage.setVisibility(View.GONE);
				LayoutParams params = mImageView.getLayoutParams();
				params.height = LayoutParams.MATCH_PARENT;
				params.width = LayoutParams.MATCH_PARENT;
				mImageView.setLayoutParams(params);
				mAttacher.update();
			}
			
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current,int total) {
				int percentage ;//下载百分比
				percentage = (current / total) *100;
				loading_percentage.setText(percentage+"%");
				if(percentage == 100){
					progressBar.setVisibility(View.GONE);
					loading_percentage.setVisibility(View.GONE);
					LayoutParams params = mImageView.getLayoutParams();
					params.height = LayoutParams.MATCH_PARENT;
					params.width = LayoutParams.MATCH_PARENT;
					mImageView.setLayoutParams(params);
					mAttacher.update();
				}
			}
		}
		);
	}
	
	public void showSaveBtnOrNot(){
		String local = mImageUrl.replace("file://", "");
		if(!DataUtil.isFileExists(local)){
			System.out.println("is not exist:"+local);
		    btn_save.setVisibility(View.VISIBLE);
		}else{
			System.out.println("is exist:"+local);
			btn_save.setVisibility(View.GONE);
		}
	}
}
