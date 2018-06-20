package com.wcyc.zigui2.newapp.videoutils;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.utils.DataUtil;

/**
 * 郑国栋 20170207 2.0.10
 */
public class VideoViewActivity extends BaseActivity implements View.OnClickListener {
    VideoView videoView;
    // 创建一个MediaController的对象用于控制视频的播放
    MediaController mediaController;
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    private int pagerPosition;

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.videoview_activity);
        TextView new_content = (TextView) findViewById(R.id.new_content);// 标题
        LinearLayout title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
        new_content.setText("视频预览");
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(VideoViewActivity.this);
        String subForder = Environment.getExternalStorageDirectory() + "/ZIGUI_Photos/";
        //先创建一个文件夹
        File dir = new File(subForder);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 获取界面上的VideoView组件
        videoView = (VideoView) findViewById(R.id.videoView);
        //显示正在加载
        DataUtil.showDialog(VideoViewActivity.this);
        //加载完回调，隐藏正在加载
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                DataUtil.clearDialog();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                    /*
                    错误常数

MEDIA_ERROR_IO
文件不存在或错误，或网络不可访问错误
值: -1004 (0xfffffc14)

MEDIA_ERROR_MALFORMED
流不符合有关标准或文件的编码规范
值: -1007 (0xfffffc11)

MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK
视频流及其容器不适用于连续播放视频的指标（例如：MOOV原子）不在文件的开始.
值: 200 (0x000000c8)

MEDIA_ERROR_SERVER_DIED
媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
值: 100 (0x00000064)

MEDIA_ERROR_TIMED_OUT
一些操作使用了过长的时间，也就是超时了，通常是超过了3-5秒
值: -110 (0xffffff92)

MEDIA_ERROR_UNKNOWN
未知错误
值: 1 (0x00000001)

MEDIA_ERROR_UNSUPPORTED
比特流符合相关编码标准或文件的规格，但媒体框架不支持此功能
值: -1010 (0xfffffc0e)


what int: 标记的错误类型:
    MEDIA_ERROR_UNKNOWN
    MEDIA_ERROR_SERVER_DIED
extra int: 标记的错误类型.
    MEDIA_ERROR_IO
    MEDIA_ERROR_MALFORMED
    MEDIA_ERROR_UNSUPPORTED
    MEDIA_ERROR_TIMED_OUT
    MEDIA_ERROR_SYSTEM (-2147483648) - low-level system error.

* */
                if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                    //媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
                    Toast.makeText(VideoViewActivity.this,
                            "网络服务错误",
                            Toast.LENGTH_LONG).show();
                } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN){
                    if (extra == MediaPlayer.MEDIA_ERROR_IO) {
                        //文件不存在或错误，或网络不可访问错误
                        Toast.makeText(VideoViewActivity.this,
                                "网络文件错误",
                                Toast.LENGTH_LONG).show();
                        VideoViewActivity.this.finish();
                    } else if (extra == MediaPlayer.MEDIA_ERROR_TIMED_OUT) {
                        //超时
                        Toast.makeText(VideoViewActivity.this,
                                "网络超时",
                                Toast.LENGTH_LONG).show();
                        VideoViewActivity.this.finish();
                    } else {
                        Toast.makeText(VideoViewActivity.this,
                                "未知错误",
                                Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        //播放完监听，不能播放也会执行这里，隐藏正在加载
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                DataUtil.clearDialog();
                VideoViewActivity.this.finish();
            }
        });

        // 初始化mediaController
        mediaController = new MediaController(this);
        mediaController.setVisibility(View.VISIBLE);//设置进度条一直显示
//		mediaController.show();
//		mediaController.show(0);
//		mediaController.setAnchorView(videoView);
        mediaController.setKeepScreenOn(true);

        // 将videoView与mediaController建立关联
        videoView.setMediaController(mediaController);
        // 将mediaController与videoView建立关联
        mediaController.setMediaPlayer(videoView);

        pagerPosition = this.getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        List<PictureURL> urls = (List<PictureURL>) getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
//		File vFile = new File(path);
//		if (vFile.exists()) {// 如果文件存在

        //加载本地视频
        //videoView.setVideoPath(vFile.getAbsolutePath());

        //初始化网络视频链接
        //String httpMp4Path="http://www.hsz88.cn/video/hszmain.mp4";
        //加载网络视频 setVideoURI(Uri uri)：加载uri所对应的视频

        //String httpMp4Path="http://cs.ziguiw.cn:8021/dls/download?fileId=9472&authId=MB@864690025904296@14480";
        String httpMp4Path = urls.get(0).getPictureURL();
        Uri uri = Uri.parse(httpMp4Path);
        System.out.println("===视频地址uri==" + uri);
        videoView.setVideoURI(uri);
        // 让videoView获得焦点
        videoView.requestFocus();
        videoView.start();


//		}else{
//			System.out.println("===不存在==");
//		}
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                VideoViewActivity.this.finish();
                break;
        }
    }

    @Override
    protected void getMessage(String data) {

    }

}
