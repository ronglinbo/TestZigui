/*
 * 文 件 名:ChooseDeptFragment.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-08
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.chooseContact;

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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.bean.AllDeptList;
import com.wcyc.zigui2.newapp.bean.TeacherSelectInfo;
import com.wcyc.zigui2.widget.MyListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


//选择部门fragment
public class ChooseLowerToLowerFragment extends Fragment {
    private AllDeptList list, choosedAllList, showList;//已经选择的项
    /**
     * deptListView 行政机构
     * <p/> gradeListView 年级列表（教学机构）
     * <p/> commonListView 常用分组
     * <p/> contactListView 我的分组
     */
    private int id = 0;   //代表分组类型 1代表 班主任
    public static boolean ischecked = false;
    public static boolean is_select_all = true;
    private List<Object> datas;
    private View view;
    private MyListView listView;
    private TextView top_title, school;
    private TextView three_title;
    private ChooseTeacherActivity activity;
    private ListAdapter listadapter;
    private String groups, title;
    private int position;
    private Object object;
    private RelativeLayout select_all;
    private CheckBox selectall_checkbox;
    public static HashSet<Integer> positions = new HashSet();
    private static float scrllo_y, scrllo_x; //记录 scrlloview 的坐标
    private ScrollView scrollView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        view = inflater.inflate(R.layout.fragment_choose_lowertolower, null);
        initView();
        initEvent();
        return view;
    }


    public ChooseLowerToLowerFragment() {

    }


    private boolean checkbox_ischecked = false;

    private void initEvent() {
        select_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //拿到 先清空 在添加
                if (selectall_checkbox.isChecked()) {
                    selectall_checkbox.setChecked(false);
                    ischecked = false;
                    Log.e("证明存在", "1111");
                    if (id == activity.CLASSES) {
                        TeacherSelectInfo.InfoBean.ClassesBeanX classesBeanx = null;
                        for (TeacherSelectInfo.InfoBean.ClassesBeanX classesbeanx : activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                            if (title.equals(classesbeanx.getName())) {//证明存在
                                Log.e("证明存在", classesbeanx.getName());
                                classesBeanx = classesbeanx;

                            }
                        }
                        activity.teacherSelectInfo_choose.getInfo().getClasses().remove(classesBeanx);
                        Log.e("证明存在", "222");
                        listadapter = new ListAdapter(activity.teacherSelectInfo.getInfo().getClasses().get(position).getClasses());
                        listView.setAdapter(listadapter);
                    }
                    if (id == activity.JIAOYAN_TEACHER_LIST) {
                        TeacherSelectInfo.InfoBean.GroupsBean groupsBean = null;
                        for (TeacherSelectInfo.InfoBean.GroupsBean g : activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                            if (title.equals(g.getName())) {//证明存在
                                Log.e("证明存在", g.getName());
                                groupsBean = g;

                            }
                        }
                        activity.teacherSelectInfo_choose.getInfo().getGroups().remove(groupsBean);
                        Log.e("证明存在", "222");
                        listadapter = new ListAdapter(activity.teacherSelectInfo.getInfo().getGroups().get(position).getTeacherGroups());
                        listView.setAdapter(listadapter);
                    }
                    if (id == activity.PREPARELESSION) {
                        TeacherSelectInfo.InfoBean.PrepareLessionBean prepareLessionBean = null;
                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean g : activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                            if (title.equals(g.getName())) {//证明存在
                                Log.e("证明存在", g.getName());
                                prepareLessionBean = g;
                            }
                        }
                        activity.teacherSelectInfo_choose.getInfo().getPrepareLession().remove(prepareLessionBean);
                        Log.e("证明存在", "222");
                        listadapter = new ListAdapter(activity.teacherSelectInfo.getInfo().getPrepareLession().get(position).getPrepareLssions());
                        listView.setAdapter(listadapter);
                    }

                } else {
                    selectall_checkbox.setChecked(true);
                    if (id == activity.CLASSES) {
                        ischecked = true;

                        //判断全选按钮
                        boolean isHave = false;
                        for (TeacherSelectInfo.InfoBean.ClassesBeanX classesbeanx : activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                            if (title.equals(classesbeanx.getName())) {//证明存在
                                //证明存在
                                isHave = true;
                                classesbeanx.getClasses().clear();
                                classesbeanx.getClasses().addAll(activity.teacherSelectInfo.getInfo().getClasses().get(position).getClasses());

                            }
                        }
                        if (!isHave) { //不存在在那个集合
                            TeacherSelectInfo.InfoBean.ClassesBeanX c = new TeacherSelectInfo.InfoBean.ClassesBeanX();
                            c.setName(title);
                            c.getClasses().addAll(activity.teacherSelectInfo.getInfo().getClasses().get(position).getClasses());
                            activity.teacherSelectInfo_choose.getInfo().getClasses().add(c);

                        }

                        listadapter = new ListAdapter(activity.teacherSelectInfo.getInfo().getClasses().get(position).getClasses());
                        listView.setAdapter(listadapter);
                    }
                    if (id == activity.JIAOYAN_TEACHER_LIST) {
                        ischecked = true;

                        //判断全选按钮
                        boolean isHave = false;
                        for (TeacherSelectInfo.InfoBean.GroupsBean g : activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                            if (title.equals(g.getName())) {//证明存在
                                //证明存在
                                isHave = true;
                                g.getTeacherGroups().clear();
                                g.getTeacherGroups().addAll(activity.teacherSelectInfo.getInfo().getGroups().get(position).getTeacherGroups());

                            }
                        }
                        if (!isHave) { //不存在在那个集合
                            TeacherSelectInfo.InfoBean.GroupsBean c = new TeacherSelectInfo.InfoBean.GroupsBean();
                            c.setName(title);
                            c.getTeacherGroups().addAll(activity.teacherSelectInfo.getInfo().getGroups().get(position).getTeacherGroups());
                            activity.teacherSelectInfo_choose.getInfo().getGroups().add(c);

                        }

                        listadapter = new ListAdapter(activity.teacherSelectInfo.getInfo().getGroups().get(position).getTeacherGroups());
                        listView.setAdapter(listadapter);
                    }
                    if (id == activity.PREPARELESSION) {
                        ischecked = true;

                        //判断全选按钮
                        boolean isHave = false;
                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean g : activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                            if (title.equals(g.getName())) {//证明存在
                                //证明存在
                                isHave = true;
                                g.getPrepareLssions().clear();
                                g.getPrepareLssions().addAll(activity.teacherSelectInfo.getInfo().getPrepareLession().get(position).getPrepareLssions());

                            }
                        }
                        if (!isHave) { //不存在在那个集合
                            TeacherSelectInfo.InfoBean.PrepareLessionBean c = new TeacherSelectInfo.InfoBean.PrepareLessionBean();
                            c.setName(title);
                            c.getPrepareLssions().addAll(activity.teacherSelectInfo.getInfo().getPrepareLession().get(position).getPrepareLssions());
                            activity.teacherSelectInfo_choose.getInfo().getPrepareLession().add(c);

                        }

                        listadapter = new ListAdapter(activity.teacherSelectInfo.getInfo().getPrepareLession().get(position).getPrepareLssions());
                        listView.setAdapter(listadapter);
                    }

                }


            }
        });

        top_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.placeViewThreeReturnTwo(2, id, 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position_, long id_) {
                //子项点击 时间
                ListAdapter.ViewHolder holder = (ListAdapter.ViewHolder) view.getTag();
                positions.add(position);

                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(false);
                    selectall_checkbox.setChecked(false);
                    // 根据对应id 删除数据进去 第一次进来 是空的
                    //班主任
                    if (id == activity.CLASSES) {
                        Log.e("班级ClassesBean", "-");
                        TeacherSelectInfo.InfoBean.ClassesBeanX choose = null;

                        for (TeacherSelectInfo.InfoBean.ClassesBeanX c : activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                            if (c.getName().equals(title)) {
                                choose = c;
                            }

                        }

                        TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean bean = (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean) holder.object;
                        int i = 0;
                        for (int b = 0; b < choose.getClasses().size(); b++) {
                            if (choose.getClasses().get(b).getName().equals(bean.getName())) {

                                i = b;
                                Log.e("班级ClassesBean--", bean.getName() + "--" + i);
                            }
                        }
                        //choose代表的是  classBeanX
                        choose.getClasses().remove(i);
                        if (choose.getClasses().size() == 0) {
                            activity.teacherSelectInfo_choose.getInfo().getClasses().remove(choose);
                        }


                        //如果下级还有下级 记得 清空
                        Log.e("班级ClassesBean", choose.getClasses().size() + "");
                        for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean h : choose.getClasses()) {

                            Log.e("班级ClassesBean", h.getName());
                        }
                        listadapter.notifyDataSetChanged();

                    }

                    // 根据对应id 删除数据进去 第一次进来 是空的
                    //教研组
                    if (id == activity.JIAOYAN_TEACHER_LIST) {
                        Log.e("GroupsBean", "-");
                        TeacherSelectInfo.InfoBean.GroupsBean choose = null;

                        for (TeacherSelectInfo.InfoBean.GroupsBean c : activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                            if (c.getName().equals(title)) {
                                choose = c;
                            }

                        }

                        TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX bean = (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX) holder.object;
                        int i = 0;
                        for (int b = 0; b < choose.getTeacherGroups().size(); b++) {
                            if (choose.getTeacherGroups().get(b).getName().equals(bean.getName())) {

                                i = b;
                                Log.e("GroupsBean--", bean.getName() + "--" + i);
                            }
                        }
                        //choose代表的是  classBeanX
                        choose.getTeacherGroups().remove(i);
                        if (choose.getTeacherGroups().size() == 0) {
                            activity.teacherSelectInfo_choose.getInfo().getGroups().remove(choose);
                        }


                        //如果下级还有下级 记得 清空
                        Log.e("GroupsBean", choose.getTeacherGroups().size() + "");
                        for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX h : choose.getTeacherGroups()) {

                            Log.e("GroupsBean", h.getName());
                        }

                        listadapter.notifyDataSetChanged();
                    }
                    // 根据对应id 删除数据进去 第一次进来 是空的
                    //备课组
                    if (id == activity.PREPARELESSION) {
                        Log.e("PrepareLessionBean", "-");
                        TeacherSelectInfo.InfoBean.PrepareLessionBean choose = null;

                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean c : activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                            if (c.getName().equals(title)) {
                                choose = c;
                            }

                        }

                        TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX bean = (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX) holder.object;
                        int i = 0;
                        for (int b = 0; b < choose.getPrepareLssions().size(); b++) {
                            if (choose.getPrepareLssions().get(b).getName().equals(bean.getName())) {

                                i = b;
                                Log.e("PrepareLessionBean--", bean.getName() + "--" + i);
                            }
                        }
                        //choose代表的是  classBeanX
                        choose.getPrepareLssions().remove(i);
                        if (choose.getPrepareLssions().size() == 0) {
                            activity.teacherSelectInfo_choose.getInfo().getPrepareLession().remove(choose);
                        }


                        //如果下级还有下级 记得 清空
                        Log.e("PrepareLessionBean", choose.getPrepareLssions().size() + "");
                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX h : choose.getPrepareLssions()) {

                            Log.e("PrepareLessionBean", h.getName());
                        }

                        listadapter.notifyDataSetChanged();
                    }


                } else {
                    Log.e("班级ClassesBean", "+");

                    holder.checkBox.setChecked(true);


                    // 根据对应id 添加数据进去
                    //班主任
                    if (id == activity.CLASSES) {


                        TeacherSelectInfo.InfoBean.ClassesBeanX choose = null;

                        for (TeacherSelectInfo.InfoBean.ClassesBeanX c : activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                            if (c.getName().equals(title)) {
                                choose = c;
                            }

                        }

                        if (choose == null) {
                            if (!ischecked) { //2级没选的话  就要新加一个集合
                                TeacherSelectInfo.InfoBean.ClassesBeanX c = new TeacherSelectInfo.InfoBean.ClassesBeanX();
                                c.setName(title);
                                activity.teacherSelectInfo_choose.getInfo().getClasses().add(c);
                                c.getClasses().add((TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean) holder.object);
                                Log.e("班级ClassesBean", c.getClasses().size() + "");
                                for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean h : c.getClasses()) {

                                    Log.e("班级ClassesBean123456", h.getName());
                                }
                            }
                        } else {
                            choose.getClasses().add((TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean) holder.object);
                            Log.e("班级ClassesBean", choose.getClasses().size() + "");
                            for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean h : choose.getClasses()) {

                                Log.e("班级ClassesBean", h.getName());
                            }
                        }
                        //判断全选按钮
                        int choose_size = 0;
                        for (TeacherSelectInfo.InfoBean.ClassesBeanX classesbeanx : activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                            if (title.equals(classesbeanx.getName())) {//证明存在

                                for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean c : classesbeanx.getClasses()) {
                                    choose_size++;

                                }
                            }
                        }

                        int size = 0;
                        for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean c : activity.teacherSelectInfo.getInfo().getClasses().get(position).getClasses()) {
                            size++;
                        }


                        if (choose_size == size) {
                            //全选状态
                            selectall_checkbox.setChecked(true);
                        } else {
                            selectall_checkbox.setChecked(false);
                        }


                    }
                    if (id == activity.JIAOYAN_TEACHER_LIST) {


                        TeacherSelectInfo.InfoBean.GroupsBean choose = null;

                        for (TeacherSelectInfo.InfoBean.GroupsBean c : activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                            if (c.getName().equals(title)) {
                                choose = c;
                            }

                        }

                        if (choose == null) {
                            if (!ischecked) { //3级没选的话  就要新加一个集合
                                TeacherSelectInfo.InfoBean.GroupsBean c = new TeacherSelectInfo.InfoBean.GroupsBean();
                                c.setName(title);
                                activity.teacherSelectInfo_choose.getInfo().getGroups().add(c);
                                c.getTeacherGroups().add((TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX) holder.object);
                                Log.e("GroupsBean", c.getTeacherGroups().size() + "");
                                for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX h : c.getTeacherGroups()) {

                                    Log.e("GroupsBean123", h.getName());
                                }
                            }
                        } else {
                            choose.getTeacherGroups().add((TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX) holder.object);
                            Log.e("GroupsBean", choose.getTeacherGroups().size() + "");
                            for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX h : choose.getTeacherGroups()) {

                                Log.e("GroupsBean", h.getName());
                            }
                        }
                        //判断全选按钮
                        int choose_size = 0;
                        for (TeacherSelectInfo.InfoBean.GroupsBean groupsBean : activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                            if (title.equals(groupsBean.getName())) {//证明存在

                                for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX c : groupsBean.getTeacherGroups()) {
                                    choose_size++;

                                }
                            }
                        }
                        int size = 0;
                        for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX c : activity.teacherSelectInfo.getInfo().getGroups().get(position).getTeacherGroups()) {
                            size++;
                        }
                        if (choose_size == size) {
                            //全选状态
                            selectall_checkbox.setChecked(true);
                        } else {
                            selectall_checkbox.setChecked(false);
                        }
                    }
                    if (id == activity.PREPARELESSION) {
                        TeacherSelectInfo.InfoBean.PrepareLessionBean choose = null;
                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean c : activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                            if (c.getName().equals(title)) {
                                choose = c;
                            }

                        }

                        if (choose == null) {
                            if (!ischecked) { //2级没选的话  就要新加一个集合
                                TeacherSelectInfo.InfoBean.PrepareLessionBean c = new TeacherSelectInfo.InfoBean.PrepareLessionBean();
                                c.setName(title);
                                activity.teacherSelectInfo_choose.getInfo().getPrepareLession().add(c);
                                c.getPrepareLssions().add((TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX) holder.object);
                                Log.e("PrepareLessionBean", c.getPrepareLssions().size() + "");
                                for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX h : c.getPrepareLssions()) {

                                    Log.e("PrepareLessionBean123", h.getName());
                                }
                            }
                        } else {
                            choose.getPrepareLssions().add((TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX) holder.object);
                            Log.e("PrepareLessionBean", choose.getPrepareLssions().size() + "");
                            for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX h : choose.getPrepareLssions()) {

                                Log.e("PrepareLessionBean", h.getName());
                            }
                        }
                        //判断全选按钮
                        int choose_size = 0;
                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean prepareLessionBean : activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                            if (title.equals(prepareLessionBean.getName())) {//证明存在

                                for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX c : prepareLessionBean.getPrepareLssions()) {
                                    choose_size++;

                                }
                            }
                        }

                        int size = 0;
                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX c : activity.teacherSelectInfo.getInfo().getPrepareLession().get(position).getPrepareLssions()) {
                            size++;
                        }


                        if (choose_size == size) {
                            //全选状态
                            selectall_checkbox.setChecked(true);
                        } else {
                            selectall_checkbox.setChecked(false);
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
    //  groups 所属组  title 上级标题
    public static Fragment newInstance(int index, int id, boolean ischecked, String groups, String title, int position, Object object) {
        // TODO Auto-generated method stub
        Fragment fragment = new ChooseLowerToLowerFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putInt("id", id);
        args.putInt("position", position);
        args.putString("groups", groups);
        args.putString("title", title);
        args.putBoolean("ischecked", ischecked);
        if (id == ChooseTeacherActivity.CLASSES) {
            args.putSerializable("CLASSESBEANX", (TeacherSelectInfo.InfoBean.ClassesBeanX) object);
        }
        if (id == ChooseTeacherActivity.JIAOYAN_TEACHER_LIST) {
            args.putSerializable("GROUPSBEAN", (TeacherSelectInfo.InfoBean.GroupsBean) object);
        }
        if (id == ChooseTeacherActivity.PREPARELESSION) {
            args.putSerializable("PREPARELESSIONBEAN", (TeacherSelectInfo.InfoBean.PrepareLessionBean) object);
        }
        fragment.setArguments(args);
        //fragment.setIndex(index);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        activity = (ChooseTeacherActivity) getActivity();
        activity.setRecord_fragment(this);
        if (bundle != null) {
            id = bundle.getInt("id");
            ischecked = bundle.getBoolean("ischecked");
            groups = bundle.getString("groups");
            title = bundle.getString("title");
            position = bundle.getInt("position");
            if (id == ChooseTeacherActivity.CLASSES) {
                object = bundle.getSerializable("CLASSESBEANX");
            }
            if (id == ChooseTeacherActivity.JIAOYAN_TEACHER_LIST) {
                object = bundle.getSerializable("GROUPSBEAN");
            }
            if (id == ChooseTeacherActivity.PREPARELESSION) {
                object = bundle.getSerializable("PREPARELESSIONBEAN");
            }


        }
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //    ListToMap();
        initData();

    }

    private void initView() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        top_title = (TextView) view.findViewById(R.id.top_title);//2级标题
        school = (TextView) view.findViewById(R.id.school);//1级标题
        school.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.placeView(0, 1, false);  //返回部门  id=1 说明是返回的
            }
        });
        listView = (MyListView) view.findViewById(R.id.listview);//3级列表
        three_title = (TextView) view.findViewById(R.id.three_title); //3级标题
        select_all = (RelativeLayout) view.findViewById(R.id.select_all);//全选按钮

        selectall_checkbox = (CheckBox) view.findViewById(R.id.choose);
        if (id == activity.CLASSES) {
            //先要判断 上级集合存不存在 于 选择集合里面
            int choose_size = 0;
            for (TeacherSelectInfo.InfoBean.ClassesBeanX classesbeanx : activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                if (title.equals(classesbeanx.getName())) {//证明存在

                    for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean c : classesbeanx.getClasses()) {
                        choose_size++;

                    }
                }
            }

            int size = 0;
            for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean c : activity.teacherSelectInfo.getInfo().getClasses().get(position).getClasses()) {
                size++;
            }


            if (choose_size == size) {
                //全选状态
                selectall_checkbox.setChecked(true);
            } else {
                selectall_checkbox.setChecked(false);
            }

        }

        if (id == activity.JIAOYAN_TEACHER_LIST) {
            //先要判断 上级集合存不存在 于 选择集合里面
            int choose_size = 0;
            for (TeacherSelectInfo.InfoBean.GroupsBean groupsBean : activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                if (title.equals(groupsBean.getName())) {//证明存在

                    for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX c : groupsBean.getTeacherGroups()) {
                        choose_size++;

                    }
                }
            }

            int size = 0;
            for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX c : activity.teacherSelectInfo.getInfo().getGroups().get(position).getTeacherGroups()) {
                size++;
            }


            if (choose_size == size) {
                //全选状态
                selectall_checkbox.setChecked(true);
            } else {
                selectall_checkbox.setChecked(false);
            }

        }
        if (id == activity.PREPARELESSION) {
            //先要判断 上级集合存不存在 于 选择集合里面
            int choose_size = 0;
            for (TeacherSelectInfo.InfoBean.PrepareLessionBean prepareLessionBean : activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                if (title.equals(prepareLessionBean.getName())) {//证明存在

                    for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX c : prepareLessionBean.getPrepareLssions()) {
                        choose_size++;

                    }
                }
            }

            int size = 0;
            for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX c : activity.teacherSelectInfo.getInfo().getPrepareLession().get(position).getPrepareLssions()) {
                size++;
            }


            if (choose_size == size) {
                //全选状态
                selectall_checkbox.setChecked(true);
            } else {
                selectall_checkbox.setChecked(false);
            }

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("LowertoLowerFragment", "onPause");
        scrllo_x = scrollView.getScrollX();
        scrllo_y = scrollView.getScrollY();
        Log.e("scrllo_x", scrllo_x + "");
        Log.e("scrllo_y", scrllo_y + "");
        is = false;
    }

    private void initData() {
        if (id == activity.CLASSES) {
            listadapter = new ListAdapter(activity.teacherSelectInfo.getInfo().getClasses().get(position).getClasses());
            top_title.setText(groups + " > ");
            three_title.setText(title);
        }
        if (id == activity.JIAOYAN_TEACHER_LIST) {
            listadapter = new ListAdapter(activity.teacherSelectInfo.getInfo().getGroups().get(position).getTeacherGroups());
            top_title.setText(groups + " > ");
            three_title.setText(title);
        }
        if (id == activity.PREPARELESSION) {
            listadapter = new ListAdapter(activity.teacherSelectInfo.getInfo().getPrepareLession().get(position).getPrepareLssions());
            top_title.setText(groups + " > ");
            three_title.setText(title);
        }
        listView.setAdapter(listadapter);
        listadapter.notifyDataSetChanged();
    }

    private class ListAdapter extends BaseAdapter {
        private List<?> list;

        public ListAdapter(List<?> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (list != null)
                return list.size();
            return 0;
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
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            if (arg1 == null) {
                holder = new ViewHolder();
                arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.list_dept_item, null);
                holder.checkBox = (CheckBox) arg1.findViewById(R.id.choose);
                holder.name = (TextView) arg1.findViewById(R.id.name);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }

            holder.checkBox.setChecked(false);

//数据获取 不同类型 加载
            if (id == activity.CLASSES) {
                holder.object = list.get(arg0);
                TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean classesBean = (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean) list.get(arg0);
                holder.name.setText(classesBean.getName());
                TeacherSelectInfo.InfoBean.ClassesBeanX bean = (TeacherSelectInfo.InfoBean.ClassesBeanX) object;

                if (is) { //从activity 回显
                    //判断
                    //部分选择  包含的需要改了
                    for (TeacherSelectInfo.InfoBean.ClassesBeanX c : activity.teacherSelectInfo_choose.getInfo().getClasses()) {
                        if (c.getName().equals(title)) {
                            for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean h : c.getClasses()) {
                                if (h.getName().equals(classesBean.getName())) {
                                    holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                }

                            }
                        }
                    }
                } else {
                    //从2级页面 回来显示
                    try {

                        for (TeacherSelectInfo.InfoBean.ClassesBeanX choose : activity.teacherSelectInfo_choose.getInfo().getClasses()) {

                            if (choose.getName().equals(title)) {

                                //相等的话  拿到的 是此3级标题的集合 说明有这个选择集合
                                //  activity.teacherSelectInfo_choose.getInfo().getClasses().get(position);
                                if (ischecked) {//全选状态 判断是不是已经选过了


                                    if (choose.getClasses().size() == bean.getClasses().size()) {
                                        holder.checkBox.setChecked(true);

                                    } else if (choose.getClasses().size() > 0) {

                                        //部分选择  包含的需要改了
                                        for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean h :
                                                choose.getClasses()) {
                                            if (h.getName().equals(classesBean.getName())) {
                                                holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                            }

                                        }
                                        //部分选择
                                        if (choose.getClasses().contains(classesBean)) {
                                            holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                        }

                                    } else {

                                        holder.checkBox.setChecked(true);
                                        // activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();
                                        choose.getClasses().add(classesBean);
                                    }

                                    //判断是不是有了


                                } else {//未全选  将之前数据都要删空

                                    choose.getClasses().clear();
                                }

                            }
                        }


                    } catch (Exception e) {

                    }


                }


            }

            if (id == activity.JIAOYAN_TEACHER_LIST) {
                holder.object = list.get(arg0);
                TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX teacherGroupsBeanX = (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX) list.get(arg0);
                holder.name.setText(teacherGroupsBeanX.getName());
                TeacherSelectInfo.InfoBean.GroupsBean bean = (TeacherSelectInfo.InfoBean.GroupsBean) object;


                if (is) {
                    //判断
                    //部分选择  包含的需要改了
                    for (TeacherSelectInfo.InfoBean.GroupsBean c : activity.teacherSelectInfo_choose.getInfo().getGroups()) {
                        if (c.getName().equals(title)) {
                            for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX h : c.getTeacherGroups()) {
                                if (h.getName().equals(teacherGroupsBeanX.getName())) {
                                    holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                }

                            }
                        }
                    }
                } else {
                    try {

                        for (TeacherSelectInfo.InfoBean.GroupsBean choose : activity.teacherSelectInfo_choose.getInfo().getGroups()) {

                            if (choose.getName().equals(title)) {

                                //相等的话  拿到的 是此3级标题的集合 说明有这个选择集合
                                //  activity.teacherSelectInfo_choose.getInfo().getClasses().get(position);
                                if (ischecked) {//全选状态 判断是不是已经选过了


                                    if (choose.getTeacherGroups().size() == bean.getTeacherGroups().size()) {
                                        holder.checkBox.setChecked(true);

                                    } else if (choose.getTeacherGroups().size() > 0) {

                                        //部分选择  包含的需要改了
                                        for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX h :
                                                choose.getTeacherGroups()) {
                                            if (h.getName().equals(teacherGroupsBeanX.getName())) {
                                                holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                            }

                                        }
                                        //部分选择
                                        if (choose.getTeacherGroups().contains(teacherGroupsBeanX)) {
                                            holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                        }

                                    } else {

                                        holder.checkBox.setChecked(true);
                                        // activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();
                                        choose.getTeacherGroups().add(teacherGroupsBeanX);
                                    }

                                    //判断是不是有了


                                } else {//未全选  将之前数据都要删空

                                    choose.getTeacherGroups().clear();
                                }

                            }
                        }


                    } catch (Exception e) {

                    }
                }


            }

            if (id == activity.PREPARELESSION) {
                holder.object = list.get(arg0);
                TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX prepareLssionsBeanX = (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX) list.get(arg0);
                holder.name.setText(prepareLssionsBeanX.getName());
                TeacherSelectInfo.InfoBean.PrepareLessionBean bean = (TeacherSelectInfo.InfoBean.PrepareLessionBean) object;


                if (is) {
                    //从其他activity 回来显示
                    //判断
                    //部分选择  包含的需要改了
                    for (TeacherSelectInfo.InfoBean.PrepareLessionBean c : activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                        if (c.getName().equals(title)) {
                            for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX h : c.getPrepareLssions()) {
                                if (h.getName().equals(prepareLssionsBeanX.getName())) {
                                    holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                }

                            }
                        }
                    }
                } else {

                    try {

                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean choose : activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
                            if (choose.getName().equals(title)) {

                                //相等的话  拿到的 是此3级标题的集合 说明有这个选择集合
                                //  activity.teacherSelectInfo_choose.getInfo().getClasses().get(position);
                                if (ischecked) {//全选状态 判断是不是已经选过了


                                    if (choose.getPrepareLssions().size() == bean.getPrepareLssions().size()) {
                                        holder.checkBox.setChecked(true);

                                    } else if (choose.getPrepareLssions().size() > 0) {

                                        //部分选择  包含的需要改了
                                        for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX h :
                                                choose.getPrepareLssions()) {
                                            if (h.getName().equals(prepareLssionsBeanX.getName())) {
                                                holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                            }

                                        }
                                        //部分选择
                                        if (choose.getPrepareLssions().contains(prepareLssionsBeanX)) {
                                            holder.checkBox.setChecked(true); //包含此对象 则设置成true
                                        }

                                    } else {

                                        holder.checkBox.setChecked(true);
                                        // activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();
                                        choose.getPrepareLssions().add(prepareLssionsBeanX);
                                    }

                                    //判断是不是有了


                                } else {//未全选  将之前数据都要删空

                                    choose.getPrepareLssions().clear();
                                }

                            }
                        }


                    } catch (Exception e) {

                    }

                }


            }


            arg1.setTag(holder);
            return arg1;
        }

        private class ViewHolder {
            TextView name;
            CheckBox checkBox;
            Object object;

        }

    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            scrollView.smoothScrollTo((int) scrllo_x, (int) scrllo_y);// 改变滚动条的位置
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Log.e("LowertoLowerFragment", "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("LowertoLowerFragment", "onStop");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("LowertoLowerFragment", "onSaveInstanceState");
    }

    private boolean is = false;

    @Override
    public void onResume() {
        super.onResume();
        Log.e("LowertoLowerFragment", "onResume");
        try {
            Log.e("LowertoLowerFragment", activity.teacherSelectInfo_choose.getInfo().getClasses().get(0).getClasses().size() + "");
        } catch (Exception e) {

        }
        Log.e("X", scrllo_x + "");
        Log.e("Y", scrllo_y + "");
        Handler handler = new Handler();
        handler.postDelayed(runnable, 200);
        if (!is_select_all) {
            selectall_checkbox.setChecked(false);
            is_select_all = true;
        }
        is = true;
        listadapter.notifyDataSetChanged();
    }

    public boolean getNameByClasses(String name) {
        for (TeacherSelectInfo.InfoBean.ClassesBeanX c : activity.teacherSelectInfo_choose.getInfo().getClasses()) {

            if (c.getName().equals(name)) {
                //相等的话

            }
        }

        return true;
    }

    public void onBack() {

        activity.placeViewThreeReturnTwo(2, id, 1);
    }


}