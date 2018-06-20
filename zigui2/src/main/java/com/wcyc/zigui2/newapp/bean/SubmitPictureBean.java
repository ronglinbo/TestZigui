package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzc
 * @time 2018/4/26
 */
public class SubmitPictureBean implements Serializable {

    public List getClassIdList() {
        return classIdList;
    }

    public void setClassIdList(List classIdList) {
        this.classIdList = classIdList;
    }

    private List classIdList;

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    private String picId;
}
