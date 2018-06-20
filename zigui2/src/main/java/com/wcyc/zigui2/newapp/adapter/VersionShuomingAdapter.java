package com.wcyc.zigui2.newapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PointBean;
import com.wcyc.zigui2.newapp.bean.NewPointBean;
import com.wcyc.zigui2.newapp.bean.Versionupdateinfolist;
import com.wcyc.zigui2.newapp.module.duty.NewMyDutyBean;
import com.wcyc.zigui2.utils.DateUtils;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 版本更新说明 章豪
 * @author xiehua
 * @version
 * @since 20151222
 */
public class VersionShuomingAdapter extends BaseAdapter {
    private Context mContext;
    private List<Versionupdateinfolist> data_list;

    public VersionShuomingAdapter(Context mContext,
                                   List<Versionupdateinfolist> data_list) {
        super();
        this.mContext = mContext;
        this.data_list = data_list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        // System.out.println("size:"+data_list.size());
        return data_list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TextView tv_content, tv_name = null;
        ViewHolder viewholder = null;
        // System.out.println("position:"+position
        // +" size:"+data_list.size()+" list:"+data_list.get(position));
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.version_shuoming_item, parent, false);
            viewholder.title = (TextView) convertView
                    .findViewById(R.id.title);
            viewholder.date = (TextView) convertView
                    .findViewById(R.id.date);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        // String name = (String) data_list.get(position).getAuthorname();

        String title = data_list.get(position).getTitle();
        String date=data_list.get(position).getUpdateTime();

        String datestr= DateUtils.getInstance().stringDateToStringData("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", date);
        viewholder.date.setText(datestr);
        viewholder.title.setText(title);





        return convertView;
    }
    // 添加数据
    public void addItem(ArrayList<Versionupdateinfolist> i){

        data_list.addAll(i);
    }

    private class ViewHolder {
        TextView title;
        TextView date;


    }
}