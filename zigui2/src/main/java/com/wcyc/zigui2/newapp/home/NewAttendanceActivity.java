package com.wcyc.zigui2.newapp.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;
//import com.wcyc.zigui2.home.ImagePagerActivity;
//import com.wcyc.zigui2.home.PutNewWorkActivity;
import com.wcyc.zigui2.listener.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.GradeleaderBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewPublishAttendanceBean;
import com.wcyc.zigui2.three.CityPicker;
import com.wcyc.zigui2.three.Cityinfo;
import com.wcyc.zigui2.three.CityPicker.OnSelectingListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;
import com.wcyc.zigui2.widget.CustomDialog;
import com.wcyc.zigui2.widget.SpinnerButton;

/**
 * 新版app考勤发布界面
 *
 * @author 郑国栋 2016-4-18
 * @version 2.0
 */
public class NewAttendanceActivity extends BaseActivity implements
        OnClickListener, ImageUploadAsyncTaskListener {
    private static final int KQ_BUSIINERFACE = 1; // 获取考勤基本信息 请求后      getmassege标记为1
    private static final int PUB_ATTENDANCE = 2; // 发布考勤 请求后      getmassege标记为2
    private TextView title2_off, new_content, title2_ok;// 取消键   标题  确定键
    private EditText ed_attendance; // 考勤编辑框
    private ImageView add_student_iv;// 添加学生
    private ArrayList<String> student_id_List_checked;//被选中的学生id
    private ArrayList<String> student_name_List_checked;// 被选中的学生name
    private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    private TextView add_student_tv;// 显示学生姓名
    private ImageView add_class_iv;// 添加班级
    private TextView first;//显示节次
    private TextView second;//显示课程
    private TextView three;//显示考勤类型
    private ImageView add_select_kq_iv;//添加考勤信息
    private NewPublishAttendanceBean attendance_bean = new NewPublishAttendanceBean();//封装发布考勤的业务类
    private SpinnerButton spinnerButton;//添加班级
    private ListView spinnerListView;//班级listview
    private List<NewClasses> cList;//任教班级
    private List<NewClasses> cList0930;//任教班级
    private List<NewClasses> cList_aa;
    private int class_i = -1;
    private List<Cityinfo> province_list = new ArrayList<Cityinfo>();// 弹出对话框 节次列表
    private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();  // 弹出对话框  课程列表
    private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>(); // 弹出对话框  考勤类型列表
    private CustomDialog dialog;
    private boolean allowAllClassTag = false;
    private boolean gradeleader = false;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.new_teacher_attendance_release);
        initView();
        initDatas();
        initEvents();
        inputState();
    }

    /*
     * 初始化
     */
    private void initView() {
        //初始化 控件
        title2_off = (TextView) findViewById(R.id.title2_off);
        new_content = (TextView) findViewById(R.id.new_content);
        title2_ok = (TextView) findViewById(R.id.title2_ok);
        ed_attendance = (EditText) findViewById(R.id.ed_attendance);
        ed_attendance_delete_icon = (ImageView) findViewById(R.id.ed_attendance_delete_icon);
        TextFilter textFilter1 = new TextFilter(ed_attendance);
//        ed_attendance.addTextChangedListener(textFilter1);// 设置不能输入空格
        textFilter1.setEditeTextClearListener(ed_attendance,
                ed_attendance_delete_icon);
        add_student_iv = (ImageView) findViewById(R.id.add_student_iv);
        add_student_tv = (TextView) findViewById(R.id.add_student_tv);
        add_class_iv = (ImageView) findViewById(R.id.add_class_iv);
        add_select_kq_iv = (ImageView) findViewById(R.id.add_select_kq_iv);
        first = (TextView) findViewById(R.id.first);
        second = (TextView) findViewById(R.id.second);
        three = (TextView) findViewById(R.id.three);
        spinnerButton = (SpinnerButton) findViewById(R.id.spinner_butt_class);
        // 初始化数组
        student_id_List_checked = new ArrayList<String>();
        student_name_List_checked = new ArrayList<String>();
    }

    /*
     * 初始化 数据
     */
    private void initDatas() {
        new_content.setText("发布考勤");
        cList = new ArrayList<NewClasses>();
        cList = CCApplication.app.getMemberDetail().getClassList();//任教班级
        if(cList!=null){
            cList0930=new ArrayList<NewClasses>();
            cList0930.addAll(cList);
        }
        for (int i = 0; i < CCApplication.getInstance().getMemberDetail().getRoleList().size(); i++) {
            String roleCode = CCApplication.getInstance().getMemberDetail().getRoleList().get(i).getRoleCode();
            System.out.println("==roleCode====" + roleCode);
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
        }
        if (allowAllClassTag) {
            List<NewClasses> schoolAllClassList = CCApplication.getInstance().getSchoolAllClassList();
            if (schoolAllClassList != null && !gradeleader) {
                if(cList==null){
                    cList=new ArrayList<NewClasses>();
                }
                cList.addAll(schoolAllClassList);
            } else if (schoolAllClassList != null && gradeleader){//如果只是年级组长
                try {
                    if (cList == null) {
                        cList = new ArrayList<NewClasses>();
                    }
                    List<GradeleaderBean> gradeInfoList = CCApplication.getInstance().getMemberDetail().getGradeInfoList();
                    for (int i = 0; i < gradeInfoList.size(); i++) {
                        String userGradeId=gradeInfoList.get(i).getGradeId();
                        for (int j = 0; j < schoolAllClassList.size(); j++){
                            String gradeId=schoolAllClassList.get(j).getGradeId();
                            if(userGradeId.equals(gradeId)){
                                cList.add(schoolAllClassList.get(j));
                            }
                        }
                    }
//					}
                } catch (Exception e) {
                }
            }
        }
        String usertype_a = CCApplication.getInstance().getPresentUser().getUserType();
        System.out.println("当前用户角色  下标=======" + usertype_a);
        if (cList == null || cList.size() < 1) {
            System.out.println("是否有班级=======没有");
            DataUtil.getToast("您无任教班级，不能发布考勤！");
            NewAttendanceActivity.this.finish();
            return;
        } else if (!Constants.TEACHER_STR_TYPE.equals(usertype_a)) {
            DataUtil.getToast("您无任教班级，不能发布考勤！");
            NewAttendanceActivity.this.finish();
        } else {/*			spinnerButton.setText(cList.get(0).getGradeName()+cList.get(0).getClassName());*/
        }
    }

    /*
     * 设置监听
     */
    private void initEvents() {
        title2_off.setOnClickListener(this);
        title2_off.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String ed_attendance_str = ed_attendance.getText().toString();
                if (!DataUtil.isNullorEmpty(ed_attendance_str)) {
                    exitOrNot();
                    return;
                }
                finish();
            }
        });
        title2_ok.setOnClickListener(this);
        title2_ok.setClickable(false);
        title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
        add_student_iv.setOnClickListener(this);
        add_select_kq_iv.setOnClickListener(this);
        //班级下拉列表
        spinnerButton.showAble(true);
        spinnerButton.setResIdAndViewCreatedListener(R.layout.spinner_layout,
                new SpinnerButton.ViewCreatedListener() {
                    @Override
                    public void onViewCreated(View v) {
                        spinnerListView = (ListView) v.findViewById(R.id.spinner_lv);
                    }
                });
        final MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter();
        spinnerListView.setAdapter(spinnerAdapter);//添加打气筒
        spinnerListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                spinnerButton.dismiss();
                if (!DataUtil.isNetworkAvailable(getBaseContext())) {
                    DataUtil.getToast(getResources().getString(R.string.no_network));
                    return;
                }
                //显示年级班级
                spinnerButton.setText(cList_aa.get(arg2).getGradeName() + cList_aa.get(arg2).getClassName());
                add_student_tv.setText("");//清空之前数据
                first.setText("");
                second.setText("");
                three.setText("");
                isSelected.clear();
                class_i = arg2;//标记选择的是哪个班
                String schoolId = CCApplication.getInstance().getPresentUser().getSchoolId();
                String gradeId = cList_aa.get(arg2).getGradeId();
                httpBusiInerface_kq_base(schoolId, gradeId);//选择班级后     获取考勤基本信息
            }

        });
    }

    private boolean isClassIdExist(List<NewClasses> classlist, String classId) {
        for (int i = 0; i < classlist.size(); i++) {
            if (classlist.get(i).getClassID().equals(classId)) {
                return true;
            }
        }
        return false;
    }

    //设置选择班级  adapter   下拉列表
    class MySpinnerAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        public MySpinnerAdapter() {
            super();
            inflater = LayoutInflater.from(NewAttendanceActivity.this);
        }
        @Override
        public int getCount() {
            if (cList != null) {
                cList_aa = new ArrayList<NewClasses>();
                for (NewClasses classes : cList) {
                    if (!isClassIdExist(cList_aa, classes.getClassID()))//是否已存在list中
                        cList_aa.add(classes);
                }
                return cList_aa.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return cList_aa.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewholder = null;
            if (convertView == null) {
                // 实例化控件
                viewholder = new ViewHolder();
                // 配置单个item的布局
                convertView = inflater.inflate(R.layout.new_class_list_name,
                        parent, false);//打气筒设置布局   选择班级的   一个班级 布局
                viewholder.bzr_class_name = (TextView) convertView.findViewById(R.id.class_name);//找到要显示  年级班级 的控件
                // 设置标签
                convertView.setTag(viewholder);
            } else {
                // 获得标签 如果已经实例化则用历史记录
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.bzr_class_name.setText(cList_aa.get(position).getGradeName() + cList_aa.get(position).getClassName());//设置显示年级班级
            return convertView;
        }
        private class ViewHolder {
            TextView bzr_class_name;
        }
    }

    @Override
    public void onImageUploadComplete(String result, String ID) {
    }

    @Override
    public void onImageUploadCancelled() {
    }

    @Override
    public void onImageUploadComplete(String result) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title2_ok://点击确定    发布考勤
                httpBusiInerface();
                break;
            case R.id.add_student_iv:// 点击添加学生
                if (class_i == -1) {
                    DataUtil.getToast("请选择班级");
                } else {
                    String class_id = cList_aa.get(class_i).getClassID();
                    System.out.println("class_id====" + class_id);
                    Intent intent = new Intent();
                    intent.putExtra("classId", class_id);
                    intent.putExtra("isSelected", isSelected);
                    intent.setClass(NewAttendanceActivity.this,
                            NewSelectStudentActivity.class);
                    startActivityForResult(intent, 2);
                }
                break;
            case R.id.add_class_iv://点击添加班级
                break;
            case R.id.add_select_kq_iv:// 点击添加考勤信息
                if (class_i == -1) {
                    DataUtil.getToast("请选择班级");
                } else {
                    add_select_kq();
                }
                break;
            default:
                break;
        }
    }

    //添加考勤信息    三联动
    private void add_select_kq() {
        //设置打气筒
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog, null);//对话框设置布局
        //对话框设置标题
        final AlertDialog ad = new AlertDialog.Builder(this).setTitle("请选择").setView(layout).show();
        //获得  节次  课程  考勤类型 布局控件
        CityPicker cityPicker = (CityPicker) layout
                .findViewById(R.id.citypicker);
        //输出 节次 课程  考勤类型
        if (province_list.size() == 0 || city_map.size() == 0 || couny_map.size() == 0
                || province_list == null || city_map == null || couny_map == null) {
            DataUtil.getToast("请后台先准备数据 考勤时段 考勤科目 考勤类别");
        } else {
            //设置  节次 课程  考勤类型   三联动
            cityPicker.setListData(province_list, city_map, couny_map);
            //三联动监听
            cityPicker.setOnSelectingListener(new OnSelectingListener() {

                @Override
                public void selectedData(String str) {
                    String[] chooseAttenInfos = str.split(",", -1);//分割数据 以逗号分割  之前是- 分割   但班级id数据库可能传-1 -2这些
                    String[] attenInfoIds = chooseAttenInfos[0].split("#", -1);//分割id
                    String[] attenInfoNames = chooseAttenInfos[1].split("#", -1);//分割name
                    if (attenInfoNames != null) {//如果选择了 节次 课程  考勤类型
                        first.setText(attenInfoNames[0]);// 显示节次
                        second.setText(attenInfoNames[1]);//显示课程
                        three.setText(attenInfoNames[2]);//显示考勤类型
						/*
						 * 发布考勤业务类  封装数据
						 */
                        attendance_bean.setCourseNo(attenInfoIds[0]);
                        attendance_bean.setCourseNoName(attenInfoNames[0]);
                        attendance_bean.setCourseName(attenInfoNames[1].trim());
                        attendance_bean.setCourseCode(attenInfoIds[1]);
                        attendance_bean.setType(attenInfoIds[2]);
                        attendance_bean.setTypeName(attenInfoNames[2]);
                    }
                    ad.dismiss();
                }

            });
        }
    }

    //从选择学生界面返回  携带数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (resultCode) {
            case RESULT_OK:// 从学生界面返回
                if (intent != null) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        // 获得从学生界面返回时携带的数据
                        student_id_List_checked = bundle
                                .getStringArrayList("student_id_List_checked");//返回被选中的学生id
                        student_name_List_checked = bundle
                                .getStringArrayList("student_name_List_checked");//返回被选中的学生name
                        isSelected = (HashMap<Integer, Boolean>) bundle
                                .getSerializable("isSelected");
                        String student_name = "";//要显示的学生名字
                        if (student_id_List_checked.size() != 0) {
                            for (String student_id_checkd : student_id_List_checked) {//迭代输出
                                System.out.println("返回的被选中学生=id=="
                                        + student_id_checkd);
                            }
                            System.out.println("student_id_List_checked====有数据");
                        } else {
                            System.out.println("student_id_List_checked======无数据");
                        }
                        if (student_name_List_checked.size() != 0) {
                            for (int i = 0; i < student_name_List_checked.size(); i++) {//迭代输出
                                student_name += student_name_List_checked.get(i);
                                student_name += ",";
                            }
                        }
                        add_student_tv.setText(student_name);//设置显示学生名字
                    }
                }
        }
    }

    //需要准备的  考勤封装内容的类
    class Kq_object_bean {
        String code;//封装的id
        String name;//封装的name
        public String getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    //节次列表   课程列表   考勤类型列表
    List<Kq_object_bean> classTimeList = new ArrayList<NewAttendanceActivity.Kq_object_bean>();
    List<Kq_object_bean> courseList = new ArrayList<NewAttendanceActivity.Kq_object_bean>();
    List<Kq_object_bean> attendTypeList = new ArrayList<NewAttendanceActivity.Kq_object_bean>();
    private ImageView ed_attendance_delete_icon;

    //返回的考勤基础信息接口 返回的 业务封装类
    class Kq_object_retrun_bean {
        List<Kq_object_bean> classTimeList;//返回的节次列表
        List<Kq_object_bean> courseList;//返回的课程列表
        List<Kq_object_bean> attendTypeList;//返回的考勤类型列表
        public List<Kq_object_bean> getClassTimeList() {
            return classTimeList;
        }
        public List<Kq_object_bean> getCourseList() {
            return courseList;
        }
        public List<Kq_object_bean> getAttendTypeList() {
            return attendTypeList;
        }
    }

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case KQ_BUSIINERFACE://1   表示  获取考勤基础信息
                try {
                    //用实体类接收  考勤基础信息接口  返回的数据
                    Kq_object_retrun_bean bean = JsonUtils.fromJson(data, Kq_object_retrun_bean.class);
                    classTimeList = bean.getClassTimeList();
                    courseList = bean.getCourseList();
                    attendTypeList = bean.getAttendTypeList();
                    if ("0".equals(cList_aa.get(class_i).getIsAdviser())) {
                        courseList.clear();
                        Kq_object_bean couseObj = new Kq_object_bean();
                        couseObj.setCode(cList_aa.get(class_i).getCouseId());
                        couseObj.setName(cList_aa.get(class_i).getCouseName());
                        courseList.add(couseObj);
                    }
//				//输出 节次code 和 name
//				//输出 课程 code 和name
//				//输出 考勤类型 code和 name				
                    //将课程列表  转为   CityPicker三联动  可封装的  List<Cityinfo>类型列表
                    List<Cityinfo> courseList_change = new ArrayList<Cityinfo>();
                    for (int i = 0; i < courseList.size(); i++) {
                        Kq_object_bean aa = courseList.get(i);//获得节次列表的 单个节次
                        if (aa.getName() != null) {
                            Cityinfo bb = new Cityinfo();
                            bb.setId(aa.getCode());//将节次 的code  转为CityPicker三联动  可封装的  List<Cityinfo>类型中Cityinfo的id
                            bb.setCity_name(aa.getName());//将节次 的code  转为CityPicker三联动  可封装的  List<Cityinfo>类型中Cityinfo的name
                            courseList_change.add(bb);//将Cityinfo对象添加到List<Cityinfo>类型的集合中
                        }
                    }
                    List<Cityinfo> courseList_change_null = new ArrayList<Cityinfo>();//如到校  放学  就寝  显示科目为无
                    Cityinfo bb_null = new Cityinfo();
                    bb_null.setCity_name(" ");
                    courseList_change_null.add(bb_null);
                    //将考勤类型列表  转为   CityPicker三联动  可封装的  List<Cityinfo>类型列表
                    List<Cityinfo> attendTypeList_change = new ArrayList<Cityinfo>();
                    for (int i = 0; i < attendTypeList.size(); i++) {
                        Kq_object_bean aa = attendTypeList.get(i);
                        Cityinfo bb = new Cityinfo();
                        bb.setId(aa.getCode());
                        bb.setCity_name(aa.getName());
                        attendTypeList_change.add(bb);
                    }
                    //将节次类型列表  转为   CityPicker三联动  可封装的  List<Cityinfo>类型列表
                    province_list.clear();//清空之前数据
                    for (int i = 0; i < classTimeList.size(); i++) {
                        Kq_object_bean aa = classTimeList.get(i);
                        Cityinfo bb = new Cityinfo();
                        bb.setId(aa.getCode());
                        bb.setCity_name(aa.getName());
                        province_list.add(bb);
                        //三联动
                        if (Integer.parseInt(aa.getCode()) > 0) {
                            city_map.put(aa.getCode(), courseList_change);
                        } else {
                            city_map.put(aa.getCode(), courseList_change_null);
                        }
                        couny_map.put(aa.getCode(), attendTypeList_change);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PUB_ATTENDANCE://发布考勤
                System.out.println("发布考勤出参 data====" + data);
                //请求结果
                NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
                if (ret.getServerResult().getResultCode() != 200) {//失败
                    DataUtil.getToast(ret.getServerResult().getResultMessage());
                } else {//成功
                    DataUtil.getToast("发布考勤成功");
//				 GoHtml5Function.goToHtmlApp(this, "考勤");//这句不要了  BaseWebView重写了 重新获得焦点效果  onResume
                    // 通知BaseWebView  activity刷新数据
                    Intent broadcast = new Intent(
                            BaseWebviewActivity.INTENT_REFESH_DATA);
                    sendBroadcast(broadcast);
                    NewAttendanceActivity.this.finish();
                }
                break;
        }
    }

    //请求 考勤基本信息
    private void httpBusiInerface_kq_base(String schoolId, String gradeId) {
        try {
            JSONObject json = new JSONObject();
            json.put("schoolId", schoolId);
            json.put("gradeId", gradeId);
            queryPost(Constants.GET_ATTEN_BASIC_INFO, json);
            action = KQ_BUSIINERFACE;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //发布考勤   封装考勤业务逻辑
    private void httpBusiInerface() {
        String ed_attendance_str = ed_attendance.getText().toString();
        if (class_i == -1) {
            DataUtil.getToast("请选择班级");
        } else if (student_id_List_checked.size() == 0) {
            DataUtil.getToast("请选择学生");
        } else if (attendance_bean.getTypeName() == null) {
            DataUtil.getToast("请选择考勤信息");
        } else if (ed_attendance_str.length() == 0) {
            DataUtil.getToast("请输入考勤内容");
        } else {
            //封装请求参数
            String class_id_a = cList_aa.get(class_i).getClassID();
            attendance_bean.setClassId(class_id_a);//封装班级id
            attendance_bean.setStudentId(student_id_List_checked);//封装学生id列表
            attendance_bean.setContent(ed_attendance_str);//封装输入的考勤内容
            Gson gson = new Gson();
            String string = gson.toJson(attendance_bean);
            JSONObject json;
            try {
                json = new JSONObject(string);
                System.out.println("发布考勤 json====" + json);
                queryPost(Constants.PUBLISH_STUDENT_ATEN, json);
                action = PUB_ATTENDANCE;//设置发布考勤   返回action标记为2
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void inputState() {
        spinnerButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String spinnerButtonStr = spinnerButton.getText().toString().trim();
                String add_student_tvStr = add_student_tv.getText().toString().trim();
                String firstStr = first.getText().toString().trim();
                String ed_attendanceStr = ed_attendance.getText().toString();
                if (spinnerButtonStr.length() > 0 && add_student_tvStr.length() > 0
                        && firstStr.length() > 0 && ed_attendanceStr.length() > 0) {
                    title2_ok.setClickable(true);
                    title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
                } else {
                    title2_ok.setClickable(false);
                    title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
                }
            }
        });
        add_student_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String spinnerButtonStr = spinnerButton.getText().toString().trim();
                String add_student_tvStr = add_student_tv.getText().toString().trim();
                String firstStr = first.getText().toString().trim();
                String ed_attendanceStr = ed_attendance.getText().toString();
                if (spinnerButtonStr.length() > 0 && add_student_tvStr.length() > 0
                        && firstStr.length() > 0 && ed_attendanceStr.length() > 0) {
                    title2_ok.setClickable(true);
                    title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
                } else {
                    title2_ok.setClickable(false);
                    title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
                }
            }
        });
        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String spinnerButtonStr = spinnerButton.getText().toString().trim();
                String add_student_tvStr = add_student_tv.getText().toString().trim();
                String firstStr = first.getText().toString().trim();
                String ed_attendanceStr = ed_attendance.getText().toString();
                if (spinnerButtonStr.length() > 0 && add_student_tvStr.length() > 0
                        && firstStr.length() > 0 && ed_attendanceStr.length() > 0) {
                    title2_ok.setClickable(true);
                    title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
                } else {
                    title2_ok.setClickable(false);
                    title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
                }
            }
        });
        ed_attendance.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String spinnerButtonStr = spinnerButton.getText().toString().trim();
                String add_student_tvStr = add_student_tv.getText().toString().trim();
                String firstStr = first.getText().toString().trim();
                String ed_attendanceStr = ed_attendance.getText().toString();
                if (spinnerButtonStr.length() > 0 && add_student_tvStr.length() > 0
                        && firstStr.length() > 0 && ed_attendanceStr.length() > 0) {
                    title2_ok.setClickable(true);
                    title2_ok.setTextColor(getResources().getColor(R.color.font_darkblue));
                } else {
                    title2_ok.setClickable(false);
                    title2_ok.setTextColor(getResources().getColor(R.color.font_lightgray));
                }
                if (s.length() > 499) {
                    DataUtil.getToast("缺勤事由不能超过500个字");
                    ed_attendance.requestFocus();
                }
            }
        });
    }
    private void exitOrNot() {
        dialog = new CustomDialog(this, R.style.mystyle,
                R.layout.customdialog, handler);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.setTitle("退出此次编辑?");
        dialog.setContent("");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//			String title = subject.getText().toString();
            String ed_attendance_str = ed_attendance.getText().toString();
            if (!DataUtil.isNullorEmpty(ed_attendance_str)) {//||!DataUtil.isNullorEmpty(teacherChoosed)
                exitOrNot();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 控制CustomDialog按钮效果.
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
                    finish();
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cList0930!=null){
            if(CCApplication.app.getMemberDetail()!=null){
                CCApplication.app.getMemberDetail().setClassList(cList0930);
            }
        }
    }
}
