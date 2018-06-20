package com.wcyc.zigui2.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.adapter.ContactAdapter;
import com.wcyc.zigui2.newapp.adapter.ContactSpinnerPopAdapter;
import com.wcyc.zigui2.bean.Classes;
import com.wcyc.zigui2.bean.MemberBean;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.widget.ForwardDialog;
import com.wcyc.zigui2.widget.Sidebar;
import com.wcyc.zigui2.widget.SpinnerButton;

/**
 * 联系人列表页
 * 
 */
public class ContactlistActivity extends BaseActivity {
	private ContactAdapter adapter;
	private List<User> contactList;
	private ListView listView;
	private Sidebar sidebar;
	private InputMethodManager inputMethodManager;
	private SpinnerButton spinnerButton;
	private ListView spinnerListView;
	private final String class_url = "/myInfoservice/queryClasses";
	private List<Classes> classList;// 到服务器查询到的老师所对应班级的详情信息
//	private List<Child> childs;//小孩列表
	private ContactSpinnerPopAdapter spinnerPopAdapter;
	private NewMemberBean member;
	private ImageView titleIcon;
	private Button button;
	
	//转发消息的id
	private String forward_msg_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_contact_list);
//		httpClassBusiInerface(getUserID());
		//是否是转发消息
		forward_msg_id = getIntent().getStringExtra("forward_msg_id");
		
		initView();
		initData();
	}
	private void initView() {
		titleIcon = (ImageView) findViewById(R.id.title2_icon);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		listView = (ListView) findViewById(R.id.list);
		sidebar = (Sidebar) findViewById(R.id.sidebar);
		button = (Button) findViewById(R.id.title2_btn);
		sidebar.setListView(listView);
		spinnerButton = (SpinnerButton) findViewById(R.id.title2_spinner);
		// 给Spinner设置监听数据
		spinnerButton.setResIdAndViewCreatedListener(R.layout.spinner_layout,
				new SpinnerButton.ViewCreatedListener() {
					@Override
					public void onViewCreated(View v) {
						spinnerListView = (ListView) v
								.findViewById(R.id.spinner_lv);
					}
				});
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ContactlistActivity.this.finish();
			}
		});
		spinnerListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String o = classList.get(position).getClassID();
				changeClassId(o);
				
				handleClick(classList.get(position).getGradeName()+classList.get(position).getClassName()+"通讯录");
				//设置选中的班级
				spinnerPopAdapter.setSelect(position);
				if ("1".equals(classList.get(position).getIsAdviser())) {
					titleIcon.setVisibility(View.VISIBLE);
				} else {
					titleIcon.setVisibility(View.INVISIBLE);
				}
			}
		});
		spinnerPopAdapter = new ContactSpinnerPopAdapter(this);
		spinnerListView.setAdapter(spinnerPopAdapter);
		member = CCApplication.app.getMemberInfo();
		contactList = new ArrayList<User>();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final User user = adapter.getItem(position);
				if(user.getGroup() == 0){
					String username = adapter.getItem(position).getUsername();
					if (Constants.NEW_FRIENDS_USERNAME.equals(username)) {
						// 进入申请与通知页面
//						User user = CCApplication.getInstance().getContactList()
//								.get(Constants.NEW_FRIENDS_USERNAME);
//						user.setUnreadMsgCount(0);
					} else if (Constants.GROUP_USERNAME.equals(username)) {
						// 进入群聊列表页面
					} else {
						if (forward_msg_id != null) {
							//如果是转发消息 则跳入聊天界面
							new ForwardDialog(ContactlistActivity.this,R.style.mystyle,R.layout.customdialog
									,user.getNick(),new OnClickListener() {
										@Override
										public void onClick(View v) {
											Intent intent = new Intent(ContactlistActivity.this, ChatActivity.class);
											intent.putExtra("userId", user.getUsername());
											intent.putExtra("userNick", user.getNick());
											intent.putExtra("avatar", user.getAvatar());
											intent.putExtra("forward_msg_id", forward_msg_id);
											startActivity(intent);
											ContactlistActivity.this.finish();
										}
									}).show();
						} else {
							// 进入用户详情页
							Intent intent = new Intent(ContactlistActivity.this,
									ContactDetail.class);
							intent.putExtra("cellPhone", user.getCellphone());
							intent.putExtra("userName", user.getUsername());
							intent.putExtra("userNick", user.getNick());
							// 接受者头像
							intent.putExtra("avatarUrl", user.getAvatar());
							startActivity(intent);
						}
					}
				} else if(user.getGroup() == 1){
					//进入群聊界面
//					if(DataUtil.isInGroup(user.getUsername())) {
						if (forward_msg_id != null) {
							new ForwardDialog(ContactlistActivity.this,R.style.mystyle,R.layout.customdialog
									,user.getNick(),new OnClickListener() {
										@Override
										public void onClick(View v) {
											Intent intent = new Intent(ContactlistActivity.this, ChatActivity.class);
											intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
											intent.putExtra("groupId", user.getUsername());
											intent.putExtra("forward_msg_id", forward_msg_id);
											startActivity(intent);
											ContactlistActivity.this.finish();
										}
									}).show();
						}else{
								Intent intent = new Intent(ContactlistActivity.this, ChatActivity.class);
								intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
								intent.putExtra("groupId", user.getUsername());
								startActivity(intent);
						}
//					}else{
//						DataUtil.getToast("您已被移出群组");
//					}
				}
			}
		});
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				if (ContactlistActivity.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (ContactlistActivity.this.getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(
								ContactlistActivity.this.getCurrentFocus()
										.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
		registerForContextMenu(listView);
		((Button) findViewById(R.id.title2_btn)).setText("通讯录");
	}
	/**
	 * 初始化班级信息
	 */
	private void initData() {
		// TODO Auto-generated method stub
		if (member != null) {// 家长
//			classList = member.getContactClassList();// 班级集合
			if(classList != null && classList.size()>0){
				Classes cls = classList.get(0);
				if(classList.size()>1){
					spinnerButton.showAble(true);
					findViewById(R.id.title2_town).setVisibility(View.VISIBLE);
					spinnerButton.setText(classList.get(0).getGradeName()+classList.get(0).getClassName() + "通讯录");
					handleClick(classList.get(0).getGradeName()+classList.get(0).getClassName() + "通讯录");
					spinnerPopAdapter.setClassBean(classList);
					spinnerPopAdapter.notifyDataSetChanged();
					
					if(cls.getIsAdviser().equals("1")){
						titleIcon.setVisibility(View.VISIBLE);
					}else{
						titleIcon.setVisibility(View.INVISIBLE);
					}
				}else{
					findViewById(R.id.title2_town).setVisibility(View.GONE);
				}
				changeClassId(cls.getClassID());
			}else{
				findViewById(R.id.title2_town).setVisibility(View.GONE);
			}
		}else{
			findViewById(R.id.title2_town).setVisibility(View.GONE);
		}
	}
	/**
	 * 设置选择的班级
	 * 
	 * @param classId
	 */
	private void changeClassId(String classId) {
		getContactList(classId);
		// 设置adapter
		adapter = new ContactAdapter(this, R.layout.row_contact, contactList,
				sidebar);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 获取班级联系人列表
	 * 
	 * @param classId
	 *            班级id
	 */
	private void getContactList(String classId) {
//		UserDao dao = new UserDao(ContactlistActivity.this);zs
//		contactList = dao.getContactListbyClassId(classId);
		// 排序
		Collections.sort(contactList, new Comparator<User>() {
			@Override
			public int compare(User lhs, User rhs) {
				return lhs.getHeader().compareTo(rhs.getHeader());
			}
		});
	}

	/*
	 *  与服务器通信 查询当前用户的班级数据
	 * 
	 * @param userID
	 *            void
	 */
/*	private void httpClassBusiInerface(String userID) {
		JSONObject json = new JSONObject();
		try {
			json.put("userID", userID);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (!isLoading()) {
			model.queryPost(class_url, json);
		}
	}*/

	@Override
	protected void getMessage(String data) {
//		NoticeBean ret = JsonUtils.fromJson(data, NoticeBean.class);
//		classList = new ArrayList<Classes>();
//		if (ret.getResultCode() != 200) {
//			DataUtil.getToast(ret.getErrorInfo());
//		} else {
//			Classes avdarClass = null;
//			classList = ret.getClassList();// 班级集合
//			for (int i = 0; i < classList.size(); i++) {
//				Classes classes = classList.get(i);
//				String avdar = classes.getIsAdviser();
//				if (avdar.equals("1")) {
//					avdarClass = classes;
//				}
//			}
//			if (member != null && member.getUserType() == 2
//					&& classList != null) {
//				// 老师账号处理
//				if (avdarClass != null) {
//					spinnerButton.setText(avdarClass.getGradeName()+avdarClass.getClassName());
//					handleClick(avdarClass.getGradeName()+avdarClass.getClassName());
//					titleIcon.setVisibility(View.VISIBLE);
//				} else {
//					spinnerButton.setText(classList.get(0).getGradeName()+classList.get(0).getClassName());
//					handleClick(classList.get(0).getGradeName()+classList.get(0).getClassName());
//					titleIcon.setVisibility(View.INVISIBLE);
//				}
//				spinnerPopAdapter.setClassBean(classList, member.getUserType());
//				spinnerPopAdapter.notifyDataSetChanged();
//				changeClassId(avdarClass.getClassID());
//			}
//			spinnerPopAdapter.setClassBean(classList, member.getUserType());
//			spinnerPopAdapter.notifyDataSetChanged();
//		}

	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	/**
	 * Spinner的点击效果
	 * 
	 * @param text
	 *            void
	 */
	private void handleClick(String text) {
		spinnerButton.dismiss();
		spinnerButton.setText(text);
	}
}
