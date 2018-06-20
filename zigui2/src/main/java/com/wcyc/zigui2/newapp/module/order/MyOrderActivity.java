package com.wcyc.zigui2.newapp.module.order;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseRecyleviewFragment;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyOrderActivity extends BaseActivity implements View.OnClickListener, DaiPayOrderFragment.ChangeDaiPayNumber, QianPayOrderFragment.ChangeQianPayNumber {
    private LinearLayout title_back;// 返回键布局
    private TextView new_content;// 标题
    private ViewPager viewPager;
    private ViewpageAdapter viewpageAdapter;
    private List<Fragment> fragments;
    public RadioGroup radioGroup;
    private TextView dai_pay_number, qian_pay_number;// 待付款，欠款 标题的 数量
    //  private List<Order> orders; //服务器最终数据
    private List<Map<String, String>> orders; //本地模拟数据
    private final int GET_ORDER_COUNT = 1;
    private final int REFERSH_ORDER_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_order);
        initView();
        initEvents();
        initDatas();
    }

    public static int current_status = -1;
    public int status = -1;

    public void initView() {

        dai_pay_number = (TextView) findViewById(R.id.dai_pay_number);// 待付款 订单数量
        qian_pay_number = (TextView) findViewById(R.id.qian_pay_number);// 欠款 订单数量
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键布局
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        viewPager = (ViewPager) findViewById(R.id.viewpager);//
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        fragments = new ArrayList<>();
        title_back.setVisibility(View.VISIBLE);
        new_content.setText("我的订单");
        try {
            status = getIntent().getExtras().getInt("status");
        } catch (Exception e) {

        }

    }


    public void initEvents() {
        title_back.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.all:
                        current_status = -1;
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.dai_pay:
                        current_status = 0;
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.qian_pay:
                        current_status = 4;
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.have_pay:
                        current_status = 1;
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.cancel:
                        current_status = 3;
                        viewPager.setCurrentItem(4);
                        break;

                }
                dismissPd();

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        current_status = -1;
                        radioGroup.check(R.id.all);
                        break;
                    case 1:
                        current_status = 0;
                        radioGroup.check(R.id.dai_pay);
                        break;
                    case 2:
                        current_status = 4;
                        radioGroup.check(R.id.qian_pay);
                        break;
                    case 3:
                        current_status = 1;
                        radioGroup.check(R.id.have_pay);
                        break;

                    case 4:
                        current_status = 3;
                        radioGroup.check(R.id.cancel);
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private BaseRecyleviewFragment
            allOrderFragment, //全部订单
            daiPayOrderFragment,//待付款订单
            cancelOrderFragment,//已取消订单
            havePayOrderFragment,//已支付订单
            qianPayOrderFragment;//欠款订单

    public void initDatas() {
        allOrderFragment = new AllOrderFragment("");//全部订单
        daiPayOrderFragment = new AllOrderFragment("0");//待付款订单
        cancelOrderFragment = new AllOrderFragment("3");//已取消订单
        havePayOrderFragment = new AllOrderFragment("1");//已支付订单
        qianPayOrderFragment = new AllOrderFragment("4");//欠款订单
        fragments.add(allOrderFragment);
        fragments.add(daiPayOrderFragment);
        fragments.add(qianPayOrderFragment);
        fragments.add(havePayOrderFragment);
        fragments.add(cancelOrderFragment);
        viewpageAdapter = new ViewpageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(viewpageAdapter);
        //获取记录数
        JSONObject json = new JSONObject();
        try {
            json.put("studentId", CCApplication.getInstance().getPresentUser().getChildId());
            json.put("schoolId", CCApplication.getInstance().getPresentUser().getSchoolId());
            queryPost(Constants.GET_ORDER_COUNT, json);
            action = GET_ORDER_COUNT;
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override

    protected void getMessage(String data) {
        switch (action) {
            case REFERSH_ORDER_COUNT:
                System.out.println("待支付，欠款记录数" + data);
                MyOrderCount myOrderCount1 = JsonUtils.fromJson(data, MyOrderCount.class);
                int qian_pay1 = Integer.parseInt(myOrderCount1.getDebtCount());
                int dai_pay1 = Integer.parseInt(myOrderCount1.getNoPayCount());
                if (qian_pay1 == 0) {
                    qian_pay_number.setVisibility(View.INVISIBLE);
                } else {
                    qian_pay_number.setVisibility(View.VISIBLE);
                    if (qian_pay1 > 99) {
                        qian_pay_number.setText("99+");
                    } else {
                        qian_pay_number.setText(myOrderCount1.getDebtCount());
                    }

                }
                if (dai_pay1 == 0) {
                    dai_pay_number.setVisibility(View.INVISIBLE);
                } else {// 大于0

                    dai_pay_number.setVisibility(View.VISIBLE);
                    if (dai_pay1 > 99) {
                        dai_pay_number.setText("99+");
                    } else {
                        dai_pay_number.setText(myOrderCount1.getNoPayCount());
                    }

                }

                break;
            case GET_ORDER_COUNT:
                try {
                    System.out.println("待支付，欠款记录数" + data);
                    MyOrderCount myOrderCount = JsonUtils.fromJson(data, MyOrderCount.class);
                    int qian_pay = Integer.parseInt(myOrderCount.getDebtCount());
                    int dai_pay = Integer.parseInt(myOrderCount.getNoPayCount());
                    if (qian_pay == 0) {
                        qian_pay_number.setVisibility(View.INVISIBLE);
                    } else {
                        qian_pay_number.setVisibility(View.VISIBLE);
                        if (qian_pay > 99) {
                            qian_pay_number.setText("99+");
                        } else {
                            qian_pay_number.setText(myOrderCount.getDebtCount());
                        }

                    }
                    if (dai_pay == 0) {
                        dai_pay_number.setVisibility(View.INVISIBLE);
                    } else {// 大于0

                        dai_pay_number.setVisibility(View.VISIBLE);
                        if (dai_pay > 99) {
                            dai_pay_number.setText("99+");
                        } else {
                            dai_pay_number.setText(myOrderCount.getNoPayCount());
                        }

                    }
                    //  showProgessBar();
                    if (status > -1) { //从支付回调接口
                        //优先 status跳转
                        radioGroup.check(R.id.have_pay);
                    } else {
                        //根据判断 根据数量
                        if (dai_pay > 0) {
                            radioGroup.check(R.id.dai_pay);
                        } else {
                            if (qian_pay > 0) {
                                radioGroup.check(R.id.qian_pay);
                            } else {
                                //都等于0的话
                                radioGroup.check(R.id.all);
                            }

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                MyOrderActivity.this.finish();
                break;
            default:
                break;

        }
    }

    //设置欠款的 number
    @Override
    public void setDaiPay(int num) {
        dai_pay_number.setText(num + "");
    }

    //设置待付款的 number
    @Override
    public void setQianPay(int num) {
        qian_pay_number.setText(num + "");
    }

    public void refershNum() {
        //获取记录数  更新记录数
        JSONObject json = new JSONObject();

        try {
            json.put("studentId", CCApplication.getInstance().getPresentUser().getChildId());
            json.put("schoolId", CCApplication.getInstance().getPresentUser().getSchoolId());
            queryPost(Constants.GET_ORDER_COUNT, json);
            action = REFERSH_ORDER_COUNT;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    protected void onResume() {
        super.onResume();
        //获取记录数  更新记录数
        JSONObject json = new JSONObject();

        try {
            json.put("studentId", CCApplication.getInstance().getPresentUser().getChildId());
            json.put("schoolId", CCApplication.getInstance().getPresentUser().getSchoolId());
            queryPost(Constants.GET_ORDER_COUNT, json);
            action = GET_ORDER_COUNT;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class ViewpageAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments;
        FragmentManager fragmentManager;

        public ViewpageAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
            super(fragmentManager);
            this.fragments = fragments;
            this.fragmentManager = fragmentManager;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub

            return super.instantiateItem(container, position);
        }

        private void removeFragment(ViewGroup container, int index) {
            String tag = getFragmentTag(container.getId(), index);
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment == null)
                return;
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.remove(fragment);
            ft.commit();
            ft = null;
            fragmentManager.executePendingTransactions();
        }

            private String getFragmentTag(int viewId, int index) {
                try {
                    Class<FragmentPagerAdapter> cls = FragmentPagerAdapter.class;
                    Class<?>[] parameterTypes = {int.class, long.class};
                    Method method = cls.getDeclaredMethod("makeFragmentName",
                            parameterTypes);
                    method.setAccessible(true);
                    String tag = (String) method.invoke(this, viewId, index);
                    return tag;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
            }
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


    }
}
