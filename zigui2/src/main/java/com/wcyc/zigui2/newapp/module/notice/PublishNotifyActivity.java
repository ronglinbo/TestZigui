package com.wcyc.zigui2.newapp.module.notice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.wcyc.zigui2.R;


import com.wcyc.zigui2.chooseContact.ChooseStudentActivity;
import com.wcyc.zigui2.chooseContact.ChooseStudentByClassAdminActivity;
import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.adapter.AttachmentListAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AllDeptList;
import com.wcyc.zigui2.newapp.bean.AllDeptList.CommonGroup;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.GetText;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.TeacherSelectInfo;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.AllDeptList.ContactGroupMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.DepMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.GradeMap;
import com.wcyc.zigui2.newapp.bean.AllTeacherList.TeacherMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.newapp.home.NewAttendanceActivity;
import com.wcyc.zigui2.newapp.module.email.CreateEmailReq;
import com.wcyc.zigui2.newapp.widget.TestArrayAdapter;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.SlipButton;
import com.wcyc.zigui2.utils.SlipButton.OnChangedListener;
import com.wcyc.zigui2.widget.CustomDialog;
import com.wcyc.zigui2.widget.SpinnerButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 发布通知界面
 */
public class PublishNotifyActivity extends BaseActivity implements ImageUploadAsyncTaskListener, HttpRequestAsyncTaskListener {
    /**
     * cancel 取消 <p/> enter 确定
     */
    private TextView cancel, enter;
    /**
     * addST 添加学生 <p/> addTeacher 添加老师
     */
    private ImageButton addST, addTeacher;
    /**
     * addAttach 添加附件
     */
    private Button addAttach;
    /**
     * subject 主题 <p/>  content 内容
     */
    private EditText subject, content;
    /**
     * 是否短信提醒
     */
    private SlipButton messageAlert;
    /**
     * teacher 老师文本输入 <p/>  student 学生文本输入
     */
    private TextView teacher, student;
    private ListView listView;
    /**
     * 老师布局
     */
    private View view;//选择老师布局
    /**
     * 账户信息
     */
    private NewMemberBean member;
    /**
     * isChecked 是否选中<p/> isTeacherChoose 是否选择老师
     * <p/> isStudentChoose 是否选择学生 <p/> isSubjectWrite 是否书写主题
     */
    private boolean isChecked, isTeacherChoose, isStudentChoose, isSubjectWrite;
    private TeacherSelectInfo all_departs;

    private List<ContactGroupMap> contactList;
    private List<GradeMap> gradeList;
    private List<CommonGroup> commonList;
    private List<DepMap> deptList;
    private List<TeacherMap> chooseTeacherList; //选择老师
    private AllDeptList allList;//选择老师群组

    private List<ClassMap> chooseClassList;//选择学生
    private List<Student> chooseStudentList;//选择学生
    private List<NewClasses> classAdmin;//担任班主任的班级信息
    //	private String teacherName = "" ,studentName = "";
    private static final int PICK_PICTURE = 100;
    private static final int CHOOSE_TEACHER = 101;
    private static final int CHOOSE_STUDENT = 102;

    private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
    private UploadFileResult ret;
    private CustomDialog dialog;
    private int i = 0;
    private boolean isSingle = true;//是否一张张的上传图片
    private List<String> attachment = new ArrayList<String>();
    private AttachmentListAdapter adapter;
    private SpinnerButton spLeixin;
    private String getLeixin;
    private List<String> list1;
    private List<GetText.RelationShipListBean> list;
    private ListView spinnerListView;

    private boolean selectAll;
    private boolean school_leader;
    private boolean departmentcharge;

    private TeacherSelectInfo teacherSelectInfo_record = null;
    private List<TeacherMap> chooseTeacherList_record; //选择老师 记录
    //天元小学 新增布局
    private LinearLayout lv;
    private RelativeLayout tianyuan;
    private SlipButton is_synchro; //是否同步
    private boolean isChecked_tianyuan = false;
    //天元小学官网同步按钮是否存在
    private boolean isExist=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_notify);
        getSelectFromService();
        initView();
        initEvent();
        member = CCApplication.getInstance().getMemberInfo();
        Intent intent = getIntent();
//		parseIntent(intent);
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.new_content);
        title.setText("发布通知");
        //这里修改了
        spLeixin = (SpinnerButton) findViewById(R.id.sp_leixin);
        spLeixin.showAble(true);
        spLeixin.setResIdAndViewCreatedListener(R.layout.spinner_layout,
                new SpinnerButton.ViewCreatedListener() {
                    @Override
                    public void onViewCreated(View v) {
                        spinnerListView = (ListView) v.findViewById(R.id.spinner_lv);
                    }
                });

//        getSelect();

        cancel = (TextView) findViewById(R.id.title2_off);
        enter = (TextView) findViewById(R.id.title2_ok);
//		enter.setEnabled(false);
        addST = (ImageButton) findViewById(R.id.add_student_bt);
        addTeacher = (ImageButton) findViewById(R.id.add_teacher_bt);
        addAttach = (Button) findViewById(R.id.add_attach);
        messageAlert = (SlipButton) findViewById(R.id.message_alert);
        subject = (EditText) findViewById(R.id.subject);
        content = (EditText) findViewById(R.id.content);
        teacher = (TextView) findViewById(R.id.add_teacher);
        student = (TextView) findViewById(R.id.add_student);
        listView = (ListView) findViewById(R.id.attachList);
        view = findViewById(R.id.rl_add_teacher);
        lv = (LinearLayout) findViewById(R.id.lv);
        tianyuan = (RelativeLayout) findViewById(R.id.tianyuan);
        is_synchro = (SlipButton) findViewById(R.id.message_alert_tianyuan);
        if (NoticeRightControll.hasPublishNoticeToAllUserRight()) {
            view.setVisibility(View.VISIBLE);
        }
        //天元小學 同步官网按钮从后台配置获取
        isExistInterface();

    }

    private void getSelectFromService() {
        action = 1;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("parentCode", "TZLX");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queryPost(Constants.GET_TEXT, jsonObject);
    }

    //获取通知类型所选中的值
//    private void getSelect() {
//        s
//        spLeixin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String spleixin = list1.get(position);
//                if (spleixin.equals(list.get(position).getConfigName())){
//                    getLeixin=list.get(position).getConfigCode();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//    }

    private void enableButton(boolean enable) {
        enter.setClickable(enable);
        enter.setEnabled(enable);
        if (enable) {
            enter.setTextColor(getResources().getColor(R.color.blue));
        } else {
            enter.setTextColor(Color.GRAY);
        }
    }


    //必填项是否已填
    private void inputState() {
        teacher.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.length() == 0) {
                    if (isStudentChoose == false) {
                        enableButton(false);
                    }
                    isTeacherChoose = false;
                } else {
                    isTeacherChoose = true;
                    if (isSubjectWrite == true) {
                        enableButton(true);
                    }
                }
            }

        });
        student.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.length() == 0) {
                    if (isTeacherChoose == false) {
                        enableButton(false);
                    }
                    isStudentChoose = false;
                } else {
                    isStudentChoose = true;
                    if (isSubjectWrite == true) {
                        enableButton(true);
                    }
                }
            }

        });
        subject.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                if (s.length() == 0) {
                    enableButton(false);
                } else {
                    isSubjectWrite = true;
                    //可以选择老师或学生

                    if (isTeacherChoose == true
                            || isStudentChoose == true) {
                        enableButton(true);
                    }

                    if (s.length() >= 240) {
                        DataUtil.getToast("通知主题不能超过240个字");
                    }
                }
            }

        });
        content.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (s.length() >= 5000) {
                    DataUtil.getToast("通知内容不能超过5000个字");
                }
            }

        });

    }

    // 天元小学 同步官网 按钮 是否存在
    public void isExistInterface() {
        JSONObject json = new JSONObject();
        try {
            json.put("schoolId", CCApplication.getInstance().getPresentUser().getSchoolId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpRequestAsyncTask(json, this, this, true).execute(Constants.IS_EXIST_INTERFACE);
    }

    private void exitOrNot() {
        dialog = new CustomDialog(this, R.style.mystyle,
                R.layout.customdialog, handler);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setTitle("退出此次编辑?");
        dialog.setContent("");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isEdit()) {
                exitOrNot();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isEdit() {
        String title = subject.getText().toString();
        String text = content.getText().toString();
        String teacherChoosed = teacher.getText().toString();
        String studentChoosed = student.getText().toString();

        return !DataUtil.isNullorEmpty(title) || !DataUtil.isNullorEmpty(teacherChoosed)
                || !DataUtil.isNullorEmpty(text) || !DataUtil.isNullorEmpty(studentChoosed);
    }

    /**
     * 控制CustomDialog按钮事件.
     */
    Handler handler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);
            if (0 != msg.arg1) {
//				radiobuttonState = msg.arg1;
            }
            switch (msg.what) {
                case CustomDialog.DIALOG_CANCEL:// 取消退出
                    dialog.dismiss();
                    break;
                case CustomDialog.DIALOG_SURE:// 确认退出
                    setResult(RESULT_CANCELED);
                    finish();
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void initEvent() {
        enableButton(false);
        inputState();
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (isEdit()) {
                    exitOrNot();
                    return;
                }
                setResult(RESULT_CANCELED);
                finish();
            }

        });

        enter.setOnClickListener(new OnClickListener() {        //确定按钮点击事件

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String title = subject.getText().toString();
                String TeacherChoosed = teacher.getText().toString();
                String text = content.getText().toString();
                if (DataUtil.isNullorEmpty(title)) {
                    DataUtil.getToast("请输入主题");
                    return;
                }
                if (DataUtil.isNullorEmpty(text)) {
                    DataUtil.getToast("请填写通知内容");
                    return;
                }

                if (DataUtil.isNullorEmpty(getLeixin)) {
                    DataUtil.getToast("类型不允许为空");
                    return;
                }

//				if(DataUtil.isNullorEmpty(TeacherChoosed)){
//					DataUtil.getToast("请选择老师");
//					return;
//				}
                if (!DataUtil.isNetworkAvailable(PublishNotifyActivity.this)) {
                    DataUtil.getToast(getApplicationContext().getResources().getString(R.string.no_network));
                    return;
                }
                if (imagePaths.isEmpty()) {
                    publish();
                } else {
                    uploadFile();
                    //finish();
                }
            }

        });

        addST.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable("choosedStudentClass", (Serializable) chooseClassList);
                bundle.putSerializable("choosedStudentList", (Serializable) chooseStudentList);
                intent.putExtras(bundle);
                classAdmin = DataUtil.getClassAdmin();
                if (NoticeRightControll.hasPublishNoticeToAllUserRight()) {
                    intent.setClass(PublishNotifyActivity.this, ChooseStudentActivity.class);
                } else if (classAdmin != null && classAdmin.size() > 0) {
                    intent.setClass(PublishNotifyActivity.this, ChooseStudentByClassAdminActivity.class);
                }

                startActivityForResult(intent, CHOOSE_STUDENT);
            }

        });
        if (addTeacher != null) {
            addTeacher.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
//					bundle.putSerializable("common", commonList);
                    bundle.putInt("type", 1);
                    bundle.putSerializable("teacherSelectInfo_record", teacherSelectInfo_record);
                    bundle.putBoolean("selectAll", selectAll);
                    bundle.putBoolean("school_leader", school_leader);
                    bundle.putBoolean("departmentcharge", departmentcharge);
                    bundle.putSerializable("teacher", (Serializable) chooseTeacherList_record);
                    bundle.putSerializable("allDept", allList);
                    bundle.putSerializable("teacher", (Serializable) chooseTeacherList);
                    intent.putExtras(bundle);
                    intent.setClass(PublishNotifyActivity.this, ChooseTeacherActivity.class);
                    startActivityForResult(intent, CHOOSE_TEACHER);
                }

            });
        }

        addAttach.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(PublishNotifyActivity.this, SelectImageActivity.class);
                intent.putExtra("limit", Constants.MAX_ATTACH_LIST);
                intent.putStringArrayListExtra("addPic", imagePaths);
                intent.putExtra("attachmentLimit", "attachmentLimit");

                startActivityForResult(intent, PICK_PICTURE);
            }

        });

        is_synchro.SetOnChangedListener(new OnChangedListener() {
            @Override
            public void OnChanged(boolean CheckState) {
                isChecked_tianyuan = CheckState;
            }
        });

        messageAlert.SetOnChangedListener(new OnChangedListener() {

            @Override
            public void OnChanged(boolean CheckState) {
                // TODO Auto-generated method stub
                isChecked = CheckState;
            }

        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ImageView del = (ImageView) arg1.findViewById(R.id.iv_del);
                del.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        imagePaths.remove(arg2);
                        adapter.notifyDataSetChanged();
                    }

                });

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = null;
        if (data != null)
            bundle = data.getExtras();
        Log.i(Constants.TAKE_PHOTO, "requestCode:" + requestCode + " resultCode:" + resultCode + " data:" + data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_PICTURE:
                    imagePaths = bundle.getStringArrayList("pic_paths");
                    Log.i(Constants.TAKE_PHOTO, "imagePaths:" + imagePaths);
                    if (imagePaths != null) {
                        long sizeAllLong = 0;
                        for (int i = 0; i < imagePaths.size(); i++) {
                            String file = imagePaths.get(i);
                            Log.i(Constants.TAKE_PHOTO, "file:" + file);
                            long size = 0;
                            try {
                                size = DataUtil.getFileSize(new File(file));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            sizeAllLong += size;
                        }

                        if (sizeAllLong > 20 * 1024 * 1024) {//判断拍照后返回来的数据集合 大小  大于20M 则
                            imagePaths.remove(imagePaths.size() - 1);
                            DataUtil.getToast("图片总大小不能超过20M");
                        }

                    }
                    data.getBooleanExtra("is_compress", true);
                    adapter = new AttachmentListAdapter(PublishNotifyActivity.this, imagePaths);
                    listView.setAdapter(adapter);
                    break;
                case CHOOSE_TEACHER:
//				commonList = (ArrayList<Object>) bundle.getSerializable("common");
                    teacherName = "";

                    allList = (AllDeptList) bundle.getSerializable("allDept");
                    chooseTeacherList = (List<TeacherMap>) bundle.getSerializable("teacher");
                    all_departs = (TeacherSelectInfo) bundle.getSerializable("choose_list");
                    addTeacher();
                    departmentcharge = ChooseTeacherActivity.departleadr;
                    school_leader = ChooseTeacherActivity.schoolleader;
                    selectAll = ChooseTeacherActivity.isSelectAll;
                    if (chooseTeacherList.size() > 0) {
                        try {
                            chooseTeacherList_record = deepCopy(chooseTeacherList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        chooseTeacherList_record = null;
                    }
                    if (all_departs != null) {
                        try {
                            teacherSelectInfo_record = deepCopy(all_departs); //拷贝一份 作为 记录
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        addDeparts(requestCode, subordinateGroupMap, all_departs);
                    }
                    break;
                case CHOOSE_STUDENT:
                    chooseClassList = (List<ClassMap>) bundle.getSerializable("studentClass");
                    chooseStudentList = (List<Student>) bundle.getSerializable("studentList");
                    addStudent();
                    break;
            }
        }
    }

    private String teacherName = "";

    protected void addTeacher() {

        if (allList != null) {
            deptList = allList.getDepMapList();
            contactList = allList.getContactGroupMapList();
            gradeList = allList.getGradeMapList();
            commonList = allList.getCommonList();
        }

        if (deptList != null) {
            for (int i = 0; i < deptList.size(); i++) {
                DepMap dept = deptList.get(i);
                String name = dept.getDepartmentName();
                teacherName += name;
                teacherName += ",";
            }
        }
        if (contactList != null) {
            for (int i = 0; i < contactList.size(); i++) {
                ContactGroupMap contact = contactList.get(i);
                String name = contact.getName();
                teacherName += name;
                teacherName += ",";
            }
        }
        if (gradeList != null) {
            for (int i = 0; i < gradeList.size(); i++) {
                GradeMap grade = gradeList.get(i);
                String name = grade.getName();
                teacherName += name;
                teacherName += ",";
            }
        }
        if (commonList != null) {
            for (CommonGroup item : commonList) {
                teacherName += item.getName();
                teacherName += ",";
            }
        }
        if (chooseTeacherList != null) {
            for (int i = 0; i < chooseTeacherList.size(); i++) {
                String name = chooseTeacherList.get(i).getName();
                teacherName += name;
                teacherName += ",";
            }
        }
        teacher.setText(teacherName);
    }

    protected void addStudent() {
        String studentName = "";
        if (chooseClassList != null) {
            for (int i = 0; i < chooseClassList.size(); i++) {
                String name = chooseClassList.get(i).getName();
                studentName += name;
                studentName += ",";
            }
        }
        if (chooseStudentList != null) {
            for (int i = 0; i < chooseStudentList.size(); i++) {
                String name = chooseStudentList.get(i).getName();
                studentName += name;
                studentName += ",";
            }
        }

        student.setText(studentName);
    }

    protected void uploadFile() {
        if (isSingle) {
            for (String file : imagePaths) {
                ImageUploadAsyncTask upload = new ImageUploadAsyncTask(
                        this, Constants.PIC_TYPE, file, Constants.UPLOAD_URL, this);
                upload.execute();
            }
        } else {
            ImageUploadAsyncTask upload = new ImageUploadAsyncTask(
                    this, Constants.PIC_TYPE, imagePaths, Constants.UPLOAD_URL, this);
            upload.execute();
        }
    }

    protected void publish() {
        action = 2;
        UserType user = CCApplication.getInstance().getPresentUser();
        PublishNotifyReq req = new PublishNotifyReq();
        req.setNoticeType(getLeixin);
        try {
            req.setUserId(user.getUserId());
            req.setSchoolId(user.getSchoolId());
            if (chooseClassList != null) {
                List<String> classId = new ArrayList();
                for (ClassMap classMap : chooseClassList) {
                    classId.add(classMap.getId() + "");
                }
                req.setClassIdList(classId);
            }
            //选择教师
            Map<Long, String> map = new HashMap<Long, String>();
            if (chooseTeacherList != null) {

                for (TeacherMap teacher : chooseTeacherList) {
                    map.put((long) teacher.getId(), Constants.TEACHER_STR_TYPE);
                }

                req.setUserIdTypeMap(map);
            }

//选择部门

            if (selectAll) {
                List<String> deptId = new ArrayList<String>();
                deptId.add("teacherstaff");
                req.setCommonGroupList(deptId);//对了
            } else {
                //行政部门
                if (all_departs != null) {
                    List<String> deptId = new ArrayList<String>();
                    for (int i = 0; i < all_departs.getInfo().getDeparts().size(); i++) {

                        deptId.add(all_departs.getInfo().getDeparts().get(i).getId() + "");
                    }
                    req.setDepartMentIdList(deptId);
                }
                // 常用分组

                List<String> commonId = new ArrayList<String>();
                if (ChooseTeacherActivity.departleadr) {
                    commonId.add("departmentcharge");
                }
                //校级领导
                if (ChooseTeacherActivity.schoolleader) {
                    commonId.add("schoolleader");
                }
                req.setCommonGroupList(commonId);
                //添加有3级的分组
                if (subordinateGroupMap != null) {
                    req.setSubordinateGroupMap(subordinateGroupMap);
                }

            }


            //学生
            if (chooseStudentList != null) {

                for (Student student : chooseStudentList) {
                    map.put((long) student.getId(), Constants.STUDENT_STR_TYPE);
                }
                req.setUserIdTypeMap(map);
            }
            if (!isSingle) {
                if (ret != null) {
                    if (ret.getSuccFiles() != null) {
                        Set<String> set = ret.getSuccFiles().keySet();
                        for (String string : set) {
                            attachment.add(string);
                        }

                        req.setAttachmentIdList(attachment);
                    }
                }
            } else {
                req.setAttachmentIdList(attachment);
            }
            req.setMsgText(content.getText().toString());
            req.setMsgTitle(subject.getText().toString());

            req.setIsSmsRemind(isChecked ? "1" : "0");


            //判断天元按钮    1.  //存在同步官网按钮 2.校级领导和管理员 3.学校通知
            if(getLeixin!=null){
                if(isExist&&getLeixin.equals("1")&&NoticeRightControll.hasDeleteAllNoticeRight()){
                    //天元选项判断 1.先判断是否是
                    if (isChecked_tianyuan) {
                        req.setSyncFlag("1");
                    } else {
                        req.setSyncFlag("0");
                    }
                }

            }




            Gson gson = new Gson();
            String string = gson.toJson(req);
            JSONObject json = new JSONObject(string);
            System.out.println("PUBLISH_NOTICE json:" + json);
            queryPost(Constants.PUBLISH_NOTICE, json);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    protected void getMessage(String data) {
        // TODO Auto-generated method stub
        System.out.println("data:================" + data);
        switch (action) {
            case 1:
                Gson gson = new Gson();
                GetText getText = gson.fromJson(data, GetText.class);
                list = getText.getRelationShipList();
                list1 = new ArrayList<String>();
                if (list != null && list.size() > 0) {
                    for (GetText.RelationShipListBean relationShipListBean : list) {
                        list1.add(relationShipListBean.getConfigName());
                    }
                }
                if (list1 != null && list1.size() > 0) {
                    final PublishNotifyActivity.MySpinnerAdapter spinnerAdapter = new PublishNotifyActivity.MySpinnerAdapter();
                    spinnerListView.setAdapter(spinnerAdapter);//添加打气筒
                    spinnerListView.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                long arg3) {
                            spLeixin.dismiss();
                            if (!DataUtil.isNetworkAvailable(getBaseContext())) {
                                DataUtil.getToast(getResources().getString(R.string.no_network));
                                return;
                            }
                            spLeixin.setText(list1.get(arg2));
                            String leixing = spLeixin.getText().toString().trim();

                            if (leixing.equals(list.get(arg2).getConfigName())) {
                                getLeixin = list.get(arg2).getConfigCode();
                            }
                            //判断天元按钮    1.  //存在同步官网按钮 2.校级领导和管理员 3.学校通知
                            if(isExist&&getLeixin.equals("1")&&NoticeRightControll.hasDeleteAllNoticeRight()){
                                lv.setVisibility(View.VISIBLE);
                                tianyuan.setVisibility(View.VISIBLE);
                            }else {
                                lv.setVisibility(View.GONE);
                                tianyuan.setVisibility(View.GONE);
                            }



                        }

                    });
                }
                break;
            case 2:
                NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
                if (ret.getServerResult().getResultCode() != Constants.SUCCESS_CODE) {
                    DataUtil.getToast(ret.getServerResult().getResultMessage());
                } else {
                    DataUtil.getToast("发布成功!");
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onImageUploadCancelled() {
        // TODO Auto-generated method stub
        System.out.println("cancel!");
    }

    @Override
    public void onImageUploadComplete(String result) {
        // TODO Auto-generated method stub
        System.out.println("result:" + result + " i:" + i);

        ret = JsonUtils.fromJson(result, UploadFileResult.class);
        if (isSingle) {
            i++;
            if (ret != null) {
                if (ret.getSuccFiles() != null) {
                    Set<String> set = ret.getSuccFiles().keySet();
                    for (String string : set) {
                        attachment.add(string);
                    }
                }
            }
            if (i == imagePaths.size()) {
                publish();
                i = 0;
            }
        } else
            publish();
    }

    Map<String, List<String>> subordinateGroupMap = null;
    Map<String, List<String>> CCsubordinateGroupMap = null;

    protected void addDeparts(int type, Map<String, List<String>> map, TeacherSelectInfo departs) {
        String departname = "";
        if (ChooseTeacherActivity.isSelectAll) {
            departname += "全体教职工";
            departname += ",";


        }

        if (type == CHOOSE_TEACHER) {
            subordinateGroupMap = new HashMap<String, List<String>>();
        }

        //常用分组 班主任 教研组长 备课组长 年级组长
        //校级领导
        if (ChooseTeacherActivity.schoolleader) {
            departname += "校级领导";
            departname += ",";
        }
        if (ChooseTeacherActivity.departleadr) {
            departname += "部门负责人";
            departname += ",";
        }
        //部门负责人
        //班主任
        List<TeacherSelectInfo.InfoBean.GradesTeacherBean> gradesTeacherBeanList = departs.getInfo().getGradesTeacher();
        if (gradesTeacherBeanList.size() > 0) {
            List<String> list = new ArrayList<String>();
            for (TeacherSelectInfo.InfoBean.GradesTeacherBean g : gradesTeacherBeanList) {
                list.add(g.getId() + "");

            }

            if (type == CHOOSE_TEACHER) {
                subordinateGroupMap.put("sybzr@grade", list);
            }
            //数据拼接显示
            if (gradesTeacherBeanList.size() == ChooseTeacherActivity.teacherSelectInfo.getInfo().getGradesTeacher().size()) {
                //全选 则拼接班主任
                departname += "班主任";
                departname += ",";
            } else {
                for (TeacherSelectInfo.InfoBean.GradesTeacherBean g : gradesTeacherBeanList) {
                    list.add(g.getName());
                    departname += g.getName() + "(班主任)";
                    departname += ",";
                }
            }


        }


        //教研组长
        List<TeacherSelectInfo.InfoBean.GroupsLeaderBean> groupsLeaderBeanList = departs.getInfo().getGroupsLeader();
        if (groupsLeaderBeanList.size() > 0) {
            //数据存到map集合上
            List<String> list = new ArrayList<String>();
            for (TeacherSelectInfo.InfoBean.GroupsLeaderBean g : groupsLeaderBeanList) {
                list.add(g.getCode() + "");

            }

            if (type == CHOOSE_TEACHER) {
                subordinateGroupMap.put("syjyzz@grade", list);
            }
            //数据拼接显示
            if (groupsLeaderBeanList.size() == ChooseTeacherActivity.teacherSelectInfo.getInfo().getGroupsLeader().size()) {
                //全选 则拼接班主任
                departname += "教研组长";
                departname += ",";
            } else {
                for (TeacherSelectInfo.InfoBean.GroupsLeaderBean g : groupsLeaderBeanList) {
                    departname += g.getName() + "(教研组长)";
                    departname += ",";
                }
            }

        }
        //备课组长
        List<TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean> prepareLessionLeaderBeanList = departs.getInfo().getPrepareLessionLeader();
        if (prepareLessionLeaderBeanList.size() > 0) {
            //部分选
            List<String> list = new ArrayList<String>();
            for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean g : prepareLessionLeaderBeanList) {
                list.add(g.getId() + "");

            }

            if (type == CHOOSE_TEACHER) {
                subordinateGroupMap.put("sybkzz@grade", list);
            }
            if (prepareLessionLeaderBeanList.size() == ChooseTeacherActivity.teacherSelectInfo.getInfo().getPrepareLessionLeader().size()) {
                //全选 则拼接班主任
                departname += "备课组长";
                departname += ",";
            } else {
                for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean g : prepareLessionLeaderBeanList) {
                    departname += g.getName() + "(备课组长)";
                    departname += ",";
                }
            }

        }
        //年级组长
        List<TeacherSelectInfo.InfoBean.GradeGroupBean> gradeGroupBeanList = departs.getInfo().getGradeGroup();
        if (gradeGroupBeanList.size() > 0) {
            //部分选
            List<String> list = new ArrayList<String>();
            for (TeacherSelectInfo.InfoBean.GradeGroupBean g : gradeGroupBeanList) {
                list.add(g.getCode() + "");
            }

            if (type == CHOOSE_TEACHER) {
                subordinateGroupMap.put("synjzz", list);
            }
            if (gradeGroupBeanList.size() == ChooseTeacherActivity.teacherSelectInfo.getInfo().getGradeGroup().size()) {
                //全选 则拼接班主任
                departname += "年级组长";
                departname += ",";
            } else {
                for (TeacherSelectInfo.InfoBean.GradeGroupBean g : gradeGroupBeanList) {
                    departname += g.getName() + "(年级组长)";
                    departname += ",";
                }
            }
        }


        //行政机构
        List<TeacherSelectInfo.InfoBean.DepartsBean> departsBeanList = departs.getInfo().getDeparts();
        for (TeacherSelectInfo.InfoBean.DepartsBean g : departsBeanList) {
            departname += g.getDepartmentName();
            departname += ",";
        }

        //教学机构 年级/班级 教研组 备课组
        List<TeacherSelectInfo.InfoBean.ClassesBeanX> classesBeanXList = departs.getInfo().getClasses();
        if (classesBeanXList.size() > 0) {
            //数据加载进 上传的map集合
            List<String> list = new ArrayList<String>();
            for (TeacherSelectInfo.InfoBean.ClassesBeanX g : classesBeanXList) {
                for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean c : g.getClasses()) {
                    list.add(c.getId() + "");
                }
            }

            if (type == CHOOSE_TEACHER) {
                subordinateGroupMap.put("classes", list);
            }
            int choose_size = 0;
            for (TeacherSelectInfo.InfoBean.ClassesBeanX classbean : classesBeanXList) {
                choose_size++;
                for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean classbeanx : classbean.getClasses()) {
                    choose_size++;
                }

            }
            int size = 0;
            for (TeacherSelectInfo.InfoBean.ClassesBeanX classbean : ChooseTeacherActivity.teacherSelectInfo.getInfo().getClasses()) {
                size++;
                for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean classbeanx : classbean.getClasses()) {
                    size++;
                }

            }

            //负责 拼接数据 显示
            if (choose_size == size) {
                departname += "年级/班级";
                departname += ",";
            } else {// 2级页面没有全选    2种情况:1.2级项 部分选择 则拼接所有下级   2级项 全选 则拼接2级项名字
                for (TeacherSelectInfo.InfoBean.ClassesBeanX g : classesBeanXList) {//选择集合
                    for (TeacherSelectInfo.InfoBean.ClassesBeanX c : ChooseTeacherActivity.teacherSelectInfo.getInfo().getClasses()) {
                        if (g.getName().equals(c.getName())) {
                            if (g.getClasses().size() == c.getClasses().size()) { //判断是不是全选
                                //是的话 则拼接 此name
                                departname += g.getName();
                                departname += ",";
                            } else {//没全选

                                for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean classesBean : g.getClasses()) {
                                    departname += classesBean.getName();
                                    departname += ",";
                                }

                            }
                        }
                    }

                }


            }
            //全选
        }

        List<TeacherSelectInfo.InfoBean.PrepareLessionBean> prepareLessionBeanList = departs.getInfo().getPrepareLession();
        if (prepareLessionBeanList.size() > 0) {//部分
            //部分选
            List<String> list = new ArrayList<String>();
            for (TeacherSelectInfo.InfoBean.PrepareLessionBean g : prepareLessionBeanList) {
                for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX c : g.getPrepareLssions()) {
                    list.add(c.getId() + "");

                }
            }

            if (type == CHOOSE_TEACHER) {
                subordinateGroupMap.put("prepareLssion", list);
            }
            int choose_size = 0;
            for (TeacherSelectInfo.InfoBean.PrepareLessionBean classbean : prepareLessionBeanList) {
                choose_size++;
                for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX classbeanx : classbean.getPrepareLssions()) {
                    choose_size++;
                }

            }
            int size = 0;
            for (TeacherSelectInfo.InfoBean.PrepareLessionBean classbean : ChooseTeacherActivity.teacherSelectInfo.getInfo().getPrepareLession()) {
                size++;
                for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX classbeanx : classbean.getPrepareLssions()) {
                    size++;
                }

            }

            //负责 拼接数据 显示
            if (choose_size == size) {
                departname += "备课组";
                departname += ",";
            } else {// 2级页面没有全选    2种情况:1.2级项 部分选择 则拼接所有下级   2级项 全选 则拼接2级项名字
                for (TeacherSelectInfo.InfoBean.PrepareLessionBean g : prepareLessionBeanList) {//选择集合
                    for (TeacherSelectInfo.InfoBean.PrepareLessionBean c : ChooseTeacherActivity.teacherSelectInfo.getInfo().getPrepareLession()) {
                        if (g.getName().equals(c.getName())) {
                            if (g.getPrepareLssions().size() == c.getPrepareLssions().size()) { //判断是不是全选
                                //是的话 则拼接 此name
                                departname += g.getName() + "(备课组)";
                                departname += ",";
                            } else {//没全选

                                for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX classesBean : g.getPrepareLssions()) {
                                    departname += classesBean.getName() + "(" + g.getName() + ")";
                                    departname += ",";
                                }

                            }
                        }
                    }

                }


            }

        }
        List<TeacherSelectInfo.InfoBean.GroupsBean> groupsBeanList1 = departs.getInfo().getGroups();
        if (groupsBeanList1.size() > 0) {//部分
            //部分选
            List<String> list = new ArrayList<String>();
            for (TeacherSelectInfo.InfoBean.GroupsBean g : groupsBeanList1) {
                for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX c : g.getTeacherGroups()) {
                    list.add(c.getId() + "");
                }
            }

            if (type == CHOOSE_TEACHER) {
                subordinateGroupMap.put("group", list);
            }

            int choose_size = 0;
            for (TeacherSelectInfo.InfoBean.GroupsBean classbean : groupsBeanList1) {
                choose_size++;
                for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX classbeanx : classbean.getTeacherGroups()) {
                    choose_size++;
                }

            }
            int size = 0;
            for (TeacherSelectInfo.InfoBean.GroupsBean classbean : ChooseTeacherActivity.teacherSelectInfo.getInfo().getGroups()) {
                size++;
                for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX classbeanx : classbean.getTeacherGroups()) {
                    size++;
                }

            }

            //负责 拼接数据 显示
            if (choose_size == size) {
                departname += "教研组";
                departname += ",";
            } else {// 2级页面没有全选    2种情况:1.2级项 部分选择 则拼接所有下级   2级项 全选 则拼接2级项名字
                for (TeacherSelectInfo.InfoBean.GroupsBean g : groupsBeanList1) {//选择集合
                    for (TeacherSelectInfo.InfoBean.GroupsBean c : ChooseTeacherActivity.teacherSelectInfo.getInfo().getGroups()) {
                        if (g.getName().equals(c.getName())) {
                            if (g.getTeacherGroups().size() == c.getTeacherGroups().size()) { //判断是不是全选
                                //是的话 则拼接 此name
                                departname += g.getName() + "(教研组)";
                                departname += ",";
                            } else {//没全选

                                for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX classesBean : g.getTeacherGroups()) {
                                    departname += classesBean.getName() + "(" + g.getName() + ")";
                                    departname += ",";
                                }

                            }
                        }
                    }

                }


            }

        }


        Log.e("部门选择", departname);



      /*  if(!TextUtils.isEmpty(content.getText().toString())&&!TextUtils.isEmpty(subject.getText().toString())&&!TextUtils.isEmpty(tvTeacher.getText().toString())&&!TextUtils.isEmpty(tvCC.getText().toString())){
            //内容为空
            enter.setEnabled(true);
            enter.setTextColor(getResources().getColor(R.color.blue));
        }else{
            enter.setEnabled(false);
            enter.setTextColor(Color.GRAY);
        }*/

        teacher.setText(departname + teacherName);

    }

    @Override
    public void onRequstComplete(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject serverResult=jsonObject.getJSONObject("serverResult");
            String code = serverResult.getString("resultCode");
            if (code.equals("200")) {
                String exist = jsonObject.getString("isExist");
                if (exist.equals("1")) {
                    isExist=true;
                    if(getLeixin!=null){
                        //判断天元按钮    1.  //存在同步官网按钮 2.校级领导和管理员 3.学校通知
                        if(isExist&&getLeixin.equals("1")&&NoticeRightControll.hasDeleteAllNoticeRight()){
                            lv.setVisibility(View.VISIBLE);
                            tianyuan.setVisibility(View.VISIBLE);
                        }
                    }

                }
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequstCancelled() {

    }


    class MySpinnerAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MySpinnerAdapter() {
            super();
            inflater = LayoutInflater.from(PublishNotifyActivity.this);
        }

        @Override
        public int getCount() {

            return list1.size();
        }

        @Override
        public Object getItem(int position) {
            return list1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PublishNotifyActivity.MySpinnerAdapter.ViewHolder viewholder = null;
            if (convertView == null) {
                // 实例化控件
                viewholder = new PublishNotifyActivity.MySpinnerAdapter.ViewHolder();
                // 配置单个item的布局
                convertView = inflater.inflate(R.layout.new_class_list_name,
                        parent, false);//打气筒设置布局   选择班级的   一个班级 布局
                viewholder.bzr_class_name = (TextView) convertView.findViewById(R.id.class_name);//找到要显示  年级班级 的控件
                // 设置标签
                convertView.setTag(viewholder);
            } else {
                // 获得标签 如果已经实例化则用历史记录
                viewholder = (PublishNotifyActivity.MySpinnerAdapter.ViewHolder) convertView.getTag();
            }
            viewholder.bzr_class_name.setText(list1.get(position));//设置显示年级班级
            return convertView;
        }

        private class ViewHolder {
            TextView bzr_class_name;
        }
    }

    public static <T> T deepCopy(T src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        T dest = (T) in.readObject();
        return dest;
    }
}
