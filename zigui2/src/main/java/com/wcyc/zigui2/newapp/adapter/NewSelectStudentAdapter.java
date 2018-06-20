package com.wcyc.zigui2.newapp.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.contactselect.ClassChild;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.NewUsedHelpBean;
import com.wcyc.zigui2.newapp.bean.AllDeptList.ContactGroupMap;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.widget.RoundImageView;
import com.wcyc.zigui2.widget.Sidebar;
import com.wcyc.zigui2.widget.SidebarSelectStudent;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 选择学生 适配器
 * 
 * @author 郑国栋 2016-4-14
 * @version 2.0
 */
public class NewSelectStudentAdapter extends ArrayAdapter<Student> implements SectionIndexer
		,OnClickListener {
	
	private BaseActivity activity;
	private Context myContext;// 上下文
	private ArrayList<Student> studentList;// 学生列表
	// private ArrayList<Student> studentList_checked;// 被选中了的 学生列表
	private ArrayList<String> student_id_List_checked = new ArrayList<String>();// 被选中了的
																				// 学生id列表
	private ArrayList<String> student_name_List_checked = new ArrayList<String>();// 被选中了的
																					// 学生名字列表
//	private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
	private HashMap<Integer, Boolean> isSelected;
	
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;

	private int res;
	private LayoutInflater layoutInflater;
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;
		
	public NewSelectStudentAdapter(Context context, int resource,BaseActivity activity,
			ArrayList<Student> studentList,SidebarSelectStudent sidebarSelectStudent,
			HashMap<Integer, Boolean> isSelected) {
		super(context, resource, studentList);
		
		
		this.myContext = context;
		this.res=resource;
		this.activity = activity;
		this.studentList = studentList;
		
		if (studentList != null) {
			this.isSelected=new HashMap<Integer, Boolean>();
			for (int j = 0; j < studentList.size(); j++) {
				getIsSelected().put(j, false);
			}
		}
		
		if(isSelected!=null&&isSelected.size()>0){
			
			this.isSelected=isSelected;
			
			for (int i = 0; i < isSelected.size(); i++) {
				if(isSelected.get(i)){
					
					student_id_List_checked.add(studentList.get(i).getId() + "");//选中则 添加
					student_name_List_checked.add(studentList.get(i).getName());
				}
				
			}
		}
		
		
		layoutInflater = LayoutInflater.from(context);
		
		alphaIndexer = new HashMap<String, Integer>();
//		sections = new String[studentList.size()];
		
	}
	

	
	@Override
	public int getCount() {
		if (studentList != null) {

			return studentList.size();// 长度

		}

		return 0;
	}

	@Override
	public Student getItem(int position) {

		return null;// 当前位置的对象
	}

	@Override
	public long getItemId(int position) {

		return position;// 当前位置ID
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? 0 : 1;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		Student class_student = studentList.get(position);
		if (convertView == null) {
			// 实例化控件
			viewholder = new ViewHolder();
			// 配置单个item的布局
			convertView = LayoutInflater.from(myContext).inflate(
					R.layout.new_select_student_list_item, parent, false);

			// 获得布局中的控件
			viewholder.student_header = (TextView) convertView.findViewById(R.id.student_header);
			viewholder.name = (TextView) convertView.findViewById(R.id.name);
			viewholder.checkBox = (CheckBox) convertView
					.findViewById(R.id.contact_item_flag);
			viewholder.icon = (RoundImageView) convertView
					.findViewById(R.id.contact_item_icon);

			// 设置标签
			convertView.setTag(viewholder);
		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}

		viewholder.name.setText(class_student.getName());
		
		viewholder.icon.setImageResource(R.drawable.pho_touxiang);
		if(class_student.getImgUrl()!=null){//头像
			String url = DataUtil.getDownloadURL(activity, class_student.getImgUrl());
			activity.getImageLoader().displayImage(url,
					viewholder.icon,
					(activity).getImageLoaderOptions());
		}
		
		Student student = studentList.get(position);
		String header=student.getHeader();//名字首字母
		if (position == 0 || header != null
				&& !header.equals(studentList.get(position - 1).getHeader())) {
			
				viewholder.student_header.setVisibility(View.VISIBLE);
				viewholder.student_header.setText(header);//不同 则显示字母  
			
		} else {
			viewholder.student_header.setVisibility(View.GONE);//相同则隐藏字母
		}

		final int position_i = position;
		

		final Student student_aa = student;
		viewholder.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (isSelected.get(position_i)) {//原先是   true 
					isSelected.put(position_i, false);//点击后 为false
					setIsSelected(isSelected);

					student_id_List_checked.remove(student_aa.getId() + "");//取消则 删除
					student_name_List_checked.remove(student_aa.getName());

				} else {//原先是false     
					isSelected.put(position_i, true);//点击后 为true
					setIsSelected(isSelected);

					student_id_List_checked.add(student_aa.getId() + "");//选中则 添加
					student_name_List_checked.add(student_aa.getName());

				}
				

			}

		});
		viewholder.checkBox.setChecked(getIsSelected().get(position_i));
		return convertView;
	}

	class ViewHolder {
		TextView student_header;
		CheckBox checkBox;
		RoundImageView icon;
		TextView name;

		ViewHolder() {
		}
	}

	public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * 将选中的位置记录下来
	 * 
	 * @return HashMap&lt;Integer,Boolean&gt;
	 */
	public HashMap<Integer, Boolean> getIsSelected() {
		
		
		
		return isSelected;
	}

	public ArrayList<String> getStudent_id_List_checked() {
		return student_id_List_checked;
	}

	public void setStudent_id_List_checked(
			ArrayList<String> student_id_List_checked) {
		this.student_id_List_checked = student_id_List_checked;
	}

	public ArrayList<String> getStudent_name_List_checked() {
		return student_name_List_checked;
	}

	public void setStudent_name_List_checked(
			ArrayList<String> student_name_List_checked) {
		this.student_name_List_checked = student_name_List_checked;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.layout.new_select_student_list_item:

			break;
		default:
			break;
		}
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		return positionOfSection.get(sectionIndex);
	}

	@Override
	public int getSectionForPosition(int position) {
		return sectionOfPosition.get(position);
	}

	@Override
	public Object[] getSections() {//配置点击字母是跳转位置
		positionOfSection = new SparseIntArray();
		sectionOfPosition = new SparseIntArray();
		int count = getCount();
		List<String> list = new ArrayList<String>();
		list.add("#");
		positionOfSection.put(0, 0);
		sectionOfPosition.put(0, 0);
		for (int i = 1; i < count; i++) {

			String letter = studentList.get(i).getHeader();
			int section = list.size() - 1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				positionOfSection.put(section, i);
			}
			sectionOfPosition.put(i, section);
		}
		return list.toArray(new String[list.size()]);
		
	}
	
}
