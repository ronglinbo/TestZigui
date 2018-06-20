package com.wcyc.zigui2.newapp.module.order;

/**
 * Created by 章豪 on 2017/6/29.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseRecycleAdapter;
import com.wcyc.zigui2.core.BaseRecyleviewFragment;
import com.wcyc.zigui2.newapp.module.charge2.NewPayPop;

import java.util.ArrayList;
import java.util.List;

//import com.wcyc.zigui2.newapp.module.othernumber.NewOtherNumberActivity;


public class PayDaiOrderFragment extends BaseRecyleviewFragment<Order> {
    private String type = "";
    private List<Order> mData = new ArrayList<>();

    int countB = 0;



    ;

    @Override
    public void initDatas() {
//        mData.clear();
//        //初始化数据
//        for (int i = 0; i < 10; i++) {
//            Order order = new Order();
//            order.setCreatetime_play("2017-06-30  00:00:0" + countB);
//            order.setTaocan_name("2017年上学期套餐" + i);
//            order.setPay_state("1");
//            order.setService_date_display("2017-02-01~2017-07-3" + countB);
//            order.setsNo_display(new Date().getTime() + "");
//            if (i > 0 && i < 5) {
//                order.setService("个性服务");
//            } else {
//                order.setService("子贵课堂");
//            }
//
//            mData.add(order);
//            countB++;
//        }
//        setDatas(mData);


    }

    int countA = 0;

    @Override
    public void loadmoreData() {
//
//        if (countA == 0) {
//            //加载更多
//            for (int i = 0; i < 10; i++) {
//                Order order = new Order();
//                order.setCreatetime_play("2017-06-30  00:00:0" + countB);
//                order.setTaocan_name("2017年上学期套餐" + i);
//                order.setPay_state("1");
//                order.setService_date_display("2017-02-01~2017-07-3" + countB);
//                order.setsNo_display(new Date().getTime() + "");
//                if (i > 0 && i < 5) {
//                    order.setService("个性服务");
//                } else {
//                    order.setService("子贵课堂");
//                }
//
//                mData.add(order);
//                countB++;
//
//            }
//            countA++;
//            setDatas(mData);
//        } else if (countA == 1) {
//
//            for (int i = 0; i < 10; i++) {
//                Order order = new Order();
//                order.setCreatetime_play("2017-06-30  00:00:0" + countB);
//                order.setTaocan_name("2017年上学期套餐" + i);
//                order.setPay_state("1");
//                order.setService_date_display("2017-02-01~2017-07-3" + countB);
//                order.setsNo_display(new Date().getTime() + "");
//                if (i > 0 && i < 5) {
//                    order.setService("个性服务");
//                } else {
//                    order.setService("子贵课堂");
//                }
//
//                mData.add(order);
//                countB++;
//            }
//            countA = -1;
//            setDatas(mData);
//        } else if (countA == -1) {
//            DataUtil.getToast("没有更多数据了");
//            if (baseRecycleAdapter.view != null) {
//                baseRecycleAdapter.view.setVisibility(View.GONE);
//            }
//
//        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("AllOrderFragment:" + type, "onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("AllOrderFragment:" + type, "onActivityCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("AllOrderFragment:" + type, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("AllOrderFragment:" + type, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("AllOrderFragment:" + type, "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("AllOrderFragment:" + type, "onDestroyView");
    }

    @Override
    public void onRefreshData() {
//        //下拉刷新
//        countB = 0;
//        countA=0;
//        mData.clear();
//        for (int i = 0; i < 10; i++) {
//            Order order = new Order();
//            order.setCreatetime_play("2017-06-30  00:00:0" + countB);
//            order.setTaocan_name("2017年上学期套餐" + i);
//            order.setPay_state("1");
//            order.setService_date_display("2017-02-01~2017-07-3" + countB);
//            order.setsNo_display(new Date().getTime() + "");
//            if (i > 0 && i < 5) {
//                order.setService("个性服务");
//            } else {
//                order.setService("子贵课堂");
//            }
//
//            mData.add(order);
//            countB++;
//        }
//        setDatas(mData);


    }


    @Override
    public int getAdapterLayoutId() {
        return R.layout.order_item;
    }

    protected NewPayPop payPop;
    @Override
    public void bindAdapterData(BaseRecycleAdapter.BaseViewHolder holder, int position) {
//        //绑定适配器数据 shifu_rl
//        final Order order = getDatas().get(position);
//        RelativeLayout shifu_rl = (RelativeLayout) holder.getView(R.id.shifu_rl); //
//        shifu_rl.setVisibility(View.GONE);
//        TextView service = (TextView) holder.getView(R.id.service); //服务类型
//        TextView service_date_display = (TextView) holder.getView(R.id.service_date_display); //服务时间范围
//        TextView sNo_display = (TextView) holder.getView(R.id.sNo_display);//订单编号
//        TextView createtime_play = (TextView) holder.getView(R.id.createtime_play);//创建时间
//        TextView taocan_name = (TextView) holder.getView(R.id.taocan_name); //套餐名称
//        Button gopay = (Button) holder.getView(R.id.bt_gopay);//去支付按钮
//        TextView pay_state = (TextView) holder.getView(R.id.pay_state); //订单状态
//
//        service.setText(order.getService());
//        service_date_display.setText(order.getService_date_display());
//        sNo_display.setText(order.getsNo_display());
//        createtime_play.setText(order.getCreatetime_play());
//        taocan_name.setText(order.getTaocan_name());
//        pay_state.setText("待付款");
//        gopay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                payPop = new NewPayPop((BaseActivity) getContext(),4,order);
//                payPop.showAtLocation(v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//            }
//        });



    }


    @Override
    public void initEvents() {
        //处理效果
    }
}

