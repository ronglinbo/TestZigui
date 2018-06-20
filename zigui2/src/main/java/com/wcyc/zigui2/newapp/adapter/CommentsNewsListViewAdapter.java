package com.wcyc.zigui2.newapp.adapter;

import java.util.List;
import java.util.Map;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.NewSchoolNewsCommentBean;
import com.wcyc.zigui2.bean.PointBean;
import com.wcyc.zigui2.newapp.bean.NewPointBean;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 新闻评论
 * 
 * @author 郑国栋 2016-7-1
 * @version 2.1
 */

public class CommentsNewsListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<NewSchoolNewsCommentBean> data_list;

	public CommentsNewsListViewAdapter(Context mContext,
			List<NewSchoolNewsCommentBean> data_list) {
		super();
		this.mContext = mContext;
		this.data_list = data_list;
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
		// TextView tv_content ,tv_name = null;
		ViewHolder viewholder = null;
		// System.out.println("position:"+position
		// +" size:"+data_list.size()+" list:"+data_list.get(position));
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.new_school_news_comment, parent, false);
			viewholder.content = (TextView) convertView
					.findViewById(R.id.content);
			viewholder.name = (TextView) convertView
					.findViewById(R.id.authorname);
			viewholder.authorname_reply_tv = (TextView) convertView
					.findViewById(R.id.authorname_reply_tv);
			viewholder.authorname_to_authorname_tv = (TextView) convertView
					.findViewById(R.id.authorname_to_authorname_tv);
			viewholder.news_comment_icon=(ImageView)convertView.findViewById(R.id.news_comment_icon);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		// String name = (String) data_list.get(position).getAuthorname();
		String name;
		String content = data_list.get(position).getContent();
		String comment_user_name=data_list.get(position).getComment_user_name();
		String point_comment_user=data_list.get(position).getPoint_comment_user();

		if (TextUtils.isEmpty(point_comment_user)) {
			name = comment_user_name + ":";
			viewholder.name.setText(name);

			viewholder.authorname_reply_tv.setVisibility(View.GONE);
			viewholder.authorname_to_authorname_tv.setVisibility(View.GONE);
		} else {
			viewholder.authorname_reply_tv.setVisibility(View.VISIBLE);
			viewholder.authorname_to_authorname_tv.setVisibility(View.VISIBLE);

			viewholder.name.setText(comment_user_name);
			viewholder.authorname_to_authorname_tv.setText(point_comment_user+":");
			name = comment_user_name + "回复" + point_comment_user+":";

		}
		
		try {
			int a = name.getBytes("UTF-8").length;// utf-8占3个字节
			int b = name.length();// 两个字符直接有空隙
			String null_a = "";
			for (int i = 0; i < a + b + 2; i++) {// +2 考虑到字符前后 +3回复评论时，加一个utf-8的长度 用：@时
				null_a += " ";// 空格个数
			}
			viewholder.content.setText(null_a + content);// 相对布局中 内容前面空 N个空格 N是为了显示名字

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(position<1){
			viewholder.news_comment_icon.setVisibility(View.VISIBLE);
		}else{
			viewholder.news_comment_icon.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView content;
		TextView name;
		TextView authorname_reply_tv;
		TextView authorname_to_authorname_tv;
		ImageView news_comment_icon;
	}

	// 添加更多数据
	public void addItem(List<NewSchoolNewsCommentBean> i) {
		 data_list.addAll(i);
	}
}