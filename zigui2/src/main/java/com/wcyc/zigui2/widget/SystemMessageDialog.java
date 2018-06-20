package com.wcyc.zigui2.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wcyc.zigui2.R;


/**
 * 系统消息弹窗
 */
public class SystemMessageDialog extends Dialog  {

    /**
     * 布局文件
     */
    int layoutRes;
    /**
     * 上下文对象
     **/
    Context context;
    /**
     * 确定按钮
     **/
    private Button confirmBtn;
    /**
     * 取消按钮
     **/
    private Button cancelBtn;
    /**
     * 离线消息按钮
     **/
    private RadioButton myRadioButton;
    private TextView contentTv, titleTv, tv_time;
    /**
     * 点击次数
     **/
    private int check_count = 0;
    /**
     * Toast时间
     **/
    public static final int TOAST_TIME = 1000;
    private Handler handler;
    /**
     * 确定按钮被点击
     **/
    public static final int DIALOG_SURE = 1;
    /**
     * 取消按钮被点击
     **/
    public static final int DIALOG_CANCEL = 2;
    /**
     * 单选按钮被选中
     **/
    public static final int RADIOBUTTON_CHECKED = 3;
    /**
     * 单选按钮未被选中
     **/
    public static final int RADIOBUTTON_NO_CHECKED = 4;


    public SystemMessageDialog(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 自定义布局的构造方法
     *
     * @param context
     * @param resLayout
     */
    public SystemMessageDialog(Context context, int resLayout) {
        super(context);
        this.context = context;
        this.layoutRes = resLayout;
    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param theme
     * @param resLayout
     */
    public SystemMessageDialog(Context context, int theme, int resLayout) {
        super(context, theme);
        this.context = context;
        this.layoutRes = resLayout;
    }


    public void setTime(String title) {
        if (tv_time != null) {
            tv_time.setText(title);
        }
    }

    public String getTime() {
        String time = tv_time.getText().toString().trim();
        return time;
    }

    public void setTitle(String title) {
        if (titleTv != null) {
            titleTv.setText(title);
        }
    }

    public String getTitle() {
        String titleStr = titleTv.getText().toString().trim();
        return titleStr;
    }

    public void setContent(String content) {
        if (contentTv != null)
            contentTv.setText(content);
    }

    public String getContent() {
        String content = contentTv.getText().toString().trim();
        return content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 指定布局
        this.setContentView(layoutRes);

        // 根据id在布局中找到控件对象etMeasuredDimension
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        myRadioButton = (RadioButton) findViewById(R.id.my_rbtn);
        contentTv = (TextView) findViewById(R.id.tv_content);
        titleTv = (TextView) findViewById(R.id.title_tv);
        tv_time = (TextView) findViewById(R.id.tv_time);
    }

}