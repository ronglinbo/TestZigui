package com.wcyc.zigui2.newapp.module.notice;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiehua on 2016/11/14.
 */

public class NoticeBrowserBean extends NewBaseBean {
    public int getBrowseNumber() {
        return browseNumber;
    }

    public void setBrowseNumber(int browseNumber) {
        this.browseNumber = browseNumber;
    }

    public List<ResultMap> getResultMap() {
        return resultMap;
    }

    public void setResultMap(List<ResultMap> resultMap) {
        this.resultMap = resultMap;
    }

    class ResultMap implements Serializable {
        private int userType;//1学生 2 教师
        private String userName;//姓名
        private int isRead;//0 未读 1已读

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }
    }
    private int browseNumber;//浏览人数
    private List<ResultMap> resultMap;
}
