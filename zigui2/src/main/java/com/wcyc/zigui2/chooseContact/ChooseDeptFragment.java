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
import java.lang.reflect.Field;
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
public class ChooseDeptFragment extends Fragment{
	private AllDeptList list,choosedAllList,showList;//已经选择的项
	/** deptListView 行政机构
	 * <p/> gradeListView 年级列表（教学机构）
	 * <p/> commonListView 常用分组
	 * <p/> contactListView 我的分组
	 * */
	private ListView deptListView,gradeListView,commonListView,contactListView;
	private View view;
	private int select;
	private ScrollView scrollView;

	private ListDeptAdapter deptAdapter;
	private ListCommonAdapter commonAdapter;
	private ListContactAdapter contactAdapter;
	private ListGradeAdapter gradeAdapter;
	private RelativeLayout select_all;
	private CheckBox selectall_checkbox;
	private ArrayList<GradeMap> gradeList;
	private ArrayList<CommonGroup> commonList;
	private ArrayList<DepMap> deptList;
	private ArrayList<ContactGroupMap> contactList;
	private Map<Integer,Boolean> isDeptChecked = new HashMap<Integer,Boolean>();
	private Map<Integer,Boolean> isContactChecked = new HashMap<Integer,Boolean>();
	private Map<Integer,Boolean> isGradeChecked = new HashMap<Integer,Boolean>();
	private Map<Integer,Boolean> isCommonChecked = new HashMap<Integer,Boolean>();
	private ChooseTeacherActivity activity;
	public float x=0, y=0;


	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle bundle){
		Log.e("ChooseDeptFragment","onCreateView");
		view = inflater.inflate(R.layout.choose_dept_fragment, null);
		initView();
		intEvent();
		return view;
	}

	public ChooseDeptFragment(){

	}
//	private ChooseDeptFragment(AllDeptList list,AllDeptList choosedList){
//		this.list = list;
//		this.showList = choosedList;
//	}
	public static Fragment newInstance(int index,AllDeptList list ,AllDeptList choosedList) {
		// TODO Auto-generated method stub
		Fragment fragment;
		if(ChooseTeacherActivity.getDeptfragment()!=null){
			fragment = ChooseTeacherActivity.getDeptfragment();
		}else{
			fragment=new ChooseDeptFragment();
			Bundle args = new Bundle();
			args.putInt("index", index);
			//	args.putInt("select", select);
			args.putSerializable("allDept",list);
			args.putSerializable("allDeptChoose",choosedList);
			fragment.setArguments(args);
		}



		//fragment.setIndex(index);
		return fragment;
	}
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			scrollView.smoothScrollTo((int)x,(int)y);// 改变滚动条的位置
		}
	};
	@Override
	public void onResume() {
		super.onResume();
		Log.e("ChooseDeptFragment","onResume");
		Log.e("X",x+"");
		Log.e("Y",y+"");

		if(activity.isSelectAll){
			selectall_checkbox.setChecked(true);
		}else{
			selectall_checkbox.setChecked(false);
		}

		Handler handler=new Handler();
		handler.postDelayed(runnable,100);
        scrollView.smoothScrollTo((int)x,(int)y);
	    deptAdapter.notifyDataSetChanged();
		commonAdapter.notifyDataSetChanged();
		contactAdapter.notifyDataSetChanged();
		gradeAdapter.notifyDataSetChanged();
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.e("ChooseDeptFragment","onCreate");
		Bundle bundle = getArguments();
		if(bundle != null){
			list = (AllDeptList)bundle.getSerializable("allDept");
			showList = (AllDeptList)bundle.getSerializable("allDeptChoose");
			select=bundle.getInt("select");
		}
	}
	public void ListToMap(){
		if(showList == null) return;
		List<DepMap> deptList = showList.getDepMapList();
		if(deptList != null){
			for(DepMap dept:deptList){
				isDeptChecked.put(dept.getId(), true);
			}
		}
		List<GradeMap> gradeList = showList.getGradeMapList();
		if(gradeList != null){
			for(GradeMap grade:gradeList){
				isGradeChecked.put(grade.getId(),true);
			}
		}
		List<ContactGroupMap> contactList = showList.getContactGroupMapList();
		if(contactList != null){
			for(ContactGroupMap contact:contactList){
				isContactChecked.put(contact.getId(), true);
			}
		}
		List<CommonGroup> commonList = showList.getCommonList();
		if(commonList != null){
			for(CommonGroup item:commonList){
				isCommonChecked.put(item.getId(),true);
			}
		}
	}
	

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Log.e("ChooseDeptFragment","onActivityCreated");
			ListToMap();
		initData();

		activity= (ChooseTeacherActivity) getActivity();
		activity.setRecord_fragment(this);
		if(activity.isSelectAll){
			selectall_checkbox.setChecked(true);
		}
	}

	private void intEvent() {
		select_all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                 if(selectall_checkbox.isChecked()){
					 selectall_checkbox.setChecked(false);
					 activity.isSelectAll=false;

				 }else {
					 selectall_checkbox.setChecked(true);
					 activity.isSelectAll=true;



				 }


			}
		});


	}

	private void initView(){
		deptListView = (ListView) view.findViewById(R.id.dept_list);//行政机构
		gradeListView = (ListView) view.findViewById(R.id.class_list);//年级列表
		contactListView = (ListView) view.findViewById(R.id.contact_list);//我的分组
		commonListView = (ListView) view.findViewById(R.id.common_list);//常用分组
		select_all=(RelativeLayout) view.findViewById(R.id.select_all);
		scrollView= (ScrollView) view.findViewById(R.id.scrollView);
		selectall_checkbox=(CheckBox) view.findViewById(R.id.choose);

		//滚动面板
		scrollView = (ScrollView) view.findViewById(R.id.scrollView); //控制滚动面板位置
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e("ChooseDeptFragment","onPause");
		Log.e("X",x+"");
		   Log.e("Y",y+"");
		x=scrollView.getScrollX();
		y=scrollView.getScrollY();
		Log.e("X",x+"");
		Log.e("Y",y+"");


	}

	private void initData(){


		if(list != null){

			deptAdapter = new ListDeptAdapter(ChooseTeacherActivity.teacherSelectInfo.getInfo().getDeparts());
			deptListView.setAdapter(deptAdapter);
			//行政部门机构
			deptListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Log.e("DepartsBean","1111");
                               ViewHolder holder= (ViewHolder) view.getTag();
                                    if(holder.check.isChecked()){
										selectall_checkbox.setChecked(false);
										activity.isSelectAll=false; //全体教职工按钮
										Log.e("DepartsBean","-");
										holder.check.setButtonDrawable(R.drawable.checkbox_style);
										holder.check.setChecked(false);
										Iterator<TeacherSelectInfo.InfoBean.DepartsBean> stuIter2 = activity.teacherSelectInfo_choose.getInfo().getDeparts().iterator();
										while (stuIter2.hasNext()) {
											TeacherSelectInfo.InfoBean.DepartsBean student = stuIter2.next();
											if (student.getDepartmentName().equals(holder.departsBean.getDepartmentName())){
												stuIter2.remove();
											}//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
										}

										Log.e("DepartsBean","size"+activity.teacherSelectInfo_choose.getInfo().getDeparts().size());
										for (TeacherSelectInfo.InfoBean.DepartsBean be:activity.teacherSelectInfo_choose.getInfo().getDeparts()) {
											Log.e("DepartsBean",be.getDepartmentName());
										}
									}else{
										holder.check.setButtonDrawable(R.drawable.checkbox_style);
										Log.e("DepartsBean","+");
										holder.check.setChecked(true);
										activity.teacherSelectInfo_choose.getInfo().getDeparts().add(holder.departsBean);
										Log.e("DepartsBean","size"+activity.teacherSelectInfo_choose.getInfo().getDeparts().size());
										for (TeacherSelectInfo.InfoBean.DepartsBean be:activity.teacherSelectInfo_choose.getInfo().getDeparts()) {
											Log.e("DepartsBean",be.getDepartmentName());
										}
									}
					Log.e("DepartsBean","1223");
				}
			});
			gradeAdapter = new ListGradeAdapter(list.getGradeMapList());
			gradeListView.setAdapter(gradeAdapter);
//教学机构
			gradeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ViewHolder holder= (ViewHolder) view.getTag();
					if(holder.check.getTag()!=null){
						holder.check.setButtonDrawable(R.drawable.checkbox_style);
						holder.check.setChecked(true);
						if(holder.name.getText().equals("备课组")){
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
							holder.check.setTag(null);

						}
						if(holder.name.getText().equals("教研组")){
							activity.teacherSelectInfo_choose.getInfo().getGroups().clear();//先清一遍

							for (TeacherSelectInfo.InfoBean.GroupsBean  g: activity.teacherSelectInfo.getInfo().getGroups()) {
								//添加的时候 复制集合
								TeacherSelectInfo.InfoBean.GroupsBean groupsBean=null;
								try {
									groupsBean=deepCopy(g);

								} catch (Exception e) {
									e.printStackTrace();
								}
									activity.teacherSelectInfo_choose.getInfo().getGroups().add(groupsBean);
							}
							holder.check.setTag(null);
						}
						if(holder.name.getText().equals("年级/班级")){
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
						}
						holder.check.setTag(null);
					}else{
						if(holder.check.isChecked()){
							selectall_checkbox.setChecked(false);
							activity.isSelectAll=false; //全体教职工按钮

							holder.check.setButtonDrawable(R.drawable.checkbox_style);
							holder.check.setChecked(false);

							if(holder.name.getText().equals("备课组")){
								activity.teacherSelectInfo_choose.getInfo().getPrepareLession().clear();

							}
							if(holder.name.getText().equals("教研组")){
								activity.teacherSelectInfo_choose.getInfo().getGroups().clear();

							}
							if(holder.name.getText().equals("年级/班级")){
								activity.teacherSelectInfo_choose.getInfo().getClasses().clear();

							}
							holder.check.setTag(null);

						}else{
							holder.check.setButtonDrawable(R.drawable.checkbox_style);
							holder.check.setChecked(true);
							if(holder.name.getText().equals("备课组")){
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
								holder.check.setTag(null);

							}
							if(holder.name.getText().equals("教研组")){
								activity.teacherSelectInfo_choose.getInfo().getGroups().clear();//先清一遍

								for (TeacherSelectInfo.InfoBean.GroupsBean  g: activity.teacherSelectInfo.getInfo().getGroups()) {
									//添加的时候 复制集合
									TeacherSelectInfo.InfoBean.GroupsBean groupsBean=null;
									try {
										groupsBean=deepCopy(g);

									} catch (Exception e) {
										e.printStackTrace();
									}
									activity.teacherSelectInfo_choose.getInfo().getGroups().add(groupsBean);
								}
								holder.check.setTag(null);
							}
							if(holder.name.getText().equals("年级/班级")){
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
							}
							holder.check.setTag(null);
						}
					}

				}
			});
			contactAdapter = new ListContactAdapter(list.getContactGroupMapList());
			contactListView.setAdapter(contactAdapter);
			
			commonAdapter = new ListCommonAdapter(list.getCommonList());
			commonListView.setAdapter(commonAdapter);
//常用分组
			commonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ViewHolder holder= (ViewHolder) view.getTag();
					if(holder.check.getTag()!=null){
						holder.check.setButtonDrawable(R.drawable.checkbox_style);
						holder.check.setChecked(true);

						if(holder.name.getText().equals("校级领导")){
							activity.schoolleader=true;

						}
						if(holder.name.getText().equals("部门负责人")){
							activity.departleadr=true;

						}
						if(holder.name.getText().equals("班主任")){
							activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();//先清一遍
							for (TeacherSelectInfo.InfoBean.GradesTeacherBean  g:
									activity.teacherSelectInfo.getInfo().getGradesTeacher()) {
								activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().add(g);
							}

						}
						if(holder.name.getText().equals("教研组长")){
							activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().clear();//先清一遍

							for (TeacherSelectInfo.InfoBean.GroupsLeaderBean  g:
									activity.teacherSelectInfo.getInfo().getGroupsLeader()) {
								activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().add(g);
							}
						}
						if(holder.name.getText().equals("备课组长")){
							activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().clear();//先清一遍

							for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean  g:
									activity.teacherSelectInfo.getInfo().getPrepareLessionLeader()) {
								activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().add(g);
							}
						}
						if(holder.name.getText().equals("年级组长")){
							activity.teacherSelectInfo_choose.getInfo().getGradeGroup().clear();//先清一遍

							for (TeacherSelectInfo.InfoBean.GradeGroupBean  g:
									activity.teacherSelectInfo.getInfo().getGradeGroup()) {
								activity.teacherSelectInfo_choose.getInfo().getGradeGroup().add(g);
							}
						}


						holder.check.setTag(null);
					}else {
						if(holder.check.isChecked()){
							//selectall_checkbox.setChecked(false);

							holder.check.setChecked(false);
							holder.check.setButtonDrawable(R.drawable.checkbox_style);
							if(holder.name.getText().equals("班主任")){
								activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();

							}
							if(holder.name.getText().equals("教研组长")){
								activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().clear();

							}
							if(holder.name.getText().equals("备课组长")){
								activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().clear();

							}
							if(holder.name.getText().equals("年级组长")){
								activity.teacherSelectInfo_choose.getInfo().getGradeGroup().clear();

							}
							if(holder.name.getText().equals("校级领导")){
								activity.schoolleader=false;

							}
							if(holder.name.getText().equals("部门负责人")){
								activity.departleadr=false;

							}

						}else{
							holder.check.setButtonDrawable(R.drawable.checkbox_style);
							holder.check.setChecked(true);

							if(holder.name.getText().equals("校级领导")){
								activity.schoolleader=true;

							}
							if(holder.name.getText().equals("部门负责人")){
								activity.departleadr=true;

							}
							if(holder.name.getText().equals("班主任")){
								activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().clear();//先清一遍
								for (TeacherSelectInfo.InfoBean.GradesTeacherBean  g:
										activity.teacherSelectInfo.getInfo().getGradesTeacher()) {
									activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().add(g);
								}

							}
							if(holder.name.getText().equals("教研组长")){
								activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().clear();//先清一遍

								for (TeacherSelectInfo.InfoBean.GroupsLeaderBean  g:
										activity.teacherSelectInfo.getInfo().getGroupsLeader()) {
									activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().add(g);
								}
							}
							if(holder.name.getText().equals("备课组长")){
								activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().clear();//先清一遍

								for (TeacherSelectInfo.InfoBean.PrepareLessionLeaderBean  g:
										activity.teacherSelectInfo.getInfo().getPrepareLessionLeader()) {
									activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().add(g);
								}
							}
							if(holder.name.getText().equals("年级组长")){
								activity.teacherSelectInfo_choose.getInfo().getGradeGroup().clear();//先清一遍

								for (TeacherSelectInfo.InfoBean.GradeGroupBean  g:
										activity.teacherSelectInfo.getInfo().getGradeGroup()) {
									activity.teacherSelectInfo_choose.getInfo().getGradeGroup().add(g);
								}
							}
						}
					}

				}
			});


		}
		contactList = new ArrayList<ContactGroupMap>();
		deptList = new ArrayList<DepMap>();
		gradeList = new ArrayList<GradeMap>();
		commonList = new ArrayList<CommonGroup>();
	}
	
	private class ListDeptAdapter extends BaseAdapter{
		private List<TeacherSelectInfo.InfoBean.DepartsBean> list;
		public ListDeptAdapter(List<TeacherSelectInfo.InfoBean.DepartsBean> list){
			this.list = list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(list != null )
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
			if(arg1 == null){
				holder = new ViewHolder();
				arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.list_dept_item, null);
				holder.check = (CheckBox) arg1.findViewById(R.id.choose);
				holder.name = (TextView) arg1.findViewById(R.id.name);
				holder.view = arg1.findViewById(R.id.dept_info);
				arg1.setTag(holder);
			}else{
				holder = (ViewHolder) arg1.getTag();
			}

			holder.check.setChecked(false);
			holder.departsBean=list.get(arg0);
			TeacherSelectInfo.InfoBean.DepartsBean dept = list.get(arg0);
			for (TeacherSelectInfo.InfoBean.DepartsBean d:activity.teacherSelectInfo_choose.getInfo().getDeparts()) {
				if(d.getDepartmentName().equals(dept.getDepartmentName())){

					holder.check.setChecked(true);
				}
			}




			String name = dept.getDepartmentName();
			holder.name.setText(name);




			return arg1;
		}
		
	}
	
	public class ListGradeAdapter extends BaseAdapter{
		private List<GradeMap> list;
		public ListGradeAdapter(List<GradeMap> list){
			this.list = list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(list != null )
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
			if(arg1 == null){
				holder = new ViewHolder();
				arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.list_dept_item, null);
				holder.check = (CheckBox) arg1.findViewById(R.id.choose);
				holder.name = (TextView) arg1.findViewById(R.id.name);

				holder.title=(TextView) arg1.findViewById(R.id.title);
				arg1.setTag(holder);
			}else{
				holder = (ViewHolder) arg1.getTag();
			}



			final GradeMap gradmap = list.get(arg0);
			holder.name.setText(gradmap.getName());


			holder.check.setChecked(false);
			holder.title.setVisibility(View.VISIBLE);
			holder.title.setText("下级");
			if(gradmap.getName().equals("年级/班级")||gradmap.getName().equals("教研组")||gradmap.getName().equals("备课组")){
				holder.title.setVisibility(View.VISIBLE);
			}




			holder.title.setOnClickListener(new OnClickListener() {//下级的跳转
				@Override
				public void onClick(View v) {
					if(gradmap.getName().equals("教研组")){
						//班主任合集
						//判断下级
						if(activity.teacherSelectInfo.getInfo().getGroups().size()!=0){
							activity.placeView(2,activity.JIAOYAN_TEACHER_LIST,holder.check.isChecked()); //1代表班主任
						}

					}//扩展
					if(gradmap.getName().equals("备课组")){
						//备课组合集

						activity.placeView(2,activity.PREPARELESSION,holder.check.isChecked()); //1代表班主任
					}//扩展
					if(gradmap.getName().equals("年级/班级")){
						//年级/班级合集

						activity.placeView(2,activity.CLASSES,holder.check.isChecked()); //年级/班级
					}//扩展

				}
			});


			if(gradmap.getName().equals("教研组")){ //设置按钮状态  根据选择情况
				//教研组     还需要判断3级页面集合大小
				int choose_size=0;
				for (TeacherSelectInfo.InfoBean.GroupsBean groupsBean: activity.teacherSelectInfo_choose.getInfo().getGroups()) {
					choose_size++;
					for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX teacherGroupsBeanX :groupsBean.getTeacherGroups()) {
						choose_size++;
					}

				}
				Log.e("choose_size",choose_size+"");
				int size=0;
				for (TeacherSelectInfo.InfoBean.GroupsBean groupsBean: activity.teacherSelectInfo.getInfo().getGroups()) {
					size++;
					for (TeacherSelectInfo.InfoBean.GroupsBean.TeacherGroupsBeanX teacherGroupsBeanX :groupsBean.getTeacherGroups()) {
						size++;
					}


				}
				Log.e("size",size+"");
				if(choose_size==size){
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(true); //全选
				}else if(choose_size>0&&choose_size<size){
					holder.check.setButtonDrawable(R.drawable.checkbox_select_portion);
					holder.check.setChecked(true);
					holder.check.setTag("未全选");
				}

			}
			if(gradmap.getName().equals("备课组")){ //设置按钮状态  根据选择情况
				//教研组     还需要判断3级页面集合大小
				int choose_size=0;
				for (TeacherSelectInfo.InfoBean.PrepareLessionBean prepareLessionBean: activity.teacherSelectInfo_choose.getInfo().getPrepareLession()) {
					choose_size++;
					for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX prepareLssionsBeanX :prepareLessionBean.getPrepareLssions()) {
						choose_size++;
					}

				}
				Log.e("choose_size",choose_size+"");
				int size=0;
				for (TeacherSelectInfo.InfoBean.PrepareLessionBean prepareLessionBean: activity.teacherSelectInfo.getInfo().getPrepareLession()) {
					size++;
					for (TeacherSelectInfo.InfoBean.PrepareLessionBean.PrepareLssionsBeanX prepareLssionsBeanX :prepareLessionBean.getPrepareLssions()) {
						size++;
					}


				}
				Log.e("size",size+"");
				if(choose_size==size){
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(true); //全选
				}else if(choose_size>0&&choose_size<size){
					holder.check.setButtonDrawable(R.drawable.checkbox_select_portion);
					holder.check.setChecked(true);
					holder.check.setTag("未全选");
				}

				/*int choose_size=activity.teacherSelectInfo_choose.getInfo().getClasses().size();
				int size=activity.teacherSelectInfo.getInfo().getClasses().size();
				if(choose_size==size){
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(true); //全选
				}else if(choose_size>0&&choose_size<size){
					holder.check.setButtonDrawable(R.drawable.checkbox_select_portion);
					holder.check.setChecked(true);
					holder.check.setTag("未全选");
				}*/
			}
			if(gradmap.getName().equals("年级/班级")){ //设置按钮状态  根据选择情况
				//教研组     还需要判断3级页面集合大小
				int choose_size=0;
				for (TeacherSelectInfo.InfoBean.ClassesBeanX classesBeanX: activity.teacherSelectInfo_choose.getInfo().getClasses()) {
					choose_size++;
					for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean classeBean :classesBeanX.getClasses()) {
						choose_size++;
					}

				}
				Log.e("choose_size",choose_size+"");
				int size=0;
				for (TeacherSelectInfo.InfoBean.ClassesBeanX classesBeanX: activity.teacherSelectInfo.getInfo().getClasses()) {
					size++;
					for (TeacherSelectInfo.InfoBean.ClassesBeanX.ClassesBean classeBean :classesBeanX.getClasses()) {
						size++;
					}


				}
				Log.e("size",size+"");
				if(choose_size==size){
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(true); //全选
				}else if(choose_size>0&&choose_size<size){
					holder.check.setButtonDrawable(R.drawable.checkbox_select_portion);
					holder.check.setChecked(true);
					holder.check.setTag("未全选");
				}


				/*int choose_size=activity.teacherSelectInfo_choose.getInfo().getClasses().size();
				int size=activity.teacherSelectInfo.getInfo().getClasses().size();
				if(choose_size==size){
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(true); //全选
				}else if(choose_size>0&&choose_size<size){
					holder.check.setButtonDrawable(R.drawable.checkbox_select_portion);
					holder.check.setChecked(true);
					holder.check.setTag("未全选");
				}*/
			}

			//判断下级
			if((gradmap.getName().equals("教研组"))){
				if(activity.teacherSelectInfo.getInfo().getGroups().size()==0){
					holder.title.setText("");
					holder.title.setVisibility(View.VISIBLE);
					holder.check.setChecked(false);
					holder.check.setClickable(false);

				}
			}
			//判断下级
			if((gradmap.getName().equals("年级/班级"))){
				if(activity.teacherSelectInfo.getInfo().getClasses().size()==0){
					holder.title.setText("");
					holder.title.setVisibility(View.VISIBLE);
					holder.check.setChecked(false);
					holder.check.setClickable(false);

				}
			}
			//判断下级
			if((gradmap.getName().equals("备课组"))){
				if(activity.teacherSelectInfo.getInfo().getPrepareLession().size()==0){
					holder.title.setText("");
					holder.title.setVisibility(View.VISIBLE);
					holder.check.setChecked(false);
					holder.check.setClickable(false);

				}
			}


			return arg1;
		}



	}
	
	public class ListContactAdapter extends BaseAdapter{
		private List<ContactGroupMap> list;
		public ListContactAdapter(List<ContactGroupMap> list){
			this.list = list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(list != null )
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
			if(arg1 == null){
				holder = new ViewHolder();
				arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.list_dept_item, null);
				holder.check = (CheckBox) arg1.findViewById(R.id.choose);
				holder.name = (TextView) arg1.findViewById(R.id.name);
				holder.view = arg1.findViewById(R.id.dept_info);
				arg1.setTag(holder);
			}else{
				holder = (ViewHolder) arg1.getTag();
			}

			ContactGroupMap contact = list.get(arg0);

			final int id = contact.getId();

			if(isContactChecked != null && isContactChecked.containsKey(id)){
				holder.check.setChecked(isContactChecked.get(id));
			}else{
				holder.check.setChecked(false);
			}
			holder.name.setText(contact.getName());
			holder.check.setTag(contact);

			holder.view.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(isContactChecked != null){
						boolean checked = false;
						if(isContactChecked.containsKey(id)){
							checked = isContactChecked.get(id);
						}

						if(checked == false){
							holder.check.setChecked(!checked);
							isContactChecked.put(id,!checked);
						}else{
							holder.check.setChecked(!checked);
							isContactChecked.remove(id);
						}
					}
				}

			});
			return arg1;
		}
		
	}
	boolean isportionselect=true;
	public class ListCommonAdapter extends BaseAdapter{
		private List<CommonGroup> list;
		public ListCommonAdapter(List<CommonGroup> list){
			this.list = list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(list != null )
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
			if(arg1 == null){
				holder = new ViewHolder();
				arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.list_dept_item, null);
				holder.check = (CheckBox) arg1.findViewById(R.id.choose);
				holder.name = (TextView) arg1.findViewById(R.id.name);

				holder.title=(TextView) arg1.findViewById(R.id.title);
				arg1.setTag(holder);
			}else{
				holder = (ViewHolder) arg1.getTag();
			}
			
			final CommonGroup contact = list.get(arg0);
			holder.name.setText(contact.getName());
			holder.check.setChecked(false);
			holder.title.setVisibility(View.GONE);
			final int id = contact.getId();
            Log.e("eeee","123");
            if(activity.schoolleader){
				if(contact.getName().equals("校级领导")){
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(true);
				}

			}
			if(activity.departleadr){
				holder.check.setButtonDrawable(R.drawable.checkbox_style);
				if(contact.getName().equals("部门负责人")){
					holder.check.setChecked(true);
				}

			}

            if(contact.getName().equals("班主任")||contact.getName().equals("教研组长")||contact.getName().equals("备课组长")||contact.getName().equals("年级组长")){

				holder.title.setVisibility(View.VISIBLE);
			}

			holder.title.setOnClickListener(new OnClickListener() {//下级的跳转
				@Override
				public void onClick(View v) {
					if(contact.getName().equals("班主任")){
						//班主任合集

						activity.placeView(2,activity.TEACHER_LIST,holder.check.isChecked()); //1代表班主任
					}//扩展
					if(contact.getName().equals("教研组长")){
						//教研组长合集

						activity.placeView(2,activity.JIYAOYAN_LEADER_LIST,holder.check.isChecked()); //1代表班主任
					}//扩展
					if(contact.getName().equals("备课组长")){
						//备课组长 集合

						activity.placeView(2,activity.PREPARE_LESSION_LEADER,holder.check.isChecked()); //1代表班主任
					}//扩展
					if(contact.getName().equals("年级组长")){
						//备课组长 集合

						activity.placeView(2,activity.GRADE_GROUP,holder.check.isChecked()); //1代表班主任
					}//扩展
				}
			});

			if(contact.getName().equals("班主任")){ //设置按钮状态  根据选择情况
				//班主任合集

				int choose_size=activity.teacherSelectInfo_choose.getInfo().getGradesTeacher().size();
				Log.e("班主任choose_size",choose_size+"");
				int size=activity.teacherSelectInfo.getInfo().getGradesTeacher().size();
				if(choose_size==size){
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(true); //全选
				}else if(choose_size>0&&choose_size<size){
					//部分选择
					holder.check.setButtonDrawable(R.drawable.checkbox_select_portion);
					holder.check.setChecked(true);
					holder.check.setTag("部分选择");

				}else{ //没有选
					Log.e("1","1");
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(false);
				}
			}
			if(contact.getName().equals("教研组长")){ //设置按钮状态  根据选择情况
				//教研组长集
				int choose_size=activity.teacherSelectInfo_choose.getInfo().getGroupsLeader().size();
				int size=activity.teacherSelectInfo.getInfo().getGroupsLeader().size();
				Log.e("教研组长",choose_size+"");
				if(choose_size==size){
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(true); //全选
				}else if(choose_size>0&&choose_size<size){

					holder.check.setButtonDrawable(R.drawable.checkbox_select_portion);
					holder.check.setChecked(true);
					holder.check.setTag("未全选");
				}
			}
			if(contact.getName().equals("备课组长")){ //设置按钮状态  根据选择情况
				//备课组长
				int choose_size=activity.teacherSelectInfo_choose.getInfo().getPrepareLessionLeader().size();
				Log.e("备课组长",choose_size+"");
				int size=activity.teacherSelectInfo.getInfo().getPrepareLessionLeader().size();
				if(choose_size==size){
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(true); //全选
				}else if(choose_size>0&&choose_size<size){
					holder.check.setButtonDrawable(R.drawable.checkbox_select_portion);
					holder.check.setChecked(true);
					holder.check.setTag("未全选");
				}
			}
			if(contact.getName().equals("年级组长")){ //设置按钮状态  根据选择情况
				//年级组长
				int choose_size=activity.teacherSelectInfo_choose.getInfo().getGradeGroup().size();
				int size=activity.teacherSelectInfo.getInfo().getGradeGroup().size();
				if(choose_size==size){
					holder.check.setButtonDrawable(R.drawable.checkbox_style);
					holder.check.setChecked(true); //全选
				}else if(choose_size>0&&choose_size<size){
					holder.check.setButtonDrawable(R.drawable.checkbox_select_portion);
					holder.check.setChecked(true);
					holder.check.setTag("未全选");
				}
			}
			

			return arg1;
		}
		
	}
	
	private class ViewHolder{
		CheckBox  check;
		TextView name;
		TextView title;//下级按钮
		TeacherSelectInfo.InfoBean.DepartsBean departsBean;
		View view;
	}
	
	public ArrayList<DepMap> getChooseDeptList(){
		List<DepMap> allDept = list.getDepMapList();
		if(allDept != null){
			for(DepMap dept:allDept){
				int id = dept.getId();
				if(isDeptChecked.containsKey(id)){
					deptList.add(dept);
				}
			}
		}
		return deptList;
	}
	
	public ArrayList<ContactGroupMap> getChooseContactList(){
		List<ContactGroupMap> allContact = list.getContactGroupMapList();
		if(allContact != null){
			for(ContactGroupMap contact:allContact){
				int id = contact.getId();
				if(isContactChecked.containsKey(id)){
					contactList.add(contact);
				}
			}
		}
		return contactList;
	}
	
	public ArrayList<GradeMap> getChooseGradeList(){
		List<GradeMap> allGrade = list.getGradeMapList();
		if(allGrade != null){
			for(GradeMap grade:allGrade){
				int id = grade.getId();
				if(isGradeChecked.containsKey(id)){
					gradeList.add(grade);
				}
			}
		}
		return gradeList;
	}
	
	public ArrayList<CommonGroup> getChooseCommonList(){
		List<CommonGroup> allCommon = list.getCommonList();
		if(allCommon != null){
			for(CommonGroup item:allCommon){
				int id = item.getId();
				if(isCommonChecked.containsKey(id)){
					commonList.add(item);
				}
			}
		}
		return commonList;
	}
	
	public AllDeptList getAllChoosedList(){
		getChooseDeptList();
		getChooseContactList();
		getChooseGradeList();
		getChooseCommonList();
		choosedAllList = new AllDeptList();
		choosedAllList.setContactGroupMapList(contactList);
		choosedAllList.setDepMapList(deptList);
		choosedAllList.setGradeMapList(gradeList);
		choosedAllList.setCommonList(commonList);
		return choosedAllList;
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
	public void onDetach() {
		super.onDetach();

		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}