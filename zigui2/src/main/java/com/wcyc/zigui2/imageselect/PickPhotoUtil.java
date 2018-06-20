/*
* 文 件 名:PickPhotoUtil.java
* 创 建 人： 姜韵雯
* 日    期： 2014-10-6
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.imageselect;

import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.SoftReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.utils.Constants;

//2014-10-6
/**
 * 图片选择器的调用相机类.
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public class PickPhotoUtil {
	
	private static final String TAG = "PickPhotoUtil";
	
	private static PickPhotoUtil instance;

    private static String file = null;
    
	public static PickPhotoUtil getInstance() {
		if (instance == null) {
			instance = new PickPhotoUtil();
		}
		return instance;
	}

	public interface PickPhotoCode {
		/**
		 * 基数
		 */
		int BASE_CAMERA = 200;
		/**
		 * 拍照
		 */
		int PICKPHOTO_TAKE = BASE_CAMERA + 1;
		/**
		 * 裁剪
		 */
		int PICKPHOTO_CUTTING = BASE_CAMERA + 2;
		/**
		 * 相册
		 */
		int PICKPHOTO_LOCAL = BASE_CAMERA + 3;
	}
	
	private PickPhotoUtil() {}	
	
	/**
	 * 是否有SD卡
	 * @return
	 */
	private boolean isStorageState() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}
	
	/**
	 * 从相册取
	 * @param mActivity
	 */
	public void localPhoto(Activity mActivity) {
		if (isStorageState()) {
			Intent mIntent = new Intent(Intent.ACTION_PICK);
			mIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			mActivity.startActivityForResult(mIntent,
					PickPhotoCode.PICKPHOTO_LOCAL);
		} else {
			Toast.makeText(mActivity, "未找到SD卡", Toast.LENGTH_LONG).show();
		}		
	}
	
	/**
	 * 拍照选取
	 * @param mActivity
	 */
	public String takePhoto(Activity mActivity, String userName, String path) {
		boolean hasSD = isStorageState();
		Log.i(Constants.TAKE_PHOTO,"hasSD:"+hasSD);
		if (hasSD) {
			File imgFile = new File(path);
			Log.i(Constants.TAKE_PHOTO,"imgFile:" + imgFile);
			if (null != imgFile) {
				Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// Samsung的系统相机，版式是横板的,同时此activity不要设置单例模式
//				if (OsBuild.isModel(OsBuild.Model.SAMSUNG_GT_S6)) {
//					mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//				}
				//去掉针对三星手机
				mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
				// 调用系统拍照
				mActivity.startActivityForResult(mIntent,
						PickPhotoCode.PICKPHOTO_TAKE);
				//保存图片路径
				file = path;
				Log.i(Constants.TAKE_PHOTO,"takePhoto file:"+file);
				CCApplication.getInstance().setPickPhotoFilename(file);
				return imgFile.getAbsolutePath();
			}else {
				Log.e(TAG, "创建图片对象有误");
				Toast.makeText(mActivity, "创建图片对象有误", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(mActivity, "未找到SD卡", Toast.LENGTH_LONG).show();
		}	
		return null;
	}
	
	/**
	 * 裁剪图片.
	 * @param mActivity 页面
	 * @param uri 图片URI
	 * @param outputX 裁剪图片的宽，如果传递的数&lt;=0，则赋默认80
	 * @param outputY 裁剪图片的高，如果传递的数&lt;=0，则赋默认80
	 */
	public void cutting(Activity mActivity, Uri uri, int outputX, int outputY) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");

		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		int aspectX = 0;
		int aspectY = 0;
		
		aspectX = 1;
		aspectY = 1;
		
		if (outputX <= 0) {
			outputX = 80;
		}
		if (outputY <= 0) {
			outputY = 80;
		}

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		// outputX outputY 是裁剪图片宽
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);

		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);

		mActivity.startActivityForResult(intent, PickPhotoCode.PICKPHOTO_CUTTING);		
	}	
	
	/**
	 * 拍照后获取uri
	 * @param mContext
	 * @param data
	 * @param imgFile
	 * @return
	 */
	public String takeResult(Context mContext, Intent data, File imgFile) {
		String mUri = null;
		Uri uri = null;
		if (data != null) {
			uri = data.getData();
			Log.i(Constants.TAKE_PHOTO,"---uri---" + uri);
			if (uri != null) {
				mUri = uri.toString();
			}
		}
		if (mUri == null || "".equals(mUri)) {
			mUri = insert(mContext, imgFile);
			Log.i(Constants.TAKE_PHOTO,"---mUri---" + mUri);
		}
		return mUri;
	}

	/**
	 * 拍照时图片的数据库
	 * 主动调用主要是防止拍照是，系统没有主动保存数据库，回调查询该照片不为
	 * 
	 * @param mContext
	 *            当前对象
	 * @param obj
	 *            文件对象
	 */
	private String insert(Context mContext, Object obj) {
		Log.i(Constants.TAKE_PHOTO,"insert");
		if (mContext == null || obj == null) {
			return "";
		}
		String insertImageUri = null;
		String filePath = null;
		if (obj instanceof String) {
			filePath = (String) obj;
		} else if (obj instanceof File) {
			filePath = obj.toString();
		}
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			return "";
		}
		String fileName = substring(filePath);
		Bitmap bitmap = null;
		try {
			long length = getFileLength(file);
			if (length < 1024 * 1024) {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(
						filePath));
			} else {
				bitmap = compressBitmap(filePath);
			}
//			if (OsBuild.isModel(OsBuild.Model.SAMSUNG)) {
//				Log.e(TAG, "is SAMSUNG, reset bitmap");
//				Matrix matrix = new Matrix(); 
//				matrix.setRotate(90);
//				bitmap= Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//			}
			insertImageUri = MediaStore.Images.Media.insertImage(
					mContext.getContentResolver(), bitmap, fileName, fileName);
			String _id = substring(insertImageUri);
			if (_id != null && !"".equals(_id)) {
				return insertImageUri;
			}
		} catch (Exception e) {
			insertImageUri = null;
			e.printStackTrace();
		} finally {
			if (null != bitmap) {
				bitmap.recycle();
			}
		}
		return insertImageUri;
	}	
	
	private String substring(String str) {
		if (str == null || "".equals(str)) {
			return null;
		}
		return str.substring(str.lastIndexOf("/") + 1);
	}

	private Bitmap compressBitmap(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return null;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = 4;
		try {
			options.inJustDecodeBounds = false;

			Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
			return bmp == null ? null : bmp;
		} catch (OutOfMemoryError err) {
			return null;
		}
	}

	/**
	 * 
	 * 获取文件的大小
	 * @param obj
	 * @return long
	 */
	private long getFileLength(Object obj) {
		if (obj == null || "".equals(obj)) {
			return 0;
		}
		File file;
		if (obj instanceof String) {
			file = new File(obj.toString());
		} else if (obj instanceof File) {
			file = new File(obj.toString());
		} else {
			return 0;
		}
		if (file != null && file.isFile()) {
			return file.length();
		}
		return 0;
	}	
	
	/**
	 * 回收bitmap对象
	 * @param bmpRef
	 */
	private void recycleRefIfNeeded(SoftReference<Bitmap> bmpRef) {
		if (bmpRef != null && bmpRef.get() != null
				&& !bmpRef.get().isRecycled()) {
			bmpRef.get().recycle();
			bmpRef.clear();
		}
	}	
	
	/**
	 * 使用Uri返回对应的bitmap对象
	 * @param uri
	 */
	public Bitmap getBitmapFromUri (Context context, Uri uri) {
		String scheme = uri.getScheme();
		String pathName = "";
		if ("file".equalsIgnoreCase(scheme)) {
			pathName = uri.getPath();
			
		}
		else if ("content".equalsIgnoreCase(scheme)) {
	        String[] filePathColumn = {MediaStore.Images.Media.DATA};  
	        Cursor cursor = context.getContentResolver().query(uri,  
	                filePathColumn, null, null, null);  
	        cursor.moveToFirst();  
	        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
	        pathName = cursor.getString(columnIndex);  
	        cursor.close();
		}
		return BitmapFactory.decodeFile(pathName);		
	}

	public String getPicturePath(){
		return file;
	}
	
	public void clearPicturePath(){
		Log.i(Constants.TAKE_PHOTO,"clearPicturePath:");
	    file = null;
	}
}