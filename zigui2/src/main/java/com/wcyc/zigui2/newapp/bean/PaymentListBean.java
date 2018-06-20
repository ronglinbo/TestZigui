package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzc
 * @time 2018/2/26 0026
 */
public class PaymentListBean implements Serializable {

    /**
     * curPage : 1
     * list : [{"amount":8,"endTime":"2018-03-31","endTimeDate":"2018-03-31 00:00:00","payAmount":0,"payChannel":"支付宝","payName":"仅手续费比例的缴费-家长支付","payTypeName":"活动费","publishId":132,"publishTime":"2018-03-27 10:08:47","status":0},{"amount":50,"endTime":"2018-03-26","endTimeDate":"2018-03-26 00:00:00","payAmount":0,"payChannel":"支付宝","payName":"个人缴费","payTypeName":"学杂费","publishId":131,"publishTime":"2018-03-27 10:04:26","status":2},{"amount":2,"endTime":"2018-03-31","endTimeDate":"2018-03-31 00:00:00","payAmount":0,"payChannel":"支付宝","payName":"学杂费x1101班","payTypeName":"学杂费","publishId":127,"publishTime":"2018-03-26 16:56:01","status":0},{"amount":1,"endTime":"2018-03-31","endTimeDate":"2018-03-31 00:00:00","payAmount":0,"payChannel":"支付宝","payName":"春游活动（没有最低和最高手续费）","payTypeName":"活动费","publishId":126,"publishTime":"2018-03-26 16:28:40","status":0},{"amount":1,"endTime":"2018-03-31","endTimeDate":"2018-03-31 00:00:00","orderSn":"20180326150216007Ijggtrt4QF","payAmount":1.01,"payChannel":"支付宝","payName":"缴费项目1","payStatus":1,"payTime":"2018-03-26 15:03","payTimeDate":"2018-03-26 15:03:12","payTypeName":"校服费","publishId":121,"publishTime":"2018-03-26 10:53:33","status":1}]
     * pageSize : 10
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     * totalPageNum : 1
     */

    private int curPage;
    private int pageSize;
    private ServerResultBean serverResult;
    private int totalPageNum;
    private List<ListBean> list;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
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

    public static class ListBean {
        /**
         * amount : 8
         * endTime : 2018-03-31
         * endTimeDate : 2018-03-31 00:00:00
         * payAmount : 0
         * payChannel : 支付宝
         * payName : 仅手续费比例的缴费-家长支付
         * payTypeName : 活动费
         * publishId : 132
         * publishTime : 2018-03-27 10:08:47
         * status : 0
         * orderSn : 20180326150216007Ijggtrt4QF
         * payStatus : 1
         * payTime : 2018-03-26 15:03
         * payTimeDate : 2018-03-26 15:03:12
         */

        private double amount;
        private String endTime;
        private String endTimeDate;
        private double payAmount;
        private String payChannel;
        private String payName;
        private String payTypeName;
        private int publishId;
        private String publishTime;
        private int status;
        private String orderSn;
        private int payStatus;
        private String payTime;
        private String payTimeDate;

        public double getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getEndTimeDate() {
            return endTimeDate;
        }

        public void setEndTimeDate(String endTimeDate) {
            this.endTimeDate = endTimeDate;
        }

        public double getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(int payAmount) {
            this.payAmount = payAmount;
        }

        public String getPayChannel() {
            return payChannel;
        }

        public void setPayChannel(String payChannel) {
            this.payChannel = payChannel;
        }

        public String getPayName() {
            return payName;
        }

        public void setPayName(String payName) {
            this.payName = payName;
        }

        public String getPayTypeName() {
            return payTypeName;
        }

        public void setPayTypeName(String payTypeName) {
            this.payTypeName = payTypeName;
        }

        public int getPublishId() {
            return publishId;
        }

        public void setPublishId(int publishId) {
            this.publishId = publishId;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getOrderSn() {
            return orderSn;
        }

        public void setOrderSn(String orderSn) {
            this.orderSn = orderSn;
        }

        public int getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(int payStatus) {
            this.payStatus = payStatus;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getPayTimeDate() {
            return payTimeDate;
        }

        public void setPayTimeDate(String payTimeDate) {
            this.payTimeDate = payTimeDate;
        }
    }
}
