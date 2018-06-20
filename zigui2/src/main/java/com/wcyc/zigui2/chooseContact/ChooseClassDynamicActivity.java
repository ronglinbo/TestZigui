/*
 * 文 件 名:ChooseStudentByClassAdminActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-08
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.chooseContact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.bean.AllGradeClass;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.newapp.bean.ClassStudentList;
import com.wcyc.zigui2.newapp.bean.GradeClass;
import com.wcyc.zigui2.newapp.bean.GradeMap;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 教职工选择发布班级动态的班级选择器
 */
public class ChooseClassDynamicActivity extends BaseActivity {
    private TextView cancel, enter;
    private Button chooseClass, chooseStudent;
    private List<GradeMap> gradeList;
    private AllGradeClass allGradeClass;
    private ChooseClassFragment chooseClassFragment;
    private ChooseStudentFragment chooseStudentFragment;
    private List<ClassMap> choosedClassList;//已选择学生班级
    private List<Student> choosedStudentList;//已选择学生
    private List<NewClasses> adminClass;//担任班主任的班级信息
    List<ClassStudent> list;
    private TextView tv_title;
    private LinearLayout ll_double_choice;
    private boolean singleChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_student);
        initView();
        parseIntent();
        initEvent();

        adminClass = DataUtil.getTeachClass();
        placeView(0);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        choosedClassList = (List<ClassMap>) bundle.getSerializable("choosedStudentClass");
        choosedStudentList = (List<Student>) bundle.getSerializable("choosedStudentList");

        //班级动态.只需要选择班级即可  不需要选择学生
        singleChoose = bundle.getBoolean("singleChoose");
        if (singleChoose) {
            ll_double_choice.setVisibility(View.GONE);
            tv_title.setText("选择班级");
            tv_title.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {

        chooseClass = (Button) findViewById(R.id.choose_class);
        chooseStudent = (Button) findViewById(R.id.choose_student);
        tv_title = (TextView) findViewById(R.id.new_content);
        tv_title.setVisibility(View.GONE);

        cancel = (TextView) findViewById(R.id.title2_off);
        enter = (TextView) findViewById(R.id.title2_ok);

        ll_double_choice = (LinearLayout) findViewById(R.id.ll_double_choice);
    }

    private void initEvent() {
        chooseClass.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                placeView(0);
            }

        });
        chooseStudent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                placeView(1);
            }

        });
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }

        });

        enter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                returnStudent();
            }

        });
    }

//    private void returnStudent() {
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        if (chooseStudentFragment != null) {
//            bundle.putSerializable("studentList", (Serializable) chooseStudentFragment.getChooseStudentList());
//        } else {
//            bundle.putSerializable("studentList", (Serializable) choosedStudentList);
//        }
//        if (chooseClassFragment != null) {
//            bundle.putSerializable("studentClass", (Serializable) chooseClassFragment.getChooseList());
//        } else {
//            bundle.putSerializable("studentClass", (Serializable) choosedClassList);
//        }
//        intent.putExtras(bundle);
//        setResult(RESULT_OK, intent);
//        finish();
//    }

    private void returnStudent() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (chooseStudentFragment != null) {
            bundle.putSerializable("studentList", (Serializable) chooseStudentFragment.getChooseStudentList());
        } else {
            bundle.putSerializable("studentList", (Serializable) choosedStudentList);
        }
        if (chooseClassFragment != null) {
            bundle.putSerializable("studentClass", (Serializable) chooseClassFragment.getChooseList());
            if (singleChoose) {
                bundle.putSerializable("allGradleList", allGradeClass);
            }
        } else {
            bundle.putSerializable("studentClass", (Serializable) choosedClassList);
        }

        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    public void placeView(int index) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(index + "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            switch (index) {
                case 0:
                    if (adminClass != null) {
                        allGradeClass = new AllGradeClass();
                        gradeList = new ArrayList<GradeMap>();
                        ArrayList<NewClasses> adminClass = (ArrayList<NewClasses>) this.adminClass;
                        ArrayList<NewClasses> newClasses = removeDuplicateData(adminClass);

                        int pos = 0;

                        for (NewClasses classes : newClasses) {
                            GradeMap grade = new GradeMap();//
                            ClassMap Class = new ClassMap();
                            List<ClassMap> classMapList = new ArrayList<ClassMap>();
                            grade.setName(classes.getGradeName());
                            Class.setName(classes.getClassName());
                            int classId = Integer.parseInt(classes.getClassID());
                            int id = Integer.parseInt(classes.getGradeId());
                            Class.setId(classId);
                            grade.setId(id);
                            classMapList.add(Class);
                            grade.setClassMapList(classMapList);

                            //是否已插入
                            if ((pos = isItemInList(gradeList, id)) < 0) {
                                //年级未存在
                                gradeList.add(grade);
                            } else {
                                gradeList.get(pos).getClassMapList().add(Class);
                            }
                        }
                        allGradeClass.setGradeMapList(gradeList);
                    }
                    chooseClassFragment = (ChooseClassFragment)
                            (fragment = ChooseClassFragment.newInstance(index, allGradeClass, choosedClassList));
                    chooseClass.setSelected(true);
                    chooseStudent.setSelected(false);
                    break;
                default:
                    break;
            }
        }
        ft.replace(R.id.maincontent, fragment, index + "");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
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

    private int isItemInList(List<GradeMap> list, int gradeId) {
        int i = 0;
        for (GradeMap grade : list) {
            if (gradeId == grade.getId()) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    protected void getMessage(String data) {
        // TODO Auto-generated method stub
    }


}
