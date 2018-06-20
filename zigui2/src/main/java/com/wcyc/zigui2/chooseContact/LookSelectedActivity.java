package com.wcyc.zigui2.chooseContact;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.SearchContactActivity;
import com.wcyc.zigui2.newapp.adapter.SelectedDepartAdapter;
import com.wcyc.zigui2.newapp.adapter.SelectedTeacherAdapter;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.TeacherSelectInfo;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.newapp.widget.RefreshListView2;
import com.wcyc.zigui2.newapp.widget.SmartScrollView;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.DateUtils;
import com.wcyc.zigui2.widget.MyListView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.wcyc.zigui2.chooseContact.ChooseTeacherActivity.teacherSelectInfo_choose;

/**
 * 查看已选的主界面
 * Created by xiehua on 2017/5/16.
 */

public class LookSelectedActivity extends BaseActivity implements RefreshListView2.OnRefreshListener {

    private static final int RESULT_CODE_FOR_SELECTED_TEACHER = 200;
    /**
     * cancel 取消
     * <p/> enter 确定
     */
    private  String text;
    private TextView enter, new_content;
    private ImageView cancel;
    private SmartScrollView scrollView;
    private Button searchButton;
    private MyListView departmentList;
    private RefreshListView2 teacherList;
    private SelectedTeacherAdapter teacherAdapter;
    private List<AllTeacherList.TeacherMap> chooseTeacherList; //查看已选数据集合
    private String userId;
    private SelectedDepartAdapter departmentAdapter;
    public static final int LOOK_SELECTED = 100;
    private List<SelectedDepartmentBean> backMatchList;//上一个页面返回的删除的list
    private List<AllTeacherList.TeacherMap> matchTeacherList;

    public List<AllTeacherList.TeacherMap> getChooseTeacherList_page() {
        return chooseTeacherList_page;
    }

    private List<AllTeacherList.TeacherMap> chooseTeacherList_page = new ArrayList<AllTeacherList.TeacherMap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_selected);

        parseIntent();
        initView();
        initEvent();
        initData();
    }

    private void parseIntent() {
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            userId = user.getUserId();
        }
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            chooseTeacherList = (List<AllTeacherList.TeacherMap>) bundle.getSerializable("teacher");

            setToalPage();
        }


    }


    private void initView() {
        scrollView = (SmartScrollView) findViewById(R.id.scrollView);
        new_content = (TextView) findViewById(R.id.new_content);    //标题
        cancel = (ImageView) findViewById(R.id.title_arrow_iv);  //返回
        enter = (TextView) findViewById(R.id.title_right_tv);    //确定

        searchButton = (Button) findViewById(R.id.searchButton);    //搜索
        departmentList = (MyListView) findViewById(R.id.department_list);   //部门下的listview
        teacherList = (RefreshListView2) findViewById(R.id.teacher_list);     //老师下的listview
    }

    private int page = 1;
    private int sumpage = 0;//总页数

    private void setToalPage() {
        float i = (float) chooseTeacherList.size() / 10;
        float b = i - (int) i;
        if (b > 0) {//说明是小数 已经到尾页了 不是整数页
            sumpage = (int) i + 1;
        }
        if (b == 0) {
            sumpage = (int) i;
        }

    }

    private List<AllTeacherList.TeacherMap> getTeacherlistBypage(int page) {
        List<AllTeacherList.TeacherMap> list = new ArrayList<>();
        if (willtolastpage) {
            int i = 1;
            for (AllTeacherList.TeacherMap item : chooseTeacherList) {
                if (i > page * 10 && i <= chooseTeacherList.size()) {
                    list.add(item);
                }
                i++;
            }
        } else {
            int b = 1;
            int sum = page * 10;
            for (AllTeacherList.TeacherMap item : chooseTeacherList) {
                if (b > (page - 1) * 10 && b <= sum) {
                    if (item.getId() > 0) {
                        list.add(item);
                    }
                }
                b++;
            }
        }
        return list;

    }

    private void refershData() {
        page = 1;//初始化page
        chooseTeacherList_page.clear();
        if (chooseTeacherList.size() <= 10) {
            try {
                chooseTeacherList_page = deepCopy(chooseTeacherList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            teacherAdapter.setChooseTeacherList(chooseTeacherList_page);
        } else {

            for (int i = 0; i < 10; i++) {
                chooseTeacherList_page.add(chooseTeacherList.get(i));
            }

        }
        teacherAdapter.setChooseTeacherList(chooseTeacherList_page);

    }

    private void loadmoreData() {
        if (chooseTeacherList.size() <= 10) {
            //直接显示数据

            //    DataUtil.getToast("已经没有更多数据");
        } else {
            if ((sumpage * 10 - page * 10) < 0) {
                //已经到尾页

                DataUtil.getToast("已经没有更多数据");
            } else {
                //是不是倒数第2页 是的话 则记录
                if ((sumpage * 10 - page * 10) > 0 && (sumpage * 10 - page * 10) <= 10) {

                    willtolastpage = true; //变量表示 是不是下页就是尾页了
                }
                chooseTeacherList_page.addAll(getTeacherlistBypage(page));
                teacherAdapter.setChooseTeacherList(chooseTeacherList_page);
                //page 逻辑判断
                //数据是不是到尾页了


                page++;
            }
        }

    }

    private boolean willtolastpage = false;//是不是要到尾页了

    private void initEvent() {
        scrollView.setScanScrollChangedListener(new SmartScrollView.ISmartScrollChangedListener() {
            @Override
            public void onScrolledToBottom() {
                //滑动到底部
                onLoadingMore(); //加载更多
            }

            @Override
            public void onScrolledToTop() {

            }
        });
        teacherList.setOnRefreshListener(this);

        new_content.setText("已选");
        enter.setText("确定");
        enter.setTextColor(this.getResources().getColor(R.color.blue));
        enter.setVisibility(View.VISIBLE);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("lookSelectedTeacher", (Serializable) chooseTeacherList);
                bundle.putSerializable("lookSelectedDepartment", (Serializable) mSelectedDepartmentBeanList);
                Intent intent = new Intent();
                intent.setClass(LookSelectedActivity.this, SearchContactActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, LOOK_SELECTED);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putSerializable("teacher", (Serializable) teacherAdapter.getChooseTeacherList());
                setResult(RESULT_CODE_FOR_SELECTED_TEACHER, getIntent().putExtras(bundle));
                finish();
            }

        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(LookSelectedActivity.this, PublishNotifyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/

                for (int i = 0; i < CCApplication.activityList.size(); i++) {
                    if (CCApplication.activityList.get(i) instanceof ChooseTeacherActivity) {
                        ChooseTeacherActivity activity = (ChooseTeacherActivity) CCApplication.activityList.get(i);
                        activity.returnTeacher();
                        CCApplication.activityList.get(i).finish();
                    }
                }
                finish();
            }
        });

    }

    public List<SelectedDepartmentBean> mSelectedDepartmentBeanList;
    public List<SelectedDepartmentBean> mSelectedDepartmentBeanList1 = new ArrayList<SelectedDepartmentBean>();

    private int count = 0;
    private int count1 = 0;
    private int count2 = 0;

    public static <T> T deepCopy(T src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        T dest = (T) in.readObject();
        return dest;
    }

    private void initData() {

        teacherList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        teacherAdapter = new SelectedTeacherAdapter(this, userId);
        teacherAdapter.setChooseTeacherList1(chooseTeacherList);//设置总数据集合
        chooseTeacherList_page = new ArrayList<>();
        if (chooseTeacherList.size() <= 10) {
            try {
                chooseTeacherList_page = deepCopy(chooseTeacherList);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            for (int i = 0; i < 10; i++) {
                chooseTeacherList_page.add(chooseTeacherList.get(i));
            }

        }

        teacherList.setAdapter(teacherAdapter);
        teacherAdapter.setChooseTeacherList(chooseTeacherList_page);
        mSelectedDepartmentBeanList = new ArrayList<SelectedDepartmentBean>();
        TeacherSelectInfo teacherSelectInfo = teacherSelectInfo_choose;
        TeacherSelectInfo severnInfo = ChooseTeacherActivity.teacherSelectInfo;
        if (teacherSelectInfo != null && teacherSelectInfo.getInfo().getClasses().size() > 0) {
//            int count = 0;
            if (teacherSelectInfo.getInfo().getClasses().size() == severnInfo.getInfo().getClasses().size()) {

                for (int i = 0; i < severnInfo.getInfo().getClasses().size(); i++) {

                    for (int j = 0; j < teacherSelectInfo.getInfo().getClasses().size(); j++) {
                        if (severnInfo.getInfo().getClasses().get(i).getName().equals(teacherSelectInfo.getInfo().getClasses().get(j).getName())) {
                            if (severnInfo.getInfo().getClasses().get(i).getClasses().size() == teacherSelectInfo.getInfo().getClasses().get(j).getClasses().size()) {
                                count++;
                                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                selectedDepartmentBean.department = teacherSelectInfo.getInfo().getClasses().get(j).getName();
                                selectedDepartmentBean.secondDepartment = "年级/班级";
                                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                            } else {

                                for (int k = 0; k < teacherSelectInfo.getInfo().getClasses().get(j).getClasses().size(); k++) {
                                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                    selectedDepartmentBean.department = teacherSelectInfo.getInfo().getClasses().get(j).getClasses().get(k).getName();
                                    //selectedDepartmentBean.secondDepartment = teacherSelectInfo.getInfo().getClasses().get(j).getName();
                                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                                }
                            }

                        }
                    }
                }
                if (count == severnInfo.getInfo().getClasses().size()) {
                    Iterator<SelectedDepartmentBean> iterator = mSelectedDepartmentBeanList.iterator();
                    while (iterator.hasNext()) {
                        SelectedDepartmentBean next = iterator.next();
                        if ("年级/班级".equals(next.secondDepartment)) {
                            iterator.remove();
                        }
                    }
                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                    selectedDepartmentBean.department = "年级/班级";
                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);

                }

            } else { //未全选
                for (int i = 0; i < teacherSelectInfo.getInfo().getClasses().size(); i++) { //遍历获取年级/班级的下级
                    if (teacherSelectInfo.getInfo().getClasses().get(i).getClasses().size() > 0) { //如果年级/班级的下级的下级 有数据
                        for (int j = 0; j < severnInfo.getInfo().getClasses().size(); j++) {    //遍历服务器中年级/班级的下级
                            if (teacherSelectInfo.getInfo().getClasses().get(i).getName().equals(severnInfo.getInfo().getClasses().get(j).getName())) {//被选择的下级与服务器的下级是否相等
                                if (teacherSelectInfo.getInfo().getClasses().get(i).getClasses().size() == severnInfo.getInfo().getClasses().get(j).getClasses().size()) {  //如果选择的下下级数据与服务器上的下下级数据相等   代表下级全选
                                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                    selectedDepartmentBean.department = teacherSelectInfo.getInfo().getClasses().get(i).getName();
                                    //selectedDepartmentBean.secondDepartment = "年级/班级";
                                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                                } else {    // 下下级部分选择
                                    for (int k = 0; k < teacherSelectInfo.getInfo().getClasses().get(i).getClasses().size(); k++) { //遍历被选择的下下级
                                        SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                        selectedDepartmentBean.department = teacherSelectInfo.getInfo().getClasses().get(i).getClasses().get(k).getName();
                                        //selectedDepartmentBean.secondDepartment = teacherSelectInfo.getInfo().getClasses().get(i).getName();
                                        mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        /*if (teacherSelectInfo.getInfo().getGroups().size() > 0) {
            if (teacherSelectInfo.getInfo().getGroups().size() == severnInfo.getInfo().getGroups().size()) {
                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                selectedDepartmentBean.department = "教研组";
                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
            }*/


        if (teacherSelectInfo != null && teacherSelectInfo.getInfo().getGroups().size() > 0) {
//                int count = 0;
            if (teacherSelectInfo.getInfo().getGroups().size() == severnInfo.getInfo().getGroups().size()) {

                for (int i = 0; i < severnInfo.getInfo().getGroups().size(); i++) {

                    for (int j = 0; j < teacherSelectInfo.getInfo().getGroups().size(); j++) {
                        if (severnInfo.getInfo().getGroups().get(i).getName().equals(teacherSelectInfo.getInfo().getGroups().get(j).getName())) {
                            if (severnInfo.getInfo().getGroups().get(i).getTeacherGroups().size() == teacherSelectInfo.getInfo().getGroups().get(j).getTeacherGroups().size()) {
                                count1++;
                                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                selectedDepartmentBean.department = teacherSelectInfo.getInfo().getGroups().get(j).getName();
                                selectedDepartmentBean.secondDepartment = "教研组";
                                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                            } else {

                                for (int k = 0; k < teacherSelectInfo.getInfo().getGroups().get(j).getTeacherGroups().size(); k++) {
                                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                    selectedDepartmentBean.department = teacherSelectInfo.getInfo().getGroups().get(j).getTeacherGroups().get(k).getName();
                                    selectedDepartmentBean.secondDepartment = teacherSelectInfo.getInfo().getGroups().get(j).getName();
                                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                                }
                            }

                        }
                    }
                }
                if (count1 == severnInfo.getInfo().getGroups().size()) {
                    Iterator<SelectedDepartmentBean> iterator = mSelectedDepartmentBeanList.iterator();
                    while (iterator.hasNext()) {
                        SelectedDepartmentBean next = iterator.next();
                        if ("教研组".equals(next.secondDepartment)) {
                            iterator.remove();
                        }
                    }
                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                    selectedDepartmentBean.department = "教研组";
                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);

                }

            } else { //未全选
                for (int i = 0; i < teacherSelectInfo.getInfo().getGroups().size(); i++) { //遍历获取年级/班级的下级
                    if (teacherSelectInfo.getInfo().getGroups().get(i).getTeacherGroups().size() > 0) { //如果年级/班级的下级的下级 有数据
                        for (int j = 0; j < severnInfo.getInfo().getGroups().size(); j++) {    //遍历服务器中年级/班级的下级
                            if (teacherSelectInfo.getInfo().getGroups().get(i).getName().equals(severnInfo.getInfo().getGroups().get(j).getName())) {//被选择的下级与服务器的下级是否相等
                                if (teacherSelectInfo.getInfo().getGroups().get(i).getTeacherGroups().size() == severnInfo.getInfo().getGroups().get(j).getTeacherGroups().size()) {  //如果选择的下下级数据与服务器上的下下级数据相等   代表下级全选
                                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                    selectedDepartmentBean.department = teacherSelectInfo.getInfo().getGroups().get(i).getName();
                                    selectedDepartmentBean.secondDepartment = "教研组";
                                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                                } else {    // 下下级部分选择
                                    for (int k = 0; k < teacherSelectInfo.getInfo().getGroups().get(i).getTeacherGroups().size(); k++) { //遍历被选择的下下级
                                        SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                        selectedDepartmentBean.department = teacherSelectInfo.getInfo().getGroups().get(i).getTeacherGroups().get(k).getName();
                                        selectedDepartmentBean.secondDepartment = teacherSelectInfo.getInfo().getGroups().get(i).getName();
                                        mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        /*if (teacherSelectInfo.getInfo().getGroups().size() > 0){
            if (teacherSelectInfo.getInfo().getGroups().size() == severnInfo.getInfo().getGroups().size()){
                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                selectedDepartmentBean.department = "教研组";
                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
            }else {
                for (int i = 0; i < teacherSelectInfo.getInfo().getGroups().size(); i++) {
                    if (teacherSelectInfo.getInfo().getGroups().get(i).getTeacherGroups().size() >  0) {
                        if (teacherSelectInfo.getInfo().getGroups().get(i).getTeacherGroups().size() == severnInfo.getInfo().getGroups().get(i).getTeacherGroups().size()) {
                            SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                            selectedDepartmentBean.department = teacherSelectInfo.getInfo().getGroups().get(i).getName();
                            selectedDepartmentBean.secondDepartment = "教研组";
                            mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                        } else {
                            for (int j = 0; j < teacherSelectInfo.getInfo().getGroups().get(i).getTeacherGroups().size(); j++) {
                                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                selectedDepartmentBean.department = teacherSelectInfo.getInfo().getGroups().get(i).getTeacherGroups().get(j).getName();
                                selectedDepartmentBean.secondDepartment = teacherSelectInfo.getInfo().getGroups().get(i).getName();
                                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                            }
                        }
                    }
                }
            }
        }*/

        /*if (teacherSelectInfo.getInfo().getPrepareLession().size() > 0) {
            if (teacherSelectInfo.getInfo().getPrepareLession().size() == severnInfo.getInfo().getPrepareLession().size()) {
                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                selectedDepartmentBean.department = "备课组";
                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
            }*/

        if (teacherSelectInfo != null && teacherSelectInfo.getInfo().getPrepareLession().size() > 0) {
//                int count = 0;
            if (teacherSelectInfo.getInfo().getPrepareLession().size() == severnInfo.getInfo().getPrepareLession().size()) {

                for (int i = 0; i < severnInfo.getInfo().getPrepareLession().size(); i++) {

                    for (int j = 0; j < teacherSelectInfo.getInfo().getPrepareLession().size(); j++) {
                        if (severnInfo.getInfo().getPrepareLession().get(i).getName().equals(teacherSelectInfo.getInfo().getPrepareLession().get(j).getName())) {
                            if (severnInfo.getInfo().getPrepareLession().get(i).getPrepareLssions().size() == teacherSelectInfo.getInfo().getPrepareLession().get(j).getPrepareLssions().size()) {
                                count2++;
                                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                selectedDepartmentBean.department = teacherSelectInfo.getInfo().getPrepareLession().get(j).getName();
                                selectedDepartmentBean.secondDepartment = "备课组";
                                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                            } else {

                                for (int k = 0; k < teacherSelectInfo.getInfo().getPrepareLession().get(j).getPrepareLssions().size(); k++) {
                                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                    selectedDepartmentBean.department = teacherSelectInfo.getInfo().getPrepareLession().get(j).getPrepareLssions().get(k).getName();
                                    selectedDepartmentBean.secondDepartment = teacherSelectInfo.getInfo().getPrepareLession().get(j).getName();
                                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                                }
                            }

                        }
                    }
                }
                if (count2 == severnInfo.getInfo().getPrepareLession().size()) {
                    Iterator<SelectedDepartmentBean> iterator = mSelectedDepartmentBeanList.iterator();
                    while (iterator.hasNext()) {
                        SelectedDepartmentBean next = iterator.next();
                        if ("备课组".equals(next.secondDepartment)) {
                            iterator.remove();
                        }
                    }
                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                    selectedDepartmentBean.department = "备课组";
                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);

                }

            } else { //未全选
                for (int i = 0; i < teacherSelectInfo.getInfo().getPrepareLession().size(); i++) { //遍历获取年级/班级的下级
                    if (teacherSelectInfo.getInfo().getPrepareLession().get(i).getPrepareLssions().size() > 0) { //如果年级/班级的下级的下级 有数据
                        for (int j = 0; j < severnInfo.getInfo().getPrepareLession().size(); j++) {    //遍历服务器中年级/班级的下级
                            if (teacherSelectInfo.getInfo().getPrepareLession().get(i).getName().equals(severnInfo.getInfo().getPrepareLession().get(j).getName())) {//被选择的下级与服务器的下级是否相等
                                if (teacherSelectInfo.getInfo().getPrepareLession().get(i).getPrepareLssions().size() == severnInfo.getInfo().getPrepareLession().get(j).getPrepareLssions().size()) {  //如果选择的下下级数据与服务器上的下下级数据相等   代表下级全选
                                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                    selectedDepartmentBean.department = teacherSelectInfo.getInfo().getPrepareLession().get(i).getName();
                                    selectedDepartmentBean.secondDepartment = "备课组";
                                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                                } else {    // 下下级部分选择
                                    for (int k = 0; k < teacherSelectInfo.getInfo().getPrepareLession().get(i).getPrepareLssions().size(); k++) { //遍历被选择的下下级
                                        SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                        selectedDepartmentBean.department = teacherSelectInfo.getInfo().getPrepareLession().get(i).getPrepareLssions().get(k).getName();
                                        selectedDepartmentBean.secondDepartment = teacherSelectInfo.getInfo().getPrepareLession().get(i).getName();
                                        mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        /*if (teacherSelectInfo.getInfo().getPrepareLession().size() > 0){
            if (teacherSelectInfo.getInfo().getPrepareLession().size() == severnInfo.getInfo().getPrepareLession().size()){
                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                selectedDepartmentBean.department = "备课组";
                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
            }else {
                for (int i = 0; i < teacherSelectInfo.getInfo().getPrepareLession().size(); i++) {
                    if (teacherSelectInfo.getInfo().getPrepareLession().get(i).getPrepareLssions().size() > 0) {
                        if (teacherSelectInfo.getInfo().getPrepareLession().get(i).getPrepareLssions().size() == severnInfo.getInfo().getPrepareLession().get(i).getPrepareLssions().size()) {
                            SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                            selectedDepartmentBean.department = teacherSelectInfo.getInfo().getPrepareLession().get(i).getName();
                            selectedDepartmentBean.secondDepartment = "备课组";
                            mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                        } else {
                            for (int j = 0; j < teacherSelectInfo.getInfo().getPrepareLession().get(i).getPrepareLssions().size(); j++) {
                                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                                selectedDepartmentBean.department = teacherSelectInfo.getInfo().getPrepareLession().get(i).getPrepareLssions().get(j).getName();
                                selectedDepartmentBean.secondDepartment = teacherSelectInfo.getInfo().getPrepareLession().get(i).getName();
                                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                            }
                        }
                    }
                }
            }
        }*/

        if (teacherSelectInfo.getInfo().getGradesTeacher().size() > 0) {
            if (teacherSelectInfo.getInfo().getGradesTeacher().size() == severnInfo.getInfo().getGradesTeacher().size()) {
                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                selectedDepartmentBean.department = "班主任";
                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
            } else {
                for (int i = 0; i < teacherSelectInfo.getInfo().getGradesTeacher().size(); i++) {
                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                    selectedDepartmentBean.department = teacherSelectInfo.getInfo().getGradesTeacher().get(i).getName();
                    selectedDepartmentBean.secondDepartment = "班主任";
                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                }
            }
        }


        if (teacherSelectInfo.getInfo().getGroupsLeader().size() > 0) {
            if (teacherSelectInfo.getInfo().getGroupsLeader().size() == severnInfo.getInfo().getGroupsLeader().size()) {
                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                selectedDepartmentBean.department = "教研组长";
                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
            } else {
                for (int i = 0; i < teacherSelectInfo.getInfo().getGroupsLeader().size(); i++) {
                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                    selectedDepartmentBean.department = teacherSelectInfo.getInfo().getGroupsLeader().get(i).getName();
                    selectedDepartmentBean.secondDepartment = "教研组长";
                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                }
            }
        }


        if (teacherSelectInfo.getInfo().getGradeGroup().size() > 0) {
            if (teacherSelectInfo.getInfo().getGradeGroup().size() == severnInfo.getInfo().getGradeGroup().size()) {
                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                selectedDepartmentBean.department = "年级组长";
                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
            } else {
                for (int i = 0; i < teacherSelectInfo.getInfo().getGradeGroup().size(); i++) {
                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                    selectedDepartmentBean.department = teacherSelectInfo.getInfo().getGradeGroup().get(i).getName();
                    selectedDepartmentBean.secondDepartment = "年级组长";
                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                }
            }
        }

        if (teacherSelectInfo.getInfo().getPrepareLessionLeader().size() > 0) {
            if (teacherSelectInfo.getInfo().getPrepareLessionLeader().size() == severnInfo.getInfo().getPrepareLessionLeader().size()) {
                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                selectedDepartmentBean.department = "备课组长";
                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
            } else {
                for (int i = 0; i < teacherSelectInfo.getInfo().getPrepareLessionLeader().size(); i++) {
                    SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                    selectedDepartmentBean.department = teacherSelectInfo.getInfo().getPrepareLessionLeader().get(i).getName();
                    selectedDepartmentBean.secondDepartment = "备课组长";
                    mSelectedDepartmentBeanList.add(selectedDepartmentBean);
                }
            }
        }
        if (teacherSelectInfo.getInfo().getDeparts().size() > 0) {
            for (TeacherSelectInfo.InfoBean.DepartsBean depart : teacherSelectInfo.getInfo().getDeparts()) {
                SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
                selectedDepartmentBean.department = depart.getDepartmentName();
                mSelectedDepartmentBeanList.add(selectedDepartmentBean);
            }
        }

        if (ChooseTeacherActivity.isSelectAll == true) {
            SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
            selectedDepartmentBean.department = "全体教职工";
            mSelectedDepartmentBeanList.add(selectedDepartmentBean);
        }

        if (ChooseTeacherActivity.schoolleader == true) {
            SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
            selectedDepartmentBean.department = "校级领导";
            mSelectedDepartmentBeanList.add(selectedDepartmentBean);
        }

        if (ChooseTeacherActivity.departleadr == true) {
            SelectedDepartmentBean selectedDepartmentBean = new SelectedDepartmentBean();
            selectedDepartmentBean.department = "部门负责人";
            mSelectedDepartmentBeanList.add(selectedDepartmentBean);
        }
        departmentAdapter = new SelectedDepartAdapter(this, mSelectedDepartmentBeanList);
        departmentList.setAdapter(departmentAdapter);
    }


    @Override
    protected void getMessage(String data) {

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("teacher", (Serializable) teacherAdapter.getChooseTeacherList());
        setResult(RESULT_CODE_FOR_SELECTED_TEACHER, getIntent().putExtras(bundle));

        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOOK_SELECTED && resultCode == 6) {
            if (data != null) {
                backMatchList = (List<SelectedDepartmentBean>) data.getSerializableExtra("back");//部门选择返回的数据

                matchTeacherList = (List<AllTeacherList.TeacherMap>) data.getSerializableExtra("backteach");//教师返回的数据

                for (AllTeacherList.TeacherMap teacherMap : matchTeacherList) {
                    for (int i = 0; i < chooseTeacherList.size(); i++) {
                        if (teacherMap.getId() == chooseTeacherList.get(i).getId()) {
                            teacherAdapter.deleteItem2(chooseTeacherList.get(i));
                        }
                    }
                    Iterator<AllTeacherList.TeacherMap> iterator = chooseTeacherList_page.iterator();
                    while (iterator.hasNext()) {
                        AllTeacherList.TeacherMap teacherMap1 = iterator.next();
                        if (teacherMap1.getId() == teacherMap.getId()) {
                            iterator.remove();
                        }
                    }

                }
                teacherAdapter.notifyDataSetChanged();

                for (SelectedDepartmentBean selectedDepartmentBean : backMatchList) {
                    for (int i = 0; i < mSelectedDepartmentBeanList.size(); i++) {
                        if (selectedDepartmentBean.department.equals(mSelectedDepartmentBeanList.get(i).department)) {
                            departmentAdapter.deleteItem(mSelectedDepartmentBeanList.get(i));
                        }
                    }
                }

            }

        }
    }

    public void onDownPullRefresh() {
        //下拉刷新
        refershData();
        teacherList.hideHeaderView();

    }

    @Override
    public void onLoadingMore() {
        //上拉加载
        if (page == 1) {
            page = 2;
        }
        Log.e("page", page + "");
        loadmoreData();
        teacherList.hideFooterView();

    }
}