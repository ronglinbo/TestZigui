/*
 * 文 件 名:ChooseTeacherFragment.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-08
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.chooseContact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.util.HanziToPinyin;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.SearchContactActivity;
import com.wcyc.zigui2.newapp.bean.AllTeacherList;
import com.wcyc.zigui2.newapp.bean.AllTeacherList.TeacherMap;
import com.wcyc.zigui2.newapp.bean.LastRecord;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.email.InboxFragment;
import com.wcyc.zigui2.newapp.widget.CharacterParser;
import com.wcyc.zigui2.newapp.widget.PinyinComparator;
import com.wcyc.zigui2.newapp.widget.SideBar;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.LocalUtil;

import org.apache.commons.logging.Log;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//选择老师fragment
public class ChooseTeacherFragment extends Fragment {
	private AllTeacherList list;
	private List<TeacherMap> teacherList, chooseList, showList, teacherList1;
	private View view;
	private ListView listView;
	public ListAdapter adapter;
	private Button searchButton;



	private Map<Integer, Boolean> isChecked = new HashMap<Integer, Boolean>();
	public static final int SEARCH_TEACHER = 100;
	private Context mcontext;
	private String userId;
	private TextView dialog;
	private SideBar sidrbar;
	private CharacterParser characterParser;
	private List<LastRecord.LastInfoSchoolDailyRecordBean.IsdrListBean> lastList;//上一条记录的list

	public Map<Integer, Boolean> getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Map<Integer, Boolean> isChecked) {
		this.isChecked = isChecked;
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
		view = inflater.inflate(R.layout.choose_teacher_fragment, null);
		mcontext = getActivity();
		UserType user = CCApplication.getInstance().getPresentUser();
		if (user != null) {
			userId = user.getUserId();
		}
		return view;
	}

	//	public ChooseTeacherFragment(AllTeacherList list,List<TeacherMap> choosedTeacher){
//		this.list = list;
//		this.showList = choosedTeacher;
//	}
	public ChooseTeacherFragment() {
	}


	//设置最后最后一条记录所选中的教师
	private void setLastTeacherList() {
		if (showList!=null){
			for (TeacherMap teacher : showList) {
				isChecked.put(teacher.getId(), true);
			}
			adapter.notifyDataSetChanged();
		}else if (lastList != null && lastList.size() > 0) {
			for (LastRecord.LastInfoSchoolDailyRecordBean.IsdrListBean isdrListBean : lastList) {
				isChecked.put(isdrListBean.getUserId(), true);
			}
			adapter.notifyDataSetChanged();
		}

	}

	public void listToMap() {
		int i = 0;
		if (showList != null) {
			for (TeacherMap teacher : showList) {
				isChecked.put(showList.get(i++).getId(), true);
			}
		}

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			list = (AllTeacherList) bundle.getSerializable("list");
			showList = (List<TeacherMap>) bundle.getSerializable("choosed");
		}
	}

	public static Fragment newInstance(int index, AllTeacherList list, List<TeacherMap> choosedTeacher) {
		// TODO Auto-generated method stub
		Fragment fragment;
		if (ChooseTeacherActivity.getTeacherFragment() != null) {
			fragment = ChooseTeacherActivity.getTeacherFragment();
		} else {
			fragment = new ChooseTeacherFragment();
		}

		Bundle args = new Bundle();
		args.putInt("index", index);
		args.putSerializable("list", list);
		args.putSerializable("choosed", (Serializable) choosedTeacher);
		fragment.setArguments(args);
		//fragment.setIndex(index);
		return fragment;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Intent intent = getActivity().getIntent();
		//获取上个页面传过来最后一条日志记录的list
		lastList = (List<LastRecord.LastInfoSchoolDailyRecordBean.IsdrListBean>) intent.getSerializableExtra("lastList");
		initView();
		initData();
	}

	private void initView() {
		//新增控件

		dialog = (TextView) view.findViewById(R.id.dialog);
		sidrbar = (SideBar) view.findViewById(R.id.sidrbar);
		characterParser = CharacterParser.getInstance();
		sidrbar.setTextView(dialog);
		sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					listView.setSelection(position);
				}
			}
		});

		listView = (ListView) view.findViewById(R.id.contact_list_view);
		searchButton = (Button) view.findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putInt("TeacherList", 1);
				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchContactActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, SEARCH_TEACHER);
			}

		});
	}

	private void initData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				listToMap();
				sort();
				if (list != null && list.getTeacherMapList().size() > 0) {
					teacherList = list.getTeacherMapList();
					teacherList1 = filledData((ArrayList<TeacherMap>) teacherList);
				}
				PinyinComparator pinyinComparator = new PinyinComparator();
				if (pinyinComparator != null) {
					if (teacherList1 != null) {
						Collections.sort(teacherList1, pinyinComparator);
					}
				}
				adapter = new ListAdapter(teacherList1);
				listView.setAdapter(adapter);
				chooseList = new ArrayList<TeacherMap>();
				setLastTeacherList();
			}

		}).run();

	}
	public void setData(List<TeacherMap> teacherList) {
		isChecked.clear();
		for (int j = 0; j < teacherList.size(); j++) {
			isChecked.put(teacherList.get(j).getId(), true);

		}

	}
	public void setData1(List<TeacherMap> teacherList) {
		isChecked.clear();
		for  (int j = 0; j < teacherList.size(); j++) {
		     if(teacherList.get(j).getId()>0){
				 isChecked.put(teacherList.get(j).getId(), true);
			 }


		}
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}

	}

	public class ListAdapter extends BaseAdapter {
		private List<TeacherMap> teacherList1;

		public ListAdapter(List<TeacherMap> teacherList1) {
			this.teacherList1 = teacherList1;
		}

		public void setData(List<TeacherMap> teacherList) {
			isChecked.clear();
			for (int j = 0; j < teacherList.size(); j++) {
				isChecked.put(teacherList.get(j).getId(), true);

			}
			if(adapter!=null){
				notifyDataSetChanged();
			}

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (teacherList1 != null)
				return teacherList1.size();
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return teacherList1.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final ViewHolder viewholder;
			if (arg1 == null) {
				viewholder = new ViewHolder();
				arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.list_teacher_item, null);
				viewholder.choose = (CheckBox) arg1.findViewById(R.id.choose);
				viewholder.image = (ImageView) arg1.findViewById(R.id.image);
				viewholder.name = (TextView) arg1.findViewById(R.id.name);
				viewholder.view = arg1.findViewById(R.id.teacher_info);
				viewholder.tvLetter = (TextView) arg1.findViewById(R.id.tv_catagory);
				viewholder.department_name = (TextView) arg1.findViewById(R.id.department_name);
				arg1.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) arg1.getTag();
			}
			final TeacherMap teacher = teacherList1.get(arg0);
			final int id = teacher.getId();
			if (!DataUtil.isNullorEmpty(teacher.getName())) {
				viewholder.name.setText(teacher.getName());
			} else {
				viewholder.name.setText(teacher.getEmployeeNo());
			}
			//字母搜索
			int section = getSectionForPosition(arg0);

			if (arg0 == getPositionForSection(section)) {
				viewholder.tvLetter.setVisibility(View.VISIBLE);
				viewholder.tvLetter.setText(teacher.getSortLetters());
			} else {
				viewholder.tvLetter.setVisibility(View.GONE);
			}
			//所在部门设置数据
			if (!DataUtil.isNullorEmpty(teacher.getDepartment_name())) {
				viewholder.department_name.setVisibility(View.VISIBLE);
				viewholder.department_name.setText(teacher.getDepartment_name());
			} else {
				viewholder.department_name.setVisibility(View.GONE);
			}


			setImage(viewholder, teacher.getPicAddress(), String.valueOf(id));
			viewholder.choose.setTag(teacher);

			if (isChecked != null && isChecked.containsKey(id)) {
				viewholder.choose.setChecked(isChecked.get(id));
			} else {
				viewholder.choose.setChecked(false);
			}

			viewholder.view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isChecked != null) {
						boolean checked = false;
						if (isChecked.containsKey(id)) {
							checked = isChecked.get(id);
						}

						if (checked == false) {
							//chooseList.add(teacher);
							viewholder.choose.setChecked(true);
							isChecked.put(id, !checked);
						} else {
							//chooseList.remove(teacher);
							viewholder.choose.setChecked(false);
							isChecked.remove(id);
						}
					}
				}

			});
			return arg1;
		}

		public int getSectionForPosition(int position) {
			return teacherList1.get(position).getSortLetters().charAt(0);
		}


		public int getPositionForSection(int section) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = teacherList1.get(i).getSortLetters();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}

			return -1;
		}

	}

	private void setImage(ViewHolder holder, String file, String userId) {
		if (file != null) {
			if (LocalUtil.mBitMap != null && userId != null && userId.equals(this.userId)) {
				holder.image.setImageBitmap(LocalUtil.mBitMap);
			} else {
				String url = DataUtil.getIconURL(file);
				System.out.println("url:" + url);
				((BaseActivity) mcontext).getImageLoader().displayImage(url, holder.image,
						((BaseActivity) mcontext).getImageLoaderOptions());
			}
		} else {
			holder.image.setImageResource(R.drawable.pho_touxiang);
		}

	}

	private static class ViewHolder {
		CheckBox choose;
		TextView name;
		ImageView image;
		View view;
		TextView department_name;//所在部门
		TextView tvLetter;
	}


	public List<TeacherMap> getChooseTeacherList() {
		if(chooseList==null){
			chooseList=new ArrayList<TeacherMap>();
		}
		chooseList.clear();
		if(teacherList==null){

				teacherList = CCApplication.getInstance().getAllTeacherList().getTeacherMapList();

		}

			for (TeacherMap teacher : teacherList) {
				int id = teacher.getId();
				if (isChecked.containsKey(id)) {
					chooseList.add(teacher);
				}
			}

		return chooseList;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	public Map<Integer, Boolean> getChooseTeacherMap() {
		return isChecked;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (SEARCH_TEACHER == requestCode && resultCode == -1) {
			int id = data.getIntExtra("teacherId", 0);
			int position = data.getIntExtra("position", 0);
			System.out.println("id:" + id);
			isChecked.put(id, true);
			int i=0;
			for (TeacherMap teacher:teacherList1) {
                if(teacher.getId()==id){
				   break;
				}
				i++;
			}
		//	listView.smoothScrollToPosition(i);
			adapter.notifyDataSetChanged();

			final int finalI = i;
			listView.post(new Runnable() {
				@Override
				public void run() {
					listView.setSelection(finalI);
				}
			});



			//listView.smoothScrollToPosition(i);

		}
	}

	private void sort() {
		if (list != null)
			teacherList = list.getTeacherMapList();
		if (teacherList == null) return;
		for (TeacherMap item : teacherList) {
			String name = item.getName();
			String header = getHeader(name);
			item.setHeader(header);
		}
		// 排序
		Collections.sort(teacherList, new Comparator<TeacherMap>() {
			@Override
			public int compare(TeacherMap lhs, TeacherMap rhs) {
				return lhs.getHeader().compareTo(rhs.getHeader());
			}
		});
	}

	private String getHeader(String name) {
		StringBuilder sb = new StringBuilder();
		ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(name);
		if (tokens != null && tokens.size() > 0) {
			for (HanziToPinyin.Token token : tokens) {
				if (HanziToPinyin.Token.PINYIN == token.type) {
					sb.append(token.target);
				} else {
					sb.append(token.source);
				}
			}
		}

		return sb.toString().toLowerCase();
	}

	private List<TeacherMap> filledData(ArrayList<TeacherMap> list1) {
		List<TeacherMap> mSortList = new ArrayList<TeacherMap>();
		ArrayList<String> indexString = new ArrayList<String>();

		for (int i = 0; i < list1.size(); i++) {
			TeacherMap sortModel = list1.get(i);

			//去掉名字前面的空格
			String name = sortModel.getName().replace(" ", "");
			String pinyin = characterParser.getSelling(name);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]") || sortString.matches("[1-9]")) {
				//对与账号开头为1-9的账户做特殊处理
				if (pinyin.startsWith("[1-9]")) {
					sortString = "#";
					sortModel.setSortLetters("#");
				} else {
					sortModel.setSortLetters(sortString.toUpperCase());
				}


				if (!indexString.contains(sortString)) {
					indexString.add(sortString);
				}
			}
			mSortList.add(sortModel);
		}
		Collections.sort(indexString);
//        sidrbar.setIndexText(indexString);
		return mSortList;
	}

/*	@Override
	public void onPause() {
		if (isChecked!=null){
			isChecked.clear();
		}
		super.onPause();
	}*/

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