package com.wcyc.zigui2.newapp.module.charge2;


import java.util.HashMap;
import java.util.List;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.PackageRoduct;



import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter{
	private List<PackageRoduct> list;
	private List<SysProductListInfo.Schoolallproductlist> schoolallproductlists;
	private Context mContext;
	private HashMap<String,Boolean> isSelected = new HashMap<String,Boolean>();
	
	public ProductAdapter(Context mContext,List<SysProductListInfo.Schoolallproductlist> schoolallproductlists){
		this.schoolallproductlists = schoolallproductlists;
		this.mContext = mContext;
	}
	
	public void setSelected(HashMap<String,Boolean> isSelected){
		this.isSelected = isSelected;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(schoolallproductlists != null){
			return schoolallproductlists.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(schoolallproductlists != null){
			return schoolallproductlists.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		SysProductListInfo.Schoolallproductlist info = schoolallproductlists.get(arg0);

		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.product_list_item, null);
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.check = (RadioButton) arg1.findViewById(R.id.check);
			holder.image = (ImageView) arg1.findViewById(R.id.image);
//			if(isComboProduct(info.getProductCode())
//					&&info.getDiscount() > 0){
			if(info.getProductName().contains("+")){
				holder.image.setVisibility(View.VISIBLE);
			}
			holder.name.setText(info.getProductName());
			
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		boolean checked = getCheckStatus(arg0);
		holder.check.setChecked(checked);
		return arg1;
	}
	
	private boolean isComboProduct(String code){
		return !("01".equals(code)
				|| "02".equals(code)
				|| "03".equals(code));
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
		TextView name;
		RadioButton check;
		ImageView image;
	}
}