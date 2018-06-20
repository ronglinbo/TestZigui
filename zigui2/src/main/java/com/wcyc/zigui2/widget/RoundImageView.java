/**   
 * 文件名：com.wcyc.zigui.widget.RoundImageView.java   
 *   
 * 版本信息：   
 * 日期：2014年8月14日 上午9:52:32  
 * Copyright 惟楚有材 Corporation 2014    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.wcyc.zigui2.R;

//2014年8月14日 上午9:52:32
/**
 * 自定义圆形图片 控件.<P>
 * 创建一个新的实例 RoundImageView.
 * 
 * @author 王登辉
 * @version 1.01
 */
public class RoundImageView extends ImageView {

	private int type;
	private static final int TYPE_CIRCLE = 0;// 圆形图片
	private static final int TYPE_ROUND = 1;// 圆角图片
	private Bitmap mSrc;
	
	/**
	 * 圆角的大小
	 */
	private int mRadius;

	/**
	 * 控件的宽度
	 */
	private int mWidth;
	/**
	 * 控件的高度
	 */
	private int mHeight;

	public RoundImageView(Context context) {
		this(context, null);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 
	     * 创建一个新的实例 RoundImageView.   
	     *在xml文件里面定义好了自定义控件的属性后，会在该方法里面通过   AttributeSet参数返回自定义的属性 ，要获取属性的话，
	     *直接调用obtainStyledAttributes方法将AttributeSet的值作为一个数组返回
	     * @param context
	     * @param attrs
	     * @param defStyle
	 */
	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundImageView,defStyle , 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.RoundImageView_src://图片路径
				mSrc = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;
			case R.styleable.RoundImageView_type://图片类型 默认为circle
				type = a.getInt(attr, 0);
				break;
			case R.styleable.RoundImageView_borderRadius://图片圆角  角度
				a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, getResources().getDisplayMetrics()));
				break;
			default:
				break;
			}
		}
		a.recycle();//共用资源 ，每次用完后必须清除
	}
	
	    
	/**
	 * 计算控件的高度和宽度
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		/**
		 * 设置宽度
		 */
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mWidth = specSize;
		} else
		{
			// 由图片决定的宽
			int desireByImg = getPaddingLeft() + getPaddingRight() + mSrc.getWidth();
			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				mWidth = Math.min(desireByImg, specSize);
			}
		}

		/***
		 * 设置高度
		 */

		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY){// match_parent , accurate
			mHeight = specSize;
		} else{
			int desire = getPaddingTop() + getPaddingBottom();
			if(mSrc != null)
				desire += mSrc.getHeight();
			if (specMode == MeasureSpec.AT_MOST){// wrap_content
				mHeight = Math.min(desire, specSize);
			}
		}
		setMeasuredDimension(mWidth, mHeight);
	
	}
	
	    
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		

		switch (type)
		{
		// 如果是TYPE_CIRCLE绘制圆形
		case TYPE_CIRCLE:

			int min = Math.min(mWidth, mHeight);
			/**
			 * 长度如果不一致，按小的值进行压缩
			 */
			if(mSrc != null){
				mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
				canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
			}
			break;
		case TYPE_ROUND:
			canvas.drawBitmap(createRoundConerImage(mSrc), 0, 0, null);
			break;

		}
	}
	
	//jiang覆盖此方法，修改第三方的imageloader不能正确显示此控件的bug
	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		super.setImageBitmap(bm);
		mSrc = bm;
	}
	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		mSrc = BitmapFactory.decodeResource(getResources(), resId);
	}
	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if(drawable !=null)
		mSrc = ((BitmapDrawable) drawable).getBitmap();
	}
//end jiang
	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @param min
	 * @return
	 */
	private Bitmap createCircleImage(Bitmap source, int min)
	{
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN，参考上面的说明
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	/**
	 * 根据原图添加圆角
	 * 
	 * @param source
	 * @return
	 */
	private Bitmap createRoundConerImage(Bitmap source)
	{
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
		canvas.drawRoundRect(rect, 50f, 50f, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}
	

}
