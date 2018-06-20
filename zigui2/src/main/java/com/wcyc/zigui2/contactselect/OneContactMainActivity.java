package com.wcyc.zigui2.contactselect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.contactselect.LetterListView.OnTouchingLetterChangedListener;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
//import com.wcyc.zigui2.home.ScoreActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.RoundImageView;

//2014年10月12日 下午7:12:52
/**
 * 选择学生类 单选
 * @author 谭园园
 * @version 1.18
 */
public class OneContactMainActivity extends BaseActivity implements OnClickListener {
	private MyListAdapter adapter;
	private ListView listview;
	private TextView overlay , contact_choice_text;
	private int count = 1;
	static String TAG = "http ret";
	//朝霞新增点评的接口
	private final String http_url1 = "/myInfoservice/queryCommentStusAndCount";
	//原来的接口（成绩）
	private final String http_url = "/myInfoservice/queryStudents";
	//周涛考勤接口
	private final String http_url2 = "/attenRecordService/getCommentStusAndCount";
	private ClassChildBean classChild;
	private List<ClassChild> choiceChild = null;// 选中小孩返回给下一个页面的集合
	private LetterListView letterListView;
	private static final String NAME = "name", SORT_KEY = "sort_key";
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	public List<ClassChild> list = new ArrayList<ClassChild>();
	private WindowManager windowManager;
	private ImageView choiceAll;
	private List<ClassChild> childs;
	private Button sureButton , btn_time;
	private Button title_btn;
	private String teacherClassID ="";
	private String teacherId ="";
	private String comeForWebStuID ="";
	private String comeForWhere="";
	private String otherTypeStuID="";
	private String speedinessComment1;
	private String speedinessComment2;
	private String classname;//班级名称
	private String commentContent;//作业内容
	private String[] str;
	private String[] otherTypeStr ;
	private int selectCount;
	private LinearLayout id_rl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.one_contact_main);
		windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		initView();
		initDatas();
		initOverlay();
		initEvents();
		contact_choice_text.setText("选择学生");
		// 1 .历史考勤 3.成绩 4.点评
		if ("4".equals(comeForWhere)) {
			httpBusiInerface(teacherClassID,http_url1);
//			contact_choice_text.setText(classname);
		}else if ("3".equals(comeForWhere)) {
			httpBusiInerface(teacherClassID,http_url);
//			contact_choice_text.setText(classname);
			id_rl.setVisibility(View.GONE);
		}else if ("1".equals(comeForWhere)) {
			httpBusiInerface(teacherClassID,http_url2);
//			contact_choice_text.setText(classname);
			id_rl.setVisibility(View.GONE);
		}
		
	}

	/**
	 * 初始化视图控件.
	 */
	private void initView() {
		title_btn = (Button) findViewById(R.id.title_btn);
		btn_time = (Button) findViewById(R.id.btn_time);
		listview = (ListView) findViewById(R.id.contact_list_view);
		letterListView = (LetterListView) findViewById(R.id.letter_list_view);
		choiceAll = (ImageView) findViewById(R.id.contact_choice_all_check);
		sureButton = (Button) findViewById(R.id.contact_btn);
		id_rl = (LinearLayout) findViewById(R.id.id_rl);
		contact_choice_text = (TextView) findViewById(R.id.contact_choice_text);
	}

	/**
	 * 给控件设置基本数据.
	 */
	private void initDatas() {
		teacherClassID = this.getIntent().getStringExtra("teacherClassID");
		teacherId = this.getIntent().getStringExtra("teacherId");
		comeForWebStuID = this.getIntent().getStringExtra("comeForWebStuID");
		comeForWhere = this.getIntent().getStringExtra("comeForWhere");
		otherTypeStuID = this.getIntent().getStringExtra("otherTypeStuID");
		otherTypeStuID = this.getIntent().getStringExtra("otherTypeStuID");
		otherTypeStuID = this.getIntent().getStringExtra("otherTypeStuID");
		speedinessComment1 = this.getIntent().getStringExtra("speedinessComment1");
		speedinessComment2 =  this.getIntent().getStringExtra("speedinessComment2");
		commentContent = this.getIntent().getStringExtra("commentContent");
		classname = this.getIntent().getStringExtra("classname");
		if (comeForWebStuID != null) {
			int length = comeForWebStuID.split(",").length;
			str = new String[length];
			str = comeForWebStuID.split(",");
		}
		if (otherTypeStuID != null) {
			int length = otherTypeStuID.split(",").length;
			otherTypeStr = new String[length];
			otherTypeStr = otherTypeStuID.split(",");
		}
		
		
		title_btn.setText(R.string.choice_stu);
		alphaIndexer = new HashMap<String, Integer>();
		new Handler();
		new OverlayThread();
	}

	/**
	 * 给控件设置时间监听.
	 */
	private void initEvents() {
		title_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				titleBack();
			}
		});
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
//		choiceAll.setOnClickListener(this);
		sureButton.setOnClickListener(this);
		// 给listview设置条目监听事件
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				// 获取到ViewHolder对象
//				ViewHolder holder = (ViewHolder) view.getTag();
//				holder.checkBox.toggle();
//				if (choiceAll.isSelected()) {
//					count = 1;
//					choiceAll.setSelected(false);
//				}
//				// 将CheckBox的选中状况记录下来
//				adapter.getIsSelected().put(position,
//						holder.checkBox.isChecked());
//				selectCount = adapter.getSelectedCount();
//				sureButton.setText("确定" + "(" + (selectCount) + ")");
				if ("4".equals(comeForWhere)) {
					Bundle bundle1 = new Bundle();
					String url = new StringBuilder(Constants.WEBVIEW_URL)
					.append("/studentComment.do?method=findHistoryCommentByStuDetail&")
					.append("userId").append("=").append(getUserID()).append("&")
					.append("classId").append("=").append(teacherClassID).append("&")
					.append("teacherId").append("=").append(teacherId).append("&")
					.append("studentId").append("=").append(childs.get(position).getChildID()).append("&")
					.toString();
					bundle1.putString("url", url);
					newActivity(BaseWebviewActivity.class, bundle1);
					OneContactMainActivity.this.finish();
				}else if ("3".equals(comeForWhere)) {
					int i = kemu1(childs.get(position).getChildID());
					if (i==0) {
						Toast.makeText(OneContactMainActivity.this, "该学生暂无成绩信息", Toast.LENGTH_SHORT).show();
					}else {
						Bundle bundle1 = new Bundle();
						bundle1.putString("childName", childs.get(position).getChildName());
						bundle1.putString("studentId", childs.get(position).getChildID());
//						newActivity(ScoreActivity.class, bundle1);
//					newActivity(NoticeActivity.class, bundle1);
//						OneContactMainActivity.this.finish();
					}
				}else if ("1".equals(comeForWhere)) {
					if (childs.get(position).getCommentNum().equals("0")) {
						Toast.makeText(OneContactMainActivity.this, "该学生暂无考勤记录", Toast.LENGTH_SHORT).show();
					}else {
						Bundle bundle1 = new Bundle();
						String url = new StringBuilder(Constants.WEBVIEW_URL)
						.append("/studentAttenRecord.do?method=findHistoryKqByStudentDetail&")
						.append("userId").append("=").append(getUserID()).append("&")
						.append("classId").append("=").append(teacherClassID).append("&")
						.append("studentId").append("=").append(childs.get(position).getChildID()).append("&")
						.toString();
						bundle1.putString("url", url);
						newActivity(BaseWebviewActivity.class, bundle1);
						OneContactMainActivity.this.finish();
					}
				}
				
//				Intent intent = new Intent(OneContactMainActivity.this,ScoreActivity.class);
//				intent.putExtra("status", "teacher");
//				startActivity(intent);
			}
		});
		
		btn_time.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OneContactMainActivity.this.finish();
			}
		});
	}

	private void httpBusiInerface(String teacherClassID,String http_url_1) {
		JSONObject json = new JSONObject();
		try {
			json.put("teacherClassID", teacherClassID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (!isLoading()) {
			model.queryPost(http_url_1, json);
		}
	}

	@Override
	protected void getMessage(String data) {
		classChild = JsonUtils.fromJson(data, ClassChildBean.class);
		
//		try {
//			JSONObject obj = new JSONObject(data);
//			JSONArray ja = obj.getJSONArray("childList");
//			Gson gson = new Gson();
//			Type t = new TypeToken<List<ClassChildBean>>(){}.getType();
//			List<ClassChildBean> list = gson.fromJson(ja.toString(),t);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		childs = new ArrayList<ClassChild>();
		if (classChild.getResultCode() != Constants.SUCCESS_CODE) {
			// 提交失败
			DataUtil.getToast(classChild.getErrorInfo());
		} else {
			childs = classChild.getClassChildList();
			
			// web页面传过来已经选中的学生ID 与查询出来的值做比较 将其选中
//			if (childs != null && str.length != 0) {
//				if (childs.size() == str.length) {
//					choiceAll.setSelected(true);
//				}
//			}
			//移除不能选中的数据
			for (int i = 0; i < childs.size(); i++) {
				if (null != otherTypeStr) {
					String childId = childs.get(i).getChildID();
					for (int j = 0; j < otherTypeStr.length; j++) {
						String s = otherTypeStr[j];
						if (childId.equals(s.trim())) {
							childs.remove(i);
							i--;
						}
					}
				}
			}
			setAdapter(childs);
			//设置选中
			for (int i = 0; i < childs.size(); i++) {
				if (null != str) {
					String childId = childs.get(i).getChildID();
					for (int j = 0; j < str.length; j++) {
						String s = str[j];
						if (childId.equals(s)) {
							adapter.getIsSelected().put(i, true);
							break;
						}
					}
				}
			}
			if (adapter.getSelectedCount() > 0)
				sureButton.setText("确定" + "(" + adapter.getSelectedCount()+ ")");
			else
				sureButton.setText("确定");
		}
	}

	@SuppressWarnings("deprecation")
	public void getContent() {
		Cursor cur = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		startManagingCursor(cur);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void setAdapter(List<ClassChild> list) {
		adapter = new MyListAdapter(this, list);
		listview.setAdapter(adapter);
	}

	class MyListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		// 用来控制CheckBox的选中状况
		@SuppressLint("UseSparseArrays")
		private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
		private List<ClassChild> list;
		MyListAdapter adapter = null;

		public MyListAdapter() {
			initDate();
		}

		/**
		 * 初始化isSelected的数据.
		 */
		private void initDate() {
			int size = (null == list ? 0 : list.size());
			for (int i = 0; i < size; i++) {
				getIsSelected().put(i, false);
			}
		}

		public MyListAdapter(Context context, List<ClassChild> list) {
			mOptions = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.pho_touxiang)
					.showImageOnFail(R.drawable.pho_touxiang)
					.resetViewBeforeLoading(true).cacheInMemory(true)
					.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.showImageOnLoading(R.drawable.pho_touxiang)
					.displayer(new FadeInBitmapDisplayer(300)).build();
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];

			for (int i = 0; i < list.size(); i++) {
				String currentStr = getAlpha(list.get(i).getSortLetter());
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
						.getSortLetter()) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getSortLetter());
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
			initDate();
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			// String iconUrl = cv.getChildIconURL();
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.one_contact_list_item, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.contact_item_tv = (TextView) convertView.findViewById(R.id.contact_item_tv);
//				holder.checkBox = (CheckBox) convertView
//						.findViewById(R.id.contact_item_flag);
				holder.icon = (RoundImageView) convertView
						.findViewById(R.id.contact_item_icon);
				// holder.icon.setTag(iconUrl);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				resetViewHolder(holder);
			}
			ClassChild cv = list.get(position);
			setView(holder, position, cv);
			return convertView;
		}

		protected void resetViewHolder(ViewHolder p_ViewHolder) {
			p_ViewHolder.icon.setImageDrawable(null);
			p_ViewHolder.name.setText(null);
			p_ViewHolder.contact_item_tv.setText(null);
		}

		/**
		 * 给ListView设置数据.
		 * @param holder ViewHolder类型
		 * @param position 位置
		 * @param cv 班级孩子
		 */
		private void setView(ViewHolder holder, int position, ClassChild cv) {
			holder.icon.setTag(cv.getChildIconURL());
			String iconUrl = (String) holder.icon.getTag();
			getImageLoader().displayImage(iconUrl, holder.icon, mOptions);
			holder.name.setText(cv.getChildName());
			holder.contact_item_tv.setTextColor(Color.rgb(1, 174, 240));
			holder.contact_item_tv.setText(cv.getCommentNum());
//			holder.checkBox.setChecked(getIsSelected().get(position));
			String currentStr = getAlpha(list.get(position).getSortLetter());
			String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
					position - 1).getSortLetter()) : " ";
			if (!previewStr.equals(currentStr)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
		}

		class ViewHolder {
			TextView alpha;
//			CheckBox checkBox;
			RoundImageView icon;
			TextView name;
			TextView contact_item_tv;
		}

		/**
		 * 将选中的位置记录下来
		 * 
		 * @return HashMap&lt;Integer,Boolean&gt;
		 */
		public HashMap<Integer, Boolean> getIsSelected() {
			return isSelected;
		}

		public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
			adapter = new MyListAdapter();
			adapter.isSelected = isSelected;
		}

		/**
		 * 获取当前被选中是个数
		 */
		public int getSelectedCount() {
			int count = 0;
			for (int i = 0; i < isSelected.size(); i++) {
				boolean flag = isSelected.get(i);
				if (flag == true) {
					count++;
				}
			}
			return count;
		}

	}

	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.contact_overlay, null);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(120,
				120, 100, 0, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		windowManager.addView(overlay, lp);
	}

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		public void onTouchingLetterChanged(final String s, float y, float x) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				listview.setSelection(position);
				overlay.setText(sections[position]);
				overlay.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onTouchingLetterEnd() {
			overlay.setVisibility(View.GONE);
		}
	}

	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	@SuppressLint("DefaultLocale")
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	@Override
	protected void onDestroy() {
		if (windowManager != null) {
			windowManager.removeView(overlay);
		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		titleBack();
	}

	/**
	 * 返回键
	 */
	private void titleBack() {
		Bundle bundle2 = new Bundle();
		String url1 = "";
		//历史考勤返回
		if ("1".equals(comeForWhere)) {
			url1 = new StringBuilder(Constants.WEBVIEW_URL)
			.append("/studentAttenRecord.do?method=appteacherpastatten&")
			.append("userid").append("=").append(getUserID()).append("&")
			.append("teacherid").append("=").append(teacherId).append("&")
			.append("classid").append("=").append(teacherClassID).append("&")
			.append("classname").append("=").append(classname).append("&")
			.append("version").append("=").append(getCurVersion())
			.toString();
			bundle2.putString("url", url1);
			newActivity(BaseWebviewActivity.class, bundle2);  
		}//成绩返回
		else if ("3".equals(comeForWhere)) {
			url1 = new StringBuilder(Constants.WEBVIEW_URL)
			.append("/studentExamScore.do?method=appteacherlist&")
			.append("userid").append("=").append(getUserID()).append("&")
			.append("teacherid").append("=").append(teacherId).append("&")
			.append("version").append("=").append(getCurVersion())
			.toString();
			bundle2.putString("url", url1);
			newActivity(BaseWebviewActivity.class, bundle2);  
		}
		
//		int size = 0;
//		if (null != childs) {
//			boolean flag = false;
//			ClassChild child = null;
//			choiceChild = new ArrayList<ClassChild>();
//			for (int i = 0; i < size; i++) {
//				flag = adapter.getIsSelected().get(i);
//				if (true == flag) {
//					child = childs.get(i);
//					choiceChild.add(child);
//				}
//			}
//			Bundle bundle2 = new Bundle();
//			String url1 = "";
//			if (comeForWhere.equals("4")) {// 4：点评界面
//				if(comeForWebStuID.length() == 0 ) {
//					finish();
//				} else {
//					url1 = new StringBuilder(Constants.WEBVIEW_URL)
//					.append("/studentComment.do?method=appteacherlist&")
//					.append("userid").append("=").append(getUserID()).append("&")
//					.append("teacherid").append("=").append(teacherId).append("&")
//					.append("classescode").append("=").append(teacherClassID).append("&")
//					.append("studentids").append("=").append(comeForWebStuID).append("&")
//					.append("commentImageId").append("=").append(speedinessComment1).append("&")
//					.append("commentQuickId").append("=").append(speedinessComment2).append("&")
//					.append("content").append("=").append(commentContent)
//					.toString();
//					bundle2.putString("url", url1);
//					newActivity(BaseWebviewActivity.class, bundle2);  
//				}
//			}else if (comeForWebStuID.length() == 0 && otherTypeStuID.length() == 0
//					&& comeForWhere.equals("1")) {// 迟到情况首次过来
//				url1 = new StringBuilder(Constants.WEBVIEW_URL)
//				.append("/studentAttenRecord.do?method=appteacherlist&")
//				.append("userid").append("=").append(getUserID()).append("&")
//				.append("teacherid").append("=").append(teacherId).append("&")
//				.append("classid").append("=").append(teacherClassID)
//				.toString();
//				bundle2.putString("url", url1);
//				newActivity(BaseWebviewActivity.class, bundle2);
//				OneContactMainActivity.this.finish();
//			} else if (comeForWhere.equals("1")) {// 迟到情况非首次过来
//				url1 = new StringBuilder(Constants.WEBVIEW_URL)
//				.append("/studentAttenRecord.do?method=appteacherconfirmlater&")
//				.append("userid").append("=").append(getUserID()).append("&")
//				.append("teacherid").append("=").append(teacherId).append("&")
//				.append("classid").append("=").append(teacherClassID).append("&")
//				.append("studentid").append("=").append(comeForWebStuID)
//				.toString();
//				bundle2.putString("url", url1);
//				newActivity(BaseWebviewActivity.class, bundle2);
//			} else if (comeForWhere.equals("3")) {// 3：留校
//				url1 = new StringBuilder(Constants.WEBVIEW_URL)
//				.append("/studentAttenRecord.do?method=appteacherconfirmearly&")
//				.append("userid").append("=").append(getUserID()).append("&")
//				.append("teacherid").append("=").append(teacherId).append("&")
//				.append("classid").append("=").append(teacherClassID).append("&")
//				.append("earlystusid").append("=").append(otherTypeStuID).append("&")
//				.append("leavestusid").append("=").append(comeForWebStuID)
//				.toString();
//				bundle2.putString("url", url1);
//				newActivity(BaseWebviewActivity.class, bundle2);
//			} else if (comeForWhere.equals("2")) {// 2：早退
//				url1 = new StringBuilder(Constants.WEBVIEW_URL)
//				.append("/studentAttenRecord.do?method=appteacherconfirmearly&")
//				.append("userid").append("=").append(getUserID())
//				.append("&").append("teacherid").append("=").append(teacherId).append("&")
//				.append("classid").append("=").append(teacherClassID).append("&")
//				.append("earlystusid").append("=").append(comeForWebStuID).append("&")
//				.append("leavestusid").append("=").append(otherTypeStuID)
//				.toString();
//				bundle2.putString("url", url1);
//				newActivity(BaseWebviewActivity.class, bundle2);
//			}
//			OneContactMainActivity.this.finish();
//		}
		OneContactMainActivity.this.finish();
	}

	@Override
	public void onClick(View v) {
		if(adapter == null){
			return;
		}
		int size = 0;
		if (null != childs) {
			size = adapter.getIsSelected().size();
		}
		switch (v.getId()) {
		case R.id.contact_choice_all_check:// 全部选中
			count++;
			if (count % 2 == 0) {
				for (int i = 0; i < size; i++) {
					adapter.getIsSelected().put(i, true);
				}
				choiceAll.setSelected(true);
				selectCount = adapter.getSelectedCount();
				sureButton.setText("确定" + "(" + (selectCount) + ")");
			} else {
				for (int i = 0; i < size; i++) {
					adapter.getIsSelected().put(i, false);
				}
				choiceAll.setSelected(false);
				sureButton.setText("确定");
			}
			if (null != childs) {
				adapter.notifyDataSetChanged();
			}
			break;
		case R.id.contact_btn:// 选中完毕，确定按钮
			if (DataUtil.isFastDoubleClick()) {
				break;
			}
			if (null != childs) {
				boolean flag = false;
				ClassChild child = null;
				choiceChild = new ArrayList<ClassChild>();
				for (int i = 0; i < size; i++) {
					flag = adapter.getIsSelected().get(i);
					if (true == flag) {
						child = childs.get(i);
						choiceChild.add(child);
					}
				}
				// 没有选学生直接按确定 传值带其它页面
				if (0 != choiceChild.size()) {
					String childID = "";
					String str = "";
					for (int i = 0; i < choiceChild.size(); i++) {
						childID = choiceChild.get(i).getChildID();
						if (i == choiceChild.size() - 1) {
							str = str + childID;
						} else {
							str = str + childID + ",";
						}
					}
					/**
					 * http://10.0.7.60:8080/zgw/studentAttenRecord.do?method=
					 * appteacherconfirmlater &userid=5 &teacherid=003
					 * &classid=001 &studentid=001,002,003
					 */
					Bundle bundle1 = new Bundle();
					String url = "";
					if (comeForWhere.equals("1")) {// 迟到的情况下
						url = new StringBuilder(Constants.WEBVIEW_URL)
								.append("/studentAttenRecord.do?method=appteacherconfirmlater&")
								.append("userid").append("=")
								.append(getUserID()).append("&")
								.append("teacherid").append("=")
								.append(teacherId).append("&")
								.append("classid").append("=")
								.append(teacherClassID).append("&")
								.append("studentid").append("=").append(str)
								.toString();
						bundle1.putString("url", url);
						newActivity(BaseWebviewActivity.class, bundle1);
						OneContactMainActivity.this.finish();
					} else if (comeForWhere.equals("3")) {// 3：留校
						/**
						 * http://10.0.6.50:8080/zgw/studentAttenRecord.do?
						 * method=appteacherconfirmearly&
						 * userid=5&teacherid=003&
						 * classid=001&earlystusid=001,002&leavestusid=003
						 */
						url = new StringBuilder(Constants.WEBVIEW_URL)
								.append("/studentAttenRecord.do?method=appteacherconfirmearly&")
								.append("userid").append("=")
								.append(getUserID()).append("&")
								.append("teacherid").append("=")
								.append(teacherId).append("&")
								.append("classid").append("=")
								.append(teacherClassID).append("&")
								.append("earlystusid").append("=")
								.append(otherTypeStuID).append("&")
								.append("leavestusid").append("=").append(str)
								.toString();
						bundle1.putString("url", url);
						newActivity(BaseWebviewActivity.class, bundle1);
						OneContactMainActivity.this.finish();
					} else if (comeForWhere.equals("2")) {// 2：早退
						url = new StringBuilder(Constants.WEBVIEW_URL)
								.append("/studentAttenRecord.do?method=appteacherconfirmearly&")
								.append("userid").append("=").append(getUserID()).append("&")
								.append("teacherid").append("=").append(teacherId).append("&")
								.append("classid").append("=").append(teacherClassID).append("&")
								.append("earlystusid").append("=").append(str).append("&")
								.append("leavestusid").append("=").append(otherTypeStuID)
								.toString();
						bundle1.putString("url", url);
						newActivity(BaseWebviewActivity.class, bundle1);
						OneContactMainActivity.this.finish();
					} else if (comeForWhere.equals("4")) { // 4：点评界面
						url = new StringBuilder(Constants.WEBVIEW_URL)
								.append("/studentComment.do?method=appteacherlist&")
								.append("userid").append("=").append(getUserID()).append("&")
								.append("teacherid").append("=").append(teacherId).append("&")
								.append("classescode").append("=").append(teacherClassID).append("&")
								.append("studentids").append("=").append(str).append("&")
								.append("commentImageId").append("=").append(speedinessComment1).append("&")
								.append("commentQuickId").append("=").append(speedinessComment2).append("&")
								.append("content").append("=").append(commentContent)
								.toString();
						bundle1.putString("url", url);
						newActivity(BaseWebviewActivity.class, bundle1);
						OneContactMainActivity.this.finish();
					}
				} else {
					// 直接跳转到其它界面
					DataUtil.getToast("请选择学生");
				}
			} else {
				// 如果请求服务器失败childs为空 直接跳转到其它界面
				DataUtil.getToast("请求服务器失败");
			}
			break;
		default:
			break;
		}
	}
	
	public int kemu1(String studentId){
		String result = null;
		String ss = null;
		int i = 0;
		JSONObject json = new JSONObject();
		try {
			json.put("xsId", studentId);
			String url = new StringBuilder(Constants.SERVER_URL).append(
					"/studentexamservice/getStudentExamCourse").toString();
			result = HttpHelper.httpPostJson(url, json);
			JSONObject obj = new JSONObject(result);
			JSONArray json1 = obj.getJSONArray("courseResult");
			i = json1.length();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
}