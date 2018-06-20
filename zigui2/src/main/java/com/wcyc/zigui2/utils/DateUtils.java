package com.wcyc.zigui2.utils;

/**
 * Created by 章豪 on 2017/5/19 0019.
 */

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.wcyc.zigui2.core.CCApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    private static DateUtils util;

    public static DateUtils getInstance() {

        if (util == null) {
            util = new DateUtils();
        }
        return util;

    }

    private DateUtils() {
        super();
    }

    public SimpleDateFormat date_Formater_1 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public SimpleDateFormat date_Formater_3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public SimpleDateFormat date_Formater_2 = new SimpleDateFormat("yyyy-MM-dd");

    public Date getDate(String dateStr) {
        Date date = new Date();
        if (TextUtils.isEmpty(dateStr)) {
            return date;
        }
        try {
            date = date_Formater_1.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return date;

    }

    public String getDataString_1(Date date) {
        if (date == null) {
            date = new Date();
        }
        String str = date_Formater_1.format(date);
        return str;

    }

    public String getDataString_2(Date date) {
        if (date == null) {
            date = new Date();
        }
        String str = date_Formater_2.format(date);
        return str;

    }

    public String getDataString_3(Date date) {
        if (date == null) {
            date = new Date();
        }
        String str = date_Formater_3.format(date);
        return str;

    }

    /**
     * 将日期变成常见中文格式
     *
     * @param date
     * @return
     */
    public String getRencentTime(String date) {
        Date time = getDate(date);
        if (time == null) {
            return "一个月前";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        String curDate = date_Formater_2.format(cal.getTime());
        String paramDate = date_Formater_2.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = "一个月前";
        } else {
            ftime = date_Formater_2.format(time);
        }
        return ftime;
    }

    /**
     * 日期时间格式转换
     *
     * @param typeFrom 原格式
     * @param typeTo   转为格式
     * @param value    传入的要转换的参数
     * @return
     */
    public String stringDateToStringData(String typeFrom, String typeTo,
                                         String value) {
        String re = value;
        SimpleDateFormat sdfFrom = new SimpleDateFormat(typeFrom);
        SimpleDateFormat sdfTo = new SimpleDateFormat(typeTo);

        try {
            re = sdfTo.format(sdfFrom.parse(re));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re;
    }

    /**
     * 得到这个月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public int getMonthLastDay(int year, int month) {
        if (month == 0) {
            return 0;
        }
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到年份
     *
     * @return
     */
    public String getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "";
    }

    /**
     * 得到月份
     *
     * @return
     */
    public String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return (c.get(Calendar.MONTH) + 1) + "";
    }

    /**
     * 获得当天的日期
     *
     * @return
     */
    public String getCurrDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH) + "";
    }

    /**
     * 得到几天/周/月/年后的日期，整数往后推,负数往前移动
     *
     * @param calendar
     * @param calendarType Calendar.DATE,Calendar.WEEK_OF_YEAR,Calendar.MONTH,Calendar.
     *                     YEAR
     * @param next
     * @return
     */
    public String getDayByDate(Calendar calendar, int calendarType, int next) {

        calendar.add(calendarType, next);
        Date date = calendar.getTime();
        String dateString = date_Formater_1.format(date);
        return dateString;

    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间  //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

//  String pTime = "2012-03-12";
    private String getWeek(String pTime) {


        String Week = "";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }


        return Week;
    }

    public  String getWeek(Calendar c) {

        String Week = "";

        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }


        return Week;
    }


    private static final int FIRST_DAY = Calendar.MONDAY;

    public static List<Calendar> getWeekdays(Date date) {
        List<Calendar> calendars = new ArrayList<>();
        Calendar monday_calendar = Calendar.getInstance();
        monday_calendar.setTime(date);
        setToFirstDay(monday_calendar);
        for (int i = 0; i < 7; i++) {
            if(i==0){
                calendars.add(monday_calendar);
            }else{
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(calendars.get(i-1).getTime());
                calendar.add(Calendar.DATE, 1);
                calendars.add(calendar);
            }

        }

        return calendars;
    }

    private static void setToFirstDay(Calendar calendar) {
        while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
            calendar.add(Calendar.DATE, -1);
        }
    }

    private static void printDay(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd EE");
        System.out.println(dateFormat.format(calendar.getTime()));
    }

    public static boolean sameDate(Calendar d1, Calendar d2) {
        if (null == d1 || null == d2)
            return false;

        d1.set(Calendar.HOUR_OF_DAY, 0);
        d1.set(Calendar.MINUTE, 0);
        d1.set(Calendar.SECOND, 0);
        d1.set(Calendar.MILLISECOND, 0);

        d2.set(Calendar.HOUR_OF_DAY, 0);
        d2.set(Calendar.MINUTE, 0);
        d2.set(Calendar.SECOND, 0);
        d2.set(Calendar.MILLISECOND, 0);
        return d1.getTime().equals(d2.getTime());
    }

    //判断是不是本周 返回今天的星期
    public String  getCurrentWeekOfDay(String create_time1){
        //时间
        Date create_time = DateUtils.getInstance().getDate(create_time1);
        Date server_date =null;
        //系统当前时间
        if(DataUtil.isNetworkAvailable(CCApplication.applicationContext)){
            //网络可用
            server_date = DateUtils.getInstance().getDate(CCApplication.getInstance().getServer_date());
        }else{
            //网络不可用 手机时间
            server_date=new Date();
        }

        Calendar create_time_calendar = Calendar.getInstance();
        create_time_calendar.setTime(create_time);
        List<Calendar> calendars = DateUtils.getInstance().getWeekdays(server_date);
        for (Calendar ca : calendars) {
            Date date=ca.getTime();
            System.out.println(DateUtils.getInstance().getDataString_1(date));
            boolean isSame = DateUtils.sameDate(ca, create_time_calendar);
            if (isSame) {
                return  "星期"+DateUtils.getInstance().getWeek(ca) ;
            }
        }
        return "";
    }



    /*使用方法
    String dataStr = DateHelper.getInstance().getDataString_1(null);

    String toStringData = DateUtils.getInstance().stringDateToStringData("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", dataStr);
    String date = DateHelper.getInstance().getDayByDate(
            Calendar.getInstance(), Calendar.DATE, 1);
    String week = DateHelper.getInstance().getDayByDate(
            Calendar.getInstance(), Calendar.WEEK_OF_YEAR, 1);
    String month = DateHelper.getInstance().getDayByDate(
            Calendar.getInstance(), Calendar.MONTH, 1);
    String year = DateHelper.getInstance().getDayByDate(
            Calendar.getInstance(), Calendar.YEAR, 1);
    int lastDay = DateHelper.getInstance().getMonthLastDay(2015, 2);
    System.out.println(dataStr);
    System.out.println(toStringData);
    System.out.println(date);
    System.out.println(week);
    System.out.println(month);
    System.out.println(year);
    System.out.println("2月有"+lastDay+"天");*/


}


