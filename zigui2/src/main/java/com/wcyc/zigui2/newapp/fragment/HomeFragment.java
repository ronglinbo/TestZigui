/*
 * 文 件 名:NewMessageActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-02-18
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wcyc.zigui2.R;


import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.newapp.home.NewTeacherAccountActivity;
import com.wcyc.zigui2.newapp.service.ChatLoginService;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.GetAllContactsReq;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.ModelRemindList;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.bean.NewMessageListBean;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.widget.ChooseRolesList;
import com.wcyc.zigui2.newapp.widget.ChooseRolesList.ListAdapter;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.widget.RoundImageView;
import com.wcyc.zigui2.widget.SpinnerButton;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;

public class HomeFragment extends Fragment
        implements OnClickListener, HttpRequestAsyncTaskListener {

    private boolean payStatus = true;
    private TextView unreadLabel;
    private FragmentTabHost tabHost;
    private RoundImageView icon;
    private TextView tvPersonInfo, tvSchoolInfo;
    private SpinnerButton spin;
    private ListView listView;
    private String texts[] = {"消息", "通讯录"};
    //	private List<Role> roles;
    private List<UserType> users;
    private String currentName, schoolName = "", grade = "", name;
    private ChooseRolesList pop;
    private View view;
    private Class fragments[] = {AllMessageFragment.class,
            ContactFragment.class};
    private final int ACTION_GET_QIDONG_PAGE = 5;
    private View layoutView;
    private FragmentTabHost mTabHost;
    private Button message, contact;
    private AllMessageFragment messageFragment;
    private ContactFragment contactFragment;
    private AllContactListBean allContactList;
    private NewMemberBean member;
    private int action;
    public static final String INTENT_SWITCH_USER = "com.wcyc.zigui.action.INTENT_SWITCH_USER";

    public static Fragment newInstance(int index) {
        Fragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter mrefeshDataFilter = new IntentFilter(INTENT_SWITCH_USER);
        getActivity().registerReceiver(refeshDataReceiver, mrefeshDataFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(refeshDataReceiver);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        layoutView = inflater.inflate(R.layout.new_message_view, null);
        placeView(0);
        initView();
        initData();
        initEvent();
        return layoutView;
    }


    protected void initView() {
        unreadLabel = (TextView) layoutView
                .findViewById(R.id.unread_msg_number);
        tvPersonInfo = (TextView) layoutView.findViewById(R.id.person_info);
        tvSchoolInfo = (TextView) layoutView.findViewById(R.id.school_info);
        icon = (RoundImageView) layoutView
                .findViewById(R.id.my_information_icon);

        tabHost = (FragmentTabHost) layoutView
                .findViewById(android.R.id.tabhost);

        spin = (SpinnerButton) layoutView.findViewById(R.id.title2_spinner);
        view = layoutView.findViewById(R.id.my_title);
        message = (Button) layoutView.findViewById(R.id.message);
        message.setOnClickListener(this);
        message.setTextColor(Color.WHITE);
        contact = (Button) layoutView.findViewById(R.id.contact);
        contact.setOnClickListener(this);
        layoutView.findViewById(R.id.message).setSelected(true);
    }

    private void initData() {
        member = CCApplication.getInstance().getMemberInfo();

        if (member != null) {
//			name = member.getUserName();
            users = member.getUserTypeList();
        }
        CCApplication.getInstance().setPresentUser(null);
        UserType curUser = CCApplication.getInstance().getPresentUser();
        if (curUser != null) {
            curUser.setIschecked(true);
        }
        if (users != null) {
            for (UserType user : users) {
                if (user.getUserType().equals(Constants.TEACHER_STR_TYPE)) {
                    if (user.getUserId().equals(curUser.getUserId())
                            && user.getUserType().equals(curUser.getUserType())) {
                        user.setIschecked(true);
                        //					break;
                    } else {
                        user.setIschecked(false);
                    }
                    name = user.getTeacherName();//获取老师的名字并保存
                    member.setTeacherName(name);
                } else {
                    if (user.getChildId().equals(curUser.getChildId())
                            && user.getUserType().equals(curUser.getUserType())) {
                        user.setIschecked(true);
                        //					break;
                    } else {
                        user.setIschecked(false);
                    }
                }
            }
        }
        int i = 0;
        int curIndex = CCApplication.getInstance().getPresentUserIndex();
        if (curUser != null && Constants.TEACHER_STR_TYPE.equals(curUser.getUserType())) {
            name = curUser.getTeacherName();//修改在2.1版本上
            member.setTeacherName(name);
            tvPersonInfo.setText(name + "（教职工）");
            tvSchoolInfo.setText(curUser.getSchoolName());
        } else if (curUser != null) {
            tvPersonInfo.setText(curUser.getChildName() + curUser.getRelationTypeName());
            tvSchoolInfo.setText(curUser.getSchoolName() + curUser.getGradeName() + curUser.getClassName());
        }

        CCApplication.getInstance().setPresentUser(curIndex);
        if (member != null) {
            String file = member.getUserIconURL();
            System.out.println("name:" + name + " curUser:" + curUser + " file:" + file);
            HomeActivity activity = (HomeActivity) getActivity();
            // Bitmap bitmap = activity.getIcon();
            if (LocalUtil.mBitMap != null) {
                icon.setImageBitmap(LocalUtil.mBitMap);
            } else {
                ImageUtils.showImage(getActivity(), file, icon);
            }
        }
    }

    private String getClassInfo(String classId) {
        MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
        if (detail != null) {
            List<NewChild> list = detail.getChildList();
            if (list != null) {
                for (NewChild child : list) {
                    if (classId.equals(child.getChildClassID())) {
                        return child.getGradeName();
                    }
                }
            }
            List<NewClasses> classList = detail.getClassList();
            if (classList != null) {
                for (NewClasses item : classList) {
                    if (classId.equals(item.getClassID())) {
                        return item.getGradeName();
                    }
                }
            }
        }
        return "";
    }

    //不显示子贵学苑的身份
    private List<UserType> filterItem(List<UserType> users) {
        if (users != null) {
            List<UserType> list = new ArrayList<UserType>();
            for (UserType user : users) {
                if (!Constants.ZIGUI_SCHOOL_ID.equals(user.getSchoolId())) {
                    list.add(user);
                }
            }
            return list;
        }
        return users;
    }

    private void initEvent() {
        if (users != null && users.size() > 1) {
            spin.showAble(true);
//			final List<UserType> list = filterItem(users);
            spin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    // TODO Auto-generated method stub
                    pop = new ChooseRolesList(getActivity(), users, name);
                    listView = pop.list;
                    final ListAdapter adapter = pop.GetAdapter();
                    pop.showAsDropDown(view);
                    listView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                final int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            if (pop != null) pop.dismiss();
                            UserType user = users.get(arg2);
                            user.setIschecked(true);
                            for (int i = 0; i < users.size(); i++) {// 其它的去勾选
                                if (i != arg2) {
                                    users.get(i).setIschecked(false);
                                }
                            }
                            if (Constants.TEACHER_STR_TYPE.equals(user.getUserType())) {
                                currentName = user.getTeacherName() + "（教职工）";
                                schoolName = user.getSchoolName();
                            } else {
                                currentName = user.getChildName() + user.getRelationTypeName();
                                schoolName = user.getSchoolName() + user.getGradeName() + user.getClassName();
                            }
                            tvPersonInfo.setText(currentName);
                            tvSchoolInfo.setText(schoolName);
                            System.out.println("school:" + schoolName);
                            // 修改成员显示数据
                            ChooseTeacherActivity.teacherSelectInfo = null;//初始化选择部门
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    CCApplication.getInstance().logoutOnServer("1");
                                    CCApplication.getInstance().setPresentUser(arg2);
                                    requestGetPresentUserInfo(arg2);//重新读取当前用户信息 ，不从本地读取
                                    //	switchUser();
                                }
                            }).run();

                        }
                    });
                }

            });
        } else {
            if (spin != null)
                spin.setVisibility(View.GONE);
        }

    }

    private BroadcastReceiver refeshDataReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("member") != null) {
                initData();
            } else {
                switchUser();
            }

        }
    };


    private void parseQIDong(String data) {
        System.out.print(data);
        try {

            JSONObject jsonObject = new JSONObject(data);
            jsonObject = jsonObject.getJSONObject("imageUrlList");
            String url = Constants.URL + "/" + jsonObject.get(getDpi());
            Picasso.with(getActivity()).load(url);
            CCApplication.dbsp.putString("QidongUrl", url);
        } catch (JSONException e) {
            //没有的话 默认 子贵校园
            CCApplication.dbsp.putString("QidongUrl", "zigui");
        }
    }

    private void switchUser() {
        new Thread(new Runnable() {

            @Override
            public void run() {

//				requestGetPresentUserInfo();//重新读取当前用户信息 ，不从本地读取
                ((HomeActivity) getActivity()).getMenuConfig();
                if (messageFragment != null) {
                    CCApplication.getInstance().setMessageList(null);
//					messageFragment.refresh();
                    getModelRemind();
                    messageFragment.getNewMessage();
                    messageFragment.getSystemMessage();
                }
                if (contactFragment != null) {
                    CCApplication.getInstance().setAllContactList(null);
                    getAllContact();
                }

                checkServiceExpired();
            }
        }).run();
    }

    private void getNewMessage() {
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
                System.out.println("HomeFragment getNewMessage:" + json);
                new HttpRequestAsyncTask(json, this, getActivity())
                        .execute(Constants.GET_ALL_MESSAGE_URL);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void checkServiceExpired() {
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null
                && Constants.PARENT_STR_TYPE.equals(user.getUserType())) {
            JSONObject json = new JSONObject();
            try {
                json.put("userId", user.getUserId());
                json.put("studentId", user.getChildId());
                System.out.println("HomeFragment checkServiceExpired:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            new HttpRequestAsyncTask(json, this, getActivity())
                    .execute(Constants.CHECK_SERVICE_EXPIRE);

        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.message:
                placeView(0);
                contact.setSelected(false);
                message.setTextColor(Color.WHITE);
                contact.setTextColor(getResources().getColor(
                        (R.color.btn_blue_normal)));
                break;
            case R.id.contact:
                placeView(1);
                message.setSelected(false);
                contact.setTextColor(Color.WHITE);
                message.setTextColor(getResources().getColor(
                        (R.color.btn_blue_normal)));
                break;
            default:
                break;
        }
        v.setSelected(true);
    }

    public void placeView(int index) {
        Fragment fragment = getChildFragmentManager()
                .findFragmentByTag(index + "");
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (fragment == null) {
            switch (index) {
                case 0:
                    if (messageFragment == null) {
                        messageFragment = (AllMessageFragment) (fragment = AllMessageFragment
                                .newInstance(index));
                    } else {
                        fragment = messageFragment;
                    }
                    break;
                case 1:
                    System.out.println("context:" + getActivity());
                    if (contactFragment == null) {
                        contactFragment = (ContactFragment) (fragment = ContactFragment
                                .newInstance(index));
                    } else {
                        fragment = contactFragment;
                    }
                    break;
                default:
                    break;
            }
        }
        if (ft != null && !fragment.isAdded()) {
            ft.replace(R.id.maincontent, fragment, index + "");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            // ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
        }
    }

    public ChooseRolesList getPopWindowHandle() {
        return pop;
    }

    public void setPopWindowHandle(ChooseRolesList pop) {
        this.pop = pop;
    }

    public Fragment getContactFragment() {
        return contactFragment;
    }

    public Fragment getMessageFragment() {
        return messageFragment;
    }

    private void requestGetPresentUserInfo(int position) {//重新读取当前用户信息 ，不从本地读取
//		JSONObject json = new JSONObject();
//		UserType user = CCApplication.app.getPresentUser();
//		try {
//			String userType = user.getUserType();
//			json.put("userId", user.getUserId());
//			json.put("userType", userType);
//			json.put("schoolId", user.getSchoolId());
//			if(Constants.PARENT_STR_TYPE.equals(userType)){
//				json.put("studentId",user.getChildId());
//			}
//			System.out.println("homeFragment getLoginInfo:"+json);
//			String url = new StringBuilder(Constants.SERVER_URL).append(Constants.LOGIN_INFO_URL).toString();
//			String result = HttpHelper.httpPostJson(getActivity(), url, json);
//			System.out.println("homeFragment:"+result);
//			MemberDetailBean loginDetail = JsonUtils.fromJson(result, MemberDetailBean.class);
//			loginDetail = DataUtil.sortUserList(loginDetail);
//			CCApplication.app.setMemberDetail(loginDetail);
//			CCApplication.getInstance().setUserName(loginDetail.getHxUsername());
//			CCApplication.getInstance().setPassword(loginDetail.getHxPassword());
////			切换身份 先退出环信 在登陆环信
//			if (CCApplication.getInstance().getUserName() != null && CCApplication.getInstance().getPassword() != null) {
//				try {
//					EMChatManager.getInstance().logout();
//				}catch (Exception e){
//					e.printStackTrace();
//				}
//
//			}
//			LoginEmob();
//			// 通知activity刷新数据
//			Intent broadcast = new Intent(HomeFragment.INTENT_SWITCH_USER);
//			getActivity().sendBroadcast(broadcast);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

        List<UserType> users = member.getUserTypeList();
        UserType user_position = users.get(position);
        user_position.setIschecked(true);
        for (int i = 0; i < users.size(); i++) {// 其它的去勾选
            if (i != position) {
                users.get(i).setIschecked(false);
            }
        }

        // ImageView check = (ImageView)
        // view.findViewById(R.id.checked);

//		adapter.selectdItem(position);
//		adapter.notifyDataSetChanged();
        CCApplication.getInstance().logoutOnServer("1");
        CCApplication.getInstance().setPresentUser(position);
        // 重新获取当前用户信息，并封装
        JSONObject json = new JSONObject();
        UserType user = CCApplication.app.getPresentUser();
        try {
            String userType = user.getUserType();
            json.put("userId", user.getUserId());
            json.put("userType", userType);
            json.put("schoolId", user.getSchoolId());
            if (Constants.PARENT_STR_TYPE.equals(userType)) {
                json.put("studentId", user.getChildId());
            }
            String url = new StringBuilder(Constants.SERVER_URL)
                    .append(Constants.LOGIN_INFO_URL).toString();
            String result = HttpHelper.httpPostJson(
                    getActivity(), url, json);
            MemberDetailBean loginDetail = JsonUtils.fromJson(result,
                    MemberDetailBean.class);
            loginDetail = DataUtil.sortUserList(loginDetail);
            CCApplication.app.setMemberDetail(loginDetail);

            //先退出阿里百川再登陆
            if (CCApplication.getInstance().getUserName() != null && CCApplication.getInstance().getPassword() != null) {
                try {
                    CCApplication.getInstance().logoutAliChat();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 登陆环信
            Intent intent = new Intent(
                    CCApplication.applicationContext,
                    ChatLoginService.class);
            // 启动服务
            getActivity().startService(intent);

            MemberDetailBean aa = CCApplication.app.getMemberDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 通知activity刷新数据
        Intent broadcast = new Intent(HomeFragment.INTENT_SWITCH_USER);
        getActivity().sendBroadcast(broadcast);
        //切换轮播图
        getLanucherPage();
        // getAllContact();
        CCApplication.getInstance().setAllContactList(null);
        checkServiceExpired();
        //DataUtil.getToast("身份切换成功");
        ChooseTeacherActivity.teacherSelectInfo = null;//更新教师选择器选择部门

        //清空notification数据
        clearNotification();
    }

    public void clearNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

    //获取 本机 dpi 类型
    public String getDpi() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int densityDpi = dm.densityDpi;
        if (densityDpi == 320) {
            return "xhdpi";
        }
        if (densityDpi == 240) {
            return "hdpi";
        }
        if (densityDpi == 160) {
            return "mdpi";
        }
        if (densityDpi == 120) {
            return "ldpi";
        }
        return "xhdpi";

    }

    /**
     * 启动页 获取
     */
    private void getLanucherPage() {
        try {
            UserType userType = CCApplication.app.getPresentUser();
            String schoolId = userType.getSchoolId();

            JSONObject json = new JSONObject();
            try {
                json.put("schoolId", schoolId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            action = ACTION_GET_QIDONG_PAGE;
            new HttpRequestAsyncTask(json, this, getActivity()).execute(Constants.GET_IMAGE_URL);

        } catch (Exception e) {

        }

    }

    /**
     * 登录环信.
     */
    public void LoginEmob() {
        Intent intent = new Intent(CCApplication.applicationContext, ChatLoginService.class);
        // 启动服务  
        CCApplication.applicationContext.startService(intent);
    }

    private void getAllContact() {

        GetAllContactsReq req = new GetAllContactsReq();
        UserType user = CCApplication.getInstance().getPresentUser();
        try {
            JSONObject json;
            List<String> classIdList = new ArrayList<String>();
            if (user.getUserType().equals(Constants.TEACHER_STR_TYPE)) {
                List<NewClasses> list = CCApplication.getInstance().getMemberDetail().getClassList();
                if (list != null) {
                    for (NewClasses classes : list) {
                        if (!DataUtil.isClassIdExist(classIdList, classes.getClassID()))//是否已存在list中
                            classIdList.add(classes.getClassID());
                    }
                }
            } else if (user.getUserType().equals(Constants.PARENT_STR_TYPE)) {
                classIdList.add(user.getClassId());
            }
            req.setSchoolId(user.getSchoolId());
            req.setUserId(user.getUserId());
            req.setUserType(user.getUserType());
            if (classIdList.size() > 0) {
                req.setClassIdList(classIdList);
            } else {
                classIdList.add("-1");
                req.setClassIdList(classIdList);
            }
            Gson gson = new Gson();
            String string = gson.toJson(req);
            json = new JSONObject(string);
            System.out.println("getAllContact json:" + json);

            new HttpRequestAsyncTask(json, this, getActivity())
                    .execute(Constants.GET_CONTACT_URL);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void parseMessageDate(String result) {
        NewMessageListBean list = null;
        NewMessageBean message = null;
        if (result.contains("messageList")) {
            System.out.println("GET_ALL_MESSAGE_URL:" + result);
            list = JsonUtils.fromJson(result, NewMessageListBean.class);
            if (list.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                if (list != null) {
                    list = removeUnusedMessage(list);
                }
                CCApplication.getInstance().setMessageList(list);
            }
        } else {//关联消息
            NewMessageBean ret = JsonUtils.fromJson(result, NewMessageBean.class);
            if (ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                message = ret;
            }
        }
        if (messageFragment != null) {
            messageFragment.refresh();
        }
    }

    //只显示实现的模块消息
    private NewMessageListBean removeUnusedMessage(NewMessageListBean list) {
        List<NewMessageBean> messageList = list.getMessageList();
        NewMessageListBean ret = new NewMessageListBean();
        List<NewMessageBean> used = new ArrayList<NewMessageBean>();
        for (NewMessageBean message : messageList) {
            for (String type : Constants.func) {
                if (type.equals(message.getMessageType())) {
                    used.add(message);
                }
            }
        }
        ret.setMessageList(used);
        return ret;
    }

    private void getModelRemind() {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            try {
                json.put("userId", user.getUserId());
                json.put("userType", user.getUserType());
                if (CCApplication.getInstance().isCurUserParent()) {
                    json.put("studentId", user.getChildId());
                }
                json.put("schoolId", user.getSchoolId());
                System.out.println("getModelRemind:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(json, this, getActivity()).execute(Constants.GET_MODEL_REMIND);
        }
    }

    public void parseModelRemind(String result) {
        System.out.println("get Remind:" + result);
        ModelRemindList remind = JsonUtils.fromJson(result, ModelRemindList.class);
        if (remind.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
            CCApplication.getInstance().setModelRemindList(remind);
        }
        ((HomeActivity) getActivity()).updateUnreadLabel();
        int unreadNum = DataUtil.getUnreadMsgCountTotal();
        if (unreadNum > 99) unreadNum = 99;
        try {
            ShortcutBadger.setBadge(getActivity(), unreadNum);
        } catch (ShortcutBadgeException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequstComplete(String result) {
        if (action == ACTION_GET_QIDONG_PAGE) {
            parseQIDong(result);
            action = 0;
            return;
        }
        // TODO Auto-generated method stub
        if (result.contains("messageList")) {
            parseMessageDate(result);
        } else if (result.contains("modelRemindList")) {
            parseModelRemind(result);
        } else if (result.contains("serviceList")) {
            ServiceExpiredBean service = JsonUtils.fromJson(result, ServiceExpiredBean.class);
            if (service.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                CCApplication.getInstance().setServiceExpiredInfo(service);
            }
        } else {
            allContactList = JsonUtils.fromJson(result, AllContactListBean.class);
            if (allContactList.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                CCApplication.getInstance().setAllContactList(allContactList);
                if (messageFragment != null) {
                    messageFragment.refresh();
                }
                if (contactFragment != null) {
                    contactFragment.initData();
                    contactFragment.initAdapter();
                    ContactFragment.ContactListAdapter adapter = contactFragment.getAdapter();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    ContactFragment.GroupListAdapter groupAdapter = contactFragment.getGroupAdapter();
                    if (groupAdapter != null) {
                        groupAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onRequstCancelled() {
        // TODO Auto-generated method stub

    }
}
