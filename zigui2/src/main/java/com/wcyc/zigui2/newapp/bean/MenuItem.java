package com.wcyc.zigui2.newapp.bean;

import com.dh.groupTree.GroupListActivity;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.NewEducationInfoActivity;
import com.wcyc.zigui2.newapp.activity.PayNoticeActivity;
import com.wcyc.zigui2.newapp.activity.PaymentListActivity;
import com.wcyc.zigui2.newapp.activity.SystemMessageActivity;
import com.wcyc.zigui2.newapp.module.classdynamics.NewClassDynamicsActivity;
import com.wcyc.zigui2.newapp.module.consume.NewConsumeActivity;
import com.wcyc.zigui2.newapp.module.consume.OneCardActivity;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordActivity;
import com.wcyc.zigui2.newapp.module.duty.NewDutyInquiryActivity;
import com.wcyc.zigui2.newapp.module.email.EmailActivity;
import com.wcyc.zigui2.newapp.module.email.MenuConfigBean;
import com.wcyc.zigui2.newapp.module.leave.NewMyLeaveActivity;
import com.wcyc.zigui2.newapp.module.leavemessage.LeaveMeassageActivity;
import com.wcyc.zigui2.newapp.module.mailbox.SchoolMasterMailActivity;
import com.wcyc.zigui2.newapp.module.news.NewSchoolNewsActivity;
import com.wcyc.zigui2.newapp.module.notice.NotifyActivity;
import com.wcyc.zigui2.newapp.module.summary.SummaryActivity;
import com.wcyc.zigui2.newapp.module.wages.NewWagesActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.SPConstants;
import com.wcyc.zigui2.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//菜单项
public class MenuItem {
    private String itemType;
    private String itemName;
    private int itemNumber;
    private int resId;
    private Class ClassName;
    private int unreadNum;
    private int ProductType;
    private boolean isFree = true;//是否免费

    public MenuItem(String itemName) {
        this.itemName = itemName;
    }

    public MenuItem(String itemType, String itemName,int itemNumber, int resId, Class ClassName) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemNumber=itemNumber;
        this.resId = resId;
        this.ClassName = ClassName;
    }

    public MenuItem(int ProductType, String itemType, String itemName,int itemNumber, int resId, Class ClassName) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.itemNumber=itemNumber;
        this.resId = resId;
        this.ClassName = ClassName;
        this.setProductType(ProductType);
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public Class getClassName() {
        return ClassName;
    }

    public void setClassName(Class className) {
        ClassName = className;
    }

    public int getUnreadNum() {
        return unreadNum;
    }

    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }

    public void addUnreadNum(int unreadNum) {
        this.unreadNum += unreadNum;
    }

    public int getProductType() {
        return ProductType;
    }

    public void setProductType(int productType) {
        ProductType = productType;
    }

    public static final String FREE = "1";//1：免费应用 2：个性服务;
    public static final String CHARGE = "2";
    public static final int VALID = 0;//（0:有效 1:禁用）
    public static final int INVALID = 1;


//    /*
//    *
//    * 老师的
//    *
//    * */
//
//    public static  String NOTICE_TEACHER = "通知";
//    public static  String SCHEDULE_TEACHER = "课程表";
//    public static  String CONSUME_TEACHER = "卡消费充值";
//    public static  String EMAIL_TEACHER = "邮件";
//    public static  String SCHOOLNEWS_TEACHER = "校园新闻";
//    public static  String MONITER_TEACHER = "校园监控";
//    public static  String CALENDAR_TEACHER = "校历";
//    public static  String TIMETABLE_TEACHER = "作息时间";
//    public static  String SCHOOLMAIL_TEACHER = "校长信箱";
//    public static  String WEBSITE_TEACHER = "常用网址";
//    public static  String HOMEWORK_TEACHER = "作业";
//    public static  String SCORE_TEACHER = "成绩";
//    public static  String COMMENT_TEACHER = "点评";
//    public static  String DYNAMICS_TEACHER = "班级动态";
//    public static  String SCHOOL_BUS_ATT_TEACHER = "校车考勤";
//    public static  String DORMITORY_ATT_TEACHER = "宿舍考勤";
//    public static  String MANUAL_ATT_TEACHER = "人工考勤";
//    public static  String OUTINTO_SCHOOL_ATTTEACHER = "进出校考勤";

    /*
    *
    * 家长的
    * */
    public static  String NOTICE = "通知";
    public static  String SCHOOLNEWS = "校园新闻";
    public static  String SCHEDULE = "课程表";
    public static  String LEAVE = "请假条";
    public static  String SCHOOLMAIL = "校长信箱";
    public static  String CALENDAR = "校历";
    public static  String TIMETABLE = "作息时间";
    public static  String PARENTSCHOOL = "家长学校";
    public static  String DYNAMICS = "班级动态";
    public static  String HOMEWORK = "作业";
    public static  String SCORE = "成绩";
    public static  String ATTENDANCE = "考勤";
    public static  String COMMENT = "点评";
    public static  String CONSUME = "卡消费充值";
    public static  String COURSE = "子贵课堂";

    public static  String WEIKE = "微课网";
    public static  String PRIMARYSCHOOL = "小学宝";
    public static  String MONITER = "校园监控";
    public static  String MONITER1 = "子贵探视";
    public static  String EMAIL = "邮件";
    public static  String QUICK_SCHOOLMAIL = "给校长写信";
    public static  String QUICK_LEAVE = "请假申请";
    //新增
    public static  String LEAVE_MESSAGE = "班牌留言";

    //考勤新增
    public static  String SCHOOL_BUS_ATTENDANCE = "校车考勤";
    public static  String DORMITORY_ATTENDANCE = "宿舍考勤";
    public static  String OUTINTO_SCHOOL_ATTENDANCE = "进出校考勤";
    public static  String MANUAL_ATTENDANCE = "人工考勤";


    public static String DAILY_RECORD="日志";

    //新增网址导航
    public static  String WEBSITE = "常用网址";

    //新增兴趣班选课
    public static  String INTEREST_COURSE = "兴趣班选课";

    //新增兴趣班选课
    public static  String PAYMENT = "缴费";

    //一卡通充值
    public static  String ONE_CARD = "一卡通充值";


    //类型区别 1.免费应用   2.个性服务  3.学习资源

    static {
        getMenuItemName();
    }


    /*
    * 初始化MenuItemName
    * */
    public static void initMenuItemName(){
        NOTICE = "通知";
        SCHOOLNEWS = "校园新闻";
        SCHEDULE = "课程表";
        LEAVE = "请假条";
        SCHOOLMAIL = "校长信箱";
        CALENDAR = "校历";
        TIMETABLE = "作息时间";
        PARENTSCHOOL = "家长学校";
        DYNAMICS = "班级动态";
        HOMEWORK = "作业";
        SCORE = "成绩";
        ATTENDANCE = "考勤";
        COMMENT = "点评";
        CONSUME = "卡消费充值";
        COURSE = "子贵课堂";
        WEIKE = "微课网";
        PRIMARYSCHOOL = "小学宝";
        MONITER = "校园监控";
        MONITER1 = "子贵探视";
        EMAIL = "邮件";
        QUICK_SCHOOLMAIL = "给校长写信";
        QUICK_LEAVE = "请假申请";
        LEAVE_MESSAGE = "班牌留言";
        SCHOOL_BUS_ATTENDANCE = "校车考勤";
        DORMITORY_ATTENDANCE = "宿舍考勤";
        OUTINTO_SCHOOL_ATTENDANCE = "进出校考勤";
        MANUAL_ATTENDANCE = "人工考勤";
        WEBSITE = "常用网址";
        INTEREST_COURSE = "兴趣班选课";
        PAYMENT = "缴费";
        ONE_CARD = "一卡通充值";

        updateMenuItemName();
    }


    /**
     * 获取本地的MenuItemName
     */
    public static void getMenuItemName(){

        NOTICE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(NOTICE_NUMBER),NOTICE));  //通知
        SCHOOLNEWS= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(SCHOOLNEWS_NUMBER),SCHOOLNEWS));  //*校园新闻
        SCHEDULE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(SCHEDULE_NUMBER),SCHEDULE));  //*课程表
        LEAVE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(LEAVE_NUMBER),LEAVE));  //*请假条
        SCHOOLMAIL= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(SCHOOLMAIL_NUMBER),SCHOOLMAIL)); //*校长信箱
        CALENDAR= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(CALENDAR_NUMBER),CALENDAR));  //*校历
        TIMETABLE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(TIMETABLE_NUMBER),TIMETABLE));  //*作息时间
        PARENTSCHOOL= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(PARENTSCHOOL_NUMBER),PARENTSCHOOL));//*家长学校
        WEBSITE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(WEBSITE_NUMBER),WEBSITE));//*常用网址
        DYNAMICS= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(DYNAMICS_NUMBER),DYNAMICS));//班级动态
        HOMEWORK= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(HOMEWORK_NUMBER),HOMEWORK));//*作业
        SCORE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(SCORE_NUMBER),SCORE));//*成绩
        OUTINTO_SCHOOL_ATTENDANCE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(OUTINTO_SOL_ATTE_NUMBER),OUTINTO_SCHOOL_ATTENDANCE));//*进出校考勤
        DORMITORY_ATTENDANCE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(DORMITORY_ATTE_NUMBER),DORMITORY_ATTENDANCE)); //*宿舍考勤
        MANUAL_ATTENDANCE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(MANUAL_ATTE_NUMBER),MANUAL_ATTENDANCE)); //*人工考勤
        SCHOOL_BUS_ATTENDANCE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(SCHOOL_BUS_ATTE_NUMBER),SCHOOL_BUS_ATTENDANCE));//*校车考勤
        LEAVE_MESSAGE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(LEAVE_MESSAGE_NUMBER),LEAVE_MESSAGE));//*班牌留言
        CONSUME= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(CONSUME_NUMBER),CONSUME)); //*卡消费充值
        COMMENT= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(COMMENT_NUMBER),COMMENT));//*点评
        MONITER= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(MONITER_NUMBER),MONITER));//*子贵探视
        INTEREST_COURSE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(INTEREST_COURSE_NUMBER),INTEREST_COURSE));//*兴趣班选课
        ONE_CARD= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(ONE_CARD_NUMBER),ONE_CARD));//*一卡通充值
        PAYMENT= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(PAYMENT_NUMBER),PAYMENT));//*缴费
        COURSE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(COURSE_NUMBER),COURSE));//*子贵课堂
        WEIKE= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(WEIKE_NUMBER),WEIKE));//* 微课网
        MONITER1= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(MONITER1_NUMBER),MONITER1));//* 子贵探视

        updateMenuItemName();

    }


    /**
     * 更新MenuItemName
     */
    public static void updateMenuItemName(){
        try{
            leavemessage.setItemName(LEAVE_MESSAGE);
            notice.setItemName(NOTICE);
            schoolNews.setItemName(SCHOOLNEWS);
            schedule.setItemName(SCHEDULE);
            leave.setItemName(LEAVE);
            schoolMail.setItemName(SCHOOLMAIL);
            calendar.setItemName(CALENDAR);
            timeTable.setItemName(TIMETABLE);
            parentSchool.setItemName(PARENTSCHOOL);
            //网址导航
            website.setItemName(WEBSITE);
            //一卡通充值
            oneCard.setItemName(ONE_CARD);

            //兴趣班选课
            interestCourse.setItemName(INTEREST_COURSE);
            //缴费
            payment.setItemName(PAYMENT);
            dynamics.setItemName(DYNAMICS);
            homework.setItemName(HOMEWORK);
            score.setItemName(SCORE);
            attendance.setItemName(ATTENDANCE);
            comment.setItemName(COMMENT);
            consume.setItemName(CONSUME);
            course.setItemName(COURSE);
            weike.setItemName(WEIKE);
            primarySchool.setItemName(PRIMARYSCHOOL);
            moniter.setItemName(MONITER);
            moniter1.setItemName(MONITER1);

            email.setItemName(EMAIL);
            //30 进出校考勤 ；31 宿舍考勤 ； 32 校车考勤  11 人工考勤
            SchoolBusAttendance.setItemName(SCHOOL_BUS_ATTENDANCE);
            DormitoryAttendance.setItemName(DORMITORY_ATTENDANCE);
            OutIntoSchoolAttendance.setItemName(OUTINTO_SCHOOL_ATTENDANCE);
            ManualAttendance.setItemName(MANUAL_ATTENDANCE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void menuItemNameToSp(int key,String defaultValue){
        defaultValue= String.valueOf(SPUtils.get(CCApplication.applicationContext, SPConstants.MENUITEM_NAME_FILE,String.valueOf(key),defaultValue));  //通知
    }



    //functionNumber
    /*
    * NOTICE_NUMBER
    * */
    public static final int NOTICE_NUMBER=1;       //*通知             1
    public static final int SCHOOLNEWS_NUMBER=2;   //*校园新闻          2
    public static final int SCHEDULE_NUMBER=3;     //*课程表           3
    public static final int LEAVE_NUMBER=4;       //*请假条           4
    public static final int SCHOOLMAIL_NUMBER=5;     //*校长信箱         5
    public static final int CALENDAR_NUMBER=6;      //*校历             6
    public static final int TIMETABLE_NUMBER=7;     //*作息时间         7
    public static final int PARENTSCHOOL_NUMBER=8;     //*家长学校         8
    public static final int WEBSITE_NUMBER=9;        //*常用网址         9
    public static final int DYNAMICS_NUMBER=10;       //*班级动态         10
    public static final int HOMEWORK_NUMBER=11;        //*作业             11
    public static final int SCORE_NUMBER=12;           //*成绩             12
    public static final int OUTINTO_SOL_ATTE_NUMBER=13;  //*进出校考勤        13
    public static final int DORMITORY_ATTE_NUMBER=14;     //*宿舍考勤          14
    public static final int MANUAL_ATTE_NUMBER=15;        //*人工考勤          15
    public static final int SCHOOL_BUS_ATTE_NUMBER=16;  //*校车考勤          16
    public static final int LEAVE_MESSAGE_NUMBER =17;    //*班牌留言          17
    public static final int CONSUME_NUMBER=18;           //*卡消费充值        18
    public static final int COMMENT_NUMBER=20;          //*点评              20
    public static final int MONITER_NUMBER=21;         //*子贵探视           21
    public static final int INTEREST_COURSE_NUMBER=22;     //*兴趣班选课         22
    public static final int ONE_CARD_NUMBER=23;            //*一卡通充值        23
    public static final int PAYMENT_NUMBER=25;            //*缴费             25
    //*
    public static final int COURSE_NUMBER=-10001;          //*子贵课堂           -10001
    public static final int WEIKE_NUMBER=-10002;           //* 微课网            -10002
    //*小学资源           -10003
    public static final int MONITER1_NUMBER=-10004;        //* 子贵探视          -10004


    public static final int ATTENDANCE_NUMBER = 18761;      //考勤
    public static final int PRIMARYSCHOOL_NUMBER=18762;      //小学宝



    public static final int EMAIL_NUMBER=18701;         //邮件
    public static final int DAILY_RECORD_NUMBER=18702;  //日志
    public static final int SUMMARY_NUMBER=18703;       //总结
    public static final int EMAILPARENT_NUMBER=18704;   //邮件  没有用到
    public static final int SCHOOLMASTERPARENT_NUMBER=18705; //给校长写信
    public static final int LEAVEPARENT_NUMBER = 18706;  //请假申请


    public static final int CLASS_CIRCLE_NUMBER=18707;  //班级圈

    public static final int ATTENDANCE_INQUIRE_NUMBER=18708;  //值班查询


    public static final int EXAM_NUMBER=18709;                //考试
    public static final int HANDLE_BUSINESS_NUMBER=18710;      //业务办理




//    String[] service = {"通知", "邮件", "作业", "考勤", "点评", "成绩",
////                "课程表", "请假条", MenuItem.CONSUME, "校长信箱", "校历", "作息时间", "班级动态"
////                , MenuItem.PARENTSCHOOL, MenuItem.SCHOOLNEWS, "班牌留言", "人工考勤", "宿舍考勤", "校车考勤", "进出校考勤", "常用网址", "兴趣班选课", "缴费", "子贵探视", MenuItem.ONE_CARD};

    public static int nameToNumber(String name){
        if(name.equals(NOTICE)){ // = "通知";
            return NOTICE_NUMBER;
        }else if(name.equals(SCHOOLNEWS)){ // = "校园新闻";
            return SCHOOLNEWS_NUMBER;
        }else if(name.equals(SCHEDULE)){//= "课程表";
            return SCHEDULE_NUMBER;
        }else if(name.equals(LEAVE)){//= "请假条";
            return LEAVE_NUMBER;
        }else if(name.equals(SCHOOLMAIL)){ //= "校长信箱";
            return SCHOOLMAIL_NUMBER;
        }else if(name.equals(CALENDAR)){//= "校历";
            return CALENDAR_NUMBER;
        }else if(name.equals(TIMETABLE)){// = "作息时间";
            return TIMETABLE_NUMBER;
        }else if(name.equals(PARENTSCHOOL)){ // = "家长学校";
            return PARENTSCHOOL_NUMBER;
        }else if(name.equals(DYNAMICS)){//= "班级动态";
            return DYNAMICS_NUMBER;
        }else if(name.equals(HOMEWORK)){ // = "作业";
            return HOMEWORK_NUMBER;
        }else if(name.equals(SCORE)){//= "成绩";
            return SCORE_NUMBER;
        }else if(name.equals(ATTENDANCE)){  //= "考勤";
            return ATTENDANCE_NUMBER;
        }else if(name.equals(COMMENT)){ //= "点评";
            return COMMENT_NUMBER;
        }else if(name.equals(CONSUME)){ // = "卡消费充值";
            return CONSUME_NUMBER;
        }else if(name.equals(COURSE)){ //= "子贵课堂";
            return COURSE_NUMBER;
        }else if(name.equals(WEIKE)){// = "微课网";
            return WEIKE_NUMBER;
        }else if(name.equals(PRIMARYSCHOOL)){  // = "小学宝";
            return PRIMARYSCHOOL_NUMBER;
        }else if(name.equals(MONITER)){ // = "校园监控";
            return MONITER_NUMBER;
        }else if(name.equals(MONITER1)){   //= "子贵探视";
            return MONITER_NUMBER;
        }else if(name.equals(EMAIL)){ //= "邮件";
            return EMAIL_NUMBER;
        }else if(name.equals(QUICK_SCHOOLMAIL)){ //= "给校长写信";
            return SCHOOLMASTERPARENT_NUMBER;
        }else if(name.equals(QUICK_LEAVE)){// = "请假申请";
            return LEAVEPARENT_NUMBER;
        }else if(name.equals(LEAVE_MESSAGE)){// = "班牌留言";
            return LEAVE_MESSAGE_NUMBER;
        }else if(name.equals(SCHOOL_BUS_ATTENDANCE)){// = "校车考勤";
            return SCHOOL_BUS_ATTE_NUMBER;
        }else if(name.equals(DORMITORY_ATTENDANCE)){// = "宿舍考勤";
            return DORMITORY_ATTE_NUMBER;
        }else if(name.equals(OUTINTO_SCHOOL_ATTENDANCE)){// = "进出校考勤";
            return OUTINTO_SOL_ATTE_NUMBER;
        }else if(name.equals(MANUAL_ATTENDANCE)){//= "人工考勤";
            return MANUAL_ATTE_NUMBER;
        }else if(name.equals(WEBSITE)){    // = "常用网址";
            return WEBSITE_NUMBER;
        }else if(name.equals(INTEREST_COURSE)){//= "兴趣班选课";
            return INTEREST_COURSE_NUMBER;
        }else if(name.equals(PAYMENT)){// = "缴费";
            return PAYMENT_NUMBER;
        }else if(name.equals(ONE_CARD)){//= "一卡通充值";
            return ONE_CARD_NUMBER;
        }else if(name.equals(DAILY_RECORD)){
            return DAILY_RECORD_NUMBER;
        }

        return 0;

    }


//          private String[][] func =
//            {{"1", "系统消息"}, {"33", "订单催缴"}, {"34", "缴费"},
//            {"2", "通知1"}, {"3", "资源状态改变消息"}, {"4", "版本更新"}, {"5", "续费提醒"},
//            {"6", "成绩"}, {"7", "点评"}, {"8", "作业"}, {"9", "校园风采"},
//            {"10", "班级动态"}, {"11", "考勤"}, {"12", "回复意见"}, {"13", MenuItem.CONSUME},
//            {"14", "邮件"}, {"15", "待办事项"}, {"16", "工资条"}, {"17", "值班查询"},
//            {"18", "校长信箱"}, {"19", "日志"}, {"20", "总结"}, {"21", "考试"},
//            {"22", "业务办理"}, {"23", "学生请假单"}, {"24", "业务办理"}, {"25", "业务办理"}, {"29", "班牌留言"}};




    /**
     * 根据messageType获取messageName
     * @param messageType
     * @param messageName
     * @return
     */
     public static String getMessageName(String messageType,String messageName){
         if(messageType.equals("01")){
             return "系统消息";
         }else if(messageType.equals("02")){
             return NOTICE;
         }else if(messageType.equals("03")){
             return "资源状态改变消息";
         }else if(messageType.equals("04")){
             return "版本更新";
         }else if(messageType.equals("05")){
             return "续费提醒";
         }else if(messageType.equals("06")){
             return SCORE;
         }else if(messageType.equals("07")){
             return COMMENT;
         }else if(messageType.equals("08")){
             return HOMEWORK;
         }else if(messageType.equals("09")){
             return "校园风采";
         }else if(messageType.equals("10")){
             return DYNAMICS;
         }else if(messageType.equals("11")){
             return MANUAL_ATTENDANCE;
         }else if(messageType.equals("12")){
             return "回复意见";
         }else if(messageType.equals("13")){
             return CONSUME;
         }else if(messageType.equals("14")){
             return EMAIL;
         }else if(messageType.equals("15")){
             return "待办事项";
         }else if(messageType.equals("16")){
             return "工资条";
         }else if(messageType.equals("17")){
             return "值班查询";
         }else if(messageType.equals("18")){
             return SCHOOLMAIL;
         }else if(messageType.equals("19")){
             return "日志";
         }else if(messageType.equals("20")){
             return "总结";
         }else if(messageType.equals("21")){
             return "考试";
         }else if(messageType.equals("22")){
             return "业务办理";
         }else if(messageType.equals("23")){
             return LEAVE;
         }else if(messageType.equals("24")){
             return "业务办理";
         }else if(messageType.equals("25")){
             return "业务办理";
         }/*else if(messageType.equals("26")){
             return "一卡通考勤";
         }*/else if(messageType.equals("27")){
             return SCHOOLNEWS;
         }else if(messageType.equals("28")){
             return "教育资讯";
         }else if(messageType.equals("29")){
             return LEAVE_MESSAGE;
         }else if(messageType.equals("30")){
             return OUTINTO_SCHOOL_ATTENDANCE;
         }else if(messageType.equals("31")){
             return DORMITORY_ATTENDANCE;
         }else if(messageType.equals("32")){
             return SCHOOL_BUS_ATTENDANCE;
         }else if(messageType.equals("33")){
             return "订单催缴";
         }else if(messageType.equals("34")){
             return PAYMENT;
         }

         return messageName;
     }



    public static Class  getMessageToClass(String messageType){
        if(messageType.equals("02")){
            return NotifyActivity.class;
        }else if(messageType.equals("18")){
            return SchoolMasterMailActivity.class;
        }else if(messageType.equals("20")){
            return SummaryActivity.class;
        }else if(messageType.equals("14")){
            return EmailActivity.class;
        }else if(messageType.equals("16")){
            return NewWagesActivity.class;
        }else if(messageType.equals(LEAVE)){
            return NewMyLeaveActivity.class;
        }else if(messageType.equals("17")){
            return NewDutyInquiryActivity.class;
        }else if(messageType.equals(CONSUME)){
            return NewConsumeActivity.class;
        }else if(messageType.equals("10")){
            return NewClassDynamicsActivity.class;
        }else if(messageType.equals("27")){
            return NewSchoolNewsActivity.class;
        }else if(messageType.equals("28")){
            return NewEducationInfoActivity.class;
        }else if(messageType.equals("29")){
            return LeaveMeassageActivity.class;
        }else if(messageType.equals("01")){
            return SystemMessageActivity.class;
        }else if(messageType.equals("33")){
            return PayNoticeActivity.class;
        }else if(messageType.equals("34")){
            return PaymentListActivity.class;
        }else if(messageType.equals("19")){
            return DailyRecordActivity.class;
        }
        return null;
    }

//
//    //消息列表 Item 点击之后会跳转到相应的Activity
//    private HashMap activities = new HashMap() {
//        {
//            put(MenuItem.NOTICE/*"通知1"*/, NotifyActivity.class);
//            put(MenuItem.SCHOOLMAIL/*"校长信箱"*/, SchoolMasterMailActivity.class);
//            put("总结"/*"总结"*/, SummaryActivity.class);
//            put("日志"/*"日志"*/, DailyRecordActivity.class);
//            put(MenuItem.EMAIL/*"邮件"*/, EmailActivity.class);
//            put("工资条"/*"工资条"*/, NewWagesActivity.class);//
//            put(MenuItem.LEAVE/*"请假条"*/, NewMyLeaveActivity.class);//
//            put("值班查询"/*"值班查询"*/, NewDutyInquiryActivity.class);
//            put(MenuItem.CONSUME, NewConsumeActivity.class);
//            put(MenuItem.DYNAMICS/*"班级动态"*/, NewClassDynamicsActivity.class);
//            put(MenuItem.SCHOOLNEWS/*"校园新闻"*/, NewSchoolNewsActivity.class);
////            put("教育资讯", EducationInforActivity.class);
//            put("教育资讯"/*"教育资讯"*/, NewEducationInfoActivity.class); //新版的教育资讯页面
//            put(MenuItem.LEAVE_MESSAGE/*"班牌留言"*/, LeaveMeassageActivity.class);
//
//            put("系统消息"/*"系统消息"*/, SystemMessageActivity.class);
//            put("订单催缴"/*"订单催缴"*/, PayNoticeActivity.class);
//            put(MenuItem.PAYMENT/*"缴费"*/, PaymentListActivity.class);
//        }
//    };





    //服务编号:
    // 01系统消息；02通知；03资源状态改变消息；04版本更新；05续费提醒；06成绩；07点评；08作业；09校园风采；10班级动态；
    // 11考勤；12回复意见；13消费信息；14邮件；15待办事项；16工资条；17值班查询；18校长信箱；19日志；20总结；
    // 21考试 ；22请假审批结果；23学生请假单（家长端）；24维修处理结果；25文印审批结果 ；26 一卡通考勤；27 校园新闻 ;
    // 28 教育资讯 ; 29 留言板 ；30 进出校考勤 ；31 宿舍考勤 ； 32 校车考勤  33订单催缴 34缴费
    public static final MenuItem leavemessage = new MenuItem(1, "29", LEAVE_MESSAGE,LEAVE_MESSAGE_NUMBER, R.drawable.icon_liuyanban, LeaveMeassageActivity.class);
    public static final MenuItem notice = new MenuItem(1, "02", NOTICE,NOTICE_NUMBER, R.drawable.icon_tongzhi, NotifyActivity.class);
    public static final MenuItem schoolNews = new MenuItem(1, "27", SCHOOLNEWS,SCHOOLNEWS_NUMBER, R.drawable.icon_xiaoyuanxinwen, NewSchoolNewsActivity.class);
    public static final MenuItem schedule = new MenuItem(1, "", SCHEDULE,SCHEDULE_NUMBER, R.drawable.icon_kechengbiao, null);
    public static final MenuItem leave = new MenuItem(1, "23", LEAVE,LEAVE_NUMBER, R.drawable.icon_qingjiatiao, NewMyLeaveActivity.class);
    public static final MenuItem schoolMail = new MenuItem(1, "18", SCHOOLMAIL,SCHOOLMAIL_NUMBER, R.drawable.icon_xiaozhangxinxiang, SchoolMasterMailActivity.class);
    public static final MenuItem calendar = new MenuItem(1, "", CALENDAR,CALENDAR_NUMBER, R.drawable.icon_xiaoli, null);
    public static final MenuItem timeTable = new MenuItem(1, "", TIMETABLE,TIMETABLE_NUMBER, R.drawable.icon_zuoxishijian, null);
    public static final MenuItem parentSchool = new MenuItem(1, "", PARENTSCHOOL,PARENTSCHOOL_NUMBER, R.drawable.icon_jiazhangxuexiao, null);
    //网址导航
    public static final MenuItem website = new MenuItem(1, "", WEBSITE,WEBSITE_NUMBER, R.drawable.icon_wangzhidaohang, null);
    //一卡通充值
    public static final MenuItem oneCard = new MenuItem(1, "", ONE_CARD,ONE_CARD_NUMBER, R.drawable.icon_yikatongchongzhi, OneCardActivity.class);

    //兴趣班选课
    public static final MenuItem interestCourse = new MenuItem(1, "", INTEREST_COURSE,INTEREST_COURSE_NUMBER, R.drawable.icon_interest, null);
    //缴费
    public static final MenuItem payment = new MenuItem("34", PAYMENT,PAYMENT_NUMBER, R.drawable.icon_jiaofei, PaymentListActivity.class);

    public static final MenuItem dynamics = new MenuItem("10", DYNAMICS,DYNAMICS_NUMBER, R.drawable.icon_banjidongtai, NewClassDynamicsActivity.class);
    public static final MenuItem homework = new MenuItem("08", HOMEWORK,HOMEWORK_NUMBER, R.drawable.icon_zuoye, null);
    public static final MenuItem score = new MenuItem("06", SCORE,SCORE_NUMBER, R.drawable.icon_chengji, null);
    public static final MenuItem attendance = new MenuItem("11", ATTENDANCE,ATTENDANCE_NUMBER, R.drawable.icon_kaoqin, null);           //不存在了，所以18761是乱写的
    public static final MenuItem comment = new MenuItem("07", COMMENT,COMMENT_NUMBER, R.drawable.icon_dianping, null);
    public static final MenuItem consume = new MenuItem(1, "13", CONSUME,CONSUME_NUMBER, R.drawable.icon_xiaofei, NewConsumeActivity.class);

    public static final MenuItem course = new MenuItem(2, "", COURSE,COURSE_NUMBER, R.drawable.icon_ziguiketang, null);
    public static final MenuItem weike = new MenuItem(2, "", WEIKE,WEIKE_NUMBER, R.drawable.icon_weikewang, null);
    public static final MenuItem primarySchool = new MenuItem(2, "", PRIMARYSCHOOL,PRIMARYSCHOOL_NUMBER, R.drawable.icon_xiaoxue, null); //不存在了，所以18762是乱写的

    public static final MenuItem moniter = new MenuItem("", MONITER,MONITER1_NUMBER, R.drawable.icon_tanshi, GroupListActivity.class);
    public static final MenuItem moniter1 = new MenuItem("", MONITER1,MONITER_NUMBER, R.drawable.icon_tanshi, GroupListActivity.class);
    public static final MenuItem blank = new MenuItem("", "",0, 0, null);

    public static final MenuItem todo = new MenuItem("15", "待办事项",18763, R.drawable.icon_daibanshixiang, null);          //functionNumber没有这个值  18763所以是乱写的
    public static final MenuItem business = new MenuItem(Constants.BUSINESSTYPE, "业务办理",18764, R.drawable.icon_yewubanli, null);  //functionNumber没有这个值  18764所以是乱写的
    public static final MenuItem wage = new MenuItem("16", "工资条",18765, R.drawable.icon_gongzitiao, NewWagesActivity.class);  //functionNumber没有这个值  18765所以是乱写的
    public static final MenuItem daily = new MenuItem("19", "日志",18766, R.drawable.icon_rizhi, DailyRecordActivity.class);          //functionNumber没有这个值  18766所以是乱写的
    public static final MenuItem summary = new MenuItem("20", "总结",18767, R.drawable.icon_zongjie, SummaryActivity.class);             //functionNumber没有这个值  18767所以是乱写的

    public static final MenuItem duty = new MenuItem("17", "值班查询",18768, R.drawable.icon_zhibanchaxun, NewDutyInquiryActivity.class);      //functionNumber没有这个值  18768所以是乱写的
    public static final MenuItem email = new MenuItem("14", EMAIL,18769, R.drawable.icon_youjian, EmailActivity.class);                        //functionNumber没有这个值  18769所以是乱写的

        //30 进出校考勤 ；31 宿舍考勤 ； 32 校车考勤  11 人工考勤
    public static final MenuItem SchoolBusAttendance = new MenuItem("32", SCHOOL_BUS_ATTENDANCE,SCHOOL_BUS_ATTE_NUMBER, R.drawable.icon_xiaoche, null);
    public static final MenuItem DormitoryAttendance = new MenuItem("31", DORMITORY_ATTENDANCE,DORMITORY_ATTE_NUMBER, R.drawable.icon_sushekaoqin, null);
    public static final MenuItem OutIntoSchoolAttendance = new MenuItem("30", OUTINTO_SCHOOL_ATTENDANCE,OUTINTO_SOL_ATTE_NUMBER, R.drawable.icon_jinchuxiao, null);
    public static final MenuItem ManualAttendance = new MenuItem("11", MANUAL_ATTENDANCE,MANUAL_ATTE_NUMBER, R.drawable.icon_rengong, null);


    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }
}