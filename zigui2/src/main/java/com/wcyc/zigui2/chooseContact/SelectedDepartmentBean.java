package com.wcyc.zigui2.chooseContact;

import java.io.Serializable;

/**
 * Created by win0 on 2017/5/27.
 */
public class SelectedDepartmentBean implements Serializable{
    public String department;   //部门名称

    public String secondDepartment; //部门上级

    public String header;//首字母

    @Override
    public String toString() {
        return "SelectedDepartmentBean{" +
                "department='" + department + '\'' +
                ", secondDepartment='" + secondDepartment + '\'' +
                ", header='" + header + '\'' +
                '}';
    }
}
