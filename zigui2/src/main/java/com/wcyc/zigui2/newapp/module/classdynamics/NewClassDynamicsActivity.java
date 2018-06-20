package com.wcyc.zigui2.newapp.module.classdynamics;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.bean.NewClassDynamicsBean;
import com.wcyc.zigui2.bean.NewClassDynamicsBean1;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.listener.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.GradeleaderBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.ModelRemindList;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewPointBean;
import com.wcyc.zigui2.newapp.bean.ModelRemindList.ModelRemind;
import com.wcyc.zigui2.newapp.bean.SubmitPictureBean;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.bean.UserType;

import com.wcyc.zigui2.newapp.widget.ChangeBackgroundPop;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.newapp.widget.RefreshListView1.OnRefreshListener;


import com.wcyc.zigui2.newapp.widget.SelectPicturePop;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;

import com.wcyc.zigui2.utils.PhotoBitmapUtils;
import com.wcyc.zigui2.widget.CircleImageViewTwo;
import com.wcyc.zigui2.widget.CustomDialog;
import com.wcyc.zigui2.widget.SpinnerButton;

/**
 * 班级动态界面
 *
 * @author 郑国栋
 * @version 2.0
 */
public class NewClassDynamicsActivity extends BaseActivity implements
        HttpRequestAsyncTaskListener, OnClickListener, SelectPicturePop.SelectPictureInterface, ImageUploadAsyncTaskListener, ChangeBackgroundPop.ChangeBackgroundInterface {

    private RefreshListView1 teacher_list;
    private SpinnerButton spinnerButton;
    private ListView spinnerListView;
    private NewClassDynamicsAdapter mClassDynamicsAdapter;
    private String userid;
    private String resource_id;
    private ArrayList<String> classNameList = new ArrayList<String>();// 接收班级名字的List
    private ArrayList<String> njmcList = new ArrayList<String>();// 接收年级名称的List
    private ArrayList<String> resourceIdList = new ArrayList<String>();// 返回ID的List
    private ArrayList<String> bzrIdList = new ArrayList<String>();// 班级ID的List
    ArrayList<String> bmList = new ArrayList<String>();// 判断班级是否有新动态红点的list
    // true表示有新动态显示红点
    private ArrayList<String> njidList = new ArrayList<String>();// 年级ID的List
    int class_i = 0;
    int class_a = -1;
    private Button loadMore;
    private int k = 2;// 请求下页班级动态的数据
    private ImageView iv_bg, title2_icon;
    private String userName, teacherId;

    private Map<String, Object> mBitmapMap = new HashMap<String, Object>();
    private List<NewClassDynamicsBean1> list = new ArrayList<NewClassDynamicsBean1>();

    private final int ACTION_GET_CLASS_LIST = 1;
    private final int ACTION_GET_CLASS_LIST_MORE = 4;
    private final int ACTION_REQUEST_CLASS_DATA = 2;
    private final int ACTION_GET_CLASS_DATA = 3;

    private static final int ACTION_UPDATE_CLASS_DYNAMIC_BACKGROUND = 5;

    private static final int ACTION_GET_PARENTS_CLASS_DATA = 6;
    // 网络断了后，提示发送失败
    public static final String INTENT_REFESH_DATA = HomeActivity.INTENT_NEW_MESSAGE;

    private LinearLayout title_back;
    private RelativeLayout title_imgbtn_add;
    private CircleImageViewTwo class_circleImageView;
    private List<NewClasses> cList;
    private String usertype;
    private CustomDialog dialog;
    private String class_id_a;
    private int scrolledX = 0;
    private int scrolledY = 0;

    private View parentView;
    private RelativeLayout rl_guide_no; //访问数
    private TextView tv_today_no; //今天访问数
    private TextView tv_sum_no; //总计访问数
    private RelativeLayout rl_background; //背景图片
    private String imagePath;
    private FrameLayout fl_header;
    private ImageView iv_background;

    private static final int REQUEST_CODE_PICK = 1000; // 相册选图标记
    private static final int REQUEST_CODE_TAKE = 2000; // 相机拍照标记
    private static final int REQUEST_CODE_CULT = 3000; // 图片裁切标记
    private static final int REQUEST_CODE_REFRESH = 4000; // 刷新数据标记

    private static final String IMAGE_FILE_NAME = "background.jpg";// 裁剪之后的图片


    private boolean hidePublish = false;
    private String fileName;
    private Uri imageUri;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        setContentView(R.layout.new_class_dynamics_mian);

        parentView = LayoutInflater.from(this).inflate(R.layout.new_class_dynamics_mian, null);
        setContentView(parentView);

        initView();
        initData();
        initEvents();

        getClassDynamicsData();

        IntentFilter mFilter = new IntentFilter(
                NewPublishDynamicActivity.INTENT_BEGIN_UPLOAD_PICTURE);
        registerReceiver(uploadReceiver, mFilter);
        IntentFilter mfinishFilter = new IntentFilter(
                NewPublishDynamicActivity.INTENT_FINISH_UPLOAD_PICTURE);
        registerReceiver(finishUploadReceiver, mfinishFilter);
        IntentFilter mrefeshDataFilter = new IntentFilter(INTENT_REFESH_DATA);
        registerReceiver(refeshDataReceiver, mrefeshDataFilter);
    }

    private void getClassDynamicsData() {
        UserType presentUser = CCApplication.getInstance().getPresentUser();
        String userType = presentUser.getUserType();
        if (Constants.TEACHER_STR_TYPE.equals(userType)) {
            requestAllClassDynamicsData();    //请求全部班级动态
        } else {
            String classId = presentUser.getClassId();
            if (DataUtil.isNullorEmpty(classId)) {
                DataUtil.getToast("获取班级动态信息失败");
            } else {
                resource_id = classId;
                class_a = Integer.valueOf(classId);
                class_id_a = String.valueOf(classId);
                updateVisitorNum(classId);
            }
        }
    }

    Handler handler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case CustomDialog.DIALOG_CANCEL:// 取消退出
                    dialog.dismiss();
                    break;
                case CustomDialog.DIALOG_SURE:// 确认开通
                    // 充值
//				Intent intent = new Intent(getApplicationContext(),
//						NewPackageSelectActivity.class);
//				startActivity(intent);
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
    private ImageView iv_title_xiala;

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case ACTION_GET_CLASS_LIST:
                dismissPd();
                parseAllClassDynamicsData(data);
                break;

            case ACTION_GET_CLASS_LIST_MORE:
                parseGetMoreClassDynamicsData(data);
                break;

            case ACTION_REQUEST_CLASS_DATA:
                break;

            case ACTION_GET_CLASS_DATA:
                dismissPd();
                parseSingleClassDynamicsData(data, resource_id);
                break;

            case ACTION_UPDATE_CLASS_DYNAMIC_BACKGROUND:
                parseUpdateBackground(data);
                break;

            case ACTION_GET_PARENTS_CLASS_DATA:
                parseParentsClassDynamicsData(data, resource_id);
                break;

            default:
                break;
        }
    }

    private boolean isClassIdExist(List<NewClasses> classList, String classId) {
        for (int i = 0; i < classList.size(); i++) {
            if (classList.get(i).getClassID().equals(classId)) {
                return true;
            }
        }

        return false;
    }

    private void initView() {
        teacher_list = (RefreshListView1) parentView.findViewById(R.id.teacher_list);
        iv_bg = (ImageView) parentView.findViewById(R.id.iv_bg);
        title_back = (LinearLayout) parentView.findViewById(R.id.title_back);
        spinnerButton = (SpinnerButton) parentView.findViewById(R.id.new_content);
        iv_title_xiala = (ImageView) parentView.findViewById(R.id.iv_title_xiala);

        title_imgbtn_add = (RelativeLayout) parentView.findViewById(R.id.title_imgbtn_add);
        class_circleImageView = (CircleImageViewTwo) parentView.findViewById(R.id.class_circleImageView);

        rl_guide_no = (RelativeLayout) parentView.findViewById(R.id.rl_guide_no);
        tv_today_no = (TextView) parentView.findViewById(R.id.tv_today_no);
        tv_sum_no = (TextView) parentView.findViewById(R.id.tv_sum_no);

        rl_background = (RelativeLayout) parentView.findViewById(R.id.rl_background);
        fl_header = (FrameLayout) parentView.findViewById(R.id.fl_header);
        iv_background = (ImageView) parentView.findViewById(R.id.iv_background);
    }

    private void initData() {

        spinnerButton.setText("班级动态");
        userid = this.getIntent().getStringExtra("userid");
        // 添加底部按钮
        View bottomView = getLayoutInflater().inflate(R.layout.score_bottom,
                null);
        loadMore = (Button) bottomView.findViewById(R.id.load);
        loadMore.setOnClickListener(new ButtonClickListener());
        title_back.setVisibility(View.VISIBLE);

        usertype = CCApplication.getInstance().getPresentUser().getUserType();

        //如果是老师
        if (Constants.TEACHER_STR_TYPE.equals(usertype)) {
            title_imgbtn_add.setVisibility(View.VISIBLE);//老师才能发布班级动态  20161114

            List<NewClasses> cList_aa = CCApplication.app.getMemberDetail().getClassList();
            if (cList_aa != null) {
                cList = new ArrayList<NewClasses>();

                for (NewClasses classes : cList_aa) {
                    if (!isClassIdExist(cList, classes.getClassID()))//是否已存在list中
                        cList.add(classes);
                }
            }

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
            System.out.println("==allowAllClassTag===" + allowAllClassTag);
            if (allowAllClassTag) {
                if (cList == null) {
                    cList = new ArrayList<NewClasses>();
                }
                List<NewClasses> schoolAllClassList = CCApplication.getInstance().getSchoolAllClassList();
                if (schoolAllClassList != null && !gradeleader) {
                    if (cList == null) {
                        cList = new ArrayList<NewClasses>();
                    }
                    cList.clear();
                    cList.addAll(schoolAllClassList);
                } else if (schoolAllClassList != null && gradeleader) {//如果是年级组长
                    try {
                        if (cList == null) {
                            cList = new ArrayList<NewClasses>();
                        }
                        List<GradeleaderBean> gradeInfoList = CCApplication.getInstance().getMemberDetail().getGradeInfoList();
                        for (int i = 0; i < gradeInfoList.size(); i++) {
//							}
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

            ArrayList<NewClasses> sampleClassList = (ArrayList<NewClasses>) cList;
            if(sampleClassList != null){
                cList = removeDuplicateData(sampleClassList);
            }

        } else if ("3".equals(usertype)) {
            title_imgbtn_add.setVisibility(View.INVISIBLE);//家长不能发布班级动态20161114
            cList = new ArrayList<NewClasses>();

            UserType presentUser = CCApplication.getInstance().getPresentUser();
            NewClasses newclass = new NewClasses();
            newclass.setClassID(presentUser.getClassId());
            newclass.setClassName(presentUser.getClassName());
            newclass.setGradeId(presentUser.getGradeId());
            newclass.setGradeName(presentUser.getGradeName());

            cList.add(newclass);
        }
    }

    //去除重复元素
    public ArrayList<NewClasses> removeDuplicateData(ArrayList<NewClasses> list) {
        ArrayList<NewClasses> newList = new ArrayList<>();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            NewClasses obj = (NewClasses) it.next();
            if (!newList.contains(obj)) {
                newList.add(obj);
            }
        }
        return newList;
    }

    private void initEvents() {
        title_back.setOnClickListener(this);
        title_imgbtn_add.setOnClickListener(this);
        spinnerButton.showAble(true);
        spinnerButton.setResIdAndViewCreatedListener(R.layout.spinner_layout,
                new SpinnerButton.ViewCreatedListener() {
                    @Override
                    public void onViewCreated(View v) {
                        spinnerListView = (ListView) v
                                .findViewById(R.id.spinner_lv);
                    }
                });
        // spinnerListView设置数据
        final MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter();
        spinnerListView.setAdapter(spinnerAdapter);
        spinnerListView.setOnItemClickListener(new OnItemClickListener() {
            private String class_id;

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                spinnerButton.dismiss();
                if (!DataUtil.isNetworkAvailable(getBaseContext())) {
                    DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                    return;
                }
                class_i = arg2;
                class_a = arg2;
                System.out.println("====class_i=====" + class_i);

                k = 2;// 重置请求的数据页面值
                // 点击后 如果这个ID是班主任ID 那么上面的显示白色图标
                // arg2 第几条数据 返回这个数据 得到一个list刷新界面
                String njmc = "", className = null;
                if (cList.size() > 0) {
                    spinnerButton.setVisibility(View.GONE);
                    iv_title_xiala.setVisibility(View.GONE);
                    if (arg2 == 0) {
                        spinnerButton.setText("全部");
                    } else {

                        njmc = cList.get(arg2 - 1).getGradeName();
                        className = cList.get(arg2 - 1).getClassName();
                        spinnerButton.setText(njmc + className);
                    }

                    spinnerButton.setVisibility(View.VISIBLE);
                    iv_title_xiala.setVisibility(View.VISIBLE);
                }

                if (arg2 == 0) {
                    requestAllClassDynamicsData();// 请求班级动态
                } else {

                    class_id = cList.get(arg2 - 1).getClassID();
                    class_id_a = cList.get(arg2 - 1).getClassID();
                    requestSingleClassDynamicsData(class_id, userid);
                }
            }
        });

        if (CCApplication.getInstance().getMemberInfo().getUserIconURL() != null) {
            if (LocalUtil.mBitMap != null) {
                class_circleImageView.setImageBitmap(LocalUtil.mBitMap);
            } else {
                String file = member.getUserIconURL();
                ImageUtils.showImage(this, file, class_circleImageView);
            }

        } else {
            if (LocalUtil.mBitMap != null) {
                class_circleImageView.setImageBitmap(LocalUtil.mBitMap);
            } else {
                String file = member.getUserIconURL();
                ImageUtils.showImage(this, file, class_circleImageView);
            }
        }
        class_circleImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if ("2".equals(usertype)) {
                    if (!DataUtil.isNetworkAvailable(NewClassDynamicsActivity.this)) {
                        DataUtil.getToast(getResources().getString(
                                R.string.no_network));
                        return;
                    }
                    Intent intent = new Intent(NewClassDynamicsActivity.this,
                            NewPersonalDynamicsActivity.class);
                    intent.putExtra("publishUserId", CCApplication.getInstance().getPresentUser().getUserId() + "");
                    intent.putExtra("publisherImgUrl", CCApplication.getInstance().getMemberInfo()
                            .getUserIconURL());
                    intent.putExtra("publisherName", CCApplication.getInstance().getMemberInfo().getUserName());// 发布人
                    if (cList != null) {
                        intent.putExtra("dynamics_class", "");//不默认第一个班级了，默认不显示
//						intent.putExtra("dynamics_class", cList.get(0).getClassName());
                    }

                    startActivityForResult(intent, REQUEST_CODE_REFRESH);
                }
                //家长不在有动态
//				else if ("3".equals(usertype)) {
//
//					intent.putExtra("dynamics_class", CCApplication.getInstance().getPresentUser().getClassName());
//				}
            }
        });
        teacher_list.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                return false;
            }
        });

        teacher_list.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {// 下拉刷新
                k = 2;//初始化  k=2
                System.out.println("=====class_a=====" + class_a);
                if (class_a <= 0) {
                    requestAllClassDynamicsData();// 请求所有班级动态
                } else {
                    requestSingleClassDynamicsData(class_id_a, userid);// 请求某班级动态
                }
                teacher_list.hideHeaderView();//收起下拉刷新
            }

            @Override
            public void onLoadingMore() {// 上拉加载更多
                if (k <= Integer.parseInt(totalPageNum_str)) {// 后一页页， 总页数// 如果后面还有数据// 则加载一页
                    loadData();
                } else {
                    DataUtil.getToast("没有更多数据!");
                }
                teacher_list.hideFooterView();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                NewClassDynamicsActivity.this.finish();
                break;
            case R.id.title_imgbtn_add:

                boolean ret = CCApplication.app.CouldFunctionBeUse("班级圈", MenuItem.CLASS_CIRCLE_NUMBER);
                ret = true;//发布班级动态是免费的

                if (ret) {
                    Intent intent = new Intent(this,
                            NewPublishDynamicActivity.class);

                    intent.putStringArrayListExtra("classIDList", resourceIdList);// 班级ID的list
                    intent.putStringArrayListExtra("njmcList", njmcList);// 年级名称
                    intent.putStringArrayListExtra("classNameList", classNameList);// 班级名称
                    intent.putStringArrayListExtra("njidList", njidList);// 年级名称

                    startActivity(intent);
                    DataUtil.hasUnfinishedTask = true;
                    DataUtil.isAlert = false;
//				Intent intentService = new Intent(this,
//						PushDynamicsService.class);
//				startService(intentService);

                } else {

                    dialog = new CustomDialog(this, R.style.mystyle,
                            R.layout.customdialog, handler);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    dialog.setTitle(getResources().getString(R.string.vip));
                    dialog.setContent(getResources().getString(R.string.vip_tip));

                }

                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(uploadReceiver);
        unregisterReceiver(finishUploadReceiver);
        // unregisterReceiver(cancelUploadReceiver);
        unregisterReceiver(refeshDataReceiver);
    }

    /**
     * spinnerButton 数据
     *
     * @return
     */
    class MySpinnerAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MySpinnerAdapter() {
            super();
            inflater = LayoutInflater.from(NewClassDynamicsActivity.this);
        }

        @Override
        public int getCount() {
            return classNameList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return cList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.new_class_list_name,
                    parent, false);
            TextView bzr_class_name = (TextView) convertView
                    .findViewById(R.id.class_name);
            if (position == 0) {
                bzr_class_name.setText("全部");
            } else {

                bzr_class_name.setText(cList.get(position - 1).getGradeName()
                        + cList.get(position - 1).getClassName());
            }
            return convertView;
        }
    }


    /**
     * 查看所有班级的班级动态
     */
    private void requestAllClassDynamicsData() {
        JSONObject json = null;
        try {
            if (cList == null) {
                DataUtil.getToast("您无任教班级，不能查看班级动态！");
                NewClassDynamicsActivity.this.finish();
                return;
            } else if (cList.size() == 0) {
                DataUtil.getToast("未获取到班级信息！");
                NewClassDynamicsActivity.this.finish();
                return;
            }
            ArrayList<String> mmClassIDlist = new ArrayList<String>();
            for (int i = 0; i < cList.size(); i++) {
                String mClassID = cList.get(i).getClassID();
                mmClassIDlist.add(mClassID);
            }
            NewClassDynamicsBean cd = new NewClassDynamicsBean();
            cd.setClassIdList(mmClassIDlist);
            cd.setCurPage(1);
            cd.setPageSize(10);
            cd.setIsNeedCLA("1");

            //全部班级
            cd.setHidePublish(true);

            UserType user = CCApplication.getInstance().getPresentUser();
            String userType = user.getUserType();

            if (Constants.TEACHER_STR_TYPE.equals(userType)) {
                cd.setUserType(Constants.TEACHER_STR_TYPE);
                cd.setVisitorId(user.getUserId());
                cd.setParentsName(CCApplication.getInstance().getMemberInfo().getUserName());
            } else {
                cd.setUserType(Constants.PARENT_STR_TYPE);
                cd.setVisitorId(user.getUserId());
                cd.setParentsName(user.getChildName() + user.getRelationTypeName());
            }

            Gson gson = new Gson();
            String string = gson.toJson(cd);

            json = new JSONObject(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isLoading()) {
            System.out.println("=====班级动态入参json====" + json);
            queryPost(Constants.GET_CLASS_DYNAMIC_LIST, json);
            action = ACTION_GET_CLASS_LIST;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scrolledY = getScroll_Y();
    }

    /**
     * 解析当前用户所有班级动态
     *
     * @param result
     */
    private void parseAllClassDynamicsData(String result) {
        System.out.println("===班级动态出参==" + result);

        hidePublish = true;
        rl_guide_no.setVisibility(View.GONE);
        fl_header.setOnClickListener(null);
        iv_background.setImageResource(R.drawable.bg_banjidongtai);

        try {
            ArrayList<String> tempClassNameList = new ArrayList<String>();// 班级名字list
            ArrayList<String> tempNjmcList = new ArrayList<String>();// 年级名字list
            ArrayList<String> tempResourceIdList = new ArrayList<String>();// 返回ID的List
            ArrayList<String> tempBzrIdList = new ArrayList<String>();// 班级ID的List
            ArrayList<String> tempBmList = new ArrayList<String>();
            ArrayList<String> tempnjidList = new ArrayList<String>();// 年级id
            // 得到班级名称的list
            for (int i = 0; i < cList.size(); i++) {
                String mClassName = cList.get(i).getClassName();
                tempClassNameList.add(mClassName);
            }
            for (int i = 0; i < cList.size(); i++) {
                String mGradeName = cList.get(i).getGradeName();
                tempNjmcList.add(mGradeName);
            }

            JSONObject json = new JSONObject(result); //

            totalPageNum_str = json.getString("totalPageNum");
            System.out.println("=====总页数====" + totalPageNum_str);
            if ("0".equals(totalPageNum_str)) {
                iv_bg.setVisibility(View.VISIBLE);// 显示无数据的图片
                teacher_list.setVisibility(View.GONE);//隐藏动态list
            } else if (totalPageNum_str.equals("1")) {
                loadMore.setVisibility(View.GONE);
                iv_bg.setVisibility(View.GONE);// 隐藏无数据的图片
                teacher_list.setVisibility(View.VISIBLE);//显示动态
            } else {
                loadMore.setVisibility(View.VISIBLE);
                iv_bg.setVisibility(View.GONE);// 隐藏无数据的图片
                teacher_list.setVisibility(View.VISIBLE);//显示动态
            }

            String classList = json.getString("interactionList");

            JSONArray json2 = new JSONArray(classList);
            for (int i = 0; i < json2.length(); i++) {
                JSONObject jo = (JSONObject) json2.get(i);
                String resourceId = jo.get("id") + "";
                tempResourceIdList.add(resourceId);

                String bzrId = jo.get("classId") + "";
                tempBzrIdList.add(bzrId);

                String tempnjid = jo.get("gradeId") + "";
                tempnjidList.add(tempnjid);
                // // bm 新加 判断是不是有新动态 true就显示红点 表示有新动态
                // String bm = (String) jo.get("bm");
                // tempBmList.add(bm);
            }
            classNameList = tempClassNameList;
            resourceIdList = tempResourceIdList;
            bmList = tempBmList;
            njmcList = tempNjmcList;
            njidList = tempnjidList;

            if (!classNameList.isEmpty()) {
                String njmc = "", className = null, detail = null;
                if (njmcList != null && njmcList.size() > 0)
                    njmc = njmcList.get(0);
                if (classNameList != null)
                    className = classNameList.get(0);
                detail = njmc + className;

                JSONArray ja = json.getJSONArray("interactionList");

                Gson gson = new Gson();
                Type t = new TypeToken<List<NewClassDynamicsBean1>>() {
                }.getType();
                list = gson.fromJson(ja.toString(), t);

                if (list != null && list.size() > 0) {
                    if (resourceIdList != null && resourceIdList.size() > 0) {
                        resource_id = resourceIdList.get(class_i);
                    }

                    //附件
                    if (list.get(class_i).getAttachmentInfoList_new() == null) {
                        list.get(class_i).setAttachmentInfoList_new(new ArrayList<NewClassDynamicsBean1.ClassAttachmentBean>());
                    }

                    //评论
                    if (list.get(class_i).getCommentList() == null) {
                        list.get(class_i).setCommentList(new ArrayList<NewPointBean>());
                    }

                    //点赞
                    if (list.get(class_i).getLoveList() == null) {
                        list.get(class_i).setLoveList(new ArrayList<NewPointBean>());
                    }

                    mClassDynamicsAdapter = new NewClassDynamicsAdapter(
                            NewClassDynamicsActivity.this, list, userid,
                            resource_id, CCApplication.app.getUserName(), parentView, hidePublish);

                    teacher_list.setAdapter(mClassDynamicsAdapter);


//					teacher_list.setSelection(6);定位到adapter的某一行
//					mClassDynamicsAdapter.notifyDataSetInvalidated();

                    if (list != null) {
                        userid = CCApplication.getInstance().getPresentUser()
                                .getUserId();

                        ModelRemindList remindList = null;
                        List<ModelRemind> remind = null;
                        JSONObject json_3 = new JSONObject();
                        UserType user = CCApplication.getInstance().getPresentUser();
                        if (user != null) {
                            try {
                                json_3.put("userId", user.getUserId());
                                json_3.put("userType", user.getUserType());
                                if (CCApplication.getInstance().isCurUserParent()) {
                                    json_3.put("studentId", user.getChildId());
                                }
                                json_3.put("schoolId", user.getSchoolId());

                                String url_3 = new StringBuilder(Constants.SERVER_URL)
                                        .append(Constants.GET_MODEL_REMIND).toString();

                                System.out.println("===获取模块记录数入参==" + json_3);
                                String result_3 = HttpHelper.httpPostJson(this, url_3,
                                        json_3);
                                remindList = JsonUtils.fromJson(result_3, ModelRemindList.class);
                                remind = remindList.getMessageList();
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                        String newClassDynamicsNumb = "";
                        if (remind != null) {
                            for (int i = 0; i < remind.size(); i++) {
                                String remindType = remind.get(i).getType();
                                if ("10".equals(remindType)) {
                                    newClassDynamicsNumb = remind.get(i).getCount();
                                }
                            }
                        }
                        if (!DataUtil.isNullorEmpty(newClassDynamicsNumb)) {
                            JSONObject json_a = new JSONObject();
                            json_a.put("userId", userid);
                            json_a.put("userType", usertype);

                            if ("3".equals(usertype)) {
                                String studentId = CCApplication.getInstance()
                                        .getPresentUser().getChildId();
                                json_a.put("studentId", studentId);
                            }

                            json_a.put("modelType", "10");

                            String url_a = new StringBuilder(Constants.SERVER_URL)
                                    .append(Constants.DEL_MODEL_REMIND_ZGD).toString();
                            String result_a = HttpHelper.httpPostJson(this, url_a,
                                    json_a);
                            // json对象 里面有属性ServerResult 请求结果
                            NewBaseBean bb = JsonUtils.fromJson(result_a,
                                    NewBaseBean.class);
                            if (bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                                System.out.println("===模块最新动态已删除===");

                                for (int i = 0; i < remind.size(); i++) {
                                    String remindType = remind.get(i).getType();
                                    if ("10".equals(remindType)) {
                                        CCApplication.getInstance().getModelRemindList().getMessageList().remove(i);
                                    }
                                }

                                Intent broadcast = new Intent(
                                        HomeActivity.INTENT_HIDE_RED_POINT);
                                sendBroadcast(broadcast);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前用户所有班级动态列表  加载更多
     *
     * @param result
     */
    private void parseGetMoreClassDynamicsData(String result) {
        try {

            List<NewClassDynamicsBean1> list1 = null;
            JSONObject obj = new JSONObject(result);
            // 总页数
            int totalPageNum = obj.getInt("totalPageNum");
            loadMore.setVisibility(View.GONE);
            JSONArray ja = obj.getJSONArray("interactionList");
            Gson gson1 = new Gson();
            Type t = new TypeToken<List<NewClassDynamicsBean1>>() {
            }.getType();
            list1 = gson1.fromJson(ja.toString(), t);
            mClassDynamicsAdapter.addItem(list1);
            mClassDynamicsAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个班级的班级动态
     *
     * @param classid ,userid
     */
    private void requestSingleClassDynamicsData(String classid, String userid) {
        JSONObject json = null;
        try {
            ArrayList<String> mmClassIDlist = new ArrayList<String>();
            mmClassIDlist.add(classid);
            NewClassDynamicsBean cd = new NewClassDynamicsBean();
            cd.setClassIdList(mmClassIDlist);
            cd.setIsNeedCLA("1");
            cd.setType("0");
            cd.setCurPage(1);
            cd.setPageSize(10);
            //一个班级
            cd.setHidePublish(false);


            UserType user = CCApplication.getInstance().getPresentUser();
            String userType = user.getUserType();

            if (Constants.TEACHER_STR_TYPE.equals(userType)) {
                cd.setUserType(Constants.TEACHER_STR_TYPE);
                cd.setVisitorId(user.getUserId());
                cd.setParentsName(CCApplication.getInstance().getMemberInfo().getUserName());
            } else {
                cd.setUserType(Constants.PARENT_STR_TYPE);
                cd.setVisitorId(user.getUserId());
                cd.setParentsName(user.getChildName() + user.getRelationTypeName());
            }

            Gson gson = new Gson();
            String string = gson.toJson(cd);
            json = new JSONObject(string);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isLoading()) {
            queryPost(Constants.GET_CLASS_DYNAMIC_LIST, json);
        }
        action = ACTION_GET_CLASS_DATA;
    }

    /**
     * 解析当前用户某个班级动态列表
     *
     * @param result
     */
    private void parseSingleClassDynamicsData(String result, String resource_id) {

        hidePublish = false;

        try {
            JSONObject json = new JSONObject(result);

            JSONArray ja = json.getJSONArray("interactionList");

            Gson gson = new Gson();
            Type t = new TypeToken<List<NewClassDynamicsBean1>>() {
            }.getType();
            list = gson.fromJson(ja.toString(), t);

            if (list == null || list.size() < 1) {
                iv_bg.setVisibility(View.VISIBLE);// 无数据 显示这张图
                teacher_list.setVisibility(View.GONE);
            } else {
                iv_bg.setVisibility(View.GONE);// 有数据 隐藏
                teacher_list.setVisibility(View.VISIBLE);
            }

            totalPageNum_str = json.getString("totalPageNum");
            mClassDynamicsAdapter = new NewClassDynamicsAdapter(
                    NewClassDynamicsActivity.this, list, userid, resource_id,
                    CCApplication.app.getUserName(), parentView, hidePublish);
            teacher_list.setAdapter(mClassDynamicsAdapter);

            // json.getString("")
            String pageNum_i = json.getString("pageNum");
            if (Integer.parseInt(pageNum_i) > 1) {
                loadMore.setVisibility(View.VISIBLE);
            } else {
                loadMore.setVisibility(View.GONE);
            }

            int todayCount = json.getInt("todayCount");
            int visitorCount = json.getInt("visitorcount");
            rl_guide_no.setVisibility(View.VISIBLE);
            if (todayCount > 99999) {
                tv_today_no.setText("99999+");
            } else {
                tv_today_no.setText(String.valueOf(todayCount));
            }
            if (visitorCount > 99999) {
                tv_sum_no.setText("99999+");
            } else {
                tv_sum_no.setText(String.valueOf(visitorCount));
            }

            rl_guide_no.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转地址
                    if (!DataUtil.isNullorEmpty(class_id_a)) {
                        String url = new StringBuilder(Constants.URL)
                                .append("/classdynamic/classdynamicdetails.do?classId=")
                                .append(class_id_a)
                                .append("&mobileType=")
                                .append("android")
                                .toString();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        System.out.println("url:" + url);
                        newActivity(BaseWebviewActivity.class, bundle);
                    }
                }
            });

            //教职工才有权限修改背景图片
            UserType presentUser = CCApplication.getInstance().getPresentUser();
            if (Constants.TEACHER_STR_TYPE.equals(presentUser.getUserType())) {
                fl_header.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChangeBackgroundPop changePop = new ChangeBackgroundPop(NewClassDynamicsActivity.this, NewClassDynamicsActivity.this);
                        changePop.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                });
            }


            //显示背景图片
            int backgroundId = json.getInt("backgroundId");
            if (0 != backgroundId) {
                String imgAuthId = Constants.AUTHID + "@" + getDeviceID()
                        + "@" + CCApplication.app.getMemberInfo().getAccId();
                String url = new StringBuilder(Constants.URL)
                        .append("/downloadApi?fileId=")
                        .append(backgroundId)
                        .append(imgAuthId).toString();
                getImageLoader().displayImage(url, iv_background, getImageLoaderOptions());
                System.out.println("背景图片下载地址:" + url);
            } else {
                iv_background.setImageResource(R.drawable.bg_banjidongtai);
            }
        } catch (Exception e) {
            e.printStackTrace();
            iv_background.setImageResource(R.drawable.bg_banjidongtai);
        }
    }

    /**
     * loadMore 的点击事件
     */
    class ButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!DataUtil.isNetworkAvailable(getBaseContext())) {
                DataUtil.getToast("当前网络不可用，请检查您的网络设置");
                return;
            }
            loadMore.setText("数据加载中");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();// 加载数据的方法
                    mClassDynamicsAdapter.notifyDataSetChanged();
                    loadMore.setText("加载更多");
                }
            }, 1000);
        }
    }

    // //加载数据
    public void loadData() {

        if (!DataUtil.isNetworkAvailable(getBaseContext())) {
            DataUtil.getToast("当前网络不可用，请检查您的网络设置");
            return;
        }

        if (isLoading()) {
            return;
        }

        loadDataByIndex(k);
        k++;
        System.out.println("==k==" + k);
    }

    public void loadDataByIndex(int index) {
        List<NewClassDynamicsBean1> list1 = null;
        String result = null;
        JSONObject json = null;
        try {
            if (class_a <= 0) {
                ArrayList<String> mmClassIDlist = new ArrayList<String>();
                for (int i = 0; i < cList.size(); i++) {
                    String mClassID = cList.get(i).getClassID();
                    mmClassIDlist.add(mClassID);
                }
                NewClassDynamicsBean cd = new NewClassDynamicsBean();
                cd.setClassIdList(mmClassIDlist);
                cd.setCurPage(index);
                cd.setPageSize(10);
                cd.setIsNeedCLA("1");
                //全部班级动态
                cd.setHidePublish(true);

                UserType user = CCApplication.getInstance().getPresentUser();
                String userType = user.getUserType();

                if (Constants.TEACHER_STR_TYPE.equals(userType)) {
                    cd.setUserType(Constants.TEACHER_STR_TYPE);
                    cd.setVisitorId(user.getUserId());
                    cd.setParentsName(CCApplication.getInstance().getMemberInfo().getUserName());
                } else {
                    cd.setUserType(Constants.PARENT_STR_TYPE);
                    cd.setVisitorId(user.getUserId());
                    cd.setParentsName(user.getChildName() + user.getRelationTypeName());
                }

                Gson gson = new Gson();
                String string = gson.toJson(cd);
                json = new JSONObject(string);

                //改为异步
                queryPost(Constants.GET_CLASS_DYNAMIC_LIST, json);
                action = ACTION_GET_CLASS_LIST_MORE;
            } else {

                ArrayList<String> mmClassIDlist = new ArrayList<String>();
                mmClassIDlist.add(class_id_a);

                NewClassDynamicsBean cd = new NewClassDynamicsBean();
                cd.setClassIdList(mmClassIDlist);
                cd.setCurPage(index);
                cd.setPageSize(10);
                cd.setIsNeedCLA("1");
                //一个班级动态
                cd.setHidePublish(false);

                UserType user = CCApplication.getInstance().getPresentUser();
                String userType = user.getUserType();

                if (Constants.TEACHER_STR_TYPE.equals(userType)) {
                    cd.setUserType(Constants.TEACHER_STR_TYPE);
                    cd.setVisitorId(user.getUserId());
                    cd.setParentsName(CCApplication.getInstance().getMemberInfo().getUserName());
                } else {
                    cd.setUserType(Constants.PARENT_STR_TYPE);
                    cd.setVisitorId(user.getUserId());
                    cd.setParentsName(user.getChildName() + user.getRelationTypeName());
                }
                Gson gson = new Gson();
                String string = gson.toJson(cd);
                json = new JSONObject(string);

                //改为异步
                queryPost(Constants.GET_CLASS_DYNAMIC_LIST, json);
                action = ACTION_GET_CLASS_LIST_MORE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver uploadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 需要改成直接显示本地图片
            ArrayList<String> paths = intent
                    .getStringArrayListExtra("imagePaths");

            String njmc = "", className = null;
            if (njmcList != null && njmcList.size() > class_i) {
                njmc = njmcList.get(class_i);
            }
            spinnerButton.setText(njmc + classNameList.get(class_i));
            if (paths.size() == 0) {// 只有文字没有 图片
                requestSingleClassDynamicsData(resource_id, userid);
            } else {
                DataUtil.getToast("班级动态正在后台处理,请稍候");
            }
        }

    };

    private BroadcastReceiver finishUploadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            DataUtil.hasUnfinishedTask = false;
            DataUtil.isAlert = false;
            requestSingleClassDynamicsData(resource_id, userid);
            boolean is_compress = intent.getBooleanExtra("is_compress", true);
            int size = intent.getIntExtra("upload_nums", 12);
            DataUtil.cleanTempFile(is_compress, size);
        }
    };
    BroadcastReceiver cancelUploadReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            DataUtil.hasUnfinishedTask = false;
            System.out.println("intent：" + intent);
            int reason = intent.getIntExtra("reason",
                    NewPublishDynamicActivity.REASON_BACK);
            String msgID = intent.getStringExtra("msgID");
            if (reason == NewPublishDynamicActivity.REASON_CANCEL) {
                if (!DataUtil.isAlert) {
                    DataUtil.getToast("发送图片失败!", Toast.LENGTH_LONG);
                    DataUtil.isAlert = true;
                    if (!DataUtil.isNetworkAvailable(getBaseContext())) {
                        return;
                    }
                    String result = httpBusiInerfaceFinish(msgID,
                            NewPublishDynamicActivity.UPLOADING_TYPE);
                    JSONObject json;
                    try {
                        json = new JSONObject(result);
                        int ret = (Integer) json.get("resultCode");
                        if (ret != 200) {
                            DataUtil.getToast("发送图片失败!", Toast.LENGTH_LONG);
                            DataUtil.isAlert = true;
                        } else {
                            DataUtil.getToast("发送图片成功!", Toast.LENGTH_LONG);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    };

    private BroadcastReceiver refeshDataReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            // requestSingleClassDynamicsData(resource_id, userid);//某个班级动态
//			requestAllClassDynamicsData();// 所有班级数据
            //		k=2;//初始化  k=2
            boolean refreshDeleteData = intent.getBooleanExtra("RefreshDeleteData", false);
            if (refreshDeleteData) {
                if (class_a <= 0) {
                    requestAllClassDynamicsData();
                } else {
                    requestSingleClassDynamicsData(class_id_a, userid);// 请求某班级动态
                }
                showProgessBar();
            } else {
                if (intent.getStringExtra("publish") != null) {
//                    requestAllClassDynamicsData();

                }
                if (class_a <= 0) {
                    requestAllClassDynamicsData();
                    //本地刷新评论
                    loadLocalData(intent);
                } else {
                    requestSingleClassDynamicsData(class_id_a, userid);// 请求某班级动态

                }
            }


        }
    };

    /**
     * 加载本地数据__点赞 评论
     *
     * @param intent
     */
    private void loadLocalData(Intent intent) {
        int classId = intent.getIntExtra("classId", 0);
        int interactionId = intent.getIntExtra("interactionId", 0);
        int flag = intent.getIntExtra("flag", -1);
        int position = intent.getIntExtra("position", 0);
        int pointCommentId = intent.getIntExtra("pointCommentId", -1);
        int user_id = Integer.parseInt(CCApplication.getInstance().getPresentUser().getUserId());
        String username = "";
        if (CCApplication.getInstance().getPresentUser().getUserType().equals(Constants.PARENT_STR_TYPE)) {
            username = CCApplication.getInstance().getPresentUser().getChildName() + CCApplication.getInstance().getPresentUser().getRelationTypeName();
        } else if (CCApplication.getInstance().getPresentUser().getUserType().equals(Constants.TEACHER_STR_TYPE)) {
            username = CCApplication.getInstance().getPresentUser().getTeacherName();
        }

        String type = intent.getStringExtra("type");
        if (type != null) {

            if (type.equals("good")) {
                Iterator<NewClassDynamicsBean1> iterator = list.iterator();
                //本地刷新点赞
                while (iterator.hasNext()) {
                    NewClassDynamicsBean1 bean1 = iterator.next();
                    if (bean1.getId() == interactionId) { //同一个动态
                        Iterator<NewPointBean> iterator1 = bean1.getLoveList().iterator();
                        if (flag == 0) { //点赞
                            int i = 0;
                            while (iterator1.hasNext()) {
                                NewPointBean newPointBean = iterator1.next();
                                if (newPointBean.getCommentUserId() == user_id) {//包含点赞
                                    i++;

                                }
                            }
                            if (i == 0) {
                                NewPointBean newPointBean1 = new NewPointBean();
                                newPointBean1.setCommentUserId(Integer.parseInt(CCApplication.getInstance().getPresentUser().getUserId()));
                                newPointBean1.setInteractionId(interactionId);
                                newPointBean1.setCommentUserName(username);
                                bean1.getLoveList().add(newPointBean1);
                            }

                        }
                        if (flag == 2) {
                            while (iterator1.hasNext()) {
                                NewPointBean newPointBean = iterator1.next();
                                if (newPointBean.getCommentUserId() == user_id) {//包含点赞
                                    iterator1.remove();
                                }
                            }
                        }

                    }
                }
            } else if (type.equals("comment")) { //本地刷新 评论
                Iterator<NewClassDynamicsBean1> iterator = list.iterator();
                //本地刷新评论
                while (iterator.hasNext()) {
                    NewClassDynamicsBean1 bean2 = iterator.next();
                    if (bean2.getId() == interactionId) { //同一个动态
                        Iterator<NewPointBean> iterator2 = bean2.getCommentList().iterator();
                        if (flag == 3) {
                            //删除评论
                            while (iterator2.hasNext()) {
                                NewPointBean newPointBean = iterator2.next();
                                if (newPointBean.getId() == pointCommentId) {//判断添加
                                    iterator2.remove();
                                }
                            }
                        }
                        if (flag == 1) {
                            //添加评论
                            //评论
                            int i = 0;
                            while (iterator2.hasNext()) {
                                NewPointBean newPointBean = iterator2.next();
                                if (newPointBean.getId() == pointCommentId) {//判断添加
                                    i++;
                                }
                            }
                            if (i == 0) {
                                //添加评论
                                NewPointBean newPointBean1 = new NewPointBean();
                                newPointBean1.setCommentUserId(Integer.parseInt(CCApplication.getInstance().getPresentUser().getUserId()));
                                newPointBean1.setInteractionId(interactionId);
                                newPointBean1.setCommentUserName(username);
                                newPointBean1.setContent(intent.getStringExtra("textfield"));
                                newPointBean1.setId(pointCommentId);
                                bean2.getCommentList().add(newPointBean1);
                            }
                        }


                    }
                }

            }
        }

        try {
            mClassDynamicsAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    private String totalPageNum_str;

    void DownloadAllImages() {
        int num = list.size();
        List<NewClassDynamicsBean1.ClassAttachmentBean> clssAttachmentList = new ArrayList<NewClassDynamicsBean1.ClassAttachmentBean>();
        List<String> tempurls = new ArrayList<String>();
        for (int i = num - 1; i >= 0; i--) {
            clssAttachmentList = list.get(i).getAttachmentInfoList_new();
            for (int k = 0; k < clssAttachmentList.size(); k++) {
                String attachementUrl = list.get(i).getAttachmentInfoList_new().get(k).getAttachementUrl();
                tempurls.add(attachementUrl);
            }
            int length = tempurls.size();
            for (int j = 0; j < length; j++) {
                getImageLoader().loadImage(
                        Constants.BASE_URL + "/" + tempurls.get(j), mOptions,
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingCancelled(String arg0,
                                                           View arg1) {

                            }

                            @Override
                            public void onLoadingComplete(String arg0,
                                                          View arg1, Bitmap arg2) {
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

	/*
     * 业务入口
	 *
	 * 发布完成
	 *
	 * ID String 用户ID Type String 反馈内容
	 *
	 * ? 出参 参数名 参数类型 描述 code Integer 返回代码 （200 成功 201 失败）
	 */

    private String httpBusiInerfaceFinish(String ID, String Type) {
        String result = null;
        JSONObject json = new JSONObject();
        try {
            json.put("ID", ID);
            json.put("Type", Type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            result = HttpHelper.httpPostJson(Constants.UPLOAD_STATE_ZGD,
                    json);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getScroll_Y() {
        View c = teacher_list.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = teacher_list.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }


    /**
     * 拍照
     */
    @Override
    public void takePicture() {
        boolean flag = isHavePermissionToUseCamera();
        if (flag) {
//            File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME)
//            imageUri = FileUtil.getTmpUri();//The Uri to store the big bitmap

            fileName = PhotoBitmapUtils.getPhotoFileName(NewClassDynamicsActivity.this);
            File file = new File(fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Uri imageUri = Uri.fromFile(file);

            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takeIntent, REQUEST_CODE_TAKE);
        } else {
            DataUtil.getToast("没有获取摄像头权限!");
        }
    }

    /**
     * 选择相册
     */
    @Override
    public void selectPicture() {
        try {
            fileName = PhotoBitmapUtils.getPhotoFileName(NewClassDynamicsActivity.this);
            File file = new File(fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            imageUri = Uri.fromFile(file);

            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, REQUEST_CODE_PICK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param requestCode 请求码 父类BaseActivity 请求码为1的时候 表示强制升级  如果取消了 就会关闭App
     * @param resultCode  结果码
     * @param data        intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("临时TAG", "requestCode:" + requestCode + "--imageUri:" + imageUri);
        switch (requestCode) {
            case REQUEST_CODE_PICK:// 直接从相册获取
                try {
                    if (data == null) {
                        return;
                    } else {
                        startPhotoZoom(data.getData());
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;

            case REQUEST_CODE_TAKE:// 调用相机拍照
//                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                try {
                    // 得到修复后的照片路径  因为三星手机拍照截图会旋转
                    String filepath = PhotoBitmapUtils.amendRotatePhoto(fileName, NewClassDynamicsActivity.this);
                    File file = new File(filepath);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    imageUri = Uri.fromFile(file);
                    startPhotoZoom(imageUri);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    DataUtil.getToast("没有获取到照片,请重试");
                }

                break;

            case REQUEST_CODE_CULT:// 取得裁剪后的图片
                try {
                    if (data != null) {
                        setPicToView(data);
                    } else {
                        DataUtil.getToast("已取消");
                    }
                } catch (Exception e) {
                    DataUtil.getToast("裁切图片异常,请重试");
                }
                break;

            //删除个人动态页面需要刷新数据
            case REQUEST_CODE_REFRESH:
                if (resultCode == RESULT_OK) {
                    getClassDynamicsData();
                }
                break;

            default:
                break;
        }

    }

    /**
     * 图片栽剪的URI
     */
//    private Uri imageUri;


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
//        imageUri = FileUtil.getTmpUri();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 3);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 600);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("noFaceDetection", true); // no face detection

        startActivityForResult(intent, REQUEST_CODE_CULT);
    }


    /**
     * 保存裁剪之后的图片数据
     *
     * @param picData
     */
    private void setPicToView(Intent picData) {
//        Bundle extras = picData.getExtras();
//        if (extras != null) {
        // 取得SDCard图片路径做显示
//            Bitmap photo = extras.getParcelable("data");
        Bitmap photo = null;
        try {
            photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (photo == null) {
            DataUtil.getToast("图片选择出错，请重新选择");
            return;
        }

        //把bitmap存储到本地
//            String folder = Environment.getExternalStorageDirectory() + "/ZIGUI_Photos/";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String time = formatter.format(new Date());
        imagePath = PhotoBitmapUtils.ZIGUI_Photos + time + ".jpeg";

        boolean flag = false;
        try {
            flag = DataUtil.saveBitmap(photo, imagePath);
        } catch (Exception e) {
            DataUtil.getToast("没有开启存储权限!");
        }
        //保存成功
        if (flag) {
            System.out.println(imagePath);
            ImageUploadAsyncTask imageUploadAsyncTask = new ImageUploadAsyncTask(this, Constants.PIC_TYPE, imagePath, Constants.UPLOAD_URL, this);
            imageUploadAsyncTask.execute();
        } else {
            DataUtil.getToast("更换背景图片失败");
        }
//        }
    }

    @Override
    public void onImageUploadCancelled() {
        DataUtil.getToast("更换背景图片失败");
    }

    @Override
    public void onImageUploadComplete(String result) {
        if (DataUtil.isNullorEmpty(result)) {
            DataUtil.getToast("更换背景图片失败");
            return;
        }

        UploadFileResult uploadFileResult = JsonUtils.fromJson(result, UploadFileResult.class);
        if (uploadFileResult.getServerResult().getResultCode() == 0) {
            HashMap<String, String> map = uploadFileResult.getSuccFiles();
            Set<String> keySet = map.keySet();
            String picId = "";
            for (String s : keySet) {
                picId = s;
            }
            submitBackground(picId);
        }
    }

    /**
     * 更换背景图片
     *
     * @param picId
     */
    private void submitBackground(String picId) {
        JSONObject json;
        try {
            ArrayList<String> classIdList = new ArrayList<>();
            classIdList.add(class_id_a);
            SubmitPictureBean submitPictureBean = new SubmitPictureBean();
            submitPictureBean.setClassIdList(classIdList);
            submitPictureBean.setPicId(picId);
            Gson gson = new Gson();
            String string = gson.toJson(submitPictureBean);
            json = new JSONObject(string);

            if (!DataUtil.isNetworkAvailable(this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            if (!isLoading()) {
                System.out.println("替换背景图片=====" + json);
                action = ACTION_UPDATE_CLASS_DYNAMIC_BACKGROUND;
                queryPost(Constants.UPDATE_CLASS_DYNAMIC_BACKGROUND, json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseUpdateBackground(String data) {
        try {
            JSONObject jsonRoot = new JSONObject(data);
            JSONObject serverResult = jsonRoot.getJSONObject("serverResult");
            int code = serverResult.getInt("resultCode");
            if (code == Constants.SUCCESS_CODE) {
//                iv_background.setBackgroundDrawable(Drawable.createFromPath(imagePath));
                iv_background.setImageDrawable(Drawable.createFromPath(imagePath));

                PhotoBitmapUtils.deleteTempAlum();
                DataUtil.getToast("设置背景图片成功");
            } else {
                DataUtil.getToast("更换背景图片失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            DataUtil.getToast("更换背景图片失败");
        }
    }


    @Override
    public void changeBackGround() {
        SelectPicturePop selectPop = new SelectPicturePop(NewClassDynamicsActivity.this, NewClassDynamicsActivity.this);
        selectPop.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    /**
     * 获取到家长账号查看的班级动态
     *
     * @param classId 班级id
     */
    private void requestParentsSingleClassDynamicsData(String classId) {
        JSONObject json = null;
        try {
            ArrayList<String> mmClassIDlist = new ArrayList<String>();
            mmClassIDlist.add(classId);
            NewClassDynamicsBean cd = new NewClassDynamicsBean();
            cd.setClassIdList(mmClassIDlist);
            cd.setIsNeedCLA("1");
            cd.setType("0");
            cd.setCurPage(1);
            cd.setPageSize(10);
            //一个班级
            cd.setHidePublish(false);


            UserType user = CCApplication.getInstance().getPresentUser();
            String userType = user.getUserType();

            if (Constants.TEACHER_STR_TYPE.equals(userType)) {
                cd.setUserType(Constants.TEACHER_STR_TYPE);
                cd.setVisitorId(user.getUserId());
                cd.setParentsName(CCApplication.getInstance().getMemberInfo().getUserName());
            } else {
                cd.setUserType(Constants.PARENT_STR_TYPE);
                cd.setVisitorId(user.getUserId());
                cd.setParentsName(user.getChildName() + user.getRelationTypeName());
            }

            Gson gson = new Gson();
            String string = gson.toJson(cd);
            json = new JSONObject(string);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isLoading()) {
            queryPost(Constants.GET_CLASS_DYNAMIC_LIST, json);
        }
        action = ACTION_GET_PARENTS_CLASS_DATA;
    }


    /**
     * 解析家长查看的某一个班级的班级动态列表
     *
     * @param result      json数据
     * @param resource_id 班级ID
     */
    private void parseParentsClassDynamicsData(String result, String resource_id) {

        spinnerButton.setText("班级动态");
        iv_title_xiala.setVisibility(View.INVISIBLE);
        spinnerButton.setOnClickListener(null);
        hidePublish = false;

        try {
            JSONObject json = new JSONObject(result);
            JSONArray ja = json.getJSONArray("interactionList");
            Gson gson = new Gson();
            Type t = new TypeToken<List<NewClassDynamicsBean1>>() {
            }.getType();
            list = gson.fromJson(ja.toString(), t);

            if (list == null || list.size() < 1) {
                iv_bg.setVisibility(View.VISIBLE);// 无数据 显示这张图
                teacher_list.setVisibility(View.GONE);
            } else {
                iv_bg.setVisibility(View.GONE);// 有数据 隐藏
                teacher_list.setVisibility(View.VISIBLE);
            }

            totalPageNum_str = json.getString("totalPageNum");
            mClassDynamicsAdapter = new NewClassDynamicsAdapter(
                    NewClassDynamicsActivity.this, list, userid, resource_id,
                    CCApplication.app.getUserName(), parentView, hidePublish);
            teacher_list.setAdapter(mClassDynamicsAdapter);

            String pageNum_i = json.getString("pageNum");
            if (Integer.parseInt(pageNum_i) > 1) {
                loadMore.setVisibility(View.VISIBLE);
            } else {
                loadMore.setVisibility(View.GONE);
            }

            int todayCount = json.getInt("todayCount");
            int visitorCount = json.getInt("visitorcount");
            rl_guide_no.setVisibility(View.VISIBLE);
            if (todayCount > 99999) {
                tv_today_no.setText("99999+");
            } else {
                tv_today_no.setText(String.valueOf(todayCount));
            }
            if (visitorCount > 99999) {
                tv_sum_no.setText("99999+");
            } else {
                tv_sum_no.setText(String.valueOf(visitorCount));
            }

            rl_guide_no.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!DataUtil.isNullorEmpty(class_id_a)) {
                        String url = new StringBuilder(Constants.URL)
                                .append("/classdynamic/classdynamicdetails.do?classId=")
                                .append(class_id_a)
                                .append("&mobileType=")
                                .append("android")
                                .toString();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        System.out.println("url:" + url);
                        newActivity(BaseWebviewActivity.class, bundle);
                    }
                }
            });

            //教职工才有权限修改背景图片
            UserType presentUser = CCApplication.getInstance().getPresentUser();
            if (Constants.TEACHER_STR_TYPE.equals(presentUser.getUserType())) {
                fl_header.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChangeBackgroundPop changePop = new ChangeBackgroundPop(NewClassDynamicsActivity.this, NewClassDynamicsActivity.this);
                        changePop.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                });
            }


            //显示背景图片
            int backgroundId = json.getInt("backgroundId");
            if (0 != backgroundId) {
                String imgAuthId = Constants.AUTHID + "@" + getDeviceID()
                        + "@" + CCApplication.app.getMemberInfo().getAccId();
                String url = new StringBuilder(Constants.URL)
                        .append("/downloadApi?fileId=")
                        .append(backgroundId)
                        .append(imgAuthId).toString();
                getImageLoader().displayImage(url, iv_background, getImageLoaderOptions());
                System.out.println("背景图片下载地址:" + url);
            } else {
                iv_background.setImageResource(R.drawable.bg_banjidongtai);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateVisitorNum(String classId) {

        UserType user = CCApplication.getInstance().getPresentUser();
        String userType = user.getUserType();
        NewClassDynamicsBean cd = new NewClassDynamicsBean();

        if (Constants.TEACHER_STR_TYPE.equals(userType)) {
            cd.setUserType(Constants.TEACHER_STR_TYPE);
            cd.setVisitorId(user.getUserId());
            cd.setParentsName(CCApplication.getInstance().getMemberInfo().getUserName());
        } else {
            cd.setUserType(Constants.PARENT_STR_TYPE);
            cd.setVisitorId(user.getUserId());
            cd.setParentsName(user.getChildName() + user.getRelationTypeName());
        }

        ArrayList<String> classList = new ArrayList<>();
        classList.add(classId);
        cd.setHidePublish(false);
        cd.setClassIdList(classList);


        Gson gson = new Gson();
        JSONObject para = null;
        String string = gson.toJson(cd);
        try {
            para = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpRequestAsyncTask(para, this, this, false).execute(Constants.UPDATE_CLASS_DYNAMIC_VISITOR_COUNT);
    }

    @Override
    public void onRequstComplete(String result) {
        System.out.println("访问数出参" + result);
        requestParentsSingleClassDynamicsData(resource_id);
    }

    @Override
    public void onRequstCancelled() {
        System.out.println("请求取消");
        requestParentsSingleClassDynamicsData(resource_id);
    }


}
