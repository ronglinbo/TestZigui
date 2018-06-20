package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;

/**
 * @author zzc
 * @time 2018/1/4 0004
 */
public class SystemAlertMessageBean implements Serializable{


    /**
     * bmc : {"appStatus":"1","content":"<p>内容<\/p>","id":143,"messageRange":"3","operateId":8,"operatePerson":"彭焱","operateTime":"2018-01-04 10:35:41","receiptContent":"我已阅读","receiptPerson":1,"receiptType":"1","schoolId":"38","schoolName":"常德市第三中学","status":"1","title":"测试APP端消息接收","totalCount":35,"type":"10050","unreadCount":35,"validTime":"2018-01-31 00:00:00"}
     * pageNum : 0
     * pageSize : 0
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     * totalPageNum : 0
     */

    private BmcBean bmc;
    private int pageNum;
    private int pageSize;
    private ServerResultBean serverResult;
    private int totalPageNum;

    public BmcBean getBmc() {
        return bmc;
    }

    public void setBmc(BmcBean bmc) {
        this.bmc = bmc;
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

    public static class BmcBean {
        /**
         * appStatus : 1
         * content : <p>内容</p>
         * id : 143
         * messageRange : 3
         * operateId : 8
         * operatePerson : 彭焱
         * operateTime : 2018-01-04 10:35:41
         * receiptContent : 我已阅读
         * receiptPerson : 1
         * receiptType : 1
         * schoolId : 38
         * schoolName : 常德市第三中学
         * status : 1
         * title : 测试APP端消息接收
         * totalCount : 35
         * type : 10050
         * unreadCount : 35
         * validTime : 2018-01-31 00:00:00
         */

        private String appStatus;
        private String content;
        private int id;
        private String messageRange;
        private int operateId;
        private String operatePerson;
        private String operateTime;
        private String receiptContent;
        private int receiptPerson;
        private String receiptType;
        private String schoolId;
        private String schoolName;
        private String status;
        private String title;
        private int totalCount;
        private String type;
        private int unreadCount;
        private String validTime;
        private String typeName; //类型名称

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getAppStatus() {
            return appStatus;
        }

        public void setAppStatus(String appStatus) {
            this.appStatus = appStatus;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMessageRange() {
            return messageRange;
        }

        public void setMessageRange(String messageRange) {
            this.messageRange = messageRange;
        }

        public int getOperateId() {
            return operateId;
        }

        public void setOperateId(int operateId) {
            this.operateId = operateId;
        }

        public String getOperatePerson() {
            return operatePerson;
        }

        public void setOperatePerson(String operatePerson) {
            this.operatePerson = operatePerson;
        }

        public String getOperateTime() {
            return operateTime;
        }

        public void setOperateTime(String operateTime) {
            this.operateTime = operateTime;
        }

        public String getReceiptContent() {
            return receiptContent;
        }

        public void setReceiptContent(String receiptContent) {
            this.receiptContent = receiptContent;
        }

        public int getReceiptPerson() {
            return receiptPerson;
        }

        public void setReceiptPerson(int receiptPerson) {
            this.receiptPerson = receiptPerson;
        }

        public String getReceiptType() {
            return receiptType;
        }

        public void setReceiptType(String receiptType) {
            this.receiptType = receiptType;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(int unreadCount) {
            this.unreadCount = unreadCount;
        }

        public String getValidTime() {
            return validTime;
        }

        public void setValidTime(String validTime) {
            this.validTime = validTime;
        }
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
}
