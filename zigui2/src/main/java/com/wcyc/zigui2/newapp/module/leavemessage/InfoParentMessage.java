package com.wcyc.zigui2.newapp.module.leavemessage;

import com.wcyc.zigui2.newapp.bean.ServerResult;

import java.util.List;

/**
 * Created by 章豪 on 2017/8/15.
 */

public class InfoParentMessage {

    private List<LeaveMessage> parentMessageBoardList;
    private int pages;
    private ServerResult serverResult;

    public List<LeaveMessage> getParentMessageBoardList() {
        return parentMessageBoardList;
    }

    public void setParentMessageBoardList(List<LeaveMessage> parentMessageBoardList) {
        this.parentMessageBoardList = parentMessageBoardList;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public ServerResult getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResult serverResult) {
        this.serverResult = serverResult;
    }
}
