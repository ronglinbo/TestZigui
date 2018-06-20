/*
 * 文 件 名:ChatLoginService.java
 * 创 建 人： 姜韵雯
 * 日    期： 2014-10-27
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.service;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWMessage;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.HanziToPinyin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.asynctask.JoinGroupAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.AllGradeClass;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.GetAllContactsReq;
import com.wcyc.zigui2.newapp.bean.GradeClass;
import com.wcyc.zigui2.newapp.bean.GradeMap;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.bean.NewMessageListBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

/**
 * 环信登录的服务类.
 */
public class ChatLoginService extends Service
        implements HttpRequestAsyncTaskListener, JoinGroupAsyncTaskListener {

    public static boolean canRun = true;
    private boolean refesh = false;
    private static final String TAG = ChatLoginService.class.getName();
    private MemberDetailBean detail;
    private List<NewMessageBean> messageList;
    private AllContactListBean contactList;
    private String[][] func =
            {{"2", "通知"}, {"3", "资源状态改变消息"}, {"4", "版本更新"}, {"5", "续费提醒"},
                    {"6", "成绩"}, {"7", "点评"}, {"8", "作业"}, {"9", "校园风采"},
                    {"10", "班级动态"}, {"11", "考勤"}, {"12", "回复意见"}, {"13", MenuItem.CONSUME},
                    {"14", "邮件"}, {"15", "待办事项"}, {"16", "工资条"}, {"17", "值班查询"},
                    {"18", "校长信箱"}, {"19", "日志"}, {"20", "总结"}, {"21", "考试"},
                    {"22", "业务办理"}, {"23", "学生请假单"}, {"24", "业务办理"}, {"25", "业务办理"}};
    public static final String LOAD_ALL_MESSAGE = "com.wcyc.zigui2.LOAD_ALL_MESSAGE_FINISH";

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("ChatLoginService onCreate");
        detail = CCApplication.getInstance().getMemberDetail();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("ChatLoginService onStartCommand:" + isLoading);
        detail = CCApplication.getInstance().getMemberDetail();
        refeshLogin();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 登录环信.
     *
     * @param username   用户名
     * @param password   密码
     * @param constraint 是否强制登录，不需要看是否之前登录成功过
     */
    public void LoginEmob(final String username, final String password, boolean constraint) {
//        loginHx(username, password);

        //登录阿里百川
        //获取到sdk对象

        //如果账号为空__有提示

        if (!DataUtil.isNullorEmpty(username) && !DataUtil.isNullorEmpty(password)) {
            final YWIMKit mIMKit = YWAPI.getIMKitInstance(username, Constants.BAI_CHUAN_APPKEY);

            if (mIMKit != null) {
                CCApplication.app.setIMKit(mIMKit);
                IYWLoginService loginService = mIMKit.getLoginService();
                YWLoginParam loginParam = YWLoginParam.createLoginParam(username, password);
                loginService.login(loginParam, new IWxCallback() {

                    @Override
                    public void onSuccess(Object... arg0) {
                        System.out.println("LoginService" + "登陆百川成功!");
                        //存储个人信息
                        CCApplication.getInstance().setUserName(username);
                        CCApplication.getInstance().setPassword(password);
                    }

                    @Override
                    public void onProgress(int arg0) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onError(int errCode, String description) {
                        //如果登录失败，errCode为错误码,description是错误的具体描述信息
                        System.out.println("LoginService" + "登陆百川失败!:" + description);
                        CCApplication.getInstance().setPassword(null);
                        new Thread(runnable).start();
                    }
                });
            }
        } else {
            System.out.println("副号没有聊天功能");
        }
    }


    private void loginHx(final String username, final String password) {
        String name = CCApplication.getInstance().getUserName();
        String psw = CCApplication.getInstance().getPassword();
//		if (/*!constraint && */name != null && name.equals(username)
//				&& psw != null && psw.equals(password)) {
////			ChatLoginService.this.stopSelf();
//			System.out.println("已经登录环信！！！");
//			return;
//		}else{
        System.out.println("开始登录环信:" + username);
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            try {
                EMChatManager.getInstance().login(username, password, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        // 登录陆成功，保存用户名密码
                        try {
//							List<EMGroup> grouplist = EMGroupManager.getInstance().getGroupsFromServer();//
//							EasemobGroupInfo groupInfo = new EasemobGroupInfo();
//							groupInfo.setUserGroupList(grouplist);
//							CCApplication.getInstance().setHxGroupList(groupInfo);
                            EMGroupManager.getInstance().getAllGroups();
                            EMChatManager.getInstance().getAllConversations();
                            System.out.println("登录环信成功!!!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        CCApplication.getInstance().setUserName(username);
                        CCApplication.getInstance().setPassword(password);
//						ChatLoginService.this.stopSelf();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                        //如果聊天登录失败了，那么清除数据
                        System.out.println("登录环信失败:" + code + ":" + message);
                        CCApplication.getInstance().setPassword(null);
                        new Thread(runnable).start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            DataUtil.getToast("账号信息有误，请联系管理员");
            ChatLoginService.this.stopSelf();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (detail != null) {
                System.out.println("loginEmob HxUsername:" + detail.getHxUsername() + " HxPassword:" + detail.getHxPassword());
                LoginEmob(detail.getHxUsername(), detail.getHxPassword(), true);
//				LoginEmob(userName,psw,true);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //新增的联系人功能
    protected boolean isLoading = false;

    /**
     * 更新登陆，登陆环信，查询联系人
     */
    private void refeshLogin() {
        canRun = true;
        refesh = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (detail != null) {
                    isLoading = true;
                    LoginEmob(detail.getHxUsername(), detail.getHxPassword(), true);
                    getAllContact();
//					getOtherHxUserName();
                    getNewMessage();
                    getSchoolAllClassList();
                    UserType user = CCApplication.getInstance().getPresentUser();
                    if (user != null
                            && user.getUserType().equals(Constants.TEACHER_STR_TYPE)) {
                        getAllTeacherFromServer();
                    }
                }
            }
        }).start();
    }

    /**
     * 获取到所有消息的第一条消息
     */
    public void getNewMessage() {
        JSONObject json = new JSONObject();
        try {
            UserType user = CCApplication.getInstance().getPresentUser();
            if (user != null) {
                json.put("userId", user.getUserId());
                String type = user.getUserType();
                json.put("userType", type);
                if (type.equals(Constants.PARENT_STR_TYPE)) {
                    json.put("childId", user.getChildId());
                }
                System.out.println("AllMessageFragment getNewMessage:" + json);
                String url = new StringBuilder(Constants.SERVER_URL).append(Constants.GET_ALL_MESSAGE_URL).toString();
                try {
                    String result = HttpHelper.httpPostJson(this, url, json);
                    parseNewMessage(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取系统本地版本号.
     *
     * @return 系统本地版本号
     */
    public String getCurVersion() {
        Properties properties = new Properties();

        try {
            InputStream stream = this.getAssets().open("ver.cfg");
            properties.load(stream);
        } catch (FileNotFoundException e) {
            return "100";
        } catch (IOException e) {
            return "100";
        } catch (Exception e) {
            return "100";
        }

        String version = String.valueOf(properties.get("Version").toString());

        return version;
    }

    //获取其他身份的环信用户名
    private void getOtherHxUserName() {
        NewMemberBean member = CCApplication.getInstance().getMemberInfo();
        if (member == null) return;
        List<UserType> list = member.getUserTypeList();
        if (list == null) return;
        for (UserType user : list) {
            JSONObject json = new JSONObject();
            try {
                String userType = user.getUserType();
                json.put("userId", user.getUserId());
                json.put("userType", userType);
                json.put("schoolId", user.getSchoolId());
                if (Constants.PARENT_STR_TYPE.equals(userType)) {
                    json.put("studentId", user.getChildId());
                }
                System.out.println("login getLoginInfo:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                String url = new StringBuilder(Constants.SERVER_URL).append(Constants.LOGIN_INFO_URL).toString();
                String result = HttpHelper.httpPostJson(this, url, json);
                MemberDetailBean detail = JsonUtils.fromJson(result, MemberDetailBean.class);

                detail = DataUtil.sortUserList(detail);

                if (detail != null) {
                    String userName = detail.getHxUsername();
                    user.setHxUserName(userName);
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        CCApplication.app.setMemberInfo(member);
    }

    private void getAllContact() {
        GetAllContactsReq req = new GetAllContactsReq();
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user == null) return;
        try {
            JSONObject json;
            String userType = user.getUserType();
            List<String> classIdList = new ArrayList<String>();
            if (Constants.TEACHER_STR_TYPE.equals(userType)) {
                List<NewClasses> list = CCApplication.getInstance().getMemberDetail().getClassList();
                if (list != null) {
                    for (NewClasses classes : list) {
                        if (!DataUtil.isClassIdExist(classIdList, classes.getClassID()))//是否已存在list中
                            classIdList.add(classes.getClassID());
                    }
                }
            } else if (Constants.PARENT_STR_TYPE.equals(userType)) {
                classIdList.add(user.getClassId());
            }
            req.setSchoolId(user.getSchoolId());
            req.setUserId(user.getUserId());
            req.setUserType(userType);
            if (classIdList.size() > 0) {
                req.setClassIdList(classIdList);
            } else {
                classIdList.add("-1");
                req.setClassIdList(classIdList);
            }
            Gson gson = new Gson();
            String string = gson.toJson(req);
            json = new JSONObject(string);
            System.out.println("chatLoginService getAllContact json:" + json);
            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.GET_CONTACT_URL).toString();//
            String result = HttpHelper.httpPostJson(this, url, json);
            contactList = JsonUtils.fromJson(result, AllContactListBean.class);
            if (contactList.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                CCApplication.getInstance().setAllContactList(contactList);
            }
//			new HttpRequestAsyncTask(json,this,this).execute(Constants.GET_CONTACT_URL);
//			System.out.println("chatLoginService:"+result);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getSchoolAllClassList() {
        System.out.println("获取全校学生开始");
        String http_getClassStudentList = "/getClassStudentList";
        ArrayList<Student> allStudentList;
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user == null) return;
        String usertype = user.getUserType();
        if (detail == null) return;
        if (Constants.TEACHER_STR_TYPE.equals(usertype)) {
            List<NewClasses> allClassList = new ArrayList<NewClasses>();
            long cur1 = System.currentTimeMillis();

            try {
                JSONObject json = new JSONObject();
                json.put("isNeedAllGrade", "1");
                String url = new StringBuilder(Constants.SERVER_URL).append(
                        "/getGradeClassView").toString();//
                String result = HttpHelper.httpPostJson(this, url, json);
                GradeClass gradeClass = JsonUtils.fromJson(result, GradeClass.class);
                List<GradeMap> gradeList = gradeClass.getGradeMapList();

                if (gradeList != null && gradeList.size() > 0) {
                    AllGradeClass allGradeClass = new AllGradeClass();
                    for (GradeMap item : gradeList) {
                        JSONObject jsonB = new JSONObject();
                        int gradeId = item.getId();
                        jsonB.put("gradeId", gradeId);
                        jsonB.put("isNeedAllGrade", "0");
                        String urlB = new StringBuilder(Constants.SERVER_URL).append(
                                "/getGradeClassView").toString();//
                        String resultB = HttpHelper.httpPostJson(this, urlB, jsonB);
                        GradeClass gradeClassB = JsonUtils.fromJson(resultB, GradeClass.class);
                        List<ClassMap> classInGradeList = gradeClassB.getClassMapList();
                        item.setClassMapList(classInGradeList);

                        for (int j = 0; j < classInGradeList.size(); j++) {
                            NewClasses newClasses = new NewClasses();
                            newClasses.setClassID(classInGradeList.get(j).getId() + "");
                            newClasses.setClassName(classInGradeList.get(j).getName());
                            newClasses.setGradeId(item.getId() + "");
                            newClasses.setGradeName(item.getName());
                            newClasses.setIsAdviser("1");
                            allClassList.add(newClasses);
                        }
                    }
                    allGradeClass.setGradeMapList(gradeList);
                    CCApplication.getInstance().setAllGradeClass(allGradeClass);
                }
                CCApplication.getInstance().setSchoolAllClassList(allClassList);

            } catch (Exception e) {
            }

            if (!Constants.TEACHER_STR_TYPE.equals(usertype)) {
            } else if (allClassList == null || allClassList.size() < 1) {
            } else {
                List<NewClasses> classList = allClassList;

                for (int i = 0; i < classList.size(); i++) {
                    try {

                        String classId = classList.get(i).getClassID();
                        // JSON对象
                        JSONObject json = new JSONObject();
                        json.put("classId", classId);
                        //请求地址
                        String url = new StringBuilder(Constants.SERVER_URL).append(http_getClassStudentList).toString();
                        String result = HttpHelper.httpPostJson(this, url, json);
                        JSONObject json3 = new JSONObject(result);

                        //活动学生list
                        JSONArray ja = json3.getJSONArray("studentList");
                        Gson gson1 = new Gson();
                        Type t = new TypeToken<List<Student>>() {
                        }.getType();
                        //将gson格式转为json格式的字符串
                        allStudentList = gson1.fromJson(ja.toString(), t);
                        for (Student item : allStudentList) {
                            String header = getHeader(item.getName());
                            item.setHeader(header);
                        }
                        classList.get(i).setContactList(allStudentList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("获取全校学生耗时：" + (System.currentTimeMillis() - cur1));
                CCApplication.getInstance().setSchoolAllClassList(classList);
            }
        }
    }

    private void getAllTeacherFromServer() {
        JSONObject json = new JSONObject();
        String url = new StringBuilder(Constants.SERVER_URL)
                .append(Constants.GET_ALL_TEACHER).toString();
        try {
            String result = HttpHelper.httpPostJson(this, url, json);
            AllTeacherList teacherList = JsonUtils.fromJson(result, AllTeacherList.class);
            sort(teacherList);
            CCApplication.getInstance().setAllTeacherList(teacherList);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void sort(AllTeacherList list) {
        List<AllTeacherList.TeacherMap> teacherList = null;
        if (list != null)
            teacherList = list.getTeacherMapList();
        if (teacherList == null) return;
        for (AllTeacherList.TeacherMap item : teacherList) {
            String name = item.getName();
            String header = getHeader(name);
            item.setHeader(header);
        }
        // 排序
        Collections.sort(teacherList, new Comparator<AllTeacherList.TeacherMap>() {
            @Override
            public int compare(AllTeacherList.TeacherMap lhs, AllTeacherList.TeacherMap rhs) {
                return lhs.getHeader().compareTo(rhs.getHeader());
            }
        });
        list.setTeacherMapList(teacherList);
    }

    private String getHeader(String name) {
        StringBuilder sb = new StringBuilder();
        ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(name);
        if (tokens != null && tokens.size() > 0) {
            for (HanziToPinyin.Token token : tokens) {
                if (HanziToPinyin.Token.PINYIN == token.type) {
                    sb.append(token.target);
                } else {
                    sb.append(token.source);
                }
            }
        }
        return sb.toString().toLowerCase();
    }

    private boolean isClassIdExist(List<NewClasses> classlist, String classId) {
        for (int i = 0; i < classlist.size(); i++) {
            if (classlist.get(i).getClassID().equals(classId)) {
                return true;
            }
        }
        return false;
    }

    private void getGradeClass(String isNeedAllGrade, String gradeId) {
        JSONObject json = new JSONObject();
        try {
            json.put("isNeedAllGrade", isNeedAllGrade);
            json.put("gradeId", gradeId);

            String url = new StringBuilder(Constants.SERVER_URL)
                    .append(Constants.GET_GRADE_CLASS).toString();
            try {
                String result = HttpHelper.httpPostJson(this, url, json);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onRequstComplete(String result) {
        // TODO Auto-generated method stub
        System.out.println("chatLoginService:" + result);
        System.out.println("获取最新消息列表结果:" + result);
        parseNewMessage(result);
    }

    private void parseNewMessage(String result) {
        NewMessageListBean list = null;
        list = JsonUtils.fromJson(result, NewMessageListBean.class);
        if (list != null && list.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
            list = handleMessage(list);

//			CCApplication.getInstance().setMessageList(list);
//			NewMessageListBean all = CCApplication.getInstance().getMessageList();

            if (list != null) {
                messageList = list.getMessageList();
            }
            System.out.println("before loadchat:" + DataUtil.getCurrentDate(System.currentTimeMillis()));

            list.setMessageList(messageList);
            CCApplication.getInstance().setMessageList(list);
            Intent intent = new Intent(LOAD_ALL_MESSAGE);
            sendBroadcast(intent);
            System.out.println("finish loadchat:" + DataUtil.getCurrentDate(System.currentTimeMillis()));
        }
    }

    private void sortMessageByTime(List<NewMessageBean> messageList) {
        Collections.sort(messageList, new Comparator<NewMessageBean>() {
            @Override
            public int compare(final NewMessageBean message1, final NewMessageBean message2) {
                long time1 = 0;
                long time2 = 0;
                try {
                    time1 = DataUtil.getLongDate(message1.getMessageTime());
                    time2 = DataUtil.getLongDate(message2.getMessageTime());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (time1 == time2) {
                    return 0;
                } else if (time1 < time2) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    //处理消息列表
    private NewMessageListBean handleMessage(NewMessageListBean list) {
        List<NewMessageBean> messageList = list.getMessageList();
        NewMessageListBean ret = new NewMessageListBean();
        List<NewMessageBean> handled = new ArrayList<NewMessageBean>();
        List<NewMessageBean> fill = new ArrayList<NewMessageBean>();
        if (messageList != null) {
            for (NewMessageBean message : messageList) {
                String name = message.getMessageTypeName();
                String messageType = message.getMessageType();
                //填充消息名称
                if (name == null) {
                    for (int i = 0; i < func.length; i++) {
                        String type = func[i][0];
                        if (type.equals(messageType)) {
                            message.setMessageTypeName(func[i][1]);
                        }
                    }
                }
                if (!DataUtil.isFunctionEnable(MenuItem.nameToNumber(name)))
                    continue;
                //合并消息（22，24，25）为业务办理
                if (messageType.equals(Constants.LEAVE)//请假审批
                        || messageType.equals(Constants.GUARRANTEE)//维修处理
                        || messageType.equals(Constants.PRINT)) {//文印审批
                    message.setMessageTypeName("业务办理");
                    fill.add(message);
                } else {
                    if (messageType.equals("23")) {
                        message.setMessageTypeName("请假条");
                    }
                    if (!Constants.hasEmailFunc) {
                        if (messageType.equals(Constants.EMAIL)) {
                            continue;
                        }
                    }
                    handled.add(message);
                }
            }
        }
        //合并消息（22，24，25）为业务办理
        int size = 0;
        if (fill != null && (size = fill.size()) > 0) {
            long[] all = new long[size];
            String type = "";
            long maxTime = 0;
            int pos = 0, totalNum = 0;

            for (int i = 0; i < size; i++) {
                String time = fill.get(i).getMessageTime();
                String count = fill.get(i).getCount();
                totalNum += Integer.parseInt(count);
                type += fill.get(i).getMessageType();
                type += ",";
                try {
                    all[i] = DataUtil.getLongDate(time);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //找到最新的一条
            for (int i = 0; i < all.length; i++) {
                if (all[i] > maxTime) {
                    maxTime = all[i];
                    pos = i;
                }
            }
            fill.get(pos).setCount(String.valueOf(totalNum));
            fill.get(pos).setMessageType(type);
            handled.add(fill.get(pos));
        }
        ret.setMessageList(handled);
        return ret;
    }

    private boolean setMessage(EMConversation conversation, NewMessageBean message) {
        // 把最后一条消息的内容作为item的message内容
        EMMessage lastMessage = conversation.getLastMessage();

        if (lastMessage != null) {
            String content = getMessageDigest(lastMessage, this);
            String messageTime = DataUtil.getCurrentDate(lastMessage.getMsgTime());
            message.setMessageContent(content);
            message.setMessageTime(messageTime);
            message.setMessageType("chat");
            int count = conversation.getUnreadMsgCount();

            message.setCount(count + "");
            System.out.println("content:" + content + " count:" + count);
            return true;
        } else {
            return false;
        }
    }

    private String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        if (message == null) return digest;
        switch (message.getType()) {
            case LOCATION: // 位置消息

                break;
            case IMAGE: // 图片消息
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                //在消息列表界面中，图片消息不显示图片路径
                digest = getString(context, R.string.picture);
                break;
            case VOICE:// 语音消息
                digest = getString(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getString(context, R.string.video);
                break;
            case TXT: // 文本消息
                TextMessageBody txtBody = (TextMessageBody) message.getBody();
                digest = txtBody.getMessage();
                break;
            case FILE: // 普通文件消息
                digest = getString(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }

    String getString(Context context, int resId) {
        if (context != null)
            return context.getResources().getString(resId);
        else
            return null;
    }

    @Override
    public void onRequstCancelled() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onJoinGroupCancelled() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onJoinGroupComplete(String result) {
        // TODO Auto-generated method stub

    }
}
