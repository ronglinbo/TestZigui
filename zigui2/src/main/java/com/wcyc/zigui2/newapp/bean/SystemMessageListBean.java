package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzc
 * @time 2018/1/16 0016
 */
public class SystemMessageListBean implements Serializable {


    /**
     * msgList : [{"appStatus":"0","content":"<p>123123123123<\/p>","id":445,"messageRange":"1","operateId":1,"operatePerson":"系统管理员2","operateTime":"2018-02-01 14:12:42","readStatus":0,"receiptContent":"我已阅读","receiptPerson":3,"receiptStatus":1,"receiptType":"1","schoolId":"0","schoolName":"全部学校","status":"1","title":"1233213123","totalCount":235848,"type":"10050","typeName":"致家长书","unreadCount":235848,"validTime":"2018-02-28 00:00:00"},{"appStatus":"0","content":"<p>123123123123<\/p>","id":445,"messageRange":"1","operateId":1,"operatePerson":"系统管理员2","operateTime":"2018-02-01 14:12:42","readStatus":0,"receiptContent":"我已阅读","receiptPerson":3,"receiptStatus":1,"receiptType":"1","schoolId":"0","schoolName":"全部学校","status":"1","title":"1233213123","totalCount":235848,"type":"10050","typeName":"致家长书","unreadCount":235848,"validTime":"2018-02-28 00:00:00"},{"appStatus":"0","content":"<p>11111111111111<\/p>","id":444,"messageRange":"1","operateId":1,"operatePerson":"系统管理员2","operateTime":"2018-02-01 13:54:44","readStatus":0,"receiptContent":"我已阅读1","receiptPerson":3,"receiptStatus":1,"receiptType":"1","schoolId":"0","schoolName":"全部学校","status":"1","title":"111111111111111111","totalCount":235848,"type":"10050","typeName":"致家长书","unreadCount":235848,"validTime":"2018-02-28 00:00:00"},{"appStatus":"0","content":"<p>11111111111111<\/p>","id":444,"messageRange":"1","operateId":1,"operatePerson":"系统管理员2","operateTime":"2018-02-01 13:54:44","readStatus":0,"receiptContent":"我已阅读1","receiptPerson":3,"receiptStatus":1,"receiptType":"1","schoolId":"0","schoolName":"全部学校","status":"1","title":"111111111111111111","totalCount":235848,"type":"10050","typeName":"致家长书","unreadCount":235848,"validTime":"2018-02-28 00:00:00"},{"appStatus":"1","content":"<p><br /><\/p><p><br /><\/p><p>卡卡卡卡卡卡阿卡卡<\/p>","id":440,"messageRange":"2","operateId":1,"operatePerson":"系统管理员2","operateTime":"2018-02-01 11:42:11","readStatus":1,"receiptContent":"我已阅读","receiptPerson":3,"receiptStatus":1,"receiptType":"1","schoolId":"9","schoolName":"惟楚有才学校","status":"1","timePush":"2018-02-01 00:00:00","title":"哈哈哈哈哈","totalCount":11914,"type":"10050","typeName":"致家长书","unreadCount":11914,"validTime":"2018-02-03 00:00:00"},{"appStatus":"1","content":"<p><br /><\/p><p><br /><\/p><p>哈哈哈哈哈哈<\/p>","id":438,"messageRange":"2","operateId":1,"operatePerson":"系统管理员2","operateTime":"2018-02-01 11:19:12","readStatus":1,"receiptContent":"我已阅读","receiptPerson":3,"receiptStatus":1,"receiptType":"0","schoolId":"9","schoolName":"惟楚有才学校","status":"1","title":"测试删除数据","totalCount":11914,"type":"10050","typeName":"致家长书","unreadCount":11914,"validTime":"2018-02-01 00:00:00"},{"appStatus":"1","content":"<p><br /><\/p><p><br /><\/p><p>您已经欠费 100万<\/p>","id":437,"messageRange":"2","operateId":1,"operatePerson":"系统管理员2","operateTime":"2018-02-01 11:09:13","readStatus":0,"receiptContent":"我已阅读","receiptPerson":3,"receiptStatus":1,"receiptType":"1","schoolId":"9","schoolName":"惟楚有才学校","status":"1","title":"阿西吧 测试测试","totalCount":11914,"type":"10050","typeName":"致家长书","unreadCount":11914,"validTime":"2018-02-03 00:00:00"},{"appStatus":"1","content":"<p>0000000000000000000000000<\/p>","id":431,"messageRange":"2","operateId":1,"operatePerson":"系统管理员2","operateTime":"2018-01-31 15:12:19","readStatus":1,"receiptContent":"我已阅读","receiptPerson":3,"receiptStatus":1,"receiptType":"1","schoolId":"9","schoolName":"惟楚有才学校","status":"3","title":"00000000000","totalCount":11914,"type":"10050","typeName":"致家长书","unreadCount":11914,"validTime":"2018-01-31 00:00:00"},{"appStatus":"0","content":"<p>00000000000000000<\/p>","id":430,"messageRange":"2","operateId":1,"operatePerson":"系统管理员2","operateTime":"2018-01-31 15:10:32","readStatus":1,"receiptContent":"我已阅读","receiptPerson":3,"receiptStatus":1,"receiptType":"1","schoolId":"9","schoolName":"惟楚有才学校","status":"3","title":"0000000","totalCount":11914,"type":"10050","typeName":"致家长书","unreadCount":11914,"validTime":"2018-01-31 00:00:00"},{"appStatus":"0","content":"<p>890890890<\/p>","id":429,"messageRange":"2","operateId":1,"operatePerson":"系统管理员2","operateTime":"2018-01-31 14:54:01","readStatus":1,"receiptContent":"我已阅读","receiptPerson":3,"receiptStatus":1,"receiptType":"1","schoolId":"9","schoolName":"惟楚有才学校","status":"3","title":"8800890890","totalCount":11914,"type":"10050","typeName":"致家长书","unreadCount":11914,"validTime":"2018-01-31 00:00:00"}]
     * pageNum : 1
     * pageSize : 10
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     * totalPageNum : 2
     */

    private int pageNum;
    private int pageSize;
    private ServerResultBean serverResult;
    private int totalPageNum;
    private List<MsgListBean> msgList;

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

    public List<MsgListBean> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MsgListBean> msgList) {
        this.msgList = msgList;
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

    public static class MsgListBean {
        /**
         * appStatus : 0
         * content : <p>123123123123</p>
         * id : 445
         * messageRange : 1
         * operateId : 1
         * operatePerson : 系统管理员2
         * operateTime : 2018-02-01 14:12:42
         * readStatus : 0
         * receiptContent : 我已阅读
         * receiptPerson : 3
         * receiptStatus : 1
         * receiptType : 1
         * schoolId : 0
         * schoolName : 全部学校
         * status : 1
         * title : 1233213123
         * totalCount : 235848
         * type : 10050
         * typeName : 致家长书
         * unreadCount : 235848
         * validTime : 2018-02-28 00:00:00
         * timePush : 2018-02-01 00:00:00
         */

        private String appStatus;
        private String content;
        private int id;
        private String messageRange;
        private int operateId;
        private String operatePerson;
        private String operateTime;
        private int readStatus;
        private String receiptContent;
        private int receiptPerson;
        private int receiptStatus;
        private String receiptType;
        private String schoolId;
        private String schoolName;
        private String status;
        private String title;
        private int totalCount;
        private String type;
        private String typeName;
        private int unreadCount;
        private String validTime;
        private String timePush;

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

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
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

        public int getReceiptStatus() {
            return receiptStatus;
        }

        public void setReceiptStatus(int receiptStatus) {
            this.receiptStatus = receiptStatus;
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

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
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

        public String getTimePush() {
            return timePush;
        }

        public void setTimePush(String timePush) {
            this.timePush = timePush;
        }
    }
}
