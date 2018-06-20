package com.wcyc.zigui2.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by 章豪 on 2017/9/18.
 */
@Entity
public class ZiGuiVideo {
    @Id
    private Long id;
    private String vid;
    private int time;
    @Generated(hash = 198955936)
    public ZiGuiVideo(Long id, String vid, int time) {
        this.id = id;
        this.vid = vid;
        this.time = time;
    }
    @Generated(hash = 133882346)
    public ZiGuiVideo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getVid() {
        return this.vid;
    }
    public void setVid(String vid) {
        this.vid = vid;
    }
    public int getTime() {
        return this.time;
    }
    public void setTime(int time) {
        this.time = time;
    }


}
