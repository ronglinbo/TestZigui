package com.wcyc.zigui2.newapp.adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wcyc.zigui2.R;


import com.wcyc.zigui2.contactselect.ClassChild;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.ClassList;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewUsedHelpBean;
import com.wcyc.zigui2.newapp.bean.AllDeptList.ContactGroupMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.newapp.home.NewSelectSingleStudentActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.widget.RoundImageView;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
/**
 * 选择学生 适配器
 * 
 * @author 郑国栋
 * 2016-4-14
 * @version 2.0
 */
public class NewSelectStudentSingleAdapter extends BaseExpandableListAdapter{
	private BaseActivity activity;
	private Context myContext;//上下文
	private ArrayList<Student> studentList;// 学生列表
	private TextView v;
	private ArrayList<String> student_id_List_checked=new ArrayList<String>();// 被选中了的  学生id列表
	private ArrayList<String> student_name_List_checked=new ArrayList<String>();// 被选中了的  学生名字列表
	
//	private List<ClassList> classList;//班级list
//	private List<List<ContactsList>> contactList;//学生
	private List<NewClasses> classList;//
	private List<NewClasses> cList_aa;
	private List<NewClasses> cList;
	
	private String http_getClassStudentList = "/getClassStudentList";
	private List<List<Student>> contactList;//学生
	private ArrayList<Student> studentList_aa;
	

	public NewSelectStudentSingleAdapter(Context myContext,
			ArrayList<Student> studentList, TextView v,BaseActivity activity) {
		super();
		this.activity=activity;
		this.myContext = myContext;
		this.studentList = studentList;
		this.v = v;		
		initDatas();
	}
	
	public NewSelectStudentSingleAdapter(Context myContext,
			ArrayList<Student> studentList, TextView v,BaseActivity activity,List<NewClasses> cList) {
		super();
		this.activity=activity;
		this.myContext = myContext;
		this.studentList = studentList;
		this.v = v;
		this.cList=cList;
		Log.i("临时TAG","处理数据7777");
		initDatas();
		Log.i("临时TAG","处理数据8888");
	}
	
	private boolean isClassIdExist(List<NewClasses> classlist,String classId){
		for (int i = 0; i < classlist.size(); i++) {
			if(classlist.get(i).getClassID().equals(classId)){
				return true;
			}
		}
		
		return false;
	}
	
	public void initDatas(){
		Log.i("临时TAG","处理数据77771111");
//		classList.get(0).ge
		cList_aa=CCApplication.app.getMemberDetail().getClassList();
		if(cList_aa==null||cList_aa.size()==0){
			cList_aa=cList;
		}
		Log.i("临时TAG","处理数据77772222");
		if (cList_aa != null) {//去重复
			classList = new ArrayList<NewClasses>();
			
			for(NewClasses classes:cList_aa){
				if(!isClassIdExist(classList,classes.getClassID()))//是否已存在list中
					classList.add(classes);
			}
			
//			List<NewClasses> schoolAllClassList=CCApplication.getInstance().getSchoolAllClassList();
//			if(schoolAllClassList!=null&&schoolAllClassList.size()>0){
//				List<Student> student_i=schoolAllClassList.get(0).getContactList();
//				if(student_i!=null&&student_i.size()>0){
//					classList=schoolAllClassList;
//				}
//			}
			Log.i("临时TAG","处理数据77773333");
			if(classList.get(0).getContactList()==null){
				for (int i = 0; i < classList.size(); i++) {
					
					try {
						
						String classId = classList.get(i).getClassID();
						// JSON对象
						JSONObject json = new JSONObject();
						json.put("classId", classId);
						//请求地址
						String url = new StringBuilder(Constants.SERVER_URL).append(http_getClassStudentList).toString();
						Log.i("临时TAG","处理数据777744444");
						String result = HttpHelper.httpPostJson(activity,url, json);
						Log.i("临时TAG","处理数据777755555");
						JSONObject json3 = new JSONObject(result);

						//活动学生list
						JSONArray ja = json3.getJSONArray("studentList");
						Gson gson1 = new Gson();
						Type t = new TypeToken<List<Student>>() {}.getType();
						//将gson格式转为json格式的字符串
						studentList_aa = gson1.fromJson(ja.toString(), t);
						
						classList.get(i).setContactList(studentList_aa);	
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
//				CCApplication.getInstance().setSchoolAllClassList(classList);
			}	
		}
	}


	@Override
	public Object getChild(int groupPosition, int childPosition) {
		
		return contactList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		
		return childPosition;
	}
	
	//学生
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
//		String string = contactList.get(groupPos).get(childPos).getNick();
		Student list = classList.get(groupPosition).getContactList().get(childPosition);
		String name = list.getName();
		
//		String title = list.getText();
//		list.getUserIconURL();
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = ((Activity) myContext).getLayoutInflater().inflate(R.layout.row_contact, null);
			viewHolder.avatar = (RoundImageView) convertView.findViewById(R.id.avatar);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
			viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.header);
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.avatar.setImageResource(R.drawable.pho_touxiang);
		if(list.getImgUrl()!=null){
			String url = DataUtil.getDownloadURL(myContext, list.getImgUrl());
			((BaseActivity) myContext).getImageLoader().displayImage(url, viewHolder.avatar,
					((BaseActivity) myContext).getImageLoaderOptions());
		}
		
		viewHolder.tvName.setText(name);		
		return convertView;
	}
	

	@Override
	public int getChildrenCount(int groupPosition) {
		
		if(classList.get(groupPosition).getContactList() != null)
			return classList.get(groupPosition).getContactList().size();
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		
		return classList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		if(classList != null)
			return classList.size();
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {//班级
		
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			
			convertView = ((Activity) myContext).getLayoutInflater().inflate(R.layout.list_grade_item, null);
			holder.tvNum = (TextView) convertView.findViewById(R.id.num);
			holder.tvName = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);				
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		String string="";
		if(classList != null && classList.get(groupPosition).getContactList() != null){
			holder.tvNum.setText(classList.get(groupPosition).getContactList().size()+"");
			string = classList.get(groupPosition).getGradeName()+classList.get(groupPosition).getClassName();
		}else{
			holder.tvNum.setText("0");
		}
		
		
		
		
		
		
		
		holder.tvName.setText(string);
		return convertView;
		
	}

	@Override
	public boolean hasStableIds() {
		
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		
		return true;
	}
	
	private static class ViewHolder {
		/** 联系人名字 */
		TextView tvName;
		TextView tvNum;
		/** 头字符 */
		TextView tvHeader;
		/** 用户头像 */
		RoundImageView avatar;
		/** 职位 */
		TextView tvTitle;
	}
	
	
	
	public ArrayList<String> getStudent_id_List_checked() {
		return student_id_List_checked;
	}
	
	public void setStudent_id_List_checked(ArrayList<String> student_id_List_checked) {
		this.student_id_List_checked = student_id_List_checked;
	}
	
	public ArrayList<String> getStudent_name_List_checked() {
		return student_name_List_checked;
	}
	
	public void setStudent_name_List_checked(
			ArrayList<String> student_name_List_checked) {
		this.student_name_List_checked = student_name_List_checked;
	}
	
}


