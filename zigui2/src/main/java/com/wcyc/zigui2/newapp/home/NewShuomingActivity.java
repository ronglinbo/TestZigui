package com.wcyc.zigui2.newapp.home;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
public class NewShuomingActivity extends BaseActivity {


     private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_myset_version_shuoming);
        System.out.println("=====getDeviceID()===="+getDeviceID());
        initView();
        initData();
        initEvents();
    }


    private void  initView(){
        listview= (ListView) findViewById(R.id.listview);
    }

    private void initEvents() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }


    private void initData() {
    }

    @Override
    protected void getMessage(String data) {

    }


}
