package com.wcyc.zigui2.dao;

import java.util.ArrayList;
import java.util.List;

import com.wcyc.zigui2.bean.DynamicShare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 
 * 动态内容数据库基类
 * @author xiehua
 */
public class ShareDao{
	//public static String TABLE_NAME = "share";

	public static String COLUMN_NAME_ID = "id";
	public static String COLUMN_NAME_PATH = "picPath";
	public static String COLUMN_NAME_CONTENT = "content";
	public static String COLUMN_NAME_PRAISE = "praise";
	public static String COLUMN_NAME_COMMENTS = "comments";
	public static String COLUMN_NAME_TIME = "time";
	
	//protected DbOpenShareHelper dbHelper;
	
	public ShareDao(Context context){
		//dbHelper = DbOpenShareHelper.getInstance(context);
	}
	
	//保存一条动态信息
	public void SaveDyn(DynamicShare dyn){
		return;
	}
	
	
	//保存所有动态信息
	/*public void SaveAllDyn(List<DynamicShare> list){
		return;
	}
	
	//从数据库里获取所有的动态发布
	public List<DynamicShare> GetAllShare(){
		return null;
	}*/
}