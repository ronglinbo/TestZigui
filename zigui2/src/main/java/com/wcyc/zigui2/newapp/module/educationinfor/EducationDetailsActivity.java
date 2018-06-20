package com.wcyc.zigui2.newapp.module.educationinfor;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.NewSchoolNewsCommentBean;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.ImagePagerActivity;
import com.wcyc.zigui2.newapp.adapter.CommentsNewsListViewAdapter;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewPointBean;
import com.wcyc.zigui2.newapp.bean.ShareModel;
import com.wcyc.zigui2.newapp.fragment.NewFindFragment;
import com.wcyc.zigui2.newapp.widget.SharePopWindow;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 教育资讯详情
 *
 * @author 郑国栋 201703-02
 * @version 2.0.12
 */
public class EducationDetailsActivity extends BaseActivity implements
        OnClickListener {

    private LinearLayout title_back;
    private TextView new_content;
    private WebView webView;
    private TextView education_title;
    private TextView education_time;
    private TextView education_read_numb;
    private ImageView news_good_iv;
    private TextView news_goodNum;
    private ImageView news_comment_iv;
    private TextView news_commentNum;
    private MyListView news_comment_mylv;
    private String hasGoodComment;
    private String interactiveCount;
    private String news_goodNumStr;
    private String news_commentNumStr;
    private int news_goodNumInt;
    private EducationCommentsAdapter educationCommentsAdapter;
    private TextView load_more;
    private EducationInforBean educationInforBean;
    private List<EducationCommentBean> educationCommentBeanList;
    private String campuNewsId;
    private String userid;
    private String userType;
    private int k = 2;
    private int size = 0;
    private int totalPageNum = 0;
    private String commentUserName;
    private String accId;
    private String deviceId;
    private EcirclesTypeBean ecirclesTypeBean;
    private TextView education_type;
    private String isRead;
    private int position;

    //是否可以评论 默认是否
    private boolean commentStatus;
    private ImageView iv_share;
    private View parentView;
    private String clearContent;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.education_details_activity);

        parentView = LayoutInflater.from(this).inflate(R.layout.education_details_activity, null);
        setContentView(parentView);

        initView();
        initDatas();
        initEvents();
    }

    // 实例化组件
    private void initView() {
        new_content = (TextView) parentView.findViewById(R.id.new_content);// 标题
        title_back = (LinearLayout) parentView.findViewById(R.id.title_back);// 返回键
        education_title = (TextView) parentView.findViewById(R.id.education_title);// 标题
        education_time = (TextView) parentView.findViewById(R.id.education_time);// 发布时间
        education_read_numb = (TextView) parentView.findViewById(R.id.education_read_numb);// 浏览数
        webView = (WebView) parentView.findViewById(R.id.education_html_content);// 内容
        news_good_iv = (ImageView) parentView.findViewById(R.id.news_good_iv);// 点赞图标
        news_goodNum = (TextView) parentView.findViewById(R.id.news_goodNum);// 点赞数
        news_comment_iv = (ImageView) parentView.findViewById(R.id.news_comment_iv);// 评论图标
        news_commentNum = (TextView) parentView.findViewById(R.id.news_commentNum);// 评论数
        news_comment_mylv = (MyListView) parentView.findViewById(R.id.news_comment_mylv);// 评论listview
        load_more = (TextView) parentView.findViewById(R.id.load_more);
        education_type = (TextView) parentView.findViewById(R.id.education_type);

        iv_share = (ImageView) parentView.findViewById(R.id.title_imgbtn_add);
        iv_share.setBackgroundResource(R.drawable.icon_fenxiang);
        iv_share.setVisibility(View.VISIBLE);
    }

    // 初始化数据
    @SuppressLint("NewApi")
    private void initDatas() {
        k = 2;
        new_content.setText("教育资讯详情");
        educationInforBean = new EducationInforBean();
        educationCommentBeanList = new ArrayList<EducationCommentBean>();
        userid = CCApplication.getInstance().getPresentUser().getUserId();
        accId = CCApplication.getInstance().getMemberInfo().getAccId();
        deviceId = getDeviceID();
        userType = CCApplication.getInstance().getPresentUser().getUserType();
        if ("2".equals(userType)) {
            commentUserName = CCApplication.getInstance().getMemberInfo().getUserName();
        } else if ("3".equals(userType)) {
            commentUserName = CCApplication.getInstance().getPresentUser().getChildName()
                    + CCApplication.getInstance().getPresentUser().getRelationTypeName();
        }
        position = getIntent().getIntExtra("position", 0);
        campuNewsId = getIntent().getStringExtra("campuNewsId");
        isRead = getIntent().getStringExtra("isRead");
        try {
            JSONObject json2 = new JSONObject();
            json2.put("campuNewsId", campuNewsId);
            json2.put("userId", userid);
            json2.put("userType", userType);
            json2.put("imgAuthId", "MB@" + deviceId + "@" + accId);
            json2.put("curPage", 1);// curPage,pageSize
            json2.put("pageSize", 30);
            if ("3".equals(userType)) {
                String studentId = CCApplication.getInstance().getPresentUser().getChildId();
                json2.put("studentId", studentId);
//				json2.put("userType", "1");//模块数需要用1学生，2老师，3家长
            }
            if (!DataUtil.isNetworkAvailable(EducationDetailsActivity.this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }
            if (!isLoading()) {
                System.out.println("==教育资讯详情入参===" + json2);
                String url = new StringBuilder(Constants.SERVER_URL).append(
                        Constants.GET_SCHOOL_ECIRCLES_BY_ID).toString();
                String result = HttpHelper.httpPostJson(this, url, json2);
                System.out.println("==教育资讯详情出参=====" + result);
                NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
                if (ret.getServerResult().getResultCode() != 200) {// 请求失败
                    DataUtil.getToast(ret.getServerResult().getResultMessage());
                    return;
                } else {
                    JSONObject json3 = new JSONObject(result);
                    String totalPageNumStr = json3.getString("totalPageNum");
                    totalPageNum = Integer.parseInt(totalPageNumStr);
                    System.out.println("totalPageNum=====" + totalPageNum);

                    String schoolEcircles = json3.getString("schoolEcircles");
                    educationInforBean = JsonUtils.fromJson(schoolEcircles,
                            EducationInforBean.class);

                    //获取到是否可以评论和回复
                    commentStatus = educationInforBean.isComment_status();
                    if (commentStatus) {
                        news_comment_iv.setVisibility(View.VISIBLE);
                        news_commentNum.setVisibility(View.VISIBLE);
                    } else {
                        news_comment_iv.setVisibility(View.GONE);
                        news_commentNum.setVisibility(View.GONE);
                    }

                    // 分享的内容
                    clearContent = educationInforBean.getClearContent();

                    //分享的封面
                    imageUrl = educationInforBean.getCover();

                    String commentListStr = json3.getString("commentList");
                    if (!DataUtil.isNullorEmpty(commentListStr)) {
                        JSONArray jsonA = new JSONArray(commentListStr);
                        for (int i = 0; i < jsonA.length(); i++) {
                            EducationCommentBean educationCommentBean = JsonUtils
                                    .fromJson(jsonA.get(i).toString(),
                                            EducationCommentBean.class);
                            educationCommentBeanList
                                    .add(educationCommentBean);
                        }
                        size = educationCommentBeanList.size();// totalPage
                    }
                    hasGoodComment = json3.getString("hasGoodComment");
                    interactiveCount = json3.getString("interactiveCount");

                    String ecirclesType = json3.getString("ecirclesType");
                    ecirclesTypeBean = JsonUtils.fromJson(ecirclesType,
                            EcirclesTypeBean.class);

                    //如果是未读
                    if ("0".equals(isRead)) {
                        //删除模块已读记录
                        JSONObject jsonMod = new JSONObject();
                        if (user != null) {
                            try {
                                jsonMod.put("userId", user.getUserId());
                                jsonMod.put("userType", user.getUserType());
                                jsonMod.put("dataId", campuNewsId);
                                jsonMod.put("modelType", "28");
                                if (CCApplication.getInstance().isCurUserParent()) {
                                    jsonMod.put("studentId", user.getChildId());
                                }
//							System.out.println("===清除这条教育资讯记录数入参=="+jsonMod);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String urlMod = new StringBuilder(Constants.SERVER_URL)
                                    .append(Constants.DEL_MODEL_REMIND_ZGD).toString();
                            String resultMod = HttpHelper.httpPostJson(this, urlMod, jsonMod);
//							System.out.println("==resultMod==="+resultMod);
                            // json对象 里面有属性ServerResult 请求结果
                            NewBaseBean bb = JsonUtils.fromJson(resultMod,
                                    NewBaseBean.class);
                            if (bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                                System.out.println("===教育资讯模块记录数-已删除===");
                            }

                            //发现页面小红点
                            NewFindFragment.ed_unread_msg_int -= 1;
                        }
//						//删除消息记录
//						JSONObject jsonMes = new JSONObject();
//						if(user != null){
//							try {
//								jsonMes = new JSONObject();
//								jsonMes.put("userId", user.getUserId());
//								jsonMes.put("userType",user.getUserType());
//								jsonMes.put("msgId",campuNewsId);
//								jsonMes.put("messageType", "27");
//								if(CCApplication.getInstance().isCurUserParent()){
//									jsonMod.put("studentId", user.getChildId());
//								}
////							System.out.println("===删除除这条教育资讯消息记录入参=="+jsonMes);
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//
//							String urlMes = new StringBuilder(Constants.SERVER_URL)
//									.append(Constants.DEL_READ_MESSAGE).toString();
//							String resultMes = HttpHelper.httpPostJson(this, urlMes, jsonMes);
////							System.out.println("==resultMes==="+resultMes);
//							// json对象 里面有属性ServerResult 请求结果
//							NewBaseBean bb = JsonUtils.fromJson(resultMes,
//									NewBaseBean.class);
//							if (bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
//								System.out.println("===教育资讯消息页记录-已删除===");
//							}
//						}
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        education_title.setText(educationInforBean.getTitle());
        education_time.setText(educationInforBean.getPublishTime());
//        education_read_numb.setText(educationInforBean.getBrowseNo() + "");
        if (educationInforBean.getBrowseNo() < 100000) {
            education_read_numb.setText(educationInforBean.getBrowseNo() + "");
        } else {
            education_read_numb.setText("99999+");
        }

        String contentStr = educationInforBean.getContent();
        if (!DataUtil.isNullorEmpty(contentStr)) {
            if (contentStr.length() > 0) {
                String content = contentStr;
                String head = "<head><style>img{max-width:320px !important;max-height:320px !important;}</style></head>";
                content = head + content;
                webView.getSettings().setDefaultTextEncodingName("UTF -8");// 设置默认为utf-8
                webView.getSettings().setJavaScriptEnabled(true);

                webView.removeJavascriptInterface("searchBoxJavaBridge_");
                webView.removeJavascriptInterface("accessibility");
                webView.removeJavascriptInterface("accessibilityTraversal");

                webView.getSettings().setSavePassword(false);
                webView.loadData(content, "text/html; charset=UTF-8", null);//
                webView.addJavascriptInterface(new JavascriptInterface(this),
                        "imagelistner");
                webView.setWebViewClient(new MyWebViewClient());
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int dpi = dm.densityDpi;
                double scale = DataUtil.TableWidthExceed(this, content);
                if (scale > 1) {
                    double times = ((double) dpi) / 240;
                    webView.setInitialScale((int) (scale * times * 100));
                }

            } else {
                webView.setVisibility(View.GONE);
            }
        }

        news_goodNumStr = educationInforBean.getGoodCommentNo();
        news_goodNum.setText(news_goodNumStr);
        news_commentNumStr = interactiveCount;
        news_commentNum.setText(news_commentNumStr);

        if ("true".equals(hasGoodComment)) {
            news_good_iv.setImageResource(R.drawable.new_love_ok);
        } else if ("false".equals(hasGoodComment)) {
            news_good_iv.setImageResource(R.drawable.new_love_no);
        }

        educationCommentsAdapter = new EducationCommentsAdapter(this,
                educationCommentBeanList);
        news_comment_mylv.setAdapter(educationCommentsAdapter);
        if (totalPageNum > 1) {
            load_more.setVisibility(View.VISIBLE);
        } else {
            load_more.setVisibility(View.GONE);
        }
        try {
            education_type.setText(ecirclesTypeBean.getConfigName() + "");
        } catch (Exception e) {
            education_type.setText("");
        }

//		Intent broadcast = new Intent(EducationInforActivity.EDUCATION_REFESH_DATA);//进入次界面，或刷新时，通知新闻列表刷新浏览数
//		sendBroadcast(broadcast);

        if (DataUtil.pd != null) {
            DataUtil.clearDialog();
        }
    }

    // 设置点击效果监听器
    private void initEvents() {
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);
        news_good_iv.setOnClickListener(this);
//		news_comment_iv.setOnClickListener(this);
        load_more.setOnClickListener(this);

        //回复文章
        news_comment_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentStatus) {
                    handleAddComments(0, false, 0);
                }
            }
        });

        //评论他人和删除自己评论
        news_comment_mylv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (commentStatus) {
                    String comment_user_id = educationCommentBeanList.get(arg2)
                            .getComment_user_id();
                    if (userid.equals(comment_user_id)) {
                        // 相等就是删除
                        handleDelete(0, arg2, 0);
                    } else {
                        // 不相等 就是回复那个回复人的回复
                        handleAddComments(0, true, arg2);
                    }
                }
            }
        });


        iv_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                initSharePara();
            }
        });
    }


    /**
     * 初始化分享参数
     */
    private void initSharePara() {
        //下载图片 存储在本地.
        // 因为微信分享的图片必须要可以下载的或者是存储在本地的图片
        String url = new StringBuilder(Constants.URL)
                .append("/downloadApi?fileId=")
                .append(imageUrl)
                .append("&sf=80*150").toString();

        getImageLoader().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String url, View view) {
            }

            @Override
            public void onLoadingFailed(String url, View view, FailReason failReason) {
                share(url);
            }

            @Override
            public void onLoadingComplete(String url, View view, Bitmap bitmap) {
                String folder = Environment.getExternalStorageDirectory() + "/ZIGUI_Photos/";
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String time = formatter.format(new Date());
                String fileName = folder + time + ".jpeg";

                boolean flag = DataUtil.saveBitmap(bitmap, fileName);
                //获取到bitmap对象  存储到sd上.
                if (flag) {
                    share(fileName);
                } else {
                    share(url);
                }
            }

            @Override
            public void onLoadingCancelled(String url, View view) {
            }
        });
    }


    /**
     * 分享
     * 如果是本地存储图片 则可以分享到微信和QQ空间
     * <p>
     * 如果是服务器粗存储的图片 则需要可以下载的地址  否则没有办法分享到微信
     *
     * @param imageLocal
     */
    private void share(String imageLocal) {
        ShareModel model = new ShareModel();

        //微信图片分享地址
        model.setImageLocal(imageLocal);

        String QQUrl = new StringBuilder(Constants.URL)
                .append("/downloadApi?fileId=")
                .append(imageUrl)
                .append("&sf=80*150").toString();

        //QQ图片分享地址
        model.setImageUrl(QQUrl);

        if (!DataUtil.isNullorEmpty(clearContent)) {
            model.setText(clearContent);
        } else {
            model.setText(educationInforBean.getContent());
        }

        model.setTitle("【云智全课通】" + educationInforBean.getTitle());
        String url = new StringBuilder(Constants.URL)
                .append("/eduInformation/eduInformationDetails.do?ecirclesId=")
                .append(campuNewsId).toString();
        model.setUrl(url);

        SharePopWindow share = new SharePopWindow(EducationDetailsActivity.this, model);
        share.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    // 添加评论或回复评论
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void handleAddComments(final int i, final boolean isAt,
                                   final int arg2) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 方法一
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
        // 在底部显示 被点击的控件的
        window.showAtLocation(parentView.findViewById(R.id.news_comment_iv),
                Gravity.BOTTOM, 0, 0);
        // 这里检验popWindow里的button是否可以点击
        final Button first = (Button) view.findViewById(R.id.pop_btn);
        final EditText ed = (EditText) view.findViewById(R.id.ed);
        if (isAt) {
            ed.setHint("回复" + educationCommentBeanList.get(arg2).getComment_user_name() + "评论...");
        } else {
            ed.setHint("评论...");
        }
        first.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed.getText())) {
                    return;
                }
                if (!DataUtil
                        .isNetworkAvailable(EducationDetailsActivity.this)) {
                    DataUtil.getToast(getResources().getString(
                            R.string.no_network));
                    return;
                }
                // 是否在上一个请求中
                if (!isLoading()) {
                    try {
                        JSONObject json = new JSONObject();
                        if (isAt) {
                            // 回复评论
                            json.put("commentType", 1);
                            json.put("pointCommentId",
                                    educationCommentBeanList.get(arg2)
                                            .getId());// 坑爹啊 这个字段是这条评论的id 也是醉了
                        } else {
                            // 评论
                            json.put("commentType", 0);
                        }
                        json.put("campuNewsId", campuNewsId);
                        json.put("userId", userid);//commentUserName
                        json.put("commentUserName", commentUserName);
                        json.put("userType", userType);
                        json.put("commentContent", ed.getText().toString()
                                .trim());
                        System.out.println("===评论huo回复评论入参===" + json);
                        String url = new StringBuilder(Constants.SERVER_URL)
                                .append(Constants.SEND_SCHOOL_RCIRCLES_COMMENT)
                                .toString();
                        String result = HttpHelper.httpPostJson(
                                EducationDetailsActivity.this, url, json);
                        System.out.println("===评论huo回复评论出参==" + result);
                        NewBaseBean ret = JsonUtils.fromJson(result,
                                NewBaseBean.class);
                        if (ret.getServerResult().getResultCode() != 200) {// 请求失败
                            DataUtil.getToast(ret.getServerResult()
                                    .getResultMessage());
                        } else {

                            DataUtil.getToast("评论提交成功，等待审核");
//                            showComment(result, ed, isAt, arg2);
                        }
                    } catch (Exception e) {

                    }
                }
                window.dismiss();
//				initDatas();
            }

        });

        // popWindow消失监听方法
        window.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });

        first.setClickable(false);
        first.setBackground(getResources().getDrawable(R.drawable.only_login_selector_no));
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 99) {
                    DataUtil.getToast("评论或回复内容不能超过100个字!");
                }

                if (s.length() > 0) {
                    first.setClickable(true);//@drawable/only_login_selector
                    first.setBackground(getResources().getDrawable(R.drawable.popwindow_selector));
                } else {
                    first.setClickable(false);
                    first.setBackground(getResources().getDrawable(R.drawable.only_login_selector_no));
                }
            }
        });
    }

    /**
     * 显示评论
     * <p>
     * 最新的评论标准是需要审核之后 才可以显示评论
     *
     * @param result
     * @param ed
     * @param isAt
     * @param arg2
     * @throws JSONException
     */
    private void showComment(String result, EditText ed, boolean isAt, int arg2) throws JSONException {
        JSONObject jsonB = new JSONObject(result);
        String ecirclesCommentId = jsonB.getString("ecirclesCommentId");
        EducationCommentBean educationCommentBean = new EducationCommentBean();
        educationCommentBean.setId(ecirclesCommentId);
        educationCommentBean.setContent(ed.getText().toString().trim());
        educationCommentBean.setComment_user_id(userid);
        educationCommentBean.setComment_user_name(commentUserName);
        if (isAt) {
            educationCommentBean.setPoint_comment_id(educationCommentBeanList.get(arg2).getComment_user_id());
            educationCommentBean.setPoint_comment_user(educationCommentBeanList.get(arg2).getComment_user_name());
            educationCommentBean.setComment_type("1");
        } else {
            educationCommentBean.setComment_type("0");
        }
        if (educationCommentBeanList.size() < 30) {
            educationCommentBeanList.add(educationCommentBean);
            educationCommentsAdapter.notifyDataSetChanged();
        }
        String commentNum = news_commentNum.getText().toString().trim();
        int commentNumInt = 0;
        if (!DataUtil.isNullorEmpty(commentNum)) {
            commentNumInt = Integer.parseInt(commentNum);
            commentNumInt += 1;
        }
        news_commentNum.setText(commentNumInt + "");
    }


    // 显示popupWindow 删除
    private void handleDelete(final int i, final int arg2, final int q) {
        // 利用layoutInflater获得View
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
        window.showAtLocation(parentView.findViewById(R.id.news_comment_iv),
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
                if (!DataUtil
                        .isNetworkAvailable(EducationDetailsActivity.this)) {
                    DataUtil.getToast(getResources().getString(
                            R.string.no_network));
                    return;
                }
                try {

                    JSONObject json = new JSONObject();//
                    json.put("commentId", educationCommentBeanList
                            .get(arg2).getId());
                    json.put("campuNewsId", campuNewsId);

                    String url = new StringBuilder(Constants.SERVER_URL)
                            .append(Constants.DELETE_SCHOOL_RCIRCLES_COMMENT)
                            .toString();
                    String result = HttpHelper.httpPostJson(
                            EducationDetailsActivity.this, url, json);
                    System.out.println("==删除评论出参result===" + result);
                    NewBaseBean ret = JsonUtils.fromJson(result,
                            NewBaseBean.class);
                    if (ret.getServerResult().getResultCode() != 200) {// 请求失败
                        DataUtil.getToast(ret.getServerResult()
                                .getResultMessage());
                    } else {

                    }
                } catch (Exception e) {

                }
                window.dismiss();
                initDatas();
            }

        });
        // popWindow消失监听方法
        window.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "    objs[i].onclick=function()  " + "    {  "
                + "        window.imagelistner.openImage(this.src);  "
                + "    }  " + "}" + "})()");
    }


    // js通信接口
    public class JavascriptInterface {
        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        //		@SuppressLint("JavascriptInterface")
        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            System.out.println(img);
            String url = img;
            System.out.println(url);
            List<PictureURL> datas = new ArrayList<PictureURL>();
            PictureURL pictureURL = new PictureURL();
            pictureURL.setPictureURL(url);
            datas.add(pictureURL);

            Intent intent = new Intent(context, ImagePagerActivity.class);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
                    (Serializable) datas);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 1);
            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("==view中的链接=url===" + url);
            Uri uri = Uri.parse(url); // url为你要链接的地址
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);

            view.removeJavascriptInterface("searchBoxJavaBridge_");
            view.removeJavascriptInterface("accessibility");
            view.removeJavascriptInterface("accessibilityTraversal");

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);

            view.removeJavascriptInterface("searchBoxJavaBridge_");
            view.removeJavascriptInterface("accessibility");
            view.removeJavascriptInterface("accessibilityTraversal");
            super.onPageStarted(view, url, favicon);
//			aa();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                goBack();
                break;
            case R.id.news_good_iv:

                if (!DataUtil.isNetworkAvailable(getBaseContext())) {
                    DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                    return;
                }
                if (!DataUtil.isFastDoubleClick()) {
                    news_goodNumInt = Integer.parseInt(news_goodNum.getText()
                            .toString().trim());
                    if (!isLoading()) {
                        if ("true".equals(hasGoodComment)) {
                            news_good_iv.setImageResource(R.drawable.new_love_no);
                            hasGoodComment = "false";
                            news_goodNum.setText((news_goodNumInt - 1) + "");
                            http_url_sendSchoolEcirclesGood("1", false);
                        } else if ("false".equals(hasGoodComment)) {
                            news_good_iv.setImageResource(R.drawable.new_love_ok);
                            hasGoodComment = "true";
                            news_goodNum.setText((news_goodNumInt + 1) + "");
                            http_url_sendSchoolEcirclesGood("0", true);
                        }
                    }
                } else {
                    DataUtil.getToast("请不要连续点击！");
                }
                // initDatas();
                break;
            case R.id.load_more:
                if (!DataUtil.isNetworkAvailable(getBaseContext())) {
                    DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                    return;
                }
                if (!isLoading()) {
                    loadDataByIndex(k);
                    k++;// 加载更多评论
                }
                break;
            default:
                break;
        }
    }

    //重新返回键
    @Override
    public void onBackPressed() {
        goBack();
        super.onBackPressed();
    }

    private void goBack() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("readNO", education_read_numb.getText().toString());
        setResult(RESULT_OK, intent);
        EducationDetailsActivity.this.finish();
    }

    //点赞接口
    public void http_url_sendSchoolEcirclesGood(String commentType,
                                                Boolean trueOrFalse) {
        try {
            JSONObject json = new JSONObject();
            json.put("campuNewsId", campuNewsId);
            json.put("userId", userid);
            json.put("commentType", commentType);// 0:点赞,1取消赞
            json.put("userType", userType);//commentUserName
            json.put("commentUserName", commentUserName);//commentUserName增加字段
            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.SEND_SCHOOL_RCIRCLES_LIKE).toString();//
            System.out.println("=====点赞json===" + json);
            String result = HttpHelper.httpPostJson(this, url, json);
            System.out.println("=====点赞result===" + result);
            NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
            if (ret.getServerResult().getResultCode() != 200) {// 请求失败
                DataUtil.getToast(ret.getServerResult().getResultMessage());
            } else {
            }
        } catch (Exception e) {

        }
    }

    // 加载更多评论
    public void loadDataByIndex(int k) {
        try {
            List<EducationCommentBean> educationCommentList_more = new ArrayList<EducationCommentBean>();
            JSONObject json = new JSONObject();
            json.put("campuNewsId", campuNewsId);
            json.put("userId", userid);
            json.put("userType", userType);
            json.put("imgAuthId", "MB@" + deviceId + "@" + accId);
            json.put("curPage", k);// curPage,pageSize
            json.put("pageSize", 30);
            if ("3".equals(userType)) {
                String studentId = CCApplication.getInstance().getPresentUser().getChildId();
                ;
                json.put("studentId", studentId);
//				json2.put("userType", "1");//模块数需要用1学生，2老师，3家长
            }
            System.out.println("第" + k + "页" + "===教育资讯详情入参=====" + json);
            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.GET_SCHOOL_ECIRCLES_BY_ID).toString();
            String result = HttpHelper.httpPostJson(this, url, json);
            System.out.println("第" + k + "页" + "===教育资讯详情出参=====" + result);
            NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
            if (ret.getServerResult().getResultCode() != 200) {// 请求失败
                DataUtil.getToast(ret.getServerResult().getResultMessage());
            } else {
                JSONObject json3 = new JSONObject(result);
                String commentListStr = json3.getString("commentList");
                JSONArray json2 = new JSONArray(commentListStr);
                for (int i = 0; i < json2.length(); i++) {
                    EducationCommentBean educationCommentBean = JsonUtils
                            .fromJson(json2.get(i).toString(),
                                    EducationCommentBean.class);
                    educationCommentList_more
                            .add(educationCommentBean);
                }
                educationCommentsAdapter.addItem(educationCommentList_more);
                educationCommentsAdapter.notifyDataSetChanged();//notifyDataSetChanged
            }
            if (totalPageNum > k) {
                load_more.setVisibility(View.VISIBLE);
            } else {
                load_more.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void getMessage(String data) {
    }

}
