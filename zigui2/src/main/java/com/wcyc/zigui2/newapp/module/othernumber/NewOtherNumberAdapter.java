package com.wcyc.zigui2.newapp.module.othernumber;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.listener.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.CustomDialog;

import org.json.JSONObject;

import java.util.List;

/**
 * 副号adapter
 * @author 郑国栋
 * 2016-9-25
 * @version 2.06
 */
public class NewOtherNumberAdapter extends BaseAdapter {

	private Context myContext;// 上下文
	private List<NewOtherUserBean> newOtherNumberList;
	private String schoolId;
	private BaseActivity baseActivity;
	private CustomDialog dialog;
	private int position_i=0;


	public NewOtherNumberAdapter(Context myContext, List<NewOtherUserBean> newOtherNumberList, String schoolId, BaseActivity baseActivity) {
		super();
		this.myContext = myContext;
		this.newOtherNumberList = newOtherNumberList;
		this.schoolId=schoolId;
		this.baseActivity=baseActivity;
	}
	
	@Override
	public int getCount() {
		if (newOtherNumberList != null) {
			return newOtherNumberList.size();// 长度
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		if (convertView == null) {
			// 实例化控件
			viewholder = new ViewHolder();
			// 配置单个item的布局
			convertView = LayoutInflater.from(myContext).inflate(
					R.layout.new_other_number_item, parent, false);
//			// 获得布局中的控件
			viewholder.name_relation_tv = (TextView) convertView
					.findViewById(R.id.name_relation_tv);
			viewholder.number_tv = (TextView) convertView
					.findViewById(R.id.number_tv);
			viewholder.other_number_right_tv = (Button) convertView
					.findViewById(R.id.other_number_right_tv);

			// 设置标签
			convertView.setTag(viewholder);

		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}

		String name_relation_tv=newOtherNumberList.get(position).getRelationType();
		String number_tv=newOtherNumberList.get(position).getMobile();

		viewholder.name_relation_tv.setText(name_relation_tv);
		viewholder.number_tv.setText(number_tv);

		final int position_a=position;
		viewholder.other_number_right_tv.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
					position_i=position_a;
					exitOrNot();//二次确认
				}
		});

		return convertView;
	}

	private void exitOrNot(){
//		String phone=CCApplication.getInstance().getPhoneNum();
		String phone=newOtherNumberList.get(position_i).getMobile();
		dialog = new CustomDialog(myContext, R.style.mystyle,
				R.layout.customdialogzgd, handler);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
//		dialog.setTitle("删除副号");
		dialog.setContent("您确定要删除副号"+phone+"？");
	}

	/**
	 * 控制CustomDialog按钮事件.
	 */
	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			super.dispatchMessage(msg);
			if (0 != msg.arg1) {
			}
			switch (msg.what) {
				case CustomDialog.DIALOG_CANCEL:// 取消退出
					dialog.dismiss();
					break;
				case CustomDialog.DIALOG_SURE:// 确认退出
					try {
						String childId = CCApplication.getInstance().getPresentUser().getChildId();
						JSONObject json = new JSONObject();
						json.put("childId", childId);
						json.put("deleteMobile", newOtherNumberList.get(position_i).getMobile());
						json.put("relationTypeCode", newOtherNumberList.get(position_i).getRelationTypeCode());
						json.put("relationType", newOtherNumberList.get(position_i).getRelationType());
						json.put("schoolId", schoolId);
//						System.out.println("删除副号入参===="+json);

						//改为异步的
//						baseActivity.queryPost(http_url_parentMobileDelete,json);
//						myContext.queryPost(http_url_parentMobileDelete, json);
//						new HttpRequestAsyncTask(json,NewOtherNumberAdapter.this,myContext).execute(http_url_parentMobileDelete);

						String url = new StringBuilder(Constants.SERVER_URL).append(
								Constants.PARENT_MOBILE_DALETE).toString();//
						long a=System.currentTimeMillis();
						String result = HttpHelper.httpPostJson(myContext, url, json);
						System.out.println("删除副号出参===="+result);
						long b=System.currentTimeMillis();
						System.out.println("删除副号出参==时间=="+(b-a));
						NewBaseBean ret = JsonUtils.fromJson(result, NewBaseBean.class);
						if (ret.getServerResult().getResultCode() != 200) {// 请求失败
							DataUtil.getToast(ret.getServerResult().getResultMessage());
						} else {
							DataUtil.getToast("删除副号成功");
							Intent broadcast = new Intent(NewOtherNumberActivity.INTENT_REFRESH_DATA_Other_Number);
							myContext.sendBroadcast(broadcast);
						}

					}catch (Exception e) {
						e.printStackTrace();
					}
					dialog.dismiss();
					break;
				default:
					break;
			}
		};
	};

	private class ViewHolder {
		TextView name_relation_tv, number_tv;
		Button other_number_right_tv;
	}
	
}
