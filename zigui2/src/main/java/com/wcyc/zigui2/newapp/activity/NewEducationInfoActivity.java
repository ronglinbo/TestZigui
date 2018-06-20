package com.wcyc.zigui2.newapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.adapter.FragmentAdapter;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.bean.EducationNewsTitleBean;
import com.wcyc.zigui2.newapp.bean.UserType;
import com.wcyc.zigui2.newapp.fragment.EducationNewsFragment;
import com.wcyc.zigui2.newapp.fragment.EducationRecommendFragment;
import com.wcyc.zigui2.newapp.widget.NoScrollViewPager;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.JsonUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 新版 教育咨询 Fragment+ViewPager实现
 * 原版 EducationInforActivity 仅仅显示ListView
 *
 * @author zzc
 * @time 2017/12/5 0005
 */
public class NewEducationInfoActivity extends BaseActivity implements View.OnClickListener, HttpRequestAsyncTaskListener {
    private static final int ACTION_GET_NEWS_TYPE = 0x0001;
    private ImageView iv_back;
    private ImageView iv_search;
    private MagicIndicator magicIndicator;
    private NoScrollViewPager mViewPager;

    private List<Fragment> listFragment; //创建一个List<Fragment>
    private RelativeLayout rl_no_data;
    private LinearLayout ll_content;
    private TextView tv_titile;
    private LinearLayout title_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_educationinfo);
        initView();
        initData();
    }

    private void initView() {

        //数据不为空布局
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        mViewPager = (NoScrollViewPager) findViewById(R.id.view_pager);
        iv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        rl_no_data = (RelativeLayout) findViewById(R.id.rl_no_data);
        rl_no_data.setVisibility(View.GONE);
        tv_titile = (TextView) findViewById(R.id.new_content);
        title_back = (LinearLayout) findViewById(R.id.title_back);
    }

    private void initData() {
        getNewsType();
    }


    /**
     * 获取新闻类型的标题
     */
    private void getNewsType() {

        JSONObject json = new JSONObject();
        UserType user = CCApplication.app.getPresentUser();

        if (user == null) {
            showNoDataView();
            return;
        }
        try {
            json.put("userId", user.getUserId());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        action = ACTION_GET_NEWS_TYPE;
//        queryPost(Constants.GET_EDU_NEWS_TYPE, json);
        new HttpRequestAsyncTask(json, this, this, false).execute(Constants.GET_EDU_NEWS_TYPE);
    }

    @Override
    protected void getMessage(String data) {

    }

    private void parseNewsType(String data) {
        EducationNewsTitleBean titleBean = JsonUtils.fromJson(data, EducationNewsTitleBean.class);
        if (titleBean.getServerResult().getResultCode() == 200) {
            List<EducationNewsTitleBean.EcirclesTypelistBean> ecirclesTypelist = titleBean.getEcirclesTypelist();
            if (ecirclesTypelist == null || ecirclesTypelist.size() <= 0) {
                showNoDataView();
            } else {
                showContentView(ecirclesTypelist);
            }
        } else {
            showNoDataView();
        }

    }


    private void showContentView(final List<EducationNewsTitleBean.EcirclesTypelistBean> ecirclesTypelist) {
        ll_content.setVisibility(View.VISIBLE);
        rl_no_data.setVisibility(View.GONE);
        listFragment = new ArrayList<>();

        //推荐数据需要手动输入
        EducationNewsTitleBean.EcirclesTypelistBean recommendBean = new EducationNewsTitleBean.EcirclesTypelistBean();
        recommendBean.setConfigName("推荐");
        ecirclesTypelist.add(0, recommendBean);

        EducationRecommendFragment fragment01 = new EducationRecommendFragment();
        listFragment.add(fragment01);
        for (int i = 0; i < ecirclesTypelist.size() - 1; i++) {
            EducationNewsFragment fragment = (EducationNewsFragment) EducationNewsFragment
                    .newInstance(ecirclesTypelist.get(i + 1).getId(), ecirclesTypelist.get(i + 1).getConfigName());
            listFragment.add(fragment);
        }
        //设置导航
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            //标题个数
            @Override
            public int getCount() {
                return ecirclesTypelist == null ? 0 : ecirclesTypelist.size();
            }


            //标题
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#333333"));
                colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#007AFF"));
                colorTransitionPagerTitleView.setText(ecirclesTypelist.get(index).getConfigName());
                colorTransitionPagerTitleView.setTextSize(18);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(index);
                    }
                });

                return colorTransitionPagerTitleView;
            }


            //下划线
            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setVisibility(View.VISIBLE);
                indicator.setColors(Color.parseColor("#007AFF"));
                indicator.setLineHeight(4);
                return indicator;
            }
        });


        //显示返回键和搜索按钮
        iv_back.setVisibility(View.VISIBLE);
        iv_search.setVisibility(View.VISIBLE);

        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);

        //绑定ViewPager和Fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentAdapter adapter = new FragmentAdapter(fm, listFragment);
        mViewPager.setAdapter(adapter);

        //设置ViewPager是否可以左右滑动
        mViewPager.setNoScroll(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showNoDataView() {
        ll_content.setVisibility(View.GONE);
        rl_no_data.setVisibility(View.VISIBLE);
        title_back.setVisibility(View.VISIBLE);
        tv_titile.setText("教育资讯");
        iv_back.setVisibility(View.VISIBLE);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rl_no_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewsType();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_search:
                Intent intent = new Intent();
                intent.setClass(this, EducationSearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRequstComplete(String result) {
        parseNewsType(result);
    }

    @Override
    public void onRequstCancelled() {

    }
}
