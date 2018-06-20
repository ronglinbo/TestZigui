package com.wcyc.zigui2.utils;
/**
 * 
 * @author yytan
 *   1.20
 *   防止向雅芳一直点击一个btm
 */

public class RepeatOnClick {

	 private static long lastClickTime;
	    public synchronized static boolean isFastClick() {
	        long time = System.currentTimeMillis();   
	        if ( time - lastClickTime < 500) {   
	            return true;   
	        }   
	        lastClickTime = time;   
	        return false;   
	    }
}
