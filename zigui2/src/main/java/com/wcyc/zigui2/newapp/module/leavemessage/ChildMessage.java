package com.wcyc.zigui2.newapp.module.leavemessage;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by 章豪 on 2017/8/10.
 */
@Entity
public class ChildMessage {

    @Id
    private Long id;
    //视频地址本地的  原服务器地址
    private String pcitureAddress;
    //视频第一帧图片  原服务器地址
    private String image_url;
    //时间日期
    private String createTime;

    private Long filelength;
    @Generated(hash = 1268001126)
    public ChildMessage(Long id, String pcitureAddress, String image_url,
            String createTime, Long filelength) {
        this.id = id;
        this.pcitureAddress = pcitureAddress;
        this.image_url = image_url;
        this.createTime = createTime;
        this.filelength = filelength;
    }

    @Generated(hash = 1253594459)
    public ChildMessage() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPcitureAddress() {
        return this.pcitureAddress;
    }

    public void setPcitureAddress(String pcitureAddress) {
        this.pcitureAddress = pcitureAddress;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getFilelength() {
        return this.filelength;
    }

    public void setFilelength(Long filelength) {
        this.filelength = filelength;
    }


}
