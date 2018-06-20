package com.wcyc.zigui2.newapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.adapter.EducationRecommendAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.Category;
import com.wcyc.zigui2.newapp.bean.EducationNewsBean;
import com.wcyc.zigui2.newapp.bean.RecommendNewsBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.home.SchoolIntroductionActivity;
import com.wcyc.zigui2.newapp.module.educationinfor.EducationDetailsActivity;
import com.wcyc.zigui2.newapp.module.news.NewSchoolNewsDetailsActivity;
import com.wcyc.zigui2.newapp.widget.RefreshListView1;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.HttpHelper;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 教育资讯-推荐新闻模块
 * <p>
 * 推荐  最新第一次默认加载3个  下拉刷新一次加载10个
 */
public class EducationRecommendFragment extends Fragment implements HttpRequestAsyncTaskListener {

    private View parentView;
    private RefreshListView1 lv_recomment;
    private ImageView no_data_iv;
    private ArrayList<Category> listData;
    private EducationRecommendAdapter educationRecommendAdapter;
    private View headerView;
    private ImageView iv_loading;
    private WebView ad;
    private JsInterface jsInterface;
    private Category hotNews;  //热门新闻
    private Category currentNews; //最新新闻
    private Category recommendNews; //推荐新闻

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (parentView == null) {
            parentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_education_recomment, container, false);
            initView();
            initData();
        }
        return parentView;
    }

    private void initView() {
        lv_recomment = (RefreshListView1) parentView.findViewById(R.id.lv_recomment);
        no_data_iv = (ImageView) parentView.findViewById(R.id.no_data_iv);
    }

    private void initData() {
        getNewsList();
    }


    private int k = 2;

    /**
     * 获取到推荐新闻的列表
     */
    private void getNewsList() {
        JSONObject json = new JSONObject();
        UserType user = CCApplication.app.getPresentUser();

        if (user == null) {
            return;
        }
        try {
            String userType = user.getUserType();
            if (userType.equals("3")) {
                json.put("studentId", user.getChildId());
            } else {
                json.put("userId", user.getUserId());
            }
            json.put("userType", userType);
            json.put("pageSize", 3);
            json.put("curPage", 1);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        new HttpRequestAsyncTask(json, this, getActivity(), true).execute(Constants.GET_EDUCATION_CIRCLES_LIST_BY_RECOMMEND);
    }


    @Override
    public void onRequstComplete(String result) {
        RecommendNewsBean bean = JsonUtils.fromJson(result, RecommendNewsBean.class);

        //获取到第一个totalPageNum
        pages = bean.getTotalPageNum();
        if (bean.getServerResult().getResultCode() == 200) {
            parseData(result);
        } else {
            showEmptyContent();
        }
    }

    @Override
    public void onRequstCancelled() {

    }


    /**
     * 显示错误数据
     */
    private void showEmptyContent() {
        no_data_iv.setVisibility(View.VISIBLE);
        lv_recomment.setVisibility(View.GONE);

        //重新加载
        no_data_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewsList();
            }
        });
    }

    private int position = 0;
    private int pages = 0; //总页数

    private void parseData(String data) {

        listData = formatList(data);
        if (listData != null) {
            showNewsContent();
        } else {
            lv_recomment.setVisibility(View.GONE);
            no_data_iv.setVisibility(View.VISIBLE);
        }
    }

    private void showNewsContent() {
        educationRecommendAdapter = new EducationRecommendAdapter(this, getActivity(), listData);
        // 适配器与ListView绑定
        lv_recomment.setAdapter(educationRecommendAdapter);
        lv_recomment.setOnRefreshListener(new RefreshListView1.OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                k = 2;
                position = 0;
                listData.clear();
                educationRecommendAdapter.notifyDataSetChanged();
                getNewsList();
                loadBanner();
                lv_recomment.hideHeaderView();
            }

            @Override
            public void onLoadingMore() {
                if (k <= pages) {// 当前页面是否
                    loadData();
                } else if (k > pages) {
                    DataUtil.getToastShort("没有更多数据了");
                }
                lv_recomment.hideFooterView();
            }
        });

        if (position > 0) {
            if (lv_recomment != null && educationRecommendAdapter != null) {
                System.out.println("===position==" + position);
                lv_recomment.setSelection(position);
                educationRecommendAdapter.notifyDataSetInvalidated();
            }
        }

        if (headerView == null) {
            headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_header_news, null, true);
            iv_loading = (ImageView) headerView.findViewById(R.id.iv_loading);
            ad = (WebView) headerView.findViewById(R.id.ad);

            ad.getSettings().setAppCacheEnabled(false);    //取消缓存
            ad.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
            ad.getSettings().setJavaScriptEnabled(true);    //设置WebView属性，能够执行JavaScript脚本

            ad.removeJavascriptInterface("searchBoxJavaBridge_");
            ad.removeJavascriptInterface("accessibility");
            ad.removeJavascriptInterface("accessibilityTraversal");

            ad.getSettings().setSavePassword(false);
            jsInterface = new JsInterface();
            ad.addJavascriptInterface(jsInterface, "android");
            ad.setWebViewClient(new WebViewClient() {        //web 视图
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            ad.setBackgroundColor(Color.parseColor("#eeeeee")); // 设置背景色
            ad.getBackground().setAlpha(1); // 设置填充透明度 范围：0-255

            //去掉纵向滚动条
            ad.setVerticalScrollBarEnabled(false);


            loadBanner();
            ListView.LayoutParams params = new ListView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            headerView.setLayoutParams(params);
            lv_recomment.addHeaderView(headerView);
        }

        lv_recomment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (DataUtil.pd == null || !DataUtil.pd.isShowing()) {
                    DataUtil.showDialog(getActivity());
                }
                positiontemp = position;
            }
        });
    }


    /**
     * 加载更多
     */
    private void loadData() {
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
            if (userType.equals("3")) {
                json.put("studentId", user.getSchoolId());
            } else {
                json.put("userId", user.getUserId());
            }
            json.put("userType", userType);
            json.put("pageSize", 10);
            json.put("curPage", k);

            if (!DataUtil.isNetworkAvailable(getActivity())) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }

            String url = new StringBuilder(Constants.SERVER_URL).append(
                    Constants.GET_EDUCATION_CIRCLES_LIST_BY_RECOMMEND).toString();
            String result = HttpHelper.httpPostJson(this.getActivity(), url, json);

            RecommendNewsBean bean = JsonUtils.fromJson(result, RecommendNewsBean.class);
            pages = bean.getTotalPageNum();

            List<RecommendNewsBean.CampuEcirclesListBean> list = bean.getCampuEcirclesList();
            if (list != null && list.size() > 0) {
                int beforeCount = recommendNews.getItemCount();
                for (RecommendNewsBean.CampuEcirclesListBean item : list) {
                    if (item.getClassify().equals("推荐")) {
                        recommendNews.addItem(item);
                    }
                }
                //获取到最新的推荐数据.
                int currentCount = recommendNews.getItemCount();
                if (beforeCount < currentCount) {
                    listData.set(2, recommendNews);
                    educationRecommendAdapter.refreshData(listData);
                    educationRecommendAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ArrayList<Category> formatList(String data) {

        RecommendNewsBean bean = JsonUtils.fromJson(data, RecommendNewsBean.class);
        List<RecommendNewsBean.CampuEcirclesListBean> list = bean.getCampuEcirclesList();
        hotNews = new Category("热门");
        currentNews = new Category("最新");
        recommendNews = new Category("推荐");

        if (list != null) {
            for (RecommendNewsBean.CampuEcirclesListBean ecirclesListBean : list) {
                if (ecirclesListBean.getClassify().equals("热门")) {
                    ecirclesListBean.setHideBrowserNO(true);
                    hotNews.addItem(ecirclesListBean);
                }
                if (ecirclesListBean.getClassify().equals("最新")) {
                    ecirclesListBean.setHideBrowserNO(false);
                    currentNews.addItem(ecirclesListBean);
                }
                if (ecirclesListBean.getClassify().equals("推荐")) {
                    ecirclesListBean.setHideBrowserNO(false);
                    recommendNews.addItem(ecirclesListBean);
                }

            }

            listData = new ArrayList<>();
            listData.add(currentNews);
            listData.add(hotNews);
            listData.add(recommendNews);
        }

        return listData;
    }


    private int positiontemp = 0;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_FIRST_USER) {
            if (RESULT_OK == resultCode) {
                if (data != null) {
                    int position = data.getIntExtra("position", 0);
                    String no = data.getStringExtra("readNO");
                    RecommendNewsBean.CampuEcirclesListBean bean = (RecommendNewsBean.CampuEcirclesListBean) educationRecommendAdapter.getItem(position);
                    if (bean != null && !DataUtil.isNullorEmpty(no)) {
                        if (no.equals("99999+")) {
                            bean.setBrowseNo(100000);
                        } else {
                            bean.setBrowseNo(Integer.parseInt(no));
                        }
                        educationRecommendAdapter.refreshData(listData);
                        educationRecommendAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ad != null) {
            loadBanner();
        }
    }

    /**
     * WebView的点击效果
     */
    public class JsInterface {
        //学校简介
        @JavascriptInterface
        public void openADUrl(String linkedType, String linkedUrl, String relationId) {
            //学校简介
            if (linkedType.equals("4")) {
                Intent intent = new Intent(getActivity(), SchoolIntroductionActivity.class);
                intent.putExtra("url", linkedUrl);
                intent.putExtra("title", "学校简介");
                getActivity().startActivity(intent);
            }
            //教育资讯链接
            if (linkedType.equals("0")) {
                Intent intent = new Intent(getActivity(), EducationDetailsActivity.class);
                intent.putExtra("campuNewsId", relationId);
                intent.putExtra("title", "教育资讯");
                getActivity().startActivity(intent);
            }
            //校园新闻链接
            if (linkedType.equals("1")) {
                Intent intent = new Intent(getActivity(), NewSchoolNewsDetailsActivity.class);
                intent.putExtra("campuNewsId", relationId);
                intent.putExtra("title", "校园新闻");
                getActivity().startActivity(intent);
            }

            //外部链接
            if (linkedType.equals("2")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(linkedUrl);
                intent.setData(content_url);
                startActivity(intent);
            }
        }
    }

    /**
     * 切换Fragment的时候 相距2个Tab的时候会调用此方法
     */
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 切换Fragment并不会调用此方法
     * 只有销毁掉Activity的时候才会调用
     */
    @Override
    public void onDestroy() {
        if (ad != null) {
            ad.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            ad.clearHistory();

            ((ViewGroup) ad.getParent()).removeView(ad);
            ad.destroy();
            ad = null;
        }
        super.onDestroy();
    }

    private void loadBanner() {
        UserType user = CCApplication.getInstance().getPresentUser();
        HashMap<String, String> para = new HashMap<>();
        String imgAuthId = Constants.AUTHID + "@" + ((BaseActivity) getActivity()).getDeviceID()
                + "@" + CCApplication.app.getMemberInfo().getAccId();

        para.put("X-School-Id", user.getSchoolId());
        para.put("X-mobile-Type", "android");
        para.put("imgAuthId", imgAuthId);

        //教育资讯轮播图
        String url = Constants.URL + "/app_slideshow/eduSlideshow.do";
        ad.loadUrl(url, para);
    }

}
