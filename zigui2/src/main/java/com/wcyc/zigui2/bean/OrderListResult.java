package com.wcyc.zigui2.bean;

import java.util.List;

/**
 * @author xfliu
 * @version 1.05
 */
public class OrderListResult extends BaseBean {
    private List<OrderInfoResult> orderList;

    public List<OrderInfoResult> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderInfoResult> orderList) {
        this.orderList = orderList;
    }
}
