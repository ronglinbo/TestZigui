package com.wcyc.zigui2.newapp.videoutils;

import android.content.Context;
import android.graphics.Bitmap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.wcyc.zigui2.R;
import com.wcyc.zigui2.utils.DataUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jiana on 16-4-3.
 */
public class VideoGridViewImageRVAdapter extends RecyclerView.Adapter<VideoGridViewImageRVAdapter.MViewHolder>{
    private Context context;
    /**
     * 视频链接的数据
     */
    private String[] videoUrls;
    private List<VideoModelBean> videoinfos;
    private boolean isFirst = true;
    private VideoFrameImageLoader mVideoFrameImageLoader;
    private int selectedPosition=-1;
    private TextView button_send;

    public String getVideoFilePath() {
        return videoFilePath;
    }

    private String videoFilePath="";
    private ImageView recrod_img;
    public VideoGridViewImageRVAdapter(Context context, VideoFrameImageLoader vfi,TextView button) {
        this.context = context;
        this.mVideoFrameImageLoader = vfi;
        this.videoinfos = vfi.getVideoinfos();
        button_send=button;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_video_grid_item, parent, false);

        MViewHolder vh = new MViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final MViewHolder holder, final int position) {
        if (isFirst) {
            mVideoFrameImageLoader.initList();
            isFirst = false;
        }



        // 显示内容
//		String positionVideoFilePath=videoinfos.get(position).getPath();



       String    mImageUrl = videoinfos.get(position).getPath();
        holder. grid_image.setTag(mImageUrl);

        //从缓存中获取图片
        Bitmap bitmap = mVideoFrameImageLoader.showCacheBitmap(VideoFrameImageLoader.formatVideoUrl(mImageUrl));
        if(bitmap==null){
            holder. grid_image.setImageResource(R.drawable.default_image);
        }else {
            holder. grid_image.setImageBitmap(bitmap);
        }

        long length=videoinfos.get(position).getLength();
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = sdf.format(new Date(length));
        holder.time_tv.setText(time + ""); // 设置时长
        if(position==selectedPosition){
            holder.grid_img.setImageResource(R.drawable.photo_choose_bg_s);
            holder.grid_img.setTag("true");
        }else {
            holder.grid_img.setImageResource(R.drawable.photo_choose_bg);
            holder.grid_img.setTag(null);
        }


        // 缩列图被点击选中监听
        holder.view.setOnClickListener(new View.OnClickListener() {
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
                    DataUtil.getToastShort("请选择不超过10s的视频!");
                }else if(length>40*1024*1024){
                    DataUtil.getToastShort("请选择不超过40M的视频!");
                }else if(noSuportFileEnd.contains(filePathEnd)){
                    DataUtil.getToastShort("不支持的格式!");
                } else{
                    if(videoFilePath.contains(videoinfos.get(position).getPath())){
                        //已经选择则取消选择
                        selectedPosition=-1;
                        videoFilePath="";
                        //灰色禁用 不可点击
                        button_send.setClickable(false);
                        button_send.setTextColor(context.getResources().getColor(R.color.font_lightgray));
                    }else {
                        //之前没有选择，则选中
                        selectedPosition=position;
                        videoFilePath=videoinfos.get(position).getPath();
                        //蓝色启用
                        button_send.setClickable(true);
                        button_send.setTextColor(context.getResources().getColor(R.color.font_blue));
                    }
                    System.out.println("===选中的视频videoFilePath======"+videoFilePath);
                    if(position==selectedPosition){
                        holder.grid_img.setImageResource(R.drawable.photo_choose_bg_s);
                        if(recrod_img!=null){
                          recrod_img.setImageResource(R.drawable.photo_choose_bg);
                        }
                        recrod_img= holder.grid_img;

                    }else {
                        holder.grid_img.setImageResource(R.drawable.photo_choose_bg);
                        recrod_img=null;
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoinfos.size();
    }

    public class MViewHolder extends RecyclerView.ViewHolder {

        public ImageView grid_image, grid_img;
        public  TextView time_tv;
        public  View view;

        public MViewHolder(View itemView) {
            super(itemView);
            // 获得布局中的控件
            view=itemView;
           grid_image =(ImageView) itemView
                    .findViewById(R.id.grid_image);
           grid_img = (ImageView) itemView
                    .findViewById(R.id.grid_img);
           time_tv=(TextView) itemView
                    .findViewById(R.id.time_tv);
        }
    }


}
