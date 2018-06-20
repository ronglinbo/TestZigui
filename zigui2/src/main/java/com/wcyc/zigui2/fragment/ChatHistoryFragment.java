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
package com.wcyc.zigui2.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.adapter.ChatHistoryAdapter;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.chat.ChatActivity;
import com.wcyc.zigui2.chat.ContactlistActivity;
import com.wcyc.zigui2.chat.MainActivity;
import com.wcyc.zigui2.chat.SlideListView;
import com.wcyc.zigui2.core.CCApplication;

/**
 * 聊天记录Fragment
 * 
 */
public class ChatHistoryFragment extends Fragment {

	private InputMethodManager inputMethodManager;
	private SlideListView listView;
	private Map<String, User> contactList;
	private ChatHistoryAdapter adapter;
	private ImageButton clearSearch;
	public RelativeLayout errorItem;
	public RelativeLayout goContactsList;
	public TextView errorText , tv_no_message;
	private boolean hidden;
	private Context mContext;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_conversation_history, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
		goContactsList = (RelativeLayout) getView().findViewById(R.id.goContactsList);
		goContactsList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goContactsList(v);
			}
		});
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
		//没有消息时的显示
		tv_no_message = (TextView) getView().findViewById(R.id.tv_no_message);
		// contact list
		contactList = CCApplication.getInstance().getContactList();
		listView = (SlideListView) getView().findViewById(R.id.list);
		adapter = new ChatHistoryAdapter(getActivity(), 1, loadUsersWithRecentChat());
		// 设置adapter
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User user = adapter.getItem(position);
				if (adapter.getItem(position).getUsername().equals(CCApplication.getInstance().getUserName()))
					Toast.makeText(getActivity(), "不能和自己聊天", Toast.LENGTH_SHORT).show();
				else {
					// 进入聊天页面
					  Intent intent = new Intent(getActivity(), ChatActivity.class);
//					 if (emContact instanceof EMGroup) {
					  if (user.getGroup()==1) {
		                    //it is group chat
		                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
//		                    intent.putExtra("groupId", ((EMGroup) emContact).getGroupId());
							intent.putExtra("groupId", user.getUsername());
		                } else {
//		                    User user = (User) emContact;
							intent.putExtra("userId", user.getUsername());
							intent.putExtra("userNick", user.getNick());
							// 接收者头像
							intent.putExtra("avatar", user.getAvatar());
		                }
					startActivity(intent);
				}
			}
		});
		//设置删除按钮监听
		listView.setDelButtonClickListener(new com.wcyc.zigui2.chat.SlideListView.DelButtonClickListener()
		{
			@Override
			public void clickHappend(final int position)
			{
				User tobeDeleteUser = adapter.getItem(position);
				// 删除此会话    注释下面这行是表示保留历史记录
				//删除掉后 点回贵友圈又会重现
				EMChatManager.getInstance().deleteConversation(tobeDeleteUser.getUsername());
				
				adapter.remove(tobeDeleteUser);
				adapter.notifyDataSetChanged();

				//如果没有消息那么显示没有消息的界面
				if(adapter.isEmpty()){
					tv_no_message.setVisibility(View.VISIBLE);
				}else{
					tv_no_message.setVisibility(View.GONE);
				}
				
				// 更新消息未读数
				((MainActivity) getActivity()).updateUnreadLabel();
			}
		});
		// 注册上下文菜单
//		registerForContextMenu(listView);

		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getActivity().getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
		getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
		// }
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_message) {
			User tobeDeleteUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除此会话
			EMChatManager.getInstance().deleteConversation(tobeDeleteUser.getUsername());
			adapter.remove(tobeDeleteUser);
			adapter.notifyDataSetChanged();

			// 更新消息未读数
			((MainActivity) getActivity()).updateUnreadLabel();

			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 刷新页面.
	 */
	public void refresh() {
		adapter = new ChatHistoryAdapter(getActivity(), R.layout.row_chat_history, loadUsersWithRecentChat());
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 获取有聊天记录的users和groups
	 * 
	 * @return 用户列表
	 */
	private List<User> loadUsersWithRecentChat() {
		List<User> resultList = new ArrayList<User>();
		
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		//过滤掉messages seize为0的conversation
		for(EMConversation conversation : conversations.values()){
			if(conversation.getAllMessages().size() != 0){
				String userName = conversation.getUserName();
				if(contactList.containsKey(userName)){
					resultList.add(contactList.get(userName));
				}else{
					User user = new User();
					user.setUsername(userName);
					user.setNick("未知");
					resultList.add(user);
					//如果是我们自己的服务器没有的联系人 那么删除该条信息
					// 删除此会话
//					EMChatManager.getInstance().deleteConversation(userName);
					// 更新消息未读数
//					((MainActivity) getActivity()).updateUnreadLabel();
				}
			}
		}
//		if (contactList != null) {
//			for (User user : contactList.values()) {
//				EMConversation conversation = EMChatManager.getInstance().getConversation(user.getUsername());
//				if (conversation.getMsgCount() > 0) {
//					resultList.add(user);
//				}
//			}
//
//			// 排序
//			sortUserByLastChatTime(resultList);
//		}
		// 排序
		sortUserByLastChatTime(resultList);
		//如果没有消息那么显示没有消息的界面
		if(resultList.size() < 1){
			tv_no_message.setVisibility(View.VISIBLE);
		}else{
			tv_no_message.setVisibility(View.GONE);
		}
		return resultList;
	}

	/**
	 * 依据最后一条消息的时间排序.
	 * 
	 * @param contactList 联系人列表
	 */
	private void sortUserByLastChatTime(List<User> contactList) {
		Collections.sort(contactList, new Comparator<EMContact>() {
			@Override
			public int compare(final EMContact user1, final EMContact user2) {
				EMConversation conversation1 = EMChatManager.getInstance().getConversation(user1.getUsername());
				EMConversation conversation2 = EMChatManager.getInstance().getConversation(user2.getUsername());

				EMMessage user2LastMessage = conversation2.getLastMessage();
				EMMessage user1LastMessage = conversation1.getLastMessage();
				if (user2LastMessage.getMsgTime() == user1LastMessage.getMsgTime()) {
					return 0;
				} else if (user2LastMessage.getMsgTime() > user1LastMessage.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
		}
	}
	public void goContactsList(View view){
		
		Intent intent = new Intent(getActivity(), ContactlistActivity.class);
		startActivity(intent);
	}
	
}
