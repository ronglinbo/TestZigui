/*
 * 文 件 名:ChooseTeacherActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-08
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.chooseContact;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.AllDeptList;
import com.wcyc.zigui2.newapp.bean.AllDeptList.CommonGroup;
import com.wcyc.zigui2.newapp.bean.AllDeptList.ContactGroupMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.DepMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.GradeMap;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.AllTeacherList.TeacherMap;
import com.wcyc.zigui2.newapp.bean.TeacherSelectInfo;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 选择器（部门或教师）主页面
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class ChooseTeacherActivity extends BaseActivity  {

    private static final int REQUEST_CODE_FOR_SELECTED_TEACHER = 100;
    private final String TAG = this.getClass().getSimpleName();
    public static TeacherSelectInfo teacherSelectInfo; //服务器获取的所有部门数据集合
    public static TeacherSelectInfo teacherSelectInfo_choose;//记录所有选择集合
    /**
     * cancel 取消
     * <p/> enter 确定
     */
    private TextView cancel, enter;

    public void setRecord_fragment(Fragment record_fragment) {
        this.record_fragment = record_fragment;
    }

    /**
     * chooseDept 选择部门
     * <p/> chooseTeacher选择老师
     */
     private Fragment record_fragment ;
    private Button chooseDept, chooseTeacher;
    private static final int GET_USER_GROUP = 0;
    private static final int GET_ALL_TEACHER = 1;
    /**
     * 对应不同部门的id 选择部门
     * <p/>
     */
    public static final int TEACHER_LIST = 1;//班主任

    public static final int JIYAOYAN_LEADER_LIST = 3;//教研组长
    public static final int  PREPARE_LESSION_LEADER= 4;//备课组长
    public static final int CLASSES = 5;//年级/班级
    public static final int  PREPARELESSION = 6;//备课组
    public static final int JIAOYAN_TEACHER_LIST = 2;//教研组
    public static final int GRADE_GROUP = 7;//年级组长
    public   static boolean isSelectAll=false; //全体教职工
    public   static boolean schoolleader=false; //校级领导
    public  static boolean departleadr=false; //部门负责


    /** choosedList 从通知界面传的部门集合 */
    private AllDeptList list, choosedList;
    private AllTeacherList teacherList;
    private ArrayList<CommonGroup> commonList;
    private ArrayList<GradeMap> gradeList;  //教学机构
    private ArrayList<ContactGroupMap> contactList;
    private ArrayList<DepMap> deptList;
    //	private ArrayList<Object> choosedContact,choosedDept,choosedGrade,choosedCommon;
    private List<TeacherMap> chooseTeacherList;
    private static List<TeacherMap> choosedTeacher;//回选的list

    public static  ChooseDeptFragment getDeptfragment() {
        return deptfragment;
    }

    private static ChooseDeptFragment deptfragment;

    public static ChooseTeacherFragment getTeacherFragment() {
        return teacherFragment;
    }

    private static ChooseTeacherFragment teacherFragment;
    private ChooseLowerToLowerFragment chooseLowerToLowerFragment;
    private ChooseLowerFragment  lowerFragment;
    private String[][] commonName
            = {{"校级领导", "schoolleader"}, {"部门负责人", "departmentcharge"}, {"班主任", "classteacher",},
            {"教研组长", "researchleader"}, {"备课组长", "lessonsleader"},
            {"年级组长", "gradeleader"}};

    private String[][] gradeName
            = {{"年级/班级","gradeAndClass"},{"教研组","researchgroup"},{"备课组","lessonsgroup"}};

    private TextView lookSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_teacher);
        list=new AllDeptList();
        initView();

        initEvent();
        parseIntent();
        getAllDeptFromServer();
        teacherList = CCApplication.getInstance().getAllTeacherList();
    }

    private void initView() {
        lookSelected = (TextView) findViewById(R.id.look_selected);     //查看已选

        chooseDept = (Button) findViewById(R.id.choose_dept);
        chooseTeacher = (Button) findViewById(R.id.choose_teacher);
        findViewById(R.id.new_content).setVisibility(View.GONE);    // 隐藏标题
        cancel = (TextView) findViewById(R.id.title2_off);
        enter = (TextView) findViewById(R.id.title2_ok);


    }

    private void parseIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
//			choosedDept = (ArrayList<Object>) bundle.getSerializable("dept");
//			choosedContact = (ArrayList<Object>) bundle.getSerializable("contact");
//			choosedGrade = (ArrayList<Object>) bundle.getSerializable("grade");
//			choosedCommon = (ArrayList<Object>) bundle.getSerializable("common");
            int type =  bundle.getInt("type",-1);
            TeacherSelectInfo teacherSelectInfo_record=(TeacherSelectInfo) bundle.getSerializable("teacherSelectInfo_record");

             isSelectAll=bundle.getBoolean("selectAll");; //全体教职工
             schoolleader=bundle.getBoolean("school_leader"); //校级领导
             departleadr=bundle.getBoolean("departmentcharge"); //部门负责
            List<TeacherMap>  choosedTeacher1 = (List<TeacherMap>) bundle.getSerializable("teacher");//教师选择
            choosedList = (AllDeptList) bundle.getSerializable("allDept");//教师选择
           if(deptfragment!=null){
             deptfragment.x=0;
             deptfragment.y=0;
           }
            if(teacherFragment==null){
                teacherFragment= (ChooseTeacherFragment) ChooseTeacherFragment.newInstance(1, teacherList, null);

            }

            if(type==1){//代表邮件 日志 总结 通知
                if(teacherSelectInfo_record!=null){
                    teacherSelectInfo_choose=teacherSelectInfo_record;
                }else{
                    teacherSelectInfo_choose=new TeacherSelectInfo();
                }

                if(teacherFragment!=null){

                    Map<Integer, Boolean> teacher_isChecked=null; //教师选择集
                    teacher_isChecked=teacherFragment.getIsChecked();
                    if(choosedTeacher1!=null){
                        Log.e("null3size", choosedTeacher1.size()+"");



                        teacher_isChecked.clear();
                        for (TeacherMap t :choosedTeacher1) {
                            teacher_isChecked.put(t.getId(),true);
                        }
                        teacherFragment.setIsChecked(teacher_isChecked);
            //           teacherFragment.adapter.notifyDataSetChanged();

                    }else{
                        Log.e("null1","null1");

                          teacherFragment.setIsChecked(new HashMap<Integer, Boolean>());
                        //  teacherFragment.adapter.notifyDataSetChanged();



                    }
                }



            }

            if(type==2){//代表邮件 抄送
                TeacherSelectInfo CC_teacherSelectInfo_record=(TeacherSelectInfo) bundle.getSerializable("CC_teacherSelectInfo_record");
                isSelectAll=bundle.getBoolean("CC_selectAll");; //全体教职工
                schoolleader=bundle.getBoolean("CC_school_leader"); //校级领导
                departleadr=bundle.getBoolean("CC_departmentcharge"); //部门负责
               if(CC_teacherSelectInfo_record!=null){
                   teacherSelectInfo_choose=CC_teacherSelectInfo_record;
               }else{
                   teacherSelectInfo_choose=new TeacherSelectInfo();

               }
                List<TeacherMap>  choosedTeacher2 = (List<TeacherMap>) bundle.getSerializable("CCteacher");//教师选择

                if(teacherFragment!=null){
                   //教师选择集
                    Map<Integer, Boolean> teacher_isChecked1=null;
                   teacher_isChecked1=teacherFragment.getIsChecked();
                   if(choosedTeacher2!=null){
                       Log.e("null2","null22");


                       teacher_isChecked1.clear();
                       for (TeacherMap t :choosedTeacher2) {
                           teacher_isChecked1.put(t.getId(),true);
                       }
          //             teacherFragment.adapter.notifyDataSetChanged();
                   }else{
                       Log.e("null","null");


                       teacherFragment.setIsChecked(new HashMap<Integer, Boolean>());
           //            teacherFragment.adapter.notifyDataSetChanged();
                   }

               }




            }
         /*   if(type==3){//代表总结

            }
            if(type==4){//代表通知

            }
            if(type==4){//代表日志

            }*/



        }
    }

    private void initEvent() {
        chooseDept.setOnClickListener(new OnClickListener() {   //选择部门

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                placeView(0,0,false);
            }

        });
        chooseTeacher.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				getAllTeacherFromServer();
                placeView(1,1,false);
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
                returnTeacher();
            }

        });

        lookSelected.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //newActivity(LookSelectedActivity.class, null);
                Intent intent = new Intent(ChooseTeacherActivity.this, LookSelectedActivity.class);
                Bundle bundle = new Bundle();
                if (teacherFragment != null)
                    //chooseTeacherList = teacherFragment.getChooseTeacherList();
                   try{
                       bundle.putSerializable("teacher", (Serializable) teacherFragment.getChooseTeacherList());
                   } catch (Exception e){
                       DataUtil.getToast("数据加载未完成，请稍后点击...");
                       return;
                   }

                if (deptfragment != null)
                    bundle.putSerializable("choose_list",teacherSelectInfo_choose);

                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE_FOR_SELECTED_TEACHER);
            }
        });
    }

    //返回结果给调用它的界面
    public void returnTeacher() {
        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        if (deptfragment != null) {
            bundle.putSerializable("allDept", deptfragment.getAllChoosedList());
            bundle.putSerializable("choose_list",teacherSelectInfo_choose); //选择集合
        } else {
            bundle.putSerializable("allDept", choosedList);
        }



        if (teacherFragment != null) {
            try {
                chooseTeacherList = teacherFragment.getChooseTeacherList();
            }catch (Exception e){
                DataUtil.getToastShort("网络有点慢，请稍后再点确定");
                return ;
            }
            bundle.putSerializable("teacher", (Serializable) chooseTeacherList);
        } else {
            bundle.putSerializable("teacher", (Serializable) choosedTeacher);
        }
        intent.putExtras(bundle);
        //intent.setClass(ChooseTeacherActivity.this, PublishNotifyActivity.class);
        //startActivity(intent);
        setResult(RESULT_OK, intent);
        finish();
    }

    //选择教师的fragment
    public   void placeView(int index,int id,boolean ischeck) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(index + "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (fragment == null) {
            switch (index) {
                case 0: //部门返回 teacherSelectInfo_choose
                    if(id==TEACHER_LIST){//说明是从班主任返回的 1
                        //检查班主任返回
                        if(teacherSelectInfo_choose.getInfo().getGradesTeacher().size()<teacherSelectInfo.getInfo().getGradesTeacher().size()&&teacherSelectInfo_choose.getInfo().getGradesTeacher().size()>0){
                            //代表未全选 但是选了 某些 部分选择图标
                            Log.e("choose","未全选");
                        }else if(teacherSelectInfo_choose.getInfo().getGradesTeacher().size()==teacherSelectInfo.getInfo().getGradesTeacher().size()){
                            //全选  换全选图标
                            Log.e("choose","全选");
                        }else if(teacherSelectInfo_choose.getInfo().getGradesTeacher().size()==0){
                            //未选  checkbox 为未选
                            Log.e("choose","未选");
                        }
                    }

                    deptfragment = (ChooseDeptFragment) (fragment = ChooseDeptFragment.newInstance(index, list, choosedList));
                    chooseDept.setSelected(true);
                    chooseTeacher.setSelected(false);
                    break;
                case 1:
                    teacherFragment = (ChooseTeacherFragment) (fragment = ChooseTeacherFragment.newInstance(index, teacherList, choosedTeacher));
                    chooseTeacher.setSelected(true);
                    chooseDept.setSelected(false);
                    break;
                case 2:

                    lowerFragment = (ChooseLowerFragment) (fragment = ChooseLowerFragment.newInstance(index,id, ischeck));
                    break;
                default:
                    break;
            }
        }
        ft.replace(R.id.maincontent, fragment, index + "");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    //选择第3级的fragment
    public  void placeViewLowerToLower(int index,int id,boolean ischeck,String groups,String title,int position,Object object) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(index + "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            switch (index) {


                case 3:
                    chooseLowerToLowerFragment = (ChooseLowerToLowerFragment) (fragment = ChooseLowerToLowerFragment.newInstance(index,id,ischeck,groups,title,position, object));
                    break;
                default:
                    break;
            }
        }
        ft.replace(R.id.maincontent, fragment, index + "");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //ft.addToBackStack(null);
        ft.commit();
    }
    //选择教师的fragment
    public   void placeViewThreeReturnTwo(int index,int id,int  type) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(index + "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            switch (index) {



                case 2:
                    lowerFragment = (ChooseLowerFragment) (fragment = ChooseLowerFragment.newInstance(index,id,type)); //1代表年级/班级

                    break;

                default:
                    break;
            }
        }
        ft.replace(R.id.maincontent, fragment, index + "");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //ft.addToBackStack(null);
        ft.commit();
    }




    @Override
    protected void getMessage(String data) {
        // TODO Auto-generated method stub
        System.out.print("data: =========" + data);
        switch (action) {
            case GET_USER_GROUP:

                  parseAllDept(data);
                  placeView(0,0,false);
                break;
            case GET_ALL_TEACHER:

                parseAllTeacher(data);
                placeView(1,1,false);
                break;
        }
    }

    private void getAllDeptFromServer() {


        JSONObject json = new JSONObject();
        queryPost(Constants.GET_USER_GROUP, json);
        action = GET_USER_GROUP;

    }

    private void parseAllDept(String data) {
        System.out.println("获取部门数据:" + data);
         teacherSelectInfo= JsonUtils.fromJson(data, TeacherSelectInfo.class);

//删除无下级数据的  item
        //年级/班级
        Iterator<TeacherSelectInfo.InfoBean.ClassesBeanX> stuIter = teacherSelectInfo.getInfo().getClasses().iterator();
        while (stuIter.hasNext()) {
            TeacherSelectInfo.InfoBean.ClassesBeanX student = stuIter.next();
            if (student.getClasses().size() == 0){
                stuIter.remove();
            }//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
        }
        //教研组
        Iterator<TeacherSelectInfo.InfoBean.GroupsBean> stuIter1 = teacherSelectInfo.getInfo().getGroups().iterator();
        while (stuIter1.hasNext()) {
            TeacherSelectInfo.InfoBean.GroupsBean student = stuIter1.next();
            if (student.getTeacherGroups().size() == 0){
                Log.e("chooseTeacherActivity",student.getName());
                stuIter1.remove();
            }//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
        }

        //备课组
        Iterator<TeacherSelectInfo.InfoBean.PrepareLessionBean> stuIter2 = teacherSelectInfo.getInfo().getPrepareLession().iterator();
        while (stuIter2.hasNext()) {
            TeacherSelectInfo.InfoBean.PrepareLessionBean student = stuIter2.next();
            if (student.getPrepareLssions().size() == 0){
                stuIter2.remove();
            }//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
        }

          if(teacherSelectInfo_choose==null){
    teacherSelectInfo_choose=new TeacherSelectInfo();
    }

        //填充常用分组数据
         fillCommonGroup();
        //填充教学机构数据
         fillGradeGroup();
    }

    private void fillGradeGroup() {
        gradeList = new ArrayList<GradeMap>();
        for (int i = 0; i < gradeName.length; i++) {
            GradeMap grade = list.new GradeMap();
            grade.setId(i);
            grade.setName(gradeName[i][0]);
            grade.setGradeCode(gradeName[i][1]);
            gradeList.add(grade);
        }
        list.setGradeMapList(gradeList);
    }

    private void fillCommonGroup() {
        commonList = new ArrayList<CommonGroup>();
        for (int i = 0; i < commonName.length; i++) {
            CommonGroup common = list.new CommonGroup();
            common.setId(i);
            common.setName(commonName[i][0]);
            common.setCode(commonName[i][1]);
            commonList.add(common);
        }
        list.setCommonList(commonList);
    }

    private void getAllTeacherFromServer() {
        JSONObject json = new JSONObject();
        queryPost(Constants.GET_ALL_TEACHER, json);
        action = GET_ALL_TEACHER;
    }



    private void parseAllTeacher(String data) {
        System.out.println("parseAllTeacher:" + data);

        teacherList = JsonUtils.fromJson(data, AllTeacherList.class);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(record_fragment instanceof  ChooseDeptFragment){ //1级页面
                 finish();
            }

            if(record_fragment instanceof  ChooseLowerFragment){ //2级页面
                         //跳转1级页面
               ((ChooseLowerFragment) record_fragment).onBack();


            }
            if(record_fragment instanceof  ChooseLowerToLowerFragment){ //3级页面
                         //跳转2级页面
                ((ChooseLowerToLowerFragment) record_fragment).onBack();
            }



        }
              return false;
    }




    public static <T> T deepCopy(T src) throws IOException, ClassNotFoundException{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in =new ObjectInputStream(byteIn);
        T dest = (T) in.readObject();
        return dest;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && requestCode == REQUEST_CODE_FOR_SELECTED_TEACHER){
            Bundle bundle = data.getExtras();
//            chooseTeacherList = (List<AllTeacherList.TeacherMap>) bundle.getSerializable("teacher");
//            if(teacherFragment != null) {
//                teacherFragment.setData(chooseTeacherList);
//            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
