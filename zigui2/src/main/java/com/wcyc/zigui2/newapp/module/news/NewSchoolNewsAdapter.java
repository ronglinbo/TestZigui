package com.wcyc.zigui2.newapp.module.news;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.utils.DataUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 校园新闻Adapter
 * 
 * @author 郑国栋 2016-6-23
 * @version 2.1
 */
public class NewSchoolNewsAdapter extends BaseAdapter {

	private Context myContext;// 上下文
	private ArrayList<NewSchoolNewsBean> newSchoolNewsList;// 校园新闻list

	public NewSchoolNewsAdapter(Context myContext,
			ArrayList<NewSchoolNewsBean> newSchoolNewsList) {
		super();
		this.myContext = myContext;
		this.newSchoolNewsList = newSchoolNewsList;
	}

	@Override
	public int getCount() {

		if (newSchoolNewsList != null) {

			return newSchoolNewsList.size();// 长度
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return position;// 当前位置ID
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		if (convertView == null) {
			// 实例化控件
			viewholder = new ViewHolder();
			// 配置单个item的布局
			convertView = LayoutInflater.from(myContext).inflate(
					R.layout.new_school_news_item, parent, false);
			// 获得布局中的控件
			viewholder.school_news_url_iv = (ImageView) convertView
					.findViewById(R.id.school_news_url_iv);// 图片
			viewholder.school_news_title_tv = (TextView) convertView
					.findViewById(R.id.school_news_title_tv);// 校园新闻标题
			viewholder.school_news_content_tv = (TextView) convertView
					.findViewById(R.id.school_news_content_tv);// 校园新闻内容
			viewholder.school_news_time_tv = (TextView) convertView
					.findViewById(R.id.school_news_time_tv);// 校园新闻发布时间
			viewholder.school_news_read_numb_tv = (TextView) convertView
					.findViewById(R.id.school_news_read_numb_tv);// 校园新闻浏览次数
			viewholder.unread_iv = (ImageView) convertView
					.findViewById(R.id.unread_iv);// 未读图片

			// 设置标签
			convertView.setTag(viewholder);

		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}

		// 显示图片
		String pictureUrls = newSchoolNewsList.get(position).getPictureUrls();
		if (!DataUtil.isNullorEmpty(pictureUrls)) {
			try {
				JSONObject json = new JSONObject(pictureUrls);
				String imgUrl = json.getString("imgUrl");
				if(imgUrl.contains("downloadApi?fileId=")){
					imgUrl+="&sf=150*150";
				}
				((BaseActivity) myContext).getImageLoader().displayImage(
						imgUrl, viewholder.school_news_url_iv,
						((BaseActivity) myContext).getImageLoaderOptions());

				viewholder.school_news_url_iv.setVisibility(View.VISIBLE);
			} catch (JSONException e) {

				e.printStackTrace();
			}
		} else {
			viewholder.school_news_url_iv.setVisibility(View.GONE);
		}

		viewholder.school_news_title_tv.setText(newSchoolNewsList.get(position)
				.getTitle());
		viewholder.school_news_content_tv.setText(newSchoolNewsList.get(
				position).getContent());
		String publishTime=newSchoolNewsList.get(position).getPublishTime();

		//时间  取年月日，不要时分秒
		publishTime=publishTime.substring(0,publishTime.indexOf(" "));
		viewholder.school_news_time_tv.setText(publishTime);
		viewholder.school_news_read_numb_tv.setText(newSchoolNewsList.get(
				position).getBrowseNo()+"");

		//已读未读   为1已读，为0未读
		String isRead=newSchoolNewsList.get(position).getIsRead();
		if("0".equals(isRead)){
			viewholder.unread_iv.setVisibility(View.VISIBLE);
		}else if("1".equals(isRead)){
			viewholder.unread_iv.setVisibility(View.GONE);
		}else {
			viewholder.unread_iv.setVisibility(View.GONE);
		}

		return convertView;
	}

	private class ViewHolder {
		//
		ImageView school_news_url_iv,unread_iv;
		TextView school_news_title_tv, school_news_content_tv,
				school_news_time_tv, school_news_read_numb_tv;

	}

	// 添加数据
	public void addItem(ArrayList<NewSchoolNewsBean> i) {
		newSchoolNewsList.addAll(i);
	}

}
