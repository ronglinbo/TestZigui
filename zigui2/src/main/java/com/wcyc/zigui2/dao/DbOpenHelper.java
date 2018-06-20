
package com.wcyc.zigui2.dao;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbOpenHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 4;
	private static DbOpenHelper instance;

	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
			+ UserDao.TABLE_NAME + " ("
			+ UserDao.COLUMN_NAME_NICK +" TEXT, "
		       	+UserDao.COLUMN_NAME_AVATAR+" TEXT,"
		       	+UserDao.COLUMN_NAME_CLASSID+" TEXT,"
		       	+UserDao.COLUMN_NAME_CLASSNAME+" TEXT,"
		       	+UserDao.COLUMN_NAME_ISGROUP+" INTEGER,"
		       	+UserDao.COLUMN_NAME_CELLPHONE+" TEXT,"
			+ UserDao.COLUMN_NAME_ID + " TEXT);";
			
	private DbOpenHelper(Context context) {
		super(context, getUserDatabaseName(), null, DATABASE_VERSION);
	}
	public static DbOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DbOpenHelper(context.getApplicationContext());
		}
		return instance;
	}
	private static String getUserDatabaseName() {
        return  "hxcontacts.db";
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(USERNAME_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + UserDao.TABLE_NAME);
		onCreate(db);
	}
	
	public void closeDB() {
	    if (instance != null) {
	        try {
	            SQLiteDatabase db = instance.getWritableDatabase();
	            db.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        instance = null;
	    }
	}
	
	private static AtomicInteger num = new AtomicInteger();
	
	public SQLiteDatabase getMyWritableDatabase(){
		synchronized(num){
			num.incrementAndGet();
		}
		return getWritableDatabase();
	}
	
	public SQLiteDatabase getMyReadableDatabase(){
		synchronized(num){
			num.incrementAndGet();
		}
		return getReadableDatabase();
	}
	
	public void myclose(){
		synchronized (num) {
			if(num.decrementAndGet() == 0){
				close();
			}
		}
	}
}
