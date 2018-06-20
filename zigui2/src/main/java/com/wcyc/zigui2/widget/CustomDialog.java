package com.wcyc.zigui2.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wcyc.zigui2.R;


//2014年10月10日 下午12:04:18
/**
 * 退出程序自定义Dialog
 * @author  王登辉
 * @version 1.01 
 */
public class CustomDialog extends Dialog implements
		android.view.View.OnClickListener {

	/** 
	 * 布局文件 
	 */
	int layoutRes;
	/** 
	 * 上下文对象
	 **/
	Context context;
	/** 确定按钮 **/
	private Button confirmBtn;
	/** 取消按钮 **/
	private Button cancelBtn;
	/** 离线消息按钮 **/
	private RadioButton myRadioButton;
	private TextView contentTv,titleTv;
	/** 点击次数 **/
	private int check_count = 0;
	/** Toast时间 **/
	public static final int TOAST_TIME = 1000;
	private Handler handler;
	/** 确定按钮被点击 **/
	public static final int DIALOG_SURE = 1;
	/** 取消按钮被点击 **/
	public static final int DIALOG_CANCEL = 2;
	/** 单选按钮被选中 **/
	public static final int RADIOBUTTON_CHECKED = 3;
	/** 单选按钮未被选中 **/
	public static final int RADIOBUTTON_NO_CHECKED = 4;
	private Message msg;
	private String para = null;
	

	public CustomDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 自定义布局的构造方法
	 * 
	 * @param context
	 * @param resLayout
	 */
	public CustomDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	/**
	 * 自定义主题及布局的构造方法
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public CustomDialog(Context context, int theme, int resLayout,Handler handler) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		this.handler = handler;
	}

	public CustomDialog(Context context, int theme, int resLayout,Handler handler,String para) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		this.handler = handler;
		this.para = para;
	}

	public void setTitle(String title){
		if(titleTv != null){
			titleTv.setText(title);
		}
	}
	
	public String getTitle(){
		String titleStr=titleTv.getText().toString().trim();
		return titleStr;
	}
	
	public void setContent(String content){
		if(contentTv != null)
			contentTv.setText(content);
	}
	
	public String getContentStr(){
		String contentTvStr=contentTv.getText().toString().trim();
		return contentTvStr;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 指定布局
		this.setContentView(layoutRes);

		// 根据id在布局中找到控件对象etMeasuredDimension
		confirmBtn = (Button) findViewById(R.id.confirm_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		myRadioButton = (RadioButton) findViewById(R.id.my_rbtn);
		contentTv = (TextView) findViewById(R.id.content_tv);
		titleTv = (TextView) findViewById(R.id.title_tv);
		// 设置按钮的文本颜色
//		confirmBtn.setTextColor();
//		cancelBtn.setTextColor(0xff1E90FF);

		// 为按钮绑定点击效果监听器
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		myRadioButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		msg = Message.obtain();
		switch (v.getId()) {

		case R.id.confirm_btn:
			// 点击了确认按钮
			if(para != null){
				Bundle data = new Bundle();
				data.putString("para",para);
				msg.setData(data);
				msg.what = DIALOG_SURE;
				handler.sendMessage(msg);
			}else {
				handler.sendEmptyMessage(DIALOG_SURE);
			}
			break;

		case R.id.cancel_btn:
			// 点击了取消按钮
			handler.sendEmptyMessage(DIALOG_CANCEL);
			break;

		case R.id.my_rbtn:
			check_count = check_count + 1;
			if (check_count % 2 == 0) {
				// no checked
				myRadioButton.setButtonDrawable(context.getResources()
						.getDrawable(R.drawable.radio));
				msg.arg1 = RADIOBUTTON_NO_CHECKED;
				handler.sendMessage(msg);
			} else {
				// checked
				myRadioButton.setButtonDrawable(context.getResources()
						.getDrawable(R.drawable.radio_check));
				msg.arg1 = RADIOBUTTON_CHECKED;
				handler.sendMessage(msg);
			}
			break;

		default:
			break;
		}
	}
}