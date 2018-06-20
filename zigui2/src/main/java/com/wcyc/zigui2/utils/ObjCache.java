/*
* 文 件 名:ObjCache.java
* 创 建 人： 姜韵雯
* 日    期： 2014-09-23 
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
//2014-9-23
/**
 * 缓存对象文件流.
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public class ObjCache {
    
    /**
     * 创建缓存对象
     * @param obj 缠存对象
     * @param cacheFile 缓存绝对路径，包含文件名称 
     */
    public static void create(Object obj, String cacheFile) {
        File file = new File(cacheFile);
        ObjectOutputStream oos = null;
        if (!file.exists()) {
            File cacheDir = new File(file.getParent());
            if(!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(obj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 读取缓存象
     * @param cacheFile
     * @return
     */
    public static Object get(String cacheFile){
        File file = new File(cacheFile);
        ObjectInputStream ois = null;
        if (null == file || !file.exists()) {
            return null;
        }
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            return ois.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
