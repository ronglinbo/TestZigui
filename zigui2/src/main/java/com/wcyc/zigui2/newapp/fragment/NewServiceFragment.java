/*
 * 文 件 名:NewServiceFragment.java
 * 创 建 人： xiehua
 * 日    期： 2016-02-17
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.dh.DHMoniterService;
import com.dh.groupTree.GroupListActivity;
import com.wcyc.zigui2.R;


import com.wcyc.zigui2.chat.MainActivity;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.core.TaskBaseActivity;

import com.wcyc.zigui2.newapp.adapter.MenuAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.ModelRemindList;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.bean.NewMessageListBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.ModelRemindList.ModelRemind;
import com.wcyc.zigui2.newapp.home.NewAttendanceActivity;
import com.wcyc.zigui2.newapp.home.NewCommentActivity;
import com.wcyc.zigui2.newapp.home.NewHomeworkActivity;


import com.wcyc.zigui2.newapp.module.classdynamics.NewClassDynamicsActivity;
import com.wcyc.zigui2.newapp.module.consume.NewConsumeActivity;
import com.wcyc.zigui2.newapp.module.dailyrecord.DailyRecordActivity;
import com.wcyc.zigui2.newapp.module.duty.NewDutyInquiryActivity;
import com.wcyc.zigui2.newapp.module.email.EmailActivity;
import com.wcyc.zigui2.newapp.module.email.MenuConfigBean;
import com.wcyc.zigui2.newapp.module.leave.NewMyLeaveActivity;
import com.wcyc.zigui2.newapp.module.mailbox.SchoolMasterMailActivity;
import com.wcyc.zigui2.newapp.module.news.NewSchoolNewsActivity;
import com.wcyc.zigui2.newapp.module.notice.NotifyActivity;
import com.wcyc.zigui2.newapp.module.studyresource.AoShuListResourceActivity;
import com.wcyc.zigui2.newapp.module.studyresource.ShiZhanListResourceActivity;
import com.wcyc.zigui2.newapp.module.studyresource.SyncVideoActivity;
import com.wcyc.zigui2.newapp.module.studyresource.TaoCanListResourceActivity;
import com.wcyc.zigui2.newapp.module.studyresource.ZTCListResourceActivity;
import com.wcyc.zigui2.newapp.module.summary.SummaryActivity;
import com.wcyc.zigui2.newapp.module.wages.NewWagesActivity;

import com.wcyc.zigui2.newapp.widget.QuickServicePublish;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.GoHtml5Function;
import com.wcyc.zigui2.utils.JsonUtils;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class NewServiceFragment extends Fragment implements
        OnClickListener, HttpRequestAsyncTaskListener {
    GridView menu, menu1, menu2, menu3;
    NewMessageListBean message;
    //	List <NewMessageBean> messageList;
    ModelRemindList remind;
    UserType user;
    Context mContext;
    List<MenuItem> item, item1, freeItem, parentSchool;
    List<MenuItem> resource, other;
    Map<String, MenuItem> map = new HashMap<String, MenuItem>();
    private Button[] mTabs;
    // 未读消息textview
    private TextView unreadLabel;
    /**
     * 支付状态，欠费为false 默认不欠费为true
     */
    private boolean payStatus = true;
    private View layoutView;
    private String type;

    public static Fragment newInstance(int index) {
        Fragment fragment = new NewServiceFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.new_service_view, null);
        mContext = getActivity();

        return layoutView;
    }

    public void onResume() {
        super.onResume();
        initData();
        init();
    }

    private void initMenuItem() {
        map.put(MenuItem.LEAVE_MESSAGE, MenuItem.leavemessage);
        map.put(MenuItem.NOTICE, MenuItem.notice);
        map.put(MenuItem.SCHOOLNEWS, MenuItem.schoolNews);
        map.put(MenuItem.SCHEDULE, MenuItem.schedule);
        map.put(MenuItem.LEAVE, MenuItem.leave);
        map.put(MenuItem.SCHOOLMAIL, MenuItem.schoolMail);
        map.put(MenuItem.CALENDAR, MenuItem.calendar);
        map.put(MenuItem.TIMETABLE, MenuItem.timeTable);
        map.put(MenuItem.WEBSITE, MenuItem.website);//网址导航
        //	map.put(MenuItem.PARENTSCHOOL,MenuItem.parentSchool);
        map.put(MenuItem.DYNAMICS, MenuItem.dynamics);
        map.put(MenuItem.HOMEWORK, MenuItem.homework);
        map.put(MenuItem.SCORE, MenuItem.score);
        map.put(MenuItem.ATTENDANCE, MenuItem.attendance);//考勤
        map.put(MenuItem.COMMENT, MenuItem.comment);
        map.put(MenuItem.CONSUME, MenuItem.consume);
        map.put(MenuItem.MONITER1, MenuItem.moniter1);//子贵探视


        map.put(MenuItem.SCHOOL_BUS_ATTENDANCE, MenuItem.SchoolBusAttendance);//校车考勤
        map.put(MenuItem.DORMITORY_ATTENDANCE, MenuItem.DormitoryAttendance);//宿舍考勤
        map.put(MenuItem.OUTINTO_SCHOOL_ATTENDANCE, MenuItem.OutIntoSchoolAttendance);//进出校考勤
        map.put(MenuItem.MANUAL_ATTENDANCE, MenuItem.ManualAttendance);//手动考勤

        map.put(MenuItem.INTEREST_COURSE, MenuItem.interestCourse);//兴趣班选课
        map.put(MenuItem.PAYMENT, MenuItem.payment);//缴费

        map.put(MenuItem.ONE_CARD, MenuItem.oneCard);//一卡通充值
    }

    private void initParentMenu() {
        //免费应用
        freeItem.add(MenuItem.notice);
        freeItem.add(MenuItem.schoolNews);
        freeItem.add(MenuItem.schedule);
        freeItem.add(MenuItem.leave);
        freeItem.add(MenuItem.schoolMail);
        freeItem.add(MenuItem.calendar);
        freeItem.add(MenuItem.timeTable);
        freeItem.add(MenuItem.website);
        freeItem.add(MenuItem.oneCard);
        //	freeItem.add(MenuItem.parentSchool);
        //个性服务
        item1.add(MenuItem.dynamics);
        item1.add(MenuItem.homework);
        item1.add(MenuItem.score);
        item1.add(MenuItem.attendance);
        item1.add(MenuItem.comment);
        item1.add(MenuItem.consume);
        item1.add(MenuItem.leavemessage);

        item1.add(MenuItem.SchoolBusAttendance);
        item1.add(MenuItem.DormitoryAttendance);
        item1.add(MenuItem.OutIntoSchoolAttendance);
        item1.add(MenuItem.ManualAttendance);

        item1.add(MenuItem.interestCourse);
        item1.add(MenuItem.payment);

        item1.add(MenuItem.blank);
        item1.add(MenuItem.blank);
        //	item1.add(MenuItem.moniter1);
    }

    private void initParentOtherMenu() {
        resource = new ArrayList<MenuItem>();
        other = new ArrayList<MenuItem>();
        //学习资源
        resource.add(MenuItem.course); //子贵课堂
        //resource.add(MenuItem.weike); //微课网
        //resource.add(MenuItem.primarySchool);//小学宝
        //	resource.add(MenuItem.blank);
        //其它应用
        //	other.add(MenuItem.moniter1);
        //other.add(MenuItem.blank);
        resource.add(MenuItem.blank);
        resource.add(MenuItem.blank);
        fillBlank(resource);
    }

    private void initTeacherMenu() {
        item = new ArrayList<MenuItem>();
        item1 = new ArrayList<MenuItem>();
        resource = new ArrayList<MenuItem>();

        //移动办公
        item.add(MenuItem.notice);
        item.add(MenuItem.schedule);
        item.add(MenuItem.wage);
        item.add(MenuItem.duty);

        item.add(MenuItem.consume);
        item.add(MenuItem.daily);
        item.add(MenuItem.summary);
        item.add(MenuItem.email);

        item.add(MenuItem.todo);
        item.add(MenuItem.business);
        item.add(MenuItem.schoolNews);
        item.add(MenuItem.moniter);

        item.add(MenuItem.calendar);
        item.add(MenuItem.timeTable);
        item.add(MenuItem.schoolMail);
        item.add(MenuItem.website);
//        item.add(MenuItem.blank);



        //家校互动
        item1.add(MenuItem.homework);
        item1.add(MenuItem.score);
        item1.add(MenuItem.comment);
        item1.add(MenuItem.dynamics);

        item1.add(MenuItem.SchoolBusAttendance);
        item1.add(MenuItem.OutIntoSchoolAttendance);
        item1.add(MenuItem.DormitoryAttendance);
        item1.add(MenuItem.ManualAttendance);

        //item1.add(MenuItem.attendance);
        //item1.add(MenuItem.parentSchool);
//        item1.add(MenuItem.blank);
//        item1.add(MenuItem.blank);
//        item1.add(MenuItem.blank);
        //学习资源
        resource.add(MenuItem.course);
//		resource.add(MenuItem.weike);
//		resource.add(MenuItem.primarySchool);
        resource.add(MenuItem.blank);
        resource.add(MenuItem.blank);
        fillBlank(resource);
    }

    private void initParentMenuFromConfig(MenuConfigBean config) {
        List<MenuConfigBean.MenuConfig> list = config.getPersonalConfigList();
        if (list != null) {
            for (MenuConfigBean.MenuConfig item : list) {
                if (item != null && item.getStatus() == MenuItem.VALID) {
                    MenuItem menuItem = map.get(item.getFunctionName());


                    if (menuItem != null) {
                        if (MenuItem.FREE.equals(item.getType())) {
                            menuItem.setFree(true);

                            freeItem.add(menuItem);


                        } else if (MenuItem.CHARGE.equals(item.getType())) {
                            menuItem.setFree(false);
                            item1.add(menuItem);
                        }
                    }
                }
            }
        }
        fillBlank(freeItem);
        fillBlank(item1);
    }

    private void fillBlank(List<MenuItem> item) {
        if (item.isEmpty()) return;
        //每列固定4个图标
        int div = item.size() % 4;
        if (div == 0) return;
        int left = 4 - div;
        int i = 0;
        while (i++ < left) {
            item.add(MenuItem.blank);
        }
    }

    private void init() {
        if (layoutView == null) return;
        menu = (GridView) layoutView.findViewById(R.id.menu);
        menu1 = (GridView) layoutView.findViewById(R.id.menu1);
        menu2 = (GridView) layoutView.findViewById(R.id.menu2);
        menu3 = (GridView) layoutView.findViewById(R.id.menu3);
        initMenuItem();
        MenuAdapter adapter, adapter1, adapter2, adapter3, adapter4;
        if (CCApplication.getInstance().isCurUserParent()) {
            MenuConfigBean config = CCApplication.getInstance().getMenuConfig();
            item1 = new ArrayList<MenuItem>();
            freeItem = new ArrayList<MenuItem>();
            if (config.getPersonalConfigList() == null) {
                initParentMenu(); //初始化默认的
            } else {
                initParentMenuFromConfig(config);  //从服务器配置信息 获取菜单
            }
            initParentOtherMenu();
            TextView title = (TextView) layoutView.findViewById(R.id.title);
            if (freeItem.size() > 0) {
                setUnreadStatus(freeItem);
                adapter = new MenuAdapter(mContext, freeItem);
                menu.setAdapter(adapter);
//                title.setText("免费应用");
                title.setText("基础服务");
            } else {
                title.setVisibility(View.GONE);
            }
            TextView title1 = (TextView) layoutView.findViewById(R.id.title1);
            if (item1.size() > 0) {
                setUnreadStatus(item1);
                adapter1 = new MenuAdapter(mContext, item1);
                menu1.setAdapter(adapter1);
                title1.setText("个性服务");
            } else {
                title1.setVisibility(View.GONE);
            }
            TextView title2 = (TextView) layoutView.findViewById(R.id.title2);
            title2.setVisibility(View.GONE);
            try {

                if (DataUtil.isMain()) {
                    title2.setVisibility(View.VISIBLE);
                    adapter2 = new MenuAdapter(mContext, resource);
                    menu2.setAdapter(adapter2);
                }
            } catch (Exception e) {

            }
            adapter3 = new MenuAdapter(mContext, other);
            menu3.setAdapter(adapter3);
        } else {
            initTeacherMenu();
            setUnreadStatus(item);
            adapter = new MenuAdapter(mContext, item);
            menu.setAdapter(adapter);

            setUnreadStatus(item1);
            adapter1 = new MenuAdapter(mContext, item1);
            menu1.setAdapter(adapter1);
            adapter2 = new MenuAdapter(mContext, resource);
            menu2.setAdapter(adapter2);

            fillBlank(resource);
            layoutView.findViewById(R.id.title3).setVisibility(View.GONE);
        }


        TextView titleText2 = (TextView) layoutView.findViewById(R.id.title_text_2);
        Button title_imgbtn = (Button) layoutView.findViewById(R.id.title_btn);
        ImageView image = (ImageView) layoutView.findViewById(R.id.title_arrow_iv);
        title_imgbtn.setVisibility(View.GONE);
        titleText2.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        titleText2.setText(R.string.service);
        titleText2.setTextColor(getResources().getColor(R.color.font_black));
    }

    //获取学段
    private String getStage() {
        UserType user = CCApplication.getInstance().getPresentUser();
        String childId = "";
        if (user != null) {
            childId = user.getChildId();
        }
        MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
        if (detail != null) {
            List<NewChild> list = detail.getChildList();
            if (list != null) {
                for (NewChild item : list) {
                    if (childId.equals(item.getChildID())) {
                        return item.getStageCode();
                    }
                }
            }
        }
        return null;
    }


    private void initData() {
        System.out.println("NewServiceFragment initData");
        user = CCApplication.getInstance().getPresentUser();
        remind = CCApplication.getInstance().getModelRemindList();
    }


    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        // 如果欠费就不允许跳转
        if (!payStatus)
            return;
    }

    //标记未读消息
    private int getUnreadStatus(String type) {
        if (remind != null) {
            List<ModelRemind> list = remind.getMessageList();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (type.equals(list.get(i).getType())) {
                        String count = list.get(i).getCount();
                        return Integer.parseInt(count);
                    }
                }
            }
        }
        return 0;
    }

    private void setUnreadStatus(List<MenuItem> items) {
        //重置未读消息数
        int total = 0;//业务办理
        for (MenuItem item : items) {
            if (item != null) {
                item.setUnreadNum(0);
            }
        }
        if (remind != null) {
            List<ModelRemind> list = remind.getMessageList();
            if (list != null) {
                //没有未读消息
                if (list.size() == 0) {
                    for (MenuItem item : items) {
                        item.setUnreadNum(0);
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    String modelType = list.get(i).getType();
                    if (Constants.LEAVE.equals(modelType)//合并为业务办理的数量
                            || Constants.PRINT.equals(modelType)
                            || Constants.GUARRANTEE.equals(modelType)) {
                        String count = list.get(i).getCount();
                        total += Integer.parseInt(count);
                    }
                    for (MenuItem item : items) {
                        if (item != null) {
                            String type = item.getItemType();
                            String count = list.get(i).getCount();
                            int num = Integer.parseInt(count);
                            if (type.equals(modelType)) {
                                item.setUnreadNum(num);
                            }

                            if (type.equals(Constants.BUSINESSTYPE)) {
                                item.setUnreadNum(total);
                            }
                        }
                    }
                }
            }
        }
    }

    //删除模块已读记录
    private void clearRemind(String type) {
        JSONObject json = new JSONObject();
        if (user != null) {
            try {
                json.put("userId", user.getUserId());
                json.put("userType", user.getUserType());
                json.put("modelType", type);
                if (CCApplication.getInstance().isCurUserParent()) {
                    json.put("studentId", user.getChildId());
                }
                System.out.println("clearRemind:" + json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(json, this, getActivity()).execute(Constants.DEL_MODEL_REMIND);
        }
    }


    @Override
    public void onRequstComplete(String result) {
        // TODO Auto-generated method stub
        System.out.println("clear remind:" + result);
        NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
        if (ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
            ModelRemindList remind = CCApplication.getInstance().getModelRemindList();
            if (remind != null) {
                int index = 0;
                List<ModelRemind> list = remind.getMessageList();
                for (ModelRemind iter : list) {
                    if (type.equals(iter.getType())) {
                        list.remove(index++);
                        break;
                    }
                }
                remind.setMessageList(list);
                CCApplication.getInstance().setModelRemindList(remind);
            }
        }
    }

    //更新未读数
    public void Refesh() {
        System.out.println("更新未读数");
        initData();
        init();
    }

    @Override
    public void onRequstCancelled() {
        // TODO Auto-generated method stub

    }
}