/**
  * Copyright 2017 aTool.org 
  */
package com.wcyc.zigui2.newapp.module.order;
import java.util.List;

/**
 * Auto-generated: 2017-07-14 11:2:21
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class OrderInfo {


    private List<Order> orderList;
    private int pages;
    private Serverresult serverResult;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Serverresult getServerResult() {
        return serverResult;
    }

    public void setServerResult(Serverresult serverResult) {
        this.serverResult = serverResult;
    }
}