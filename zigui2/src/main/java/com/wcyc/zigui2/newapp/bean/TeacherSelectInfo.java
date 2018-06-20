package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiehua on 2017/5/23.
 */

public class TeacherSelectInfo implements Serializable {

    /**
     * info : {"groupsLeader":[{"name":"小学","year":"2016-2017","teacherGroups":[{"id":441,"name":"语文教研组"}],"schoolId":100,"code":"2"},{"name":"初中","year":"2016-2017","teacherGroups":[],"schoolId":100,"code":"3"},{"name":"高中","year":"2016-2017","teacherGroups":[],"schoolId":100,"code":"4"}],"classes":[{"id":1113,"classes":[{"id":5296,"name":"X1102"},{"id":5297,"name":"X1103"},{"id":5298,"name":"X1104"},{"id":5299,"name":"X1105"},{"id":5300,"name":"X1106"},{"id":5301,"name":"X1107"},{"id":5295,"name":"X1101（1班）"},{"id":5303,"name":"X1109"},{"id":5304,"name":"X1110"},{"id":5302,"name":"X1108"}],"name":"小学一年级","year":"2016-2017","realName":"小学一年级","gradeCode":"smallOne"},{"id":1114,"classes":[{"id":5305,"name":"X1201"},{"id":5306,"name":"X1202"},{"id":5307,"name":"X1203"},{"id":5308,"name":"X1204"},{"id":5309,"name":"X1205"},{"id":5310,"name":"X1206"},{"id":5311,"name":"X1207"},{"id":5312,"name":"X1208"},{"id":5313,"name":"X1209"},{"id":5314,"name":"X1210"},{"id":8119,"name":"测试"}],"name":"小学二年级","year":"2016-2017","realName":"小学二年级","gradeCode":"smallTwo"},{"id":1115,"classes":[{"id":5315,"name":"X1301"},{"id":5316,"name":"X1302"},{"id":5317,"name":"X1303"},{"id":5318,"name":"X1304"},{"id":5319,"name":"X1305"},{"id":5320,"name":"X1306"},{"id":5321,"name":"X1307"},{"id":5322,"name":"X1308"},{"id":5323,"name":"X1309"},{"id":5324,"name":"X1310"}],"name":"小学三年级","year":"2016-2017","realName":"小学三年级","gradeCode":"smallThree"},{"id":1116,"classes":[{"id":5325,"name":"X1401"},{"id":5326,"name":"X1402"},{"id":5327,"name":"X1403"},{"id":5328,"name":"X1404"},{"id":5329,"name":"X1405"},{"id":5330,"name":"X1406"},{"id":5331,"name":"X1407"},{"id":5332,"name":"X1408"},{"id":5333,"name":"X1409"},{"id":5334,"name":"X1410"}],"name":"小学四年级","year":"2016-2017","realName":"小学四年级","gradeCode":"smallFour"},{"id":1117,"classes":[],"name":"小学五年级","year":"2016-2017","realName":"小学五年级","gradeCode":"smallFive"},{"id":1118,"classes":[],"name":"小学六年级","year":"2016-2017","realName":"小学六年级","gradeCode":"smallSix"},{"id":1119,"classes":[{"id":5335,"name":"C2101"},{"id":5336,"name":"C2102"},{"id":5337,"name":"C2103"},{"id":5338,"name":"C2104"},{"id":5339,"name":"C2105"},{"id":5340,"name":"C2106"},{"id":5341,"name":"C2107"},{"id":5342,"name":"C2108"},{"id":5343,"name":"C2109"},{"id":5344,"name":"C2110"}],"name":"初中一年级","year":"2016-2017","realName":"初中一年级","gradeCode":"juniorOne"},{"id":1120,"classes":[{"id":5345,"name":"C2201"},{"id":5346,"name":"C2202"},{"id":5347,"name":"C2203"},{"id":5348,"name":"C2204"},{"id":5349,"name":"C2205"},{"id":5350,"name":"C2206"},{"id":5351,"name":"C2207"},{"id":5352,"name":"C2208"},{"id":5353,"name":"C2209"},{"id":5354,"name":"C2210"}],"name":"初中二年级","year":"2016-2017","realName":"初中二年级","gradeCode":"juniorTwo"},{"id":1121,"classes":[],"name":"初中三年级","year":"2016-2017","realName":"初中三年级","gradeCode":"juniorThree"},{"id":1122,"classes":[{"id":5355,"name":"G3101"},{"id":5356,"name":"G3102"},{"id":5357,"name":"G3103"},{"id":5358,"name":"G3104"},{"id":5359,"name":"G3105"},{"id":5360,"name":"G3106"},{"id":5361,"name":"G3107"},{"id":5362,"name":"G3108"},{"id":5363,"name":"G3109"},{"id":5364,"name":"G3110"}],"name":"高中一年级","year":"2016-2017","realName":"高中一年级","gradeCode":"seniorOne"},{"id":1123,"classes":[],"name":"高中二年级","year":"2016-2017","realName":"高中二年级","gradeCode":"seniorTwo"},{"id":1124,"classes":[],"name":"高中三年级","year":"2016-2017","realName":"高中三年级","gradeCode":"seniorThree"}],"gradesTeacher":[{"id":1113,"name":"小学一年级","schoolStage":"2","gradeCode":"smallOne"},{"id":1114,"name":"小学二年级","schoolStage":"2","gradeCode":"smallTwo"},{"id":1115,"name":"小学三年级","schoolStage":"2","gradeCode":"smallThree"},{"id":1116,"name":"小学四年级","schoolStage":"2","gradeCode":"smallFour"},{"id":1117,"name":"小学五年级","schoolStage":"2","gradeCode":"smallFive"},{"id":1118,"name":"小学六年级","schoolStage":"2","gradeCode":"smallSix"},{"id":1119,"name":"初中一年级","schoolStage":"3","gradeCode":"juniorOne"},{"id":1120,"name":"初中二年级","schoolStage":"3","gradeCode":"juniorTwo"},{"id":1121,"name":"初中三年级","schoolStage":"3","gradeCode":"juniorThree"},{"id":1122,"name":"高中一年级","schoolStage":"4","gradeCode":"seniorOne"},{"id":1123,"name":"高中二年级","schoolStage":"4","gradeCode":"seniorTwo"},{"id":1124,"name":"高中三年级","schoolStage":"4","gradeCode":"seniorThree"}],"prepareLessionLeader":[{"id":1113,"prepareLssions":[],"name":"小学一年级","year":"2016-2017","gradeCode":"smallOne"},{"id":1114,"prepareLssions":[],"name":"小学二年级","year":"2016-2017","gradeCode":"smallTwo"},{"id":1115,"prepareLssions":[],"name":"小学三年级","year":"2016-2017","gradeCode":"smallThree"},{"id":1116,"prepareLssions":[],"name":"小学四年级","year":"2016-2017","gradeCode":"smallFour"},{"id":1117,"prepareLssions":[],"name":"小学五年级","year":"2016-2017","gradeCode":"smallFive"},{"id":1118,"prepareLssions":[],"name":"小学六年级","year":"2016-2017","gradeCode":"smallSix"},{"id":1119,"prepareLssions":[],"name":"初中一年级","year":"2016-2017","gradeCode":"juniorOne"},{"id":1120,"prepareLssions":[],"name":"初中二年级","year":"2016-2017","gradeCode":"juniorTwo"},{"id":1121,"prepareLssions":[],"name":"初中三年级","year":"2016-2017","gradeCode":"juniorThree"},{"id":1122,"prepareLssions":[],"name":"高中一年级","year":"2016-2017","gradeCode":"seniorOne"},{"id":1123,"prepareLssions":[],"name":"高中二年级","year":"2016-2017","gradeCode":"seniorTwo"},{"id":1124,"prepareLssions":[],"name":"高中三年级","year":"2016-2017","gradeCode":"seniorThree"}],"prepareLession":[{"id":1113,"prepareLssions":[],"name":"小学一年级","year":"2016-2017","gradeCode":"smallOne"},{"id":1114,"prepareLssions":[],"name":"小学二年级","year":"2016-2017","gradeCode":"smallTwo"},{"id":1115,"prepareLssions":[],"name":"小学三年级","year":"2016-2017","gradeCode":"smallThree"},{"id":1116,"prepareLssions":[],"name":"小学四年级","year":"2016-2017","gradeCode":"smallFour"},{"id":1117,"prepareLssions":[],"name":"小学五年级","year":"2016-2017","gradeCode":"smallFive"},{"id":1118,"prepareLssions":[],"name":"小学六年级","year":"2016-2017","gradeCode":"smallSix"},{"id":1119,"prepareLssions":[],"name":"初中一年级","year":"2016-2017","gradeCode":"juniorOne"},{"id":1120,"prepareLssions":[],"name":"初中二年级","year":"2016-2017","gradeCode":"juniorTwo"},{"id":1121,"prepareLssions":[],"name":"初中三年级","year":"2016-2017","gradeCode":"juniorThree"},{"id":1122,"prepareLssions":[],"name":"高中一年级","year":"2016-2017","gradeCode":"seniorOne"},{"id":1123,"prepareLssions":[],"name":"高中二年级","year":"2016-2017","gradeCode":"seniorTwo"},{"id":1124,"prepareLssions":[],"name":"高中三年级","year":"2016-2017","gradeCode":"seniorThree"}],"contactGroup":[{"id":110,"name":"新增的"},{"id":109,"name":"范德萨范德萨"},{"id":108,"name":"测试"},{"id":96,"name":"管理员"}],"grades":[{"id":1113,"name":"小学一年级","schoolStage":"2","gradeCode":"smallOne"},{"id":1114,"name":"小学二年级","schoolStage":"2","gradeCode":"smallTwo"},{"id":1115,"name":"小学三年级","schoolStage":"2","gradeCode":"smallThree"},{"id":1116,"name":"小学四年级","schoolStage":"2","gradeCode":"smallFour"},{"id":1117,"name":"小学五年级","schoolStage":"2","gradeCode":"smallFive"},{"id":1118,"name":"小学六年级","schoolStage":"2","gradeCode":"smallSix"},{"id":1119,"name":"初中一年级","schoolStage":"3","gradeCode":"juniorOne"},{"id":1120,"name":"初中二年级","schoolStage":"3","gradeCode":"juniorTwo"},{"id":1121,"name":"初中三年级","schoolStage":"3","gradeCode":"juniorThree"},{"id":1122,"name":"高中一年级","schoolStage":"4","gradeCode":"seniorOne"},{"id":1123,"name":"高中二年级","schoolStage":"4","gradeCode":"seniorTwo"},{"id":1124,"name":"高中三年级","schoolStage":"4","gradeCode":"seniorThree"}],"groups":[{"name":"小学","year":"2016-2017","teacherGroups":[{"id":441,"name":"语文教研组"}],"schoolId":100,"code":"2"},{"name":"初中","year":"2016-2017","teacherGroups":[],"schoolId":100,"code":"3"},{"name":"高中","year":"2016-2017","teacherGroups":[],"schoolId":100,"code":"4"}],"departs":[{"id":192,"departmentName":"校级领导"},{"id":193,"departmentName":"教务科"},{"id":194,"departmentName":"学生科"}]}
     * serverResult : {"resultCode":200,"resultMessage":"成功"}
     */

    private InfoBean info=new InfoBean();
    private ServerResultBean serverResult;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public ServerResultBean getServerResult() {
        return serverResult;
    }

    public void setServerResult(ServerResultBean serverResult) {
        this.serverResult = serverResult;
    }

    public static class InfoBean implements  Serializable {
        private List<GroupsLeaderBean> groupsLeader=new ArrayList<GroupsLeaderBean>();//教研组长
        private List<ClassesBeanX> classes=new ArrayList<ClassesBeanX>();//年级班级
        private List<GradesTeacherBean> gradesTeacher=new ArrayList<GradesTeacherBean>();//班主任
        private List<PrepareLessionLeaderBean> prepareLessionLeader=new ArrayList<PrepareLessionLeaderBean>();//备课组长
        private List<PrepareLessionBean> prepareLession=new ArrayList<PrepareLessionBean>();//备课组
        private List<ContactGroupBean> contactGroup=new ArrayList<ContactGroupBean>();//
        private List<GradesBean> grades=new ArrayList<GradesBean>();
        private List<GroupsBean> groups=new ArrayList<GroupsBean>();//教研组
        private List<DepartsBean> departs=new ArrayList<DepartsBean>();//行政机构
        private List<GradeGroupBean> gradeGroup=new ArrayList<GradeGroupBean>();//年级组长


        public List<GroupsLeaderBean> getGroupsLeader() {
            return groupsLeader;
        }

        public void setGroupsLeader(List<GroupsLeaderBean> groupsLeader) {
            this.groupsLeader = groupsLeader;
        }

        public List<ClassesBeanX> getClasses() {
            return classes;
        }

        public void setClasses(List<ClassesBeanX> classes) {
            this.classes = classes;
        }

        public List<GradesTeacherBean> getGradesTeacher() {
            return gradesTeacher;
        }

        public void setGradesTeacher(List<GradesTeacherBean> gradesTeacher) {
            this.gradesTeacher = gradesTeacher;
        }

        public List<PrepareLessionLeaderBean> getPrepareLessionLeader() {
            return prepareLessionLeader;
        }

        public void setPrepareLessionLeader(List<PrepareLessionLeaderBean> prepareLessionLeader) {
            this.prepareLessionLeader = prepareLessionLeader;
        }

        public List<PrepareLessionBean> getPrepareLession() {
            return prepareLession;
        }

        public void setPrepareLession(List<PrepareLessionBean> prepareLession) {
            this.prepareLession = prepareLession;
        }

        public List<ContactGroupBean> getContactGroup() {
            return contactGroup;
        }

        public void setContactGroup(List<ContactGroupBean> contactGroup) {
            this.contactGroup = contactGroup;
        }

        public List<GradesBean> getGrades() {
            return grades;
        }

        public void setGrades(List<GradesBean> grades) {
            this.grades = grades;
        }

        public List<GroupsBean> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupsBean> groups) {
            this.groups = groups;
        }

        public List<DepartsBean> getDeparts() {
            return departs;
        }

        public void setDeparts(List<DepartsBean> departs) {
            this.departs = departs;
        }

        public List<GradeGroupBean> getGradeGroup() {
            return gradeGroup;
        }

        public void setGradeGroup(List<GradeGroupBean> gradeGroup) {
            this.gradeGroup = gradeGroup;
        }



        public static class GradeGroupBean implements Serializable {
            /**
             * name : 小学
             * year : 2015-2016
             * teacherGroups : []
             * schoolId : 35
             * code : 2
             */

            private String name;
            private String year;
            private int schoolId;
            private String code;
            private List<?> teacherGroups;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public int getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(int schoolId) {
                this.schoolId = schoolId;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public List<?> getTeacherGroups() {
                return teacherGroups;
            }

            public void setTeacherGroups(List<?> teacherGroups) {
                this.teacherGroups = teacherGroups;
            }
        }

        public static class GroupsLeaderBean  implements  Serializable {
            /**
             * name : 小学
             * year : 2016-2017
             * teacherGroups : [{"id":441,"name":"语文教研组"}]
             * schoolId : 100
             * code : 2
             */

            private String name;
            private String year;
            private int schoolId;
            private String code;
            private List<TeacherGroupsBean> teacherGroups=new ArrayList<TeacherGroupsBean>();

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public int getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(int schoolId) {
                this.schoolId = schoolId;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public List<TeacherGroupsBean> getTeacherGroups() {
                return teacherGroups;
            }

            public void setTeacherGroups(List<TeacherGroupsBean> teacherGroups) {
                this.teacherGroups = teacherGroups;
            }

            public static class TeacherGroupsBean  implements  Serializable {
                /**
                 * id : 441
                 * name : 语文教研组
                 */

                private int id;
                private String name;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }

        public static class ClassesBeanX  implements  Serializable{
            /**
             * id : 1113
             * classes : [{"id":5296,"name":"X1102"},{"id":5297,"name":"X1103"},{"id":5298,"name":"X1104"},{"id":5299,"name":"X1105"},{"id":5300,"name":"X1106"},{"id":5301,"name":"X1107"},{"id":5295,"name":"X1101（1班）"},{"id":5303,"name":"X1109"},{"id":5304,"name":"X1110"},{"id":5302,"name":"X1108"}]
             * name : 小学一年级
             * year : 2016-2017
             * realName : 小学一年级
             * gradeCode : smallOne
             */

            private int id;
            private String name;
            private String year;
            private String realName;
            private String gradeCode;
            private List<ClassesBean> classes=new ArrayList<ClassesBean>();

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public String getGradeCode() {
                return gradeCode;
            }

            public void setGradeCode(String gradeCode) {
                this.gradeCode = gradeCode;
            }

            public List<ClassesBean> getClasses() {
                return classes;
            }

            public void setClasses(List<ClassesBean> classes) {
                this.classes = classes;
            }

            public static class ClassesBean implements  Serializable{
                /**
                 * id : 5296
                 * name : X1102
                 */

                private int id;
                private String name;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }

        public static class GradesTeacherBean implements Serializable {
            /**
             * id : 1113
             * name : 小学一年级
             * schoolStage : 2
             * gradeCode : smallOne
             */

            private int id;
            private String name;
            private String schoolStage;
            private String gradeCode;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSchoolStage() {
                return schoolStage;
            }

            public void setSchoolStage(String schoolStage) {
                this.schoolStage = schoolStage;
            }

            public String getGradeCode() {
                return gradeCode;
            }

            public void setGradeCode(String gradeCode) {
                this.gradeCode = gradeCode;
            }
        }

        public static class PrepareLessionLeaderBean implements Serializable{
            /**
             * id : 1113
             * prepareLssions : []
             * name : 小学一年级
             * year : 2016-2017
             * gradeCode : smallOne
             */

            private int id;
            private String name;
            private String year;
            private String gradeCode;
            private List<?> prepareLssions=new ArrayList<Object>();

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getGradeCode() {
                return gradeCode;
            }

            public void setGradeCode(String gradeCode) {
                this.gradeCode = gradeCode;
            }

            public List<?> getPrepareLssions() {
                return prepareLssions;
            }

            public void setPrepareLssions(List<?> prepareLssions) {
                this.prepareLssions = prepareLssions;
            }

            //

        }

        public static class PrepareLessionBean implements Serializable{
            /**
             * id : 1113
             * prepareLssions : []
             * name : 小学一年级
             * year : 2016-2017
             * gradeCode : smallOne
             */

            private int id;
            private String name;
            private String year;
            private String gradeCode;
            private List<PrepareLssionsBeanX> prepareLssions=new ArrayList<PrepareLssionsBeanX>();

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getGradeCode() {
                return gradeCode;
            }

            public void setGradeCode(String gradeCode) {
                this.gradeCode = gradeCode;
            }

            public List<PrepareLssionsBeanX> getPrepareLssions() {
                return prepareLssions;
            }

            public void setPrepareLssions(List<PrepareLssionsBeanX> prepareLssions) {
                this.prepareLssions = prepareLssions;
            }
            public static class PrepareLssionsBeanX implements Serializable{
                /**
                 * id : 3587
                 * name : 数学备课组
                 */

                private int id;
                private String name;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }



        }

        public static class ContactGroupBean implements Serializable{
            /**
             * id : 110
             * name : 新增的
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class GradesBean implements Serializable{
            /**
             * id : 1113
             * name : 小学一年级
             * schoolStage : 2
             * gradeCode : smallOne
             */

            private int id;
            private String name;
            private String schoolStage;
            private String gradeCode;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSchoolStage() {
                return schoolStage;
            }

            public void setSchoolStage(String schoolStage) {
                this.schoolStage = schoolStage;
            }

            public String getGradeCode() {
                return gradeCode;
            }

            public void setGradeCode(String gradeCode) {
                this.gradeCode = gradeCode;
            }
        }

        public static class GroupsBean implements Serializable{
            /**
             * name : 小学
             * year : 2016-2017
             * teacherGroups : [{"id":441,"name":"语文教研组"}]
             * schoolId : 100
             * code : 2
             */

            private String name;
            private String year;
            private int schoolId;
            private String code;
            private List<TeacherGroupsBeanX> teacherGroups=new ArrayList<TeacherGroupsBeanX>();

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public int getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(int schoolId) {
                this.schoolId = schoolId;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public List<TeacherGroupsBeanX> getTeacherGroups() {
                return teacherGroups;
            }

            public void setTeacherGroups(List<TeacherGroupsBeanX> teacherGroups) {
                this.teacherGroups = teacherGroups;
            }

            public static class TeacherGroupsBeanX implements Serializable{
                /**
                 * id : 441
                 * name : 语文教研组
                 */

                private int id;
                private String name;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }

        public static class DepartsBean implements Serializable {
            /**
             * id : 192
             * departmentName : 校级领导
             */

            private int id;
            private String departmentName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getDepartmentName() {
                return departmentName;
            }

            public void setDepartmentName(String departmentName) {
                this.departmentName = departmentName;
            }
        }
    }

    public static class ServerResultBean implements Serializable{
        /**
         * resultCode : 200
         * resultMessage : 成功
         */

        private int resultCode;
        private String resultMessage;

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMessage() {
            return resultMessage;
        }

        public void setResultMessage(String resultMessage) {
            this.resultMessage = resultMessage;
        }
    }
}
