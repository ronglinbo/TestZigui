package com.wcyc.zigui2.newapp.module.classdynamics;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.NewClassDynamicsBean;
import com.wcyc.zigui2.bean.NewClassDynamicsBean1;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.listener.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.ClassMap;
import com.wcyc.zigui2.newapp.bean.GradeClass;
import com.wcyc.zigui2.newapp.bean.GradeleaderBean;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewPersonalBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.news.NewSchoolNewsDetailsActivity;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.newapp.widget.RefreshListView1.OnRefreshListener;
import com.wcyc.zigui2.utils.CircleImageView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.ImageUtils;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.widget.RoundImageView;

/**
 * 个人所有动态列表界面
 * 
 * @author gdzheng 日 期： 2016-04-06
 */
public class NewPersonalDynamicsActivity extends BaseActivity implements
		OnClickListener {

	private RefreshListView1 personal_list;
	private RoundImageView personal_circleImageView;
	private String publisherImgUrl;
	private String publishUserId;
	private String publisherName;
	private LinearLayout title_back;
	private TextView new_content;
	private ImageView iv_bg;
	private int k = 2;// 请求下页班级动态的数据
	private TextView danamics_name;
	private TextView danamics_class;
	private String dynamics_class;
	private NewPersonalDynamicsAdapter mNewPersonalAdapter;
	private String usertype;
	private ArrayList<NewClasses> cList;
	private String totalPageNum;
	private List<NewPersonalBean> npbList;

	Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 个人动态
		setContentView(R.layout.new_personal_dynamic_main);
		initView();
		initData();
		initEvents();
	}

	private void initView() {
		title_back = (LinearLayout) findViewById(R.id.title_back);
		new_content = (TextView) findViewById(R.id.new_content);
		iv_bg = (ImageView) findViewById(R.id.iv_bg);
		danamics_name = (TextView) findViewById(R.id.danamics_name);
		danamics_class = (TextView) findViewById(R.id.danamics_class);
		personal_circleImageView = (RoundImageView) findViewById(R.id.personal_circleImageView);
		personal_list = (RefreshListView1) findViewById(R.id.personal_list);
	}

	private void initData() {
		title_back.setVisibility(View.VISIBLE);
		new_content.setText("个人动态");
		publishUserId = getIntent().getExtras().getString("publishUserId");
		publisherName = getIntent().getExtras().getString("publisherName");
		dynamics_class = getIntent().getExtras().getString("dynamics_class");
		publisherImgUrl = getIntent().getExtras().getString("publisherImgUrl");
		if (publisherImgUrl != null) {
			if (LocalUtil.mBitMap != null) {
				personal_circleImageView.setImageBitmap(LocalUtil.mBitMap);
			}else{
				//方法二
				ImageUtils.showImage(this, publisherImgUrl, personal_circleImageView);//缩略图
			}
		} else {
			if (LocalUtil.mBitMap != null) {
				personal_circleImageView.setImageBitmap(LocalUtil.mBitMap);
			} else {
				if(publishUserId.equals(CCApplication.getInstance().getMemberDetail().getUserId())){//发布人就是登陆人
					String file = CCApplication.getInstance().getMemberInfo()
						.getUserIconURL();
					ImageUtils.showImage(this, file, personal_circleImageView);
				}
			}
		}
		requestClassList();
	}

	private void initEvents() {
		title_back.setOnClickListener(this);
		personal_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String classId = getIntent().getExtras().getString("classId");
				Intent intent = new Intent(NewPersonalDynamicsActivity.this,
						NewPersonalDetailsActivity.class);
				intent.putExtra("v", arg2 - 1);
				intent.putExtra("publishUserId", publishUserId);
				if (!DataUtil.isNullorEmpty(classId)) {
					intent.putExtra("classId", classId);
				}
				startActivity(intent);
			}
		});
		personal_list.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return false;
			}

		});

		personal_list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onDownPullRefresh() {// 下拉刷新
				requestClassList();
				personal_list.hideHeaderView();// 收起下拉刷新
			}

			@Override
			public void onLoadingMore() {// 上拉加载更多
				if (k <= Integer.parseInt(totalPageNum)) {// 后一页页， 总页数// 如果后面还有数据// 则加载一页
					loadData();
				}
				personal_list.hideFooterView();
			}
		});

	}

	private boolean isClassIdExist(List<NewClasses> classlist, String classId) {
		for (int i = 0; i < classlist.size(); i++) {
			if (classlist.get(i).getClassID().equals(classId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			NewPersonalDynamicsActivity.this.finish();
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//		initData();
	}

	@Override
	protected void getMessage(String data) {
		try {
			JSONObject json3 = new JSONObject(data);
			totalPageNum = json3.getString("totalPageNum");
			System.out.println("===totalPageNum===" + totalPageNum);
			JSONArray ja = json3.getJSONArray("interactionList");
			Gson gson1 = new Gson();
			Type t = new TypeToken<List<NewPersonalBean>>() {
			}.getType();
			npbList = gson1.fromJson(ja.toString(), t);
			String userId = CCApplication.getInstance().getPresentUser()
					.getUserId();
			if (npbList == null || npbList.size() < 1) {
				personal_list.setVisibility(View.GONE);
				iv_bg.setVisibility(View.VISIBLE);// 无数据 显示这张图
				if (publishUserId.equals(userId)) {
					iv_bg.setImageResource(R.drawable.new_no_imageview);
					iv_bg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									NewPersonalDynamicsActivity.this,
									NewPublishDynamicActivity.class);
							startActivity(intent);
						}
					});
				}
			} else {
				personal_list.setVisibility(View.VISIBLE);
				iv_bg.setVisibility(View.GONE);// 有数据 隐藏
				mNewPersonalAdapter = new NewPersonalDynamicsAdapter(this,
						npbList);
				personal_list.setAdapter(mNewPersonalAdapter);
			}
			if (publishUserId.equals(userId)) {
				new_content.setText("我的动态");
			} else {
				new_content.setText(publisherName);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	// 请求数据
	public void requestClassList() {
		k=2;//初始化k
		npbList = null;
		try {
			// JSON对象
			JSONObject json = null;
			// 结果
			usertype = CCApplication.getInstance().getPresentUser()
					.getUserType();
			if ("2".equals(usertype)) {
				List<NewClasses> cList_aa = CCApplication.app.getMemberDetail()
						.getClassList();
				if (cList_aa != null) {
					cList = new ArrayList<NewClasses>();
					for (NewClasses classes : cList_aa) {
						if (!isClassIdExist(cList, classes.getClassID()))// 是否已存在list中
							cList.add(classes);
					}
				}
				boolean allowAllClassTag=false;
				boolean gradeleader=false;
				MemberDetailBean detail=CCApplication.getInstance().getMemberDetail();
				for (int i = 0; i < detail.getRoleList().size(); i++) {
					String roleCode = detail.getRoleList().get(i).getRoleCode();

					if("schooladmin".equals(roleCode)){
						allowAllClassTag=true;
					}

					if("schoolleader".equals(roleCode)){
						allowAllClassTag=true;
					}

					if("fileadmin".equals(roleCode)){
						allowAllClassTag=true;
					}

					if ("gradeleader".equals(roleCode)) {
						allowAllClassTag = true;
						gradeleader = true;
					}

					String phoneNumA=CCApplication.getInstance().getPhoneNum();
					if("13687395021".equals(phoneNumA)){
						allowAllClassTag=true;
					}

				}

				if(allowAllClassTag){
					if (cList == null) {
						cList = new ArrayList<NewClasses>();
					}
					List<NewClasses> schoolAllClassList=CCApplication.getInstance().getSchoolAllClassList();
					if(schoolAllClassList!=null&& !gradeleader){
						cList.clear();
						cList.addAll(schoolAllClassList);
					}else if (schoolAllClassList != null && gradeleader){//如果是年级组长
						try {
							if (cList == null) {
								cList = new ArrayList<NewClasses>();
							}
							List<GradeleaderBean> gradeInfoList = CCApplication.getInstance().getMemberDetail().getGradeInfoList();
							for (int i = 0; i < gradeInfoList.size(); i++) {
								String userGradeId=gradeInfoList.get(i).getGradeId();
								for (int j = 0; j < schoolAllClassList.size(); j++){
									String gradeId=schoolAllClassList.get(j).getGradeId();
									if(userGradeId.equals(gradeId)){
										cList.add(schoolAllClassList.get(j));
									}
								}
							}
						} catch (Exception e) {
						}

					}
				}

			} else if ("3".equals(usertype)) {
				cList = new ArrayList<NewClasses>();

				UserType presentUser = CCApplication.getInstance().getPresentUser();

				NewClasses newclass = new NewClasses();

				newclass.setClassID(presentUser.getClassId());
				newclass.setClassName(presentUser.getClassName());
				newclass.setGradeId(presentUser.getGradeId());
				newclass.setGradeName(presentUser.getGradeName());

				cList.add(newclass);
			}

			String classId = getIntent().getExtras().getString("classId");
			ArrayList<String> mmClassIDlist = new ArrayList<String>();
			for (int i = 0; i < cList.size(); i++) {
				String mClassID = cList.get(i).getClassID();
				mmClassIDlist.add(mClassID);
				if (!DataUtil.isNullorEmpty(classId)) {
					if (classId.equals(cList.get(i).getClassID())) {
						dynamics_class = cList.get(i).getClassName();
					}
				}
			}
			if (!DataUtil.isNullorEmpty(classId)) {
				mmClassIDlist.clear();
				mmClassIDlist.add(classId);
			}
			danamics_name.setText(publisherName);
			danamics_class.setText(dynamics_class);
			NewClassDynamicsBean cd = new NewClassDynamicsBean();
			cd.setClassIdList(mmClassIDlist);
			cd.setIsNeedCLA("1");
			cd.setType("0");
			cd.setCurPage(1);
			cd.setPageSize(10);

			Gson gson = new Gson();
			String string = gson.toJson(cd);
			json = new JSONObject(string);
			json.put("type", "1");
			json.put("userId", publishUserId);
			System.out.println("===个人动态入参==json====" + json);

			//网络是否可用
			if (!DataUtil.isNetworkAvailable(NewPersonalDynamicsActivity.this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}
			//是否在上一个请求中
			queryPost(Constants.GET_CLASS_DYNAMIC_LIST, json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// //加载数据
	public void loadData() {
		if (!DataUtil.isNetworkAvailable(getBaseContext())) {
			DataUtil.getToast("当前网络不可用，请检查您的网络设置");
			return;
		}
		loadDataByIndex(k);
		System.out.println("==第k页==" + k);
		k++;
	}

	// 加载更多
	public void loadDataByIndex(int index) {
		List<NewPersonalBean> npbList_2 = null;
		try {
			// JSON对象
			JSONObject json = null;
			// 结果
			String result = null;
			usertype = CCApplication.getInstance().getPresentUser()
					.getUserType();
			if ("2".equals(usertype)) {
				List<NewClasses> cList_aa = CCApplication.app.getMemberDetail()
						.getClassList();
				if (cList_aa != null) {
					cList = new ArrayList<NewClasses>();
					for (NewClasses classes : cList_aa) {
						if (!isClassIdExist(cList, classes.getClassID()))// 是否已存在list中
							cList.add(classes);
					}
				}

			} else if ("3".equals(usertype)) {
				cList = new ArrayList<NewClasses>();
				UserType presentUser = CCApplication.getInstance().getPresentUser();
				NewClasses newclass = new NewClasses();
				newclass.setClassID(presentUser.getClassId());
				newclass.setClassName(presentUser.getClassName());
				newclass.setGradeId(presentUser.getGradeId());
				newclass.setGradeName(presentUser.getGradeName());
				cList.add(newclass);
			}
			String classId = getIntent().getExtras().getString("classId");
			ArrayList<String> mmClassIDlist = new ArrayList<String>();
			for (int i = 0; i < cList.size(); i++) {
				String mClassID = cList.get(i).getClassID();
				mmClassIDlist.add(mClassID);
				if (!DataUtil.isNullorEmpty(classId)) {
					if (classId.equals(cList.get(i).getClassID())) {
						dynamics_class = cList.get(i).getClassName();
					}
				}
			}
			if (!DataUtil.isNullorEmpty(classId)) {
				mmClassIDlist.clear();
				mmClassIDlist.add(classId);
			}
			danamics_name.setText(publisherName);
			danamics_class.setText(dynamics_class);
			NewClassDynamicsBean cd = new NewClassDynamicsBean();
			cd.setClassIdList(mmClassIDlist);
			cd.setIsNeedCLA("1");
			cd.setType("0");
			cd.setCurPage(k);
			cd.setPageSize(10);

			Gson gson = new Gson();
			String string = gson.toJson(cd);
			json = new JSONObject(string);
			json.put("type", "1");
			json.put("userId", publishUserId);
			System.out.println("=====json====" + json);
			String url = new StringBuilder(Constants.SERVER_URL).append(
					Constants.GET_CLASS_DYNAMIC_LIST).toString();
			result = HttpHelper.httpPostJson(this, url, json);
			JSONObject json3 = new JSONObject(result);
			totalPageNum = json3.getString("totalPageNum");
			System.out.println("===totalPageNum===" + totalPageNum);
			JSONArray ja = json3.getJSONArray("interactionList");
			Gson gson1 = new Gson();
			Type t = new TypeToken<List<NewPersonalBean>>() {
			}.getType();
			npbList_2 = gson1.fromJson(ja.toString(), t);
			String userId = CCApplication.getInstance().getPresentUser()
					.getUserId();
			if (npbList_2 == null || npbList_2.size() < 1) {
				personal_list.setVisibility(View.GONE);
				iv_bg.setVisibility(View.VISIBLE);// 无数据 显示这张图
				if (publishUserId.equals(userId)) {
					iv_bg.setImageResource(R.drawable.new_no_imageview);
					iv_bg.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									NewPersonalDynamicsActivity.this,
									NewPublishDynamicActivity.class);
							startActivity(intent);
						}
					});
				}
			} else {
				personal_list.setVisibility(View.VISIBLE);
				iv_bg.setVisibility(View.GONE);// 有数据 隐藏
				mNewPersonalAdapter.addItem(npbList_2);
				mNewPersonalAdapter.notifyDataSetChanged();
			}

			if (publishUserId.equals(userId)) {
				new_content.setText("我的动态");

			} else {

				new_content.setText(publisherName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}