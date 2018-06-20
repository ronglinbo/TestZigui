package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.view.View;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.wcyc.zigui2.bean.User;

public class AllMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8732599535782058261L;
	//消息列表
	private NewMessageListBean message; 
	private AllContactListBean contactList;
	/**
	 * 获取有聊天记录的users和groups
	 * 
	 * @return 用户列表
	 */
	private List<NewMessageBean> loadUsersWithRecentChat() {
		List<NewMessageBean> resultList = new ArrayList<NewMessageBean>();
		int size = contactList.getClassList().size();
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		//过滤掉messages seize为0的conversation
		for(EMConversation conversation : conversations.values()){
			if(conversation.getAllMessages().size() != 0){
				String userName = conversation.getUserName();
				System.out.println("userName:"+userName);
				//if(contactList.containsKey(userName)){
				
				for(int i = 0; i < size; i++){
					ClassList classlist = contactList.getClassList().get(i);
					List<ContactsList> list = classlist.getContactsList();
					String classID = classlist.getClassID();
					String className = classlist.getClassName();
					int num = list.size();
					for(int j = 0; j < num ;j++){
						ContactsList contact = list.get(j);
						String name = contact.getUserName();
						NewMessageBean user = new NewMessageBean();
//						user.setUsername(userName);
						user.setMessageType(userName);
						
						String course = contact.getCourse();
						String identity = contact.getUserIdentity();
						if(userName.equals(contact.getUserName())){	
//							user.setNick(contact.getNickName());
//							user.setCellphone(contact.getCellphone());
//							user.setAvatar(contact.getUserIconURL());
//							user.setClassID(classID);
//							user.setClassName(className);
//							user.setCourse(course);
//							user.setHeader(identity);
							if(conversation.getMsgCount() > 0)
								resultList.add(user);
						}else{
//							user.setNick("未知");
							//如果是我们自己的服务器没有的联系人 那么删除该条信息
							// 删除此会话
							EMChatManager.getInstance().deleteConversation(userName);
						}
						
					}
				}
			}
		}
		
//		sortUserByLastChatTime(resultList);
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
				if(user2LastMessage != null && user1LastMessage != null){
					if (user2LastMessage.getMsgTime() == user1LastMessage.getMsgTime()) {
						return 0;
					} else if (user2LastMessage.getMsgTime() > user1LastMessage.getMsgTime()) {
						return 1;
					} else {
						return -1;
					}
				}
				return -1;
			}

		});
	}
}