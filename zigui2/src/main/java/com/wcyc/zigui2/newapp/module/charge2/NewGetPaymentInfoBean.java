package com.wcyc.zigui2.newapp.module.charge2;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 郑国栋 on 2017/1/12 0012.
 */
public class NewGetPaymentInfoBean implements Serializable {
    private String orderId;
    private String paymentPlatformType;
    List<String> productList;
    private String productNames;

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentPlatformType() {
        return paymentPlatformType;
    }
    public void setPaymentPlatformType(String paymentPlatformType) {
        this.paymentPlatformType = paymentPlatformType;
    }

    public List<String> getProductList() {
        return productList;
    }
    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

}
