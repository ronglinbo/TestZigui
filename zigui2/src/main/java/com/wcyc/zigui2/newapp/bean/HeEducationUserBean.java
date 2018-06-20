package com.wcyc.zigui2.newapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author zzc
 * @time 2018/3/9 0009
 */
public class HeEducationUserBean implements Serializable {


    private static final long serialVersionUID = -5966663151904259255L;
    /**
     * freeQueryResult : 0
     * result : {"msg":"成功","ret":0,"userInfo":{"birthday":"","nickName":"学生的家长","phone":"13457680002","picture":"","sex":0,"studentData":[{"birthday":"","classId":10002442,"kindred":3,"sex":0,"studentName":"学生","updateTime":"2018-03-06 15:01:16","userId":600027830}],"updateTime":"2018-03-06 00:00:00","userId":600027831,"userType":3}}
     * schoolId : 400002079
     * schoolName : 测试学校
     */

    private int freeQueryResult;
    private ResultBean result;
    private String schoolId;
    private String schoolName;

    public int getFreeQueryResult() {
        return freeQueryResult;
    }

    public void setFreeQueryResult(int freeQueryResult) {
        this.freeQueryResult = freeQueryResult;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public static class ResultBean {
        /**
         * msg : 成功
         * ret : 0
         * userInfo : {"birthday":"","nickName":"学生的家长","phone":"13457680002","picture":"","sex":0,"studentData":[{"birthday":"","classId":10002442,"kindred":3,"sex":0,"studentName":"学生","updateTime":"2018-03-06 15:01:16","userId":600027830}],"updateTime":"2018-03-06 00:00:00","userId":600027831,"userType":3}
         */

        private String msg;
        private int ret;
        private UserInfoBean userInfo;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfoBean {
            /**
             * birthday :
             * nickName : 学生的家长
             * phone : 13457680002
             * picture :
             * sex : 0
             * studentData : [{"birthday":"","classId":10002442,"kindred":3,"sex":0,"studentName":"学生","updateTime":"2018-03-06 15:01:16","userId":600027830}]
             * updateTime : 2018-03-06 00:00:00
             * userId : 600027831
             * userType : 3
             */

            private String birthday;
            private String nickName;
            private String phone;
            private String picture;
            private int sex;
            private String updateTime;
            private int userId;
            private int userType;
            private List<StudentDataBean> studentData;

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public List<StudentDataBean> getStudentData() {
                return studentData;
            }

            public void setStudentData(List<StudentDataBean> studentData) {
                this.studentData = studentData;
            }

            public static class StudentDataBean {
                /**
                 * birthday :
                 * classId : 10002442
                 * kindred : 3
                 * sex : 0
                 * studentName : 学生
                 * updateTime : 2018-03-06 15:01:16
                 * userId : 600027830
                 */

                private String birthday;
                private int classId;
                private int kindred;
                private int sex;
                private String studentName;
                private String updateTime;
                private int userId;

                public String getBirthday() {
                    return birthday;
                }

                public void setBirthday(String birthday) {
                    this.birthday = birthday;
                }

                public int getClassId() {
                    return classId;
                }

                public void setClassId(int classId) {
                    this.classId = classId;
                }

                public int getKindred() {
                    return kindred;
                }

                public void setKindred(int kindred) {
                    this.kindred = kindred;
                }

                public int getSex() {
                    return sex;
                }

                public void setSex(int sex) {
                    this.sex = sex;
                }

                public String getStudentName() {
                    return studentName;
                }

                public void setStudentName(String studentName) {
                    this.studentName = studentName;
                }

                public String getUpdateTime() {
                    return updateTime;
                }

                public void setUpdateTime(String updateTime) {
                    this.updateTime = updateTime;
                }

                public int getUserId() {
                    return userId;
                }

                public void setUserId(int userId) {
                    this.userId = userId;
                }
            }
        }
    }
}
