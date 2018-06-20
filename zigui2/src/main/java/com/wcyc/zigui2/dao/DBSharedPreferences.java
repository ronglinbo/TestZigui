package com.wcyc.zigui2.dao;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences工具类
 * 
 * @author
 * 
 */
public class DBSharedPreferences {

	private Context context;
	public static final String _NAME = "DIARY_DB";
	public static final String _USERD = "_USERD";

	public DBSharedPreferences(Context context) {
		this.context = context;
	}
	/**
	 * 清除所有数据
	 */
	public void clear() {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}
	public boolean checkUser() {
		return getBoolean("login");
	}

	public void putString(String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key) {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}

	public void putInteger(String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public Integer getInteger(String key) {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		return sp.getInt(key, -1);
	}

	public void putBoolean(String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key) {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

	public void putLong(String key, long value) {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public long getLong(String key) {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		return sp.getLong(key, -1);
	}

	public void putFloat(String key, float value) {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public float getFloat(String key) {
		SharedPreferences sp = context.getSharedPreferences(_NAME, Context.MODE_PRIVATE);
		return sp.getFloat(key, 0.0F);
	}

}