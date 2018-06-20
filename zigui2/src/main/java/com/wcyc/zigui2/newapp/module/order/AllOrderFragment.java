package com.wcyc.zigui2.newapp.module.order;

/**
 * Created by 章豪 on 2017/6/29.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseRecycleAdapter;
import com.wcyc.zigui2.core.BaseRecyleviewFragment;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.module.charge2.NewPayPop;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import com.wcyc.zigui2.newapp.module.othernumber.NewOtherNumberActivity;


@SuppressLint("ValidFragment")
public class AllOrderFragment extends BaseRecyleviewFragment<Order> implements OnClickListener, HttpRequestAsyncTaskListener {


    public interface createOrderListener {
        void createOrder(Order order);
    }

    private   String status= "";
    private List<Order> mData = new ArrayList<>();

    int countB = 0;

    public AllOrderFragment(String  status) {
        this.status = status;

    }

    public AllOrderFragment() {

    }

    int pages = 0;
    OrderInfo orderInfo;

    @Override
    public void onRequstComplete(String result) {
        //网络框架返回主线程  数据操作
        switch (action) {
            case INITDATA:
                System.out.println("初始化订单:" + result);
                orderInfo = JsonUtils.fromJson(result, OrderInfo.class);
                if(orderInfo.getServerResult().getResultCode()== Constants.SUCCESS_CODE){
                    pages = orderInfo.getPages();
                    if(pages<=1){
                        if (baseRecycleAdapter.view != null) {
                            baseRecycleAdapter.view.setVisibility(View.GONE);
                        }
                    }else{
                        if (baseRecycleAdapter.view != null) {
                            baseRecycleAdapter.view.setVisibility(View.VISIBLE);
                        }
                    }
                    mData.addAll(orderInfo.getOrderList());
                    setDatas(mData);
                }else{
                   DataUtil.getToast(orderInfo.getServerResult().getResultMessage());
                }

                dismissPd();
                break;
            case REFERSH:

                mData.clear();
                System.out.println("刷新订单:" + result);
                orderInfo = JsonUtils.fromJson(result, OrderInfo.class);
                pages = orderInfo.getPages();
                if(pages<=1){
                    if (baseRecycleAdapter.view != null) {
                        baseRecycleAdapter.view.setVisibility(View.GONE);
                    }
                }
                mData.addAll(orderInfo.getOrderList());
                setDatas(mData);
                break;
            case LOAD_MORE:
                System.out.println("加载更多订单:" + result);
                orderInfo = JsonUtils.fromJson(result, OrderInfo.class);
                pages = orderInfo.getPages();
                mData.addAll(orderInfo.getOrderList());
                setDatas(mData);
                break;
        }

    }

    @Override
    public void onRequstCancelled() {

    }

    ;
    private int page = 1;

    @Override
    public void initDatas() {
        if(!status.equals("")){
            if(MyOrderActivity.current_status==Integer.parseInt(status)){
                showProgessBar();
            }
        }else{
            if(MyOrderActivity.current_status==-1){
                showProgessBar();
            }
        }


        page = 1;
        mData.clear();
        JSONObject json = new JSONObject();
        UserType userType = CCApplication.getInstance().getPresentUser();
        try {
            json.put("userId", userType.getUserId());
            json.put("studentId", userType.getChildId());
            json.put("schoolId", userType.getSchoolId());
            json.put("status", status);
            json.put("curPage", page);
            json.put("pageSize", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpRequestAsyncTask(json, this, CCApplication.applicationContext).execute(Constants.GET_MYORDER_List);
        action = INITDATA;
    }


    int countA = 0;

    @Override
    public void loadmoreData() {
        if (page >= pages) {

            if (baseRecycleAdapter.view != null) {
                baseRecycleAdapter.view.setVisibility(View.GONE);
            }
        } else {
            //加载下一页
            page++;
            JSONObject json = new JSONObject();
            UserType userType = CCApplication.getInstance().getPresentUser();
            try {
                json.put("userId", userType.getUserId());
                json.put("studentId", userType.getChildId());
                json.put("schoolId", userType.getSchoolId());
                json.put("status", status);
                json.put("curPage", page);
                json.put("pageSize", 10);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(json, this, CCApplication.applicationContext).execute(Constants.GET_MYORDER_List);
            action = LOAD_MORE;


        }


    }
    public void refreshNum(){

    }


    @Override
    public void onRefreshData() {
        MyOrderActivity activity=(MyOrderActivity)getActivity();
        activity.refershNum();
        if (baseRecycleAdapter.view != null) {
            baseRecycleAdapter.view.setVisibility(View.VISIBLE);
        }
        page = 1;

        JSONObject json = new JSONObject();
        UserType userType = CCApplication.getInstance().getPresentUser();
        try {
            json.put("userId", userType.getUserId());
            json.put("studentId", userType.getChildId());
            json.put("schoolId", userType.getSchoolId());
            json.put("status", status);
            json.put("curPage", page);
            json.put("pageSize", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpRequestAsyncTask(json, this, CCApplication.applicationContext).execute(Constants.GET_MYORDER_List);
        action = REFERSH;

    }


    @Override
    public int getAdapterLayoutId() {
        return R.layout.order_item;
    }


    @Override
    public void bindAdapterData(BaseRecycleAdapter.BaseViewHolder holder, int position) {
        final Order order = getDatas().get(position);
        int order_state = order.getStatus();
        //绑定适配器数据
        TextView service = (TextView) holder.getView(R.id.service); //服务类型
        TextView service_date_display = (TextView) holder.getView(R.id.service_date_display); //服务时间范围
        TextView sNo_display = (TextView) holder.getView(R.id.sNo_display);//订单编号
        TextView createtime_play = (TextView) holder.getView(R.id.createtime_play);//创建时间
        TextView taocan_name = (TextView) holder.getView(R.id.taocan_name); //套餐名称
        Button gopay = (Button) holder.getView(R.id.bt_gopay);//去支付按钮
        TextView pay_state = (TextView) holder.getView(R.id.pay_state); //订单状态
        TextView paydate_play= (TextView) holder.getView(R.id.paydate_play);//支付时间显示
        TextView paydate= (TextView) holder.getView(R.id.paydate); //支付时间 名字
        TextView cancel_date_play= (TextView) holder.getView(R.id.cancel_date_play);//取消时间显示
        TextView cancel_date= (TextView) holder.getView(R.id.cancel_date); //取消时间 名字
        if (order_state == 0) { //待付款
            TextView   heh1e= (TextView) holder.getView(R.id.heh1e);
            //支付时间隐藏
            paydate_play.setVisibility(View.GONE);
            paydate.setVisibility(View.GONE);
            //取消时间隐藏
            cancel_date_play.setVisibility(View.GONE);
            cancel_date.setVisibility(View.GONE);
            //线上支付模块隐藏
            RelativeLayout shifu_rl = (RelativeLayout) holder.getView(R.id.shifu_rl);
            shifu_rl.setVisibility(View.GONE);
            //去支付模块显示
            RelativeLayout yingfu_rl = (RelativeLayout) holder.getView(R.id.yingfu_rl);
            yingfu_rl.setVisibility(View.VISIBLE);

            TextView money = (TextView) holder.getView(R.id.money); //应付价格
            int  a=order.getOrderAmount() % 100;
            if(a!= 0){
                //小数
                heh1e.setVisibility(View.GONE);
                money.setText("￥" + (float)order.getOrderAmount() / 100);//应付价格 order
            }else {
                //整数
                heh1e.setVisibility(View.VISIBLE);
                money.setText("￥" + order.getOrderAmount() / 100);//应付价格 order
            }
            pay_state.setTextColor(this.getResources().getColor(R.color.color_dai_pay));
            pay_state.setText("待付款");
        } else if (order_state == 4) { //欠款
            TextView   heh1e= (TextView) holder.getView(R.id.heh1e);
            //支付时间隐藏
            paydate_play.setVisibility(View.GONE);
            paydate.setVisibility(View.GONE);
            //取消时间隐藏
            cancel_date_play.setVisibility(View.GONE);
            cancel_date.setVisibility(View.GONE);
            //线上支付模块隐藏
            RelativeLayout shifu_rl = (RelativeLayout) holder.getView(R.id.shifu_rl);
            shifu_rl.setVisibility(View.GONE);
            //去支付模块显示
            RelativeLayout yingfu_rl = (RelativeLayout) holder.getView(R.id.yingfu_rl);
            yingfu_rl.setVisibility(View.VISIBLE);
            TextView money = (TextView) holder.getView(R.id.money);

          if((order.getOrderAmount() % 100 )!= 0){
              heh1e.setVisibility(View.GONE);
              money.setText("￥" + (float)order.getOrderAmount() / 100);//应付价格 order
          }else {
              heh1e.setVisibility(View.VISIBLE);
              money.setText("￥" + order.getOrderAmount() / 100);//应付价格 order
          }

            pay_state.setTextColor(this.getResources().getColor(R.color.color_dai_pay));
            pay_state.setText("欠款");
        } else if (order_state == 1) {   //已支付
            TextView   hehe= (TextView) holder.getView(R.id.hehe);
            //支付时间显示
            paydate_play.setVisibility(View.VISIBLE);
            paydate.setVisibility(View.VISIBLE);
            //取消时间隐藏
            cancel_date_play.setVisibility(View.GONE);
            cancel_date.setVisibility(View.GONE);
            //去支付模块隐藏
            RelativeLayout yingfu_rl = (RelativeLayout) holder.getView(R.id.yingfu_rl); //
            yingfu_rl.setVisibility(View.GONE);
            //线上支付 显示
            RelativeLayout shifu_rl = (RelativeLayout) holder.getView(R.id.shifu_rl); //
            shifu_rl.setVisibility(View.VISIBLE);
            //实付 字体 shifu
            TextView shifu= (TextView) holder.getView(R.id.shifu);
            shifu.setVisibility(View.VISIBLE);
            //实付money  shifu1_money 显示
            TextView shifu1_money= (TextView) holder.getView(R.id.shifu1_money);
            shifu1_money.setVisibility(View.VISIBLE);
            //设置支付时间
            paydate_play.setText(order.getPayDate());
            //支付方式
            TextView online_pay = (TextView) holder.getView(R.id.online_pay); //
            online_pay.setVisibility(View.VISIBLE);
            online_pay.setText(order.getPayTypeStr());

            //实付价格

            if(order.getOrderAmount() % 100 != 0){
                hehe.setVisibility(View.GONE); // 小数
                shifu1_money.setText("￥" + (float)order.getOrderAmount()/ 100);//实付价格 order
            }else {
                shifu1_money.setText("￥" + order.getOrderAmount() / 100);//实付价格 order
                hehe.setVisibility(View.VISIBLE);
            }

            TextView shifu_yinfu_money = (TextView) holder.getView(R.id.shifu_yinfu_money); // 应付价格
            int yingfu = order.getOrderAmount();
            if(order.getOrderAmount() % 100 != 0){
                 hehe.setVisibility(View.GONE);  //小数
                shifu_yinfu_money.setText("(应付:￥" + (float)yingfu/ 100+")");//应付价格 order
            }else {
                //整数
                hehe.setVisibility(View.VISIBLE);
                shifu_yinfu_money.setText("(应付:￥" + yingfu / 100+".00)");//应付价格 order
            }
            pay_state.setTextColor(this.getResources().getColor(R.color.color_qianse));
            pay_state.setText("已支付");
        } else if (order_state == 3) {    //已取消
            //支付时间显示
            paydate_play.setVisibility(View.VISIBLE);
            paydate.setVisibility(View.VISIBLE);
            //取消时间显示
            cancel_date_play.setVisibility(View.VISIBLE);
            cancel_date.setVisibility(View.VISIBLE);
            //去支付模块隐藏
            RelativeLayout yingfu_rl = (RelativeLayout) holder.getView(R.id.yingfu_rl); //
            yingfu_rl.setVisibility(View.GONE);
            //线上支付 显示

            RelativeLayout shifu_rl = (RelativeLayout) holder.getView(R.id.shifu_rl); //
            shifu_rl.setVisibility(View.VISIBLE);

            //设置支付时间
            paydate_play.setText(order.getPayDate());
            //设置取消时间时间
            cancel_date_play.setText(order.getUpdateTime());
            //隐藏线上支付部分模块
            TextView online_pay = (TextView) holder.getView(R.id.online_pay); //
            TextView shifu1_money = (TextView) holder.getView(R.id.shifu1_money); //
            TextView hehe = (TextView) holder.getView(R.id.hehe); //
            TextView shifu = (TextView) holder.getView(R.id.shifu); //
            //已經取消
            online_pay.setVisibility(View.INVISIBLE);
            shifu1_money.setVisibility(View.INVISIBLE);
            hehe.setVisibility(View.INVISIBLE);
            shifu.setVisibility(View.INVISIBLE);
            TextView shifu_yinfu_money = (TextView) holder.getView(R.id.shifu_yinfu_money); // 应付
            if(order.getOrderAmount() % 100 != 0){
                shifu_yinfu_money.setText("应付:￥" + (float)order.getOrderAmount()/100 );  //获取应付价格 order//应付价格 order
            }else {
                //整数
                shifu_yinfu_money.setText("应付:￥" + order.getOrderAmount()/100 +".00");  //获取应付价格
            }

            pay_state.setTextColor(this.getResources().getColor(R.color.color_cancel));
            pay_state.setText("已取消");
        }
        service.setText(order.getServiceName()); //设置 服务名称
        if(order.getOrderModel().equals("2")){
            //自由订购显示月数
            service_date_display.setText(order.getValidityDateValue()+"个月"); //设置 服务时间范围
        }else{
            //统一订购显示服务期限
            service_date_display.setText(order.getStartDate() + "~" + order.getEndDate()); //设置 服务时间范围
        }

        sNo_display.setText(order.getOrderNo());//设置 订单编号
        createtime_play.setText(order.getCreateTime());//订单创建时间
        taocan_name.setText(order.getProductName()); //套餐名称
        gopay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                payPop = new NewPayPop((BaseActivity) getActivity(), 4, order);
                payPop.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    protected NewPayPop payPop;


    @Override
    public void initEvents() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("AllOrderFragment:" + status, "onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("AllOrderFragment:" + status, "onActivityCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("AllOrderFragment:" + status, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("AllOrderFragment:" + status, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("AllOrderFragment:" + status, "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("AllOrderFragment:" + status, "onDestroyView");
    }
}

