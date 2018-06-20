package com.wcyc.zigui2.newapp.bean;

/**
 * Created by 章豪 on 2017/7/13.
 */

public class UserServiceInfo {

    private UserService userServie;

    private ServerResult serverResult;

    public ServerResult getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResult serverResult) {
        this.serverResult = serverResult;
    }

    public UserService getUserServie() {
        return userServie;
    }

    public void setUserServie(UserService userServie) {
        this.userServie = userServie;
    }

    public static  class UserService{
        private  int     flag;
        private  String endDate;
        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }
}
