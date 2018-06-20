package com.wcyc.zigui2.widget;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wcyc.zigui2.R;
//import com.wcyc.zigui2.adapter.InviteAttentionListAdapter;
import com.wcyc.zigui2.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.bean.FamilyResult;
import com.wcyc.zigui2.listener.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.utils.DataUtil;


//2014年12月15日 
/**
 * @author  xfliu
 * @version 1.1
 */
public class ForwardDialog extends Dialog implements android.view.View.OnClickListener{
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
	/** Toast时间 **/
	public static final int TOAST_TIME = 1000;
	/** 确定按钮被点击 **/
	public static final int DIALOG_SURE = 1;
	/** 取消按钮被点击 **/
	public static final int DIALOG_CANCEL = 2;
	/** 单选按钮被选中 **/
	public static final int RADIOBUTTON_CHECKED = 3;
	/** 单选按钮未被选中 **/
	public static final int RADIOBUTTON_NO_CHECKED = 4;
	private TextView title_tv,content_tv;
	private String name = "";
	private android.view.View.OnClickListener confirmListener;

	public ForwardDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 自定义布局的构造方法
	 * 
	 * @param context
	 * @param resLayout
	 */
	public ForwardDialog(Context context, int resLayout) {
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
	public ForwardDialog(Context context, int theme, int resLayout
			,String name,android.view.View.OnClickListener confirmListener) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		this.setCanceledOnTouchOutside(true);
		this.name = name;
		this.confirmListener = confirmListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 指定布局
		this.setContentView(layoutRes);
		// 根据id在布局中找到控件对象etMeasuredDimension
		confirmBtn = (Button) findViewById(R.id.confirm_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		title_tv = (TextView) findViewById(R.id.title_tv);
		content_tv = (TextView) findViewById(R.id.content_tv);
		//您确定要删除手机号135****1231的亲友吗？删除后该亲友将不能再获取小孩的成长信息。
		// 设置按钮的文本颜色
//		confirmBtn.setTextColor();
//		cancelBtn.setTextColor(0xff1E90FF);
		// 为按钮绑定点击效果监听器
		confirmBtn.setOnClickListener(confirmListener);
		cancelBtn.setOnClickListener(this);
		title_tv.setText(R.string.forward_title_text);
		content_tv.setText(name);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.confirm_btn:
//			// 点击了确认按钮
//			break;
		case R.id.cancel_btn:
			// 点击了取消按钮
			dismiss();
			break;
		}
	}
}