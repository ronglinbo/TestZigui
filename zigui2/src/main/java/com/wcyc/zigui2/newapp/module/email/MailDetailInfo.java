package com.wcyc.zigui2.newapp.module.email;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.module.email.NewMailInfo.Recipient;
import com.wcyc.zigui2.newapp.module.email.NewMailInfo.Attachment;

import java.util.List;

/**
 * Created by xiehua on 2016/9/7.
 */
public class MailDetailInfo extends NewBaseBean{
    private List<Recipient> teacherRealation;
    private List<Recipient> studentRealation;
    private List<Recipient> copyRealation;
    private List<Attachment> listSAI;
    private String content;

    public List<Recipient> getTeacherRealation() {
        return teacherRealation;
    }

    public void setTeacherRealation(List<Recipient> teacherRealation) {
        this.teacherRealation = teacherRealation;
    }

    public List<Recipient> getStudentRealation() {
        return studentRealation;
    }

    public void setStudentRealation(List<Recipient> studentRealation) {
        this.studentRealation = studentRealation;
    }

    public List<Recipient> getCopyRealation() {
        return copyRealation;
    }

    public void setCopyRealation(List<Recipient> copyRealation) {
        this.copyRealation = copyRealation;
    }

    public List<Attachment> getListSAI() {
        return listSAI;
    }

    public void setListSAI(List<Attachment> listSAI) {
        this.listSAI = listSAI;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
