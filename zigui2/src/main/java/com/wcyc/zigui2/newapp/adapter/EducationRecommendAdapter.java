package com.wcyc.zigui2.newapp.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.bean.Category;
import com.wcyc.zigui2.newapp.bean.RecommendNewsBean;
import com.wcyc.zigui2.newapp.module.educationinfor.EducationDetailsActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

import java.util.ArrayList;


/**
 * 教育资讯-推荐新闻
 * 主要是分类显示ListView
 */
public class EducationRecommendAdapter extends BaseAdapter {

    private static final int TYPE_CATEGORY_ITEM = 0;
    private static final int TYPE_ITEM = 1;

    private ArrayList<Category> mListData;
    private LayoutInflater mInflater;
    private Context context;
    private static final int EDUCATION_DETAILS_CODE = 100;
    private Fragment fragment;

    public EducationRecommendAdapter(Fragment fragment, Context context, ArrayList<Category> pData) {
        mListData = pData;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        int count = 0;

        if (null != mListData) {
            //  所有分类中item的总和是ListVIew  Item的总个数
            for (Category category : mListData) {
                count += category.getItemCount();
            }
        }

        return count;
    }

    @Override
    public Object getItem(int position) {

        // 异常情况处理
        if (null == mListData || position < 0 || position > getCount()) {
            return null;
        }

        // 同一分类内，第一个元素的索引值
        int categroyFirstIndex = 0;

        for (Category category : mListData) {
            int size = category.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            // item在当前分类内
            if (categoryIndex < size) {
                return category.getItem(categoryIndex);
            }

            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categroyFirstIndex += size;
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        // 异常情况处理
        if (null == mListData || position < 0 || position > getCount()) {
            return TYPE_ITEM;
        }


        int categroyFirstIndex = 0;

        for (Category category : mListData) {
            int size = category.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            if (categoryIndex == 0) {
                return TYPE_CATEGORY_ITEM;
            }

            categroyFirstIndex += size;
        }

        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE_CATEGORY_ITEM:

                if (null == convertView) {
                    convertView = mInflater.inflate(R.layout.item_sticky_header, null);
                }
                TextView textView = (TextView) convertView.findViewById(R.id.header);
                String itemValue = (String) getItem(position);
                textView.setText(itemValue);
                break;

            case TYPE_ITEM:
                ViewHolder viewHolder = null;
                if (null == convertView) {
                    convertView = mInflater.inflate(R.layout.item_sticky1, null);
                    viewHolder = new ViewHolder();
                    viewHolder.tv_news_title = (TextView) convertView.findViewById(R.id.tv_news_title);
                    viewHolder.iv_news = (ImageView) convertView.findViewById(R.id.iv_news);
                    viewHolder.tv_create_time = (TextView) convertView.findViewById(R.id.tv_create_time);
                    viewHolder.tv_watch_times = (TextView) convertView.findViewById(R.id.tv_watch_times);
                    viewHolder.rl_item_news = (RelativeLayout) convertView.findViewById(R.id.rl_item_news);
                    viewHolder.unread_iv = (ImageView) convertView.findViewById(R.id.unread_iv);
                    viewHolder.iv_eye = (ImageView) convertView.findViewById(R.id.iv_eye);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                final RecommendNewsBean.CampuEcirclesListBean item = (RecommendNewsBean.CampuEcirclesListBean) getItem(position);

                //推荐页面不需要区分已读未读
                viewHolder.unread_iv.setVisibility(View.GONE);
                viewHolder.tv_news_title.setText(item.getTitle());


                //如果是热门和最新  就不显示阅读数
                if(item.isHideBrowserNO()){
                    viewHolder.iv_eye.setVisibility(View.VISIBLE);
                    viewHolder.tv_watch_times.setVisibility(View.VISIBLE);

                    if(item.getBrowseNo()<100000){
                        viewHolder.tv_watch_times.setText(String.valueOf(item.getBrowseNo()));
                    }else{
                        viewHolder.tv_watch_times.setText("99999+");
                    }

                }else{
                    viewHolder.iv_eye.setVisibility(View.INVISIBLE);
                    viewHolder.tv_watch_times.setVisibility(View.INVISIBLE);
                }

                String publishTime = item.getPublishTime();
                if (!DataUtil.isNullorEmpty(publishTime)) {
                    publishTime = publishTime.substring(0, publishTime.lastIndexOf(":"));
                }
                viewHolder.tv_create_time.setText(publishTime);

                if (!DataUtil.isNullorEmpty(item.getCover())) {

                    if (item.getCover().equals(viewHolder.iv_news.getTag())) {

                    } else {
                        String url = Constants.URL + "/downloadApi?fileId=" + item.getCover() + "&sf=80*150";
                        ((BaseActivity) context).getImageLoader()
                                .displayImage(url, viewHolder.iv_news, ((BaseActivity) context)
                                        .getImageLoaderOptions());
                        viewHolder.iv_news.setTag(item.getCover());
                    }
                }

                viewHolder.rl_item_news.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (DataUtil.pd == null || !DataUtil.pd.isShowing()) {
                            DataUtil.showDialog(fragment.getActivity());
                        }
                        Intent intent = new Intent(context, EducationDetailsActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("campuNewsId", item.getId() + "");
                        intent.putExtra("isRead", item.getIsRead());
                        int browseNo = item.getBrowseNo();
                        browseNo += 1;
                        item.setBrowseNo(browseNo);
                        item.setIsRead("1");
                        fragment.startActivityForResult(intent, Activity.RESULT_FIRST_USER);
                        notifyDataSetChanged();
                    }
                });

                break;
        }

        return convertView;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_CATEGORY_ITEM;
    }

    public void refreshData(ArrayList<Category> listData) {
        this.mListData = listData;
    }


    private class ViewHolder {

        //新闻title
        private TextView tv_news_title;

        //新闻图片
        private ImageView iv_news;

        //创建时间
        private TextView tv_create_time;

        //查看数量
        private TextView tv_watch_times;

        private RelativeLayout rl_item_news;

        private ImageView unread_iv;

        private ImageView iv_eye;
    }


    private class StickViewHolder {
        TextView content;
    }
}
