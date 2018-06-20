package com.wcyc.zigui2.newapp.module.leavemessage;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by 章豪 on 2017/8/8.
 */
@Entity
public class LeaveMessage {

    @Id(autoincrement = true)
    private Long id;
    private boolean isdelete;
    private String content;
    private String createTime;

    @Generated(hash = 1380885072)
    public LeaveMessage(Long id, boolean isdelete, String content,
                        String createTime) {
        this.id = id;
        this.isdelete = isdelete;
        this.content = content;
        this.createTime = createTime;
    }

    @Generated(hash = 625762804)
    public LeaveMessage() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsdelete() {
        return this.isdelete;
    }

    public void setIsdelete(boolean isdelete) {
        this.isdelete = isdelete;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


}
