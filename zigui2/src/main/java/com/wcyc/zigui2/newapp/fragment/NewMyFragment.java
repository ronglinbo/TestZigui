package com.wcyc.zigui2.newapp.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.bean.MyInfoBean;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.Role;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.home.NewMySetActivity;
//import com.wcyc.zigui2.newapp.module.othernumber.NewOtherNumberActivity;
import com.wcyc.zigui2.newapp.home.NewTeacherAccountActivity;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.StudentBaseVipServices;
import com.wcyc.zigui2.newapp.module.charge2.NewRechargeProductActivity;
import com.wcyc.zigui2.newapp.module.charge2.NewVipServiceActivity;
import com.wcyc.zigui2.newapp.module.charge2.StudentBaseVipServicesAdapter;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct;


import com.wcyc.zigui2.newapp.module.order.MyOrderActivity;
import com.wcyc.zigui2.newapp.module.order.MyOrderCount;
import com.wcyc.zigui2.newapp.module.othernumber.NewOtherNumberActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.widget.MyListView;
import com.wcyc.zigui2.widget.RoundImageView;

/**
 * 我的 Fragment
 *
 * @author 谭园园
 * @version 2.0
 */
public class NewMyFragment extends Fragment implements
        OnClickListener, HttpRequestAsyncTaskListener {
    private LinearLayout myDetail;
    private RoundImageView icon;
    private TextView name, phone;
    private RelativeLayout mySet;
    public TextView new_content;
    private View layoutView;
    private LinearLayout vip_services_ll;
    private RelativeLayout order_rell;
    List<Role> role;
    private LinearLayout other_number_ll;
    private TextView other_number_numb_tv;
    private LinearLayout recharge_center_ll;
    private TextView order_number;
    public static Fragment newInstance(int index) {
        Fragment fragment = new NewMyFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }
    private final  int GET_ORDER_COUNT=1;
    private int action=-1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.new_teacher_my_firstpage, null);
        initView();
	//	initDatas();//改到了只要获得焦点就执行那里
        //获取记录数
        JSONObject json=new JSONObject();

        try {
            json.put("studentId",CCApplication.getInstance().getPresentUser().getChildId());
            json.put("schoolId",CCApplication.getInstance().getPresentUser().getSchoolId());
            new HttpRequestAsyncTask(json,this,getContext()).execute(Constants.GET_ORDER_COUNT);
            action=GET_ORDER_COUNT;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initEvents();
        return layoutView;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        order_number = (TextView) layoutView.findViewById(R.id.order_number);
        order_rell = (RelativeLayout) layoutView.findViewById(R.id.order_rell);
        myDetail = (LinearLayout) layoutView.findViewById(R.id.teacher_firstpager_my_information);
        icon = (RoundImageView) layoutView.findViewById(R.id.teacher_my_information_icon);
        name = (TextView) layoutView.findViewById(R.id.teacher_my_information_name);
        phone = (TextView) layoutView.findViewById(R.id.teacher_my_information_phone);
        mySet = (RelativeLayout) layoutView.findViewById(R.id.teacher_firstpager_my_set_ll);
        other_number_ll = (LinearLayout) layoutView.findViewById(R.id.other_number_ll);
        other_number_numb_tv = (TextView) layoutView.findViewById(R.id.other_number_numb_tv);
//		rl_charge = (LinearLayout)layoutView.findViewById(R.id.rl_charge);
        new_content = (TextView) layoutView.findViewById(R.id.new_content);
        vip_services_ll = (LinearLayout) layoutView.findViewById(R.id.vip_services_ll);
        recharge_center_ll = (LinearLayout) layoutView.findViewById(R.id.recharge_center_ll);
    }


    /**
     * 初始化数据.
     */
    private void initDatas() {
        new_content.setText("我");
        UserType user = CCApplication.getInstance().getPresentUser();
        NewMemberBean member = CCApplication.getInstance().getMemberInfo();
        String userType = user.getUserType();
        String userName = "";

        List<Role> role_a = new ArrayList<Role>();
        if (member != null) {
            String present_user_type = CCApplication.getInstance().getPresentUser().getUserType();
            if (present_user_type.equals("3")) {//如果是家长
                List<UserType> usertype = member.getUserTypeList();
                for (int i = 0; i < usertype.size(); i++) {
                    Role role_i = new Role();
                    role_i.setName(usertype.get(i).getChildName() + usertype.get(i).getRelationTypeName());

                    role_a.add(role_i);
                }
                int select = CCApplication.getInstance().getPresentUserIndex();
                userName = role_a.get(select).getName();

                if (name != null) {
                    name.setText(userName);
                }

                //1表示主号   0表示副号
                System.out.println("主号？副号？isMian=====" + DataUtil.isMain());
                if (DataUtil.isMain()) {
                    other_number_ll.setVisibility(View.VISIBLE);
                    http_url_parentMobileList(getActivity());
                } else {
                    other_number_ll.setVisibility(View.GONE);
                }

                recharge_center_ll.setVisibility(View.VISIBLE);
            } else if (present_user_type.equals("2")){//如果是老师
                other_number_ll.setVisibility(View.GONE);
                role = DataUtil.getAllRole();
                int select = CCApplication.getInstance().getPresentUserIndex();
//				userName = role.get(select).getName();
//				System.out.println("===userName=1="+userName);
//				if(name!=null){
//					name.setText(userName);
//				}
                UserType curUser = CCApplication.getInstance().getPresentUser();
                if (curUser != null) {
                    userName = curUser.getTeacherName();//
                    if (name != null) {
                        name.setText(userName + "（教职工）");
                    }
                }
            }

        }

        phone.setText(member.getMobile());
        HomeActivity activity = (HomeActivity) getActivity();
//		Bitmap bitmap = activity.getIcon();
        if (LocalUtil.mBitMap != null) {
            icon.setImageBitmap(LocalUtil.mBitMap);
        } else {
            String file = member.getUserIconURL();
            ImageUtils.showImage(getActivity(), file, icon);

        }


        if (Constants.TEACHER_STR_TYPE.equals(CCApplication.getInstance().getPresentUser().getUserType())) {
            vip_services_ll.setVisibility(View.GONE);
            recharge_center_ll.setVisibility(View.GONE);
        } else {
            vip_services_ll.setVisibility(View.VISIBLE);
            recharge_center_ll.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 事件控制.
     */
    private void initEvents() {
        myDetail.setOnClickListener(this);
        mySet.setOnClickListener(this);
        other_number_ll.setOnClickListener(this);
        vip_services_ll.setOnClickListener(this);
        recharge_center_ll.setOnClickListener(this);
        order_rell.setOnClickListener(this);
    }


    /**
     * 点击视图.
     *
     * @param v 视图
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teacher_firstpager_my_information:// 跳转到个人信息详情页面
                Intent teacherAccountIntent = new Intent(getActivity(), NewTeacherAccountActivity.class);
                startActivityForResult(teacherAccountIntent, 201);
                break;
            case R.id.teacher_firstpager_my_set_ll://我的设置
                Intent newMySetIntent = new Intent(getActivity(), NewMySetActivity.class);
                startActivity(newMySetIntent);
                break;
            case R.id.other_number_ll://副号
                Intent otherNumberIntent = new Intent(getActivity(), NewOtherNumberActivity.class);
                startActivity(otherNumberIntent);
                break;
            case R.id.vip_services_ll://我的服务
                Intent vipServicesIntent = new Intent(getActivity(), NewVipServiceActivity.class);
                startActivity(vipServicesIntent);
                break;

            case R.id.recharge_center_ll://充值中心
                Intent rechargeCenterIntent = new Intent(getActivity(), NewRechargeProductActivity.class);
                startActivity(rechargeCenterIntent);
                break;
            case R.id.order_rell://我的订单
                Intent myorderIntent = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(myorderIntent);
                break;
        }
    }

    /**
     * Resume事件.
     */
    @Override
    public void onResume() {
        super.onResume();
        initDatas();
        // 刷新获取记录数
        JSONObject json=new JSONObject();

        try {
            json.put("studentId",CCApplication.getInstance().getPresentUser().getChildId());
            json.put("schoolId",CCApplication.getInstance().getPresentUser().getSchoolId());
            new HttpRequestAsyncTask(json,this,getContext()).execute(Constants.GET_ORDER_COUNT);
            action=GET_ORDER_COUNT;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取未读消息数.
     *
     * @return 未读消息数
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        return unreadMsgCountTotal;
    }

    private void http_url_parentMobileList(Context mContext) {
        String childId = CCApplication.getInstance().getPresentUser().getChildId();
        if (childId != null) {
            try {
                JSONObject json = new JSONObject();
                json.put("childId", childId);//
//				System.out.println("副号入参json=====" + json);
                String url = new StringBuilder(Constants.SERVER_URL).append(
                        Constants.PARENT_MOBILE_LIST).toString();//

                String result = HttpHelper.httpPostJson(getActivity(), url, json);
//        		System.out.println("副号出参data=====" + result);
                NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
                if (ret.getServerResult().getResultCode() != 200) {// 请求失败
                    DataUtil.getToast(ret.getServerResult().getResultMessage());
                } else {
                    try {
                        JSONObject jsonA = new JSONObject(result);
                        int viceNumber = jsonA.getInt("viceNumber");

                        other_number_numb_tv.setText(viceNumber + "个副号");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequstComplete(String result) {
        switch (action){
            case GET_ORDER_COUNT:
                try {
                    System.out.println("待支付，欠款记录数"+result);
                    MyOrderCount myOrderCount= JsonUtils.fromJson(result, MyOrderCount.class);
                   int num= Integer.parseInt(myOrderCount.getDebtCount())+Integer.parseInt(myOrderCount.getNoPayCount());
                    if(num>0){
                        order_number.setVisibility(View.VISIBLE);
                        if(num>99){
                            order_number.setText("99+");
                        }else{
                            order_number.setText(num+"");
                        }

                    }else{
                        order_number.setVisibility(View.INVISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onRequstCancelled() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
