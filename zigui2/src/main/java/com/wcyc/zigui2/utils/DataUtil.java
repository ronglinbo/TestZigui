/*
 * 文 件 名:DataUtil.java
 * 创 建 人： 姜韵雯
 * 日    期： 2014-09-23 
 * 版 本 号： 1.00
 */

package com.wcyc.zigui2.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.chat.EMGroup;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.ContactsList;
import com.wcyc.zigui2.newapp.bean.EasemobGroupInfo;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.ModelRemindList;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.NewClasses;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.bean.NewMessageListBean;
import com.wcyc.zigui2.newapp.bean.Role;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.ModelRemindList.ModelRemind;
import com.wcyc.zigui2.newapp.module.charge.ChargeProduct.Product;
import com.wcyc.zigui2.newapp.module.email.MenuConfigBean;

import static android.content.Context.ACTIVITY_SERVICE;


//2014-9-23

/**
 * 工具类.
 *
 * @version 1.01
 * @since 1.01
 */
public class DataUtil {
    //	public static final String IMG_SERVER_URL = "http://10.0.6.22:8080/ws/upload/uploadFile";
    public static boolean hasUnfinishedTask = false;
    public static boolean isAlert = false;

    /**
     * 将流转换成字符串.
     *
     * @param stream 待转换的流
     * @return 转换后的流
     * @throws IOException
     */
    public static String getNetData(InputStream stream) throws IOException {
        String bString = new String(getNetDataBytes(stream), "utf-8");
        // if (bString.contains("partyTag"))
        // {
        // Log.i("tag","DataUtil.getNetData-网络返回原始数据2->" +
        // bString.substring(bString.indexOf("partyTag")));
        // }
        // if
        // (bString.contains("/upload/activity/2/6/9/7/2697666bd6e4bfbb186b6efc567d4b42_150x120.jpg"))
        // {
        // Log.i("tag","DataUtil.getNetData-网络返回原始数据2->" +
        // bString.substring(bString.indexOf("/upload/activity/2/6/9/7/2697666bd6e4bfbb186b6efc567d4b42_150x120.jpg")));
        // }
        // if (bString.contains("\"partyID\":\"2\""))
        // {
        // Log.i("tag","DataUtil.getNetData-网络返回原始数据2->" +
        // bString.substring(bString.indexOf("\"partyID\":\"2\"")));
        // }
        return bString;
    }


    public static boolean isAvaiableSpace(int sizeMb) {
        boolean ishasSpace = false;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            long availableSpare = (blocks * blockSize) / (1024 * 1024);
            Log.d("剩余空间", "availableSpare = " + availableSpare);
            if (availableSpare > sizeMb) {
                ishasSpace = true;
            }
        }
        return ishasSpace;
    }


	/*
     * 为ImageLoader 配置config参数
	 * @param context 内容
	 * @return  ImageLoaderConfiguration类型
	 */
//	public static ImageLoaderConfiguration getImageLoaderConfig (Context context)
//	{
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//		.memoryCacheExtraOptions(480, 800)
//		.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 1)
//		.denyCacheImageMultipleSizesInMemory()
//		.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
//		.discCacheSize(50 * 1024 * 1024)
//		.denyCacheImageMultipleSizesInMemory()
//		.discCacheFileNameGenerator(new Md5FileNameGenerator())
//		.tasksProcessingOrder(QueueProcessingType.LIFO)
//		.writeDebugLogs() // Remove for release app
//		.build();
//		return config;
//	}

    /**
     * 给ImageLoader配置opyions参数.
     *
     * @return DisplayImageOptions类型
     */
    public static DisplayImageOptions getImageLoaderOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image).
                        resetViewBeforeLoading(true).
                        cacheInMemory(true).
                        cacheOnDisc(true)
                .imageScaleType(ImageScaleType.NONE).bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.default_image)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        return options;
    }


    private static long lastClickTime;

    //王登辉

    /**
     * 防止多次点击按钮 上次点击后 两秒内点击无效.
     *
     * @return 是否快速双击。如果两次点击相差2秒，则返回true，否则返回false
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;

        if (0 < timeD && timeD < 2000) {
            return true;
        }

        lastClickTime = time;

        return false;
    }

    /**
     * 获取系统当前时间.
     *
     * @return 当前系统时间字符串
     */
    @SuppressLint("SimpleDateFormat")
    public String getCurrentData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String data = dateFormat.format(curDate);

        return data;
    }

    /**
     * 将分钟转化为字符串.
     *
     * @param intTime 分钟
     * @return 转换后的字符串
     */
    public static String minToString(int intTime) {
        String strTime = "";
        if (60 > intTime) {
            strTime = "0小时" + intTime + "分钟";
        } else {
            int mins = intTime % 60;
            int totalHour = intTime / 60;
            if (24 <= totalHour) {
                int day = totalHour / 24;
                int hour = totalHour % 24;
                strTime = day + "天" + hour + "小时" + mins + "分钟";
            } else {
                strTime = totalHour + "小时" + mins + "分钟";
            }
        }

        return strTime;
    }

    /**
     * 如果一个数字小于10，那么在前面添加一个0
     *
     * @param i 数字
     * @return 前面补零的字符串
     */
    public static String add0(int i) {
        DecimalFormat decimalFormat = new DecimalFormat("00");

        return decimalFormat.format(i);
    }

    /**
     * 校验银行卡卡号.
     *
     * @param cardId 银行卡卡号字符串
     * @return 正确返回true，否则返回false
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));

        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 得到银行卡校验码.<P>
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位。
     *
     * @param nonCheckCodeCardId 无校验码的银行卡号
     * @return 校验码
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            throw new IllegalArgumentException("Bank card code must be number!");
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }

        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 将流转换成byte[].
     *
     * @param stream 待转换的流
     * @return 转换后的byte[]
     */
    public static byte[] getNetDataBytes(InputStream stream) throws IOException {
        BufferedInputStream bufferedStream = new BufferedInputStream(stream,
                8000);
        byte[] bytes = new byte[bufferedStream.available()];
        bufferedStream.read(bytes);
        return bytes;
    }

    /**
     * 去除List中重复的数据
     *
     * @param arlList 去重前的列表
     */
    public static void removeDuplicate(ArrayList arlList) {
        HashSet h = new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
    }


    /**
     * 去除List中重复的数据
     *
     * @param arlList 去重前的列表
     */
    public static void removeDuplicate(List<NewClasses> arlList) {
        HashSet h = new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);
    }

    /**
     * 显示Toast的工具方法.
     *
     * @param string Toast中显示的字符串
     */
    public static void getToast(final String string) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(CCApplication.app, string, Toast.LENGTH_LONG);
                if (string != null) {
                    if (!string.equals("")) {
                        toast.show();
                    }
                }
                //View view=toast.getView();
                //view.setBackgroundResource(R.drawable.shape_toast_background);
                //toast.setView(view);

            }
        });
    }

    public static void getToastShort(final String string) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(CCApplication.app, string, Toast.LENGTH_SHORT);
                //View view=toast.getView();
                //view.setBackgroundResource(R.drawable.shape_toast_background);
                //toast.setView(view);
                toast.show();
            }
        });
    }

    /**
     * 显示Toast的工具方法.
     *
     * @param string Toast中显示的字符串
     * @param time   次数
     */
    public static void getToast(final String string, final int time) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(CCApplication.app, string, time).show();
            }
        });
    }

    //胡鹏

    /**
     * 根据资源ID显示Toast提示，参考上面的写法.
     *
     * @param ResourceId 资源ID
     */
    public static void getToast(final int ResourceId) {
        getToast(ResourceId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示Toast提示.<P>
     * 根据资源ID和次数显示Toast提示。
     *
     * @param ResourceId 资源ID
     * @param time       次数
     */
    public static void getToast(final int ResourceId, final int time) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CCApplication.app, ResourceId, time).show();
            }
        });
    }

    /**
     * 检查字符串是否为空.
     *
     * @param string 字符串
     * @return 如果字符串为空，则返回“”，否则返回原来字符串
     */
    public static String NullOrString(String string) {
        return string == null ? "" : string;
    }

    /**
     * 日期时间格式模板0
     */
    public static final String DATETYPE_0 = "yyyy-MM-dd";
    /**
     * 日期时间格式模板1
     */
    public static final String DATETYPE_1 = "yyyy-MM-dd HH:mm";
    /**
     * 日期时间格式模板2
     */
    public static final String DATETYPE_2 = "MM-dd";
    /**
     * 日期时间格式模板3
     */
    public static final String DATETYPE_3 = "MM.dd";
    /**
     * 日期时间格式模板4
     */
    public static final String DATETYPE_4 = "HH:mm";
    /**
     * 日期时间格式模板5
     */
    public static final String DATETYPE_5 = "yyyy-MM.dd";
    /**
     * 日期时间格式模板6
     */
    public static final String DATETYPE_6 = "yyyy.MM.dd";
    /**
     * 日期时间格式模板7
     */
    public static final String DATETYPE_7 = "MM-dd HH:mm";
    // 日期时间格式模板8
    public static final String DATETYPE_8 = "dd/MM";

    public static final String DATETYPE_9 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将字符串转化为时间.
     *
     * @param string 时间字符串
     * @return Date 时间
     */
    public static Date parseDate(String string) {
        try {
            // string.indexOf(SYApplication.NOSET) == -1
            if (string != null && !string.equals("")) {
                return new SimpleDateFormat(DATETYPE_1).parse(string);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将毫秒数转换为时间格式的字符串.
     *
     * @param longTime 毫秒数
     * @return 格式化后的时间字符串。
     */
    public static String getStringDate6(long longTime) {
        return new SimpleDateFormat(DATETYPE_6).format(longTime);
    }

    /**
     * 将毫秒数转换为时间格式的字符串.
     *
     * @param longTime 毫秒数
     * @return 格式化后的时间字符串。
     */
    public static String getStringDate7(long longTime) {
        return new SimpleDateFormat(DATETYPE_7).format(longTime);
    }

    /**
     * 将毫秒数转化为字符串.
     *
     * @param longTime 时间毫秒数
     * @return String 格式化后的文本
     */
    public static String getStringDate(long longTime) {
        return new SimpleDateFormat(DATETYPE_1).format(longTime);
    }

    /**
     * 将毫秒数转化为字符串.
     *
     * @param longTime 时间毫秒数
     * @return String 格式化后的文本
     */
    public static String getStringDate0(long longTime) {
        return new SimpleDateFormat(DATETYPE_0).format(longTime);
    }

    public static String formatDate(String date) {
        String[] dateTime = {"", ""};
        String[] time = {"", "", ""};
        try {
            if (date != null) {
                dateTime = date.split(" ");
                time = dateTime[1].split(":");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (time.length > 2) {
            return dateTime[0] + " " + time[0] + ":" + time[1];
        }
        return date;
    }

    /**
     * 将毫秒数转换为时间格式的字符串.
     *
     * @param longTime 毫秒数
     * @return 格式化后的时间字符串。
     */
    public static String getStringTime(String longTime) {
        Calendar calendar = Calendar.getInstance();
        long time = Long.parseLong(longTime);
        calendar.setTimeInMillis(time);
        if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
            return new SimpleDateFormat(DATETYPE_3).format(time) + " 上午 "
                    + new SimpleDateFormat(DATETYPE_4).format(time);
        } else {
            return new SimpleDateFormat(DATETYPE_3).format(time) + " 下午 "
                    + new SimpleDateFormat(DATETYPE_4).format(time);
        }
    }

    /**
     * 将毫秒数转换为时间格式的字符串.
     *
     * @param longTime 毫秒数
     * @return 格式化后的时间字符串。
     */
    public static String getStringTime2(String longTime) {
        Calendar calendar = Calendar.getInstance();
        long time = Long.parseLong(longTime);
        calendar.setTimeInMillis(time);
        if (calendar.get(Calendar.HOUR_OF_DAY) < 12) {
            return "上午 " + new SimpleDateFormat(DATETYPE_4).format(time);
        } else {
            return "下午 " + new SimpleDateFormat(DATETYPE_4).format(time);
        }
    }

    /**
     * 将毫秒数转换为时间格式的字符串.
     *
     * @param longTime 毫秒数
     * @return 格式化后的时间字符串。
     */
    public static String getDayTimeStr(long longTime) {
        return new SimpleDateFormat(DATETYPE_4).format(longTime);
    }

    /**
     * 将秒数转换为时间格式的字符串.
     *
     * @param time 时间
     * @return 格式化后的时间字符串
     */
    public static String getDayTimeStr2(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String hms = formatter.format(time);

        return hms;
    }

    /**
     * 将毫秒数转化为字符串.<P>
     * 如果是去年就去年，如果是同一年 的就显示月日
     *
     * @param longTime 时间毫秒数
     * @return 格式化后的文本
     */
    public static String getStringDate2(long longTime) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(longTime);
        if (calendar.get(Calendar.YEAR) > year) {
            return "去年";
        } else {
            return new SimpleDateFormat(DATETYPE_2).format(longTime);
        }

        // return new SimpleDateFormat(DATETYPE_1).format(longTime * 1000);
    }

    /**
     * 将毫秒数转换为字符串.
     *
     * @param longTime 毫秒数
     * @return 转换后的字符串。
     */
    public static String getStringDate8(long longTime) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(longTime);
        if (calendar.get(Calendar.YEAR) > year) {
            return "去年";
        } else {
            return new SimpleDateFormat(DATETYPE_8).format(longTime);
        }
    }

    /**
     * 将毫秒数转换为字符串.
     *
     * @param longTime 毫秒数
     * @return 转换后的字符串。
     */
    public static String getStringDate9(long longTime) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(longTime);
        if (calendar.get(Calendar.YEAR) > year) {
            return "去年";
        } else {
            return new SimpleDateFormat(DATETYPE_4).format(longTime);
        }
    }

    /**
     * 将字符串转化为毫秒数.
     *
     * @param StringTime 时间字符串
     * @return 时间毫秒数
     * @throws ParseException
     */
    public static Long getLongDate(String StringTime) throws ParseException {
        Date date = parseDate(StringTime);
        if (date != null) {
            return date.getTime();
        }
        return 0L;
    }

    /**
     * 将String进行MD5编码.
     *
     * @param value 待编码的字符串
     * @return 编码后的字符串。
     */
    public static String encodeMD5(String value) {
        StringBuffer enStr = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(value.getBytes());
            byte[] b = algorithm.digest();
            for (int i = 0; i < b.length; i++) {
                String tmp = Integer.toHexString(b[i] & 0xFF);
                if (tmp.length() == 1) {
                    enStr.append("0").append(tmp);
                } else {
                    enStr.append(tmp);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return enStr.toString();
    }

    /**
     * 将byte[]进行MD5编码.
     *
     * @param fileField 待编码的字节型数据
     * @return 编码后的字符串。
     */
    public static String encodeMD5(byte[] fileField) {
        StringBuffer enStr = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(fileField);
            byte[] b = algorithm.digest();
            for (int i = 0; i < b.length; i++) {
                String tmp = Integer.toHexString(b[i] & 0xFF);
                if (tmp.length() == 1) {
                    enStr.append("0").append(tmp);
                } else {
                    enStr.append(tmp);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return enStr.toString();
    }

    /**
     * 将HttpEntity转化成byte[].
     *
     * @param entity 网络返回数据对象
     * @return 转换后的字节型数据。
     * @throws IOException
     */
    public static byte[] getNetDataBytes(HttpEntity entity) throws IOException {
        byte[] byets = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        entity.writeTo(byteArrayOutputStream);
        byets = byteArrayOutputStream.toByteArray();
        Log.i("tag", "DataHandler.getNetDataBytes-length->" + byets.length);
        return byets;
    }

    /**
     * 从HttpEntity中返回UTF-8编码形式的字符串.
     *
     * @param entity 网络返回数据对象
     * @return 编码后的字符串
     * @throws IOException
     */
    public static String getNetData(HttpEntity entity) throws IOException {
        String bString = new String(getNetDataBytes(entity), "utf-8");
        bString = bString.trim();
        Log.i("tag", "DataUtil.getNetData-网络返回的原始数据->" + bString);

        return bString;
    }

    /**
     * 将字符串转化成JSON对象.
     *
     * @param string JSON字符串
     * @return JSON对象
     */
    public static JSONObject getJsonObject(String string) {
        try {
            return new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 得到Base64编码字符串.<P>
     * 将InputStream进行Base64编码,并按照默认方式转换成String
     *
     * @param stream 网络返回数据对象
     * @return 编码后的字符串。
     */
    public static String getBase64Str(InputStream stream) {
        try {
            int count = stream.available();
            byte[] buffer = new byte[count];
            new BufferedInputStream(stream).read(buffer);
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HttpEntity转JSONObject.
     *
     * @param entity 网络返回数据对象
     * @return JSONObject对象。
     * @throws JSONException
     * @throws IOException
     */
    public static JSONObject getJsonObject(HttpEntity entity)
            throws JSONException, IOException {
        JSONObject object = new JSONObject(getNetData(entity));
        Log.i("tag", "DataUtil.getJsonObject-网络返回的JSON对象->" + object);
        return object;
    }

	/*
     * 通过经纬度计算两点的距离
	 *
	 * @param ox
	 * @param oy
	 * @param dx
	 * @param dy
	 * @return
	 */
	/*
	 * public static String getDistatce(GPSPoint gpsPoint) { if (gpsPoint ==
	 * null) { return SYApplication.NOSET; } double GPS_W = gpsPoint.getW_gps();
	 * double GPS_J = gpsPoint.getJ_gps(); GPSPoint point =
	 * SYApplication.app.mInfo.memberInfo.getGpsPoint(); if (point == null) {
	 * return "无距离"; } if (GPS_W == 0 || GPS_J == 0) { return "无距离"; }
	 *
	 * double R = 6371.229; double distance = 0.0; double dLat = (GPS_W -
	 * point.w_gps) * Math.PI / 180; double dLon = (GPS_J - point.j_gps) *
	 * Math.PI / 180; double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	 * Math.cos(GPS_W * Math.PI / 180) * Math.cos(point.w_gps * Math.PI / 180) *
	 * Math.sin(dLon / 2) Math.sin(dLon / 2); distance = (2 *
	 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R; distance *= 1000;
	 *
	 * String dis = null; if (distance >= 100) { distance = distance / 100; dis
	 * = String.valueOf(distance); return dis.substring(0, dis.indexOf(".") + 2)
	 * + "km"; } else { return "100m内"; } }
	 */

	/*
	 * 根据出生日期得到年龄 yt
	 *
	 * @param birthday 1998-9-9
	 * @return
	 */
	/*
	 * public static String getAge(String birthday) { try { // 毫秒数 Calendar cal
	 * = Calendar.getInstance(Locale.CHINESE); cal.setTime(new
	 * Date(SYApplication.app.mktime)); String string =
	 * (String.valueOf(cal.get(Calendar.YEAR) -
	 * Integer.parseInt(birthday.substring(0, 4)))) + "岁"; return string; }
	 * catch (NumberFormatException e) { return "未设置"; }
	 *
	 * }
	 */

    /**
     * Bitmap转 byte[].
     *
     * @param bm 需要转换的Bitmap
     * @return 转化后的字节型数据。
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 进度条对话框
     */
    public static ProgressDialog pd;

    /**
     * 弹出进度框.
     * @param context 内容
     * @param dialogTitle 对话框标题
     * @param dialogContent 对话框内容
     */
//	public static void showDialog(Context context, String dialogTitle, String dialogContent) {
//		clearDialog();
//		if (dialogTitle == null || dialogTitle.length() == 0) {
//			dialogTitle = "请稍等";
//		}
//
//		if (dialogContent == null || dialogContent.length() == 0) {
//			dialogContent = "正在加载...";
//		}
//
//		if (pd == null || !pd.isShowing()) {
//			pd = ProgressDialog.show(context, dialogTitle, dialogContent, true,
//					true);
//			pd.setContentView(R.layout.progress_bar);
//			pd.show();
//		}
//	}

    /**
     * 加载的对话框.
     *
     * @param context 内容
     */
    public static void showDialog(final Activity context) {
        clearDialog();
        if (pd == null || !pd.isShowing()) {
            pd = new ProgressDialog(context);
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(true);
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    context.finish();
                }
            });
            pd.show();
            pd.setContentView(R.layout.progress_bar);
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.getWindow().setBackgroundDrawableResource(R.color.transparent);
        }
    }

    /**
     * 弹出进度框.
     *
     * @param context       内容
     * @param dialogContent 对话框内容
     */
    public static void showDialog(final Activity context, String dialogContent) {
        clearDialog();
        if (pd == null || !pd.isShowing()) {
            // pd = ProgressDialog.show(context, "", "登录中,请稍后..", true, false);
            pd = ProgressDialog.show(context, null, dialogContent, true, true);
            pd.setCanceledOnTouchOutside(false);
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    context.finish();
                }
            });
            pd.setContentView(R.layout.progress_bar);
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.getWindow().setBackgroundDrawableResource(R.color.transparent);
        }
    }

    /**
     * 清除对话框.
     */
    public static void clearDialog() {
        if (pd != null)
            pd.dismiss();
    }

    /**
     * 将字节型数据转换为BMP图片.
     *
     * @param b 字节型数据
     * @return BMP图片
     */
    public static Bitmap Bytes2Bimap(byte[] b) {
        return (b.length != 0) ? BitmapFactory.decodeByteArray(b, 0, b.length) : null;
    }

    /**
     * 用来判断活动是否开始或者结束.
     *
     * @param startTimeStr 活动开始时间
     * @param endTimeStr   活动结束时间
     * @return 状态字符串
     */
    public static String partyBeginOrFinish(String startTimeStr, String endTimeStr) {
        String beginOrFinishStr = null;
        long nowTime;
        long startTime = 0;
        long endTime = 0;
        try {
            if (startTimeStr != null)
                startTime = DataUtil.getLongDate(startTimeStr);
            if (endTimeStr != null)
                endTime = DataUtil.getLongDate(endTimeStr);
            nowTime = System.currentTimeMillis();
            if (nowTime < startTime) {
                return "即将开始";
            }
            if (nowTime < endTime) {
                return "进行中";
            }
            if (nowTime > endTime) {
                return "已结束";
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return beginOrFinishStr;
    }

    /**
     * 判断第一个时间和第二个时间相差多少（天/小时/分钟）.
     *
     * @param time1 第一个时间
     * @param time2 第二个时间
     * @return 时间相差的字符串
     */
    public static String fromTime(long time1, long time2) {
        long time = time2 - time1;
        StringBuffer timeStr = new StringBuffer();
        long day = time / (24 * 60 * 60 * 1000);
        long hour = (time - 24 * 60 * 60 * 1000 * day) / (60 * 60 * 1000);
        long minute = (time - 24 * 60 * 60 * 1000 * day - 60 * 60 * 1000 * hour)
                / (60 * 1000);

        if (day > 0) {
            timeStr.append(day + "天");
        }

        if (hour > 0) {
            timeStr.append(hour + "时");
        }

        if (minute > 0) {
            timeStr.append(minute + "分");
        }

        return timeStr.toString();
    }

    /**
     * 获取屏幕高度宽度.
     *
     * @param context 内容
     */
    public static void getWithHeigh(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        // 取屏幕宽高
        // DisplayMetrics metrics =
        // SYApplication.app.getResources().getDisplayMetrics();
        int[] phoneWidthHeigh = new int[]{dm.widthPixels, dm.heightPixels};
        int phoneWidth = dm.widthPixels;
        int phoneHeigh = dm.heightPixels;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm;
    }

    /**
     * 打招呼Dialog
     */
    public static Dialog dialog_create;

    /**
     * 创建活动提示选择.
     *
     * @param activity Activity类型
     */
    public static void showCreateDialog(final Activity activity) {

    }

    private static int nowYear = -1;

    /**
     * 去掉时间中不需要的部分.<P>
     * 该方法用来去掉时间中不需要的部分，如将 2012-01-01 15:20:00 修改为 1月1日 15:20 如果是去年就显示xxxx年xx月。
     * 如果type=0 ，则返回具体时间，如果type=1，则返回日期。
     *
     * @param dateTimeStr 时间字符串
     * @param type        类型
     * @return 处理后的时间字符串
     */
    public static String dateTimeChange(String dateTimeStr, int type) {
        if (nowYear == -1) {
            nowYear = Calendar.getInstance().get(Calendar.YEAR);
        }
        if (type == 0) {
            String[] dataTime = dateTimeStr.split(" ");
            String[] datas = dataTime[0].split("-");
            String[] times = dataTime[1].split(":");
            if (nowYear == Integer.parseInt(datas[0])) {
                return datas[1].replaceFirst("^(0+)", "") + "月"
                        + datas[2].replaceFirst("^(0+)", "") + "日" + " "
                        + times[0] + ":" + times[1];
            } else {
                return datas[0] + "年" + datas[1].replaceFirst("^(0+)", "") + "月";
            }
        } else {
            String[] datas = dateTimeStr.split("-");
            if (nowYear == Integer.parseInt(datas[0])) {
                return datas[1].replaceFirst("^(0+)", "") + "月"
                        + datas[2].replaceFirst("^(0+)", "") + "日";
            } else {
                return datas[0] + "年" + datas[1].replaceFirst("^(0+)", "") + "月";
            }
        }
    }

    public static Dialog dialog_big;

    /**
     * 从流中读取数据（帮助用）.
     *
     * @param inStream 输入的流
     * @return 字节型数据组
     * @throws Exception
     */
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();

        return outStream.toByteArray();
    }

    /**
     * 得到圆角的图片.
     *
     * @param bitmap  待处理的图片
     * @param roundPx 圆角参数，数字越大则圆角越大
     * @return 圆角的图片。
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, 10, 10, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 检查是否输入.<P>
     * 目前只支持EditText和Button
     *
     * @param obj 检查对象
     * @return 如果输入为空，返回true，否则返回false。
     */
    public static boolean checkIfNull(Object obj) {
        if (obj.getClass() == EditText.class) {
            return (((EditText) obj).getText().toString().trim().length() == 0);
        } else if (obj.getClass() == Button.class) {
            return (((Button) obj).getText().toString().trim().length() == 0);
        } else {
            return false;
        }
    }

    /**
     * 网络是否可用.
     *
     * @param context 内容
     * @return 如果网络可用，返回true，否则返回false。
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }
        return false;
    }

    /**
     * 是否联网网络.
     *
     * @param context 内容
     * @return 如果连接上网络，返回true，否则返回false。
     */
    public static boolean IsHaveInternet(final Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            if (info != null) {
                return true;
            } else {
                getToast("当前没有任何网络可连接， 请检查网络后重试！");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 修订图片的大小.
     *
     * @param inputStream 输入流
     * @param sizew       待设定的宽度
     * @param sizeh       待设定的高度
     * @return 修订后的图片
     * @throws IOException
     */
    public static Bitmap revitionImageSize(InputStream inputStream, int sizew, int sizeh) throws IOException {

        // 取得图片
        InputStream temp = inputStream;
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
        options.inJustDecodeBounds = true;
        // 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
        BitmapFactory.decodeStream(temp, null, options);
        // 关闭流
        temp.close();

        // 生成压缩的图片
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            // 这一步是根据要设置的大小，使宽和高都能满足
            if ((options.outWidth >> i <= sizew)
                    && (options.outHeight >> i <= sizeh)) {
                // 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
                temp = inputStream;
                // 这个参数表示 新生成的图片为原始图片的几分之一。
                options.inSampleSize = (int) Math.pow(2.0D, i);
                // 这里之前设置为了true，所以要改为false，否则就创建不出图片
                options.inJustDecodeBounds = false;

                bitmap = BitmapFactory.decodeStream(temp, null, options);
                break;
            }
            i += 1;
        }

        return bitmap;
    }

    //by yetao

    /**
     * 改变大小的BMP图片.<P>
     * 注意一下，返回的那个bitmap会很大，你用完以后要把它回收掉，不然你很容易内存报oom错误.
     *
     * @param bitmap   待改变大小的BMP图片
     * @param newWidth 新图片的宽度
     * @return 改变后的BMP图片。
     */
    public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float temp = ((float) height) / ((float) width);
        int newHeight = (int) ((newWidth) * temp);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();

        return resizedBitmap;
    }

    /**
     * 按图片大小(字节大小)缩放图片.
     *
     * @param path 图片文件路径
     * @return 转换后的图片。
     */
    public static Bitmap fitSizeImg(String path) {
        if (path == null || path.length() < 1)
            return null;
        File file = new File(path);
        Bitmap resizeBmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
//		opts.inJustDecodeBounds = true;
        // 数字越大读出的图片占用的heap越小 不然总是溢出
        if (file.length() < 512 * 1024) {//小于512k
            return null;
        } else if (file.length() < 1024 * 1024) { // 512k-1024k
            opts.inSampleSize = 4;
        } else {
            opts.inSampleSize = 5;
        }
        resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);

        return resizeBmp;
    }

    /**
     * 设置HTML字符串.
     *
     * @param color   颜色代码 如#ffffff
     * @param content 内容
     * @return 变成HTML格式的字符串。
     */
    public static StringBuffer setHtmlStr(String color, String content) {
        StringBuffer htmlBuffer = new StringBuffer();
        htmlBuffer.append("<font color=")
                .append(color)
                .append(">")
                .append(content)
                .append("</font>");

        return htmlBuffer;
    }

    /**
     * 手机验证.
     *
     * @param phone 电话号码
     * @return 如果电话号码符合规则，则返回true；否则，返回false
     */
    public static boolean checkPhone(String phone) {
        if (phone.length() != 11) {
            return false;
        }
        Pattern pattern = Pattern.compile("^1[345789]\\d{9}$");
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

    /**
     * 验证QQ号码.
     *
     * @param QQ 待验证的QQ号码字符串
     * @return 如果符合QQ号码规则，返回true，否则返回false。
     */
    public static boolean checkQQ(String QQ) {
        String regex = "^[1-9][0-9]{4,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(QQ);
        return m.matches();
    }

    /**
     * 手机验证.
     *
     * @param mobiles 手机号码
     * @return 如果符合手机号码规则，返回true，否则返回false。
     */
    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 字符串是否在长度内.
     *
     * @param str       字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 在长度内返回true，否则返回false
     */
    public static boolean isLength(String str, int minLength, int maxLength) {
        return str.length() >= minLength && str.length() <= maxLength;
    }

    /**
     * 邮箱验证
     *
     * @param phone 邮箱号码字符串
     * @return 如果是符合邮箱字符串规则，返回true，否则返回false。
     */
    public static boolean checkEmail(String phone) {
        // Pattern pattern =
        // Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

	/*
	 * 根据用户信息生成DES
	 *
	 * @param context
	 * @param memberInfo
	 * @return
	 */
    // public static String createDES(Context context, MemberInfo memberInfo) {
    // // 生成DES
    // String imel = ((TelephonyManager)
    // context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId();
    // StringBuffer stringBuffer = new StringBuffer();
    // // 平台登录 id+密码+imel 加密方式
    // if (memberInfo.getThridType() == 0) {
    // stringBuffer.append(memberInfo.getMemberID());
    // stringBuffer.append("+");
    // stringBuffer.append(memberInfo.getPassword());
    // stringBuffer.append("+");
    // stringBuffer.append(imel);
    // Log.i("tag", stringBuffer.toString());
    // // 新浪第三方 id+第三方id+imel 加密方式
    // } else if (memberInfo.getThridType() == 1) {
    // stringBuffer.append(memberInfo.getMemberID());
    // stringBuffer.append("+");
    // stringBuffer.append("");
    // stringBuffer.append("+");
    // stringBuffer.append(imel);
    // Log.i("tag", stringBuffer.toString());
    // }
    // try {
    // DES3 des3 = new DES3();
    // String loginCode1 = des3.encrypt(stringBuffer.toString());
    // des3 = null;
    // return loginCode1;
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // return "";
    // }

    /**
     * 获取手机品牌型号和系统号码.
     *
     * @param context 内容
     * @return 手机品牌型号和系统号码
     */
    public static String getPhoneInfo(Context context) {
        String BRAND = android.os.Build.BRAND;
        String MODEL = android.os.Build.MODEL;
        String RELEASE = android.os.Build.VERSION.RELEASE;

        return BRAND + "_" + MODEL + "_" + RELEASE;
    }

    /**
     * 判断字符串是否包含中文.
     *
     * @param str 字符串
     * @return 如果字符串包含中文，返回true，否则返回false。
     */
    public static boolean isCN(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");

            return bytes.length != str.length();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断EditText是否是空.
     *
     * @param editText 判断的EditText
     * @return 如果为空，则返回true，否则返回false
     */
    public static boolean isNull(EditText editText) {
        String userName = editText.getText().toString();

        return isNull(userName);
    }

    /**
     * 判断字符串是否为空或空串.
     *
     * @param str 字符串
     * @return 如果字符串为null或空串，则返回true，否则返回false。
     */
    public static boolean isNull(String str) {
        return str == null || str.equals("");
    }

    /**
     * 得到根View.
     *
     * @param context 返回根view的activity
     * @return 根view
     */
    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content))
                .getChildAt(0);
    }

    /**
     * 返回价格只取小数点后两位.
     *
     * @param d double型的数
     * @return 返回四舍五入的取小数点后两位的值
     */
    public static BigDecimal setScale(double d) {
        BigDecimal b = new BigDecimal(d);
        BigDecimal t = b.setScale(2, BigDecimal.ROUND_HALF_UP);
        return t;
    }

    /**
     * 解析时间数据变为小时分秒的字符串数组.
     *
     * @param time long型的时间
     * @return 小时 分钟 秒 的字符数组
     */
    public static List<String> parseTime(long time) {

        List<String> hms = new ArrayList<String>();
        long hour = time / (60 * 60 * 1000);
        long minute = (time - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (time - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;

        if (second >= 60) {
            second = second % 60;
            minute += second / 60;
        }

        if (minute >= 60) {
            minute = minute % 60;
            hour += minute / 60;
        }

        String sh = "";
        String sm = "";
        String ss = "";
        if (hour < 10) {
            sh = "0" + String.valueOf(hour);
        } else {
            sh = String.valueOf(hour);
        }

        if (minute < 10) {
            sm = "0" + String.valueOf(minute);
        } else {
            sm = String.valueOf(minute);
        }

        if (second < 10) {
            ss = "0" + String.valueOf(second);
        } else {
            ss = String.valueOf(second);
        }
        hms.add(sh);
        hms.add(sm);
        hms.add(ss);

        return hms;
    }

    /**
     * 判断字符串是否是空或者全是空格.
     *
     * @param str 判断的字符串
     * @return 如果全是空，返回true，否则返回false
     */
    public static boolean isNullorEmpty(String str) {
        if (str == null) {
            return true;
        }

        String text = str.replaceAll(" ", "");

        return text.equals("");
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 文件存在则返回true，否则返回false
     */
    public static boolean isFileExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

        return true;
    }

    public static long GetNetSpeed(Context context) {
        long nowTotalRxBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid)
                == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);
        long nowTimeStamp = System.currentTimeMillis();
        long lastTimeStamp = 0;
        long lastTotalRxBytes = 0;
        long speed = (nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp);
        System.out.println("speed:" + speed);
        return speed;
    }

    public static void cleanTempFile(boolean is_compress, int size) {
        boolean ret;
        String path = Environment.getExternalStorageDirectory().getPath() + "/namecard";
        if (is_compress) {
            for (int i = 0; i < size; i++) {
                File png = new File(path + i + ".png");
                File jpeg = new File(path + i + ".jpeg");
                ret = png.delete();
//				System.out.println("ret:"+ret+"file:"+png);
                ret = jpeg.delete();
//				System.out.println("ret:"+ret+"jpeg file:"+jpeg);
            }
        }
    }

    //删除下载的附件
    public static void cleanCacheFile() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/Zigui_cache";
        File file = new File(path);
        if (file.exists() == false) {
            return;
        } else {
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();

                for (File f : childFile) {
                    f.delete();
                }
            }
        }
    }

    //删除WEBVIEW 缓存
    public static void cleanWebViewCacheFile() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/com.wcyc.zigui2/";
        ;
        File file = new File(path);
        if (file.exists() == false) {
            return;
        } else {
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();

                for (File f : childFile) {
                    f.delete();
                }
            }
        }
    }

    public static void Sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void getAttachmentList(BaseActivity mContext, String buzzId, String type, int action) {
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null) {
            JSONObject json = new JSONObject();
            try {
                json.put("userId", user.getUserId());
                json.put("schoolId", user.getSchoolId());
                json.put("attachementType", type);
                json.put("buzzId", buzzId);
                mContext.setAction(action);
                System.out.println("getAttachmentList:" + json);
                mContext.queryPost(Constants.QUREY_ATTACHMENT, json);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static String getRetifitDownloadURL1(Context context, String file) {
        if (!isNull(file)) {
            String url = "";
            if (file.indexOf("downloadApi") > 0) {
                file = file.replace("/downloadApi", "");
            } else {
                file = "?" + file;
            }
            url = "download" + file//添加图像素大小+"&sf=720*1280"
                    + Constants.AUTHID + "@" + ((BaseActivity) context).getDeviceID()
                    + "@" + CCApplication.app.getMemberInfo().getAccId();
            return url;
        }
        return null;
    }

    public static String getDownloadURL(Context context, String file) {
        if (!isNull(file)) {
            String url = "";
            if (file.indexOf("downloadApi") > 0) {
                file = file.replace("/downloadApi", "");
            } else {
                file = "?" + file;
            }
            url = Constants.DOWNLOAD_URL + file//添加图像素大小+"&sf=720*1280"
                    + Constants.AUTHID + "@" + ((BaseActivity) context).getDeviceID()
                    + "@" + CCApplication.app.getMemberInfo().getAccId();
            return url;
        }
        return null;
    }

    public static String getDownloadURL(String file) {
        if (!isNull(file)) {
            String key = "/downloadApi";
            int pos = file.indexOf(key);
            if (pos >= 0) {
                file = file.substring(pos + key.length(), file.length());
//				file = file.replace("/downloadApi","");
                NewMemberBean member = CCApplication.app.getMemberInfo();

                String url = Constants.DOWNLOAD_URL + file
                        + Constants.AUTHID + "@" + CCApplication.app.getDeviceId();
                if (member != null) {
                    url += "@" + member.getAccId();
                }
                return url;
            } else {
                return file;
            }
        }
        return null;
    }

    //不需要鉴权
    public static String getDownloadURLWithNoId(String file) {
        if (!isNull(file)) {
            String key = "/downloadApi";
            int pos = file.indexOf(key);
            if (pos >= 0) {
                file = file.substring(pos + key.length(), file.length());
                String url = Constants.DOWNLOAD_URL + file + "&nologin=y";
                return url;
            } else {
                return file;
            }
        }
        return null;
    }

    //缩略图
    public static String getIconURL(Context context, String file) {
        if (file != null) {
            return Constants.URL + file + "&sf=150*150";
        } else {
            return "";
        }
    }

    public static String getIconURL(String file) {
        if (file != null) {
            String url = Constants.URL + file + "&sf=150*150" + Constants.AUTHID + "@" + CCApplication.getInstance().getDeviceId()
                    + "@" + CCApplication.getInstance().getMemberInfo().getAccId();
            return Constants.URL + file + "&sf=150*150";//;
        } else {
            return "";
        }
    }

    public static String modifyImageUrl(String content) {
        String newContent = content;
        Spanned parse = Html.fromHtml(content);
        ImageSpan[] image = parse.getSpans(0, Integer.MAX_VALUE, ImageSpan.class);
        for (ImageSpan item : image) {
            String url = item.getSource();
            String newUrl = getDownloadURL(url);
            newContent = content.replace(url, newUrl);
            content = newContent;
        }
        return newContent;
    }

    //对html内容的表格进行处理
    public static String parseHtmlTable(String content) {
        int length = content.length();
        int pos = 0;
        String addString = " border=1";
        String prefix = "<table";
        int add = addString.length();
        while (pos < length) {
            pos = content.indexOf(prefix, pos);
            if (pos >= 0) {
                int end = content.indexOf(">", pos);
                String origin = content.substring(pos, end);
                System.out.println("orgin:" + origin);
                content = content.replace(origin, origin + addString);
                System.out.println("content:" + content);
                length += add;
                pos = end;
            } else {
                break;
            }
        }
        return content;
    }

    //对html内容进行处理
    public static String parseHtmlContent(Context mContext, String content) {
        String contentStr = "";//待存储的内容
        int tableStartAndEnd = 0;//一个Exceed中的  table>  的下标
        String tablePrefix = "table";
        String prefix = "<table width=";
        String prefix1 = "<table cellpadding=";
        int len = tablePrefix.length();
        while (content.contains(prefix) || content.contains(prefix1)) {
            if (!content.contains(prefix)) {
                if (content.contains(prefix1)) {
//					prefix="width=";
                    prefix = "table cellpadding=\"0\" cellspacing=\"0\" width=";
                }
            }
            int begin = content.indexOf(prefix);
            int pos = content.indexOf("\"", begin);
            if (begin >= 0) {
                int end = content.indexOf(">", begin);
                String origin = content.substring(begin, end);
                System.out.println("orgin:" + origin);
                content = content.replace(origin, origin + " border=1");
                System.out.println("content:" + content);

                int tableStart = content.indexOf(tablePrefix);//内容中第一次出现table的下标
                String contentAfter = content.substring(tableStart + len);//内容中第一次出现table之后的内容
                int tableEnd = contentAfter.indexOf(tablePrefix);//剩余内容第一次出现table的下标 全文内容中第二次出现table
                tableStartAndEnd = tableStart + len + tableEnd + len + 1;//一个Exceed中的  table>  的下标(即>后一位)
                contentStr += content.substring(0, tableStartAndEnd);//内容中 一个Exceed中的  table> 之前的字符串  +=拼接
                content = content.substring(tableStartAndEnd);//内容中 一个Exceed中的  table> 之后的字符串
            }
        }
        contentStr += content;//+=拼接while之后剩余的字符串
        content = contentStr;//返回处理好的完整的内容
        return content;
    }

    public static double TableWidthExceed(Context mContext, String content) {
        DisplayMetrics dm = getDisplayMetrics(mContext);
        String prefix = "<table width=";
        String prefix1 = "<table cellpadding=";
        int begin = 0, endPos = 0, pos = 0;

        if (content.contains(prefix) || content.contains(prefix1)) {
            if (!content.contains(prefix)) {
                begin = content.indexOf(prefix1);
                if (begin >= 0) {
                    prefix = "width=";
                }
            }
            begin = content.indexOf(prefix, begin);
            pos = content.indexOf("\"", begin);
            endPos = content.indexOf("\"", pos + 1);
            if (begin >= 0) {
                String width = content.substring(pos + 1, endPos);
                System.out.println("width:" + width + " " + dm.widthPixels);
                if (width.contains("%")) {
                    return 1;
                }
                int w = Integer.parseInt(width);
                double MaxWidth = dm.widthPixels;
                //			if (w > MaxWidth) {
                //				return (w/MaxWidth);
                //			}
                if (w >= 320.0) {
                    return 1.5;//常规720*1280 720px=360dp 360除240为1.5
                }
            }
        }
        return 1;
    }

    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        return size;
    }

    /*** 转换文件大小单位(b/kb/mb/gb) ***/
    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1024 * 1024) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1024 * 1024 * 1024) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static List<Role> getAllRole() {
        NewMemberBean member = CCApplication.getInstance().getMemberInfo();
        MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
        String name = member.getUserName();
        List<UserType> type = member.getUserTypeList();
        UserType curUser = CCApplication.getInstance().getPresentUser();
//		String schoolname = "",grade = "";
        List<Role> roles = new ArrayList<Role>();
        int i = 0;
        if (type != null) {


            for (UserType user : type) {
                String schoolname = user.getSchoolName();
                String parentname = user.getParentName();
                String classname = user.getClassName();

                Role role = new Role();
                if (Constants.TEACHER_STR_TYPE.equals(user.getUserType())) {
                    name += "（教职工）";
                    role.name = name;
                    role.school = schoolname;
                    roles.add(role);
//					roles.remove(null);

                    role.ischecked = i++ == CCApplication.getInstance().getPresentUserIndex();
                } else {
                    role.name = parentname;
                    if (!DataUtil.isNullorEmpty(classname)) {

                        role.school = schoolname + classname;
                    } else {
                        role.school = schoolname;
                    }


                    roles.add(role);
//					roles.remove(null);

                    role.ischecked = i++ == CCApplication.getInstance().getPresentUserIndex();

                }
            }

        }
        return roles;
    }

    public static String getCurrentDate(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATETYPE_9);
        Date curDate = new Date(time);// 获取当前时间
        String date = dateFormat.format(curDate);
        return date;
    }

    public static String getLastDate(String date, String dateValue, String dateType) {
        String lastDate = "";
        int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};//非闰年的月份天数
        int[] days1 = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};//闰年的月份天数
        int value = Integer.parseInt(dateValue);
        if (isNullorEmpty(date)) return "";
        String[] dateTime = date.split(" ");
        String[] dates = dateTime[0].split("-");
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);

        value -= 1;
        int addYear = value / 12;
        int addMonth = value % 12;
        if ("年".equals(dateType)) {
            year += value;
        } else if ("月".equals(dateType)) {
            month += addMonth;
            if (month > 12) {
                year += 1;
                month -= 12;
            }
            year += addYear;
            //需要判断闰月
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                day = days[month - 1];
            } else {
                day = days1[month - 1];
            }
        } else if ("日".equals(dateType)) {

        }
        lastDate = year + "-" + month + "-" + day + " " + dateTime[1];
        return lastDate;
    }


    //转发分到元
    public static String convertF2Y(long amount) {
        return BigDecimal.valueOf(Float.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

    //是否为班主任
    public static boolean isClassAdmin() {
        MemberDetailBean userDetail = CCApplication.getInstance().getMemberDetail();
        if (userDetail != null) {

            List<NewClasses> classes = userDetail.getClassList();
            if (classes != null) {
                for (NewClasses newClass : classes) {
                    if ("1".equals(newClass.getIsAdviser())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

//	//获取担任班主任的班级信息
//	public static List<NewClasses> getClassAdmin(){
//		MemberDetailBean userDetail = CCApplication.getInstance().getMemberDetail();
//
//		if(userDetail != null){
//			List<NewClasses> list = new ArrayList<NewClasses>();
//			List<NewClasses> classes = userDetail.getClassList();
//			if(classes != null){
//				for(NewClasses newClass:classes){
//					if("1".equals(newClass.getIsAdviser())){
//						list.add(newClass);
//					}
//				}
//			}
//			return list;
//		}
//
//		return null;
//	}

    //获取担任班主任的班级信息
    public static List<NewClasses> getClassAdmin() {
        MemberDetailBean userDetail = CCApplication.getInstance().getMemberDetail();
        List<NewClasses> ret = new ArrayList<NewClasses>();
        if (userDetail != null) {
            List<NewClasses> classes = userDetail.getClassList();
            if (classes != null) {
                for (NewClasses newClass : classes) {
                    if ("1".equals(newClass.getIsAdviser())) {
                        ret.add(newClass);
                    }
                }
            }
        }

        return ret;
    }

    //获取所任教的班级信息
    public static List<NewClasses> getTeachClass() {
        MemberDetailBean userDetail = CCApplication.getInstance().getMemberDetail();

        if (userDetail != null) {
            List<NewClasses> classes = userDetail.getClassList();
            return classes;
        }

        return null;
    }

    public static boolean isClassIdExist(List<String> list, String classId) {
        for (String string : list) {
            if (string.equals(classId)) {
                return true;
            }
        }
        return false;
    }

    public static String getPayDate(Context mcontext) {
        String chargeDate = null;
        JSONObject json = new JSONObject();
        String url = new StringBuilder(Constants.SERVER_URL).append(Constants.GET_ORDER_LIST).toString();
        try {
            //获取服务器时间
            chargeDate = HttpHelper.httpPostJsonTime(mcontext, url, json);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return chargeDate;
    }

    public static String[] computeStartDate(Context mcontext) {
        String chargeDate = null, isFree = "0", endDate = "";
        String[] result = new String[2];//返回两个日期，第一个是充值开始时间，第二个是服务开始时间

        JSONObject json = new JSONObject();
        String url = new StringBuilder(Constants.SERVER_URL).append(Constants.GET_ORDER_LIST).toString();
        try {
            //获取服务器时间
            chargeDate = HttpHelper.httpPostJsonTime(mcontext, url, json);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ServiceExpiredBean expired = CCApplication.getInstance().getServiceExpiredInfo();
//		if(expired != null && "1".equals(expired.getProductExpired())){
//			endDate = expired.getEndDate();
//			isFree = expired.getIsFree();
//		}

        result = computeStartDate(endDate, chargeDate, isFree);
        System.out.println("chargeDate:" + chargeDate + " endDate:" + endDate + " result:" + result[0]);
        return result;
    }
    //服务开始时间计算办法
    //如果充值时间大于15号，服务至本月底免费赠送，服务费从下月1号开始计算。至毕业的服务截止日期为学生毕业那一年的6月30日
    //如果是之前充值到2016-5-31，2016-5-12来充值，充值的服务开始时间是2016-6-1。
    //如果是试用或免费到2016-4-30，2016-5-12来充值，充值的服务开始时间是2016-5-1。
    //如果是之前充值到2016-4-30，2016-5-12来充值，充值的服务开始时间是2016-5-1。
    //如果是试用、免费到6月30号，5月14日来充值，服务开始日期从7月1日开始

    //endDate 服务结束日期
    //chargeDate 充值日期
    //isFree 服务是否免费
    public static String[] computeStartDate(String endDate, String chargeDate, String isFree) {
        String[] chargeDateTime = chargeDate.split(" ");
        String[] YMD = chargeDateTime[0].split("-");
        String[] endDateTime;
        String[] endYMD = null;
        String ret[] = new String[2];
        int endMonth = 0, endYear;
        int serveMonth = 0;//服务开始月份
        int serveYear = 0;//服务开始年份
        int serveDay = 0;//服务开始日期
        int day = Integer.parseInt(YMD[2]);
        int month = Integer.parseInt(YMD[1]);
        int year = Integer.parseInt(YMD[0]);
        if (!isNullorEmpty(endDate)) {//之前充过值
            endDateTime = endDate.split(" ");
            endYMD = endDateTime[0].split("-");
            endMonth = Integer.parseInt(endYMD[1]);
            endYear = Integer.parseInt(endYMD[0]);
            //以前充过值并且不是免费的。
//			if("0".equals(isFree)){
            //如果充值日期在服务结束日期之前
            if (year <= endYear || month <= endMonth) {
                year = endYear;
                month = endMonth + 1;

                if (month > 12) {
                    month -= 12;
                    year += 1;
                }
                serveYear = year;
                serveMonth = month;
                serveDay = day = 1;
            } else {
                if (day > 15) {
                    serveYear = year;
                    serveMonth = month;
                    serveDay = day;
                    month += 1;
                    if (month > 12) {
                        month -= 12;
                        year += 1;
                    }
                }
            }
//			}
        } else {//没冲过值
            serveMonth = month;
            serveYear = year;
            serveDay = day;
            if (day > 15) {
                month += 1;
                if (month > 12) {
                    month -= 12;
                    year += 1;
                }
            }
        }
        day = 1;
        ret[0] = year + "-" + month + "-" + day + " " + chargeDateTime[1];
        ret[1] = serveYear + "-" + serveMonth + "-" + serveDay + " " + chargeDateTime[1];
        return ret;
    }

    public static long computeMonths(Product productItem, String startDate) {
        UserType user = CCApplication.getInstance().getPresentUser();

        int months = 0;
        String graduateDate = "";
        String dateValue = productItem.getValidityDateValue();
        String dateType = productItem.getValidityDateType();//都是月？
        if (!DataUtil.isNullorEmpty(dateValue))
            months = Integer.parseInt(dateValue);
        else {
            MemberDetailBean member = CCApplication.getInstance().getMemberDetail();
            List<NewChild> childList = member.getChildList();
            String childId = user.getChildId();
            if (childList != null) {
                for (NewChild child : childList) {//找到当前用户的孩子id
                    if (childId.equals(child.getChildID())) {
                        graduateDate = child.getGraduateDate();
                        break;
                    }
                }
                if (!DataUtil.isNullorEmpty(graduateDate)) {
                    months = getMonth(graduateDate, startDate);
                }
            }
        }
        return months;
    }

    public static int getMonth(String endDate, String startDate) {
        String[] startDateTime, endDateTime;
        int months = 0, endYear, endMonth, endDay, startYear, startMonth, startDay;
        startDateTime = startDate.split(" ");
        endDateTime = endDate.split(" ");
        String[] end = endDateTime[0].split("-");
        String[] start = startDateTime[0].split("-");
        endYear = Integer.parseInt(end[0]);
        endMonth = Integer.parseInt(end[1]);
        endDay = Integer.parseInt(end[2]);
        startYear = Integer.parseInt(start[0]);
        startMonth = Integer.parseInt(start[1]);
        if (endDay > 15) {
            months = endYear * 12 + endMonth + 1 - (startYear * 12 + startMonth);
        } else {
            months = endYear * 12 + endMonth - (startYear * 12 + startMonth);
        }
        return months;
    }

    //标记未读消息
    public static int getUnreadStatus(String type) {
        ModelRemindList remind = CCApplication.getInstance().getModelRemindList();
        if (remind != null) {
            List<ModelRemind> list = remind.getMessageList();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    ModelRemind item = list.get(i);
                    if (item != null && type != null) {
                        String count = item.getCount();
                        int num = Integer.parseInt(count);
                        if (type.equals(item.getType())) {
                            return num;
                        }
                    }
                }
            }
        }
        return 0;
    }

    //获取某个学生的毕业日期
    public static String getGraduateDate(String studentId) {
        String graduateDate;
        MemberDetailBean member = CCApplication.getInstance().getMemberDetail();
        List<NewChild> childList = member.getChildList();
        if (childList != null) {
            for (NewChild child : childList) {//找到当前用户的孩子id
                if (studentId.equals(child.getChildID())) {
                    return graduateDate = child.getGraduateDate();
                }
            }
        }
        return "";
    }

    //获取日期或时间
    public static String getDateTime(String dateTime, int type) {
        if (dateTime != null) {
            String[] ret = dateTime.split(" ");
            if (type == 1) {//日期
                return ret[0];
            } else if (type == 2) {//时间
                return ret[1];
            }
        }
        return "";
    }

    public static void setTypeColor(Button view, String type) {
        if ("问题咨询".equals(type)) {
            view.setBackgroundColor(Color.rgb(96, 149, 206));
            view.setBackgroundResource(R.drawable.btn_type_question);
        } else if ("投诉建议".equals(type)) {
            view.setBackgroundColor(Color.rgb(79, 182, 118));
            view.setBackgroundResource(R.drawable.btn_type_suggest);
        } else if ("表彰嘉奖".equals(type)) {
            view.setBackgroundColor(Color.rgb(216, 102, 124));
            view.setBackgroundResource(R.drawable.btn_type_phraise);
        } else if ("好人好事".equals(type)) {
            view.setBackgroundColor(Color.rgb(202, 164, 62));
            view.setBackgroundResource(R.drawable.btn_type_good);
        } else if ("其他".equals(type)) {
            view.setBackgroundColor(Color.rgb(245, 92, 157));
            view.setBackgroundResource(R.drawable.btn_type_other);
        }
    }

    public static String GetBeijingTime() {
        URL url;
        String date = null;
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect(); // 发出连接
            long ld = uc.getDate(); // 取得网站日期时间
            date = getCurrentDate(ld); // 转换为标准时间对象
            // 分别取得时间中的小时，分钟和秒，并输出

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 取得资源对象
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (date == null) {
            long time = System.currentTimeMillis();
            date = getCurrentDate(time);
        }
        return date;
    }

    public static void ClearModelRemind(String type) {
        ModelRemindList remind = CCApplication.getInstance().getModelRemindList();
        if (remind != null) {
            List<ModelRemind> list = remind.getMessageList();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (type.equals(list.get(i).getType())) {
                        list.remove(i);
                        remind.setMessageList(list);
                        CCApplication.getInstance().setModelRemindList(remind);
                        return;
                    }
                }
            }
        }
    }

    //是否是主号
    public static boolean isMain() {
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user == null || user.getChildId() == null) return true;
        MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
        if (detail != null) {
            List<NewChild> list = detail.getChildList();
            if (list != null) {
                for (NewChild child : list) {
                    if (user.getChildId().equals(child.getChildID())) {
                        child.getIsmain();
                        if ("1".equals(child.getIsmain())) {
                            return true;
                        } else if ("0".equals(child.getIsmain())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    //获取当前家长用户孩子所在的班级
    public static String getChildClassId() {
        UserType user = CCApplication.getInstance().getPresentUser();
        MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
        if (detail != null) {
            List<NewChild> list = detail.getChildList();
            if (list != null) {
                for (NewChild child : list) {
                    if (user.getChildId().equals(child.getChildID())) {
                        return user.getClassId();
                    }
                }
            }
        }
        return null;
    }

    public static String ReadFile(String Path) {
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void showHtmlSetting(WebView view, String content) {
        String newContent = DataUtil.modifyImageUrl(content);
        Context mContext = CCApplication.getInstance().getApplicationContext();
        WebSettings webSettings = view.getSettings();
        webSettings.setMinimumFontSize(15);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        //html页面大小自适应
        webSettings.setDisplayZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
//		view.loadDataWithBaseURL(null, strUrl, "text/html; charset=UTF-8", null, null);
        view.loadData(newContent, "text/html; charset=UTF-8", null);//这种写法可以正确解码
        view.setBackgroundColor(mContext.getResources().getColor(R.color.background_color));
    }

    public static List<ContactsList> removeSelfInContact(List<ContactsList> list) {
//		return list;
        //从联系人中去掉当前用户
        NewMemberBean member = CCApplication.getInstance().getMemberInfo();
        List<UserType> users = member.getUserTypeList();
        List<ContactsList> temp = new ArrayList<ContactsList>();

        if (list != null) {
            for (ContactsList contact : list) {
                boolean isSame = false;
                if (users != null) {
                    for (UserType user : users) {
                        String userName = contact.getUserName();
                        if (userName != null) {
                            if (userName.equals(user.getHxUserName())) {
                                isSame = true;
                            }
                        }

                        String userId = contact.getUserId();
                        if (userId != null) {
                            if (userId.equals(user.getUserId())) {
                                isSame = true;
                            }
                        }
                    }
                }
                if (isSame == false) {
                    temp.add(contact);
                } else {
                    CCApplication.getInstance().setCurrentUserContactInfo(contact);
                }
            }
        }
        return temp;
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
	    /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }


    //MIME_MapTable是所有文件的后缀名所对应的MIME类型的一个String数组：

    private final static String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    public static String getShowTime(String origin) {
        int pos = origin.lastIndexOf(":");
        String ret = origin.substring(0, pos);
        return ret;
    }

    public static String getStrToWeek(String str) {
        String week = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(format.parse(str));
            int dayForWeek = 0;
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }

            if (dayForWeek == 1) {
                week = "星期一";
            } else if (dayForWeek == 2) {
                week = "星期二";
            } else if (dayForWeek == 3) {
                week = "星期三";
            } else if (dayForWeek == 4) {
                week = "星期四";
            } else if (dayForWeek == 5) {
                week = "星期五";
            } else if (dayForWeek == 6) {
                week = "星期六";
            } else if (dayForWeek == 7) {
                week = "星期日";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return week;
    }

    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    public static boolean isTopActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            //应用程序位于堆栈的顶层
            if (getAppProcessName(context).equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    //判断用户所在的群组(后台服务器数据)是否在环信的群组列表中
    public static boolean isInGroup(String groupId) {
        EasemobGroupInfo groupInfo = CCApplication.getInstance().getHxGroupList();
        if (groupInfo != null) {
            List<EMGroup> groupList = groupInfo.getUserGroupList();//
            if (groupList != null) {
                for (EMGroup item : groupList) {
                    if (groupId.equals(item.getGroupId())) {
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }


    public static Bitmap getVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
        } catch (RuntimeException ex) {
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        kind = MediaStore.Images.Thumbnails.MICRO_KIND;
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        } else {
        }

        return bitmap;
    }

    public static String convertTypeToName(String type) {
        if (Constants.EDU_INFO.equals(type)) {
            return "教育资讯";
        }

        if (Constants.PAY_NOTICE.equals(type)) {
            return "订单催缴";
        }
        return "";
    }

    public static MemberDetailBean sortUserList(MemberDetailBean detail) {
        //班级排序，班主任的班级排前面
        if (detail != null) {
            if (detail.getClassList() != null) {
                List<NewClasses> newClassesListC = detail.getClassList();
                for (int i = 0; i < newClassesListC.size(); i++) {
                    for (int j = 0; j < newClassesListC.size() - 1 - i; j++) {
                        int a = Integer.parseInt(newClassesListC.get(j).getIsAdviser());
                        int b = Integer.parseInt(newClassesListC.get(j + 1).getIsAdviser());
                        if (a < b) {//降序，1排前面   0排后面
                            NewClasses temp = newClassesListC.get(j);
                            newClassesListC.set(j, newClassesListC.get(j + 1));
                            newClassesListC.set(j + 1, temp);
                        }
                    }
                }
            }
        }
        return detail;
    }

    public static boolean isAPKDebugMode(Context mContext) {
        try {
            ApplicationInfo info = mContext.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ProgressDialog showProcess(Context context) {
        pd = new ProgressDialog(context);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.show();
        pd.setContentView(R.layout.progress_bar);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.getWindow().setBackgroundDrawableResource(R.color.transparent);
        return pd;
    }

    public static int getAllModelRemind() {
        int count = 0;
        ModelRemindList remind = CCApplication.getInstance().getModelRemindList();
        if (remind != null) {
            List<ModelRemind> list = remind.getMessageList();
            if (list != null) {
                for (ModelRemind item : list) {
                    //班级动态,校园新闻，教育资讯另外统计
                    if (!Constants.CLASSDYN.equals(item.getType())
                            && !Constants.SCHOOL_NEWS.equals(item.getType())
                            && !Constants.EDU_INFO.equals(item.getType())) {
                        String temp = item.getCount();
                        count += Integer.parseInt(temp);
                    }
                }
            }
        }
        return count;
    }

    public static int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        NewMessageListBean AllMessageList = CCApplication.getInstance().getMessageList();
        if (AllMessageList != null) {
            List<NewMessageBean> messageList = AllMessageList.getMessageList();
            if (messageList != null) {
                for (NewMessageBean message : messageList) {
                    if ("chat".equals(message.getMessageType())) {
                        String count = message.getCount();
                        int num = 0;
                        if (count != null) {
                            num = Integer.parseInt(count);
                        }
                        unreadMsgCountTotal += num;
                    }
                }
            }
        }
        int newMessageCount = getAllModelRemind();
        unreadMsgCountTotal += newMessageCount;
        return unreadMsgCountTotal;
    }

    public static boolean isFunctionEnable(String name) {
        if (name != null) {
            MenuConfigBean config = CCApplication.getInstance().getMenuConfig();
            if (config != null) {
                List<MenuConfigBean.MenuConfig> list = config.getPersonalConfigList();
                if (list != null) {
                    for (MenuConfigBean.MenuConfig item : list) {
                        if (item != null && name.equals(item.getFunctionName())) {
                            if (item.getStatus() == MenuItem.INVALID) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }


    /**
     * 保存bitmap对象到本地
     *
     * @param bitmap   bitmap
     * @param filePath 存储路径
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, String filePath) {
        FileOutputStream bos = null;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            bos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 删除文件
     *
     * @param directory file目录
     */
    public static void deleteDir(File directory) {
        if (directory != null) {
            if (directory.isFile()) {
                directory.delete();
                return;
            }
            if (directory.isDirectory()) {
                File[] childFiles = directory.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    directory.delete();
                    return;
                }

                for (int i = 0; i < childFiles.length; i++) {
                    deleteDir(childFiles[i]);
                }
                directory.delete();
            }
        }
    }


    /**
     * 判断是否在有效期内
     *
     * @param validTime 有效期
     * @return
     */
    public static boolean judgeValidFromServer(String validTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date serverData;
            //系统当前时间
            if (DataUtil.isNetworkAvailable(CCApplication.applicationContext)) {
                String server_date = CCApplication.getInstance().getServer_date();
                serverData = sdf.parse(server_date);
            } else {
                serverData = new Date();
            }
            Date validData = sdf.parse(validTime);
            //如果服务器时间  早于有效期  表示有效
            if(serverData.before(validData)){
                return  true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将毫秒数转化为字符串.
     *
     * @param longTime 时间毫秒数
     * @return String 格式化后的文本
     */
    public static String getStringDateFromLong(long longTime) {
        Date date = new Date(longTime*1000);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    /**
     * date类型转换为String类型
     * @param data Date类型的时间
     * @param formatType formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     */
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }


    /**
     * long类型转换为String类型
     * @param currentTime currentTime要转换的long类型的时间
     * @param formatType formatType要转换的string类型的时间格式
     * @throws ParseException
     */
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * String类型转换为date类型
     * @param strTime strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日HH时mm分ss秒，
     * @param formatType strTime的时间格式必须要与formatType的时间格式相同
     */
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }


    /**
     * long转换为Date类型
     * @param currentTime currentTime要转换的long类型的时间
     * @param formatType formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
     */
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }


    /**
     * string类型转换为long类型
     * @param strTime strTime要转换的String类型的时间 strTime的时间格式和formatType的时间格式必须相同
     * @param formatType formatType时间格式
     */
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * date类型转换为long类型
     * @param date date要转换的date类型的时间
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 判断是否是本周
     *
     * @param time
     * @return
     */
    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

}
	


