package com.wcyc.zigui2.newapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.adapter.VersionShuomingAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.UpdateInfo;
import com.wcyc.zigui2.newapp.bean.Versionupdateinfolist;
import com.wcyc.zigui2.newapp.module.duty.NewMyDutyAdapter;
import com.wcyc.zigui2.newapp.module.duty.NewMyDutyBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VersionShuomingActivity extends BaseActivity implements View.OnClickListener,HttpRequestAsyncTaskListener {
    private TextView new_content,no_data_iv;
    private LinearLayout title_back;
    private RefreshListView listview;
    private ArrayList<Versionupdateinfolist> versionupdateinfolists;// 展示list
    private VersionShuomingAdapter versionShuomingAdapter;
    private  int k=2;
    private int pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_shuoming);
        initViews();
        initDatas();
        initEvents();
    }

    private void initEvents() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putString("content",versionupdateinfolists.get(position-1).getContent());
                newActivity(UpdateInfoDetailActivity.class,bundle);

            }
        });
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        listview.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {// 下拉刷新
                // requestClassList();
                Log.e("versions","下拉");
                k = 2;
                initDatas();
                listview.hideHeaderView();// 收起下拉 新
            }

            @Override
            public void onLoadingMore() {// 上拉加载更多
                Log.e("versions","上拉");
                if (k <= pages) {// 后一页页， 总页数////如果后面还有数据// 则加载一页
                    loadData();
                } else if (k > pages) {
                    DataUtil.getToast("没有更多数据了");
                }
                listview.hideFooterView();
            }
        });
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);

    }

    private void initDatas() {
        new_content.setText("版本更新说明");
        versionupdateinfolists=new ArrayList<Versionupdateinfolist>();
        //newMyDutyList = new ArrayList<NewMyDutyBean>();


        JSONObject json = new JSONObject();
        try {

            json.put("curPage", "1");
            json.put("pageSize", "10");

            if (!DataUtil.isNetworkAvailable(this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            if (!isLoading()) {

                System.out.println("我的值班入参=====" + json);
                queryPost(Constants.VERSION_SHUOMING, json);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void initViews() {
        no_data_iv= (TextView) findViewById(R.id.tv_no_message);
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键
        listview= (RefreshListView) findViewById(R.id.listview);
    }
    // 加载更多数据
    public void loadData() {
        if (!DataUtil.isNetworkAvailable(getBaseContext())) {
            DataUtil.getToast("当前网络不可用，请检查您的网络设置");
            return;
        }

        if (!isLoading()) {
            loadDataByIndex(k);
            k++;
        }
    }
    // 加载更多
    public void loadDataByIndex(int index) {
        JSONObject json = new JSONObject();
        try {

            json.put("curPage", k);
            json.put("pageSize", "10");

            System.out.println("第" + k + "页" + "我的值班入参=====" + json);

            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.VERSION_SHUOMING).toString();// BASE_URL//SERVER_URL
//进度条

            new HttpRequestAsyncTask(json,this,this).execute(url);
//这里开始  错误
            //String result = HttpHelper.httpPostJson(this, url, json);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getMessage(String data) {
        Log.e("versiondata",data);
        NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
        if (ret.getServerResult().getResultCode() != 200) {// 请求失败
            DataUtil.getToast(ret.getServerResult().getResultMessage());
        } else {
            try {
                JSONObject json = new JSONObject(data);
                String pagesStr = json.getString("totalPageNum");
                pages = Integer.parseInt(pagesStr);
                System.out.println("我的值班pages=====" + pages);

                if (pages < 1) {
                    listview.setVisibility(View.GONE);
                    no_data_iv.setVisibility(View.VISIBLE);
                } else {
                    listview.setVisibility(View.VISIBLE);
                    no_data_iv.setVisibility(View.GONE);
                    String dutyRecordsStr = json.getString("versionUpdateInfoList");
                    JSONArray json2 = new JSONArray(dutyRecordsStr);
                    for (int i = 0; i < json2.length(); i++) {
                        Versionupdateinfolist versionupdateinfolist = JsonUtils.fromJson(json2
                                .get(i).toString(), Versionupdateinfolist.class);
                       versionupdateinfolists.add(versionupdateinfolist);
                        Log.e("versiondata",versionupdateinfolist.getTitle());
                        Log.e("versiondata",versionupdateinfolist.getUpdateTime());
                        //  newMyDutyList.add(newMyDutyBean);
                    }

                    versionShuomingAdapter = new VersionShuomingAdapter(getApplicationContext(), versionupdateinfolists);
                    listview.setAdapter(versionShuomingAdapter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                VersionShuomingActivity.this.finish();
                break;
            default:
                break;

        }
    }

    @Override
    public void onRequstComplete(String result) {
        ArrayList<Versionupdateinfolist> versionshuomingList_more = new ArrayList<Versionupdateinfolist>();

        JSONObject json3 = null;
        try {
            json3 = new JSONObject(result);
            String dutyRecordsStr = json3.getString("versionUpdateInfoList");
            JSONArray json2 = new JSONArray(dutyRecordsStr);
            for (int i = 0; i < json2.length(); i++) {
                Versionupdateinfolist versionupdateinfolist = JsonUtils.fromJson(json2
                        .get(i).toString(), Versionupdateinfolist.class);
                versionshuomingList_more.add(versionupdateinfolist);
                Log.e("versiondata", versionupdateinfolist.getTitle());
                //
            }
            versionShuomingAdapter.addItem(versionshuomingList_more);
            versionShuomingAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequstCancelled() {

    }
}
