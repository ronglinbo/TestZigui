package com.wcyc.zigui2.newapp.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.NewClassDynamicsBean;
import com.wcyc.zigui2.bean.NewClassDynamicsBean1;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;


import com.wcyc.zigui2.newapp.activity.NewEducationInfoActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.GradeClass;
import com.wcyc.zigui2.newapp.bean.GradeleaderBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.ModelRemindList;
import com.wcyc.zigui2.newapp.bean.ModelRemindList.ModelRemind;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.classdynamics.NewClassDynamicsActivity;
import com.wcyc.zigui2.newapp.module.educationinfor.EducationInforActivity;
import com.wcyc.zigui2.newapp.module.news.NewSchoolNewsActivity;
import com.wcyc.zigui2.utils.CircleImageView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.RoundImageView;

/**
 * 新版app发现 Fragment
 *
 * @author yytan
 */
public class NewFindFragment extends Fragment implements OnClickListener, HttpRequestAsyncTaskListener {

    public RelativeLayout rl_find;
    private Button tab;
    public TextView new_content;
    private View layoutView;
    public getUserid listener;
    private RoundImageView riv_find;
    private ImageView unread_msg_number, ed_unread_msg_number;
    private RelativeLayout rl_find2;
    private RelativeLayout education_rl;
    public static int ed_unread_msg_int = 0;

    public static Fragment newInstance(int index) {
        Fragment fragment = new NewFindFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }


    public interface getUserid {
        String getuserid();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.new_find);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // listener = (getUserid) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ed_unread_msg_int <= 0) {
            ed_unread_msg_number.setVisibility(View.INVISIBLE);
            System.out.println("==无最教育资讯===");
            return;
        } else {
            ed_unread_msg_number.setVisibility(View.VISIBLE);
            System.out.println("==有最新教育资讯===" + ed_unread_msg_int);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.new_find, null);
        initView();
        initDatas();
        initEvents();
        return layoutView;
    }

    private void initView() {
        rl_find = (RelativeLayout) layoutView.findViewById(R.id.rl_find);
        rl_find2 = (RelativeLayout) layoutView.findViewById(R.id.rl_find2);
        rl_find.setVisibility(View.GONE);
        rl_find2.setVisibility(View.GONE);
        new_content = (TextView) layoutView.findViewById(R.id.new_content);
        riv_find = (RoundImageView) layoutView.findViewById(R.id.riv_find);
        riv_find.setVisibility(View.GONE);
        unread_msg_number = (ImageView) layoutView.findViewById(R.id.unread_msg_number);
        education_rl = (RelativeLayout) layoutView.findViewById(R.id.education_rl);
        ed_unread_msg_number = (ImageView) layoutView.findViewById(R.id.ed_unread_msg_number);
    }

    private void initDatas() {
        new_content.setText("发现");

        ModelRemindList remindList = CCApplication.getInstance().getModelRemindList();
        List<ModelRemind> remind = new ArrayList<ModelRemind>();
        if (remindList != null) {
            remind = remindList.getMessageList();
        }
        String unReadNumb = "";
        if (remind != null) {
            for (int i = 0; i < remind.size(); i++) {
                String remindType = remind.get(i).getType();
                System.out.println(remindType + "=remindType==新消息==Count==" + remind.get(i).getCount());
                if ("28".equals(remindType)) {
                    unReadNumb = remind.get(i).getCount();
                }
            }
        }
        if (DataUtil.isNullorEmpty(unReadNumb)) {
            ed_unread_msg_int = 0;
            return;
        } else {
            ed_unread_msg_int = Integer.parseInt(unReadNumb);
        }

        //2.0.12发现隐藏班级动态和校园新闻
//
//        try {
//            String usertype = CCApplication.getInstance().getPresentUser()
//                    .getUserType();
//            List<NewClasses> cList = new ArrayList<NewClasses>();
//            if (Constants.TEACHER_STR_TYPE.equals(usertype)) {
//                cList = CCApplication.app.getMemberDetail().getClassList();
//
//                boolean allowAllClassTag = false;
//                boolean gradeleader = false;
//                MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
//                for (int i = 0; i < detail.getRoleList().size(); i++) {
//                    String roleCode = detail.getRoleList().get(i).getRoleCode();
//                    if ("schooladmin".equals(roleCode)) {
//                        allowAllClassTag = true;
//                    }
//                    if ("schoolleader".equals(roleCode)) {
//                        allowAllClassTag = true;
//                    }
//                    if ("fileadmin".equals(roleCode)) {
//                        allowAllClassTag = true;
//                    }
//
//                    if ("gradeleader".equals(roleCode)) {
//                        allowAllClassTag = true;
//                        gradeleader = true;
//                    }
//
//                }
//
//                if (allowAllClassTag) {
//                    if (cList == null) {
//                        cList = new ArrayList<NewClasses>();
//                    }
//                    List<NewClasses> schoolAllClassList = CCApplication.getInstance().getSchoolAllClassList();
//                    if(schoolAllClassList!=null&& !gradeleader){
//                        cList.clear();
//                        cList.addAll(schoolAllClassList);
//                    }else if (schoolAllClassList != null && gradeleader){//如果是年级组长
//                        try {
//                            if (cList == null) {
//                                cList = new ArrayList<NewClasses>();
//                            }
//                            List<GradeleaderBean> gradeInfoList = CCApplication.getInstance().getMemberDetail().getGradeInfoList();
//                            for (int i = 0; i < gradeInfoList.size(); i++) {
//                                String userGradeId=gradeInfoList.get(i).getGradeId();
//                                for (int j = 0; j < schoolAllClassList.size(); j++){
//                                    String gradeId=schoolAllClassList.get(j).getGradeId();
//                                    if(userGradeId.equals(gradeId)){
//                                        cList.add(schoolAllClassList.get(j));
//                                    }
//                                }
//                            }
//                        } catch (Exception e) {
//                        }
//
//                    }
//                }
//            } else if (Constants.PARENT_STR_TYPE.equals(usertype)) {
//                List<NewChild> childlist = CCApplication.app.getMemberDetail()
//                        .getChildList();
//                if (childlist != null) {
//                    for (int i = 0; i < childlist.size(); i++) {
//                        NewClasses newclass = new NewClasses();
//                        newclass.setClassID(childlist.get(i).getChildClassID());
//                        newclass.setClassName(childlist.get(i)
//                                .getChildClassName());
//                        newclass.setGradeId(childlist.get(i).getGradeId());
//                        newclass.setGradeName(childlist.get(i).getGradeName());
//                        cList.add(newclass);
//                    }
//                }
//            }
//            JSONObject json = null;
//            ArrayList<String> mmClassIDlist = new ArrayList<String>();
//            if (cList != null) {
//                for (int i = 0; i < cList.size(); i++) {
//                    String mClassID = cList.get(i).getClassID();
//                    mmClassIDlist.add(mClassID);
//                }
//                NewClassDynamicsBean cd = new NewClassDynamicsBean();
//                cd.setClassIdList(mmClassIDlist);
//                cd.setCurPage(1);
//                cd.setPageSize(1);
//                cd.setIsNeedCLA("1");
//                Gson gson = new Gson();
//                String string = gson.toJson(cd);
//                json = new JSONObject(string);
//                System.out.println("======json=======" + json);
//
//                if (!DataUtil.isNetworkAvailable(getActivity())) {
//                    DataUtil.getToast(getResources().getString(R.string.no_network));// 当前网络不可用，请检查您的网络设置
//                    return;
//                }
//
//                //改为异步的
//                new HttpRequestAsyncTask(json,this,getActivity()).execute(Constants.GET_CLASS_DYNAMIC_LIST);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private void initEvents() {
        rl_find.setOnClickListener(this);
        rl_find2.setOnClickListener(this);
        education_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_find:// 班级动态
                unread_msg_number.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getActivity(),
                        NewClassDynamicsActivity.class);
                intent.putExtra("userid", CCApplication.getInstance()
                        .getPresentUser().getUserId());
                startActivity(intent);
                break;
            case R.id.rl_find2:// 校园新闻
                ((BaseActivity) getActivity()).newActivity(NewSchoolNewsActivity.class, null);//校园新闻
                break;
            case R.id.education_rl:
//                ((BaseActivity) getActivity()).newActivity(EducationInforActivity.class, null);//教育资讯
                ((BaseActivity) getActivity()).newActivity(NewEducationInfoActivity.class, null);//新版教育资讯
                break;
        }

    }


    @Override
    public void onRequstComplete(String result) {
        try {
            JSONObject json2 = new JSONObject(result);
            JSONArray ja = json2.getJSONArray("interactionList");
            Gson gson2 = new Gson();
            Type t = new TypeToken<List<NewClassDynamicsBean1>>() {
            }.getType();
            List<NewClassDynamicsBean1> list = gson2.fromJson(ja.toString(), t);
            if (list != null) {
                String imgUrl = list.get(0).getPublisherImgUrl();
                if (!DataUtil.isNullorEmpty(imgUrl)) {
                    //方法二
                    ImageUtils.showImage(getActivity(), imgUrl, riv_find);//缩略图
                    riv_find.setVisibility(View.VISIBLE);
                    CCApplication.getInstance().setClassDynUpdate(imgUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequstCancelled() {

    }

}
