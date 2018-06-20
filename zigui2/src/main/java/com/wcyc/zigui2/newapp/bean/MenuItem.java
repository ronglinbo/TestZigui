package com.wcyc.zigui2.newapp.bean;

import com.dh.groupTree.GroupListActivity;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.activity.PaymentListActivity;
import com.wcyc.zigui2.newapp.module.classdynamics.NewClassDynamicsActivity;
import com.wcyc.zigui2.newapp.module.consume.NewConsumeActivity;
import com.wcyc.zigui2.newapp.module.consume.OneCardActivity;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordActivity;
import com.wcyc.zigui2.newapp.module.duty.NewDutyInquiryActivity;
import com.wcyc.zigui2.newapp.module.email.EmailActivity;
import com.wcyc.zigui2.newapp.module.leave.NewMyLeaveActivity;
import com.wcyc.zigui2.newapp.module.leavemessage.LeaveMeassageActivity;
import com.wcyc.zigui2.newapp.module.mailbox.SchoolMasterMailActivity;
import com.wcyc.zigui2.newapp.module.news.NewSchoolNewsActivity;
import com.wcyc.zigui2.newapp.module.notice.NotifyActivity;
import com.wcyc.zigui2.newapp.module.summary.SummaryActivity;
import com.wcyc.zigui2.newapp.module.wages.NewWagesActivity;
import com.wcyc.zigui2.utils.Constants;

//菜单项
public class MenuItem {
    private String itemType;
    private String itemName;
    private int resId;
    private Class ClassName;
    private int unreadNum;
    private int ProductType;
    private boolean isFree = true;//是否免费

    public MenuItem(String itemName) {
        this.itemName = itemName;
    }

    public MenuItem(String itemType, String itemName, int resId, Class ClassName) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.resId = resId;
        this.ClassName = ClassName;
    }

    public MenuItem(int ProductType, String itemType, String itemName, int resId, Class ClassName) {
        this.itemType = itemType;
        this.itemName = itemName;
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

    public static final String NOTICE = "通知";
    public static final String SCHOOLNEWS = "校园新闻";
    public static final String SCHEDULE = "课程表";
    public static final String LEAVE = "请假条";
    public static final String SCHOOLMAIL = "校长信箱";
    public static final String CALENDAR = "校历";
    public static final String TIMETABLE = "作息时间";
    public static final String PARENTSCHOOL = "家长学校";
    public static final String DYNAMICS = "班级动态";
    public static final String HOMEWORK = "作业";
    public static final String SCORE = "成绩";
    public static final String ATTENDANCE = "考勤";
    public static final String COMMENT = "点评";
    public static final String CONSUME = "卡消费充值";
    public static final String COURSE = "在线课堂";

    public static final String WEIKE = "微课网";
    public static final String PRIMARYSCHOOL = "小学宝";
    public static final String MONITER = "校园监控";
    public static final String MONITER1 = "子贵探视";
    public static final String EMAIL = "邮件";
    public static final String QUICK_SCHOOLMAIL = "给校长写信";
    public static final String QUICK_LEAVE = "请假申请";
    //新增
    public static final String LEAVE_MESSAGE = "班牌留言";

    //考勤新增
    public static final String SCHOOL_BUS_ATTENDANCE = "校车考勤";
    public static final String DORMITORY_ATTENDANCE = "宿舍考勤";
    public static final String OUTINTO_SCHOOL_ATTENDANCE = "进出校考勤";
    public static final String MANUAL_ATTENDANCE = "人工考勤";


    //新增网址导航
    public static final String WEBSITE = "常用网址";

    //新增兴趣班选课
    public static final String INTEREST_COURSE = "兴趣班选课";

    //新增兴趣班选课
    public static final String PAYMENT = "缴费";

    //一卡通充值
    public static final String ONE_CARD = "一卡通充值";


    //类型区别 1.免费应用   2.个性服务  3.学习资源

    //服务编号:
    // 01系统消息；02通知；03资源状态改变消息；04版本更新；05续费提醒；06成绩；07点评；08作业；09校园风采；10班级动态；
    // 11考勤；12回复意见；13消费信息；14邮件；15待办事项；16工资条；17值班查询；18校长信箱；19日志；20总结；
    // 21考试 ；22请假审批结果；23学生请假单（家长端）；24维修处理结果；25文印审批结果 ；26 一卡通考勤；27 校园新闻 ;
    // 28 教育资讯 ; 29 留言板 ；30 进出校考勤 ；31 宿舍考勤 ； 32 校车考勤  33订单催缴 34缴费
    public static final MenuItem leavemessage = new MenuItem(1, "29", LEAVE_MESSAGE, R.drawable.icon_liuyanban, LeaveMeassageActivity.class);
    public static final MenuItem notice = new MenuItem(1, "02", NOTICE, R.drawable.icon_tongzhi, NotifyActivity.class);
    public static final MenuItem schoolNews = new MenuItem(1, "27", SCHOOLNEWS, R.drawable.icon_xiaoyuanxinwen, NewSchoolNewsActivity.class);
    public static final MenuItem schedule = new MenuItem(1, "", SCHEDULE, R.drawable.icon_kechengbiao, null);
    public static final MenuItem leave = new MenuItem(1, "23", LEAVE, R.drawable.icon_qingjiatiao, NewMyLeaveActivity.class);
    public static final MenuItem schoolMail = new MenuItem(1, "18", SCHOOLMAIL, R.drawable.icon_xiaozhangxinxiang, SchoolMasterMailActivity.class);
    public static final MenuItem calendar = new MenuItem(1, "", CALENDAR, R.drawable.icon_xiaoli, null);
    public static final MenuItem timeTable = new MenuItem(1, "", TIMETABLE, R.drawable.icon_zuoxishijian, null);
    public static final MenuItem parentSchool = new MenuItem(1, "", PARENTSCHOOL, R.drawable.icon_jiazhangxuexiao, null);
    //网址导航
    public static final MenuItem website = new MenuItem(1, "", WEBSITE, R.drawable.icon_wangzhidaohang, null);
    //一卡通充值
    public static final MenuItem oneCard = new MenuItem(1, "", ONE_CARD, R.drawable.icon_yikatongchongzhi, OneCardActivity.class);

    //兴趣班选课
    public static final MenuItem interestCourse = new MenuItem(1, "", INTEREST_COURSE, R.drawable.icon_interest, null);
    //缴费
    public static final MenuItem payment = new MenuItem("34", PAYMENT, R.drawable.icon_jiaofei, PaymentListActivity.class);

    public static final MenuItem dynamics = new MenuItem("10", DYNAMICS, R.drawable.icon_banjidongtai, NewClassDynamicsActivity.class);
    public static final MenuItem homework = new MenuItem("08", HOMEWORK, R.drawable.icon_zuoye, null);
    public static final MenuItem score = new MenuItem("06", SCORE, R.drawable.icon_chengji, null);
    public static final MenuItem attendance = new MenuItem("11", ATTENDANCE, R.drawable.icon_kaoqin, null);
    public static final MenuItem comment = new MenuItem("07", COMMENT, R.drawable.icon_dianping, null);
    public static final MenuItem consume = new MenuItem(1, "13", CONSUME, R.drawable.icon_xiaofei, NewConsumeActivity.class);

    public static final MenuItem course = new MenuItem(2, "", COURSE, R.drawable.icon_ziguiketang, null);
    public static final MenuItem weike = new MenuItem(2, "", WEIKE, R.drawable.icon_weikewang, null);
    public static final MenuItem primarySchool = new MenuItem(2, "", PRIMARYSCHOOL, R.drawable.icon_xiaoxue, null);

    public static final MenuItem moniter = new MenuItem("", MONITER, R.drawable.icon_tanshi, GroupListActivity.class);
    public static final MenuItem moniter1 = new MenuItem("", MONITER1, R.drawable.icon_tanshi, GroupListActivity.class);
    public static final MenuItem blank = new MenuItem("", "", 0, null);

    public static final MenuItem todo = new MenuItem("15", "待办事项", R.drawable.icon_daibanshixiang, null);
    public static final MenuItem business = new MenuItem(Constants.BUSINESSTYPE, "业务办理", R.drawable.icon_yewubanli, null);
    public static final MenuItem wage = new MenuItem("16", "工资条", R.drawable.icon_gongzitiao, NewWagesActivity.class);
    public static final MenuItem daily = new MenuItem("19", "日志", R.drawable.icon_rizhi, DailyRecordActivity.class);
    public static final MenuItem summary = new MenuItem("20", "总结", R.drawable.icon_zongjie, SummaryActivity.class);

    public static final MenuItem duty = new MenuItem("17", "值班查询", R.drawable.icon_zhibanchaxun, NewDutyInquiryActivity.class);
    public static final MenuItem email = new MenuItem("14", EMAIL, R.drawable.icon_youjian, EmailActivity.class);

        //30 进出校考勤 ；31 宿舍考勤 ； 32 校车考勤  11 人工考勤
    public static final MenuItem SchoolBusAttendance = new MenuItem("32", SCHOOL_BUS_ATTENDANCE, R.drawable.icon_xiaoche, null);
    public static final MenuItem DormitoryAttendance = new MenuItem("31", DORMITORY_ATTENDANCE, R.drawable.icon_sushekaoqin, null);
    public static final MenuItem OutIntoSchoolAttendance = new MenuItem("30", OUTINTO_SCHOOL_ATTENDANCE, R.drawable.icon_jinchuxiao, null);
    public static final MenuItem ManualAttendance = new MenuItem("11", MANUAL_ATTENDANCE, R.drawable.icon_rengong, null);


    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }
}