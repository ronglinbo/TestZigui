package com.wcyc.zigui2.newapp.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.adapter.EducationSearchAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.bean.EducationSearchBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 教育资讯搜索Activity
 *
 * @author zzc
 * @time 2017/12/6 0006
 */
public class EducationSearchActivity extends BaseActivity {
    private static final int ACTION_SEARCH_NEWS_BY_TITLE = 0x0001;
    private TextView cancel;
    private ListView lv_result;
    private SearchView search;
    private EducationSearchAdapter adapter;
    private List<EducationSearchBean.EcirclesListBean> list = new ArrayList<>();

    @Override
    protected void getMessage(String data) {
        switch (action) {
            case ACTION_SEARCH_NEWS_BY_TITLE:
                parseSearchResult(data);
                break;
        }
    }

    /**
     * 解析搜索结果
     *
     * @param data
     */
    private void parseSearchResult(String data) {
        EducationSearchBean searchBean = JsonUtils.fromJson(data, EducationSearchBean.class);
        if (searchBean.getServerResult().getResultCode() == 200) {
            showListData(searchBean);
        } else {
            showEmptyView();
        }
    }

    private void showEmptyView() {

    }

    private void showListData(EducationSearchBean searchBean) {

        //确保每次搜索都是最新的数据
        list.clear();
        list = searchBean.getEcirclesList();
        adapter = new EducationSearchAdapter(this, list);
        lv_result.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_search);
        initView();
    }


    private void initView() {
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lv_result = (ListView) findViewById(R.id.lv_result);
        search = (SearchView) findViewById(R.id.search);
        search.setQueryHint("请输入关键字");

        //搜索图标是否在框内
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String arg0) {
                // TODO Auto-generated method stub

                setSearchAapter(arg0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                // TODO Auto-generated method stub
                if (newText != null && newText.length() > 0) {
                    setSearchAapter(newText);
                }

                return false;
            }
        });
    }


    /**
     * 查找
     *
     * @param key
     */
    private void setSearchAapter(String key) {
        if (!isLoading()) {
            JSONObject json = new JSONObject();
            try {
                json.put("title", key);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            action = ACTION_SEARCH_NEWS_BY_TITLE;
            queryPost(Constants.GET_ECIRCLES_LIST_BY_TITLE, json);
        }
    }

}
