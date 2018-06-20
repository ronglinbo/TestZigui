package com.wcyc.zigui2.newapp.module.charge2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.StudentBaseVipServices;

import java.util.HashMap;
import java.util.List;

/**
 * 充值中心所有服务adapter
 *
 * @author 郑国栋 2017-01-06
 * @version 2.0.9
 */
public class NewProductGridViewAdapter extends BaseAdapter {
    private Context myContext;// 上下文
    private List<ChargeProduct.PackageRoduct>  packageRoductList;
    private List<SysProductListInfo.Schoolallproductlist>  schoolallproductlists;
    public NewProductGridViewAdapter(Context myContext,List<SysProductListInfo.Schoolallproductlist>  schoolallproductlists) {
        super();
        this.myContext = myContext;
        this.schoolallproductlists = schoolallproductlists;
    }

    @Override
    public int getCount() {
        if (schoolallproductlists != null) {
            return schoolallproductlists.size();// 长度
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
                    R.layout.new_product_gridview_item, parent, false);

            // 获得布局中的控件
            viewholder.service_name = (TextView) convertView
                    .findViewById(R.id.service_name);
            viewholder.service_name_iv = (ImageView) convertView
                    .findViewById(R.id.service_name_iv);

            // 设置标签
            convertView.setTag(viewholder);

        } else {
            // 获得标签 如果已经实例化则用历史记录
            viewholder = (ViewHolder) convertView.getTag();
        }

        String serviceName = schoolallproductlists.get(position).getProductName();
        viewholder.service_name.setText(serviceName);

//        int serviceId=packageRoductList.get(position).getId();
        int serviceId=schoolallproductlists.get(position).getServiceId();
        if (serviceId==1) {//"基础信息服务"
            viewholder.service_name_iv.setImageResource(R.drawable.icon_xinxifuwu_s);
        } else if (serviceId==2) {//"微课网（中学资源）
            viewholder.service_name_iv.setImageResource(R.drawable.icon_weikewang_s);
        } else if (serviceId==6||serviceId==7||serviceId==8) {//子贵课堂（中学资源）
            viewholder.service_name_iv.setImageResource(R.drawable.icon_ziguiketang_s);
        } else if (serviceId==8) {//"子贵探视"
            viewholder.service_name_iv.setImageResource(R.drawable.icon_ziguitanshi_s);
        } else if (serviceId==5) {//"小学资源" 小学宝
            viewholder.service_name_iv.setImageResource(R.drawable.icon_xiaoxueziyuan_s);
        } else {
            viewholder.service_name_iv.setImageResource(R.drawable.icon_xinxifuwu_s);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView service_name;
        ImageView service_name_iv;
    }

}
