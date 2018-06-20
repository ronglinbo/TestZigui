package com.wcyc.zigui2.utils;

import android.os.Environment;
/*
* 文 件 名:Constants.java

*/
//2014-9-23

/**
 * 常量类
 * *
 * ********************************
 * 1.阿里百川 BAI_CHUAN_APPKEY
 * *
 * ********************************
 * 2.根目录 URL
 * *
 * ********************************
 * 3.文件下载上传 DLS_URL
 * *
 * ********************************
 * 4 子贵课堂 ziguiUrl
 * *
 * ********************************
 * 5.保利威视 POLYV
 * *
 * ********************************
 */
public interface Constants {
    String WEB_MIMETYPE_HTML = "text/html";
    String DEFAULT_ENCODING = "UTF-8";

    //子贵学苑的学校ID
    String ZIGUI_SCHOOL_ID = "139";


    //阿里百川测试账号__子贵校园
    String BAI_CHUAN_APPKEY = "24742717";

    //阿里百川正式账号__子贵教育
//    String BAI_CHUAN_APPKEY = "24746441";

    /**
     * 根地址
     */
    //http://10.0.2.87:8028/zgwps/app_description/description.do
//	String URL = "http://10.0.7.27:8080/zgwps";//zyj的IP
//	String URL = "http://manager.ziguiw.com/zgwt";
//	String URL = "http://10.0.2.85:8028/zgwps";//开发环境
//     String URL = "http://10.0.2.87:8028/zgwps";//测试环境
//    String URL = "http://cs.ziguiw.cn:8028/zgwps";//映射到外网
    //   String URL = "http://home.ziguiw.com";//---正式线
//   String URL = "https://home.ziguiw.com";//---正式线____https

//      String URL = "http://10.0.7.92:8080/zgwps";//龙哥
    // String URL = "http://10.0.2.87:8028/zgwps";//测试环境
//    String URL = "http://10.0.7.25:8080/zgwps";//许吟秋
    //String URL="http://10.0.7.112:8080/zgwps";
    //String URL = "http://10.0.7.87:8083/zgwps"; //画
//    String URL = "http://10.0.7.117:8080/zgwps";//谭元珺
//    String URL = "http://10.0.2.83:8025/zgwps";//新搭建测试环境__百川
    //	String URL = "http://10.0.6.161:8080/zgwps";//chenx
    //  String URL = "http://10.0.7.89:8088/zgwps";//chenx
    //String URL ="https://10.0.2.80/zgwps"; //刘松Https
    String URL = "https://wcyc.ziguiw.com/zgwps"; //内网测试线____https
//    String URL ="http://10.0.7.235:8082/zgwps";  //周琨
//     String URL="http://10.0.7.25:8080/zgwps";  //许吟秋
//    String URL ="http://10.0.7.104:8088/zgwps"; //石力强
//    String URL = "http://10.0.7.233:8080/zgwps"; //彭进
//    String URL = "http://10.0.7.92:8080/zgwps" ;//龙竹桑
//    String URL = "https://homecs.ziguiw.com/zgwps" ;//阿里测试Https
//    String URL = "http://homecs.ziguiw.com/zgwps" ;//阿里测试Http
//    String URL = "http://10.0.7.115:8080/zgwps" ;//刘松本地
//    String URL = "http://10.0.7.235:8082/zgwps" ;//周坤本地


    String BASE_URL = URL + "/clientApi";
    //     String DLS_URL = "http://10.0.7.92:8080/dls";//龙哥
    //  String DLS_URL = "http://dls.ziguiw.com";//新域名正式线dls
//      String DLS_URL = "https://dls.ziguiw.com";//新域名正式线dls_______https
//     String DLS_URL = "http://10.0.2.82:8021/dls";//开发环 境dls
    String DLS_URL = "https://wcyc.ziguiw.com/dls";//内网测试线DLS_________Https
//    String DLS_URL = "http://10.0.7.233:8080/dls";//彭进
//   String DLS_URL = "http://cs.ziguiw.cn:8021/dls";//映射到外网
    //String DLS_URL = "http://10.0.7.117:8080/dls";//谭元君 http://10.0.7.25:8080/zgwps/clientApi
    //String DLS_URL="http://10.0.7.112:8080/dls";
    //String DLS_URL = "http://10.0.7.87:8083/zgwps"; //画
    //String DLS_URL="http://10.0.7.89:8088/zgwps";
//    String DLS_URL = "https://dlscs.ziguiw.com/dls";// 阿里测试HTTPS
//    String DLS_URL = "http://dlscs.ziguiw.com/dls";// 阿里测试HTTP


    String IMAGE_URL = "http://eyijiao-10000622.image.myqcloud.com";//考勤图片URL
    // String DLS_URL = "http://10.0.2.80:8021/dls";//测试环境dls
    String UPLOAD_URL = DLS_URL + "/upload";//上传附件的IP
    String DOWNLOAD_URL = DLS_URL + "/download";//下载附件的IP

//	String SMALL_MAP_URL = "http://10.0.2.85:8028/zgwps";//略缩图地址
//	String UPLOAD_URL = "http://10.0.7.137:8081/dls/upload";//调试上传下载附件的IP

    //	String ziguiUrl = "http://120.25.244.58:8080/ZGKT/app/coursecenter/index?";//创智测试服务器
//        String ziguiUrl = "http://10.0.2.85:8024/zgkt/app/coursecenter/index?";//子贵课堂 测试服务器
    //	String ziguiUrl = "http://10.0.7.51:8080/zgkt/app/coursecenter/index?";
//String ziguiUrl = "http://learn.ziguiw.com/zgkt/app/coursecenter/index?";//子贵课堂 正式服务器
//    String ziguiUrl = "https://learn.ziguiw.com/zgkt/app/coursecenter/index?";//子贵课堂 正式服务器______https

    String ziguiUrl = "https://wcyc.ziguiw.com/zgkt/app/coursecenter/index?";//子贵课堂_____内网测试https
    //String ziguiUrl = "http://10.0.7.117:8080/zgkt/app/coursecenter/index?" ;
    // http://10.0.7.117:8080/zgkt/app/coursecenter/index?";//谭
//    String ziguiUrl = "https://learncs.ziguiw.com/zgkt/app/coursecenter/index?";//阿里测试子贵课堂 HTTPS
//    String ziguiUrl = "http://learncs.ziguiw.com/zgkt/app/coursecenter/index?";//阿里测试子贵课堂 HTTP


    //保利威视_正式线o
    String POLYV = "5LRvSl9xppuvHXYX4pc/7hwlLqoH67i13aiXKW13nHlnHj//oEUlWJ6bwwf2UgLYTBgK0cMDiYPOUlcIfH5viHq2IkCVCfrIzaG2X4pX1tvhCEubSjjhjgl/QIvnHho1jS6AWZdNWPKlmntJIdUA==";

    //保利威视_测试线_刘佼账号
//    String POLYV = "ABawlst4zaLgx8Bm099tzJcVOdZDDl7MSUbOLpDeBuxe0PaYVTCMfixKQ5LxCPNYgUMXIRMbpo/5T1NVB5BMe9S5Gz/GQvZnzRbQ9/OPSLjvBR2tyzY5LRWHiOZrYEh8rPbOTFcGsB8QyVeDWOA/1g==";


    int MSG_PUSHTIME = 90 * 1000;
    int TIME_OUT = 60 * 1000;
    /**
     * 文件上传路径
     */
    String IMG_SERVER_URL = BASE_URL + "/ws/upload/uploadFile";

    /**
     * HTTP通信地址
     */
    String SERVER_URL = BASE_URL + "/ws";
    /**
     * web实现页面地址
     */
//	String WEBVIEW_URL = BASE_URL;
//	String WEBVIEW_URL = "http://10.0.2.87:8028/zgwps/";//h5测试服务器
    String WEBVIEW_URL = URL + "/";//h5地址
    //	String WEBVIEW_URL = "http://10.0.7.242:8080/zgwps/";//hsp
    String TIME_TABLE = "app_resttime/list.do";
    String SCHOOL_CALENDAR = "app_schoolManager/list.do";
    String WEBSITES = "websitemanage/websitemanagelist.do"; //常用网址
    //老师端html5地址
    String HOME_WORK_URL = "app_homework/list.do";

    //人工考勤
    String ATTENDANCE = "app_attendance/toListByTimes.do";
    //宿舍考勤
    String DORMITORY_ATTENDANCE = "app_attendance/toDormListByTimes.do";
    //进出校考勤
    String OUTINTO_SCHOOL_ATTENDANCE = "app_attendance/toSchoolListByTimes.do";
    //校车考勤
    String SCHOOL_BUS_ATTENDANCE = "app_attendance/toCarListByTimes.do";


    //教师端考勤_按学生ID查询 人工考勤
    String ATTENDANCE_BY_ID = "app_attendance/viewAttenStudent.do";
    //宿舍考勤
    String ATTENDANCE_DORM_BY_ID = "app_attendance/viewDormAttenStudent.do";
    //进出校考勤
    String ATTENDANCE_SCHOOL_BY_ID = "app_attendance/viewSchoolAttenStudent.do";
    //校车考勤
    String ATTENDANCE_SCHOOL_BUS_BY_ID = "app_attendance/viewCarAttenStudent.do";

    //兴趣班选课
    String INTEREST_COURSE_SELECT = "interestCourse/v_list.do";

    String EXAM_LIST = "app_exam/toTeacherExamList.do";
    String COMMENT = "app_comment/toListByTimes.do";
    String COMMENT_BY_ID = "app_comment/viewCommentStudent.do";

    String SCORE = "app_examScore/toList.do";
    String SCORE_BY_ID = "app_examScore/toExamStudentList.do";

    String COURSE = "app_schedule/toTeacherSchedule.do";
    String BUSINESS = "app_business/" +
            "toMainPage.do";
    String SCHEDULE = "backlog/backloglist.do";


    //家长端html5地址
    //家长人工考勤
    String ATTENDANCE_CHILD = "app_attendance/viewAttenStudentByParent.do";
    //家长宿舍考勤
    String DORMITORY_ATTENDANCE_CHILD = "app_attendance/viewDormAttenByParent.do";
    //家长校车考勤
    String SCHOOL_BUS_ATTENDANCE_CHILD = "app_attendance/viewCarAttenByParent.do";
    //家长进出校考勤
    String OUT_INTO_SCHOOL_ATTENDANCE_CHILD = "app_attendance/viewSchoolAttenByParent.do";


    String HOMEWORK_CHILD = "app_homework/viewHomeworkByParent.do";
    String COMMENT_CHILD = "app_comment/viewCommentStudentByParent.do";
    String SCHEDULE_CHILD = "app_schedule/toParentSchedule.do";
    String SCORE_CHILD = "app_exam/viewScoreByParent.do";
    //小学资源
    String PRIMARY_SCHOOL_RESOURCE = "app_xxzy/toxxzy.do";
    //微课网
    String WEIKE = "app_wkw/towkw.do";

    String PRODUC_INTRODUCE = "app_description/description.do";

    //家长端充值产品介绍地址
    String JCXX_INTRODUCE = "cpjs/jcxx_introduction.html";
    String WKW_INTRODUCE = "cpjs/wkw_introduction.html";
    String ZGKT_INTRODUCE = "cpjs/zgkt_introduction.html";
    String ZGTS_INTRODUCE = "cpjs/zgts_introduction.html";
    String XXZY_INTRODUCE = "cpjs/xxzy_introduction.html";

    String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/com.wcyc.zigui2/";

    //环信需要的常量
    String NEW_FRIENDS_USERNAME = "item_new_friends";
    String GROUP_USERNAME = "item_groups";
    String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";

    int NEW_VERSION = 200000;
    int PARENT_TYPE = 3;
    int TEACHER_TYPE = 2;
    int STUDENT_TYPE = 1;
    String PARENT_STR_TYPE = "3";
    String TEACHER_STR_TYPE = "2";
    String STUDENT_STR_TYPE = "1";

    String PIC_TYPE = "1";
    String AUDIO_VIDIO_TYPE = "2";
    String DOC_TYPE = "3";

    //查询附件类型
    String HOME_WORK = "00";
    String NOTICE = "01";
    String MASTER_MAIL = "02";
    String REPLY_MASTER_MAIL = "03";
    String DAILY_ATTACH = "08";//日志
    String SUMMARY_ATTACH = "09";//总结
    String EMAIL_ATTACH = "06";//邮件
    String FEEBACK_ATTACH = "05";//意见反馈附件类型
    String PRINT_ATTACH = "07";//文印附件类型

    int SUCCESS_CODE = 200;
    String SUCCESS_RET_CODE = "200";
    //禁用的状态码
    int ACCOUNT_DISABLE_CODE = 102005;

    //login
    String LOGIN_URL = "/login/";
    String VERIFY = "/safetyVerification";
    String GET_VERIFY_CODE = "/sendMessageCode";
    String VALID_VERIFY_CODE = "/mobileValidateCode";
    String LOGIN_OUT = "/loginOut";
    String LOGIN_INFO_URL = "/getLoginInfo";

    String GETCODE_URL = "/getCode";
    String GETPSW_URL = "/getPassword";
    String VALID_CODE = "/validateCode";
    String LOGIN_ACTIVE = "/loginActive";
    //message
    String GET_ALL_MESSAGE_URL = "/getUserAllTypeNewFirstMessage";
    String GET_CORRELATION_MESSAGE = "/getUserCorrelationMessage";
    String GET_CONTACT_URL = "/getAllContacts";
    //mail
    //管理员查询校长信箱接口
    String ADMIN_URL = "/getEmailFromSchoolMashterForManager";
    //非管理员新建校长信箱的信件接口
    String ADD_MAIL_URL = "/addMail";
    //管理员回复校长信箱的信件接口
    String REPLY_MAIL_URL = "/replyMail";
    //获取校长信箱信件回复列表
    String GET_ANSWER_MAIL = "/getSchMasterEmailAnswerList";
    //非管理员查询校长信箱接口
    String GET_SEND_MAIL_URL = "/getMySendMailList";
    //notice
    String GET_NOTICE_URL = "/getMyNoticeList";
    String BROWSE_NOTICE = "/browseNotice";
    String PUBLISH_NOTICE = "/publishNotice";
    String DELETE_NOTICE = "/delNotice";
    //String GET_USER_GROUP = "/getUserGroupView";
    String GET_USER_GROUP = "/getTeacher";
    String GET_ALL_TEACHER = "/getSchoolAllTeacher";
    String GET_ALL_STUDENT = "/getSchoolAllStudent";
    String NOTICE_BROWSE_COUNT = "/noticeBrowseCount";

    String GET_GRADE_CLASS = "/getGradeClassView";
    String GET_STUDENT_LIST = "/getClassStudentList";
    String GET_TEXT = "/getRelationCode";

    //attachment
    String QUREY_ATTACHMENT = "/getAattchmentListByBuzzId";
    String UPLOAD_ATTACHMENT = "/uploadAttachmentInfo";
    //update
    String UPDATE_URL = "/configService/versionUpdate";
    //charge
    String GET_CHARGE_INFO_URL = "/getProductInfoList";
    String CREATE_ORDER = "/createOrder";
    String GET_ORDER_LIST = "/getOrderList";
    String GET_ORDER_DETAIL = "/getOrderDetail";
    String COMMIT_ORDER_PAY = "/commitOrderPay";
    String GET_PAYMENT_INFO = "/getPaymentInfo";

    //charge2.0
    //会员功能套餐列表信息
    //String GET_VIP_SERVICE_INFO = "/getVipServiceInfos";   //geSysProductListInfo
    String GET_VIP_SERVICE_INFO = "/getSysProductListInfo"; //新的接口 我的服务 getSysProductListInfo
    //.获取订单列表
    String GET_MYORDER_List = "/getMyOrderList";
    //根据一个产品或者套餐获取充值时长的信息
//	String GET_PRODUCT_TIME = "/getProductTime";
    String GET_PRODUCT_TIME = "/queryProductTime";
    //查询充值记录
    String GET_CHARGE_LOGS = "/getVipRechargeLogs";
    //创建订单
    String CREATE_SYS_ORDER = "/createSysOrder";
    //根据订单号查询订单信息状态
    String GET_ORDER_DETAIL_INFO = "/getOrderDetailInfo";
    //更新订单状态
    String UPDATE_ORDER = "/updateOrder";
    //modify user info
    String MODIFY_USER_INFO = "/modifyUserInfo";
    String GET_ORDER_COUNT = "/getMyOrderCount";
    String CHECK_SERVICE_EXPIRE = "/checkProductServiceExpire";
    String VERSION_UPDATE = "/versionUpdate";
    //新的版本更新接口
    String VERSION_CHECK = "/versionSearch";
    String GET_MODEL_REMIND = "/getUnReadModelRemind";//获取模块未读动态记录数
    String DEL_MODEL_REMIND = "/delModelRemind";//删除模块已读记录
    String DEL_READ_MESSAGE = "/delReadMessage";//删除已阅读的消息

    String DAILY_RECORD_LIST = "/getLogList";//日志列表
    String DAILY_RECORD_BROWSE = "/logBrowsing";//日志浏览
    String PUBLISH_DAILY_RECORD = "/publishLog";//发布日志
    String DELETE_LOG = "/DeleteLog";//删除日志
    String GET_LAST_INFO = "/searchSumLogLastInfo";//日志总结上一条记录

    String SUMMARY_LIST = "/sumList";//总结列表
    String SUMMARY_BROWSE = "/sumBrowsing";//总结浏览
    String PUBLISH_SUMMARY = "/publishSum";//发布总结
    String GET_DEPART_INFO = "/GetDepart";//获取担任部门负责人的部门信息
    String DELETE_SUMMARY = "/DeleteSum";//删除总结

    String EMAIL_LIST = "/EmailList";   //邮件列表
    String EMAIL_BROWSE = "/Emailbrowsing";   //邮件浏览
    String PUBLISH_EMAIL = "/createEmail";   //邮件发布
    String DELETE_EMAIL = "/deleteEmail";   //邮件删除
    String RESTORE_EMAIL = "/EmailRestore";    //邮件还原

    //======================================
    String GET_IMAGE_URL = "/getImageUrlInfo";// 启动页
    String ACTIVE_PODUCT_SERVICE = "/activeProductService";//. 激活自由订购产品试用时长
    String CHECKPRODUCTSERVICESTATE = "/checkProductServiceExpire";//检查服务是否到期
    String PUBLISH_STUDENT_ATEN = "/publishStudentAten";    //发布考勤
    String GET_ATTEN_BASIC_INFO = "/getAttenBasicInfo";    //获取考勤基本信息
    String PUBLISH_STUENT_COMMENT = "/publishStuentComment";    //发布点评
    String PUBLISH_HOMEWORK = "/publishHomework";    //发布作业
    String RECHARGE_SERVICE_NEW_ORDER = "/rechargeService/newOrder";    //会员中心 获取订单列表
    String SET_MEAL_RENEW_SERVICE_BUY_SET_MEAL = "/setMealRenewService/buySetMeal";    //会员中心 是否续费
    String VERSION_SEARCH = "/versionSearch";    //检查是否有版本更新
    String FEE_BACK = "/feeBack";    //意见反馈
    String MODIFY_USER_INFO_ZGD = "/modifyUserInfo";    //修改用户 信息  email则改邮箱  password则修改密码
    String GET_CLASS_STUDENT_LIST = "/getClassStudentList";    //获取班级学生
    String USED_HELP = "/usedHelp";    //使用帮助
    String GET_CLASS_DYNAMIC_LIST = "/getClassDynamicList";    //获取班级动态
    String UPLOAD_STATE_ZGD = "/upload/uploadState";    //发布完班级动态后的状态
    String PARENT_MOBILE_LIST = "/parentMobileList";    //获取副号列表
    String DEL_MODEL_REMIND_ZGD = "/delModelRemind";    //删除已读模块
    String DELETE_CLASS_DYNAMIC = "/deleteClassDynamic";    //new删除一条班级动态的接口
    String DELETE_CLASS_DYNAMIC_COMMENT = "/deleteClassDynamicComment";    //new删除一条评论
    String PUBLISH_DYNAMIC_COMMENT = "/publishDynamicComment";    //点赞OR评论的接口
    String GET_RELATION_CODE = "/getRelationCode";    //获取所有关系接口 爷爷奶奶外公外婆等
    String PUBLISH_CLASS_DYNAMIC = "/publishClassDynamic";    //发布班级动态
    String GET_CARD_REMAINING = "/getCardRemaining";    //消费余额
    String GET_CONSUME_INFO = "/getConsumeInfo";    //获取消费充值列表
    String GET_DUTY_LOG = "/getDutyLog";    //值班日志
    String GET_DATY_LISTS = "/getDutyLists";    //值班查询列表
    String GET_TEACHER_STAFF_INFO = "/getTeacherStaffInfo";    //获取个人环信信息--值班查询里用到
    String INPUT_DUTY = "/inputDuty";    //值班登记
    String MA_DATY_PLAN = "/myDutyPlan";    //我的值班 查询
    String VERSION_SHUOMING = "/getVersionUpdateInfo";    //我的值班 查询
    String LEAVE_LIST = "/leaveList";    //我的请假
    String LEAVE_ADD_INFO = "/LeaveAddInfo";    //请假申请
    String LEAVE_DELETE_INFO = "/LeaveDeleteInfo";    //删除请假
    String GET_SCHOOL_NEWS_LIST = "/getSchoolNewsList";    //获得校园新闻列表
    String GET_SCHOOL_NEWS_DETAILS = "/getSchoolNewsDetails";    //获得校园新闻详情
    String SEND_SCHOOL_NEWS_COMMENT = "/sendSchoolNewsComment";    //发送校园新闻评论或回复评论
    String DELETE_SCHOOL_NEWS_COMMENT = "/deleteSchoolNewsComment";    //删除评论或删除回复的评论
    String SEND_SCHOOL_NEWS_LIKE = "/sendSchoolNewsLike";    //点赞或取消点赞

    String PARENT_MOBILE_ADD = "/parentMobileAdd";    //添加副号接口
    String PARENT_MOBILE_DALETE = "/parentMobileDelete";    //删除副号
    String GET_WAGE_RECORDS = "/getWageRecords";    //获取工资列表
    String GET_WAGE_RECORD_DETAIL = "/getWageRecordDetail";    //获取工资详情
    String GET_MONITER_LIST = "/getIcpUserGroupList";//获取监控服务器与摄像头列表
    String SEND_VEIKE_RECORD = "/appBaseVkoUserSave";//记录登录微课需要保存相关的用户信息
    String GET_EDUCATION_CIRCLES_LIST = "/getEducationCirclesList";    //获取教育资讯列表
    String GET_SCHOOL_ECIRCLES_BY_ID = "/getSchoolEcirclesById";//获取教育资讯详情
    String SEND_SCHOOL_RCIRCLES_LIKE = "/sendSchoolEcirclesLike";//教育资讯点赞
    String SEND_SCHOOL_RCIRCLES_COMMENT = "/sendSchoolEcirclesComment";    //教育资讯评论或回复评论
    String DELETE_SCHOOL_RCIRCLES_COMMENT = "/deleteSchoolEcirclesComment";    //教育资讯删除评论或删除回复的评论
    String GET_TEACHER = "/getTeacher";        //获取教师选择器下级数据
    //======================================
    String GET_STUDENTBOARD_MESSAGE = "/getStudentBoardMessage";//获取班牌留言
    String DELETE_STUDENTBOARD_MESSAGE = "/deleteStudentBoardMessage";//删除班牌留言
    String PUBLISH_PARENT_MESSAGE_BOARD = "/publicParentMessageBoard"; //发布家长留言
    String GET_PARENT_MESSAGE_BOARD_LIST = "/getParentMessageBoardList"; //获取家长留言
    String DELETE_PARENT_MESSAGE = "/deleteParentMessageBoard";//删除家长留言


    String POST_TYPE_REPAIRMAN = "/getRepairTypeList";//获取维修类型及维修人
    String PUBLISH_REPAIR = "/publishRepair";  //申请维修


    String GET_SCH_STUDENT_LIST = "/getSchStudentList";  //获取用户有权限的班级学生列表


    //======================================

    String AUTHID = "&authId=MB";

    String MAIL_ADMIN = "headermailadmin";//校长信箱管理员权限代码

    String SCHOOL_ADMIN = "schooladmin";//学校管理员
    String DEPT_ADMIN = "departmentcharge";//部门负责人
    String SCHOOL_LEADER = "schoolleader";//校级领导
    String GRADE_LEADER = "gradeleader";//年级组长
    String CLASS_HEADER = "classteacher";//班主任
    String ALL_TEACHER = "全体教职工";
    //修改个人信息接口

    //每页获取默认条数
    int defaultPageSize = 10;

    //可以上传的最大的附件数
    int MAX_ATTACH_LIST = 10;
    int MBYTE = 1024 * 1024;
    //当前可以接受通知的模块
    String[] func = {"02", "18", "08", "11", "07", "06"};
    String EMAIL = "14";//邮件
    String SCHOOLMASTER = "18";//校长信箱
    String DAILY = "19";
    String SUMMARY = "20";
    String CLASSDYN = "10";//班级动态
    String LEAVE = "22";//请假审批
    String GUARRANTEE = "24";//维修处理
    String PRINT = "25";//文印审批
    String BUSINESSTYPE = "001";//业务办理
    String SCHOOL_NEWS = "27";//校园新闻
    String EDU_INFO = "28";//教育资讯
    String MSG_URL = BASE_URL + GET_ALL_MESSAGE_URL;

    String PAY_NOTICE = "33";//订单催缴
    String SYSTEM_MESSAGE = "01";//系统消息

    String TAKE_PHOTO = "takePhoto";
    //应用市场下载地址
    String MARKET_APK_DOWNLOAD
            = "http://openbox.mobilem.360.cn/index/d/sid/3326539";
    boolean hasEmailFunc = true;//邮件功能开关
    //01系统消息；02通知；03资源状态改变消息；04版本更新；05续费提醒；
    // 06成绩；07点评；08作业；09校园风采；10班级动态；11考勤；
    // 12回复意见；13消费信息；14邮件；15待办事项；16工资条；
    // 17值班查询；18校长信箱；19日志；20总结；21考试；22请假申请

    //友盟推送 推送消息对应类型
    String[][] MsgType =
            //0  : 强制下线
            {{"01", "系统消息"},
                    {"02", "通知"},
                    {"03", "资源状态改变消息"},
                    {"04", "版本更新"},
                    {"05", "续费提醒"},
                    {"06", "成绩"},
                    {"07", "点评"},
                    {"08", "作业"},
                    {"09", "校园风采"},
                    {"10", "班级动态"},
                    {"11", "人工考勤"},
                    {"12", "回复意见"},
                    {"13", "消费"},
                    {"14", "邮件"},
                    {"15", "待办事项"},
                    {"16", "工资条"},
                    {"17", "值班查询"},
                    {"18", "校长信箱"},
                    {"19", "日志"},
                    {"20", "总结"},
                    {"21", "考试"},
                    {"22", "请假申请"},
                    {"26", "一卡通考勤"},
                    {"27", "校园新闻"},
                    {"28", "教育资讯"},
                    {"29", "班牌留言"},
                    {"30", "进出校考勤"},
                    {"31", "宿舍考勤"},
                    {"33", "订单催缴"},
                    {"34", "缴费"},
                    {"32", "校车考勤"},
            };

    //是否存在 同步推送按钮
    String IS_EXIST_INTERFACE = "/isExistInterface";

    //将消息设为已读
    String UPDATE_MESSAGE_STATUS = "/updateMessageStatus";

    //将消息设为已读__缴费提醒的设为已读
    String UPDATE_MESSAGE_STATUS_PAY_NOTICE = "/orderUrgeMsgBatch2read";

    //首页轮播图地址
    String HOME_BANNER_AD = "app_slideshow/homeSlideshow.do";

    //教育资讯新闻类型
    String GET_EDU_NEWS_TYPE = "/getSECirclesTypeList";

    //教育资讯_推荐类别
    String GET_EDUCATION_CIRCLES_LIST_BY_RECOMMEND = "/getEducationCirclesListByRecommend";

    //教育资讯_其他类别
    String GET_ECIRCLES_LIST_BY_ECTYPE_ID = "/getECirclesListByEcTypeId";

    //教育资讯-搜索
    String GET_ECIRCLES_LIST_BY_TITLE = "/getECirclesListByTitle";


    //弹窗提醒 我知道了
    String UPDATE_MESSAGE_APP_STATUS = "/updateMessageAppStatus";


    //弹窗提醒
    String GET_SYSTEM_MESSAGE_BY_USER = "/getSystemMessageByUser";

    //系统消息列表
    String GET_MESSAGE_LIST_BY_USER = "/getMessageListByUser";


    //订单催缴列表
    String GET_ORDER_URGE_MSG_LIST_BY_USER = "/getOrderUrgeMsgListByUser";


    //消息详情地址
    String SYSTEM_MESSAGE_DETAIL_URL = "/systemMessage/messageDetail.do?";

    //缴费详情地址
    String PAY_NOTICE_DETAIL_URL = "/orderUrge/urgeDetail.do?";

    //缴费列表
    String GET_USER_FEE_PAY_LIST = "/getUserFeePayList";

    //去缴费页面
    String PAYMENT_GO_PAY = "/app_onlinepay/queryPublicPayById.do?";

    //缴费详情地址
    String PAYMENT_DETAIL_INFO = "/app_onlinepay/rechargeDetailInfo.do?";

    //学校是否开通在线充值业务
    String GET_CARD_PAY_STATUS = "/getCardPayStatus";

    //一卡通充值 卡消费充值地址
    String H5_PAY_URL = "/h5pay/v_cardPay.do?";

    //在线充值记录
    String GET_ONLINE_RECORD = "/getCardRechargeInfo";

    //学校启动页查询
    String GET_SCHOOL_LAUNCHER_INFO = "/getAppInfoSchoolStart";

    //班级动态默认分享的图标ID
    String SHARE_CLASS_DYNAMICS_PICTURE_ID = "1800083";

    //班级动态默认分享的图标ID
    String UPDATE_CLASS_DYNAMIC_BACKGROUND = "/updateClassDynamicBground";

    //更新班级动态的访客数
    String UPDATE_CLASS_DYNAMIC_VISITOR_COUNT = "/updateClassDynamicVisitorCount";
}



