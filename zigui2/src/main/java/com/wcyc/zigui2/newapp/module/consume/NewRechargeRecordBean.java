package com.wcyc.zigui2.newapp.module.consume;

/**
 * 充值记录业务bean
 * Created by xiehua on 2017/5/3.
 */

public class NewRechargeRecordBean {

    /**
     * 充值时间
     */
    private String updatetime;
    /**
     * 充值类型（4 线下充值）
     */
    private String consumeType;
    /**
     * 充值金额（元）
     */
    private String addmoney;
    /**
     * 充值后余额
     */
    private String cardRemaining;

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(String consumeType) {
        this.consumeType = consumeType;
    }

    public String getAddmoney() {
        return addmoney;
    }

    public void setAddmoney(String addmoney) {
        this.addmoney = addmoney;
    }

    public String getCardRemaining() {
        return cardRemaining;
    }

    public void setCardRemaining(String cardRemaining) {
        this.cardRemaining = cardRemaining;
    }

    @Override
    public String toString() {
        return "NewRechargeRecordBean{" +
                "updatetime='" + updatetime + '\'' +
                ", consumeType='" + consumeType + '\'' +
                ", addmoney='" + addmoney + '\'' +
                ", cardRemaining='" + cardRemaining + '\'' +
                '}';
    }
}
