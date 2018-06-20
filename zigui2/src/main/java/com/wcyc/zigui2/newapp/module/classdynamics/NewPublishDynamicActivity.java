package com.wcyc.zigui2.newapp.module.classdynamics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.adapter.TConfAddImageGvAdapter;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.bean.PutNoticeBean;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.ImagePagerActivity;
import com.wcyc.zigui2.newapp.asynctask.VideoFileUploadAsyncTask;
import com.wcyc.zigui2.newapp.home.TConfigrChoiceClassActivity;

import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.GradeleaderBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewPublishBean;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.videoutils.SelectVideoActivity;
import com.wcyc.zigui2.newapp.videoutils.VideoFileUtil;
import com.wcyc.zigui2.newapp.videoutils.VideoViewActivity;
import com.wcyc.zigui2.newapp.videoutils.VideoActivity;
import com.wcyc.zigui2.utils.BitmapCompression;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;
import com.wcyc.zigui2.widget.ExpandGridView;
import com.wcyc.zigui2.widget.SpinnerButton;

/**
 * 新版app 班级动态发布界面
 *
 * @author yytan
 * @version 2.0
 * @since 2.0
 */

public class NewPublishDynamicActivity extends BaseActivity implements
		OnClickListener, ImageUploadAsyncTaskListener,SaveClassPop.SelectPicOrVideo {
	private PutNoticeBean ret;
	private static final int REQUEST_CODE = 100;
	private static final int VEDIO_CODE = 200;
	private EditText publish_dynamic_content;
	private static final int MAX_IMAGE = 9;
	private ExpandGridView publish_dynamic_addiv_gv;
	private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
	private ArrayList<String> transmitImagePaths = new ArrayList<String>();// 传递给服务器的集合
	private TConfAddImageGvAdapter gvAdapter;
	public final static String UPLOADING_TYPE = "5";
	private static final String TAG = NewPublishDynamicActivity.class
			.getSimpleName();
	private final int ACTION_ADD = 1;
	private final int ACTION_FINISH = 2;
	private final int ACTION_PUBIESH_CLASS_DYNAMIC = 101;
	private int nowImageNumber = 0;
	BitmapCompression bitmapCompression;
	private static String mMsgID;
	private static int upload_nums = 0;

	private class Task {
		String msgID;// 发送的msgID
		int uploadNum;// 已经发送的图片数量
		int picNum;// 总共需要发送的图片数目
		int taskID;// 当前的任务号
	}
	private boolean is_compress;
	// private static boolean hasUnfinishedTask = false;
	public static final String INTENT_BEGIN_UPLOAD_PICTURE = "com.wcyc.zigui2.action.UPLOAD_PICTURE_BEGIN";
	public static final String INTENT_FINISH_UPLOAD_PICTURE = "com.wcyc.zigui2.action.UPLOAD_PICTURE_FINISH";
	public static final String INTENT_CANCEL_UPLOAD_PICTURE = "com.wcyc.zigui2.action.UPLOAD_PICTURE_CANCEL";
	public static final int REASON_BACK = 1;
	public static final int REASON_CANCEL = 2;
	public static final int MAX_TASKS = 10;
	// public static boolean fromWeb = true;
	/**
	 * 发布消息时候，去相册选取图片后 返回的路径集合，封装为List&lt;PictureURL&gt;集合
	 */
	List<PictureURL> datas = new ArrayList<PictureURL>();
	static ArrayList<Task> task = new ArrayList<Task>();
	private ImageView publish_dynamic_content_delete_icon;
	public TextView title2_off, title2_ok, new_content;
	public ArrayList<String> classIDList, njmcList, classNameList, njidList;
	private SpinnerButton spinnerButton;
	private ListView spinnerListView;
	public int class_i = -1;
	private List<NewClasses> cList;
	private List<NewClasses> cList0930;
	private List<NewClasses> cList_aa;
	private String usertype;
	public ImageView class_vedio_iv,video_del_iv;
	private RelativeLayout class_vedio_ll;
	private DisplayImageOptions options;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	private Boolean isVideo=false;
	private int publish_dynamic_addiv_gv_position=0;
	private ArrayList<String> videoPaths = new ArrayList<String>();// 视频选择集合
	private ArrayList<String> transmitVideoPaths = new ArrayList<String>();// 视频传递给服务器的集合
	private int nowVideoNumber = 0;
	public static List<Bitmap> bitmapList = new ArrayList<Bitmap>(); // 视频bitmap集合

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		setContentView(R.layout.new_publish_dynamic);

		initView();
		initDatas();
		initEvents();

		inputState();
	}

	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);
			if (TConfAddImageGvAdapter.DELETE_IMAGES == msg.what) {
				int p = (Integer) msg.obj;
				imagePaths.remove(p);
				transmitImagePaths.remove(p);
				nowImageNumber = imagePaths.size();
				gvAdapter.notifyDataSetChanged();

				String spinnerButton_str=spinnerButton.getText().toString().trim();
				String publish_dynamic_content_str=publish_dynamic_content.getText().toString();

				if(nowImageNumber>0){
					class_vedio_iv.setVisibility(View.GONE);
					class_vedio_ll.setVisibility(View.GONE);
				}else {
//					class_vedio_iv.setVisibility(View.GONE);
				}

				Boolean alreadyChooseClass =false;
				if(!"选择发送到的班级".equals(spinnerButton_str)){
					alreadyChooseClass=true;
				}

				if (alreadyChooseClass&&(nowImageNumber > 0||publish_dynamic_content_str.length()>0||nowVideoNumber > 0)) {
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_darkblue));
				} else {
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_lightgray));
				}
			}
		}
	};

	/**
	 * 初始化控件
	 */
	private void initView() {
		publish_dynamic_content = (EditText) findViewById(R.id.publish_dynamic_content);
		publish_dynamic_content_delete_icon = (ImageView) findViewById(R.id.publish_dynamic_content_delete_icon);
		TextFilter textFilter1 = new TextFilter(publish_dynamic_content);
//		publish_dynamic_content.addTextChangedListener(textFilter1);// 设置不能输入空格
		textFilter1.setEditeTextClearListener(publish_dynamic_content,
				publish_dynamic_content_delete_icon);

		publish_dynamic_addiv_gv = (ExpandGridView) findViewById(R.id.publish_dynamic_addiv_gv);
		gvAdapter = new TConfAddImageGvAdapter(this, imagePaths,
				getImageLoader(), handler);
		publish_dynamic_addiv_gv.setAdapter(gvAdapter);
		bitmapCompression = new BitmapCompression();

		title2_off = (TextView) findViewById(R.id.title2_off);
		title2_ok = (TextView) findViewById(R.id.title2_ok);
		new_content = (TextView) findViewById(R.id.new_content);
		spinnerButton = (SpinnerButton) findViewById(R.id.sb_pd);

		class_vedio_iv = (ImageView) findViewById(R.id.class_vedio_iv);
		class_vedio_ll = (RelativeLayout) findViewById(R.id.class_vedio_ll);
		video_del_iv= (ImageView) findViewById(R.id.video_del_iv);
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
			new_content.setText("发布动态");
		usertype = CCApplication.getInstance().getPresentUser().getUserType();
		if ("2".equals(usertype)) {

			cList = CCApplication.app.getMemberDetail().getClassList();// 任教班级
			if(cList!=null){
				cList0930=new ArrayList<NewClasses>();
				cList0930.addAll(cList);
			}

			String usertype_a = CCApplication.getInstance().getPresentUser()
					.getUserType();
			System.out.println("当前用户角色  下标=======" + usertype_a);

			boolean allowAllClassTag=false;
			boolean gradeleader=false;
			MemberDetailBean detail=CCApplication.getInstance().getMemberDetail();
			for (int i = 0; i < detail.getRoleList().size(); i++) {
				String roleCode = detail.getRoleList().get(i).getRoleCode();

				if("schooladmin".equals(roleCode)){
					allowAllClassTag=true;
				}

				if("schoolleader".equals(roleCode)){
					allowAllClassTag=true;
				}

				if("fileadmin".equals(roleCode)){
					allowAllClassTag=true;
				}

				if ("gradeleader".equals(roleCode)) {
					allowAllClassTag = true;
					gradeleader = true;
				}

				String phoneNumA=CCApplication.getInstance().getPhoneNum();
				if("13687395021".equals(phoneNumA)){
					allowAllClassTag=true;
				}

			}

			if(allowAllClassTag){
				if (cList == null) {
					cList = new ArrayList<NewClasses>();
				}
				List<NewClasses> schoolAllClassList=CCApplication.getInstance().getSchoolAllClassList();

				if(schoolAllClassList!=null&& !gradeleader){
					cList.clear();
					cList.addAll(schoolAllClassList);
				}else if (schoolAllClassList != null && gradeleader){//如果是年级组长
					try {
						if (cList == null) {
							cList = new ArrayList<NewClasses>();
						}
						List<GradeleaderBean> gradeInfoList = CCApplication.getInstance().getMemberDetail().getGradeInfoList();
						for (int i = 0; i < gradeInfoList.size(); i++) {
							String userGradeId=gradeInfoList.get(i).getGradeId();
							for (int j = 0; j < schoolAllClassList.size(); j++){
								String gradeId=schoolAllClassList.get(j).getGradeId();
								if(userGradeId.equals(gradeId)){
									cList.add(schoolAllClassList.get(j));
								}
							}
						}
					} catch (Exception e) {
					}

				}
			}

			if (cList == null) {
				System.out.println("是否有班级=======没有");
				DataUtil.getToast("您无任教班级，不能发布班级动态！");
				NewPublishDynamicActivity.this.finish();
				return;
			} else if (!usertype_a.equals("2")) {
				DataUtil.getToast("您无任教班级，不能发布班级动态！");
				NewPublishDynamicActivity.this.finish();
			}
		} else if ("3".equals(usertype)) {
			cList = new ArrayList<NewClasses>();

			UserType presentUser = CCApplication.getInstance().getPresentUser();

			NewClasses newclass = new NewClasses();

			newclass.setClassID(presentUser.getClassId());
			newclass.setClassName(presentUser.getClassName());
			newclass.setGradeId(presentUser.getGradeId());
			newclass.setGradeName(presentUser.getGradeName());

			cList.add(newclass);

			spinnerButton.setText(presentUser.getGradeName()+presentUser.getClassName());
			class_i=0;

		}
//		options = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.default_image)
//				.showImageForEmptyUri(R.drawable.default_image)
//				.showImageOnFail(R.drawable.default_image)
//				.cacheInMemory(true)
//				.cacheOnDisc(true)
//				.imageScaleType(ImageScaleType.NONE)
//				.bitmapConfig(Bitmap.Config.RGB_565)     //设置图片的解码类型
//				.build();
//		options = DataUtil.getImageLoaderOptions();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.default_image)
				.showImageForEmptyUri(R.drawable.default_image)
				.showImageOnFail(R.drawable.default_image)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.ARGB_8888)     //设置图片的解码类型
				.build();
	}

	/**
	 * 效果控制
	 */
	private void initEvents() {
		title2_off.setOnClickListener(this);
		title2_ok.setOnClickListener(this);
		title2_ok.setClickable(false);
		title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
		video_del_iv.setOnClickListener(this);

		publish_dynamic_addiv_gv
				.setOnItemClickListener(new OnItemClickListener() {
					private PictureURL pictureURL;

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {
						publish_dynamic_addiv_gv_position=position;
						//隐藏虚拟键盘
						((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))
								.hideSoftInputFromWindow(NewPublishDynamicActivity.this.getCurrentFocus().getWindowToken(),
										InputMethodManager.HIDE_NOT_ALWAYS);

						if (position != imagePaths.size()) {

							if (0 == imagePaths.size()) {// 第一次过去选择相片
								Intent intentAdd = new Intent(
										NewPublishDynamicActivity.this,
										SelectImageActivity.class);
								intentAdd.putExtra("limit", MAX_IMAGE);
								intentAdd.putStringArrayListExtra("addPic",
										imagePaths);
								startActivityForResult(intentAdd, REQUEST_CODE);
							} else {
								datas.clear();
								for (int i = 0; i < imagePaths.size(); i++) {
									pictureURL = new PictureURL();
									pictureURL.setPictureURL("file://"
											+ imagePaths.get(i));
									datas.add(pictureURL);
								}
							}
							if (0 != imagePaths.size()
									&& position == imagePaths.size()) {// 非第一次选取相片
								int limitImage = MAX_IMAGE;
								if (limitImage <= MAX_IMAGE) {
									Intent intentAdd = new Intent(
											NewPublishDynamicActivity.this,
											SelectImageActivity.class);
									intentAdd.putExtra("limit", limitImage);
									intentAdd.putStringArrayListExtra("addPic",
											imagePaths);
									intentAdd.putExtra("is_compress", is_compress);
									startActivityForResult(intentAdd, REQUEST_CODE);
								} else {
									DataUtil.getToast("最多不能超过" + MAX_IMAGE + "张图片");
								}
							}
							if (position != imagePaths.size()) {
								// 查看照片
								Intent intent = new Intent(
										NewPublishDynamicActivity.this,
										ImagePagerActivity.class);
								intent.putExtra(
										ImagePagerActivity.EXTRA_IMAGE_URLS,
										(Serializable) datas);
								intent.putExtra(
										ImagePagerActivity.EXTRA_IMAGE_INDEX,
										position);
								startActivity(intent);
							}

						}else{
							SaveClassPop saveClassPop = new SaveClassPop(NewPublishDynamicActivity.this,NewPublishDynamicActivity.this);
							saveClassPop.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
						}


					}
				});

		// 选择班级
		spinnerButton.showAble(true);
		spinnerButton.setResIdAndViewCreatedListener(R.layout.spinner_layout,
				new SpinnerButton.ViewCreatedListener() {
					@Override
					public void onViewCreated(View v) {
						spinnerListView = (ListView) v
								.findViewById(R.id.spinner_lv);
					}
				});
		// spinnerListView设置数据
		final MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter();
		spinnerListView.setAdapter(spinnerAdapter);
		spinnerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				spinnerButton.dismiss();
				if (!DataUtil.isNetworkAvailable(getBaseContext())) {
					DataUtil.getToast(getResources().getString(
							R.string.no_network));
					return;
				}
				spinnerButton.setText(cList_aa.get(arg2).getGradeName()
						+ cList_aa.get(arg2).getClassName());
				class_i = arg2;
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title2_off:
			// preFinish();
			SendResult(REASON_BACK);
			NewPublishDynamicActivity.this.finish();
			break;
		case R.id.title2_ok:// 发布按钮点击
			// 获取标题text
			if (task.size() >= MAX_TASKS) {
				DataUtil.getToast("每次最多发10条动态，请稍后再发");
				break;
			}
			String content = publish_dynamic_content.getText().toString();
			if (0 == content.length() && imagePaths.size() == 0&&videoPaths.size() == 0) {
				DataUtil.getToast("请输入内容或添加照片");
				break;
			}
			if (!DataUtil.isNetworkAvailable(getBaseContext())) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				break;
			}
			if (DataUtil.isFastDoubleClick()) {
				DataUtil.getToast("正在上传，请不要多次重复提交...");
				break;
			}

			if (class_i < 0) {
				DataUtil.getToast("请先选择班级");
			} else {
				if(isVideo){
					uploadFile();
					uploadVideoFile();
				}else{
					uploadFile();
				}
			}
			break;
		case R.id.video_del_iv:
			//清空视频
			transmitVideoPaths.clear();
			videoPaths.clear();
			nowVideoNumber=0;

			//清空视频缩略图
			imagePaths.clear();
			nowImageNumber=0;

			isVideo=false;

			class_vedio_ll.setVisibility(View.GONE);
			publish_dynamic_addiv_gv.setVisibility(View.VISIBLE);
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (TConfigrChoiceClassActivity.BACK_RESULT == resultCode) {
		}
		if(requestCode==100){
			if (RESULT_OK == resultCode) {
				//清空视频
				transmitVideoPaths.clear();
				videoPaths.clear();
				nowVideoNumber=0;
				isVideo=false;

				ArrayList<String> images = data.getExtras().getStringArrayList(
						"pic_paths");// 每次选择相片返回的数据
				if (null != images) {
					publish_dynamic_addiv_gv.setVisibility(View.VISIBLE);
					class_vedio_iv.setVisibility(View.GONE);
					class_vedio_ll.setVisibility(View.GONE);
					String title = "file://";
					transmitImagePaths.clear();
					imagePaths.clear();
					for (String str : images) {
						transmitImagePaths.add(title + str);
					}
					imagePaths.addAll(images);
					String spinnerButton_str=spinnerButton.getText().toString().trim();
					String publish_dynamic_content_str=publish_dynamic_content.getText().toString();
					Boolean alreadyChooseClass =false;
					if(!"选择发送到的班级".equals(spinnerButton_str)){
						alreadyChooseClass=true;
					}
					if(alreadyChooseClass){
						title2_ok.setClickable(true);
						title2_ok.setTextColor(getResources().getColor(
								R.color.font_darkblue));
					}
				}else{
					publish_dynamic_addiv_gv.setVisibility(View.VISIBLE);
					class_vedio_iv.setVisibility(View.VISIBLE);
					class_vedio_ll.setVisibility(View.VISIBLE);
				}
				if (null != imagePaths) {
					nowImageNumber = imagePaths.size();
				}
				gvAdapter.setData(transmitImagePaths);
				gvAdapter.notifyDataSetChanged();
				is_compress = data.getBooleanExtra("is_compress", true);
			}
		}else if(requestCode==200){
			if (RESULT_OK == resultCode) {
				//系统相机程序
//				Uri uri= data.getData();
//				System.out.println("==33=mp4="+uri);
//				System.out.println("==33=mp4="+uri.getPath());
				String whichActivityReturn=data.getStringExtra("whichActivityReturn");
				if("videoActivity".equals(whichActivityReturn)){//如果是从拍摄视频页返回
					bitmapList.clear();
				}else if("selectVideoActivity".equals(whichActivityReturn)){//如果是从选择本地视频页返回
					bitmapList=SelectVideoActivity.bitmapList;
				}
				//自定义相机程序
				String filePath=data.getStringExtra("filePath");
				class_vedio_iv.setVisibility(View.VISIBLE);
				class_vedio_ll.setVisibility(View.VISIBLE);
				isVideo=true;

				publish_dynamic_addiv_gv.setVisibility(View.GONE);

				//清空照片
				transmitImagePaths.clear();
				imagePaths.clear();
				nowImageNumber=0;
				gvAdapter.setData(transmitImagePaths);
				gvAdapter.notifyDataSetChanged();

				transmitVideoPaths.clear();
				videoPaths.clear();
				nowVideoNumber =1;
//				gvAdapter.setData(transmitImagePaths);
//				gvAdapter.notifyDataSetChanged();
				final Uri uri = Uri.fromFile(new File(filePath));
//				imageLoader.displayImage(uri + "", class_vedio_iv, options);
//				Bitmap bitmap=getVideoThumbnail(data.getData().getPath());
				Bitmap bitmap=getVideoThumbnail(filePath,720,720,MediaStore.Images.Thumbnails.MINI_KIND);
				class_vedio_iv.setImageBitmap(bitmap);
				//保存视频缩略图
				String videoThumbmailPath="";
				if(bitmap!=null){
					videoThumbmailPath= VideoFileUtil.savaVideoThumbmail(filePath,bitmap);
					System.out.println("==videoThumbmailPath=="+videoThumbmailPath);
				}
				class_vedio_iv.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						PictureURL pictureURL = null;
						List<PictureURL> datas = new ArrayList<PictureURL>();
						pictureURL = new PictureURL();
						pictureURL.setPictureURL(uri+"");
						datas.add(pictureURL);

						Intent intent = new Intent(NewPublishDynamicActivity.this,
								VideoViewActivity.class);
						intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_URLS,
								(Serializable) datas);
						intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_INDEX,
								0);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				});

				videoPaths.add(filePath);

				imagePaths.add(videoThumbmailPath);
				nowImageNumber=1;


				String spinnerButton_str=spinnerButton.getText().toString().trim();
				String publish_dynamic_content_str=publish_dynamic_content.getText().toString();
				Boolean alreadyChooseClass =false;
				if(!"选择发送到的班级".equals(spinnerButton_str)){
					alreadyChooseClass=true;
				}
				if(alreadyChooseClass){
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_darkblue));
				}
			}
		}

	}

	public Bitmap getVideoThumbnail(String filePath) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime();
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				retriever.release();
			}
			catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	//kind 为MINI_KIND和MICRO_KIND。其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	//MediaStore.Images.Thumbnails.MICRO_KIND
	public Bitmap getVideoThumbnail(String videoPath,int width,int height,int kind) {
		Bitmap bitmap =null;
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	private Bitmap getVideoThumbnail(String url, int width, int height) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		int kind = MediaStore.Video.Thumbnails.MINI_KIND;
		try {
			if (Build.VERSION.SDK_INT >= 14) {
				retriever.setDataSource(url, new HashMap<String, String>());
			} else {
				retriever.setDataSource(url);
			}
			bitmap = retriever.getFrameAtTime();
		} catch (IllegalArgumentException ex) {
		} catch (RuntimeException ex) {
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
			}
		}

		kind = MediaStore.Images.Thumbnails.MICRO_KIND;
		if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}else{
		}

		return bitmap;
	}

	/*
	 * 输入参数校验
	 */
	private boolean Validate() {
		return true;
	}

	/*
	 * 业务入口 发布通知 userID String 用户ID msgType String 消息类型（学校通知1 年级通知2 班级通知 3）
	 * msgTitle String 通知标题 msgText String 通知内容 picNum String 作业图片数 classList
	 * List 班级list classID String 班级ID
	 */

	@Override
	protected void getMessage(String data) {
		switch (action) {
		case ACTION_ADD:
			parseAddData(data);
			break;
		case ACTION_FINISH:
			parseFinishtData(data);
			break;
		case ACTION_PUBIESH_CLASS_DYNAMIC:
			System.out.println("==发布班级动态出参==="+data);
			// 请求结果
			NewBaseBean ret1 = JsonUtils.fromJson(data, NewBaseBean.class);
			if (ret1.getServerResult().getResultCode() != 200) {
				DataUtil.getToast(ret1.getServerResult().getResultMessage());
			} else {
				DataUtil.getToast("班级动态发布成功");
				Intent broadcast = new Intent(
						HomeActivity.INTENT_NEW_MESSAGE);
				broadcast.putExtra("publish","publish");
				sendBroadcast(broadcast);
				NewPublishDynamicActivity.this.finish();
			}
			break;
		default:
			parseAddData(data);
			break;
		}
	}

	private void parseAddData(String data) {
		try {
			JSONObject json = new JSONObject(data);
			Log.i(TAG, "data:" + data);
			int ret = (Integer) json.get("resultCode");
			if (ret != 200) {
				DataUtil.getToast("发布失败");
			} else {
				String msgID = (String) json.get("shareID");
				mMsgID = msgID;
				upload_nums = 0;
				System.out.println("msgID:" + msgID);
				if (imagePaths.size() > 0) {

					Task newTask = new Task();
					newTask.msgID = msgID;
					newTask.uploadNum = 0;
					newTask.picNum = imagePaths.size();
					newTask.taskID++;
					task.add(newTask);
					String url = "";
					beginUpload(is_compress, imagePaths);
				} else {
					finishUpload(false, 0);
				}
				NewPublishDynamicActivity.this.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseFinishtData(String data) {
		DataUtil.clearDialog();
		ret = JsonUtils.fromJson(data, PutNoticeBean.class);
		if (ret.getResultCode() != 200) {
			DataUtil.getToast(ret.getErrorInfo());
		} else {
			DataUtil.getToast("班级动态发布成功");
			NewPublishDynamicActivity.this.finish();
		}
	}

	@Override
	public void onBackPressed() {
		SendResult(REASON_BACK);
		NewPublishDynamicActivity.this.finish();
	}

	/*
	 * 业务入口
	 * 
	 * 发布完成
	 * 
	 * ID String 用户ID Type String 反馈内容
	 * 
	 * ? 出参 参数名 参数类型 描述 code Integer 返回代码 （200 成功 201 失败）
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(cList0930!=null){
			if(CCApplication.app.getMemberDetail()!=null){
				CCApplication.app.getMemberDetail().setClassList(cList0930);
			}
		}
	}

	private void SendResult(int reason) {
		Intent intent = new Intent(INTENT_CANCEL_UPLOAD_PICTURE);
		intent.putExtra("reason", reason);
		intent.putExtra("msgID", mMsgID);
		intent.putExtra("is_compress", is_compress);
		Context mContext = getApplicationContext();
		mContext.sendBroadcast(intent);
	}

	private void beginUpload(boolean is_compress, ArrayList<String> imagePaths) {
		Intent intent = new Intent(INTENT_BEGIN_UPLOAD_PICTURE);
		intent.putExtra("is_compress", is_compress);
		intent.putExtra("imagePaths", imagePaths);
		Context mContext = getApplicationContext();
		mContext.sendBroadcast(intent);
	}

	private void finishUpload(boolean is_compress, int size) {
		Intent intent = new Intent(INTENT_FINISH_UPLOAD_PICTURE);
		intent.putExtra("is_compress", is_compress);
		intent.putExtra("sizes", size);
		Context mContext = getApplicationContext();
		mContext.sendBroadcast(intent);
	}

	private boolean isClassIdExist(List<NewClasses> classlist, String classId) {
		for (int i = 0; i < classlist.size(); i++) {
			if (classlist.get(i).getClassID().equals(classId)) {
				return true;
			}
		}

		return false;
	}

	class MySpinnerAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MySpinnerAdapter() {
			super();
			inflater = LayoutInflater.from(NewPublishDynamicActivity.this);
		}

		@Override
		public int getCount() {
			if (cList != null) {
				cList_aa = new ArrayList<NewClasses>();
				for (NewClasses classes : cList) {
					if (!isClassIdExist(cList_aa, classes.getClassID()))// 是否已存在list中
						cList_aa.add(classes);
				}

				return cList_aa.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return cList_aa.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.new_class_list_name,
					parent, false);
			TextView bzr_class_name = (TextView) convertView
					.findViewById(R.id.class_name);
			bzr_class_name.setText(cList_aa.get(position).getGradeName()
					+ cList_aa.get(position).getClassName());

			return convertView;
		}
	}

	protected void uploadFile() {
		System.out.println("===附件是图片==");
		ImageUploadAsyncTask upload = new ImageUploadAsyncTask(this, "1",
				imagePaths, Constants.UPLOAD_URL, this);
		upload.execute();
	}

	protected void uploadVideoFile() {
		System.out.println("===附件是视频==");
		VideoFileUploadAsyncTask upload=new VideoFileUploadAsyncTask(this, "2",
				videoPaths, Constants.UPLOAD_URL, this);
		upload.execute();
	}

	private List<String> vedioPicIdList=new ArrayList<String>();
	private int howTimes=0;

	@Override
	public void onImageUploadCancelled() {
		howTimes=0;
		SendResult(REASON_CANCEL);
	}

	@Override
	public void onImageUploadComplete(String result) {
		System.out.println("==上传附件出参=result="+result);
		howTimes+=1;
		UploadFileResult ret = JsonUtils.fromJson(result,
				UploadFileResult.class);
		String content = publish_dynamic_content.getText().toString();
		List<String> sf = new ArrayList<String>();
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			Set<String> set = ret.getSuccFiles().keySet();
			for (String string : set) {
				if(isVideo){
					vedioPicIdList.add(string);
				}
				sf.add(string);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		for (String string : sf) {
			System.out.println("=a=string=="+string);
		}
		try {
			NewPublishBean npb = new NewPublishBean();
			npb.setClassId(cList_aa.get(class_i).getClassID());
			npb.setGradeId(cList_aa.get(class_i).getGradeId());
			// npb.setUserId(CCApplication.getInstance().getMemberInfo()
			// .getAccId());// 账号id
			npb.setUserId((CCApplication.getInstance().getPresentUser()
					.getUserId()));// 当前用户id
			npb.setContent(content);
			npb.setPicNum(sf.size() + "");
			npb.setAttachmentIdList(sf);
			// cList.get(class_i).getCouseName()
			npb.setUserType(usertype);
			if(isVideo){
				npb.setVedioPic("fileId="+vedioPicIdList.get(0));
			}
			if ("3".equals(usertype)) {
				String childId = CCApplication.getInstance().getPresentUser()
						.getChildId();
				String relation=CCApplication.getInstance().getPresentUser().getRelationTypeName();
				npb.setChildId(childId);
				npb.setRelation(relation);
			}
			Gson gson = new Gson();
			String string = gson.toJson(npb);
			json = new JSONObject(string);
			if(isVideo){
				if(howTimes==2){
					System.out.println("=====发布班级动态json数据=====" + json);
					action=ACTION_PUBIESH_CLASS_DYNAMIC;
					queryPost(Constants.PUBLISH_CLASS_DYNAMIC,json);
				}
			}else {
				System.out.println("=====发布班级动态json数据=====" + json);
				action=ACTION_PUBIESH_CLASS_DYNAMIC;
				queryPost(Constants.PUBLISH_CLASS_DYNAMIC,json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void selectVideo() {
//		调用系统相机 程序
//		System.out.println("==2525=");
//		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//		String subForder = Environment.getExternalStorageDirectory()+"/ZIGUI_Photos/";
//		//先创建一个文件夹
//		File dir = new File(subForder);
//		if(!dir.exists()) {
//			System.out.println("==dir.mkdirs创建文件夹=");
//			dir.mkdirs();
//		}
//		String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
//		File file = new File(subForder, time+".mp4");
//		System.out.println("==file.getName()=mp4="+file.getName());
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//		//0 最小质量   1最大质量  不设置则为默认中间0.5质量
//		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//		//限制时长
//		intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
//		//限制大小
////			intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 20*1024*1024L);
//		startActivityForResult(intent, VEDIO_CODE);

		//自定义相机程序
		try {
			Camera camera = Camera.open();
			camera.release();
			camera = null;
			Intent intent = new Intent(this, VideoActivity.class);
			startActivityForResult(intent, VEDIO_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			DataUtil.getToast("没有获取摄像头权限!");
		}
	}

	@Override
	public void selectLocalVideo(){
		Intent intent = new Intent(NewPublishDynamicActivity.this,
				SelectVideoActivity.class);
		startActivityForResult(intent, VEDIO_CODE);
	}

	@Override
	public void selectPic() {
		int position=publish_dynamic_addiv_gv_position;
		PictureURL pictureURL=new PictureURL();
		if (0 == imagePaths.size()) {// 第一次过去选择相片
			Intent intentAdd = new Intent(
					NewPublishDynamicActivity.this,
					SelectImageActivity.class);
			intentAdd.putExtra("limit", MAX_IMAGE);
			intentAdd.putStringArrayListExtra("addPic",
					imagePaths);
			startActivityForResult(intentAdd, REQUEST_CODE);
		} else {
			datas.clear();
			for (int i = 0; i < imagePaths.size(); i++) {
				pictureURL = new PictureURL();
				pictureURL.setPictureURL("file://"
						+ imagePaths.get(i));
				datas.add(pictureURL);
			}
		}
		if (0 != imagePaths.size()
				&& position == imagePaths.size()) {// 非第一次选取相片
			int limitImage = MAX_IMAGE;
			if (limitImage <= MAX_IMAGE) {
				Intent intentAdd = new Intent(
						NewPublishDynamicActivity.this,
						SelectImageActivity.class);
				intentAdd.putExtra("limit", limitImage);
				intentAdd.putStringArrayListExtra("addPic",
						imagePaths);
				intentAdd.putExtra("is_compress", is_compress);
				startActivityForResult(intentAdd, REQUEST_CODE);
			} else {
				DataUtil.getToast("最多不能超过" + MAX_IMAGE + "张图片");
			}
		}
	}



	private void inputState() {
		publish_dynamic_content.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String spinnerButton_str=spinnerButton.getText().toString().trim();
				String publish_dynamic_content_str=publish_dynamic_content.getText().toString();

				Boolean alreadyChooseClass =false;
				if(!"选择发送到的班级".equals(spinnerButton_str)){
					alreadyChooseClass=true;
				}

				if (alreadyChooseClass&&(s.length() > 0 || imagePaths.size() > 0)) {
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_darkblue));
				} else {
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_lightgray));
				}

				if (s.length() > 1999) {
					DataUtil.getToast("动态内容不能超过2000个字");
					publish_dynamic_content.requestFocus();
				}

			}

		});




		spinnerButton.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String spinnerButton_str=spinnerButton.getText().toString().trim();
				String publish_dynamic_content_str=publish_dynamic_content.getText().toString();

				Boolean alreadyChooseClass =false;
				if(!"选择发送到的班级".equals(spinnerButton_str)){
					alreadyChooseClass=true;
				}

				if(alreadyChooseClass&&(publish_dynamic_content_str.length()>0||imagePaths.size() > 0||videoPaths.size() > 0)){
					title2_ok.setClickable(true);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_darkblue));
				}else {
					title2_ok.setClickable(false);
					title2_ok.setTextColor(getResources().getColor(
							R.color.font_lightgray));
				}

			}});

	}
}
