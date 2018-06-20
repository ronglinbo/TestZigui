/*
 * 文 件 名:MenuAdapter.java
 * 创 建 人： xiehua
 * 日    期： 2016-02-17
 * 版 本 号： 1.00
 */
package com.wcyc.zigui2.newapp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.activity.HomeActivity;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.ModelRemindList;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;

import com.wcyc.zigui2.newapp.bean.UserServiceInfo;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.bean.ModelRemindList.ModelRemind;

import com.wcyc.zigui2.newapp.module.charge2.NewRechargeProductActivity;
import com.wcyc.zigui2.newapp.module.email.MenuConfigBean;
import com.wcyc.zigui2.utils.ApiManager;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.DateUtils;
import com.wcyc.zigui2.utils.GoHtml5Function;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.CustomDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import okhttp3.Headers;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuAdapter extends BaseAdapter implements HttpRequestAsyncTaskListener{

	ArrayList<HashMap<String,Object>> list;
	List<MenuItem> items;
	Context mContext;
	private boolean quickPulish = false;
	private CustomDialog dialog;
	private String type;
	private int pos;
	
	String []oa = {"通知","待办事项","业务办理","工资条"
			,"日志","总结","课程表","值班查询"
			,"消费","校长信箱","校历","作息时间"
			,"邮件","作业","考勤","点评","成绩","子贵探视","班级动态"};

	String []res = {"年级套餐","同步学习","同步试题","推荐课程"
			,"同步练习","专项练习","在线课程","口算王","听写助手","汉语大词典","汉字笔画"
			,"家长头条","子贵课堂"};
	
	String []func = {"作业","考勤","点评","值班查询"};

	//快捷发布菜单对应模块
	String [][]quick_publish = {{MenuItem.QUICK_SCHOOLMAIL,MenuItem.SCHOOLMAIL},
								{MenuItem.QUICK_LEAVE,MenuItem.LEAVE}};
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(items != null){
			return items.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if(arg1 == null){
			holder = new ViewHolder();
			if(quickPulish){
				arg1 = LayoutInflater.from(mContext).inflate(R.layout.quick_service_menu_item,arg2,false);
			}else{
				arg1 = LayoutInflater.from(mContext).inflate(R.layout.new_service_menu_item,arg2,false);
				holder.numTv = (TextView) arg1.findViewById(R.id.unread_num);
				holder.unRead = (ImageView) arg1.findViewById(R.id.unread_msg);
			}
			holder.imageView = (ImageView) arg1.findViewById(R.id.imageView);
			holder.textView = (TextView) arg1.findViewById(R.id.textView);
			
			holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
			holder.imageView.setPadding(2, 2, 0, 0);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		
//		final HashMap<String,Object> item = list.get(arg0);
		final MenuItem item = items.get(arg0);
		if(item != null){
			holder.imageView.setImageResource(item.getResId());
			holder.textView.setText(item.getItemName());
		}
		if(!quickPulish){
			int unreadNum = item.getUnreadNum();
			if(unreadNum > 0){
				if("班级动态".equals(item.getItemName())
						||"校园新闻".equals(item.getItemName())) {
					if(holder.unRead != null){
						holder.unRead.setVisibility(View.VISIBLE);
					}
				}else {
					if (holder.numTv != null) {
						holder.numTv.setText(unreadNum + "");
						holder.numTv.setVisibility(View.VISIBLE);
					}
				}
			}else{
				holder.numTv.setVisibility(View.GONE);
			}
		}
		arg1.setTag(holder);
		
		View view = arg1.findViewById(R.id.item);
		if(view != null){
			view.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					handleClick(item, arg0);
				}
				
			});
		}
		return arg1;
	}

	//快捷发布菜单对应模块
	private String convertFunctionName(String name){
		for(String [] item:quick_publish){
			if(item[0].equals(name)){
				return item[1];
			}
		}
		return name;
	}
    private  MenuItem menuItem=null;//记录当前点击的item
	private  int posintion=-1;//记录当前点击的item 的位置
	public static  int b=-1;
	private void handleClick(MenuItem item,int arg0){
		menuItem=item;
		posintion=arg0;
		final Intent intent = new Intent();
		Class<?> cls = item.getClassName();
		String functionName = item.getItemName();
		functionName = convertFunctionName(functionName);

		if(!DataUtil.isNullorEmpty(functionName)){

			boolean ret = CCApplication.app.CouldFunctionBeUseFromConfig(functionName,b);

			if(ret == true){
				type = item.getItemType();
				System.out.println("type:"+type);
				if(!quickPulish){
					int unreadNum = item.getUnreadNum();
					String function = item.getItemName();
					if(unreadNum > 0&&CCApplication.app.couldClearRemind(function)){
						pos = arg0;
						clearRemind(type);
					}
				}
				if(cls != null){
					if(quickPulish){
						intent.putExtra("quickPublish",true);
					}
					intent.setClass(mContext,cls);
					mContext.startActivity(intent);
				}else{
					String function = item.getItemName();
					GoHtml5Function.goToHtmlApp(mContext,function);
				}
			}else{

					chargePop(functionName);//已过期

//				if(b==2){
//					//从未购买过
//					popShiYong(functionName);
//				}


			}
		}
	}
	//弹出充值提示框
	public void chargePop(String name){
		dialog = new CustomDialog(mContext, R.style.mystyle,
				R.layout.customdialog, handler,name);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		if(isExist(name,res)){
			dialog.setTitle("暂未开通此套餐");
			dialog.setContent("立即开通使用?");
		}else{
			dialog.setTitle(mContext.getResources().getString(R.string.vip));
			dialog.setContent(mContext.getResources().getString(R.string.vip_tip));
		}
	}
	//弹出激活试用
	public void popShiYong(String name){
		dialog = new CustomDialog(mContext, R.style.mystyle,
				R.layout.customdialog, handler1,name);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

			dialog.setTitle("你有15天产品试用期");
			dialog.setContent("立即开始使用?");

	}
	
	private boolean isExist(String name,String[] funcs){
		for(String str:funcs){
			if(str.equals(name)){
				return true;
			}
		}
		return false;
	}

	Handler handler1 = new Handler() {
		public void dispatchMessage(android.os.Message msg) { //处理激活试用跳转逻辑
			super.dispatchMessage(msg);
			switch (msg.what) {
				case CustomDialog.DIALOG_CANCEL:// 取消退出
					dialog.dismiss();
					break;
				case CustomDialog.DIALOG_SURE:// 确认试用
					//激活试用 调接口
					Bundle data = msg.getData();
					String name = data.getString("para"); //激活试用的
				    //向服务器激活
					jihuoShiyong(CCApplication.getInstance().getIntegerServiceKind(name)+""); //  2.个性服务

					dialog.dismiss();
					break;
				default:
					break;
			}
		}
	};
	private  int action=-1;
	private final  int  ACTIVE_PODUCT_SERVICE=1;
	private  void jihuoShiyong(String productCode){
		JSONObject json = new JSONObject();
		UserType user = CCApplication.getInstance().getPresentUser();
		if(user != null) {
			try {
		        json.put("judgeFlag", 1+"");
				json.put("studentId", user.getChildId());
				json.put("schoolId",user.getSchoolId());
				json.put("productCode","0"+productCode);
				System.out.println("激活试用出参:"+json.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		new HttpRequestAsyncTask(json,this,mContext,true).execute(Constants.ACTIVE_PODUCT_SERVICE);
		action=ACTIVE_PODUCT_SERVICE;
	}

	private void startIntent() {
		Intent  intent=new Intent();
		type = menuItem.getItemType();
		System.out.println("type:"+type);
		if(!quickPulish){
			int unreadNum = menuItem.getUnreadNum();
			String function = menuItem.getItemName();
			if(unreadNum > 0&&CCApplication.app.couldClearRemind(function)){
				pos = posintion;
				clearRemind(type);
			}
		}
		if(menuItem.getClassName() != null){
			if(quickPulish){
				intent.putExtra("quickPublish",true);
			}
			intent.setClass(mContext,menuItem.getClassName());
			mContext.startActivity(intent);
		}else{
			String function = menuItem.getItemName();
			GoHtml5Function.goToHtmlApp(mContext,function);
		}
	}

	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) { //处理充值中心业务
			super.dispatchMessage(msg);
			switch (msg.what) {
			case CustomDialog.DIALOG_CANCEL:// 取消退出
				dialog.dismiss();
				break;
			case CustomDialog.DIALOG_SURE:// 确认开通or确认试用
				//充值
				Bundle data = msg.getData();
				String name = data.getString("para");
				Intent intent = new Intent(mContext,NewRechargeProductActivity.class);
				intent.putExtra("module",name);
				mContext.startActivity(intent);
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	ProgressDialog pd;
	public void dismissPd(){
		if(pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}
	public void showProgessBar(){
		pd = new ProgressDialog(mContext.getApplicationContext());
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(true);
		pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				//cancel(true);
				dismissPd();

			}
		});
		pd.show();
		pd.setContentView(R.layout.progress_bar);
		pd.getWindow().setGravity(Gravity.CENTER);
		pd.getWindow().setBackgroundDrawableResource(R.color.transparent);


	}
	
	private class ViewHolder{
		ImageView imageView;
		ImageView unRead;
		TextView textView;
		TextView numTv;
	}
	
	public MenuAdapter(Context mContext,ArrayList<HashMap<String,Object>> list){
		this.mContext = mContext;
		this.list = list;
	}
	
	public MenuAdapter(Context mContext,List<MenuItem> list){
		this.items = list;
		this.mContext = mContext;
	}
	
	public MenuAdapter(Context mContext,List<MenuItem> list,boolean quickPulish){
		this.items = list;
		this.mContext = mContext;
		this.quickPulish = quickPulish;
	}
	
	public MenuAdapter(Context mContext,ArrayList<HashMap<String,Object>> list,boolean quickPulish){
		this.mContext = mContext;
		this.list = list;
		this.quickPulish = quickPulish;
	}
	//能够删除模块所有未读数
	public boolean couldClearRemind(String typeName){
		String []func = {"作业","考勤","点评","值班查询","校长信箱"};
		for(String str:func){
			if(typeName.equals(str)){
				return true;
			}
		}
		return false;
	}
	//删除模块已读记录
	private void clearRemind(String type){
		JSONObject json = new JSONObject();
		UserType user = CCApplication.getInstance().getPresentUser();
		if(user != null){
			try {
				json.put("userId", user.getUserId());
				json.put("userType",user.getUserType());
				json.put("modelType",type);
				if(CCApplication.getInstance().isCurUserParent()){
					json.put("studentId", user.getChildId());
				}
				System.out.println("MenuAdapter clearRemind:"+json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new HttpRequestAsyncTask(json, this, mContext).execute(Constants.DEL_MODEL_REMIND);
		}
	}


	@Override
	public void onRequstComplete(String result) {

		switch (action){
			case ACTIVE_PODUCT_SERVICE:
				System.out.println(result);
				UserServiceInfo	 config= JsonUtils.fromJson(result,UserServiceInfo.class);
				if(config != null && config.getServerResult() != null ){
					if(config.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
						   //handleResponse(config); 处理结果
						//激活成功处理逻辑
					UserServiceInfo.UserService	userService=config.getUserServie();
						if(userService.getFlag()==1){
                          //激活成功
                           // 直接跳转进去服务页面
						    DataUtil.getToast("激活试用成功");
                            startIntent();

						}else{


						}
					//	dismissPd();
					}else{
						DataUtil.getToast(config.getServerResult().getResultMessage());
					}
				}
				break;
			default:
				// TODO Auto-generated method stub
				System.out.println("clear remind:"+result);
				NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
				if(ret.getServerResult().getResultCode() == Constants.SUCCESS_CODE){
					ModelRemindList remind = CCApplication.getInstance().getModelRemindList();
					if(remind != null){
						DataUtil.ClearModelRemind(type);
//				this.list.get(pos).put("unread", 0);
						items.get(pos).setUnreadNum(0);
						notifyDataSetChanged();
						((HomeActivity) mContext).updateUnreadLabel();
					}
				}
				break;

		}

	}


	@Override
	public void onRequstCancelled() {
		// TODO Auto-generated method stub
		
	}
}