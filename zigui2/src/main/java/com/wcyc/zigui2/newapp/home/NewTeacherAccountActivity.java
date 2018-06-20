package com.wcyc.zigui2.newapp.home;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.bean.TeacherInfoBean;
import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.LauncherInfoBean;
import com.wcyc.zigui2.newapp.fragment.AllMessageFragment;
import com.wcyc.zigui2.newapp.module.classdynamics.NewClassDynamicsActivity;
import com.wcyc.zigui2.newapp.service.ChatLoginService;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.LoginActivity;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.adapter.RoleListAdapterZgd;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.Role;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.fragment.HomeFragment;
import com.wcyc.zigui2.newapp.fragment.NewMyFragment;
import com.wcyc.zigui2.newapp.widget.SelectPicturePop;
import com.wcyc.zigui2.utils.CircleImageView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.utils.MyLog;
import com.wcyc.zigui2.utils.PhotoBitmapUtils;
import com.wcyc.zigui2.widget.CustomDialog;
import com.wcyc.zigui2.widget.MyListView;
import com.wcyc.zigui2.widget.RoundImageView;

/**
 * 账户管理界面
 * <p>
 * 老师端 个人信息页面类.
 *
 * @author yytan
 * @version 2.0
 */
public class NewTeacherAccountActivity extends BaseActivity implements
        OnClickListener, ImageUploadAsyncTaskListener ,HttpRequestAsyncTaskListener, SelectPicturePop.SelectPictureInterface {

    private TeacherInfoBean personalInfo;
    private String type = "1";
    private Button title_btn;
    //	private RoundImageView icon;
    private CircleImageView icon;
    private RelativeLayout icon_item, password_item, email_item, card_item,
            phone_item;
    private TextView emailTV, phoneTV, cardTV;
    private String name;
    public static final String TEACHER_JUMP = "3";
    private final int GET_INFO_ACTION = 1;
    private final int PUTICON_ACTION = 2;
    private final int CHECK_SERVICE_EXPIRED = 3;

    private final static String FINISH_URL = "/upload/uploadState";
    private NewBaseBean ret;
    private String imagePath = "";

    private TextView new_content, qiehuan;
    private LinearLayout title_back;

    private Button exit_btn;
    public int radiobuttonState = CustomDialog.RADIOBUTTON_NO_CHECKED;
    private CustomDialog dialog;

    private MyListView mlv_children;

    private List<Role> role = new ArrayList<Role>();
    private RoleListAdapterZgd adapter;
    private LinearLayout qiehuan_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_teacher_account);
        initView();
        initEvents();
        initDatas();
    }
    @Override
    protected void onResume() {
        checkServiceExpired();
        super.onResume();
    }
    /**
     * 初始化控件.
     */
    private void initView() {
        title_btn = (Button) findViewById(R.id.title_btn);
//		icon = (RoundImageView) findViewById(R.id.mydetail_item_icon_iv);
        icon = (CircleImageView) findViewById(R.id.mydetail_item_icon_iv);
        icon_item = (RelativeLayout) findViewById(R.id.teacher_mydeetail_item_icon);
        emailTV = (TextView) findViewById(R.id.mydeetail_item_email_number);
        phoneTV = (TextView) findViewById(R.id.teac_mydeetail_item_phone_tv);
        password_item = (RelativeLayout) findViewById(R.id.teacher_mydeetail_item_password);
        cardTV = (TextView) findViewById(R.id.teac_mydeetail_item_card_tv);
        email_item = (RelativeLayout) findViewById(R.id.teacher_mydeetail_item_email);
        card_item = (RelativeLayout) findViewById(R.id.teac_mydeetail_item_card);
        phone_item = (RelativeLayout) findViewById(R.id.teac_mydeetail_item_phone);
        new_content = (TextView) findViewById(R.id.new_content);
        title_back = (LinearLayout) findViewById(R.id.title_back);
        exit_btn = (Button) findViewById(R.id.my_information_exit_btn);
        qiehuan_ll = (LinearLayout) findViewById(R.id.qiehuan_ll);
        mlv_children = (MyListView) findViewById(R.id.mlv_children);
        qiehuan = (TextView) findViewById(R.id.qiehuan);
    }

    /**
     * 初始化数据.
     */
    private void initDatas() {
        new_content.setText("账户管理");
        member = CCApplication.getInstance().getMemberInfo();
        UserType curUser = CCApplication.getInstance().getPresentUser();
        curUser.setIschecked(true);
        if (member.getUserTypeList() != null) {
            List<UserType> usertype = member.getUserTypeList();
            for (int i = 0; i < usertype.size(); i++) {
                String present_user_type = usertype.get(i).getUserType();

                Role role_i = new Role();
                if (present_user_type.equals("3")) {
                    role_i.setName(usertype.get(i).getChildName()
                            + usertype.get(i).getRelationTypeName());
                    role_i.setSchool(usertype.get(i).getSchoolName()
                            + usertype.get(i).getClassName());
                } else if (present_user_type.equals("2")) {
                    //防止教职工 名称为null
                    if(!DataUtil.isNullorEmpty(member.getTeacherName())){
                        role_i.setName(member.getTeacherName() + "（教职工）");
                    }else{
                        role_i.setName(usertype.get(i).getTeacherName() + "（教职工）");
                    }
                    role_i.setSchool(usertype.get(i).getSchoolName());
                }
//				//不显示子贵学苑的身份
//				if(!Constants.ZIGUI_SCHOOL_ID.equals(usertype.get(i).getSchoolId())) {
                role.add(role_i);
//				}
            }
            adapter = new RoleListAdapterZgd(this, role);
            int select = CCApplication.getInstance().getPresentUserIndex();
            adapter.selectdItem(select);
            mlv_children.setAdapter(adapter);
            qiehuan.setVisibility(View.VISIBLE);
            // if(role.size()>4){
            //
            // //dp转px是乘 这个方法 这里是将dp转为像素px px转dp是除
            // float scale = getResources().getDisplayMetrics().density;
            // int h=(int)(160 * scale + 0.5f);
            //
            // LayoutParams laParams=(LayoutParams)qiehuan_ll.getLayoutParams();
            // laParams.height=h;
            //
            // }

        }
        phoneTV.setText(member.getMobile());
        emailTV.setText(member.getEmail());
        if (LocalUtil.mBitMap != null) {
            icon.setImageBitmap(LocalUtil.mBitMap);
        } else {
            String file = member.getUserIconURL();
            String url = DataUtil.getDownloadURL(this, file);
            System.out.println("=url==" + url);
            getImageLoader().displayImage(url, icon, getImageLoaderOptions());
        }
    }

    /**
     * 事件控制.
     */
    private void initEvents() {
        email_item.setOnClickListener(this);
        icon_item.setOnClickListener(this);
        password_item.setOnClickListener(this);
        phone_item.setOnClickListener(this);
        // 设置返回键可见
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);
        exit_btn.setOnClickListener(this);
        mlv_children.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

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

                adapter.selectdItem(position);
                adapter.notifyDataSetChanged();
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
                            NewTeacherAccountActivity.this, url, json);
                    MemberDetailBean loginDetail = JsonUtils.fromJson(result,
                            MemberDetailBean.class);
                    loginDetail = DataUtil.sortUserList(loginDetail);
                    CCApplication.app.setMemberDetail(loginDetail);


                    //要先退出环信在登陆
//                    if (CCApplication.getInstance().getUserName() != null && CCApplication.getInstance().getPassword() != null) {
//                        try {
//                            EMChatManager.getInstance().logout();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }

                    //退出阿里百川.再登陆
                    if (CCApplication.getInstance().getUserName() != null && CCApplication.getInstance().getPassword() != null) {
                        try {
                            CCApplication.getInstance().logoutAliChat();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // 登陆百川
                    Intent intent = new Intent(
                            CCApplication.applicationContext,
                            ChatLoginService.class);

                    // 启动服务
                    startService(intent);

                    MemberDetailBean aa = CCApplication.app.getMemberDetail();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 通知activity刷新数据
                Intent broadcast = new Intent(HomeFragment.INTENT_SWITCH_USER);
                sendBroadcast(broadcast);

//                getLanucherPage();
                // getAllContact();
                CCApplication.getInstance().setAllContactList(null);
                checkServiceExpired();
                DataUtil.getToast("身份切换成功");
                ChooseTeacherActivity.teacherSelectInfo = null;//更新教师选择器选择部门

                //清空notification数据
                clearNotification();
            }
        });
    }


    public void clearNotification() {
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

    //获取 本机 dpi 类型
    public String getDpi() {
        DisplayMetrics dm = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(dm);
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

//           new HttpRequestAsyncTask(json,this,this).execute(Constants.GET_IMAGE_URL);
            new HttpRequestAsyncTask(json,this,this).execute(Constants.GET_SCHOOL_LAUNCHER_INFO);
        } catch (Exception e) {

        }

    }

    private void parseQIDong(String data) {
        System.out.println("切换账号 启动页数据" + data);
        try {
            LauncherInfoBean launcherInfoBean = JsonUtils.fromJson(data, LauncherInfoBean.class);
            if (Constants.SUCCESS_CODE == launcherInfoBean.getServerResult().getResultCode() && launcherInfoBean.getInfoSchoolStart() != null) {
                CCApplication.getInstance().setLauncherInfo(launcherInfoBean);
            } else {
                CCApplication.getInstance().setDefaultLauncherInfo();
            }
        } catch (Exception e) {
            CCApplication.getInstance().setDefaultLauncherInfo();
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
                System.out.println("checkServiceExpired:" + json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            queryPost(Constants.CHECK_SERVICE_EXPIRE, json);
            setAction(CHECK_SERVICE_EXPIRED);
        }
    }

    /**
     * 点击事件.
     *
     * @param v 视图
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teacher_mydeetail_item_icon:// 换自己头像头像点击处理
                Intent intent = new Intent(NewTeacherAccountActivity.this,
                        SelectImageActivity.class);
                intent.putExtra("limit", 1);
                intent.putExtra("is_show_checkbox", false);
                startActivityForResult(intent, 200);

//                SelectPicturePop selectPop = new SelectPicturePop(NewTeacherAccountActivity.this, NewTeacherAccountActivity.this);
//                selectPop.showAtLocation(v.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.teacher_mydeetail_item_password:// 修改密码入口
                newActivity(NewRevisePasswordActivity.class, null);
                break;
            case R.id.teacher_mydeetail_item_email:// 修改邮箱入口
                Intent intent3 = new Intent(NewTeacherAccountActivity.this,
                        NewReviseEmailActivity.class);
                startActivityForResult(intent3, 203);
                break;
            case R.id.title_btn:
                NewTeacherAccountActivity.this.finish();
                break;
            case R.id.teac_mydeetail_item_card:// 银行卡入口
                break;
            case R.id.teac_mydeetail_item_phone:// 修改手机号码
//			newActivity(null, null);
                // 启动修改手机界面
            /*
			 * 暂时屏敝此功能 Intent intent2=new Intent(NewTeacherAccountActivity.this,
			 * NewRevisePhoneActivity.class); startActivityForResult(intent2,
			 * 201);
			 */
                // 关闭账号管理界面
                // NewTeacherAccountActivity.this.finish();
//			 newActivity(NewZhanshiActivity.class, null);//展示
                // newActivity(NewConsumeActivity.class, null);//消费
                // newActivity(NewWagesActivity.class, null);//工资条
                // newActivity(NewDutyInquiryActivity.class, null);//值班查询
                // newActivity(NewMyDutyActivity.class, null);//我的值班
//			 newActivity(NewDutyRegisterActivity.class, null);//

//			 newActivity(NewBusinessProcessActivity.class, null);//我所有模块
                // newActivity(NewWagesDetailsActivity.class, null);
//			newActivity(NewMyLeaveActivity.class, null);
//			newActivity(NewMyLeaveAskActivity.class, null);
//			newActivity(NewSchoolNewsDetailsActivity.class, null);

                break;
            case R.id.title_back:// 返回
                Intent intent_back = new Intent();
                intent_back.setClass(NewTeacherAccountActivity.this,
                        NewMyFragment.class);
                // 返回
                NewTeacherAccountActivity.this.setResult(RESULT_OK, intent_back);// 返回上一个界面，并带参数
                NewTeacherAccountActivity.this.finish();
                break;
            case R.id.my_information_exit_btn:// 退出
                if (radiobuttonState == CustomDialog.RADIOBUTTON_CHECKED) {
                    radiobuttonState = CustomDialog.RADIOBUTTON_NO_CHECKED;
                }
                dialog = new CustomDialog(this, R.style.mystyle,
                        R.layout.customdialog, handler);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 200:
//                    getPhotos(data);
                      toPhotoCrop(data);
                    break;
                case 201:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            String revise_new_phone_str = bundle
                                    .getString("revise_new_phone_str");
                            phoneTV.setText(revise_new_phone_str);
                            CCApplication.getInstance().getMemberInfo()
                                    .setMobile(revise_new_phone_str);
                        }
                    }
                case 203:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            String revise_email = bundle.getString("revise_email");
                            emailTV.setText(revise_email);
                            CCApplication.getInstance().getMemberInfo()
                                    .setEmail(revise_email);

                        }
                    }
                case REQUEST_CODE_CULT:  //裁剪返回
                    getPhotos(imageUri.getPath());
                    break;
                default:
                    break;
            }
        }

    }


    /**
     * 图片裁剪回调处理.上传到服务器
     * @param photoPath tu
     */
    private void getPhotos(String photoPath) {
//        ArrayList<String> list = data.getExtras().getStringArrayList(
//                "pic_paths");
//        if (0 != list.size()) {
//            imagePath = list.get(0).toString();
            if(!TextUtils.isEmpty(photoPath)){
                ImageUploadAsyncTask imagepost = new ImageUploadAsyncTask(this,
                        Constants.PIC_TYPE, photoPath, Constants.UPLOAD_URL, this);
                imagepost.execute();
            }

//        }
    }


    /**
     * 跳转到图片裁剪
     * @param data
     */
    private void toPhotoCrop(final Intent data){
        ArrayList<String> list = data.getExtras().getStringArrayList(
                "pic_paths");
        String imPath=list.get(0).toString();
        Uri imageUri = Uri.fromFile(new File(imPath));

        startPhotoZoom(imageUri);
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    private final int REQUEST_CODE_CULT=10010;
    private Uri imageUri;
    public void startPhotoZoom(Uri uri) {
        imageUri = Uri.fromFile(new File(PhotoBitmapUtils.getPhotoFileName(NewTeacherAccountActivity.this)));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
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
     * 参数校验.
     */
    private boolean Validate() {
        return true;
    }

    /**
     * 上传头像服务器回传.
     *
     * @param data 接口字符串
     */
    private void parseFinishtData(String data) {
        DataUtil.clearDialog();
        ret = JsonUtils.fromJson(data, NewBaseBean.class);
        if (ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
            DataUtil.getToast(ret.getServerResult().getResultMessage());
        } else {
            getImageLoader().displayImage("file://" +imageUri.getPath() /*"file://" + imagePath*/, icon,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String arg0, View arg1) {

                        }

                        @Override
                        public void onLoadingFailed(String arg0, View arg1,
                                                    FailReason arg2) {
                        }

                        @Override
                        public void onLoadingComplete(String arg0, View arg1,
                                                      Bitmap bitmap) {
                            LocalUtil.mBitMap = bitmap;// 需保持头像bitmap
                            //更新本地头像 URL
                            NewMemberBean memberBean = CCApplication.getInstance().getMemberInfo();
                            memberBean.setUserIconURL("/downloadApi?fileId=" + image_fileid);
                            CCApplication.getInstance().setMember(memberBean);

                        }

                        @Override
                        public void onLoadingCancelled(String arg0, View arg1) {
                        }
                    });
            DataUtil.getToast("头像修改成功");
        }
    }

    /**
     * 得到消息
     *
     * @param data 接口数据
     */
    @Override
    protected void getMessage(String data) {
        switch (action) {
            case PUTICON_ACTION:
                parseFinishtData(data);
                break;
            case CHECK_SERVICE_EXPIRED:
                ServiceExpiredBean service = JsonUtils.fromJson(data,
                        ServiceExpiredBean.class);
                if (service.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    CCApplication.getInstance().setServiceExpiredInfo(service);
                }
                break;

            default:
                break;
        }
    }

    private String image_fileid = "";

    @Override
    public void onImageUploadComplete(String result) {
        List<String> attachment = new ArrayList<String>();
        UploadFileResult ret = JsonUtils.fromJson(result,
                UploadFileResult.class);
        if (ret != null) {
            if (ret.getSuccFiles() != null) {
                Set<String> set = ret.getSuccFiles().keySet();
                for (String string : set) {
                    attachment.add(string);
                }
            }
        }
        image_fileid = attachment.get(0);
        try {
            JSONObject json = new JSONObject(result);
            json.put("userId", CCApplication.getInstance().getPresentUser()
                    .getUserId());
            json.put("userPicFileId", attachment.get(0));
            queryPost(Constants.MODIFY_USER_INFO, json);
            action = PUTICON_ACTION;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageUploadCancelled() {
        // TODO Auto-generated method stub
        DataUtil.getToast("图片上传失败!");
    }

    /**
     * 控制CustomDialog按钮事件.
     */
    Handler handler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);
            if (0 != msg.arg1) {
                radiobuttonState = msg.arg1;
            }
            switch (msg.what) {
                case CustomDialog.DIALOG_CANCEL:// 取消退出
                    dialog.dismiss();
                    break;
                case CustomDialog.DIALOG_SURE:// 确认退出
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            CCApplication.app.logout();
                            ChooseTeacherActivity.teacherSelectInfo = null;// 初始化部门信息
                            CCApplication.app.finishAllActivity();
                            newActivity(LoginActivity.class, null);// 跳转到登陆界面
                        }
                    }).start();

                    if (CustomDialog.RADIOBUTTON_CHECKED == radiobuttonState) {
                        System.exit(0);// 停掉整个程序
                    } else if (CustomDialog.RADIOBUTTON_NO_CHECKED == radiobuttonState) {
                    }
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
    private NewMemberBean member;

    @Override
    public void onRequstComplete(String result) {
        parseQIDong(result);
    }

    @Override
    public void onRequstCancelled() {

    }

    /**
     * 拍照上传图片
     */
    @Override
    public void takePicture() {
        DataUtil.getToast("拍照上传");
    }

    /**
     * 相册选图上传图片
     */
    @Override
    public void selectPicture() {
        DataUtil.getToast("相册选图");
    }
}
