package com.wcyc.zigui2.dao;

import java.util.ArrayList;
import java.util.List;

//import com.wcyc.zigui2.bean.DynamicShare;
import com.wcyc.zigui2.bean.TeacherDynamicShare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 
 * 老师侧动态内容数据库操作接口类
 * @author xiehua
 */
public class TeacherShareDao extends ShareDao{

	public static String TABLE_NAME = "teacherShare";
	
	public static String COLUMN_NAME_CLASSID = "classId";
	
	protected DbOpenTeacherShareHelper dbHelper;
	public TeacherShareDao(Context context) {
		super(context);
	}
	
	//删除一条动态信息
	public void DeleteDyn(TeacherDynamicShare dync){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[] {dync.GetID()});
			dbHelper.close();
		}
	}
	
	//保存一条动态信息
	public void SaveDyn(TeacherDynamicShare dyn){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		String id = dyn.GetID();
		String path = dyn.GetURL();
		boolean praise = dyn.GetPraise();
		String content = dyn.GetContent();
		List<String> comments = dyn.GetComments();
		String classId = dyn.getClassId();
		String commentAll = null;
		String time = dyn.GetTime();
		
		if(id != null){
			values.put(COLUMN_NAME_ID, id);
		}
		
		if(path != null){
			values.put(COLUMN_NAME_PATH, path);
		}
		
		if(content != null){
			values.put(COLUMN_NAME_CONTENT, content);
		}
		
		values.put(COLUMN_NAME_PRAISE,praise);
		if(classId != null){
			values.put(COLUMN_NAME_CLASSID,classId);
		}
		
		if(comments != null){
			for(String s : comments){
				commentAll += s;
				commentAll += ";";
			}
			values.put(COLUMN_NAME_COMMENTS,commentAll);
		}
		if(time != null){
			values.put(COLUMN_NAME_TIME, time);
		}
		if(db.isOpen()){
			db.insert(TABLE_NAME, null, values);
			dbHelper.close();
		}
	}
	
	//保存所有动态信息
	public void SaveAllDyn(List<TeacherDynamicShare> list){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			for(TeacherDynamicShare dshare : list){
				ContentValues values = new ContentValues();
				String id = dshare.GetID();
				String path = dshare.GetURL();
				boolean praise = dshare.GetPraise();
				String content = dshare.GetContent();
				List<String> comments = dshare.GetComments();
				String commentAll = null;
				String classId = dshare.getClassId();
				String time = dshare.GetTime();
				
				if(id != null){
					values.put(COLUMN_NAME_ID, id);
				}
				
				if(path != null){
					values.put(COLUMN_NAME_PATH, path);
				}
				
				if(content != null){
					values.put(COLUMN_NAME_CONTENT, content);
				}
				
				values.put(COLUMN_NAME_PRAISE,praise);
				
				if(classId != null){
					values.put(COLUMN_NAME_CLASSID,classId);
				}
				if(comments != null){
					for(String s : comments){
						commentAll += s;
						commentAll += ";";
					}
					values.put(COLUMN_NAME_COMMENTS,commentAll);
				}
				if(time != null){
					values.put(COLUMN_NAME_TIME, time);
				}
				db.insert(TABLE_NAME, null, values);
			}
			dbHelper.close();
		}
	}
		
	//从数据库里获取所有的动态发布
	public List<TeacherDynamicShare> GetAllShare(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<TeacherDynamicShare> list = new ArrayList<TeacherDynamicShare>();
		if(db.isOpen()){
			Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
			while(cursor.moveToNext()){
				String id = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String content = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CONTENT));
				String path = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PATH));
				String praise = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRAISE));
				String comments = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENTS));
				String classId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CLASSID));
				boolean isPraise = "true".equals(praise);
				String time = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIME));
				TeacherDynamicShare share = new TeacherDynamicShare(id,path,content,isPraise,comments,classId,time);
				list.add(share);
			}
			cursor.close();
			dbHelper.close();
		}
		return list;
	}
}