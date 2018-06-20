package com.wcyc.zigui2.newapp.module.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 章豪 on 2017/6/30.
 */

public abstract class BaseOrderRecyleviewFragment<T> extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    // SwipeRefreshLayout
    private SwipeRefreshLayout mSwipeRefreshLayout;
    // RecyclerView
    private RecyclerView mRecyclerView;
    // RecyclerView的ListView模式
    private LinearLayoutManager mLayoutManager;
    private List<T> datas = new ArrayList<>();
    private TextView tv_no_message;
    public BaseRecycleAdapter<T> baseRecycleAdapter = new BaseRecycleAdapter<T>(datas) {
        @Override
        protected void bindData(BaseViewHolder holder, int position) {
            bindAdapterData(holder, position);
        }

        @Override
        public int getLayoutId() {
            return
                    getAdapterLayoutId();
        }
    };
    private View layoutView;

    public static Fragment newInstance(int index) {
        Fragment fragment = null;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


           // if(layoutView==null){
                layoutView = inflater.inflate(R.layout.fragment_baserecyleview, null);
                tv_no_message = (TextView) layoutView.findViewById(R.id.tv_no_message);
                mSwipeRefreshLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.srl);
                mRecyclerView = (RecyclerView) layoutView.findViewById(R.id.recyclerView);
                mSwipeRefreshLayout = (SwipeRefreshLayout) layoutView.findViewById(R.id.srl);
                // 刷新监听事件，必须有
                // 刷新监听事件，必须有
                mSwipeRefreshLayout.setOnRefreshListener(this);


//        mSwipeRefreshLayout.setScrollBarStyle();

                // 设置进度圈的背景色
//        mSwipeRefreshLayout.setProgressBackgroundColor(R.color.colorAccent);
                // 设置进度动画的颜色,可以使用多种颜色
                mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);

                mLayoutManager = new LinearLayoutManager(getActivity());
                // 初始化Adapter




                // 设置RecyclerView属性为ListView
                mRecyclerView.setLayoutManager(mLayoutManager);
                // 加载适配器
                mRecyclerView.setAdapter(baseRecycleAdapter);
                // 添加滑动监听
                mRecyclerView.addOnScrollListener(mOnScrollListener);
                initDatas();
                //    initview();
                initEvents();

           // }


        return layoutView;
    }



    public abstract void loadmoreData();

    public abstract int getAdapterLayoutId();

    public abstract void onRefreshData();

    public abstract void bindAdapterData(BaseRecycleAdapter.BaseViewHolder holder, int position);

    /**
     * 初始化控件
     */
 //   public abstract void initview();

    /**
     * 初始化数据.
     */
    public abstract void initDatas();

    /**
     * 事件控制.
     */
    public abstract void initEvents();

    /**
     * 点击视图.
     *
     * @param v 视图
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    public  void setDatas(List<T> datas){
        if(datas==null||datas.size()==0){
            tv_no_message.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }else{
            tv_no_message.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        baseRecycleAdapter.setDatas(datas);
        baseRecycleAdapter.notifyDataSetChanged();

    }
    public  List<T> getDatas(){
       return baseRecycleAdapter.datas;

    }

    /**
     * Resume事件.
     */
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    // RecyclerView的滑动监听事件
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        // 滑动状态改变
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            /**
             * scrollState有三种状态，分别是SCROLL_STATE_IDLE、SCROLL_STATE_TOUCH_SCROLL、SCROLL_STATE_FLING
             * SCROLL_STATE_IDLE是当屏幕停止滚动时
             * SCROLL_STATE_TOUCH_SCROLL是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时
             * SCROLL_STATE_FLING是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
             */

            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == baseRecycleAdapter.getItemCount()
                    && baseRecycleAdapter.isShowFooter()) {

                // 加载更多
                loadmoreData();

            }
        }

        // 滑动位置
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 给lastVisibleItem赋值
            // findLastVisibleItemPosition()是返回最后一个item的位置
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };

    // 设置下拉刷新
    @Override
    public void onRefresh() {
        onRefreshData();
        // 关闭加载进度条
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public  boolean isVisBottom(){
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = mRecyclerView.getScrollState();
        if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == mRecyclerView.SCROLL_STATE_IDLE){
            return true;
        }else {
            return false;
        }
    }

    public  boolean isSlideToBottom() {
        if (mRecyclerView == null) return false;
        if (mRecyclerView.computeVerticalScrollExtent() + mRecyclerView.computeVerticalScrollOffset()
                >= mRecyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }


}
