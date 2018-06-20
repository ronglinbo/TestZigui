/*
* 文 件 名:MyListView.java
* 创 建 人： 姜韵雯
* 日    期： 2014-12-08
* 版 本 号： 1.05
*/
package com.wcyc.zigui2.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 用来处理ListView和ScrollView冲突的自定义listview
 * 
 * @author 姜韵雯
 * @version 1.05
 * @since 1.05
 */
	public class MyListView extends ListView { 

    public MyListView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 

    public MyListView(Context context) { 
        super(context); 
    } 

    public MyListView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 

    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

        int expandSpec = MeasureSpec.makeMeasureSpec( 
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
} 