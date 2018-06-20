/*
 * 文 件 名:SelectImageActivity.java
 * 创 建 人： 姜韵雯
 * 日    期： 2014-10-6
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.imageselect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.core.TaskBaseActivity;
import com.wcyc.zigui2.imageselect.SelectImageActivity.GridAdapter.GridHolder;
import com.wcyc.zigui2.utils.BitmapTool;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

//2014-10-6
/**
 * 图片选择器的主类
 *
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public class SelectImageActivity extends TaskBaseActivity {

	private GridView gridview;
	private GridAdapter gridAdatper;
	TextView group_text;
	ListView listview;
	private ProgressDialog mProgressDialog;
//	private ProgressDialog mDirDialog;
	private ImageLoader mImageLoader;
	// 文件夹作为键 文件夹下面的图片绝对路径作为值 包含系统地那话所有的图片
	private HashMap<String, ArrayList<String>> mGruopMap = new HashMap<String, ArrayList<String>>();
	// 存放图片对象 放是集合
	private ArrayList<ImageBean> imgBeanLists = new ArrayList<ImageBean>();
	// 所有的图片
	//jiang HashMap 是无序的，必须修改成有序的
	HashMap<String, String> mAllImgs = new HashMap<String, String>();
	/**
	 * 所有原图列表，根据时间排序的
	 */
	private ArrayList<String> mAllList = new ArrayList<String>();
	// 缩略图列表。key代表原图路径，value代表缩略图路径
	HashMap<String, String> thumbnailList = new HashMap<String, String>();
	private final static int SCAN_OK = 1;
	private final static int SCAN_FOLDER_OK = 2;
	private final static int UPDATA_IMAGEVIEW = 3;
	private RelativeLayout list_layout;
	private DisplayImageOptions options;
	private Button button_back;
	private ListAdapter listAdapter;
	private int limit_count;
	private ArrayList<Integer> chooseItem = new ArrayList<Integer>();// 根据路径选择的相册
	private ArrayList<String> addedPath = null;// 被选中相片路径存放集合
	private long sizeAllLong=0;//图片总大小
	private static String tempCameraPath = "";
	//拍照的保存路径
	private static final String savePhotoPath
			= Environment.getExternalStorageDirectory().getAbsolutePath()  + "/ZIGUI_Photos/";
	ArrayList<String> nowStrs = new ArrayList<String>();// 已经选中的图片路径集合
	Animation toUp, toDown;
	private ImageButton titleButton;
	private TextView button_send;
	private CheckBox choose_original;
	ContentResolver cr;
	private boolean is_choose_original = false;//选择原图发送
	private boolean is_show_original_checkbox = false;
	/**
	 * 是否在处理旋转的图片
	 */
	private boolean isRotaing = false ;
    private TextView tv_choose_pic;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		limit_count = getIntent().getIntExtra("limit" , 8);
		attachmentLimit = getIntent().getStringExtra("attachmentLimit");

		setContentView(R.layout.image_select);
		cr = getContentResolver();
		//建立保存相片的文件夹
        File cacheDir = new File(savePhotoPath);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
		initView();
		initData();
		setListener();
	}

	private void initView() {
		button_back = (Button) findViewById(R.id.title_btn);
		gridview = (GridView) findViewById(R.id.gridview);
		group_text = (TextView) findViewById(R.id.group_text);
		listview = (ListView) findViewById(R.id.group_listview);
		list_layout = (RelativeLayout) findViewById(R.id.list_layout);
		button_send = (TextView) findViewById(R.id.title_imgbtn_accomplish);  // 完成按钮
		button_send.setVisibility(View.VISIBLE);
		choose_original = (CheckBox)findViewById(R.id.choose_original);
        tv_choose_pic = (TextView) findViewById(R.id.tv_choose_pic_num);
    }

	private void initData() {
		button_back.setText(R.string.selector_img_title);

		button_send.setTextColor(getResources().getColor(R.color.font_lightgray));//灰色禁用 不可点击   蓝色测试

		// 初始化数据
		chooseItem.add(0);
		gridAdatper = new GridAdapter();
		gridAdatper.setData(new ArrayList<String>());
		gridview.setAdapter(gridAdatper);
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				getApplicationContext()).memoryCacheExtraOptions(480, 800)
//				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 1)
//				.denyCacheImageMultipleSizesInMemory()
//				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
//				.discCacheSize(50 * 1024 * 1024)
//				.denyCacheImageMultipleSizesInMemory()
//				.discCacheFileNameGenerator(new Md5FileNameGenerator())
//				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs() // Remove for release app
//				.build();
//		ImageLoader.getInstance().init(config);
		// 3、使用ImageLoader进行图片加载的时候，先要实例化ImageLoader，调用以下方法进行实例化，在每个布局里面都要实例化后再使用
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.showImageForEmptyUri(R.drawable.friends_sends_pictures_no)
				.showImageOnFail(R.drawable.friends_sends_pictures_no)
				.showStubImage(R.drawable.friends_sends_pictures_no)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		//jiang 在这里得到传回来的String数组，如果不是空，那么将已经选过的图片加入到addedPath中
		addedPath = getIntent().getStringArrayListExtra("addPic");
		if(addedPath == null ){
			addedPath = new ArrayList<String>();
		}
        is_show_original_checkbox = getIntent().getBooleanExtra("is_show_checkbox",true);
        System.out.println("is_show_original_checkbox:"+is_show_original_checkbox);
        if(is_show_original_checkbox){
    		boolean isCompress = getIntent().getBooleanExtra("is_compress", true);
    		choose_original.setChecked(!isCompress);
        }else{
            choose_original.setVisibility(View.GONE);
        }

        //第二次进来,判断已经选择的图片张数
        showChoosePicNum();

        // limit_count = getIntent().getIntExtra("limit_count", 8);
		// total_text.setText("0/" + limit_count + "张");
		getImages();
	}

    private void setListener() {
		button_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mGruopMap.clear();
				imgBeanLists.clear();
				nowStrs.clear();
				SelectImageActivity.this.finish();
			}
		});
		// 将选取的照片传到上一个页面
		button_send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(0 == addedPath.size()) {
					return;
				}
				/***********旋转选择的图片*************/
				//DataUtil.showDialog(SelectImageActivity.this, "正在处理图片，请稍等");

				new Thread(new Runnable(){
					public void run(){
						int size = addedPath.size();
						for(int i = 0; i < size; i++){
							String imagePath = addedPath.get(i).toString();
							int degree = readPictureDegree(imagePath);
							//System.out.println("degree:"+degree);
							Bitmap bitmap = null;
							FileOutputStream out = null;
							try {
								if (degree != 0) {
									bitmap = BitmapTool.getBitmap(SelectImageActivity.this,imagePath);
									bitmap = rotaingImageView(degree, bitmap);
									//String tempFilePath = Environment.getExternalStorageDirectory() + "/temp.jpg";
									out = new FileOutputStream(imagePath);
									bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
									//addedPath.add(0,tempFilePath);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}finally{
								if (out != null) {
									try {
										out.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
						/******************************************/
						returnPic();
					}
				}).start();
			}
		});
		// 所有图片按钮被点击
		group_text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (list_layout.getVisibility() == View.VISIBLE) {
					list_layout.setVisibility(View.GONE);
				} else if (list_layout.getVisibility() == View.GONE) {
					list_layout.setVisibility(View.VISIBLE);
				}
			}
		});

		// 文件夹相册列表
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 点击刷新对应的视图
				if (chooseItem.get(0) == position) {
					// 不做操作，返回
					list_layout.setVisibility(View.GONE);
					// list_layout.startAnimation(toDown);
				} else {
					chooseItem.clear();
					chooseItem.add(position);
					listAdapter.notifyDataSetChanged();
					list_layout.setVisibility(View.GONE);
					// list_layout.startAnimation(toDown);
					// gridview.requestFocus();
					// 获取到mAllImgs；并显示到数据中
					gridAdatper.setData(new ArrayList<String>());
					// 得到当前的来刷新
					if (0 == position) {
//						getImages();
						nowStrs.clear();
						nowStrs.addAll(mAllList);
						mHandler.sendEmptyMessage(SCAN_FOLDER_OK);
					} else {
						// 刷新当前的GridView
//						mDirDialog = ProgressDialog.show(
//								SelectImageActivity.this, null, "正在加载...");
						nowStrs.clear();
						String fa_path = imgBeanLists.get(position)
								.getFa_filepath();
						nowStrs.addAll(mGruopMap.get(fa_path));
						mHandler.sendEmptyMessage(SCAN_FOLDER_OK);
					}
					group_text.setText(imgBeanLists.get(position).getFolderName());

				}
			}
		});

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (chooseItem.get(0) == 0 && 0 == position && !isRotaing) {
					// 调用系统相机
					// 判断是否已满8张图片
					if (addedPath.size() >= limit_count) {
						DataUtil.getToast("最多选" + limit_count + "张，请取消后再点击拍照");
						return;
					}
					SimpleDateFormat fomater = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
					String time = fomater.format(new Date());
					tempCameraPath = savePhotoPath + time + ".jpg";

					PickPhotoUtil.getInstance().takePhoto(
							SelectImageActivity.this, "tempUser",
							tempCameraPath);
				}
			}
		});

		choose_original.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				is_choose_original = isChecked;
				System.out.println("isChecked:"+isChecked);
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(Constants.TAKE_PHOTO,"resultCode:"+resultCode);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PickPhotoUtil.PickPhotoCode.PICKPHOTO_TAKE:
				String path;
				if(data != null) {
					Uri uri = data.getData();
					if (uri != null) {
						path = uri.getPath();
						System.out.println("path:" + path);
					}
				}
				Log.i(Constants.TAKE_PHOTO,"orgin tempCameraPath:" + tempCameraPath);
				tempCameraPath = CCApplication.getInstance().getPickPhotoFilename();
				Log.i(Constants.TAKE_PHOTO,"tempCameraPath:" + tempCameraPath);
				if(tempCameraPath == null) {
					displayMemory();
					return;
				}
				final File fi = new File(tempCameraPath);
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fi)));

				/**
				 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
				 */
				final int degree = readPictureDegree(tempCameraPath);
				Log.i(Constants.TAKE_PHOTO,"degree:"+degree+" tempCameraPath:"+tempCameraPath);
				//如果图片是旋转了的，那么旋转回来重新保存
				if(degree != 0){
					isRotaing = true;
					DataUtil.showDialog(SelectImageActivity.this, "正在处理图片，请稍等");
					new Thread(new Runnable() {
						@Override
						public void run() {
							Bitmap cameraBitmap = null;
							try{
								cameraBitmap = BitmapTool.getBitmap(SelectImageActivity.this,tempCameraPath);
								/**
								 * 把图片旋转为正的方向
								 */
								cameraBitmap = rotaingImageView(degree, cameraBitmap);
							}catch(Exception e){
								e.printStackTrace();
							}
							if(cameraBitmap != null){
								//旋转后的图片重新保存到sd卡中
								FileOutputStream out;
								try {
									out = new FileOutputStream(fi);
									cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
								    out.flush();
								    out.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
								cameraBitmap = null;
							}
							runOnUiThread(new Runnable() {
								public void run() {
									DataUtil.clearDialog();
									addedPath.add(tempCameraPath);
									returnPic();
									isRotaing = false;
								}
							});
						}
					}).start();
				}else{
					addedPath.add(tempCameraPath);
					Log.i(Constants.TAKE_PHOTO,"cameraPath:"+tempCameraPath);
					returnPic();
				}
				PickPhotoUtil.getInstance().clearPicturePath();
				break;

			default:
				break;
			}
		}
	}
	/**
	 * 返回调用它的界面
	 */
	private void returnPic(){
		Intent dataIntent = new Intent();
		Bundle dataBundle = new Bundle();
		dataBundle.putStringArrayList("pic_paths", addedPath);
		dataIntent.putExtras(dataBundle);
		dataIntent.putExtra("is_compress",!is_choose_original);
		setResult(RESULT_OK, dataIntent);
		SelectImageActivity.this.finish();
	}
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
   public int readPictureDegree(String path) {
       int degree  = 0;
       try {
               ExifInterface exifInterface = new ExifInterface(path);
               int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
               switch (orientation) {
               case ExifInterface.ORIENTATION_ROTATE_90:
                       degree = 90;
                       break;
               case ExifInterface.ORIENTATION_ROTATE_180:
                       degree = 180;
                       break;
               case ExifInterface.ORIENTATION_ROTATE_270:
                       degree = 270;
                       break;
               }
       } catch (IOException e) {
               e.printStackTrace();
       }
       return degree;
   }
	/**
    * 旋转图片
    * @param angle  旋转的角度
    * @param bitmap 原bitmap
    * @return Bitmap 旋转回来的bitmap
    */
   public Bitmap rotaingImageView(int angle , Bitmap bitmap) {
       //旋转图片 动作
       Matrix matrix = new Matrix();
       matrix.postRotate(angle);
       // 创建新的图片
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
               bitmap.getWidth(), bitmap.getHeight(), matrix, true);
       return resizedBitmap;
   }


	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN_OK:
				// 关闭进度条
				mProgressDialog.dismiss();
				// 获取到mAllImgs；并显示到数据中
				gridAdatper.setData(mAllList);
				// 扫描完成后，给listview赋值------给所有图片赋值
				imgBeanLists = subGroupOfImage(mGruopMap);
				listAdapter = new ListAdapter();
				listAdapter.setData(imgBeanLists);
				listview.setAdapter(listAdapter);
				break;
			case SCAN_FOLDER_OK:// 扫描单个文件夹成功
//				mDirDialog.dismiss();
				// 获取到mAllImgs；并显示到数据中
				gridAdatper.setData(nowStrs);
				break;
			case UPDATA_IMAGEVIEW:
				((GridAdapter) gridview.getAdapter()).notifyDataSetChanged();
				break;
			}
		}

	};
	private static final String[] STORE_IMAGES = {
			MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE,
			MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media._ID,
			MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATE_TAKEN };

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 *
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "请检查sd卡或者是否开启存储权限", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		try {
			mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		}catch (Exception e){

		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				mAllImgs.clear();
				mGruopMap.clear();
				mAllList.clear();
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				// 只查询jpeg和png的图片
				Cursor mCursor = cr.query(mImageUri,
						STORE_IMAGES, null,null,
						MediaStore.Images.Media.DATE_MODIFIED + " desc");
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					//System.out.println("path:" + path);
					//如果图片不存在跳过
					if(!DataUtil.isFileExists(path)) {
						System.err.println("path not exist:" + path);
						sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
						continue;
					}

					int id = mCursor.getInt(mCursor
							.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
					// 获取该图片的父路径名
					File pa_file = new File(path).getParentFile();
					String parentName = pa_file.getAbsolutePath();
					// if (mAllImgs.size() < 1000) {// 281
					mAllImgs.put(""+id, path);
					mAllList.add(path);
					// }
					// 根据父路径名将图片放入到mGruopMap中
					if (!mGruopMap.containsKey(parentName)) {
						ArrayList<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGruopMap.put(parentName, chileList);
					} else {
						mGruopMap.get(parentName).add(path);
					}
				}
				mCursor.close();
				getThumbnail();

			}
		}).start();

	}

	/**
	 * 得到缩略图
	 */
	private void getThumbnail() {
		new Thread(new Runnable() {
			@Override
			public void run() {
//				DisplayMetrics outMetrics = new DisplayMetrics();
//				(SelectImageActivity.this).getWindowManager()
//						.getDefaultDisplay().getMetrics(outMetrics);
//				float density = outMetrics.density; // 像素密度
//				int itemWidth = (int) (100 * density);
//
//				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//				ContentResolver mContentResolver = SelectImageActivity.this
//						.getContentResolver();
				String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
						Thumbnails.DATA };
				Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
						null, null, null);
				getThumbnailColumnData(cursor);

				Iterator<Entry<String, String>> it = mAllImgs.entrySet()
						.iterator();
				while (it.hasNext()) {
					Entry<String, String> entry = it.next();
					String key = entry.getKey();
					String value = entry.getValue();
					String url = thumbnailList.get(key);
					if(url != null){
						thumbnailList.remove(key);
						thumbnailList.put(value, url);
					}else{
//						String value2 = getThumbnailForId(key);
						thumbnailList.put(value, value);
					}
				}
				cursor.close();
				mAllImgs.clear();
//				nowStrs = getAllImages();
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);
			}
		}).start();
	}

	/**
	 * 从数据库中得到缩略图
	 *
	 * @param cur
	 */
	private void getThumbnailColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int _id;
			int image_id;
			String image_path;
			int _idColumn = cur.getColumnIndex(Thumbnails._ID);
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);

				// Do something with the values.
				// Log.i(TAG, _id + " image_id:" + image_id + " path:"
				// + image_path + "---");
				// HashMap<String, String> hash = new HashMap<String, String>();
				// hash.put("image_id", image_id + "");
				// hash.put("path", image_path);
				// thumbnailList.add(hash);
				//判断缩略图文件是否存在
				if(DataUtil.isFileExists(image_path)){
					thumbnailList.put("" + image_id, image_path);
				}
			} while (cur.moveToNext());
		}
	}
//	private String getThumbnailForId(String id){
//		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
//				Thumbnails.DATA };
//		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
//				Thumbnails.IMAGE_ID + "=" + id, null, null);
//		String path = null;
//		if (cursor != null) {
//			if (cursor.moveToFirst()) {
//				path = cursor.getString(cursor.getColumnIndex(Thumbnails.DATA));
//			}
//		}
//		cursor.close();
//		return path;
//	}

	/**
	 * 组装分组界面GridView的数据源。<P>
	 * 因为我们扫描手机的时候将图片信息放在HashMap中， 所以需要遍历HashMap将数据组装成List
	 *
	 * @param groupMap 组映射
	 * @return ImageBean列表
	 */
	private ArrayList<ImageBean> subGroupOfImage(HashMap<String, ArrayList<String>> groupMap) {
		ArrayList<ImageBean> list = new ArrayList<ImageBean>();
		if (groupMap.size() == 0) {
			return list;
		}
		Iterator<Map.Entry<String, ArrayList<String>>> it = groupMap.entrySet()
				.iterator();
		ImageBean ig0 = new ImageBean();
		ig0.setFolderName("所有图片");
		ig0.setImageCounts(mAllList.size());
		if(mAllList.size() > 0){
			ig0.setTopImagePath(mAllList.get(0));
		}
		list.add(0, ig0);

		while (it.hasNext()) {
			Map.Entry<String, ArrayList<String>> entry = it.next();
			ImageBean mImageBean = new ImageBean();
			String key = entry.getKey();
			List<String> value = entry.getValue();
			File dir_file = new File(key);
			mImageBean.setFolderName(dir_file.getName());
			mImageBean.setImageCounts(value.size());
			mImageBean.setTopImagePath(value.get(0));// 获取该组的第一张图片
			mImageBean.setFa_filepath(key);
			list.add(mImageBean);
		}

		return list;
	}

	// gridview的Adapter
	class GridAdapter extends BaseAdapter {
		// 根据三种不同的布局来应用
		final int VIEW_TYPE = 2;
		final int TYPE_1 = 0;
		final int TYPE_2 = 1;
		LayoutInflater inflater;
		private ArrayList<String> gridStrings;
		/**
		 * 用来存储图片的选中情况
		 */
		private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();

		public GridAdapter() {
			gridStrings = new ArrayList<String>();
			inflater = LayoutInflater.from(SelectImageActivity.this);
		}

		public void setData(ArrayList<String> strs) {
			if (gridStrings != null && !gridStrings.isEmpty()) {
				gridStrings.clear();
			} else {
				gridStrings = new ArrayList<String>();
			}
			if (chooseItem.get(0) == 0) {
				gridStrings.add("");
			}
			if (strs != null) {
				for (String str : strs) {
					gridStrings.add(str);
				}
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return gridStrings == null ? 0 : gridStrings.size();
		}

		@Override
		public String getItem(int position) {
			return gridStrings.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			if (chooseItem.get(0) == 0) {
				if (position == 0) {
					return TYPE_1;
				} else {
					return TYPE_2;
				}
			} else {
				return TYPE_2;
			}
		}

		@Override
		public int getViewTypeCount() {
			if (chooseItem.get(0) == 0) {
				return VIEW_TYPE;
			} else {
				return 1;
			}
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			// gridHolder = new GridHolder();
			PhotoHolder photoHodler = null;
			GridHolder gridHolder = null;
			int type = getItemViewType(position);
			if (convertView == null) {
				switch (type) {
				case TYPE_1:
					// 显示拍照
					photoHodler = new PhotoHolder();
					convertView = inflater.inflate(
							R.layout.image_select_take_photo, arg2, false);
					convertView.setTag(photoHodler);
					break;
				case TYPE_2:
					gridHolder = new GridHolder();
					convertView = inflater.inflate(
							R.layout.image_select_grid_item, arg2, false);
					gridHolder.grid_image = (ImageView) convertView
							.findViewById(R.id.grid_image);
					gridHolder.grid_img = (ImageView) convertView
							.findViewById(R.id.grid_img);
					convertView.setTag(gridHolder);
					break;
				default:
					break;
				}
			} else {
				switch (type) {
				case TYPE_1:
					// 显示拍照
					photoHodler = (PhotoHolder) convertView.getTag();
					break;
				case TYPE_2:
					gridHolder = (GridHolder) convertView.getTag();
					break;
				default:
					break;
				}
			}

			if (type == TYPE_2) {
				// 判断是否已经添加
				// mImageLoader.displayImage("file://" + getItem(position),
				// gridHolder.grid_image, options);
				setImageView(getItem(position), gridHolder.grid_image);
				String url = getItem(position);
				// 图片被选中点击
				gridHolder.grid_image.setOnClickListener(new MyOnClickListener(
						gridHolder, url));
				// 缩列图被点击选中监听
				gridHolder.grid_img
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View view) {

								if (addedPath.contains(getItem(position))) {
									// 已经包含这个path了，则干掉
									addedPath.remove(getItem(position));
									((ImageView) view)
											.setImageResource(R.drawable.photo_choose_bg);
									button_send.setTextColor(getResources().getColor(R.color.font_lightgray));

								} else {

									if(DataUtil.isNullorEmpty(attachmentLimit)){
										// 判断图片张数大小
										if (addedPath.size() < limit_count) {
											addedPath.add(getItem(position));
											((ImageView) view)
													.setImageResource(R.drawable.photo_choose_bg_s);
											// 添加图片，显示出来张数
										} else {
											if(limit_count == 1){
												addedPath.remove(0);
												addedPath.add(getItem(position));
												((ImageView) view)
														.setImageResource(R.drawable.photo_choose_bg_s);
											}else{
												DataUtil.getToast("最多选" + limit_count
														+ "张");
											}
										}
									}else{
										try {
											sizeAllLong=0;
											for (int i = 0; i < addedPath.size(); i++) {
												String file=addedPath.get(i);
												long size = DataUtil.getFileSize(new File(file));
												sizeAllLong += size;
											}

											long thisPicSize=DataUtil.getFileSize(new File(getItem(position)));
											sizeAllLong+=thisPicSize;

//											if(sizeAllLong>20*1024*1024){//20M的长度是20971520
//												DataUtil.getToast("图片总大小不能超过20M");
//											}else{
												// 判断图片张数大小
												if (addedPath.size() < limit_count) {
													addedPath.add(getItem(position));
													((ImageView) view)
															.setImageResource(R.drawable.photo_choose_bg_s);
													// 添加图片，显示出来张数
												} else {
													if(limit_count == 1){
														addedPath.remove(0);
														addedPath.add(getItem(position));
														((ImageView) view)
																.setImageResource(R.drawable.photo_choose_bg_s);
													}else{
														DataUtil.getToast("最多选" + limit_count
																+ "张");
													}
												}
//											}



										} catch (Exception e) {
											e.printStackTrace();
										}
									}

									button_send.setTextColor(getResources().getColor(R.color.font_black));
								}
								mYhandler.sendEmptyMessage(0);
							}
						});


				if (addedPath.contains(getItem(position))) {
					// 已经添加过了
					gridHolder.grid_img
							.setImageResource(R.drawable.photo_choose_bg_s);

				} else {
					gridHolder.grid_img
							.setImageResource(R.drawable.photo_choose_bg);
				}

				if(addedPath.size()>0){

					button_send.setTextColor(getResources().getColor(R.color.font_black));
				}else{
					button_send.setTextColor(getResources().getColor(R.color.font_lightgray));
				}

			}
			return convertView;
		}

		private void setImageView(String uri, ImageView iv) {
//			if (mThumbnail.containsKey(uri)) {
//				iv.setImageBitmap(mThumbnail.get(uri));
//			}
			String url = (String) iv.getTag();
			if( url ==null || !url.equals(uri)){
				mImageLoader.displayImage("file://" + thumbnailList.get(uri), iv, options);
				iv.setTag(uri);
			}
		}

		class PhotoHolder {

		}

		class GridHolder {
			ImageView grid_image;
			public ImageView grid_img;
		}
	}

	//2014年10月11日 下午12:08:51
	/**
	 * 点击条目更改选中图标
	 *
	 * @author 王登辉
	 * @version 1.01
	 */
	class MyOnClickListener implements OnClickListener {
		/**
		 * 创建一个新的实例 SelectImageActivity.MyOnClickListener.
		 *
		 */
		private GridHolder gridHolder;
		private String url;

		public MyOnClickListener(GridHolder gridHolder, String url) {
			this.gridHolder = gridHolder;
			this.url = url;
		}

		@Override
		public void onClick(View view) {
			if (addedPath.contains(url)) {
				// 已经包含这个path了，则干掉
				addedPath.remove(url);
				gridHolder.grid_img
						.setImageResource(R.drawable.photo_choose_bg);
			} else {

				if(DataUtil.isNullorEmpty(attachmentLimit)){
					// 判断图片张数大小
					if (addedPath.size() < limit_count) {
						addedPath.add(url);
						gridHolder.grid_img
								.setImageResource(R.drawable.photo_choose_bg_s);
					} else {
						if(limit_count == 1){
							addedPath.remove(0);
							addedPath.add(url);
							gridHolder.grid_img
							.setImageResource(R.drawable.photo_choose_bg_s);
						}else{
							DataUtil.getToast("最多选" + limit_count
									+ "张");
						}

					}
				}else{
					try {
						sizeAllLong=0;
						for (int i = 0; i < addedPath.size(); i++) {
							String file=addedPath.get(i);
							long size = DataUtil.getFileSize(new File(file));
							sizeAllLong += size;
						}

						long thisPicSize=DataUtil.getFileSize(new File(url));
						sizeAllLong+=thisPicSize;

//						if(sizeAllLong>20971520){//20M的长度是20971520
//							DataUtil.getToast("图片总大小不能超过20M");
//							
//						}else{
							// 判断图片张数大小
							if (addedPath.size() < limit_count) {
								addedPath.add(url);
								gridHolder.grid_img
										.setImageResource(R.drawable.photo_choose_bg_s);
							} else {
								if(limit_count == 1){
									addedPath.remove(0);
									addedPath.add(url);
									gridHolder.grid_img
									.setImageResource(R.drawable.photo_choose_bg_s);
								}else{
									DataUtil.getToast("最多选" + limit_count
											+ "张");
								}

							}
//						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
			mYhandler.sendEmptyMessage(0);
		}
	}

	Handler mYhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// total_text.setText(addedPath.size() + "/" + limit_count +
				// "张");
				// button_send.setTextSize(18f);
				// button_send.setText(addedPath.size() + "/" + limit_count);
                showChoosePicNum();
				gridAdatper.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};
	private String attachmentLimit;

	class ListAdapter extends BaseAdapter {
		private ArrayList<ImageBean> beans = null;
		LayoutInflater inflater;

		public ListAdapter() {
			inflater = LayoutInflater.from(SelectImageActivity.this);
			beans = new ArrayList<ImageBean>();
		}

		public void setData(ArrayList<ImageBean> listBeans) {
			if (listBeans != null) {
				beans.clear();
				beans.addAll(listBeans);
				notifyDataSetChanged();
			}
		}

		@Override
		public int getCount() {
			return beans.size();
		}

		@Override
		public ImageBean getItem(int arg0) {
			return beans.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			final ListViewHolder listHoder;
			ImageBean imageBean = beans.get(position);
			if (convertView == null) {
				listHoder = new ListViewHolder();
				convertView = inflater.inflate(R.layout.image_select_list_item,
						null);
				listHoder.myimage_view = (ImageView) convertView
						.findViewById(R.id.myimage_view);
				listHoder.choose_img = (ImageView) convertView
						.findViewById(R.id.choose_img);
				listHoder.folder_text = (TextView) convertView
						.findViewById(R.id.folder_text);
				listHoder.count_text = (TextView) convertView
						.findViewById(R.id.count_text);
				convertView.setTag(listHoder);
			} else {
				listHoder = (ListViewHolder) convertView.getTag();
			}
			int cho_posi = chooseItem.get(0);
			if (position == cho_posi) {
				// 相等则显示
				listHoder.choose_img.setVisibility(View.VISIBLE);
			} else {
				listHoder.choose_img.setVisibility(View.GONE);
			}
			String img_path = "";
			img_path = imageBean.getTopImagePath();
			listHoder.count_text.setVisibility(View.VISIBLE);
			listHoder.count_text.setText(imageBean.getImageCounts() + "张");

			listHoder.folder_text.setText(imageBean.getFolderName());
			mImageLoader.displayImage("file://" + img_path,
					listHoder.myimage_view, options);
			return convertView;
		}

		class ListViewHolder {
			ImageView myimage_view;
			ImageView choose_img;
			TextView folder_text, count_text;
		}

	}

	public void displayMemory() {
		Runtime rt = Runtime.getRuntime();
		long maxMemory = rt.maxMemory();
		Log.i(Constants.TAKE_PHOTO,Long.toString(maxMemory>>20)+"M");

		final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		Log.i(Constants.TAKE_PHOTO,"系统剩余内存:"+(info.availMem >> 10)+"k");
		Log.i(Constants.TAKE_PHOTO,"系统是否处于低内存运行："+info.lowMemory);
		Log.i(Constants.TAKE_PHOTO,"当系统剩余内存低于"+(info.threshold >> 20) +"时就看成低内存运行");
	}


    private void showChoosePicNum() {
        if(!addedPath.isEmpty()){
            int size = addedPath.size();
            tv_choose_pic.setText("已选择"+size+"张相片");
            tv_choose_pic.setVisibility(View.VISIBLE);
        }else{
            tv_choose_pic.setVisibility(View.GONE);
        }
    }

}
