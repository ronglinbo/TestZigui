package com.wcyc.zigui2.newapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.adapter.PaymentListAdapter;
import com.wcyc.zigui2.newapp.adapter.SystemMessageAdapter;
import com.wcyc.zigui2.newapp.bean.PaymentListBean;
import com.wcyc.zigui2.newapp.bean.SystemMessageListBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaymentListActivity extends BaseActivity implements View.OnClickListener {

    private static final int GET_PAYMENT_MESSAGE_LIST = 0x0001;
    private RefreshListView1 listView;
    private LinearLayout title_back;
    private ImageView iv_no_data;
    private int k = 2;
    private int pages = 0;
    private List<PaymentListBean.ListBean> list;
    private PaymentListAdapter adapter;
    private String userId;
    private String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);
        initView();
        initEvent();
        initDatas();
    }

    private void initEvent() {
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnRefreshListener(new RefreshListView1.OnRefreshListener() {

            //下拉刷新
            @Override
            public void onDownPullRefresh() {
                k = 2;
                initDatas();
                listView.hideHeaderView();// 收起下拉刷新
            }


            //上拉加载
            @Override
            public void onLoadingMore() {
                if (k <= pages) {
                    loadData();
                } else if (k > pages) {
                    DataUtil.getToastShort(getString(R.string.string_no_more_data));
                }
                listView.hideFooterView();
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return false;
            }
        });

    }

    /**
     * 加载更多
     */
    private void loadData() {
        if (!DataUtil.isNetworkAvailable(getBaseContext())) {
            DataUtil.getToast("当前网络不可用，请检查您的网络设置");
            return;
        }
        if (!isLoading()) {
            loadDataByIndex(k);
            k++;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
//            if (RESULT_OK == resultCode) {
//                Bundle bundle = data.getExtras();
//                if (bundle != null) {
//                    initDatas();
//                }
//            }
            initDatas();
        }
    }

    /**
     * 加载更多
     *
     * @param k
     */
    private void loadDataByIndex(int k) {


        user = CCApplication.getInstance().getPresentUser();
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
            json.put("userType", userType);//
            json.put("curPage", k);
            json.put("pageSize", "10");
            json.put("schoolId", schoolId);

            if ("3".equals(userType)) {
                String studentId = CCApplication.getInstance().getPresentUser().getChildId();
                json.put("studentId", studentId);
            }

            if (!DataUtil.isNetworkAvailable(PaymentListActivity.this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            if (!isLoading()) {

                String url = new StringBuilder(Constants.SERVER_URL).append(
                        Constants.GET_USER_FEE_PAY_LIST).toString();
                String result = HttpHelper
                        .httpPostJson(PaymentListActivity.this, url, json);
                parseMoreData(result);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载更多
     *
     * @param data
     */
    private void parseMoreData(String data) {
        if (DataUtil.isNullorEmpty(data)) {
            return;
        }

        PaymentListBean moreBean = JsonUtils.fromJson(data, PaymentListBean.class);
        if (moreBean.getServerResult().getResultCode() == 200) {
            if (moreBean.getList().size() > 0) {
                List<PaymentListBean.ListBean> moreData = moreBean.getList();
                pages = moreBean.getTotalPageNum();
                adapter.addItem(moreData);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initDatas() {
        k = 2;
        list = new ArrayList<>();
        try {
            user = CCApplication.getInstance().getPresentUser();
            userId = user.getUserId();
            userType = user.getUserType();
            schoolId = user.getSchoolId();

            JSONObject json = new JSONObject();

            json.put("userId", userId);
            json.put("userType", userType);//
            json.put("schoolId", schoolId);//
            json.put("curPage", 1);
            json.put("pageSize", 10);


            if ("3".equals(userType)) {
                String studentId = CCApplication.getInstance().getPresentUser().getChildId();
                json.put("studentId", studentId);
            }

            if (!DataUtil.isNetworkAvailable(this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            if (!isLoading()) {
                System.out.println("==缴费记录入参==" + json);
                action = GET_PAYMENT_MESSAGE_LIST;
                queryPost(Constants.GET_USER_FEE_PAY_LIST, json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void initView() {
        TextView tv_title = (TextView) findViewById(R.id.new_content);
        tv_title.setText("缴费");

        title_back = (LinearLayout) findViewById(R.id.title_back);
        title_back.setVisibility(View.VISIBLE);

        listView = (RefreshListView1) findViewById(R.id.lv_payment_list);
        iv_no_data = (ImageView) findViewById(R.id.iv_empty);

        iv_no_data.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        listView.setDivider(null);
    }


    @Override
    protected void getMessage(String data) {

        switch (action) {
            case GET_PAYMENT_MESSAGE_LIST:
                parseInitData(data);
                break;


        }
    }

    private void parseInitData(String data) {
        System.out.println("初始化数据出参:" + data);

        if (DataUtil.isNullorEmpty(data)) {
            showEmptyData();
            return;
        }

        PaymentListBean messageListBean = JsonUtils.fromJson(data, PaymentListBean.class);
        if (messageListBean.getServerResult().getResultCode() == 200) {
            if (messageListBean.getList().size() > 0) {
                showInitData(messageListBean);
            } else {
                showEmptyData();
            }
        } else {
            showEmptyData();
        }
    }

    private void showEmptyData() {
        iv_no_data.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }



    private void showInitData(PaymentListBean messageListBean) {
        pages = messageListBean.getTotalPageNum();
        list = messageListBean.getList();
        adapter = new PaymentListAdapter(list, PaymentListActivity.this);
        listView.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
        }
    }
}
