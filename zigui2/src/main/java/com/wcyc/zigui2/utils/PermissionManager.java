package com.wcyc.zigui2.utils;

import android.Manifest;

/**
 * 权限常量类
 */
public class PermissionManager {

    //存储权限
    public final static String[] WRITE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //读取权限
    public final static String[] READ = {Manifest.permission.READ_EXTERNAL_STORAGE};

    //定位
    public final static String[] LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION};

    //电话
    public final static String[] CALL = {Manifest.permission.CALL_PHONE};

    //相机
    public final static String[] CAMERA = {Manifest.permission.CAMERA};

    //短信
    public final static String[] SMS = {Manifest.permission.SEND_SMS};

    //通讯录
    public final static String[] CONTACT = {Manifest.permission.READ_CONTACTS};

    //麦克风
    public final static String[] RECORD_AUDIO = {Manifest.permission.RECORD_AUDIO};

    //所有权限
    public final static String[] ALL = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECORD_AUDIO

    };

}
