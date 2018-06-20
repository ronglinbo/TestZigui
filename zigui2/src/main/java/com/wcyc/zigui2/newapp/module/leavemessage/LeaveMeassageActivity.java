package com.wcyc.zigui2.newapp.module.leavemessage;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.newapp.widget.NoReloadViewPager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LeaveMeassageActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout title_back;// 返回键布局
    private TextView new_content;// 标题
    private ImageButton title_imgbtn_add;// 发布留言
    private NoReloadViewPager viewPager;
    private RadioGroup radioGroup;
    private List<Fragment> fragments;
    private ViewpageAdapter viewpageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_meassage);
        initView();
        initEvent();
        initData();
    }

    public static int current_status = -1;

    private void initData() {
        fragments = new ArrayList<>();

        ChildMeassageFragment childMeassageFragment = new ChildMeassageFragment();
        MyMeassageFragment myMeassageFragment = new MyMeassageFragment();
        fragments.add(childMeassageFragment);
        fragments.add(myMeassageFragment);
        viewpageAdapter = new ViewpageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(viewpageAdapter);
        int status = getIntent().getIntExtra("status", 0);
        if (status == 1) {
            viewPager.setCurrentItem(1);
            radioGroup.check(R.id.my_message);
        } else {
            viewPager.setCurrentItem(0);
            radioGroup.check(R.id.my_child_message);
        }

    }

    private void initView() {
        title_back = (LinearLayout) findViewById(R.id.title_back);// 返回键布局
        new_content = (TextView) findViewById(R.id.new_content);// 标题
        title_imgbtn_add = (ImageButton) findViewById(R.id.title_imgbtn_add);// 发布留言
        title_imgbtn_add.setVisibility(View.VISIBLE);
        new_content.setText("班牌留言");
        title_back.setVisibility(View.VISIBLE);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        viewPager = (NoReloadViewPager) findViewById(R.id.viewpager);

    }

    private void initEvent() {
        title_back.setOnClickListener(this);
        title_imgbtn_add.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.my_child_message:
                        current_status = 0;
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.my_message:
                        current_status = 1;
                        viewPager.setCurrentItem(1);
                        break;
                }


            }
        });
        viewPager.setOnPageChangeListener(new NoReloadViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        current_status = 0;
                        radioGroup.check(R.id.my_child_message);
                        break;
                    case 1:
                        current_status = 1;
                        radioGroup.check(R.id.my_message);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    protected void getMessage(String data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                LeaveMeassageActivity.this.finish();
                break;
            case R.id.title_imgbtn_add:
                newActivity(PublishLeaveMessageActivity.class, null);
                break;
            default:
                break;

        }
    }

    private class ViewpageAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments;
        FragmentManager fragmentManager;

        public ViewpageAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
            super(fragmentManager);
            this.fragments = fragments;
            this.fragmentManager = fragmentManager;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub

            return super.instantiateItem(container, position);
        }

        private void removeFragment(ViewGroup container, int index) {
            String tag = getFragmentTag(container.getId(), index);
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment == null)
                return;
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.remove(fragment);
            ft.commit();
            ft = null;
            fragmentManager.executePendingTransactions();
        }

        private String getFragmentTag(int viewId, int index) {
            try {
                Class<FragmentPagerAdapter> cls = FragmentPagerAdapter.class;
                Class<?>[] parameterTypes = {int.class, long.class};
                Method method = cls.getDeclaredMethod("makeFragmentName",
                        parameterTypes);
                method.setAccessible(true);
                String tag = (String) method.invoke(this, viewId, index);
                return tag;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


    }


}
