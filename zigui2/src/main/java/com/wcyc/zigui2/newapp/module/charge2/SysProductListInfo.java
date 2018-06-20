package com.wcyc.zigui2.newapp.module.charge2; /**
  * Copyright 2017 aTool.org 
  */

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2017-07-07 15:17:10
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class SysProductListInfo  implements Serializable{


    private List<PurchasedProcutList> purchasedProcutList;
    private List<Productlist> productList;


    private List<Schoolallproductlist> schoolAllProductList;

    public List<PurchasedProcutList> getPurchasedProcutList() {
        return purchasedProcutList;
    }

    public void setPurchasedProcutList(List<PurchasedProcutList> purchasedProcutList) {
        this.purchasedProcutList = purchasedProcutList;
    }

    public List<Schoolallproductlist> getSchoolAllProductList() {
        return schoolAllProductList;
    }

    public void setSchoolAllProductList(List<Schoolallproductlist> schoolAllProductList) {
        this.schoolAllProductList = schoolAllProductList;
    }

    private Serverresult serverResult;

    public List<Productlist> getProductList() {
        return productList;
    }

    public void setProductList(List<Productlist> productList) {
        this.productList = productList;
    }

    public Serverresult getServerResult() {
        return serverResult;
    }

    public void setServerResult(Serverresult serverResult) {
        this.serverResult = serverResult;
    }

    class Productlist implements Serializable {

        private int amount;

        private String startDate;

        private int serviceId;

        private int orderModel;
        private int overdue;

        private String productCode;

        private String endDate;

        private int actualAmount;

        private String productName;

        private String serviceName;
        public void setAmount(int amount) {
            this.amount = amount;
        }
        public int getAmount() {
            return amount;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public int getOrderModel() {
            return orderModel;
        }

        public void setOrderModel(int orderModel) {
            this.orderModel = orderModel;
        }

        public int getOverdue() {
            return overdue;
        }

        public void setOverdue(int overdue) {
            this.overdue = overdue;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }



        public int getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(int actualAmount) {
            this.actualAmount = actualAmount;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }
    }


   public static class Schoolallproductlist  implements Serializable {

        private int amount;

        private int serviceId;

        private int orderModel;
         private String startDate;
         private String endDate;
        private int  productCode;

        private int actualAmount;

        private String productName;

        private String serviceName;
       private String  service_value;

       public String getService_value() {
           return service_value;
       }

       public void setService_value(String service_value) {
           this.service_value = service_value;
       }

       public int getProductCode() {
           return productCode;
       }

       public void setProductCode(int productCode) {
           this.productCode = productCode;
       }

       public void setAmount(int amount) {
            this.amount = amount;
        }
        public int getAmount() {
            return amount;
        }

       public String getStartDate() {
           return startDate;
       }

       public void setStartDate(String startDate) {
           this.startDate = startDate;
       }

       public String getEndDate() {
           return endDate;
       }

       public void setEndDate(String endDate) {
           this.endDate = endDate;
       }

       public int getServiceId() {
           return serviceId;
       }

       public void setServiceId(int serviceId) {
           this.serviceId = serviceId;
       }

       public int getOrderModel() {
           return orderModel;
       }

       public void setOrderModel(int orderModel) {
           this.orderModel = orderModel;
       }


       public int getActualAmount() {
           return actualAmount;
       }

       public void setActualAmount(int actualAmount) {
           this.actualAmount = actualAmount;
       }

       public String getProductName() {
           return productName;
       }

       public void setProductName(String productName) {
           this.productName = productName;
       }

       public String getServiceName() {
           return serviceName;
       }

       public void setServiceName(String serviceName) {
           this.serviceName = serviceName;
       }
   }

    static class PurchasedProcutList  implements Serializable {

        private int amount;

        private int serviceId;

        private int orderModel;
        private String startDate;
        private String endDate;
        private int  productCode;

        private int actualAmount;

        private String productName;

        private String serviceName;

        public int getProductCode() {
            return productCode;
        }

        public void setProductCode(int productCode) {
            this.productCode = productCode;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
        public int getAmount() {
            return amount;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public int getOrderModel() {
            return orderModel;
        }

        public void setOrderModel(int orderModel) {
            this.orderModel = orderModel;
        }


        public int getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(int actualAmount) {
            this.actualAmount = actualAmount;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }
    }

    class Serverresult  implements Serializable {


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