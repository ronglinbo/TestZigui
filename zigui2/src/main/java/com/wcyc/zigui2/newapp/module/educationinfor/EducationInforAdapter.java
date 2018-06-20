package com.wcyc.zigui2.newapp.module.educationinfor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.utils.DataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 教育资讯Adapter
 * 
 * @author 郑国栋 201703-02
 * @version 2.0.12
 */
public class EducationInforAdapter extends BaseAdapter {

	private Context myContext;// 上下文
	private ArrayList<EducationInforBean> educationInforList;// list

	public EducationInforAdapter(Context myContext,
			ArrayList<EducationInforBean> educationInforList) {
		super();
		this.myContext = myContext;
		this.educationInforList = educationInforList;
	}

	@Override
	public int getCount() {

		if (educationInforList != null) {

			return educationInforList.size();// 长度
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
					R.layout.education_infor_item, parent, false);

			// 获得布局中的控件
			viewholder.education_iv = (ImageView) convertView
					.findViewById(R.id.education_iv);// 图片

			viewholder.education_title_tv = (TextView) convertView
					.findViewById(R.id.education_title_tv);// 标题
			viewholder.education_content_tv = (TextView) convertView
					.findViewById(R.id.education_content_tv);// 内容
			viewholder.education_time_tv = (TextView) convertView
					.findViewById(R.id.education_time_tv);// 发布时间
			viewholder.education_read_numb_tv = (TextView) convertView
					.findViewById(R.id.education_read_numb_tv);// 浏览次数
			viewholder.unread_iv = (ImageView) convertView
					.findViewById(R.id.unread_iv);// 未读图片
			// 设置标签
			convertView.setTag(viewholder);

		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}

		// 显示图片
		String pictureUrls = educationInforList.get(position).getPictureUrls();
		if (!DataUtil.isNullorEmpty(pictureUrls)) {
			try {
				JSONObject json = new JSONObject(pictureUrls);
				String imgUrl = json.getString("imgUrl");
				if(imgUrl.contains("downloadApi?fileId=")){
					imgUrl+="&sf=150*150";
				}
				((BaseActivity) myContext).getImageLoader().displayImage(
						imgUrl, viewholder.education_iv,
						((BaseActivity) myContext).getImageLoaderOptions());

				viewholder.education_iv.setVisibility(View.VISIBLE);
			} catch (JSONException e) {

				e.printStackTrace();
			}
		} else {
			viewholder.education_iv.setVisibility(View.GONE);
		}

		viewholder.education_title_tv.setText(educationInforList.get(position)
				.getTitle());
		viewholder.education_content_tv.setText(educationInforList.get(
				position).getContent());
		String publishTime=educationInforList.get(position).getPublishTime();

		//时间  取年月日，不要时分秒
		if(!DataUtil.isNullorEmpty(publishTime)){
			publishTime=publishTime.substring(0,publishTime.indexOf(" "));
		}
		viewholder.education_time_tv.setText(publishTime);
		viewholder.education_read_numb_tv.setText(educationInforList.get(
				position).getBrowseNo()+"");

		//已读未读   为1已读，为0未读
		String isRead=educationInforList.get(position).getIsRead();
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
		ImageView education_iv,unread_iv;
		TextView education_title_tv, education_content_tv,
				education_time_tv, education_read_numb_tv;

	}

	// 添加数据
	public void addItem(ArrayList<EducationInforBean> i) {
		educationInforList.addAll(i);
	}

}
