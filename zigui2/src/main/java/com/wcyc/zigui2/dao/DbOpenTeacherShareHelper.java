package com.wcyc.zigui2.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 
 * 老师侧动态内容数据库操作类
 * @author xiehua
 */
public class DbOpenTeacherShareHelper extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 1;
	private static final String DB_NAME = "share.db";
	private static DbOpenTeacherShareHelper instance;
	
	private static final String SHARE_TABLE_CREATE = "CREATE TABLE "
			+ TeacherShareDao.TABLE_NAME + " ("
			+ ShareDao.COLUMN_NAME_ID + " TEXT, "
			+ TeacherShareDao.COLUMN_NAME_CLASSID + " TEXT, "
			+ ShareDao.COLUMN_NAME_CONTENT + " TEXT, "
			+ ShareDao.COLUMN_NAME_PATH + " TEXT, "
			+ ShareDao.COLUMN_NAME_PRAISE + " TEXT, "
			+ ShareDao.COLUMN_NAME_COMMENTS + " TEXT); ";
	
	private DbOpenTeacherShareHelper(Context context){
		super(context,DB_NAME,null,DATABASE_VERSION);
	}
	
	public static DbOpenTeacherShareHelper getInstance(Context context){
		if(instance == null){
			instance = new DbOpenTeacherShareHelper(context.getApplicationContext());
		}
		return instance;
	}
	
	public void onCreate(SQLiteDatabase db){
		db.execSQL(SHARE_TABLE_CREATE);
	}
	
	public void onUpgrade(SQLiteDatabase db,int oldVer ,int newVer){
		db.execSQL("DROP TABLE IF EXISTS " + TeacherShareDao.TABLE_NAME);
		onCreate(db);
	}
}