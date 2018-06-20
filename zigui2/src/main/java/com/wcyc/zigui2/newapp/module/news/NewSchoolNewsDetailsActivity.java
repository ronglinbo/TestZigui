package com.wcyc.zigui2.newapp.module.news;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.adapter.CommentsNewsListViewAdapter;
import com.wcyc.zigui2.bean.NewSchoolNewsCommentBean;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;

import com.wcyc.zigui2.newapp.activity.ImagePagerActivity;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewPointBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.MyListView;

/**
 * 校园新闻详情
 * 
 * @author 郑国栋 2016-6-30
 * @version 2.0
 */
public class NewSchoolNewsDetailsActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout title_back;
	private TextView new_content;
	private WebView webView;
	private TextView new_school_news_title;
	private TextView new_school_news_time;
	private TextView new_school_news_read_numb;
	private ImageView news_good_iv;
	private TextView news_goodNum;
	private ImageView news_comment_iv;
	private TextView news_commentNum;
	private MyListView news_comment_mylv;
	private String hasGoodComment;
	private String interactiveCount;
	private String news_goodNumStr;
	private String news_commentNumStr;
	private int news_goodNumInt;
	private List<NewPointBean> schoolNewsCommentList;
	private CommentsNewsListViewAdapter commentsNewsListViewAdapter;
	private TextView load_more;
	private NewSchoolNewsBean newSchoolNewsBean;
	private List<NewSchoolNewsCommentBean> newSchoolNewsCommentBeanList;
	private String campuNewsId;
	private String userid;
	private String userType;
	private int k = 2;
	private int size = 0;
	private int totalPageNum = 0;
	private String commentUserName;
	private String accId;
	private String deviceId;
	private String isRead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_school_news_details);
		initView();
		initDatas();
		initEvents();
	}

	// 实例化组件
	private void initView() {
		new_content = (TextView) findViewById(R.id.new_content);// 标题
		title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
		new_school_news_title = (TextView) findViewById(R.id.new_school_news_title);// 校园新闻标题
		new_school_news_time = (TextView) findViewById(R.id.new_school_news_time);// 校园新闻发布时间
		new_school_news_read_numb = (TextView) findViewById(R.id.new_school_news_read_numb);// 校园新闻浏览数
		webView = (WebView) findViewById(R.id.new_school_news_html_content);// 校园新闻内容
		news_good_iv = (ImageView) findViewById(R.id.news_good_iv);// 校园新闻点赞图标
		news_goodNum = (TextView) findViewById(R.id.news_goodNum);// 校园新闻点赞数
		news_comment_iv = (ImageView) findViewById(R.id.news_comment_iv);// 校园新闻评论图标
		news_commentNum = (TextView) findViewById(R.id.news_commentNum);// 校园新闻评论数
		news_comment_mylv = (MyListView) findViewById(R.id.news_comment_mylv);// 校园新闻评论listview
		load_more = (TextView) findViewById(R.id.load_more);
	}

	// 初始化数据
	@SuppressLint("NewApi")
	private void initDatas() {
		k=2;
		new_content.setText("校园新闻详情");
		newSchoolNewsBean = new NewSchoolNewsBean();
		newSchoolNewsCommentBeanList = new ArrayList<NewSchoolNewsCommentBean>();
		userid = CCApplication.getInstance().getPresentUser().getUserId();
		accId = CCApplication.getInstance().getMemberInfo().getAccId();
		deviceId = getDeviceID();
		userType = CCApplication.getInstance().getPresentUser().getUserType();
		 if ("2".equals(userType)) {
			 commentUserName = CCApplication.getInstance().getMemberInfo().getUserName();
		 } else if ("3".equals(userType)){
			 commentUserName = CCApplication.getInstance().getPresentUser().getChildName()
						+ CCApplication.getInstance().getPresentUser().getRelationTypeName();
		 }
		String position = getIntent().getStringExtra("position");
		campuNewsId = getIntent().getStringExtra("campuNewsId");
		isRead = getIntent().getStringExtra("isRead");
		try {
			JSONObject json2 = new JSONObject();
			json2.put("campuNewsId", campuNewsId);
			json2.put("userId", userid);
//			System.out.println("====accId==="+accId);//
//			System.out.println("====deviceId==="+deviceId);
			json2.put("imgAuthId", "MB@"+deviceId+"@"+accId);
			json2.put("curPage", 1);// curPage,pageSize
			json2.put("pageSize", 30);
			if (!DataUtil.isNetworkAvailable(NewSchoolNewsDetailsActivity.this)) {
				DataUtil.getToast(getResources().getString(R.string.no_network));
				return;
			}
			if (!isLoading()) {
				System.out.println("===校园新闻详情入参==" + json2);
				String url = new StringBuilder(Constants.SERVER_URL).append(
						Constants.GET_SCHOOL_NEWS_DETAILS).toString();
				String result = HttpHelper.httpPostJson(this, url, json2);
				System.out.println("===校园新闻详情出参==" + result);
				NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
				if (ret.getServerResult().getResultCode() != 200) {// 请求失败
					DataUtil.getToast(ret.getServerResult().getResultMessage());
					return;
				} else {
					JSONObject json3 = new JSONObject(result);
					String totalPageNumStr = json3.getString("totalPageNum");
					totalPageNum = Integer.parseInt(totalPageNumStr);
					System.out.println("==totalPageNum===" + totalPageNum);

					String schoolNews = json3.getString("schoolNews");
					newSchoolNewsBean = JsonUtils.fromJson(schoolNews,
							NewSchoolNewsBean.class);
					String commentListStr = json3.getString("commentList");
					if (!DataUtil.isNullorEmpty(commentListStr)) {
						JSONArray jsonA = new JSONArray(commentListStr);
						for (int i = 0; i < jsonA.length(); i++) {
							NewSchoolNewsCommentBean newSchoolNewsCommentBean = JsonUtils
									.fromJson(jsonA.get(i).toString(),
											NewSchoolNewsCommentBean.class);
							newSchoolNewsCommentBeanList
									.add(newSchoolNewsCommentBean);
						}
						size = newSchoolNewsCommentBeanList.size();// totalPage
					}
					hasGoodComment = json3.getString("hasGoodComment");
					interactiveCount= json3.getString("interactiveCount");

					//如果是未读
					if("0".equals(isRead)){
						//删除模块已读记录
						JSONObject jsonMod = new JSONObject();
						if(user != null){
							try {
								jsonMod.put("userId", user.getUserId());
								jsonMod.put("userType",user.getUserType());
								jsonMod.put("dataId",campuNewsId);
								jsonMod.put("modelType", "27");
								if(CCApplication.getInstance().isCurUserParent()){
									jsonMod.put("studentId", user.getChildId());
								}
//							System.out.println("===清除这条校园新闻记录数入参=="+jsonMod);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							String urlMod = new StringBuilder(Constants.SERVER_URL)
									.append(Constants.DEL_MODEL_REMIND_ZGD).toString();
							String resultMod = HttpHelper.httpPostJson(this, urlMod, jsonMod);
//							System.out.println("==resultMod==="+resultMod);
							// json对象 里面有属性ServerResult 请求结果
							NewBaseBean bb = JsonUtils.fromJson(resultMod,
									NewBaseBean.class);
							if (bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
								System.out.println("===校园新闻模块记录数-已删除===");
							}
						}
//						//删除消息记录
//						JSONObject jsonMes = new JSONObject();
//						if(user != null){
//							try {
//								jsonMes = new JSONObject();
//								jsonMes.put("userId", user.getUserId());
//								jsonMes.put("userType",user.getUserType());
//								jsonMes.put("msgId",campuNewsId);
//								jsonMes.put("messageType", "27");
//								if(CCApplication.getInstance().isCurUserParent()){
//									jsonMod.put("studentId", user.getChildId());
//								}
////							System.out.println("===删除除这条校园新闻消息记录入参=="+jsonMes);
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//
//							String urlMes = new StringBuilder(Constants.SERVER_URL)
//									.append(Constants.DEL_READ_MESSAGE).toString();
//							String resultMes = HttpHelper.httpPostJson(this, urlMes, jsonMes);
////							System.out.println("==resultMes==="+resultMes);
//							// json对象 里面有属性ServerResult 请求结果
//							NewBaseBean bb = JsonUtils.fromJson(resultMes,
//									NewBaseBean.class);
//							if (bb.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
//								System.out.println("===校园新闻消息页记录-已删除===");
//							}
//						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		new_school_news_title.setText(newSchoolNewsBean.getTitle());
		new_school_news_time.setText(newSchoolNewsBean.getPublishTime());
		new_school_news_read_numb.setText(newSchoolNewsBean.getBrowseNo()+"");
		String contentStr = newSchoolNewsBean.getContent();
		if(!DataUtil.isNullorEmpty(contentStr)){
			if (contentStr.length() > 0) {
//			String content=DataUtil.parseHtmlTwoContent(this,contentStr);
				String content=contentStr;
				//设置图片大小，文字大小不变，图片超过320px则最大，小于320px则原图显示
				String head="<head><style>img{max-width:320px !important;max-height:320px !important;}</style></head>";
				content=head+content;
				webView.getSettings().setDefaultTextEncodingName("UTF -8");// 设置默认为utf-8
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setSavePassword(false);
				webView.removeJavascriptInterface("searchBoxJavaBridge_");
				webView.removeJavascriptInterface("accessibility");
				webView.removeJavascriptInterface("accessibilityTraversal");

				// webView.loadData(data, "text/html",
				// "UTF -8");//API提供的标准用法，无法解决乱码问题
				webView.loadData(content, "text/html; charset=UTF-8", null);//
				// 这种写法可以正确解码
				// webView.loadUrl("file:///assets/www/wrong.html");
				// webView.loadUrl("file:///android_asset/www/wrong.html");
				// webView.loadUrl("http://news.ifeng.com/a/20160705/49299109_0.shtml");
				// webView.loadUrl("http://tl.changyou.com/screen.shtml");
				// webView.loadUrl("http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=11010702000007");
				// webView.loadUrl("http://www.baidu.com");
				// webView.loadUrl("http://www.hnflcp.com/");
				// webView.loadUrl(url, additionalHttpHeaders);
				// 添加js交互接口类，并起别名 imagelistner
				webView.addJavascriptInterface(new JavascriptInterface(this),
						"imagelistner");
				webView.setWebViewClient(new MyWebViewClient());
				webView.getSettings().setBlockNetworkImage(false);

				// 下面几句代码主要用来使网页自适应屏幕大小，设置false则按PC端大小显示
//			WebSettings webSettings = webView.getSettings();
////			webSettings.setUseWideViewPort(true);
//			webSettings.setLoadWithOverviewMode(true);
//			// 设置可以支持缩放
//			webSettings.setSupportZoom(true);
//			// 设置出现缩放工具
//			webSettings.setBuiltInZoomControls(false);
//			// 缩放功能
////			webSettings.setUseWideViewPort(false);
//			webSettings.setDomStorageEnabled(true);
//			webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//			webSettings.setSaveFormData(true);
//			webSettings.setAppCacheMaxSize(1024 * 5);
				DisplayMetrics dm = getResources().getDisplayMetrics();
				int dpi = dm.densityDpi;
				double scale = DataUtil.TableWidthExceed(this,content);
				if(scale > 1) {
					double times = ((double)dpi)/240;
					webView.setInitialScale((int) (scale * times * 100));
				}
			} else {
				webView.setVisibility(View.GONE);
			}
		}

		news_goodNumStr = newSchoolNewsBean.getGoodCommentNo();
		news_goodNum.setText(news_goodNumStr);
		news_commentNumStr = interactiveCount;
		news_commentNum.setText(news_commentNumStr);

		if ("true".equals(hasGoodComment)) {
			news_good_iv.setImageResource(R.drawable.new_love_ok);
		} else if ("false".equals(hasGoodComment)) {
			news_good_iv.setImageResource(R.drawable.new_love_no);
		}

		commentsNewsListViewAdapter = new CommentsNewsListViewAdapter(this,
				newSchoolNewsCommentBeanList);
		news_comment_mylv.setAdapter(commentsNewsListViewAdapter);
		if (totalPageNum > 1) {
			load_more.setVisibility(View.VISIBLE);
		} else {
			load_more.setVisibility(View.GONE);
		}
		
		
		Intent broadcast = new Intent(NewSchoolNewsActivity.NEWS_REFESH_DATA);//进入次界面，或刷新时，通知新闻列表刷新浏览数
		sendBroadcast(broadcast);
		//其实浏览数不对没关系，这个反而不合理，如图是第62条新闻，这个会从第一条加载，二不是定位到第62条
		//listview.setSelection(int position);
		//adapter.notifyDataSetInvalidated();通知adapter数据有变化

	}

	// 设置点击效果监听器
	private void initEvents() {
		title_back.setVisibility(View.VISIBLE);
		title_back.setOnClickListener(this);
		news_good_iv.setOnClickListener(this);
		news_comment_iv.setOnClickListener(this);
		load_more.setOnClickListener(this);
		news_comment_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleAddComments(0, false, 0);
			}

		});
		news_comment_mylv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String comment_user_id = newSchoolNewsCommentBeanList.get(arg2)
						.getComment_user_id();
				if (userid.equals(comment_user_id)) {
					// 相等就是删除
					handleDelete(0, arg2, 0);
				} else {
					// 不相等 就是回复那个回复人的回复
					handleAddComments(0, true, arg2);
				}
			}

		});
	}

	// 添加评论或回复评论
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void handleAddComments(final int i, final boolean isAt,
			final int arg2) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 方法一
		View view = inflater.inflate(R.layout.popwindow_ed, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		final PopupWindow window = new PopupWindow(view,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);
		// 防止虚拟软键盘被弹出菜单遮住
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);
		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示 被点击的控件的
		window.showAtLocation(findViewById(R.id.news_comment_iv),
				Gravity.BOTTOM, 0, 0);
		// 这里检验popWindow里的button是否可以点击
		final Button first = (Button) view.findViewById(R.id.pop_btn);
		final EditText ed = (EditText) view.findViewById(R.id.ed);
		if (isAt) {
			ed.setHint("回复"+newSchoolNewsCommentBeanList.get(arg2).getComment_user_name()+"评论...");
		} else {
			ed.setHint("评论...");
		}
		first.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(ed.getText())) {
					return;
				}
				if (!DataUtil
						.isNetworkAvailable(NewSchoolNewsDetailsActivity.this)) {
					DataUtil.getToast(getResources().getString(
							R.string.no_network));
					return;
				}
				// 是否在上一个请求中
				if (!isLoading()) {
					try {
						JSONObject json = new JSONObject();
						if (isAt) {
							// 回复评论
							json.put("commentType", 1);
							json.put("pointCommentId",
									newSchoolNewsCommentBeanList.get(arg2)
											.getId());// 坑爹啊 这个字段是这条评论的id 也是醉了
						} else {
							// 评论
							json.put("commentType", 0);
						}
						json.put("campuNewsId", campuNewsId);
						json.put("userId", userid);//commentUserName
						json.put("commentUserName", commentUserName);
						json.put("userType", userType);
						json.put("commentContent", ed.getText().toString()
								.trim());
						System.out.println("评论huo回复评论入参=====" + json);
						String url = new StringBuilder(Constants.SERVER_URL)
								.append(Constants.SEND_SCHOOL_NEWS_COMMENT)
								.toString();
						String result = HttpHelper.httpPostJson(
								NewSchoolNewsDetailsActivity.this, url, json);
						System.out.println("评论huo回复评论出参=====" + result);
						NewBaseBean ret = JsonUtils.fromJson(result,
								NewBaseBean.class);
						if (ret.getServerResult().getResultCode() != 200) {// 请求失败
							DataUtil.getToast(ret.getServerResult()
									.getResultMessage());
						} else {
							JSONObject jsonB = new JSONObject(result);
							String newsCommentId= jsonB.getString("newsCommentId");
							NewSchoolNewsCommentBean newSchoolNewsCommentBean=new NewSchoolNewsCommentBean();
							newSchoolNewsCommentBean.setId(newsCommentId);
							newSchoolNewsCommentBean.setContent(ed.getText().toString().trim());
							newSchoolNewsCommentBean.setComment_user_id(userid);
							newSchoolNewsCommentBean.setComment_user_name(commentUserName);
							if (isAt) {
								newSchoolNewsCommentBean.setPoint_comment_id(newSchoolNewsCommentBeanList.get(arg2).getComment_user_id());
								newSchoolNewsCommentBean.setPoint_comment_user(newSchoolNewsCommentBeanList.get(arg2).getComment_user_name());
								newSchoolNewsCommentBean.setComment_type("1");
							}else{
								newSchoolNewsCommentBean.setComment_type("0");
							}
							if(newSchoolNewsCommentBeanList.size()<30){
								newSchoolNewsCommentBeanList.add(newSchoolNewsCommentBean);
								commentsNewsListViewAdapter.notifyDataSetChanged();
							}
							String commentNum=news_commentNum.getText().toString().trim();
							int commentNumInt=0;
							if(!DataUtil.isNullorEmpty(commentNum)){
								commentNumInt=Integer.parseInt(commentNum);
								commentNumInt+=1;
							}
							news_commentNum.setText(commentNumInt+"");
						}
					} catch (Exception e) {
					}
				}
				window.dismiss();
//				initDatas();
			}

		});

		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});

		first.setClickable(false);
		first.setBackground(getResources().getDrawable(R.drawable.only_login_selector_no));
		ed.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(s.length()>99){
					DataUtil.getToast("评论或回复内容不能超过100个字!");
				}
				
				if(s.length()>0){
					first.setClickable(true);//@drawable/only_login_selector
					first.setBackground(getResources().getDrawable(R.drawable.popwindow_selector));
				}else{
					first.setClickable(false);
					first.setBackground(getResources().getDrawable(R.drawable.only_login_selector_no));
				}
			}
		});
	}
	

	// 显示popupWindow 删除
	private void handleDelete(final int i, final int arg2, final int q) {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.delete_point, null);

		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

		final PopupWindow window = new PopupWindow(view,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 防止虚拟软键盘被弹出菜单遮住
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
		// 在底部显示
		window.showAtLocation(findViewById(R.id.news_comment_iv),
				Gravity.BOTTOM, 0, 0);

		// 这里检验popWindow里的button是否可以点击
		Button cancel_delete = (Button) view.findViewById(R.id.cancel_delete);
		Button determine_delete = (Button) view
				.findViewById(R.id.determine_delete);

		// 取消
		cancel_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		// 确定
		determine_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!DataUtil
						.isNetworkAvailable(NewSchoolNewsDetailsActivity.this)) {
					DataUtil.getToast(getResources().getString(
							R.string.no_network));
					return;
				}
				try {

					JSONObject json = new JSONObject();//
					json.put("commentId", newSchoolNewsCommentBeanList
							.get(arg2).getId());
					json.put("campuNewsId", campuNewsId);

					String url = new StringBuilder(Constants.SERVER_URL)
							.append(Constants.DELETE_SCHOOL_NEWS_COMMENT)
							.toString();
					String result = HttpHelper.httpPostJson(
							NewSchoolNewsDetailsActivity.this, url, json);
					NewBaseBean ret = JsonUtils.fromJson(result,
							NewBaseBean.class);
					if (ret.getServerResult().getResultCode() != 200) {// 请求失败
						DataUtil.getToast(ret.getServerResult()
								.getResultMessage());
					} else {

					}
				} catch (Exception e) {

				}
				window.dismiss();
				initDatas();
			}

		});
		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
			}
		});
	}

	// 注入js函数监听
	private void addImageClickListner() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
		webView.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\"); "
				+ "for(var i=0;i<objs.length;i++)  " + "{"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(this.src);  "
				+ "    }  " + "}" + "})()");
	}
	

	// js通信接口
	public class JavascriptInterface {
		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

//		@SuppressLint("JavascriptInterface")
		@android.webkit.JavascriptInterface
		public void openImage(String img) {
			System.out.println(img);
			// Intent intent = new Intent();
			// intent.putExtra("image", img);
			// intent.setClass(context, ShowWebImageActivity.class);
			// context.startActivity(intent);
			String url = img;
//			url=url.substring(0, url.lastIndexOf("&"));
//			url+="&authId=MB@864690025904296@19849";
//			img = "/downloadApi?fileId=4286";// 先用固定的图片地址
//			String url = DataUtil.getDownloadURL(context, img);
			System.out.println(url);
			List<PictureURL> datas = new ArrayList<PictureURL>();
			PictureURL pictureURL = new PictureURL();
			pictureURL.setPictureURL(url);
			datas.add(pictureURL);

			Intent intent = new Intent(context, ImagePagerActivity.class);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
					(Serializable) datas);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 1);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	// 监听
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			System.out.println("url:" + url);
			Uri uri = Uri.parse(url); // url为你要链接的地址
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			view.getSettings().setJavaScriptEnabled(true);
			view.removeJavascriptInterface("searchBoxJavaBridge_");
			view.removeJavascriptInterface("accessibility");
			view.removeJavascriptInterface("accessibilityTraversal");
			super.onPageFinished(view, url);
			// html加载完成之后，添加监听图片的点击js函数
			addImageClickListner();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);
			view.removeJavascriptInterface("searchBoxJavaBridge_");
			view.removeJavascriptInterface("accessibility");
			view.removeJavascriptInterface("accessibilityTraversal");
			super.onPageStarted(view, url, favicon);
//			aa();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			NewSchoolNewsDetailsActivity.this.finish();
			break;
		case R.id.news_good_iv:

			if (!DataUtil.isNetworkAvailable(getBaseContext())) {
				DataUtil.getToast("当前网络不可用，请检查您的网络设置");
				return;
			}
			if (!DataUtil.isFastDoubleClick()) {
				news_goodNumInt = Integer.parseInt(news_goodNum.getText()
						.toString().trim());
				if (!isLoading()) {
					if ("true".equals(hasGoodComment)) {
						news_good_iv.setImageResource(R.drawable.new_love_no);
						hasGoodComment = "false";
						news_goodNum.setText((news_goodNumInt - 1) + "");
						http_url_sendSchoolNewsGood("1", false);
					} else if ("false".equals(hasGoodComment)) {
						news_good_iv.setImageResource(R.drawable.new_love_ok);
						hasGoodComment = "true";
						news_goodNum.setText((news_goodNumInt + 1) + "");
						http_url_sendSchoolNewsGood("0", true);
					}
				}
			} else {
				DataUtil.getToast("请不要连续点击！");
			}
			// initDatas();
			break;
		case R.id.news_comment_iv:
			break;
		case R.id.load_more:
			if (!DataUtil.isNetworkAvailable(getBaseContext())) {
				DataUtil.getToast("当前网络不可用，请检查您的网络设置");
				return;
			}
			if (!isLoading()) {
				loadDataByIndex(k);
				k++;// 加载更多评论
			}
			break;
		default:
			break;
		}
	}

	public void http_url_sendSchoolNewsGood(String commentType,
			Boolean trueOrFalse) {
		try {
			JSONObject json = new JSONObject();
			json.put("campuNewsId", campuNewsId);
			json.put("userId", userid);
			json.put("commentType", commentType);// 0:点赞,1取消赞
			json.put("userType", userType);//commentUserName
			json.put("commentUserName", commentUserName);//commentUserName增加字段
			String url = new StringBuilder(Constants.SERVER_URL).append(
					Constants.SEND_SCHOOL_NEWS_LIKE).toString();//
			String result = HttpHelper.httpPostJson(this, url, json);
			System.out.println("=====result==="+result);
			NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
			if (ret.getServerResult().getResultCode() != 200) {// 请求失败
				DataUtil.getToast(ret.getServerResult().getResultMessage());
			} else {
			}
		} catch (Exception e) {

		}
	}

	// 加载更多评论
	public void loadDataByIndex(int k) {
		try {
			List<NewSchoolNewsCommentBean> schoolNewsCommentList_more = new ArrayList<NewSchoolNewsCommentBean>();
			JSONObject json = new JSONObject();
			json.put("campuNewsId", campuNewsId);
			json.put("userId", userid);
			json.put("imgAuthId", "MB@"+deviceId+"@"+accId);
			json.put("curPage", k);// curPage,pageSize
			json.put("pageSize", 30);
			System.out.println("第" + k + "页" + "校园新闻详情入参=====" + json);
			String url = new StringBuilder(Constants.SERVER_URL).append(
					Constants.GET_SCHOOL_NEWS_DETAILS).toString();
			String result = HttpHelper.httpPostJson(this, url, json);
			System.out.println("第" + k + "页" + "校园新闻详情出参=====" + json);
			NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
			if (ret.getServerResult().getResultCode() != 200) {// 请求失败
				DataUtil.getToast(ret.getServerResult().getResultMessage());
			} else {
				JSONObject json3 = new JSONObject(result);
				String commentListStr = json3.getString("commentList");
				JSONArray json2 = new JSONArray(commentListStr);
				for (int i = 0; i < json2.length(); i++) {
					NewSchoolNewsCommentBean newSchoolNewsCommentBean = JsonUtils
							.fromJson(json2.get(i).toString(),
									NewSchoolNewsCommentBean.class);
					schoolNewsCommentList_more
							.add(newSchoolNewsCommentBean);
				}
				commentsNewsListViewAdapter.addItem(schoolNewsCommentList_more);
				commentsNewsListViewAdapter.notifyDataSetChanged();//notifyDataSetChanged
			}
			if (totalPageNum > k) {
				load_more.setVisibility(View.VISIBLE);
			} else {
				load_more.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			
		}
	}

	@Override
	protected void getMessage(String data) {
	}

}
