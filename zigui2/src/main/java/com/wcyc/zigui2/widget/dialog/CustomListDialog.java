package com.wcyc.zigui2.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.wcyc.zigui2.R;
import com.wcyc.zigui2.utils.CommonUtil;
import com.wcyc.zigui2.widget.listview.BaseViewHolderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronglinbo on 2018/6/1.
 */

public class CustomListDialog extends Dialog {


    private Context context;
    private int height;
    private int width;
    private boolean cancelTouchout;
    private View view;


    private CustomListDialog(Builder builder) {
        super(builder.context);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }


    private CustomListDialog(Builder builder, int resStyle) {
        super(builder.context, resStyle);
        context = builder.context;
        height = builder.height;
        width = builder.width;
        cancelTouchout = builder.cancelTouchout;
        view = builder.view;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(cancelTouchout);

        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = height;
        lp.width = width;
        window.setAttributes(lp);

    }


    public static Builder Builder(Context context) {
        return new Builder(context);
    }


    public static final class Builder {

        private Context context;
        private int height;
        private int width;
        private boolean cancelTouchout;
        private View view;
        private int resStyle = -1;
        private List<String> data = new ArrayList<String>();
        private int maxShowNumber = 5;
        private onClickItemListener itemListener;

        public CustomListDialog.Builder getDefault() {
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
            width = CommonUtil.dip2px(context.getApplicationContext(), 320);
            cancelTouchout = false;
            resStyle = R.style.customDialogStyle;
            return this;
        }


        public Builder(Context context) {
            this.context = context;
        }

        public CustomListDialog.Builder view(int resView) {
            view = LayoutInflater.from(context).inflate(resView, null);
            return this;
        }

        public CustomListDialog.Builder heightpx(int val) {
            height = val;
            return this;
        }

        public CustomListDialog.Builder widthpx(int val) {
            width = val;
            return this;
        }

        public CustomListDialog.Builder heightdp(int val) {
            height = CommonUtil.dip2px(context, val);
            return this;
        }

        public CustomListDialog.Builder widthdp(int val) {
            width = CommonUtil.dip2px(context, val);
            return this;
        }

        public CustomListDialog.Builder heightDimenRes(int dimenRes) {
            height = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public CustomListDialog.Builder widthDimenRes(int dimenRes) {
            width = context.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public CustomListDialog.Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public CustomListDialog.Builder cancelTouchout(boolean val) {
            cancelTouchout = val;
            return this;
        }

        public CustomListDialog.Builder addViewOnclick(int viewRes, View.OnClickListener listener) {
            view.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }

        public CustomListDialog.Builder setViewText(int viewRes, String text) {
            TextView textView = (TextView) view.findViewById(viewRes);
            textView.setText(text);
            return this;
        }

        public CustomListDialog.Builder setViewText(int viewRes, int resId) {
            TextView textView = (TextView) view.findViewById(viewRes);
            textView.setText(resId);
            return this;
        }


        public CustomListDialog.Builder setListViewData(List<String> data) {
            this.data = data;
            setAdapter();
            return this;
        }

        public CustomListDialog.Builder setItemClick(onClickItemListener itemListener) {
            this.itemListener = itemListener;
            return this;
        }

        public CustomListDialog.Builder setAdapter() {
            if (data == null || data.size() <= 0) {
                if (data == null) {
                    data = new ArrayList<String>();
                }
                data.add("没有数据");
            }
            if (data.size() > maxShowNumber) {
                height = CommonUtil.dip2px(context, 350);
            }
            if (data.size() > 0 && data.size() <= maxShowNumber) {
                height = CommonUtil.dip2px(context, 50 * data.size() + 100);
            }

            CustomListDialogAdapter dialogAdapter = new CustomListDialogAdapter(context, data, R.layout.dialog_custom_list_itme);
            ListView listView = (ListView) view.findViewById(R.id.listView);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (itemListener != null) {
                        itemListener.onClickItem(position, data.get(position));
                    }
                }
            });
            listView.setAdapter(dialogAdapter);
            return this;
        }

        public CustomListDialog build() {
            if (resStyle != -1) {
                return new CustomListDialog(this, resStyle);
            } else {
                return new CustomListDialog(this);
            }
        }
    }


    static class CustomListDialogAdapter extends BaseViewHolderAdapter<String> {

        private Context aContext;
        private List<String> data;

        public CustomListDialogAdapter(Context context, List<String> data, int layoutRes) {
            super(context, data, layoutRes);
            aContext = context;
            this.data = data;
        }

        @Override
        protected void bindData(int pos, String itemData) {
            TextView tvName = getViewFromHolder(R.id.tvName);
            tvName.setText(itemData);
        }
    }


    public interface onClickItemListener {
        void onClickItem(int position, String itemName);
    }


}
