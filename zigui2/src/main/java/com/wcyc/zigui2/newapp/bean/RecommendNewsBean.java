package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzc
 * @time 2017/12/28 0028
 */
public class RecommendNewsBean implements Serializable {


    /**
     * browseNum : 0
     * campuEcirclesList : [{"browseNo":11,"classify":"推荐","cover":"1790703","id":140,"isRead":"1","operatorTime":"2018-01-04 17:03:43","publishTime":"2018-01-04 16:46:22","title":"2"},{"browseNo":48,"classify":"推荐","cover":"1790697","id":136,"isRead":"1","operatorTime":"2018-01-04 15:30:27","publishTime":"2018-01-04 15:30:27","title":"教育资讯--子贵游学01"},{"browseNo":14,"classify":"推荐","cover":"1790579","id":120,"isRead":"0","operatorTime":"2018-01-04 14:06:21","publishTime":"2018-01-03 15:59:04","title":"Test10"},{"browseNo":585,"classify":"热门","cover":"1790589","id":117,"isRead":"0","operatorTime":"2018-01-04 10:46:08","publishTime":"2018-01-03 15:47:47","title":"test07"},{"browseNo":237,"classify":"热门","cover":"1790574","id":115,"isRead":"1","operatorTime":"2018-01-03 15:05:52","publishTime":"2018-01-03 14:53:24","title":"Test5"},{"browseNo":223,"classify":"热门","cover":"1790573","id":114,"isRead":"1","operatorTime":"2018-01-03 14:48:33","publishTime":"2018-01-03 14:48:33","title":"Test4"},{"browseNo":17,"classify":"最新","cover":"1790723","id":144,"isRead":"0","operatorTime":"2018-01-04 17:16:16","publishTime":"2018-01-04 17:16:16","title":"测试推送"},{"browseNo":11,"classify":"最新","cover":"1790703","id":140,"isRead":"1","operatorTime":"2018-01-04 17:03:43","publishTime":"2018-01-04 16:46:22","title":"2"},{"browseNo":7,"classify":"最新","cover":"1790704","id":141,"isRead":"1","operatorTime":"2018-01-04 16:46:18","publishTime":"2018-01-04 16:46:18","title":"3"}]
     * interactiveCount : 0
     * pageNum : 1
     * pageSize : 3
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     * totalPageNum : 2
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
        public boolean isHideBrowserNO() {
            return hideBrowserNO;
        }

        public void setHideBrowserNO(boolean hideBrowserNO) {
            this.hideBrowserNO = hideBrowserNO;
        }

        /**
         * browseNo : 11
         * classify : 推荐
         * cover : 1790703
         * id : 140
         * isRead : 1
         * operatorTime : 2018-01-04 17:03:43
         * publishTime : 2018-01-04 16:46:22
         * title : 2
         */

        private boolean hideBrowserNO;

        private int browseNo;
        private String classify;
        private String cover;
        private int id;
        private String isRead;
        private String operatorTime;
        private String publishTime;
        private String title;

        public int getBrowseNo() {
            return browseNo;
        }

        public void setBrowseNo(int browseNo) {
            this.browseNo = browseNo;
        }

        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
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
    }
}
