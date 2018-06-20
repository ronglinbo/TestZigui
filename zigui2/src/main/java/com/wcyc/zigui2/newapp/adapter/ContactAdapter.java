/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wcyc.zigui2.newapp.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.MemberBean;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.widget.RoundImageView;
import com.wcyc.zigui2.widget.Sidebar;

/**
 * 简单的好友Adapter实现
 * 
 */
public class ContactAdapter extends ArrayAdapter<User> implements
		SectionIndexer {

	private LayoutInflater layoutInflater;
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;
	NewMemberBean memberInfo = CCApplication.app.getMemberInfo();
//	private Sidebar sidebar;
	private int res;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	Context context;
	public ContactAdapter(Context context, int resource, List<User> objects,
			Sidebar sidebar) {
		super(context, resource, objects);
		this.res = resource;
//		this.sidebar = sidebar;
		this.context=context;
		
		layoutInflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.pho_touxiang)
				.showImageOnFail(R.drawable.pho_touxiang).resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnLoading(R.drawable.pho_touxiang)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		
        imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? 0 : 1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(res, null);
			holder.avatar = (RoundImageView) convertView
					.findViewById(R.id.avatar);
			holder.nameTextview = (TextView) convertView
					.findViewById(R.id.name);
			holder.tvHeader = (TextView) convertView
					.findViewById(R.id.header);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		new Thread(new Runnable(){
			public void run(){
				setView(holder,position);
			}
		}).run();
		return convertView;
	}
	
	private void setView(ViewHolder holder,int position) {
		User user = getItem(position);
		String ss = user.toString();
		System.out.println(ss+"------------"+user.getClassName());
		String header = user.getHeader();
		String avatarUrl = user.getAvatar();
		if (position == 0 || header != null
				&& !header.equals(getItem(position - 1).getHeader())) {
			if ("".equals(header)) {
//				holder.tvHeader.setVisibility(View.GONE);
				if(position == 0 && user.getGroup() == 1){
					holder.tvHeader.setVisibility(View.VISIBLE);
//					holder.tvHeader.setText("班级群");
					holder.tvHeader.setText("群");
				}else{
					holder.tvHeader.setVisibility(View.GONE);
				}
			} else {
				holder.tvHeader.setVisibility(View.VISIBLE);
				holder.tvHeader.setText(header);
			}
		} else {
			holder.tvHeader.setVisibility(View.GONE);
		}
		
		//web端传过来的数据 在后面加上这个称号
		holder.nameTextview.setText(user.getNick());
		
		if(user.getGroup() == 0){
			String url = (String) holder.avatar.getTag();
			if( url ==null || !url.equals(avatarUrl)){
				holder.avatar.setTag(avatarUrl);
				System.out.println("url:"+url + "avatarUrl:"+avatarUrl+":end");
				//4.4版本以下会有OOM的问题
				if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
					imageLoader.displayImage(avatarUrl, holder.avatar, options);
					/*if(avatarUrl != null && !avatarUrl.isEmpty()){
						Bitmap mp = ImageUtils.getHttpBitmap(avatarUrl);
						if(mp != null){
							holder.avatar.setImageBitmap(mp);
						}
					}*/
				}
			}
		}else{
			holder.avatar.setImageResource(R.drawable.groups_icon);
		}
	}
	private static class ViewHolder {
		/** 联系人名字 */
		TextView nameTextview;
		/** 头字符 */
		TextView tvHeader;
		/** 用户头像 */
		RoundImageView avatar;
	}
	public int getPositionForSection(int section) {
		return positionOfSection.get(section);
	}

	public int getSectionForPosition(int position) {
		return sectionOfPosition.get(position);
	}

	@Override
	public Object[] getSections() {
		positionOfSection = new SparseIntArray();
		sectionOfPosition = new SparseIntArray();
		int count = getCount();
		List<String> list = new ArrayList<String>();
		list.add("#");
		positionOfSection.put(0, 0);
		sectionOfPosition.put(0, 0);
		for (int i = 1; i < count; i++) {

			String letter = getItem(i).getHeader();
			System.err.println("contactadapter getsection getHeader:" + letter
					+ " name:" + getItem(i).getUsername());
			int section = list.size() - 1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				positionOfSection.put(section, i);
			}
			sectionOfPosition.put(i, section);
		}
		return list.toArray(new String[list.size()]);
	}

}
