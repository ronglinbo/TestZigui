package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hbj on 2017/5/23.
 */

public class LastRecord {


    private LastInfoSchoolDailyRecordBean lastInfoSchoolDailyRecord;
    private ServerResultBean serverResult;

    public LastInfoSchoolDailyRecordBean getLastInfoSchoolDailyRecord() {
        return lastInfoSchoolDailyRecord;
    }

    public void setLastInfoSchoolDailyRecord(LastInfoSchoolDailyRecordBean lastInfoSchoolDailyRecord) {
        this.lastInfoSchoolDailyRecord = lastInfoSchoolDailyRecord;
    }

    public ServerResultBean getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResultBean serverResult) {
        this.serverResult = serverResult;
    }

    public static class LastInfoSchoolDailyRecordBean {

        private String content;
        private String createTime;
        private int createUserId;
        private int departId;
        private String departName;
        private int id;
        private int readNo;
        private int schoolId;
        private int status;
        private String title;
        private String userCode;
        private String userName;
        private List<IsdrListBean> isdrList;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(int createUserId) {
            this.createUserId = createUserId;
        }

        public int getDepartId() {
            return departId;
        }

        public void setDepartId(int departId) {
            this.departId = departId;
        }

        public String getDepartName() {
            return departName;
        }

        public void setDepartName(String departName) {
            this.departName = departName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getReadNo() {
            return readNo;
        }

        public void setReadNo(int readNo) {
            this.readNo = readNo;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public List<IsdrListBean> getIsdrList() {
            return isdrList;
        }

        public void setIsdrList(List<IsdrListBean> isdrList) {
            this.isdrList = isdrList;
        }

        public static class IsdrListBean implements Serializable {
            /**
             * dailyId : 1077
             * id : 128643
             * isRead : 0
             * userId : 1093
             * userName : 张继续
             */

            private int dailyId;
            private int id;
            private int isRead;
            private int userId;
            private String userName;

            public int getDailyId() {
                return dailyId;
            }

            public void setDailyId(int dailyId) {
                this.dailyId = dailyId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIsRead() {
                return isRead;
            }

            public void setIsRead(int isRead) {
                this.isRead = isRead;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
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
