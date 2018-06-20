package com.wcyc.zigui2.newapp.module.charge2;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import com.wcyc.zigui2.R;

import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge.ChargeProduct.Product;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.SchoolProducts;
import com.wcyc.zigui2.utils.DataUtil;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
//套餐充值时长
public class ProductInfoAdapter extends BaseAdapter{
	private List<SchoolProducts> list;
	private List<ProductInfo.ProductTime> productTimes;

	private Context mContext;
	private HashMap<String,Boolean> isSelected = new HashMap<String,Boolean>();
	
	public ProductInfoAdapter(Context mContext,List<ProductInfo.ProductTime> productTimes){
		this.productTimes = productTimes;
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(productTimes != null){
			return productTimes.size();
		}
		return 0;
	}
	public void setSelected(HashMap<String,Boolean> isSelected){
		this.isSelected = isSelected;
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(productTimes != null){
			return productTimes.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		ProductInfo.ProductTime info = productTimes.get(arg0);
		String startDate = null;
		String ret[];
		if(arg1 == null){
			
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.product_info_item, null);
			holder.date = (TextView) arg1.findViewById(R.id.date);
			holder.price = (TextView) arg1.findViewById(R.id.price);
			holder.check = (RadioButton) arg1.findViewById(R.id.check);
			
//			ret = DataUtil.computeStartDate(mContext);
//			startDate = ret[0];
			String months = info.getValidityDateValue();
			String name = info.getProductName();
			holder.date.setText(name);
			if(!DataUtil.isNullorEmpty(months)){
				int due = Integer.parseInt(months);
				long amount = info.getActualAmount();
				long originAmount = info.getAmount();
				
				String actual ="";
				if((amount% 100)!=0){

					DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
					actual = decimalFormat.format((float)amount/100);//format 返回的是字符串
				}else{
					actual=amount/100+".00";
				}
				holder.price.setText("¥"+actual);
				String origin="";
                if((originAmount% 100)!=0){
					DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
					origin = decimalFormat.format((float)originAmount/100);//format 返回的是字符串
				}else{
					origin=originAmount/100+".00";
				}
				holder.originPrice = (TextView) arg1.findViewById(R.id.origin_price);
				holder.originPrice.setText("原价"+origin+"元");
				//设置中划线并加清晰
				holder.originPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);  
			}
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
//		arg1.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View view) {
//				// TODO Auto-generated method stub
//				for(String key:isSelected.keySet()){
//					isSelected.put(key,false);
//				}
//				isSelected.put(String.valueOf(arg0), true);
//				notifyDataSetChanged();
//			}
//			
//		});
		boolean checked = getCheckStatus(arg0);
		holder.check.setChecked(checked);
		return arg1;
	}
	
	private boolean getCheckStatus(int pos){
		boolean ret = true;
		String key = String.valueOf(pos);
		if(isSelected != null){
			if(isSelected.get(key)== null
					||isSelected.get(key)== false){
				ret = false;
				isSelected.put(key, false);
			}
		}	
		return ret;
	}
	

	private static class ViewHolder {
		TextView name,price,date,originPrice;
		RadioButton check;
	}
}