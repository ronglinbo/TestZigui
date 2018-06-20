package com.wcyc.zigui2.newapp.bean;

/**
 * Created by xiehua on 2016/10/8.
 */

public class NewVersionCheckModel extends NewBaseBean {
    //是否需要更新 （0 否 1 是）
    private String isNeedUpdate;
    //更新类型 （0 非强制更新 1 强制更新）
    private int isUpdate;
    //更新描述
    private String updateExplain;
    //更新地址
    private String updateAddress;

    private String versionName;

    public String getIsNeedUpdate() {
        return isNeedUpdate;
    }

    public void setIsNeedUpdate(String isNeedUpdate) {
        this.isNeedUpdate = isNeedUpdate;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getUpdateExplain() {
        return updateExplain;
    }

    public void setUpdateExplain(String updateExplain) {
        this.updateExplain = updateExplain;
    }

    public String getUpdateAddress() {
        return updateAddress;
    }

    public void setUpdateAddress(String updateAddress) {
        this.updateAddress = updateAddress;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
