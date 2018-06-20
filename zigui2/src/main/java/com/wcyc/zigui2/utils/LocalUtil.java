package com.wcyc.zigui2.utils;

import java.util.List;

import android.graphics.Bitmap;

import com.wcyc.zigui2.bean.Child;
import com.wcyc.zigui2.bean.MyInfoBean;
import com.wcyc.zigui2.newapp.bean.CommitOrderPayReq;
/**
 * 本地缓存
 * 
 * @author xfliu
 * @version 1.05
 * @since 1.05
 */
public class LocalUtil {
	public static Bitmap mBitMap;
	public static List<Child> childList;
	public static String userIdtoChildList;
	public static String userIdtoMyself;
	public static MyInfoBean myInfo;
	public static CommitOrderPayReq req;
	
	public static void clearData(){
		mBitMap = null;
		childList = null;
		userIdtoChildList = null;
		userIdtoMyself = null;
		myInfo = null;
		req = null;
	}
}
