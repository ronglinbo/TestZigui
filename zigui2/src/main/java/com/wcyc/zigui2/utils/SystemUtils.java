package com.wcyc.zigui2.utils;

import android.content.Context;
import android.os.Debug;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2018/5/22.
 */

public class SystemUtils {

     public static String TAG = "SystemUtils";

     /** 判断手机是否root，不弹出root请求框<br/> */
     public static boolean isRoot() {
         String binPath = "/system/bin/su";
         String xBinPath = "/system/xbin/su";
         if (new File(binPath).exists() && isExecutable(binPath))
                return true;
         if (new File(xBinPath).exists() && isExecutable(xBinPath))
                return true;
         return false;
     }

     private static boolean isExecutable(String filePath) {
         Process p = null;
             try {
                 p = Runtime.getRuntime().exec("ls -l " + filePath);
                 // 获取返回内容
                 BufferedReader in = new BufferedReader(new InputStreamReader(
                               p.getInputStream()));
                 String str = in.readLine();
                 Log.i(TAG, str);
                 if (str != null && str.length() >= 4) {
                      char flag = str.charAt(3);
                      if (flag == 's' || flag == 'x')
                               return true;
                   }
             } catch (IOException e) {
                 e.printStackTrace();
             }finally{
                 if(p!=null){
                       p.destroy();
                   }
             }
         return false;
     }


    /**
     *
     * 检测是否是模拟器
     *
     */


    //Device ID
    private static String[] known_device_ids = {"000000000000000", // Default emulator id
            "e21833235b6eef10", // VirusTotal id
            "012345678912345"};
    public static boolean hasKnownDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String deviceId = telephonyManager.getDeviceId();

        for (String known_deviceId : known_device_ids) {
            if (known_deviceId.equalsIgnoreCase(deviceId)) {
                return true;
            }

        }
        return false;
    }




    //Default Number
    private static String[] known_numbers = {
            "15555215554", // 模拟器默认电话号码 + VirusTotal
            "15555215556", "15555215558", "15555215560", "15555215562", "15555215564", "15555215566",
            "15555215568", "15555215570", "15555215572", "15555215574", "15555215576", "15555215578",
            "15555215580", "15555215582", "15555215584",};
    public static boolean hasKnownPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String phoneNumber = telephonyManager.getLine1Number();

        for (String number : known_numbers) {
            if (number.equalsIgnoreCase(phoneNumber)) {
                return true;
            }

        }
        return false;
    }






    //IMSI
    private static String[] known_imsi_ids = {"310260000000000" // 默认IMSI编号
    };
    public static boolean hasKnownImsi(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();

        for (String known_imsi : known_imsi_ids) {
            if (known_imsi.equalsIgnoreCase(imsi)) {
                return true;
            }
        }
        return false;
    }




    //Build类
    public static boolean hasEmulatorBuild(Context context) {
        String BOARD = android.os.Build.BOARD; // The name of the underlying board, like "unknown".
        // This appears to occur often on real hardware... that's sad
        // String BOOTLOADER = android.os.Build.BOOTLOADER; // The system bootloader version number.
        String BRAND = android.os.Build.BRAND; // The brand (e.g., carrier) the software is customized for, if any.
        // "generic"
        String DEVICE = android.os.Build.DEVICE; // The name of the industrial design. "generic"
        String HARDWARE = android.os.Build.HARDWARE; // The name of the hardware (from the kernel command line or
        // /proc). "goldfish"
        String MODEL = android.os.Build.MODEL; // The end-user-visible name for the end product. "sdk"
        String PRODUCT = android.os.Build.PRODUCT; // The name of the overall product.
        if ((BOARD.compareTo("unknown") == 0) /* || (BOOTLOADER.compareTo("unknown") == 0) */
                || (BRAND.compareTo("generic") == 0) || (DEVICE.compareTo("generic") == 0)
                || (MODEL.compareTo("sdk") == 0) || (PRODUCT.compareTo("sdk") == 0)
                || (HARDWARE.compareTo("goldfish") == 0)) {
            return true;
        }
        return false;
    }




    //运营商名
    public static boolean isOperatorNameAndroid(Context paramContext) {
        String szOperatorName = ((TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
        boolean isAndroid = szOperatorName.equalsIgnoreCase("android");
        return isAndroid;
    }





    //QEMU文件
    private static String[] known_files = {"/system/lib/libc_malloc_debug_qemu.so", "/sys/qemu_trace",
            "/system/bin/qemu-props"};
    /**
     * 检查是否存在已知的QEMU环境文件
     *
     * @return {@code true} if any files where found to exist or {@code false} if not.
     */
    public static boolean hasQEmuFiles() {
        for (String pipe : known_files) {
            File qemu_file = new File(pipe);
            if (qemu_file.exists()) {
                return true;
            }
        }

        return false;
    }






    //Genymotion文件
    private static String[] known_geny_files = {"/dev/socket/genyd", "/dev/socket/baseband_genyd"};
    /**
     * 检查是否存在已知的Genemytion环境文件
     *
     * @return {@code true} if any files where found to exist or {@code false} if not.
     */
    public static boolean hasGenyFiles() {
        for (String file : known_geny_files) {
            File geny_file = new File(file);
            if (geny_file.exists()) {
                return true;
            }
        }

        return false;
    }


    //QEMU管道
    private static String[] known_pipes = {"/dev/socket/qemud", "/dev/qemu_pipe"};
    /**
     * 检查是否存在已知的QEMU使用的管道
     *
     * @return {@code true} if any pipes where found to exist or {@code false} if not.
     */
    public static boolean hasPipes() {
        for (String pipe : known_pipes) {
            File qemu_socket = new File(pipe);
            if (qemu_socket.exists()) {
                return true;
            }
        }

        return false;
    }




    /**
     * 你信或不信, 还真有许多加固程序使用这个方法...
     */
    public static boolean isBeingDebugged() {
        return Debug.isDebuggerConnected();
    }




    public static boolean checkSimulator(Context context){

        int count=0;

        //DeviceId
        if(hasKnownDeviceId(context)){
            Log.i(TAG,"hasKnownDeviceId:true");
            count++;
        }else{
            Log.i(TAG,"hasKnownDeviceId:false");
        }

        //Default Number
        if(hasKnownPhoneNumber(context)){
            count++;
            Log.i(TAG,"hasKnownPhoneNumber:true");
        }else{
            Log.i(TAG,"hasKnownPhoneNumber:false");
        }

        //IMSI
        if(hasKnownImsi(context)){
            count++;
            Log.i(TAG,"hasKnownImsi:true");
        }else{
            Log.i(TAG,"hasKnownImsi:false");
        }

        //Build类
        if(hasEmulatorBuild(context)){
            count++;
            Log.i(TAG,"hasEmulatorBuild:true");
        }else{
            Log.i(TAG,"hasEmulatorBuild:false");
        }

        //运营商名
        if(isOperatorNameAndroid(context)){
            count++;
            Log.i(TAG,"isOperatorNameAndroid:true");
        }else{
            Log.i(TAG,"isOperatorNameAndroid:false");
        }

        //QEMU文件
        if(hasQEmuFiles()){
            count++;
            Log.i(TAG,"hasQEmuFiles:true");
        }else{
            Log.i(TAG,"hasQEmuFiles:false");
        }

        //Genymotion文件
        if(hasGenyFiles()){
            count++;
            Log.i(TAG,"hasGenyFiles:true");
        }else{
            Log.i(TAG,"hasGenyFiles:false");
        }

        if(isBeingDebugged()){
            count++;
            Log.i(TAG,"isBeingDebugged:true");
        }else {
            Log.i(TAG,"isBeingDebugged:false");
        }

        //如果有3个条件通过就判定他是模拟器
        if(count>=3){
            return true;
        }else{
            return false;
        }

    }




















}
