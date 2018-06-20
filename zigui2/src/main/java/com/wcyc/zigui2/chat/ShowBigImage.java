/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wcyc.zigui2.chat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatConfig;
import com.easemob.chat.EMChatManager;
import com.easemob.cloud.CloudOperationCallback;
import com.easemob.cloud.HttpFileManager;
import com.easemob.util.ImageUtils;
import com.easemob.util.PathUtil;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.utils.BitmapSave;
import com.wcyc.zigui2.utils.RepeatOnClick;
import com.wcyc.zigui2.widget.photoview.PhotoView;
import com.wcyc.zigui2.widget.photoview.PhotoViewAttacher;

/**
 * 下载显示大图
 *
 */
public class ShowBigImage extends BaseActivity {

    private ProgressDialog pd;
    private PhotoView image;
    private int default_res = R.drawable.default_avatar;
    // flag to indicate if need to delete image on server after download
    private boolean deleteAfterDownload;
    private boolean showAvator;
    private String localFilePath;
    private String username;
    private Bitmap bitmap;
    private boolean isDownloaded;
    private ProgressBar loadLocalPb;
    private ImageView btn_save_char;
    static final String TAG = "ShowBigImage";

    private LinearLayout title_back;
    private TextView new_content;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show_big_image);
        super.onCreate(savedInstanceState);

        image = (PhotoView) findViewById(R.id.image);
        btn_save_char = (ImageView) findViewById(R.id.btn_save_char);
        loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);

        title_back = (LinearLayout) findViewById(R.id.title_back);
        new_content = (TextView) findViewById(R.id.new_content);
        new_content.setVisibility(View.INVISIBLE);

        default_res = getIntent().getIntExtra("default_image", R.drawable.default_avatar);
        showAvator = getIntent().getBooleanExtra("showAvator", false);
        username = getIntent().getStringExtra("username");
        deleteAfterDownload = getIntent().getBooleanExtra("delete", false);
        final Uri uri = getIntent().getParcelableExtra("uri");
        final String remotepath = getIntent().getExtras().getString("remotepath");
        String secret = getIntent().getExtras().getString("secret");
        Log.i(TAG, "uri:" + uri + " remotepath:" + remotepath);
        //本地存在，直接显示本地的图片
        if (uri != null && new File(uri.getPath()).exists()) {
            System.err.println("showbigimage file exists. directly show it");
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            // int screenWidth = metrics.widthPixels;
            // int screenHeight =metrics.heightPixels;
            bitmap = ImageCache.getInstance().get(uri.getPath());
            if (bitmap == null) {
                LoadLocalBigImgTask task = new LoadLocalBigImgTask(this, uri.getPath(), image, loadLocalPb, ImageUtils.SCALE_IMAGE_WIDTH,
                        ImageUtils.SCALE_IMAGE_HEIGHT);
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    task.execute();
                }
            } else {
                image.setImageBitmap(bitmap);
            }
        } else if (remotepath != null) { //去服务器下载图片
            System.err.println("download remote image");
            Map<String, String> maps = new HashMap<String, String>();
            String accessToken = EMChatManager.getInstance().getAccessToken();
            maps.put("Authorization", "Bearer " + accessToken);
            if (!TextUtils.isEmpty(secret)) {
                maps.put("share-secret", secret);
            }
            maps.put("Accept", "application/octet-stream");
            downloadImage(remotepath, maps);
        } else {
            image.setImageResource(default_res);
        }
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //photoView 点击效果不生效
/*
        image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
*/
        image.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });

        btn_save_char.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (RepeatOnClick.isFastClick()) {
                    return;
                }
                btn_save_char.setClickable(false);
                //add by xiehua 20151029还需要修改，直接显示网上下载的图片
                if (uri != null) {
                    bitmap = ImageCache.getInstance().get(uri.getPath());
                    //end
                }
                Log.i(TAG, "bitmap:" + bitmap);
                if (bitmap != null) {
                    try {
                        String img_path = BitmapSave.saveFile(getBaseContext(), bitmap);


                        Toast.makeText(ShowBigImage.this, "图片已成功保存至：" + img_path, Toast.LENGTH_SHORT).show();
//						btn_save_char.setVisibility(View.VISIBLE);
                        btn_save_char.setClickable(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ShowBigImage.this, "保存失败", Toast.LENGTH_SHORT).show();
                }

//				 HttpGet httpRequest = new HttpGet(remotepath);
//				 HttpClient httpclient = new DefaultHttpClient();
//				 try {
//					HttpResponse httpResponse = httpclient.execute(httpRequest);
//					  if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){   
//			                //取得相关信息 取得HttpEntiy  
//			                HttpEntity httpEntity = httpResponse.getEntity();  
//			                //获得一个输入流  
//			                InputStream is = httpEntity.getContent();  
//			                Bitmap bitmap = BitmapFactory.decodeStream(is);  
//			                is.close();  
//			                BitmapSave.saveFile(bitmap);
//			                Toast.makeText(ShowBigImage.this, "保存成功", 0).show();
//			            } 
//				} catch (Exception e) {
//					e.printStackTrace();
//					 Toast.makeText(ShowBigImage.this, "保存失败", 0).show();
//				} 
            }
        });
    }

    /**
     * 下载图片
     *
     * @param remoteFilePath
     */
    private void downloadImage(final String remoteFilePath, final Map<String, String> headers) {
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("下载图片: 0%");
        pd.show();
        if (!showAvator) {
            if (remoteFilePath.contains("/"))
                localFilePath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/"
                        + remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
            else
                localFilePath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/" + remoteFilePath;
        } else {
            if (remoteFilePath.contains("/"))
                localFilePath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/"
                        + remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
            else
                localFilePath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/" + remoteFilePath;

        }

        Log.i(TAG, "showAvator:" + showAvator + " localFilePath:" + localFilePath);
        final HttpFileManager httpFileMgr = new HttpFileManager(this, EMChatConfig.getInstance().getStorageUrl());
        final CloudOperationCallback callback = new CloudOperationCallback() {
            public void onSuccess(String resultMsg) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        int screenWidth = metrics.widthPixels;
                        int screenHeight = metrics.heightPixels;

                        bitmap = ImageUtils.decodeScaleImage(localFilePath, screenWidth, screenHeight);
                        if (bitmap == null) {
                            image.setImageResource(default_res);
                        } else {
                            image.setImageBitmap(bitmap);
                            ImageCache.getInstance().put(localFilePath, bitmap);
                            isDownloaded = true;
                        }
                        if (pd != null) {
                            pd.dismiss();
                        }
                    }
                });
            }

            public void onError(String msg) {
                Log.e("###", "offline file transfer error:" + msg);
                File file = new File(localFilePath);
                if (file.exists()) {
                    file.delete();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        image.setImageResource(default_res);
                    }
                });
            }

            public void onProgress(final int progress) {
                Log.d("ease", "Progress: " + progress);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.setMessage("下载图片: " + progress + "%");
                    }
                });
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                httpFileMgr.downloadFile(remoteFilePath, localFilePath, EMChatConfig.getInstance().APPKEY, headers, callback);
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        if (isDownloaded) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    protected void getMessage(String data) {
    }
}