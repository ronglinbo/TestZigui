package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzc
 * @time 2018/3/29
 */
public class OnlineRechargeRecordBean implements Serializable {
    private static final long serialVersionUID = -167617923960718779L;


    /**
     * cardRechargeList : [{"createTime":"2018-03-29 09:17:14","fullAmount":50,"id":460,"orderAmount":50.01,"orderSn":"20180329091713647PSXsIHqyRa","orderType":2,"payType":"1","status":1,"studentId":374100,"updateTime":"2018-03-29 09:17:43","userId":645607,"userType":3},{"createTime":"2018-03-28 10:48:55","fullAmount":50,"id":437,"orderAmount":50.01,"orderSn":"20180328104854972mZEjsAImfh","orderType":2,"payType":"1","status":1,"studentId":374100,"updateTime":"2018-03-28 10:49:09","userId":644257,"userType":3},{"createTime":"2018-03-28 10:46:47","fullAmount":50,"id":436,"orderAmount":50.01,"orderSn":"20180328104647256hqAlZGW7eT","orderType":2,"payType":"1","status":1,"studentId":374100,"updateTime":"2018-03-28 10:47:01","userId":644257,"userType":3},{"createTime":"2018-03-28 10:44:57","fullAmount":50,"id":434,"orderAmount":50.01,"orderSn":"20180328104457390GkGxa3bZlQ","orderType":2,"payType":"1","status":1,"studentId":374100,"updateTime":"2018-03-28 10:45:29","userId":644257,"userType":3},{"createTime":"2018-03-27 16:14:59","fullAmount":50,"id":397,"orderAmount":50.01,"orderSn":"201803271614590490JB7wNsRYu","orderType":2,"payType":"1","status":1,"studentId":374100,"updateTime":"2018-03-27 16:15:11","userId":644257,"userType":3},{"createTime":"2018-03-27 15:54:02","fullAmount":100,"id":392,"orderAmount":100.01,"orderSn":"20180327155401584EKS8HLjsmI","orderType":2,"payType":"1","status":1,"studentId":374100,"updateTime":"2018-03-27 15:54:51","userId":645607,"userType":3},{"createTime":"2018-03-26 15:14:30","fullAmount":50,"id":340,"orderAmount":50.01,"orderSn":"20180326151429740PUSRUz2J3v","orderType":2,"payType":"1","status":1,"studentId":374100,"updateTime":"2018-03-26 15:14:44","userId":644257,"userType":3},{"createTime":"2018-03-26 11:09:31","fullAmount":100,"id":328,"orderAmount":100.01,"orderSn":"20180326110931397l68UhqG8SK","orderType":2,"payType":"1","status":1,"studentId":374100,"updateTime":"2018-03-26 11:09:59","userId":645607,"userType":3},{"createTime":"2018-03-26 10:43:46","fullAmount":100,"id":323,"orderAmount":100.01,"orderSn":"201803261043462394uTTW3vXJV","orderType":2,"payType":"1","status":1,"studentId":374100,"updateTime":"2018-03-26 10:44:15","userId":644257,"userType":3},{"createTime":"2018-03-26 10:22:02","fullAmount":50,"id":322,"orderAmount":50.01,"orderSn":"20180326102202347OZOn97nP0x","orderType":2,"payType":"1","status":1,"studentId":374100,"updateTime":"2018-03-26 10:22:23","userId":644257,"userType":3}]
     * pages : 2
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     */

    private int pages;
    private ServerResultBean serverResult;
    private List<CardRechargeListBean> cardRechargeList;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public ServerResultBean getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResultBean serverResult) {
        this.serverResult = serverResult;
    }

    public List<CardRechargeListBean> getCardRechargeList() {
        return cardRechargeList;
    }

    public void setCardRechargeList(List<CardRechargeListBean> cardRechargeList) {
        this.cardRechargeList = cardRechargeList;
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

    public static class CardRechargeListBean {
        /**
         * createTime : 2018-03-29 09:17:14
         * fullAmount : 50.0
         * id : 460
         * orderAmount : 50.01
         * orderSn : 20180329091713647PSXsIHqyRa
         * orderType : 2
         * payType : 1
         * status : 1
         * studentId : 374100
         * updateTime : 2018-03-29 09:17:43
         * userId : 645607
         * userType : 3
         */

        private String createTime;
        private double fullAmount;
        private int id;
        private double orderAmount;
        private String orderSn;
        private int orderType;
        private String payType;
        private int status;
        private int studentId;
        private String updateTime;
        private int userId;
        private int userType;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public double getFullAmount() {
            return fullAmount;
        }

        public void setFullAmount(double fullAmount) {
            this.fullAmount = fullAmount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(double orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }
}
