package com.wcyc.zigui2.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.core.WebviewActivity;
import com.wcyc.zigui2.newapp.activity.ApplyForMaintainActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.module.studyresource.ZiguiCourseActivity;
import com.wcyc.zigui2.newapp.bean.UserType;

import org.json.JSONException;
import org.json.JSONObject;

public class GoHtml5Function {
    static Context mContext;
    static String weikeLogin = "http://sso.vko.cn/thdLogin?";
    static String[][] gradeNames = {{"初中一年级", "七年级"},
            {"初中二年级", "八年级"},
            {"初中三年级", "九年级"},
            {"高中一年级", "高中"},
            {"高中二年级", "高中"},
            {"高中三年级", "高中"}};

    public static void goToHtmlApp(Context context, String function,int functionNumber) {
        mContext = context;
        UserType user = CCApplication.getInstance().getPresentUser();
        boolean isParent = Constants.PARENT_STR_TYPE.equals(user.getUserType());
        String studentId = user.getChildId();
        HashMap<String, String> para = new HashMap<String, String>();
        para.put("X-School-Id", user.getSchoolId());
        para.put("X-mobile-Type", "android");
        System.out.println("goToHtmlApp:" + function+"----functionNumber:"+functionNumber+"----");

        if ("作息时间".equals(function)||functionNumber==MenuItem.TIMETABLE_NUMBER) {
            goTimeTable(para);
        } else if ("常用网址".equals(function)||functionNumber==MenuItem.WEBSITE_NUMBER) {
            goWebSite(para);
        }else if("兴趣班选课".equals(function)||functionNumber==MenuItem.INTEREST_COURSE_NUMBER){
            para.put("X-User-Id", user.getUserId());
            goInterestCourse(para,studentId);
        } else if ("校历".equals(function)||functionNumber==MenuItem.CALENDAR_NUMBER) {
            goSchoolCalendar(para);
        } else if ("作业".equals(function)||functionNumber==MenuItem.HOMEWORK_NUMBER) {
            if (isParent) {
                goHomeWorkChild(para, studentId);
            } else {
                para.put("X-User-Id", user.getUserId());
                goHomeWork(para);
            }

            //人工考勤
        } else if ("人工考勤".equals(function)||functionNumber==MenuItem.MANUAL_ATTE_NUMBER) {
            if (isParent) {
                goAttendenceChild(para, studentId, function,functionNumber);
            } else {
                para.put("X-User-Id", user.getUserId());
                goAttendence(para, function,functionNumber);
            }

        } else if ("进出校考勤".equals(function)||functionNumber==MenuItem.OUTINTO_SOL_ATTE_NUMBER) {
            if (isParent) {
                goAttendenceChild(para, studentId, function,functionNumber);
            } else {
                para.put("X-User-Id", user.getUserId());
                goAttendence(para, function,functionNumber);
            }
        } else if ("宿舍考勤".equals(function)||functionNumber==MenuItem.DORMITORY_ATTE_NUMBER) {
            if (isParent) {
                goAttendenceChild(para, studentId, function,functionNumber);
            } else {
                para.put("X-User-Id", user.getUserId());
                goAttendence(para, function,functionNumber);
            }
        } else if ("校车考勤".equals(function)||functionNumber==MenuItem.SCHOOL_BUS_ATTE_NUMBER) {
            if (isParent) {
                goAttendenceChild(para, studentId, function,functionNumber);
            } else {
                para.put("X-User-Id", user.getUserId());
                goAttendence(para, function,functionNumber);
            }
        } else if ("点评".equals(function)||functionNumber==MenuItem.COMMENT_NUMBER) {
            if (isParent) {
                goCommentChild(para, studentId);
            } else {
                para.put("X-User-Id", user.getUserId());
                goComment(para);
            }
        } else if ("考试".equals(function)||functionNumber == MenuItem.EXAM_NUMBER) {
            para.put("X-User-Id", user.getUserId());
            goExam(para);
        } else if ("成绩".equals(function)||functionNumber==MenuItem.SCORE_NUMBER) {
            para.put("X-User-Id", user.getUserId());
            if (isParent) {
                goScoreChild(para, studentId);
            } else {
                goScore(para);
            }
        } else if ("课程表".equals(function)||functionNumber==MenuItem.SCHEDULE_NUMBER) {
            if (isParent) {
                para.put("X-Class-Id", user.getClassId());
                para.put("schoolId", user.getSchoolId());
                para.put("classId", user.getClassId());
                goCourseChild(para);
            } else {
                para.put("X-User-Id", user.getUserId());
                goCourse(para);
            }
        } else if ("业务办理".equals(function)||functionNumber==MenuItem.HANDLE_BUSINESS_NUMBER) {
            para.put("X-User-Id", user.getUserId());
//			para.put("X-User-Id", "5");
//			para.put("X-School-Id", "10001");

            goBusiness(para);

        } else if ("待办事项".equals(function)) {
            para.put("X-User-Id", user.getUserId());
            goSchedule(para);
        } else if ("同步学习".equals(function)) {
            String url = "http://www.vko.cn/learning/synchro.html";
            url = loginWeikeWeb(url);
            goWeb(url, function);
        } else if ("同步试题".equals(function)) {
            String url = "http://tiku.vko.cn/v8/exam";
            url = loginWeikeWeb(url);
            goWeb(url, function);
        } else if ("推荐课程".equals(function)) {
            String url = "http://www.vko.cn/learning/special_all.html";
            url = loginWeikeWeb(url);
            goWeb(url, function);
        } else if ("同步练习".equals(function)) {
            String url = "http://haojiazhang123.com/share/ziguixiao/zhuyeliu.html";
            goWeb(url, function);
        } else if ("口算王".equals(function)) {
            String url = "http://arithmaticking.sinaapp.com/";
            goWeb(url, function);
        } else if ("听写助手".equals(function)) {
            String url = "http://namibox.com/v/dictate?x=zUUpgAAAAACl";
            goWeb(url, function);
        } else if ("汉语大词典".equals(function)) {
            String url = "http://namibox.com/dict";
            goWeb(url, function);
        } else if ("汉字笔画".equals(function)) {
            String url = "http://namibox.com/dict/zidian?kw=子";
            goWeb(url, function);
        } else if ("专项练习".equals(function)) {
            String url = "http://haojiazhang123.com/share/ziguixiao/zhuanxiangxunlian.html";
            goWeb(url, function);
        } else if ("家长学校".equals(function)||functionNumber==MenuItem.PARENTSCHOOL_NUMBER) {
            String url = "http://haojiazhang123.com/share/ziguixiao/parents.html";
            goWeb(url, function);
        } else if ("在线课程".equals(function)) {
            String url = "http://haojiazhang123.com/share/ziguixiao/onlineCourseHome.html";
            goWeb(url, function);
        } else if ("子贵课堂".equals(function)||functionNumber==MenuItem.COURSE_NUMBER) {
            goZiguiCourse();
        } else if ("小学宝".equals(function)||functionNumber==MenuItem.PRIMARYSCHOOL_NUMBER) {
            para.put("X-User-Id", user.getUserId());
            para.put("X-School-Id", user.getSchoolId());
            goH5Web(Constants.PRIMARY_SCHOOL_RESOURCE, para);
        } else if ("微课网".equals(function)||functionNumber==MenuItem.WEIKE_NUMBER) {
            para.put("X-User-Id", user.getUserId());
            para.put("schoolid", user.getSchoolId());
            goH5Web(Constants.WEIKE, para);
        }
    }

    // 作息时间
    public static void goTimeTable(HashMap<String, String> para) {
        System.out.println("goTimeTable");
        goHtml5(Constants.TIME_TABLE, para);
    }

    // 常用网址
    public static void goWebSite(HashMap<String, String> para) {
        System.out.println("goTimeTable");
        goHtml5(Constants.WEBSITES, para);
    }

    // 校历
    public static void goSchoolCalendar(HashMap<String, String> para) {
        System.out.println("goSchoolCalendar");
        goHtml5(Constants.SCHOOL_CALENDAR, para);
    }

    // 作业列表老师端
    public static void goHomeWork(HashMap<String, String> para) {
        System.out.println("goHomeWork");
        goHtml5(Constants.HOME_WORK_URL, para);
    }

    // 作业家长端
    public static void goHomeWorkChild(HashMap<String, String> para,
                                       String studentId) {
        System.out.println("goHomeWorkChild");
        goHtml5(Constants.HOMEWORK_CHILD, studentId, para);
    }

    // 考勤老师端
    public static void goAttendence(HashMap<String, String> para, String fuction,int functionNumber) {
        System.out.println("goAttendence");


        if (fuction.equals("人工考勤")||functionNumber==MenuItem.MANUAL_ATTE_NUMBER) {
            goHtml5(Constants.ATTENDANCE, para);
        } else if (fuction.equals("校车考勤")||functionNumber==MenuItem.SCHOOL_BUS_ATTE_NUMBER) {
            goHtml5(Constants.SCHOOL_BUS_ATTENDANCE, para);
        } else if (fuction.equals("进出校考勤")||functionNumber==MenuItem.OUTINTO_SOL_ATTE_NUMBER) {
            goHtml5(Constants.OUTINTO_SCHOOL_ATTENDANCE, para);
        } else if (fuction.equals("宿舍考勤")||functionNumber==MenuItem.DORMITORY_ATTE_NUMBER) {
            goHtml5(Constants.DORMITORY_ATTENDANCE, para);
        }
    }

    // 考勤家长端
    public static void goAttendenceChild(HashMap<String, String> para,
                                         String studentId, String fuction,int functionNumber) {
        System.out.println("goAttendenceChild");

        if (fuction.equals("人工考勤")||functionNumber==MenuItem.MANUAL_ATTE_NUMBER) {
            goHtml5(Constants.ATTENDANCE_CHILD, studentId, para);
        } else if (fuction.equals("校车考勤")||functionNumber==MenuItem.SCHOOL_BUS_ATTE_NUMBER) {
            goHtml5(Constants.SCHOOL_BUS_ATTENDANCE_CHILD, studentId, para);
        } else if (fuction.equals("进出校考勤")||functionNumber==MenuItem.OUTINTO_SOL_ATTE_NUMBER) {
            goHtml5(Constants.OUT_INTO_SCHOOL_ATTENDANCE_CHILD, studentId, para);
        } else if (fuction.equals("宿舍考勤")||functionNumber==MenuItem.DORMITORY_ATTE_NUMBER) {
            goHtml5(Constants.DORMITORY_ATTENDANCE_CHILD, studentId, para);
        }

    }

    // 考勤老师端-人工考勤
    public static void goAttendenceChildById(HashMap<String, String> para,
                                             String studentId) {
        System.out.println("goAttendenceChildById");
        goHtml5(Constants.ATTENDANCE_BY_ID, studentId, para);
    }

    //考勤老师端-进出校考勤
    public static void goSchoolAttendenceChildById(HashMap<String, String> para,
                                                   String studentId) {
        System.out.println("goAttendenceChildById");
        goHtml5(Constants.ATTENDANCE_SCHOOL_BY_ID, studentId, para);
    }

    //考勤老师端-校车考勤
    public static void goSchoolBusAttendenceChildById(HashMap<String, String> para,
                                                      String studentId) {
        System.out.println("goAttendenceChildById");
        goHtml5(Constants.ATTENDANCE_SCHOOL_BUS_BY_ID, studentId, para);
    }

    //考勤老是端-宿舍考勤
    public static void goDormAttendenceChildById(HashMap<String, String> para,
                                                 String studentId) {
        System.out.println("goAttendenceChildById");
        goHtml5(Constants.ATTENDANCE_DORM_BY_ID, studentId, para);
    }

    // 点评老师端
    public static void goComment(HashMap<String, String> para) {
        System.out.println("goComment");
        goHtml5(Constants.COMMENT, para);
    }

    // 点评老师端详情
    public static void goCommentById(HashMap<String, String> para,
                                     String studentId) {
        System.out.println("goCommentById");
        goHtml5(Constants.COMMENT_BY_ID, studentId, para);
    }

    // 点评家长端
    public static void goCommentChild(HashMap<String, String> para,
                                      String studentId) {
        System.out.println("goCommentChild");
        goHtml5(Constants.COMMENT_CHILD, studentId, para);
    }

    // 考试
    public static void goExam(HashMap<String, String> para) {
        System.out.println("goExam");
        goHtml5(Constants.EXAM_LIST, para);
    }

    // 成绩
    public static void goScore(HashMap<String, String> para) {
        System.out.println("goScore");
        goHtml5(Constants.SCORE, para);
    }

    // 成绩老师端详情
    public static void goScoreChildById(HashMap<String, String> para,
                                        String studentId) {
        System.out.println("goScoreChildById");
        goHtml5(Constants.SCORE_BY_ID, studentId, para);
    }

    // 课程表
    public static void goCourse(HashMap<String, String> para) {
        System.out.println("goCourse");
        goHtml5(Constants.COURSE, para);
    }

    // 课表家长端
    public static void goCourseChild(HashMap<String, String> para) {
        System.out.println("goCourseChild");
        goHtml5(Constants.SCHEDULE_CHILD, para);
    }

    //成绩家长端
    public static void goScoreChild(HashMap<String, String> para, String studentId) {
        System.out.println("goScoreChild");
        goHtml5(Constants.SCORE_CHILD, studentId, para);
    }

    //业务办理
    public static void goBusiness(HashMap<String, String> para) {
        System.out.println("goBusiness");
        goHtml5(Constants.BUSINESS, para);
    }

    //代办事项
    public static void goSchedule(HashMap<String, String> para) {
        System.out.println("goSchedule");
        goHtml5(Constants.SCHEDULE, para);
    }

    //兴趣班选课
    public static void goInterestCourse(HashMap<String, String> para, String studentId) {
        System.out.println("goInterestCourse");
        UserType user = CCApplication.getInstance().getPresentUser();
        goHtml5(Constants.INTEREST_COURSE_SELECT, studentId, para,user.getUserId());
    }

    //子贵课堂
    public static void goZiguiCourse() {
        System.out.println("goZiguiCourse");
        Bundle bundle = new Bundle();
        UserType user = CCApplication.getInstance().getPresentUser();
        boolean isParent = CCApplication.getInstance().isCurUserParent();
        boolean isCharge = CCApplication.getInstance().isServiceExpired(MenuItem.COURSE_NUMBER,"子贵课堂");
        if (user != null) {
            String gradeName = null, gradeId = null;
            StringBuilder para = new StringBuilder();
            para.append("userId=");
            para.append(user.getUserId());
            para.append("&userType=");
            para.append(user.getUserType());
            para.append("&isVip=");
            String isVip = (isParent) ? ((isCharge) ? "1" : "0") : "1";
            para.append(isVip);
            para.append("&childId=");
            if (!isParent) {
                para.append(user.getUserId());
            } else {
                para.append(user.getChildId());
            }

            para.append("&gradeId=");

            if (isParent) {
                gradeId = user.getGradeId();
                gradeName = user.getGradeName();
            } else {
                MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
                if (detail != null) {
                    List<NewClasses> list = detail.getClassList();
                    if (list != null) {
                        NewClasses item = list.get(0);
                        if (item != null) {
                            gradeId = item.getGradeId();
                            gradeName = item.getGradeName();
                        }
                    }
                }
                gradeName = "";
            }
            para.append(gradeId);
            //  gradeName = ConvertGradeName(gradeName);
            para.append("&gradeName=");
            para.append(gradeName);
            para.append("&phone=");
            para.append(CCApplication.getInstance().getMemberInfo().getMobile());
            if (user.getUserType().equals("2")) {
                para.append("&name=");
                para.append(user.getTeacherName());
            } else {
                para.append("&name=");
                para.append(CCApplication.getInstance().getPresentUser().getChildName() + CCApplication.getInstance().getPresentUser().getRelationTypeName());
            }

            para.append("&img=");
            if (CCApplication.getInstance().getMemberInfo().getUserIconURL() != null) {
                para.append(DataUtil.getIconURL(CCApplication.getInstance().getMemberInfo().getUserIconURL()));
            }

            String url = new StringBuilder(Constants.ziguiUrl).append(para)
                    .toString();
            bundle.putString("url", url);
            System.out.println("url:" + url);
            newActivity(ZiguiCourseActivity.class, bundle);
        }
    }

    private static String ConvertGradeName(String gradeName) {
        if (gradeName == null) return gradeNames[0][1];
        for (String[] string : gradeNames) {
            if (gradeName.equals(string[0])) {
                return string[1];
            }
        }
        return gradeName;
    }

    public static void goHtml5(String appUrl, HashMap<String, String> para) {
        Bundle bundle = new Bundle();
        String url = new StringBuilder(Constants.WEBVIEW_URL).append(appUrl)
                .toString();
        bundle.putString("url", url);
        System.out.println("url2:" + url);
        bundle.putSerializable("para", para);
        newActivity(BaseWebviewActivity.class, bundle);
    }

    public static void goHtml5(String appUrl, String studentId,
                               HashMap<String, String> para) {
        Bundle bundle = new Bundle();
        String url = new StringBuilder(Constants.WEBVIEW_URL).append(appUrl)
                .append("?studentId=" + studentId).toString();
        bundle.putString("url", url);
        bundle.putSerializable("para", para);
        System.out.println("url:" + url);
        newActivity(BaseWebviewActivity.class, bundle);
    }

    public static void goHtml5(String appUrl, String studentId,
                               HashMap<String, String> para,String userId) {
        Bundle bundle = new Bundle();
        String url = new StringBuilder(Constants.WEBVIEW_URL).append(appUrl)
                .append("?studentId=").append(studentId)
                .append("&userId=").append(userId).toString();
        bundle.putString("url", url);
        bundle.putSerializable("para", para);
        System.out.println("url:" + url);
        newActivity(BaseWebviewActivity.class, bundle);
    }

    //三方web资源
    public static void goWeb(String url, String title) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        bundle.putBoolean("canGoBack", true);
        System.out.println("url:" + url);
        newActivity(BaseWebviewActivity.class, bundle);
    }

    public static String loginWeikeWeb(String url) {
        String thdCode = "thdCode=1001";
        String userId = "&userId=";
        String sign = "&sign=";
        String toUrl = "&toUrl=";
        String backUrl = "&backUrl=";
        String adid = "&adid=";
        String key = "&key=ziguiwangvkopartner00000";
        String time = "&time=" + System.currentTimeMillis();
        StringBuffer loginUrl = new StringBuffer();
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            userId += user.getUserId();
        }
        String str = thdCode + userId + adid + time + key;
        sendInfoToServer(user, url);
        sign += DataUtil.encodeMD5(str);
        try {
            toUrl += URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        loginUrl.append(weikeLogin);
        loginUrl.append(thdCode);
        loginUrl.append(toUrl);
        loginUrl.append(userId);
        loginUrl.append(sign);
        loginUrl.append(backUrl);
        loginUrl.append(key);
        loginUrl.append(adid);
        loginUrl.append(time);

        return loginUrl.toString();
    }

    //记录登录微课需要保存相关的用户信息
    private static void sendInfoToServer(UserType user, String url) {
        JSONObject json = new JSONObject();

        try {
            json.put("platformType", 3);
            if (Constants.PARENT_STR_TYPE.equals(user.getUserType())) {
                json.put("userCode", user.getChildId());
                json.put("userType", "1");
            } else {
                json.put("userCode", user.getUserId());
                json.put("userType", user.getUserType());
            }
            json.put("vkoUrl", url);

            String server = new StringBuilder(Constants.SERVER_URL)
                    .append(Constants.SEND_VEIKE_RECORD).toString();
            try {
                String ret = HttpHelper.httpPostJson(mContext, server, json);
                System.out.println("sendInfoToServer:" + ret);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到新的activity
     *
     * @param cls
     */
    public static void newActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(mContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    public static void goH5Web(String url, HashMap<String, String> para) {
        UserType user = CCApplication.getInstance().getPresentUser();
        boolean isParent = CCApplication.getInstance().isCurUserParent();
        if (user != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            sb.append("?userType=" + user.getUserType());
            if (isParent) {
                sb.append("&childId=" + user.getChildId());
            }
            sb.append("&productCode=23");
            System.out.print(sb.toString());
            goWebViewActivity(sb.toString(), para);
        }
    }

    public static void goWebViewActivity(String appUrl, HashMap<String, String> para) {
        Bundle bundle = new Bundle();
        String url = new StringBuilder(Constants.WEBVIEW_URL).append(appUrl)
                .toString();
        bundle.putString("url", url);
        bundle.putSerializable("para", para);
        newActivity(WebviewActivity.class, bundle);
    }
}