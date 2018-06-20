package com.wcyc.zigui2.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.wcyc.zigui2.R;

public class BaloonView extends PopupWindow {
	private FrameLayout container;
	public BaloonView(Context context,View content) {
	    super(context);
	    container = new FrameLayout(context);
		container.addView(content);

		setContentView(container);
	    setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
	    setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
	    setTouchable(true);
	    setFocusable(true);
	    setOutsideTouchable(true);  
	    setTouchInterceptor(new View.OnTouchListener() {
	          @Override
	          public boolean onTouch(View v, MotionEvent event) {
	            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
	                BaloonView.this.dismiss();                
	              return true;
	            }               
	            return false;
	          }
	        });
	}
	
	public void showUnderView(View view ) {
	  //  setBackgroundDrawable(view.getContext().getResources().getDrawable(R.drawable.menu_bg));

	    int[] location = new int[2];
	    view.getLocationOnScreen(location);
	    container.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	    int xoffset = view.getWidth() / 2 - container.getMeasuredWidth() / 2;
	    showAsDropDown(view, xoffset, 0);
	}
}