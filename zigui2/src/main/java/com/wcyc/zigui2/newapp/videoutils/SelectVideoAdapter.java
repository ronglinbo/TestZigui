package com.wcyc.zigui2.newapp.videoutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.utils.DataUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 选择本地视频Adapter
 * 
 * @author 郑国栋 2016-6-23
 * @version 2.1
 */
public class SelectVideoAdapter extends BaseAdapter {

	private Context myContext;// 上下文
	private List<VideoModelBean> videoinfos = new ArrayList<VideoModelBean>(); // 视频文件集合
	private String videoFilePath="";
	private TextView button_send;
	private int selectedPosition=-1;
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	private DisplayImageOptions options;
	private List<Bitmap> bitmapList = new ArrayList<Bitmap>(); // 视频bitmap集合

	public SelectVideoAdapter(Context myContext, List<VideoModelBean> videoinfos,List<Bitmap> bitmapList,TextView button_send) {
		super();
		this.myContext = myContext;
		this.videoinfos = videoinfos;
		this.button_send = button_send;
		this.bitmapList = bitmapList;

//		options = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.ic_launcher)
//				.showImageForEmptyUri(R.drawable.ic_launcher)
//				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
//				.cacheOnDisc(true)
//				// cacheOnDisc cacheOnDisk
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
	}

	@Override
	public int getCount() {

		if (videoinfos != null) {
			return videoinfos.size();// 长度
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return position;// 当前位置ID
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		if (convertView == null) {
			// 实例化控件
			viewholder = new ViewHolder();
			// 配置单个item的布局
			convertView = LayoutInflater.from(myContext).inflate(
					R.layout.select_video_grid_item, parent, false);

			// 获得布局中的控件
			viewholder.grid_image =(ImageView) convertView
					.findViewById(R.id.grid_image);
			viewholder.grid_img = (ImageView) convertView
					.findViewById(R.id.grid_img);
			viewholder.time_tv=(TextView) convertView
					.findViewById(R.id.time_tv);

			// 设置标签
			convertView.setTag(viewholder);

		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}

		// 显示内容
//		String positionVideoFilePath=videoinfos.get(position).getPath();

		Bitmap bitmap=bitmapList.get(position);
		if(bitmap==null){
			viewholder.grid_image.setImageResource(R.drawable.default_image);
		}else {
			viewholder.grid_image.setImageBitmap(bitmap);
		}

		long length=videoinfos.get(position).getLength();
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		String time = sdf.format(new Date(length));
		viewholder.time_tv.setText(time + ""); // 设置时长

		if(position==selectedPosition){
			viewholder.grid_img.setImageResource(R.drawable.photo_choose_bg_s);
		}else {
			viewholder.grid_img.setImageResource(R.drawable.photo_choose_bg);
		}

		// 缩列图被点击选中监听
		convertView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						long timeLength=videoinfos.get(position).getLength();
						File file = new File(videoinfos.get(position).getPath());
						long length = file.length();
						//不识别的视频
						String filePath=videoinfos.get(position).getPath();
						String filePathEnd=filePath.substring(filePath.lastIndexOf("."));
						String noSuportFileEnd=".rmvb,.flv,.mov,.mpg,.mkv,.mpeg,.mp3,.rm";
						if(timeLength>11*1000){
							DataUtil.getToast("请选择不超过10s的视频!");
						}else if(length>6*1000*1000){
							DataUtil.getToast("请选择不超过40M的视频!");
						}else if(noSuportFileEnd.contains(filePathEnd)){
							DataUtil.getToast("不支持的格式!");
						} else{
							if(videoFilePath.contains(videoinfos.get(position).getPath())){
								//已经选择则取消选择
								selectedPosition=-1;
								videoFilePath="";
								//灰色禁用 不可点击
								button_send.setClickable(false);
								button_send.setTextColor(myContext.getResources().getColor(R.color.font_lightgray));
							}else {
								//之前没有选择，则选中
								selectedPosition=position;
								videoFilePath=videoinfos.get(position).getPath();
								//蓝色启用
								button_send.setClickable(true);
								button_send.setTextColor(myContext.getResources().getColor(R.color.font_blue));
							}
							System.out.println("===选中的视频videoFilePath======"+videoFilePath);
							notifyDataSetChanged();
						}
					}
				});

		return convertView;
	}

	private class ViewHolder {
		ImageView grid_image, grid_img;
		TextView time_tv;
	}

	public void setVideoFilePath(String videoFilePath){
		this.videoFilePath=videoFilePath;
	}
	public String getVideoFilePath(){
		return videoFilePath;
	}

	//kind 为MINI_KIND和MICRO_KIND。其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	//MediaStore.Images.Thumbnails.MICRO_KIND
	public Bitmap getVideoThumbnail(String videoPath,int width,int height,int kind) {
		Bitmap bitmap =null;
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
}
