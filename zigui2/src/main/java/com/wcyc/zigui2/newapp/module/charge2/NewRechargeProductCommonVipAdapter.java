package com.wcyc.zigui2.newapp.module.charge2;

import android.content.Context;
import android.graphics.Paint;
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
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
public class NewRechargeProductCommonVipAdapter extends BaseAdapter {
    private Context myContext;// 上下文
    private List<ChargeProduct.PackageRoduct> packageRoductList;//

    public List<SysProductListInfo.Schoolallproductlist> getSchoolallproductlists() {
        return schoolallproductlists;
    }

    private List<SysProductListInfo.PurchasedProcutList> purchasedProcutLists;
    private List<SysProductListInfo.Schoolallproductlist> schoolallproductlists;//
    private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    private Button recharge_center_bt;
    private List<Integer> isSelectedKey = new ArrayList<Integer>();
    private NewRechargeProductVipAdapter adapter;
    private int moduleIntegerId;

    public NewRechargeProductCommonVipAdapter(Context myContext, List<SysProductListInfo.Schoolallproductlist> schoolallproductlists, HashMap isSelected, Button recharge_center_bt, NewRechargeProductVipAdapter adapter, List<SysProductListInfo.PurchasedProcutList> purchasedProcutLists, int id) {
        super();
        this.myContext = myContext;
        this.purchasedProcutLists = purchasedProcutLists;
        this.isSelected = isSelected;
        this.recharge_center_bt = recharge_center_bt;
        this.adapter = adapter;
        this.schoolallproductlists = schoolallproductlists;
        this.moduleIntegerId = id;

    }

    @Override
    public int getCount() {
        if (schoolallproductlists != null) {
            return schoolallproductlists.size();// 长度
        }
        return 0;
    }

    boolean isfrist = true;

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
                    R.layout.new_recharge_product_vipcommon_item, parent, false);
            //  TextView taocan_name,service_name,service_date,service_charge,original_cost,product_introduce;
            // 获得布局中的控件
            viewholder.taocan_name = (TextView) convertView
                    .findViewById(R.id.taocan_name); //套餐名稱
            viewholder.service_name = (TextView) convertView
                    .findViewById(R.id.service_name);//服務名稱
            viewholder.service_date = (TextView) convertView
                    .findViewById(R.id.service_date);//服務期限
            viewholder.service_charge = (TextView) convertView
                    .findViewById(R.id.service_charge);//套餐現價
            viewholder.original_cost = (TextView) convertView
                    .findViewById(R.id.original_cost);//原價
            viewholder.product_introduce = (TextView) convertView
                    .findViewById(R.id.product_introduce);//產品説明
            viewholder.im_yigoumai = (ImageView) convertView
                    .findViewById(R.id.im_yigoumai);  //已購買圖片
            viewholder.product_radio_button = (CheckBox) convertView
                    .findViewById(R.id.product_radio_button);//box
            viewholder.view = convertView;
            // 设置标签
            convertView.setTag(viewholder);

        } else {
            // 获得标签 如果已经实例化则用历史记录
            viewholder = (ViewHolder) convertView.getTag();
        }
        //设置现价
        if ((schoolallproductlists.get(position).getActualAmount() % 100) != 0) {
            //不等于0 说明代表有角 分
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format((float) schoolallproductlists.get(position).getActualAmount() / 100);//format 返回的是字符串
            viewholder.service_charge.setText("￥" + p);
        } else {
            //为整数
            viewholder.service_charge.setText("￥" + schoolallproductlists.get(position).getActualAmount() / 100 + ".00");
        }
        // 原价设置中划线并加清晰
        if ((schoolallproductlists.get(position).getAmount() % 100) != 0) {
            //不等于0 说明代表有角 分
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format((float) schoolallproductlists.get(position).getAmount() / 100);//format 返回的是字符串
            viewholder.original_cost.setText("原价:￥" + p);
        } else {
            //为整数
            viewholder.original_cost.setText("原价:￥" + schoolallproductlists.get(position).getAmount() / 100 + ".00");
        }
        viewholder.original_cost.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        //设置套餐名称
        final String productName = schoolallproductlists.get(position).getProductName();
        viewholder.taocan_name.setText(productName);
        viewholder.taocan_name.setSelected(true);
        //设置服务名称
        viewholder.service_name.setText(schoolallproductlists.get(position).getServiceName());
        //设置服期限
        String startdate = schoolallproductlists.get(position).getStartDate();

        String enddate = schoolallproductlists.get(position).getEndDate();

        viewholder.service_date.setText("服务期限" + startdate + "至" + enddate); // 格式 (服务期限2017-02-01至2017-08-31)
        //获取服务产品id
        int id = schoolallproductlists.get(position).getProductCode();
        final String[] values = schoolallproductlists.get(position).getService_value().split(",");
        int serviceId = id;
        //初始化复用view 的状态

        //设置不显示已购买图片
        viewholder.im_yigoumai.setVisibility(View.INVISIBLE);
        //设置未购买可选button
        viewholder.product_radio_button.setButtonDrawable(R.drawable.product_checkbox_style);
        //设置item可点击
        viewholder.view.setClickable(true);
        //设置现价颜色
        viewholder.service_charge.setTextColor(myContext.getResources().getColor(R.color.color_dai_pay));

        //设置已购买状态
        boolean isCharse = false;
        for (SysProductListInfo.PurchasedProcutList item : purchasedProcutLists) {
            if (item.getProductCode() == serviceId) {
                isCharse = true;
                //设置已购买图片
                viewholder.im_yigoumai.setVisibility(View.VISIBLE);
                //设置已购买不可选button
                viewholder.product_radio_button.setButtonDrawable(R.drawable.icon_bukexuan);
                //设置item不可点击
                viewholder.view.setClickable(false);
                //设置现价颜色
                viewholder.service_charge.setTextColor(myContext.getResources().getColor(R.color.color_qianse));
            }
        }


        final int final_id = id;
        //已购买过的就不加进去了
        isSelectedKey.add(id);

        //没有购买做判断
        if (!isCharse) {
            if (isfrist) {
                //统一订购 默认选中逻辑变更
                int record_id = -1;
                for (String ids : values) {
                    int id_ = Integer.parseInt(ids);
                    if (id_ == moduleIntegerId) {
                        record_id = serviceId;
                    }
                }
                if (record_id > 0) {
                    isSelected.put(final_id, true);
                    viewholder.product_radio_button.setChecked(true);//选中
                    isfrist = false;
                } else {
                    viewholder.product_radio_button.setChecked(false);//未选中
                }
            }
            for (int i = 0; i < isSelectedKey.size(); i++) {
                Integer idKey = isSelectedKey.get(i);
                if (isSelected.get(idKey)) {
                    viewholder.product_radio_button.setChecked(true);//选中
                    buttonEnable(recharge_center_bt, true);
                } else {
                    viewholder.product_radio_button.setChecked(false);//未选中
                }
            }
        }


        final boolean finalIsCharse = isCharse;
        viewholder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moduleIntegerId = 0;
                if (!finalIsCharse) {
                    CheckBox product_radio_button = (CheckBox) v.findViewById(R.id.product_radio_button);
                    Boolean isBoolean = isSelected.get(final_id);
                    recharge_center_bt.setText("去支付");
                    if (isBoolean){//如果之前已选中
                        product_radio_button.setChecked(false);//则取消选中
                        isSelected.put(final_id, false);//hashMap设置为false
                        Boolean okOrNo = false;
//                    Set<Map.Entry<Integer,Boolean>> set=isSelected.entrySet();
//                    Iterator  iterator =set.iterator();
//                    while (iterator.hasNext()){
//                        Map.Entry<Integer,Boolean> map= (Map.Entry<Integer, Boolean>) iterator.next();
//                        if(map.getKey()==final_id){
//                            isSelected.put(map.getKey(),false);
//                        }
//
//                    }
                        buttonEnable(recharge_center_bt, okOrNo);
                    } else{
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
                        NewRechargeProductActivity p = (NewRechargeProductActivity) myContext;
                        p.NewRechargeProductVipAdapter.notifyDataSetChanged();
                        notifyDataSetChanged();
                        buttonEnable(recharge_center_bt, true);
                    }
                }

            }
        });


        viewholder.product_introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String url = Constants.WEBVIEW_URL;
                String title = "产品说明";
                url += Constants.PRODUC_INTRODUCE + "?serviceValue=" + schoolallproductlists.get(position).getService_value(); //新版

//                if (final_id == 1) {//"基础信息服务"
//                    url += Constants.JCXX_INTRODUCE;
//                } else if (final_id == 2) {//"微课网（中学资源）
//                    url += Constants.WKW_INTRODUCE;
//                } else if (final_id == 6 || final_id == 7 || final_id == 8) {//子贵课堂（中学资源）
//                    url += Constants.ZGKT_INTRODUCE;
//                } else if (final_id == 8) {//"子贵探视"
//                    url += Constants.ZGTS_INTRODUCE;
//                } else if (final_id == 5) {//"小学资源"
//                    url += Constants.XXZY_INTRODUCE;
//                } else {
//                    url = null;
//                    title = null;
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
        TextView taocan_name, service_name, service_date, service_charge, original_cost, product_introduce;
        ImageView im_yigoumai;
        CheckBox product_radio_button;
        View view;
    }

}
