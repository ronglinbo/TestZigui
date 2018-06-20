package com.wcyc.zigui2.newapp.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.easemob.util.HanziToPinyin;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chat.ContactDetail;
import com.wcyc.zigui2.chooseContact.ChooseLowerFragment;
import com.wcyc.zigui2.chooseContact.ChooseLowerToLowerFragment;
import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.chooseContact.SelectedDepartmentBean;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.AllContactListBean;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.AllTeacherList.TeacherMap;
import com.wcyc.zigui2.newapp.bean.ClassList;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.newapp.bean.ClassStudentList;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.TeacherSelectInfo;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.newapp.widget.RefreshListView2;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.GoHtml5Function;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.utils.RefreshableView;
import com.wcyc.zigui2.widget.RoundImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchContactActivity extends BaseActivity {
    private SearchView search;
    private RefreshListView1 result;
    private RefreshListView1 result2;
    private RefreshListView1 result1;
    private ListView result_department;

    private AllContactListBean allContactList;
    private ClassStudentList classStudentList;
    private ClassStudentList classStudentListSingle;
    private int Teacherlist;
    private TextView cancel;
    private String type;
    private String userId;
    private Context mContext;
    private List<AllTeacherList.TeacherMap> chooseTeacherList;
    private List<SelectedDepartmentBean> mSelectedDepartmentBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_contact);

        initInfo();
        initView();
    }

    private void initInfo() {
        mContext = SearchContactActivity.this;
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            userId = user.getUserId();
        }
        Bundle bundle = getIntent().getExtras();
        allContactList = (AllContactListBean) bundle.getSerializable("allContact");
        classStudentList = (ClassStudentList) bundle.getSerializable("classStudentList");
        classStudentListSingle = (ClassStudentList) bundle.getSerializable("classStudentListSingle");
        Teacherlist = bundle.getInt("TeacherList");
        chooseTeacherList = (List<TeacherMap>) bundle.getSerializable("lookSelectedTeacher");
        mSelectedDepartmentBeanList = (List<SelectedDepartmentBean>) bundle.getSerializable("lookSelectedDepartment");
        type = bundle.getString("type");
    }

    private TextView department_name_tv;
    private TextView teacher_tv;
    private static final int RESULT_CODE = 6;
    List<TeacherMap> matchTeacher = new ArrayList<TeacherMap>();
    List<SelectedDepartmentBean> matchDepartment = new ArrayList<SelectedDepartmentBean>();

    private void initView() {
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("back", (Serializable) backMatchList);
                intent.putExtra("backteach", (Serializable) matchTeacherList);
                Log.i("1234", String.valueOf(backMatchList.size()) + "....");
                setResult(RESULT_CODE, intent);
                finish();
            }

        });
        search = (SearchView) findViewById(R.id.search);
        if (type != null) {
            search.setQueryHint("姓名/姓名首字母");
        } else {
            search.setQueryHint("姓名/姓名首字母/手机号/部门名称");
        }

//		search.setSubmitButtonEnabled(true);
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String arg0) {
                // TODO Auto-generated method stub

                setSearchAapter(arg0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String arg0) {
                // TODO Auto-generated method stub
                setSearchAapter(arg0);

                return false;
            }
        });
        result = (RefreshListView1) findViewById(R.id.result);
        result1 = (RefreshListView1) findViewById(R.id.result1);
        result2 = (RefreshListView1) findViewById(R.id.result2);
        result_department = (ListView) findViewById(R.id.result_department);
        department_name_tv = (TextView) findViewById(R.id.department_name_tv);
        teacher_tv = (TextView) findViewById(R.id.teacher_tv);
        result.setOnRefreshListener(new RefreshListView1.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                //下拉刷新
                result.hideHeaderView();

            }

            @Override
            public void onLoadingMore() {
                //上拉加载
                if (page == 1) {
                    page = 2;
                }
                loadmoreData(3); //代表 查看已选教师
            }
        });
        result1.setOnRefreshListener(new RefreshListView1.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                //下拉刷新
                result1.hideHeaderView();

            }

            @Override
            public void onLoadingMore() {
                //上拉加载
                if (page == 1) {
                    page = 2;
                }
                loadmoreData(1); //代表 查看已选教师
            }
        });
        result2.setOnRefreshListener(new RefreshListView1.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                //下拉刷新
                result2.hideHeaderView();

            }

            @Override
            public void onLoadingMore() {
                //上拉加载
                if (page == 1) {
                    page = 2;
                }
                loadmoreData(2); //代表 查看学生
            }
        });
        result2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if (allContactList != null) {
                    ContactsList contact = (ContactsList) result.getAdapter().getItem(arg2);
                    if (contact == null) return;
                    String nickName = contact.getHxNickName();
                    System.out.println("onChildClick:" + arg1);
                    Intent intent = new Intent(SearchContactActivity.this, ContactDetail.class);
                    intent.putExtra("cellPhone", contact.getCellphone());
                    intent.putExtra("userName", contact.getUserName());
                    if (DataUtil.isNullorEmpty(nickName)) {
                        nickName = contact.getNickName();
                    }
                    intent.putExtra("userNick", nickName);
                    intent.putExtra("userTitle", contact.getText());
                    // 接受者头像
                    intent.putExtra("avatarUrl", contact.getUserIconURL());
                    startActivity(intent);
                } else if (classStudentList != null) {
                    Intent intent = new Intent();
                    Student student = (Student) arg0.getItemAtPosition(arg2);
                    if (student != null)
                        intent.putExtra("studentId", student.getId());
                    setResult(RESULT_OK, intent);
                } else if (classStudentListSingle != null) {
                    Student student = (Student) arg0.getItemAtPosition(arg2);
                    String studentId = student.getId() + "";

                    HashMap<String, String> para = new HashMap<String, String>();
                    para.put("X-mobile-Type", "android");

                    if ("comment".equals(type)) {
                        GoHtml5Function.goCommentById(para, studentId);

                        //人工考勤
                    } else if ("attendance".equals(type)) {
                        GoHtml5Function.goAttendenceChildById(para, studentId);

                        //进出校考勤
                    } else if ("schoolAttendance".equals(type)) {
                        GoHtml5Function.goSchoolAttendenceChildById(para, studentId);

                        //校车考勤
                    } else if ("carAttendance".equals(type)) {
                        GoHtml5Function.goSchoolBusAttendenceChildById(para, studentId);

                        //宿舍考勤
                    } else if ("dormAttendance".equals(type)) {
                        GoHtml5Function.goDormAttendenceChildById(para, studentId);
                    } else if ("score".equals(type)) {
                        GoHtml5Function.goScoreChildById(para, studentId);

                    }
                } else if (Teacherlist == 1) {
                    Intent intent = new Intent();
                    TeacherMap teacher = (TeacherMap) arg0.getItemAtPosition(arg2);
                    if (teacher != null)
                        intent.putExtra("teacherId", teacher.getId());
                    setResult(RESULT_OK, intent);
                }
                finish();
            }

        });
        result.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if (allContactList != null) {
                    ContactsList contact = (ContactsList) result.getAdapter().getItem(arg2);
                    if (contact == null) return;
                    String nickName = contact.getHxNickName();
                    System.out.println("onChildClick:" + arg1);
                    Intent intent = new Intent(SearchContactActivity.this, ContactDetail.class);
                    intent.putExtra("cellPhone", contact.getCellphone());
                    intent.putExtra("userName", contact.getUserName());
                    if (DataUtil.isNullorEmpty(nickName)) {
                        nickName = contact.getNickName();
                    }
                    intent.putExtra("userNick", nickName);
                    intent.putExtra("userTitle", contact.getText());
                    // 接受者头像
                    intent.putExtra("avatarUrl", contact.getUserIconURL());
                    startActivity(intent);
                } else if (classStudentList != null) {
                    Intent intent = new Intent();
                    Student student = (Student) arg0.getItemAtPosition(arg2);
                    if (student != null)
                        intent.putExtra("studentId", student.getId());
                    setResult(RESULT_OK, intent);
                } else if (classStudentListSingle != null) {
                    Student student = (Student) arg0.getItemAtPosition(arg2);
                    String studentId = student.getId() + "";

                    HashMap<String, String> para = new HashMap<String, String>();
                    para.put("X-mobile-Type", "android");

                    if ("comment".equals(type)) {
                        GoHtml5Function.goCommentById(para, studentId);

                    } else if ("attendance".equals(type)) {
                        GoHtml5Function.goAttendenceChildById(para, studentId);

                    } else if ("schoolAttendance".equals(type)) {
                        GoHtml5Function.goSchoolAttendenceChildById(para, studentId);

                    } else if ("carAttendance".equals(type)) {
                        GoHtml5Function.goSchoolBusAttendenceChildById(para, studentId);
                    } else if ("dormAttendance".equals(type)) {
                        GoHtml5Function.goDormAttendenceChildById(para, studentId);
                    }

                    else if ("score".equals(type)) {
                        GoHtml5Function.goScoreChildById(para, studentId);

                    }
                } else if (Teacherlist == 1) {
                    Intent intent = new Intent();
                    TeacherMap teacher = (TeacherMap) arg0.getItemAtPosition(arg2);
                    if (teacher != null)
                        intent.putExtra("teacherId", teacher.getId());
                    intent.putExtra("position", arg2);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }

        });
    }

    SearchTeacherListAdapter teachsearchList;
    SearchStudentListAdapter studentsearchList;
    LookSelectedTeacherAdapter searchList;
    private List<TeacherMap> matchTeacher_lookselected_page = new ArrayList<TeacherMap>();//查看已选老师分页集合
    List<Student> matchStudent_page = new ArrayList<>();//选择学生搜索集合分页
    List<Student> matchStudent = new ArrayList<>();//选择学生 搜索结果集合
    List<TeacherMap> matchTeacher_page = new ArrayList<>(); //选择教师 分页结合
    List<TeacherMap> matchTeacher_xuanze = new ArrayList<>(); //选择教师 搜索结果总集合

    public static <T> T deepCopy(T src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        T dest = (T) in.readObject();
        return dest;
    }

    private void setSearchAapter(final String key) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                //部门搜索的条件
                if (key.equals("")) {
                    return;
                }


                List<TeacherMap> matchname = searchTeacher1(key);
                List<String> departmentlist = new ArrayList<String>();
                for (TeacherMap teacherMap : matchname) {
                    if (teacherMap.getDepartment_name() != null) {
                        departmentlist.add(teacherMap.getDepartment_name());
                    }
                }

                List<TeacherMap> matchname_lookselected = searchTeacher5(key);
                List<String> departmentlist_lookselected = new ArrayList<String>();
                for (TeacherMap teacherMap : matchname_lookselected) {
                    if (teacherMap.getDepartment_name() != null) {
                        departmentlist_lookselected.add(teacherMap.getDepartment_name());
                    }
                }
                //字母搜索的条件  其实就是选择教师 字母搜索
                String zimu = key.toLowerCase();
                List<TeacherMap> zimulist = searchTeacher2(key);
                List<String> zimulist1 = new ArrayList<String>();
                for (TeacherMap teacherMap : zimulist) {
                    if (teacherMap.getHeader().substring(0, 1).contains(zimu)) {
                        zimulist1.add(teacherMap.getHeader());
                    }
                }


                if (Teacherlist == 1) { //选择教师
                    List<TeacherMap> match1 = searchTeacher2(key);
                    matchTeacher_page = new ArrayList<TeacherMap>();
                    //字母搜索
                    if (zimulist1 != null && zimulist1.size() > 0) {
                        matchTeacher_xuanze = searchTeacher2(key);

                    } else {//中文搜索
                        matchTeacher_xuanze = searchTeacher(key);
                    }
                    //新增加的部门搜索
                    if (departmentlist != null && departmentlist.size() > 0) {
                        for (TeacherMap item : matchname) {
                            boolean ishave = false;
                            for (TeacherMap item1 : matchTeacher_xuanze) {
                                if (item1.getId() == item.getId()) {
                                    ishave = true;
                                }
                            }
                            if (!ishave) {
                                matchTeacher_xuanze.add(0, item);
                            }
                        }
                    }
                    setToalPage(3); //代表选择教师

                    if (matchTeacher_xuanze.size() <= 10) {
                        try {
                            matchTeacher_page = deepCopy(matchTeacher_xuanze);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                        for (int i = 0; i < 10; i++) {
                            matchTeacher_page.add(matchTeacher_xuanze.get(i));
                        }

                    }
                    teachsearchList = new SearchTeacherListAdapter(matchTeacher_page);
                    result.setAdapter(teachsearchList);
                }

                //新增加的部门搜索
            /*		if (departmentlist != null && departmentlist.size() > 0) {
                        List<TeacherMap> match = searchTeacher1(key);
						SearchTeacherListAdapter searchList = new SearchTeacherListAdapter(match);
						result.setAdapter(searchList);
					} else */

                if (allContactList != null) {
                    List<ContactsList> match = searchContact(key);
                    SearchListAdapter searchList = new SearchListAdapter(match);
                    result.setAdapter(searchList);
                } else if (classStudentList != null) {
                    List<Student> studentsmatch = searchStudent(key);
                    matchStudent = studentsmatch;
                    setToalPage(2);
                    matchStudent_page = new ArrayList<Student>();
                    if (matchStudent.size() <= 10) {
                        try {
                            matchStudent_page = deepCopy(matchStudent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                        for (int i = 0; i < 10; i++) {
                            matchStudent_page.add(matchStudent.get(i));
                        }

                    }
                    studentsearchList = new SearchStudentListAdapter(matchStudent_page);
                    result2.setAdapter(studentsearchList);
                } else if (classStudentListSingle != null) {
                    List<Student> match = searchSingleStudent(key);

                    SearchStudentListAdapter searchList = new SearchStudentListAdapter(match);
                    result.setAdapter(searchList);
                } else if (chooseTeacherList != null) {
                    if (chooseTeacherList.size() > 0) {
                        List<TeacherMap> match = searchTeacher3(key);
                        matchTeacher = match;
                        for (TeacherMap item : matchname_lookselected) {
                            boolean ishave = false;
                            for (TeacherMap item1 : matchTeacher) {
                                if (item1.getId() == item.getId()) {
                                    ishave = true;
                                }
                            }
                            if (!ishave) {
                                matchTeacher.add(0, item);
                            }
                        }
                        setToalPage(1);
                        matchTeacher_lookselected_page = new ArrayList<TeacherMap>();
                        if (matchTeacherList != null && matchTeacherList.size() > 0) {
                            for (TeacherMap teacherMap : matchTeacherList) {
                                for (int i = 0; i < match.size(); i++) {
                                    if (teacherMap.getId() == match.get(i).getId()) {
                                        matchTeacher.remove(teacherMap);
                                    }
                                }
                            }
                        }
                        if (matchTeacher.size() <= 10) {
                            try {
                                matchTeacher_lookselected_page = deepCopy(matchTeacher);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {

                            for (int i = 0; i < 10; i++) {
                                matchTeacher_lookselected_page.add(matchTeacher.get(i));
                            }

                        }


                        searchList = new LookSelectedTeacherAdapter(matchTeacher_lookselected_page);
                        result1.setAdapter(searchList);
                        teacher_tv.setVisibility(View.VISIBLE);
                    } else {
                        teacher_tv.setVisibility(View.GONE);
                    }
                }
                if (mSelectedDepartmentBeanList != null) {
                    if (mSelectedDepartmentBeanList.size() > 0) {
                        List<SelectedDepartmentBean> match = searchTeacher4(key);
                        matchDepartment = match;
                        if (backMatchList != null && backMatchList.size() > 0) {
                            for (SelectedDepartmentBean selectedDepartmentBean : backMatchList) {
                                for (int i = 0; i < match.size(); i++) {
                                    if (selectedDepartmentBean.department.equals(match.get(i).department)) {
                                        matchDepartment.remove(selectedDepartmentBean);
                                    }
                                }
                            }
                        }
                        LookSelectedDepartmentAdapter searchDepartmentAdapter = new LookSelectedDepartmentAdapter(match);
                        result_department.setAdapter(searchDepartmentAdapter);
                        result_department.setVisibility(View.VISIBLE);
                        department_name_tv.setVisibility(View.VISIBLE);
                    } else {
                        department_name_tv.setVisibility(View.GONE);
                        result_department.setVisibility(View.GONE);
                    }
                }


            }

        }).run();
    }

    private List<SelectedDepartmentBean> searchTeacher4(String text) {
        String key = text.toLowerCase();
        boolean isChinese = false;
        if (text.getBytes().length > 1) {
            isChinese = true;
        }
        //List<ClassList> list = allContactList.getClassList();
        List<SelectedDepartmentBean> match = new ArrayList<SelectedDepartmentBean>();
        for (SelectedDepartmentBean item : mSelectedDepartmentBeanList) {
            //List<SelectedDepartmentBean> contactsList = item.getContactsList();
            //for (SelectedDepartmentBean contact : contactsList) {
            String department = item.department;
            String header = item.header;
            if (header == null) {
                if (department != null) {
                    header = getHeader(department);
                }
                item.header = header;
            }
            if ((department != null && department.contains(key))
                    || (/*isChinese == true && */header != null && header.contains(key))) {
                match.add(item);
            }
        }

        return match;
    }

    private List<ContactsList> searchContact(String text) {
        String key = text.toLowerCase();
        boolean isChinese = false;
        if (text.getBytes().length > 1) {
            isChinese = true;
        }
        List<ClassList> list = allContactList.getClassList();
        List<ContactsList> match = new ArrayList<ContactsList>();
        for (ClassList item : list) {
            List<ContactsList> contactsList = item.getContactsList();
            for (ContactsList contact : contactsList) {
                String nickName = contact.getHxNickName();
                String name = contact.getNickName();
                String phone = contact.getCellphone();
                String headers = contact.getHeader();
                if (headers == null) {
                    if (nickName != null) {
                        headers = getHeader(nickName);
                    } else if (name != null) {
                        headers = getHeader(name);
                    }
                    contact.setHeader(headers);
                }

                if ((nickName != null && nickName.contains(key))
                        || (name != null && name.contains(key))
                        || (phone != null && phone.contains(key))
                        || (/*isChinese == true && */headers != null && headers.contains(key))) {
                    match.add(contact);
                }
            }
        }
        return match;
    }

    private List<Student> searchStudent(String text) {
        String key = text.toLowerCase();
        boolean isChinese = false;
        if (text.getBytes().length > 1) {
            isChinese = true;
        }
        List<ClassStudent> list = classStudentList.getClassStudent();
        List<Student> match = new ArrayList<Student>();
        for (ClassStudent item : list) {
            List<Student> studentList = item.getStudentList();
            if (studentList != null) {
                for (Student student : studentList) {
                    String name = student.getName();
                    String headers = student.getHeader();
                    if (headers == null) {
                        if (name != null) {
                            headers = getHeader(name);
                        }
                        student.setHeader(headers);
                    }

                    if ((name != null && name.contains(key))
                            || (/*isChinese == true && */headers != null && headers.contains(key))) {
                        match.add(student);
                    }
                }
            }
        }
        return match;
    }

    private List<TeacherMap> searchTeacher(String text) {
        String key = text.toLowerCase();
        boolean isChinese = false;
        if (text.getBytes().length > 1) {
            isChinese = true;
        }
        List<TeacherMap> list = CCApplication.getInstance().getAllTeacherList().getTeacherMapList();
        List<TeacherMap> match = new ArrayList<TeacherMap>();

        for (TeacherMap item : list) {
            String name = item.getName();
            String mobile = item.getMobile();
            String headers = item.getHeader();
            if (headers == null) {
                if (name != null) {
                    headers = getHeader(name);
                }
                item.setHeader(headers);
            }

            if ((name != null && name.contains(key)) || (/*isChinese == true && */headers != null && headers.contains(key) || (mobile != null && mobile.contains(key)))) {
                match.add(item);
            }
        }
        return match;
    }

    private List<TeacherMap> searchTeacher3(String text) {
        String key = text.toLowerCase();
        boolean isChinese = false;
        if (text.getBytes().length > 1) {
            isChinese = true;
        }
        //List<TeacherMap> list = Teacherlist.getTeacherMapList();
        List<TeacherMap> match = new LinkedList<TeacherMap>();

        for (TeacherMap item : chooseTeacherList) {
            if (item.getId() > 0) { //小于0 说明已经被删除掉了
                String name = item.getName();
                String mobile = item.getMobile();
                String headers = item.getHeader();
                if (headers == null) {
                    if (name != null) {
                        headers = getHeader(name);
                    }
                    item.setHeader(headers);
                }

                if ((name != null && name.contains(key)) || (/*isChinese == true && */headers != null && headers.contains(key) || (mobile != null && mobile.contains(key)))) {
                    match.add(item);
                }
            }

        }
        return match;
    }

    //字母搜索
    private List<TeacherMap> searchTeacher2(String text) {
        String key = text.toLowerCase();
        List<TeacherMap> zimulist = new ArrayList<TeacherMap>();
        if (Teacherlist == 1) {
            List<TeacherMap> list = CCApplication.getInstance().getAllTeacherList().getTeacherMapList();
            for (TeacherMap item : list) {
                String name = item.getName();
                String headers = item.getHeader().substring(0, 1);
                if (headers == null) {
                    if (name != null) {
                        headers = getHeader(name);
                    }
                    item.setHeader(headers);
                }
                if ((headers != null && headers.contains(key))) {
                    zimulist.add(item);
                }

            }
        }
        return zimulist;
    }

    //部门搜索 选择教师
    private List<TeacherMap> searchTeacher1(String text) {
        String key = text.toLowerCase();
        boolean isChinese = false;
        if (text.getBytes().length > 1) {
            isChinese = true;
        }
        List<TeacherMap> match = new ArrayList<TeacherMap>();
        if (Teacherlist == 1) {
            List<TeacherMap> list = CCApplication.getInstance().getAllTeacherList().getTeacherMapList();
            for (TeacherMap item : list) {
                if (item.getDepartment_name() != null) {
                    String name = item.getName();
                    String deapartment = item.getDepartment_name();
                    String headers = item.getHeader();
                    if (headers == null) {
                        if (name != null) {
                            headers = getHeader(name);
                        }
                        item.setHeader(headers);
                    }
                    if ((deapartment != null && deapartment.contains(key))) {
                        match.add(item);
                    }
                }
            }
        }
        return match;

    }

    //部门搜索 查看已选
    private List<TeacherMap> searchTeacher5(String text) {
        String key = text.toLowerCase();
        boolean isChinese = false;
        if (text.getBytes().length > 1) {
            isChinese = true;
        }
        List<TeacherMap> match = new ArrayList<TeacherMap>();
        if (chooseTeacherList != null) {
            List<TeacherMap> list = chooseTeacherList;
            for (TeacherMap item : list) {
                if (item.getDepartment_name() != null) {
                    String name = item.getName();
                    String deapartment = item.getDepartment_name();
                    String headers = item.getHeader();
                    if (headers == null) {
                        if (name != null) {
                            headers = getHeader(name);
                        }
                        item.setHeader(headers);
                    }
                    if ((deapartment != null && deapartment.contains(key))) {
                        match.add(item);
                    }
                }
            }
        }
        return match;

    }

    private List<Student> searchSingleStudent(String text) {
        String key = text.toLowerCase();
        List<ClassStudent> list = classStudentListSingle.getClassStudent();
        List<Student> match = new ArrayList<Student>();
        for (ClassStudent item : list) {
            List<Student> studentList = item.getStudentList();
            for (Student student : studentList) {
                String name = student.getName();

                String headers = student.getHeader();
                if (headers == null) {
                    if (name != null) {
                        headers = getHeader(name);
                    }
                    student.setHeader(headers);
                }

                if ((name != null && name.contains(key))
                        || (headers != null && headers.contains(key))) {
                    match.add(student);
                }
            }
        }
        return match;
    }

    private String getHeader(String name) {
        String header = "";
        for (int i = 0; i < name.length(); i++) {
            try {
                header += HanziToPinyin.getInstance().get(name).get(i)
                        .target.substring(0, 1).toLowerCase();
            } catch (Exception e) {
                System.out.println("name" + name);
                e.printStackTrace();
            }
        }
        return header;
    }

    private class SearchListAdapter extends BaseAdapter {
        private List<ContactsList> match;

        public SearchListAdapter(List<ContactsList> match) {
            this.match = match;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (match != null)
                return match.size();
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return match.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder;
//			if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.row_contact, null);
            viewHolder.avatar = (RoundImageView) convertView.findViewById(R.id.avatar);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.header);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(viewHolder);
//			}else{
//				viewHolder = (ViewHolder) convertView.getTag();
//			}
            ContactsList contact = match.get(arg0);
            String name = contact.getHxNickName();
            if (DataUtil.isNullorEmpty(name)) {
                name = contact.getNickName();
            }
            viewHolder.tvTitle.setText(contact.getText());
            final String file = DataUtil.getDownloadURL(contact.getUserIconURL());

            viewHolder.tvName.setText(name);
            getImageLoader().displayImage(file, viewHolder.avatar, getImageLoaderOptions());
            return convertView;
        }

    }

    private class SearchStudentListAdapter extends BaseAdapter {
        private List<Student> match;

        public SearchStudentListAdapter(List<Student> match) {
            this.match = match;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (match != null)
                return match.size();
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return match.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder;
//			if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.row_contact, null);
            viewHolder.avatar = (RoundImageView) convertView.findViewById(R.id.avatar);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.header);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(viewHolder);
//			}else{
//				viewHolder = (ViewHolder) convertView.getTag();
//			}
            Student student = match.get(arg0);
            String name = student.getName();
            final int id = student.getId();
            setImage(viewHolder, student.getImgUrl(), String.valueOf(id));
            viewHolder.tvName.setText(name);

            return convertView;
        }

    }


    private class SearchTeacherListAdapter extends BaseAdapter {
        private List<TeacherMap> match;

        public SearchTeacherListAdapter(List<TeacherMap> match) {
            this.match = match;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (match != null)
                return match.size();
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return match.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder;
//			if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = getLayoutInflater().inflate(R.layout.row_contact, null);
            viewHolder.avatar = (RoundImageView) convertView.findViewById(R.id.avatar);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.header);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(viewHolder);
//			}else{
//				viewHolder = (ViewHolder) convertView.getTag();
//			}
            TeacherMap teacher = match.get(arg0);
            String name = teacher.getName();
            final int id = teacher.getId();
            setImage(viewHolder, teacher.getPicAddress(), String.valueOf(id));
            viewHolder.tvName.setText(name);

            return convertView;
        }

    }

    private List<SelectedDepartmentBean> backMatchList = new ArrayList<SelectedDepartmentBean>();//返回上一个页面的list,记录删除了哪些数据

    private class LookSelectedDepartmentAdapter extends BaseAdapter {
        private List<SelectedDepartmentBean> match;

        public LookSelectedDepartmentAdapter(List<SelectedDepartmentBean> match) {
            this.match = match;
        }

        @Override
        public int getCount() {
            if (match != null) {
                return match.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return match.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.row_contact, null);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title1);
                viewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
                viewHolder.avatar = (RoundImageView) convertView.findViewById(R.id.avatar);
                viewHolder.ivDelete.setVisibility(View.VISIBLE);
                viewHolder.avatar.setVisibility(View.GONE);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            SelectedDepartmentBean departmentName = match.get(position);
            viewHolder.tvName.setText(departmentName.department);
            if (departmentName.secondDepartment != null) {
                viewHolder.tvTitle.setText("（" + departmentName.secondDepartment + "）");
                viewHolder.tvTitle.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvTitle.setVisibility(View.GONE);
            }

            viewHolder.ivDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (match != null && match.size() > 0) {
                        backMatchList.add(match.get(position));
                        Log.i("1234", String.valueOf(backMatchList.size()));

                        ChooseLowerFragment.is_select_all = false;
                        ChooseLowerToLowerFragment.is_select_all = false;
                        SelectedDepartmentBean o = match.get(position);
                        TeacherSelectInfo teacherSelectInfo_choose = ChooseTeacherActivity.teacherSelectInfo_choose;

                        if ("年级/班级".equals(o.department)) {
                            teacherSelectInfo_choose.getInfo().getClasses().clear();
                        } else {
                            for (int j = 0; j < teacherSelectInfo_choose.getInfo().getClasses().size(); j++) {  //年级/班级
                                if (o.department.equals(teacherSelectInfo_choose.getInfo().getClasses().get(j).getName())) {
                                    teacherSelectInfo_choose.getInfo().getClasses().remove(teacherSelectInfo_choose.getInfo().getClasses().get(j));
                                } else {
                                    int b = -1;
                                    for (int k = 0; k < teacherSelectInfo_choose.getInfo().getClasses().get(j).getClasses().size(); k++) {
                                        if (o.department.equals(teacherSelectInfo_choose.getInfo().getClasses().get(j).getClasses().get(k).getName())) {
                                            teacherSelectInfo_choose.getInfo().getClasses().get(j).getClasses().remove(teacherSelectInfo_choose.getInfo().getClasses().get(j).getClasses().get(k));
                                            if (teacherSelectInfo_choose.getInfo().getClasses().get(j).getClasses().size() == 0) {
                                                b = j;
                                            }
                                        }

                                    }
                                    if (b >= 0) {
                                        teacherSelectInfo_choose.getInfo().getClasses().remove(b);
                                    }

                                }

                            }
                        }

                        if ("教研组".equals(o.department)) {
                            teacherSelectInfo_choose.getInfo().getGroups().clear();
                        } else {
                            for (int j = 0; j < teacherSelectInfo_choose.getInfo().getGroups().size(); j++) {   //教研组
                                if (o.department.equals(teacherSelectInfo_choose.getInfo().getGroups().get(j).getName())) {
                                    teacherSelectInfo_choose.getInfo().getGroups().remove(teacherSelectInfo_choose.getInfo().getGroups().get(j));
                                } else {
                                    int b = -1;
                                    for (int k = 0; k < teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().size(); k++) {
                                        if (o.department.equals(teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().get(k).getName())) {
                                            teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().remove(teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().get(k));
                                            if (teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().size() == 0) {
                                                b = j;
                                            }

                                        }

                                    }

                                    if (b >= 0) {
                                        teacherSelectInfo_choose.getInfo().getGroups().remove(b);
                                    }

                                }
                            }
                        }


                        if ("备课组".equals(o.department)) {
                            teacherSelectInfo_choose.getInfo().getPrepareLession().clear();
                        } else {
                            for (int j = 0; j < teacherSelectInfo_choose.getInfo().getPrepareLession().size(); j++) {   //备课组
                                if (o.department.equals(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getName())) {
                                    teacherSelectInfo_choose.getInfo().getPrepareLession().remove(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j));
                                } else {
                                    int b = -1;
                                    for (int k = 0; k < teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().size(); k++) {
                                        if (o.department.equals(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().get(k).getName())) {
                                            teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().remove(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().get(k));
                                            if (teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().size() == 0) {
                                                b = j;
                                            }
                                        }
                                    }
                                    if (b >= 0) {
                                        teacherSelectInfo_choose.getInfo().getPrepareLession().remove(b);
                                    }
                                }
                            }
                        }

                        for (int j = 0; j < teacherSelectInfo_choose.getInfo().getDeparts().size(); j++) {  //行政机构
                            if (o.department.equals(teacherSelectInfo_choose.getInfo().getDeparts().get(j).getDepartmentName())) {
                                teacherSelectInfo_choose.getInfo().getDeparts().remove(teacherSelectInfo_choose.getInfo().getDeparts().get(j));

                            }

                        }

                        for (int j = 0; j < teacherSelectInfo_choose.getInfo().getGradeGroup().size(); j++) {   //年级组长
                            if ("年级组长".equals(o.department)) {
                                teacherSelectInfo_choose.getInfo().getGradeGroup().clear();
                            } else if (o.department.equals(teacherSelectInfo_choose.getInfo().getGradeGroup().get(j).getName())) {
                                teacherSelectInfo_choose.getInfo().getGradeGroup().remove(teacherSelectInfo_choose.getInfo().getGradeGroup().get(j));
                            }
                        }

                        if (teacherSelectInfo_choose.getInfo().getGradeGroup().size() == 0) {
                            Log.e("getGradeGroupsize", teacherSelectInfo_choose.getInfo().getGradeGroup().size() + "");
                            teacherSelectInfo_choose.getInfo().setGradeGroup(new ArrayList<TeacherSelectInfo.InfoBean.GradeGroupBean>());
                            ChooseLowerFragment.ischecked = false;
                        }
                        for (int j = 0; j < teacherSelectInfo_choose.getInfo().getGradesTeacher().size(); j++) {    //班主任

                            if ("班主任".equals(o.department)) {
                                teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();
                            } else if (o.department.equals(teacherSelectInfo_choose.getInfo().getGradesTeacher().get(j).getName())) {
                                teacherSelectInfo_choose.getInfo().getGradesTeacher().remove(teacherSelectInfo_choose.getInfo().getGradesTeacher().get(j));
                            }
                        }
                        if (teacherSelectInfo_choose.getInfo().getGradesTeacher().size() == 0) {
                            teacherSelectInfo_choose.getInfo().setGradesTeacher(new ArrayList<TeacherSelectInfo.InfoBean.GradesTeacherBean>());
                            ChooseLowerFragment.ischecked = false;
                        }
                        for (int j = 0; j < teacherSelectInfo_choose.getInfo().getGroupsLeader().size(); j++) { //教研组长
                            if ("教研组长".equals(o.department)) {
                                teacherSelectInfo_choose.getInfo().getGroupsLeader().clear();
                            } else if (o.department.equals(teacherSelectInfo_choose.getInfo().getGroupsLeader().get(j).getName())) {
                                teacherSelectInfo_choose.getInfo().getGroupsLeader().remove(teacherSelectInfo_choose.getInfo().getGroupsLeader().get(j));
                            }
                        }
                        if (teacherSelectInfo_choose.getInfo().getGroupsLeader().size() == 0) {
                            teacherSelectInfo_choose.getInfo().setGroupsLeader(new ArrayList<TeacherSelectInfo.InfoBean.GroupsLeaderBean>());
                            ChooseLowerFragment.ischecked = false;
                        }


                        for (int j = 0; j < teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().size(); j++) { //备课组长
                            if ("备课组长".equals(o.department)) {
                                teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().clear();
                            } else if (o.department.equals(teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().get(j).getName())) {
                                teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().remove(teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().get(j));
                            }
                        }
                        if (teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().size() == 0) {
                            teacherSelectInfo_choose.getInfo().setPrepareLessionLeader(new ArrayList<TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean>());
                            ChooseLowerFragment.ischecked = false;
                        }


                        if (o.department.equals("全体教职工")) {
                            ChooseTeacherActivity.isSelectAll = false;
                        }
                        if (o.department.equals("校级领导")) {
                            ChooseTeacherActivity.schoolleader = false;
                        }
                        if (o.department.equals("部门负责人")) {
                            ChooseTeacherActivity.departleadr = false;
                        }

                        match.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });
            return convertView;
        }
    }

    private List<TeacherMap> matchTeacherList = new ArrayList<TeacherMap>();

    private class LookSelectedTeacherAdapter extends BaseAdapter {
        private List<TeacherMap> match;

        public LookSelectedTeacherAdapter(List<TeacherMap> match) {
            this.match = match;
        }

        @Override
        public int getCount() {
            if (match != null) {
                return match.size();
            }
            return 0;
        }

        public void deleteItem(int position) {
            if (match != null && match.size() > position) {
                match.remove(position);

                notifyDataSetChanged();
            }
        }

        @Override
        public Object getItem(int position) {
            return match.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.row_contact, null);
                viewHolder.avatar = (RoundImageView) convertView.findViewById(R.id.avatar);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.header);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
                viewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.ivDelete.setVisibility(View.VISIBLE);
            TeacherMap teacher = match.get(position);
            String name = teacher.getName();

            viewHolder.tvName.setText(name);
            final int id = teacher.getId();
            setImage(viewHolder, teacher.getPicAddress(), String.valueOf(id));

            viewHolder.ivDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.i("条目被点击了" + position);
                    matchTeacherList.add(match.get(position));
                    deleteItem(position);
                }
            });

            return convertView;
        }
    }

    private static class ViewHolder {
        /**
         * 联系人名字
         */
        TextView tvName;
        TextView tvNum;
        /**
         * 头字符
         */
        TextView tvHeader;
        /**
         * 用户头像
         */
        RoundImageView avatar;
        /**
         * 职位
         */
        TextView tvTitle;
        /**
         * 删除图标
         */
        ImageView ivDelete;
    }

    @Override
    protected void getMessage(String data) {
        // TODO Auto-generated method stub

    }

    private void setImage(SearchContactActivity.ViewHolder holder, String file, String userId) {
        if (file != null) {
            if (LocalUtil.mBitMap != null && userId != null && userId.equals(this.userId)) {
                holder.avatar.setImageBitmap(LocalUtil.mBitMap);
            } else {
                String url = DataUtil.getIconURL(file);
                System.out.println("url:" + url);
                ((BaseActivity) mContext).getImageLoader().displayImage(url, holder.avatar,
                        ((BaseActivity) mContext).getImageLoaderOptions());
            }
        } else {
            holder.avatar.setImageResource(R.drawable.pho_touxiang);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("back", (Serializable) backMatchList);
        intent.putExtra("backteach", (Serializable) matchTeacherList);
        Log.i("1234", String.valueOf(backMatchList.size()) + "....");
        setResult(RESULT_CODE, intent);
        super.onBackPressed();
    }

    private boolean willtolastpage = false;
    private int page = 1;
    private int sumpage = 0;//总页数

    private void setToalPage(int type) {
        if (type == 1) {

            float i = (float) matchTeacher.size() / 10;
            float b = i - (int) i;
            if (b > 0) {//说明是小数 已经到尾页了 不是整数页
                sumpage = (int) i + 1;
            }
            if (b == 0) {
                sumpage = (int) i;
            }
        }
        if (type == 2) {
            float i = (float) matchStudent.size() / 10;
            float b = i - (int) i;
            if (b > 0) {//说明是小数 已经到尾页了 不是整数页
                sumpage = (int) i + 1;
            }
            if (b == 0) {
                sumpage = (int) i;
            }
        }
        if (type == 3) {
            float i = (float) matchTeacher_xuanze.size() / 10;
            float b = i - (int) i;
            if (b > 0) {//说明是小数 已经到尾页了 不是整数页
                sumpage = (int) i + 1;
            }
            if (b == 0) {
                sumpage = (int) i;
            }
        }


    }

    private List<Student> getStudentListByPage(int page) {
        List<Student> list = new ArrayList<>();
        if (willtolastpage) {
            int i = 1;

            for (Student item : matchStudent) {
                if (i > page * 10 && i <= matchStudent.size()) {
                    list.add(item);
                }
                i++;
            }
        } else {
            int b = 1;
            int sum = page * 10;
            for (Student item : matchStudent) {
                if (b > (page - 1) * 10 && b <= sum) {
                    list.add(item);
                }
                b++;
            }
        }
        return list;

    }

    private List<AllTeacherList.TeacherMap> getTeacherlistBypage(int page) {
        List<AllTeacherList.TeacherMap> list = new ArrayList<>();
        if (willtolastpage) {
            int i = 1;

            for (AllTeacherList.TeacherMap item : matchTeacher) {
                if (i > page * 10 && i <= matchTeacher.size()) {
                    list.add(item);
                }
                i++;
            }
        } else {
            int b = 1;
            int sum = page * 10;
            for (AllTeacherList.TeacherMap item : matchTeacher) {
                if (b > (page - 1) * 10 && b <= sum) {
                    list.add(item);
                }
                b++;
            }
        }
        return list;

    }

    private List<AllTeacherList.TeacherMap> getTeacherlistBypage1(int page) {
        List<AllTeacherList.TeacherMap> list = new ArrayList<>();
        if (willtolastpage) {
            int i = 1;

            for (AllTeacherList.TeacherMap item : matchTeacher_xuanze) {
                if (i > page * 10 && i <= matchTeacher.size()) {
                    list.add(item);
                }
                i++;
            }
        } else {
            int b = 1;
            int sum = page * 10;
            for (AllTeacherList.TeacherMap item : matchTeacher_xuanze) {
                if (b > (page - 1) * 10 && b <= sum) {
                    list.add(item);
                }
                b++;
            }
        }
        return list;

    }
//	private void refershData(){
//		page=1;//初始化page
//		chooseTeacherList_page.clear();
//		if(chooseTeacherList.size()<=10){
//			try {
//				chooseTeacherList_page=deepCopy(chooseTeacherList);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			teacherAdapter.setChooseTeacherList(chooseTeacherList_page);
//		}else{
//
//			for(int i=0;i<10;i++){
//				chooseTeacherList_page.add(chooseTeacherList.get(i));
//			}
//
//		}
//		teacherAdapter.setChooseTeacherList(chooseTeacherList_page);
//
//	}

    private void loadmoreData(int type) {
        if (type == 1) {
            if (matchTeacher.size() <= 10) {
                //直接显示数据

                //	DataUtil.getToast("已经没有更多数据");
            } else {
                if ((sumpage * 10 - page * 10) < 0) {
                    //已经到尾页

                    //	DataUtil.getToast("已经没有更多数据");
                } else {
                    //是不是倒数第2页 是的话 则记录
                    if ((sumpage * 10 - page * 10) > 0 && (sumpage * 10 - page * 10) <= 10) {

                        willtolastpage = true; //变量表示 是不是下页就是尾页了
                    }
                    matchTeacher_lookselected_page.addAll(getTeacherlistBypage(page));
                    searchList.notifyDataSetChanged();
                    //page 逻辑判断
                    //数据是不是到尾页了


                    page++;
                }
            }
            result1.hideFooterView();
        }
        if (type == 2) {
            if (matchStudent.size() <= 10) {
                //直接显示数据

                //	DataUtil.getToast("已经没有更多数据");
            } else {
                if ((sumpage * 10 - page * 10) < 0) {
                    //已经到尾页

                    //	DataUtil.getToast("已经没有更多数据");
                } else {
                    //是不是倒数第2页 是的话 则记录
                    if ((sumpage * 10 - page * 10) > 0 && (sumpage * 10 - page * 10) <= 10) {

                        willtolastpage = true; //变量表示 是不是下页就是尾页了
                    }
                    matchStudent_page.addAll(getStudentListByPage(page));
                    studentsearchList.notifyDataSetChanged();
                    //page 逻辑判断
                    //数据是不是到尾页了


                    page++;
                }
            }
            result2.hideFooterView();
        }
        if (type == 3) {
            if (matchTeacher_xuanze.size() <= 10) {
                //直接显示数据

                //	DataUtil.getToast("已经没有更多数据");
            } else {
                if ((sumpage * 10 - page * 10) < 0) {
                    //已经到尾页

                    //	DataUtil.getToast("已经没有更多数据");
                } else {
                    //是不是倒数第2页 是的话 则记录
                    if ((sumpage * 10 - page * 10) > 0 && (sumpage * 10 - page * 10) <= 10) {

                        willtolastpage = true; //变量表示 是不是下页就是尾页了
                    }
                    matchTeacher_page.addAll(getTeacherlistBypage1(page));
                    teachsearchList.notifyDataSetChanged();
                    //page 逻辑判断
                    //数据是不是到尾页了


                    page++;
                }
            }
            result.hideFooterView();
        }


    }
}
