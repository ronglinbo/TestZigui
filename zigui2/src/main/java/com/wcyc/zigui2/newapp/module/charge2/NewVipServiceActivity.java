/*
 * 文 件 名:PackageSelectActivity.java
 * 版 本 号： 1.05
 */
package com.wcyc.zigui2.newapp.module.charge2;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseWebviewActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.MyListView;

import org.json.JSONObject;

import java.util.List;

/**
 * 会员服务
 *
 * @author 郑国栋 2017-01-10
 * @version 2.0.9
 */
public class NewVipServiceActivity extends BaseActivity implements
        OnClickListener {
    private LinearLayout title_back;// 返回键布局
    private TextView new_content;// 标题
    private MyListView vip_services_mlv;
    private List<SysProductListInfo.Productlist> studentBaseVipServicesList;
    private Button recharge_center_bt;
    private static final int GET_VIP_SERVICE_INFO = 101; //
    private TextView tv_no_message;
    private View line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_vip_service_activity);

        initView();
        initDatas();
        initEvents();
    }

    // 实例化组件
    private void initView() {
        line= (View) findViewById(R.id.line);// 返回键
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        tv_no_message = (TextView) findViewById(R.id.tv_no_message);// 返回键
        vip_services_mlv = (MyListView) findViewById(R.id.vip_services_mlv);
        recharge_center_bt = (Button) findViewById(R.id.recharge_center_bt);
    }

    // 初始化数据
    private void initDatas() {
        title_back.setVisibility(View.VISIBLE);
        new_content.setText("我的服务");

        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null
                && Constants.PARENT_STR_TYPE.equals(user.getUserType())) {
            try {

                JSONObject json = new JSONObject();
            //    json.put("parentId", user.getUserId());
            //    json.put("userType", user.getUserType());
                json.put("schoolId", user.getSchoolId());
                json.put("studentId", user.getChildId());
                json.put("classId", user.getClassId());
                json.put("gradeId", user.getGradeId());
                System.out.println("===会员服务入参===" + json);
                queryPost(Constants.GET_VIP_SERVICE_INFO, json);
                action = GET_VIP_SERVICE_INFO;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    // 设置点击效果监听器
    private void initEvents() {
        title_back.setOnClickListener(this);
        recharge_center_bt.setOnClickListener(this);
        vip_services_mlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Bundle bundle=new Bundle();
//                String url= Constants.WEBVIEW_URL;
//                String title="产品说明";
//                final int final_id=studentBaseVipServicesList.get(position).getServiceId();
//                if (final_id==1) {//"基础信息服务"
//                    url+=Constants.JCXX_INTRODUCE;
//                } else if (final_id==2) {//"微课网（中学资源）
//                    url+=Constants.WKW_INTRODUCE;
//                } else if (final_id==3) {//子贵课堂（中学资源）
//                    url+=Constants.ZGKT_INTRODUCE;
//                } else if (final_id==4) {//"子贵探视"
//                    url+=Constants.ZGTS_INTRODUCE;
//                } else if (final_id==5) {//"小学资源"
//                    url+=Constants.XXZY_INTRODUCE;
//                } else {
//                    url=null;
//                    title=null;
//                }
////                System.out.println("===title="+title);
//                System.out.println("===url="+url);
//                if(!DataUtil.isNullorEmpty(url)){
//                    bundle.putString("title", title);
//                    bundle.putString("url", url);
//                    newActivity(BaseWebviewActivity.class, bundle);
//                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                NewVipServiceActivity.this.finish();
                break;
            case R.id.recharge_center_bt://跳转去充值按钮
				newActivity(NewRechargeProductActivity.class, null);
                break;
            default:
                break;

        }
    }

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case GET_VIP_SERVICE_INFO://
                System.out.println("===会员服务出参===" + data);
                SysProductListInfo sysProductListInfo=JsonUtils.fromJson(data, SysProductListInfo.class);
                if (sysProductListInfo.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    studentBaseVipServicesList = sysProductListInfo.getProductList();
                    StudentBaseVipServicesAdapter baseVipServicesAdapter =
                            new StudentBaseVipServicesAdapter(this, studentBaseVipServicesList);
                    vip_services_mlv.setAdapter(baseVipServicesAdapter);
                    if(studentBaseVipServicesList.size()==0){
                        tv_no_message.setVisibility(View.VISIBLE);
                        line.setVisibility(View.VISIBLE);
                    }else{
                        tv_no_message.setVisibility(View.INVISIBLE);
                        line.setVisibility(View.INVISIBLE);
                    }


                } else {
                    DataUtil.getToast(sysProductListInfo.getServerResult().getResultMessage());
                }
              /*  ChargeProduct chargeProduct = JsonUtils.fromJson(data, ChargeProduct.class);
                if (chargeProduct.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    studentBaseVipServicesList = chargeProduct.getStudentBaseVipServices();
                    StudentBaseVipServicesAdapter baseVipServicesAdapter =
                            new StudentBaseVipServicesAdapter(this, studentBaseVipServicesList);
                    vip_services_mlv.setAdapter(baseVipServicesAdapter);
                } else {
                    DataUtil.getToast(chargeProduct.getServerResult().getResultMessage());
                }*/
                break;
        }
    }

}
