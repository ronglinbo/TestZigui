/**   
 * 文件名：com.wcyc.zigui.utils.TextFilter.java   
 *   
 * 版本信息：   
 * 日期：2014年10月13日 下午3:23:26  
 * Copyright 惟楚有材 Corporation 2014    
 * 版权所有   
 *   
 */

package com.wcyc.zigui2.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

//2014年10月13日 下午3:23:26
/**
 * EditText输入的时候不让 限制输入空格符号.
 * @author 王登辉
 * @version 1.01
 */
public class TextFilter implements TextWatcher {

	private EditText editText;
	private ImageView deleteIv;
	String tmp = "";
	String digits = "abcdef";
	private int length;
	private String text;
	 
	 public void setEditeTextClearListener(final EditText editText ,ImageView deleView)
	 {
		 this.editText = editText;
		 this.deleteIv = deleView;
		 deleteIv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					editText.setText("");
				}
			});
	 }
	 
	/**
	 * 创建实例.
	 * @param editText EditText类型
	 */
	public void setEditTextListener(EditText editText) {
		this.editText = editText;
	}
	
	/**   
	 * 创建一个新的实例 TextFilter.   
	 */   
	public TextFilter() {
		super();
	}

	/**
	 * 创建一个新的实例 TextFilter.
	 * @param editText EditText类型
	 */
	public TextFilter(EditText editText) {
		this.editText = editText;
	}

	/**
	 * 文本改变前.
	 * @param s 字符序列
	 * @param start 开始点
	 * @param count 个数
	 * @param after 后面
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		 tmp = s.toString();
	}

	/**
	 * 文本改变时.
	 * @param s 字符序列
	 * @param start 开始点
	 * @param before 前面
	 * @param count 个数
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		text = s.toString();
		length = text.length();
		if(length>0) {
			deleteIv.setVisibility(View.VISIBLE);
		} else {
			deleteIv.setVisibility(View.INVISIBLE);
		}
		
		if (text.contains(" ")) {
			text = text.replaceAll(" ", "");
			editText.setText(text);
			int l = text.length();
			editText.setSelection(l);
		}
    }  

	/**
	 * 文本改变后.
	 * @param s 字符序列
	 */
	@Override
	public void afterTextChanged(Editable s) {

	}
}
