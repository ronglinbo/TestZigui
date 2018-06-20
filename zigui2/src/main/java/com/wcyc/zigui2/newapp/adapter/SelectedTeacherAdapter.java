package com.wcyc.zigui2.newapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chooseContact.ChooseTeacherActivity;
import com.wcyc.zigui2.chooseContact.LookSelectedActivity;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.widget.RoundImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by win0 on 2017/5/26.
 */

public class SelectedTeacherAdapter extends BaseAdapter implements View.
        OnClickListener {
    private TeacherViewHold holder;
    private List<AllTeacherList.TeacherMap> chooseTeacherList;

    public void setChooseTeacherList1(List<AllTeacherList.TeacherMap> chooseTeacherList1) {
        this.chooseTeacherList1 = chooseTeacherList1;
    }

    private List<AllTeacherList.TeacherMap> chooseTeacherList1;//真实选择集合
    private Context mContext;
    private String userId;

    public SelectedTeacherAdapter(Context mContext, String userId) {
        this.userId = userId;
        this.mContext = mContext;
        chooseTeacherList = new ArrayList<AllTeacherList.TeacherMap>();
    }

    public void setChooseTeacherList(List<AllTeacherList.TeacherMap> chooseTeacherList) {
        if(chooseTeacherList != null) {
            this.chooseTeacherList.clear();
            this.chooseTeacherList.addAll(chooseTeacherList);
            notifyDataSetChanged();
        }

    }

    public void deleteItem(int position) {
        if (chooseTeacherList != null && chooseTeacherList.size() > position) {
            chooseTeacherList.remove(position);
            notifyDataSetChanged();
            ChooseTeacherActivity.getTeacherFragment().adapter.setData(chooseTeacherList);

        }
    }


    public void deleteItem1(AllTeacherList.TeacherMap teacherMap) {
        if (chooseTeacherList != null && chooseTeacherList.size() > 0) {
            //删除适配器的 部分的 copy集合
            Iterator<AllTeacherList.TeacherMap>  teacherMapIterator=chooseTeacherList.iterator();
            while (teacherMapIterator.hasNext()){
                AllTeacherList.TeacherMap teacherMap1= teacherMapIterator.next();
                if(teacherMap1.getId()==teacherMap.getId()){
                    teacherMapIterator.remove();
                }
            }
            LookSelectedActivity look= (LookSelectedActivity) mContext;
            //删除适配器的 page 部分的 copy集合
            Iterator<AllTeacherList.TeacherMap>  teacherMapIterator2=look.getChooseTeacherList_page().iterator();
            while (teacherMapIterator2.hasNext()){
                AllTeacherList.TeacherMap teacherMap1= teacherMapIterator2.next();
                if(teacherMap1.getId()==teacherMap.getId()){
                    teacherMapIterator2.remove();
                }
            }
            //删除总数据的
            Iterator<AllTeacherList.TeacherMap>  teacherMapIterator1=chooseTeacherList1.iterator();
            while (teacherMapIterator1.hasNext()){
                AllTeacherList.TeacherMap teacherMap1= teacherMapIterator1.next();
                if(teacherMap1.getId()==teacherMap.getId()){
                //    teacherMapIterator1.remove();
                    teacherMap1.setId(-1);
                }
            }
            notifyDataSetChanged();
            ChooseTeacherActivity.getTeacherFragment().setData1(chooseTeacherList1);
            if(look.getChooseTeacherList_page().size()<10){
                look.onLoadingMore();
            }


//            if(chooseTeacherList.size()<10){
//                look.onLoadingMore();
//            }

        }
    }

    public void deleteItem2(AllTeacherList.TeacherMap teacherMap) {
        if (chooseTeacherList != null && chooseTeacherList.size() > 0) {
            //删除适配器的 部分的 copy集合
            Iterator<AllTeacherList.TeacherMap>  teacherMapIterator=chooseTeacherList.iterator();
            while (teacherMapIterator.hasNext()){
                AllTeacherList.TeacherMap teacherMap1= teacherMapIterator.next();
                if(teacherMap1.getId()==teacherMap.getId()){
                    teacherMapIterator.remove();
                }
            }
            //删除总数据的
            Iterator<AllTeacherList.TeacherMap>  teacherMapIterator1=chooseTeacherList1.iterator();
            while (teacherMapIterator1.hasNext()){
                AllTeacherList.TeacherMap teacherMap1= teacherMapIterator1.next();
                if(teacherMap1.getId()==teacherMap.getId()){
                  //  teacherMapIterator1.remove();
                    teacherMap1.setId(-1); //-1代表删除
                }
            }
            ChooseTeacherActivity.getTeacherFragment().setData1(chooseTeacherList1);
        }
    }

    public List<AllTeacherList.TeacherMap> getChooseTeacherList() {
        return chooseTeacherList;
    }

    @Override
    public int getCount() {
        if (chooseTeacherList.size() > 0) {
            return chooseTeacherList.size();
        }
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.teacher_list_item, null);
            holder = new TeacherViewHold(view);
            view.setTag(holder);
        } else {
            holder = (TeacherViewHold) view.getTag();
        }
        if(chooseTeacherList != null){
            holder.tv_name.setText(chooseTeacherList.get(i).getName());
        }
        if (chooseTeacherList != null) {
            holder.department_name.setText(chooseTeacherList.get(i).getDepartment_name());
        }
        final AllTeacherList.TeacherMap teacher = chooseTeacherList.get(i);
        final int id = teacher.getId();
        setImage(holder, teacher.getPicAddress(), String.valueOf(id));

        holder.iv_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem1(chooseTeacherList.get(i));
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
        return 0;
    }

    @Override
    public void onClick(View v) {

    }


    public class TeacherViewHold {
        public RoundImageView image;
        private TextView tv_name;
        private ImageView iv_delect;
        private TextView department_name;
        public TeacherViewHold(View itemView){
            image = (RoundImageView) itemView.findViewById(R.id.image);
            tv_name = (TextView) itemView.findViewById(R.id.name);
            iv_delect = (ImageView) itemView.findViewById(R.id.iv_del);
            department_name = (TextView) itemView.findViewById(R.id.department_name);
        }

    }

    private void setImage(SelectedTeacherAdapter.TeacherViewHold holder, String file, String userId) {
        if (file != null) {
            if (LocalUtil.mBitMap != null && userId != null && userId.equals(this.userId)) {
                holder.image.setImageBitmap(LocalUtil.mBitMap);
            } else {
                String url = DataUtil.getIconURL(file);
                System.out.println("url:" + url);
                ((BaseActivity) mContext).getImageLoader().displayImage(url, holder.image,
                        ((BaseActivity) mContext).getImageLoaderOptions());
            }
        } else {
            holder.image.setImageResource(R.drawable.pho_touxiang);
        }

    }
}
