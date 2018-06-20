package com.wcyc.zigui2.newapp.module.classdynamics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.bean.NewClassDynamicsBean1;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewPersonalBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * 
 * 创 建 人： gdzheng
 * 日    期： 2016-04-06
 * 版 本 号： 
 */

/**
 * @author gdzheng
 */
public class NewPersonalDynamicsAdapter extends BaseAdapter {
    private Context mContext;
    private List<NewPersonalBean> data_list;

    DisplayImageOptions options; // 显示图片的设置
    ImageLoader imageLoader;
    private Map<String, Object> mBitmapMap = new HashMap<String, Object>();
    private String attchmentType;

    public NewPersonalDynamicsAdapter(Context mContext,
                                      List<NewPersonalBean> data_list) {
        super();
        this.mContext = mContext;
        this.data_list = data_list;
        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image).cacheInMemory(true)
                .imageScaleType(ImageScaleType.NONE).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .build();

        DownloadAllImages();
    }

    private void DownloadAllImages() {
        final int num = data_list.size();
        // String[] tempurls;
        List<String> tempurls;
        for (int i = 0; i < num; i++) {
            List<NewPersonalBean.ClassAttachmentBean> clssAttachmentList = new ArrayList<NewPersonalBean.ClassAttachmentBean>();
            tempurls = new ArrayList<String>();
            clssAttachmentList = data_list.get(i).getAttachmentInfoList_new();
            for (int k = 0; k < clssAttachmentList.size(); k++) {
                String attachementUrl = data_list.get(i).getAttachmentInfoList_new().get(k).getAttachementUrl();
                tempurls.add(attachementUrl);
            }
            if (tempurls != null) {
                int length = tempurls.size();

                for (int j = 0; j < length; j++) {
                    // tempurls.get(j)+ "&sf=80*80");
                    attchmentType = data_list.get(i).getAttchmentType();//1是图片  2是视频音频 3是文件文档
                    if (!"2".equals(attchmentType) && !"6".equals(attchmentType) && !"5".equals(attchmentType)) {
                        imageLoader.loadImage(Constants.URL + tempurls.get(j)
                                + "&sf=150*150", options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingCancelled(String arg0, View arg1) {
                            }

                            @Override
                            public void onLoadingComplete(String arg0, View arg1,
                                                          Bitmap arg2) {
                                mBitmapMap.put(arg0, arg2);
                            }

                            @Override
                            public void onLoadingFailed(String arg0, View arg1,
                                                        FailReason arg2) {
                            }

                            @Override
                            public void onLoadingStarted(String arg0, View arg1) {
                            }
                        });
                    }
                }
                //
            }
        }
    }

    @Override
    public int getCount() {

        return data_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.new_personal_interaction_item, parent, false);
            viewholder.personl_time = (TextView) convertView
                    .findViewById(R.id.personl_time);
            viewholder.personl_zi = (TextView) convertView
                    .findViewById(R.id.personl_zi);
            viewholder.personl_yuan = (ImageView) convertView
                    .findViewById(R.id.personl_yuan);
            viewholder.personl_tu = (ImageView) convertView
                    .findViewById(R.id.personl_tu);
            viewholder.personl_pic_numb = (TextView) convertView
                    .findViewById(R.id.personl_pic_numb);
            viewholder.class_vedio_ll = (RelativeLayout) convertView
                    .findViewById(R.id.class_vedio_ll);
            viewholder.class_vedio_tag_iv = (ImageView) convertView
                    .findViewById(R.id.class_vedio_tag_iv);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();

            viewholder.personl_time.setVisibility(View.VISIBLE);
            viewholder.personl_yuan.setVisibility(View.VISIBLE);
            viewholder.personl_tu.setVisibility(View.VISIBLE);
            viewholder.class_vedio_ll.setVisibility(View.VISIBLE);
            viewholder.personl_pic_numb.setText("");
        }
        // 写逻辑
        // 时间显示 这次的这个时间和上次的相等 那就不要显示了 圆和时间是一起的
        String this_time = data_list.get(position).getPublishTime().split(" ")[0];
        String this_time1 = this_time.substring(this_time.indexOf("-") + 1);
        this_time1 = this_time1.replace("-", "/");

        if (position != 0) {
            String last_time = data_list.get(position - 1).getPublishTime()
                    .split(" ")[0];
            // data_list.get(position).getPublishTime().equals(data_list.get(position-1).getPublishTime())
            // this_time.equals(last_time)
            if (this_time.equals(last_time)) {
                // viewholder.personl_time.setVisibility(View.GONE);
                viewholder.personl_time.setVisibility(View.INVISIBLE);
                viewholder.personl_yuan.setVisibility(View.INVISIBLE);
            } else {
                viewholder.personl_time.setVisibility(View.VISIBLE);
                viewholder.personl_yuan.setVisibility(View.VISIBLE);
                // this_time data_list.get(position).getPublishTime()
                viewholder.personl_time.setText(this_time1);
            }
        } else {
            viewholder.personl_time.setText(this_time1);

        }
        // 显示图片
        if (data_list.get(position).getAttachmentInfoList_new().size() == 0) {
            viewholder.personl_tu.setVisibility(View.GONE);
            viewholder.class_vedio_ll.setVisibility(View.GONE);
        } else {

            if (data_list.get(position).getAttachmentInfoList_new().get(0) != null) {

                if (data_list.get(position).getAttachmentInfoList_new() != null) {
                    String file = data_list.get(position).getAttachmentInfoList_new().get(0).getAttachementUrl();
                    attchmentType = data_list.get(position).getAttchmentType();//1是图片  2是视频音频 3是文件文档
                    if ("2".equals(attchmentType) || "6".equals(attchmentType) || "5".equals(attchmentType)) {
                        viewholder.class_vedio_tag_iv.setVisibility(View.VISIBLE);
                        if ("2".equals(attchmentType)) {//如果是app发的班级动态
                            String pcitureAddress = data_list.get(position).getPcitureAddress();
                            if (!DataUtil.isNullorEmpty(pcitureAddress)) {
                                String url = DataUtil.getDownloadURL( mContext, "/downloadApi?" + pcitureAddress);
                                if (!DataUtil.isNullorEmpty(url)) {
                                    url = url.replaceAll("&vedio=ys", "");
                                }
                                ((BaseActivity) mContext).getImageLoader().displayImage(url, viewholder.personl_tu,
                                        ((BaseActivity) mContext).getImageLoaderOptions());
                            } else {
                                viewholder.personl_tu.setImageResource(R.drawable.default_image);
                            }
                        } else {
                            String url = DataUtil.getDownloadURL(mContext, file + "&vedio=pic");
                            if (!DataUtil.isNullorEmpty(url)) {
                                url = url.replaceAll("&vedio=ys", "");
                            }
                            ((BaseActivity) mContext).getImageLoader().displayImage(url, viewholder.personl_tu,
                                    ((BaseActivity) mContext).getImageLoaderOptions());
                        }
                    } else {
                        viewholder.class_vedio_tag_iv.setVisibility(View.GONE);
                        //方法一
                        file = file.replace("/downloadApi", "");
                        String url = Constants.DOWNLOAD_URL + file + "&sf=240*320"//添加图像素大小
                                + Constants.AUTHID + "@" + ((BaseActivity) mContext).getDeviceID()
                                + "@" + CCApplication.app.getMemberInfo().getAccId();

                        ((BaseActivity) mContext).getImageLoader().displayImage(
                                url, viewholder.personl_tu,
                                ((BaseActivity) mContext).getImageLoaderOptions());

                        //方法二
//					ImageUtils.showImage((BaseActivity) mContext, file, viewholder.personl_tu);//缩略图

                        //方法三
//					String url_2 =Constants.URL + file + "&sf=240*320";
//					ImageLoader mloader = ((BaseActivity)mContext).getImageLoader();
//					mloader.displayImage(url_2, viewholder.personl_tu);
                    }

                    String pic_numb = data_list.get(position)
                            .getAttachmentInfoList_new().size()
                            + "";
                    viewholder.personl_pic_numb.setText("共" + pic_numb + "张");

                }
            }

        }
        // 显示内容
        viewholder.personl_zi.setText(data_list.get(position).getContent());
        return convertView;
    }

    private class ViewHolder {
        TextView personl_time, personl_zi, personl_pic_numb;
        ImageView personl_yuan, personl_tu, class_vedio_tag_iv;
        RelativeLayout class_vedio_ll;
    }

    // 添加数据
    public void addItem(List<NewPersonalBean> i) {
        data_list.addAll(i);
    }
}
