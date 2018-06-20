package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzc
 * @time 2018/1/2 0002
 */
public class EducationSearchBean implements Serializable{


    /**
     * browseNum : 0
     * ecirclesList : [{"id":66,"publishTime":"2017-12-28 16:45:43","title":"男神吴尊空降《爸爸去哪儿5》，节目组再次为您解读\u201c陪伴\u201d的意义"}]
     * interactiveCount : 0
     * pageNum : 0
     * pageSize : 0
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     * totalPageNum : 0
     */

    private int browseNum;
    private int interactiveCount;
    private int pageNum;
    private int pageSize;
    private ServerResultBean serverResult;
    private int totalPageNum;
    private List<EcirclesListBean> ecirclesList;

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

    public List<EcirclesListBean> getEcirclesList() {
        return ecirclesList;
    }

    public void setEcirclesList(List<EcirclesListBean> ecirclesList) {
        this.ecirclesList = ecirclesList;
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

    public static class EcirclesListBean {
        /**
         * id : 66
         * publishTime : 2017-12-28 16:45:43
         * title : 男神吴尊空降《爸爸去哪儿5》，节目组再次为您解读“陪伴”的意义
         */

        private int id;
        private String publishTime;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
