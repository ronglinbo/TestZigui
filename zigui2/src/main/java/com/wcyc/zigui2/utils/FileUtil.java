package com.wcyc.zigui2.utils;

import android.net.Uri;
import android.os.Environment;

import com.wcyc.zigui2.core.CCApplication;

import java.io.File;

/**
 * Created by Administrator on 2018/5/25.
 */

public class FileUtil {


    public static String ZIGUI_Photos=Environment.getExternalStorageDirectory() + "/ZIGUI_Photos/";
//    public static String IMAGE_FILE_DIR=Environment.getExternalStorageDirectory() + "/" + "ZIGUI_Photos_temporarily";



    //获得临时保存图片的Uri，用当前的毫秒值作为文件名
    public static Uri getTmpUri() {
        File dir = new File(ZIGUI_Photos);
        File file = new File(ZIGUI_Photos, Long.toString(System.currentTimeMillis()));
        //非常重要！！！如果文件夹不存在必须先手动创建
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return Uri.fromFile(file);
    }

    /**
     * 删除栽剪临时保存的文件
     */
    public static void deleteTmpUriFile(){
        File dir = new File(ZIGUI_Photos);
        deleteDirWihtFile(dir);
    }

    /**
     *删除栽剪临时保存的文件
     */
    public static void deleteZiguiPhotosFile(){
        File dir = new File(ZIGUI_Photos);
        deleteDirWihtFile(dir);
    }

    /**
     *   删除文件夹下所以的文件
     * @param file 要删除的文件夹的所在位置
     */
    private static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
//            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }



    /**
     *   删除文件夹下所以的文件
     * @param dir 要删除的文件夹的所在位置
     */
    private static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
//        dir.delete();// 删除目录本身
    }


}
