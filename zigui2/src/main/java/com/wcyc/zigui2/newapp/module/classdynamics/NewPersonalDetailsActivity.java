package com.wcyc.zigui2.newapp.module.classdynamics;

/**
 * 个人一条动态详情界面
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.newapp.adapter.CommentsListViewAdapter;
import com.wcyc.zigui2.bean.NewClassDynamicsBean;
import com.wcyc.zigui2.bean.NewClassDynamicsBean1;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.adapter.NewGridViewAdapter;

import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.bean.ChildRelationTypeBean;
import com.wcyc.zigui2.newapp.bean.GradeleaderBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewPointBean;
import com.wcyc.zigui2.newapp.bean.ShareModel;
import com.wcyc.zigui2.newapp.videoutils.VideoViewActivity;
import com.wcyc.zigui2.newapp.widget.SharePopWindow;
import com.wcyc.zigui2.utils.ApiManager;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.PhotoBitmapUtils;
import com.wcyc.zigui2.utils.RepeatOnClick;
import com.wcyc.zigui2.widget.MyListView;
import com.wcyc.zigui2.widget.NoScrollGridView;
import com.wcyc.zigui2.widget.RoundImageView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 *
 * 创 建 人： gdzheng
 * 日    期： 2016-04-06
 *
 */
public class NewPersonalDetailsActivity extends BaseActivity implements
        HttpRequestAsyncTaskListener, OnClickListener {

    private static final int ACTION_DELETE_MESSAGE_ITEM = 0x00001;

    private RoundImageView one_class_dynamics_civ;
    private TextView one_author_name, one_author_delete, new_message_content,
            one_message_time;
    private NoScrollGridView one_gridView;
    private ImageView new_message_support;
    private MyListView one_message_comment_lv;
    private View line;
    private LinearLayout title_back;
    String publishUserId;
    int v;
    private TextView new_content;
    private ImageView iv_title_youxia;
    private List<NewClassDynamicsBean1> onelist;
    private TextView one_mew_bjmc;
    private TextView one_mew_bjmc_couse_teacher;
    private ImageView message_support_count_icon;
    private String user_id_app;
    private String class_id_a;
    private ImageView message_comment_icon;
    private TextView message_support_count_name;
    private String usertype;
    private RelativeLayout message_ll_support_count;
    private List<ChildRelationTypeBean> childRelationTypeList;
    private ImageView class_vedio_iv;
    private RelativeLayout class_vedio_ll;
    private ImageView iv_share;
    private int dynamicsId;
    private String dynamicsContent;
    private Context context;
    private View parentView;
    private TextView tv_one_message_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        parentView = LayoutInflater.from(context).inflate(R.layout.new_personal_details, null, false);
        setContentView(parentView);
        initView();
        initData();
        initEvents();

    }

    private void initView() {
        new_content = (TextView) parentView.findViewById(R.id.new_content);
        iv_title_youxia = (ImageView) parentView.findViewById(R.id.iv_title_xiala);
        title_back = (LinearLayout) parentView.findViewById(R.id.title_back);
        one_class_dynamics_civ = (RoundImageView) parentView.findViewById(R.id.one_class_dynamics_civ);
        one_author_name = (TextView) parentView.findViewById(R.id.one_author_name);
        one_mew_bjmc = (TextView) parentView.findViewById(R.id.one_mew_bjmc);
        one_mew_bjmc_couse_teacher = (TextView) parentView.findViewById(R.id.one_mew_bjmc_couse_teacher);

        one_author_delete = (TextView) parentView.findViewById(R.id.one_author_delete);
        new_message_content = (TextView) parentView.findViewById(R.id.new_message_content);
        one_message_time = (TextView) parentView.findViewById(R.id.one_message_time);
        one_gridView = (NoScrollGridView) parentView.findViewById(R.id.one_gridView);

        new_message_support = (ImageView) parentView.findViewById(R.id.new_message_support);
        message_support_count_icon = (ImageView) parentView.findViewById(R.id.message_support_count_icon);
        message_ll_support_count = (RelativeLayout) parentView.findViewById(R.id.message_ll_support_count);
        message_comment_icon = (ImageView) parentView.findViewById(R.id.message_comment_icon);
        message_support_count_name = (TextView) parentView.findViewById(R.id.message_support_count_name);
        one_message_comment_lv = (MyListView) parentView.findViewById(R.id.one_message_comment_lv);
        line = parentView.findViewById(R.id.line);
        class_vedio_iv = (ImageView) parentView.findViewById(R.id.class_vedio_iv);
        class_vedio_ll = (RelativeLayout) parentView.findViewById(R.id.class_vedio_ll);

        iv_share = (ImageView) parentView.findViewById(R.id.iv_share);

        tv_one_message_delete = (TextView) findViewById(R.id.tv_one_message_delete);
    }

    private void initData() {

        //获取到个人的id
        String user_id_app = CCApplication.getInstance().getPresentUser()
                .getUserId();

        getRelationCode();

        v = getIntent().getExtras().getInt("v");
        v = v + 1;//下标从0开始  ，第几项从1开始
        int pageNumb = v / 10;//整除
        int weiNumb = v % 10;//取余
        if (weiNumb > 0) {
            pageNumb += 1;//如果有余，则页数加1
        }

        if (v > 10) {
            v = v - (pageNumb - 1) * 10;//某一页的第几项
        }
        v = v - 1;//下标从0开始  ，第几项从1开始
        publishUserId = getIntent().getExtras().getString("publishUserId");


        if (user_id_app.equals(publishUserId)) {
            tv_one_message_delete.setVisibility(View.VISIBLE);
        } else {
            tv_one_message_delete.setVisibility(View.INVISIBLE);
        }

        title_back.setVisibility(View.VISIBLE);
        iv_title_youxia.setVisibility(View.GONE);

        try {
            usertype = CCApplication.getInstance().getPresentUser()
                    .getUserType();
            // 班级列表
            List<NewClasses> cList = new ArrayList<NewClasses>();

            if ("2".equals(usertype)) {
                cList = CCApplication.app.getMemberDetail()// 保存列表中class_id
                        .getClassList();
                boolean allowAllClassTag = false;
                boolean gradeleader = false;
                MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
                for (int i = 0; i < detail.getRoleList().size(); i++) {
                    String roleCode = detail.getRoleList().get(i).getRoleCode();

                    if ("schooladmin".equals(roleCode)) {
                        allowAllClassTag = true;
                    }

                    if ("schoolleader".equals(roleCode)) {
                        allowAllClassTag = true;
                    }

                    if ("fileadmin".equals(roleCode)) {
                        allowAllClassTag = true;
                    }

                    if ("gradeleader".equals(roleCode)) {
                        allowAllClassTag = true;
                        gradeleader = true;
                    }

                    String phoneNumA = CCApplication.getInstance().getPhoneNum();
                    if ("13687395021".equals(phoneNumA)) {
                        allowAllClassTag = true;
                    }

                }

                if (allowAllClassTag) {
                    if (cList == null) {
                        cList = new ArrayList<NewClasses>();
                    }
                    List<NewClasses> schoolAllClassList = CCApplication.getInstance().getSchoolAllClassList();
                    if (schoolAllClassList != null && !gradeleader) {
                        cList.clear();
                        cList.addAll(schoolAllClassList);
                    } else if (schoolAllClassList != null && gradeleader) {//如果是年级组长
                        try {
                            if (cList == null) {
                                cList = new ArrayList<NewClasses>();
                            }
                            List<GradeleaderBean> gradeInfoList = CCApplication.getInstance().getMemberDetail().getGradeInfoList();
                            for (int i = 0; i < gradeInfoList.size(); i++) {
                                String userGradeId = gradeInfoList.get(i).getGradeId();
                                for (int j = 0; j < schoolAllClassList.size(); j++) {
                                    String gradeId = schoolAllClassList.get(j).getGradeId();
                                    if (userGradeId.equals(gradeId)) {
                                        cList.add(schoolAllClassList.get(j));
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }

            } else if ("3".equals(usertype)) {
                List<NewChild> childlist = CCApplication.app.getMemberDetail()
                        .getChildList();
                if (childlist != null) {
                    for (int i = 0; i < childlist.size(); i++) {
                        NewClasses newclass = new NewClasses();
//						newclass.setClassId(childlist.get(i).getChildClassID());
                        newclass.setClassID(childlist.get(i).getChildClassID());
                        newclass.setClassName(childlist.get(i)
                                .getChildClassName());
                        newclass.setGradeId(childlist.get(i).getGradeId());
                        newclass.setGradeName(childlist.get(i).getGradeName());

                        cList.add(newclass);
                    }
                }
            }

            // JSON对象
            JSONObject json = null;
            // 结果
            String result = null;

            ArrayList<String> mmClassIDlist = new ArrayList<String>();
            for (int i = 0; i < cList.size(); i++) {
                String mClassID = cList.get(i).getClassID();
                mmClassIDlist.add(mClassID);
            }

            String classId = getIntent().getExtras().getString("classId");
            if (!DataUtil.isNullorEmpty(classId)) {
                mmClassIDlist.clear();
                mmClassIDlist.add(classId);
            }

            NewClassDynamicsBean cd = new NewClassDynamicsBean();
            cd.setClassIdList(mmClassIDlist);
            cd.setCurPage(pageNumb);
            cd.setPageSize(10);
            cd.setIsNeedCLA("1");
            cd.setHidePublish(true);

            Gson gson = new Gson();

            String string = gson.toJson(cd);
            json = new JSONObject(string);
            json.put("type", "1");
            json.put("userId", publishUserId);


            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.GET_CLASS_DYNAMIC_LIST).toString();
            result = HttpHelper.httpPostJson(this, url, json);

            showContent(cList, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showContent(List<NewClasses> cList, String result) throws JSONException {
        JSONObject json5 = new JSONObject(result);
        JSONArray ja = json5.getJSONArray("interactionList");
        Gson gson2 = new Gson();
        Type t = new TypeToken<List<NewClassDynamicsBean1>>() {
        }.getType();
        onelist = gson2.fromJson(ja.toString(), t);

        String publishUserTpye = onelist.get(v).getPublishUserType();
        new_content.setText("动态详情");// 统一显示  动态详情  几个字

        if ("2".equals(publishUserTpye)) {
//				new_content.setText(onelist.get(v).getPublisherName());// 老师 标题名字
            one_author_name.setText(onelist.get(v).getPublisherName());// 发布人名字
        } else if ("3".equals(publishUserTpye)) {
//				new_content.setText(onelist.get(v).getPublishChildName()
//						+onelist.get(v).getRelation());// 标题   家长端
            one_author_name.setText(onelist.get(v).getPublishChildName() + onelist.get(v).getRelation());// 发布人  家长端
        }

        class_id_a = onelist.get(v).getClassId() + "";
        String class_name_b = "";


        //判断身份.如果是教师身份,就用传递过来的数据.
/*        String userType = CCApplication.getInstance().getPresentUser().getUserType();
        if (Constants.TEACHER_STR_TYPE.equals(userType)) {
            String className = onelist.get(v).getClassName();
            one_mew_bjmc.setText(className);
        } else {
            for (int i = 0; i < cList.size(); i++) {
                String class_id_b = cList.get(i).getClassID();
                if (class_id_a.equals(class_id_b)) {
                    class_name_b = cList.get(i).getClassName();
                    one_mew_bjmc.setText(class_name_b);
                }
            }

            String subjectView = onelist.get(v).getSubjectView();// 发布人在这个班任教科目
            if (!DataUtil.isNullorEmpty(subjectView)) {
                one_mew_bjmc_couse_teacher.setText(subjectView + "老师");
            }
        }*/

        for (int i = 0; i < cList.size(); i++) {
            String class_id_b = cList.get(i).getClassID();
            if (class_id_a.equals(class_id_b)) {
                class_name_b = cList.get(i).getClassName();
                one_mew_bjmc.setText(class_name_b);
            }
        }

        String subjectView = onelist.get(v).getSubjectView();// 发布人在这个班任教科目
        if (!DataUtil.isNullorEmpty(subjectView)) {
            one_mew_bjmc_couse_teacher.setText(subjectView + "老师");
        }

        // 这条班级动态人的头像
        if (onelist.get(v).getPublisherImgUrl() != null) {
//				String url1 = DataUtil.getDownloadURL(this, onelist.get(v)
//						.getPublisherImgUrl());
//				this.getImageLoader().displayImage(url1,
//						one_class_dynamics_civ, this.getImageLoaderOptions());

            //方法二
            String url11 = onelist.get(v).getPublisherImgUrl();
            ImageUtils.showImage(this, onelist.get(v).getPublisherImgUrl(), one_class_dynamics_civ);//缩略图
        }

        user_id_app = CCApplication.app.getPresentUser().getUserId();
        // 删除按钮

        one_author_delete.setVisibility(View.GONE);// 删除键 设置隐藏了 个人动态详情不能删除
        // }

        // 内容
        dynamicsContent = onelist.get(v).getContent();
        if (DataUtil.isNullorEmpty(dynamicsContent)) {
            new_message_content.setVisibility(View.GONE);
        } else {
            new_message_content.setVisibility(View.VISIBLE);
            new_message_content.setText(dynamicsContent);

            try {
                Linkify.addLinks(new_message_content, Linkify.WEB_URLS);//识别文本超链接
            } catch (Exception e) {
                DataUtil.getToast("您把手机内置默认浏览器删除了~~~");
            }
        }

        String time = onelist.get(v).getPublishTime();

        Date nowdate = new Date();//当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式
        String nowdate_str = sdf.format(nowdate);
        Date d;
        try {
            nowdate = sdf.parse(nowdate_str);//当前时间 年月日
            d = sdf.parse(time);//发布时间  年月日
            int flag = d.compareTo(nowdate);//比较
            if (flag >= 0) {//  >=0  今天，
                time = time.substring(10, 16);
                one_message_time.setText(time);
            } else {// <0就是以前
                time = time.substring(5, 16);
                one_message_time.setText(time);//发布时间
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 评论的条数是大于0的时候就显示下划线
        int comSize = onelist.get(v).getCommentList().size();
        if (comSize == 0) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.VISIBLE);
        }


        // 图片处理
        if (onelist.get(v).getAttachmentInfoList_new() != null) {
            List<String> pcList = new ArrayList<String>();
            List<NewClassDynamicsBean1.ClassAttachmentBean> clssAttachmentList = new ArrayList<NewClassDynamicsBean1.ClassAttachmentBean>();
            clssAttachmentList = onelist.get(v).getAttachmentInfoList_new();
            for (int k = 0; k < clssAttachmentList.size(); k++) {
                String attachementUrl = onelist.get(v).getAttachmentInfoList_new().get(k).getAttachementUrl();
                pcList.add(attachementUrl);
            }

            String attchmentType = onelist.get(v).getAttchmentType();//1是图片  2是视频音频 3是文件文档
            final List<String> pcList_final = pcList;
            //			2是app发的   5和6是web发的
            if ("2".equals(attchmentType) || "6".equals(attchmentType) || "5".equals(attchmentType)) {
                one_gridView.setVisibility(View.GONE);
                class_vedio_iv.setVisibility(View.VISIBLE);
                class_vedio_ll.setVisibility(View.VISIBLE);
//                    class_vedio_iv.setImageResource(R.drawable.shipinmorentubiao);
                if ("2".equals(attchmentType)) {//如果是app发的班级动态
                    String pcitureAddress = onelist.get(v).getPcitureAddress();
                    if (!DataUtil.isNullorEmpty(pcitureAddress)) {
                        String vedioPicUrl = DataUtil.getDownloadURL(this, "/downloadApi?" + pcitureAddress);
                        if (!DataUtil.isNullorEmpty(vedioPicUrl)) {
                            vedioPicUrl = vedioPicUrl.replaceAll("&vedio=ys", "");
                        }
                        getImageLoader().displayImage(vedioPicUrl, class_vedio_iv, getImageLoaderOptions());
                    } else {
                        class_vedio_iv.setImageResource(R.drawable.default_image);
                    }
                } else {
                    String vedioPicUrl = DataUtil.getDownloadURL(this, pcList.get(0) + "&vedio=pic");
                    if (!DataUtil.isNullorEmpty(vedioPicUrl)) {
                        vedioPicUrl = vedioPicUrl.replaceAll("&vedio=ys", "");
                    }
                    getImageLoader().displayImage(vedioPicUrl, class_vedio_iv, getImageLoaderOptions());

                }

                class_vedio_iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        PictureURL pictureURL = null;
//                        List<PictureURL> datas = new ArrayList<PictureURL>();
//                        pictureURL = new PictureURL();
//                        String url = DataUtil.getDownloadURL(NewPersonalDetailsActivity.this, pcList_final.get(0));
//                        pictureURL.setPictureURL(url);
//                        datas.add(pictureURL);
//
//                        Intent intent = new Intent(NewPersonalDetailsActivity.this,
//                                VideoViewActivity.class);
//                        intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_URLS,
//                                (Serializable) datas);
//                        intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_INDEX,
//                                0);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);


                        PictureURL pictureURL = null;
                        List<PictureURL> datas = new ArrayList<PictureURL>();
                        pictureURL = new PictureURL();
                        String url = DataUtil.getDownloadURL(NewPersonalDetailsActivity.this, pcList_final.get(0));
                        String filename = pcList_final.get(0);
                        filename = filename.substring(filename.indexOf("fileId="));
                        showProgessBar();
                        downLoad(url, filename);
                    }
                });
            } else {
                class_vedio_iv.setVisibility(View.GONE);
                class_vedio_ll.setVisibility(View.GONE);
                if (pcList.size() == 0) {
                    one_gridView.setVisibility(View.GONE);
                } else {
                    one_gridView.setVisibility(View.VISIBLE);
                    NewGridViewAdapter imageAdapter = new NewGridViewAdapter(
                            this, pcList, pcList, mBitmapMap);
                    one_gridView.setAdapter(imageAdapter);
                }
            }
        }

        // 点赞图标
        final List<NewPointBean> lovel = onelist.get(v).getLoveList();
        for (int i = 0; i < lovel.size(); i++) {
            if (lovel.get(i).getCommentUserId() == Integer
                    .parseInt(user_id_app)) {
                new_message_support
                        .setImageResource(R.drawable.new_love_ok);
            } else {
                new_message_support
                        .setImageResource(R.drawable.new_love_no);
            }
        }

        final int v_a = v;
        // 点赞人图标事件
        new_message_support.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!DataUtil.isNetworkAvailable(getApplicationContext())) {
                    DataUtil.getToast(getApplicationContext()
                            .getResources().getString(R.string.no_network));
                    return;
                }
                if (RepeatOnClick.isFastClick()) {
                    return;
                }
                handleClickPraise(v_a);
            }
        });

        // 点赞人名前面图标
        // new_goodlove_gv_p
        if (lovel.size() == 0) {// 点赞人前面图标
            message_support_count_icon.setVisibility(View.GONE);
            message_ll_support_count.setVisibility(View.GONE);

        } else {
            message_support_count_icon.setVisibility(View.VISIBLE);
            message_ll_support_count.setVisibility(View.VISIBLE);

        }

        // 点赞人名
        // 点赞人名字的显示
        // lnList = new LoveNameAdapter(this, lovel);
        // new_goodlove_gv_p.setAdapter(lnList);
        message_support_count_name.setText("");
        if (lovel.size() == 0) {// 点赞人前面图标
            message_support_count_icon.setVisibility(View.GONE);
            message_ll_support_count.setVisibility(View.GONE);

        } else {
            message_support_count_icon.setVisibility(View.VISIBLE);
            message_ll_support_count.setVisibility(View.VISIBLE);

            for (int i = 0; i < lovel.size(); i++) {

                String commentUserNameStr = lovel.get(i)
                        .getCommentUserName();

                SpannableString spannableString = new SpannableString(
                        commentUserNameStr + ",");

                final int i2 = i;
                spannableString.setSpan(new ClickableSpan() {

                                            @Override
                                            public void updateDrawState(TextPaint ds) {
                                                super.updateDrawState(ds);
                                                // ds.setColor(Color.WHITE); //设置文件颜色
                                                ds.setUnderlineText(false); // 设置取消下划线
                                                ds.setColor(Color.parseColor("#01afef"));
                                            }

                                            @Override
                                            public void onClick(View widget) {

                                                if (!DataUtil.isNetworkAvailable(NewPersonalDetailsActivity.this)) {
                                                    DataUtil.getToast("无网络");
                                                    return;
                                                }
                                                String commentUserName = lovel.get(i2).getCommentUserName();
                                                if (isFamily(commentUserName)) {
                                                    DataUtil.getToast("家长暂未开放动态！");
                                                } else {

                                                    Intent intent = new Intent(NewPersonalDetailsActivity.this,
                                                            NewPersonalDynamicsActivity.class);
                                                    intent.putExtra("publishUserId", lovel.get(i2)
                                                            .getCommentUserId() + "");

                                                    String publisherImgUrl = getPointOrCommentUrl(lovel.get(i2)
                                                            .getCommentUserId() + "", onelist);
                                                    if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                                        intent.putExtra("publisherImgUrl", publisherImgUrl);
                                                    } else {
                                                        intent.putExtra("publisherImgUrl", lovel.get(i2)
                                                                .getCommentUserId());// 服务器没有传头像地址给我
                                                    }

                                                    intent.putExtra("publisherName", lovel.get(i2)
                                                            .getCommentUserName());
                                                    intent.putExtra("classId", onelist.get(v).getClassId() + "");

                                                    startActivity(intent);
                                                    NewPersonalDetailsActivity.this.finish();
                                                }


                                            }
                                        }, 0, commentUserNameStr.length() + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                // textview.setHighlightColor(Color.TRANSPARENT);//取消高亮

                message_support_count_name.append(spannableString);
                message_support_count_name.setVisibility(View.VISIBLE);
                message_support_count_name
                        .setMovementMethod(LinkMovementMethod.getInstance());
            }

            subLikeTextView();

        }

        // 评论list设置数据

        CommentsListViewAdapter listAdapter = new CommentsListViewAdapter(
                this, onelist.get(v).getCommentList());
        one_message_comment_lv.setAdapter(listAdapter);

        dynamicsId = onelist.get(v).getId();

        //删除事件
        tv_one_message_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DataUtil.isNetworkAvailable(getApplicationContext())) {
                    DataUtil.getToast(getApplicationContext()
                            .getResources().getString(R.string.no_network));
                    return;
                }
                if (RepeatOnClick.isFastClick()) {
                    return;
                }

                //删除整条动态
                deleteMessage(dynamicsId);
            }
        });

        dismissPd();
    }

    /**
     * 截取点赞人数后面的逗号
     */
    private void subLikeTextView() {
        String trim = message_support_count_name.getText().toString().trim();
        if (!DataUtil.isNullorEmpty(trim)) {
            String substring = trim.substring(0, trim.length() - 1);
            message_support_count_name.setText(substring);
        }
    }

    /**
     * 删除整条动态
     *
     * @param id
     */
    private void deleteMessage(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("是否删除本条班级动态?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doDeleteMessage(id);
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create();
        builder.show();

    }

    private void doDeleteMessage(int id) {
        JSONObject json = new JSONObject();
        try {
            json.put("classDynamicId", id);
            json.put("hidePublish", true);

            //网络是否可用
            if (!DataUtil.isNetworkAvailable(this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }
            //是否在上一个请求中
            if (!isLoading()) {
                action = ACTION_DELETE_MESSAGE_ITEM;
                System.out.println("删除班级动态入参=====" + json);
                queryPost(Constants.DELETE_CLASS_DYNAMIC, json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvents() {
        title_back.setOnClickListener(this);

        final int v_a = v;
        // 评论图标点击事件
        message_comment_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                handleAddComments(v_a, false, 0);
            }
        });

        // 评论的条目删除,主号才能操作
        one_message_comment_lv
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {

                        final int final_arg2 = arg2;

                        // 暂时不考虑主副号isMain
                        String authorid = onelist.get(v_a).getCommentList()
                                .get(arg2).getCommentUserId()
                                + "";

                        //
                        if (user_id_app.equals(authorid)) {
                            handleDelete(v_a, arg2, 0);
                        } else {
                            // 不相等 就是回复那个回复人的回复
                            handleAddComments(v_a, true, arg2);
                        }

                        arg1.findViewById(R.id.authorname).setOnClickListener(NewPersonalDetailsActivity.this);
                        arg1.findViewById(R.id.authorname).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!DataUtil.isNetworkAvailable(NewPersonalDetailsActivity.this)) {
                                    DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                                    return;
                                }

                                String commentUserName = onelist.get(v_a).getCommentList().get(final_arg2).getCommentUserName() + "";
                                if (isFamily(commentUserName)) {
                                    DataUtil.getToast("家长暂未开放动态！");
                                } else {

                                    Intent intent = new Intent(NewPersonalDetailsActivity.this,
                                            NewPersonalDynamicsActivity.class);
                                    intent.putExtra("publishUserId",
                                            onelist.get(v_a).getCommentList().get(final_arg2).getCommentUserId() + "");

                                    String publisherImgUrl = getPointOrCommentUrl(
                                            onelist.get(v_a).getCommentList().get(final_arg2).getCommentUserId() + "", onelist);
                                    if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                        intent.putExtra("publisherImgUrl", publisherImgUrl);
                                    } else {
                                        intent.putExtra("publisherImgUrl",
                                                onelist.get(v_a).getCommentList().get(final_arg2).getCommentUserId() + "");
                                        // 服务器没有传头像地址给我
                                    }
                                    intent.putExtra("publisherName",
                                            onelist.get(v_a).getCommentList().get(final_arg2).getCommentUserName() + "");
                                    intent.putExtra("classId", onelist.get(v_a).getClassId() + "");
                                    startActivity(intent);
                                    NewPersonalDetailsActivity.this.finish();
                                }
                            }
                        });

                        arg1.findViewById(R.id.authorname_to_authorname_tv).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!DataUtil.isNetworkAvailable(NewPersonalDetailsActivity.this)) {
                                    DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                                    return;
                                }
                                String commentUserName = onelist.get(v_a).getCommentList().get(final_arg2).getToCommentUserName() + "";
                                if (isFamily(commentUserName)) {
                                    DataUtil.getToast("家长暂未开放动态！");
                                } else {

                                    Intent intent = new Intent(NewPersonalDetailsActivity.this,
                                            NewPersonalDynamicsActivity.class);
                                    intent.putExtra("publishUserId",
                                            onelist.get(v_a).getCommentList().get(final_arg2).getToCommentUserId() + "");

                                    String publisherImgUrl = getPointOrCommentUrl(
                                            onelist.get(v_a).getCommentList().get(final_arg2).getToCommentUserId() + "", onelist);
                                    if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                        intent.putExtra("publisherImgUrl", publisherImgUrl);
                                    } else {
                                        intent.putExtra("publisherImgUrl",
                                                onelist.get(v_a).getCommentList().get(final_arg2).getToCommentUserId() + "");
                                        // 服务器没有传头像地址给我
                                    }
                                    intent.putExtra("publisherName",
                                            onelist.get(v_a).getCommentList().get(final_arg2).getToCommentUserName() + "");
                                    intent.putExtra("classId", onelist.get(v_a).getClassId() + "");
                                    startActivity(intent);
                                    NewPersonalDetailsActivity.this.finish();
                                }
                            }
                        });
                    }
                });

        iv_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.title_back:
                NewPersonalDetailsActivity.this.finish();
                break;

            case R.id.iv_share:
                //初始化分享参数

                String shareContent;
                String userName = null;
                if ("2".equals(usertype)) {
                    userName = CCApplication.getInstance().getMemberInfo().getUserName();
                } else if ("3".equals(usertype)) {
                    userName = CCApplication.getInstance().getPresentUser().getChildName()
                            + CCApplication.getInstance().getPresentUser().getRelationTypeName();

                }

                if (DataUtil.isNullorEmpty(dynamicsContent)) {
                    shareContent = userName + "给你分享了一条班级动态";
                } else {
                    shareContent = dynamicsContent;
                }

                initSharePara(dynamicsId, shareContent);
                break;
        }
    }


    @Override
    public void onRequstComplete(String result) {
        // TODO Auto-generated method stub
        parseRelationCode(result);
    }

    @Override
    public void onRequstCancelled() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void getMessage(String data) {
        // TODO Auto-generated method stub
        switch (action) {

            case ACTION_DELETE_MESSAGE_ITEM:
                parseDeleteMessageItem(data);
                break;

            default:
                break;
        }
    }

    private void parseDeleteMessageItem(String data) {
        try {
            JSONObject jsonRoot = new JSONObject(data);
            JSONObject serverResult = jsonRoot.getJSONObject("serverResult");
            int code = serverResult.getInt("resultCode");
            if (Constants.SUCCESS_CODE == code) {
                DataUtil.getToastShort("删除成功");
                setResult(RESULT_OK);
                finish();
            } else {
                DataUtil.getToastShort("删除失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 点赞图标事件
    private void handleClickPraise(int position) {
        // 设置不可点击
        List<NewPointBean> lo = onelist.get(position).getLoveList();
        if (lo.size() == 0) {
            new_message_support.setImageResource(R.drawable.new_love_ok);

            message_support_count_icon.setVisibility(View.VISIBLE);
            message_ll_support_count.setVisibility(View.VISIBLE);

            NewPointBean npb = new NewPointBean();
            if ("2".equals(usertype)) {

                String userName = CCApplication.getInstance().getMemberInfo()
                        .getUserName();
                npb.setCommentUserName(userName);
            } else if ("3".equals(usertype)) {
                String userName = CCApplication.getInstance().getPresentUser().getChildName()
                        + CCApplication.getInstance().getPresentUser().getRelationTypeName();
                npb.setCommentUserName(userName);
            }

            npb.setCommentUserId(Integer.parseInt(user_id_app));

            // 点赞人名字的显示
            // lnList = new LoveNameAdapter(activity, all);
            // holder.new_goodlove_gv.setAdapter(lnList);

            final List<NewPointBean> lll = onelist.get(position).getLoveList();
            lll.add(npb);

            message_support_count_name.setText("");
            for (int i = 0; i < lll.size(); i++) {

                String commentUserNameStr = lll.get(i)
                        .getCommentUserName();

                SpannableString spannableString = new SpannableString(
                        commentUserNameStr + ",");

                final int i2 = i;
                spannableString.setSpan(new ClickableSpan() {

                                            @Override
                                            public void updateDrawState(TextPaint ds) {
                                                super.updateDrawState(ds);
                                                // ds.setColor(Color.WHITE); //设置文件颜色
                                                ds.setUnderlineText(false); // 设置取消下划线
                                                ds.setColor(Color.parseColor("#01afef"));
                                            }

                                            @Override
                                            public void onClick(View widget) {

                                                if (!DataUtil.isNetworkAvailable(NewPersonalDetailsActivity.this)) {
                                                    DataUtil.getToast("无网络");
                                                    return;
                                                }

                                                String commentUserName = lll.get(i2).getCommentUserName() + "";
                                                if (isFamily(commentUserName)) {
                                                    DataUtil.getToast("家长暂未开放动态！");
                                                } else {

                                                    Intent intent = new Intent(NewPersonalDetailsActivity.this,
                                                            NewPersonalDynamicsActivity.class);
                                                    intent.putExtra("publishUserId", lll.get(i2)
                                                            .getCommentUserId() + "");

                                                    String publisherImgUrl = getPointOrCommentUrl(lll.get(i2)
                                                            .getCommentUserId() + "", onelist);
                                                    if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                                        intent.putExtra("publisherImgUrl", publisherImgUrl);
                                                    } else {
                                                        intent.putExtra("publisherImgUrl", lll.get(i2)
                                                                .getCommentUserId());// 服务器没有传头像地址给我
                                                    }

                                                    intent.putExtra("publisherName", lll.get(i2)
                                                            .getCommentUserName());
                                                    intent.putExtra("classId", onelist.get(v).getClassId() + "");

                                                    startActivity(intent);
                                                    NewPersonalDetailsActivity.this.finish();
                                                }

                                            }
                                        }, 0, commentUserNameStr.length() + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                // textview.setHighlightColor(Color.TRANSPARENT);//取消高亮

                message_support_count_name.append(spannableString);
                message_support_count_name.setVisibility(View.VISIBLE);
                message_support_count_name
                        .setMovementMethod(LinkMovementMethod.getInstance());
            }

            subLikeTextView();
            praiseWithServer(position, 0);
        } else {

            boolean like = false;// 判断是否点赞过
            for (int i = 0; i < lo.size(); i++) {
                if (lo.get(i).getCommentUserId() == Integer
                        .parseInt(user_id_app)) {
                    like = true;
                }
            }

            if (like) {
                // id相等 表示已经点赞
                // 如果有点赞 点赞后变灰色心心
                System.out.println("=====只会为灰=====");

                if (lo.size() > 1) {
                    message_support_count_icon.setVisibility(View.VISIBLE);
                    message_ll_support_count.setVisibility(View.VISIBLE);
                } else {

                    message_support_count_icon.setVisibility(View.GONE);
                    message_ll_support_count.setVisibility(View.GONE);
                }

                new_message_support.setImageResource(R.drawable.new_love_no);
                final List<NewPointBean> lll = onelist.get(position).getLoveList();
                for (int j = 0; j < lll.size(); j++) {
                    if (lll.get(j).getCommentUserId() == Integer
                            .parseInt(user_id_app)) {
                        lll.remove(j);
                    }
                }


                message_support_count_name.setText("");
                for (int i = 0; i < lll.size(); i++) {

                    String commentUserNameStr = lll.get(i)
                            .getCommentUserName();

                    SpannableString spannableString = new SpannableString(
                            commentUserNameStr + ",");

                    final int i2 = i;
                    spannableString.setSpan(new ClickableSpan() {

                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    // ds.setColor(Color.WHITE); //设置文件颜色
                                                    ds.setUnderlineText(false); // 设置取消下划线
                                                    ds.setColor(Color.parseColor("#01afef"));
                                                }

                                                @Override
                                                public void onClick(View widget) {

                                                    if (!DataUtil.isNetworkAvailable(NewPersonalDetailsActivity.this)) {
                                                        DataUtil.getToast("无网络");
                                                        return;
                                                    }

                                                    String commentUserName = lll.get(i2).getCommentUserName() + "";
                                                    if (isFamily(commentUserName)) {
                                                        DataUtil.getToast("家长暂未开放动态！");
                                                    } else {

                                                        Intent intent = new Intent(NewPersonalDetailsActivity.this,
                                                                NewPersonalDynamicsActivity.class);
                                                        intent.putExtra("publishUserId", lll.get(i2)
                                                                .getCommentUserId() + "");

                                                        String publisherImgUrl = getPointOrCommentUrl(lll.get(i2)
                                                                .getCommentUserId() + "", onelist);
                                                        if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                                            intent.putExtra("publisherImgUrl", publisherImgUrl);
                                                        } else {
                                                            intent.putExtra("publisherImgUrl", lll.get(i2)
                                                                    .getCommentUserId());// 服务器没有传头像地址给我
                                                        }

                                                        intent.putExtra("publisherName", lll.get(i2)
                                                                .getCommentUserName());
                                                        intent.putExtra("classId", onelist.get(v).getClassId() + "");

                                                        startActivity(intent);
                                                        NewPersonalDetailsActivity.this.finish();
                                                    }


                                                }
                                            }, 0, commentUserNameStr.length() + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // textview.setHighlightColor(Color.TRANSPARENT);//取消高亮

                    message_support_count_name.append(spannableString);
                    message_support_count_name.setVisibility(View.VISIBLE);
                    message_support_count_name
                            .setMovementMethod(LinkMovementMethod.getInstance());
                }

                subLikeTextView();
                praiseWithServer(position, 2);
            } else {
                // 不相等 表示没有点赞
                // 如果没有点赞 点赞后变蓝色心心
                // System.out.println("=====只会为蓝=====");

                new_message_support.setImageResource(R.drawable.new_love_ok);

                NewPointBean npb = new NewPointBean();
                if ("2".equals(usertype)) {

                    String userName = CCApplication.getInstance().getMemberInfo()
                            .getUserName();
                    npb.setCommentUserName(userName);
                } else if ("3".equals(usertype)) {
                    String userName = CCApplication.getInstance().getPresentUser()
                            .getChildName()
                            + CCApplication.getInstance().getPresentUser().getRelationTypeName();
                    npb.setCommentUserName(userName);
                }


                npb.setCommentUserId(Integer.parseInt(user_id_app));
                final List<NewPointBean> lll = onelist.get(position).getLoveList();
                lll.add(npb);


                message_support_count_name.setText("");
                for (int i = 0; i < lll.size(); i++) {

                    String commentUserNameStr = lll.get(i)
                            .getCommentUserName();

                    SpannableString spannableString = new SpannableString(
                            commentUserNameStr + ",");

                    final int i2 = i;
                    spannableString.setSpan(new ClickableSpan() {

                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    // ds.setColor(Color.WHITE); //设置文件颜色
                                                    ds.setUnderlineText(false); // 设置取消下划线
                                                    ds.setColor(Color.parseColor("#01afef"));
                                                }

                                                @Override
                                                public void onClick(View widget) {

                                                    if (!DataUtil.isNetworkAvailable(NewPersonalDetailsActivity.this)) {
                                                        DataUtil.getToast("无网络");
                                                        return;
                                                    }

                                                    String commentUserName = lll.get(i2).getCommentUserName() + "";
                                                    if (isFamily(commentUserName)) {
                                                        DataUtil.getToast("家长暂未开放动态！");
                                                    } else {

                                                        Intent intent = new Intent(NewPersonalDetailsActivity.this,
                                                                NewPersonalDynamicsActivity.class);
                                                        intent.putExtra("publishUserId", lll.get(i2)
                                                                .getCommentUserId() + "");

                                                        String publisherImgUrl = getPointOrCommentUrl(lll.get(i2)
                                                                .getCommentUserId() + "", onelist);
                                                        if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                                            intent.putExtra("publisherImgUrl", publisherImgUrl);
                                                        } else {
                                                            intent.putExtra("publisherImgUrl", lll.get(i2)
                                                                    .getCommentUserId());// 服务器没有传头像地址给我
                                                        }

                                                        intent.putExtra("publisherName", lll.get(i2)
                                                                .getCommentUserName());
                                                        intent.putExtra("classId", onelist.get(v).getClassId() + "");

                                                        startActivity(intent);
                                                        NewPersonalDetailsActivity.this.finish();
                                                    }


                                                }
                                            }, 0, commentUserNameStr.length() + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // textview.setHighlightColor(Color.TRANSPARENT);//取消高亮

                    message_support_count_name.append(spannableString);
                    message_support_count_name.setVisibility(View.VISIBLE);
                    message_support_count_name
                            .setMovementMethod(LinkMovementMethod.getInstance());
                }

                subLikeTextView();
                praiseWithServer(position, 0);
            }

        }
        // notifyDataSetChanged();
        if (!DataUtil.isNetworkAvailable(this)) {
            DataUtil.getToast(this.getResources()
                    .getString(R.string.no_network));
            return;
        }
        // //点击完后发送给web 其他数据从web拿去然后改变组件显示
        // praiseWithServer(position,flag);
    }

    // 点赞往服务器传数据
    private void praiseWithServer(final int position, final int flag) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("userId", user_id_app);
                    String userName = "";
                    if ("2".equals(usertype)) {

                        userName = CCApplication.getInstance()
                                .getMemberInfo().getUserName();
                        json.put("userName", userName);
                    } else if ("3".equals(usertype)) {
                        userName = CCApplication.getInstance()
                                .getPresentUser().getChildName()
                                + CCApplication.getInstance().getPresentUser().getRelationTypeName();
                        json.put("userName", userName);
                    }

                    json.put("interactionId", onelist.get(position).getId());
                    json.put("flag", flag);
                    json.put("classId", Integer.parseInt(class_id_a));
                    json.put("hidePublish", true);
                    json.put("userType", usertype);

                    String url = new StringBuilder(Constants.SERVER_URL)
                            .append(Constants.PUBLISH_DYNAMIC_COMMENT).toString();
                    String result = HttpHelper.httpPostJson(
                            NewPersonalDetailsActivity.this, url, json);
                    JSONObject json1 = new JSONObject(result);
                    String isDelete = json1.getString("isDelete");
                    Intent broadcast = new Intent(
                            NewClassDynamicsActivity.INTENT_REFESH_DATA);
                    broadcast.putExtra("classId", Integer.parseInt(class_id_a));
                    broadcast.putExtra("interactionId", onelist.get(position).getId());
                    broadcast.putExtra("flag", flag);
                    broadcast.putExtra("position", position);
                    broadcast.putExtra("userid", user_id_app);
                    broadcast.putExtra("username", userName);
                    broadcast.putExtra("type", "good");
                    sendBroadcast(broadcast);
                    if ("1".equals(isDelete)) {
                        DataUtil.getToast("该内容已被删除");
                        NewPersonalDetailsActivity.this.finish();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 添加评论
     */
    private void handleAddComments(final int i, final boolean isAt,
                                   final int arg2) {
        String getParentName_a = CCApplication.getInstance().getPresentUser()
                .getParentName();
        String getUserName = CCApplication.getInstance().getMemberInfo()
                .getUserName();
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);//方法一
//		LayoutInflater inflater = context.getLayoutInflater();//方法二
        View view = inflater.inflate(R.layout.popwindow_ed, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 防止虚拟软键盘被弹出菜单遮住
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(findViewById(R.id.message_comment_icon),
                Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        Button first = (Button) view.findViewById(R.id.pop_btn);
        final EditText ed = (EditText) view.findViewById(R.id.ed);
        if (isAt) {
            ed.setHint("回复评论...");
        } else {
            ed.setHint("评论...");
        }


        first.setOnClickListener(new OnClickListener() {

            /*
             * (non-Javadoc)
             *
             * @see android.view.View.OnClickListener#onClick(android.view.View)
             */
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed.getText())) {
                    return;
                }
                if (!DataUtil
                        .isNetworkAvailable(NewPersonalDetailsActivity.this)) {
                    DataUtil.getToast(getResources().getString(
                            R.string.no_network));
                    return;
                }

                String commontId = "";
                // new app分两个接口首先是发布
                try {
                    // String
                    // getParentName_a=CCApplication.getInstance().getPresentUser().getParentName();
                    String userid = CCApplication.getInstance()
                            .getPresentUser().getUserId();


                    JSONObject json = new JSONObject();
                    json.put("userId", userid);

                    if ("2".equals(usertype)) {

                        String userName = CCApplication.getInstance()
                                .getMemberInfo().getUserName();
                        json.put("userName", userName);
                    } else if ("3".equals(usertype)) {
                        String userName = CCApplication.getInstance()
                                .getPresentUser().getChildName()
                                + CCApplication.getInstance().getPresentUser().getRelationTypeName();
                        json.put("userName", userName);
                    }


                    json.put("interactionId", onelist.get(i).getId());
                    json.put("flag", 1);
                    json.put("classId", class_id_a);
                    json.put("textfield", ed.getText());
                    if (isAt) {
                        json.put("pointCommentId", onelist.get(i)
                                .getCommentList().get(arg2).getId());

                    } else {
                        json.put("pointCommentId", "");

                    }
                    json.put("hidePublish", true);
                    json.put("userType", usertype);
                    String url = new StringBuilder(Constants.SERVER_URL)
                            .append(Constants.PUBLISH_DYNAMIC_COMMENT).toString();
                    String result = HttpHelper.httpPostJson(
                            NewPersonalDetailsActivity.this, url, json);
                    Log.e("Comment", result);
                    JSONObject json1 = new JSONObject(result);
                    commontId = json1.getString("commontId");
                    String isDelete = json1.getString("isDelete");
                    if ("1".equals(isDelete)) {
                        DataUtil.getToast("该内容已被删除");
                    }
                    window.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                initData();// 显示刚才的评论 或回复

                // 通知activity刷新数据

                Intent broadcast = new Intent(
                        NewClassDynamicsActivity.INTENT_REFESH_DATA);
                broadcast.putExtra("classId", Integer.parseInt(class_id_a));
                broadcast.putExtra("interactionId", onelist.get(i).getId());
                broadcast.putExtra("flag", 1);
                broadcast.putExtra("textfield", ed.getText().toString());
                broadcast.putExtra("type", "comment");
                broadcast.putExtra("username", userName);
                broadcast.putExtra("pointCommentId", Integer.parseInt(commontId));
                sendBroadcast(broadcast);
            }
        });

        // popWindow消失监听方法
        window.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });

    }

    /**
     * 显示popupWindow 删除
     */
    private void handleDelete(final int postion, final int arg2, final int q) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("是否删除本条评论?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doDelete(q, postion, arg2);
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create();
        builder.show();

        // 利用layoutInflater获得View
//        showPopDelete(postion, arg2, q);
    }

    private void showPopDelete(final int postion, final int arg2, final int q) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.delete_point, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 防止虚拟软键盘被弹出菜单遮住
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(findViewById(R.id.message_comment_icon),
                Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        Button cancel_delete = (Button) view.findViewById(R.id.cancel_delete);
        Button determine_delete = (Button) view
                .findViewById(R.id.determine_delete);
        // 取消
        cancel_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        // 确定
        determine_delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (doDelete(q, postion, arg2)) return;

                window.dismiss();
            }
        });

        // popWindow消失监听方法
        window.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });
    }

    private boolean doDelete(int q, int i, int arg2) {
        if (!DataUtil
                .isNetworkAvailable(NewPersonalDetailsActivity.this)) {
            DataUtil.getToast(getResources().getString(
                    R.string.no_network));
            return true;
        }
        // q == 1表示是删除整条班级动态的操作
        if (q == 1) {
            // notifyDataSetChanged();//查一下 这句话干嘛的
            String result = null;
            JSONObject json = new JSONObject();
            try {
                json.put("classDynamicId", onelist.get(i).getId());
                json.put("hidePublish", true);
                String url = new StringBuilder(Constants.SERVER_URL)
                        .append(Constants.DELETE_CLASS_DYNAMIC).toString();

                result = HttpHelper.httpPostJson(
                        NewPersonalDetailsActivity.this, url, json);

                // activity.queryPost(url,json);
                // resultCode 200 删除成功
                onelist.remove(i);
                Intent broadcast = new Intent(
                        NewClassDynamicsActivity.INTENT_REFESH_DATA);
                sendBroadcast(broadcast);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {// 删除某条评论
            String result = null;
            JSONObject json = new JSONObject();
            try {
                json.put("classDynamicCommentId", onelist.get(i)
                        .getCommentList().get(arg2).getId());
                json.put("hidePublish", true);
                String url = new StringBuilder(Constants.SERVER_URL)
                        .append(Constants.DELETE_CLASS_DYNAMIC_COMMENT).toString();
                result = HttpHelper.httpPostJson(
                        NewPersonalDetailsActivity.this, url, json);

                // resultCode 200 删除成功
                JSONObject json1 = new JSONObject(result);
                //  String resultCode=json1.getString("resultCode");
                Intent broadcast = new Intent(
                        NewClassDynamicsActivity.INTENT_REFESH_DATA);
                broadcast.putExtra("classId", Integer.parseInt(class_id_a));
                broadcast.putExtra("flag", 3);
                broadcast.putExtra("type", "comment");
                broadcast.putExtra("interactionId", onelist.get(i).getId());
                broadcast.putExtra("pointCommentId", onelist.get(i)
                        .getCommentList().get(arg2).getId());
                sendBroadcast(broadcast);
                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        initData();// 显示刚才的评论 或回复
        return false;
    }


    //动态列表中是否有 点赞人 评论人 被评论人 头像
    private String getPointOrCommentUrl(String pointOrCommentId, List<NewClassDynamicsBean1> list) {
        if (list != null && !DataUtil.isNullorEmpty(pointOrCommentId)) {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String pubishUserId = list.get(i).getPublishUserId() + "";
                    if (!DataUtil.isNullorEmpty(pubishUserId)) {
                        if (pointOrCommentId.endsWith(pubishUserId)) {
                            return list.get(i).getPublisherImgUrl();
                        }
                    }
                }
            }
        }
        return "";
    }

    private void getRelationCode() {
        try {
            JSONObject json = new JSONObject();
            json.put("parentCode", "GXM");
//            System.out.println("获取所有关系入参json=====" + json);


//            String url = new StringBuilder(Constants.SERVER_URL).append(Constants.GET_RELATION_CODE).toString();
//            String result = HttpHelper.httpPostJson(this, url, json);

            //网络是否可用
            if (!DataUtil.isNetworkAvailable(this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }
            //是否在上一个请求中
            if (!isLoading()) {
                System.out.println("获取所有关系入参json=====" + json);

                new HttpRequestAsyncTask(json, this, this, true)
                        .execute(Constants.GET_RELATION_CODE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseRelationCode(String result) {
        NewBaseBean ret2 = JsonUtils.fromJson(result, NewBaseBean.class);
        if (ret2.getServerResult().getResultCode() != 200) {// 请求失败
            DataUtil.getToast(ret2.getServerResult().getResultMessage());
        } else {
            JSONObject jsonB = null;
            try {
                jsonB = new JSONObject(result);
                childRelationTypeList = new ArrayList<>();
                String relationShipList = jsonB.getString("relationShipList");
                JSONArray json2 = new JSONArray(relationShipList);
//                        JSONArray json2 = json.getJSONArray("relationShipList");
                for (int i = 0; i < json2.length(); i++) {
                    ChildRelationTypeBean childRelationTypeBean = JsonUtils.fromJson(json2
                            .get(i).toString(), ChildRelationTypeBean.class);
                    childRelationTypeList.add(childRelationTypeBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private Boolean isFamily(String name) {
        if (childRelationTypeList != null && !DataUtil.isNullorEmpty(name)) {
            if (childRelationTypeList.size() > 0) {
                for (int i = 0; i < childRelationTypeList.size(); i++) {
                    String childRelationType = childRelationTypeList.get(i).getConfigName();
//                    System.out.println("=====childRelationType=="+childRelationType);
                    if (name.contains(childRelationType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 初始化分享参数
     *
     * @param id      班级动态id
     * @param content 班级动态文字内容
     */
    private void initSharePara(final int id, final String content) {
        //下载图片 存储在本地.
        // 因为微信分享的图片必须要可以下载的或者是存储在本地的图片
        String url = new StringBuilder(Constants.URL)
                .append("/downloadApi?fileId=")
                .append(Constants.SHARE_CLASS_DYNAMICS_PICTURE_ID)
                .append("&sf=80*150").toString();

        ((BaseActivity) context).getImageLoader().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String url, View view) {
            }

            @Override
            public void onLoadingFailed(String url, View view, FailReason failReason) {
                share(id, content, url);
            }

            @Override
            public void onLoadingComplete(String url, View view, Bitmap bitmap) {
//                String folder = Environment.getExternalStorageDirectory() + "/ZIGUI_Photos/";
                String folder = PhotoBitmapUtils.ZIGUI_Photos;
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String time = formatter.format(new Date());
                String fileName = folder + time + ".jpeg";

                boolean flag = DataUtil.saveBitmap(bitmap, fileName);
                //获取到bitmap对象  存储到sd上.
                if (flag) {
                    share(id, content, fileName);
                } else {
                    share(id, content, url);
                }
            }

            @Override
            public void onLoadingCancelled(String url, View view) {
            }
        });
    }

    /**
     * 分享
     *
     * @param id         分享id
     * @param content    分享文字内容
     * @param imageLocal 分享图片链接
     */
    private void share(int id, String content, String imageLocal) {
        ShareModel model = new ShareModel();

        //微信图片分享地址
        model.setImageLocal(imageLocal);

        String QQUrl = new StringBuilder(Constants.URL)
                .append("/downloadApi?fileId=")
                .append(Constants.SHARE_CLASS_DYNAMICS_PICTURE_ID)
                .append("&sf=80*150").toString();

        //QQ图片分享地址
        model.setImageUrl(QQUrl);


        model.setText(content);
        model.setTitle("【子贵校园】班级动态");
        String imgAuthId = Constants.AUTHID + "@" + NewPersonalDetailsActivity.this.getDeviceID()
                + "@" + CCApplication.app.getMemberInfo().getAccId();

        String url = new StringBuilder(Constants.URL)
                .append("/classdynamic/classdynamicshare.do?dynamicId=")
                .append(id)
                .append(imgAuthId).toString();
        model.setUrl(url);

        SharePopWindow share = new SharePopWindow(NewPersonalDetailsActivity.this, model);
        share.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    ///视频播放

    private void downLoad(String url, final String fileid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DLS_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(CCApplication.getInstance().initClient())
                .build();
        ApiManager apiManager = retrofit.create(ApiManager.class);
        Call<ResponseBody> call = apiManager.downloadFileWithDynamicUrlSync(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("111", "server contacted and has file");
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), fileid);
                    dismissPd();
                    if (writtenToDisk) {
                        gotoVideoView();
                    } else {
                        DataUtil.getToast("没有获取存储权限!");
                    }
                    //进入视频打开界面
                } else {
                    DataUtil.getToast(NewPersonalDetailsActivity.this.getResources().getString(R.string.no_network));// 当前网络不可用，请检查您的网络设置
                    dismissPd();
                    Log.d("111", "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DataUtil.getToastShort(t.getMessage());
                Log.e("111", "error");
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileid) {
        try {
            //视频缓存路径
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Constants.CACHE_PATH + "video-cache" + "/" + fileid);
            if (futureStudioIconFile.exists()) {
                futureStudioIconFile.delete();
                futureStudioIconFile = new File(Constants.CACHE_PATH + "video-cache" + "/" + fileid);
            }
            currentFile = futureStudioIconFile.getPath();
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("111", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void gotoVideoView() {
        //点击事件
        PictureURL pictureURL = null;
        List<PictureURL> datas = new ArrayList<PictureURL>();
        pictureURL = new PictureURL();

        pictureURL.setPictureURL(currentFile);
        datas.add(pictureURL);
        Intent intent = new Intent(CCApplication.applicationContext,
                VideoViewActivity.class);
        intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_URLS,
                (Serializable) datas);
        intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_INDEX,
                0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NewPersonalDetailsActivity.this.startActivity(intent);

    }

    private String currentFile = "";
}
