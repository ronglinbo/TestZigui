package com.wcyc.zigui2.newapp.module.notice;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.map.Symbol;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.widget.ExpandGridView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xiehua on 2016/11/14.
 */

public class NoticeBrowseDetail extends PopupWindow {
    private Activity mContext;
    private NoticeBrowserBean browserBean;
    private List<NoticeBrowserBean.ResultMap> studentList,teacherList;
    private final int LOAD_ALL_STUDENT_FINISH = 101;
    private final int LOAD_ALL_TEACHER = 100;
    private final int LOAD_ALL_STUDENT = 200;
    private final int LOAD_NUMBER = 500;
    private ExpandGridView teacher,student;
    private BrowserAdapter teacherAdapter,studentAdapter;
    private class Pos{
        int start;
        int end;
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOAD_ALL_STUDENT:
                    DataUtil.showDialog(mContext);
                    Bundle bundle = msg.getData();
                    List<NoticeBrowserBean.ResultMap> studentList =
                            (List<NoticeBrowserBean.ResultMap>) bundle.getSerializable("student");
                    setStudent(studentList);
                    DataUtil.clearDialog();
                    break;

                case LOAD_ALL_TEACHER:
                    bundle = msg.getData();
                    List<NoticeBrowserBean.ResultMap> teacherList =
                            (List<NoticeBrowserBean.ResultMap>) bundle.getSerializable("teacher");
                    setTeacher(teacherList);
                    break;
                case LOAD_ALL_STUDENT_FINISH:
                    studentAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public NoticeBrowseDetail(Activity mContext,NoticeBrowserBean browserBean){
        this.mContext = mContext;
        this.browserBean = browserBean;
        initView();
    }

    public NoticeBrowseDetail(Activity mContext,final List<NoticeBrowserBean.ResultMap> teacherList,
                              final List<NoticeBrowserBean.ResultMap> studentList){
        this.mContext = mContext;
        this.teacherList = teacherList;
        this.studentList = studentList;
        initView();
    }

    private void initView(){
        LayoutInflater mInflater = mContext.getLayoutInflater();
        View view = mInflater.inflate(R.layout.notice_browse_detail,null);
        TextView title = (TextView) view.findViewById(R.id.new_content);
        title.setText("查看人数");
        title.setTextColor(Color.BLACK);
        title.setTextSize(18);
        TextView back = (TextView) view.findViewById(R.id.title_right_tv);
        back.setText("关闭");
        back.setTextColor(Color.BLACK);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        teacher = (ExpandGridView) view.findViewById(R.id.teacherlist);
        student = (ExpandGridView) view.findViewById(R.id.studentlist);
        ScrollView sView = (ScrollView) view.findViewById(R.id.scrollView);
        initAdapter();
        sView.smoothScrollTo(0,0);
    }

    public void initAdapter(){
        int size = 0;
        if(teacherList != null){
            size = teacherList.size();
            System.out.println("老師人數:" + size);
            if(size > 0) {
                Message msg = new Message();
                msg.what = LOAD_ALL_TEACHER;
                Bundle bundle = new Bundle();
                bundle.putSerializable("teacher", (Serializable) teacherList);
                msg.setData(bundle);
                mHandler.sendMessageDelayed(msg,500);
            }
        }
        if(studentList != null) {
            size = studentList.size();
            System.out.println("学生人数:"+size);
            Message msg = new Message();
            msg.what = LOAD_ALL_STUDENT;
            Bundle bundle = new Bundle();
            if (studentList != null) {
                bundle.putSerializable("student", (Serializable) studentList);
            }
            msg.setData(bundle);
            mHandler.sendMessageDelayed(msg,500);
        }
    }

    public void setTeacher(List<NoticeBrowserBean.ResultMap> teacherList){
        teacherAdapter = new BrowserAdapter(teacherList);
        teacher.setAdapter(teacherAdapter);
    }

    public void setStudent(List<NoticeBrowserBean.ResultMap> studentList){
        studentAdapter = new BrowserAdapter(studentList);
        System.out.println(studentAdapter.getCount());
        student.setAdapter(studentAdapter);
    }

    private void addStudent(final List<NoticeBrowserBean.ResultMap> studentList){
        new Thread(new Runnable() {
            @Override
            public void run() {
                studentAdapter.addItem(studentList);
                Message message = new Message();
                message.what = LOAD_ALL_STUDENT_FINISH;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    class BrowserAdapter extends BaseAdapter{
        List<NoticeBrowserBean.ResultMap> list;
        public BrowserAdapter(List<NoticeBrowserBean.ResultMap> list){
            this.list = list;
        }
        @Override
        public int getCount() {
            if(list != null) {
                return list.size();
            }
            return 0;
        }
        public void addItem(List<NoticeBrowserBean.ResultMap> left){
            list.addAll(left);
        }
        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = mContext.getLayoutInflater().inflate(R.layout.activity_notify_browser_item, null);
                holder.textView = (TextView) convertView.findViewById(R.id.item);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            NoticeBrowserBean.ResultMap item = list.get(position);
            if (item.getIsRead() == 0) {
                holder.textView.setTextColor(mContext.getResources().getColor(R.color.font_money_color));
            }
            holder.textView.setText(item.getUserName());
            return convertView;
        }

        class ViewHolder{
            TextView textView;
        }
    }

    private List<NoticeBrowserBean.ResultMap>
    getBrowserList(List<NoticeBrowserBean.ResultMap> list,int type){
        if(list == null) return null;
        List<NoticeBrowserBean.ResultMap> result = new ArrayList<NoticeBrowserBean.ResultMap>();
        for(NoticeBrowserBean.ResultMap item:list){
            if(item.getUserType() == type) {
                result.add(item);
            }
        }
        return result;
    }

    private SpannableString getListString(List<NoticeBrowserBean.ResultMap> list,int type){
        String result = "";
        SpannableString sp = null;
        List<Pos> posList =  new ArrayList<Pos>();
        int pos = 0;
        if(list == null) return sp;
        for(NoticeBrowserBean.ResultMap item:list){
            if(item.getUserType() == type) {
                String name = item.getUserName();
                result += name;
                result += ",";
                if(item.getIsRead() == 0){
                    Pos posItem = new Pos();
                    posItem.start = pos;
                    posItem.end = pos + name.length();
                    posList.add(posItem);
                }
                pos += name.length() + 1;
            }
        }
        if(!DataUtil.isNullorEmpty(result)){
            sp = new SpannableString(result);

            for(Pos posItem:posList){
                ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
                sp.setSpan(span,posItem.start,posItem.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return sp;
    }
}
