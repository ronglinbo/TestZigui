package com.wcyc.zigui2.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewMemberBean;
import com.wcyc.zigui2.newapp.bean.UserType;

public class RequestHeader {
	private String userName,schoolId,pwd,deviceID,accId,operatorName,osVersion;
	private Context mContext;
	private UserType user;


	public RequestHeader(Context mContext){
		if(mContext != null) {
			this.mContext = mContext;
		}else{
			this.mContext = CCApplication.getInstance().getApplicationContext();
		}
		user = CCApplication.getInstance().getPresentUser();
		userName = "";
		schoolId = "";
		pwd = "";
		deviceID = "";
		accId = "";
		operatorName = "";
		osVersion = "";
	}
	
	public String getUserName(){
        if(user != null){
	        userName = user.getUserId();
	        schoolId = user.getSchoolId();
        }else{
        	userName = CCApplication.app.getUserName();
        }
		return userName;
	}
	
	public String getUserId(){
		if(user != null){
			return user.getUserId();
		}
		return "";
	}
	
	public String getAccId(){
		NewMemberBean member = CCApplication.app.getMemberInfo();
		if(member != null)
			accId = member.getAccId();
		return accId;
	}
	
	public String getPassword(){
		pwd = CCApplication.app.getPhonePwd();
//			if(pwd != null){
//				return DataUtil.encodeMD5(pwd);
//			}
		return pwd;
	}
	
	public String getSchoolId(){
		if(user != null){
			schoolId = user.getSchoolId();
		}
		return schoolId;
	}
	
	public String getMac(){
		if(mContext != null) {
			WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			if(wifi != null) {
				WifiInfo info = wifi.getConnectionInfo();
				if(info.getMacAddress()!=null){
					return info.getMacAddress();
				}else {
					return "";
				}

			}
		}
		return "";
	}



	public String 		getDeviceId(){
/*		if(mContext == null) return "";
		TelephonyManager telephonyManager =
	             ( TelephonyManager )mContext.getSystemService(Context.TELEPHONY_SERVICE);
	    deviceID = telephonyManager.getDeviceId();
		operatorName = telephonyManager.getSimOperatorName();
		//telephonyManager.getSimOperator();
		Log.e("operatorName",operatorName);
//		System.out.println("deviceID:"+deviceID);
		return deviceID;*/

		if (mContext == null) return "";
		TelephonyManager telephonyManager =
				(TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		deviceID = telephonyManager.getDeviceId();
		operatorName = telephonyManager.getNetworkOperatorName();
		if (deviceID == null) {
			// android pad
			deviceID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
		}
//		System.out.println("deviceID:"+deviceID);
		Log.d("ceshidaima:" , deviceID);
		return deviceID;
	}

	public String getResolution(){
		return getDisplayResolution();
	}	
	
	public String getMobileType(){
		return "Android";
	}
	
	public String getVersion(){
		if(getCurVersion()==null){
		    return "unknown";
		}
		return getCurVersion();
	}
	
//	public String getDisplayResolution() {
//		DisplayMetrics dm = new DisplayMetrics();
//		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
//		String resolution = dm.heightPixels+"*"+dm.widthPixels;
//		return resolution;
//	}
	
	public String getDisplayResolution(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		String resolution = dm.heightPixels+"*"+dm.widthPixels;
		return resolution;
	}
	
	/**
	 * 获取系统本地版本号.
	 * @return 系统本地版本号
	 */
	public String getCurVersion(){ 
		Properties properties = new Properties(); 
		
		try { 
			InputStream stream = mContext.getAssets().open("ver.cfg"); 
			properties.load(stream); 
		} catch (FileNotFoundException e){ 
			return "100"; 
		} catch(IOException e) { 
			return "100"; 
		} catch(Exception e){ 
			return "100"; 
		} 
		
		String version = String.valueOf(properties.get("Version").toString()); 
		
		return version; 
	}

	//设备型号
	public String getDeviceType(){
		String str = Build.MODEL;
		if(str==null){
             return "unknown";
		}
		if(str.length() > 20){
			str = str.substring(0,19);
		}

		return str;
	}

	public String getDeviceBrand(){
       if(Build.MANUFACTURER==null){
		   return "unknown";
	   }
		return Build.MANUFACTURER;
	}

	public String  checkNumberOperatorName(String operatorName ){
		String name="";
		if(operatorName.indexOf("46000")>=0){
			name= "hina Mobile";
		}
		if(operatorName.indexOf("46001")>=0){
			name= "China Unicom";
		}
		if(operatorName.indexOf("46002")>=0){
			name= "hina Mobile";
		}
		if(operatorName.indexOf("46003")>=0){
			return "China telecom";
		}
		if(operatorName.indexOf("46005")>=0){
			name= "China telecom";
		}
		if(operatorName.indexOf("46006")>=0){
			name= "China Unicom";
		}
		if(operatorName.indexOf("46006")>=0){
			name= "China Unicom";
		}
		if(operatorName.indexOf("46007")>=0){
			name="China Mobile";
		}
		if(operatorName.indexOf("46011")>=0){
			name="China telecom";
		}
		return name ;
	}

	public String  checkChatOperatorNname(String operatorName){
		 String name="";
		if(operatorName.indexOf("中国电信")>=0&&operatorName.indexOf("中国移动")>=0){
			 name="China telecom,China Mobile";
			return name;
		}
		if(operatorName.indexOf("中国电信")>=0&&operatorName.indexOf("中国联通")>=0){
			name="China telecom,China Unicom";
			return name;
		}
		if(operatorName.indexOf("中国移动")>=0&&operatorName.indexOf("中国联通")>=0){
			name="China Mobile,China Unicom,";
			return name;
		}
		if(operatorName.indexOf("中國電信")>=0&&operatorName.indexOf("中國移動")>=0){
			name="China telecom,China Mobile";
			return name;
		}
		if(operatorName.indexOf("中國電信")>=0&&operatorName.indexOf("中國聯通")>=0){
			name="China telecom,China Unicom";
			return name;
		}
		if(operatorName.indexOf("中國移動")>=0&&operatorName.indexOf("中國聯通")>=0){
			name="China Mobile,China Unicom,";
			return name;
		}

		if(operatorName.indexOf("中国电信")>=0){
			name="China telecom";
		}
		if(operatorName.indexOf("中国移动")>=0){
			name="China Mobile";
		}
		if(operatorName.indexOf("中国联通")>=0){
			name= "China Unicom";
		}
		if(operatorName.indexOf("中國電信")>=0){
			name="China telecom";
		}
		if(operatorName.indexOf("中國移動")>=0){
			name="China Mobile";
		}
		if(operatorName.indexOf("中國聯通")>=0){
			name= "China Unicom";
		}

		return name;
	}

	/* 根据运营商
	*/
	public static String getTelephoneDetailByOperator(String operatorName){

			if (operatorName != null) {
				if (operatorName.equals("46000") || operatorName.equals("46002")) {
					// 中国移动
					return "China Mobile";
				} else if (operatorName.equals("46001")) {
					// 中国联通
					return "China Unicom";
				} else if (operatorName.equals("46003")) {
					// 中国电信
					return "China telecom";
				}
			}



		    return "unknown2";
	}



	public String getOperatorName1(){
//		int operator = getProviders();
		System.out.println("=运营商=="+operatorName);
		String name="";
		if(DataUtil.isNullorEmpty(operatorName)){
			return "unknown1";
		}


		if(operatorName.equals("46000")){
			name= "hina Mobile";
		}
		if(operatorName.equals("46001")){
			name= "China Unicom";
		}
		if(operatorName.equals("46002")){
			name= "hina Mobile";
		}
		if(operatorName.equals("46003")){
			return "China telecom";
		}
		if(operatorName.equals("46005")){
			name= "China telecom";
		}
		if(operatorName.equals("46006")){
			name= "China Unicom";
		}
		if(operatorName.equals("46006")){
			name= "China Unicom";
		}
		if(operatorName.equals("46007")){
			name="China Mobile";
		}
		if(operatorName.equals("46011")){
			name="China telecom";
		}
		return name ;






	}

	public String getOperatorName(){
//		int operator = getProviders();
		System.out.println("=运营商=="+operatorName);
		if(DataUtil.isNullorEmpty(operatorName)){
			return "unknown1";
		}

		try{
			Long.parseLong(operatorName);//检验是否报错 区分数字和字符

			return checkNumberOperatorName(operatorName); //返回对应的区号 运营商
		}catch (Exception e){
			//字符 中文和英文 中文(有些厂商会乱码)
			String name=checkChatOperatorNname(operatorName);
			if(name.equals("")){
				if(operatorName.length()>20){
					operatorName=operatorName.substring(0,19);
				}
				char c = operatorName.trim().charAt(1);
				if((c >= 0x4e00)&&(c <= 0x9fbb)) {
					System.out.println("是中文");
					return "unknown2";
				}

				return operatorName;
			}else{

				return  name;
			}




		}


//		System.out.println("=运营商=="+operatorName);
//		if(DataUtil.isNullorEmpty(operatorName)){
//			return "unknown";
//		}else if(("中国联通").equals(operatorName)){
//			return "China Unicom";
//		}else if(("中国电信").equals(operatorName)){
//			return "China Telecom";
//		}else if(("中国移动").equals(operatorName)){
//			return "China Mobile";
//		}else {
//			return "unknown";
//		}

//		try {
//			operatorName = URLEncoder.encode(operatorName,"UTF-8");
//			System.out.println("运营商:"+operatorName);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return operatorName;
	}



	public String getOsVersion(){
		osVersion = Build.VERSION.RELEASE;
		if(osVersion==null){
			return "unknown";
		}
		return osVersion;
	}

	public String getDeviceToken(){
		String deviceToken = CCApplication.getInstance().getDeviceToken();
//		System.out.println("deviceToken:"+deviceToken);
		if(deviceToken==null){
			return "unknown";
		}
		return deviceToken;
	}

	public String getNetWork() {
		String NOWNET = null;
		ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			if (info.getTypeName().equals("WIFI")) {
				NOWNET = info.getTypeName();
			} else {
				NOWNET = info.getExtraInfo();// cmwap/cmnet/wifi/uniwap/uninet
			}
		}
		return NOWNET;
	}

	public List<String> getNetWorkList() {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] infos = cm.getAllNetworkInfo();
		List<String> list = new ArrayList<String>();
		if (infos != null) {
			for (int i = 0; i < infos.length; i++) {
				NetworkInfo info = infos[i];
				String name = null;
				if (info.getTypeName().equals("WIFI")) {
					name = info.getTypeName();
				} else {
					name = info.getExtraInfo();
				}
				if (name != null && list.contains(name) == false) {
					list.add(name);
					// System.out.println(name);
				}
			}
		}
		return list;
	}
	//1是移动，2是联通，3是电信
	private int getProviders() {
		String net = getNetWork();
		List<String> infos = getNetWorkList();
		if (net == null || net.equals("WIFI")) {
			if (infos.size() > 1) {
				infos.remove("WIFI");
				net = infos.get(0);
				if (net.equals("3gwap") || net.equals("uniwap")
						|| net.equals("3gnet") || net.equals("uninet")) {
					return 2;
				} else if (net.equals("cmnet") || net.equals("cmwap")) {
					return 1;
				} else if (net.equals("ctnet") || net.equals("ctwap")) {
					return 3;
				}
			} else {
				return 0;
			}
		} else {
			if (net.equals("3gwap") || net.equals("uniwap")
					|| net.equals("3gnet") || net.equals("uninet")) {
				return 2;
			} else if (net.equals("cmnet") || net.equals("cmwap")) {
				return 1;
			} else if (net.equals("ctnet") || net.equals("ctwap")) {
				return 3;
			}
		}
		return 0;
	}
}