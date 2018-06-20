/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wcyc.zigui2.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.easemob.util.HanziToPinyin;
import com.wcyc.zigui2.bean.User;
import com.wcyc.zigui2.newapp.service.ChatLoginService;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

public class UserDao {
	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	public static final String COLUMN_NAME_CLASSID = "classid";
	public static final String COLUMN_NAME_CLASSNAME = "classname";
	public static final String COLUMN_NAME_ISGROUP = "isgroup";
	public static final String COLUMN_NAME_CELLPHONE = "cellphone";
	
	protected DbOpenHelper dbHelper;
	
	public UserDao(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	/**
	 * 保存好友list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<User> contactList) {
		SQLiteDatabase db = dbHelper.getMyWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, null, null);
			for (User user : contactList) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_NAME_ID, user.getUsername());
				if (user.getNick() != null)
					values.put(COLUMN_NAME_NICK, user.getNick());
				if (user.getAvatar() != null) {
					values.put(COLUMN_NAME_AVATAR, user.getAvatar());
				}
				if (user.getClassID() != null) {
					values.put(COLUMN_NAME_CLASSID, user.getClassID());
				}
				if (user.getClassName() != null) {
					values.put(COLUMN_NAME_CLASSNAME, user.getClassName());
				}
				if (user.getCellphone() != null) {
					values.put(COLUMN_NAME_CELLPHONE, user.getCellphone());
				}
				values.put(COLUMN_NAME_ISGROUP, user.getGroup());
				//只有是服务可以运行的时候才能操作数据库，防止数据库重复操作
				if(ChatLoginService.canRun){
					db.insert(TABLE_NAME, null, values);
				}else{
					return;
				}
			}
			dbHelper.myclose();
		}
	}

	/**
	 * 获取好友list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		SQLiteDatabase db = dbHelper.getMyReadableDatabase();
		Map<String, User> users = new HashMap<String, User>();
		System.out.println("----getContactList()");
		if (db.isOpen()) {
//			Cursor cursor = db.rawQuery(
//					"select * from " + TABLE_NAME /* + " desc" */, null);
			Cursor cursor = db.query(TABLE_NAME, 
					null, 
					null,
					null, 
					null, null, null);
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_ID));
				String nick = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_NICK));
				String avatar = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_AVATAR));
				String classId = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_CLASSID));
				String className = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_CLASSNAME));
				int group = cursor.getInt(cursor
						.getColumnIndex(COLUMN_NAME_ISGROUP));
				User user = new User();
				user.setUsername(username);
				user.setNick(nick);
				user.setAvatar(avatar);
				user.setClassID(classId);
				user.setClassName(className);
				user.setGroup(group);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNick())) {
					headerName = user.getNick();
				} else {
					headerName = user.getUsername();
				}
				if (group == 1 || username.equals(Constants.NEW_FRIENDS_USERNAME)
						|| username.equals(Constants.GROUP_USERNAME)) {
					user.setHeader("");
				} else if (DataUtil.isNullorEmpty(headerName)||Character.isDigit(headerName.charAt(0))) {
					user.setHeader("#");
				} else {
					user.setHeader(HanziToPinyin.getInstance()
							.get(headerName.substring(0, 1)).get(0).target
							.substring(0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				users.put(username, user);
			}
			cursor.close();
			dbHelper.myclose();
		}
		return users;
	}

	/**
	 * 删除一个联系人
	 * 
	 * @param username
	 */
	public void deleteContact(String username) {
		SQLiteDatabase db = dbHelper.getMyWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?",
					new String[] { username });
			dbHelper.myclose();
		}
	}

	/**
	 * 删除所有联系人
	 */
	public void deleteAllContact() {
		SQLiteDatabase db = dbHelper.getMyWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, null, null);
			dbHelper.myclose();
		}
	}

	/**
	 * 保存一个联系人 ?
	 * 
	 * @param user
	 */
	public void saveContact(User user) {
		SQLiteDatabase db = dbHelper.getMyWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_ID, user.getUsername());
		if (user.getNick() != null)
			values.put(COLUMN_NAME_NICK, user.getNick());
		if (db.isOpen()) {
			db.insert(TABLE_NAME, null, values);
			dbHelper.myclose();
		}
	}
	
	/**
	 * 返回某个班的联系人列表
	 * @param classId 班级ID
	 * @return 联系人列表
	 */
	public List<User> getContactListbyClassId(String classId) {
		SQLiteDatabase db = dbHelper.getMyReadableDatabase();
		List<User> users = new ArrayList<User>();
		System.out.println("----getContactList()");
		if (db.isOpen()) {
//			String sql = "select * from " + TABLE_NAME /* + " desc" */;
			Cursor cursor = db.query(TABLE_NAME, 
					null, 
					"classid=?",
					new String[]{classId}, 
					null, null, null);
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_ID));
				String nick = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_NICK));
				String avatar = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_AVATAR));
//				String classId = cursor.getString(cursor
//						.getColumnIndex(COLUMN_NAME_CLASSID));
				String className = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_CLASSNAME));
				int group = cursor.getInt(cursor
						.getColumnIndex(COLUMN_NAME_ISGROUP));
				String cellPhone = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_CELLPHONE));
				
				User user = new User();
				user.setUsername(username);
				user.setNick(nick);
				user.setAvatar(avatar);
				user.setClassID(classId);
				user.setClassName(className);
				user.setGroup(group);
				user.setCellphone(cellPhone);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNick())) {
					headerName = user.getNick();
				} else {
					headerName = user.getUsername();
				}

				if (group == 1 || username.equals(Constants.NEW_FRIENDS_USERNAME)
						|| username.equals(Constants.GROUP_USERNAME)) {
					user.setHeader("");
				} else if (DataUtil.isNullorEmpty(headerName)||Character.isDigit(headerName.charAt(0))) {
					user.setHeader("#");
				} else {
					String Header = HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase();
					user.setHeader(Header);
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				users.add(user);
			}
			cursor.close();
			dbHelper.myclose();
		}
		return users;
	}
	/**
	 * 根据班级id获取好友map
	 * 
	 * @return
	 */
	public Map<String, User> getContactMapbyClassId(String classId) {
		SQLiteDatabase db = dbHelper.getMyReadableDatabase();
		Map<String, User> users = new HashMap<String, User>();
		if (db.isOpen()) {
//			Cursor cursor = db.rawQuery(
//					"select * from " + TABLE_NAME /* + " desc" */, null);
			Cursor cursor = db.query(TABLE_NAME, 
					null, 
					"classid=?",
					new String[]{classId}, 
					null, null, null);
			while (cursor.moveToNext()) {
				String username = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_ID));
				String nick = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_NICK));
				String avatar = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_AVATAR));
				String className = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_CLASSNAME));
				int group = cursor.getInt(cursor
						.getColumnIndex(COLUMN_NAME_ISGROUP));
				User user = new User();
				user.setUsername(username);
				user.setNick(nick);
				user.setAvatar(avatar);
				user.setClassID(classId);
				user.setClassName(className);
				user.setGroup(group);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNick())) {
					headerName = user.getNick();
				} else {
					headerName = user.getUsername();
				}
				if (group == 1 || username.equals(Constants.NEW_FRIENDS_USERNAME)
						|| username.equals(Constants.GROUP_USERNAME)) {
					user.setHeader("");
				} else if (DataUtil.isNullorEmpty(headerName)||Character.isDigit(headerName.charAt(0))) {
					user.setHeader("#");
				} else {
					user.setHeader(HanziToPinyin.getInstance()
							.get(headerName.substring(0, 1)).get(0).target
							.substring(0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				users.put(username, user);
			}
			cursor.close();
			dbHelper.myclose();
		}
		return users;
	}
}
