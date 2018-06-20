package com.wcyc.zigui2.newapp.videoutils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.utils.DataUtil;

import java.io.File;


/**
 * 拍摄页面
 *
 * @ClassName: VideoActivity
 * @Description:
 * @author: 郑国栋
 * @creationtime: 2017-02-14 下午3:43:25
 */
@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class VideoActivity extends BaseActivity implements OnClickListener {
    private ImageButton ivRecordStart, ivRecordStop;
    private ImageButton ivVideoManage;

    private MediaRecorder mRecorder;
    private CameraPreviewView previewView; // 视频预览的SurfaceView

    //  屏幕分辨率
//    public static int WIDTH = 1920;
//    public static int HEIGHT = 1080;
    public static int WIDTH = 1280;
    public static int HEIGHT = 720;

    // 记录是否正在进行录制
    private boolean isRecording = false;
    private String videoFilePath;
    private TextView video_time_tv;
    private TextView video_btn_tv;
    private long startTime=0;
    private long endTime=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initUI();
    }

    private void initUI() {
        // 获取屏幕分辨率
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        int screen_width = wm.getDefaultDisplay().getWidth();
//        int screen_height = wm.getDefaultDisplay().getHeight();
//        if (screen_width < screen_height) {
//            int temp = screen_width;
//            screen_width = screen_height;
//            screen_height = temp;
//        }
//        System.out.println(screen_width+"=screen_width======screen_height="+screen_height);
////
////        if (screen_width < WIDTH) {
//            WIDTH = screen_width;
//            HEIGHT = screen_height;
////        }

        TextView title2_off = (TextView) findViewById(R.id.title2_off);
        TextView title2_ok = (TextView) findViewById(R.id.title2_ok);
        TextView new_content = (TextView) findViewById(R.id.new_content);
        title2_ok.setVisibility(View.GONE);
        new_content.setText("视频拍摄");
//        new_content.setVisibility(View.GONE);
        title2_off.setOnClickListener(this);

        ivRecordStart = (ImageButton) findViewById(R.id.live_ib_record_start);
        ivRecordStop = (ImageButton) findViewById(R.id.live_ib_record_stop);
        ivVideoManage = (ImageButton) findViewById(R.id.live_ib_video_manage);

        ivRecordStop.setEnabled(false);
        ivRecordStart.setOnClickListener(this);
        ivRecordStop.setOnClickListener(this);
        ivVideoManage.setOnClickListener(this);

        previewView = (CameraPreviewView) this.findViewById(R.id.sView);
        previewView.getHolder().setFixedSize(WIDTH, HEIGHT);
        previewView.getHolder().setKeepScreenOn(true); // 设置该组件让屏幕不会自动关闭
        //设置surface不需要自己的缓冲区
        previewView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        video_time_tv = (TextView) findViewById(R.id.video_time_tv);
        video_btn_tv =(TextView) findViewById(R.id.video_btn_tv);
//        video_btn_tv.setVisibility(View.GONE);
        video_btn_tv.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //按下操作
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(startTime==0){
                        startTime=System.currentTimeMillis();
                    }
                    video_btn_tv.setText("松开停止");
                    video_btn_tv.setTextColor(getResources().getColor(
                            R.color.font_red));
                    if(!isRecording){
                        startRecord();
                    }
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){
                    }
                    return true;
                }
                //抬起操作
                if(event.getAction()==MotionEvent.ACTION_UP){
                    endTime=System.currentTimeMillis();
                    video_btn_tv.setText("按住录");
                    video_btn_tv.setTextColor(getResources().getColor(
                            R.color.font_darkblue));
                    if((endTime-startTime)<3000){
                        DataUtil.getToast("视频录制必须大于3秒");
                    }
                    stopRecord();

                    return true;
                }
                //移动操作
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            previewView.startPreview();
        }catch (Exception e){
//            DataUtil.getToast("相机异常，请重启手机");
        }
    }

    @Override
    protected void onPause() {
        try{
            recorderRelease();
            previewView.stopPreview();
        }catch (Exception e){
//            DataUtil.getToast("相机异常，请重启手机");
        }
        super.onPause();
    }

    @Override
    public void onClick(View source) {
        switch (source.getId()) {
            case R.id.title2_off:
                VideoActivity.this.finish();
                break;
            case R.id.live_ib_record_start:
                startRecord();
                break;
            case R.id.live_ib_record_stop:
                stopRecord();
                break;
        }
    }

    /**
     * 开始录像
     *
     * @author:
     * @creationtime: 2016-9-22 下午3:07:50
     */
    private void startRecord() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(VideoActivity.this, "SD卡不存在，请插入SD卡！", Toast.LENGTH_SHORT).show();
            return;
        }

        isRecording = true;
        ivRecordStart.setEnabled(false);
        ivRecordStop.setEnabled(true);

        try {
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
            }
            mRecorder.reset();

            // 使用设定了自动对焦的Camera对象，保证在视频录制的时候可以自动对焦，否则画面就是模糊了
            Camera camera = previewView.getCamera();
            if (camera != null) {
                camera.stopPreview();
                //设置相机 录制视频的方向   竖屏
                camera.setDisplayOrientation(90);
                camera.unlock();
                mRecorder.setCamera(camera);
            }

            // 设置从麦克风采集声音(或来自录像机的声音AudioSource.CAMCORDER)
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置从摄像头采集图像
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // 设置视频文件的输出格式
            // 必须在设置声音编码格式、图像编码格式之前设置
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 设置声音编码的格式   保持和ios一至AAC+MPEG_4_SP  音频苹果AAC格式才播出
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            // 设置图像编码的格式
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // 视频大小
            System.out.println(WIDTH + "=width====height=" + HEIGHT);
            mRecorder.setVideoSize(WIDTH, HEIGHT);
            // 每秒30帧
            mRecorder.setVideoFrameRate(30);
            // 设置视频编码帧率 (这个)
            mRecorder.setVideoEncodingBitRate(1 * WIDTH * HEIGHT);
            // 创建保存录制视频的视频文件
            videoFilePath = VideoFileUtil.createVideoFile().getAbsolutePath();
            System.out.println("=videoFilePath=" + videoFilePath);
            mRecorder.setOutputFile(videoFilePath);

            //设置录制视频的方向   竖屏
            mRecorder.setOrientationHint(90);

            // 指定使用SurfaceView来预览视频
            mRecorder.setPreviewDisplay(previewView.getHolder().getSurface());
            //设置视频最大时长
            mRecorder.setMaxDuration(10 * 1000);
//			//设置视频大小
            mRecorder.setMaxFileSize(6 * 1024 * 1024);
            // 一切就绪
            mRecorder.prepare();
            // 开始录制
            mRecorder.start();

            //倒计时10s
            runtime();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束录像
     *
     * @author:
     * @creationtime: 2016-9-22 下午3:08:02
     */
    private void stopRecord() {
        if (isRecording) {
            recorderRelease();
            isRecording=false;
            ivRecordStart.setEnabled(true);
            ivRecordStop.setEnabled(false);

        }
        if((endTime-startTime)>3000){
            // 返回
            returnVideo();
        }

        video_time_tv.setVisibility(View.GONE);
        startTime=0;
        endTime=0;
    }

    /**
     * 释放记录仪
     *
     * @author:
     * @creationtime: 2016-9-21 下午3:15:48
     */
    private void recorderRelease() {
        if (mRecorder != null) {
            try {
                if (isRecording) {
                    mRecorder.stop();
                }
                mRecorder.release();
                mRecorder = null;
            } catch (Exception e) {
//                DataUtil.getToast("相机异常，请从起手机");
            }
        }
    }

//	private long exitTime; // 最后一次按返回键的时间
//	@Override
//	public void onBackPressed() {
//    	if (System.currentTimeMillis() - exitTime > 1000 * 2){
//    		Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//        	exitTime = System.currentTimeMillis();
//    	} else {
//    		finish();
//    		System.exit(0);
//    	}
//	}

    /**
     * 返回调用它的界面
     */
    private void returnVideo() {

        //同时发送广播  通知各处更新最新的视频
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(videoFilePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

        Intent dataIntent = new Intent();
        dataIntent.putExtra("filePath", videoFilePath);
        dataIntent.putExtra("whichActivityReturn", "videoActivity");
        setResult(RESULT_OK, dataIntent);
        finish();
    }

    @Override
    protected void getMessage(String data) {

    }

    // 验证码倒计时功能
    private int time;

    private void runtime() {
        video_time_tv.setVisibility(View.VISIBLE);
        time = 10;
        handler.removeCallbacks(runnable_time);
        handler.postAtTime(runnable_time, 1000);
    }

    Runnable runnable_time = new Runnable() {
        @Override
        public void run() {
            setTimeView();
            time -= 1;
            if (time >= 0) {
                handler.postDelayed(this, 1000);
            } else {
                endTime=System.currentTimeMillis();
                stopRecord();
            }
        }
    };

    private void setTimeView() {
        video_time_tv.setText(time + "s");
    }

}