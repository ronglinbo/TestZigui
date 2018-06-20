package com.wcyc.zigui2.core;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcyc.zigui2.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 章豪 on 2017/6/30.
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 设置底部布局
    private static final int TYPE_FOOTER = 0;
    // 设置默认布局
    private static final int TYPE_DEFAULT = 1;
    // 上下文
    private Context mContext;
    // 判断是不是最后一个item，默认是true
    private boolean mShowFooter = true;

    // 构造函数
    public BaseRecycleAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public   List<T> datas;
    // 构造函数
    public BaseRecycleAdapter() {

    }
   public  View view;
    public BaseRecycleAdapter(List<T> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DEFAULT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);

            BaseRecycleAdapter.BaseViewHolder vh = new BaseRecycleAdapter.BaseViewHolder(v);

            return vh;
        } else {
            // 实例化布局
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, null);
            // 代码实现加载布局
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

               view.setVisibility(View.INVISIBLE);



            return new FooterViewHolder(view);
        }




    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
               if(holder instanceof BaseViewHolder){
                   bindData((BaseViewHolder<T>) holder,position);
               }


    }

    // 设置不同的item
    @Override
    public int getItemViewType(int position) {



        // 判断当前位置+1是不是等于数据总数（因为数组从0开始计数），是的就加载底部布局刷新，不是就加载默认布局
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_DEFAULT;
        }
    }
    // 设置是否显示底部加载提示（将值传递给全局变量）
    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    // 判断是否显示底部，数据来自全局变量
    public boolean isShowFooter() {
        return this.mShowFooter;
    }


    // 底部布局的ViewHolder
    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View view) {
            super(view);
        }
    }




    /**
     * 刷新数据
     * @param datas
     */
    public void refresh(List<T> datas){
       // this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }


    /**
     * 添加数据
     * @param datas
     */
    public void addData(List<T> datas){
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * @param datas
     */
    public void setDatas(List<T> datas){
        this.datas=datas;
        notifyDataSetChanged();
    }

    /**
     *  绑定数据
     * @param holder  具体的viewHolder
     * @param position  对应的索引
     */
    protected abstract void bindData(BaseRecycleAdapter.BaseViewHolder holder, int position);


    // 获取数据数目
    @Override
    public int getItemCount() {
        // 判断是不是显示底部，是就返回1，不是返回0
        int begin = mShowFooter ? 1 : 0;
        // 没有数据的时候，直接返回begin
        if (datas == null) {
            return begin;
        }
        // 因为底部布局要占一个位置，所以总数目要+1
        return datas.size() + begin;
    }



    /**
     * 封装ViewHolder ,子类可以直接使用
     */
    public class BaseViewHolder<T> extends  RecyclerView.ViewHolder{


        private Map<Integer, View> mViewMap;

        public BaseViewHolder(View itemView) {
            super(itemView);
            mViewMap = new HashMap<>();
        }

        /**
         * 获取设置的view
         * @param id
         * @return
         */
        public View getView(int id) {
            View view = mViewMap.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                mViewMap.put(id, view);
            }
            return view;
        }
    }

    /**
     * 获取子item
     * @return
     */
    public abstract int getLayoutId();



    /**
     * 设置文本属性
     * @param view
     * @param text
     */
    public void setItemText(View view,String text){
        if(view instanceof TextView){
            ((TextView) view).setText(text);
        }
    }
}
