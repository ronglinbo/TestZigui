package com.wcyc.zigui2.newapp.module.othernumber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.NewBaseBean;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 副号页面
 *
 * @author gdzehng
 * @version 2.06
 */
public class NewOtherNumberActivity extends BaseActivity implements OnClickListener {

    private LinearLayout title_back;
    private TextView new_content;
    private ImageButton title_imgbtn_add;
    private ListView other_number_lv;
    private ImageView no_data_iv;
    public static final String INTENT_REFRESH_DATA_Other_Number = "com.wcyc.zigui.action.INTENT_REFRESH_DATA_Other_Number";//刷新的广播
    private int viceNumber=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_other_number);
        initView();
        initDatas();
        initEvents();

        IntentFilter mrefeshDataFilter = new IntentFilter(INTENT_REFRESH_DATA_Other_Number);
        registerReceiver(refeshDataReceiver, mrefeshDataFilter);

    }

    /**
     * 实例化标题按钮组件
     */
    private void initView() {
        title_back = (LinearLayout) findViewById(R.id.title_back);
        new_content = (TextView) findViewById(R.id.new_content);
        title_imgbtn_add = (ImageButton) findViewById(R.id.title_imgbtn_add);
        no_data_iv = (ImageView) findViewById(R.id.no_data_iv);

        other_number_lv = (ListView) findViewById(R.id.other_number_lv);

    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        title_back.setVisibility(View.VISIBLE);
        new_content.setText("副号");
        title_imgbtn_add.setVisibility(View.VISIBLE);

        String childId = CCApplication.getInstance().getPresentUser().getChildId();
        try {
            JSONObject json = new JSONObject();
            json.put("childId", childId);
            if (!DataUtil.isNetworkAvailable(NewOtherNumberActivity.this)) {
                DataUtil.getToast(getResources().getString(R.string.no_network));
                return;
            }
            if (!isLoading()) {
                System.out.println("副号入参json=====" + json);
                queryPost(Constants.PARENT_MOBILE_LIST, json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置点击效果监听器
     */
    private void initEvents() {
        title_back.setOnClickListener(this);
        title_imgbtn_add.setOnClickListener(this);


    }

    private BroadcastReceiver refeshDataReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            initDatas();
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(refeshDataReceiver);
    }


    /**
     * 得到信息
     *
     * @param data 数据
     */
    @Override
    protected void getMessage(String data) {
        System.out.println("副号出参data=====" + data);
        NewBaseBean ret = JsonUtils.fromJson(data, NewBaseBean.class);
        if (ret.getServerResult().getResultCode() != 200) {// 请求失败
            DataUtil.getToast(ret.getServerResult().getResultMessage());
        } else {
            try {
                JSONObject json = new JSONObject(data);
                viceNumber = json.getInt("viceNumber");
                System.out.println("副号个数出参viceNumber=====" + viceNumber);
                if (viceNumber < 1) {//无数据
                    other_number_lv.setVisibility(View.GONE);
                    no_data_iv.setVisibility(View.VISIBLE);
                } else {//有数据
                    List<NewOtherUserBean> newOtherNumberList = new ArrayList<NewOtherUserBean>();
                    other_number_lv.setVisibility(View.VISIBLE);
                    no_data_iv.setVisibility(View.GONE);

                    String parentMoblieList = json.getString("parentMoblieList");
                    JSONArray json2 = new JSONArray(parentMoblieList);
                    for (int i = 0; i < json2.length(); i++) {
                        NewOtherUserBean newOtherUserBean = JsonUtils.fromJson(json2
                                .get(i).toString(), NewOtherUserBean.class);
                        if("0".equals(newOtherUserBean.getIsmain()+"")){
                            newOtherNumberList.add(newOtherUserBean);
                        }
                    }
                    //设置adapter
                    NewOtherNumberAdapter newOtherNumberAdapter = new NewOtherNumberAdapter(this, newOtherNumberList,schoolId,this);
                    other_number_lv.setAdapter(newOtherNumberAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 点击效果. 当点击视图中的标题按钮，则表明
     *
     * @param v 点击的视图
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                NewOtherNumberActivity.this.finish();
                break;
            case R.id.title_imgbtn_add:
                if(viceNumber<10){
                    newActivity(NewAddOtherNumberActivity.class,null);
                }else {
                    DataUtil.getToast("副号已达上限，无法继续添加");
                }
                break;

            default:
                break;
        }
    }

}
