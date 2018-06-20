/**
 * 文件名：com.wcyc.zigui.adapter.TConfAddImageGvAdapter.java
 *
 * 版本信息：
 * 日期：2014年10月15日 下午8:27:37
 * Copyright 2014-2015 惟楚有有才网络股份有限公司
 * 版权所有
 *
 */

package com.wcyc.zigui2.newapp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.utils.DataUtil;

//2014年10月15日 下午8:27:37
/**
 * 老师通知页面选择图片的gridview适配器.
 * @author 王登辉
 * @version 1.01
 */
public class TConfAddImageGvAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> imagePaths;
	private ImageLoader mImageLoader;
	private Handler handler;
	public static final int DELETE_IMAGES = 100;
	private DisplayImageOptions options;

	/**
	 * 创建一个新的实例 TConfAddImageGvAdapter.
	 *
	 * @param context
	 * @param imagePaths
	 */

	public TConfAddImageGvAdapter(Context context,
			ArrayList<String> imagePaths, ImageLoader mImageLoader,
			Handler handler) {
		super();
		this.mImageLoader = mImageLoader;
		this.handler = handler;
		this.context = context;
		options = DataUtil.getImageLoaderOptions();
		setData(imagePaths);
	}

	@Override
	public int getCount() {
		return imagePaths == null ? 0 : imagePaths.size();
	}

	@Override
	public Object getItem(int position) {
		return imagePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		convertView = LayoutInflater.from(context).inflate(
				R.layout.classinteraction_gridview_item, null);
		holder = new ViewHolder();
		holder.deleteIv = (ImageView) convertView
				.findViewById(R.id.classinteraction_gridview_item_delete_image);
		holder.photo = (ImageView) convertView
				.findViewById(R.id.classinteraction_gridview_item_image);
		convertView.setTag(holder);

		if (position >= imagePaths.size() -1) {
			holder.photo.setImageResource(R.drawable.notification_addimage);
			holder.photo.setTag("");
			holder.deleteIv.setVisibility(View.GONE);
		} else {
//			String imagePath = "file://" + imagePaths.get(position);
			String imagePath = imagePaths.get(position);
//			ImageLoaderConfiguration config = DataUtil.getImageLoaderConfig(context);
//			mImageLoader = ImageLoader.getInstance();
//			mImageLoader.init(config);
			mImageLoader.displayImage(imagePath, holder.photo,options);
			holder.photo.setTag(imagePath);
			holder.deleteIv.setImageResource(R.drawable.delete_image);
			holder.deleteIv.setVisibility(View.VISIBLE);
		}

		holder.deleteIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imagePaths.remove(position);
				Message msg = Message.obtain();
				msg.obj = position;
				msg.what = DELETE_IMAGES;
				handler.sendMessage(msg);
			}
		});

		return convertView;
	}

	public class ViewHolder {
		public ImageView deleteIv, photo;
	}

	/**
	 *  接受外界传进来的数据
	 */

	public void setData(ArrayList<String> imagePath) {
        if (imagePaths != null && !imagePaths.isEmpty()) {
        	imagePaths.clear();
        }else{
        	imagePaths = new ArrayList<String>();
        }
		if(imagePath == null){
			imagePaths.add("");
			return;
		}
        for (String src : imagePath) {
        	imagePaths.add(src);
        }
        imagePaths.add("");
	}

}
