package com.wcyc.zigui2.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.wcyc.zigui2.R;

//2014年10月13日 下午9:08:52
/**
  * 自定义Spinner.
  * @author 王登辉
  * @version 1.01 
  */
public class SpinnerButton extends Button {

	private int width;
	private Context mContext;
	/** 下拉PopupWindow */
	private UMSpinnerDropDownItems mPopupWindow;
	/** 下拉布局文件ResourceId */
	private int mResId;
	/** 下拉布局文件创建监听器 */
	private ViewCreatedListener mViewCreatedListener;
	
	private boolean showSpinner = false;

	/**
	 * 此方法描述的是：   是否显示spinner
	 * @param showAble  void
	 */
	public void showAble(boolean showAble)
	{
		this.showSpinner = showAble;
	}
	public SpinnerButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initButton(context);
	}

	public SpinnerButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initButton(context);
	}

	public SpinnerButton(Context context, final int resourceId,
			ViewCreatedListener mViewCreatedListener) {
		super(context);
		setResIdAndViewCreatedListener(resourceId, mViewCreatedListener);
		initButton(context);
	}
	
	public void setWidth(int width)
	{
		this.width = width;
		 mPopupWindow = new UMSpinnerDropDownItems(mContext);
	}

	private void initButton(Context context) {
		this.mContext = context;
		// UMSpinnerButton监听事件
		setOnClickListener(new UMSpinnerButtonOnClickListener());
	}

	public PopupWindow getPopupWindow() {
		return mPopupWindow;
	}

	public void setPopupWindow(UMSpinnerDropDownItems mPopupWindow) {
		this.mPopupWindow = mPopupWindow;
	}

	public int getResId() {
		return mResId;
	}

	/**
	 * 隐藏下拉布局.
	 */
	public void dismiss() {
		mPopupWindow.dismiss();
	}

	/**
	 * 设置下拉布局文件,及布局文件创建监听器.
	 * @param mResId 下拉布局文件ID
	 * @param mViewCreatedListener 布局文件创建监听器
	 */
	public void setResIdAndViewCreatedListener(int mResId,
			ViewCreatedListener mViewCreatedListener) {
		this.mViewCreatedListener = mViewCreatedListener;
		// 下拉布局文件id
		this.mResId = mResId;
		// 初始化PopupWindow
		mPopupWindow = new UMSpinnerDropDownItems(mContext);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * UMSpinnerButton的点击事件
	 */
	class UMSpinnerButtonOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (mPopupWindow != null) {
				if(showSpinner)
				{
					if (!mPopupWindow.isShowing()) {
						// 设置PopupWindow弹出,退出样式
						mPopupWindow.setAnimationStyle(R.style.Animation_dropdown);
						// 计算popupWindow下拉x轴的位置
						int lx = (SpinnerButton.this.getWidth()
								- mPopupWindow.getmViewWidth() - 7) / 2;
						// showPopupWindow
						mPopupWindow.showAsDropDown(SpinnerButton.this, lx, -5);
					}
				}
				
			}
		}
	}

	/**
	 * 下拉界面.
	 */
	public class UMSpinnerDropDownItems extends PopupWindow {

		private Context mContext;
		/** 
		 * 下拉视图的宽度 
		 */
		private int mViewWidth;
		/**
		 * 下拉视图的高度
		 */
		private int mViewHeight;
		
		public UMSpinnerDropDownItems(Context context) {
			super(context);
			this.mContext = context;
			loadViews();
		}

		/**
		 * 加载布局文件.
		 */
		private void loadViews() {
			// 布局加载器加载布局文件
			LayoutInflater inflater = LayoutInflater.from(mContext);
			final View v = inflater.inflate(mResId, null, false);
			// 计算view宽高
			onMeasured(v);
			// 必须设置

//			setWidth(LayoutParams.WRAP_CONTENT);
//			setHeight(LayoutParams.WRAP_CONTENT);

			if(width == 0){
				setWidth(LayoutParams.WRAP_CONTENT);
			}else{
				setWidth(width);
			}
			
			setHeight(LayoutParams.WRAP_CONTENT);
			setContentView(v);
			setFocusable(true);

			// 设置布局创建监听器，以便在实例化布局控件对象
			if (mViewCreatedListener != null) {
				mViewCreatedListener.onViewCreated(v);
			}
		}

		/**
		 * 计算View长宽.
		 * @param v 视图
		 */
		private void onMeasured(View v) {
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			v.measure(w, h);

			mViewWidth = v.getMeasuredWidth();
			mViewHeight = v.getMeasuredHeight();
		}

		public int getmViewWidth() {
			return mViewWidth;
		}

		public void setmViewWidth(int mViewWidth) {
			this.mViewWidth = mViewWidth;
		}

		public int getmViewHeight() {
			return mViewHeight;
		}

		public void setmViewHeight(int mViewHeight) {
			this.mViewHeight = mViewHeight;
		}

	}

	/**
	 * 布局创建监听器，实例化布局控件对象.
	 */
	public interface ViewCreatedListener {
		void onViewCreated(View v);
	}
}
