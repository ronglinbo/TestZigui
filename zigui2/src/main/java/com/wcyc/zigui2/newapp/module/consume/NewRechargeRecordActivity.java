package com.wcyc.zigui2.newapp.module.consume;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * 充值记录界面
 * Created by xiehua on 2017/5/3.
 */

public class NewRechargeRecordActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 标题
     */
    private TextView new_content;
    /**
     * 返回键
     */
    private LinearLayout title_back;
    /**
     * 无数据显示的空视图
     */
    private ImageView no_data_iv;
    /**
     * 自定义的ListView
     */
    private RefreshListView new_recharge_record_lv;

    /**
     * 展示list
     */
    private ArrayList<NewRechargeRecordBean> cardConsumeList;

    /**
     * 总页数
     */
    private int pages;

    private NewRechargeRecordAdapter newRechargeRecordAdapter;

    private int count = 2;
    private String userId;
    private String userType;
    private String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recharge_record);

        initView();
        initDates();
        initEvents();
    }

    //初始化控件
    private void initView() {
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键

        new_recharge_record_lv = (RefreshListView) findViewById(R.id.new_recharge_record_lv);
        no_data_iv = (ImageView) findViewById(R.id.no_data_iv);
    }

    //初始化数据
    private void initDates() {
        new_content.setText("充值记录");
        cardConsumeList = new ArrayList<NewRechargeRecordBean>();

        try {
            userId = CCApplication.getInstance().getPresentUser()
                    .getUserId();
            userType = CCApplication.getInstance().getPresentUser().getUserType();

            JSONObject json = new JSONObject();
            json.put("schoolId", schoolId);
            if ("2".equals(userType)) {
                json.put("userId", userId);
            } else if ("3".equals(userType)) {
                childId = CCApplication.getInstance().getPresentUser().getChildId();
                json.put("userId", childId);
            }
            json.put("userType", userType);
            json.put("curPage", "1");
            json.put("pageSize", "10");
            json.put("flag", "2");
            //网络是否可用
            if (!DataUtil.isNetworkAvailable(NewRechargeRecordActivity.this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }
            //是否在上一个请求中
            if (!isLoading()) {
                System.out.println("充值记录入参=====" + json);
                queryPost(Constants.GET_CONSUME_INFO, json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置点击事件的监听
    private void initEvents() {
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);

        new_recharge_record_lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        new_recharge_record_lv.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {   //下拉刷新
                count = 2;
                initDates();
                new_recharge_record_lv.hideHeaderView();    //收起下拉刷新
            }

            @Override
            public void onLoadingMore() {   //上拉加载更多
                if (count <= pages) {// 后一页页， 总页数////// 如果后面还有数据// 则加载一页
                    loadData();
                } else if (count > pages) {
                    DataUtil.getToast("没有更多数据了");
                }
                new_recharge_record_lv.hideFooterView();
            }
        });
    }

    // 加载更多数据
    public void loadData() {
        if (!DataUtil.isNetworkAvailable(getBaseContext())) {
            DataUtil.getToast("当前网络不可用，请检查您的网络设置");
            return;
        }
        if (!isLoading()) {
            loadDataByIndex(count);
            count++;
        }
    }

    // 加载更多
    public void loadDataByIndex(int index) {
        JSONObject json = new JSONObject();
        try {
            json.put("schoolId", schoolId);
            if("2".equals(userType)){
                json.put("userId", userId);
            }else if("3".equals(userType)){
                json.put("userId", childId);
            }
            json.put("userType", userType);
            json.put("curPage", count);
            json.put("pageSize", "10");
            json.put("flag", "2");

            System.out.println("第" + count + "页" + "消费记录入参=====" + json);
            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.GET_CONSUME_INFO).toString();//
            String result = HttpHelper.httpPostJson(this, url, json);
            ArrayList<NewRechargeRecordBean> consumerecoadList_more = new ArrayList<NewRechargeRecordBean>();
            JSONObject json3 = new JSONObject(result);
            String cardConsumeListStr = json3.getString("cardConsumeList");//获取列表数据
            JSONArray json2 = new JSONArray(cardConsumeListStr);
            for (int i = 0; i < json2.length(); i++) {
                NewRechargeRecordBean newRechargeRecordBean = JsonUtils.fromJson(json2.get(i)
                        .toString(), NewRechargeRecordBean.class);//封装数据
                consumerecoadList_more.add(newRechargeRecordBean);
            }
            newRechargeRecordAdapter.addItem(consumerecoadList_more);//添加数据
            newRechargeRecordAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getMessage(String data) {
        NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
        if (ret.getServerResult().getResultCode() != 200) {// 请求失败
            DataUtil.getToast(ret.getServerResult().getResultMessage());
        } else {
            try {
                JSONObject json = new JSONObject(data);
                String pagesStr = json.getString("pages");
                pages = Integer.parseInt(pagesStr);
                System.out.println("消费记录出参pages=====" + pages);
                if (pages < 1) {//无数据
                    new_recharge_record_lv.setVisibility(View.GONE);
                    no_data_iv.setVisibility(View.VISIBLE);
                } else {//有数据
                    new_recharge_record_lv.setVisibility(View.VISIBLE);
                    no_data_iv.setVisibility(View.GONE);

                    String cardConsumeListStr = json.getString("cardConsumeList");
                    JSONArray json2 = new JSONArray(cardConsumeListStr);
                    for (int i = 0; i < json2.length(); i++) {
                        NewRechargeRecordBean newRechargeRecordBean = JsonUtils.fromJson(json2
                                .get(i).toString(), NewRechargeRecordBean.class);
                        cardConsumeList.add(newRechargeRecordBean);
                    }
                    //设置adapter
                    newRechargeRecordAdapter = new NewRechargeRecordAdapter(this,
                            cardConsumeList);
                    new_recharge_record_lv.setAdapter(newRechargeRecordAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                NewRechargeRecordActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
