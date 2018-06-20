/*
 * 文 件 名:ChooseDeptFragment.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-08
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.chooseContact;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.AllDeptList;
import com.wcyc.zigui2.newapp.bean.AllDeptList.CommonGroup;
import com.wcyc.zigui2.newapp.bean.AllDeptList.ContactGroupMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.DepMap;
import com.wcyc.zigui2.newapp.bean.AllDeptList.GradeMap;
import com.wcyc.zigui2.newapp.bean.TeacherSelectInfo;
import com.wcyc.zigui2.widget.MyListView;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
//选择部门fragment
public class ChooseLowerFragment extends Fragment{
    private AllDeptList list,choosedAllList,showList;//已经选择的项
    /** deptListView 行政机构
     * <p/> gradeListView 年级列表（教学机构）
     * <p/> commonListView 常用分组
     * <p/> contactListView 我的分组
     * */
    private  int id=0;   //代表分组类型 1代表 班主任
    private  int type=0;   //代表分组类型 回显 类型 教研组3 备课组2 年级/班级 1
    public   static boolean ischecked=false;
    public   static boolean is_select_all=true;
    private List<Object> datas;
    private View view;
    private MyListView listView;
    private  TextView top_title,school;
    private  ChooseTeacherActivity activity;
    private  ListAdapter listadapter;
    private RelativeLayout select_all;  //全选 item
    private CheckBox selectall_checkbox;  //全选 item
    private static  float scrllo_y,scrllo_x; //记录 scrlloview 的坐标
    private ScrollView scrollView;



    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle){
        view = inflater.inflate(R.layout.fragment_choose_lower, null);
        initView();
       initEvent();
        return view;
    }



    public ChooseLowerFragment(){

    }

    private boolean checkbox_ischecked=false;
    private void initEvent() {
        school.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.placeView(0,1,false);  //返回部门  id=1 说明是返回的
            }
        });
        select_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //拿到 先清空 在添加
               /* if(selectall_checkbox.isChecked()){
                    selectall_checkbox.setChecked(false);
                    activity.teacherSelectInfo_choose.getInfo().getClasses().clear();
                    listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getClasses());
                    listView.setAdapter(listadapter);
                }else {
                    selectall_checkbox.setChecked(true);
                    if(id==activity.CLASSES){
                        activity.teacherSelectInfo_choose.getInfo().getClasses().clear();//先清空一边
                        try {
                            activity.teacherSelectInfo_choose.getInfo().getClasses().addAll(deepCopy(activity.teacherSelectInfo.getInfo().getClasses()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getClasses());
                        listView.setAdapter(listadapter);
                    }


                }*/

                if(selectall_checkbox.isChecked()){
                    selectall_checkbox.setChecked(false);
                    ischecked=false;
                   if(id==activity.CLASSES){ //年级/班级


                        activity.teacherSelectInfo_choose.getInfo().getClasses().clear();
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getClasses());
                        listView.setAdapter(listadapter);

                    }
                    if(id==activity.PREPARELESSION){//备课组
                        activity.teacherSelectInfo_choose.getInfo().getPrepareLession().clear();
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getPrepareLession());
                        listView.setAdapter(listadapter);

                    }
                    if(id==activity.JIAOYAN_TEACHER_LIST){//教研组
                        activity.teacherSelectInfo_choose.getInfo().getGroups().clear();
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getGroups());
                        listView.setAdapter(listadapter);

                    }
                    if(id==activity.JIYAOYAN_LEADER_LIST){//教研组长
                        activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().clear();
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getGroupsLeader());
                        listView.setAdapter(listadapter);

                    }
                    if(id==activity.TEACHER_LIST){//班主任

                        activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();
                       Log.e("size", activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().size()+"");
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getGradesTeacher());
                        listView.setAdapter(listadapter);

                    }
                    if(id==activity.PREPARE_LESSION_LEADER){//备课组长
                        activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().clear();
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getPrepareLessionLeader());
                        listView.setAdapter(listadapter);

                    }
                    if(id==activity.GRADE_GROUP){//备课组长
                        activity.teacherSelectInfo_choose.getInfo().getGradeGroup().clear();
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getGradeGroup());
                        listView.setAdapter(listadapter);

                    }


                }else {
                    selectall_checkbox.setChecked(true);
                    if(id==activity.CLASSES){
                        ischecked=true;
                        activity.teacherSelectInfo_choose.getInfo().getClasses().clear();//先清一遍

                        for (TeacherSelectInfo.InfoBean.ClassesBeanX  g:
                                activity.teacherSelectInfo.getInfo().getClasses()) {
                            //添加的时候 复制集合
                            TeacherSelectInfo.InfoBean.ClassesBeanX classesBeanX=null;
                            try {
                                classesBeanX=deepCopy(g);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            activity.teacherSelectInfo_choose.getInfo().getClasses().add(classesBeanX);
                        }
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getClasses());
                        listView.setAdapter(listadapter);
                    }
                    if(id==activity.PREPARELESSION){
                        ischecked=true;
                        activity.teacherSelectInfo_choose.getInfo().getPrepareLession().clear();//先清一遍

                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean  g:
                                activity.teacherSelectInfo.getInfo().getPrepareLession()) {
                            //添加的时候 复制集合
                            TeacherSelectInfo.InfoBean.PrepareLessionBean prepareLessionBean=null;
                            try {
                                prepareLessionBean=deepCopy(g);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            activity.teacherSelectInfo_choose.getInfo().getPrepareLession().add(prepareLessionBean);
                        }
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getPrepareLession());
                        listView.setAdapter(listadapter);
                    }
                    if(id==activity.JIAOYAN_TEACHER_LIST){
                        ischecked=true;
                        activity.teacherSelectInfo_choose.getInfo().getGroups().clear();//先清一遍

                        for (TeacherSelectInfo.InfoBean.GroupsBean  g:
                                activity.teacherSelectInfo.getInfo().getGroups()) {
                            //添加的时候 复制集合
                            TeacherSelectInfo.InfoBean.GroupsBean groupsBean=null;
                            try {
                                groupsBean=deepCopy(g);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            activity.teacherSelectInfo_choose.getInfo().getGroups().add(groupsBean);
                        }
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getGroups());
                        listView.setAdapter(listadapter);
                    }
                    if(id==activity.TEACHER_LIST){
                        ischecked=true;
                        activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();//先清一遍

                        for (TeacherSelectInfo.InfoBean.GradesTeacherBean  g:
                                activity.teacherSelectInfo.getInfo().getGradesTeacher()) {


                            activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().add(g);
                        }
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getGradesTeacher());
                        listView.setAdapter(listadapter);
                    }
                    if(id==activity.JIYAOYAN_LEADER_LIST){
                        ischecked=true;
                        activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().clear();//先清一遍

                        for (TeacherSelectInfo.InfoBean.GroupsLeaderBean  g:
                                activity.teacherSelectInfo.getInfo().getGroupsLeader()) {

                            activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().add(g);
                        }
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getGroupsLeader());
                        listView.setAdapter(listadapter);
                    }
                    if(id==activity.PREPARE_LESSION_LEADER){
                        ischecked=true;
                        activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().clear();//先清一遍

                        for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean  g:
                                activity.teacherSelectInfo.getInfo().getPrepareLessionLeader()) {


                            activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().add( g);
                        }
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getPrepareLessionLeader());
                        listView.setAdapter(listadapter);
                    }
                    if(id==activity.GRADE_GROUP){
                        ischecked=true;
                        activity.teacherSelectInfo_choose.getInfo().getGradeGroup().clear();//先清一遍

                        for (TeacherSelectInfo.InfoBean.GradeGroupBean  g:
                                activity.teacherSelectInfo.getInfo().getGradeGroup()) {


                            activity.teacherSelectInfo_choose.getInfo().getGradeGroup().add( g);
                        }
                        listadapter=new ListAdapter( activity.teacherSelectInfo.getInfo().getGradeGroup());
                        listView.setAdapter(listadapter);
                    }

                }



            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id_) {
             //子项点击 时间
                ListAdapter.ViewHolder holder= (ListAdapter.ViewHolder) view.getTag();
                if(holder.checkBox.getTag()!=null){
                    Log.e("teacherSelectInfochoose","+");
                    holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                    holder.checkBox.setChecked(true);
                    // 根据对应id 添加数据进去

                    //备课组
                    if(id==activity.PREPARELESSION){
                        TeacherSelectInfo.InfoBean.PrepareLessionBean prepareLessionBean= null;
                        try {
                            prepareLessionBean = deepCopy((TeacherSelectInfo.InfoBean.PrepareLessionBean) holder.object);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Iterator<TeacherSelectInfo.InfoBean.PrepareLessionBean> prepareLessionBeanIterator= activity.teacherSelectInfo_choose.getInfo().getPrepareLession().iterator();
                        while (prepareLessionBeanIterator.hasNext()){
                            TeacherSelectInfo.InfoBean.PrepareLessionBean prepareLessionBean1=   prepareLessionBeanIterator.next();
                            if(prepareLessionBean1.getName().equals(prepareLessionBean.getName())){
                                prepareLessionBeanIterator.remove();
                            }
                        }
                        activity.teacherSelectInfo_choose.getInfo().getPrepareLession().add(prepareLessionBean);
                        Log.e("getPrepareLession",activity.teacherSelectInfo_choose.getInfo().getPrepareLession().size()+"");
                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean h: activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {

                            Log.e("getPrepareLession",h.getName());
                        }
                        //判断是否全选 选一个全的时候
                        int size=0;
                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean c:activity.teacherSelectInfo.getInfo().getPrepareLession()) {
                            size++;
                            for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX g:c.getPrepareLssions()) {
                                size++;
                            }
                        }
                        int choose_size=0;
                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean c:activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                            choose_size++;
                            for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX g:c.getPrepareLssions()) {
                                choose_size++;
                            }
                        }

                        if(choose_size==size){
                            //全选状态
                            selectall_checkbox.setChecked(true);
                        }else{
                            selectall_checkbox.setChecked(false);
                        }


                    }
                    //教研组
                    if(id==activity.JIAOYAN_TEACHER_LIST){
                        TeacherSelectInfo.InfoBean.GroupsBean groupsBean= null;
                        try {
                            groupsBean = deepCopy((TeacherSelectInfo.InfoBean.GroupsBean) holder.object);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Iterator<TeacherSelectInfo.InfoBean.GroupsBean> groupsBeanIterator= activity.teacherSelectInfo_choose.getInfo().getGroups().iterator();
                        while (groupsBeanIterator.hasNext()){
                            TeacherSelectInfo.InfoBean.GroupsBean groupsBean1=   groupsBeanIterator.next();
                            if(groupsBean1.getName().equals(groupsBean.getName())){
                                groupsBeanIterator.remove();
                            }
                        }
                        activity.teacherSelectInfo_choose.getInfo().getGroups().add(groupsBean);
                        Log.e("groupsBean",activity.teacherSelectInfo_choose.getInfo().getGroups().size()+"");
                        for (TeacherSelectInfo.InfoBean.GroupsBean h: activity.teacherSelectInfo_choose.getInfo().getGroups()) {

                            Log.e("groupsBean",h.getName());
                        }

                        //  断是否全选 选一个全的时候
                        int size=0;
                        for (TeacherSelectInfo.InfoBean.GroupsBean c:activity.teacherSelectInfo.getInfo().getGroups()) {
                            size++;
                            for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX g:c.getTeacherGroups()) {
                                size++;
                            }
                        }
                        int choose_size=0;
                        for (TeacherSelectInfo.InfoBean.GroupsBean c:activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                            choose_size++;
                            for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX g:c.getTeacherGroups()) {
                                choose_size++;
                            }
                        }

                        if(choose_size==size){
                            //全选状态
                            selectall_checkbox.setChecked(true);
                        }else{
                            selectall_checkbox.setChecked(false);
                        }

                    }
                    //年级/班级
                    if(id==activity.CLASSES){
                        TeacherSelectInfo.InfoBean.ClassesBeanX classesBeanX1= null;
                        try {
                            classesBeanX1 = deepCopy((TeacherSelectInfo.InfoBean.ClassesBeanX) holder.object);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       Iterator<TeacherSelectInfo.InfoBean.ClassesBeanX> classesBeanXIterator= activity.teacherSelectInfo_choose.getInfo().getClasses().iterator();
                       while (classesBeanXIterator.hasNext()){
                           TeacherSelectInfo.InfoBean.ClassesBeanX classesBeanX=   classesBeanXIterator.next();
                           if(classesBeanX.getName().equals(classesBeanX1.getName())){
                               classesBeanXIterator.remove();
                           }
                       }
                        activity.teacherSelectInfo_choose.getInfo().getClasses().add(classesBeanX1);
                        Log.e("getClasses",activity.teacherSelectInfo_choose.getInfo().getClasses().size()+"");
                        for (TeacherSelectInfo.InfoBean.ClassesBeanX h: activity.teacherSelectInfo_choose.getInfo().getClasses()) {

                            Log.e("getClasses",h.getName());
                        }
//判断是否全选 选一个全的时候
                        int size=0;
                        for (TeacherSelectInfo.InfoBean.ClassesBeanX c:activity.teacherSelectInfo.getInfo().getClasses()) {
                            size++;
                            for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean g:c.getClasses()) {
                                size++;
                            }
                        }
                        int choose_size=0;
                        for (TeacherSelectInfo.InfoBean.ClassesBeanX c:activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                            choose_size++;
                            for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean g:c.getClasses()) {
                                choose_size++;
                            }
                        }

                        if(choose_size==size){
                            //全选状态
                            selectall_checkbox.setChecked(true);
                        }else{
                            selectall_checkbox.setChecked(false);
                        }
                    }

                    holder.checkBox.setTag(null);
                }else{
                    if(holder.checkBox.isChecked()){
                        holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                        holder.checkBox.setChecked(false);
                        selectall_checkbox.setChecked(false);
                        // 根据对应id 删除数据进去  第一次进来 是空的
                        //班主任
                        if(id==activity.TEACHER_LIST){
                            Log.e("teacherSelectInfochoose","-");
                            TeacherSelectInfo.InfoBean.GradesTeacherBean bean=   (TeacherSelectInfo.InfoBean.GradesTeacherBean) holder.object;
                            Iterator<TeacherSelectInfo.InfoBean.GradesTeacherBean> stuIter2 =  activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().iterator();
                            while (stuIter2.hasNext()) {
                                TeacherSelectInfo.InfoBean.GradesTeacherBean  gradesTeacherBean = stuIter2.next();
                                if (gradesTeacherBean.getName().equals(bean.getName())){
                                    stuIter2.remove();
                                }//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
                            }                    //如果下级还有下级 记得 清空
                            if(activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().size()==0){
                                ischecked=false;
                            }
                            Log.e("teacherSelectInfochoose",activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().size()+"");
                            for (TeacherSelectInfo.InfoBean.GradesTeacherBean h: activity.teacherSelectInfo_choose.getInfo().getGradesTeacher()) {

                                Log.e("teacherSelectInfochoose",h.getName());
                            }

                        }

                        //教研组长  还有下级
                        if(id==activity.JIYAOYAN_LEADER_LIST){
                            Log.e("GroupsLeaderBean","-");
                            TeacherSelectInfo.InfoBean.GroupsLeaderBean bean=   (TeacherSelectInfo.InfoBean.GroupsLeaderBean) holder.object;
                            Iterator<TeacherSelectInfo.InfoBean.GroupsLeaderBean> stuIter2 =  activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().iterator();
                            while (stuIter2.hasNext()) {
                                TeacherSelectInfo.InfoBean.GroupsLeaderBean groupsLeaderBean = stuIter2.next();
                                if (groupsLeaderBean.getName().equals(bean.getName())){
                                    stuIter2.remove();
                                }//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
                            }


                            if(activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().size()==0){
                                ischecked=false;
                            }

                            //如果下级还有下级 记得 清空 下级集合
                            Log.e("GroupsLeaderBean",activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().size()+"");
                            for (TeacherSelectInfo.InfoBean.GroupsLeaderBean h: activity.teacherSelectInfo_choose.getInfo().getGroupsLeader()) {

                                Log.e("GroupsLeaderBean",h.getName());
                            }
                        }

                        //年级组长
                        if(id==activity.GRADE_GROUP){
                            Log.e("getGradeGroup","-");
                            TeacherSelectInfo.InfoBean.GradeGroupBean bean=   (TeacherSelectInfo.InfoBean.GradeGroupBean) holder.object;
                            Iterator<TeacherSelectInfo.InfoBean.GradeGroupBean> stuIter2 =  activity.teacherSelectInfo_choose.getInfo().getGradeGroup().iterator();
                            while (stuIter2.hasNext()) {
                                TeacherSelectInfo.InfoBean.GradeGroupBean gradeGroupBean = stuIter2.next();
                                if (gradeGroupBean.getName().equals(bean.getName())){
                                    stuIter2.remove();
                                }//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
                            }
                            if(activity.teacherSelectInfo_choose.getInfo().getGradeGroup().size()==0){
                                ischecked=false;
                            }

                            //如果下级还有下级 记得 清空
                            Log.e("getGradeGroup",activity.teacherSelectInfo_choose.getInfo().getGradeGroup().size()+"");
                            for (TeacherSelectInfo.InfoBean.GradeGroupBean h: activity.teacherSelectInfo_choose.getInfo().getGradeGroup()) {

                                Log.e("getGradeGroup",h.getName());
                            }
                        }
                        //备课组长
                        if(id==activity.PREPARE_LESSION_LEADER){
                            Log.e("getPrepareLessionLeader","-");
                            TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean bean=   (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean) holder.object;
                            Iterator<TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean> stuIter2 =  activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().iterator();
                            while (stuIter2.hasNext()) {
                                TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean prepareLessionLeaderBean = stuIter2.next();
                                if (prepareLessionLeaderBean.getName().equals(bean.getName())){
                                    stuIter2.remove();
                                }//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
                            }
                            if(activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().size()==0){
                                ischecked=false;
                            }

                            //如果下级还有下级 记得 清空
                            Log.e("getPrepareLessionLeader",activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().size()+"");
                            for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean h: activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader()) {

                                Log.e("getPrepareLessionLeader",h.getName());
                            }
                        }
                        //备课组  还有下级
                        if(id==activity.PREPARELESSION){
                            Log.e("getPrepareLession","-");
                            TeacherSelectInfo.InfoBean.PrepareLessionBean bean=  (TeacherSelectInfo.InfoBean.PrepareLessionBean) holder.object;
                            int i=0;
                            for (int b=0;b<activity.teacherSelectInfo_choose.getInfo().getPrepareLession().size();b++) {
                                if( activity.teacherSelectInfo_choose.getInfo().getPrepareLession().get(b).getName().equals(bean.getName())){

                                    i=b;
                                    Log.e("getPrepareLession--",bean.getName()+"--"+b);
                                }
                            }
                            if(  activity.teacherSelectInfo_choose.getInfo().getPrepareLession().size()!=0){
                                activity.teacherSelectInfo_choose.getInfo().getPrepareLession().remove(i);
                            }
                            if(activity.teacherSelectInfo_choose.getInfo().getPrepareLession().size()==0){

                                ischecked=false;
                            }


                            Log.e("getPrepareLession",activity.teacherSelectInfo_choose.getInfo().getPrepareLession().size()+"");
                            for (TeacherSelectInfo.InfoBean.PrepareLessionBean h: activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {

                                Log.e("getPrepareLession",h.getName());
                            }
                        }
                        //教研组 还有下级
                        if(id==activity.JIAOYAN_TEACHER_LIST){
                            Log.e("GroupsBean","-");
                            TeacherSelectInfo.InfoBean.GroupsBean bean=  (TeacherSelectInfo.InfoBean.GroupsBean) holder.object;
                            int i=0;
                            for (int b=0;b<activity.teacherSelectInfo_choose.getInfo().getGroups().size();b++) {
                                if( activity.teacherSelectInfo_choose.getInfo().getGroups().get(b).getName().equals(bean.getName())){

                                    i=b;
                                    Log.e("GroupsBean--",bean.getName()+"--"+b);
                                }
                            }
                            if(  activity.teacherSelectInfo_choose.getInfo().getGroups().size()!=0){
                                activity.teacherSelectInfo_choose.getInfo().getGroups().remove(i);
                            }
                            if(activity.teacherSelectInfo_choose.getInfo().getGroups().size()==0){
                                Log.e("GroupsBean-",activity.teacherSelectInfo_choose.getInfo().getGroups().size()+"");
                                ischecked=false;
                            }

                            Log.e("GroupsBean",activity.teacherSelectInfo_choose.getInfo().getGroups().size()+"");
                            for (TeacherSelectInfo.InfoBean.GroupsBean h: activity.teacherSelectInfo_choose.getInfo().getGroups()) {

                                Log.e("GroupsBean",h.getName());
                            }
                        }
                        //年级/班级   还有下级
                        if(id==activity.CLASSES){
                            Log.e("getClasses","-");
                            TeacherSelectInfo.InfoBean.ClassesBeanX bean=  (TeacherSelectInfo.InfoBean.ClassesBeanX) holder.object;
                            int i=0;
                            for (int b=0;b<activity.teacherSelectInfo_choose.getInfo().getClasses().size();b++) {
                                if( activity.teacherSelectInfo_choose.getInfo().getClasses().get(b).getName().equals(bean.getName())){

                                    i=b;
                                    Log.e("getClasses--",bean.getName()+"--"+b);
                                }
                            }
                            if(  activity.teacherSelectInfo_choose.getInfo().getClasses().size()!=0){
                                activity.teacherSelectInfo_choose.getInfo().getClasses().remove(i);

                            }

                            if(activity.teacherSelectInfo_choose.getInfo().getClasses().size()==0){
                                Log.e("getClassesSIZE-",activity.teacherSelectInfo_choose.getInfo().getClasses().size()+"");
                                ischecked=false;
                            }


                            Log.e("getClasses",activity.teacherSelectInfo_choose.getInfo().getClasses().size()+"");
                            for (TeacherSelectInfo.InfoBean.ClassesBeanX h: activity.teacherSelectInfo_choose.getInfo().getClasses()) {

                                Log.e("getClasses",h.getName());
                            }



                        }


                    }else{
                        Log.e("teacherSelectInfochoose","+");

                        holder.checkBox.setChecked(true);
                        // 根据对应id 添加数据进去
                        //班主任
                        if(id==activity.TEACHER_LIST){

                            activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().add((TeacherSelectInfo.InfoBean.GradesTeacherBean) holder.object);
                            Log.e("teacherSelectInfochoose",activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().size()+"");
                            for (TeacherSelectInfo.InfoBean.GradesTeacherBean h: activity.teacherSelectInfo_choose.getInfo().getGradesTeacher()) {

                                Log.e("teacherSelectInfochoose",h.getName());
                            }
                            //判断是否全选 选一个全的时候
                            int size=0;
                            for (TeacherSelectInfo.InfoBean.GradesTeacherBean c:activity.teacherSelectInfo.getInfo().getGradesTeacher()) {
                                size++;


                            }
                            int choose_size=0;
                            for (TeacherSelectInfo.InfoBean.GradesTeacherBean c:activity.teacherSelectInfo_choose.getInfo().getGradesTeacher()) {
                                choose_size++;

                            }

                            if(choose_size==size){
                                //全选状态
                                selectall_checkbox.setChecked(true);
                            }else{
                                selectall_checkbox.setChecked(false);
                            }
                        }



                        //教研组长
                        if(id==activity.JIYAOYAN_LEADER_LIST){
                            activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().add((TeacherSelectInfo.InfoBean.GroupsLeaderBean) holder.object);
                            Log.e("getGroupsLeader",activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().size()+"");
                            for (TeacherSelectInfo.InfoBean.GroupsLeaderBean h: activity.teacherSelectInfo_choose.getInfo().getGroupsLeader()) {

                                Log.e("getGroupsLeader",h.getName());
                            }
                            //判断是否全选 选一个全的时候
                            int size=0;
                            for (TeacherSelectInfo.InfoBean.GroupsLeaderBean c:activity.teacherSelectInfo.getInfo().getGroupsLeader()) {
                                size++;


                            }
                            int choose_size=0;
                            for (TeacherSelectInfo.InfoBean.GroupsLeaderBean c:activity.teacherSelectInfo_choose.getInfo().getGroupsLeader()) {
                                choose_size++;

                            }

                            if(choose_size==size){
                                //全选状态
                                selectall_checkbox.setChecked(true);
                            }else{
                                selectall_checkbox.setChecked(false);
                            }
                        }
                        //年级组长
                        if(id==activity.GRADE_GROUP){
                            activity.teacherSelectInfo_choose.getInfo().getGradeGroup().add((TeacherSelectInfo.InfoBean.GradeGroupBean) holder.object);
                            Log.e("getGroupsLeader",activity.teacherSelectInfo_choose.getInfo().getGradeGroup().size()+"");
                            for (TeacherSelectInfo.InfoBean.GradeGroupBean h: activity.teacherSelectInfo_choose.getInfo().getGradeGroup()) {

                                Log.e("getGroupsLeader",h.getName());
                            }
                            //判断是否全选 选一个全的时候
                            int size=0;
                            for (TeacherSelectInfo.InfoBean.GradeGroupBean c:activity.teacherSelectInfo.getInfo().getGradeGroup()) {
                                size++;


                            }
                            int choose_size=0;
                            for (TeacherSelectInfo.InfoBean.GradeGroupBean c:activity.teacherSelectInfo_choose.getInfo().getGradeGroup()) {
                                choose_size++;

                            }

                            if(choose_size==size){
                                //全选状态
                                selectall_checkbox.setChecked(true);
                            }else{
                                selectall_checkbox.setChecked(false);
                            }
                        }
                        //备课组长
                        if(id==activity.PREPARE_LESSION_LEADER){
                            activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().add((TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean) holder.object);
                            Log.e("getPrepareLessionLeader",activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().size()+"");
                            for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean h: activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader()) {

                                Log.e("getPrepareLessionLeader",h.getName());
                            }
                            //判断是否全选 选一个全的时候
                            int size=0;
                            for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean c:activity.teacherSelectInfo.getInfo().getPrepareLessionLeader()) {
                                size++;


                            }
                            int choose_size=0;
                            for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean c:activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader()) {
                                choose_size++;

                            }

                            if(choose_size==size){
                                //全选状态
                                selectall_checkbox.setChecked(true);
                            }else{
                                selectall_checkbox.setChecked(false);
                            }

                        }
                        //备课组
                        if(id==activity.PREPARELESSION){
                            TeacherSelectInfo.InfoBean.PrepareLessionBean prepareLessionBean= null;
                            try {
                                prepareLessionBean = deepCopy((TeacherSelectInfo.InfoBean.PrepareLessionBean) holder.object);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            activity.teacherSelectInfo_choose.getInfo().getPrepareLession().add(prepareLessionBean);
                            Log.e("getPrepareLession",activity.teacherSelectInfo_choose.getInfo().getPrepareLession().size()+"");
                            for (TeacherSelectInfo.InfoBean.PrepareLessionBean h: activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {

                                Log.e("getPrepareLession",h.getName());
                            }
                            //判断是否全选 选一个全的时候
                            int size=0;
                            for (TeacherSelectInfo.InfoBean.PrepareLessionBean c:activity.teacherSelectInfo.getInfo().getPrepareLession()) {
                                size++;
                                for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX g:c.getPrepareLssions()) {
                                    size++;
                                }
                            }
                            int choose_size=0;
                            for (TeacherSelectInfo.InfoBean.PrepareLessionBean c:activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                                choose_size++;
                                for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX g:c.getPrepareLssions()) {
                                    choose_size++;
                                }
                            }

                            if(choose_size==size){
                                //全选状态
                                selectall_checkbox.setChecked(true);
                            }else{
                                selectall_checkbox.setChecked(false);
                            }


                        }
                       //教研组
                        if(id==activity.JIAOYAN_TEACHER_LIST){
                            TeacherSelectInfo.InfoBean.GroupsBean groupsBean= null;
                            try {
                                groupsBean = deepCopy((TeacherSelectInfo.InfoBean.GroupsBean) holder.object);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            activity.teacherSelectInfo_choose.getInfo().getGroups().add(groupsBean);
                            Log.e("groupsBean",activity.teacherSelectInfo_choose.getInfo().getGroups().size()+"");
                            for (TeacherSelectInfo.InfoBean.GroupsBean h: activity.teacherSelectInfo_choose.getInfo().getGroups()) {

                                Log.e("groupsBean",h.getName());
                            }

                            //  断是否全选 选一个全的时候
                            int size=0;
                            for (TeacherSelectInfo.InfoBean.GroupsBean c:activity.teacherSelectInfo.getInfo().getGroups()) {
                                size++;
                                for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX g:c.getTeacherGroups()) {
                                    size++;
                                }
                            }
                            int choose_size=0;
                            for (TeacherSelectInfo.InfoBean.GroupsBean c:activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                                choose_size++;
                                for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX g:c.getTeacherGroups()) {
                                    choose_size++;
                                }
                            }

                            if(choose_size==size){
                                //全选状态
                                selectall_checkbox.setChecked(true);
                            }else{
                                selectall_checkbox.setChecked(false);
                            }

                        }
                        //年级/班级
                        if(id==activity.CLASSES){
                            TeacherSelectInfo.InfoBean.ClassesBeanX classesBeanX1= null;
                            try {
                                classesBeanX1 = deepCopy((TeacherSelectInfo.InfoBean.ClassesBeanX) holder.object);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            activity.teacherSelectInfo_choose.getInfo().getClasses().add(classesBeanX1);
                            Log.e("getClasses",activity.teacherSelectInfo_choose.getInfo().getClasses().size()+"");
                            for (TeacherSelectInfo.InfoBean.ClassesBeanX h: activity.teacherSelectInfo_choose.getInfo().getClasses()) {

                                Log.e("getClasses",h.getName());
                            }
//判断是否全选 选一个全的时候
                            int size=0;
                            for (TeacherSelectInfo.InfoBean.ClassesBeanX c:activity.teacherSelectInfo.getInfo().getClasses()) {
                                size++;
                                for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean g:c.getClasses()) {
                                    size++;
                                }
                            }
                            int choose_size=0;
                            for (TeacherSelectInfo.InfoBean.ClassesBeanX c:activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                                choose_size++;
                                for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean g:c.getClasses()) {
                                    choose_size++;
                                }
                            }

                            if(choose_size==size){
                                //全选状态
                                selectall_checkbox.setChecked(true);
                            }else{
                                selectall_checkbox.setChecked(false);
                            }
                        }
                    }
                }




            }
        });

    }
    //	private ChooseDeptFragment(AllDeptList list,AllDeptList choosedList){
//		this.list = list;
//		this.showList = choosedList;
//	}
    public static Fragment newInstance(int index ,int id,boolean ischecked) {
        // TODO Auto-generated method stub


        Fragment fragment = new ChooseLowerFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putInt("id", id);
        args.putBoolean("ischecked",ischecked);
        fragment.setArguments(args);
        //fragment.setIndex(index);
        return fragment;
    }
    public static Fragment newInstance(int index ,int id,int type) {
        // TODO Auto-generated method stub


        Fragment fragment = new ChooseLowerFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putInt("id", id);
        args.putInt("type", type);
        fragment.setArguments(args);
        //fragment.setIndex(index);
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.e("ChooseLowerFragment","onCreate");
        Bundle bundle = getArguments();
        activity= (ChooseTeacherActivity) getActivity();
        activity.setRecord_fragment(this);
        if(bundle != null){
            id = bundle.getInt("id");
            ischecked=bundle.getBoolean("ischecked");
            type=bundle.getInt("type");

        }}



    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.e("ChooseLowerFragment","onActivityCreated");
    //    ListToMap();
        initData();

    }

    @Override
    public void onPause() {
        super.onPause();
        scrllo_x=scrollView.getScrollX();
        scrllo_y=scrollView.getScrollY();


    }

    private void initView(){
        scrollView= (ScrollView) view.findViewById(R.id.scrollView);
        top_title=(TextView) view.findViewById(R.id.top_title);//上级标题
        school=(TextView) view.findViewById(R.id.school);//上级标题
        listView = (MyListView) view.findViewById(R.id.listview);//下级列表
        school.requestFocus();
        select_all=(RelativeLayout) view.findViewById(R.id.select_all);//全选按钮
        selectall_checkbox=(CheckBox) view.findViewById(R.id.choose);
        if(id==activity.CLASSES){
            int size=0;
            for (TeacherSelectInfo.InfoBean.ClassesBeanX c:activity.teacherSelectInfo.getInfo().getClasses()) {
                size++;
                for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean g:c.getClasses()) {
                    size++;
                }
            }
            int choose_size=0;
            for (TeacherSelectInfo.InfoBean.ClassesBeanX c:activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                choose_size++;
                for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean g:c.getClasses()) {
                    choose_size++;
                }
            }

            if(choose_size==size){
                //全选状态
                selectall_checkbox.setChecked(true);
            }else{
                selectall_checkbox.setChecked(false);
            }

        }
        if(id==activity.PREPARELESSION){
            int size=0;
            for (TeacherSelectInfo.InfoBean.PrepareLessionBean c:activity.teacherSelectInfo.getInfo().getPrepareLession()) {
                size++;
                for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX g:c.getPrepareLssions()) {
                    size++;
                }
            }
            int choose_size=0;
            for (TeacherSelectInfo.InfoBean.PrepareLessionBean c:activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                choose_size++;
                for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX g:c.getPrepareLssions()) {
                    choose_size++;
                }
            }

            if(choose_size==size){
                //全选状态
                selectall_checkbox.setChecked(true);
            }else{
                selectall_checkbox.setChecked(false);
            }

        }

        if(id==activity.JIAOYAN_TEACHER_LIST){
            //  断是否全选 选一个全的时候
            int size=0;
            for (TeacherSelectInfo.InfoBean.GroupsBean c:activity.teacherSelectInfo.getInfo().getGroups()) {
                size++;
                for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX g:c.getTeacherGroups()) {
                    size++;
                }
            }
            int choose_size=0;
            for (TeacherSelectInfo.InfoBean.GroupsBean c:activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                choose_size++;
                for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX g:c.getTeacherGroups()) {
                    choose_size++;
                }
            }

            if(choose_size==size){
                //全选状态
                selectall_checkbox.setChecked(true);
            }else{
                selectall_checkbox.setChecked(false);
            }

        }
        if(id==activity.TEACHER_LIST){
            //判断是否全选 选一个全的时候
            int size=0;
            for (TeacherSelectInfo.InfoBean.GradesTeacherBean c:activity.teacherSelectInfo.getInfo().getGradesTeacher()) {
                size++;


            }
            int choose_size=0;
            for (TeacherSelectInfo.InfoBean.GradesTeacherBean c:activity.teacherSelectInfo_choose.getInfo().getGradesTeacher()) {
                choose_size++;

            }
            if(choose_size==size){
                //全选状态
                selectall_checkbox.setChecked(true);
            }else{
                selectall_checkbox.setChecked(false);
            }

        }

        if(id==activity.GRADE_GROUP){
            //判断是否全选 选一个全的时候
            int size=0;
            for (TeacherSelectInfo.InfoBean.GradeGroupBean c:activity.teacherSelectInfo.getInfo().getGradeGroup()) {
                size++;


            }
            int choose_size=0;
            for (TeacherSelectInfo.InfoBean.GradeGroupBean c:activity.teacherSelectInfo_choose.getInfo().getGradeGroup()) {
                choose_size++;

            }
            if(choose_size==size){
                //全选状态
                selectall_checkbox.setChecked(true);
            }else{
                selectall_checkbox.setChecked(false);
            }

        }

        if(id==activity.JIYAOYAN_LEADER_LIST){
            //判断是否全选 选一个全的时候
            int size=0;
            for (TeacherSelectInfo.InfoBean.GroupsLeaderBean c:activity.teacherSelectInfo.getInfo().getGroupsLeader()) {
                size++;


            }
            int choose_size=0;
            for (TeacherSelectInfo.InfoBean.GroupsLeaderBean c:activity.teacherSelectInfo_choose.getInfo().getGroupsLeader()) {
                choose_size++;

            }

            if(choose_size==size){
                //全选状态
                selectall_checkbox.setChecked(true);
            }else{
                selectall_checkbox.setChecked(false);
            }
        }
        if(id==activity.PREPARE_LESSION_LEADER){
            //判断是否全选 选一个全的时候
            int size=0;
            for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean c:activity.teacherSelectInfo.getInfo().getPrepareLessionLeader()) {
                size++;


            }
            int choose_size=0;
            for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean c:activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader()) {
                choose_size++;

            }

            if(choose_size==size){
                //全选状态
                selectall_checkbox.setChecked(true);
            }else{
                selectall_checkbox.setChecked(false);
            }

        }
    }

    private void initData(){
        if(id==activity.TEACHER_LIST){
            listadapter=new ListAdapter(activity.teacherSelectInfo.getInfo().getGradesTeacher());
            top_title.setText("班主任");
        }else if(id==activity.JIAOYAN_TEACHER_LIST){

            listadapter=new ListAdapter(activity.teacherSelectInfo.getInfo().getGroups());
            top_title.setText("教研组");
        }else if(id==activity.JIYAOYAN_LEADER_LIST){
            listadapter=new ListAdapter(activity.teacherSelectInfo.getInfo().getGroupsLeader());
            top_title.setText("教研组长");
        }else if(id==activity.PREPARE_LESSION_LEADER){
            listadapter=new ListAdapter(activity.teacherSelectInfo.getInfo().getPrepareLessionLeader());
            top_title.setText("备课组长");
        }else if(id==activity.PREPARELESSION){
            listadapter=new ListAdapter(activity.teacherSelectInfo.getInfo().getPrepareLession());
            top_title.setText("备课组");
        }else if(id==activity.CLASSES){
            listadapter=new ListAdapter(activity.teacherSelectInfo.getInfo().getClasses());
            top_title.setText("年级/班级");
        }else if(id==activity.GRADE_GROUP){
            listadapter=new ListAdapter(activity.teacherSelectInfo.getInfo().getGradeGroup());
            top_title.setText("年级组长");
        }




        listView.setAdapter(listadapter);

    }

    private class ListAdapter extends BaseAdapter{
        private List<?> list;
        public ListAdapter(List<?> list ){
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(list != null ){
                return list.size();
            }else{

            return 0;
            }
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return list.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            if(arg1 == null){
                holder = new ViewHolder();
                arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.list_dept_item, null);
                holder.checkBox = (CheckBox) arg1.findViewById(R.id.choose);
                holder.name = (TextView) arg1.findViewById(R.id.name);
                holder.title = (TextView) arg1.findViewById(R.id.title);
                arg1.setTag(holder);
            }else{
                holder = (ViewHolder) arg1.getTag();
            }
            //

            boolean isFocuse=false;
            holder.checkBox.setChecked(false);
            holder.title.setVisibility(View.GONE);
            holder.object=null;
//数据获取 不同类型 加载
            boolean isVisible=false;
            if(id==activity.TEACHER_LIST){ //班主任

               holder.object= list.get(arg0);
               TeacherSelectInfo.InfoBean.GradesTeacherBean gradesTeacherBean= (TeacherSelectInfo.InfoBean.GradesTeacherBean) list.get(arg0);
               holder.name.setText(gradesTeacherBean.getName());
            //    Log.e("gradesTeacherBean",gradesTeacherBean.getName());


                if(is){
                    //从其他activity 回来显示
                    for (TeacherSelectInfo.InfoBean.GradesTeacherBean h:
                            activity.teacherSelectInfo_choose.getInfo().getGradesTeacher()) {
                        if(h.getName().equals(gradesTeacherBean.getName())){
                            holder.checkBox.setChecked(true); //包含此对象 则设置成true
                        }

                    }
                }else{
                    if(ischecked){     //全选状态 判断是不是已经选过了
                        if(activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().size()==activity.teacherSelectInfo.getInfo().getGradesTeacher().size()){
                            holder.checkBox.setChecked(true);

                        }else if(activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().size()>0){


                            //部分选择  包含的需要改了
                            for (TeacherSelectInfo.InfoBean.GradesTeacherBean h:
                                    activity.teacherSelectInfo_choose.getInfo().getGradesTeacher()) {
                                if(h.getName().equals(gradesTeacherBean.getName())){
                                    holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                }

                            }
                            //部分选择

                            if( activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().contains(gradesTeacherBean)){
                                Log.e("name",  activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().size()+"");
                                Log.e("name",gradesTeacherBean.getName());
                                holder.checkBox.setChecked(true); //包含此对象 则设置成true



                            }

                        }else{

                            holder.checkBox.setChecked(true);
                            // activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();
                            activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().add(gradesTeacherBean);
                        }

                        //判断是不是有了


                    }else{//未全选  将之前数据都要删空
                        holder.checkBox.setChecked(false);
                        activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();
                    }
                }



            }

            if(id==activity.JIYAOYAN_LEADER_LIST){ //教研组长 有下级


                holder.object= list.get(arg0);
                TeacherSelectInfo.InfoBean.GroupsLeaderBean groupsLeaderBean= (TeacherSelectInfo.InfoBean.GroupsLeaderBean) list.get(arg0);
                holder.name.setText(groupsLeaderBean.getName());
                if(is){
                    for (TeacherSelectInfo.InfoBean.GroupsLeaderBean h:
                            activity.teacherSelectInfo_choose.getInfo().getGroupsLeader()) {
                        if(h.getName().equals(groupsLeaderBean.getName())){
                            holder.checkBox.setChecked(true); //包含此对象 则设置成true
                        }

                    }
                }else{
                    if(ischecked){//全选状态 判断是不是已经选过了
                        if(activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().size()==activity.teacherSelectInfo.getInfo().getGroupsLeader().size()){
                            holder.checkBox.setChecked(true);
                        }else if(activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().size()>0){
                            //部分选择  包含的需要改了
                            for (TeacherSelectInfo.InfoBean.GroupsLeaderBean h:
                                    activity.teacherSelectInfo_choose.getInfo().getGroupsLeader()) {
                                if(h.getName().equals(groupsLeaderBean.getName())){
                                    holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                }

                            }
                            //部分选择
                            if( activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().contains(groupsLeaderBean)){
                                holder.checkBox.setChecked(true); //包含此对象 则设置成true
                            }

                        }else{
                            holder.checkBox.setChecked(true);
                            //   activity.teacherSelectInfo_choose.getInfo().getGroups().clear();
                            activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().add(groupsLeaderBean);
                        }

                        //判断是不是有了


                    }else{
                        holder.checkBox.setChecked(false);
                        activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().clear();
                    }
                }




            }

            if(id==activity.PREPARE_LESSION_LEADER){ //备课组长 有下级


                holder.object= list.get(arg0);
                TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean prepareLessionLeaderBean= (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean) list.get(arg0);
                holder.name.setText(prepareLessionLeaderBean.getName());
                if(is){
                    for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean h:
                            activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader()) {
                        if(h.getName().equals(prepareLessionLeaderBean.getName())){
                            holder.checkBox.setChecked(true); //包含此对象 则设置成true
                        }

                    }
                }/*else{

                    if(ischecked){//全选状态 判断是不是已经选过了
                        if(activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().size()==activity.teacherSelectInfo.getInfo().getPrepareLessionLeader().size()){
                            holder.checkBox.setChecked(true);
                        }else if(activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().size()>0){
                            //部分选择  包含的需要改了
                            for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean h:
                                    activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader()) {
                                if(h.getName().equals(prepareLessionLeaderBean.getName())){
                                    holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                }

                            }

                            //部分选择
                            if( activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().contains(prepareLessionLeaderBean)){
                                holder.checkBox.setChecked(true); //包含此对象 则设置成true
                            }

                        }else{
                            holder.checkBox.setChecked(true);
                            //   activity.teacherSelectInfo_choose.getInfo().getGroups().clear();
                            activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().add(prepareLessionLeaderBean);
                        }

                        //判断是不是有了


                    }else{
                        holder.checkBox.setChecked(false);
                        activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().clear();
                    }
                }*/


                isFocuse=true;
            }
//年级组长 有下级
            if(id==activity.GRADE_GROUP){


                holder.object= list.get(arg0);
                TeacherSelectInfo.InfoBean.GradeGroupBean gradeGroupBean= (TeacherSelectInfo.InfoBean.GradeGroupBean) list.get(arg0);
                holder.name.setText(gradeGroupBean.getName());
                if(ischecked){//全选状态 判断是不是已经选过了
                    if(activity.teacherSelectInfo_choose.getInfo().getGradeGroup().size()==activity.teacherSelectInfo.getInfo().getGradeGroup().size()){
                        holder.checkBox.setChecked(true);
                    }else if(activity.teacherSelectInfo_choose.getInfo().getGradeGroup().size()>0){
                        //部分选择  包含的需要改了
                        for (TeacherSelectInfo.InfoBean.GradeGroupBean h:
                                activity.teacherSelectInfo_choose.getInfo().getGradeGroup()) {
                            if(h.getName().equals(gradeGroupBean.getName())){
                                holder.checkBox.setChecked(true); //包含此对象 则设置成true
                            }

                        }

                        //部分选择
                        if( activity.teacherSelectInfo_choose.getInfo().getGradeGroup().contains(gradeGroupBean)){
                            holder.checkBox.setChecked(true); //包含此对象 则设置成true
                        }

                    }else{
                        holder.checkBox.setChecked(true);
                        //   activity.teacherSelectInfo_choose.getInfo().getGroups().clear();
                        activity.teacherSelectInfo_choose.getInfo().getGradeGroup().add(gradeGroupBean);
                    }

                    //判断是不是有了


                }else{
                    holder.checkBox.setChecked(false);
                    activity.teacherSelectInfo_choose.getInfo().getGradeGroup().clear();
                }

            }
//备课组 有下级
            boolean isHavelower2=false;
            if(id==activity.PREPARELESSION){
                isHavelower2=true;

                if(activity.teacherSelectInfo.getInfo().getPrepareLession().get(arg0).getPrepareLssions().size()>0){
                    //代表有下级 检查状态
                    //设置按钮状态  根据选择情况 /有下级
                    //年级/班级
              /*      if(activity.teacherSelectInfo_choose.getInfo().getClasses().size()!=0){
                        int choose_size=activity.teacherSelectInfo_choose.getInfo().getClasses().get(arg0).getClasses().size();
                        int size=activity.teacherSelectInfo.getInfo().getClasses().get(arg0).getClasses().size();
                        if(choose_size==size){
                            holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                            holder.checkBox.setChecked(true); //全选
                        }else if(choose_size>0&&choose_size<size){
                            holder.checkBox.setButtonDrawable(R.drawable.checkbox_select_portion);
                            holder.checkBox.setChecked(true);

                        }
                    }*/


                    holder.title.setVisibility(View.VISIBLE);
                    holder.title.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TeacherSelectInfo.InfoBean.PrepareLessionBean  prepareLessionBean= (TeacherSelectInfo.InfoBean.PrepareLessionBean) list.get(arg0);

                            activity.placeViewLowerToLower(3,activity.PREPARELESSION,holder.checkBox.isChecked(),"备课组",prepareLessionBean.getName(),arg0,prepareLessionBean); // 去3级页面
                        }
                    });
                }


                ////////////
                holder.object= list.get(arg0);
                TeacherSelectInfo.InfoBean.PrepareLessionBean  prepareLessionBean= (TeacherSelectInfo.InfoBean.PrepareLessionBean) list.get(arg0);
                holder.name.setText(prepareLessionBean.getName());
              /*  boolean isCreated=false;
                for (TeacherSelectInfo.InfoBean.ClassesBeanX c :activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                   if(c.getName().equals(classesBeanX.getName())){//如果集合已经创建好了
                      isCreated=true;
                   }
                }
                if(!isCreated){
                    //没有创建 则创建
                    TeacherSelectInfo.InfoBean.ClassesBeanX c=new TeacherSelectInfo.InfoBean.ClassesBeanX();
                    activity.teacherSelectInfo_choose.getInfo().getClasses().add(c);
                }*/



                //下级回显 3级
                if(type==1||is){

                    //拿到当前classesBean 与选择集合bean 做比较
                    for (TeacherSelectInfo.InfoBean.PrepareLessionBean choose:activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                        if(prepareLessionBean.getName().equals(choose.getName())){
                            int choose_size=choose.getPrepareLssions().size();
                            int size=prepareLessionBean.getPrepareLssions().size();
                            if(choose_size==size){
                                holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                                holder.checkBox.setChecked(true); //全选
                            }else if(choose_size>0&&choose_size<size){//部分选
                                holder.checkBox.setButtonDrawable(R.drawable.checkbox_select_portion);
                                holder.checkBox.setTag("部分选择");
                                holder.checkBox.setChecked(true);

                            }else{
                                holder.checkBox.setChecked(false);
                            }
                        }
                    }










                }else{
                    //上级回显
                    if(ischecked){//全选状态 判断是不是已经选过了
                        if(activity.teacherSelectInfo_choose.getInfo().getPrepareLession().size()==activity.teacherSelectInfo.getInfo().getPrepareLession().size()){
                            holder.checkBox.setChecked(true);
                        }else if(activity.teacherSelectInfo_choose.getInfo().getPrepareLession().size()>0){
                            //部分选择  包含的需要改了
                            for (TeacherSelectInfo.InfoBean.PrepareLessionBean h:
                                    activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                                if(h.getName().equals(prepareLessionBean.getName())){
                                    holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                }

                            }
                            if( activity.teacherSelectInfo_choose.getInfo().getPrepareLession().contains(prepareLessionBean)){
                                holder.checkBox.setChecked(true); //包含此对象 则设置成true
                            }

                        }else{

                            holder.checkBox.setChecked(true);
                            //   activity.teacherSelectInfo_choose.getInfo().getGroups().clear();
                            if(isHavelower2){ //有下级 就深克隆
                                try {
                                    TeacherSelectInfo.InfoBean.PrepareLessionBean prepareLessionBean1= deepCopy(prepareLessionBean);
                                    prepareLessionBean1.getPrepareLssions().clear();
                                    activity.teacherSelectInfo_choose.getInfo().getPrepareLession().add(prepareLessionBean1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }else {
                                activity.teacherSelectInfo_choose.getInfo().getPrepareLession().add(prepareLessionBean);

                            }

                        }


                        //拿到当前classesBean 与选择集合bean 做比较
                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean choose:activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                            if(prepareLessionBean.getName().equals(choose.getName())){
                                int choose_size=choose.getPrepareLssions().size();
                                int size=prepareLessionBean.getPrepareLssions().size();
                                if(choose_size==size){
                                    holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                                    holder.checkBox.setChecked(true); //全选
                                }else if(choose_size>0&&choose_size<size){//部分选
                                    holder.checkBox.setButtonDrawable(R.drawable.checkbox_select_portion);
                                    holder.checkBox.setTag("部分选择");
                                    holder.checkBox.setChecked(true);

                                }else{
                                    holder.checkBox.setChecked(false);
                                }
                            }
                        }

                    }else{
                        holder.checkBox.setChecked(false);
                        activity.teacherSelectInfo_choose.getInfo().getPrepareLession().clear();
                    }
                }
            }


//年级/班级
            boolean isHavelower=false;
            if(id==activity.CLASSES){
                isHavelower=true;

                if(activity.teacherSelectInfo.getInfo().getClasses().get(arg0).getClasses().size()>0){
                  //代表有下级 检查状态
                    //设置按钮状态  根据选择情况 /有下级
                    //年级/班级
              /*      if(activity.teacherSelectInfo_choose.getInfo().getClasses().size()!=0){
                        int choose_size=activity.teacherSelectInfo_choose.getInfo().getClasses().get(arg0).getClasses().size();
                        int size=activity.teacherSelectInfo.getInfo().getClasses().get(arg0).getClasses().size();
                        if(choose_size==size){
                            holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                            holder.checkBox.setChecked(true); //全选
                        }else if(choose_size>0&&choose_size<size){
                            holder.checkBox.setButtonDrawable(R.drawable.checkbox_select_portion);
                            holder.checkBox.setChecked(true);

                        }
                    }*/


                    holder.title.setVisibility(View.VISIBLE);
                    holder.title.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TeacherSelectInfo.InfoBean.ClassesBeanX  classesBeanX= (TeacherSelectInfo.InfoBean.ClassesBeanX) list.get(arg0);

                             activity.placeViewLowerToLower(3,activity.CLASSES,holder.checkBox.isChecked(),"年级/班级",classesBeanX.getName(),arg0,classesBeanX); // 去3级页面
                        }
                    });
                }


                ////////////
                holder.object= list.get(arg0);
                TeacherSelectInfo.InfoBean.ClassesBeanX  classesBeanX= (TeacherSelectInfo.InfoBean.ClassesBeanX) list.get(arg0);
                 holder.name.setText(classesBeanX.getName());
              /*  boolean isCreated=false;
                for (TeacherSelectInfo.InfoBean.ClassesBeanX c :activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                   if(c.getName().equals(classesBeanX.getName())){//如果集合已经创建好了
                      isCreated=true;
                   }
                }
                if(!isCreated){
                    //没有创建 则创建
                    TeacherSelectInfo.InfoBean.ClassesBeanX c=new TeacherSelectInfo.InfoBean.ClassesBeanX();
                    activity.teacherSelectInfo_choose.getInfo().getClasses().add(c);
                }*/



                //下级回显 3级
                if(type==1||is){

                       //拿到当前classesBean 与选择集合bean 做比较
                    for (TeacherSelectInfo.InfoBean.ClassesBeanX choose:activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                        if(classesBeanX.getName().equals(choose.getName())){
                            int choose_size=choose.getClasses().size();
                            int size=classesBeanX.getClasses().size();
                            if(choose_size==size){
                                holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                                holder.checkBox.setChecked(true); //全选
                            }else if(choose_size>0&&choose_size<size){//部分选
                                holder.checkBox.setButtonDrawable(R.drawable.checkbox_select_portion);
                                holder.checkBox.setTag("部分选择");
                                holder.checkBox.setChecked(true);

                            }else{
                                holder.checkBox.setChecked(false);
                            }
                        }
                    }










                }else{
                    //上级回显
                    if(ischecked){//全选状态 判断是不是已经选过了
                        if(activity.teacherSelectInfo_choose.getInfo().getClasses().size()==activity.teacherSelectInfo.getInfo().getClasses().size()){
                            holder.checkBox.setChecked(true);
                        }else if(activity.teacherSelectInfo_choose.getInfo().getClasses().size()>0){
                            //部分选择  包含的需要改了
                            for (TeacherSelectInfo.InfoBean.ClassesBeanX h:
                                    activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                                if(h.getName().equals(classesBeanX.getName())){
                                    holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                }

                            }
                            if( activity.teacherSelectInfo_choose.getInfo().getClasses().contains(classesBeanX)){
                                holder.checkBox.setChecked(true); //包含此对象 则设置成true
                            }

                        }else{

                            holder.checkBox.setChecked(true);
                            //   activity.teacherSelectInfo_choose.getInfo().getGroups().clear();
                            if(isHavelower){ //有下级 就深克隆
                                try {
                                    TeacherSelectInfo.InfoBean.ClassesBeanX classesBeanX1= deepCopy(classesBeanX);
                                    classesBeanX1.getClasses().clear();
                                    activity.teacherSelectInfo_choose.getInfo().getClasses().add(classesBeanX1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }else {
                                activity.teacherSelectInfo_choose.getInfo().getClasses().add(classesBeanX);

                            }

                        }


                        //拿到当前classesBean 与选择集合bean 做比较
                        for (TeacherSelectInfo.InfoBean.ClassesBeanX choose:activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                            if(classesBeanX.getName().equals(choose.getName())){
                                int choose_size=choose.getClasses().size();
                                int size=classesBeanX.getClasses().size();
                                if(choose_size==size){
                                    holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                                    holder.checkBox.setChecked(true); //全选
                                }else if(choose_size>0&&choose_size<size){//部分选
                                    holder.checkBox.setButtonDrawable(R.drawable.checkbox_select_portion);
                                    holder.checkBox.setTag("部分选择");
                                    holder.checkBox.setChecked(true);

                                }else{
                                    holder.checkBox.setChecked(false);
                                }
                            }
                        }

                    }else{
                        holder.checkBox.setChecked(false);
                        activity.teacherSelectInfo_choose.getInfo().getClasses().clear();
                    }
                }



            }


            //教研组 有下级
            boolean isHavelower1=false;
            if(id==activity.JIAOYAN_TEACHER_LIST){
                isHavelower1=true;

                if(activity.teacherSelectInfo.getInfo().getGroups().get(arg0).getTeacherGroups().size()>0){
                    //代表有下级 检查状态
                    //设置按钮状态  根据选择情况 /有下级
                    //年级/班级
              /*      if(activity.teacherSelectInfo_choose.getInfo().getClasses().size()!=0){
                        int choose_size=activity.teacherSelectInfo_choose.getInfo().getClasses().get(arg0).getClasses().size();
                        int size=activity.teacherSelectInfo.getInfo().getClasses().get(arg0).getClasses().size();
                        if(choose_size==size){
                            holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                            holder.checkBox.setChecked(true); //全选
                        }else if(choose_size>0&&choose_size<size){
                            holder.checkBox.setButtonDrawable(R.drawable.checkbox_select_portion);
                            holder.checkBox.setChecked(true);

                        }
                    }*/


                    holder.title.setVisibility(View.VISIBLE);
                    holder.title.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TeacherSelectInfo.InfoBean.GroupsBean  groupsBean= (TeacherSelectInfo.InfoBean.GroupsBean) list.get(arg0);

                            activity.placeViewLowerToLower(3,activity.JIAOYAN_TEACHER_LIST,holder.checkBox.isChecked(),"教研组",groupsBean.getName(),arg0,groupsBean); // 去3级页面
                        }
                    });
                }


                ////////////
                holder.object= list.get(arg0);
                TeacherSelectInfo.InfoBean.GroupsBean  groupsBean= (TeacherSelectInfo.InfoBean.GroupsBean) list.get(arg0);
                holder.name.setText(groupsBean.getName());
              /*  boolean isCreated=false;
                for (TeacherSelectInfo.InfoBean.ClassesBeanX c :activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                   if(c.getName().equals(classesBeanX.getName())){//如果集合已经创建好了
                      isCreated=true;
                   }
                }
                if(!isCreated){
                    //没有创建 则创建
                    TeacherSelectInfo.InfoBean.ClassesBeanX c=new TeacherSelectInfo.InfoBean.ClassesBeanX();
                    activity.teacherSelectInfo_choose.getInfo().getClasses().add(c);
                }*/



                //下级回显 3级
                if(type==1||is){

                    //拿到当前classesBean 与选择集合bean 做比较
                    for (TeacherSelectInfo.InfoBean.GroupsBean choose:activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                        if(groupsBean.getName().equals(choose.getName())){
                            int choose_size=choose.getTeacherGroups().size();
                            int size=groupsBean.getTeacherGroups().size();
                            if(choose_size==size){
                                holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                                holder.checkBox.setChecked(true); //全选
                            }else if(choose_size>0&&choose_size<size){//部分选
                                holder.checkBox.setButtonDrawable(R.drawable.checkbox_select_portion);
                                holder.checkBox.setTag("部分选择");
                                holder.checkBox.setChecked(true);

                            }else{
                                holder.checkBox.setChecked(false);
                            }
                        }
                    }

                }else{
                    //上级回显
                    if(ischecked){//全选状态 判断是不是已经选过了
                        if(activity.teacherSelectInfo_choose.getInfo().getGroups().size()==activity.teacherSelectInfo.getInfo().getGroups().size()){
                            holder.checkBox.setChecked(true);
                        }else if(activity.teacherSelectInfo_choose.getInfo().getGroups().size()>0){
                            //部分选择  包含的需要改了
                            for (TeacherSelectInfo.InfoBean.GroupsBean h:
                                    activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                                if(h.getName().equals(groupsBean.getName())){
                                    holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                }

                            }
                            if( activity.teacherSelectInfo_choose.getInfo().getGroups().contains(groupsBean)){
                                holder.checkBox.setChecked(true); //包含此对象 则设置成true
                            }

                        }else{

                            holder.checkBox.setChecked(true);
                            //   activity.teacherSelectInfo_choose.getInfo().getGroups().clear();
                            if(isHavelower){ //有下级 就深克隆
                                try {
                                    TeacherSelectInfo.InfoBean.GroupsBean groupsBean1= deepCopy(groupsBean);
                                    groupsBean1.getTeacherGroups().clear();
                                    activity.teacherSelectInfo_choose.getInfo().getGroups().add(groupsBean1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }else {
                                activity.teacherSelectInfo_choose.getInfo().getGroups().add(groupsBean);

                            }

                        }


                        //拿到当前classesBean 与选择集合bean 做比较
                        for (TeacherSelectInfo.InfoBean.GroupsBean choose:activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                            if(groupsBean.getName().equals(choose.getName())){
                                int choose_size=choose.getTeacherGroups().size();
                                int size=groupsBean.getTeacherGroups().size();
                                if(choose_size==size){
                                    holder.checkBox.setButtonDrawable(R.drawable.checkbox_style);
                                    holder.checkBox.setChecked(true); //全选
                                }else if(choose_size>0&&choose_size<size){//部分选
                                    holder.checkBox.setButtonDrawable(R.drawable.checkbox_select_portion);
                                    holder.checkBox.setTag("部分选择");
                                    holder.checkBox.setChecked(true);

                                }else{
                                    holder.checkBox.setChecked(false);
                                }
                            }
                        }

                    }else{
                        holder.checkBox.setChecked(false);
                        activity.teacherSelectInfo_choose.getInfo().getGroups().clear();
                    }
                }

            }


              arg1.setTag(holder);
              return arg1;
        }




        private class ViewHolder{
            TextView name;
            TextView title;
            CheckBox checkBox;
            Object object;

        }

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
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            scrollView.smoothScrollTo((int)scrllo_x,(int)scrllo_y);// 改变滚动条的位置
        }
    };

    private  boolean is=false;
    @Override
    public void onResume() {
        super.onResume();
        Log.e("ChooseLowerFragment","onResume");
        Handler handler=new Handler();
        handler.postDelayed(runnable,100);

        if(!is_select_all){
            selectall_checkbox.setChecked(false);
            is_select_all=true;
        }
        is=true;
        listadapter.notifyDataSetChanged();


    }

    public  void onBack(){
    activity.placeView(0,1,false);  //返回部门  id=1 说明是返回的
}





}