package com.wcyc.zigui2.newapp.home;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.contactselect.LetterListView;
import com.wcyc.zigui2.contactselect.LetterListView.OnTouchingLetterChangedListener;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.listener.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.adapter.NewSelectStudentAdapter;
import com.wcyc.zigui2.newapp.bean.ClassStudent;
import com.wcyc.zigui2.newapp.bean.NewUsedHelpBean;
import com.wcyc.zigui2.newapp.bean.ClassStudent.Student;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.widget.Sidebar;
import com.wcyc.zigui2.widget.SidebarSelectStudent;
/**
 * 选择学生界面   列表  选多少个
 * 
 * @author 郑国栋
 * 2016-4-18
 * @version 2.0
 */
public class NewSelectStudentActivity extends BaseActivity implements OnClickListener{
	/** 标题 */
	private TextView new_content;
	/** 取消 */
	private TextView title2_off;
	/** 确定 */
	private TextView title2_ok;
	private ClassStudent studentList;// 学生列表
	private ListView student_list_view;
	private ArrayList<Student> studentList_aa;
	private NewSelectStudentAdapter myNewSelectStudentAdapter;
	private LetterListView letterListView;
	private BaseActivity activity;
	
	private HashMap<String, Integer> alphaIndexer;
	private TextView overlay;
	private String[] sections;
	private SidebarSelectStudent sidebarSelectStudent;
	private HashMap<Integer, Boolean> isSelected=new HashMap<Integer, Boolean>();

	/** 全选 */
	private Button bt_selectall;
	/** 取消 */
	private Button bt_deselectall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.new_select_student);
		
		initView();
		initDatas();
		initEvents();
		
	}
	
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);
		title2_off = (TextView) findViewById(R.id.title2_off);
		title2_ok = (TextView) findViewById(R.id.title2_ok);
		student_list_view = (ListView) findViewById(R.id.contact_list_view);
		sidebarSelectStudent = (SidebarSelectStudent) findViewById(R.id.sidebarSelectStudent);//侧边拼音首字母
		sidebarSelectStudent.setVisibility(View.VISIBLE);
		sidebarSelectStudent.setListView(student_list_view);

		bt_selectall = (Button) findViewById(R.id.bt_selectall);
		bt_deselectall = (Button) findViewById(R.id.bt_deselectall);
		
	}
	private void initDatas() {
		new_content.setText("选择学生");

		String result="";
		try {
			
			String classId=getIntent().getExtras().getString("classId");
			isSelected=(HashMap<Integer, Boolean>) getIntent().getExtras().getSerializable("isSelected");
			
			
			// JSON对象
			JSONObject json = new JSONObject();
			json.put("classId", classId);
//			json.put("classId", "1");
			
			//请求地址
			String url = new StringBuilder(Constants.SERVER_URL).append(Constants.GET_CLASS_STUDENT_LIST).toString();
			
			if (!DataUtil.isNetworkAvailable(NewSelectStudentActivity.this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				
			}
			
			result = HttpHelper.httpPostJson(this,url, json);
			
			JSONObject json3 = new JSONObject(result);

			//活动学生list
			JSONArray ja = json3.getJSONArray("studentList");
			Gson gson1 = new Gson();
			Type t = new TypeToken<List<Student>>() {}.getType();
			//将gson格式转为json格式的字符串
			studentList_aa = gson1.fromJson(ja.toString(), t);
			
			if(studentList_aa!=null){//设置首字母 和   学生按首字母排序
				for (int i = 0; i < studentList_aa.size(); i++) {
					studentList_aa.get(i).setHeaderStr();
				}
				// 排序
				Collections.sort(studentList_aa, new Comparator<Student>() {				
					@Override
					public int compare(Student lhs, Student rhs) {
						return lhs.getHeader().compareTo(rhs.getHeader());
					}
				});				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		activity=this;
		//设置选择学生adapter
		myNewSelectStudentAdapter = new NewSelectStudentAdapter(this,
				R.layout.new_select_student_list_item,activity,studentList_aa,sidebarSelectStudent,isSelected);
		student_list_view.setAdapter(myNewSelectStudentAdapter);
		student_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	private void initEvents() {
		title2_off.setOnClickListener(this);
		title2_ok.setOnClickListener(this);


		//全选
		bt_selectall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 遍历list的长度，将MyAdapter中的map值全部设为true
				if(studentList_aa!=null){
					myNewSelectStudentAdapter.getStudent_id_List_checked().clear();
					myNewSelectStudentAdapter.getStudent_name_List_checked().clear();

					for (int i = 0; i < studentList_aa.size(); i++) {
						myNewSelectStudentAdapter.getIsSelected().put(i, true);

						Student student = studentList_aa.get(i);
						myNewSelectStudentAdapter.getStudent_id_List_checked().add(student.getId()+"");
						myNewSelectStudentAdapter.getStudent_name_List_checked().add(student.getName());
					}
					// 刷新listview
					myNewSelectStudentAdapter.notifyDataSetChanged();
				}
			}

		});

		//取消
		// 取消按钮的回调接口
		bt_deselectall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 遍历list的长度，将MyAdapter中的map值全部设为false
				if(studentList_aa!=null){
					for (int i = 0; i < studentList_aa.size(); i++) {
						myNewSelectStudentAdapter.getIsSelected().put(i, false);

						Student student = studentList_aa.get(i);
						myNewSelectStudentAdapter.getStudent_id_List_checked().remove(student.getId()+"");
						myNewSelectStudentAdapter.getStudent_name_List_checked().remove(student.getName());
					}
					// 刷新listview
					myNewSelectStudentAdapter.notifyDataSetChanged();
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		if(myNewSelectStudentAdapter == null){
			return;
		}
		switch (v.getId()) {
		case R.id.title2_off:
			NewSelectStudentActivity.this.finish();//取消键
			break;
		case R.id.title2_ok://确定键
			//返回需要的数据
			ArrayList<String> student_id_List_checked = myNewSelectStudentAdapter
					.getStudent_id_List_checked();
			ArrayList<String> student_name_List_checked = myNewSelectStudentAdapter
					.getStudent_name_List_checked();
			isSelected = myNewSelectStudentAdapter.getIsSelected();
			Bundle bundle=new Bundle();
			//封装数据
			bundle.putSerializable("student_id_List_checked", student_id_List_checked);
			bundle.putSerializable("student_name_List_checked", student_name_List_checked);
			bundle.putSerializable("isSelected", isSelected);
			
			//封装bundle
			Intent intent=new Intent();
//			intent.setClass(NewSelectStudentActivity.this, NewCommentActivity.class);
			intent.putExtras(bundle);
			
			//返回
			NewSelectStudentActivity.this.setResult(RESULT_OK, intent);//返回上一个界面，并带参数			
			NewSelectStudentActivity.this.finish();
			break;
		}	
	}
		
	@Override
	protected void getMessage(String data) {
	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(final String s, float y, float x) {
		}
		@Override
		public void onTouchingLetterEnd() {
		}
	}
}
