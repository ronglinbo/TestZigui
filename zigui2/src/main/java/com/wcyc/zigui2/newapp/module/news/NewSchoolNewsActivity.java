package com.wcyc.zigui2.newapp.module.news;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.module.wages.NewWagesAdapter;
import com.wcyc.zigui2.newapp.module.wages.NewWagesBean;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.newapp.widget.RefreshListView1.OnRefreshListener;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

/**
 * 校园新闻
 *
 * @author 郑国栋 2016-6-30
 * @version 2.0
 */
public class NewSchoolNewsActivity extends BaseActivity implements
        OnClickListener {

    private LinearLayout title_back;
    private TextView new_content;
    private RefreshListView1 new_school_news_lv;
    private ArrayList<NewSchoolNewsBean> newSchoolNewsList;// 校园新闻list
    private NewSchoolNewsAdapter newSchoolNewsAdapter;
    private int k = 2;
    private int pages;// 总页数
    private ImageView no_data_iv;
    private String userid;
    private String userTpye;
    public static final String NEWS_REFESH_DATA = "com.wcyc.zigui.action.NEWS_REFESH_DATA";
    private int positiontemp = 0;
    private final int GET_SCHOOL_NEWS_LIST = 100;
    private final int GET_SCHOOL_NEWS_LIST_RDATA = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_school_news);

        initView();
        initDatas();
        initEvents();

//		SharedPreferences sp=getSharedPreferences("userData.dat", Context.MODE_PRIVATE);
        IntentFilter mrefeshDataFilter = new IntentFilter(NEWS_REFESH_DATA);
        registerReceiver(refeshDataReceiver, mrefeshDataFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
//		initDatas();
        if (positiontemp > 0) {
            int pageSizeRdata = (positiontemp - 1) / 10;
            if (pageSizeRdata >0) {
                k = pageSizeRdata+2;
            } else {
                k = 2;
            }
            pageSizeRdata = (pageSizeRdata + 1) * 10;

            try {
                userid = CCApplication.getInstance().getPresentUser()
                        .getUserId();
                userTpye = CCApplication.getInstance().getPresentUser().getUserType();
                JSONObject json = new JSONObject();
                json.put("schoolId", schoolId);
                json.put("userId", userid);
                json.put("userType", userTpye);//
                json.put("curPage", 1);
                json.put("pageSize", pageSizeRdata);
                if ("3".equals(userTpye)) {
                    String studentId = CCApplication.getInstance().getPresentUser().getChildId();
                    ;
                    json.put("studentId", studentId);
                    json.put("userType", "1");//模块数需要用1学生，2老师，3家长
                }

                if (!DataUtil.isNetworkAvailable(NewSchoolNewsActivity.this)) {
                    DataUtil.getToast(getResources().getString(R.string.no_network));
                    return;
                }

                if (!isLoading()) {
                    System.out.println("===校园新闻入参==" + json);
                    action = GET_SCHOOL_NEWS_LIST_RDATA;
                    queryPost(Constants.GET_SCHOOL_NEWS_LIST, json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 实例化组件
    private void initView() {
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键

        new_school_news_lv = (RefreshListView1) findViewById(R.id.new_school_news_lv);
        no_data_iv = (ImageView) findViewById(R.id.no_data_iv);// 无数据
    }

    // 初始化数据
    private void initDatas() {
        new_content.setText("校园新闻");
        k = 2;
        newSchoolNewsList = new ArrayList<NewSchoolNewsBean>();

        try {
            userid = CCApplication.getInstance().getPresentUser()
                    .getUserId();
            userTpye = CCApplication.getInstance().getPresentUser().getUserType();
            JSONObject json = new JSONObject();
            json.put("schoolId", schoolId);
            json.put("userId", userid);
            json.put("userType", userTpye);//
            json.put("curPage", 1);
            json.put("pageSize", "10");
            if ("3".equals(userTpye)) {
                String studentId = CCApplication.getInstance().getPresentUser().getChildId();
                ;
                json.put("studentId", studentId);
                json.put("userType", "1");//模块数需要用1学生，2老师，3家长
            }

            if (!DataUtil.isNetworkAvailable(NewSchoolNewsActivity.this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            if (!isLoading()) {
                System.out.println("===校园新闻入参==" + json);
                action = GET_SCHOOL_NEWS_LIST;
                queryPost(Constants.GET_SCHOOL_NEWS_LIST, json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 设置点击效果监听器
    private void initEvents() {
        title_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(this);

        new_school_news_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                positiontemp = arg2;
                Bundle bundle = new Bundle();
                bundle.putString("position", arg2 + "");
                bundle.putString("campuNewsId", newSchoolNewsList.get(arg2 - 1).getId());
                bundle.putString("isRead", newSchoolNewsList.get(arg2 - 1).getIsRead());

                int browseNo = newSchoolNewsList.get(arg2 - 1).getBrowseNo();
                browseNo += 1;
                newSchoolNewsList.get(arg2 - 1).setBrowseNo(browseNo);
                newSchoolNewsList.get(arg2 - 1).setIsRead("1");
                newSchoolNewsAdapter.notifyDataSetChanged();

                newActivity(NewSchoolNewsDetailsActivity.class, bundle);// 去校园新闻详情界面
            }
        });

        new_school_news_lv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return false;
            }
        });
        new_school_news_lv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {// 下拉刷新
                k = 2;
                initDatas();
                new_school_news_lv.hideHeaderView();// 收起下拉刷新
            }

            @Override
            public void onLoadingMore() {// 上拉加载更多
                if (k <= pages) {// 后一页页， 总页数////// 如果后面还有数据// 则加载一页
                    loadData();
                } else if (k > pages) {
                    DataUtil.getToast("没有更多数据了");
                }
                new_school_news_lv.hideFooterView();
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
            loadDataByIndex(k);
            k++;
        }
    }

    // 加载更多
    public void loadDataByIndex(int index) {

        JSONObject json = new JSONObject();
        try {
            json.put("schoolId", schoolId);
            json.put("userId", userid);
            json.put("userType", userTpye);//
            json.put("curPage", k);
            json.put("pageSize", "10");
            if ("3".equals(userTpye)) {
                String studentId = CCApplication.getInstance().getPresentUser().getChildId();
                ;
                json.put("studentId", studentId);
                json.put("userType", "1");//模块数需要用1学生，2老师，3家长
            }

            if (!DataUtil.isNetworkAvailable(NewSchoolNewsActivity.this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            if (!isLoading()) {
                System.out.println("第" + k + "页" + "===校园新闻入参==" + json);
                String url = new StringBuilder(Constants.SERVER_URL).append(
                        Constants.GET_SCHOOL_NEWS_LIST).toString();
                String result = HttpHelper.httpPostJson(this, url, json);
                ArrayList<NewSchoolNewsBean> newSchoolNewsBeanList_more = new ArrayList<NewSchoolNewsBean>();
                JSONObject json3 = new JSONObject(result);
                String campuNewsList = json3.getString("campuNewsList");
                JSONArray json2 = new JSONArray(campuNewsList);
                for (int i = 0; i < json2.length(); i++) {
                    NewSchoolNewsBean newSchoolNewsBean = JsonUtils.fromJson(json2.get(i)
                            .toString(), NewSchoolNewsBean.class);
                    newSchoolNewsBeanList_more.add(newSchoolNewsBean);
                }
                newSchoolNewsAdapter.addItem(newSchoolNewsBeanList_more);
                newSchoolNewsAdapter.notifyDataSetChanged();

            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                NewSchoolNewsActivity.this.finish();
                break;
            default:
                break;

        }
    }

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case GET_SCHOOL_NEWS_LIST:
                System.out.println("===校园新闻出参==" + data);
                NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
                if (ret.getServerResult().getResultCode() != 200) {// 请求失败
                    DataUtil.getToast(ret.getServerResult().getResultMessage());
                } else {

                    try {
                        JSONObject json = new JSONObject(data);
                        if (json.has("totalPageNum")) {
                            String pagesStr = json.getString("totalPageNum");
                            pages = Integer.parseInt(pagesStr);
                        }
                        if (pages < 1) {
                            new_school_news_lv.setVisibility(View.GONE);
                            no_data_iv.setVisibility(View.VISIBLE);
                        } else {
                            new_school_news_lv.setVisibility(View.VISIBLE);
                            no_data_iv.setVisibility(View.GONE);
                            String campuNewsList = json.getString("campuNewsList");
                            JSONArray json2 = new JSONArray(campuNewsList);
                            for (int i = 0; i < json2.length(); i++) {
                                NewSchoolNewsBean newSchoolNewsBean = JsonUtils.fromJson(json2
                                        .get(i).toString(), NewSchoolNewsBean.class);
                                newSchoolNewsList.add(newSchoolNewsBean);
                            }
//
                            newSchoolNewsAdapter = new NewSchoolNewsAdapter(this,
                                    newSchoolNewsList);
                            new_school_news_lv.setAdapter(newSchoolNewsAdapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case GET_SCHOOL_NEWS_LIST_RDATA:
                NewBaseBean retRdata = JsonUtils.fromJson(data, NewBaseBean.class);
                if (retRdata.getServerResult().getResultCode() != 200) {// 请求失败
                    DataUtil.getToast(retRdata.getServerResult().getResultMessage());
                } else {
                    try {
                        JSONObject json = new JSONObject(data);
                        String campuNewsList = json.getString("campuNewsList");
                        JSONArray json2 = new JSONArray(campuNewsList);
                        newSchoolNewsList.clear();
                        for (int i = 0; i < json2.length(); i++) {
                            NewSchoolNewsBean newSchoolNewsBean = JsonUtils.fromJson(json2
                                    .get(i).toString(), NewSchoolNewsBean.class);
                            newSchoolNewsList.add(newSchoolNewsBean);
                        }
                        new_school_news_lv.setSelection(positiontemp);
                        newSchoolNewsAdapter.notifyDataSetInvalidated();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }


    }

    private BroadcastReceiver refeshDataReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
//			initDatas();//其实浏览数不对没关系，这个反而不合理，如图是第62条新闻，这个会从第一条加载，二不是定位到第62条
            //listview.setSelection(int position);
            //adapter.notifyDataSetInvalidated();通知adapter数据有变化
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(refeshDataReceiver);
    }
}
