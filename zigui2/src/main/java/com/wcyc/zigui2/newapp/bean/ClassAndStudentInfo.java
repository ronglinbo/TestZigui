package com.wcyc.zigui2.newapp.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class ClassAndStudentInfo {

    public List<ClassStudentInfo> studentList1;

    public List<ClassStudentInfo> studentList2;

    public ServerResult serverResult;

    public static class ClassStudentInfo{
        public int gradeId;
        public int classId;
        public int studentId;
        public String studentName;
        public String className;
        public int schoolId;
        public String studentType;
        public String gradesName;
    }
}
