package com.wcyc.zigui2.newapp.adapter;

import java.util.List;
import java.util.Map;

import com.wcyc.zigui2.R;
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
 * 班级动态评论
 * @author xiehua
 * @version
 * @since 20151222
 */
public class CommentsListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<NewPointBean> data_list;

	public CommentsListViewAdapter(Context mContext,
			List<NewPointBean> data_list) {
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
					R.layout.class_dynamics_point, parent, false);
			viewholder.content = (TextView) convertView
					.findViewById(R.id.content);
			viewholder.name = (TextView) convertView
					.findViewById(R.id.authorname);
			viewholder.authorname_reply_tv = (TextView) convertView
					.findViewById(R.id.authorname_reply_tv);
			viewholder.authorname_to_authorname_tv = (TextView) convertView
					.findViewById(R.id.authorname_to_authorname_tv);
			viewholder.message_support_count_icon = (ImageView) convertView
					.findViewById(R.id.message_support_count_icon);

			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		// String name = (String) data_list.get(position).getAuthorname();
		String name;
		String content = data_list.get(position).getContent();
		String commentUserName=data_list.get(position).getCommentUserName();
		String toCommentUserName=data_list.get(position).getToCommentUserName();

		if (TextUtils.isEmpty(toCommentUserName)) {
			name = commentUserName + ":";
			viewholder.name.setText(name);

			viewholder.authorname_reply_tv.setVisibility(View.GONE);
			viewholder.authorname_to_authorname_tv.setVisibility(View.GONE);
		} else {
			viewholder.authorname_reply_tv.setVisibility(View.VISIBLE);
			viewholder.authorname_to_authorname_tv.setVisibility(View.VISIBLE);
			viewholder.name.setText(commentUserName);
			viewholder.authorname_to_authorname_tv.setText(toCommentUserName+":");
			name = commentUserName + "回复" + toCommentUserName+":";
		}
		
		try {
			int a = name.getBytes("UTF-8").length;// utf-8占3个字节
			int b = name.length();// 两个字符之间有空隙
			String null_a = "";
			for (int i = 0; i < a + b + 2; i++) {// +2 考虑到字符前后 +3回复评论时，加一个utf-8的长度  用：@时
				null_a += " ";// 空格个数
			}
			viewholder.content.setText(null_a + content);// 相对布局中 内容前面空 N个空格  N是为了显示名字

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (position < 1) {
			viewholder.message_support_count_icon.setVisibility(View.VISIBLE);
		} else {
			viewholder.message_support_count_icon.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView content;
		TextView name;
		TextView authorname_reply_tv;
		TextView authorname_to_authorname_tv;
		ImageView message_support_count_icon;
	}
}