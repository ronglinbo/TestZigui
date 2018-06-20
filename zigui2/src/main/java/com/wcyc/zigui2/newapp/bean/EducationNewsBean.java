package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzc
 * @time 2017/12/6 0006
 */
public class EducationNewsBean implements Serializable{


    /**
     * browseNum : 0
     * campuEcirclesList : [{"browseNo":202,"cover":"1790574","id":115,"isRead":"1","operatorTime":"2018-01-03 15:05:52","publishTime":"2018-01-03 14:53:24","title":"Test5","topTime":"2018-01-03 15:05:52","top_status":true},{"browseNo":31,"cover":"1790570","id":111,"isRead":"1","operatorTime":"2018-01-03 17:02:43","publishTime":"2018-01-03 11:29:43","title":"Test1","top_status":true},{"browseNo":1,"cover":"1790589","id":130,"isRead":"0","operatorTime":"2018-01-03 15:51:35","publishTime":"2018-01-03 15:51:35","title":"Test20","topTime":"2018-01-03 15:54:02","top_status":false},{"browseNo":2,"cover":"1790588","id":129,"isRead":"1","operatorTime":"2018-01-03 16:14:27","publishTime":"2018-01-03 16:14:27","title":"Test19","topTime":"2018-01-03 15:53:46","top_status":false},{"cover":"1790587","id":128,"isRead":"1","operatorTime":"2018-01-03 16:14:24","publishTime":"2018-01-03 16:14:24","title":"Test18","topTime":"2018-01-03 15:53:31","top_status":false},{"cover":"11526","id":117,"isRead":"0","operatorTime":"2018-01-03 15:47:47","publishTime":"2018-01-03 15:47:47","title":"test07","topTime":"2018-01-03 15:53:15","top_status":false},{"cover":"1790586","id":127,"isRead":"1","operatorTime":"2018-01-03 16:14:20","publishTime":"2018-01-03 16:14:20","title":"Test17","topTime":"2018-01-03 15:52:56","top_status":false},{"cover":"1790585","id":126,"isRead":"1","operatorTime":"2018-01-03 16:14:17","publishTime":"2018-01-03 16:14:17","title":"Test16","topTime":"2018-01-03 15:52:44","top_status":false},{"browseNo":1,"cover":"1790584","id":125,"isRead":"1","operatorTime":"2018-01-03 16:14:13","publishTime":"2018-01-03 16:14:13","title":"Test15","topTime":"2018-01-03 15:52:31","top_status":false},{"browseNo":1,"cover":"1790583","id":124,"isRead":"1","operatorTime":"2018-01-03 16:14:09","publishTime":"2018-01-03 16:14:09","title":"Test14","topTime":"2018-01-03 15:52:18","top_status":false}]
     * interactiveCount : 0
     * pageNum : 1
     * pageSize : 10
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     * totalPageNum : 3
     */

    private int browseNum;
    private int interactiveCount;
    private int pageNum;
    private int pageSize;
    private ServerResultBean serverResult;
    private int totalPageNum;
    private List<CampuEcirclesListBean> campuEcirclesList;

    public int getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(int browseNum) {
        this.browseNum = browseNum;
    }

    public int getInteractiveCount() {
        return interactiveCount;
    }

    public void setInteractiveCount(int interactiveCount) {
        this.interactiveCount = interactiveCount;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public ServerResultBean getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResultBean serverResult) {
        this.serverResult = serverResult;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public List<CampuEcirclesListBean> getCampuEcirclesList() {
        return campuEcirclesList;
    }

    public void setCampuEcirclesList(List<CampuEcirclesListBean> campuEcirclesList) {
        this.campuEcirclesList = campuEcirclesList;
    }

    public static class ServerResultBean {
        /**
         * resultCode : 200
         * resultMessage : 成功
         */

        private int resultCode;
        private String resultMessage;

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMessage() {
            return resultMessage;
        }

        public void setResultMessage(String resultMessage) {
            this.resultMessage = resultMessage;
        }
    }

    public static class CampuEcirclesListBean {
        /**
         * browseNo : 202
         * cover : 1790574
         * id : 115
         * isRead : 1
         * operatorTime : 2018-01-03 15:05:52
         * publishTime : 2018-01-03 14:53:24
         * title : Test5
         * topTime : 2018-01-03 15:05:52
         * top_status : true
         */

        private int browseNo;
        private String cover;
        private int id;
        private String isRead;
        private String operatorTime;
        private String publishTime;
        private String title;
        private String topTime;
        private boolean top_status;

        public int getBrowseNo() {
            return browseNo;
        }

        public void setBrowseNo(int browseNo) {
            this.browseNo = browseNo;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }

        public String getOperatorTime() {
            return operatorTime;
        }

        public void setOperatorTime(String operatorTime) {
            this.operatorTime = operatorTime;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTopTime() {
            return topTime;
        }

        public void setTopTime(String topTime) {
            this.topTime = topTime;
        }

        public boolean isTop_status() {
            return top_status;
        }

        public void setTop_status(boolean top_status) {
            this.top_status = top_status;
        }
    }
}
