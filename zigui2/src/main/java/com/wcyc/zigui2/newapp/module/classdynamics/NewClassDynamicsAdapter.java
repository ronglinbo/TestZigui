package com.wcyc.zigui2.newapp.module.classdynamics;

/*
 * 文 件 名:ClassDynamicsAdapter.java
 * 创 建 人： yytan
 * 日    期： 2015-12-09
 * 版 本 号： 
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.newapp.adapter.CommentsListViewAdapter;
import com.wcyc.zigui2.bean.NewClassDynamicsBean1;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.adapter.NewGridViewAdapter;
import com.wcyc.zigui2.newapp.bean.ChildRelationTypeBean;
import com.wcyc.zigui2.newapp.bean.GradeleaderBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewPointBean;
import com.wcyc.zigui2.newapp.bean.ShareModel;
import com.wcyc.zigui2.newapp.module.educationinfor.EducationDetailsActivity;
import com.wcyc.zigui2.newapp.module.leavemessage.ChildMessage;
import com.wcyc.zigui2.newapp.videoutils.VideoViewActivity;
import com.wcyc.zigui2.newapp.widget.EditTextLengthFilter;
import com.wcyc.zigui2.newapp.widget.SharePopWindow;
import com.wcyc.zigui2.utils.ApiManager;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.PhotoBitmapUtils;
import com.wcyc.zigui2.utils.RepeatOnClick;
import com.wcyc.zigui2.widget.RoundImageView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yytan
 */
public class NewClassDynamicsAdapter extends BaseAdapter {

    private List<NewClassDynamicsBean1> list;
    private LayoutInflater inflater;
    private BaseActivity activity;
    private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();// 图片list
    private List<Map<String, Object>> data_list_Point = new ArrayList<Map<String, Object>>();// popwin
    private Map<String, Object> mBitmapMap = new HashMap<String, Object>();
    private String userid;
    private String userName;
    private String resource_id;
    private String isMain;
    // 评论只是穿数据去WEB
    DisplayImageOptions options; // 显示图片的设置
    ImageLoader imageLoader;
    private String usertype;
    private long longTime1 = 0;
    private long longTime2 = 0;
    private static final String OPEN = "全部";
    private static final String CLOSE = "收起";
    private List<ChildRelationTypeBean> childRelationTypeList;
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    DisplayImageOptions build;

    private boolean hidePublish; //是否是全部班级...true 是全部班级  false是单个班级

    private void DownloadAllImages() {
        final int num = list.size();
        // String[] tempurls;
        List<String> tempurls;
        for (int i = 0; i < num; i++) {
            List<NewClassDynamicsBean1.ClassAttachmentBean> clssAttachmentList = new ArrayList<NewClassDynamicsBean1.ClassAttachmentBean>();
            tempurls = new ArrayList<String>();
            clssAttachmentList = list.get(i).getAttachmentInfoList_new();
            for (int k = 0; k < clssAttachmentList.size(); k++) {
                String attachementUrl = list.get(i).getAttachmentInfoList_new().get(k).getAttachementUrl();
                tempurls.add(attachementUrl);
            }
            if (tempurls != null) {
                int length = tempurls.size();
                for (int j = 0; j < length; j++) {
                    String attchmentType = list.get(i).getAttchmentType();//1是图片  2是视频音频 3是文件文档 5-6也是视频
                    if (!"2".equals(attchmentType) && !"6".equals(attchmentType) && !"5".equals(attchmentType)) {
                        imageLoader.loadImage(Constants.URL + tempurls.get(j)
                                + "&sf=150*150", options, new ImageLoadingListener() {//&sf=80*80之前的，感觉太不清晰
                            @Override
                            public void onLoadingCancelled(String arg0, View arg1) {

                            }

                            @Override
                            public void onLoadingComplete(String arg0, View arg1,
                                                          Bitmap arg2) {
                                mBitmapMap.put(arg0, arg2);
                            }

                            @Override
                            public void onLoadingFailed(String arg0, View arg1,
                                                        FailReason arg2) {

                            }

                            @Override
                            public void onLoadingStarted(String arg0, View arg1) {

                            }
                        });
                    }
                }
            }
        }
    }

    private View parentView;

    public NewClassDynamicsAdapter(BaseActivity activity,
                                   List<NewClassDynamicsBean1> list, String userid,
                                   String resource_id, String userName, View parentView, boolean hidePublish) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.list = list;
        this.userid = userid;
        this.userid = userid = CCApplication.getInstance().getPresentUser()
                .getUserId();
        this.userName = userName;
        this.resource_id = resource_id;
        this.parentView = parentView;
        this.hidePublish = hidePublish;
        this.mBitmapMap = mBitmapMap;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image).cacheInMemory(true)
                .imageScaleType(ImageScaleType.NONE).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .build();
        DownloadAllImages();
        getRelationCode();
    }


    public NewClassDynamicsAdapter(BaseActivity activity,
                                   List<NewClassDynamicsBean1> list, String userid,
                                   String resource_id, String userName, String isMain) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.list = list;
        this.userid = userid;
        this.userName = userName;
        this.resource_id = resource_id;
        this.mBitmapMap = mBitmapMap;
        this.isMain = isMain;
        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image).cacheInMemory(true)
                .imageScaleType(ImageScaleType.NONE).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .build();
        DownloadAllImages();
        getRelationCode();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.new_classinteraction_item,
                    parent, false);
            holder = new ViewHolder();
            holder.author_name = (TextView) convertView
                    .findViewById(R.id.author_name);// 发布人

            holder.mew_bjmc = (TextView) convertView
                    .findViewById(R.id.mew_bjmc);// 发布人所在班级
            holder.mew_bjmc_couse_teacher = (TextView) convertView
                    .findViewById(R.id.mew_bjmc_couse_teacher);// 发布人 如果是老师 显示科目
            // 即xx老师

            holder.gridView = (GridView) convertView
                    .findViewById(R.id.gridView);// gridView 图片
            holder.class_vedio_iv = (ImageView) convertView
                    .findViewById(R.id.class_vedio_iv);// 视频
            holder.class_vedio_ll = (RelativeLayout) convertView
                    .findViewById(R.id.class_vedio_ll);// 视频布局

            holder.author_delete = (TextView) convertView
                    .findViewById(R.id.author_delete);// 删除

            holder.message_content = (TextView) convertView
                    .findViewById(R.id.message_content);// 内容
            holder.message_content_long = (TextView) convertView
                    .findViewById(R.id.message_content_long);// 内容长
            holder.id_openOrClose = (TextView) convertView
                    .findViewById(R.id.id_openOrClose);//  全部 收起

            holder.message_time = (TextView) convertView
                    .findViewById(R.id.message_time);// 时间
            holder.message_ll_support_count = (RelativeLayout) convertView
                    .findViewById(R.id.message_ll_support_count);// 点赞图标和点赞人   布局
            holder.message_support_count_name = (TextView) convertView
                    .findViewById(R.id.message_support_count_name);// 点赞人的名字
            holder.goodNum = (TextView) convertView.findViewById(R.id.goodNum);// 点赞条数
            holder.message_support_count_icon = (ImageView) convertView
                    .findViewById(R.id.message_support_count_icon);// 点赞人名前面的图标
            holder.message_support = (ImageView) convertView
                    .findViewById(R.id.message_support);// 点赞图标
            holder.pointNum = (TextView) convertView
                    .findViewById(R.id.pointNum);// 评论条数
            holder.message_support_text = (TextView) convertView
                    .findViewById(R.id.message_support_text);// "赞"
            holder.message_comment_text = (TextView) convertView
                    .findViewById(R.id.message_comment_text);// "评论"
            holder.message_comment_lv = (ListView) convertView
                    .findViewById(R.id.message_comment_lv);// 评论listView
            holder.message_comment_icon = (ImageView) convertView
                    .findViewById(R.id.message_comment_icon);// 评论点击图标
            holder.class_dynamics_civ = (RoundImageView) convertView
                    .findViewById(R.id.class_dynamics_civ);// 这条班级动态人的头像

            holder.iv_share = (ImageView) convertView.findViewById(R.id.iv_share);

            // holder.new_goodlove_gv = (GridView) convertView
            // .findViewById(R.id.new_goodlove_gv);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

            holder.message_support.setImageResource(R.drawable.new_love_no);
        }

        initDataToView(holder, position);// 新

        return convertView;
    }

    private void initDataToView(final ViewHolder holder, final int position) {

        usertype = CCApplication.getInstance().getPresentUser().getUserType();
        String class_id_a = list.get(position).getClassId() + "";// 请求列表中class_id

        List<NewClasses> classlist = new ArrayList<NewClasses>();
        List<NewClasses> allClassList = new ArrayList<NewClasses>();
        String PhoneNum = CCApplication.getInstance().getPhoneNum();

        boolean allowAllClassTag = false;
        boolean gradeleader = false;
        if ("2".equals(usertype)) {
            classlist = CCApplication.app.getMemberDetail()// 保存列表中class_id
                    .getClassList();

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
                if (allClassList == null) {
                    allClassList = new ArrayList<NewClasses>();
                }
                List<NewClasses> schoolAllClassList = CCApplication.getInstance().getSchoolAllClassList();
                if (schoolAllClassList != null && !gradeleader) {
                    allClassList.addAll(schoolAllClassList);
                } else if (schoolAllClassList != null && gradeleader) {//如果是年级组长
                    try {
                        if (allClassList == null) {
                            allClassList = new ArrayList<NewClasses>();
                        }
                        List<GradeleaderBean> gradeInfoList = CCApplication.getInstance().getMemberDetail().getGradeInfoList();
                        for (int i = 0; i < gradeInfoList.size(); i++) {
                            String userGradeId = gradeInfoList.get(i).getGradeId();
                            for (int j = 0; j < schoolAllClassList.size(); j++) {
                                String gradeId = schoolAllClassList.get(j).getGradeId();
                                if (userGradeId.equals(gradeId)) {
                                    allClassList.add(schoolAllClassList.get(j));
                                }
                            }

                        }
                    } catch (Exception e) {
                    }

                }
            }
        } else if ("3".equals(usertype)) {
            List<NewChild> childlist = null;
            try {
                childlist = CCApplication.app.getMemberDetail()
                        .getChildList();
            } catch (Exception e) {
                DataUtil.getToastShort("数据未加载完成，稍后再试");
                return;
            }

            if (childlist != null) {

                for (int i = 0; i < childlist.size(); i++) {
                    NewClasses newclass = new NewClasses();
//					newclass.setClassId(childlist.get(i).getChildClassID());
                    newclass.setClassID(childlist.get(i).getChildClassID());
                    newclass.setClassName(childlist.get(i).getChildClassName());
                    newclass.setGradeId(childlist.get(i).getGradeId());
                    newclass.setGradeName(childlist.get(i).getGradeName());

                    classlist.add(newclass);
                }
            }
        }

        holder.mew_bjmc.setText("");// 发布人所在班级
        String class_name_b = "";

        //如果是全部班级
        if (hidePublish) {
            String allClassName = list.get(position).getClassName();
            if (!DataUtil.isNullorEmpty(allClassName)) {
                holder.mew_bjmc.setText(allClassName);
            } else {
                holder.mew_bjmc.setText("");
            }

            //如果是选择单个班级
        } else {
            if (classlist != null) {
                for (int i = 0; i < classlist.size(); i++) {
                    String class_id_b = classlist.get(i).getClassID();
                    if (class_id_a.equals(class_id_b)) {
                        class_name_b = classlist.get(i).getClassName();
                        holder.mew_bjmc.setText(class_name_b);
                    }
                }
            }

            //如果是管理员或校级领导
            if (allowAllClassTag) {
                if (allClassList != null) {
                    for (int i = 0; i < allClassList.size(); i++) {
                        String class_id_b = allClassList.get(i).getClassID();
                        if (class_id_a.equals(class_id_b)) {
                            class_name_b = allClassList.get(i).getClassName();
                            holder.mew_bjmc.setText(class_name_b);
                        }
                    }
                }
            }
        }


        String publishUserType = list.get(position).getPublishUserType();
        if ("2".equals(publishUserType)) {

            holder.author_name.setText(list.get(position).getPublisherName());// 发布人
        } else if ("3".equals(publishUserType)) {

            if (list.get(position).getRelation() != null) {
                holder.author_name.setText(list.get(position).getPublishChildName() + list.get(position).getRelation());
            } else {
                holder.author_name.setText(list.get(position).getPublishChildName() + "家长");
            }
        }

        // 这条班级动态人的头像以及监听事件
        if (list.get(position).getPublisherImgUrl() != null) {
            //方法一
//			String url = DataUtil.getDownloadURL(activity, list.get(position)
//					.getPublisherImgUrl());
//			((BaseActivity) activity).getImageLoader().displayImage(url,
//					holder.class_dynamics_civ,
//					(activity).getImageLoaderOptions());

            //方法二

            String url = list.get(position).getPublisherImgUrl();
            if (!DataUtil.isNullorEmpty(url)) {
                String imageurl = DataUtil.getIconURL(url);
                System.out.println("icon file:" + url);

                if (build == null) {
                    build = new DisplayImageOptions.Builder()
                            .resetViewBeforeLoading(true)
                            .cacheInMemory(true)
                            .cacheOnDisc(true)
                            .showImageForEmptyUri(R.drawable.pho_touxiang)  // empty URI时显示的图片
                            .showImageOnFail(R.drawable.pho_touxiang)       // 不是图片文件 显示图片
                            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .considerExifParams(true)
                            .resetViewBeforeLoading(false)
                            .displayer(new FadeInBitmapDisplayer(300)).build();
                }


                //防止点击页面的时候抖动
                if (imageurl.equals(holder.class_dynamics_civ.getTag())) {

                } else {
                    holder.class_dynamics_civ.setTag(imageurl);
                    (activity).getImageLoader().displayImage(imageurl, holder.class_dynamics_civ, build);
                }
//                (activity).getImageLoader().displayImage(imageurl, holder.class_dynamics_civ, build);
            }
        } else {
            if (list.get(position).getPublisherName().equals("于文源")) {
                String url = list.get(position).getPublisherImgUrl();
            }
            holder.class_dynamics_civ.setImageResource(R.drawable.pho_touxiang);
        }

        final String dynamics_class = class_name_b;
        final String publishUserType_final = publishUserType;
        // 名字点击跳转和图片点击跳转 是一样的
        holder.class_dynamics_civ.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //  if(list.get(position).getPublisherName().equals("于文源")){
                String url = list.get(position).getPublisherImgUrl();
                Log.e("url", list.get(position).getPublisherName() + "1");
                //   }
                if ("2".equals(publishUserType_final)) {
                    if (!DataUtil.isNetworkAvailable(activity)) {
                        DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                        return;
                    }

                    String commentUserName = list.get(position).getPublisherName();
                    if (isFamily(commentUserName)) {
                        DataUtil.getToast("家长暂未开放动态！");
                    } else {
                        Intent intent = new Intent(activity,
                                NewPersonalDynamicsActivity.class);
                        intent.putExtra("publishUserId", list.get(position)
                                .getPublishUserId() + "");
                        intent.putExtra("publisherImgUrl", list.get(position)
                                .getPublisherImgUrl());
                        intent.putExtra("publisherName", list.get(position)
                                .getPublisherName());// 发布人
                        intent.putExtra("dynamics_class", dynamics_class);
                        intent.putExtra("classId", list.get(position).getClassId() + "");

                        activity.startActivity(intent);
                    }
                }
                //家长不在有动态
//				else if ("3".equals(publishUserType_final)) {
//
//					String relation = list.get(position).getRelation();
//					if(DataUtil.isNullorEmpty(relation)){
//						relation = "家长";
//					}
//
//					intent.putExtra("publisherName", list.get(position)
//							.getPublishChildName()+relation);// 发布人 家长时
//
//				}

            }
        });
        holder.author_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("2".equals(publishUserType_final)) {
                    if (!DataUtil.isNetworkAvailable(activity)) {
                        DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                        return;
                    }

                    String commentUserName = list.get(position).getPublisherName();
                    if (isFamily(commentUserName)) {
                        DataUtil.getToast("家长暂未开放动态！");
                    } else {
                        Intent intent = new Intent(activity,
                                NewPersonalDynamicsActivity.class);
                        intent.putExtra("publishUserId", list.get(position)
                                .getPublishUserId() + "");
                        intent.putExtra("publisherImgUrl", list.get(position)
                                .getPublisherImgUrl());
                        intent.putExtra("publisherName", list.get(position)
                                .getPublisherName());// 发布人
                        intent.putExtra("dynamics_class", dynamics_class);
                        intent.putExtra("classId", list.get(position).getClassId() + "");

                        activity.startActivity(intent);
                    }
                }
                //家长不在用动态
//				else if ("3".equals(publishUserType_final)) {
//
//					String relation = list.get(position).getRelation();
//					if(DataUtil.isNullorEmpty(relation)){
//						relation = "家长";
//					}
//
//					intent.putExtra("publisherName", list.get(position)
//							.getPublishChildName()+relation);// 发布人 家长时
//
//				}

            }
        });


        String subjectView = list.get(position).getSubjectView();// 发布人在这个班任教科目

//        if (hidePublish) {
//            holder.mew_bjmc_couse_teacher.setVisibility(View.GONE);
//        } else {
//            holder.mew_bjmc_couse_teacher.setVisibility(View.VISIBLE);
//            holder.mew_bjmc_couse_teacher.setText("");
//            if (!DataUtil.isNullorEmpty(subjectView)) {
//                holder.mew_bjmc_couse_teacher.setText(subjectView + "老师");
//            }
//        }

        holder.mew_bjmc_couse_teacher.setVisibility(View.VISIBLE);
        holder.mew_bjmc_couse_teacher.setText("");
        if (!DataUtil.isNullorEmpty(subjectView)) {
            holder.mew_bjmc_couse_teacher.setText(subjectView + "老师");
        }


        String user_id_pub = list.get(position).getPublishUserId() + "";
        String user_id_app = CCApplication.getInstance().getPresentUser()
                .getUserId();

        String acc_id_app = CCApplication.getInstance().getMemberInfo()
                .getAccId();


        holder.author_delete.setVisibility(View.VISIBLE);// 先设置总是可以删除

        if (user_id_app.equals(user_id_pub)) {
            holder.author_delete.setVisibility(View.VISIBLE);
        } else {
            holder.author_delete.setVisibility(View.GONE);
        }


        // 点击删除
        holder.author_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDelete(position, holder, 0, 1);
            }
        });
        // 内容
        String content = list.get(position).getContent();
        if (DataUtil.isNullorEmpty(content)) {
            holder.message_content.setVisibility(View.GONE);
            holder.message_content_long.setVisibility(View.GONE);
        } else {
            holder.message_content.setVisibility(View.VISIBLE);
            holder.message_content_long.setVisibility(View.GONE);
            holder.message_content.setText(content);
            holder.message_content_long.setText(content);

            int contentTength = content.length();
            holder.id_openOrClose.setText(OPEN);//默认显示      全部
            holder.id_openOrClose.setVisibility(View.GONE);//默认 隐藏

            if (contentTength > 90) {
                holder.id_openOrClose.setVisibility(View.VISIBLE);
                holder.id_openOrClose.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //如果显示的是 展开
                        if (holder.id_openOrClose.getText().equals(OPEN)) {
                            //修改为 收起
                            holder.id_openOrClose.setText(CLOSE);
                            //隐藏短文字的文本框
                            holder.message_content.setVisibility(View.GONE);
                            //显示长文字的文本框
                            holder.message_content_long.setVisibility(View.VISIBLE);
                        } else if (holder.id_openOrClose.getText().equals(CLOSE)) {
                            holder.id_openOrClose.setText(OPEN);
                            holder.message_content.setVisibility(View.VISIBLE);
                            holder.message_content_long.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                holder.id_openOrClose.setVisibility(View.GONE);
            }

            try {
                Linkify.addLinks(holder.message_content, Linkify.WEB_URLS);//识别文本超链接
                Linkify.addLinks(holder.message_content_long, Linkify.WEB_URLS);//识别文本超链接
            } catch (Exception e) {
                DataUtil.getToast("您把手机内置默认浏览器删除了~~~");
            }

        }

        String time = list.get(position).getPublishTime();
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
                holder.message_time.setText(time);
            } else {// <0就是以前
                time = time.substring(5, 16);
                holder.message_time.setText(time);//发布时间
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 评论的条数是大于0的时候就显示下划线
        int comSize = list.get(position).getCommentList().size();
        // }


        // 图片处理

        if (list.get(position).getAttachmentInfoList_new() != null) {

            String attchmentType = list.get(position).getAttchmentType();//1是图片  2是视频音频 3是文件文档
            List<String> pcList = new ArrayList<String>();
            List<NewClassDynamicsBean1.ClassAttachmentBean> clssAttachmentList = new ArrayList<NewClassDynamicsBean1.ClassAttachmentBean>();
            clssAttachmentList = list.get(position).getAttachmentInfoList_new();
            for (int k = 0; k < clssAttachmentList.size(); k++) {
                String attachementUrl = list.get(position).getAttachmentInfoList_new().get(k).getAttachementUrl();
                pcList.add(attachementUrl);
            }
//			2是app发的   5和6是web发的
            if ("2".equals(attchmentType) || "6".equals(attchmentType) || "5".equals(attchmentType)) {
                holder.class_vedio_iv.setVisibility(View.VISIBLE);
                holder.class_vedio_ll.setVisibility(View.VISIBLE);
                holder.gridView.setVisibility(View.GONE);
                if ("2".equals(attchmentType)) {//如果是app发的班级动态
                    String pcitureAddress = list.get(position).getPcitureAddress();
                    if (!DataUtil.isNullorEmpty(pcitureAddress)) {
                        String url = DataUtil.getDownloadURL(activity, "/downloadApi?" + pcitureAddress);
                        if (!DataUtil.isNullorEmpty(url)) {
                            url = url.replaceAll("&vedio=ys", "");
                        }
                        activity.getImageLoader().displayImage(url, holder.class_vedio_iv, activity.getImageLoaderOptions());
//                        ImageUtils.showImage(activity, "/downloadApi?"+pcitureAddress, holder.class_vedio_iv);//缩略图
                    } else {
                        holder.class_vedio_iv.setImageResource(R.drawable.default_image);
                    }
                } else {
                    String file = list.get(position).getAttachmentInfoList_new().get(0).getAttachementUrl();
                    String url = DataUtil.getDownloadURL(activity, file + "&vedio=pic");
                    if (!DataUtil.isNullorEmpty(url)) {
                        url = url.replaceAll("&vedio=ys", "");
                    }
                    activity.getImageLoader().displayImage(url, holder.class_vedio_iv,
                            activity.getImageLoaderOptions());
                }

                final List<String> pcList_final = pcList;
                holder.class_vedio_iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PictureURL pictureURL = null;
                        List<PictureURL> datas = new ArrayList<PictureURL>();
                        pictureURL = new PictureURL();
                        String url = DataUtil.getDownloadURL(activity, pcList_final.get(0));
                        String filename = pcList_final.get(0);
                        filename = filename.substring(filename.indexOf("fileId="));
                        showProgessBar();
                        downLoad(url, filename);
//                        pictureURL.setPictureURL(url);
//                        datas.add(pictureURL);
//                        Intent intent = new Intent(activity,
//                                VideoViewActivity.class);
//                        intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_URLS,
//                                (Serializable) datas);
//                        intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_INDEX,
//                                0);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        activity.startActivity(intent);
                    }
                });
            } else {
                holder.class_vedio_iv.setVisibility(View.GONE);
                holder.class_vedio_ll.setVisibility(View.GONE);
                if (pcList.size() == 0) {
                    holder.gridView.setVisibility(View.GONE);
                } else {
                    holder.gridView.setVisibility(View.VISIBLE);

                    NewGridViewAdapter imageAdapter = new NewGridViewAdapter(
                            activity, pcList, pcList, mBitmapMap);
                    holder.gridView.setAdapter(imageAdapter);

                }
            }


        }
        // 点赞
        List<NewPointBean> lovel = list.get(position).getLoveList();

        holder.message_support.setImageResource(R.drawable.new_love_no);
        for (int i = 0; i < lovel.size(); i++) {
            if (!DataUtil.isNullorEmpty(userid)) {
                if (lovel.get(i).getCommentUserId() == Integer.parseInt(userid)) {
                    holder.message_support
                            .setImageResource(R.drawable.new_love_ok);
                }
            }
        }

        // 点赞人图标事件
        holder.message_support.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!DataUtil.isNetworkAvailable(activity)) {
                    DataUtil.getToast(activity.getResources().getString(
                            R.string.no_network));
                    return;
                }
                if (RepeatOnClick.isFastClick()) {
                    return;
                }
                handleClickPraise(holder, position);
            }
        });
        // 点赞人前面的图标

        list.get(position).getLoveList();

        final List<NewPointBean> all = list.get(position).getLoveList();
        holder.message_support_count_name.setText("");

        if (all.size() == 0) {// 点赞人前面图标
            holder.message_support_count_icon.setVisibility(View.GONE);
            holder.message_ll_support_count.setVisibility(View.GONE);

        } else {
            holder.message_ll_support_count.setVisibility(View.VISIBLE);
            holder.message_support_count_icon.setVisibility(View.VISIBLE);

            for (int i = 0; i < all.size(); i++) {

                String commentUserNameStr = all.get(i).getCommentUserName();

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
                                                String commentUserName = all.get(i2).getCommentUserName();
                                                if (isFamily(commentUserName)) {
                                                    DataUtil.getToast("家长暂未开放动态！");
                                                } else {
                                                    if (!DataUtil.isNetworkAvailable(activity)) {
                                                        DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                                                        return;
                                                    }

                                                    Intent intent = new Intent(activity,
                                                            NewPersonalDynamicsActivity.class);
                                                    intent.putExtra("publishUserId", all.get(i2)
                                                            .getCommentUserId() + "");

                                                    String publisherImgUrl = getPointOrCommentUrl(all.get(i2)
                                                            .getCommentUserId() + "", list);
                                                    if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                                        intent.putExtra("publisherImgUrl", publisherImgUrl);
                                                    } else {
                                                        intent.putExtra("publisherImgUrl", all.get(i2)
                                                                .getCommentUserId());// 服务器没有传头像地址给我
                                                    }

                                                    intent.putExtra("publisherName", all.get(i2)
                                                            .getCommentUserName());
                                                    intent.putExtra("classId", list.get(position).getClassId() + "");

                                                    activity.startActivity(intent);
                                                }


                                            }
                                        }, 0, commentUserNameStr.length() + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                // textview.setHighlightColor(Color.TRANSPARENT);//取消高亮

                holder.message_support_count_name.append(spannableString);
                holder.message_support_count_name.setVisibility(View.VISIBLE);
                holder.message_support_count_name
                        .setMovementMethod(LinkMovementMethod.getInstance());
            }

            subLikeTextView(holder);


        }

        // 评论点击事件
        holder.message_comment_icon.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                longTime1 = (new Date()).getTime();
                handleAddComments(position, holder, false, 0);
            }
        });
        // 评论list设置数据

        CommentsListViewAdapter listAdapter = new CommentsListViewAdapter(
                activity, list.get(position).getCommentList());
        holder.message_comment_lv.setAdapter(listAdapter);

        // 评论的条目删除,主号才能操作
        holder.message_comment_lv
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            final int arg2, long arg3) {
                        // 暂时不考虑主副号
                        // if(!"0".equals(isMain)){

                        final int final_arg2 = arg2;

                        String authorid = list.get(position).getCommentList()
                                .get(final_arg2).getCommentUserId()
                                + "";

                        if (userid.equals(authorid)) {
                            handleDelete(position, holder, final_arg2, 0);
                        } else {
                            // 不相等 就是回复那个回复人的回复
                            handleAddComments(position, holder, true, final_arg2);
                        }

                        arg1.findViewById(R.id.authorname).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!DataUtil.isNetworkAvailable(activity)) {
                                    DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                                    return;
                                }

                                String commentUserName = list.get(position).getCommentList().get(arg2).getCommentUserName() + "";
                                if (isFamily(commentUserName)) {
                                    DataUtil.getToast("家长暂未开放动态！");
                                } else {
                                    Intent intent = new Intent(activity,
                                            NewPersonalDynamicsActivity.class);
                                    intent.putExtra("publishUserId", list.get(position).getCommentList().get(arg2).getCommentUserId() + "");

                                    String publisherImgUrl = getPointOrCommentUrl(list.get(position).getCommentList().get(arg2).getCommentUserId() + "", list);
                                    if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                        intent.putExtra("publisherImgUrl", publisherImgUrl);
                                    } else {
                                        intent.putExtra("publisherImgUrl", list.get(position).getCommentList().get(arg2).getCommentUserId() + "");// 服务器没有传头像地址给我
                                    }
                                    intent.putExtra("publisherName", list.get(position).getCommentList().get(arg2).getCommentUserName() + "");
                                    intent.putExtra("classId", list.get(position).getClassId() + "");
                                    activity.startActivity(intent);
                                }
                            }
                        });

                        arg1.findViewById(R.id.authorname_to_authorname_tv).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!DataUtil.isNetworkAvailable(activity)) {
                                    DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                                    return;
                                }
                                String commentUserName = list.get(position).getCommentList().get(arg2).getToCommentUserName() + "";
                                if (isFamily(commentUserName)) {
                                    DataUtil.getToast("家长暂未开放动态！");
                                } else {
                                    Intent intent = new Intent(activity,
                                            NewPersonalDynamicsActivity.class);
                                    intent.putExtra("publishUserId", list.get(position).getCommentList().get(arg2).getToCommentUserId() + "");

                                    String publisherImgUrl = getPointOrCommentUrl(list.get(position).getCommentList().get(arg2).getToCommentUserId() + "", list);
                                    if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                        intent.putExtra("publisherImgUrl", publisherImgUrl);
                                    } else {
                                        intent.putExtra("publisherImgUrl", list.get(position).getCommentList().get(arg2).getToCommentUserId() + "");// 服务器没有传头像地址给我
                                    }
                                    intent.putExtra("publisherName", list.get(position).getCommentList().get(arg2).getToCommentUserName() + "");
                                    intent.putExtra("classId", list.get(position).getClassId() + "");
                                    activity.startActivity(intent);
                                }
                            }
                        });


                    }
                });
        holder.iv_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //分享内容
                String content = list.get(position).getContent();
                String userName = null;
                if ("2".equals(usertype)) {
                    userName = CCApplication.getInstance().getMemberInfo().getUserName();
                } else if ("3".equals(usertype)) {
                    userName = CCApplication.getInstance().getPresentUser().getChildName()
                            + CCApplication.getInstance().getPresentUser().getRelationTypeName();

                }
                //当班级动态没有文字时，显示： 分享人+“给您分享”+班级名称+“动态”，如张三家长给您分享c1001班级动态..
                if (DataUtil.isNullorEmpty(content)) {
                    content = userName + "给你分享了一条班级动态";
                }

                initSharePara(list.get(position).getId(), content);

            }
        });
    }

    /**
     * 截取点赞人数后面的逗号
     *
     * @param holder
     */
    private void subLikeTextView(ViewHolder holder) {
        String trim = holder.message_support_count_name.getText().toString().trim();
        if (!DataUtil.isNullorEmpty(trim)) {
            String substring = trim.substring(0, trim.length() - 1);
            holder.message_support_count_name.setText(substring);
        }
    }

    /**
     * 初始化分享参数
     *
     * @param id
     * @param content
     */
    private void initSharePara(final int id, final String content) {
        //下载图片 存储在本地.
        // 因为微信分享的图片必须要可以下载的或者是存储在本地的图片
        String url = new StringBuilder(Constants.URL)
                .append("/downloadApi?fileId=")
                .append(Constants.SHARE_CLASS_DYNAMICS_PICTURE_ID)
                .append("&sf=80*150").toString();

        activity.getImageLoader().loadImage(url, new ImageLoadingListener() {
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


    // 副号不能点赞和评论
    // private void forbiddenOperation(ViewHolder holder){
    // if("0".equals(isMain)){
    // holder.message_comment_icon.setClickable(false);
    // holder.message_support.setClickable(false);
    // holder.message_comment_lv.setClickable(false);
    // //副号的话 点赞和评论的图标以及数字 全部隐藏
    // holder.message_support.setVisibility(View.GONE);
    // holder.goodNum.setVisibility(View.GONE);
    // holder.pointNum.setVisibility(View.GONE);
    // holder.message_comment_icon.setVisibility(View.GONE);
    // holder.message_support_text.setVisibility(View.GONE);
    // holder.message_comment_text.setVisibility(View.GONE);
    // }
    // }


    /**
     * 分享
     * 如果是本地存储图片 则可以分享到微信和QQ空间
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

        String imgAuthId = Constants.AUTHID + "@" + activity.getDeviceID()
                + "@" + CCApplication.app.getMemberInfo().getAccId();

        String url = new StringBuilder(Constants.URL)
                .append("/classdynamic/classdynamicshare.do?dynamicId=")
                .append(id)
                .append(imgAuthId).toString();
        model.setUrl(url);

        SharePopWindow share = new SharePopWindow(activity, model);
        share.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    class ViewHolder {
        TextView author_name;
        TextView mew_bjmc;
        TextView mew_bjmc_couse_teacher;

        TextView author_delete;

        TextView message_content;
        TextView message_content_long;
        TextView id_openOrClose;

        TextView message_time;
        TextView message_support_count_name;
        TextView pointNum;
        TextView goodNum;
        RelativeLayout message_ll_support_count;
        ImageView message_support_count_icon;
        ImageView message_support;
        ImageView message_comment_icon;
        ListView message_comment_lv;
        // GridView gridView, new_goodlove_gv;
        GridView gridView;
        TextView message_support_text, message_comment_text;
        RoundImageView class_dynamics_civ;
        ImageView class_vedio_iv;
        RelativeLayout class_vedio_ll;

        ImageView iv_share;
    }

    // 添加数据
    public void addItem(List<NewClassDynamicsBean1> i) {
        list.addAll(i);
        // list.add(i);
    }

    /**
     * 给图片数据
     *
     * @param sourceStrArray
     * @return
     */
    public List<Map<String, Object>> getData(Bitmap[] sourceStrArray) {
        // cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < sourceStrArray.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", sourceStrArray[i]);
            data_list.add(map);
        }
        return data_list;
    }

    /**
     * 给popwin数据
     *
     * @param
     * @return
     */
    public List<Map<String, Object>> getDataPoint(List<String> authorname,
                                                  List<String> content) {
        for (int i = 0; i < authorname.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("authorname", authorname.get(i));
            map.put("content", content.get(i));
            data_list_Point.add(map);
        }
        return data_list_Point;
    }

    // 点赞图标事件
    private void handleClickPraise(ViewHolder holder, final int position) {

        if (!DataUtil.isNetworkAvailable(activity)) {
            DataUtil.getToast(activity.getResources().getString(
                    R.string.no_network));
            return;
        }

        // 设置不可点击
        List<NewPointBean> lo = list.get(position).getLoveList();
        if (lo.size() == 0) {
            holder.message_support.setImageResource(R.drawable.new_love_ok);

            holder.message_ll_support_count.setVisibility(View.VISIBLE);
            holder.message_support_count_icon.setVisibility(View.VISIBLE);

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

            npb.setCommentUserId(Integer.parseInt(userid));

            // 点赞人名字的显示


            final List<NewPointBean> lll = list.get(position).getLoveList();
            lll.add(npb);

            holder.message_support_count_name.setText("");

            for (int i = 0; i < lll.size(); i++) {
                // String text = "点赞人";
                String commentUserNameStr = lll.get(i).getCommentUserName();

                SpannableString spannableString = new SpannableString(
                        commentUserNameStr + ",");

                final int i2 = i;
                spannableString.setSpan(new ClickableSpan() {

                                            @Override
                                            public void updateDrawState(TextPaint ds) {
                                                super.updateDrawState(ds);
                                                // ds.setColor(Color.WHITE); //设置文件颜色
                                                ds.setUnderlineText(false); // 设置下划线
                                                ds.setColor(Color.parseColor("#01afef"));
                                            }

                                            @Override
                                            public void onClick(View widget) {

                                                if (!DataUtil.isNetworkAvailable(activity)) {
                                                    DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                                                    return;
                                                }

                                                String commentUserName = lll.get(i2).getCommentUserName();
                                                if (isFamily(commentUserName)) {
                                                    DataUtil.getToast("家长暂未开放动态！");
                                                } else {
                                                    Intent intent = new Intent(activity,
                                                            NewPersonalDynamicsActivity.class);
                                                    intent.putExtra("publishUserId", lll.get(i2)
                                                            .getCommentUserId() + "");

                                                    String publisherImgUrl = getPointOrCommentUrl(lll.get(i2)
                                                            .getCommentUserId() + "", list);
                                                    if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                                        intent.putExtra("publisherImgUrl", publisherImgUrl);
                                                    } else {
                                                        intent.putExtra("publisherImgUrl", lll.get(i2)
                                                                .getCommentUserId());// 服务器没有传头像地址给我
                                                    }

                                                    intent.putExtra("publisherName", lll.get(i2)
                                                            .getCommentUserName());
                                                    intent.putExtra("classId", list.get(position).getClassId() + "");

                                                    activity.startActivity(intent);
                                                }

                                            }
                                        }, 0, commentUserNameStr.length() + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                // textview.setHighlightColor(Color.TRANSPARENT);//取消高亮

                holder.message_support_count_name.append(spannableString);
                holder.message_support_count_name.setVisibility(View.VISIBLE);
                holder.message_support_count_name
                        .setMovementMethod(LinkMovementMethod.getInstance());
            }
            subLikeTextView(holder);
            praiseWithServer(position, 0);
        } else {

            boolean like = false;// 判断是否点赞过

            for (int i = 0; i < lo.size(); i++) {
                if (lo.get(i).getCommentUserId() == Integer.parseInt(userid)) {
                    like = true;
                }
            }

            if (like) {
                // id相等 表示已经点赞
                // 如果有点赞 点赞后变灰色心心

                if (lo.size() > 1) {
                    holder.message_ll_support_count.setVisibility(View.VISIBLE);
                    holder.message_support_count_icon
                            .setVisibility(View.VISIBLE);
                } else {
                    holder.message_ll_support_count.setVisibility(View.GONE);
                    holder.message_support_count_icon.setVisibility(View.GONE);
                }

                holder.message_support.setImageResource(R.drawable.new_love_no);
                final List<NewPointBean> lll = list.get(position).getLoveList();
                for (int j = 0; j < lll.size(); j++) {
                    if (lll.get(j).getCommentUserId() == Integer
                            .parseInt(userid)) {
                        lll.remove(j);
                    }
                }

                holder.message_support_count_name.setText("");
                for (int i = 0; i < lll.size(); i++) {
                    // String text = "点赞人";
                    String commentUserNameStr = lll.get(i).getCommentUserName();

                    SpannableString spannableString = new SpannableString(
                            commentUserNameStr + ",");

                    final int i2 = i;
                    spannableString.setSpan(new ClickableSpan() {

                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    // ds.setColor(Color.WHITE); //设置文件颜色
                                                    ds.setUnderlineText(false); // 设置下划线
                                                    ds.setColor(Color.parseColor("#01afef"));
                                                }

                                                @Override
                                                public void onClick(View widget) {

                                                    if (!DataUtil.isNetworkAvailable(activity)) {
                                                        DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                                                        return;
                                                    }
                                                    String commentUserName = lll.get(i2).getCommentUserName();
                                                    if (isFamily(commentUserName)) {
                                                        DataUtil.getToast("家长暂未开放动态！");
                                                    } else {

                                                        Intent intent = new Intent(activity,
                                                                NewPersonalDynamicsActivity.class);
                                                        intent.putExtra("publishUserId", lll.get(i2)
                                                                .getCommentUserId() + "");

                                                        String publisherImgUrl = getPointOrCommentUrl(lll.get(i2)
                                                                .getCommentUserId() + "", list);
                                                        if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                                            intent.putExtra("publisherImgUrl", publisherImgUrl);
                                                        } else {
                                                            intent.putExtra("publisherImgUrl", lll.get(i2)
                                                                    .getCommentUserId());// 服务器没有传头像地址给我
                                                        }

                                                        intent.putExtra("publisherName", lll.get(i2)
                                                                .getCommentUserName());
                                                        intent.putExtra("classId", list.get(position).getClassId() + "");

                                                        activity.startActivity(intent);
                                                    }


                                                }
                                            }, 0, commentUserNameStr.length() + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // textview.setHighlightColor(Color.TRANSPARENT);//取消高亮

                    holder.message_support_count_name.append(spannableString);
                    holder.message_support_count_name
                            .setVisibility(View.VISIBLE);
                    holder.message_support_count_name
                            .setMovementMethod(LinkMovementMethod.getInstance());
                }
                subLikeTextView(holder);
                praiseWithServer(position, 2);
            } else {
                // 不相等 表示没有点赞
                // 如果没有点赞 点赞后变蓝色心心


                holder.message_support.setImageResource(R.drawable.new_love_ok);
                // 点击完后发送给web 其他数据从web拿去然后改变组件显示
                NewPointBean npb = new NewPointBean();

                if ("2".equals(usertype)) {

                    String userName = CCApplication.getInstance()
                            .getMemberInfo().getUserName();
                    npb.setCommentUserName(userName);
                } else if ("3".equals(usertype)) {
                    String userName = CCApplication.getInstance().getPresentUser().getChildName()
                            + CCApplication.getInstance().getPresentUser().getRelationTypeName();
                    npb.setCommentUserName(userName);
                }

                npb.setCommentUserId(Integer.parseInt(userid));
                final List<NewPointBean> lll = list.get(position).getLoveList();
                lll.add(npb);

                holder.message_support_count_name.setText("");
                for (int i = 0; i < lll.size(); i++) {
                    // String text = "点赞人";
                    String commentUserNameStr = lll.get(i).getCommentUserName();

                    SpannableString spannableString = new SpannableString(
                            commentUserNameStr + ",");

                    final int i2 = i;
                    spannableString.setSpan(new ClickableSpan() {

                                                @Override
                                                public void updateDrawState(TextPaint ds) {
                                                    super.updateDrawState(ds);
                                                    // ds.setColor(Color.WHITE); //设置文件颜色
                                                    ds.setUnderlineText(false); // 设置下划线
                                                    ds.setColor(Color.parseColor("#01afef"));
                                                }

                                                @Override
                                                public void onClick(View widget) {

                                                    if (!DataUtil.isNetworkAvailable(activity)) {
                                                        DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                                                        return;
                                                    }

                                                    String commentUserName = lll.get(i2).getCommentUserName();
                                                    if (isFamily(commentUserName)) {
                                                        DataUtil.getToast("家长暂未开放动态！");
                                                    } else {

                                                        Intent intent = new Intent(activity,
                                                                NewPersonalDynamicsActivity.class);
                                                        intent.putExtra("publishUserId", lll.get(i2)
                                                                .getCommentUserId() + "");

                                                        String publisherImgUrl = getPointOrCommentUrl(lll.get(i2)
                                                                .getCommentUserId() + "", list);
                                                        if (!DataUtil.isNullorEmpty(publisherImgUrl)) {
                                                            intent.putExtra("publisherImgUrl", publisherImgUrl);
                                                        } else {
                                                            intent.putExtra("publisherImgUrl", lll.get(i2)
                                                                    .getCommentUserId());// 服务器没有传头像地址给我
                                                        }

                                                        intent.putExtra("publisherName", lll.get(i2)
                                                                .getCommentUserName());
                                                        intent.putExtra("classId", list.get(position).getClassId() + "");

                                                        activity.startActivity(intent);
                                                    }

                                                }
                                            }, 0, commentUserNameStr.length() + 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // textview.setHighlightColor(Color.TRANSPARENT);//取消高亮

                    holder.message_support_count_name.append(spannableString);
                    holder.message_support_count_name
                            .setVisibility(View.VISIBLE);
                    holder.message_support_count_name
                            .setMovementMethod(LinkMovementMethod.getInstance());
                }
                subLikeTextView(holder);
                praiseWithServer(position, 0);
            }

        }
        // notifyDataSetChanged();
        // //点击完后发送给web 其他数据从web拿去然后改变组件显示
        // praiseWithServer(position,flag);
    }

    // 点赞往服务器传数据
    private void praiseWithServer(final int position, final int flag) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("userId", userid);
                    if ("2".equals(usertype)) {

                        String userName = CCApplication.getInstance()
                                .getMemberInfo().getUserName();
                        json.put("userName", userName);
                    } else if ("3".equals(usertype)) {
                        String userName = CCApplication.getInstance().getPresentUser().getChildName()
                                + CCApplication.getInstance().getPresentUser().getRelationTypeName();
                        json.put("userName", userName);
                    }

                    json.put("interactionId", list.get(position).getId());
                    json.put("flag", flag);
                    json.put("classId", Integer.parseInt(resource_id));
                    json.put("hidePublish", hidePublish);
                    json.put("userType", usertype);

                    String url = new StringBuilder(Constants.SERVER_URL)
                            .append(Constants.PUBLISH_DYNAMIC_COMMENT).toString();
                    String result = HttpHelper
                            .httpPostJson(activity, url, json);
                    JSONObject json1 = new JSONObject(result);
                    String isDelete = json1.getString("isDelete");
                    System.out.println("===isDelete====" + isDelete);

                    if ("1".equals(isDelete)) {
                        DataUtil.getToast("该内容已被删除");
                        Intent broadcast = new Intent(
                                NewClassDynamicsActivity.INTENT_REFESH_DATA);
                        activity.sendBroadcast(broadcast);
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
    private void handleAddComments(final int i, final ViewHolder holder,
                                   final boolean isAt, final int arg2) {
        String getParentName_a = CCApplication.getInstance().getPresentUser()
                .getParentName();
        String getUserName = CCApplication.getInstance().getMemberInfo()
                .getUserName();

//		LayoutInflater inflater = (LayoutInflater) activity
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(activity);
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
        window.showAtLocation(activity.findViewById(R.id.message_comment_icon),
                Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        Button first = (Button) view.findViewById(R.id.pop_btn);
        final EditText ed = (EditText) view.findViewById(R.id.ed);

        ed.setFilters(new InputFilter[]{new EditTextLengthFilter(100)});

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
                if (!DataUtil.isNetworkAvailable(activity)) {
                    DataUtil.getToast(activity.getResources().getString(
                            R.string.no_network));
                    return;
                }
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
                        String userName = CCApplication.getInstance().getPresentUser().getChildName()
                                + CCApplication.getInstance().getPresentUser().getRelationTypeName();
                        json.put("userName", userName);
                    }

                    json.put("interactionId", list.get(i).getId());
                    json.put("flag", 1);
                    json.put("classId", resource_id);
                    json.put("textfield", ed.getText());
                    json.put("hidePublish", hidePublish);
                    json.put("userType", usertype);
                    if (isAt) {
                        json.put("pointCommentId", list.get(i).getCommentList()
                                .get(arg2).getId());
                    } else {
                        json.put("pointCommentId", "");
                    }

                    String url = new StringBuilder(Constants.SERVER_URL)
                            .append(Constants.PUBLISH_DYNAMIC_COMMENT).toString();
                    String result = HttpHelper
                            .httpPostJson(activity, url, json);

                    JSONObject json1 = new JSONObject(result);
                    String isDelete = json1.getString("isDelete");
                    if ("1".equals(isDelete)) {
                        DataUtil.getToast("该内容已被删除");
                    } else {
                        //局部刷新listview中的数据
                        NewPointBean pb = new NewPointBean();


                        if ("2".equals(usertype)) {

                            String userName = CCApplication.getInstance().getMemberInfo()
                                    .getUserName();
                            pb.setCommentUserName(userName);

                        } else if ("3".equals(usertype)) {
                            String userName = CCApplication.getInstance().getPresentUser().getChildName()
                                    + CCApplication.getInstance().getPresentUser().getRelationTypeName();

                            pb.setCommentUserName(userName);
                        }

                        if (isAt) {
                            pb.setToCommentUserName(list.get(i).getCommentList().get(arg2).getCommentUserName());

                        } else {
                            pb.setToCommentUserName("");
                        }


                        pb.setCommentUserId(Integer.parseInt(userid));
                        pb.setContent(ed.getText().toString());
                        int id = Integer.parseInt(json1.getString("commontId"));

                        pb.setId(id);

                        list.get(i).getCommentList().add(pb);
                        notifyDataSetChanged();
                    }


                    window.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 通知activity刷新数据
//				Intent broadcast = new Intent(
//						NewClassDynamicsActivity.INTENT_REFESH_DATA);
//				activity.sendBroadcast(broadcast);
            }
        });

        longTime2 = (new Date()).getTime();
        System.out.println("====longTime2-longTime2====" + (longTime2 - longTime1));

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
    private void handleDelete(final int postion, final ViewHolder holder,
                              final int arg2, final int q) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("温馨提示");
        if (1 == q) {
            builder.setMessage("是否删除本条班级动态?");
        } else {
            builder.setMessage("是否删除本条评论?");
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doDelete(q, postion, arg2, hidePublish);
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


//        // 利用layoutInflater获得View
//        LayoutInflater inflater = (LayoutInflater) activity
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.delete_point, null);
//
//        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
//
//        final PopupWindow window = new PopupWindow(view,
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT);
//
//        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
//        window.setFocusable(true);
//
//        // 防止虚拟软键盘被弹出菜单遮住
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        window.setBackgroundDrawable(dw);
//
//        // 设置popWindow的显示和消失动画
//        window.setAnimationStyle(R.style.mypopwindow_anim_style);
//        // 在底部显示
//        window.showAtLocation(activity.findViewById(R.id.message_comment_icon),
//                Gravity.BOTTOM, 0, 0);
//
//        // 这里检验popWindow里的button是否可以点击
//        Button cancel_delete = (Button) view.findViewById(R.id.cancel_delete);
//        Button determine_delete = (Button) view
//                .findViewById(R.id.determine_delete);
//        // 取消
//        cancel_delete.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                window.dismiss();
//            }
//        });
//        // 确定
//        determine_delete.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                doDelete(q, postion, arg2);
//
//                window.dismiss();
//            }
//        });
//
//        // popWindow消失监听方法
//        window.setOnDismissListener(new OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//            }
//        });
    }

    private void doDelete(int q, int i, int arg2, boolean hidePublish) {
        if (!DataUtil.isNetworkAvailable(activity)) {
            DataUtil.getToast(activity.getResources().getString(
                    R.string.no_network));
            return;
        }
        // q == 1表示是删除整条班级动态的操作
        if (q == 1) {
            notifyDataSetChanged();
            String result = null;
            JSONObject json = new JSONObject();
            try {
                json.put("classDynamicId", list.get(i).getId());
                json.put("hidePublish", hidePublish);
                String url = new StringBuilder(Constants.SERVER_URL)
                        .append(Constants.DELETE_CLASS_DYNAMIC).toString();

                result = HttpHelper.httpPostJson(activity, url, json);

                // activity.queryPost(url,json);
                // resultCode 200 删除成功
                list.remove(i);
                Intent broadcast = new Intent(
                        NewClassDynamicsActivity.INTENT_REFESH_DATA);

                activity.sendBroadcast(broadcast);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {// 删除某条评论
            String result = null;
            JSONObject json = new JSONObject();
            try {
                json.put("classDynamicCommentId", list.get(i)
                        .getCommentList().get(arg2).getId());
                json.put("hidePublish", hidePublish);

                String url = new StringBuilder(Constants.SERVER_URL)
                        .append(Constants.DELETE_CLASS_DYNAMIC_COMMENT).toString();
                result = HttpHelper.httpPostJson(activity, url, json);

                // resultCode 200 删除成功
                JSONObject json1 = new JSONObject(result);

//						notifyDataSetChanged();
                Intent broadcast = new Intent(
                        NewClassDynamicsActivity.INTENT_REFESH_DATA);

                broadcast.putExtra("flag", 3);
                broadcast.putExtra("type", "comment");
                broadcast.putExtra("interactionId", list.get(i).getId());
                broadcast.putExtra("pointCommentId", list.get(i)
                        .getCommentList().get(arg2).getId());
                activity.sendBroadcast(broadcast);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            if (!DataUtil.isNetworkAvailable(activity)) {
                DataUtil.getToast("");
                return;
            }
//            System.out.println("获取所有关系入参json=====" + json);
            String url = new StringBuilder(Constants.SERVER_URL).append(Constants.GET_RELATION_CODE).toString();
            String result = HttpHelper.httpPostJson(activity, url, json);
//            System.out.println("获取所有关系出参result===" + result);
            NewBaseBean ret2 = JsonUtils.fromJson(result, NewBaseBean.class);
            if (ret2.getServerResult().getResultCode() != 200) {// 请求失败
                DataUtil.getToast(ret2.getServerResult().getResultMessage());
            } else {
                JSONObject jsonB = new JSONObject(result);
                childRelationTypeList = new ArrayList<ChildRelationTypeBean>();
                String relationShipList = jsonB.getString("relationShipList");
                JSONArray json2 = new JSONArray(relationShipList);
                for (int i = 0; i < json2.length(); i++) {
                    ChildRelationTypeBean childRelationTypeBean = JsonUtils.fromJson(json2
                            .get(i).toString(), ChildRelationTypeBean.class);
                    childRelationTypeList.add(childRelationTypeBean);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean isFamily(String name) {
        if (childRelationTypeList != null && !DataUtil.isNullorEmpty(name)) {
            if (childRelationTypeList.size() > 0) {
                for (int i = 0; i < childRelationTypeList.size(); i++) {
                    String childRelationType = childRelationTypeList.get(i).getConfigName();
                    if (name.contains(childRelationType)) {
                        return true;
                    }
                }
            }
        }
        return false;
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
                    DataUtil.getToast(activity.getResources().getString(R.string.no_network));// 当前网络不可用，请检查您的网络设置
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
        activity.startActivity(intent);

    }

    private String currentFile = "";
    private ChildMessage current_childMessage = null;

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

    public void dismissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private ProgressDialog pd;

    public void showProgessBar() {

        pd = new ProgressDialog(activity);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //cancel(true);
                dismissPd();

            }
        });
        pd.show();
        pd.setContentView(R.layout.progress_bar);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.getWindow().setBackgroundDrawableResource(R.color.transparent);


    }


}
