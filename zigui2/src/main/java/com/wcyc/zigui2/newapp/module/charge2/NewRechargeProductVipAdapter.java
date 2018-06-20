package com.wcyc.zigui2.newapp.module.charge2;

import android.content.Context;
import android.os.Bundle;
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
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.StudentBaseVipServices;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 充值中心所有服务adapter
 *
 * @author 郑国栋 2017-01-06
 * @version 2.0.9
 */
public class NewRechargeProductVipAdapter extends BaseAdapter {
    private Context myContext;// 上下文
    private List<ChargeProduct.PackageRoduct> packageRoductList;//
    private List<SysProductListInfo.Schoolallproductlist> schoolallproductlists;//
    private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    private Button recharge_center_bt;
    private List<Integer> isSelectedKey = new ArrayList<Integer>();
    private NewRechargeProductCommonVipAdapter adapter;

    public NewRechargeProductVipAdapter(Context myContext, List<SysProductListInfo.Schoolallproductlist> schoolallproductlists, HashMap isSelected, Button recharge_center_bt, NewRechargeProductCommonVipAdapter adapter) {
        super();
        this.myContext = myContext;
        this.packageRoductList = packageRoductList;
        this.isSelected = isSelected;
        this.recharge_center_bt = recharge_center_bt;
        this.adapter = adapter;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        if (convertView == null) {
            // 实例化控件
            viewholder = new ViewHolder();
            // 配置单个item的布局
            convertView = LayoutInflater.from(myContext).inflate(
                    R.layout.new_recharge_product_vip_item, parent, false);

            // 获得布局中的控件
            viewholder.product_name = (TextView) convertView
                    .findViewById(R.id.service_name);
            viewholder.product_name_iv = (ImageView) convertView
                    .findViewById(R.id.service_name_iv);
            viewholder.base_vip_rl = (RelativeLayout) convertView
                    .findViewById(R.id.base_vip_rl);
            viewholder.product_introduce = (TextView) convertView
                    .findViewById(R.id.product_introduce);
            viewholder.product_radio_button = (CheckBox) convertView
                    .findViewById(R.id.product_radio_button);

            viewholder.view = convertView;
            // 设置标签
            convertView.setTag(viewholder);

        } else {
            // 获得标签 如果已经实例化则用历史记录
            viewholder = (ViewHolder) convertView.getTag();
        }

        final String productName = schoolallproductlists.get(position).getProductName();
        viewholder.product_name.setText(productName);

        int id = schoolallproductlists.get(position).getServiceId();
        int serviceId = id;

//        if (serviceId==1) {//"基础信息服务"
//            viewholder.product_name_iv.setImageResource(R.drawable.icon_xinxifuwu);
//        } else if (serviceId==2) {//"微课网（中学资源）
//            viewholder.product_name_iv.setImageResource(R.drawable.icon_weikewang);
//        } else if (serviceId==3) {//子贵课堂（中学资源）
//            viewholder.product_name_iv.setImageResource(R.drawable.icon_ziguiketang);
//        } else if (serviceId==8) {//"子贵探视"
//            viewholder.product_name_iv.setImageResource(R.drawable.icon_tanshi);
//        } else if (serviceId==16) {//"小学资源"
//            viewholder.product_name_iv.setImageResource(R.drawable.icon_xiaoxue);
//        } else {
//            viewholder.product_name_iv.setImageResource(R.drawable.icon_xinxifuwu);
//        }
        final int final_id = id;
        isSelectedKey.add(id);
        for (int i = 0; i < isSelectedKey.size(); i++) {
            Integer idKey = isSelectedKey.get(i);
            if (isSelected.get(idKey)) {
                viewholder.product_radio_button.setChecked(true);//选中
                recharge_center_bt.setText("下一步");
                buttonEnable(recharge_center_bt, true);
            } else {
                viewholder.product_radio_button.setChecked(false);//未选中
            }
        }
        viewholder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox product_radio_button = (CheckBox) v.findViewById(R.id.product_radio_button);
                Boolean isBoolean = isSelected.get(final_id);
                recharge_center_bt.setText("去支付");
                if (isBoolean) {//如果之前已选中
                    product_radio_button.setChecked(false);//则取消选中
                    isSelected.put(final_id, false);//hashMap设置为false
                    buttonEnable(recharge_center_bt, false);
                } else {
                    recharge_center_bt.setText("下一步");
                    product_radio_button.setChecked(true);
                    isSelected.put(final_id, true);
                    Set<Map.Entry<Integer, Boolean>> set = isSelected.entrySet();
                    Iterator iterator = set.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Integer, Boolean> map = (Map.Entry<Integer, Boolean>) iterator.next();
                        if (map.getKey() != final_id) {
                            isSelected.put(map.getKey(), false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    notifyDataSetChanged();
                    buttonEnable(recharge_center_bt, true);
                }
            }
        });


        viewholder.product_introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String url = Constants.WEBVIEW_URL;
                String title = "产品说明";
                url += Constants.PRODUC_INTRODUCE + "?serviceValue=" + schoolallproductlists.get(position).getServiceId(); //新版
//                if (final_id==1) {//"基础信息服务"
//                    url+=Constants.JCXX_INTRODUCE;
//                } else if (final_id==2) {//"微课网（中学资源）
//                    url+=Constants.WKW_INTRODUCE;
//                } else if (final_id==6||final_id==7||final_id==8) {//子贵课堂（ 初 中 高学资源）
//                    url+=Constants.ZGKT_INTRODUCE;
//                } else if (final_id==8) {//"子贵探视"
//                    url+=Constants.ZGTS_INTRODUCE;
//                } else if (final_id==5) {//"小学资源"
//                    url+=Constants.XXZY_INTRODUCE;
//                } else {
//                    url=null;
//                    title=null;
//                }
//                System.out.println("===title="+title);
                System.out.println("===url=" + url);
                if (!DataUtil.isNullorEmpty(url)) {
                    bundle.putString("title", title);
                    bundle.putString("url", url);
                    ((BaseActivity) myContext).newActivity(BaseWebviewActivity.class, bundle);
                }
            }
        });
        return convertView;
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    private void buttonEnable(Button button, boolean enable) {
        if (enable) {
            button.setClickable(true);
            button.setEnabled(true);
            button.setBackgroundResource(R.drawable.fogetpassworld_btn_send_selector);
        } else {
            button.setClickable(false);
            button.setEnabled(false);
            button.setBackgroundResource(R.drawable.fogetpassworld_btn_send_shape_gray);
        }
    }

    private class ViewHolder {
        View view;
        TextView product_name, product_introduce;
        ImageView product_name_iv;
        RelativeLayout base_vip_rl;
        CheckBox product_radio_button;
    }

}
