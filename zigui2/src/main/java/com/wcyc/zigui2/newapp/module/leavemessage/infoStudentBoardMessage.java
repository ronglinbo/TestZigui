package com.wcyc.zigui2.newapp.module.leavemessage;

import com.wcyc.zigui2.newapp.bean.ServerResult;

import java.util.List;

/**
 * Created by 章豪 on 2017/8/14.
 */

public class infoStudentBoardMessage {
    private List<ChildMessage> infoStudentBoardMessageList;
    private ServerResult serverResult;
    private int totalPageNum;

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public List<ChildMessage> getInfoStudentBoardMessageList() {
        return infoStudentBoardMessageList;
    }

    public void setInfoStudentBoardMessageList(List<ChildMessage> infoStudentBoardMessageList) {
        this.infoStudentBoardMessageList = infoStudentBoardMessageList;
    }

    public ServerResult getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResult serverResult) {
        this.serverResult = serverResult;
    }
}
