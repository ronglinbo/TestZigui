package com.wcyc.zigui2.newapp.module.charge2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.StudentBaseVipServices;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.DateUtils;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * vip所有服务adapter
 * @author 郑国栋
 * 2016-8-5
 * @version 2.0
 */
public class StudentBaseVipServicesAdapter extends BaseAdapter{
	private Context myContext;// 上下文
	private List<StudentBaseVipServices> studentBaseVipServicesList;// 上下文
	private List<SysProductListInfo.Productlist> studentBaseVipServicesList1;// 上下文
	public StudentBaseVipServicesAdapter(Context myContext,
										 List<SysProductListInfo.Productlist> studentBaseVipServicesList) {
		super();
		this.myContext = myContext;
		this.studentBaseVipServicesList1 = studentBaseVipServicesList;
	}
	
	@Override
	public int getCount() {
		if (studentBaseVipServicesList1 != null) {
			return studentBaseVipServicesList1.size();// 长度
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
					R.layout.student_base_vip_services_item, parent, false);

			// 获得布局中的控件
			viewholder.service_name = (TextView) convertView
					.findViewById(R.id.service_name);
			viewholder.charge_duedate = (TextView) convertView
					.findViewById(R.id.charge_duedate);
			viewholder.service_name_iv = (ImageView) convertView
					.findViewById(R.id.service_name_iv);

			// 设置标签
			convertView.setTag(viewholder);

		} else {
			// 获得标签 如果已经实例化则用历史记录
			viewholder = (ViewHolder) convertView.getTag();
		}

		String serviceName=studentBaseVipServicesList1.get(position).getServiceName();
		viewholder.service_name.setText(serviceName);

		int serviceId=studentBaseVipServicesList1.get(position).getServiceId();
		if (serviceId==1) {//"基础信息服务" 个性服务
			viewholder.service_name_iv.setImageResource(R.drawable.icon_xinxifuwu);
		} else if (serviceId==2) {//"微课网（中学资源）
			viewholder.service_name_iv.setImageResource(R.drawable.icon_weikewang);
		} else if (serviceId==6||serviceId==7||serviceId==8) {//子贵课堂（中学资源）
			viewholder.service_name_iv.setImageResource(R.drawable.icon_ziguiketang);
		} else if (serviceId==4) {//"子贵探视"
			viewholder.service_name_iv.setImageResource(R.drawable.icon_tanshi);
		} else if (serviceId==5) {//"小学资源" 小学宝
			viewholder.service_name_iv.setImageResource(R.drawable.icon_xiaoxue);
		} else {
			viewholder.service_name_iv.setImageResource(R.drawable.icon_xinxifuwu);
		}
		
		String endDate=studentBaseVipServicesList1.get(position).getEndDate()+"";
		if(DataUtil.isNullorEmpty(endDate)){
			viewholder.charge_duedate.setText("未开通");
		}else{
			try {
//
//				Date nowdate=new Date(Long.parseLong(endDate));
//				endDate=DateUtils.getInstance().getDataString_2(nowdate);
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
//				Date d = sdf.parse(endDate);

			   int flag=	studentBaseVipServicesList1.get(position).getOverdue();
				if(flag==1){
					viewholder.charge_duedate.setText("已过期");
				}else if(flag==0){
					viewholder.charge_duedate.setText(endDate+"到期");
				}
			}catch (Exception e){

			}
		}
		
		return convertView;
	}

	private class ViewHolder {
		//
		TextView service_name, charge_duedate;
		ImageView service_name_iv;
	}
	
}
