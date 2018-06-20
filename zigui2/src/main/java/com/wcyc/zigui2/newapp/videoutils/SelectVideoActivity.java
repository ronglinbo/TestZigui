package com.wcyc.zigui2.newapp.videoutils;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.module.classdynamics.NewPublishDynamicActivity;
import com.wcyc.zigui2.utils.BitmapCompression;
import com.wcyc.zigui2.widget.MarginDecoration;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 选择本地视频
 *
 * @author zhengguodong 2017-02-23
 */
public class SelectVideoActivity extends BaseActivity implements View.OnClickListener {

    private static final String saveVideoPath
            = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ZIGUI_Photos/";
    private Button title_btn;
    private RecyclerView gridview;
    private VideoGridViewImageRVAdapter selectVideoAdapter;
    private TextView button_send;
    private List<VideoModelBean> videoinfos = new ArrayList<VideoModelBean>(); // 视频文件集合
    public static List<Bitmap> bitmapList = new ArrayList<Bitmap>(); // 视频bitmap集合
    public static List<VideoModelBean> videoinfosTemp= new ArrayList<VideoModelBean>();
    private LinearLayout title_back;
    private ImageView no_data_iv;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_video_activity);
        //建立文件夹
        File cacheDir = new File(saveVideoPath);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        initView();
        initData();
        initEvents();
    }


    private void initView() {
        title_btn = (Button) findViewById(R.id.title_btn);
        gridview = (RecyclerView) findViewById(R.id.gridview);
        button_send = (TextView) findViewById(R.id.title_imgbtn_accomplish);  // 完成按钮
        button_send.setVisibility(View.VISIBLE);
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
        title_back.setVisibility(View.VISIBLE);
        no_data_iv = (ImageView) findViewById(R.id.no_data_iv);
        no_data_iv.setVisibility(View.GONE);
    }

    private void initData() {
        title_btn.setText("视频");
        videoinfos = getSDCardVideoFiles(Environment
                .getExternalStorageDirectory().getAbsolutePath());

        if (videoinfos != null) {
            String noSuportFileEnd=".rmvb,.flv,.mov,.mpg";
            for (int i = 0; i < videoinfos.size(); i++) {
                //删除不识别的视频
                String filePath=videoinfos.get(i).getPath();
                String filePathEnd=filePath.substring(filePath.lastIndexOf("."));
                if(noSuportFileEnd.contains(filePathEnd)){
                    videoinfos.remove(i);
                    --i;
                    continue;
                }

            System.out.println("===后=videoinfos.size()======" + videoinfos.size());

//
        }


            gridview.setHasFixedSize(true);
            //设置网格布局的列数
            gridview.setLayoutManager(new GridLayoutManager(this, 3));
            //给每一个item添加间距
            gridview.addItemDecoration(new MarginDecoration(this));
        if (videoinfos.size() > 0) {
            videoinfosTemp.add(videoinfos.get(0));
            VideoFrameImageLoader mVideoFrameImageLoader = new VideoFrameImageLoader(this, gridview, videoinfos);
            selectVideoAdapter = new VideoGridViewImageRVAdapter(this,mVideoFrameImageLoader,button_send);
            gridview.setAdapter(selectVideoAdapter);

        } else {
            gridview.setVisibility(View.GONE);
            no_data_iv.setVisibility(View.VISIBLE);
        }
    }
    }

    private void initEvents() {
        title_back.setOnClickListener(this);
        button_send.setOnClickListener(this);
        button_send.setClickable(false);
        button_send.setTextColor(getResources().getColor(R.color.font_lightgray));//灰色禁用 不可点击   蓝色测试
        title_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                videoinfosTemp.clear();
                bitmapList.clear();
                finish();
                break;
			case R.id.title_btn:
                videoinfosTemp.clear();
                bitmapList.clear();
                finish();
                break;
            case R.id.title_imgbtn_accomplish:
                videoinfosTemp.clear();
                bitmapList.clear();
                String videoFilePath = selectVideoAdapter.getVideoFilePath();
                Intent dataIntent = new Intent();
                dataIntent.putExtra("filePath", videoFilePath);
                dataIntent.putExtra("whichActivityReturn", "selectVideoActivity");
                setResult(RESULT_OK, dataIntent);
                finish();
                break;
        }
    }

    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            videoinfosTemp.clear();
            bitmapList.clear();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void getMessage(String data) {

    }

    // 数据库读取视频文件
    public List<VideoModelBean> getSDCardVideoFiles(String path) {
        List<VideoModelBean> list = new ArrayList<VideoModelBean>();
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DURATION};
        Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection, null, null,
                MediaStore.Video.Media.DATE_ADDED+" desc");
        cursor.moveToFirst();
        int count = cursor.getCount();
        System.out.println("===原=count======" + count);
        for (int i = 0; i < count; i++) {
            String address = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Video.Media.DATA));
            String name = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Video.Media.TITLE));
            long time = cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            VideoModelBean model = new VideoModelBean();
            model.setPath(address);
            model.setName(name);
            model.setLength(time);
            list.add(model);
            cursor.moveToNext();
        }
        return list;
    }

    // 通过文件路径获取文件的大小,并自动格式化
    public String getVideoLength(String filePath) {
        String string = "0B";
        DecimalFormat df = new DecimalFormat();
        File file = new File(filePath);
        long length = file.length();
        if (length == 0) { // 文件大小为0,直接返回0B
            return string;
        }
        if (length < 1024) { // 文件小于1KB,单位为 B
            string = df.format((double) length) + "B";
        } else if (length < 1048576) {// 文件小于1M,单 位为 KB
            string = df.format((double) length / 1024) + "K";
        } else if (length < 1073741824) {// 文件小于1G,单 位为 MB
            string = df.format((double) length / 1048576) + "M";
        } else {
            string = df.format((double) length / 1073741824) + "G";
        }
        return string;
    }

    //kind 为MINI_KIND和MICRO_KIND。其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
    //MediaStore.Images.Thumbnails.MICRO_KIND
    public Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        bitmap=  BitmapCompression.compressImage1(bitmap);
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
