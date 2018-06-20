package com.wcyc.zigui2.dao;

import java.util.ArrayList;
import java.util.List;

//import com.wcyc.zigui2.bean.DynamicShare;
import com.wcyc.zigui2.bean.ClassDynamicsBean;
import com.wcyc.zigui2.bean.ParentDynamicShare;
import com.wcyc.zigui2.bean.PointBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
/**
 * 
 * 家长侧动态内容数据库操作接口类
 * @author xiehua
 */
public class ParentShareDao extends ShareDao{

	public static String TABLE_NAME = "parentShare";
	
	public static String COLUMN_NAME_CHILDID = "childId";
	
	protected DbOpenParentShareHelper dbHelper;
	public ParentShareDao(Context context) {
		super(context);
	}
	
	//删除一条动态信息
	public void DeleteDyn(ClassDynamicsBean dync){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[] {dync.getClassesInteractionId()});
			dbHelper.close();
		}
	}
	
	//保存一条动态信息
	public void SaveDyn(ClassDynamicsBean dyn){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		String id = dyn.getClassesInteractionId();
		String path = dyn.getPic();
		String praise = dyn.getGoodFlag();
		String content = dyn.getContent();
		//List<String> comments = dyn.GetComments();
//		String childId = dyn.getChildId();
		String commentAll = null;
		String time = dyn.getTime();
		
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
		
//		if(childId != null){
//			values.put(COLUMN_NAME_CHILDID,childId);
//		}
		
//		if(comments != null){
//			for(String s : comments){
//				commentAll += s;
//				commentAll += ";";
//			}
//			values.put(COLUMN_NAME_COMMENTS,commentAll);
//		}
		
		if(time != null){
			values.put(COLUMN_NAME_TIME, time);
		}
		
		if(db.isOpen()){
			db.insert(TABLE_NAME, null, values);
			dbHelper.close();
		}
	}
	
	//保存所有动态信息
	public void SaveAllDyn(List<ClassDynamicsBean> list){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			for(ClassDynamicsBean dshare : list){
				ContentValues values = new ContentValues();
				String id = dshare.getClassesInteractionId();
				String path = dshare.getPic();
				String praise = dshare.getGoodFlag();
				String content = dshare.getContent();
//				List<String> comments = dshare.GetComments();
				String commentAll = null;
//				String childId = dshare.getChildId();
				String time = dshare.getTime();
				
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
				
//				if(childId != null){
//					values.put(COLUMN_NAME_CHILDID,childId);
//				}
//				if(comments != null){
//					for(String s : comments){
//						commentAll += s;
//						commentAll += ";";
//					}
//					values.put(COLUMN_NAME_COMMENTS,commentAll);
//				}
				if(time != null){
					values.put(COLUMN_NAME_TIME, time);
				}
				db.insert(TABLE_NAME, null, values);
			}
			dbHelper.close();
		}
	}
		
	//从数据库里获取所有的动态发布
	public List<ClassDynamicsBean> GetAllShare(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<ClassDynamicsBean> list = new ArrayList<ClassDynamicsBean>();
		if(db.isOpen()){
			Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
			while(cursor.moveToNext()){
				String id = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
				String content = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CONTENT));
				String path = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PATH));
				String praise = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PRAISE));
				String comments = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COMMENTS));
				String childId = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CHILDID));
				boolean isPraise = "true".equals(praise);
				String time = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TIME));
//				ClassDynamicsBean share = new ClassDynamicsBean(id,
//						List<PointBean> commentMapList, path, String pic,
//						content, time, String name, String goodNum,
//						String className, String goodFlag, String pointNum,
//						String headPortrait, String goodName, String delFlag,
//						String[] urls, ArrayList<Bitmap> sltList);
				ClassDynamicsBean share = new ClassDynamicsBean();
				list.add(share);
			}
			cursor.close();
			dbHelper.close();
		}
		return list;
	}
}