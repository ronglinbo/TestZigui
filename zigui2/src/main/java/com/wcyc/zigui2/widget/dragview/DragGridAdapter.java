package com.wcyc.zigui2.widget.dragview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 可拖拽的GradView
 * 同时固定最后一个添加的Button.
 */
public class DragGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> imagePaths;
    private ImageLoader mImageLoader;
    private Handler handler;
    public static final int DELETE_IMAGES = 100;
    private DisplayImageOptions options;

    public DragGridAdapter(Context context, ArrayList<String> imagePath, ImageLoader imageLoader, Handler handler) {

        this.mImageLoader = imageLoader;
        this.handler = handler;
        this.context = context;
        options = DataUtil.getImageLoaderOptions();
        setData(imagePath);
    }


    @Override
    public int getCount() {
        return imagePaths == null ? 0 : imagePaths.size();
    }

    @Override
    public String getItem(int i) {
        return imagePaths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

//        if (view == null) {
//            view = LayoutInflater.from(context).inflate(getLayoutId(), null);
//            viewHolder = new ViewHolder(view);
//            initView(viewHolder);
//            view.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) view.getTag();
//        }

        view = LayoutInflater.from(context).inflate(getLayoutId(), null);
        viewHolder = new ViewHolder(view);
        initView(viewHolder);
        view.setTag(viewHolder);
        setViewValue(viewHolder, i);


        return view;
    }

    protected int getLayoutId() {
        return R.layout.classinteraction_gridview_item;
    }

    protected void initView(ViewHolder holder) {
        holder.addView(R.id.classinteraction_gridview_item_delete_image);
        holder.addView(R.id.classinteraction_gridview_item_image);
    }

    protected void setViewValue(ViewHolder holder, final int position) {
        ImageView iv_delete = (ImageView) holder.getView(R.id.classinteraction_gridview_item_delete_image);
        ImageView photo = (ImageView) holder.getView(R.id.classinteraction_gridview_item_image);


        if (position >= imagePaths.size() - 1) {
            photo.setImageResource(R.drawable.notification_addimage);
            photo.setTag("");
            iv_delete.setVisibility(View.GONE);
        } else {
            String imagePath = imagePaths.get(position);
            mImageLoader.displayImage(imagePath, photo, options);
            photo.setTag(imagePath);
            iv_delete.setImageResource(R.drawable.delete_image);
            iv_delete.setVisibility(View.VISIBLE);
        }

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePaths.remove(position);
                Message msg = Message.obtain();
                msg.obj = position;
                msg.what = DELETE_IMAGES;
                handler.sendMessage(msg);
            }
        });


    }

    public void moveItem(int start, int end) {
        List<String> tmpList = new ArrayList<>();
        if (start < end) {
            tmpList.clear();
            for (String s : imagePaths) tmpList.add(s);
            String endMirror = tmpList.get(end);

            tmpList.remove(end);
            tmpList.add(end, getItem(start));

            for (int i = start + 1; i <= end; i++) {
                tmpList.remove(i - 1);
                if (i != end) {
                    tmpList.add(i - 1, getItem(i));
                } else {
                    tmpList.add(i - 1, endMirror);
                }

            }

        } else {
            tmpList.clear();
            for (String s : imagePaths) tmpList.add(s);
            String startMirror = tmpList.get(end);
            tmpList.remove(end);
            tmpList.add(end, getItem(start));

            for (int i = start - 1; i >= end; i--) {
                tmpList.remove(i + 1);
                if (i != start) {
                    tmpList.add(i + 1, getItem(i));
                } else {
                    tmpList.add(i + 1, startMirror);
                }
            }

        }
        imagePaths.clear();
        imagePaths.addAll(tmpList);

        notifyDataSetChanged();
    }


    /**
     * 接受外界传进来的数据
     */
    public void setData(ArrayList<String> imagePath) {
        if (imagePaths != null && !imagePaths.isEmpty()) {
            imagePaths.clear();
        } else {
            imagePaths = new ArrayList<>();
        }
        if (imagePath == null) {
            imagePaths.add("");
            return;
        }
        for (String src : imagePath) {
            imagePaths.add(src);
        }
        imagePaths.add("");
    }

    /**
     * 获取到原本图片集合
     */
    public ArrayList<String> getOriginalData() {
        ArrayList<String> list = new ArrayList<>();
        for (String path : imagePaths) {
            if (path.contains("file://")) {
                String replace = path.replace("file://", "");
                list.add(replace);
            }
        }
        return list;
    }

    /**
     * 获取到图片地址集合
     */
    public ArrayList<PictureURL> getImageLocalData() {
        ArrayList<PictureURL> pictureList = new ArrayList<>();
        for (String path : imagePaths) {
            if (!"".equals(path)) {
                PictureURL pictureURL = new PictureURL();
                pictureURL.setPictureURL(path);
                pictureList.add(pictureURL);
            }
        }
        return pictureList;
    }
}
