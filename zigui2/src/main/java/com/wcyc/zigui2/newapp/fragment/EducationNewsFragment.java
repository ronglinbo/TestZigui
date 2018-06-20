package com.wcyc.zigui2.newapp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.adapter.EducationRecommendAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.adapter.EducationNewsAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.bean.EducationNewsBean;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @author zzc
 * @time 2017/11/29 0029
 */
public class EducationNewsFragment extends Fragment implements HttpRequestAsyncTaskListener {

    private View parentView;

    EducationNewsAdapter adapter;
    private String configName;
    private int id;
    private RefreshListView1 lv_news;
    private ImageView no_data_iv;
    private List<EducationNewsBean.CampuEcirclesListBean> campuEcirclesList;
    private int k = 2;
    private int pages;// 总页数
    private int position = 0;//位置 第几个跳转的
    private int positiontemp = 0;

    public static Fragment newInstance(int id, String configName) {
        Fragment fragment = new EducationNewsFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("configName", configName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configName = getArguments().getString("configName");
        id = getArguments().getInt("id");
    }


    private BroadcastReceiver refreshDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean refreshData = intent.getBooleanExtra("RefreshData", false);
            System.out.println(refreshData);

            if (refreshData) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                k = 2;
                                position = 0;
                                initDatas();
                            }
                        });
                    }
                }).start();
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        IntentFilter mRefreshDataFilter = new IntentFilter(EducationRecommendAdapter.REFRESH_DATA);
        getActivity().registerReceiver(refreshDataReceiver, mRefreshDataFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //ViewPager页面切换 会重复调用onCreateView
        if (parentView == null) {
            parentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_education_world, container, false);
            initView();
            initDatas();
            initEvents();
        }
        return parentView;
    }

    private void initView() {
        lv_news = (RefreshListView1) parentView.findViewById(R.id.lv_news);
        no_data_iv = (ImageView) parentView.findViewById(R.id.no_data_iv);
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long l) {
                positiontemp = arg2;
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        k = 2;
        campuEcirclesList = new ArrayList<>();
        JSONObject json = new JSONObject();
        UserType user = CCApplication.app.getPresentUser();
        if (user == null) {
            return;
        }
        try {
            String userType = user.getUserType();
            json.put("userType", userType);

            if (userType.equals("3")) {
                json.put("studentId", user.getChildId());
            } else {
                json.put("userId", user.getUserId());
            }
            json.put("ecTypeId", id);
            json.put("pageSize", 10);
            json.put("curPage", 1);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new HttpRequestAsyncTask(json, this, getActivity(), true).execute(Constants.GET_ECIRCLES_LIST_BY_ECTYPE_ID);
    }


    // 设置点击事件监听器
    private void initEvents() {

        lv_news.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return false;
            }
        });

        lv_news.setOnRefreshListener(new RefreshListView1.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {// 下拉刷新
                k = 2;
                position = 0;
                initDatas();
                lv_news.hideHeaderView();// 收起下拉刷新
            }

            @Override
            public void onLoadingMore() {// 上拉加载更多
                if (k <= pages) {// 后一页页， 总页数////// 如果后面还有数据// 则加载一页
                    loadData();
                } else if (k > pages) {
                    DataUtil.getToast("没有更多数据了");
                }
                lv_news.hideFooterView();
            }

        });

    }


    // 加载更多数据
    public void loadData() {
        if (!DataUtil.isNetworkAvailable(getActivity())) {
            DataUtil.getToast("当前网络不可用，请检查您的网络设置");
            return;
        }
        loadDataByIndex();
        k++;
    }


    /**
     * 分页加载
     */
    public void loadDataByIndex() {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.app.getPresentUser();

        if (user == null) {
            return;
        }

        try {
            String userType = user.getUserType();
            json.put("userType", userType);

            if (userType.equals("3")) {
                json.put("studentId", user.getSchoolId());
            } else {
                json.put("userId", user.getUserId());
            }
            json.put("ecTypeId", id);
            json.put("pageSize", 10);
            json.put("curPage", k);


            if (!DataUtil.isNetworkAvailable(getActivity())) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            System.out.println("第" + k + "页" + "==教育资讯入参===" + json);
            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.GET_ECIRCLES_LIST_BY_ECTYPE_ID).toString();
            String result = HttpHelper.httpPostJson(this.getActivity(), url, json);


            EducationNewsBean bean = JsonUtils.fromJson(result, EducationNewsBean.class);
            List<EducationNewsBean.CampuEcirclesListBean> list = bean.getCampuEcirclesList();
            if (list.size() > 0) {
                adapter.addItem(list);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {

        }
    }


    /**
     * ViewPager切换页面的时候 将其销毁
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (parentView != null) {
            ViewGroup parent = (ViewGroup) parentView.getParent();
            parent.removeView(parentView);
        }
    }

    @Override
    public void onRequstComplete(String result) {
        EducationNewsBean newsListBean = JsonUtils.fromJson(result, EducationNewsBean.class);
        if (newsListBean.getServerResult().getResultCode() == 200) {
            showContentView(result);
        } else {
            showErrorView();
        }
    }

    private void showErrorView() {
        lv_news.setVisibility(View.GONE);
        no_data_iv.setVisibility(View.VISIBLE);
        no_data_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDatas();
            }
        });
    }

    private void showContentView(String data) {

        lv_news.setVisibility(View.VISIBLE);
        no_data_iv.setVisibility(View.GONE);

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
                    lv_news.setVisibility(View.GONE);
                    no_data_iv.setVisibility(View.VISIBLE);
                } else {
                    lv_news.setVisibility(View.VISIBLE);
                    no_data_iv.setVisibility(View.GONE);

                    EducationNewsBean bean = JsonUtils.fromJson(data, EducationNewsBean.class);
                    campuEcirclesList = bean.getCampuEcirclesList();
                    adapter = new EducationNewsAdapter(campuEcirclesList, this, getActivity());
                    lv_news.setAdapter(adapter);

                    if (position > 0) {
                        if (lv_news != null && adapter != null) {
                            System.out.println("===position==" + position);
                            lv_news.setSelection(position);
                            adapter.notifyDataSetInvalidated();
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequstCancelled() {
        DataUtil.getToast("请求取消");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_FIRST_USER) {
            if (RESULT_OK == resultCode) {
                if (data != null) {
                    int position = data.getIntExtra("position", 0);
                    String no = data.getStringExtra("readNO");
                    if (!DataUtil.isNullorEmpty(no)) {
                        EducationNewsBean.CampuEcirclesListBean bean = campuEcirclesList.get(position);
                        if (no.equals("99999+")) {
                            bean.setBrowseNo(100000);
                        } else {
                            bean.setBrowseNo(Integer.parseInt(no));
                        }
                        adapter.RefreshData(campuEcirclesList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

}
