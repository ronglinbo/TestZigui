package com.wcyc.zigui2.three;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.three.ScrollerNumberPicker.OnSelectListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 城市Picker
 * 
 * @author zd
 * 
 */
public class CityPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	private ScrollerNumberPicker counyPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = 0;
	private int temCityIndex = 0;
	private int tempCounyIndex = 0;
	private Context mContext;
	private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
	private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
	private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();

	private CitycodeUtil citycodeUtil;
	
	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}
	public CityPicker(Context context) {
		super(context);
		this.mContext = context;
	}
	
	public void setListData(List<Cityinfo> province_list,HashMap<String, List<Cityinfo>> city_map ,HashMap<String, List<Cityinfo>> couny_map ){
		this.province_list = province_list;
		this.city_map = city_map;
		this.couny_map = couny_map;
		setOnLayoutSelectListener();
	}

	private void setOnLayoutSelectListener(){
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
		
		findViewById(R.id.but).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onSelectingListener != null){
					//选择考勤三级联动信息
					String lv1 = citycodeUtil.getProvince_list().get(tempProvinceIndex);
					String lv1Id = citycodeUtil.getProvince_list_code().get(tempProvinceIndex);
					String lv2 = citycodeUtil.getCity_list().get(temCityIndex);
					String lv2Id = citycodeUtil.getCity_list_code().get(temCityIndex);
					String lv3 = citycodeUtil.getCouny_list().get(tempCounyIndex);
					String lv3Id = citycodeUtil.getCouny_list_code().get(tempCounyIndex);
					onSelectingListener.selectedData(lv1Id+"#"+lv2Id+"#"+lv3Id + "," + lv1+"#"+lv2+"#"+lv3);
				}
				
			}
		});
		
		citycodeUtil = CitycodeUtil.getSingleton();
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);

		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
		counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
		
		provincePicker.setData(citycodeUtil.getProvince(province_list));
		provincePicker.setDefault(0);
		
		cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil
				.getProvince_list_code().get(0)));
		cityPicker.setDefault(0);
		
		//counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil
		//		.getCity_list_code().get(1)));
		counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil
				.getProvince_list_code().get(0)));
		counyPicker.setDefault(0);
		
		provincePicker.setOnSelectListener(new OnSelectListener() {
			@Override
			public void endSelect(int id, String text) {
				//System.out.println("id-->" + id + "text----->" + text);
				if (text.equals("") || text == null)
					return;
				if (tempProvinceIndex != id) {
					String selectDay = cityPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					// 城市数组
					cityPicker.setData(citycodeUtil.getCity(city_map,
							citycodeUtil.getProvince_list_code().get(id)));
					cityPicker.setDefault(0);
					
					counyPicker.setData(citycodeUtil.getCouny(couny_map,
							citycodeUtil.getProvince_list_code().get(id)));
					counyPicker.setDefault(0);
					
					int lastDay = Integer.valueOf(provincePicker.getListSize());
					if (id > lastDay) {
						provincePicker.setDefault(lastDay - 1);
					}
					
					tempProvinceIndex = id;
					Message message = new Message();
					message.what = REFRESH_VIEW;
					handler.sendMessage(message);
				}
			}
			@Override
			public void selecting(int id, String text) {
			}
		});
		cityPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null)
					return;
				if (temCityIndex != id) {
					
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = counyPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					
					//counyPicker.setData(citycodeUtil.getCouny(couny_map,
				//			citycodeUtil.getCity_list_code().get(id)));
				//	counyPicker.setDefault(1);
					
					int lastDay = Integer.valueOf(cityPicker.getListSize());
					if (id > lastDay) {
						cityPicker.setDefault(lastDay - 1);
					}
					
					temCityIndex = id;
					Message message = new Message();
					message.what = REFRESH_VIEW;
					handler.sendMessage(message);
				}
			}

			@Override
			public void selecting(int id, String text) {
			}
		});
		counyPicker.setOnSelectListener(new OnSelectListener() {
			@Override
			public void endSelect(int id, String text) {
				System.out.println("id-->" + id + "text----->" + text);
				if (text.equals("") || text == null)
					return;
				if (tempCounyIndex != id) {
					String selectDay = provincePicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					String selectMonth = cityPicker.getSelectedText();
					if (selectMonth == null || selectMonth.equals(""))
						return;
					// 城市数组
					
					int lastDay = Integer.valueOf(counyPicker.getListSize());
					if (id > lastDay) {
						counyPicker.setDefault(lastDay - 1);
					}
					
					tempCounyIndex = id;
					Message message = new Message();
					message.what = REFRESH_VIEW;
					handler.sendMessage(message);
				}
			}
			@Override
			public void selecting(int id, String text) {

			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null){
					//onSelectingListener.selected(true);
					//String lv1 = citycodeUtil.getProvince_list_code().get(tempProvinceIndex);
					//String lv2 = citycodeUtil.getCity_list_code().get(temCityIndex);
					//String lv3 = citycodeUtil.getCouny_list_code().get(tempCounyIndex);
					
					//onSelectingListener.selectedData(lv1 +" -- "+ lv2 + " -- "+lv3);
				}
				break;
			default:
				break;
			}
		}
	};
	
	

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}
	

	public interface OnSelectingListener {
		//public void selected(boolean selected);
		void selectedData(String str);
	}
}
