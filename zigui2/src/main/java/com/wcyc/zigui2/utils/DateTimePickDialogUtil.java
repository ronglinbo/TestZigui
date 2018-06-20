package com.wcyc.zigui2.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.wcyc.zigui2.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

/**
 *  * 日期时间选择控件 
 *  
 *  使用方法： private TextView inputDate;//需要设置的日期时间文本编辑框 private String
 * initDateTime="2016-7-18 14:44",//初始日期时间值 在点击效果中使用：
 * inputDate.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View v) { DateTimePickDialogUtil
 *           dateTimePicKDialog=new
 *           DateTimePickDialogUtil(SinvestigateActivity.this,initDateTime);
 *           dateTimePicKDialog.dateTimePicKDialog(inputDate);
 * 
 *           } });
 *           
 * @author 郑国栋
 * 2016-7-18
 * @version 2.0
 */
public class DateTimePickDialogUtil implements OnDateChangedListener,
		OnTimeChangedListener {
	private DatePicker datePicker;
	private TimePicker timePicker;
	private AlertDialog ad;
	private String dateTime;
	private String initDateTime;
	private Activity activity;
	private int selectYear;
	private int selectMonth;
	private int selectDay;
	private int selectHour;
	private int selectMinute;

	/**
	 * 日期时间弹出选择框构造函数
	 * 
	 * @param activity
	 *            ：调用的父activity
	 * @param initDateTime
	 *            初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */
	public DateTimePickDialogUtil(Activity activity, String initDateTime) {
		
		this.activity = activity;
		this.initDateTime = initDateTime;
		
		if(!DataUtil.isNullorEmpty(initDateTime)){
			selectYear = Integer.parseInt(initDateTime.substring(0, 4));
			selectMonth = Integer.parseInt(initDateTime.substring(5, 7));
			selectDay = Integer.parseInt(initDateTime.substring(8, 10));
			selectHour = Integer.parseInt(initDateTime.substring(11, 13));
			selectMinute = Integer.parseInt(initDateTime.substring(14));
			
		}

	}


	/**
	 * 弹出日期时间选择框方法
	 * 
	 * @param inputDate
	 *            :为需要设置的日期时间文本编辑框
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public AlertDialog dateTimePicKDialog(final TextView inputDate) {
		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater().inflate(R.layout.common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		init(datePicker, timePicker);
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(this);

		ad = new AlertDialog.Builder(activity)
				.setTitle(initDateTime)
				.setView(dateTimeLayout)
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputDate.setText(dateTime);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						inputDate.setText("");
					}
				}).show();

		if(selectYear==0){
			Calendar calendarB = Calendar.getInstance();
			calendarB.set(Calendar.MINUTE, calendarB.get(Calendar.MINUTE)-1);//不能和当前时间一样 不知道为什么
			datePicker.setMinDate(calendarB.getTimeInMillis());//最小日期
		}else{
			Calendar calendarB = Calendar.getInstance();
			calendarB.set(Calendar.YEAR, selectYear);//
			calendarB.set(Calendar.MONTH, selectMonth-1);//
			calendarB.set(Calendar.DAY_OF_MONTH, selectDay);//
			
			calendarB.set(Calendar.MINUTE, calendarB.get(Calendar.MINUTE)-1);//不能和当前时间一样 不知道为什么
			datePicker.setMinDate(calendarB.getTimeInMillis());//最小日期
		}
		
		onDateChanged(datePicker, 0, 0, 0);
		return ad;
	}

	

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void init(DatePicker datePicker, TimePicker timePicker) {
		Calendar calendar = Calendar.getInstance();	
		if (!(null == initDateTime || "".equals(initDateTime))) {
			calendar = this.getCalendarByInintData(initDateTime);
		} else {
			initDateTime = calendar.get(Calendar.YEAR) + "-"
					+ calendar.get(Calendar.MONTH) + "-"
					+ calendar.get(Calendar.DAY_OF_MONTH) + " "
					+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
					+ calendar.get(Calendar.MINUTE);
		}
		
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}
	
	
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(datePicker, 0, 0, 0);
	}

	@SuppressLint("SimpleDateFormat")
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
  
        Calendar calendar = Calendar.getInstance(); 
        
        if(selectYear==0){
        	if (datePicker.getYear()==calendar.get(Calendar.YEAR)
        			&&datePicker.getMonth()==calendar.get(Calendar.MONTH)
        			&&datePicker.getDayOfMonth() == calendar.get(Calendar.DAY_OF_MONTH)) {//设置小时范围 
        		setMinAndMaxHour(calendar.get(Calendar.HOUR_OF_DAY), timePicker);
        	}else {
        		setMinAndMaxHour(0, timePicker);
        	}
        	
        	if (datePicker.getYear()==calendar.get(Calendar.YEAR)
        			&&datePicker.getMonth()==calendar.get(Calendar.MONTH)
        			&&datePicker.getDayOfMonth() == calendar.get(Calendar.DAY_OF_MONTH)
        			&&timePicker.getCurrentHour() == calendar.get(Calendar.HOUR_OF_DAY)) {//设置分钟范围 
        		setMinAndMaxMinute(calendar.get(Calendar.MINUTE), timePicker);
        	}else {
        		setMinAndMaxMinute(0, timePicker);
        	}
        }else{
        	if (datePicker.getYear()==selectYear
        			&&datePicker.getMonth()==(selectMonth-1)
        			&&datePicker.getDayOfMonth() == selectDay) {//设置小时范围 
        		setMinAndMaxHour(selectHour, timePicker);
        	}else {
        		setMinAndMaxHour(0, timePicker);
        	}
        	
        	if (datePicker.getYear()==selectYear
        			&&datePicker.getMonth()==(selectMonth-1)
        			&&datePicker.getDayOfMonth() == selectDay
        			&&timePicker.getCurrentHour() == selectHour) {//设置分钟范围 
        		setMinAndMaxMinute(selectMinute, timePicker);
        	}else {
        		setMinAndMaxMinute(0, timePicker);
        	}
        }
        
        calendar.set(datePicker.getYear(), datePicker.getMonth(),
        		datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
        		timePicker.getCurrentMinute());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTime = sdf.format(calendar.getTime());
        ad.setTitle(dateTime);
        
	}
	
	
	
	/**
	 * 设置分钟范围
	 * @param minMin
	 * @param maxMin
	 * @param timePicker
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	private void setMinAndMaxMinute(int minMin, int maxMin,
	private void setMinAndMaxMinute(int minMin, 
			final TimePicker timePicker) {
		try {//反射
			Field minuteSpinnerField = timePicker.getClass().getDeclaredField("mMinuteSpinner");
			minuteSpinnerField.setAccessible(true);
			NumberPicker hourSpinner = (NumberPicker) minuteSpinnerField.get(timePicker);
			hourSpinner.setMinValue(minMin);
//			hourSpinner.setMaxValue(maxMin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 设置小时范围
	 * @param minHour
	 * @param maxHour
	 * @param timePicker
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	private void setMinAndMaxHour(int minHour, int maxHour,
	private void setMinAndMaxHour(int minHour,
			final TimePicker timePicker) {
		try {//反射
			Field hourSpinnerField = timePicker.getClass().getDeclaredField("mHourSpinner");
			hourSpinnerField.setAccessible(true);
			NumberPicker hourSpinner = (NumberPicker) hourSpinnerField.get(timePicker);
			hourSpinner.setMinValue(minHour);
//			hourSpinner.setMaxValue(maxHour);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 实现将初始日期时间2016年07月18日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
	 * 
	 * @param initDateTime
	 *            初始日期时间值 字符串型
	 * @return Calendar
	 */
	private Calendar getCalendarByInintData(String initDateTime) {
		Calendar calendar = Calendar.getInstance();

		// 将初始日期时间2016年07月22日 16:45 拆分成年 月 日 时 分 秒
		String date = spliteString(initDateTime, " ", "index", "front"); // 日期
		String time = spliteString(initDateTime, " ", "index", "back"); //时间
		

		String yearStr = spliteString(date, "-", "index", "front"); //年份
		String monthAndDay = spliteString(date, "-", "index", "back"); // 月日
		

		String monthStr = spliteString(monthAndDay, "-", "index", "front"); // 月
		String dayStr = spliteString(monthAndDay, "-", "last", "back"); // 日
		

		String hourStr = spliteString(time, ":", "index", "front"); // 时
		String minuteStr = spliteString(time, ":", "index", "back"); // 分
		

		
		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();
		int currentHour = Integer.valueOf(hourStr.trim()).intValue();
		int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay, currentHour,
				currentMinute);
		return calendar;
	}

	/**
	 * 截取子串
	 * 
	 * @param srcStr
	 *            源串
	 * @param pattern
	 *            匹配模式
	 * @param indexOrLast
	 * @param frontOrBack
	 * @return
	 */
	public static String spliteString(String srcStr, String pattern,
			String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
		} else {
			loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // 截取子串
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
		}
		return result;
	}

}
