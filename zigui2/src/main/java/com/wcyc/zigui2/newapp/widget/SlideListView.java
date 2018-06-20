/*
* 文 件 名:SlideListView.java
*
*/
package com.wcyc.zigui2.newapp.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.chat.SlideListView.DelButtonClickListener;
/**
 * 右滑弹出删除的listview类
 * 
 * 
 */
public class SlideListView extends ListView
	implements OnTouchListener,OnGestureListener{

	private static final String TAG = "ListView";

	/**
	 * 用户滑动的最小距离
	 */
	private int touchSlop;

	/**
	 * 是否响应滑动
	 */
	private boolean isSliding;

	/**
	 * 手指按下时的x坐标
	 */
	private int xDown;
	/**
	 * 手指按下时的y坐标
	 */
	private int yDown;
	/**
	 * 手指移动时的x坐标
	 */
	private int xMove;
	/**
	 * 手指移动时的y坐标
	 */
	private int yMove;

	private LayoutInflater mInflater;
	
	private Button mDelBtn;

	/** 
	* 滑动时出现的按钮 
	*/  
	private View btnDelete;  
	private View view;
	/** 
	* listview的每一个item的布局 
	*/  
	private ViewGroup viewGroup;  
	/** 
	 * 选中的项 
	*/  
	private int selectedItem;  
	
	/** 
	* 是否已经显示删除按钮 
	*/  
	private boolean isDeleteShow;  

	/**
	 * 为删除按钮提供一个回调接口
	 */
	private DelButtonClickListener mListener;

	/**
	 * 当前手指触摸的View
	 */
	private View mCurrentView;

	/**
	 * 当前手指触摸的位置
	 */
	private int mCurrentViewPos;
	
	private GestureDetector gesture;
	
	/** 
	* 点击删除按钮时删除每一行的事件监听器 
	*/  
	private OnItemDeleteListener onItemDeleteListener; 

	/**
	 * 必要的一些初始化
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlideListView(Context context, AttributeSet attrs){
		super(context, attrs);
		gesture = new GestureDetector(getContext(), this);
		mInflater = LayoutInflater.from(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		view = mInflater.inflate(R.layout.delete_btn, null);
		mDelBtn = (Button) view.findViewById(R.id.id_item_btn);
		setOnTouchListener(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch (action){

		case MotionEvent.ACTION_DOWN:
			xDown = x;
			yDown = y;
			
			// 获得当前手指按下时的item的位置
			mCurrentViewPos = pointToPosition(xDown, yDown);
			// 获得当前手指按下时的item
			View view = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
			mCurrentView = view;
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = x;
			yMove = y;
			int dx = xMove - xDown;
			int dy = yMove - yDown;
			/**
			 * 判断是否是从右到左的滑动
			 */
			if (xMove < xDown && Math.abs(dx) > touchSlop && Math.abs(dy) < touchSlop){
				isSliding = true;
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev){
		int action = ev.getAction();
		/**
		 * 如果是从右到左的滑动才相应
		 */
		if (isSliding){
			switch (action){
			case MotionEvent.ACTION_MOVE:

				if(mCurrentView != null){
					int[] location = new int[2];
					// 获得当前item的位置x与y
					mCurrentView.getLocationOnScreen(location);
					viewGroup = (ViewGroup)getChildAt(selectedItem - getFirstVisiblePosition());  
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);    
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);   
					btnDelete = LayoutInflater.from(getContext()).inflate(R.layout.btn_delete, null);  
					btnDelete.setLayoutParams(layoutParams);  
					viewGroup.addView(view);  
					btnShow(btnDelete);  
					isDeleteShow = true;  
					// 设置删除按钮的回调
					mDelBtn.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							if (mListener != null){
								mListener.clickHappend(mCurrentViewPos);
							}
						}
					});
				}
				break;
			case MotionEvent.ACTION_UP:
				isSliding = false;

			}
			// 相应滑动期间屏幕itemClick事件，避免发生冲突
			return true;
		}

		return super.onTouchEvent(ev);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {  
		//得到当前触摸的条目  
		selectedItem = pointToPosition((int)event.getX(), (int)event.getY());  
		//如果删除按钮已经显示，那么隐藏按钮，异常按钮在当前位置的绘制  
		if (isDeleteShow) {  
			btnHide(btnDelete);  
			viewGroup.removeView(btnDelete);  
			btnDelete = null;  
			isDeleteShow = false;  
			return false;  
		}else{  
		//如果按钮没显示，则触发手势事件  
		//由此去触发GestureDetector的事件，可以查看其源码得知，onTouchEvent中进行了手势判断，调用onFling  
			return gesture.onTouchEvent(event);  
		}  
	}  

	@Override
	public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY){
		//如果删除按钮没有显示，并且手势滑动符合我们的条件  
		//此处可以根据需要进行手势滑动的判断，如限制左滑还是右滑，我这里是左滑右滑都可以  
		if (!isDeleteShow && Math.abs(velocityX) > Math.abs(velocityY)) {  
			//在当前布局上，动态添加我们的删除按钮，设置按钮的各种参数、事件，按钮的点击事件响应我们的删除项监听器  
			btnDelete = LayoutInflater.from(getContext()).inflate(R.layout.btn_delete, null);  
			btnDelete.setOnClickListener(new OnClickListener() {  
			
				@Override  
				public void onClick(View v) {  
				//btnHide(btnDelete);  
					viewGroup.removeView(btnDelete);    
					btnDelete = null;    
					isDeleteShow = false;    
					onItemDeleteListener.onItemDelete(selectedItem);    
				}  
			});  
			viewGroup = (ViewGroup)getChildAt(selectedItem - getFirstVisiblePosition());  
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);    
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			
			btnDelete.setLayoutParams(layoutParams);  
			viewGroup.addView(btnDelete);  
			btnShow(btnDelete);  
			isDeleteShow = true;   
		}else{  
			setOnTouchListener(this);  
		}  

		return false;
	}
	
	public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {  
		this.onItemDeleteListener = onItemDeleteListener;  
	}  

	public interface OnItemDeleteListener{  
		void onItemDelete(int selectedItem);
	} 

//	public boolean onKeyDown(int keyCode,KeyEvent event){
//		if(keyCode == event.KEYCODE_BACK){
//			return false;
//		}else{
//			return super.onKeyDown(keyCode, event);
//		}
//	}
	
	public void setDelButtonClickListener(DelButtonClickListener listener){
		mListener = listener;
	}

	public interface DelButtonClickListener{
		void clickHappend(int position);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		//得到当前触摸的条目  
		if (!isDeleteShow) {  
			selectedItem = pointToPosition((int)e.getX(), (int)e.getY());  
		}  
		return true; 

	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void btnShow(View v){  
//		v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.btn_show));  
	} 

	private void btnHide(View v){  
//		v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.btn_hide));  
	}
	
}
