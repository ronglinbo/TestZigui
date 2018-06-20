/*
 * 文 件 名:NotifyDetailActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-08
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.module.notice;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.layout;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.ImagePagerActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AttachmentBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;
import com.wcyc.zigui2.newapp.widget.AttachmentActionOption;
import com.wcyc.zigui2.newapp.widget.CustomWebView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.HttpUtils;
import com.wcyc.zigui2.utils.JsonUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NotifyDetailActivity extends BaseActivity 
	implements HttpRequestAsyncTaskListener{
	private TextView tvTitle,tvDesc,tvContent,tvNotify,tvReadTimes;
	private WebView webview;
	private View vBrowseTimes;
	private String desc,title,content,readTimes;
	private LinearLayout view,attach;
	private ListView listview;
	private NoticeDetail detail;
	private ShowAttachListAdapter showAttach;
	private NoticeBrowserBean ret;
	private final int ACTION_GET_ATTACHMENT_LIST = 0;
	private final int ACTION_MARK_READ = 1;
	private final int GET_BROWSE_NUM = 0;
	private final int LOAD_ALL_TEACHER = 2;
	private final int LOAD_ALL_STUDENT = 3;
	private NoticeBrowseDetail detailPop;

	private class Pos{
		int start;
		int end;
	}
	private List<NoticeBrowserBean.ResultMap> teacherList,studentList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notify_detail);
		initData();
		initView();
		initEvent();
		markRead();
		getAttachement();
	}
	
	private void initView(){
		attach = (LinearLayout) findViewById(R.id.attach_ll);
		tvTitle = (TextView) findViewById(R.id.title);
		tvTitle.setText(title);
		tvNotify = (TextView) findViewById(R.id.new_content);
		tvNotify.setText("通知详情");
		tvDesc = (TextView) findViewById(R.id.desc);
		tvDesc.setText(desc);

		webview = (WebView) findViewById(R.id.html_content);
		System.out.println(content);
		if(content.length() > 0){
			new CustomWebView(this,webview,content,R.color.background_color);
//			DataUtil.showHtmlSetting(webview,content);
			// 添加js交互接口类，并起别名 imagelistner
		//		webview.addJavascriptInterface(new JavascriptInterface(this),
		//				"imagelistner");
		//		webview.setWebViewClient(new MyWebViewClient());
		}else{
			webview.setVisibility(View.GONE);
		}

		view = (LinearLayout) findViewById(R.id.title_back);
		view.setVisibility(View.VISIBLE);
		//家长端去掉查看人数功能
		if(!Constants.PARENT_STR_TYPE.equals(user.getUserType())) {
			tvReadTimes = (TextView) findViewById(R.id.read_times);
			tvReadTimes.setText("查看人数");
		}
		vBrowseTimes = findViewById(R.id.rl_browse);
		listview = (ListView) findViewById(R.id.attach_list);
	}
	
	private void initEvent(){
		view.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		vBrowseTimes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				System.out.println("detailPop:"+detailPop);
				if(ret != null) {
//					System.out.println("time pop :" + new Date());
					int size = 0;
					if(teacherList != null ){
						size += teacherList.size();
					}
					if(studentList != null){
						size += studentList.size();
					}
					if(size > 2000) {
						DataUtil.getToast("查看列表正在加载中");
					}
					detailPop = new NoticeBrowseDetail(NotifyDetailActivity.this, teacherList, studentList);
//						detailPop = new NoticeBrowseDetail(NotifyDetailActivity.this,ret);
					detailPop.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//					System.out.println("time finish pop:" + new Date());
				}
			}
		});
	}

	private void initData(){
		Intent intent = getIntent();
		int pos = intent.getIntExtra("pos", 0);
		detail = (NoticeDetail) intent.getExtras().getSerializable("detail");
		//detail = CCApplication.getInstance().getNotice().getNoticeList().get(pos);
		if(detail != null) {
			title = detail.getNoticeTitle();
			String time = DataUtil.getShowTime(detail.getNoticetime());
			desc = detail.getCreatorName() + "发布于" + time;
			content = detail.getNoticeContent();
			readTimes = String.valueOf(detail.getNoticeBrowseNum());
		}
	}
	
	private void getAttachement(){
		DataUtil.getAttachmentList(this,
				detail.getNoticeId()+"",Constants.NOTICE,ACTION_GET_ATTACHMENT_LIST);
	}
	
	private void markRead(){
		UserType user = CCApplication.getInstance().getPresentUser();
		JSONObject json = new JSONObject();
//		if(!detail.getCreatorId().equals(user.getUserId())){//当前用户不是发布者
		if(!"1".equals(detail.getIsMysend())){

			try {
				if(Constants.PARENT_STR_TYPE.equals(user.getUserType())){
					json.put("userId", user.getChildId());
				}else{
					json.put("userId", user.getUserId());
				}
				json.put("noticeId", detail.getNoticeId());
				json.put("schoolId", user.getSchoolId());

				System.out.println("markRead:"+json);
				String url = new StringBuilder(Constants.SERVER_URL)
						.append(Constants.BROWSE_NOTICE).toString();
				try {
					String result = HttpHelper.httpPostJson(this,url,json);
					if(result != null){
						NewBaseBean ret = JsonUtils.fromJson(result,NewBaseBean.class);
						if(ret != null
								&& ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
//				new HttpRequestAsyncTask(json,this,this).execute(Constants.BROWSE_NOTICE);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//家长端去掉查看人数功能
		if(!Constants.PARENT_STR_TYPE.equals(user.getUserType())) {
			getBrowserNum();
		}
		//删除模块已读记录
		json = new JSONObject();
		if(user != null){
			try {
				json.put("userId", user.getUserId());
				json.put("userType",user.getUserType());
				json.put("dataId",detail.getNoticeId());
				json.put("modelType", "02");
				if(CCApplication.getInstance().isCurUserParent()){
					json.put("studentId", user.getChildId());
				}
				System.out.println("clearRemind:"+json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new HttpRequestAsyncTask(json, this, this).execute(Constants.DEL_MODEL_REMIND);
		}
		//删除消息记录
		if(user != null){
			try {
				json = new JSONObject();
				json.put("userId", user.getUserId());
				json.put("userType",user.getUserType());
				json.put("msgId",detail.getNoticeId());
				json.put("messageType", "02");
				System.out.println("delReadMessage:"+json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new HttpRequestAsyncTask(json, this, this).execute(Constants.DEL_READ_MESSAGE);
		}
	}
	private void getBrowserNum(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				//获取浏览人数
				JSONObject json = null;
				try{
					json = new JSONObject();
					json.put("noticeId",detail.getNoticeId());
				}catch (JSONException e){
					e.printStackTrace();
				}
				DataUtil.getToastShort("查看人数正在加载中");
				String url = new StringBuilder(Constants.SERVER_URL).append(Constants.NOTICE_BROWSE_COUNT).toString();
//				new HttpRequestAsyncTask(json,NotifyDetailActivity.this,NotifyDetailActivity.this)
//						.execute(Constants.NOTICE_BROWSE_COUNT);
				System.out.println("获取浏览人数结果開始："+json);

				String result = HttpUtils.useHttpUrlConnectionPost(NotifyDetailActivity.this,url,json);
				System.out.println("获取浏览人数结果："+result);
//					String result = HttpHelper.httpPostJson(NotifyDetailActivity.this,url,json);
				Message msg = new Message();
				msg.what = GET_BROWSE_NUM;
				Bundle bundle = new Bundle();
				bundle.putString("result",result);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		switch(action){
		case ACTION_GET_ATTACHMENT_LIST:
			System.out.println("parseAttachment:"+data);
			AttachmentBean attach = JsonUtils.fromJson(data, AttachmentBean.class);
			List<Attachment> list = attach.getAttachmentList();
			detail.setAttachDetail(list);
			if(list != null && list.size() > 0){
				this.attach.setVisibility(View.VISIBLE);
				ShowAttachListAdapter listAdapter = new ShowAttachListAdapter(this,detail.getAttachDetail());
				listview.setAdapter(listAdapter);
				setListViewHeight(listAdapter);
			}
			break;
		}
	}
	private void setListViewHeight(ShowAttachListAdapter listAdapter){
		int totalHeight = 0;  
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目  
			View listItem = listAdapter.getView(i, null, listview);  
//			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度  
		}  
		
		ViewGroup.LayoutParams params = listview.getLayoutParams();  
		params.height = totalHeight  
		             + (listview.getDividerHeight() * (listAdapter.getCount() - 1));  
		// listView.getDividerHeight()获取子项间分隔符占用的高度  
		// params.height最后得到整个ListView完整显示需要的高度  
		listview.setLayoutParams(params);  
	}
	public void handleOnClick(Attachment attach){
		AttachmentActionOption option = new AttachmentActionOption(this,attach);
		View view = findViewById(R.id.email_body);
		option.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	@Override
	public void onRequstComplete(String result) {
		// TODO Auto-generated method stub
		parseBrowseNum(result);
	}

	@Override
	public void onRequstCancelled() {
		// TODO Auto-generated method stub
		
	}

	private void parseBrowseNum(String result){
		if(result != null && result.contains("browseNumber")) {
			ret = JsonUtils.fromJson(result, NoticeBrowserBean.class);
			if (ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
				if (tvReadTimes != null) {
					int times = ret.getBrowseNumber();
					tvReadTimes.setText("查看人数"+String.valueOf(times));
					loadAllReceipt(ret);
				}
			}
		}
	}
	private Handler mHandler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what){
				case GET_BROWSE_NUM:
					Bundle bundle = msg.getData();
					if(bundle != null){
						String result = bundle.getString("result");
						parseBrowseNum(result);
					}
				break;
				case LOAD_ALL_STUDENT:
					bundle = msg.getData();
					studentList = (List<NoticeBrowserBean.ResultMap>) bundle.getSerializable("student");
					DataUtil.clearDialog();
					break;
				case LOAD_ALL_TEACHER:
					bundle = msg.getData();
					teacherList = (List<NoticeBrowserBean.ResultMap>) bundle.getSerializable("teacher");
					if(detailPop != null){
						detailPop.setTeacher(teacherList);
					}
					break;
			}
		}
	};

	private void loadAllReceipt(final NoticeBrowserBean browserBean){
		if(browserBean != null){
			try {
				DataUtil.showDialog(this);
			}catch(Exception e){
				e.printStackTrace();
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<NoticeBrowserBean.ResultMap> list = browserBean.getResultMap();

					List<NoticeBrowserBean.ResultMap> studentList = getBrowserList(list,Constants.STUDENT_TYPE);
					Bundle bundle = new Bundle();
					if (studentList != null) {
						bundle.putSerializable("student", (Serializable) studentList);
					}
					Message msg = new Message();
					msg.what = LOAD_ALL_STUDENT;
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				}
			}).start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					List<NoticeBrowserBean.ResultMap> list = browserBean.getResultMap();

					List<NoticeBrowserBean.ResultMap> teacherList = getBrowserList(list,Constants.TEACHER_TYPE);
					Message msg = new Message();
					msg.what = LOAD_ALL_TEACHER;
					Bundle bundle = new Bundle();
					if (teacherList != null) {
						bundle.putSerializable("teacher", (Serializable) teacherList);
					}
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				}
			}).start();
		}
	}

	private List<NoticeBrowserBean.ResultMap>
	getBrowserList(List<NoticeBrowserBean.ResultMap> list,int type){
		if(list == null) return null;
		List<NoticeBrowserBean.ResultMap> result = new ArrayList<NoticeBrowserBean.ResultMap>();
		for(NoticeBrowserBean.ResultMap item:list){
			if(item.getUserType() == type) {
				result.add(item);
			}
		}
		return result;
	}

	private SpannableString getListString(List<NoticeBrowserBean.ResultMap> list,int type){
		String result = "";
		SpannableString sp = null;
		List<Pos> posList =  new ArrayList<Pos>();
		int pos = 0;
		if(list == null) return sp;
//		System.out.println("begin :"+new Date());
		for(NoticeBrowserBean.ResultMap item:list){
			if(item.getUserType() == type) {
				String name = item.getUserName();
				result += name;
				result += ",";
				if(item.getIsRead() == 0){
					Pos posItem = new Pos();
					posItem.start = pos;
					posItem.end = pos + name.length();
					posList.add(posItem);
				}
				pos += name.length() + 1;
			}
		}
//		System.out.println("finish :"+new Date());
		if(!DataUtil.isNullorEmpty(result)){
			sp = new SpannableString(result);

			for(Pos posItem:posList){
				ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
				sp.setSpan(span,posItem.start,posItem.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
//		System.out.println("finish all:"+new Date());
		return sp;
	}

	// 注入js函数监听
	private void addImageClickListner() {
		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
		webview.loadUrl("javascript:(function(){"
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
			super.onPageFinished(view, url);
			// html加载完成之后，添加监听图片的点击js函数
			addImageClickListner();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageStarted(view, url, favicon);
//			aa();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}
}
