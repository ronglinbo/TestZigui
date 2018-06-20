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
import com.wcyc.zigui2.newapp.bean.EducationNewsBean;
import com.wcyc.zigui2.newapp.fragment.EducationNewsFragment;
import com.wcyc.zigui2.newapp.module.educationinfor.EducationDetailsActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

import java.util.List;


/**
 * 教育资讯_其他类型新闻列表
 */
public class EducationNewsAdapter extends BaseAdapter {

    private Context context;
    private List<EducationNewsBean.CampuEcirclesListBean> campuEcirclesList;
    private EducationNewsFragment fragment;

    public EducationNewsAdapter(List<EducationNewsBean.CampuEcirclesListBean> campuEcirclesList, EducationNewsFragment fragment, Context context) {
        this.context = context;
        this.campuEcirclesList = campuEcirclesList;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return campuEcirclesList == null ? 0 : campuEcirclesList.size();
    }

    @Override
    public Object getItem(int position) {
        return campuEcirclesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        EducationNewsAdapter.ViewHolder viewHolder = null;
        final EducationNewsBean.CampuEcirclesListBean item = campuEcirclesList.get(position);
        if (convertView == null) {
            // 实例化控件
            viewHolder = new EducationNewsAdapter.ViewHolder();
            // 配置单个item的布局
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sticky1, null);
            viewHolder = new EducationNewsAdapter.ViewHolder();
            viewHolder.tv_news_title = (TextView) convertView.findViewById(R.id.tv_news_title);
            viewHolder.iv_news = (ImageView) convertView.findViewById(R.id.iv_news);
            viewHolder.tv_create_time = (TextView) convertView.findViewById(R.id.tv_create_time);
            viewHolder.tv_watch_times = (TextView) convertView.findViewById(R.id.tv_watch_times);
            viewHolder.rl_item_news = (RelativeLayout) convertView.findViewById(R.id.rl_item_news);
            viewHolder.unread_iv = (ImageView) convertView.findViewById(R.id.unread_iv);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (EducationNewsAdapter.ViewHolder) convertView.getTag();
        }

        // 显示图片
        if (!DataUtil.isNullorEmpty(item.getCover())) {

            //防止刷新ListView的时候 ImageLoad加载图片  导致ListView图片全部一闪
            if (item.getCover().equals(viewHolder.iv_news.getTag())) {

            } else {
                String url = Constants.URL + "/downloadApi?fileId=" + item.getCover() + "&sf=80*150";
                ((BaseActivity) context).getImageLoader()
                        .displayImage(url, viewHolder.iv_news, ((BaseActivity) context)
                                .getImageLoaderOptions());
                viewHolder.iv_news.setTag(item.getCover());
            }
        }

        viewHolder.tv_news_title.setText(item.getTitle());

        if (item.getBrowseNo() < 100000) {
            viewHolder.tv_watch_times.setText(String.valueOf(item.getBrowseNo()));
        } else {
            viewHolder.tv_watch_times.setText("99999+");
        }

        String publishTime = item.getPublishTime();

        //不需要秒
        if (!DataUtil.isNullorEmpty(publishTime)) {
            publishTime = publishTime.substring(0, publishTime.lastIndexOf(":"));
        }
        viewHolder.tv_create_time.setText(publishTime);

        //已读未读   为1已读，为0未读
        String isRead = item.getIsRead();
        if ("0".equals(isRead)) {
            viewHolder.unread_iv.setVisibility(View.VISIBLE);
        } else if ("1".equals(isRead)) {
            viewHolder.unread_iv.setVisibility(View.GONE);
        } else {
            viewHolder.unread_iv.setVisibility(View.GONE);
        }

        //新闻Item点击时间.
        viewHolder.rl_item_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //只有当外界没有显示进度条的时候这里才调用显示进度条
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

        return convertView;
    }

    public void RefreshData(List<EducationNewsBean.CampuEcirclesListBean> campuEcirclesList) {
        this.campuEcirclesList = campuEcirclesList;
    }

    private class ViewHolder {

        //新闻title
        private TextView tv_news_title;

        private ImageView unread_iv;

        //新闻图片
        private ImageView iv_news;

        //创建时间
        private TextView tv_create_time;

        //查看数量
        private TextView tv_watch_times;

        private RelativeLayout rl_item_news;
    }

    // 添加数据
    public void addItem(List<EducationNewsBean.CampuEcirclesListBean> i) {
        campuEcirclesList.addAll(i);
    }

}