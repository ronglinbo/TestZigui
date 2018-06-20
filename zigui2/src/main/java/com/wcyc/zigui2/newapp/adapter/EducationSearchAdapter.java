package com.wcyc.zigui2.newapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.EducationSearchBean;
import com.wcyc.zigui2.newapp.module.educationinfor.EducationDetailsActivity;

import java.util.List;

/**
 * @author zzc
 * @time 2018/1/2 0002
 */
public class EducationSearchAdapter extends BaseAdapter {
    private Context context;

    private List<EducationSearchBean.EcirclesListBean> list;

    public EducationSearchAdapter(Context context, List<EducationSearchBean.EcirclesListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.tv_news_title = (TextView) convertView.findViewById(R.id.tv_news_title);
            viewHolder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final EducationSearchBean.EcirclesListBean item = list.get(position);
        viewHolder.tv_news_title.setText(item.getTitle());
        //新闻Item点击时间.
        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EducationDetailsActivity.class);
                intent.putExtra("campuNewsId", item.getId() + "");
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_news_title;
        private LinearLayout ll_item;
    }
}
