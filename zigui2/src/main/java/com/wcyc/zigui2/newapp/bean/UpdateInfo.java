package com.wcyc.zigui2.newapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18 0018.
 */
public class UpdateInfo {

    private int pagenum;

    private int pagesize;

    private ServerResult serverresult;

    private int totalpagenum;

    private List<Versionupdateinfolist> versionupdateinfolist;
    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }
    public int getPagenum() {
        return pagenum;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
    public int getPagesize() {
        return pagesize;
    }

    public void setServerresult(ServerResult serverresult) {
        this.serverresult = serverresult;
    }
    public ServerResult getServerresult() {
        return serverresult;
    }

    public void setTotalpagenum(int totalpagenum) {
        this.totalpagenum = totalpagenum;
    }
    public int getTotalpagenum() {
        return totalpagenum;
    }

    public void setVersionupdateinfolist(List<Versionupdateinfolist> versionupdateinfolist) {
        this.versionupdateinfolist = versionupdateinfolist;
    }
    public List<Versionupdateinfolist> getVersionupdateinfolist() {
        return versionupdateinfolist;
    }
}
