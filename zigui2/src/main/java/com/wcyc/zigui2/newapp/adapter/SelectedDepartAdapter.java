package com.wcyc.zigui2.newapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chooseContact.ChooseLowerFragment;
import com.wcyc.zigui2.chooseContact.ChooseLowerToLowerFragment;
import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.chooseContact.SelectedDepartmentBean;
import com.wcyc.zigui2.newapp.bean.TeacherSelectInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win0 on 2017/5/26.
 */

public class SelectedDepartAdapter extends BaseAdapter {
    private DeparViewHolder holder;
    private Context mContext;
    private List<SelectedDepartmentBean> mSelectedDepartmentBeanList;


    public SelectedDepartAdapter(Context context, List<SelectedDepartmentBean> selectedDepartmentBeen) {
        mContext = context;
        mSelectedDepartmentBeanList = selectedDepartmentBeen;

    }

    public void deleteItem(SelectedDepartmentBean selectedDepartmentBean ){
        if (mSelectedDepartmentBeanList!=null && mSelectedDepartmentBeanList.size()>0){
            mSelectedDepartmentBeanList.remove(selectedDepartmentBean);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mSelectedDepartmentBeanList.size() == 0 ? 0 :mSelectedDepartmentBeanList.size();
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_depar, null);
            holder = new DeparViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (DeparViewHolder) view.getTag();
        }
        if (mSelectedDepartmentBeanList != null) {
            holder.tvName.setText(mSelectedDepartmentBeanList.get(i).department);
            if (mSelectedDepartmentBeanList.get(i).secondDepartment != null  ) {
                if("年级/班级".equals(mSelectedDepartmentBeanList.get(i).secondDepartment)) {
                    holder.tvSecondName.setVisibility(View.INVISIBLE);
                }else {
                    holder.tvSecondName.setText("（" + mSelectedDepartmentBeanList.get(i).secondDepartment + "）");
                    holder.tvSecondName.setVisibility(View.VISIBLE);
                }

            } else {
                holder.tvSecondName.setVisibility(View.INVISIBLE);
            }
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseLowerFragment.is_select_all = false;
                ChooseLowerToLowerFragment.is_select_all = false;
                SelectedDepartmentBean o = mSelectedDepartmentBeanList.get(i);
                TeacherSelectInfo teacherSelectInfo_choose = ChooseTeacherActivity.teacherSelectInfo_choose;

                if ("年级/班级".equals(o.department)) {
                    teacherSelectInfo_choose.getInfo().getClasses().clear();
                } else {
                    for (int j = 0; j < teacherSelectInfo_choose.getInfo().getClasses().size(); j++) {  //年级/班级
                        if (o.department.equals(teacherSelectInfo_choose.getInfo().getClasses().get(j).getName())) {
                            teacherSelectInfo_choose.getInfo().getClasses().remove(teacherSelectInfo_choose.getInfo().getClasses().get(j));
                        } else {
                            int b=-1;
                            for (int k = 0; k < teacherSelectInfo_choose.getInfo().getClasses().get(j).getClasses().size(); k++) {
                                if (o.department.equals(teacherSelectInfo_choose.getInfo().getClasses().get(j).getClasses().get(k).getName())) {
                                    teacherSelectInfo_choose.getInfo().getClasses().get(j).getClasses().remove(teacherSelectInfo_choose.getInfo().getClasses().get(j).getClasses().get(k));
                                    if( teacherSelectInfo_choose.getInfo().getClasses().get(j).getClasses().size()==0){
                                        b=j;
                                    }
                                }

                            }
                            if(b>=0){
                                teacherSelectInfo_choose.getInfo().getClasses().remove(b);
                            }

                        }

                    }
                }

                /*if("教研组".equals(o.department)) {
                    teacherSelectInfo_choose.getInfo().getGroups().clear();
                }else {
                    for (int j = 0; j < teacherSelectInfo_choose.getInfo().getGroups().size(); j++) {   //教研组
                        for (int k = 0; k < teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().size(); k++) {
                            if (o.department.equals(teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().get(k).getName())){
                                teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().remove(teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().get(k));
                            }else if (o.department.equals(teacherSelectInfo_choose.getInfo().getGroups().get(j).getName())) {
                                teacherSelectInfo_choose.getInfo().getGroups().remove(teacherSelectInfo_choose.getInfo().getGroups().get(j));
                            }
                        }

                    }
                }*/

                if ("教研组".equals(o.department)) {
                    teacherSelectInfo_choose.getInfo().getGroups().clear();
                } else {
                    for (int j = 0; j < teacherSelectInfo_choose.getInfo().getGroups().size(); j++) {   //教研组
                        if (o.department.equals(teacherSelectInfo_choose.getInfo().getGroups().get(j).getName())) {
                            teacherSelectInfo_choose.getInfo().getGroups().remove(teacherSelectInfo_choose.getInfo().getGroups().get(j));
                        } else {
                            int b=-1;
                            for (int k = 0; k < teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().size(); k++) {
                                if (o.department.equals(teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().get(k).getName())) {
                                    teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().remove(teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().get(k));
                                    if( teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().size()==0){
                                        b=j;
                                    }

                                }

                            }

                            if(b>=0){
                                teacherSelectInfo_choose.getInfo().getGroups().remove(b);
                            }

                        }
                    }
                }


                /*if("备课组".equals(o.department)) {
                    teacherSelectInfo_choose.getInfo().getPrepareLession().clear();
                }else {
                    for (int j = 0; j < teacherSelectInfo_choose.getInfo().getPrepareLession().size(); j++) {   //备课组
                        for (int k = 0; k < teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().size(); k++) {
                            if (o.department.equals(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().get(k).getName())){
                                teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().remove(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().get(k));
                            }else if (o.department.equals(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getName())) {
                                teacherSelectInfo_choose.getInfo().getPrepareLession().remove(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j));
                            }
                        }

                    }
                }*/

                if ("备课组".equals(o.department)) {
                    teacherSelectInfo_choose.getInfo().getPrepareLession().clear();
                } else {
                    for (int j = 0; j < teacherSelectInfo_choose.getInfo().getPrepareLession().size(); j++) {   //备课组
                        if (o.department.equals(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getName())) {
                            teacherSelectInfo_choose.getInfo().getPrepareLession().remove(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j));
                        } else {
                            int b=-1;
                            for (int k = 0; k < teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().size(); k++) {
                                if (o.department.equals(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().get(k).getName())) {
                                    teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().remove(teacherSelectInfo_choose.getInfo().getPrepareLession().get(j).getPrepareLssions().get(k));
                                    if( teacherSelectInfo_choose.getInfo().getGroups().get(j).getTeacherGroups().size()==0){
                                        b=j;
                                    }
                                }
                            }
                            if(b>=0){
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
                mSelectedDepartmentBeanList.remove(o);

                notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class DeparViewHolder {
        private TextView tvName;
        private ImageView delete;
        private TextView tvSecondName;

        public DeparViewHolder(View itemView) {
            tvName = (TextView) itemView.findViewById(R.id.name);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            tvSecondName = (TextView) itemView.findViewById(R.id.name_second);

        }
    }
}
