/*
 * 文 件 名:PackageSelectActivity.java
 * 版 本 号： 1.05
 */
package com.wcyc.zigui2.newapp.module.charge2;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;


import com.allinpay.appayassistex.APPayAssistEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.CommitOrderPayReq;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.NewChild;
import com.wcyc.zigui2.newapp.bean.ServiceExpiredBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.PackageRoduct;
import com.wcyc.zigui2.newapp.module.charge2.ChargeProduct.SchoolProducts;
import com.wcyc.zigui2.newapp.module.order.MyOrderActivity;
import com.wcyc.zigui2.newapp.module.order.PayUtil;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.utils.LocalUtil;
import com.wcyc.zigui2.widget.CustomDialog;
import com.wcyc.zigui2.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 选择套餐
 *
 * @author 郑国栋 2017-01-06
 * @version 2.0.9
 */
public class NewRechargeProductActivity extends BaseActivity implements
        OnClickListener {
    private LinearLayout title_back;// 返回键布局
    private TextView new_content;// 标题
    private TextView title_right_tv;// 右标题
    private MyListView vip_services_mlv;
    private List<PackageRoduct> packageRoductList;
    private Button recharge_center_bt;
    private static final int GET_VIP_SERVICE_INFO = 101; //
    private HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    public NewRechargeProductVipAdapter NewRechargeProductVipAdapter;
    private ChargeProduct chargeProduct;
    private Integer moduleIntegerId = 0;
    private CustomDialog dialog;
    private Intent rechargePriceIntent;
    public NewRechargeProductCommonVipAdapter newRechargeProductCommonVipAdapter;
    private MyListView vip_commonservice_mlv;
    private TextView tv_no_message;
    private TextView ziyou;
    private TextView product_tv;
    private LinearLayout lv_all;// 有数据  显示此 lv

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recharge_product_activity3);
        initView();
        initDatas();
        initEvents();
    }

    // 实例化组件
    private void initView() {
        tv_no_message = (TextView) findViewById(R.id.tv_no_message);// 无数据 默认显示
        lv_all = (LinearLayout) findViewById(R.id.lv_all);// 有数据  显示此 lv
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        title_right_tv = (TextView) findViewById(R.id.title_right_tv);// 右标题
        vip_services_mlv = (MyListView) findViewById(R.id.vip_services_mlv);
        vip_commonservice_mlv = (MyListView) findViewById(R.id.vip_commonservice_mlv);
        recharge_center_bt = (Button) findViewById(R.id.recharge_center_bt);
        product_tv = (TextView) findViewById(R.id.product_tv);
        ziyou = (TextView) findViewById(R.id.ziyou);
    }

    private String call = ""; //判断是否从子贵课堂进来

    // 初始化数据
    private void initDatas() {
        title_back.setVisibility(View.VISIBLE);
        new_content.setText("充值中心");
        title_right_tv.setVisibility(View.VISIBLE);
        title_right_tv.setText("充值记录");
        title_right_tv.setTextColor(getResources().getColor(R.color.blue));

        Intent intent = getIntent();
        if (intent != null) {
            String moduleStr = intent.getStringExtra("module");
            int moduleNumber=intent.getIntExtra("moduleNumber",0);
            call = intent.getStringExtra("call");
            NewPayPop.call = call;
            if (!DataUtil.isNullorEmpty(moduleStr)) {
                moduleIntegerId = CCApplication.getInstance().getIntegerServiceKind2(moduleStr,moduleNumber);
            }
        }
        buttonEnable(recharge_center_bt,false);
        UserType user = CCApplication.getInstance().getPresentUser();
        if (user != null
                && Constants.PARENT_STR_TYPE.equals(user.getUserType())) {
            try {
                JSONObject json = new JSONObject();
                json.put("parentId", user.getUserId());//
                json.put("userType", user.getUserType());//
                json.put("schoolId", user.getSchoolId());
                json.put("studentId", user.getChildId());
                json.put("classId", user.getClassId());
                json.put("gradeId", user.getGradeId());
                System.out.println("===充值中心入参===" + json);
                queryPost(Constants.GET_VIP_SERVICE_INFO, json);
                action = GET_VIP_SERVICE_INFO;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    // 设置点击事件监听器
    private void initEvents() {
        title_back.setOnClickListener(this);
        title_right_tv.setOnClickListener(this);
        recharge_center_bt.setOnClickListener(this);
    }

    NewPayPop payPop;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                NewRechargeProductActivity.this.finish();
                break;
            case R.id.title_right_tv://跳转充值记录
                Bundle bundle = new Bundle();
                bundle.putInt("status", 1);
                newActivity(MyOrderActivity.class, bundle);
                break;
            case R.id.recharge_center_bt://跳转下一步按钮
                isSelected = NewRechargeProductVipAdapter.getIsSelected();
                boolean isSelectTongyi = false;
                SysProductListInfo.Schoolallproductlist schoolallproductlist = null;
                for (SysProductListInfo.Schoolallproductlist item : newRechargeProductCommonVipAdapter.getSchoolallproductlists()) {
                    if (isSelected.get(item.getProductCode()) != null) {
                        if (isSelected.get(item.getProductCode())) {
                            isSelectTongyi = true;
                            schoolallproductlist = item;
                        }
                    }
                }
                if (isSelectTongyi) {
                    //直接弹出去支付
                    payPop = new NewPayPop(this, 5, schoolallproductlist);
                    payPop.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    //执行原来逻辑
                    rechargePriceIntent = new Intent(this, NewRechargePriceActivity.class);
                    rechargePriceIntent.putExtra("isSelected", isSelected);
                    if (call != null) {
                        rechargePriceIntent.putExtra("call", call);
                    }
                    rechargePriceIntent.putExtra("sysProductListInfo", sysProductListInfo);
                    if ("2".equals(getStage())) {
                        Boolean flag = false;//判断是否包含 子贵课堂 或 微课网
                        if (isSelected.containsKey(2)) {
                            if (isSelected.get(2)) {
                                flag = true;
                            }
                        }
                        if (isSelected.containsKey(3)) {
                            if (isSelected.get(3)) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            exitOrNot();
                        } else {
                            startActivity(rechargePriceIntent);
                            //   finish();
                        }
                    } else {
                        startActivity(rechargePriceIntent);
                        //  finish();
                    }
                }


                break;
            default:
                break;

        }
    }

    SysProductListInfo sysProductListInfo;

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case GET_VIP_SERVICE_INFO://
                System.out.println("===充值中心出参===" + data);
                sysProductListInfo = JsonUtils.fromJson(data, SysProductListInfo.class);
                if (sysProductListInfo.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    List<SysProductListInfo.Schoolallproductlist> schoolallproductlists = new ArrayList<>();
                    schoolallproductlists = sysProductListInfo.getSchoolAllProductList();
                    //將統一訂購和自由订购区分
                    List<SysProductListInfo.Schoolallproductlist> schoolallproductlists_tongyi = new ArrayList<>();
                    List<SysProductListInfo.Schoolallproductlist> schoolallproductlists_ziyou = new ArrayList<>();
                    for (SysProductListInfo.Schoolallproductlist item : schoolallproductlists) {
                        if (item.getOrderModel() == 1) {
                            schoolallproductlists_tongyi.add(item);
                        }
                        if (item.getOrderModel() == 2) {
                            schoolallproductlists_ziyou.add(item);
                        }
                    }
                    if (schoolallproductlists != null) {
                        if (schoolallproductlists.size() > 0) {
                            for (int i = 0; i < schoolallproductlists.size(); i++) {
                                int id = schoolallproductlists.get(i).getServiceId();
                                int moduleIntId = moduleIntegerId;
                                if (moduleIntId == id) {
                                    isSelected.put(id, true); //肯定是自由订购产品
                                } else {
                                    if (schoolallproductlists.get(i).getOrderModel() == 1) {
                                        //统一订购  存放productCode
                                        isSelected.put(schoolallproductlists.get(i).getProductCode(), false);
                                    } else {
                                        //自由订购   存放serviceId
                                        isSelected.put(id, false);
                                    }

                                }
                            }
                        }
                    }
                    if (schoolallproductlists_tongyi.size() == 0 && schoolallproductlists_ziyou.size() == 0) {
                        //无数据
                        lv_all.setVisibility(View.INVISIBLE);
                        tv_no_message.setVisibility(View.VISIBLE);
                    } else {
                        //有数据
                        lv_all.setVisibility(View.VISIBLE);
                        tv_no_message.setVisibility(View.INVISIBLE);
                        //加个判断  无统一订购产品 不显示
                        if (schoolallproductlists_tongyi.size() == 0) {
                            product_tv.setVisibility(View.GONE);
                        }
                        //加个判断  无自由订购订购产品 不显示
                        if (schoolallproductlists_ziyou.size() == 0) {
                            ziyou.setVisibility(View.GONE);
                        }
                    }

                    newRechargeProductCommonVipAdapter = new NewRechargeProductCommonVipAdapter(this, schoolallproductlists_tongyi, isSelected, recharge_center_bt, NewRechargeProductVipAdapter, sysProductListInfo.getPurchasedProcutList(), moduleIntegerId);
                    NewRechargeProductVipAdapter = new NewRechargeProductVipAdapter(this, schoolallproductlists_ziyou, isSelected, recharge_center_bt, newRechargeProductCommonVipAdapter);
                    vip_services_mlv.setAdapter(NewRechargeProductVipAdapter);
                    vip_commonservice_mlv.setAdapter(newRechargeProductCommonVipAdapter);
                } else {
                DataUtil.getToast(sysProductListInfo.getServerResult().getResultMessage());
            }
               /*  y原来的
                chargeProduct = JsonUtils.fromJson(data, ChargeProduct.class);
                if (chargeProduct.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    packageRoductList = new ArrayList<PackageRoduct>();
                    packageRoductList = chargeProduct.getPackageRoductList();

                    if (packageRoductList != null) {
                        if (packageRoductList.size() > 0) {
                            for (int i = 0; i < packageRoductList.size(); i++) {
                                int id = Integer.parseInt(packageRoductList.get(i).getProductCode());
                                int moduleIntId = moduleIntegerId;
                                if (moduleIntId == id) {
                                    isSelected.put(id, true);
                                } else {
                                    isSelected.put(id, false);
                                }
                            }
                        }
                    }
                    newRechargeProductCommonVipAdapter=new NewRechargeProductCommonVipAdapter(this,packageRoductList, isSelected, recharge_center_bt,NewRechargeProductVipAdapter);
                    NewRechargeProductVipAdapter = new NewRechargeProductVipAdapter(this, packageRoductList, isSelected, recharge_center_bt,newRechargeProductCommonVipAdapter);
                    vip_services_mlv.setAdapter(NewRechargeProductVipAdapter);
                    vip_commonservice_mlv.setAdapter(newRechargeProductCommonVipAdapter);
                } else {
                    DataUtil.getToast(chargeProduct.getServerResult().getResultMessage());
                }*/
                break;
        }
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

    //获取学段
    private String getStage() {
        UserType user = CCApplication.getInstance().getPresentUser();
        String childId = "";
        if (user != null) {
            childId = user.getChildId();
        }
        MemberDetailBean detail = CCApplication.getInstance().getMemberDetail();
        if (detail != null) {
            List<NewChild> list = detail.getChildList();
            if (list != null) {
                for (NewChild item : list) {
                    if (childId.equals(item.getChildID())) {
                        return item.getStageCode();
                    }
                }
            }
        }
        return null;
    }

    private void exitOrNot() {
        dialog = new CustomDialog(this, R.style.mystyle,
                R.layout.customdialogzgd, handler);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setTitle("");
        dialog.setContent("子贵课堂/微课网属于中学资源服务，您确定要购买吗？");
    }

    /**
     * 控制CustomDialog按钮事件.
     */
    Handler handler = new Handler() {
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (0 != msg.arg1) {
            }
            switch (msg.what) {
                case CustomDialog.DIALOG_CANCEL:// 取消
                    dialog.dismiss();
                    break;
                case CustomDialog.DIALOG_SURE:// 确认
                    startActivity(rechargePriceIntent);
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

//    /**
//     * 通联支付回调的返回码
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (APPayAssistEx.REQUESTCODE == requestCode) {
//            if (null != data) {
//                String payRes = null;
//                String payAmount = null;
//                String payTime = null;
//                try {
//                    JSONObject resultJson = new JSONObject(data.getExtras().getString("result"));
//                    payRes = resultJson.getString(APPayAssistEx.KEY_PAY_RES);
//                    payAmount = resultJson.getString("payAmount");
//                    payTime = resultJson.getString("payTime");
//                    System.out.println("payTime:" + payTime);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (null != payRes && payRes.equals(APPayAssistEx.RES_SUCCESS)) {
//                    DataUtil.getToast("支付成功");
//                    setResult(RESULT_OK);
//                    finish();
////					CCApplication.app.finishAllActivity();
////					newActivity(MyInformationActivity.class, null);
//                    // new PayUtil.PayResultAsyncTask(PayUtil.MOBILCXETLPAYSECURE, "1", LocalUtil.req).execute();//写死的
//                } else {
//                    DataUtil.getToast("支付失败");
//                    newActivity(PayFailActivity.class, null);
//                    setResult(RESULT_CANCELED);
//                    finish();
//                }
//            }
//        }
////        super.onActivityResult(requestCode, resultCode, data);
//    }

}
