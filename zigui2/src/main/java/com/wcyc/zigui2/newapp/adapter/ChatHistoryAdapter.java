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

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.utils.SmileUtils;

/**
 * 聊天记录adpater
 * 
 */
public class ChatHistoryAdapter extends ArrayAdapter<User> {

	private LayoutInflater inflater;
	private	ImageLoader imageLoader;
	private	DisplayImageOptions options;

	public ChatHistoryAdapter(Context context, int textViewResourceId,
			List<User> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.defaulticon)
				.showImageOnFail(R.drawable.defaulticon).resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnLoading(R.drawable.defaulticon)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_chat_history, parent,
					false);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.unreadLabel = (TextView) convertView
					.findViewById(R.id.unread_msg_number);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.msgState = convertView.findViewById(R.id.msg_state);
			holder.list_item_layout = (RelativeLayout) convertView
					.findViewById(R.id.list_item_layout);
			convertView.setTag(holder);
		}
//		holder.list_item_layout
//		.setBackgroundResource(R.drawable.mm_listitem);
		User user = getItem(position);
		setAvatar(user , holder);


		String username = user.getUsername();
		// 获取与此用户/群组的会话
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(username);
		//只有不是群消息的时候才加上班级名字
		if(user.getGroup() != 1){
			holder.name.setText(user.getNick() != null ? (user.getNick() + "-" + user.getClassName()) : (username+ user.getClassName()));
		}else{
			holder.name.setText(user.getNick() != null ? (user.getNick()) : (username));
		}
		if (conversation.getUnreadMsgCount() > 0) {
			// 显示与此用户的消息未读数
			holder.unreadLabel.setText(String.valueOf(conversation
					.getUnreadMsgCount()));
			holder.unreadLabel.setVisibility(View.VISIBLE);
		} else {
			holder.unreadLabel.setVisibility(View.INVISIBLE);
		}

		if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			holder.message
					.setText(
							SmileUtils.getSmiledText(
									getContext(),
									getMessageDigest(lastMessage,
											(this.getContext()))),
							BufferType.SPANNABLE);
			holder.time.setText(DateUtils.getTimestampString(new Date(
					lastMessage.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND
					&& lastMessage.status == EMMessage.Status.FAIL) {
				holder.msgState.setVisibility(View.VISIBLE);
			} else {
				holder.msgState.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	private void setAvatar(User user, ViewHolder holder) {
		if(user.getGroup() == 0){
			String url = (String) holder.avatar.getTag();
			String avatarUrl = user.getAvatar();
			if( url ==null || !url.equals(avatarUrl)){
				holder.avatar.setTag(avatarUrl);
				imageLoader.displayImage(avatarUrl, holder.avatar, options);
			}
		}else{
			holder.avatar.setImageResource(R.drawable.groups_icon);
		}
	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			// if (message.direct == EMMessage.Direct.RECEIVE) {
			// //从sdk中提到了ui中，使用更简单不犯错的获取string方法
			// // digest = EasyUtils.getAppResourceString(context,
			// "location_recv");
			// digest = getString(context, R.string.location_recv);
			// digest = String.format(digest, message.getFrom());
			// return digest;
			// } else {
			// // digest = EasyUtils.getAppResourceString(context,
			// "location_prefix");
			// digest = getString(context, R.string.location_prefix);
			// }
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
//			digest = getString(context, R.string.picture)
//					+ imageBody.getFileName();
			//在消息列表界面中，图片消息不显示图片路径
			digest = getString(context, R.string.picture);
			break;
		case VOICE:// 语音消息
			digest = getString(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getString(context, R.string.video);
			break;
		case TXT: // 文本消息
			TextMessageBody txtBody = (TextMessageBody) message.getBody();
			digest = txtBody.getMessage();
			break;
		case FILE: // 普通文件消息
			digest = getString(context, R.string.file);
			break;
		default:
			System.err.println("error, unknow type");
			return "";
		}

		return digest;
	}

	private static class ViewHolder {
		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		ImageView avatar;
		/** 最后一条消息的发送状态 */
		View msgState;
		/** 整个list中每一行总布局 */
		RelativeLayout list_item_layout;

	}

	String getString(Context context, int resId) {
		return context.getResources().getString(resId);
	}
}
