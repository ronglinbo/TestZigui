package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;

/**
 * @author zzc
 * @date 2018/4/19
 */
public class LauncherInfoBean implements Serializable {

    private static final long serialVersionUID = -8848023844158885593L;


    /**
     * infoSchoolStart : {"id":1,"imageUrl":"downloadApi?fileId=1797643","isTextDefault":0,"schoolId":9,"schoolName":"惟楚有才学校","status":1,"textLogoUrl":"downloadApi?fileId=1797645","updateTime":"2018-04-10 11:04:11","updateUserId":1,"updateUserName":"系统管理员2"}
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     */

    private InfoSchoolStartBean infoSchoolStart;
    private ServerResultBean serverResult;

    public InfoSchoolStartBean getInfoSchoolStart() {
        return infoSchoolStart;
    }

    public void setInfoSchoolStart(InfoSchoolStartBean infoSchoolStart) {
        this.infoSchoolStart = infoSchoolStart;
    }

    public ServerResultBean getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResultBean serverResult) {
        this.serverResult = serverResult;
    }

    public static class InfoSchoolStartBean {
        /**
         * id : 1
         * imageUrl : downloadApi?fileId=1797643
         * isTextDefault : 0
         * schoolId : 9
         * schoolName : 惟楚有才学校
         * status : 1
         * textLogoUrl : downloadApi?fileId=1797645
         * updateTime : 2018-04-10 11:04:11
         * updateUserId : 1
         * updateUserName : 系统管理员2
         */

        private int id;
        private String imageUrl;
        private int isTextDefault;
        private int schoolId;
        private String schoolName;
        private int status;
        private String textLogoUrl;
        private String updateTime;
        private int updateUserId;
        private String updateUserName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getIsTextDefault() {
            return isTextDefault;
        }

        public void setIsTextDefault(int isTextDefault) {
            this.isTextDefault = isTextDefault;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTextLogoUrl() {
            return textLogoUrl;
        }

        public void setTextLogoUrl(String textLogoUrl) {
            this.textLogoUrl = textLogoUrl;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUpdateUserId() {
            return updateUserId;
        }

        public void setUpdateUserId(int updateUserId) {
            this.updateUserId = updateUserId;
        }

        public String getUpdateUserName() {
            return updateUserName;
        }

        public void setUpdateUserName(String updateUserName) {
            this.updateUserName = updateUserName;
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
