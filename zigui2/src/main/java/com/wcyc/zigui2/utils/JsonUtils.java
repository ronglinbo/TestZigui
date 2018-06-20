/*
* 文 件 名:JsonUtils.java
* 创 建 人： 姜韵雯
* 日    期： 2014-09-23 
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//2014-9-23
/**
 * JSON工具类
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public class JsonUtils {
    /** 默认的 {@code JSON} 日期/时间字段的格式化模式。 */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
     * 
     * @param json 给定的 {@code JSON} 字符串。
     * @param clazz 类模板。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.01
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat(DEFAULT_DATE_PATTERN);
        Gson gson = builder.create();
        return gson.fromJson(json, clazz);
    }
    
    public static <T> List<T> fromList(String json, Type type) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat(DEFAULT_DATE_PATTERN);
        Gson gson = builder.create();
        return gson.fromJson(json, type);
    }
    
    /**
     * 
     * 函数名称: parseData
     * 函数描述: 将json字符串转换为map
     * @param data
     * @return
     */
    public static Map<String, Object> parseData(String data,Type type){
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        Map<String, Object> map = g.fromJson(data, type); 
        return map;
    }
    
    
}
