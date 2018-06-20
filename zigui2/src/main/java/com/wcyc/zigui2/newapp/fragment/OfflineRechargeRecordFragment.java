package com.wcyc.zigui2.newapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.module.consume.NewRechargeRecordAdapter;
import com.wcyc.zigui2.newapp.module.consume.NewRechargeRecordBean;
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
 * 线下充值支付记录_原来版本的充值记录
 */
public class OfflineRechargeRecordFragment extends Fragment implements HttpRequestAsyncTaskListener {


    private View viewParent;
    private TextView new_content;
    private LinearLayout title_back;
    private ImageView no_data_iv;
    private RefreshListView new_recharge_record_lv;
    private ArrayList<NewRechargeRecordBean> cardConsumeList;
    private int pages;
    private NewRechargeRecordAdapter newRechargeRecordAdapter;
    private int count = 2;
    private String userId;
    private String userType;
    private String childId;
    private Context context;
    private String schoolId;
    private BaseActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewParent = inflater.inflate(R.layout.fragment_offline_record, container, false);

        context = getActivity();
        initView();
        initDatas();
        return viewParent;
    }

    private void initDatas() {
        new_content.setText("充值记录");
        cardConsumeList = new ArrayList<>();

        try {
            userId = CCApplication.getInstance().getPresentUser()
                    .getUserId();
            userType = CCApplication.getInstance().getPresentUser().getUserType();
            schoolId = CCApplication.getInstance().getPresentUser().getSchoolId();

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
            if (!DataUtil.isNetworkAvailable(getActivity())) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            activity = (BaseActivity) getActivity();
            //是否在上一个请求中
            if (!activity.isLoading()) {
                System.out.println("充值记录入参=====" + json);
                new HttpRequestAsyncTask(json, this, context).execute(Constants.GET_CONSUME_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        new_content = (TextView) viewParent.findViewById(R.id.new_content);// 标题
        title_back = (LinearLayout) viewParent.findViewById(R.id.title_back);// 返回键
        new_recharge_record_lv = (RefreshListView) viewParent.findViewById(R.id.new_recharge_record_lv);
        no_data_iv = (ImageView) viewParent.findViewById(R.id.no_data_iv);
        title_back.setVisibility(View.VISIBLE);

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
                initDatas();
                new_recharge_record_lv.hideHeaderView();    //收起下拉刷新
            }

            @Override
            public void onLoadingMore() {   //上拉加载更多
                if (count <= pages) {// 后一页页， 总页数////// 如果后面还有数据// 则加载一页
                    loadData();
                } else if (count > pages) {
                    DataUtil.getToastShort("没有更多数据了");
                }
                new_recharge_record_lv.hideFooterView();
            }
        });
    }


    public static Fragment newInstance(int index) {
        Fragment fragment = new OfflineRechargeRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onRequstComplete(String data) {
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
                    newRechargeRecordAdapter = new NewRechargeRecordAdapter(context, cardConsumeList);
                    new_recharge_record_lv.setAdapter(newRechargeRecordAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequstCancelled() {
        DataUtil.getToastShort("加载数据取消");
    }


    // 加载更多数据
    public void loadData() {
        if (!DataUtil.isNetworkAvailable(context)) {
            DataUtil.getToast("当前网络不可用，请检查您的网络设置");
            return;
        }
        if (!activity.isLoading()) {
            loadDataByIndex(count);
            count++;
        }
    }

    // 加载更多
    public void loadDataByIndex(int index) {
        JSONObject json = new JSONObject();
        try {
            json.put("schoolId", schoolId);
            if ("2".equals(userType)) {
                json.put("userId", userId);
            } else if ("3".equals(userType)) {
                json.put("userId", childId);
            }
            json.put("userType", userType);
            json.put("curPage", count);
            json.put("pageSize", "10");
            json.put("flag", "2");

            System.out.println("第" + count + "页" + "消费记录入参=====" + json);
            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.GET_CONSUME_INFO).toString();//
            String result = HttpHelper.httpPostJson(context, url, json);
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

}
