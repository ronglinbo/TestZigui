package com.wcyc.zigui2.newapp.bean;

import java.util.List;

/**
 * Created by hbj on 2017/5/24.
 */

public class LastinfoSchool {

    /**
     * lastinfoSchoolSummarize : {"additionNo":0,"content":"jbvvv","createTime":"2017-05-23 17:57:48","createUserId":18018,"departId":106,"departName":"教务科","id":576,"listISS":[{"id":61557,"isRead":0,"summarizeId":576,"userId":12974,"userName":"柏雪霜"}],"readNo":0,"schoolId":35,"status":1,"summarizeType":"周期小结","summarizeTypeCode":"1","summarizeTypeStr":"周期小结","title":"uijjh","userCode":"551","userName":"何波静"}
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     */

    private LastinfoSchoolSummarizeBean lastinfoSchoolSummarize;
    private ServerResultBean serverResult;

    public LastinfoSchoolSummarizeBean getLastinfoSchoolSummarize() {
        return lastinfoSchoolSummarize;
    }

    public void setLastinfoSchoolSummarize(LastinfoSchoolSummarizeBean lastinfoSchoolSummarize) {
        this.lastinfoSchoolSummarize = lastinfoSchoolSummarize;
    }

    public ServerResultBean getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResultBean serverResult) {
        this.serverResult = serverResult;
    }

    public static class LastinfoSchoolSummarizeBean {
        /**
         * additionNo : 0
         * content : jbvvv
         * createTime : 2017-05-23 17:57:48
         * createUserId : 18018
         * departId : 106
         * departName : 教务科
         * id : 576
         * listISS : [{"id":61557,"isRead":0,"summarizeId":576,"userId":12974,"userName":"柏雪霜"}]
         * readNo : 0
         * schoolId : 35
         * status : 1
         * summarizeType : 周期小结
         * summarizeTypeCode : 1
         * summarizeTypeStr : 周期小结
         * title : uijjh
         * userCode : 551
         * userName : 何波静
         */

        private int additionNo;
        private String content;
        private String createTime;
        private int createUserId;
        private int departId;
        private String departName;
        private int id;
        private int readNo;
        private int schoolId;
        private int status;
        private String summarizeType;
        private String summarizeTypeCode;
        private String summarizeTypeStr;
        private String title;
        private String userCode;
        private String userName;
        private List<ListISSBean> listISS;

        public int getAdditionNo() {
            return additionNo;
        }

        public void setAdditionNo(int additionNo) {
            this.additionNo = additionNo;
        }

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

        public String getSummarizeType() {
            return summarizeType;
        }

        public void setSummarizeType(String summarizeType) {
            this.summarizeType = summarizeType;
        }

        public String getSummarizeTypeCode() {
            return summarizeTypeCode;
        }

        public void setSummarizeTypeCode(String summarizeTypeCode) {
            this.summarizeTypeCode = summarizeTypeCode;
        }

        public String getSummarizeTypeStr() {
            return summarizeTypeStr;
        }

        public void setSummarizeTypeStr(String summarizeTypeStr) {
            this.summarizeTypeStr = summarizeTypeStr;
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

        public List<ListISSBean> getListISS() {
            return listISS;
        }

        public void setListISS(List<ListISSBean> listISS) {
            this.listISS = listISS;
        }

        public static class ListISSBean {
            /**
             * id : 61557
             * isRead : 0
             * summarizeId : 576
             * userId : 12974
             * userName : 柏雪霜
             */

            private int id;
            private int isRead;
            private int summarizeId;
            private int userId;
            private String userName;

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

            public int getSummarizeId() {
                return summarizeId;
            }

            public void setSummarizeId(int summarizeId) {
                this.summarizeId = summarizeId;
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
