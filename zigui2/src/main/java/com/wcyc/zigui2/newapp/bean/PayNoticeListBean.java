package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单催缴列表
 * @time 2018/2/3 0003
 */
public class PayNoticeListBean implements Serializable{


    /**
     * msgList : [{"content":"<div class=\"notice_add\">\n  <div class=\"sendMar\" style=\"line-height:25px;font-size:15px;\">\n    尊敬的<span style=\"border-bottom:1px solid #000; width:50px; display:inline-block\">朱泽宇<\/span>同学的家长：<br>\n  \n <p style=\"text-indent:2em;\">\n您好！您本学期产生的<span style=\"border-bottom:1px solid #000;min-width:30px;text-indent:0em; display:inline-block\">2017-2018学年服务包<\/span>信息服务费，共计<span style=\"border-bottom:1px solid #000;min-width:30px;text-indent:0em;  display:inline-block\">90<\/span>元，至今尚未缴付。学校马上进入期末考试阶段，为保证学生成绩、学期评价、放假通知、假期作业等大量信息和注意事项的正常接收，请您接通知后，务必于一周内，通过子贵校园App进行在线缴费或联系班主任老师咨询现金代充予以结清。逾期我们将根据子贵校园服务条例，采取一切合法的手段进行追偿，包括但不限于在平台上公布欠费名单、发布欠费提醒单，递送书面催缴函等。<\/p>\n<br><p style=\"text-indent:2em;\">\n    感谢您的配合！<\/p><br>\n    <div style=\"text-align:right\"><span style=\"border-bottom:1px solid #000;min-width:30px; display:inline-block\">常德市第三中学<\/span>子贵校园运维办<br>\n    <span style=\"border-bottom:1px solid #000; width:50px; display:inline-block\">2018<\/span>年<span style=\"border-bottom:1px solid #000; width:50px; display:inline-block\">02<\/span>月<span style=\"border-bottom:1px solid #000; width:50px; display:inline-block\">03<\/span>日 <\/div><\/div>\n  <br clear=\"all\">\n<\/div>","id":22,"operateTime":"2018-02-03 09:28:40","readStatus":0,"status":1,"title":"[2017-2018学年服务包] 期末结算单","type":"33","typeName":" 期末结算单"},{"content":"<div class=\"notice_add\">\n  <div class=\"sendMar\" style=\"line-height:25px;font-size:15px;\">\n    尊敬的<span style=\"border-bottom:1px solid #000; width:50px; display:inline-block\">朱泽宇<\/span>同学的家长：<br>\n    <p style=\"text-indent:2em;\">您好！经核查，您的<span style=\"border-bottom:1px solid #000;min-width:30px;text-indent:0em;display:inline-block\">2017-2018学年服务包<\/span>信息服务费（<span style=\"border-bottom:1px solid #000;min-width:30px;text-indent:0em; display:inline-block\">2017<\/span>年<span style=\"border-bottom:1px solid #000;min-width:30px;text-indent:0em; display:inline-block\">08<\/span>月<span style=\"border-bottom:1px solid #000;min-width:30px;text-indent:0em; display:inline-block\">01<\/span>日- <span style=\"border-bottom:1px solid #000;min-width:30px;text-indent:0em;display:inline-block\">2018<\/span>年<span style=\"border-bottom:1px solid #000;min-width:30px;text-indent:0em; display:inline-block\">07<\/span>月<span style=\"border-bottom:1px solid #000;min-width:30px;text-indent:0em; display:inline-block\">31<\/span>日）尚未缴付，共计<span style=\"border-bottom:1px solid #000; min-width:50px;text-indent:0em; display:inline-block\">90<\/span>元。请您接通知后，于一周内通过子贵校园App进行在线缴费，或联系班主任老师咨询现金代充方式。逾期我们将按规定暂停您的家校互动所有信息推送服务，由于无法正常接收学校信息而给您带来不便和影响，我们表示遗憾。<\/p><br>\n    <p style=\"text-indent:2em;\">谢谢合作！<\/p><br>\n    <div style=\"text-align:right\"><span style=\"border-bottom:1px solid #000;min-width:30px;display:inline-block\">常德市第三中学<\/span>子贵校园运维办<br>\n    <span style=\"border-bottom:1px solid #000; width:50px; display:inline-block\">2018<\/span>年<span style=\"border-bottom:1px solid #000; width:50px; display:inline-block\">02<\/span>月<span style=\"border-bottom:1px solid #000; width:50px; display:inline-block\">03<\/span>日 <\/div><\/div>\n  <br clear=\"all\">\n<\/div>","id":21,"operateTime":"2018-02-03 09:28:32","readStatus":0,"status":1,"title":"[2017-2018学年服务包] 缴费提醒","type":"33","typeName":" 缴费提醒"}]
     * pageNum : 1
     * pageSize : 10
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     * totalPageNum : 1
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
         * content : <div class="notice_add">
         <div class="sendMar" style="line-height:25px;font-size:15px;">
         尊敬的<span style="border-bottom:1px solid #000; width:50px; display:inline-block">朱泽宇</span>同学的家长：<br>

         <p style="text-indent:2em;">
         您好！您本学期产生的<span style="border-bottom:1px solid #000;min-width:30px;text-indent:0em; display:inline-block">2017-2018学年服务包</span>信息服务费，共计<span style="border-bottom:1px solid #000;min-width:30px;text-indent:0em;  display:inline-block">90</span>元，至今尚未缴付。学校马上进入期末考试阶段，为保证学生成绩、学期评价、放假通知、假期作业等大量信息和注意事项的正常接收，请您接通知后，务必于一周内，通过子贵校园App进行在线缴费或联系班主任老师咨询现金代充予以结清。逾期我们将根据子贵校园服务条例，采取一切合法的手段进行追偿，包括但不限于在平台上公布欠费名单、发布欠费提醒单，递送书面催缴函等。</p>
         <br><p style="text-indent:2em;">
         感谢您的配合！</p><br>
         <div style="text-align:right"><span style="border-bottom:1px solid #000;min-width:30px; display:inline-block">常德市第三中学</span>子贵校园运维办<br>
         <span style="border-bottom:1px solid #000; width:50px; display:inline-block">2018</span>年<span style="border-bottom:1px solid #000; width:50px; display:inline-block">02</span>月<span style="border-bottom:1px solid #000; width:50px; display:inline-block">03</span>日 </div></div>
         <br clear="all">
         </div>
         * id : 22
         * operateTime : 2018-02-03 09:28:40
         * readStatus : 0
         * status : 1
         * title : [2017-2018学年服务包] 期末结算单
         * type : 33
         * typeName :  期末结算单
         */

        private String content;
        private int id;
        private String operateTime;
        private int readStatus;
        private int status;
        private String title;
        private String type;
        private String typeName;

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
    }
}
