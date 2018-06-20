package com.wcyc.zigui2.newapp.bean;

import java.util.List;

/**
 * @author zzc
 * @time 2017/12/27 0027
 */
public class EducationNewsTitleBean {


    /**
     * browseNum : 0
     * ecirclesTypelist : [{"configName":"子贵游学","id":10043},{"configName":"教育界","id":10044},{"configName":"亲子教育","id":10045},{"configName":"子贵动态","id":10046},{"configName":"应用分享","id":10047}]
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
    private List<EcirclesTypelistBean> ecirclesTypelist;

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

    public List<EcirclesTypelistBean> getEcirclesTypelist() {
        return ecirclesTypelist;
    }

    public void setEcirclesTypelist(List<EcirclesTypelistBean> ecirclesTypelist) {
        this.ecirclesTypelist = ecirclesTypelist;
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

    public static class EcirclesTypelistBean {
        /**
         * configName : 子贵游学
         * id : 10043
         */

        private String configName;
        private int id;

        public String getConfigName() {
            return configName;
        }

        public void setConfigName(String configName) {
            this.configName = configName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
