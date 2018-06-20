package com.wcyc.zigui2.newapp.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.ImagePagerActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * @author xiehua
 * @version
 * @since 20151217
 */
public class NewGridViewAdapter extends BaseAdapter{
	private Context mContext;
//	private String[] urls;//small pic
//	private String[] pics;//big pic
	private List<String> picList;
	private List<String> bigPicList;
	private Map<String,Object> mBitmapMap;
	private static final String TAG = NewGridViewAdapter.class.getName();
	ImageLoader mImageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions mOptions;
	
//	public GridViewAdapter(Context mContext,String[] urls,String[] pics,Map<String,Object> mBitmapMap){
//		super();
//		this.mContext = mContext;
//		this.urls = urls;
//		this.pics = pics;
//		this.mBitmapMap = mBitmapMap;
//        
//        mOptions = new DisplayImageOptions.Builder()  
//        .showStubImage(R.drawable.default_image)  
//        .showImageForEmptyUri(R.drawable.default_image)  
//        .showImageOnFail(R.drawable.default_image)  
//        .cacheInMemory(true)  
//        .cacheOnDisc(true)  
//        .imageScaleType(ImageScaleType.NONE)
//        .bitmapConfig(Bitmap.Config.RGB_565)     //设置图片的解码类型  
//        .build(); 
//	}
	
	public NewGridViewAdapter(Context mContext,List<String> picList,List<String> bigPicList,Map<String,Object> mBitmapMap){
		super();
		this.mContext = mContext;
		this.picList = picList;
		this.bigPicList = bigPicList;
		this.mBitmapMap = mBitmapMap;
        
        mOptions = new DisplayImageOptions.Builder()  
        .showStubImage(R.drawable.default_image)  
        .showImageForEmptyUri(R.drawable.default_image)  
        .showImageOnFail(R.drawable.default_image)  
        .cacheInMemory(true)  
        .cacheOnDisc(true)  
        .imageScaleType(ImageScaleType.NONE)
        .bitmapConfig(Bitmap.Config.RGB_565)     //设置图片的解码类型  
        .build(); 
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(picList != null){
			return picList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return picList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		String file = urls[position];
		//GridViewHolder holder = null;
		ImageView image = null;
		if(convertView == null){
			//holder = new GridViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.classinteraction_item_grid_image, parent, false);
			image = (ImageView) convertView.findViewById(R.id.image);
			image.setScaleType(ImageView.ScaleType.CENTER_CROP);
			image.setPadding(2, 2, 2, 2);
			convertView.setTag(image);
			showImage(position,image,convertView,picList.get(position));
		}else{
			image = (ImageView) convertView.getTag();
		}
		return convertView;
	}

	private void showImage(final int position,ImageView image,View convertView,String file){
	    Bitmap bm = (Bitmap) mBitmapMap.get(Constants.URL+file+"&sf=240*320");
		if(bm != null){
			((ImageView) convertView).setImageBitmap(bm);
		}else{
			mImageLoader.displayImage(Constants.URL+file+"&sf=240*320", ((ImageView) convertView), mOptions);
		}
		image.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PictureURL pictureURL = null;
				List<PictureURL> datas = new ArrayList<PictureURL>();
				
				Log.i(TAG,"len:"+bigPicList.size());
				
				for (int i = 0; i < bigPicList.size(); i++) {
				    Log.i(TAG,"pic:"+bigPicList.get(i));
					pictureURL = new PictureURL();
					
					//方法一 未设置下载像素
					String url = DataUtil.getDownloadURL(mContext, bigPicList.get(i));

					//方法二 
//					String file = bigPicList.get(i).replace("/downloadApi","");
//					String url = Constants.DOWNLOAD_URL + file//测试环境的一个配置不对   则添加像素大小+"&sf=720*1280"
//							+ Constants.AUTHID + "@" + ((BaseActivity)mContext).getDeviceID()
//							+ "@" + CCApplication.app.getMemberInfo().getAccId();
					
					pictureURL.setPictureURL(url);
					datas.add(pictureURL);
				}
				Intent intent = new Intent(mContext,
						ImagePagerActivity.class);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
						(Serializable) datas);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,
						position);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});
	}
	private class GridViewHolder{
		ImageView image;
	}
}