/*
 * 文 件 名:MasterMailDetailActivity.java
 * 创 建 人： xiehua
 * 日    期： 2016-03-18
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.module.mailbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.R.color;
import com.wcyc.zigui2.R.layout;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.adapter.AttachmentListAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.AttachmentBean;
import com.wcyc.zigui2.newapp.bean.EmailBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.AttachmentBean.Attachment;
import com.wcyc.zigui2.newapp.module.mailbox.MasterReplyMail.ReplyMailBody;
import com.wcyc.zigui2.newapp.module.notice.ShowAttachListAdapter;
import com.wcyc.zigui2.newapp.widget.CustomWebView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
//校长信箱管理员详情界面
public class SchoolMasterMailDetailActivity extends BaseActivity 
	implements ImageUploadAsyncTaskListener,HttpRequestAsyncTaskListener{
	
	private TextView tvTitle,tvDate,tvSender,tvContent,tvNotify,cancel,enter,tvType;
	private TextView tvReplied_Title,tvReplied_Sender,tvReplied_Date,
						tvReplied_Notify;
	private String date,attach,sender,emailTitle,content,title;
	private EditText replyContent,replyTitle;
	private LinearLayout back,llAttach;
	private ListView listView,listViewShow,replied_list;
	private Button addAttach,bType;
	private WebView webview,RepliedContent;
	private int type,pos;
	private MailInfo data;

	private AttachmentBean attachment;
	private List<String> uploadList = new ArrayList<String>();
	private int i = 0;
	private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
	private AttachmentListAdapter adapter;
	private UploadFileResult ret;
	private static final int PICK_PICTURE = 100;
	
	private static final int GET_REPLY_MAIL = 0;
	private static final int REPLY_MAIL = 1;
	private static final int QUERY_ATTACHMENT = 2;
	private static final int QUERY_REPLY_ATTACHMENT = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initAllKindView();
		initEvent();
	}
	
	protected void onResume(){
		super.onResume();
	}
	
	private void initAllKindView(){
		if("0".equals(data.getIsReply())){//管理员未回复邮件界面
			setContentView(R.layout.activity_master_mail_detail);
			initUnReplyView();
			if(attach != null && Integer.parseInt(attach) > 0){
				getAttachment(QUERY_ATTACHMENT,data.getMailId());
				if(llAttach != null) llAttach.setVisibility(View.VISIBLE);
			}
			
		}else{//已回复邮件界面
			setContentView(R.layout.activity_master_replied_mail_detail);
			if(attach != null && Integer.parseInt(attach) > 0){
				getOriginAttachment();
				if(llAttach != null) llAttach.setVisibility(View.VISIBLE);
			}
			initRepliedView();
			getReplyMail();
		}
		
	}
	
	private void initView(){
		TextView tv = (TextView) findViewById(R.id.new_content);
		tv.setText("邮件详情");
		back = (LinearLayout) findViewById(R.id.title_back);
		if(back != null) back.setVisibility(View.VISIBLE);
		
		View view = findViewById(R.id.title_right_tv);
		if(view != null) view.setVisibility(View.GONE);
		
		llAttach = (LinearLayout) findViewById(R.id.ll_attachment);
		
		tvTitle = (TextView) findViewById(R.id.title);
		tvTitle.setText(emailTitle);
		tvDate = (TextView) findViewById(R.id.date);
		tvDate.setText(date);
		tvSender = (TextView)findViewById(R.id.sender);
		tvSender.setText(sender);

		webview = (WebView)findViewById(R.id.html_content);
		if(content.length() > 0){
//			content = parseHtmlContent(content);
			if(webview != null){
//				DataUtil.showHtmlSetting(webview,content);
				new CustomWebView(this,webview,content, R.color.background_color);
			}
		}else{
			webview.setVisibility(View.GONE);
		}
//		Spanned parse = Html.fromHtml(content);
		replyContent = (EditText)findViewById(R.id.reply_content);
		addAttach = (Button) findViewById(R.id.add_attach);
		listView = (ListView) findViewById(R.id.attachList);//未上传的list
		listViewShow = (ListView) findViewById(R.id.attach_list);//服务器带过来的list
		bType = (Button) findViewById(R.id.type);
		String type = data.getTypeName();
		bType.setText(type);
//		tvType.setText(data.getTypeName());
		DataUtil.setTypeColor(bType, type);
	}

	private void initRepliedView(){
		tvReplied_Title = (TextView) findViewById(R.id.replied_title);
		tvReplied_Sender = (TextView) findViewById(R.id.replied_sender);
		tvReplied_Date = (TextView) findViewById(R.id.replied_date);
		replied_list = (ListView)findViewById(R.id.replied_attach_list);
		RepliedContent = (WebView) findViewById(R.id.replied_content);
		if(RepliedContent != null){
			RepliedContent.getSettings().setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
		}
		initView();
	}

	private void initUnReplyView(){
		cancel = (TextView) findViewById(R.id.title2_off);
		enter = (TextView) findViewById(R.id.title2_ok);
		enter.setTextColor(getResources().getColor(color.gray_normal));
		enter.setEnabled(false);
		initView();
		replyContent.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if(arg0.length() > 0){
					enter.setTextColor(getResources().getColor(color.blue));
					enter.setEnabled(true);
				}else{
					enter.setTextColor(getResources().getColor(color.gray_normal));
					enter.setEnabled(false);
				}
			}
			
		});
	}
	
	private void initEvent(){
		if(cancel != null){
			cancel.setOnClickListener(new OnClickListener(){
	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
				
			});
		}
		if(enter != null){
			enter.setOnClickListener(new OnClickListener(){
	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(type == SchoolMasterMailActivity.UNREPLY){
						if(imagePaths.size() == 0){
							replyMail();
						}else{//先传图片
							uploadFile();
						}
					}else{
						finish();
					}
				}
				
			});
		}
		if(addAttach != null){
			addAttach.setOnClickListener(new OnClickListener(){
	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(SchoolMasterMailDetailActivity.this,SelectImageActivity.class);
					intent.putExtra("limit", 8);
					intent.putStringArrayListExtra("addPic", imagePaths);
					startActivityForResult(intent,PICK_PICTURE);
				}
			});
		}
		if(back != null){
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
				
			});
		}
		if(listView != null){
			listView.setOnItemClickListener(new OnItemClickListener(){
	
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					ImageView del = (ImageView) arg0.findViewById(R.id.iv_del);
					del.setOnClickListener(new OnClickListener(){
	
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							imagePaths.remove(arg2);
							adapter.notifyDataSetChanged();
						}
						
					});
				}
				
			});
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("requestCode:"+requestCode+" resultCode:"+resultCode+" data:"+data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
			case PICK_PICTURE:
				Bundle bundle = data.getExtras();
				imagePaths = bundle.getStringArrayList("pic_paths");
				data.getBooleanExtra("is_compress", true);
				adapter = new AttachmentListAdapter(this,imagePaths);
				listView.setAdapter(adapter);
				break;
			}
		}
	}
	
	private void initData(){
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			type = bundle.getInt("type");
			pos = bundle.getInt("pos");
			data = (MailInfo) bundle.getSerializable("data");
			if(data != null) {
				emailTitle = data.getMailTile();
				sender = data.getMailSenderName();

				date = data.getPublishTime();
				attach = data.getAdditionNum();
				content = data.getMailContent();
			}
		}
	}
	
	protected void uploadFile(){
		for(String file:imagePaths) {
			ImageUploadAsyncTask upload = new ImageUploadAsyncTask(
					this, Constants.PIC_TYPE, file, Constants.UPLOAD_URL, this);
			upload.execute();
		}
	}
	
	protected void getOriginAttachment(){
		JSONObject json = new JSONObject();
		try {
			json.put("userId", user.getUserId());
			json.put("schoolId", user.getSchoolId());
			
			json.put("attachementType", "02");
			json.put("buzzId", data.getMailId());
			System.out.println("getOriginAttachment json:"+json);
			new HttpRequestAsyncTask(json, this, this).execute(Constants.QUREY_ATTACHMENT);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void replyMail(){
		
		try {
			MasterReplyMailReq req = new MasterReplyMailReq();
			req.setUserId(user.getUserId());
			req.setSchoolId(user.getSchoolId());
			req.setMailId(data.getMailId());
			req.setReplyContent(replyContent.getText().toString());
			req.setReplyTitle(emailTitle);
			if(ret != null){
//				List<String> attachment = new ArrayList<String>();
//				Set<String> set = ret.getSuccFiles().keySet();
//				for(String string:set){
//					attachment.add(string);
//				}
				req.setAttchementNum(uploadList.size()+"");
				req.setAttachmentList(uploadList);
			}else{
				req.setAttchementNum("0");
			}
			req.setIsSms("0");
			Gson gson = new Gson();
			String string = gson.toJson(req);
			JSONObject json = new JSONObject(string);
			System.out.println("replyMail:"+json);
			queryPost(Constants.REPLY_MAIL_URL,json);
			action = REPLY_MAIL;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void parseReplyMail(String data){
		System.out.println("parseReplyMail:"+data);
		NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
			DataUtil.getToast("发布成功");
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("type",type);
			bundle.putInt("pos",pos);
			bundle.putSerializable("data",this.data);
			intent.putExtras(bundle);

			setResult(RESULT_OK,intent);
			finish();
		}else{
			DataUtil.getToast("发布失败");
		}
	}
	
	protected void getReplyMail(){
		JSONObject json = new JSONObject();
		try {
			json.put("mailId", data.getMailId());
			queryPost(Constants.GET_ANSWER_MAIL,json);
			action = GET_REPLY_MAIL;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void parseGetReplyMailData(String data){
		MasterReplyMail reply = JsonUtils.fromJson(data, MasterReplyMail.class);
		if(reply.getAnwerList() != null){
			ReplyMailBody body = reply.getAnwerList().get(0);//默认只有一个回复需修改
			
			String attach = body.getAdditionNo();
			tvReplied_Title.setText(body.getTitle());
			tvReplied_Date.setText(body.getAnswerTime());
			tvReplied_Sender.setText(body.getAnswerName());
			//tvReplied_Attach.setText(attach);
//			RepliedContent.setText(body.getContent());
			String content = body.getContent();
//			content = parseHtmlContent(content);
			new CustomWebView(this,RepliedContent,content);
//			RepliedContent.loadData(body.getContent(), "text/html; charset=UTF-8", null);//这种写法可以正确解码
			if(attach != null && Integer.parseInt(attach) > 0){
				getAttachment(QUERY_REPLY_ATTACHMENT,body.getAnswerId());
			}
		}
	}
	
	protected void getAttachment(int action,String mailId){
		JSONObject json = new JSONObject();
		UserType user = CCApplication.getInstance().getPresentUser();
		try {
			json.put("userId", user.getUserId());
			json.put("schoolId", user.getSchoolId());
			if(type == SchoolMasterMailActivity.REPLYED){
				json.put("attachementType", "03");
			}else if(type == SchoolMasterMailActivity.UNREPLY){
				json.put("attachementType", "02");
			}else if(type == SchoolMasterMailActivity.ALL){
				json.put("attachementType", "02");
			}
			json.put("buzzId", mailId);
			System.out.println("QUERY_REPLY_ATTACHMENT json:"+json);
			queryPost(Constants.QUREY_ATTACHMENT,json);
			this.action = action;
			DataUtil.Sleep(100);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//解析回复邮件的附件
	protected void parseAttachment(int action,String data){
		System.out.println("action:" +action+" parseAttachment:"+data);
		attachment = JsonUtils.fromJson(data, AttachmentBean.class);
		if (llAttach != null) llAttach.setVisibility(View.VISIBLE);
		if(action == QUERY_ATTACHMENT) {
			if (listViewShow != null)
				listViewShow.setAdapter(new ShowAttachListAdapter(this, attachment.getAttachmentList()));

		}else if(action == QUERY_REPLY_ATTACHMENT){
			if (replied_list != null)
				replied_list.setAdapter(new ShowAttachListAdapter(this, attachment.getAttachmentList()));
		}
	}
	
	@Override
	protected void getMessage(String data) {
		// TODO Auto-generated method stub
		System.out.println("data:"+data);
		switch(action){
		case GET_REPLY_MAIL:
			parseGetReplyMailData(data);
//			if(attach != null && Integer.parseInt(attach) > 0){
//				getAttachment(QUERY_ATTACHMENT);
//			}
			break;
		case REPLY_MAIL:
			parseReplyMail(data);
			break;
		case QUERY_REPLY_ATTACHMENT:
		case QUERY_ATTACHMENT:
			parseAttachment(action,data);
			break;
		}
	}

	@Override
	public void onImageUploadCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onImageUploadComplete(String result) {
		// TODO Auto-generated method stub
		System.out.println("result:"+result);
		ret = JsonUtils.fromJson(result, UploadFileResult.class);
		i++;
		if(ret != null){
			if(ret.getSuccFiles() != null){
				Set<String> set = ret.getSuccFiles().keySet();
				for(String string:set){
					uploadList.add(string);
				}
			}
		}
		if(i == imagePaths.size()) {
			replyMail();
			i = 0;
		}
	}

	@Override
	public void onRequstComplete(String result) {
		// TODO Auto-generated method stub
		attachment = JsonUtils.fromJson(result, AttachmentBean.class);
		String text = "";
		if(attachment != null){
			List<Attachment> list = attachment.getAttachmentList();
			if(list != null){
				for(Attachment temp:list){
					text += temp.getAttachementName();
					text += " ";
				}
			}
		}

		if(llAttach != null) llAttach.setVisibility(View.VISIBLE);
		listViewShow.setAdapter(new ShowAttachListAdapter(this,attachment.getAttachmentList()));	
	}

	@Override
	public void onRequstCancelled() {
		// TODO Auto-generated method stub
		
	}
	//对html内容进行处理
	private String parseHtmlContent(String content){
		String prefix = "<table width=";
		int begin = content.indexOf(prefix);
		if(begin >= 0) {
			int end = content.indexOf(">", begin);
			String origin = content.substring(begin, end);
			content = content.replace(origin, origin + " border=1");
		}
		return content;
	}
}
