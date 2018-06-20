package com.wcyc.zigui2.newapp.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.adapter.TConfAddImageGvAdapter;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.ImagePagerActivity;
import com.wcyc.zigui2.newapp.home.TConfigrChoiceClassActivity;
import com.wcyc.zigui2.imageselect.SelectImageActivity;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.ImageUploadAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.NewOpinionBean;
import com.wcyc.zigui2.newapp.bean.UploadFileResult;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.TextFilter;
import com.wcyc.zigui2.widget.ExpandGridView;

/**
 * 意见反馈
 * 
 * @author 郑国栋 2016-4-14
 * @version 2.0
 */

public class NewOpinionActivity extends BaseActivity implements
		OnClickListener, ImageUploadAsyncTaskListener {

	private NewBaseBean ret;
	// private Button titleBack;
	private Button sendButton;
	private EditText contentFace, contactWay;
	private boolean flag = false;

	private LinearLayout title_back;
	private TextView new_content;
//	private String classId;
	private ImageView advicefeedback_advice_content_delete_icon;
	private ImageView advicefeedback_advice_contact_way_delete_icon;

	// 图片选择
	private ExpandGridView publish_dynamic_addiv_gv;
	private ArrayList<String> imagePaths = new ArrayList<String>();// 图片选择集合
	private TConfAddImageGvAdapter gvAdapter;
	private static final int MAX_IMAGE = 6;
	private static final int REQUEST_CODE = 100;
	List<PictureURL> datas = new ArrayList<PictureURL>();// 发布消息时候，去相册选取图片后
															// 返回的路径集合，封装为List&lt;PictureURL&gt;集合
	private boolean is_compress;
	private ArrayList<String> transmitImagePaths = new ArrayList<String>();// 传递给服务器的集合
	private int nowImageNumber = 0;
	public static final int REASON_CANCEL = 2;
	public static final String INTENT_CANCEL_UPLOAD_PICTURE = "com.wcyc.zigui2.action.UPLOAD_PICTURE_CANCEL";
	private static String mMsgID;
	private List<String> attachementList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_opinion);
		initView();
		initDatas();
		initEvents();

		inputState();
	}

	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);
			if (TConfAddImageGvAdapter.DELETE_IMAGES == msg.what) {
				int p = (Integer) msg.obj;
				imagePaths.remove(p);
				transmitImagePaths.remove(p);
				nowImageNumber = imagePaths.size();
				gvAdapter.notifyDataSetChanged();
			}
		}
	};

	/**
	 * 初始化控件
	 */

	private void initView() {
		// titleBack = (Button) findViewById(R.id.title_btn);
		title_back = (LinearLayout) findViewById(R.id.title_back);
		findViewById(R.id.title_imgbtn_add)
				.setVisibility(View.GONE);

		sendButton = (Button) findViewById(R.id.advicefeedback_commit_btn);

		contentFace = (EditText) findViewById(R.id.advicefeedback_advice_content);
		advicefeedback_advice_content_delete_icon = (ImageView) findViewById(R.id.advicefeedback_advice_content_delete_icon);
		TextFilter textFilter1 = new TextFilter(contentFace);
//		contentFace.addTextChangedListener(textFilter1);// 设置不能输入空格
		textFilter1.setEditeTextClearListener(contentFace,
				advicefeedback_advice_content_delete_icon);

		contactWay = (EditText) findViewById(R.id.advicefeedback_advice_contact_way);
		advicefeedback_advice_contact_way_delete_icon = (ImageView) findViewById(R.id.advicefeedback_advice_contact_way_delete_icon);
		TextFilter textFilter2 = new TextFilter(contactWay);
		contactWay.addTextChangedListener(textFilter2);// 设置不能输入空格
		textFilter2.setEditeTextClearListener(contactWay,
				advicefeedback_advice_contact_way_delete_icon);

		new_content = (TextView) findViewById(R.id.new_content);

		publish_dynamic_addiv_gv = (ExpandGridView) findViewById(R.id.publish_dynamic_addiv_gv);
		gvAdapter = new TConfAddImageGvAdapter(this, imagePaths,
				getImageLoader(), handler);
		publish_dynamic_addiv_gv.setAdapter(gvAdapter);
	}

	/**
	 * 初始化数据
	 */

	private void initDatas() {
		new_content.setText("意见反馈");
		title_back.setVisibility(View.VISIBLE);

	}

	/**
	 * 效果控制
	 */

	private void initEvents() {
		title_back.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		sendButton.setClickable(false);
		sendButton
				.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);

		publish_dynamic_addiv_gv
				.setOnItemClickListener(new OnItemClickListener() {
					private PictureURL pictureURL;

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {
						if (0 == imagePaths.size()) {// 第一次过去选择相片
							Intent intentAdd = new Intent(
									NewOpinionActivity.this,
									SelectImageActivity.class);
							intentAdd.putExtra("limit", MAX_IMAGE);
							intentAdd.putStringArrayListExtra("addPic",
									imagePaths);
							startActivityForResult(intentAdd, REQUEST_CODE);
						} else {
							datas.clear();
							for (int i = 0; i < imagePaths.size(); i++) {
								pictureURL = new PictureURL();
								pictureURL.setPictureURL("file://"
										+ imagePaths.get(i));
								datas.add(pictureURL);
							}
						}
						if (0 != imagePaths.size()
								&& position == imagePaths.size()) {// 非第一次选取相片
							int limitImage = MAX_IMAGE;
							if (limitImage <= MAX_IMAGE) {
								Intent intentAdd = new Intent(
										NewOpinionActivity.this,
										SelectImageActivity.class);
								intentAdd.putExtra("limit", limitImage);
								intentAdd.putStringArrayListExtra("addPic",
										imagePaths);
								intentAdd.putExtra("is_compress", is_compress);
								startActivityForResult(intentAdd, REQUEST_CODE);
							} else {
								DataUtil.getToast("最多不能超过" + MAX_IMAGE + "张图片");
								publish_dynamic_addiv_gv.setVisibility(View.INVISIBLE);
							}
						}
						if (position != imagePaths.size()) {
							// 查看照片
							Intent intent = new Intent(NewOpinionActivity.this,
									ImagePagerActivity.class);
							intent.putExtra(
									ImagePagerActivity.EXTRA_IMAGE_URLS,
									(Serializable) datas);
							intent.putExtra(
									ImagePagerActivity.EXTRA_IMAGE_INDEX,
									position);
							startActivity(intent);
						}
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (TConfigrChoiceClassActivity.BACK_RESULT == resultCode) {
		}
		if (RESULT_OK == resultCode) {
			ArrayList<String> images = data.getExtras().getStringArrayList(
					"pic_paths");// 每次选择相片返回的数据
			if (null != images) {
				String title = "file://";
				transmitImagePaths.clear();
				imagePaths.clear();
				for (String str : images) {
					transmitImagePaths.add(title + str);
				}
				imagePaths.addAll(images);
			}
			if (null != imagePaths) {
				nowImageNumber = imagePaths.size();
				
				
			}
			gvAdapter.setData(transmitImagePaths);
			gvAdapter.notifyDataSetChanged();
			is_compress = data.getBooleanExtra("is_compress", true);
		}
	}

	private void inputState() {
		contentFace.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					sendButton.setClickable(true);
					sendButton
							.setBackgroundResource(R.drawable.btn_blue_selector);
				} else {
					sendButton.setClickable(false);
					sendButton
							.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
				}
				if (s.length() > 149) {
					DataUtil.getToast("反馈内容不能超过150个字");
					String temp=contentFace.getText().toString();
					temp=temp.substring(0,149);
					contentFace.setText(temp);
//					contentFace.requestFocus();
				}

			}

		});

		contactWay.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 29) {
					DataUtil.getToast("联系方式不能超过30个字");
					contactWay.requestFocus();
				}
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			NewOpinionActivity.this.finish();
			break;
		case R.id.advicefeedback_commit_btn:// 发布按钮点击
			// 获取反馈内容和联系方式text
			String content = contentFace.getText().toString();
			if (content.length() < 1) {
				contentFace.requestFocus();
				DataUtil.getToast("请输入反馈内容");
				break;
			}

			if (!DataUtil.isNetworkAvailable(getBaseContext())) {
				DataUtil.getToast(getResources().getString(R.string.no_network));// 当前网络不可用，请检查您的网络设置
				break;
			}

			if (DataUtil.isFastDoubleClick()) {// 多次点击判断 时间间隔两秒
				DataUtil.getToast("正在上传，请不要多次重复提交...");
				break;
			}
			// 上传图片 如果成功执行... 如果失败执行....
			ImageUploadAsyncTask upload = new ImageUploadAsyncTask(this, "1",
					imagePaths, Constants.UPLOAD_URL, this);
			upload.execute();

			break;
		default:
			break;
		}

	}

	/*
	 * 输入参数校验
	 */

	private boolean Validate() {

		return true;
	}

	/*
	 * 业务入口
	 * 
	 * 意见反馈
	 * 
	 * 参数名 参数类型（长度） 描述 userID String 用户ID content String 反馈内容 contacts String
	 * 联系方式
	 * 
	 *  出参 参数名 参数类型 描述 code Integer 返回代码 （200 成功 201 失败）
	 */

	private void httpBusiInerface() {
		String userid = CCApplication.app.getPresentUser().getUserId();

		// 获取反馈内容和联系方式text
		String questionDesc = contentFace.getText().toString();
		String feedbackContact = contactWay.getText().toString();

		String userType = CCApplication.getInstance().getPresentUser()
				.getUserType();		
		
			if (!Validate())
				return;
			try {
				NewOpinionBean newopinion= new NewOpinionBean();
				newopinion.setUserId(userid);
				if("3".equals(userType)){
					String childName=CCApplication.getInstance().getPresentUser().getChildName();
					String relation=CCApplication.getInstance().getPresentUser().getRelationTypeName();
					String classId=CCApplication.getInstance().getPresentUser().getClassId();
					newopinion.setUserName(childName+relation);
					newopinion.setClassId(classId);
				}else if("2".equals(userType)){
					String userName=CCApplication.getInstance().getMemberInfo().getUserName();
					newopinion.setUserName(userName);
				}
				if (feedbackContact.length() > 0) {
					newopinion.setFeedbackContact(feedbackContact);
					System.out.println("=====填了联系方式=====" + feedbackContact);
				}

				newopinion.setUserType(userType);
				newopinion.setQuestionDesc(questionDesc);
				if (attachementList.size() > 0) {
					newopinion.setAttachementList(attachementList);
				} else {
				}
				// json有引用类型时， 引用类型转Gson--在Gson转字符串--在字符串转json
				// 才能得到json格式的字符串
				Gson gson = new Gson();
				String string = gson.toJson(newopinion);				
				JSONObject json = new JSONObject(string);
				System.out.println("====意见反馈入参json======"+json);
				if (!isLoading()) {
					queryPost(Constants.FEE_BACK, json);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	protected void getMessage(String data) {
		ret = JsonUtils.fromJson(data, NewBaseBean.class);
		if (ret.getServerResult().getResultCode() != 200) {
			 DataUtil.getToast(ret.getServerResult().getResultMessage());
		} else {
			DataUtil.getToast("反馈成功，谢谢您的意见！");
			NewOpinionActivity.this.finish();

		}
	}

	@Override
	public void onImageUploadCancelled() {
		DataUtil.getToast("发送失败，请稍后再试");

	}

	@Override
	public void onImageUploadComplete(String result) {
		UploadFileResult ret = JsonUtils.fromJson(result,
				UploadFileResult.class);
		try {

			Set<String> set = ret.getSuccFiles().keySet();
			for (String string : set) {
				attachementList.add(string);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// 获取反馈内容和联系方式text
		String contactWayText = contactWay.getText().toString();

		if (contactWayText.length() > 0) {// 如果有联系方式 则检查联系方式是否正确
			if ((DataUtil.checkEmail(contactWayText)
					|| DataUtil.checkPhone(contactWayText) || DataUtil
						.checkQQ(contactWayText))) {
				httpBusiInerface();
			} else {
				contactWay.requestFocus();
				DataUtil.getToast("联系方式格式不正确");
			}
		} else {
			httpBusiInerface();
		}
	}

}
