package com.wcyc.zigui2.newapp.bean;

/**
 * Created by xiehua on 2017/3/7.
 */

public class LoginBean extends NewBaseBean {
    private String userName;
    private String password;

    public LoginBean(String userName,String password){
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
