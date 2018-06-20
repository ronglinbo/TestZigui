package com.wcyc.zigui2.newapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.adapter.OnlineRechargeAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.OnlineRechargeRecordBean;
import com.wcyc.zigui2.newapp.module.consume.NewRechargeRecordAdapter;
import com.wcyc.zigui2.newapp.module.consume.NewRechargeRecordBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzc
 * @time 2018/3/29
 */
public class OnlineRechargeFragment extends Fragment implements HttpRequestAsyncTaskListener {


    private View viewParent;
    private RefreshListView lv_online_record;
    private ImageView iv_empty;
    private int count = 2;
    private int pages;
    private BaseActivity activity;
    private String userId;
    private String userType;
    private String schoolId;
    private String childId;
    private Context context;
    private OnlineRechargeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewParent = inflater.inflate(R.layout.fragment_online_record, container, false);
        context = getActivity();
        initView();
        initData();
        return viewParent;
    }

    private void initView() {
        lv_online_record = (RefreshListView) viewParent.findViewById(R.id.lv_online_record);
        iv_empty = (ImageView) viewParent.findViewById(R.id.iv_empty);
        lv_online_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        lv_online_record.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {   //下拉刷新
                count = 2;
                initData();
                lv_online_record.hideHeaderView();    //收起下拉刷新
            }

            @Override
            public void onLoadingMore() {   //上拉加载更多
                if (count <= pages) {// 后一页页， 总页数////// 如果后面还有数据// 则加载一页
                    loadData();
                } else if (count > pages) {
                    DataUtil.getToastShort("没有更多数据了");
                }
                lv_online_record.hideFooterView();
            }
        });
    }

    private void initData() {
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
            //网络是否可用
            if (!DataUtil.isNetworkAvailable(getActivity())) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            activity = (BaseActivity) getActivity();
            //是否在上一个请求中
            if (!activity.isLoading()) {
                System.out.println("充值记录入参=====" + json);
                new HttpRequestAsyncTask(json, this, context, true).execute(Constants.GET_ONLINE_RECORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadData() {
        if (!DataUtil.isNetworkAvailable(context)) {
            DataUtil.getToast("当前网络不可用，请检查您的网络设置");
            return;
        }
        if (!activity.isLoading()) {
            loadDataByIndex(count);
            count++;
        }
    }

    private void loadDataByIndex(int count) {
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


            System.out.println("第" + count + "页" + "线下消费记录入参=====" + json);
            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.GET_ONLINE_RECORD).toString();//
            String result = HttpHelper.httpPostJson(context, url, json);
            OnlineRechargeRecordBean bean = JsonUtils.fromJson(result, OnlineRechargeRecordBean.class);
            if(!bean.getCardRechargeList().isEmpty()){
                adapter.addItem(bean.getCardRechargeList());
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Fragment newInstance(int index) {
        Fragment fragment = new OnlineRechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onRequstComplete(String data) {
        System.out.println(data);

        if (DataUtil.isNullorEmpty(data)) {
            showEmptyView();
        } else {
            OnlineRechargeRecordBean bean = JsonUtils.fromJson(data, OnlineRechargeRecordBean.class);
            if (bean.getServerResult().getResultCode() == 200) {
                List<OnlineRechargeRecordBean.CardRechargeListBean> rechargeList = bean.getCardRechargeList();
                if (rechargeList.isEmpty()) {
                    showEmptyView();
                } else {
                    pages = bean.getPages();
                    showContentView(rechargeList);
                }
            } else {
                showEmptyView();
            }
        }


    }

    private void showContentView(List<OnlineRechargeRecordBean.CardRechargeListBean> rechargeList) {
        adapter = new OnlineRechargeAdapter(context, rechargeList);
        lv_online_record.setAdapter(adapter);
    }

    private void showEmptyView() {
        lv_online_record.setVisibility(View.GONE);
        iv_empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequstCancelled() {

    }
}
