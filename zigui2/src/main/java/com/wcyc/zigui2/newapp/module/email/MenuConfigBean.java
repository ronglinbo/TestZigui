package com.wcyc.zigui2.newapp.module.email;

import android.text.TextUtils;

import com.wcyc.zigui2.newapp.bean.NewBaseBean;

import java.util.List;

/**
 * Created by xiehua on 2017/3/31.
 */

public class MenuConfigBean extends NewBaseBean {
    private List<MenuConfig> personalConfigList;

    public List<MenuConfig> getPersonalConfigList() {
        return personalConfigList;
    }

    public void setPersonalConfigList(List<MenuConfig> personalConfigList) {
        this.personalConfigList = personalConfigList;
    }

    public class MenuConfig{
        private String functionName;
        private int functionNumber;
        private int sort;//排序号
        private String type;//所属栏目（1：免费应用 2：个性服务）
        private int status;//状态（0:有效 1:禁用）

        public String getFunctionName() {
            return functionName;
        }

        public void setFunctionName(String functionName) {
            this.functionName = functionName;
        }

        public int getFunctionNumber() {
            return functionNumber;
        }

        public void setFunctionNumber(int functionNumber) {
            this.functionNumber = functionNumber;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
