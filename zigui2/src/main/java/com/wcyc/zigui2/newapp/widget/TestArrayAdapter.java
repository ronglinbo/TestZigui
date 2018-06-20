package com.wcyc.zigui2.newapp.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wcyc.zigui2.R;

import java.util.List;


/**
 * Created by hbj on 2017/4/27.
 * 改变系统Spinner 默认字体与颜色
 *
 */

public class TestArrayAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> list;


    public TestArrayAdapter(Context context, List<String> list) {
        super(context, android.R.layout.simple_spinner_item, list);
        mContext = context;
        this.list = list;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //修改Spinner展开后的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(list.get(position));
        tv.setTextSize(15f);
        tv.setTextColor(mContext.getResources().getColor(R.color.btn_gray_pressed));
        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 修改Spinner选择后结果的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(list.get(position));
        tv.setTextSize(15f);
        tv.setTextColor(mContext.getResources().getColor(R.color.btn_gray_pressed));
        return convertView;
    }

}
