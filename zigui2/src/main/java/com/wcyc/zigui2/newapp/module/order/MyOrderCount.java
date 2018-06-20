package com.wcyc.zigui2.newapp.module.order;

/**
 * Created by 章豪 on 2017/7/11.
 */

public class MyOrderCount {

            private String debtCount;
            private String noPayCount;
            private int  pages;
              private ServerResult  serverResult;

    public ServerResult getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResult serverResult) {
        this.serverResult = serverResult;
    }

    public String getDebtCount() {
        return debtCount;
    }

    public void setDebtCount(String debtCount) {
        this.debtCount = debtCount;
    }

    public String getNoPayCount() {
        return noPayCount;
    }

    public void setNoPayCount(String noPayCount) {
        this.noPayCount = noPayCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    class ServerResult{
           private  String  serverResult;
           private   int resultCode;
           private String   resultMessage;

           public String getServerResult() {
               return serverResult;
           }

           public void setServerResult(String serverResult) {
               this.serverResult = serverResult;
           }

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


