package com.wcyc.zigui2.utils;

import java.io.File;

/**
 * Created by 章豪 on 2017/9/28.
 */

public class JNIUtil {
//    //每次进入app，遍历assets目录下所有的文件，是否在data/data目录下都已经存在，不存在则拷贝
//    private void initAssetsFile() {
//
//        boolean needCopy = false;
//
//        // 创建data/data目录
//        File file = getApplicationContext().getFilesDir();
//        String path = file.toString() + "/armeabi/";
//
//        // 遍历assets目录下所有的文件，是否在data/data目录下都已经存在
//        try {
//            String[] fileNames = getApplicationContext().getAssets().list("armeabi");
//            for (int i = 0; fileNames != null &amp;&amp; i &lt; fileNames.length; i++) {
//                if (!TFileUtils.isFileExit(path + fileNames[i])) {
//                    needCopy = true;
//                    break;
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (needCopy) {
//            CommonUtils.copyFilesFassets(getApplicationContext(), "armeabi", path);
//        }
//    }
//
//    //将旧目录中的文件全部复制到新目录
//    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
//        try {
//
//            // 获取assets目录下的所有文件及目录名
//            String fileNames[] = context.getAssets().list(oldPath);
//
//            // 如果是目录名，则将重复调用方法递归地将所有文件
//            if (fileNames.length &gt; 0) {
//                File file = new File(newPath);
//                file.mkdirs();
//                for (String fileName : fileNames) {
//                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
//                }
//            }
//            // 如果是文件，则循环从输入流读取字节写入
//          else {
//                InputStream is = context.getAssets().open(oldPath);
//                FileOutputStream fos = new FileOutputStream(new File(newPath));
//                byte[] buffer = new byte[1024];
//                int byteCount = 0;
//                while ((byteCount = is.read(buffer)) != -1) {
//                    fos.write(buffer, 0, byteCount);
//                }
//                fos.flush();
//                is.close();
//                fos.close();
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    //在需要初始化SDK的地方，指定so文件的路径
//    private void initAPI() {
//        File file = getApplicationContext().getFilesDir();
//        String path = file.toString() + "/armeabi/";
//        EzvizAPI.init(getApplication(), key, path);
//    }
}
