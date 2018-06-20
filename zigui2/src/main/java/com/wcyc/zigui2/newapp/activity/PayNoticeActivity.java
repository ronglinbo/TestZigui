package com.wcyc.zigui2.newapp.activity;

import android.content.Context;
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
import com.wcyc.zigui2.newapp.adapter.PayNoticeAdapter;
import com.wcyc.zigui2.newapp.bean.PayNoticeListBean;
import com.wcyc.zigui2.newapp.bean.SystemMessageListBean;
import com.wcyc.zigui2.newapp.bean.UserType;
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

/**
 * @author zzc
 * @time 2018/2/3 0003
 */
public class PayNoticeActivity extends BaseActivity {

    private static final int GET_SYSTEM_PAY_NOTICE_LIST = 0x0001;
    private ImageView iv_no_data;
    private String userId;
    private String userType;
    private int k = 2;


    private int pages = 0;
    private String schoolId;
    private UserType user;
    private List<PayNoticeListBean.MsgListBean> list;
    private PayNoticeAdapter adapter;
    private RefreshListView1 listView;

    private LinearLayout title_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        initView();
        initEvent();
        initData();
    }

    private void initData() {
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
                System.out.println("==系统消息入参==" + json);
                action = GET_SYSTEM_PAY_NOTICE_LIST;
                queryPost(Constants.GET_ORDER_URGE_MSG_LIST_BY_USER, json);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                initData();
                listView.hideHeaderView();// 收起下拉刷新
            }


            //上拉加载
            @Override
            public void onLoadingMore() {
                if (k <= pages) {// 后一页页， 总页数////// 如果后面还有数据// 则加载一页
                    loadData();
                } else if (k > pages) {
                    DataUtil.getToastShort("没有更多数据了");
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


    private void initView() {
        TextView tv_title = (TextView) findViewById(R.id.new_content);
        tv_title.setText("订单催缴");

        title_back = (LinearLayout) findViewById(R.id.title_back);
        title_back.setVisibility(View.VISIBLE);

        listView = (RefreshListView1) findViewById(R.id.lv_system_message);
        iv_no_data = (ImageView) findViewById(R.id.no_data_iv);

        iv_no_data.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        listView.setDivider(null);
    }

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case GET_SYSTEM_PAY_NOTICE_LIST:
                parseInitData(data);
                break;
        }
    }


    private void showEmptyData() {
        iv_no_data.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    private void showInitData(PayNoticeListBean messageListBean) {
        pages = messageListBean.getTotalPageNum();
        list = messageListBean.getMsgList();
        adapter = new PayNoticeAdapter(list, PayNoticeActivity.this);
        listView.setAdapter(adapter);
    }


    /**
     * 初始化数据
     *
     * @param data
     */
    private void parseInitData(String data) {
        if (DataUtil.isNullorEmpty(data)) {
            return;
        }
        PayNoticeListBean messageListBean = JsonUtils.fromJson(data, PayNoticeListBean.class);
        if (messageListBean.getServerResult().getResultCode() == 200 && messageListBean.getMsgList() != null) {
            if (messageListBean.getMsgList().size() > 0) {
                showInitData(messageListBean);
            } else {
                showEmptyData();
            }
        } else {
            showEmptyData();
        }
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

            if (!DataUtil.isNetworkAvailable(PayNoticeActivity.this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            if (!isLoading()) {

                String url = new StringBuilder(Constants.SERVER_URL).append(
                        Constants.GET_ORDER_URGE_MSG_LIST_BY_USER).toString();
                String result = HttpHelper
                        .httpPostJson(PayNoticeActivity.this, url, json);
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

        PayNoticeListBean moreBean = JsonUtils.fromJson(data, PayNoticeListBean.class);
        if (moreBean.getServerResult().getResultCode() == 200) {
            if (moreBean.getMsgList().size() > 0) {
                List<PayNoticeListBean.MsgListBean> moreData = moreBean.getMsgList();
                pages = moreBean.getTotalPageNum();
                adapter.addItem(moreData);
                adapter.notifyDataSetChanged();
            }
        }

    }

    private static final int CLEAR_UNREAD = 200;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (CLEAR_UNREAD == resultCode) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    int position = bundle.getInt("position");
                    PayNoticeListBean.MsgListBean bean = list.get(position);
                    bean.setReadStatus(1);
                    adapter.RefreshData(list);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
