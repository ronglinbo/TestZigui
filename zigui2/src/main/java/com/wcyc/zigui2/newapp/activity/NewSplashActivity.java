package com.wcyc.zigui2.newapp.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.TaskBaseActivity;

public class NewSplashActivity extends TaskBaseActivity 
	implements OnPageChangeListener {
	
	private LinearLayout ll_banner_dot,ll_buttons;
	private ViewPager mScrollPager;
	private ScrollPagerAdapter mAdapter;
	private LayoutInflater inflater;
	private int page;
	
	/**
	 * 是否是第一次进入应用
	 * @return 如果是第一次进入应用，返回true，否则返回 false
	 */
	public boolean isFirst() {
		SharedPreferences sp = getSharedPreferences("little_data", 1);
		
		return sp.getBoolean("isFirstInApp", true);
	}
	
	/**
	 * 保存已经看过splash信息.
	 */
	public void saveFirst(){
		SharedPreferences sp = getSharedPreferences("little_data", 1);
		Editor editor = sp.edit();
		editor.putBoolean("isFirstInApp", false);
		editor.commit();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if(!isFirst()){
			goLoginActivity();
			return;
		}
		inflater = LayoutInflater.from(this);
		initView();
		initData();
	}
	
	/**
	 * Resume继承了父类的方法.
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	/**
	 * 初始化控件.
	 */
	private void initView() {
		// TODO Auto-generated method stub
		ll_banner_dot = (LinearLayout) findViewById(R.id.ll_banner_dot);
		ll_buttons = (LinearLayout) findViewById(R.id.ll_buttons);
		// ViewPager初始化
		mScrollPager = (ViewPager) findViewById(R.id.index_banner);
		mAdapter = new ScrollPagerAdapter();
		mScrollPager.setAdapter(mAdapter);
		mScrollPager.setCurrentItem(Integer.MAX_VALUE);
		mScrollPager.setOnPageChangeListener(this);
		initTips(tipsSize);
//		initButton();
	}
	
	/**
	 * 初始化数据.
	 */
	private void initData() {
		PageItem item1 = new PageItem(R.drawable.guide1, -1, -1, R.string.splash_next);
		PageItem item2 = new PageItem(R.drawable.guide2, -1, -1, -1);
		PageItem item3 = new PageItem(R.drawable.guide3, -1, -1, R.string.splash_finish);
		
		mDatas.add(item1);
		mDatas.add(item2);
		mDatas.add(item3);

		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 记录viewpage的数据.
	 */
	private class PageItem {
		public int imageId;
		public int titleId;
		public int contentId;
		public int buttonId;
		public PageItem(int imageId, int titleId, int contentId, int buttonId) {
			super();
			this.imageId = imageId;
			this.titleId = titleId;
			this.contentId = contentId;
			this.buttonId = buttonId;
		}
		
	}
	//viewpage的适配器
	protected List<PageItem> mDatas = new ArrayList<PageItem>();
	
	/**
	 * 滚动页适配器类.
	 */
	private class ScrollPagerAdapter extends PagerAdapter {
		/**
		 * 得到数目.
		 * @return 数目
		 */
		@Override
		public int getCount() {
			if (mDatas == null) {
				return 0;
			}
			return mDatas.size();
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View imageLayout = inflater.inflate(R.layout.new_activity_splash_pager_item, container, false);
			ImageView iv_splash = (ImageView) imageLayout.findViewById(R.id.iv_splash);

			Button bt_splash_next = (Button) imageLayout.findViewById(R.id.bt_splash_next);
			PageItem item = mDatas.get(position);
			
			if(item.imageId != -1){
				Bitmap bitmap=readBitMap(NewSplashActivity.this,item.imageId);
				iv_splash.setImageBitmap(bitmap);
			} else {
				iv_splash.setVisibility(View.GONE);
			}
				
			if(item.buttonId != -1){
				bt_splash_next.setText(item.buttonId);
				if(item.buttonId == R.string.splash_next){
					bt_splash_next.setBackgroundResource(R.drawable.shape_splash_next_btn);
				} else {
					bt_splash_next.setBackgroundResource(R.drawable.shape_splash_next_btn2);
				}
				bt_splash_next.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						saveFirst();
						goLoginActivity();
					}
				});
			} else {
				bt_splash_next.setVisibility(View.GONE);
			}
			
			container.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}
		
		/**
		* 以最省内存的方式读取本地资源的图片.
		* @param context 内容
		* @param resId 资源ID
		* @return 资源图片
		*/
		public Bitmap readBitMap(Context context, int resId) {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			// 获取资源图片
			InputStream is = context.getResources().openRawResource(resId);
			
			return BitmapFactory.decodeStream(is, null, opt);
		}
	}
	
	/**
	 * 装点点的ImageView数组.
	 */
	private ImageView[] tips;
	private ImageView button;
	private final int tipsSize = 3;
	
	/**
	 * 设置选中的tip的背景.
	 * @param selectItem 选中的项
	 */
	private void setTips(int selectItem){
		for(int i=0; i<tips.length; i++){
			tips[i].setBackgroundResource((i == selectItem) ? R.drawable.dot_selected : R.drawable.dot);
		}
	}
	
	private void setButton(int page){
		if(page == 0){
			button.setBackgroundResource(R.drawable.btn_tiaoguo);
		}else if(page == 2){
//			button.setBackgroundResource(R.drawable.btn_kaishitiyan);
		}else if(page == 1){
			button.setBackgroundResource(0);
		}
		
	}

	/**
	 * 初始化提示.
	 * @param size 数目
	 */
	private void initTips(int size){
		//将点点加入到ViewGroup中
		tips = new ImageView[size];
		ll_banner_dot.removeAllViews();
		for(int i=0; i<tips.length; i++){
			ImageView imageView = new ImageView(this);
	    	imageView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,  
                    LayoutParams.WRAP_CONTENT));
	    	tips[i] = imageView;
    		tips[i].setBackgroundResource((i == 0) ? R.drawable.dot_selected : R.drawable.dot);
	    	
	    	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,  
                    LayoutParams.WRAP_CONTENT));
	    	layoutParams.leftMargin = 25;
	    	layoutParams.rightMargin = 25;
	    	ll_banner_dot.addView(imageView, layoutParams);
		}
	}
	
	public boolean dispatchTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_UP){
			
			float x = event.getX();
			float y = event.getY();
			int xdp = px2dip(this,x);
			int ydp = px2dip(this,y);
			System.out.println("xdp:"+xdp+" ydp:"+ydp);
			if(page == 2 && xdp > 140 && xdp < 480 && ydp > 400 && ydp < 800){//开始体验
				saveFirst();
				goLoginActivity();
			}
			if(page == 0 && xdp > 280 && xdp < 480 && ydp > 260 && ydp < 600){
				saveFirst();
				goLoginActivity();
			}
		}
		return super.dispatchTouchEvent(event);
	}
	
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

/**
  * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
  */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	private void initButton(){
		button = new ImageView(this);
		ll_buttons.removeAllViews();
		LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(params);
		button = imageView;
		button.setBackgroundResource(R.drawable.btn_tiaoguo);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(params);
		layoutParams.rightMargin = 15;
		layoutParams.leftMargin = 425;

		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goLoginActivity();
			}
			
		});
		ll_buttons.addView(imageView,layoutParams);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	
	@Override
	public void onPageSelected(int arg0) {
		//设置当前是第几个
		setTips(arg0);
//		setButton(arg0);
		page = arg0;
	}
	
	/**
	 * 跳入登录界面.
	 */
	public void goLoginActivity() {
		Intent intent = new Intent(NewSplashActivity.this, LoginActivity.class);
		startActivity(intent);
		NewSplashActivity.this.finish();
	}
}
