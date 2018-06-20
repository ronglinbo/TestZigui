package com.wcyc.zigui2.newapp.bean;

import java.util.List;

/**
 * Created by xiehua on 2017/5/4.
 */

public class GetText {


    private ServerResultBean serverResult;
    private List<RelationShipListBean> relationShipList;

    public ServerResultBean getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResultBean serverResult) {
        this.serverResult = serverResult;
    }

    public List<RelationShipListBean> getRelationShipList() {
        return relationShipList;
    }

    public void setRelationShipList(List<RelationShipListBean> relationShipList) {
        this.relationShipList = relationShipList;
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

    public static class RelationShipListBean {
        /**
         * configCode : 1
         * configDesc :
         * configName : 学校通知
         * id : 1371
         */

        private String configCode;
        private String configDesc;
        private String configName;
        private int id;

        public String getConfigCode() {
            return configCode;
        }

        public void setConfigCode(String configCode) {
            this.configCode = configCode;
        }

        public String getConfigDesc() {
            return configDesc;
        }

        public void setConfigDesc(String configDesc) {
            this.configDesc = configDesc;
        }

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
