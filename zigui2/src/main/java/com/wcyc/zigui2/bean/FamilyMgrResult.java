/*
 * 文 件 名:FamilyMgrResult.java
 * 创 建 人： 姜韵雯
 * 日    期： 2014-12-16
 * 版 本 号： 1.10
 */
package com.wcyc.zigui2.bean;

/**
 * 套餐管理实体类
 * 
 * @author 姜韵雯
 * @version 1.10
 * @since 1.10
 */
public class FamilyMgrResult extends BaseBean{
	private static final long serialVersionUID = 3477316828237888946L;
	private String familyState;
    private String endDate;
    private String haveDate;
    private String rechargeMoney;
    private String description;

    public String getFamilyState() {
        return familyState;
    }

    public void setFamilyState(String familyState) {
        this.familyState = familyState;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getHaveDate() {
        return haveDate;
    }

    public void setHaveDate(String haveDate) {
        this.haveDate = haveDate;
    }

    public String getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(String rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
