package com.wcyc.zigui2.newapp.module.consume;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.support.v7.app.AlertDialog;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.fragment.OfflineRechargeRecordFragment;
import com.wcyc.zigui2.newapp.fragment.OnlineRechargeFragment;
import com.wcyc.zigui2.utils.DataUtil;


/**
 * 充值记录 线上充值记录+线下充值记录
 * 之前的只有线下记录
 */
public class NewRechargeRecordActivity1 extends BaseActivity implements View.OnClickListener {


    private LinearLayout title_back;
    private Button bt_online;
    private Button bt_offline;
    private FrameLayout maincontent;
    private OnlineRechargeFragment onlineRechargeFragment;
    private OfflineRechargeRecordFragment offlineRechargeRecordFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_record_activity);

        int status = getIntent().getIntExtra("status", 0);
        showTips(status);

        initView();
    }


    private void initView() {
        title_back = (LinearLayout) findViewById(R.id.title_back);
        title_back.setOnClickListener(this);

        bt_online = (Button) findViewById(R.id.bt_online);
        bt_online.setOnClickListener(this);
        bt_online.setTextColor(Color.WHITE);
        bt_online.setSelected(true);

        bt_offline = (Button) findViewById(R.id.bt_offline);
        bt_offline.setOnClickListener(this);
        maincontent = (FrameLayout) findViewById(R.id.maincontent);
        placeView(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;

            case R.id.bt_online:
                placeView(0);
                bt_online.setSelected(true);
                bt_offline.setSelected(false);
                bt_online.setTextColor(Color.WHITE);
                bt_offline.setTextColor(getResources().getColor((R.color.btn_blue_normal)));
                break;

            case R.id.bt_offline:
                placeView(1);
                bt_offline.setSelected(true);
                bt_online.setSelected(false);
                bt_offline.setTextColor(Color.WHITE);
                bt_online.setTextColor(getResources().getColor((R.color.btn_blue_normal)));
                break;

        }
    }


    private void showTips(int status) {

        if (0 == status) {
            return;
        }

        if (1 == status) {
            DataUtil.getToast("充值成功!");
        } else if (2 == status) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("充值成功");
            builder.setMessage("请持卡去学校补助机/圈存机刷卡领钱。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create();
            builder.show();
        }
    }

    /**
     * 选择Fragment
     *
     * @param index
     */
    public void placeView(int index) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(index + "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (fragment == null) {
            switch (index) {
                case 0:
                    if (onlineRechargeFragment == null) {
                        onlineRechargeFragment = (OnlineRechargeFragment) OnlineRechargeFragment.newInstance(index);
                        fragment = onlineRechargeFragment;
                    } else {
                        fragment = onlineRechargeFragment;
                    }
                    break;

                case 1:
                    if (offlineRechargeRecordFragment == null) {
                        offlineRechargeRecordFragment = (OfflineRechargeRecordFragment) OfflineRechargeRecordFragment.newInstance(index);
                        fragment = offlineRechargeRecordFragment;
                    } else {
                        fragment = offlineRechargeRecordFragment;
                    }
                    break;
                default:
                    break;
            }
        }
        if (ft != null && !fragment.isAdded()) {
            ft.replace(R.id.maincontent, fragment, index + "");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commitAllowingStateLoss();
        }
    }


    @Override
    protected void getMessage(String data) {

    }

    public Fragment getOnlineRecord() {
        return onlineRechargeFragment;
    }

    public Fragment getOfflineRecord() {
        return offlineRechargeRecordFragment;
    }

}
