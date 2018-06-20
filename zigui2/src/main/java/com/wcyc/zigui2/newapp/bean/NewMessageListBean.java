package com.wcyc.zigui2.newapp.bean;

import java.util.List;

public class NewMessageListBean extends NewBaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1371642877711315005L;

	private List <NewMessageBean> messageList;
	public List <NewMessageBean> getMessageList() {
		return messageList;
	}
	public void setMessageList(List <NewMessageBean> messageList) {
		this.messageList = messageList;
	}
	public void addMessage(NewMessageBean item){
		messageList.add(item);
	}
	public void delMessage(NewMessageBean item){
		messageList.remove(item);
	}
}