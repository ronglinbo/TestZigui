package com.dh.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.PlaySDK.IPlaySDK;

import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_RealStream_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Ptz_Direct_Info_t;
import com.dh.DpsdkCore.Ptz_Operation_Info_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.dh.Player.Player;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.utils.DataUtil;

import static com.company.PlaySDK.Constants.PLAY_CMD_GetMediaInfo;

public class RealPlayActivity extends Activity{

	public final static String IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/snapshot/";
	public final static String IMGSTR = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
	private static final int PicFormat_JPEG = 1;
	
	private TextView etCam;
	private TextView tvRet;
	private TextView title;
	private View tip;

	private byte[] m_szCameraId = null;
	private int m_pDLLHandle = 0;
	SurfaceView m_svPlayer = null;
	private int m_nPort = 0;
	private int m_nSeq = 0;
	private int mTimeOut = 30*1000;
	private int PLAYSDK_BUF_SIZE = 1024*1024*3;
	private boolean isPort = true;
	private boolean exitFullScreen = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_real_play);
		handleScreenRotate();
		m_pDLLHandle = CCApplication.get().getDpsdkHandle();
		 // 查找控件
        findViews();
		setListener();
        m_szCameraId = getIntent().getStringExtra("channelId").getBytes();
		title.setText(getIntent().getStringExtra("channelName"));
		System.out.println("RealPlayActivity onCreate");
		initSurface();
		openVideo();
	}

	private void handleScreenRotate(){
		if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			isPort = false;
		}else{
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			isPort = true;
		}
	}

	private void rotateScreen(){
		if(isPort) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			isPort = false;
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			isPort = true;
		}
	}
	@Override
	public void onConfigurationChanged(Configuration config){
		System.out.println("RealPlayActivity onConfigurationChanged");
		super.onConfigurationChanged(config);
		if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}else{
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setContentView(R.layout.fragment_real_play);
			findViews();
			initSurface();
			openVideo();
		}
	}

	private void setListener(){
		if(m_svPlayer != null){
			m_svPlayer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					rotateScreen();
				}
			});
		}
	}

	private void initSurface(){
		if(isPort) {
			DisplayMetrics dm = DataUtil.getDisplayMetrics(RealPlayActivity.this);
			int width = dm.widthPixels;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width/4*3);
			m_svPlayer.setLayoutParams(params);
		}
		m_nPort = IPlaySDK.PLAYGetFreePort();
		SurfaceHolder holder = m_svPlayer.getHolder();
		holder.addCallback(new Callback() {
			public void surfaceCreated(SurfaceHolder holder) {
				Log.d("xss", "surfaceCreated");

				IPlaySDK.InitSurface(m_nPort, m_svPlayer);
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int width,
									   int height) {
				System.out.println("surfaceChanged width:" + width + " height:" + height);
			}

			public void surfaceDestroyed(SurfaceHolder holder)
			{
				Log.d("xss", "surfaceDestroyed");
			}
		});
	}

	private void openVideo(){
		final fMediaDataCallback fm = new fMediaDataCallback() {

			@Override
			public void invoke(int nPDLLHandle, int nSeq, int nMediaType,
							   byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) {
//				DataUtil.clearDialog();
				int ret = IPlaySDK.PLAYInputData(m_nPort, szData, nDataLen);
			}
		};
		if(!StartRealPlay()){
			Log.e("xss", "StartRealPlay failed!");
//			DataUtil.clearDialog();
			StopRealPlay();
			Toast.makeText(getApplicationContext(), "打开摄像头失败!", Toast.LENGTH_SHORT).show();
			return;
		}

		try{
			Return_Value_Info_t retVal = new Return_Value_Info_t();
			Get_RealStream_Info_t getRealStreamInfo = new Get_RealStream_Info_t();
			//m_szCameraId = etCam.getText().toString().getBytes();
			System.arraycopy(m_szCameraId, 0, getRealStreamInfo.szCameraId, 0, m_szCameraId.length);
			//getRealStreamInfo.szCameraId = "1000096$1$0$0".getBytes();
			getRealStreamInfo.nMediaType = 1;
			getRealStreamInfo.nRight = 0;
			getRealStreamInfo.nStreamType = 2;
			getRealStreamInfo.nTransType = 1;
			Enc_Channel_Info_Ex_t ChannelInfo = new Enc_Channel_Info_Ex_t();
			IDpsdkCore.DPSDK_GetChannelInfoById(m_pDLLHandle, m_szCameraId, ChannelInfo);
			int ret = IDpsdkCore.DPSDK_GetRealStream(m_pDLLHandle, retVal, getRealStreamInfo, fm, mTimeOut);
			if(ret == 0){
				m_nSeq = retVal.nReturnValue;
				Log.e("GetRealStream success!",ret+"");
//				DataUtil.showDialog(this,"正在打开摄像头");
				Toast.makeText(getApplicationContext(), "正在打开摄像头", Toast.LENGTH_SHORT).show();
//				int width[] = new int[100];
//				int height[] = new int[100];
//				IPlaySDK.PLAYGetPictureSize(m_nPort,width,height);
//				System.out.println("PLAYGetPictureSize");
			}else{
				StopRealPlay();
//				DataUtil.clearDialog();
				handleErrorResult(ret);
			}
		}catch(Exception e){
			StopRealPlay();
			Log.e("xss", e.toString());
		}
	}

	private void handleErrorResult(int result){
		switch(result){
			case 9:
				tip.setVisibility(View.VISIBLE);
				m_svPlayer.setVisibility(View.GONE);
				break;
			case 1000432:
				Toast.makeText(getApplicationContext(), "找不到设备!", Toast.LENGTH_SHORT).show();
				tip.setVisibility(View.VISIBLE);
				m_svPlayer.setVisibility(View.GONE);
				break;
			case 1000557:
				Toast.makeText(getApplicationContext(), "设备不在线!", Toast.LENGTH_SHORT).show();
				tip.setVisibility(View.VISIBLE);
				m_svPlayer.setVisibility(View.GONE);
				break;
			default:
				System.out.println("GetRealStream failed! " + result);
				Toast.makeText(getApplicationContext(), "打开摄像头画面失败!", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	private void closeVideo(){
		//int ret = IDpsdkCore.DPSDK_CloseRealStreamByCameraId(m_pDLLHandle, m_szCameraId, mTimeOut);
		int ret = IDpsdkCore.DPSDK_CloseRealStreamBySeq(m_pDLLHandle, m_nSeq, mTimeOut);
		if(ret == 0){
			Log.e("xss","DPSDK_CloseRealStreamByCameraId success! "+ m_nSeq);
		}else{
			Log.e("xss","DPSDK_CloseRealStreamByCameraId failed! ret = " + ret +" seq:"+ m_nSeq) ;
//			Toast.makeText(getApplicationContext(), "关闭摄像头失败!", Toast.LENGTH_SHORT).show();
		}
		StopRealPlay();
	}

	private void findViews(){
		View view = findViewById(R.id.video_play_title);
		title = (TextView) view.findViewById(R.id.title_text_2);
		title.setVisibility(View.VISIBLE);
		tvRet = (TextView)findViewById(R.id.tv_excute_result);
		m_svPlayer = (SurfaceView)findViewById(R.id.sv_player);
		tip = findViewById(R.id.no_moniter);
		View back = view.findViewById(R.id.title_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				closeVideo();
				finish();
			}
		});
	}

	/**
     * 创建文件夹  保存截图 图片
     */
    private void captureBitmap() {
    	
        String path = IMAGE_PATH + IMGSTR;
    	//先创建一个文件夹
    	File dir = new File(IMAGE_PATH);
    	File file = new File(IMAGE_PATH, IMGSTR);
        if(!dir.exists()) {
        	dir.mkdir();
        } else {
        	if(file.exists()) {
        		file.delete();
        	}
        }
    	
        int result = IPlaySDK.PLAYCatchPicEx(m_nPort, path, PicFormat_JPEG);
        Log.i("PLAYCatchPicEx", String.valueOf(result));
        if (result > 0) {
        	showToast(R.string.capture_success);
        	saveIntoMediaCore();
        } else {
            showToast(R.string.capture_fail);
        }
    }
    
    private void saveIntoMediaCore(){
    	Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    	//intent.setAction(MEDIA_ROUTER_SERVICE);
    	Uri uri = Uri.parse(IMAGE_PATH + IMGSTR);
    	intent.setData(uri);
    	RealPlayActivity.this.setIntent(intent);
    }

    private void showToast(int str){
		Toast.makeText(getApplicationContext(), getResources().getString(str), Toast.LENGTH_SHORT).show();
	}

	public void StopRealPlay() {
    	try {
    		IPlaySDK.PLAYStopSoundShare(m_nPort);
    		IPlaySDK.PLAYStop(m_nPort);  		
    		IPlaySDK.PLAYCloseStream(m_nPort);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public boolean StartRealPlay() {
        if(m_svPlayer == null)
        	return false;
        
        boolean bOpenRet = IPlaySDK.PLAYOpenStream(m_nPort,null,0, PLAYSDK_BUF_SIZE) == 0? false : true;
    	if(bOpenRet) {
			boolean bPlayRet = IPlaySDK.PLAYPlay(m_nPort, m_svPlayer) == 0 ? false : true;
			System.out.println("IPlaySDK.PLAYPlay:" + bPlayRet);
			if(bPlayRet) {
				boolean bSuccess = IPlaySDK.PLAYPlaySoundShare(m_nPort) == 0 ? false : true;

				if(!bSuccess) {
//					IPlaySDK.PLAYStop(m_nPort);
//					IPlaySDK.PLAYCloseStream(m_nPort);
					System.out.println("PLAYPlaySoundShare:"+bSuccess);
					return false;
				}
			} else {
//				IPlaySDK.PLAYCloseStream(m_nPort);
				System.out.println("IPlaySDK.PLAYPlay:" + bPlayRet);
				return false;
			}
		} else {
//			IPlaySDK.PLAYCloseStream(m_nPort);
			System.out.println("IPlaySDK.PLAYPlay:" + bOpenRet);
    		return false;
    	}
        
        return true;
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		closeVideo();
	}

}
